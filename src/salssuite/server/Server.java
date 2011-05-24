package salssuite.server;

import salssuite.server.module.DutyModule;
import salssuite.server.module.CitizenModule;
import salssuite.server.module.MagazineModule;
import salssuite.server.module.CompanyModule;
import salssuite.server.module.Module;
import salssuite.server.module.AccountingModule;
import salssuite.server.gui.ProjectSetupDialog;
import salssuite.server.gui.PreProjectDialog;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.derby.drda.NetworkServerControl;
import salssuite.util.Constants;
import salssuite.util.gui.ExceptionDisplayDialog;
import salssuite.util.FileAccessException;
import salssuite.util.gui.PasswordPanel;
import salssuite.server.gui.ServerGUI;
import salssuite.server.gui.ServerPanel;
import salssuite.server.module.EmploymentModule;
import salssuite.util.Passwords;
import salssuite.util.Util;
import salssuite.util.gui.ProgressDialog;
import java.util.Scanner;
import salssuite.server.module.AdminModule;

/**
 * The SalSSuite server.
 *
 * <h2>Setting Up A Server</h2>
 * To set up a server, simply create a server object. If you
 * are lucky and all the input is done correctly, you will have a running server
 * afterwards.
 *
 * <h2>The Server Framework</h2>
 *
 * The server is mainly a framework for modules which can be created separately.
 * These modules must be inserted at some points in the code of this class and
 * in that of {@link salssuite.clients.admin.AdminClient}, each
 * marked with a 'FIXME' comment. The rest is done automatically by the server.
 * Server modules must implement all methods of the
 * {@link salssuite.server.module.Module} interface.
 *
 * <h3>Databases</h3>
 *
 * The server can in many ways be considered a frontend to a
 * <a href=http://db.apache.org/derby/> Derby</a> database
 * server. Almost all data of the server and all modules is stored in
 * one database hosted by a Derby database server.
 * Clients can connect to it directly after having authenticated
 * themselves (see below) and can then access the database in any way
 * they like.
 * <p>
 * The server sets up some database tables storing basic data which is probably
 * required by most modules. See the documentation of
 * {@link #buildServerDatabase} for details. Modules can specify tables
 * which should be added to this database in their
 * {@link salssuite.server.module.Module#buildDatabase buildDatabase()} methods.
 *
 * <h3>Server Setup</h3>
 *
 * At server start, the server asks the user whether a project should be loaded
 * or a new one is to be created using a
 * {@link salssuite.server.gui.PreProjectDialog}. Afterwards, all
 * necessary files and folders are created (if not already present),
 * logging is enabled and the Derby database server is started.
 * <br/>
 * After that all modules are loaded and their databases, as well
 * as the server database, are built using 
 * {@link salssuite.server.module.Module#buildDatabase}
 * and {@link #buildServerDatabase} respectively. This is done only
 * if the project is opened for the first time. Finally, all module panels,
 * obtained using {@link salssuite.server.module.Module#getPanel}, are added to the newly
 * created {@link salssuite.server.gui.ServerGUI} and the latter is displayed.
 *
 * <h4>Projects</h4>
 *
 * A {@link Project} object represents one 'Schule als Staat' project. It
 * is the basis for the server's work and contains all properties which are
 * relevant for setting up a server. For detailed explanations, see the documentation
 * of the <code>Project</code> class.
 * <p>
 * At server startup, the user can choose whether to a) start a new project,
 * b) continue the last one, or c) continue some existing one. Based on this choice,
 * the server decides for example if the database tables must be created (which
 * is the case when a project is started for the first time) or not.
 *
 * <h3>Logging</h3>
 *
 * The server and all modules can log messages and error messages in
 * {@link salssuite.util.Constants#serverLogFile} and
 * {@link salssuite.util.Constants#serverErrorLogFile}
 * respectively. {@link #writeToLog} and
 * {@link #writeToErrorLog} are used for this purpose.
 *
 * <h3>User Accounts and Authentication</h3>
 *
 * The SalSSuite uses a rather simple system for user accounts. Each user account
 * is identified by a username and has a password, and each account can be granted the
 * right to access certain modules.
 *
 * <p>
 *
 * The server uses a double authentication mechanism to provide extra safety.
 * <br/>
 * <b>Firstly</b>, the Derby network server is started to host the database.
 * The username given here is 'dbuser' and the password is randomly generated
 * at each server startup.
 * <br/>
 * <b>Secondly</b>, clients can connect to the server and ask it to tell them
 * the password for the Derby server. The server checks if the account username
 * and password submitted by the client are valid and if the specified user
 * has the permissions to access the module they want to access. If this is the
 * case, the password for the Derby server is submitted and the client can
 * connect to it.
 *
 * <p>
 *
 * The account data is stored in the 'accounts' table of the database. The
 * module permissions are stored in the 'permissions' table. See
 * {@link #buildServerDatabase}.
 *
 * <h3>Output</h3>
 *
 * At server shutdown or when the user gives a command to do so using
 * the server's GUI, output for all modules is produced. Output can be just
 * about anything a module thinks is worth knowing.
 *
 * <h3>Backups</h3>
 *
 * Backups are made at regular intervals by the {@link ServerLauncher}
 * which calls {@link #makeBackup}.<br/>
 * This method simply copies the whole project data to a backup folder under the
 * programme directory. It is recommended to in turn move or copy this folder
 * to a remote device to increase safety.
 *
 * <h3>Shutdown</h3>
 *
 * At shutdown the server terminates the connection to all clients that may
 * be connected and shuts down the Derby database server. Before doing so,
 * the modules can perform anything they consider necessary to do before shutdown
 * as the server calls the {@link salssuite.server.module.Module#serverShutdown}
 * method of every module.
 * <p>
 * When the JVM is shut down directly by the operating system (f.ex. by
 * sending a SIGTERM signal), the server performs all regular shutdown operations,
 * so it also calls every module's <code>serverShutdown</code> method.
 * As the user will expect this to be done in an
 * instant and some operating system may even give the JVM only a certain amount
 * of time before forcing it to halt, there should be no time consuming things
 * in the modules' <code>serverShutdown</code> methods. For the same reason,
 * no insecure operations (f.ex. involving multiple threads) should be done
 * here.
 *
 * <h3>Command line version</h3>
 *
 * The server features a simple command line interface. Using it, the user
 * can perform all actions that they cannot perform with the clients (including
 * the {@link salssuite.clients.admin.AdminClient}). To use the command line
 * interface instead of the usual GUI, just run the {@link ServerLauncher} with
 * parameter <code>--no-gui</code>.
 * @author Jannis Limperg
 */
public class Server {

//=================================CONSTANTS==================================//

//==================================FIELDS====================================//

    File programmeDir = null;

    NetworkServerControl dbserver;
    Connection dbcon;
    Statement stmt;

    //If true, does not load the GUI but rather an interactive command line
    //interface in the setup() method.
    boolean noGUI;

    //If true, all stdout is suppressed and the server does not expect any
    //user input even in command line mode.
    boolean daemonize;

    //wrapper for and System.in, for convenience
    Scanner in = new Scanner(System.in);

    //random database username and password for the authentication mechanism
    String databaseUsername;
    String databasePasswordHash;

    //the list of active modules
    ArrayList<Module> modules = new ArrayList<Module>(4);

    //active Modules
    //FIXME Add your additional modules here.
    DutyModule dutyModule;
    CompanyModule companyModule;
    MagazineModule magazineModule;
    CitizenModule citizenModule;
    AccountingModule accountingModule;
    EmploymentModule employmentModule;
    AdminModule adminModule;

    //log output
    PrintWriter logOut;
    PrintWriter errorLogOut;

    //first call of project?
    boolean firstCallOfProject;

    //current project
    Project proj;
    File projectFile;

    //constants for current project and programme directory
    Constants constants;

    //this server's GUI
    ServerGUI gui;

    /**
     * This field is true
     * - while the server is not yet loaded
     * - when the server is already performing its {@link #shutdown} method.
     * Otherwise it is false.
     *
     * This field is used by the shutdown hook added to the runtime in setup()
     * to determine if it needs to call the shutdown() method due to an
     * unexpected interrupt of the JVM.
     */
    boolean serverShuttingDown = true;

//===============================CONSTRUCTORS=================================//

    /**
     * Sole constructor. See the {@link salssuite.server.Server general class documentation}
     * on what is done at setup.
     * @param noGUI If set to <code>true</code>, the server will not attempt
     * to use any graphical user interface but communicate everything using
     * <code>System.in</code> and <code>System.out</code>. Note that not the
     * full functionality of the server's usual GUI is present in command line
     * mode, but what is not available directly can be accessed using the
     * {@link salssuite.clients.admin.AdminClient}.
     * @param daemonize If this is set to <code>true</code>, the server
     * will completely disable stdout, stderr and stdin. Consequently, you
     * will rely on the logs if something goes wrong.
     * @param projectFile A project definition file as produced by
     * {Project#saveData}. If this is not <code>null</code>, the server will
     * try to load a project from the file without asking the user. This can
     * also be used to override the behaviour when the <code>daemonize</code>
     * option is set. If the project cannot be loaded, the server exits.
     * @throws Exception This constructor throws many exceptions if something
     * severely went wrong during server setup. These should definitely be caught and
     * handled.
     */
    public Server(boolean noGUI, boolean daemonize, File projectFile) throws Exception {
        this.noGUI = noGUI;
        this.daemonize = daemonize;
        this.projectFile = projectFile;
        setup();
    }//end default constructor

//==================================METHODS===================================//

    //SETUP
    /* This method is called by all constructors. It sets up the server by
     * a) Preparing the log files;
     * b) Importing the project data and setting up the Constants;
     * c) Inserting all the needed modules;
     * d) Loading all the data with the buildModuleDatabases() method;
     * e) Starting the Derby server;
     * e) Creating the programme directory.
     */
    private void setup() throws Exception {
      
        //deamonize if required to do so
        if(daemonize) {
            System.out.close();
            System.err.close();
            System.in.close();
        }

        //set l&f
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {}

        //get the programme directory and export it for the PreProjectDialog
        programmeDir = new File(salssuite.server.Server.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI()).getParentFile();
        
        Constants.preProjectDialogNode.put("data.programmeDir", programmeDir.
                getAbsolutePath());

        if(programmeDir == null) {
            JOptionPane.showMessageDialog(null, "Konnte das Working Directory" +
                    " nicht bestimmen.", "Schwerer Fehler", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        //Import the project settings
        if(!noGUI && projectFile == null) {
            PreProjectDialog dia = new PreProjectDialog(null, true);
            dia.setVisible(true);
            projectFile = dia.getProjectDefinitionFile();
            int option = dia.getChosenOption();
            if(option == PreProjectDialog.NEW_PROJECT) {
                 try {
                    proj = ProjectSetupDialog.showProjectSetupDialog(null, true);
                    if(proj == null)
                        System.exit(0);
                    firstCallOfProject = true;
                }
                catch(Exception e) {
                    new ExceptionDisplayDialog(null, true, e, "UNBEKANNTER FEHLER").
                            setVisible(true);
                    System.exit(1);
                }
            }
            else if(option == PreProjectDialog.CONTINUE_LAST || option ==
                    PreProjectDialog.CONTINUE_OTHER){
               proj = new Project();
               proj.loadData(projectFile);
               firstCallOfProject = false;
            }
            else
                System.exit(0);
        }
        else if(!daemonize && projectFile == null) { //if nogui and not deamonize
            projectFile = CL_preProjectDialog();
            if(projectFile == null) {
                proj = CL_createProject(false);
                if(proj == null)
                    System.exit(0);
                firstCallOfProject = true;
            }
            else {
                proj = new Project();
                proj.loadData(projectFile);
                firstCallOfProject = false;
            }
        }
        else if(projectFile == null) { //if nogui and deamonize: load the last project
            if(Constants.preProjectDialogNode.get("data.lastProj", "").length() == 0)
               System.exit(1);

            projectFile = new File(Constants.preProjectDialogNode.get(
                    "data.lastProj", ""));

            if(!projectFile.exists())
                System.exit(1);
            proj = new Project();
            proj.loadData(projectFile);
        }
        else if(projectFile != null) { //project file submitted via command line
            if(!projectFile.exists()) {
                if(!noGUI) 
                    JOptionPane.showMessageDialog(null, "<html>Die auf der Kommandozeile"
                            + " angegebene Projektdefinitionsdatei<p>existiert"
                            + " nicht.<p>Beende die Anwendung.</html>", "Eingabefehler",
                            JOptionPane.ERROR_MESSAGE);
                else if(!daemonize)
                    System.out.println("Projektdefinitionsdatei ("+
                            projectFile.getAbsolutePath()+") existiert nicht.");

                System.exit(1);
            }

            proj = new Project();
            proj.loadData(projectFile);
        }

        //set up the constants
        constants = new Constants(programmeDir, proj);

        //If this is a new project, check if a project with this name
        //already exists. If so, refuse to create it.
        if(firstCallOfProject && constants.generalServerDataPath.exists()) {
            if(!noGUI)
                JOptionPane.showMessageDialog(gui, "<html>Das gewählte Projekt existiert"
                        + " bereits.<p>Bitte den Server neustarten und einen"
                        + " anderen Projektnamen wählen.</html>",
                        "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            else {
                System.out.println("Das gewählte Projekt existiert bereits. Bitte den"
                        + " Server neustarten und einen anderen Projektnamen"
                        + " wählen.");
            }


            System.exit(1);
        }

        proj.saveData(constants.projectDefFile);
        
        //create the logging output ability
        try {
            constants.serverLogPath.mkdirs();
            constants.serverLogFile.createNewFile();
            constants.serverErrorLogFile.createNewFile();


            logOut = new PrintWriter(new FileWriter(constants.serverLogFile, true));
            errorLogOut = new PrintWriter(new FileWriter(constants.serverErrorLogFile, true));
        }
        catch (IOException e) {
            throw new FileAccessException("Fehler: Konnte Logfiles nicht bereit" +
                    "machen. "+e.getMessage());
        }
        
        writeToLog("=========================SERVER START========================");
        writeToErrorLog("=========================SERVER START========================");

        //Setup the data structure if not already present
        //FIXME Add additional directories you might need here.
        try {
            programmeDir.mkdirs();
            new File(programmeDir.getAbsolutePath()+"/Projekte").mkdirs();
            constants.serverLogFile.getParentFile().mkdirs();
            constants.serverErrorLogFile.getParentFile().mkdirs();
        }
        catch (Exception e) {
            throw new FileAccessException("Fehler: Konnte Verzeichnisstruktur " +
                    "nicht anlegen. "+e.getMessage());
        }

        //create random database username and password
        writeToLog("SERVER: Setting database password and username.");
        databaseUsername = "DBUser";
        databasePasswordHash = Util.generateRandomString(30);

        //set derby properties
        writeToLog("SERVER: Setting Derby properties.");
        System.setProperty("derby.system.home", constants.generalDatabasePath.
                getAbsolutePath());
        System.setProperty("derby.connection.requireAuthentication", "true");
        System.setProperty("derby.authentication.provider", "BUILTIN");
        System.setProperty("derby.user."+databaseUsername,
                databasePasswordHash);

        //start database server
        writeToLog("SERVER: Starting the database server.");
        dbserver = new NetworkServerControl(InetAddress.getByName("0.0.0.0"),
                proj.getPort()+1);
        dbserver.start(null);

        writeToLog("SERVER: Database server up and running.");

        //open a connection to the server
        writeToLog("SERVER: Obtaining connection to the database.");
        dbcon = DriverManager.getConnection("jdbc:derby://localhost:" +
                (proj.getPort()+1) +
                "/general;" +
                "create=true;" +
                "user="+databaseUsername+";" +
                "password="+databasePasswordHash);
        stmt = dbcon.createStatement();

        //construct the database if necessary
        if(firstCallOfProject) {
            writeToLog("SERVER: This seems to be the first call of the project.");
            buildServerDatabase();
        }

        //Insert all the modules
        //FIXME Add your additional modules here.
        writeToLog("SERVER: Adding all the modules.");

        dutyModule = new DutyModule(this);
        modules.add(dutyModule);
        companyModule = new CompanyModule(this);
        modules.add(companyModule);
        magazineModule = new MagazineModule(this);
        modules.add(magazineModule);
        citizenModule = new CitizenModule(this);
        modules.add(citizenModule);
        accountingModule = new AccountingModule(this);
        modules.add(accountingModule);
        employmentModule = new EmploymentModule();
        modules.add(employmentModule);
        adminModule = new AdminModule();
        modules.add(adminModule);

        for(Module module: modules)
            writeToLog("SERVER: Inserted module '"+module.getName()+"'.");

        //build other databases or tables if necessary
        if(firstCallOfProject)
            buildModuleDatabases();

        if(!noGUI) {
            writeToLog("SERVER: Setting up the GUI.");
            gui = new ServerGUI(new PasswordPanel(dbcon, "all",
                    new ServerPanel(this), "Server"), "Server");

            //add all modules to the gui
            writeToLog("SERVER: Adding all modules to the GUI.");
            for(Module mod : modules)
                gui.addModule(mod);
        }

        //save all the server data
        try {
            saveData();
        }
        catch(FileAccessException e) {
            if(!noGUI)
                new ExceptionDisplayDialog(gui, true, e,
                        "Konnte Projekt nicht speichern.").setVisible(true);
            else {
                System.out.println("Konnte Projekt nicht speichern. Fehlermeldung:");
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                
                System.exit(1);
            }
        }

        //start listening for incoming authentication requests
        writeToLog("SERVER: Listening on port " + proj.getPort());

        Thread listener = new Thread() {
            ServerSocket listener = new ServerSocket(proj.getPort());

            @Override
            public void run() {
                while (true) {
                    try {
                        ConnectionHandler handler = new ConnectionHandler(
                                listener.accept());
                        handler.start();
                    }
                    catch(Exception e) {
                        if(!noGUI) {
                            new ExceptionDisplayDialog(gui, true, e, "Kann nicht"
                                    + " am Port "+(proj.getPort())+" auf Verbindungen"
                                    + " warten.").setVisible(true);
                        }
                        else {
                            System.out.println("Kann nicht am Port "+(proj.getPort())+
                                    " auf Verbindungen warten. Fehlermeldung:");
                            System.out.println(e.getMessage());
                            
                            e.printStackTrace();
                        }
                        writeToErrorLog("SERVER: Listening for incoming connections"
                                + " on port "+proj.getPort()+" failed.");
                        writeToErrorLog("SERVER:", e);
                    }
                }
            }
        };
        listener.start();

        //Add a shutdown hook to automatically call the server's shutdown method
        //if the JVM is interrupted irregularly (f.ex. using a SIGTERM signal).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run(){
                if(!serverShuttingDown) {
                    serverShuttingDown = true;
                    writeToLog("SERVER: Shutdown probably initiated by"
                            + " external event (JVM shutdown).");
                    try {
                        shutdown();
                    }
                    catch(Exception e) {
                        writeToErrorLog("SERVER: Shutdown failed.");
                        writeToErrorLog(e.getMessage());
                        for(StackTraceElement ele : e.getStackTrace())
                            writeToErrorLog(ele.toString());
                    }
                }
            }
        });
        serverShuttingDown = false;


        //we're done: display the GUI/start the CL interface
        writeToLog("=====================SERVER SETUP FINISHED===================");
        writeToErrorLog("=====================SERVER SETUP FINISHED===================");

        if(!noGUI)
            gui.setVisible(true);
        else if(!daemonize) {
            System.out.println("================");
            System.out.println("SERVER GESTARTET");
            System.out.println("================");
            System.out.println();
            System.out.println("Warte am Port "+proj.getPort() + " auf eingehende "
                    + "Verbindungen.");
            
            new Thread() {
                @Override
                public void run() {
                    CL_continuousInterface();
                }
            }.start();
        }
    }//end setup()

    //LOGGING
    /**
     * Appends a string to the normal log. The message is printed
     * along with a time stamp, but the caller should make sure that one knows
     * who the message comes from, e.g. by using a module name. The original
     * modules use "DUTY:", "SERVER:" etc. as prefixes. Messages with more than
     * one line (containing '\n' characters) are supported.
     * <p>
     * The normal log is intended for printing out non-exceptional information such as
     * establishing connections or terminating them in a usual way.
     * <p>
     * If the execution fails (for what reason ever) the method will simply
     * return.
     * @param message The message to be logged.
     */
    public synchronized void writeToLog(String message) {
        //split the message into single lines if necessary
        String[] messageLines = message.split("\n");

        //write the message
        for(String line : messageLines)
            logOut.println(generateLoggingTimeStamp() + line);
        logOut.flush();
    }//end writeToLog()

    /**
     * Appends all elements of the specified <code>Exception</code>'s stack trace
     * to the normal log. See {@link #writeToLog(String)} for details.
     * @param prefix A prefix which is printed out in front of each stack trace
     * element. This should usually be used to identify the module printing the
     * exception. The prefix is separated from the stack trace elements using
     * a space character.
     * @param e The exception of which the stack trace should be printed.
     */
    public void writeToLog(String prefix, Exception e) {
        String timestamp = generateLoggingTimeStamp();
        logOut.println(timestamp + prefix + "EXCEPTION");
        logOut.println(timestamp + " " + prefix + " "+ e.getMessage());
        
        for(StackTraceElement el : e.getStackTrace())
            logOut.println(timestamp + " " + prefix + " " + el);
        logOut.flush();
    }


    /**
     * Appends a string to the error log. The message is printed
     * along with a time stamp, but the caller should make sure that one knows
     * who the message comes from, e.g. by using a module name. The original
     * modules use "DUTY:","SERVER:" etc. as prefixes. Messages with more than
     * one line (containing '\n' characters) are supported.
     * <p>
     * The error log exists for printing exceptional messages such as terminating
     * a connection irregularly or mistakes in the data
     * provided by the server.
     * <p>
     * If the execution fails (for what reason ever) the method will simply
     * return.
     * @param message The message to be appended.
     */
    public synchronized void writeToErrorLog(String message) {

        //split the message into single lines if necessary
        String[] messageLines = message.split("\n");

        //write the message
        for(String line : messageLines)
            errorLogOut.println(generateLoggingTimeStamp() + line);
        errorLogOut.flush();
    }

    /**
     * Appends all elements of the specified <code>Exception</code>'s stack trace
     * to the error log. See {@link #writeToErrorLog(String)} for details.
     * @param prefix A prefix which is printed out in front of each stack trace
     * element. This should usually be used to identify the module printing the
     * exception. The prefix is separated from the stack trace elements using
     * a space character.
     * @param e The exception of which the stack trace should be printed.
     */
    public void writeToErrorLog(String prefix, Exception e) {
        String timestamp = generateLoggingTimeStamp();
        errorLogOut.println(timestamp + prefix + "EXCEPTION");
        errorLogOut.println(timestamp + " " + prefix + " "+ e.getMessage());
        
        for(StackTraceElement el : e.getStackTrace())
            errorLogOut.println(timestamp + " " + prefix + " " + el);
        errorLogOut.flush();
    }

    /**
     * Generates a timestamp suited for logging.
     * @return The timestamp.
     */
    private String generateLoggingTimeStamp() {
        GregorianCalendar cal = new GregorianCalendar();

        String timestamp = "[";
        timestamp += cal.get(GregorianCalendar.YEAR) + "/";
        timestamp += (cal.get(GregorianCalendar.MONTH)+1) + "/";
        timestamp += cal.get(GregorianCalendar.DAY_OF_MONTH) + " ";
        timestamp += cal.get(GregorianCalendar.HOUR_OF_DAY) + ":";
        timestamp += cal.get(GregorianCalendar.MINUTE) + ":";
        timestamp += cal.get(GregorianCalendar.SECOND) + ":";
        timestamp += cal.get(GregorianCalendar.MILLISECOND);

        if(timestamp.length() < 23) {
            for(int i=timestamp.length(); i<=22; i++) {
                timestamp += " ";
            }
        }

        timestamp += "] ";
        
        return timestamp;
    }


    //EXTERNAL INFLUENCE ON SERVER

    /**
     * Shuts the server down, closing all connections. The server data is saved
     * and the {@link salssuite.server.module.Module#serverShutdown serverShutdown()}
     * method of all modules is called.
     * @throws Exception if an error occurs while saving the server data.
     */
    public void shutdown() throws Exception {
        serverShuttingDown = true;
        writeToLog("SERVER: Shutting down.");

        //save server data
        saveData();

        //let modules do whatever they want to do
        writeToLog("SERVER: Letting modules perform shutdown operations.");
        for(Module mod : modules)
            mod.serverShutdown();

        //shutdown DB server
        writeToLog("SERVER: Shutting down Derby server.");
        try {
            DriverManager.getConnection("jdbc:derby:;" +
                    "shutdown=true;" +
                    "user="+databaseUsername+";" +
                    "password="+databasePasswordHash);
            dbserver.shutdown();
        }
        catch(SQLException e) {
            writeToLog("SERVER: Derby shutdown complete.");
        }
        
    }//end shutdown()

    //SAVING/RESTORING

    /**
     * Builds all database tables defined by the server's modules.
     * <p>
     * If any error occurs while calling the other methods, this method throws
     * the exception indicating the error.
     * @throws Exception if any error occurs while loading the server and
     * module data.
     */
    public void buildModuleDatabases() throws Exception {
        writeToLog("SERVER: Building module database tables.");
        for(Module mod : modules) {
            mod.buildDatabase();
        }
        writeToLog("SERVER: Finished building module database tables.");
    }//end buildModuleDatabases()


    /**
     * Saves the {@link Project} data and some usability preferences of
     * the server.
     * @throws FileAccessException if {@link Project#saveData} throws the same
     * exception.
     */
    public void saveData() throws FileAccessException {
        writeToLog("SERVER: Saving server data.");
        Preferences.userRoot().node("SalSSuite").node("PreProjectDialog").put(
                "data.lastProj", constants.projectDefFile.getAbsolutePath());
        proj.saveData(constants.projectDefFile);
        writeToLog("SERVER: Done saving server data.");
    }//end saveData()


    /**
     * Constructs the general database tables used by the server and populates them
     * with user-provided citizen data from the input folder.
     *
     * <h3>Database structure</h3>
     *
     * <h4>Citizen Table</h4>
     *
     * <code>CREATE TABLE "DBUSER"."CITIZENS"(<br/>
     * "ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),<br/>
     * "FORENAME" VARCHAR(100) NOT NULL,<br/>
     * "SURNAME" VARCHAR(100) NOT NULL,<br/>
     * "FORM" VARCHAR(20) NOT NULL,<br/>
     * "COMPANYID" INTEGER DEFAULT -1,<br/>
     * "SALARY" DOUBLE DEFAULT 0,<br/>
     * "ISBOSS" INTEGER DEFAULT 0,<br/>
     * PRIMARY KEY ("ID"));</code>
     *
     * <h4>Companies Table</h4>
     *
     * <code>CREATE TABLE "DBUSER"."COMPANIES" (<br/>
     * "ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),<br/>
     * "NAME" VARCHAR(1000) NOT NULL,<br/>
     * "ROOM" VARCHAR(20),<br/>
     * "PRODUCTDESCRIPTION" VARCHAR(2000),<br/>
     * "JOBS" INT,<br/> //the number of jobs this company has to offer
     * PRIMARY KEY ("ID"));</code>
     *
     * <h4>Account/Permissions Tables</h4>
     *
     * <code>CREATE TABLE "DBUSER"."ACCOUNTS" (<br/>
     * "USERNAME" VARCHAR(1000) NOT NULL,<br/>
     * "HASH" VARCHAR(1000) NOT NULL,<br/>
     * PRIMARY KEY ("USERNAME"));</code>
     * <p>
     * <code>CREATE TABLE "DBUSER"."PERMISSIONS" (<br/>
     * "MODULE" VARCHAR(1000) NOT NULL,<br/>
     * "USERNAME" VARCHAR(1000) NOT NULL,<br/>
     * PRIMARY KEY ("MODULE", "USERNAME"));</code>
     *
     * <h3>Data Import</h3>
     *
     * Citizen data can be imported from {@link salssuite.util.Constants#citizenInputFile}
     * because it is probably rather common to get a list of pupils and teachers
     * from the school secretary instead of compiling it by oneself.
     * <p>
     * The file must contain all the citizen data in csv format. It must be
     * built up as follows:
     * <p>
     * <code>
     * "surname","forename","form",companyId
     * </code>
     * <p>
     * Text must be enclosed in double quotes ("); values must be separated
     * by commas. Each line must be terminated with a newline character.
     * No spaces are permitted between values.<br/>
     * The first line of the file <i>must</i> contain a header
     * line. The contents of the header line are not important, it can even
     * be blank.
     * @throws Exception if the database cannot be built for any reason or if there
     * is an error while importing the citizen data.
     */
    public void buildServerDatabase() throws Exception {

        if(!firstCallOfProject)
            return;

        writeToLog("SERVER: Creating server database tables.");

        //set up the database
        writeToLog("SERVER: Creating 'citizens' table.");
        stmt.executeUpdate("CREATE TABLE citizens (" +
                "id INT NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "forename VARCHAR(100) NOT NULL," +
                "surname VARCHAR(100) NOT NULL," +
                "form VARCHAR(20) NOT NULL," +
                
                "companyId INT DEFAULT -1," +
                "salary DOUBLE DEFAULT 0," +
                "isBoss INT DEFAULT 0," +

                "PRIMARY KEY (id)" +
                ")");

        writeToLog("SERVER: Creating 'companies' table.");
        stmt.executeUpdate("CREATE TABLE companies (" +
                "id INT NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "name VARCHAR(1000) NOT NULL," +
                "room VARCHAR(20)," +
                "productDescription VARCHAR(2000)," +
                "jobs INT," +
                "PRIMARY KEY (id)" +
                ")");

        writeToLog("SERVER: Creating 'accounts' table.");
        stmt.executeUpdate("CREATE TABLE accounts ("
                + "username VARCHAR(1000) NOT NULL,"
                + "hash VARCHAR(1000) NOT NULL,"
                + "PRIMARY KEY (username)"
                + ")");

        writeToLog("SERVER: Creating 'permissions' table.");
        stmt.executeUpdate("CREATE TABLE permissions ("
                + "module VARCHAR(1000) NOT NULL,"
                + "username VARCHAR(1000) NOT NULL,"
                + "PRIMARY KEY (module, username)"
                + ")");

        //populate it with data from user-provided csv files and the install password file
        importCitizenDataFromCsv();

        try {
            writeToLog("SERVER: Reading password set at install time.");
            String installPw = Passwords.getPassword(constants.serverPasswordFile);

            if(installPw == null) {
                writeToErrorLog("SERVER: Error while reading the install time"
                        + " password. Emergency exit.");

                if(!noGUI)
                    JOptionPane.showMessageDialog(gui, "<html>Konnte bei der Installation"
                            + " angegebenes Passwort nicht auslesen.<p>"
                            + "Beende die Anwendung.</html>",
                            "Schwerer Fehler", JOptionPane.ERROR_MESSAGE);
                else {
                    System.out.println("Konnte bei der Installation angegebenes Passwort"
                            + " nicht auslesen. Beende die Anwendung.");
                    System.out.println("Eventuell sollten Sie die SalSSuite neu installieren.");
                    
                }
                System.exit(1);
            }

            stmt.executeUpdate("INSERT INTO accounts VALUES ('admin', "
                    + "'"+installPw+"'"
                    + ")");

            stmt.executeUpdate("INSERT INTO permissions VALUES ('all',"
                    + "'admin'"
                    + ")");
        }
        catch(Exception e) {
            writeToErrorLog("SERVER: Error while reading the install time password.");
            writeToErrorLog("SERVER:", e);
            if(!noGUI)
                new ExceptionDisplayDialog(gui, true, e, "KONNTE GLOBALES INSTALLATIONS"
                        + "PASSWORT NICHT LESEN").setVisible(true);
            else {
                System.out.println("Konnte globales Installationspasswort nicht lesen."
                        + " Beende die Anwendung. Fehlermeldung:");
                System.out.println(e.getMessage());
                
                e.printStackTrace();
            }
            System.exit(1);
        }

        writeToLog("SERVER: Done creating server database tables.");
    }//end buildServerDatabase()

    /**
     * Imports basic data about all citizens of the fictive state from an input
     * file to the database. Returns if the input path is not set (meaning that
     * nothing should be imported.
     * @throws FileAccessException if some error occurs while accessing the
     * input file or reading data from it.
     */
    private void importCitizenDataFromCsv() throws FileAccessException {     

        //if the user does not want to have anything imported, the input
        //path is not set
        if(constants.inputPath == null) {
            writeToLog("SERVER: User input path not set. Nothing will be imported.");
            return;
        }

        writeToLog("SERVER: Importing citizen data from user input file.");
        writeToLog("SERVER: "+constants.citizenInputFile.getAbsolutePath());

        //if the input file does not exist, display a warning message and continue
        if(!constants.citizenInputFile.exists()) {
            writeToErrorLog("SERVER: Could not locate citizen input file.");

            if(!noGUI)
                JOptionPane.showMessageDialog(gui, "<html>Die Eingabedatei für Bürgerdaten"
                        + " existiert nicht.<p>Bürgerdaten werden nicht"
                        + " importiert.<p>("
                        + constants.citizenInputFile.getAbsolutePath()+")</html>",
                        "Fehler beim Importieren", JOptionPane.WARNING_MESSAGE);
            else {
                System.out.println("Warnung: Die Eingabedatei für Bürgerdaten existiert"
                        + " nicht. Bürgerdaten werden nicht importiert. ("+
                        constants.citizenInputFile.getAbsolutePath()+")");
                
            }
            return;
        }

        Scanner fileIn;

        //display an indeterminate progress bar to the user
        ProgressDialog dia = null;
        if(!noGUI) {
            dia = ProgressDialog.showProgressDialog(
                    gui, false, javax.swing.JDialog.DO_NOTHING_ON_CLOSE, true,
                    "Importiere Bürgerdaten...");
        }
        else {
            System.out.println("Die Bürgerdaten werden importiert. Bitte haben Sie eine"
                    + " Weile Geduld; dies könnte einige Zeit dauern.");
            
        }

        //connect to the file and read the header
        writeToLog("SERVER: Beginning to read citizen data from the input file.");
        try {
            fileIn = new Scanner(new FileReader(constants.citizenInputFile));
            if(fileIn.nextLine() == null)
                throw new FileAccessException("Unbekannter Fehler beim Lesen der" +
                        " Inputdatei.", constants.citizenInputFile);
        }
        catch (IOException e) {
            writeToErrorLog("SERVER: Error while opening citizen input file.");
            writeToErrorLog("SERVER:", e);
            throw new FileAccessException("Fehler beim Öffnen der" +
                    "Importdatei: "+e.getMessage(), constants.citizenInputFile);
        }

        //line counter
        int ct = 1;

        try {
            while(true) {

                //break the loop if no input is left
                if(!fileIn.hasNextLine())
                    break;

                //read all the fields
                String[] input = fileIn.nextLine().split(",");
                ct++;

                //ID (not used later)
                Integer.parseInt(input[0].replace("\"", ""));

                //name
                String surname = input[1].replace("\"", "").replace("'", " ");
                String forename = input[2].replace("\"", "").replace("'", " ");

                //form
                String form = input[3].replace("\"", "");

                //company ID
                int companyID = -1;
                try {
                    companyID = Integer.parseInt(input[4].replace("\"", ""));
                }
                catch (ArrayIndexOutOfBoundsException e) {}

                //add everything to the database

                stmt.executeUpdate("INSERT INTO citizens (surname, forename," +
                        "form, companyId) " +
                        "VALUES (" +
                        "'"+surname+"', " +
                        "'"+forename+"', " +
                        "'"+form+"', " +
                        companyID +
                        ")");

            }//end TheGreatWhile
        }
        catch(InputMismatchException e) {
            writeToErrorLog("SERVER: Error while parsing citizen input file.");
            writeToErrorLog("SERVER:", e);
            e.printStackTrace();
            throw new FileAccessException("Fehler (Zeile "+ ct + "): Integer" +
                    " erwartet.\n"+e.getMessage());
        }
        catch(NumberFormatException e) {
            writeToErrorLog("SERVER: Error while parsing citizen input file.");
            writeToErrorLog("SERVER:", e);
            e.printStackTrace();
            throw new FileAccessException("Fehler (Zeile "+ ct + "): Integer" +
                    " erwartet.\n"+e.getMessage());
        }
        catch(SQLException e) {
            writeToErrorLog("SERVER: Error while parsing citizen input file.");
            writeToErrorLog("SERVER:", e);
            e.printStackTrace();
            throw new FileAccessException("Fehler bei der Kommunikation mit der" +
                    " Datenbank (Zeile "+ct+"): "+e.getMessage());
        }
        finally {
            if(dia != null)
                dia.dispose();
        }

        if(dia != null)
            dia.dispose();
        writeToLog("SERVER: Finished importing "+ct+" citizens from the input file.");

        if(noGUI) {
            System.out.println("Bürgerdaten erfolgreich importiert.");
            
        }
    }//end importCitizenDataFromCsv()


    /**
     * Generates a backup of the whole server data in the standard backup
     * directory determined by {@link salssuite.util.Constants#generalBackupPath}.
     * An appropriate subdirectory with a timestamp is created.
     * <p>
     * A backup is a copy of the whole server data stored in
     * {@link salssuite.util.Constants#generalServerDataPath} and its subdirectories.
     * @throws Exception if an error occurs while saving the server data or
     * copying the files.
     */
    public void makeBackup() throws Exception {
        writeToLog("SERVER: Making a backup.");

        //see what the target directory is
        String timestamp = proj.getProjectName()+Util.getDateString()+"_"+
                Util.getTimeString();

        File target = new File(constants.generalBackupPath, "/"+timestamp+"/");

        writeToLog("SERVER: The backup file is "+target.getAbsolutePath());

        //save the data
        saveData();

        //copy the whole of the saved data
        try {          
                Util.copyFile(constants.generalServerDataPath, target);
        }
        catch(FileAccessException e) {
            writeToErrorLog("SERVER: Could not make backup: Error while copying files.");
            writeToErrorLog("SERVER:", e);

            if(!noGUI)
                new ExceptionDisplayDialog(null, true, e, "KONNTE DATEN FÜR BACKUP" +
                        " NICHT KOPIEREN", 30).setVisible(true);
            else {
                System.out.println("Konnte Daten für Backup nicht kopieren. Fehlermeldung:");
                System.out.println(e.getMessage());
                
                e.printStackTrace();
            }

            return;
        }

        writeToLog("SERVER: Done making backup.");
    }


    //MAKING OUTPUT
    /**
     * Generates output for every loaded <code>Module</code> by calling its
     * {@link salssuite.server.module.Module#makeOutput makeOutput()} method.
     * @throws Exception if an exception is thrown by any of the <code>
     * makeOutput()</code> methods. Note that the exception is thrown <i>after
     * </i> all modules have produced output, and the exception then contains
     * the names of all affected modules, as well as a general error description.
     */
    public void makeOutput() throws Exception {

        boolean errorOccured = false;
        ArrayList<String> msg = new ArrayList<String>(0);

        writeToLog("SERVER: Generating output for all modules.");

        //generate file structure
        constants.recomputePaths();
        constants.outputPath.mkdirs();

        //make the output
        for(Module mod : modules) {
            try {
                mod.makeOutput();
            }
            catch(Exception e) {
                writeToErrorLog("SERVER: Error while making output for module '"+
                        mod.getName()+"'.");
                writeToErrorLog("SERVER:", e);
                errorOccured = true;
                msg.add(mod.getName());
                e.printStackTrace();
            }
        }
        if(errorOccured) {
            String names = "";
            for(String name : msg)
                names += ", "+name;
            
            throw new Exception("Fehler beim Erstellen von Output für folgende" +
                   " Module: "+names);
        }
        writeToLog("SERVER: Done generating output for all modules.");
           
    }//end makeOutput()


    //DATA MANIPULATION

    /**
     * Lets the user edit the project settings.
     * To do so a {@link salssuite.server.gui.ProjectSetupDialog} is shown.
     * @param parent The <code>JFrame</code> the created <code>ProjectSetupDialog
     * </code> should be modal to.
     */
    public void changeProject(JFrame parent) {
        writeToLog("SERVER: User attempt to change the project settings.");
        Project newProject = ProjectSetupDialog.showProjectSetupDialog(parent,
                true, proj);
        if(newProject != null) {
            writeToLog("SERVER: User changed project settings.");
            proj = newProject;
        }
        else {
            writeToLog("SERVER: User cancelled changing project settings.");
            return;
        }

        constants.changeProject(proj);
        try {
            saveData();
        }
        catch(FileAccessException e) {
            writeToErrorLog("SERVER: Error while saving the project data.");
            writeToErrorLog("SERVER:", e);
            if(!noGUI)
                new ExceptionDisplayDialog(gui, true, e,
                        "Konnte Projekt nicht speichern.").setVisible(true);
            else {
                System.out.println("Konnte Projekt nicht speichern. Fehlermeldung:");
                System.out.println(e.getMessage());
                
                e.printStackTrace();
            }
        }
    }

    //GETTER

    /**
     * Returns a connection to the general database.
     * @return A connection to the database.
     */
    public Connection getDatabaseConnection() {
        return dbcon;
    }
    
    /**
     * Returns the root directory of the programme. This is the directory
     * in which <code>Server.jar</code> is located, or the top level directory
     * of the classes tree.
     * @return The programme directory.
     */
    public File getProgrammeDirectory() {
        return programmeDir;
    }

    /**
     * Returns the constants for the specific project and programme directory.
     * @return The constants.
     */
    public Constants getConstants() {
        return constants;
    }

    /**
     * Returns the current project.
     * @return The project.
     */
    public Project getProject() {
        return proj;
    }

    /**
     * Returns the server's GUI.
     * @return The GUI.
     */
    public ServerGUI getGUI() {
        return gui;
    }

    /**
     * Returns a list of all modules which are currently activated in this server.
     * @return The modules.
     */
    public Module[] getModules() {
        return modules.toArray(new Module[1]);
    }

    /**
     * Indicates whether this server runs in normal or command line mode.
     * @return <code>true</code> if the server runs in command line mode, i.e.
     * no graphical user interface should be displayed;<br/>
     * <code>false</code> if the server runs in normal mode and has a GUI.
     */
    public boolean isNoGUI() {
        return noGUI;
    }

    // COMMAND LINE SUPPORT

    /**
     * Allows the user to save the server data, generate output, make backups
     * and edit the project while the server is running, or shutdown the
     * server. This method loops forever unless the user cancels it.
     */
    private void CL_continuousInterface() {
        while(true) {

            System.out.println();
            System.out.println("Was möchten Sie tun?");
            System.out.println("(1) Die Daten des aktuellen Projekts bearbeiten.");
            System.out.println("(2) Ein Backup anlegen.");
            System.out.println("(3) Output generieren.");
            System.out.println("(4) Die Daten des Servers speichern.");
            System.out.println("(5) Den Server herunterfahren.");
            System.out.println("Ihr Befehl (1, 2, 3, 4, 5): ");
            

            String command = in.nextLine();

            if(command.equals("1")) {
                Project newProject = CL_createProject(true);
                if(newProject == null)
                    continue;

                proj = newProject;
                try {
                    constants.changeProject(proj);
                    saveData();
                }
                catch(FileAccessException e) {
                    System.out.println("Konnte Projekt nicht speichern. Fehlermeldung:");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace());
                    
                    continue;
                }

                System.out.println("Projekt gespeichert.");
                
                continue;
            }

            else if(command.equals("2")) {
                try {
                    makeBackup();
                }
                catch(Exception e) {
                    System.out.println("Backup konnte nicht erstellt werden. Fehlermeldung:");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace());
                    
                    continue;
                }

                System.out.println("Backup erfolgreich erstellt.");
                
                continue;
            }

            else if(command.equals("3")) {
                System.out.println("Erstelle Output. Dies kann eine Weile dauern...");
                

                try {
                    makeOutput();
                }
                catch(Exception e) {
                    System.out.println("Output konnte nicht erstellt werden. Fehlermeldung:");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace());
                    
                    continue;
                }

                System.out.println("Output erfolgreich erstellt.");
                
                continue;
            }

            else if(command.equals("4")) {
                try {
                    saveData();
                }
                catch(Exception e) {
                    System.out.println("Daten konnten nicht gespeichert werden. Fehlermeldung:");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace());
                    
                    continue;
                }

                System.out.println("Daten erfolgreich gespeichert.");
                
                continue;
            }

            else if(command.equals("5")) {
                try {
                    shutdown();
                }
                catch(Exception e) {
                    System.out.println("Server konnte nicht sauber heruntergefahren"
                            + "werden. Eventuell sind Daten verloren gegangen. "
                            + "Fehlermeldung:");
                    System.out.println(e.getMessage());
                    System.out.println(e.getStackTrace());
                    
                    continue;
                }

                System.out.println("Keine Fehler beim Herunterfahren. Bis zum nächsten"
                        + " Mal.");
                
                System.exit(0);
                return;
            }

            else {
               System.out.println("Ungültige Eingabe. Bitte verwenden Sie die Zahl,"
                       + " die vor der jeweiligen Aktion in Klammern steht,"
                       + " als Befehl.");
               
               continue;
            }
        }//end thegreatwhile
    }

    /**
     * Surrogate for the PreProjectDialog when the server is run without a
     * GUI. Asks the user whether he wants continue the last, begin a new or
     * continue another project, and returns the project definition file if
     * possible.
     * @return The project definition file, or null if the user wants to
     * start a new project.
     */
    private File CL_preProjectDialog() {
        Preferences node = Constants.preProjectDialogNode;
        File projectDef = null;

        while(true) {
            System.out.println("Was möchten Sie tun?");
            System.out.println("(1) Das letzte Projekt fortsetzen.");
            System.out.println("(2) Ein neues Projekt beginnen.");
            System.out.println("(3) Ein altes Projekt öffnen.");
            System.out.println();
            System.out.println("Ihr Befehl (1, 2, 3): ");
            
            String input = in.nextLine();

            if(input.equals("1")) {
                if(node.get("data.lastProj", "").length() == 0) {
                   System.out.println("Konnte letztes Projekt nicht finden.");
                   
                   continue;
                }

                projectDef = new File(node.get("data.lastProj", ""));

                if(!projectDef.exists()) {
                    System.out.println("Fehler: Definitionsdatei für letztes Projekt ("
                            +projectDef.getAbsolutePath()+") nicht gefunden.");
                    projectDef = null;
                    continue;
                }

                return projectDef;
            }
            else if(input.equals("2")) {
                return null;
            }
            else if(input.equals("3")) {
                System.out.println("Bitte Pfad zur Projektdefinitionsdatei (normalerweise"
                        + " proj.xml) angeben:");
                
                projectDef = new File(in.nextLine());
                if(!projectDef.exists()) {
                    System.out.println("Fehler: Definitionsdatei ("
                            +projectDef.getAbsolutePath()+") nicht gefunden.");
                    projectDef = null;
                    continue;
                }
                return projectDef;
            }
            else {
                System.out.println("Fehlerhafte Eingabe.");
                continue;
            }
        }
    }

    /**
     * Lets the user create a project without using a GUI. Surrogate
     * for the ProjectSetupDialog.
     * @return The created project, or null if the user has cancelled.
     * @param editProjet Set to true if an existing project should be modified
     * rather than creating a new one. Assumes that a valid project can be found
     * using the proj field.
     */
    private Project CL_createProject(boolean editProject) {
        //introduction
        System.out.println();
        System.out.println("---------- NEUES PROJEKT ERSTELLEN ----------");
        System.out.println();
        System.out.println("Sie können bei jeder Eingabe abbrechen, indem Sie 'quit'"
                + " eingeben.");
        System.out.println();
        

        //parse name
        String name;

        if(!editProject)
            while(true) {
                System.out.println("Projektname: ");
                
                name = in.nextLine();

                if(name.equals("quit"))
                    return null;

                if(!Util.checkInput_CL(name))
                    continue;

                if(name.length() == 0) {
                    System.out.println("Bitte einen Namen für das Projekt angeben.");
                    
                    continue;
                }

                if(name.contains(System.getProperty("file.separator"))) {
                    System.out.println("Der Projektname darf kein"
                            + " Pfadtrennzeichen enthalten. ('" +
                            System.getProperty("file.separator")+"')");
                    
                    continue;
                }

                break;
            }
        else {
            System.out.println("Projektname kann nicht geändert werden.");
            
            name = proj.getProjectName();
        }

        //parse attendance time and port
        int attendanceTime;
        int port;

        while(true) {
            try {
                System.out.println("Minimale Anwesenheitszeit in Minuten: ");
                
                String attendanceTimeInput = in.nextLine();

                if(attendanceTimeInput.equals("quit"))
                    return null;

                attendanceTime = Integer.parseInt(attendanceTimeInput);

                if(editProject)
                    System.out.println("Bitte beachten Sie, dass eine Änderung des"
                            + " Ports erst bei einem Neustart des Servers"
                            + " wirksam wird.");

                System.out.println("Server-Port (*Return* für Standard-Port " +
                        Constants.GENERAL_DATABASE_PORT + "):");
                
                String portInput = in.nextLine();

                if(portInput.length() == 0)
                    port = Constants.GENERAL_DATABASE_PORT;
                else if(portInput.equals("quit"))
                    return null;
                else
                    port = Integer.parseInt(portInput);

                if(attendanceTime < 0) {
                    System.out.println("Fehler: Anwesenheitszeit muss positiv sein.");
                    
                    continue;
                }

                if(port < 0) {
                    System.out.println("Fehler: Port muss positiv sein.");
                    
                    continue;
                }

                break;
            }
            catch(NumberFormatException e) {
                System.out.println("Fehler: Anwesenheitszeit und Server-Port müssen ganze Zahlen"
                        + "sein.");
                
                continue;
            }
        }

        //parse dates
        GregorianCalendar startDay;
        GregorianCalendar endDay;

        while(true) {
            System.out.println("Starttag (Format: dd.mm.yyyy): ");
            
            String startDayInput = in.nextLine();

            if(startDayInput.equals("quit"))
                return null;

            System.out.println("Endtag (Format: dd.mm.yyyy): ");
            
            String endDayInput = in.nextLine();

            if(endDayInput.equals("quit"))
                return null;

            int endD, endM, endY;
            int startD, startM, startY;

            boolean startDayParsedSuccessfully = false;

            try {
                startD = Integer.parseInt(startDayInput.split("\\.")[0]);
                startM = Integer.parseInt(startDayInput.split("\\.")[1]);
                startY = Util.expandYear(Integer.parseInt(startDayInput.split("\\.")[2]));

                startDayParsedSuccessfully = true;

                endD = Integer.parseInt(endDayInput.split("\\.")[0]);
                endM = Integer.parseInt(endDayInput.split("\\.")[1]);
                endY = Util.expandYear(Integer.parseInt(endDayInput.split("\\.")[2]));
            }
            catch (NumberFormatException e) {
                if(startDayParsedSuccessfully) {
                    System.out.println("Ungültige Eingabe für den Endtag: Keine Zahl.");
                    continue;
                }
                else {
                    System.out.println("Ungültige Eingabe für den Starttag: Keine Zahl.");
                    continue;
                }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("Ungültiges Datumsformat.");
                continue;
            }

            //check whether ints are valid
            if(startD <= 0 || startM <= 0 || startY <= 0 || endD <= 0 || endM <= 0 ||
                    endY <= 0 || startD > 31 || endD > 31 || endM > 12 || startM > 12) {
                System.out.println("Ungültige Eingabe für Start- oder Endtag.");
                
                continue;
            }

            //create GregorianCalendars
            endDay = new GregorianCalendar(endY, endM-1, endD);
            startDay = new GregorianCalendar(startY, startM-1, startD);

            //see whether the days are valid
            if(startDay.after(endDay)) {
                System.out.println("Fehler: Endtag ist vor oder gleich Starttag.");
                
                continue;
            }

            if(startDay.get(GregorianCalendar.YEAR) != endDay.get(
                    GregorianCalendar.YEAR)) {
                System.out.println("Fehler: Start- und Endtag müssen im gleichen Jahr"
                        + "liegen.");
                
                continue;
            }

            break;
        }//end parse dates

        //parse pathes
        File inputPath;
        File outputPath;

        while(true) {
            System.out.println("Ausgabepfad: ");
            
            String outputPathInput = in.nextLine();

            if(outputPathInput.equals("quit"))
                return null;

            outputPath = new File(outputPathInput);

            if(!editProject) {
                System.out.println("Eingabepfad (*Return* um keine Daten zu importieren): ");
                
                String inputPathInput = in.nextLine();

                if(inputPathInput.equals("quit"))
                    return null;

                inputPath = new File(inputPathInput);
                
                if(inputPathInput.length() == 0) {
                    System.out.println("Wenn Sie den Eingabepfad nicht setzen, werden"
                            + " keine Daten importiert. Fortfahren? (j/n)");
                    
                    String input = in.nextLine();
                    if(!input.equals("j"))
                        continue;
                    else
                        inputPath = null;
                }

                if(inputPath != null && !inputPath.isDirectory()) {
                    System.out.println("Der Eingabepfad muss ein Verzeichnis sein und"
                            + " existieren.");
                    
                    continue;
                }
            }
            else
                inputPath = null;


            if(outputPathInput.length() == 0) {
                System.out.println("Bitte Ausgabepfad wählen.");
                
                continue;
            }
            outputPath.mkdirs();

            break;
        }

        try {
            return new Project(name, startDay, endDay, attendanceTime, port,
                    inputPath, outputPath);
        }
        catch(ProjectException e) {
            System.out.println("Fehler beim Erstellen des Projekts:");
            System.out.println(e.getMessage());
            
            return null;
        }
    }


//===============================PRIVATE CLASSES==============================//

    /**
     * A thread which handles the connection to one client and tries to serve
     * its authentication request.
     */
    private class ConnectionHandler extends Thread {

        Socket connection;
        String inetAddress;
        int port;

        Scanner in;
        PrintWriter out;

        public ConnectionHandler(Socket connection) {
            setDaemon(true);
            this.connection = connection;
            inetAddress = connection.getInetAddress().toString();
            port = connection.getPort();

            writeToLog("AUTH: Establishing connection to "+inetAddress+":"+
                    port);    
        }

        @Override
        public void run() {

            //connect to the client
            try {
                in = new Scanner(connection.getInputStream());
                out = new PrintWriter(new OutputStreamWriter(
                        connection.getOutputStream()));
            }
            catch(Exception e) {
                writeToErrorLog("AUTH: Could not establish connection to "
                        + inetAddress+":"+port);
                writeToErrorLog("AUTH:", e);
                return;
            }

            while(true) {
            try {

                //read request
                String command = in.nextLine();
                if(!command.equals("AUTHORIZATION")) {
                    writeToErrorLog("AUTH: Client "+inetAddress+":"+port+" "
                            + "sent unknown command. Terminating connection.");
                    out.close();
                    in.close();
                    return;
                }

                String username;
                String hash;
                String module;

                String usernameData = in.nextLine();
                if(!usernameData.startsWith("USERNAME ")) {
                    writeToErrorLog("AUTH: Client "+inetAddress+":"+port+" "
                            + "sent senseless input. Terminating connection.");
                    out.close();
                    in.close();
                    return;
                }
                username = usernameData.replace("USERNAME ", "");

                String hashData = in.nextLine();
                if(!hashData.startsWith("HASH ")) {
                    writeToErrorLog("AUTH: Client "+inetAddress+":"+port+" "
                            + "sent senseless input. Terminating connection.");
                    out.close();
                    in.close();
                    return;
                }
                hash = hashData.replace("HASH ", "");

                String moduleData = in.nextLine();
                if(!moduleData.startsWith("MODULE ")) {
                    writeToErrorLog("AUTH: Client "+inetAddress+":"+port+" "
                            + "sent senseless input. Terminating connection.");
                    out.close();
                    in.close();
                    return;
                }
                module = moduleData.replace("MODULE ", "");

                writeToLog("AUTH: User '"+username+"' attempts to"
                        + " access module '"+module+"' with password hash"
                        + " "+hash);


                //look up account data
                ResultSet password = stmt.executeQuery("SELECT hash FROM "
                        + "accounts WHERE username = '"+username+"'");
                if(!password.next()) {
                    writeToLog("AUTH: User '"+username+"' does not exist.");
                    out.println("FAIL UNKNOWN USERNAME");
                    out.flush();
                    continue;
                }

                if(!hash.equals(password.getString("hash"))) {
                    writeToLog("AUTH: User '"+username+"' attempted to"
                            + " connect with wrong password.");
                    out.println("FAIL WRONG PASSWORD");
                    out.flush();
                    continue;
                }

                //look up permission
                ResultSet permission = stmt.executeQuery("SELECT module FROM"
                        + " permissions WHERE (module = '"+module+"' OR"
                        + " module = 'all') AND "
                        + " username = '"+username+"'");

                if(!permission.next()) {
                    writeToLog("AUTH: User '"+username+"' was denied access to module '"
                            + module+"'.");
                    out.println("FAIL PERMISSION DENIED");
                    out.flush();
                    continue;
                }

                //if everything was successfull: print database data
                writeToLog("AUTH: User '"+username+"' authenticated.");
                out.println("SUCCESS");
                out.println("DATABASE_USERNAME "+databaseUsername);
                out.println("DATABASE_PASSWORD "+databasePasswordHash);
                out.flush();
                out.close();

                break;
            }
            catch(Exception e) {
                writeToErrorLog("AUTH: Unpredicted error while communicating"
                        + " with client "+inetAddress+":"+port+"."
                        + "Terminating connection.");
                writeToErrorLog("AUTH:", e);
                return;
            }
            }//end thegreatwhile

        }//end run()

    }//end class ConnectionHandler

}
