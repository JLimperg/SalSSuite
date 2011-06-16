/*
 * MagazineClientWarePanel.java
 *
 * Created on 05.06.2010, 14:17:43
 */

package salssuite.clients.magazine;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import salssuite.util.Util;
import salssuite.util.gui.FilterPanel;

/**
 * Panel to manage the list of wares available at the magazine. Provides direct
 * access to all the data stored in the 'goods' database table. Wares can be
 * created, deleted, and modified using a {@link WareEditingDialog}.
 * <p>
 * For the properties of each ware, see
 * {@link salssuite.server.module.MagazineModule#buildDatabase}.
 * @author Jannis Limperg
 * @see MagazineClient
 * @see salssuite.server.module.MagazineModule
 */
public class MagazineClientWarePanel extends javax.swing.JPanel {

    private static final long serialVersionUID=1;

    /**
     * Sole constructor.
     * @param client The client this panel is part of.
     */
    public MagazineClientWarePanel(MagazineClient client){
        initComponents();

        this.client = client;

        
        try {
            //connect to the client
            stmt = client.getDatabaseConnection().createStatement();
        

            //setup filter panel
            filterPanel = new FilterPanel(client,
                    (int)filterPanelPlaceholder.getPreferredSize().getWidth(),
                    client.getDatabaseConnection(),
                    new String[] {"*"},
                    "goods",
                    "id",
                    new String[] {"name", "seller"}, //string fields
                    new String[] {"id", "realPrice", "fictivePrice", "available"}, //number fields
                    new String[] {}, //date fields
                    new String[] {"Bezeichnung", "Händler"}, //string field descr
                    new String[] {"ID", "Preis(€)", "Preis(Staat)", "vorhanden"}, //number field descr
                    new String[] {}  //date field descr
            );
                        
            filterPanel.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filterEntered();
                }
            });
            filterPanelPlaceholder.setLayout(new BorderLayout());
            filterPanelPlaceholder.add(filterPanel, BorderLayout.CENTER);

            //fetch data
            updateTableModel(filterPanel.getFilteredData());
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(client, "Konnte nicht mit der" +
                    "Datenbank verbinden. Beende die Anwendung.",
                    "Kritischer Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
            return;
        }
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        filterPanelPlaceholder = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        newWareButton = new javax.swing.JButton();
        editWareButton = new javax.swing.JButton();
        deleteWareButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(730, 630));

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(666, 234));

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout filterPanelPlaceholderLayout = new javax.swing.GroupLayout(filterPanelPlaceholder);
        filterPanelPlaceholder.setLayout(filterPanelPlaceholderLayout);
        filterPanelPlaceholderLayout.setHorizontalGroup(
            filterPanelPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 730, Short.MAX_VALUE)
        );
        filterPanelPlaceholderLayout.setVerticalGroup(
            filterPanelPlaceholderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );

        refreshButton.setText("Aktualisieren");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        newWareButton.setText("Neue Ware");
        newWareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNewWare(evt);
            }
        });

        editWareButton.setText("Ware bearbeiten");
        editWareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editWare(evt);
            }
        });

        deleteWareButton.setText("Ware löschen");
        deleteWareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteWare(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newWareButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editWareButton)
                .addGap(18, 18, 18)
                .addComponent(deleteWareButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 248, Short.MAX_VALUE)
                .addComponent(refreshButton)
                .addContainerGap())
            .addComponent(filterPanelPlaceholder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterPanelPlaceholder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newWareButton)
                    .addComponent(editWareButton)
                    .addComponent(deleteWareButton)
                    .addComponent(refreshButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        filterPanel.clearFilters();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void createNewWare(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createNewWare
        //show dialog to create new ware
        WareEditingDialog.showWareEditingDialog(client, true,
                client.getDatabaseConnection(), -1);

        //update table model  
        filterPanel.clearFilters();
    }//GEN-LAST:event_createNewWare

    private void editWare(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editWare
        //get selected ware
        int row = table.getSelectedRow();
        if(row < 0)
            return;

        int ID = Integer.parseInt((String)tableModel.getValueAt(row, 0));

        //let user edit ware
        WareEditingDialog.showWareEditingDialog(null, true,
                client.getDatabaseConnection(), ID);

        filterPanel.clearFilters();
    }//GEN-LAST:event_editWare

    private void deleteWare(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteWare

        //get ID of selected ware
        int row = table.getSelectedRow();
        if(row < 0)
            return;

        int ID = Integer.parseInt((String)tableModel.getValueAt(row, 0));
        
        try {
            //check whether it is ordered; if so: refute to remove
            ResultSet orderedWare = stmt.executeQuery("SELECT (orderId) FROM " +
                    "orderParts WHERE wareId = "+ID);
            if(orderedWare.next()) {
                JOptionPane.showMessageDialog(client, "<html>Die Ware kann nicht " +
                        "entfernt werden, da sie von mindestens einer Bestellung " +
                        "referenziert wird.<p>Bitte betreffende Bestellung(en)" +
                        " löschen.</html>", "Datenfehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //if not: remove it
            stmt.executeUpdate("DELETE FROM goods WHERE id = "+ID);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(client, "Fehler bei der Kommunikation" +
                    " mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        
        
        filterPanel.clearFilters();
    }//GEN-LAST:event_deleteWare


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteWareButton;
    private javax.swing.JButton editWareButton;
    private javax.swing.JPanel filterPanelPlaceholder;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton newWareButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    MagazineClient client;
    Statement stmt;
    DefaultTableModel tableModel;

    FilterPanel filterPanel;

    //==============================METHODS===================================//

    /**
     * Is called whenever the user enters a filter using the FilterPanel. The
     * goods list is then updated accordingly by this method.
     */
    private void filterEntered() {
        updateTableModel(filterPanel.getFilteredData());
    }
    

    /**
     * Updates the table model to reflect the given list of wares.
     * @param wares A ResultSet storing all data about all wares that should
     * be displayed.
     */
    private void updateTableModel(ResultSet wares) {

        //clear model
        tableModel = new DefaultTableModel(null, new String[]{
            "ID", "Bezeichnung", "Einheit", "Vorhanden", "Bestellt", "Preis (Staat)",
            "Preis (€)", "Händler"
        }) {
            private static final long serialVersionUID = 1;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);


        //construct new model
        Object[] columnData = new Object[10];

        try {
            while(wares.next()) {
                columnData[0]=Util.addZeroes(wares.getInt("id"), 3);
                columnData[1]=wares.getString("name");
                columnData[2]=wares.getString("packageName")+" ("+
                        wares.getString("packageSize")+wares.getString("packageUnit")
                        +")";
                columnData[3]=Util.addZeroes(wares.getInt("available"), 3);
                columnData[4]=Util.addZeroes(computeOrderedAmount(wares.getInt("id")),
                        3);
                columnData[5]=Util.addZeroes(wares.getDouble("fictivePrice"), 3);
                columnData[6]=Util.addZeroes(wares.getDouble("realPrice"), 3);
                columnData[7]=wares.getString("seller");
                tableModel.addRow(columnData);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(client, "Fehler bei der Kommunikation" +
                    " mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }

    /**
     * Looks up how many pieces of one particular ware are needed at the moment.
     * @param wareID The ware's ID.
     * @return The amount of the ware that is ordered.
     */
    private int computeOrderedAmount(int wareID) throws SQLException{

        int orderedAmount = 0;

        Statement stmt2 = client.getDatabaseConnection().createStatement();

        ResultSet orders = stmt.executeQuery("SELECT pieces, orderId FROM " +
                "orderParts WHERE wareId = "+wareID);
        
        while(orders.next()) {
            ResultSet order = stmt2.executeQuery("SELECT processed FROM orders WHERE " +
                    "id = "+orders.getInt("orderId"));
            order.next();
            if(order.getInt("processed") == 0)
                orderedAmount += orders.getInt("pieces");
        }
        return orderedAmount;
    }

    //============================INNER CLASSES===============================//
}
