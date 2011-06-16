/*
 * CreditClient.java
 *
 * Created on 02.06.2011, 00:01:19
 */

package salssuite.clients.credits;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import salssuite.clients.ConnectDialog;
import salssuite.server.module.CreditModule;
import salssuite.util.Constants;
import salssuite.util.Util;
import salssuite.util.gui.FilterPanel;
import salssuite.util.gui.HelpBrowser;

/**
 * Client used to access and manipulate the credit data. See the corresponding
 * module's documentation for details on the credit mechanism.
 * @author Jannis Limperg
 * @see salssuite.server.module.CreditModule
 */
public class CreditClient extends javax.swing.JFrame {

    //Serializaten not supported!
    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     */
    public CreditClient() {
        initComponents();

        //set up the table
        tableModel = (DefaultTableModel)table.getModel();
        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                if(evt.getType() == TableModelEvent.INSERT ||
                   evt.getType() == TableModelEvent.DELETE)
                    return;
                cellUpdated(evt.getFirstRow(), evt.getColumn());
            }
        });

        table.getColumnModel().getColumn(1).setCellEditor(new
                BorrowerColumnCellEditor(this));

        //usability
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Constants.creditClientNode.putInt("window.x", getX());
                Constants.creditClientNode.putInt("window.y", getY());
                System.exit(0);
            }
        });


        setLocation(Constants.creditClientNode.getInt("window.x", 200),
                Constants.creditClientNode.getInt("window.y", 200));
        setSize(Constants.creditClientNode.getInt("window.width", 960),
                Constants.creditClientNode.getInt("window.height", 600));

        //connect to the Server
        String[] theserver;
        while(true) {
            theserver = ConnectDialog.showConnectDialog(this, CreditModule.
                    NAME);

            if(theserver == null)
                System.exit(0);

            try {
                DriverManager.setLoginTimeout(10);
                dbcon = DriverManager.getConnection("jdbc:derby://"+
                        theserver[0] + ":" +
                        theserver[1] +
                        "/general;"+
                        "user="+theserver[2]+";"+
                        "password="+theserver[3]);
                stmt = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                break;
            }
            catch(SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Konnte nicht mit der Datenbank" +
                            " verbinden.",
                            "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                theserver = null;
                continue;
            }
        }//end while
        

        //generate filter panel
        try {
        filterPanel = new FilterPanel(null,
                    (int)filterPanelPlaceholder.getPreferredSize().getWidth(),
                    dbcon,
                    new String[] {"ID, companyId, citizenId, amount, interest,"
                            + "startDay, endDay", "paid"},
                    "credits",
                    new String[] {}, //string fields
                    new String[] {"ID", "companyId", "citizenId", "amount",
                                  "interest"}, //number fields
                    new String[] {"startDay", "endDay"}, //date fields
                    new String[] {}, //string field descr
                    new String[] {"ID", "Betriebs-ID", "Bürger-ID", "Betrag",
                                  "Zinsen"}, //number field descr
                    new String[] {"ausgezahlt am", "fällig am"}  //date field descr
                );
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Konnte keine Verbindung mit der" +
                    " Datenbank herstellen. Beende die Anwendung.",
                    "Kritischer Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
            return;
        }

        filterPanel.addActionListener(new ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent ev) {
               inputSent();
           }
        });

        filterPanelPlaceholder.setLayout(new java.awt.BorderLayout());
        filterPanelPlaceholder.add(filterPanel, java.awt.BorderLayout.CENTER);

        filterPanel.clearFilters();
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "serial"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        onlyOpenFilter = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        filterPanelPlaceholder = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        onlyDueFilter = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SalSSuite - Kredite");

        onlyOpenFilter.setText("zeige nur Offene");
        onlyOpenFilter.setToolTipText("Wenn diese Checkbox aktiviert ist, werden nur nicht bezahlte Kredite gezeigt.");
        onlyOpenFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableUpdateRequested(evt);
            }
        });

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Kreditnehmer", "Gesamtbetrag", "zurückgezahlt", "Startbetrag", "Zinssatz/Tag", "ausgezahlt am", "fällig am"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Boolean.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setToolTipText("Zum Bearbeiten eines Wertes auf eine Zelle doppelklicken, Wert eingeben und <Enter> drücken.");
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        filterPanelPlaceholder.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter"));

        javax.swing.GroupLayout filterPanelPlaceholderLayout = new javax.swing.GroupLayout(filterPanelPlaceholder);
        filterPanelPlaceholder.setLayout(filterPanelPlaceholderLayout);
        filterPanelPlaceholderLayout.setHorizontalGroup(
            filterPanelPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 924, Short.MAX_VALUE)
        );
        filterPanelPlaceholderLayout.setVerticalGroup(
            filterPanelPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        refreshButton.setText("Aktualisieren");
        refreshButton.setToolTipText("Aktualisiert die Liste der ausgegebenen Kredite.");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableUpdateRequested(evt);
            }
        });

        jButton1.setText("Neuer Kredit");
        jButton1.setToolTipText("Fügt einen Kredit hinzu.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCredit(evt);
            }
        });

        jButton2.setText("Kredit löschen");
        jButton2.setToolTipText("Löscht den gewählten Kredit unwiderruflich.");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCredit(evt);
            }
        });

        onlyDueFilter.setText("zeige nur Fällige");
        onlyDueFilter.setToolTipText("Wenn diese Checkbox aktiviert ist, werden nur Kredite angezeigt, die heute fällig sind oder bereits fällig waren.");
        onlyDueFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableUpdateRequested(evt);
            }
        });

        jLabel1.setText("Zum Bearbeiten eines Wertes auf die Zelle doppelklicken, neuen Wert eingeben und <Enter> drücken.");

        jButton3.setText("Hilfe!");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                help(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filterPanelPlaceholder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 936, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(onlyOpenFilter)
                        .addGap(18, 18, 18)
                        .addComponent(onlyDueFilter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 591, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 603, Short.MAX_VALUE)
                        .addComponent(refreshButton))
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(onlyOpenFilter)
                    .addComponent(onlyDueFilter)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterPanelPlaceholder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refreshButton)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableUpdateRequested(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableUpdateRequested
        filterPanel.clearFilters();
    }//GEN-LAST:event_tableUpdateRequested

    private void newCredit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCredit
        new AddCreditDialog(this, true, dbcon).setVisible(true);
        tableUpdateRequested(null);

        //scroll to the position of the new credit
        table.changeSelection(tableModel.getRowCount()-1, 0, false, false);
    }//GEN-LAST:event_newCredit

    private void removeCredit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCredit
        //get selected rows and IDs
        int[] rows = table.getSelectedRows();
        if(rows.length == 0)
            return;

        int[] IDs = new int[rows.length];
        for(int ct = 0; ct < rows.length; ct++)
            IDs[ct] = (Integer)tableModel.getValueAt(rows[ct], 0);

        //confirm deletion
        int option;
        if(rows.length == 1)
            option = JOptionPane.showConfirmDialog(this, "Kredit wirklich löschen?",
                    "Bestätigen", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        else
            option = JOptionPane.showConfirmDialog(this, rows.length + " Kredite"
                    + " wirklich löschen?", "Bestätigen", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        if(option != JOptionPane.YES_OPTION)
            return;

        //update database
        try {
            for(int ID : IDs)
                stmt.executeUpdate("DELETE FROM credits WHERE id = "+ID);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //remove rows from table
        for(int ct = 0; ct < rows.length; ct++)
            tableModel.removeRow(rows[ct]-ct);
    }//GEN-LAST:event_removeCredit

    private void help(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_help
        new HelpBrowser("CreditClient").setVisible(true);
    }//GEN-LAST:event_help

    /**
     * Creates a new <code>CreditClient</code> and displays it.
     * @param args Command line arguments not supported.
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {}

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreditClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel filterPanelPlaceholder;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox onlyDueFilter;
    private javax.swing.JCheckBox onlyOpenFilter;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Statement stmt;
    Connection dbcon;

    FilterPanel filterPanel;
    DefaultTableModel tableModel;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Updates the table model according to the filters currently applied
     * in the filter panel.
     */
    private void inputSent() {
        //erase all data in the table model
        tableModel.setRowCount(0);

        //obtain new data
        ResultSet data = filterPanel.getFilteredData();

        try {
            while(data.next()) {
                //if user only wants to see those credits which are not yet paid
                if(onlyOpenFilter.isSelected() && data.getInt("paid") == 1)
                    continue;

                //set basic data
                Object[] row = new Object[tableModel.getColumnCount()];
                row[0] = data.getInt("ID");

                int citizenID = data.getInt("citizenId");
                int companyID = data.getInt("companyId");

                if(citizenID < 0)
                    row[1] = "Betrieb "+companyID;
                else
                    row[1] = "Bürger "+citizenID;

                //compute current amount
                double amount;
                double originalAmount = data.getDouble("amount");
                double interest = data.getDouble("interest");
                String[] startDay = Util.parseDateString(data.getString("startDay"));
                GregorianCalendar startDayCal = new GregorianCalendar(
                        Integer.parseInt(startDay[0]),
                        Integer.parseInt(startDay[1])-1,
                        Integer.parseInt(startDay[2]));
                String[] endDay = Util.parseDateString(data.getString("endDay"));
                GregorianCalendar endDayCal = new GregorianCalendar(
                        Integer.parseInt(endDay[0]),
                        Integer.parseInt(endDay[1])-1,
                        Integer.parseInt(endDay[2]));
                GregorianCalendar today = new GregorianCalendar();

                //if user only wants to see credits that are to be paid today or
                //were to be paid earlier
                if(onlyDueFilter.isSelected() && !endDayCal.before(today))
                    return;

                if(today.before(startDayCal))
                    amount = originalAmount;
                else if(today.after(endDayCal)) {
                    int daysBetweenEndAndStart = 1;
                    while(endDayCal.after(startDayCal)) {
                        daysBetweenEndAndStart += 1;
                        startDayCal.add(GregorianCalendar.DAY_OF_MONTH, 1);
                    }
                    amount = originalAmount*Math.pow(1+interest/100, daysBetweenEndAndStart);
                }
                else { //current day is between startDay and endDay
                    int daysBetweenTodayAndStart = 0;
                    while(today.after(startDayCal)) {
                        daysBetweenTodayAndStart += 1;
                        startDayCal.add(GregorianCalendar.DAY_OF_MONTH, 1);
                    }
                    amount = originalAmount*Math.pow(1+interest/100, daysBetweenTodayAndStart);
                }

                //set rest of data
                row[2] = amount;
                if(data.getInt("paid") == 0)
                    row[3] = false;
                else
                    row[3] = true;
                row[4] = originalAmount;
                row[5] = interest;
                row[6] = data.getString("startDay");
                row[7] = data.getString("endDay");

                tableModel.addRow(row);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

    }

    /**
     * Responds to a cell value change by the user. They may freely edit cells
     * (except for the "ID" and the "total amount" columns) to manipulate the
     * credit data. This method is called each time a cell is edited in that way
     * and updates the database accordingly.
     * @param row The edited cell's row.
     * @param column The edited cell's column.
     */
    private void cellUpdated(int row, int column) {
        //get the ID of the changed credit
        int ID = (Integer)tableModel.getValueAt(row, 0);

        //get the new value
        Object newValue = tableModel.getValueAt(row, column);

        //update the database
        global: try {
            if(column == 1) { //column borrower
                String[] input = ((String)newValue).split(" ");
                String modifier = input[0];
                int citizenOrCompanyID = Integer.parseInt(input[1]);
                
                if(modifier.equals("Bürger")) {
                    //update database
                    stmt.executeUpdate("UPDATE credits SET citizenId = "+
                            citizenOrCompanyID + " WHERE id = "+ID);
                    stmt.executeUpdate("UPDATE credits SET companyId = -1"
                            + " WHERE id = "+ID);
                }
                else if(modifier.equals("Betrieb")) {
                    //update database
                    stmt.executeUpdate("UPDATE credits SET companyId = "+
                            citizenOrCompanyID + " WHERE id = "+ID);
                    stmt.executeUpdate("UPDATE credits SET citizenId = -1"
                            + " WHERE id = "+ID);
                }
            //end case 1
            }
            else if(column == 3) { //column paid back
                boolean input = (Boolean)newValue;
                if(input)
                    stmt.executeUpdate("UPDATE credits SET paid = 1 WHERE ID = "
                            + ID);
                else
                    stmt.executeUpdate("UPDATE credits SET paid = 0 WHERE ID = "
                            + ID);
            //end case 3
            }
            else if(column == 4 || column == 5) { //columns amount and interest
                double amountOrInterest;
                try {
                    amountOrInterest = (Double)newValue;
                    if(amountOrInterest < 0)
                        throw new NumberFormatException();
                }
                catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Bitte positive "
                            + "Fließkommazahl ('4'; '4,4'; '90,78' etc.) eingeben.",
                            "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                    break global;
                }

                if(column == 4)
                    stmt.executeUpdate("UPDATE credits SET amount = " +
                            amountOrInterest+" WHERE id = "+ID);
                else
                    stmt.executeUpdate("UPDATE credits SET interest = " +
                            amountOrInterest+" WHERE id = "+ID);
            //end case 4 | 5
            }
            else if(column == 6 || column == 7) { //columns endday and startday
                String dayInput = (String)newValue;
                String[] dayInputSplit;
                int year, month, day;
                GregorianCalendar dayInputCal;
                if(dayInput.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
                    dayInputSplit = dayInput.split("-");
                    year = Integer.parseInt(dayInputSplit[0]);
                    month = Integer.parseInt(dayInputSplit[1]);
                    day = Integer.parseInt(dayInputSplit[2]);
                }
                else if(dayInput.matches("\\d{1,2}.\\d{1,2}.\\d{4}")) {
                    dayInputSplit = dayInput.split("\\.");
                    year = Integer.parseInt(dayInputSplit[2]);
                    month = Integer.parseInt(dayInputSplit[1]);
                    day = Integer.parseInt(dayInputSplit[0]);
                }
                else {
                    JOptionPane.showMessageDialog(this, "Das Datum bitte im"
                            + " Format 'JJJJ-MM-TT' oder 'TT.MM.JJJJ' angeben.",
                            "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                    break global;
                }

                dayInputCal = new GregorianCalendar(year, month-1, day);

                if(column == 6) { //we are modifying startDay
                    stmt.executeUpdate("UPDATE credits SET startDay = '" +
                            Util.getDateString(dayInputCal) + "' WHERE id = "+ID);
                }
                else //we are modifying endDay
                    stmt.executeUpdate("UPDATE credits SET endDay = '" +
                            Util.getDateString(dayInputCal) + "' WHERE id = "+ID);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        tableUpdateRequested(null);
        table.changeSelection(row, column, false, false);
    }

    //============================INNER CLASSES===============================//

    /**
     * Custom cell editor for the table's "borrower" column. Lets the user
     * choose if they want to choose a company or a citizen as borrower
     * and lets them type in their ID.
     */
    private class BorrowerColumnCellEditor extends AbstractCellEditor implements
            TableCellEditor {

        //Serialization not supported!
        private static final long serialVersionUID = 1;


        //FIELDS

        JPanel cellPanel;
        JComboBox comboBox;
        JTextField IDInput;

        CreditClient parent;

        String previousValue = null;
        

        //CONSTRUCTOR
        public BorrowerColumnCellEditor(CreditClient parent) {
            this.parent = parent;

            cellPanel = new JPanel();
            comboBox = new JComboBox(new String[] {"Betrieb", "Bürger"});
            comboBox.setEditable(false);
            IDInput = new JTextField();
            IDInput.setColumns(4);
            cellPanel.add(comboBox);
            cellPanel.add(IDInput);
        }


        //METHODS REQUIRED BY TableCellEditor INTERFACE

        /**
         * Returns the cell's current value, which is a string representing
         * a certain citizen or company. It is formatted as follows:
         * "Bürger"|"Betrieb" [ID]
         * The company with ID 1 is therefore described by the string
         * "Betrieb 1".
         * @return
         */
        @Override
        public Object getCellEditorValue() {
            String currentValue = "";
            if(comboBox.getSelectedIndex() == 1)
                currentValue += "Bürger";
            else
                currentValue += "Betrieb";

            //check if ID is valid
            String IDString = IDInput.getText();
            int ID;
            
            try {
                ID = Integer.parseInt(IDString);
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Keine gültige ID im"
                        + " ID-Eingabefeld.", "Eingabefehler",
                        JOptionPane.ERROR_MESSAGE);
                return previousValue;
            }

            if(ID < 0) {
                JOptionPane.showMessageDialog(parent, "Die eingegebene ID"
                        + " muss positiv sein.", "Eingabefehler",
                        JOptionPane.ERROR_MESSAGE);
                return previousValue;
            }

            try {
                if(comboBox.getSelectedIndex() == 1) {
                    ResultSet citizen = stmt.executeQuery("SELECT id "
                            + "FROM citizens WHERE id = "+ID);
                    if(!citizen.next()) {
                        JOptionPane.showMessageDialog(parent, "Der gewählte"
                                + " Bürger existiert nicht.", "Eingabefehler",
                                JOptionPane.ERROR_MESSAGE);
                        return previousValue;
                    }
                }
                else {
                    ResultSet company = stmt.executeQuery("SELECT id "
                            + "FROM companies WHERE id = "+ID);
                    if(!company.next()) {
                        JOptionPane.showMessageDialog(parent, "Der gewählte"
                                + " Betrieb existiert nicht.", "Eingabefehler",
                                JOptionPane.ERROR_MESSAGE);
                        return previousValue;
                    }
                }
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation mit der"
                        + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return previousValue;
            }

            //if all tests succeeded, return the new value
            currentValue += " " + ID;
            return currentValue;
        }

        /**
         * Returns the component to display in the specified cell.
         * @param table The table for which the Component is intended.
         * @param value The current value of the cell.
         * @param isSelected If the cell is currently selected or not.
         * @param row The cell's row.
         * @param column The cell's column.
         * @return A JPanel containing a JComboxBox and a JTextField, the first
         * of which lets the user choose whether they want a citizen or a
         * company to be the borrower and the second of which lets them type
         * in the company's or citizen's ID.
         */
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {

            previousValue = (String)value;

            if(value == null) {
                comboBox.setSelectedIndex(0);
                IDInput.setText("");
            }
            else {
                String IDDescriptor = (String)value;
                if(IDDescriptor.startsWith("Bürger"))
                    comboBox.setSelectedIndex(1);
                else
                    comboBox.setSelectedIndex(0);

                IDInput.setText(IDDescriptor.split(" ")[1]);
            }

            table.getColumnModel().getColumn(column).setMinWidth(
                    (int)cellPanel.getPreferredSize().getWidth()
                  //  + (int)IDInput.getPreferredSize().getWidth()
                    );
            table.setRowHeight(row,
                    (int)cellPanel.getPreferredSize().getHeight());

            return cellPanel;
        }

    }
}
