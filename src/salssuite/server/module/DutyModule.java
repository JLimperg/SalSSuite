package salssuite.server.module;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import salssuite.util.gui.PasswordPanel;
import salssuite.server.Server;
import salssuite.server.module.gui.DutyModulePanel;

/**
 * Manages the attendance time controlling ('duty') of the project.
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
 * appeared there at all by using the files the <code>DutyModulePanel</code>
 * can export. See
 * {@link salssuite.server.module.gui.DutyModulePanel#exportNotLoggedOutToCsv} and
 * {@link salssuite.server.module.gui.DutyModulePanel#exportLazyCitizensToCsv}.
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

    /**
     * Does nothing. Output can be generated using the <code>DutyModulePanel</code>.
     */
    public void makeOutput() {
    }

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
    

//===============================SUBCLASSES===================================//

}//end class
