/*
 * CompanyModulePanel.java
 *
 * Created on 10.05.2010, 13:01:02
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

package salssuite.server.module.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import salssuite.util.TableModelUpdater;
import salssuite.util.gui.ExceptionDisplayDialog;
import salssuite.util.gui.FilterPanel;

/**
 * Permits the user to edit the company data. This GUI provides only a simple
 * list of companies, the actual editing is performed by
 * {@link CompanyEditingDialog}s.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class CompanyModulePanel extends javax.swing.JPanel{

    private static final long serialVersionUID=1;

    /**
     * Sole constructor.
     * @param dbcon A connection to the server database.
     */
    public CompanyModulePanel(Connection dbcon) {
        
        initComponents();
        this.dbcon = dbcon;
        tableModel = (DefaultTableModel)table.getModel();

        //connect to the database
        try {
            stmt = dbcon.createStatement();
            parent = (javax.swing.JFrame)getTopLevelAncestor();
        }
        catch(ClassCastException e) {parent = new javax.swing.JFrame();}
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Konnte nicht mit der Datenbank" +
                    " verbinden. Beende die Andwendung.", "Kritischer Netzwerkfehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
            return;
        }

        //set up filter panel
        try {
            filterPanel = new FilterPanel(parent,
                    (int)filterDummyPanel.getPreferredSize().getWidth(),
                    dbcon,
                    new String[] {"id, name, room, productDescription"},
                    "companies",
                    "ID",
                    new String[] {"name", "room", "productDescription"}, //string fields
                    new String[] {"id"}, //number fields
                    new String[] {}, //date fields
                    new String[] {"Firmenname", "Raum", "Produktbezeichnung"}, //string field descr
                    new String[] {"ID"}, //number field descr
                    new String[] {}  //date field descr
                );
            }
        catch(SQLException e) {
            new ExceptionDisplayDialog(parent, true, e, "DATENBANK-FEHLER").
                    setVisible(true);
            System.exit(1);
            return;
        }
        
        filterPanel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateTableModel();
            }
        });
        filterDummyPanel.setLayout(new BorderLayout());
        filterDummyPanel.add(filterPanel, BorderLayout.CENTER);

        //update the list
        if(parent != null)
            parent.pack();
        validate();
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "serial"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterDummyPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        addCompanyButton = new javax.swing.JButton();
        editCompanyButton = new javax.swing.JButton();
        deleteCompanyButton = new javax.swing.JButton();
        generateListButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(750, 500));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        jLabel1.setText("Betrieb");

        addCompanyButton.setText("hinzufügen");
        addCompanyButton.setToolTipText("Fügt einen neuen Betrieb hinzu.");
        addCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCompany(evt);
            }
        });

        editCompanyButton.setText("bearbeiten");
        editCompanyButton.setToolTipText("Bearbeitet den gewählten Betrieb");
        editCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCompany_button(evt);
            }
        });

        deleteCompanyButton.setText("löschen");
        deleteCompanyButton.setToolTipText("Löscht den gewählten Betrieb.");
        deleteCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCompany(evt);
            }
        });

        generateListButton.setText("Liste erstellen");
        generateListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateCompanyList(evt);
            }
        });

        jButton1.setText("Aktualisieren");
        jButton1.setToolTipText("Synchronisiert alle Daten mit denen des Servers. Dies kann eine Weile dauern.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(addCompanyButton)
                .addGap(18, 18, 18)
                .addComponent(editCompanyButton)
                .addGap(18, 18, 18)
                .addComponent(deleteCompanyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(generateListButton)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCompanyButton)
                    .addComponent(jLabel1)
                    .addComponent(editCompanyButton)
                    .addComponent(deleteCompanyButton)
                    .addComponent(generateListButton)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Raum", "Produkt", "Gründer"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filterDummyPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterDummyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addCompany(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCompany
        //let user enter new company
        int newCompanyID = CompanyEditingDialog.showCompanyEditingDialog(parent, true,
                -1, dbcon);

        if(newCompanyID <= 0) //means the user has cancelled
            return;
        
        //update the GUI
        try {
            ResultSet company = stmt.executeQuery("SELECT * FROM companies"
                    + " WHERE id = "+newCompanyID);
            company.next();
            tableModel.addRow(new CompanyTableModelUpdater().buildTableRow(company));
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }//GEN-LAST:event_addCompany

    private void editCompany_button(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCompany_button
        editCompany();
    }//GEN-LAST:event_editCompany_button

    private void deleteCompany(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCompany

        //get selected company
        int selectedIndex = table.convertRowIndexToModel(table.getSelectedRow());

        if(selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Kein Betrieb zum Bearbeiten " +
                    "gewählt", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int ID = (Integer)tableModel.getValueAt(selectedIndex, 0);

        //confirm deletion
        int option = JOptionPane.showConfirmDialog(this, "Firma wirklich löschen? Diese Aktion " +
                "kann nicht rückgängig gemacht werden.", "Löschen bestätigen",
                JOptionPane.WARNING_MESSAGE);

        if(option != JOptionPane.YES_OPTION)
            return;

        //TODO it should be checked if another user has edited the selected
        //company in the meantime

        //delete
        try {
            stmt.executeUpdate("UPDATE citizens SET companyId = -1, isBoss = 0," +
                    "salary = 0 WHERE companyId = "+ID);
            stmt.executeUpdate("DELETE FROM companies WHERE id = "+ID);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation "
                    + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //update GUI
        tableModel.removeRow(selectedIndex);
    }//GEN-LAST:event_deleteCompany

    private void generateCompanyList(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateCompanyList

        //ask the user for a file to store the data
        File destFile;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Csv-Dateien",
                "csv"));
        fileChooser.setDialogTitle("Betriebsliste speichern");
        int option = fileChooser.showSaveDialog(getTopLevelAncestor());
        if(option != JFileChooser.APPROVE_OPTION)
            return;
        destFile = fileChooser.getSelectedFile();

        //open the file and print the header
        final PrintWriter out;
        try {
            out = new PrintWriter(new java.io.FileWriter(destFile));
            out.println("\"Nummer\",\"Name\",\"Produkt\",\"Raum\","
                    + "\"Gründer\",\"Angestellte\",\"freie Stellen\"");
            out.flush();
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(getTopLevelAncestor(), "Konnte gewählte"
                    + "Datei nicht öffnen", "Dateifehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //determine how many rows we will have to process
        int totalRows;
        final Statement stmt2;
        try {
            stmt2 = dbcon.createStatement();
            ResultSet rowCount = stmt2.executeQuery("SELECT COUNT(*) FROM"
                    + " companies");
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
        final ProgressMonitor monitor = new ProgressMonitor(parent, "Erstelle die"
                + " Betriebsliste...", null, 0, totalRows);

        //create the SwingWorker
        SwingWorker<Object, Integer> worker = new SwingWorker<Object, Integer>() {

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    Statement stmt3 = dbcon.createStatement();
                    ResultSet companies = stmt2.executeQuery("SELECT id,name,"
                            + "room,jobs,productDescription FROM companies ORDER BY id");

                    int rowsProcessed = 0;

                    while(companies.next()) {
                        if(monitor.isCanceled())
                            break;

                        LinkedList<String> rows = new LinkedList<String>();

                        int ID = companies.getInt("id");
                        int employeeCount = 0;

                        //generate the new row
                        String firstRow = "";
                        firstRow += ID + ",";
                        firstRow += "\"" + companies.getString("name").
                                replace("\"", "'") + "\",";
                        firstRow += "\"" + companies.getString("productDescription").
                                replace("\"", "'") + "\",";
                        firstRow += "\"" + companies.getString("room").
                                replace("\"", "'") + "\",";

                        //get the founder
                        ResultSet founder = stmt3.executeQuery("SELECT forename,surname"
                                + " FROM citizens WHERE companyId = "+ID+" AND"
                                + " isBoss = 1");
                        if(founder.next()) {
                            employeeCount ++;
                            String forename = founder.getString("forename").
                                    replace("\"", "'").
                                    split(" ")[0];
                            String surname = founder.getString("surname").
                                    replace("\"", "'");
                            firstRow += "\"" + forename + " " + surname + "\",";
                        }
                        else
                            firstRow += ",";

                        //get the employees
                        ResultSet employees = stmt3.executeQuery("SELECT forename,"
                                + "surname FROM citizens WHERE companyId = "+ID+" AND"
                                + " isBoss = 0 ORDER BY surname");
                        boolean firstEmployee = true;
                        while(employees.next()) {
                            employeeCount ++;
                            String forename = employees.getString("forename").
                                    replace("\"", "'").
                                    split(" ")[0];
                            String surname = employees.getString("surname").
                                    replace("\"", "'");
                            if(firstEmployee) {
                                firstRow += "\"" + forename + " " + surname + "\"";
                                firstEmployee = false;
                            }
                            else {
                                rows.add(",,,,,\"" + forename + " " + surname + "\"");
                            }
                        }

                        //add the number of free rows
                        firstRow += ","+(companies.getInt("jobs") - employeeCount);

                        //print everything
                        out.println(firstRow);
                        for(String row : rows)
                            out.println(row);

                        rowsProcessed ++;
                        publish(rowsProcessed);
                    }
                }
                catch(SQLException e) {
                    JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation mit der"
                            + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
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
    }//GEN-LAST:event_generateCompanyList

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        filterPanel.clearFilters();
    }//GEN-LAST:event_refreshButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCompanyButton;
    private javax.swing.JButton deleteCompanyButton;
    private javax.swing.JButton editCompanyButton;
    private javax.swing.JPanel filterDummyPanel;
    private javax.swing.JButton generateListButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Connection dbcon;
    Statement stmt;
    Frame parent;
    DefaultTableModel tableModel = new DefaultTableModel();
    FilterPanel filterPanel;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Permits the user to edit the currently selected company.
     */
    private void editCompany() {
        //get company
        int selectedIndex = table.convertRowIndexToModel(table.getSelectedRow());

        if(selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Kein Betrieb zum Bearbeiten " +
                    "gewählt", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int ID = (Integer)tableModel.getValueAt(selectedIndex, 0);

        //let user edit company
        CompanyEditingDialog.showCompanyEditingDialog(
                parent, true, ID, dbcon);

        //TODO it should be checked if another user has edited the company
        //in the meantime

        //update the GUI
        try {
            ResultSet company = stmt.executeQuery("SELECT * FROM companies"
                    + " WHERE id = "+ID);
            company.next();
            tableModel.removeRow(selectedIndex);
            tableModel.insertRow(selectedIndex, new CompanyTableModelUpdater().
                    buildTableRow(company));
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }

    /**
     * Updates the list model according to the current set of filters. This method
     * is called whenever the user modifies the filters.
     */
    private void updateTableModel() {
        //get filtered List
        ResultSet filteredData = filterPanel.getFilteredData();

        //erase all the old data
        tableModel.setRowCount(0);

        //Determine how many rows we have to process. Note that in case we
        //are processing filtered data, it is very likely that the number
        //of rows determined here does not correspond to the number
        //of rows that are actually in the ResultSet.
        int totalRows;
        try {
            Statement stmt2 = dbcon.createStatement();
            ResultSet rowCount = stmt2.executeQuery("SELECT COUNT(*) FROM"
                    + " companies");
            rowCount.next();
            totalRows = rowCount.getInt(1);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //construct new information
        new CompanyTableModelUpdater().update(filteredData, totalRows);
    }

    //============================INNER CLASSES===============================//

    private class CompanyTableModelUpdater extends TableModelUpdater {

        public CompanyTableModelUpdater() {
            super(parent, tableModel, "Lade die Betriebsdaten...");
        }

        @Override
        public Object[] buildTableRow(ResultSet data) {
            try {
                int ID = data.getInt("id");

                //get the founder
                String founder;
                Statement stmt2 = dbcon.createStatement();
                ResultSet citizens = stmt2.executeQuery("SELECT id,"
                        + " surname FROM citizens WHERE"
                        + " companyId = "+ID+" AND isBoss = 1");

                if(citizens.next())
                    founder = citizens.getString("id")+" " +
                            citizens.getString("surname");
                else
                    founder = "UNBEKANNT";

                //build the row
                Object[] row = new Object[5];
                row[0] = ID;
                row[1] = data.getString("name");
                row[2] = data.getString("room");
                row[3] = data.getString("productDescription");
                row[4] = founder;

                return row;
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation"
                        + "mit der Datenbank", "Netzwerkfehler",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return null;
            }
        }

    }
}
