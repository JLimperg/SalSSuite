/*
 *  SalSSuite - Suite of programmes for managing a SalS project
 *  Copyright (C) 2011  Jannis Limperg <jannis[dot]limperg[at]arcor[dot]de>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package salssuite.server;

import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Timer;
import javax.swing.UIManager;
import salssuite.util.gui.ExceptionDisplayDialog;

/**
 * The <code>main()</code> method sets up a {@link Server} and launches it. In
 * case of an unexpected <code>Exception</code> thrown by the server (a server
 * crash if you want so) the Launcher tries to save the server data and let it
 * take as few damage as possible.
 * <p>
 * This class is the SalSSuite Server's Main Class.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class ServerLauncher {

//=================================CONSTANTS==================================//

//==================================FIELDS====================================//

    static Server server;

//===============================CONSTRUCTORS=================================//

    /**
     * Void constructor. This class only provides a <code>main()</code> method
     * to launch the server; creating objects of it is senseless.
     */
    public ServerLauncher() {

    }

//==================================METHODS===================================//

    /**
     * Creates a new {@link Server} object, provides the necessary security,
     * and launches it.
     * @param args <br/>-n | --no-gui<br/>
     * Does not load a GUI but starts a command line interface. See {@link Server}
     * for details.
     * <p>
     * -d | --daemonize<br/>
     * Disables stdout, stderr and stdin completely. The programme can then be
     * terminated only by sending the SIGTERM signal.
     * <p>
     * -f | --file [project definition file]
     * Indicates that the specified project definition
     * file (usually proj.xml) should be used rather
     * than asking the user for a project to load or
     * automatically taking the last opened project.
     * It the file does not exist or is no valid project
     * definition file as required by {@link Project#loadData}, the server will
     * exit.
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {}

        boolean noGUI = false;
        boolean daemonize = false;
        File projectFile = null;

        for(int ct = 0; ct < args.length; ct ++) {
            String arg = args[ct];
            if(arg.equals("-n") || arg.equals("--no-gui"))
                noGUI = true;
            else if(arg.equals("-d") || arg.equals("--daemonize"))
                daemonize = true;
            else if(arg.equals("-f") || arg.equals("--file")) {
                projectFile = new File(args[ct+1]);
                ct++;
            }
            else if(arg.equals("-h") || arg.equals("--help")) {
                System.out.println("The SalSSuite Server supports the following"
                        + " command line options:");
                System.out.println();
                System.out.println("-n | --no-gui");
                System.out.println("    Does not start the server's GUI but rather"
                        + " a command line interface for environments without"
                        + " GUI support.");
                System.out.println();
                System.out.println("-d | --daemonize");
                System.out.println("    Completely disables stdout, stdin and"
                        + " stderr. The server will attempt to load the"
                        + " project that was opened last. If this fails, it"
                        + " exits.");
                System.out.println();
                System.out.println("-f | --file [project definition file]");
                System.out.println("    Indicates that the specified project definition"
                        + " file (usually proj.xml) should be used rather"
                        + " than asking the user for a project to load or"
                        + " automatically taking the last opened project."
                        + " It the file does not exist or is no valid project"
                        + " definition file, the server will exit.");
                System.out.println("-h | --help");
                System.out.print("   Prints this help message.");
                System.exit(0);
            }
            else {
                System.out.println("Unsupported command line option: "+arg);
                System.out.println("See java -jar Server.jar -h for help.");
                System.exit(1);
            }
        }

        try {
        //create a Server object
        server = new Server(noGUI, daemonize, projectFile);

        //create the automatical backup feature
        final boolean noGUIFinal = noGUI;
        
        Thread backup = new Thread() {
            @Override
            public void run() {
                Timer time = new Timer(15*1000*60, new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        try {
                            server.makeBackup();
                        }
                        catch(Exception e) {
                            server.writeToErrorLog("LAUNCHER: Server failed making" +
                                    " backup.");
                            if(!noGUIFinal)
                                new ExceptionDisplayDialog(null, true, e, "KONNTE BACKUP " +
                                        "NICHT ERSTELLEN", 30).setVisible(true);
                        }
                    }
                });
                time.start();
            }
        };
        backup.setDaemon(true);

        server.writeToLog("LAUNCHER: Server up and running");
        server.writeToErrorLog("LAUNCHER: Server up and running");

        backup.start();
        }
        catch (Exception e) {
            if(!noGUI)
                new ExceptionDisplayDialog(null, true, e, "SERVER CRASH",30).setVisible(true);
            else if(!daemonize) {
                System.out.println("UNERWARTETER SERVER-FEHLER.");
                System.out.println(e.getMessage());
                e.printStackTrace();
                System.out.println("Versuche, herunterzufahren...");
            }

            try {
                server.writeToErrorLog("LAUNCHER: SERVER CRASH for unknown reason!");
                server.writeToErrorLog("LAUNCHER: Trying to shut server down...");
                server.shutdown();
            }
            catch (NullPointerException e2) {
                System.exit(1);
            }
            catch (Exception e3) {
                if(!noGUI)
                    new ExceptionDisplayDialog(null, true, e, "KONNTE SERVER NICHT" +
                            " HERUNTERFAHREN", 30).setVisible(true);
                else if(!daemonize) {
                    System.out.println("HERUNTERFAHREN NICHT MÖGLICH.");
                    System.out.println("Mit Datenverlust ist zu rechnen.");
                    System.out.println("Entschuldigen Sie bitte die"
                            + " Unannehmlichkeiten.");
                }
            }
        }
    }//end main()

//===============================HANDLER METHODS==============================//

}

