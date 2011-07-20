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
import salssuite.server.module.gui.CitizenModulePanel;

/**
 * Manages the state's citizens. This module does not construct its own
 * database tables as the server already does this. Instead, it provides a
 * panel for the {@link salssuite.server.gui.ServerGUI} to manage citizens.
 * @author Jannis Limperg
 * @version 1.0.1
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
