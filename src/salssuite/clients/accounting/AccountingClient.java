package salssuite.clients.accounting;


import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import salssuite.util.Constants;
import salssuite.clients.ConnectDialog;
import salssuite.server.module.AccountingModule;
import salssuite.util.TableModelUpdater;
import salssuite.util.gui.FilterPanel;
import salssuite.util.gui.HelpBrowser;

/*
 * AccountingClient.java
 *
 * Created on 11.12.2010, 02:19:03
 */

/**
 * Client for the accounting module. Using this client, the user can manage
 * all incomes and expenditures in real currency before or during
 * the project.<p>
 * The incomes and expenditures can be organised in user-defined categories.
 * The client can produce some output in human-readable csv format that is
 * well suited for printing.
 * @author Jannis Limperg
 * @see salssuite.server.module.AccountingModule
 */
public class AccountingClient extends javax.swing.JFrame {

    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     */
    public AccountingClient() {
        initComponents();
        this.tableModel = (DefaultTableModel)table.getModel();

        int port = -1;
        String serverAddress = null;

        //connect to server
        do {

            String[] server = ConnectDialog.showConnectDialog(this,
                    AccountingModule.NAME);
            serverAddress = server[0];
            port = Integer.parseInt(server[1]);


            try {
                DriverManager.setLoginTimeout(10);
                dbcon = DriverManager.getConnection("jdbc:derby://"+
                        serverAddress + ":" +
                        port +
                        "/general;"+
                        "user="+server[2]+";"+
                        "password="+server[3]);

                stmt = dbcon.createStatement();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Konnte nicht mit der Datenbank" +
                        " verbinden.",
                        "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                serverAddress = null;
                e.printStackTrace();
                continue;
            }
        } while(serverAddress == null);


        //add GUI elements
        try {
            filterPanel = new FilterPanel(this,
                    (int)filterPanelPlaceholder.getPreferredSize().getWidth(),
                    dbcon,
                    new String[] {"*"},
                    "accounting",
                    "ID",
                    new String[] {"category"}, //string fields
                    new String[] {"id", "outgo", "income"}, //number fields
                    new String[] {"date"}, //date fields
                    new String[] {"Kategorie"}, //string field descr
                    new String[] {"ID", "Ausgaben", "Einnahmen"}, //number field descr
                    new String[] {"Datum"}  //date field descr
            );
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Konnte Daten nicht initialisieren." +
                    " Beende die Anwendung...",
                    "Kritischer Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        filterPanelPlaceholder.add(filterPanel, java.awt.BorderLayout.CENTER);

        filterPanel.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTableModel(filterPanel.getFilteredData());
            }
        });

        pack();

        //usability tweaks
        addWindowListener(new WindowAdapter() {
            @Override
           public void windowClosing(java.awt.event.WindowEvent evt) {
               Preferences node = Constants.accountingClientNode;
               node.putInt("window.x", getX());
               node.putInt("window.y", getY());
               node.putInt("window.width", getWidth());
               node.putInt("window.height", getHeight());
           }
        });

        Preferences node = Constants.accountingClientNode;
        setLocation(node.getInt("window.x", 300), node.getInt("window.y", 200));
        setSize(node.getInt("window.width", (int)getPreferredSize().getWidth()),
                node.getInt("window.height", (int)getPreferredSize().getHeight()));

        //data initialization
        filterPanel.clearFilters();
        updateCurrentSaldoDisplay();
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "serial"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterPanelPlaceholder = new javax.swing.JPanel();
        exportButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        manageCategoriesButton = new javax.swing.JButton();
        newEntryButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        currentSaldoDisplay = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SalSSuite - Buchhaltung");

        filterPanelPlaceholder.setPreferredSize(new java.awt.Dimension(733, 1));
        filterPanelPlaceholder.setLayout(new java.awt.BorderLayout());

        exportButton.setText("Exportieren");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export(evt);
            }
        });

        refreshButton.setText("Aktualisieren");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        manageCategoriesButton.setText("Kategorien");
        manageCategoriesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageCategories(evt);
            }
        });

        newEntryButton.setText("Neuer Eintrag");
        newEntryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newEntryButtonActionPerformed(evt);
            }
        });

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Beschreibung", "Datum", "Uhrzeit", "Kategorie", "Einnahmen", "Ausgaben", "Ktostand"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editEntry(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        jLabel1.setText("Zum Bearbeiten eines Eintrags auf die Tabellenzeile doppelklicken.");

        jLabel2.setText("Aktueller Kontostand:");

        currentSaldoDisplay.setFont(new java.awt.Font("Ubuntu", 1, 15));

        jButton1.setText("Hilfe!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                help(evt);
            }
        });

        jButton2.setText("Eintrag löschen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEntry(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(filterPanelPlaceholder, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(703, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentSaldoDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(newEntryButton)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 632, Short.MAX_VALUE)
                                .addComponent(manageCategoriesButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(exportButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 771, Short.MAX_VALUE)
                                .addComponent(refreshButton))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(261, 261, 261)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(931, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newEntryButton)
                    .addComponent(manageCategoriesButton)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exportButton)
                    .addComponent(refreshButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(currentSaldoDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(filterPanelPlaceholder, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        filterPanel.clearFilters();
        updateCurrentSaldoDisplay();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void newEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newEntryButtonActionPerformed
        int newEntryID = AccountingEntryDialog.showAccountingEntryDialog(this, true,
                dbcon);

        //update the GUI
        try {
            ResultSet entry = stmt.executeQuery("SELECT * FROM accounting WHERE"
                    + " id = "+newEntryID);
            if(entry.next())
                tableModel.addRow(new AccountingTableModelUpdater(this).buildTableRow(
                        entry));
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        updateCurrentSaldoDisplay();
    }//GEN-LAST:event_newEntryButtonActionPerformed

    private void editEntry(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editEntry
        if(evt.getClickCount() < 2)
            return;

        //get selected entry
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        if(row < 0)
            return;

        int ID = (Integer)tableModel.getValueAt(row, 0);

        //let user edit it
        AccountingEntryDialog.showAccountingEntryDialog(
                this, true, dbcon, ID);

        //update the model
        try {
            ResultSet entry = stmt.executeQuery("SELECT * FROM accounting WHERE"
                    + " id = "+ID);
            entry.next();

            tableModel.removeRow(row);
            tableModel.insertRow(row, new AccountingTableModelUpdater(this).buildTableRow(
                    entry));
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        updateCurrentSaldoDisplay();
    }//GEN-LAST:event_editEntry

    private void manageCategories(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageCategories
        new CategoryManagingDialog(this, true, dbcon).setVisible(true);
    }//GEN-LAST:event_manageCategories

    private void export(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export

        //ask user for export file
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Kommagetrennte Textdateien (csv)","csv"));
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setDialogTitle("Datei für die Ausgabe wählen");
        chooser.setFileHidingEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);

        int option = chooser.showSaveDialog(this);

        if(option != JFileChooser.APPROVE_OPTION)
            return;

        File exportFile = chooser.getSelectedFile();


        //connect to the exportFile
        final PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(exportFile));
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(this, "Konnte nicht in die Datei '"
                    + exportFile+"' schreiben.<p>Fehlermeldung: "+e.getMessage(),
                    "Dateifehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //determine how many rows we have to process
        int totalRows;
        final Statement stmt2;
        try {
            stmt2 = dbcon.createStatement();
            ResultSet rowCount = stmt2.executeQuery("SELECT COUNT(*) FROM"
                    + " accounting");
            rowCount.next();
            totalRows = rowCount.getInt(1);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //create the ProgressMonitor
        final ProgressMonitor monitor = new ProgressMonitor(this, "Exportiere...",
                null, 0, totalRows);

        //-------------------------print everything---------------------------\\

        SwingWorker<Object, Integer> worker = new SwingWorker<Object, Integer>() {

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    //SECTION 1: Overview
                    out.println("\"ÜBERSICHT\"");
                    out.println();

                    out.print("\"Gesamtausgaben\",");
                    ResultSet totalOut = stmt2.executeQuery("SELECT SUM(outgo) AS"
                            + " totalOut FROM accounting");
                    totalOut.next();
                    out.format("%1$s%2$.2f%3$s", "\"", totalOut.getDouble("totalOut"),
                            "\",");
                    out.println();

                    out.print("\"Gesamteinnahmen\",");
                    ResultSet totalIn = stmt2.executeQuery("SELECT SUM(income) AS"
                            + " totalIn FROM accounting");
                    totalIn.next();
                    out.format("%1$s%2$.2f%3$s", "\"", totalIn.getDouble("totalIn"),
                            "\",");
                    out.println();

                    out.print("\"Saldo\",");
                    ResultSet saldo = stmt2.executeQuery("SELECT SUM(income + outgo)"
                            + "AS saldo FROM accounting");
                    saldo.next();
                    out.format("%1$s%2$.2f%3$s", "\"", saldo.getDouble("saldo"),
                            "\",");
                    out.println();

                    out.println();
                    out.println();

                    //SECTION 2: Category Overview
                    out.println("KATEGORIEN-ÜBERSICHT");
                    out.println();

                    out.println("\"Kategorie\",\"Gesamtausgaben\",\"Gesamteinnahmen\"");

                    ResultSet categories = stmt2.executeQuery("SELECT DISTINCT"
                            + " category FROM accounting ORDER BY category");

                    Statement stmt3 = dbcon.createStatement();
                    while(categories.next()) {
                        String category = categories.getString("category");

                        out.print("\""+category+"\",");

                        ResultSet outgo = stmt3.executeQuery("SELECT SUM(outgo) AS"
                                + " totalOut FROM accounting WHERE category = '"+
                                category+"'");
                        outgo.next();
                        out.format("%1$s%2$.2f%3$s", "\"", outgo.getDouble("totalOut"),
                                "\",");

                        ResultSet income = stmt3.executeQuery("SELECT SUM(income) AS"
                                + " totalIn FROM accounting WHERE category = '"+
                                category+"'");
                        income.next();
                        out.format("%1$s%2$.2f%3$s", "\"", income.getDouble("totalIn"),
                                "\",");
                        out.println();
                    }

                    out.println();
                    out.println();

                    //SECTION 3: Details
                    out.println("DETAILS");
                    out.println();

                    out.println("\"Datum\",\"Uhrzeit\",\"Beschreibung\","
                            + "\"Kategorie\",\"Ausgaben\",\"Einnahmen\",\"Kontostand\"");

                    ResultSet details = stmt2.executeQuery("SELECT * FROM accounting "
                            + "ORDER BY date, time");

                    int rowsProcessed = 0;
                    while(details.next()) {
                        if(monitor.isCanceled())
                            break;

                        out.print("\""+details.getString("date")+"\",");
                        out.print("\""+details.getString("time")+"\",");
                        out.print("\""+details.getString("description")+"\",");
                        out.print("\""+details.getString("category")+"\",");
                        out.format("%1$s%2$.2f%3$s", "\"", details.getDouble("outgo"),
                                "\",");
                        out.format("%1$s%2$.2f%3$s", "\"", details.getDouble("income"),
                                "\",");

                        ResultSet temporarySaldo = stmt3.executeQuery("SELECT "
                                + "SUM(outgo + income) AS saldo FROM accounting"
                                + " WHERE date < '"+details.getString("date")+"' OR"
                                + " (date = '"+details.getString("date")+"' AND"
                                + " time <= '"+details.getString("time")+"')");
                        temporarySaldo.next();
                        out.format("%1$s%2$.2f%3$s", "\"", temporarySaldo.
                                getDouble("saldo"), "\",");
                        out.println();

                        rowsProcessed ++;
                        publish(rowsProcessed);
                    }

                    done();
                }
                catch(SQLException e) {
                    JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                            + " Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return null;
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null, "Fehler beim Schreiben in die Datei:"
                            + "<p>"+e.getMessage(), "Dateifehler",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return null;
                }

                return null;
            } //end doInBackground()

            @Override
            protected void process(List<Integer> chunks) {
                monitor.setProgress(chunks.get(chunks.size()-1));
            }

            @Override
            protected void done() {
                out.flush();
                out.close();
            }

        }; //end SwingWorker
        
        worker.execute();
    }//GEN-LAST:event_export

    private void help(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_help
        new HelpBrowser("AccountingClient").setVisible(true);
    }//GEN-LAST:event_help

    private void deleteEntry(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEntry
        //get selected entry
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        if(row < 0)
            return;

        int ID = (Integer)tableModel.getValueAt(row, 0);

        //confirm the deletion
        int option = JOptionPane.showConfirmDialog(this, "<html>Eintrag wirklich"
                + " löschen?<br/>Diese Aktion kann nicht rückgängig gemacht"
                + " werden.</html>", "Löschen bestätigen",
                JOptionPane.WARNING_MESSAGE);
        if(option != JOptionPane.YES_OPTION)
            return;

        /*
         * TODO It should be checked if the chosen entry has been modified
         * by another user in the meantime.
         */

        try {
            //delete the entry from the database
            stmt.executeUpdate("DELETE FROM accounting WHERE id = "+ID);

            //update the model
            tableModel.removeRow(row);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        updateCurrentSaldoDisplay();
    }//GEN-LAST:event_deleteEntry

    /**
     * Displays an <code>AccountingClient</code>.
     * @param args Command line arguments are not supported.
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        }

        new AccountingClient().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel currentSaldoDisplay;
    private javax.swing.JButton exportButton;
    private javax.swing.JPanel filterPanelPlaceholder;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton manageCategoriesButton;
    private javax.swing.JButton newEntryButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Connection dbcon;
    Statement stmt;

    FilterPanel filterPanel;
    DefaultTableModel tableModel;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Updates the table model according to the data given in the rs ResultSet.
     * @param rs A result set representing the accounting data.
     */
    private void updateTableModel(ResultSet rs) {
        //Delete all old rows from the model
        tableModel.setRowCount(0);

        //Determine how many rows we have to process. Note that in case we
        //are processing filtered data, it is very likely that the number
        //of rows determined here does not correspond to the number
        //of rows that are actually in the ResultSet.
        int totalRows;
        try {
            ResultSet rowCount = stmt.executeQuery("SELECT COUNT(*) FROM"
                    + " accounting");
            rowCount.next();
            totalRows = rowCount.getInt(1);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //construct new rows
        new AccountingTableModelUpdater(this).update(rs, totalRows);
        

        //do layout stuff
        Font font = table.getGraphics().getFont();
        FontMetrics fontMetrics = table.getGraphics().getFontMetrics(font);


        int col = 1;
        int columnWidth = 0;

        //set preferred width of the "description" column
        //to width of longest item in that column
        for(int row = 0; row < tableModel.getRowCount(); row++) {
            String item = tableModel.getValueAt(row, col).toString();
                
            int itemWidth = fontMetrics.stringWidth(item);
            if(columnWidth < itemWidth)
                columnWidth = itemWidth;
        }

        table.getColumn(table.getColumnName(col)).
                setPreferredWidth(columnWidth);
    }

    /**
     * Fetches the current saldo from the database and displays it.
     */
    private void updateCurrentSaldoDisplay() {

        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        try {
            ResultSet saldo = stmt.executeQuery("SELECT SUM(outgo + income)"
                    + "AS saldo FROM accounting");
            saldo.next();
            currentSaldoDisplay.setText(
                    String.format("%1$.2f", saldo.getDouble("saldo"))+" €");
        }
        catch(SQLException e) {
            currentSaldoDisplay.setText("FEHLER");
        }
        finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    //============================INNER CLASSES===============================//

    private class AccountingTableModelUpdater extends TableModelUpdater {

        public AccountingTableModelUpdater(AccountingClient client) {
            super(client, tableModel, "Lade die Einträge...");
            this.client = client;
        }

        AccountingClient client;

        @Override
        public Object[] buildTableRow(ResultSet data) {
            try {
                Object[] rowData = new Object[8];
                Statement stmt2 = dbcon.createStatement();

                rowData[0] = data.getInt("id");
                rowData[1] = data.getString("description");
                rowData[2] = data.getString("date");
                rowData[3] = data.getString("time");
                rowData[4] = data.getString("category");

                double income = data.getDouble("income");
                if(income == 0.0)
                    rowData[5] = "";
                else
                    rowData[5] = String.format("%1$.2f", data.getDouble("income"));

                double outgo = data.getDouble("outgo");
                if(outgo == 0.0)
                    rowData[6] = "";
                else
                    rowData[6] = String.format("%1$.2f", data.getDouble("outgo"));

                String thisDate = data.getString("date");
                String thisTime = data.getString("time");
                ResultSet saldo = stmt2.executeQuery("SELECT SUM(outgo + income) "
                        + "AS saldo "
                        + "FROM accounting "
                        + "WHERE date < '"+thisDate+"' OR "
                        + "(date = '"+thisDate+"' AND time <= '"+thisTime+"')");
                saldo.next();
                rowData[7] = String.format("%1$.2f", saldo.getDouble("saldo"));

                return rowData;
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(client, "Fehler bei der Kommunikation mit der"
                        + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return null;
            }
        }
    }
}
