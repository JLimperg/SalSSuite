package salssuite.server.module;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import salssuite.util.FileAccessException;
import salssuite.util.gui.PasswordPanel;
import salssuite.server.Server;
import salssuite.server.module.gui.DutyModulePanel;
import salssuite.util.Util;

/**
 * Manages the attendance controlling ('duty') of the project.
 * <p>
 * In a 'Schule als Staat' project it is absolutely vital to ensure that the
 * students come to school and stay there for at least some amount of time. This
 * so called 'attendance time' can be set in the project settings, see
 * {@link salssuite.server.Project}.
 * <p>
 * To achieve this, the duty module provides a logging ability for citizen of
 * the state. Citizens may come to school and leave it through certain checkpoints,
 * and there 'duty officers' can log their coming and going using
 * {@link salssuite.clients.duty.DutyClient}s. The server administrators can
 * then check whether someone has not passed enough time in the state or has not
 * appeared there at all by using the files this module produces as output. See
 * {@link #makeOutput}.
 * <p>
 * The module logs each 'logging event' in a database, where the columns
 * contain date and time, the affected citizen's ID and the type of event
 * (login or logout). See {@link #buildDatabase}.
 * @author Jannis Limperg
 */
public class DutyModule implements Module {

//=================================CONSTANTS==================================//

    /**
     * An identifier unique among the modules of the standard SalSSuite edition.
     * This can be used for example as a module name in the 'permissions'
     * database table.
     */
    public static final String NAME = "Zoll";

//==================================FIELDS====================================//

    //the server this module belongs to
    Server server;

    //a connection to and a statement for the general database
    Connection dbcon;
    Statement generalStmt;
    
//===============================CONSTRUCTORS=================================//

    /**
     * Sole constructor.
     * @param server The server this module belongs to.
     * @throws SQLException if the connection to the database fails.
     */
    public DutyModule(Server server) throws SQLException {
        this.server = server;

        //setup derby server

        try {
            dbcon = server.getDatabaseConnection();
            generalStmt = server.getDatabaseConnection().createStatement();
        }
        catch(Exception e) {
            if(!server.isNoGUI())
                JOptionPane.showMessageDialog(server.getGUI(), "Konnte Datenbankserver" +
                        "für den Zollbereich nicht starten. Beende die Anwendung.",
                        "Kritischer Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            else
               System.out.println("Konnte Datenbankserver für den Zollbereich nicht starten."
                       + "Beende die Anwendung.");

            server.writeToErrorLog("DUTY: Starting the database server failed.");
            e.printStackTrace();
            System.exit(1);
            return;
        }

    }

//==================================METHODS===================================//

    //DATA MANAGEMENT
    /**
     * Creates the necessary database tables. Its structure is as follows:
     * <p>
     * <code>
     * CREATE TABLE "DBUSER"."LOGS" (<br/>
     * "CITIZENID" INTEGER NOT NULL,<br/>
     * "DATE" VARCHAR(10) NOT NULL,<br/>
     * "TIME" VARCHAR(8) NOT NULL,<br/>
     * "TYPE" INTEGER NOT NULL);
     * </code>
     *
     * <p>
     *
     * Note that date and time are stored as strings, not as <code>DATE</code>
     * and <code>TIME</code> values respectively. These types caused some
     * strange problems so I decided to go with this not so elegant solution.
     * <p>
     * To create and parse standardised time strings, use
     * {@link salssuite.util.Util#getDateString}/{@link salssuite.util.Util#getTimeString} and
     * {@link salssuite.util.Util#parseDateString}/{@link salssuite.util.Util#parseTimeString}
     * respectively.
     */
    public void buildDatabase(){

        try {
            generalStmt.executeUpdate("CREATE TABLE logs (" +
                    "citizenId INT NOT NULL," +
                    "date VARCHAR(10) NOT NULL," +
                    "time VARCHAR(8) NOT NULL," +
                    "type INT NOT NULL," + //0 represents logout, 1 login
                    "PRIMARY KEY (citizenId, date, time)" +
                    ")");
        }
        catch(SQLException e) {
            if(!server.isNoGUI())
                JOptionPane.showMessageDialog(null, "Konnte Tabellen für Zollmodul" +
                        " nicht erstellen.", "Kritischer Netzwerkfehler",
                        JOptionPane.ERROR_MESSAGE);
            else
                System.out.println("Konnte Tabellen für Zollmodul nicht erstellen."
                        + " Beende die Anwendung.");

            e.printStackTrace();
            System.exit(1);
            return;
        }

    }//end buildServerDatabase()


    //OUTPUT
    /**
     * Generates output. The output is generated in
     * {@link salssuite.util.Constants#dutyOutputPath}. Two files, both in csv format, are
     * produced.
     * <p>
     * The first file contains the IDs of those citizens who have not passed
     * at least the attendance time in the state, and the time they actually
     * did pass in it.
     * <p>
     * The second file contains the IDs of those citizens who are not logged out
     * when this method is called. This is especially interesting in the evening
     * when everyone should have already left the school, because it indicates
     * that someone slipped through the logging mechanism in some way.
     * <p>
     * Note that output is produced for the current day <i>only</i>. It is
     * therefore recommended that the output generated at the end of each day
     * of the project is kept very well.
     */
    public void makeOutput() {

        server.writeToLog("DUTY: Generating output.");

        //set up file structure
        server.getConstants().dutyOutputPath.mkdirs();

        //generate output: lazy citizens
        try {
            exportLazyCitizensToCsv(server.getConstants().dutyLazyCitizensFile,
                    server.getProject().getAttendanceTime());
        }
        catch(FileAccessException e) {
            server.writeToErrorLog("DUTY: Error while generating lazy citizens " +
                    "output: "+e.getMessage());
            return;
        }

        //generate output: not logged out
        try {
            exportNotLoggedOutToCsv(server.getConstants().dutyNotLoggedOutFile);
        }
        catch(FileAccessException e) {
            server.writeToErrorLog("DUTY: Error while generating output for " +
                    "citizens who were not logged out: "+e.getMessage());
            return;
        }

        server.writeToLog("DUTY: Done generating output.");
    }


    //COMMUNICATION WITH SERVER
    /**
     * Returns this module's name.
     * @return The name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Does nothing, as there are no tasks to be performed on server shutdown.
     */
    public void serverShutdown() {

    }


    //GUI

    /**
     * Returns this module's GUI.
     * @return The GUI.
     * @see salssuite.server.module.gui.DutyModulePanel
     */
    public JPanel getPanel() {
        server.getConstants().recomputePaths();
        return new PasswordPanel(server.getDatabaseConnection(), NAME,
                new DutyModulePanel(server.getDatabaseConnection()), "Zoll");
    }

    //==============================PRIVATE===================================//

    /**
     * Exports those citizens who did not pass the required attendance time
     * in the state to a csv file.
     * @param exportFile The file to output them to.
     * @param attendanceTime The time each citizen has to pass in the state.
     * @throws FileAccessException if an error occurs while connecting to
     * the file and printing the data.
     */
    private void exportLazyCitizensToCsv(File exportFile, int
            attendanceTime) throws FileAccessException{

        //connect to the file
        PrintWriter out;

        //set up top directories
        exportFile.getParentFile().mkdirs();

        try {
            exportFile.createNewFile();
            out = new PrintWriter(new java.io.FileWriter(exportFile));
        }
        catch (IOException e) {
            throw new FileAccessException("Fehler: Konnte Datei nicht zum Schreiben" +
                    " öffnen.", exportFile);
        }
        //print the header
        out.println("\"ID\",\"Im Staat verbrachte Zeit\",\"Differenz zur " +
                "minimalen Anwesenheitsdauer\"");

        //get all lazy citizens and print the data
        try {
            ResultSet rs = generalStmt.executeQuery("SELECT id FROM citizens");
            while(rs.next()) {

                int timePassed = getTimePassedInState(rs.getInt("id"),
                        new GregorianCalendar());

                if(timePassed < 0) //means an error has occured.
                    return;

                if(timePassed >= attendanceTime)
                    continue;

                out.print(rs.getInt("id")+",");
                out.print(timePassed + ",");
                out.println(attendanceTime - timePassed);
            }
        }
        catch(SQLException e) {
            if(!server.isNoGUI())
                JOptionPane.showMessageDialog(server.getGUI(), "Fehler bei der Kommunikation "
                        + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            else
                System.out.println("Fehler bei der Kommunikation mit der Datenbank.");

            e.printStackTrace();
            return;
        }

        out.flush();
        out.close();
    }

    /**
     * Exports those citizens who are not logged out when calling this method.
     * @param exportFile The file to output them to.
     * @throws FileAccessException if an error occurs while connecting to
     * the file and printing the data.
     */
    private void exportNotLoggedOutToCsv(File exportFile)
        throws FileAccessException{

        //connect to file and print header
        PrintWriter out;

        exportFile.getParentFile().mkdirs();

        try {
            exportFile.createNewFile();
            out = new PrintWriter(new java.io.FileWriter(exportFile));
        }
        catch (IOException e) {
            throw new FileAccessException("Fehler: Konnte Datei nicht zum Schreiben" +
                    " öffnen.", exportFile);
        }
        out.println("\"ID\"");

        //get citizen who are currently not logged out and print the data
        try {
            ResultSet citizens = generalStmt.executeQuery("SELECT id FROM " +
                    "citizens");

            while(citizens.next()) {
                Statement dutyStmt2 = dbcon.createStatement(
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet logs = dutyStmt2.executeQuery("SELECT time, type FROM" +
                        " logs WHERE citizenId = "+citizens.getInt("id"));
                logs.last();

                if(logs.getRow() == 0) //means citizen has never logged in
                    continue;

                if(logs.getInt("type") == 0) //means citizen is properly logged out
                    continue;

                out.println(citizens.getInt("id"));
            }
        }
        catch(SQLException e) {
            if(!server.isNoGUI())
                JOptionPane.showMessageDialog(server.getGUI(), "Fehler bei der Kommunikation "
                        + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            else
                System.out.println("Fehler bei der Kommunikation mit der Datenbank.");

            e.printStackTrace();
            return;
        }

        out.flush();
        out.close();
    }

    /**
     * Returns the time a citizen has passed in the state on the specified
     * day. If the citizen is not logged out yet, the time is computed using the
     * current time.
     * @param citizenID The citizen's ID.
     * @param thisDay The date for which the time passed in the state should be
     * obtained.
     * @return The time passed in the state, or -1 in case of a database error.
     * If this occurs, a warning message is displayed to the user.
     */
    private int getTimePassedInState(int citizenID, GregorianCalendar thisDay) {

        try {
            Statement stmt = dbcon.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet citi = stmt.executeQuery("SELECT time, type FROM" +
                    " logs WHERE citizenId = "+citizenID+" AND date = '"+
                    Util.getDateString(thisDay)+"'");

            int timePassed = 0;

            while(citi.next()) {
                String[] logTime = Util.parseTimeString(citi.getString("time"));

                if(citi.isLast() && citi.getInt("type") == 1) { //citizen still logged in
                    GregorianCalendar currentTime = new GregorianCalendar();
                    timePassed += (currentTime.get(GregorianCalendar.HOUR_OF_DAY) -
                            Integer.parseInt(logTime[0]))*60;
                    timePassed += (currentTime.get(GregorianCalendar.MINUTE) -
                            Integer.parseInt(logTime[1]));
                    return timePassed;
                }

                if(citi.getInt("type") == 0)   //log-out-event: do nothing
                    continue;

                citi.next(); //now points to next log-out-event

                String[] logOutTime = Util.parseTimeString(citi.getString("time"));

                timePassed += (Integer.parseInt(logOutTime[0]) -
                        Integer.parseInt(logTime[0]))*60;
                timePassed += (Integer.parseInt(logOutTime[1]) -
                        Integer.parseInt(logTime[1]));
            }

            return timePassed;
        }
        catch(SQLException e) {
            if(!server.isNoGUI())
                JOptionPane.showMessageDialog(server.getGUI(), "Fehler bei der Kommunikation "
                        + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            else
                System.out.println("Fehler bei der Kommunikation mit der Datenbank.");

            e.printStackTrace();
            return -1;
        }
    }

    

//===============================SUBCLASSES===================================//

}//end class
