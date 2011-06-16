package salssuite.server.module;

import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JPanel;
import salssuite.server.Server;

/**
 * Module for the state's credit system. It is meant to implement a mechanism
 * for the state's bank using which it can lend citizens or companies money,
 * optionally with interest.
 * <p>
 * This module is responsible for setting up a database table that stores all
 * the credits. See {@link #buildDatabase}.
 * @author Jannis Limperg
 */
public class CreditModule implements Module {

    //==============================CONSTANTS=================================//

    /**
     * An identifier unique among the modules of the standard SalSSuite edition.
     * This can be used for example as a module name in the 'permissions'
     * database table.
     */
    public static final String NAME = "Kredite";

    //===============================FIELDS===================================//

    Server server;

    //============================CONSTRUCTORS================================//

    /**
     * Sole constructor.
     * @param server The <code>Server</code> this module is part of.
     */
    public CreditModule(Server server) {
        this.server = server;
    }

    //==============================METHODS===================================//
    
    //REQUIRED BY MODULE INTERFACE

    /**
     * Sets up a database table for the credit system.
     *
     * <p>
     *
     * Note that dates are stored as strings, not as <code>DATE</code> objects.
     * The <code>DATE</code> type caused some strange problems so I decided to
     * go with this not so elegant solution.
     * <p>
     * To create and parse standardised date strings, use
     * {@link salssuite.util.Util#getDateString} and
     * {@link salssuite.util.Util#parseDateString}.
     *
     * <p>
     *
     * <h3>Table Layout</h3>
     *
     * <pre>
     * <code>
     * CREATE TABLE "DBUSER"."CREDITS" (
     * "ID" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
     * "COMPANYID" INTEGER DEFAULT -1, //either of companyId or
     * "CITIZENID" INTEGER DEFAULT -1, //citizenId must be present
     * "AMOUNT" DECIMAL(10,2) NOT NULL,
     * "INTEREST" DECIMAL(10,2) NOT NULL DEFAULT 0, //means interest rate per day
     * "STARTDAY" VARCHAR(10) NOT NULL,
     * "ENDDAY" VARCHAR(10), //may be NULL to indicate unlimited credit
     * "PAID" INT DEFAULT 0, //0 means not paid, 1 means paid
     * PRIMARY KEY ("ID"))
     * </code>
     * </pre>
     * @throws Exception if some error occurs while setting up the database
     * (typically an SQlException).
     */
    public void buildDatabase() throws Exception {
        Connection dbcon = server.getDatabaseConnection();
        Statement stmt = dbcon.createStatement();

        stmt.executeUpdate("CREATE TABLE credits ("
                + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY,"
                + "companyId INT DEFAULT -1,"
                + "citizenId INT DEFAULT -1,"
                + "amount DECIMAL(10,2) NOT NULL,"
                + "interest DECIMAL(10,2) NOT NULL DEFAULT 0,"
                + "startDay VARCHAR(10) NOT NULL,"
                + "endDay VARCHAR(10),"
                + "paid INT DEFAULT 0,"
                + "PRIMARY KEY(id)"
                + ")");
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
     * Returns this module's name, which is equal to <code>CreditModule.NAME</code>.
     * @return The module's name.
     */
    public String getName() {
        return NAME;
    }

    /**
     * This module has no GUI. It can be accessed via the corresponding
     * {@link salssuite.clients.credits.CreditClient}.
     * @return <code>null</code>.
     */
    public JPanel getPanel() {
        return null;
    }

    //============================INNER CLASSES===============================//

}
