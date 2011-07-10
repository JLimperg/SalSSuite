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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import salssuite.util.FileAccessException;
import salssuite.server.Server;

/**
 * Manages the magazine data. This module has no server-side
 * GUI but only a client that the user can use for data manipulation. The
 * abilities of this client are documented in the javadoc
 * of {@link salssuite.clients.magazine.MagazineClient} and its panels
 * (<code>MagazineClientXXXPanel</code>).
 * <p>
 * The module itself mainly constructs the relevant database tables.
 * It can also import data for an initial list of goods from a
 * csv file the user provides when the project is created. See
 * <code>{@link #buildDatabase}</code> for details on both operations.
 * @see salssuite.clients.magazine.MagazineClientOrdersPanel
 * @see salssuite.clients.magazine.MagazineClientShoppingListPanel
 * @see salssuite.clients.magazine.MagazineClientWarePanel
 * @author Jannis Limperg
 * @version 1.0
 */
public class MagazineModule implements Module {



    //==============================CONSTANTS=================================//

    /**
     * An identifier unique among the modules of the standard SalSSuite edition.
     * This can be used for example as a module name in the 'permissions'
     * database table.
     */
    public static final String NAME = "Warenlager";

    //===============================FIELDS===================================//

    Server server;
    Connection dbcon;
    Statement stmt;

    //============================CONSTRUCTORS================================//

    /**
     * Sole constructor.
     * @param server The server this module is part of.
     * @throws IllegalArgumentException if the given server is <code>null</code>.
     * @throws SQLException if a statement cannot be created for the server's
     * database connection.
     */
    public MagazineModule(Server server) throws IllegalArgumentException,
        SQLException {
        this.server = server;
        if(server == null)
            throw new IllegalArgumentException("Server may not be null!");

        dbcon = server.getDatabaseConnection();
        stmt = dbcon.createStatement();
    }

    //==============================METHODS===================================//

    //REQUIRED BY MODULE INTERFACE

    /**
     * Sets up the database tables and imports the goods data.
     * <p>
     * Note that date and time are stored as strings in all tables, not as
     * <code>DATE</code>
     * and <code>TIME</code> values respectively. These types caused some
     * strange problems so I decided to go with this not so elegant solution.
     * <p>
     * To create and parse standardised time strings, use
     * {@link salssuite.util.Util#getDateString}/{@link salssuite.util.Util#getTimeString} and
     * {@link salssuite.util.Util#parseDateString}/{@link salssuite.util.Util#parseTimeString}
     * respectively.
     *
     * <h3>Goods Table</h3>
     *
     * <code>CREATE TABLE "DBUSER"."GOODS" (<br/>
     * "ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),<br/>
     * "NAME" VARCHAR(1000) NOT NULL,<br/>
     * "REALPRICE" DECIMAL(5,2),<br/>
     * "FICTIVEPRICE" DECIMAL(5,2),<br/>
     * "SELLER" VARCHAR(1000),<br/>
     * "PACKAGESIZE" REAL,<br/>
     * "PACKAGENAME" VARCHAR(1000),<br/>
     * "PACKAGEUNIT" VARCHAR(20),<br/>
     * "AVAILABLE" INTEGER DEFAULT 0,<br/>
     * PRIMARY KEY ("ID"));
     * </code>
     *
     * <p>
     *
     * 'available' is the number of pieces that are available at the magazine
     * of this ware at the moment. 'seller' is the store where the ware is
     * (usually) bought.
     *
     * <p>
     *
     * The concept of packageSize, packageName and packageUnit is introduced
     * to provide a lot of flexibility when dealing with different packaging
     * mechanisms for various goods. It can probably be best explained with some
     * examples.
     * <p>
     * <b>Example 1:</b> Milk is sold in bottles of 1l capacity.<br/>
     * <code>packageName = "1 bottle"<br/>
     * packageSize = 1<br/>
     * packageUnit = "l"<br/></code>
     * <br/>
     * <b>Example 2:</b> Dried peas are sold in packages of 5 packs of 0.25kg.<br/>
     * <code>packageName = "5 packs"<br/>
     * packageSize = 0.25<br/>
     * packageUnit = "kg"<br/></code>
     *
     *
     * <h3>Order-related Tables</h3>
     *
     * <code>
     * CREATE TABLE "DBUSER"."ORDERS" (<br/>
     * "ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),<br/>
     * "COMPANYID" INTEGER,<br/>
     * "DATE" VARCHAR(10) NOT NULL,<br/>
     * "TIME" VARCHAR(8) NOT NULL,<br/>
     * "PAID" INTEGER DEFAULT 0,<br/>
     * "PROCESSED" INTEGER DEFAULT 0,<br/>
     * PRIMARY KEY ("ID"));
     * </code>
     *
     * <p>
     *
     * 'paid' and 'processed' provide information about the state of an order.
     * If it is marked 'processed', that means that the goods have been
     * delivered to the company and are not available at the magazine any more.
     * If it is marked 'paid', the company has already paid the goods.
     * An order is processed/paid if the corresponding value is <code>1</code>.
     *
     * <p>
     *
     * The goods a company has ordered in an order go to another table:
     *
     * <p>
     *
     * <code>
     * CREATE TABLE "DBUSER"."ORDERPARTS" (<br/>
     * "ORDERID" INTEGER NOT NULL,<br/>
     * "WAREID" INTEGER NOT NULL,<br/>
     * "PIECES" INTEGER NOT NULL,<br/>
     * PRIMARY KEY ("ORDERID", "WAREID"));
     * </code>
     *
     * <h3>Real Purchases Table</h3>
     *
     * The 'real' purchases the magazine makes (that is the purchases to get
     * the goods from 'real' shops) are stored mainly to enable the client to generate
     * a balance for all purchases. As you will see, they are stored on a
     * per-ware basis.
     *
     * <code>
     * CREATE TABLE "DBUSER"."REALPURCHASES" (<br/>
     * "DATE" VARCHAR(10) NOT NULL,<br/>
     * "TIME" VARCHAR(5) NOT NULL,<br/>
     * "WAREID" INTEGER NOT NULL,<br/>
     * "PIECES" INTEGER NOT NULL,<br/>
     * "PRICEPERPIECE" DECIMAL(5,2) NOT NULL,<br/>
     * PRIMARY KEY ("DATE", "TIME", "WAREID"));
     * </code>
     *
     * <h3>Data Import</h3>
     *
     * Goods data can be imported from
     * {@link salssuite.util.Constants#magazineGoodsInputFile}
     * because it is probably rather common to compile a list of all available
     * wares in Excel of OpenOffice before starting the project.
     * <p>
     * The file must contain all the goods data in csv format. Hereby the
     * file must be built up as follows:
     * <p>
     * <code>
     * "name",realPrice,fictivePrice,"seller",packageSize,"packageName","packageUnit"
     * </code>
     * <p>
     * Text must be enclosed in double quotes ("); values must be separated
     * by commas. Each line must be terminated with a newline character.
     * No spaces are permitted between values.<br/>
     * The first line of the file <i>must</i> contain a header
     * line. The contents of the header line are not important, it can even
     * be blank.
     * @throws Exception if some error occurs while constructing the database
     * or importing the data into it.
     */
    public void buildDatabase() throws Exception {

        //lay out the database

        server.writeToLog("MAGAZINE_DB: Constructing database.");

        stmt.executeUpdate("CREATE TABLE goods (" +
                "id INT NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "name VARCHAR(1000) NOT NULL," +
                "realPrice DECIMAL(10, 2)," +
                "fictivePrice DECIMAL(10, 2)," +
                "seller VARCHAR(1000)," +
                "packageSize FLOAT(23)," +
                "packageName VARCHAR(1000)," +
                "packageUnit VARCHAR(20)," +
                "available INT DEFAULT 0," +
                "PRIMARY KEY (id)" +
                ")");

        stmt.executeUpdate("CREATE TABLE orderParts (" +
                "orderId INT NOT NULL," +
                "wareId INT NOT NULL," +
                "pieces INT NOT NULL," +
                "PRIMARY KEY (orderId, wareId)" +
                ")");

        stmt.executeUpdate("CREATE TABLE orders (" +
                "id INT NOT NULL GENERATED ALWAYS AS IDENTITY," +
                "companyId INT," +
                "date VARCHAR(10) NOT NULL," + //note that date and time are stored
                "time VARCHAR(8) NOT NULL," + // as simple strings and cannot be
                                              // accessed via getDate(). The formats
                                              // used are yyyy-mm-dd and hh:mm:ss.
                "paid INT DEFAULT 0," +
                "processed INT DEFAULT 0," +
                "PRIMARY KEY(id)" +
                ")");

        stmt.executeUpdate("CREATE TABLE realPurchases (" +
                "date VARCHAR(10) NOT NULL," +
                "time VARCHAR(8) NOT NULL," +
                "wareId INT NOT NULL," +
                "pieces INT NOT NULL," +
                "pricePerPiece DECIMAL(5, 2) NOT NULL," +
                "PRIMARY KEY (date, time, wareId)" +
                ")");

        server.writeToLog("MAGAZINE_DB: Done constructing database.");

        //load user provided data
        importDataFromCsv();
    }

    /**
     * Does nothing. Output should be created using a client.
     */
    public void makeOutput() {
    }

    /**
     * Does nothing, as no tasks need to be performed at server shutdown.
     */
    public void serverShutdown() {

    }

    /**
     * Returns this module's name.
     * @return The module's name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * This panel does not have a GUI. Instead, it should be accessed through
     * the appropriate client.
     * @return <code>null</code>.
     */
    public JPanel getPanel() {
        return null;
    }

    //DATA MANAGEMENT

    /**
     * Loads the list of wares from the input directory if the user has
     * provided any data. This method should be called only if this is the
     * first call of the project.
     */
    private void importDataFromCsv() throws FileAccessException  {

        //if the user does not want to import anything, the input path
        //is not set
        if(server.getConstants().inputPath == null)
            return;
        
        File inputFile = server.getConstants().magazineGoodsInputFile;

        //if the input file does not exist, display a warning message
        //and return
        if(!inputFile.exists()) {
            if(!server.isNoGUI())
                JOptionPane.showMessageDialog(server.getGUI(), "<html>Die Eingabedatei"
                        + " für Warendaten"
                        + " existiert nicht.<p>Warendaten werden nicht"
                        + " importiert.<p>("
                        + inputFile.getAbsolutePath()+")</html>",
                        "Fehler beim Importieren", JOptionPane.WARNING_MESSAGE);
            else
                System.out.println("Warnung: Die Eingabedatei für Warendaten"
                        + " existiert nicht. Warendaten werden nicht importiert. ("
                        + inputFile.getAbsolutePath()+")");
            return;
        }

        

        //set connection up
        Scanner in;
        try {
            in = new Scanner(new java.io.FileReader(inputFile));
        }
        catch(IOException e) {
            throw new FileAccessException("Konnte Datei nicht lesen.", inputFile);
        }


        //parse data

        in.nextLine();
        while(in.hasNextLine()) {

            String line = in.nextLine();
            String[] fields = line.split(",");
            if(fields.length > 8)
                throw new FileAccessException("Datenreihe mit zu vielen Werten entdeckt." +
                        " Möglicherweise ein Komma in einem der Werte?", inputFile);


            try {
                stmt.executeUpdate("INSERT INTO goods (name, realPrice," +
                        "fictivePrice, seller, packageSize, packageName," +
                        "packageUnit) VALUES (" +
                        "'"+fields[1].replaceAll("\"", "") + "', " +
                        Double.parseDouble(fields[2]) + ", " +
                        Double.parseDouble(fields[6]) + ", " +
                        "'"+fields[7].replaceAll("\"", "") + "', " +
                        Integer.parseInt(fields[4]) + ", " +
                        "'"+fields[3].replaceAll("\"", "") + "' ," +
                        "'"+fields[5].replaceAll("\"", "") + "')"
                        );
            }
            catch(NumberFormatException e) {
                throw new FileAccessException("Konnte numerischen Wert nicht ermitteln." +
                        " Möglicherweise ein falscher Wert in einem numerischen" +
                        " Feld? Abbruch der Operation.");
            }
            catch(SQLException e) {
                throw new FileAccessException("Fehler bei der Kommunikation mit" +
                        " der Datenbank.");
            }
        }
    }
}

    
