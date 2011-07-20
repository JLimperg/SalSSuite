/*
 * MagazineClientShoppingListPartPanel.java
 *
 * Created on 17.08.2010, 23:04:35
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

package salssuite.clients.magazine;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import salssuite.util.Util;

/**
 * Represents one ware in a shopping list. It displays one ware's name
 * and the number of pieces which must be bought of this ware. Additionally
 * it provides a method for the user to indicate that the ware is (partly)
 * available at the magazine again because it was recently refilled. Doing so,
 * the user can manipulate the 'available' property of this ware indirectly
 * without using the {@link MagazineClientWarePanel}.
 * @author Jannis Limperg
 * @version 1.0.1
 * @see MagazineClientShoppingListPanel
 * @see salssuite.server.module.MagazineModule
 */
public class MagazineClientShoppingListPartPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     * @param parent This component's parent component
     * @param databaseConnection A connection to the database holding the
     * 'goods' and 'orderParts' tables.
     * @param wareID The ware to be bought.
     * @throws IllegalArgumentException  if <code>ware</code> is null,
     * <code>parent</code> is null, or <code>requiredAmount</code> is less
     * than or equal to zero.
     * @see salssuite.server.module.MagazineModule#buildDatabase
     */
    public MagazineClientShoppingListPartPanel(JFrame parent, Connection
            databaseConnection, int wareID) {
        
        if(parent == null || wareID < 0)
            throw new IllegalArgumentException("Parameters must be non-null or" +
                    " greater than zero respectively.");

        initComponents();

        this.parent = parent;
        this.wareID = wareID;
        dbcon = databaseConnection;

        try {
            stmt = databaseConnection.createStatement();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Konnte nicht mit der Datenbank" +
                    " verbinden. Beende die Anwendung.", "Kritischer Netzwerkfehler",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return;
        }

        updateDisplays();
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        IDDisplay = new javax.swing.JLabel();
        nameDisplay = new javax.swing.JLabel();
        boughtButton = new javax.swing.JButton();
        requiredAmountDisplay = new javax.swing.JLabel();

        IDDisplay.setText("ID");

        nameDisplay.setText("name");

        boughtButton.setText("eingekauft");
        boughtButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boughtButtonActionPerformed(evt);
            }
        });

        requiredAmountDisplay.setText("requiredAmount");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(IDDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(requiredAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(boughtButton)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDDisplay)
                    .addComponent(nameDisplay)
                    .addComponent(requiredAmountDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boughtButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void boughtButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boughtButtonActionPerformed
        //get required amount of ware
        int reqAmount = getRequiredAmount();

        //get bought amount
        int boughtAmount;

        do {
            try {
                String boughtAmountString = JOptionPane.showInputDialog(parent, "Von "+
                        reqAmount+" wurden gekauft: ", reqAmount);

                if(boughtAmountString == null) //means user has cancelled
                    return;

                boughtAmount = Integer.parseInt(boughtAmountString);

                break;
            }
            catch(NumberFormatException e) {
                continue;
            }
        } while(true);

        //update the data
        int availableAmount;

        try {
            ResultSet ware = stmt.executeQuery("SELECT available, realPrice FROM " +
                    "goods WHERE id = "+wareID);
            ware.next();
            availableAmount = ware.getInt("available") + boughtAmount;
            double realPrice = ware.getDouble("realPrice");
            stmt.executeUpdate("UPDATE goods SET available = "+availableAmount+
                    " WHERE id = "+wareID);

            stmt.executeUpdate("INSERT INTO realPurchases VALUES (" +
                    "'" + Util.getDateString() + "', "+
                    "'" + Util.getTimeString() + "', "+
                    wareID + ", "+
                    boughtAmount + ", "+
                    realPrice +
                    ")");
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation" +
                    "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //update the panel
        requiredAmountDisplay.setText(""+(reqAmount-boughtAmount));

        //remove this panel from the list if the user has bought more units than
        //are required        
        if(reqAmount - boughtAmount <= 0) {
            Container parentPanel = getParent();
            parentPanel.remove(this);
            parentPanel.validate();
            parentPanel.repaint();
        }
    }//GEN-LAST:event_boughtButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IDDisplay;
    private javax.swing.JButton boughtButton;
    private javax.swing.JLabel nameDisplay;
    private javax.swing.JLabel requiredAmountDisplay;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//
    
    int wareID;

    Container parent;
    Statement stmt;
    Connection dbcon;
    LinkedList<ActionListener> listeners = new LinkedList<ActionListener>();

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Call this method to indicate that exactly the required amount of this ware
     * has been bought and that no more pieces of this ware should be bought.
     * Calling the method automatically removes this panel from the shopping list
     * and sets all data accordingly.
     */
    public void setNotRequiredAnyMore() {

        int requiredAmount = getRequiredAmount();
        int boughtAmount = requiredAmount;

        //update the data
        int availableAmount;

        try {
            ResultSet ware = stmt.executeQuery("SELECT available, realPrice FROM " +
                    "goods WHERE id = "+wareID);
            ware.next();
            availableAmount = ware.getInt("available") + boughtAmount;
            double realPrice = ware.getDouble("realPrice");
            stmt.executeUpdate("UPDATE goods SET available = "+availableAmount+
                    " WHERE id = "+wareID);

            stmt.executeUpdate("INSERT INTO realPurchases VALUES ("+
                    "'" + Util.getDateString()+"', "+
                    "'" + Util.getTimeString()+"', "+
                    wareID+", "+
                    boughtAmount+", "+
                    realPrice+
                    ")");
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation" +
                    "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //remove this panel
        Container parentPanel = getParent();
        parentPanel.remove(this);
        parentPanel.validate();
        parentPanel.repaint();
    }

    /**
     * Returns how many pieces of this ware are currently ordered.
     * Note that this value may be negative if
     * the magazine has actually bought <i>too many</i> entities; in that case
     * there are more wares available than needed.
     * <p>
     * The figure returned is equal to [(sum of all orders) - (amount available
     * at the magazine)].
     * @return The required amount.
     */
    public int getRequiredAmount() {
        
        try {
            int orderedAmount = 0;

            Statement stmt2 = dbcon.createStatement();
            Statement stmt3 = dbcon.createStatement();

            ResultSet orders = stmt2.executeQuery("SELECT pieces, orderId FROM " +
                    "orderParts WHERE wareId = "+wareID);

            while(orders.next()) {
                ResultSet order = stmt3.executeQuery("SELECT processed FROM orders WHERE " +
                        "id = "+orders.getInt("orderId"));
                order.next();
                if(order.getInt("processed") == 0)
                    orderedAmount += orders.getInt("pieces");
        }

            return orderedAmount;
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation" +
                    "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * Sets all displays to reflect the current data.
     */
    private void updateDisplays() {

        String name;
        try {
            ResultSet ware = stmt.executeQuery("SELECT (name) FROM goods WHERE" +
                    " id = "+wareID);
            ware.next();
            name = ware.getString("name");
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation" +
                    "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        IDDisplay.setText(""+wareID);
        nameDisplay.setText(name);
        requiredAmountDisplay.setText(""+getRequiredAmount());
    }

    //============================INNER CLASSES===============================//
}
