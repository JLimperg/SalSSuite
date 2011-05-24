package salssuite.util;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.prefs.Preferences;
import salssuite.server.Project;

/**
 * Provides constants, folders, files, and <code>Preferences</code> nodes
 * which are used throughout the whole programme. This class is
 * mainly useful to provide an overview over the file structure.
 * Furthermore, changing the fields' values will affect all usage made
 * of them, which should lead to consistent behaviour all over the programme.
 * <p>
 * To obtain the right file structure, you have to create a new <code>Constants</code>
 * object, passing the current project and programme directory. All further values
 * are set automatically. By default all files and folders are relative to the
 * programme directory.
 * <p>
 * This class automatically adjusts folder and file names according to the current
 * date whenever this is necessary.
 * <p>
 * Note that no effort is made to provide any thread-safety. The public members of
 * this class are meant to be <b>read only</b> and not modified by anything except
 * this class itself.
 * @author Jannis Limperg
 */
public class Constants {


//==============================CONSTANTS=====================================\\

    /**
     * The port on which the derby database server listens by default.
     * @see salssuite.server.Project
     */
    public static final int GENERAL_DATABASE_PORT = 45877;

    public static final String[] FORBIDDEN_CHARACTERS = {
        "\"",
        "'",
        "\\",
        ";"
    };


    //==============================PREFERENCES=================================

    static Preferences root = Preferences.userRoot();

    /**
     * The top level node for the programme. All the other nodes should be
     * subnodes of this one.
     */
    public static final Preferences programmeNode = root.node("SalSSuite");
    /**
     * The node for the <code>PreProjectDialog</code>.
     */
    public static final Preferences preProjectDialogNode = programmeNode.node(
            "PreProjectDialog");
    /**
     * The node for the <code>ServerGUI</code>.
     */
    public static final Preferences serverGUINode = programmeNode.node("ServerGUI");
    /**
     * The node for the <code>ProjectSetupDialog</code>.
     */
    public static final Preferences connectDialogNode = programmeNode.node("" +
           "ConnectDialog");
    /**
     * The node for the <code>DutyClient</code>.
     */
    public static final Preferences dutyClientNode = programmeNode.node("DutyClient");
    /**
     * The node for the <code>ServerSetupDialog</code>.
     */
    public static final Preferences serverSetupDialogNode = programmeNode.node(
            "ServerSetupDialog");
    /**
     * The node for the <code>MagazineClient</code>.
     */
    public static final Preferences magazineClientNode = programmeNode.node(
            "MagazineClient");
    /**
     * The node for the <code>AccountingClient</code>.
     */
    public static final Preferences accountingClientNode = programmeNode.node(
            "AccountingClient");

    public static final Preferences adminClientNode = programmeNode.node(
            "AdminClient");
    /**
     * The node for the <code>Installer</code>.
     * The installer stores the programme directory here so that the uninstaller
     * can use it to remove the SalSSuite.
     * @see salssuite.util.gui.Installer
     * @see salssuite.util.gui.Uninstaller
     */
    public static final Preferences installerNode = programmeNode.node("Installer");
    /**
     * The node for the <code>AccountManagingGUI</code>.
     */
    public static final Preferences accountManagingGUINode =
            programmeNode.node("AccountManagingGUI");
    /**
     * The node for the <code>EmploymentClient</code>.
     */
    public static final Preferences employmentClientNode = programmeNode.
            node("EmploymentClient");


//===============================FIELDS=======================================\\


    GregorianCalendar now;
    Project project;
    int currentDay = -1;
    boolean projectRunning;
    
    /*
     * The String from which subdirectories are created according to the
     * current day. This can be:
     * - pre-[timestamp] -- if the project is not yet running
     * - [currentDay] -- if the project is running
     * - post-[timestamp] -- if the project has already finished
     */
    String daySuffix;

    

    //================================THE FILES=================================
    /**
     * The top level programme directory, specified by the user during installation.
     * All directories should be subdirectories of this one.
     */
    public File programmeDir;
    /**
     * The general output path. All module output pathes should be subdirectories of
     * this directory. Output is created in a subdirectory with a timestamp, determined
     * by the member {@link #outputPath}.
     */
    public File generalOutputPath;
    /**
     * The output path with the current timestamp. All module output pathes should
     * be subdirectories of this path.
     */
    public File outputPath;
    /**
     * The general input path. All module input pathes should be subdirectories
     * of this directory.
     */
    public File inputPath;
    /**
     * The path where the server stores its backups. Appropriate subdirectories
     * with timestamps are created by the server, not by this class.
     * @see salssuite.server.Server#makeBackup
     */
    public File generalBackupPath;
    /**
     * The folder to store password files in.
     */
    public File generalPasswordPath;

    //==============================SERVER FILES================================
    /**
     * The directory for the server to store its data in if it is dependent on the
     * current day. For example, the logfiles should be stored in a separate
     * directory for each day.
     */
    public File serverDataPath;
    /**
     * The directory for the server to store its data in if it is independent
     * from the current day. E.g. the project data should not be generated every
     * day, but rather remain the same during the whole project unless the user
     * explicitely changes it.
     */
    public File generalServerDataPath;
    /**
     * The file in which a password for the initial server setup should be stored.
     * This file contains an MD5 hash of the password the user has entered
     * when installing the SalSSuite. The server uses it as a password
     * for user 'admin' when setting up the password database for a new project.
     * This file is only needed when creating a new project, afterwards it is
     * irrelevant.
     */
    public File serverPasswordFile;
    /**
     * The file from which the server imports the citizen data. This is a csv
     * file.
     */
    public File citizenInputFile;
    /**
     * The log file into which the server writes common messages.
     */
    public File serverLogFile;
    /**
     * The log file into which the server writes error messages.
     */
    public File serverErrorLogFile;
    /**
     * The directory containing all the server logs.
     */
    public File serverLogPath;
    /**
     * The file containing the project definition. An xml file by default.
     */
    public File projectDefFile;
    /**
     * The location where all project databases are stored. The Derby home
     * directory is set to be this.
     */
    public File generalDatabasePath;


    //================================DUTY FILES================================
    /**
     * The output directory of the duty module.
     */
    public File dutyOutputPath;
    /**
     * The input directory of the duty module. By default this is the same as
     * the server input directory.
     */
    public File dutyInputPath;
    /**
     * The file to which the duty module writes the citizens who were 'lazy'. This
     * means that these citizens have not passed enough time in the state,
     * according to the given attendance time (see {@link salssuite.server.Project}).
     */
    public File dutyLazyCitizensFile;
    /**
     * The file to which the duty module exports those citizens who were not
     * logged out when generating the output. This is useful at the end of a
     * day to see who did not care about logging, duty and such.
     */
    public File dutyNotLoggedOutFile;

    
    //===========================MAGAZINE FILES=================================

    /**
     * The path in which the input for the
     * {@link salssuite.server.module.MagazineModule} is placed.
     */
    public File magazineInputPath;
    /**
     * A csv file storing the goods available to the magazine as
     * provided by the user.
     */
    public File magazineGoodsInputFile;

//=============================CONSTRUCTORS===================================\\

    /**
     * Creates a new <code>Constance</code> object for the given programme root
     * directory and the given project.
     * @param programmeDir The directory in which the main binaries are located.
     * @param project The definition of the current project.
     */
    public Constants(File programmeDir, Project project) {

        this.project = project;

        setCurrentDay(0);

        this.inputPath = project.getInputPath();
        generalOutputPath = project.getOutputPath();
        this.programmeDir = programmeDir;
        
        recomputePaths();
    }


//===============================METHODS======================================\\

    /**
     * Checks whether the project is currently
     * running (that is, whether the current day is between the project's
     * start day and end day).
     * @return <code>true</code> if the project is running;<br/>
     * <code>false</code> if not.
     * @see salssuite.server.Project
     */
    public boolean isProjectRunning() {
        return projectRunning;
    }

    /**
     * Computes all the paths according to the current circumstances. This means
     * especially that output paths are regenerated with a new
     * timestamp.
     */
    public void recomputePaths() {
        outputPath = new File(generalOutputPath, makeTimeStamp());
        computeDependentPathes();
    }

    /**
     * Changes the project underlying this Constants object. Automatically
     * recomputes all paths according to the new circumstances.
     * @param project The new project.
     */
    public void changeProject(Project project) {
        this.project = project;
        outputPath = project.getOutputPath();
        inputPath = project.getInputPath();
        computeDependentPathes();
    }

    /**
     * Returns the current day of the project. If the project is not running
     * (which is, the start day is yet to come or the end day has already
     * passed), <code>-1</code> is returned.
     * @return The current day, or <code>-1</code> under the conditions explained
     * above.
     * @see salssuite.server.Project
     */
    public int getCurrentDay() {
        return currentDay;
    }

    /**
     * Returns a copy of this <code>Constants</code> object
     * with all paths updated for the given day.
     * @param day The day you'd like to have the constants for.
     * @return A <code>Constants</code> object for the given day.
     */
    public Constants getConstantsForDay(int day) {
        Constants clone = new Constants(programmeDir, project);

        clone.setCurrentDay(day);
        clone.computeDependentPathes();
        clone.recomputePaths();

        return clone;
    }

    /**
     * Sets the current day. If <code>0</code> is passed, computes the current day.
     * Paths are <b>not</b> updated automatically.
     */
    private void setCurrentDay(int day) {
        //determine which project day it is
        now = new GregorianCalendar();

        if(day > 0) {
            projectRunning = true;
            currentDay = day;
            daySuffix = String.valueOf("Tag"+Util.addZeroes(currentDay, 2));
            return;
        }

        //this is executed only if no day is given (day <= 0)
        
        if(now.after(project.getStartDay()) && project.getEndDay().after(now)) {
            projectRunning = true;
            
            currentDay = now.get(GregorianCalendar.DAY_OF_YEAR) -
                    project.getStartDay().get(GregorianCalendar.DAY_OF_YEAR) +1;
            daySuffix = String.valueOf("Tag"+Util.addZeroes(currentDay, 2));
        }
        else {
            projectRunning = false;
            currentDay = -1;

            if(project.getStartDay().after(now)) {
                daySuffix = "pre-"+now.get(GregorianCalendar.YEAR)+
                        Util.addZeroes((now.get(GregorianCalendar.MONTH)+1), 2)+
                        Util.addZeroes(
                        now.get(GregorianCalendar.DAY_OF_MONTH), 2);
            }
            else
                daySuffix = "post-"+now.get(GregorianCalendar.YEAR)+
                        Util.addZeroes((now.get(GregorianCalendar.MONTH)+1), 2)+
                        Util.addZeroes(now.get(GregorianCalendar.DAY_OF_MONTH), 2);
        }
    }//end setCurrentDay()

    /**
     * Computes all paths which are subdirectories of {@link #outputPath},
     * {@link #inputPath}, or {@link #programmeDir}.
     */
    private void computeDependentPathes() {
        //GENERAL
        generalBackupPath = new File(programmeDir, "/Backup/");
        generalPasswordPath = new File(programmeDir, "/Projekte/"+project.getProjectName()+
                "/passwords/");

        //SERVER
        serverDataPath = new File(programmeDir, "/Projekte/"+project.getProjectName()+
                "/"+daySuffix+"/");
        generalServerDataPath = new File(programmeDir, "/Projekte/"+project.getProjectName()+
                "/");
        serverLogPath = new File(serverDataPath, "/log/");
        serverLogFile = new File(serverLogPath, "/log.txt");
        serverErrorLogFile = new File(serverLogPath, "/errorlog.txt");
        projectDefFile = new File(generalServerDataPath, "/proj.xml");
        if(inputPath != null)
            citizenInputFile = new File(inputPath, "/buerger.csv");
        else
            citizenInputFile = null;
        serverPasswordFile = new File(programmeDir, "/install");
        generalDatabasePath = new File(generalServerDataPath, "/Datenbanken/");

        //DUTY
        dutyOutputPath = new File(outputPath, "/Zoll/");
        dutyInputPath = inputPath;
        dutyLazyCitizensFile = new File(dutyOutputPath, "/anwesenheitszeit_" +
                "unterschritten.csv");
        dutyNotLoggedOutFile = new File(dutyOutputPath, "/nicht_ausgeloggt.csv");
        

        //MAGAZINE
        magazineInputPath = inputPath;
        if(magazineInputPath != null)
            magazineGoodsInputFile = new File(magazineInputPath, "/waren.csv");
        else
            magazineGoodsInputFile = null;
    }

    /**
     * Generates a standardised timestamp for use with output
     * directories.
     * @return A timestamp.
     */
    private String makeTimeStamp() {
        String stamp = "";
        now = new GregorianCalendar();

        stamp += now.get(GregorianCalendar.YEAR);
        stamp += Util.addZeroes(now.get(GregorianCalendar.MONTH)+1, 2);
        stamp += Util.addZeroes(now.get(GregorianCalendar.DAY_OF_MONTH), 2);
        stamp += "_";
        stamp += Util.addZeroes(now.get(GregorianCalendar.HOUR_OF_DAY), 2);
        stamp += ":";
        stamp += Util.addZeroes(now.get(GregorianCalendar.MINUTE), 2);
        stamp += ":";
        stamp += Util.addZeroes(now.get(GregorianCalendar.SECOND), 2);
        return stamp;
    }

//===========================HANDLER METHODS==================================\\

}//end class
