package salssuite.server.module;

import javax.swing.JPanel;

/**
 * Dummy module for the {@link salssuite.clients.employment.EmploymentClient}.
 * This module is required to provide a dummy for the authentication mechanism,
 * otherwise it is completely irrelevant. The client only uses data from the
 * main server database.
 * @author Jannis Limperg
 */
public class EmploymentModule implements Module {

    //==============================CONSTANTS=================================//

    public static final String NAME = "Arbeitsamt";

    //===============================FIELDS===================================//

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//
    
    // REQUIRED BY MODULE INTERFACE

    /**
     * Does nothing.
     * @throws Exception in no case.
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
     * Returns this module's name. See {@link Module#getName}.
     * @return The module's name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns nothing.
     * @return <code>null</code>.
     */
    public JPanel getPanel() {
        return null;
    }

    //============================INNER CLASSES===============================//

}
