package salssuite.server.module;

import javax.swing.JPanel;

/**
 * Fake module for the account management system. This module is needed to
 * provide an account permission category for the <code>AdminClient</code>, i.e.
 * for access to the server panels. Otherwise it is completely useless.
 * @author Jannis Limperg
 */
public class AdminModule implements Module {

    //==============================CONSTANTS=================================//

    public static final String NAME = "Administration";

    //===============================FIELDS===================================//

    //============================CONSTRUCTORS================================//

    public AdminModule() {

    }

    //==============================METHODS===================================//

    //REQUIRED BY MODULE INTERFACE

    /**
     * Does nothing.
     * @throws Exception never.
     */
    public void buildDatabase() throws Exception {
    }

    /**
     * Does nothing.
     */
    public void serverShutdown() {
    }

    /**
     * Does nothing.
     */
    public void makeOutput() {
    }

    /**
     * Returns this module's name. Equal to <code>AdminModule.NAME</code>.
     * @return This module's name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Return <code>null</code>.
     * @return <code>null</code>
     */
    public JPanel getPanel() {
        return null;
    }


    //============================INNER CLASSES===============================//

}
