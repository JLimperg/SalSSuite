package salssuite.server.module;

import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JPanel;
import salssuite.server.Server;

/**
 * Manages the accounting of the project. This module cares
 * for the 'real' money only; it is not a module for the state's bank.
 * <p>
 * The module is entirely controlled by its client; it does not have a GUI
 * itself. Creating output is done by the client as well. This module merely
 * has the purpose to construct some database tables related to accounting.
 * For their layout, see {@link #buildDatabase}.
 * <p>
 * The accounting is done in the form of 'accounting entries'. Each entry
 * represents one transaction which can be positive (adding money to the project's
 * bank account) or negative (requiring money to be spent). These entries
 * also contain a description of the transaction and the date and time they
 * are performed on.<br/>
 * Entries can be organised using user-defined categories. Each entry must be
 * assigned a category.
 * @author Jannis Limperg
 * @see salssuite.clients.accounting.AccountingClient
 */
public class AccountingModule implements Module {

    //==============================CONSTANTS=================================//

    /**
     * An identifier unique among the modules of the standard SalSSuite edition.
     * This can be used for example as a module name in the 'permissions'
     * database table.
     */
    public static final String NAME = "Buchhaltung";

    //===============================FIELDS===================================//

    Server server;

    //============================CONSTRUCTORS================================//

    /**
     * Sole constructor.
     * @param server The server this module is part of.
     */
    public AccountingModule(Server server) {
        this.server = server;
    }

    //==============================METHODS===================================//
    
    //REQUIRED BY MODULE INTERFACE

    /**
     * Constructs two tables, one for accounting entries and one for the categories.
     * 
     * <h3>Accounting Table</h3>
     *
     * Note that date and time are stored as strings, not as <code>DATE</code>
     * and <code>TIME</code> values respectively. These types caused some
     * strange problems so I decided to go with this not so elegant solution.
     * <p>
     * To create and parse standardised time strings, use
     * {@link salssuite.util.Util#getDateString}/{@link salssuite.util.Util#getTimeString} and
     * {@link salssuite.util.Util#parseDateString}/{@link salssuite.util.Util#parseTimeString}
     * respectively.
     *
     * <p>
     *
     * <code>
     * CREATE TABLE "DBUSER"."ACCOUNTING" (<br/>
     * "ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),<br/>
     * "DESCRIPTION" VARCHAR(2000) NOT NULL,<br/>
     * "DATE" VARCHAR(10) NOT NULL,<br/>
     * "TIME" VARCHAR(8) NOT NULL,<br/>
     * "INCOME" DOUBLE,<br/>
     * "OUTGO" DOUBLE,<br/>
     * "CATEGORY" VARCHAR(1000) NOT NULL,<br/>
     * PRIMARY KEY ("ID"));
     * </code>
     *
     * <h3>Categories Table</h3>
     *
     * A list of categories which can be chosen by the user.
     *
     * <p>
     *
     * <code>
     * CREATE TABLE "DBUSER"."ACCOUNTING_CATEGORIES" (<br/>
     * "NAME" VARCHAR(1000) NOT NULL,<br/>
     * PRIMARY KEY ("NAME"));
     * </code>
     * @throws Exception  if some error occurs while setting up the database
     * (typically an SQlException).
     */
    public void buildDatabase() throws Exception {
        Connection dbcon = server.getDatabaseConnection();
        Statement stmt = dbcon.createStatement();

        stmt.executeUpdate("CREATE TABLE accounting ("
                + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                + "description VARCHAR(2000) NOT NULL,"
                + "date VARCHAR(10) NOT NULL,"
                + "time VARCHAR(8) NOT NULL,"
                + "income DOUBLE,"
                + "outgo DOUBLE,"
                + "category VARCHAR(1000) NOT NULL,"
                + "PRIMARY KEY (id)"
                + ")");

        stmt.executeUpdate("CREATE TABLE accounting_categories ("
                + "name VARCHAR(1000) NOT NULL,"
                + "PRIMARY KEY (name)"
                + ")");
    }

    /**
     * Does not do anything.
     */
    public void serverShutdown() {
    }

    /**
     * Does not do anything. Output can be generated using the corresponding
     * client.
     */
    public void makeOutput() {
    }

    /**
     * Returns this module's name.
     * @return The name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Returns <code>null</code>. This module has no server-side GUI.
     * @return <code>null</code>.
     */
    public JPanel getPanel() {
        return null;
    }

    //============================INNER CLASSES===============================//

}
