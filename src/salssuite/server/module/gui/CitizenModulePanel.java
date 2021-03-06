/*
 * CitizenModulePanel.java
 *
 * Created on 02.11.2010, 00:32:33
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

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import salssuite.util.TableModelUpdater;
import salssuite.util.Util;
import salssuite.util.gui.FilterPanel;

/**
 * GUI for the {@link salssuite.server.module.CitizenModule}. This panel
 * lets the user edit the citizens stored in the general server database
 * (table 'citizens').
 * <p>
 * The panel displays a <code>JTable</code> which exactly reflects the database
 * table. The user can edit all values except for IDs and company
 * IDs, moreover they can add and remove citizens.
 * @author Jannis Limperg
 * @version 1.0.1
 * @see salssuite.server.Server#buildServerDatabase
 */
public class CitizenModulePanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     * @param dbcon A connection to the server database.
     */
    public CitizenModulePanel(Connection dbcon) {
        initComponents();
        try {
            this.dbcon = dbcon;
            stmt = dbcon.createStatement();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                    + "Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //set up table model
        tableModel = (DefaultTableModel)table.getModel();
        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                if(evt.getType() == TableModelEvent.INSERT ||
                   evt.getType() == TableModelEvent.DELETE)
                    return;
                cellUpdated(evt.getFirstRow(), evt.getColumn());
            }
        });

        table.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                try {
                    int row = table.convertRowIndexToModel(evt.getFirstIndex());
                    originalForename = (String)tableModel.getValueAt(row,1);
                    originalSurname = (String)tableModel.getValueAt(row,2);
                    originalForm = (String)tableModel.getValueAt(row,3);
                }
                catch(IndexOutOfBoundsException e) {}
                //This exception occurs when all rows are removed from the
                //table model, in which case we don't need the originalXXX stuff
                //anyway.
            }
        });

        //generate filter panel
        try {
        filterPanel = new FilterPanel(null,
                    (int)filterPanelPlaceholder.getPreferredSize().getWidth(),
                    dbcon,
                    new String[] {"ID, forename, surname, form, companyId"},
                    "citizens",
                    "ID",
                    new String[] {"forename", "surname", "form"}, //string fields
                    new String[] {"ID", "companyId"}, //number fields
                    new String[] {}, //date fields
                    new String[] {"Vorname", "Nachname", "Klasse"}, //string field descr
                    new String[] {"ID", "Betriebs-ID"}, //number field descr
                    new String[] {}  //date field descr
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
               updateTableModel();
           }
        });

        filterPanelPlaceholder.setLayout(new java.awt.BorderLayout());
        filterPanelPlaceholder.add(filterPanel, java.awt.BorderLayout.CENTER);
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "serial"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        deleteCitizenButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        filterPanelPlaceholder = new javax.swing.JPanel();
        addCitizenButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(750, 500));

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Vorname", "Nachname", "Klasse", "Betriebs-Nr."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(table);

        deleteCitizenButton.setText("Bürger löschen");
        deleteCitizenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCitizen(evt);
            }
        });

        jLabel1.setText("Zum Bearbeiten eines Wertes auf eine Zelle doppelklicken, Wert eingeben und <Enter> drücken");

        filterPanelPlaceholder.setLayout(new java.awt.BorderLayout());

        addCitizenButton.setText("Bürger hinzufügen");
        addCitizenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCitizen(evt);
            }
        });

        refreshButton.setText("Aktualisieren");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonPressed(evt);
            }
        });

        jButton1.setText("Bürgerliste erstellen");
        jButton1.setToolTipText("Speichert eine Bürgerliste im CSV-Format.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateCitizenList(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filterPanelPlaceholder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addCitizenButton)
                        .addGap(18, 18, 18)
                        .addComponent(deleteCitizenButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(refreshButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterPanelPlaceholder, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCitizenButton)
                    .addComponent(deleteCitizenButton)
                    .addComponent(refreshButton)
                    .addComponent(jButton1))
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonPressed
        filterPanel.clearFilters();
    }//GEN-LAST:event_refreshButtonPressed

    private void addCitizen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCitizen
        try {
            //add to database
            stmt.executeUpdate("INSERT INTO citizens VALUES ("
                    + "DEFAULT, '', '', '', DEFAULT, DEFAULT, DEFAULT)");

            //add visual representation
            ResultSet newCitizen = stmt.executeQuery("SELECT id,companyId FROM"
                    + " citizens WHERE id = (SELECT MAX(ID) FROM citizens)");
            newCitizen.next();
            tableModel.addRow(new Object[]{newCitizen.getInt("id"), "", "", "",
                newCitizen.getInt("companyId")});
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                    + "Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            refreshButtonPressed(null);
            return;
        }

        //scroll down to the created citizen
        table.changeSelection(table.getRowCount()-1, 0, false, false);
    }//GEN-LAST:event_addCitizen

    private void deleteCitizen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCitizen
        //get rows to be deleted
        int[] deleteRows = table.getSelectedRows();

        //Prepare fields to store lists of already deleted and modified citizens
        //to be used for warning/error messages later.
        String deletedCitizensDescription = "";
        String modifiedCitizensDescription = "";
        
        try {
            //for each selected row:
            for(int ct = 0; ct < deleteRows.length; ct ++) {
                //get the citizen's ID
                int row = table.convertRowIndexToModel(deleteRows[ct]);
                int ID = (Integer)tableModel.getValueAt(row, 0);

                //check if the selected citizen has already been deleted
                ResultSet citizen = stmt.executeQuery("SELECT forename,surname,"
                        + "form FROM citizens WHERE id = "+ID);
                if(!citizen.next()) {
                    deletedCitizensDescription += "<br/>"+
                            (String)tableModel.getValueAt(row, 1) + " " +
                            (String)tableModel.getValueAt(row, 2) + " (Nr. "+
                            ID + ")";
                    continue;
                }

                //check if they have been modified in the meantime
                String forename = citizen.getString("forename");
                String surname = citizen.getString("surname");
                String form = citizen.getString("form");

                if(!(
                    forename.equals((String)tableModel.getValueAt(row, 1)) &&
                    surname.equals((String)tableModel.getValueAt(row, 2)) &&
                    form.equals((String)tableModel.getValueAt(row, 3))
                  )) {
                   modifiedCitizensDescription += "<br/>"+
                            citizen.getString("forename") + " " +
                            citizen.getString("surname") + " (Nr. "+
                            ID + ")";
                   deleteRows[ct] = -1;
                   continue;
                }

                //if both is not the case, update the database
                stmt.executeUpdate("DELETE FROM citizens WHERE id = "+ID);
            }

            //update the visual representation
            int deletedRows = 0;
            for(int row : deleteRows) {
                if(row != -1) {
                    tableModel.removeRow(row-deletedRows);
                    deletedRows ++;
                }
            }

            //print warning/error messages for modified/deleted citizens
            if(deletedCitizensDescription.length() != 0)
                JOptionPane.showMessageDialog(getTopLevelAncestor(),
                        "<html>Folgende Bürger wurden"
                        + " bereits von einem anderen Benutzer gelöscht:"+
                        deletedCitizensDescription + "</html>", "Information",
                        JOptionPane.INFORMATION_MESSAGE);

            if(modifiedCitizensDescription.length() != 0) {
                JOptionPane.showMessageDialog(getTopLevelAncestor(),
                        "<html>Folgende Bürger wurden zwischenzeitlich von einem "
                        + "anderen Benutzer modifiziert<br/>und werden deshalb "
                        + "nicht gelöscht:"+modifiedCitizensDescription
                        + "<br/>Aktualisiere die Daten...</html>",
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                refreshButtonPressed(null);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + "Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            refreshButtonPressed(null);
            return;
        }
            
    }//GEN-LAST:event_deleteCitizen

    private void generateCitizenList(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateCitizenList
        //ask the user for a file to store the data
        File destFile;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Csv-Dateien",
                "csv"));
        fileChooser.setDialogTitle("Bürgerliste speichern");
        int option = fileChooser.showSaveDialog(getTopLevelAncestor());
        if(option != JFileChooser.APPROVE_OPTION)
            return;
        destFile = fileChooser.getSelectedFile();

        //open the file and print the header
        final PrintWriter out;
        try {
            out = new PrintWriter(new java.io.FileWriter(destFile));
            out.println("\"Nummer\",\"Nachname\",\"Vorname\",\"Klasse\","
                    + "\"Betriebsnummer\"");
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
            ResultSet rowCount = stmt2.executeQuery("SELECT COUNT(*) FROM citizens");
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
        final ProgressMonitor monitor = new ProgressMonitor(
                (java.awt.Frame)getTopLevelAncestor(), "Erstelle die Liste...",
                null, 0, totalRows);

        //create the SwingWorker
        SwingWorker<Object, Integer> worker = new SwingWorker<Object, Integer>() {

            @Override
            protected Object doInBackground() throws Exception {
                //print the data
                try {
                    ResultSet data = stmt2.executeQuery("SELECT * FROM citizens "
                            + "ORDER BY id");
                    while(data.next()) {
                        if(monitor.isCanceled())
                            break;
                        
                        String line = "";
                        line += data.getInt("id") + ",";
                        line += "\"" + data.getString("surname") + "\",";
                        line += "\"" + data.getString("forename") + "\",";
                        line += "\"" + data.getString("form") + "\",";
                        line += data.getInt("companyId");
                        out.println(line);
                    }
                }
                catch(SQLException e) {
                    JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                            + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return null;
                }

                return null;
            }//end doInBackground()

            @Override
            protected void process(List<Integer> chunks) {
                monitor.setProgress(chunks.get(chunks.size()-1));
            }

            @Override
            protected void done() {
                out.flush();
                out.close();
            }

        };//end SwingWorker

        worker.execute();
    }//GEN-LAST:event_generateCitizenList


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCitizenButton;
    private javax.swing.JButton deleteCitizenButton;
    private javax.swing.JPanel filterPanelPlaceholder;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
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

    /* The following fields are updated each time a row is selected. They then
     * obtain all the cell values from that row and are later used to determine
     * if the row's original contents match the database table's corresponding
     * row's contents. This ensures that a user does not edit a citizen another
     * user has edited without the former knowing.
     */
    String originalForename = "";
    String originalSurname = "";
    String originalForm = "";

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Generates a new table model according to the data filtered by the
     * filterPanel.
     */
    private void updateTableModel() {
        //fetch information from filter panel
        ResultSet data = filterPanel.getFilteredData();

        //delete all rows from the table model
        tableModel.setRowCount(0);

        //Determine how many rows we have to process. Note that in case we
        //are processing filtered data, it is very likely that the number
        //of rows determined here does not correspond to the number
        //of rows that are actually in the ResultSet.
        int totalRows;
        Statement stmt2;
        try {
            stmt2 = dbcon.createStatement();
            ResultSet rowCount = stmt2.executeQuery("SELECT COUNT(*) FROM"
                    + " citizens");
            rowCount.next();
            totalRows = rowCount.getInt(1);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //fill the model
        new CitizenTableModelUpdater().update(data, totalRows);
    }

    /**
     * Reacts to a cell update by the user. This method updates the database
     * according to the user input.
     * @param row The row of the modified cell.
     * @param column The column of the modified cell.
     */
    private void cellUpdated(int row, int column) {
        //get ID of edited citizen
        int ID = (Integer)tableModel.getValueAt(row, 0);

        //get new value
        String newValue = tableModel.getValueAt(row, column).toString();

        //check if user attempts to use any prohibited characters
        if(!Util.checkInput(newValue))
            return;

        try {
            //check if this citizen has been edited or deleted by another user in
            //the meantime
            ResultSet citizen = stmt.executeQuery("SELECT forename, surname,"
                    + " form, companyId FROM citizens WHERE id = "+ID);
            if(!citizen.next()) {
                JOptionPane.showMessageDialog(getTopLevelAncestor(),
                        "<html>Dieser Bürger wurde"
                        + " zwischenzeitlich von einem anderen Benutzer"
                        + " gelöscht.</html>",
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                tableModel.removeRow(row);
                return;
            }

            if(!(
                originalForename.equals(citizen.getString("forename")) &&
                originalSurname.equals(citizen.getString("surname")) &&
                originalForm.equals(citizen.getString("form"))
              )) {
                JOptionPane.showMessageDialog(getTopLevelAncestor(),
                        "<html>Dieser Bürger wurde"
                        + " zwischenzeitlich von einem anderen Benutzer"
                        + " bearbeitet.<br/>Aktualisiere die Daten...</html>",
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                tableModel.removeRow(row);
                tableModel.insertRow(row, new Object[] {ID, citizen.getString("forename"),
                    citizen.getString("surname"), citizen.getString("form"),
                    citizen.getInt("companyId")});
                table.changeSelection(row, 0, false, false);
                return;
            }

            //update the database
            if(column == 1) { //forename change
                stmt.executeUpdate("UPDATE citizens SET forename = '"+newValue+
                        "' WHERE id = "+ID);
                originalForename = newValue;
            }
            else if(column == 2) { //surname change
                stmt.executeUpdate("UPDATE citizens SET surname = '"+newValue+
                        "' WHERE id = "+ID);
                originalSurname = newValue;
            }
            else if(column == 3) { //form change
                stmt.executeUpdate("UPDATE citizens SET form = '"+newValue+
                        "' WHERE id = "+ID);
                originalForm = newValue;
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            refreshButtonPressed(null);
            return;
        }
    }

    //============================INNER CLASSES===============================//

    private class CitizenTableModelUpdater extends TableModelUpdater {

        public CitizenTableModelUpdater() {
            super((java.awt.Frame)getTopLevelAncestor(), tableModel,
                    "Lade die Bürgerdaten...");
        }

        @Override
        public Object[] buildTableRow(ResultSet data) {
            Object[] row = new Object[5];

            try {
                row[0] = data.getInt("id");
                row[1] = data.getString("forename");
                row[2] = data.getString("surname");
                row[3] = data.getString("form");
                row[4] = data.getInt("companyId");
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog((java.awt.Frame)getTopLevelAncestor(),
                        "Fehler bei der Kommunikation mit der"
                        + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return null;
            }

            return row;
        }
    }
}
