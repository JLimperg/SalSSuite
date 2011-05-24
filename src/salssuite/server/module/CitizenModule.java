package salssuite.server.module;

import javax.swing.JPanel;
import salssuite.util.gui.PasswordPanel;
import salssuite.server.Server;
import salssuite.server.module.gui.CitizenModulePanel;

/**
 * Manages the state's citizens. This module does not construct its own
 * database tables as the server already does this. Instead, it provides a
 * panel for the {@link salssuite.server.gui.ServerGUI} to manage citizens.
 * @author Jannis Limperg
 * @see salssuite.server.module.gui.CitizenModulePanel
 */
public class CitizenModule implements Module{

    //==============================CONSTANTS=================================//

    /**
     * An identifier unique among the modules of the standard SalSSuite edition.
     * This can be used for example as a module name in the 'permissions'
     * database table.
     */
    public static final String NAME = "Bürgerverwaltung";

    //===============================FIELDS===================================//

    Server server;

    //============================CONSTRUCTORS================================//

    /**
     * Sole constructor.
     * @param server The server this module belongs to.
     */
    public CitizenModule(Server server) {
        this.server = server;
    }

    //==============================METHODS===================================//

    /**
     * Does nothing. The citizen module operates with the general citizen
     * database constructed by the server.
     * @see salssuite.server.Server#buildServerDatabase
     */
    public void buildDatabase() {
    }

    /**
     * Does nothing, as no data must be saved.
     */
    public void serverShutdown() {
    }

    /**
     * Does nothing, as no output must be produced.
     */
    public void makeOutput() {
    }

    /**
     * Returns this module's name to be displayed to the user.
     * @return The module name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns this module's panel as a user GUI.
     * @return The module panel.
     * @see salssuite.server.module.gui.CitizenModulePanel
     */
    public JPanel getPanel() {
        server.getConstants().recomputePaths();
        return new PasswordPanel(server.getDatabaseConnection(), NAME,
                new CitizenModulePanel(server.getDatabaseConnection()),
                "Bürgerverwaltung");
    }

    //============================INNER CLASSES===============================//

}
