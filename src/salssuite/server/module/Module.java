package salssuite.server.module;

import javax.swing.JPanel;

/**
 * Defines a module of the SalSSuite server. This is an abstract set of
 * methods modules must overwrite to be part of the server. Most modules will
 * probably not use all methods, in that case they can simply be left void.
 * @author Jannis Limperg
 * @see salssuite.server.Server
 */
public interface Module {

//=================================CONSTANTS==================================//

//==================================METHODS===================================//

    /**
     * Sets up database tables this module might need. This method is generally
     * called by the server when setting up a new project. The server itself
     * has tables for citizens, companies, and account management, so these need
     * not be created by modules. See
     * {@link salssuite.server.Server#buildServerDatabase}.
     * <p>
     * If the module wants to import some data when a project is created, it can
     * do so in this method.
     * @throws Exception if something goes wrong while setting up the database
     * structure or importing required data.
     */
    public void buildDatabase() throws Exception;

    /**
     * Performs all actions that are necessary when the server shuts down. This
     * could be the saving of some data, the storing of preferences etc.
     * Output should <i>not</i> be generated in this method; the server calls
     * {@link #makeOutput} at shutdown automatically.
     */
    public void serverShutdown();

    /**
     * Generates output in the output directory. The latter is usually obtained
     * using {@link salssuite.util.Constants#outputPath}.
     */
    public void makeOutput();

    /**
     * Returns the module's name. This should be a unique identifier that is
     * localised so that it can be used both for displaying information to the
     * user and for programming issues. For example this method is used
     * by the authentication mechanism as a module identifier in the
     * 'permissions' table.
     * @return This module's name.
     */
    public String getName();

    /**
     * Returns this module's GUI. The panel returned here becomes part of
     * the {@link salssuite.server.gui.ServerGUI} at server startup. Most
     * modules will not return their module panels directly but
     * rather wrap them into a {@link salssuite.util.gui.PasswordPanel} to enable the server's
     * authentication mechanism.
     * <p>
     * If your module does not have a server-side GUI (in which case it will
     * probably have a client) you can return <code>null</code> here.
     * @return The GUI.
     */
    public JPanel getPanel();
}//end interface
