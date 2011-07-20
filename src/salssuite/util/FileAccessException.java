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

package salssuite.util;

import java.io.File;

/**
 * Indicates that something went wrong while accessing a file. This exception
 * provides the common <code>Exception</code> methods and fields plus a constructor
 * to define the file which we tried to access when the error occured. That file's
 * absolute path is added to the exception message.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class FileAccessException extends Exception {

    private static final long serialVersionUID=1;

    File affectedFile;

    /**
     * Full constructor.
     * @param message The detail message.
     * @param affectedFile The affected file.
     * @throws NullPointerException if <code>affectedFile</code> is null.
     */
    public FileAccessException(String message, File affectedFile) {
        super(message+"\nFehlerquelle: Datei '"+affectedFile.getAbsolutePath()+
                "'.");
        this.affectedFile = affectedFile;
    }

    /**
     * Miscellaneous constructor. Creates a <code>FileAccessException</code> with
     * the given detail message.
     * @param message The detail message.
     */
    public FileAccessException(String message) {
        super(message);
    }

    /**
     * Returns the file which was affected by the <code>FileAccessException</code>.
     * @return The affected file, or <code>null</code> if there is none.
     */
    public File getAffectedFile() {
        return affectedFile;
    }
}//end class
