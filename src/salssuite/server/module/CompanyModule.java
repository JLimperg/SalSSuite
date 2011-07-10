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
import salssuite.util.gui.PasswordPanel;
import salssuite.server.Server;
import salssuite.server.module.gui.CompanyModulePanel;

/**
 * Manages the companies in the state. Analogue to the {@link CitizenModule},
 * it does not create its own database tables and it does not have a client.
 * Instead it only provides a panel for the {@link salssuite.server.gui.ServerGUI}
 * which the companies can be managed with.
 * @author Jannis Limperg
 * @version 1.0
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
