/*
 * DBTransfer.java
 *
 * Created on 15.12.2010, 22:31:27
 */

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

package salssuite.util.gui;

import java.io.File;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.derby.drda.NetworkServerControl;
import salssuite.util.Util;

/**
 * Standalone programme that lets the user transfer data from one SalSSuite
 * database to another. Using the command line options, the user can specify
 * which database tables should be transfered and if the target database's
 * data should be replaced or kept.
 * <p>
 * The transfer client is useful when an update has introduced a new database table.
 * In that case, data from an 'old version' database can be transfered to
 * an empty 'new version' database so that no data has to be recreated manually.
 * In that case, run the client with cmd option "-r", specify all the tables that
 * should be transfered and select the 'old version' database as source
 * and the 'new version' database as target.
 * <p>
 * This programme is not part of the standard server-client-programmes but
 * rather a utility tool.
 * @author Jannis Limperg
 * @version 1.0
 * @see salssuite.server.module.AccountingModule#buildDatabase
 * @see #main
 */
public class DBTransfer extends javax.swing.JFrame {

    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     * @param tablesToBeTransfered Indices of those tables which are to be transfered.
     * Each index is assumed to correspond to an array index of the
     * {@link #TABLES} list.
     * @param replace Indicates that the data of the target database should
     * be replaced rather than appended to. This corresponds to the
     * <code>REPLACE</code> parameter of the
     * <code>SYSCS_UTIL.SYSCS_IMPORT_DATA</code> Derby system procedure.
     * See the <a href="http://db.apache.org/derby/docs/10.6/ref/">Derby
     * reference</a> for details.
     *
     */
    public DBTransfer(Integer[] tablesToBeTransfered, boolean replace) {
        initComponents();
        setLocation(200, 200);
        this.tablesToBeTransfered = tablesToBeTransfered;
        this.replace = replace;
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sourceDBLabel = new javax.swing.JLabel();
        targetDBLabel = new javax.swing.JLabel();
        choseSourcePathButton = new javax.swing.JButton();
        choseTargetPathButton = new javax.swing.JButton();
        mergeButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SalSSuite - Finanzdatenbanken zusammenführen");

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 18));
        jLabel1.setText("Quelldatenbank");

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 18));
        jLabel2.setText("Zieldatenbank");

        sourceDBLabel.setText("Bitte Ordner wählen");

        targetDBLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        targetDBLabel.setText("Bitte Ordner wählen");

        choseSourcePathButton.setText("Durchsuchen");
        choseSourcePathButton.setToolTipText("Den Quelldatenbank-Ordner wählen.");
        choseSourcePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseSourceDBPath(evt);
            }
        });

        choseTargetPathButton.setText("Durchsuchen");
        choseTargetPathButton.setToolTipText("Den Zieldatenbank-Ordner wählen.");
        choseTargetPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseTargetDBPath(evt);
            }
        });

        mergeButton.setText("Daten übertragen");
        mergeButton.setToolTipText("Buchhaltungs-Daten von der Quell- in die Zieldatenbank übertragen");
        mergeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                merge(evt);
            }
        });

        jLabel3.setText("<html><center>Die Datenbank-Ordner sind normalerweise [SalSSuite-Installationsverzeichnis]/Projekte/[Projekt]/Datenbanken/general</center></html>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sourceDBLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(targetDBLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(choseSourcePathButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 550, Short.MAX_VALUE)
                                .addComponent(choseTargetPathButton))
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(mergeButton)
                        .addGap(318, 318, 318))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {sourceDBLabel, targetDBLabel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceDBLabel)
                    .addComponent(targetDBLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choseSourcePathButton)
                    .addComponent(choseTargetPathButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(mergeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooseSourceDBPath(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseSourceDBPath
        File path = showFileChooser("Quelldatenbank wählen");
        if(path == null)
            return;
        sourceFolder = path;
        sourceDBLabel.setText(Util.adjustStringLength(path.getAbsolutePath(),
                40));
    }//GEN-LAST:event_chooseSourceDBPath

    private void chooseTargetDBPath(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseTargetDBPath
        File path = showFileChooser("Zieldatenbank wählen");
        if(path == null)
            return;
        targetFolder = path;
        targetDBLabel.setText(Util.adjustStringLength(path.getAbsolutePath(),
                40));
    }//GEN-LAST:event_chooseTargetDBPath

    //Note that the builtin derby functions seem to be case-sensitive about
    //everything, so give all paramteters in capital letters when using these
    //functions.
    private void merge(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_merge

        //test if pathes are set correctly
        if(sourceFolder == null || targetFolder == null) {
            JOptionPane.showMessageDialog(this, "Bitte Quell- und Zieldatenbank"
                    + " angeben.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //---------------------------SOURCE-----------------------------------\\


        //set up the temporary files to store the data in
        File[] tempFiles = new File[tablesToBeTransfered.length];

        try {
            for(int ct = 0; ct < tempFiles.length; ct++) {
                tempFiles[ct] = File.createTempFile(TABLES[tablesToBeTransfered[ct]], ".csv");
                tempFiles[ct].deleteOnExit();

                //Deletion is necessary because the derby routine used for export
                //won't export to existing files. We just need the pathes.
                tempFiles[ct].delete();
            }
        }
        catch(java.io.IOException e) {
            JOptionPane.showMessageDialog(this, "<html>Konnte temporäre Dateien für"
                    + " das Zwischenspeichern der Daten nicht erstellen.<p>"
                    + "Fehlermeldung: "+e.getMessage()+"</html>");
            return;
        }

        try {
            //start Derby server for source database
            System.setProperty("derby.system.home", sourceFolder.getParentFile().
                    getAbsolutePath());
            
            NetworkServerControl dbserver = new NetworkServerControl(
                InetAddress.getByName("0.0.0.0"),
                PORT);
            dbserver.start(null);

            //connect to it
            Connection dbcon = DriverManager.getConnection("jdbc:derby://localhost:" +
                    PORT +
                    "/"+sourceFolder.getName()+";");

            //export the data
            PreparedStatement pstmt = dbcon.prepareStatement(
                    "CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE("
                    + "'DBUSER',?,?,null,null,null)");

            for(int ct = 0; ct < tablesToBeTransfered.length; ct++) {
                pstmt.setString(1, TABLES[tablesToBeTransfered[ct]]);
                pstmt.setString(2, tempFiles[ct].getAbsolutePath());
                pstmt.executeUpdate();
            }

            //shutdown source db server
            try {
                DriverManager.getConnection("jdbc:derby:;" +
                        "shutdown=true;");
                dbserver.shutdown();
            }
                catch(SQLException e) {
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(this, "<html>Konnte Daten nicht aus"
                    + " der Quelldatenbank lesen.<p>Fehlermeldung: "+e.getMessage()
                    +"</html>",
                    "Schwerer Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        
        //---------------------------TARGET-----------------------------------\\
        
        try {
            //start Derby server for target database
            System.setProperty("derby.system.home", targetFolder.getParentFile().
                    getAbsolutePath());

            NetworkServerControl dbserver = new NetworkServerControl(
                InetAddress.getByName("0.0.0.0"),
                PORT);
            dbserver.start(null);

            //connect to it
            Connection dbcon = DriverManager.getConnection("jdbc:derby://localhost:" +
                    PORT +
                    "/"+targetFolder.getName()+";");

            //import the data from the export files
            PreparedStatement pstmt = dbcon.prepareStatement(
                    "CALL SYSCS_UTIL.SYSCS_IMPORT_DATA('DBUSER',?,"
                    + "?,?,?,null,null,null,?)");

            for(int ct = 0; ct < tablesToBeTransfered.length; ct ++) {
                pstmt.setString(1, TABLES[tablesToBeTransfered[ct]]);
                pstmt.setString(2, COLUMNS[tablesToBeTransfered[ct]]);
                pstmt.setString(3, INSERTPOSITIONS[tablesToBeTransfered[ct]]);
                pstmt.setString(4, tempFiles[ct].getAbsolutePath());
                pstmt.setString(5, TABLES[tablesToBeTransfered[ct]]);
                if(replace)
                    pstmt.setInt(5, 1);
                else
                    pstmt.setInt(5, 0);

                pstmt.executeUpdate();
            }

            //shutdown derby server
            try {
                DriverManager.getConnection("jdbc:derby:;" +
                        "shutdown=true;");
                dbserver.shutdown();
            }
                catch(SQLException e) {
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(this, "<html>Konnte Daten nicht in"
                    + " die Zieldatenbank schreiben.<p>Fehlermeldung: "+e.getMessage()
                    +"</html>",
                    "Schwerer Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //print success message
        JOptionPane.showMessageDialog(this, "Daten übertragen.", "Erfolg",
                JOptionPane.INFORMATION_MESSAGE);

        //bye
        System.exit(0);
    }//GEN-LAST:event_merge

    /**
     * Creates a <code>DBTransfer</code> and displays it.
     * @param args Each argument represents a table to be transfered. Only the
     * table names without the schema name are required. If no arguments are
     * present, all tables will be transfered.
     * <p>
     * By adding "-r" to the command line, the user indicates that the target
     * database's data should be replaced with that from the source database
     * rather than inserting the source data into the target database.
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {}

        //parse cmd arguments
        LinkedList<Integer> tablesToBeTransfered = new LinkedList<Integer>();
        boolean replace = false;

        if(args.length == 0) //merge all tables
            for(int ct = 0; ct < TABLES.length; ct++)
                tablesToBeTransfered.add(ct);
        else {
            forargs: for(String arg : args) {
                if(arg.equals("-r"))
                    replace = true;
                else {
                    for(int ct = 0; ct < TABLES.length; ct++) {
                        if(arg.toUpperCase().equals(TABLES[ct])) {
                            tablesToBeTransfered.add(ct);
                            continue forargs;
                        }
                    }
                    System.out.println("Table '"+arg+"' is unknown.");
                    return;
                }
            }
        }

        //create and display the transfer client
        new DBTransfer(tablesToBeTransfered.toArray(new Integer[0]),
                replace).setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton choseSourcePathButton;
    private javax.swing.JButton choseTargetPathButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton mergeButton;
    private javax.swing.JLabel sourceDBLabel;
    private javax.swing.JLabel targetDBLabel;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    /**
     * The port this programme uses to start the Derby database server.
     */
    public static final int PORT = 46789;

    /**
     * List of all database tables that can be transfered.
     * @see #COLUMNS
     * @see #INSERTPOSITIONS
     */
    public static final String[] TABLES = {
        "ORDERS", "REALPURCHASES", "CITIZENS", "ACCOUNTING", "COMPANIES",
        "ACCOUNTING_CATEGORIES", "PERMISSIONS", "LOGS", "GOODS", "ACCOUNTS",
        "ORDERPARTS", "CREDITS"
    };

    /**
     * For each database table that can be transfered, this constant holds a
     * string describing which columns can be transfered. These correspond
     * to the <code>INSERTCOLUMNS</code> parameter of the
     * <code>SYSCS_UTIL.SYSCS_IMPORT_DATA</code> Derby system procedure.
     * See the <a href="http://db.apache.org/derby/docs/10.6/ref/">Derby
     * reference</a> for details.
     * @see #TABLES
     * @see #INSERTPOSITIONS
     */
    public static final String[] COLUMNS = {
        "COMPANYID,DATE,TIME,PAID,PROCESSED", //ORDERS table
        "DATE,TIME,WAREID,PIECES,PRICEPERPIECE", //REALPURCHASES table
        "FORENAME,SURNAME,FORM,COMPANYID,SALARY,ISBOSS", //CITIZENS table
        "DESCRIPTION,DATE,TIME,INCOME,OUTGO,CATEGORY", //ACCOUNTING table
        "NAME,ROOM,PRODUCTDESCRIPTION,JOBS", //COMPANIES table
        "NAME", //ACCOUNTING_CATEGORIES table
        "MODULE,USERNAME", //PERMISSIONS table
        "CITIZENID,DATE,TIME,TYPE", //LOGS table
        "NAME,REALPRICE,FICTIVEPRICE,SELLER,PACKAGESIZE,PACKAGENAME,PACKAGEUNIT,"
                + "AVAILABLE", //GOODS table
        "USERNAME,HASH", //ACCOUNTS table
        "ORDERID,WAREID,PIECES", //ORDERPARTS table
        "COMPANYID,CITIZENID,AMOUNT,INTEREST,STARTDAY,ENDDAY,PAID" //CREDITS table
    };

    /**
     * For each database table that can be transfered, this constant holds a string
     * describing where the columns to be transfered should be inserted. These
     * correspond to the <code>COLUMNINDEXES</code> parameter of the
     * <code>SYSCS_UTIL.SYSCS_IMPORT_DATA</code> Derby system procedure.
     * See the <a href="http://db.apache.org/derby/docs/10.6/ref/">Derby
     * reference</a> for details.
     * @see #TABLES
     * @see #COLUMNS
     */
    public static final String[] INSERTPOSITIONS = {
        "2,3,4,5,6", //ORDERS table
        "1,2,3,4,5", //REALPURCHASES table
        "2,3,4,5,6,7", //CITIZENS table
        "2,3,4,5,6,7", //ACCOUNTING table
        "2,3,4,5", //COMPANIES table
        "1", //ACCOUNTING_CATEGORIES table
        "1,2", //PERMISSIONS table
        "1,2,3,4", //LOGS table
        "2,3,4,5,6,7,8,9", //GOODS table
        "1,2", //ACCOUNTS table
        "1,2,3", //ORDERPARTS table
        "2,3,4,5,6,7,8" //CREDITS table
    };

    //===============================FIELDS===================================//

    File sourceFolder;
    File targetFolder;

    Integer[] tablesToBeTransfered;
    boolean replace;


    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Utility method. Shows the user a customised JFileChooser to let them
     * choose a database path.
     * @return The chosen database path, or null if cancelled.
     */
    private File showFileChooser(String title) {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(title);
        chooser.setMultiSelectionEnabled(false);

        int option = chooser.showOpenDialog(this);

        if(option == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        else
            return null;
    }

    //============================INNER CLASSES===============================//
}
