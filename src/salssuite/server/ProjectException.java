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

/**
 * Indicates that something went wrong concerning the data of a
 * {@link Project}.
 * <p>
 * This exception provides no special methods or fields, it is merely there
 * to identify a special kind of problem not only semantically, but also
 * syntactically.
 * @author Jannis Limperg
 * @version 1.0
 */
public class ProjectException extends Exception {

    private static final long serialVersionUID=1;

    /**
     * Creates a new instance of <code>ProjectException</code> without a detail
     * message.
     */
    public ProjectException() {
    }


    /**
     * Creates an instance of <code>ProjectException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProjectException(String msg) {
        super(msg);
    }
}
