package salssuite.server.module;

import javax.swing.JPanel;
import salssuite.util.gui.PasswordPanel;
import salssuite.server.Server;
import salssuite.server.module.gui.CompanyModulePanel;

/**
 * Manages the companies in the state. Analogue to the {@link CitizenModule},
 * it does not create its own database tables and it does not have a client.
 * Instead it only provides a panel for the {@link salssuite.server.gui.ServerGUI}
 * which the companies can be managed with.
 * @author Jannis Limperg
 * @see salssuite.server.module.gui.CompanyModulePanel
 */
public class CompanyModule implements Module{

    //==============================CONSTANTS=================================//

    /**
     * An identifier unique among the modules of the standard SalSSuite edition.
     * This can be used for example as a module name in the 'permissions'
     * database table.
     */
    public static final String NAME = "Betriebe";

    //===============================FIELDS===================================//

    Server server;

    //============================CONSTRUCTORS================================//

    /**
     * Sole constructor.
     * @param server The server this module belongs to.
     */
    public CompanyModule(Server server) {
        this.server = server;
    }



    //==============================METHODS===================================//
    
    //REQUIRED BY MODULE INTERFACE

    /**
     * Does nothing, as the database tables are built by the server.
     * @see salssuite.server.Server#buildServerDatabase
     */
    public void buildDatabase() {
        
    }

    /**
     * Does nothing, as there are no tasks to be performed at server shutdown.
     */
    public void serverShutdown() {

    }


    /**
     * Does nothing, as no output is to be generated.
     */
    public void makeOutput() {
        
    }

    /**
     * Returns this module's name by which the user can identify it.
     * @return The module's name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns this module's GUI.
     * @return The module's panel.
     * @see salssuite.server.module.gui.CompanyModulePanel
     */
    public JPanel getPanel() {
        return new PasswordPanel(server.getDatabaseConnection(), NAME,
                new CompanyModulePanel(server.getDatabaseConnection()), "Betriebe");
    }

    //============================INNER CLASSES===============================//

}
