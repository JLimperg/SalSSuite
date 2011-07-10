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

package salssuite.server.module;

import javax.swing.JPanel;

/**
 * Dummy module for the {@link salssuite.clients.employment.EmploymentClient}.
 * This module is required to provide a dummy for the authentication mechanism,
 * otherwise it is completely irrelevant. The client only uses data from the
 * main server database.
 * @author Jannis Limperg
 * @version 1.0
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
