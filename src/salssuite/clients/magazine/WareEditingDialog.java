/*
 * WareEditingDialog.java
 *
 * Created on 25.06.2010, 23:35:16
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import salssuite.util.Util;

/**
 * Lets the user edit or create a ware. This dialog is a mirror of one line
 * in the 'goods' table, permitting the user to modify all the properties
 * of one ware.
 * <p>
 * Note that the dialog does all communication with the database itself, so
 * you can just display it and wait for its disposal.
 * <p>
 * This dialog also alerts the user when they attempt to edit a ware that
 * has been edited by another user since the former brought up this client.
 * @author Jannis Limperg
 * @version 1.0.1
 * @see salssuite.server.module.MagazineModule#buildDatabase
 * @see salssuite.server.module.MagazineModule
 * @see MagazineClient
 * @see MagazineClientWarePanel
 */
public class WareEditingDialog extends javax.swing.JDialog {

    private static final long serialVersionUID=1L;

    /**
     * Sole constructor.
     * @param parent The dialog's parent frame.
     * @param modal Whether the dialog should be modal or not.
     * @param databaseConnection A connection to the database holding the
     * 'goods' table.
     * @param wareID The ware to be edited, or <code>-1</code> if a new one
     * should be created.
     * @see salssuite.server.module.MagazineModule#buildDatabase
     */
    public WareEditingDialog(java.awt.Frame parent, boolean modal, Connection
            databaseConnection, int wareID) {
        super(parent, modal);
        initComponents();

        this.wareID = wareID;
        this.parent = parent;

        try {
            //connect to database
            stmt = databaseConnection.createStatement();

            //if a new ware should be created, leave all fields empty
            if(wareID < 0)
                createNew = true;

            //set field values if an existing ware should be modified
            else {
                createNew = false;
                setFieldsAccordingToDatabase();
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Konnte nicht mit Datenbank " +
                    "verbinden. Beende die Anwendung.", "Kritischer Netzwerkfehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
            return;
        }

        //usability
        if(parent != null)
            setLocation((int)parent.getLocation().getX()/2, (int)parent.getLocation().getY()/2);
        else
            setLocation(400, 400);

        if(createNew)
            setTitle("SalSSuite - Neue Ware erstellen");
        else
            setTitle("SalSSuite - Ware bearbeiten");     
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        nameInput = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        sellerInput = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        realPriceInput = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        fictivePriceInput = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        packageSizeInput = new javax.swing.JTextField();
        packageDescriptionInput = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        packageUnitInput = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        piecesAvailableInput = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        OKButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Name");

        nameInput.setToolTipText("Name oder Beschreibung der Ware.");

        jLabel3.setText("Verkäufer");

        sellerInput.setToolTipText("Verkäufer der Ware");

        jLabel4.setText("Preis    (€)");

        realPriceInput.setToolTipText("Einkaufspreis in Euro");

        jLabel5.setText("     (Staat)");

        fictivePriceInput.setToolTipText("Abgabepreis in Staatswährung");

        jLabel6.setText("Packung");

        packageSizeInput.setToolTipText("Z.B. \"1\"");

        packageDescriptionInput.setToolTipText("Z.B. \"4 Flaschen\"");

        jLabel7.setText("à");

        packageUnitInput.setToolTipText("Z.B. \"Liter\" oder \"l\"");

        jLabel8.setText("(Packungsbeschreibung)");

        jLabel9.setText("(Größe)");

        jLabel10.setText("(Einheit)");

        jLabel11.setText("Stück vorhanden");

        cancelButton.setText("Abbrechen");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });

        OKButton.setText("Absenden");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSent(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(piecesAvailableInput))
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(packageDescriptionInput))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(packageSizeInput)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(packageUnitInput, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 260, Short.MAX_VALUE)
                        .addComponent(OKButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(realPriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fictivePriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sellerInput, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                            .addComponent(nameInput, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))
                        .addGap(21, 21, 21)))
                .addGap(0, 0, 0))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {fictivePriceInput, realPriceInput});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, packageUnitInput});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel9, packageSizeInput});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel8, packageDescriptionInput});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(sellerInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(realPriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(fictivePriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(packageDescriptionInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(packageSizeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(packageUnitInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(piecesAvailableInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(OKButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputSent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSent

        //fields for the input
        String name; String seller; double realPrice; double fictivePrice;
        String packageName; double packageSize; String packageUnit;
        int piecesAvailable;

        //parse values
        try {
            packageSize = Double.parseDouble(packageSizeInput.getText().replaceAll(",", "."));
            piecesAvailable = Integer.parseInt(piecesAvailableInput.getText());

            if(packageSize <= 0 || piecesAvailable < 0)
                throw new NumberFormatException();
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte positive ganze Zahlen für ID, " +
                    "Packungsgröße und vorhandene Stückzahl angeben.",
                    "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            wareID = -1;
            return;
        }

        try {
            realPrice = Double.parseDouble(realPriceInput.getText().replaceAll(",", "."));
            fictivePrice = Double.parseDouble(fictivePriceInput.getText().replaceAll(",", "."));

            if(realPrice < 0 || fictivePrice < 0)
                throw new NumberFormatException();
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte gültige Werte für die " +
                    "Preise angeben.", "Ungültige Eingabe", JOptionPane.
                    ERROR_MESSAGE);
            wareID = -1;
            return;
        }

        name = nameInput.getText();
        seller = sellerInput.getText();
        packageName = packageDescriptionInput.getText();
        packageUnit = packageUnitInput.getText();

        //check if user attempts to use forbidden characters
        if(!Util.checkInput(name) || !Util.checkInput(seller) ||
                !Util.checkInput(packageName) || !Util.checkInput(packageUnit))
            return;

        //add new ware or edit old one
        try {
            if(createNew) {
                String insert = "INSERT INTO goods (name, seller, " +
                        "realPrice, fictivePrice, packageName, packageSize," +
                        "packageUnit, available) VALUES (";

                insert += "'" + name + "' , ";
                insert += "'" + seller + "' , ";
                insert += realPrice + " , ";
                insert += fictivePrice + " , ";
                insert += "'" + packageName + "' , ";
                insert += packageSize + " , ";
                insert += "'" + packageUnit + "' , ";
                insert += piecesAvailable + ")";

                stmt.executeUpdate(insert);
                wareID = 0;
            }
            else {
                //Check if the ware has been deleted in the meantime
                ResultSet ware = stmt.executeQuery("SELECT * FROM goods"
                        + " WHERE id = "+wareID);
                if(!ware.next()) {
                    JOptionPane.showMessageDialog(parent, "<html>Die gewählte Ware"
                            + " existiert nicht.<br/>Möglicherweise wurde"
                            + " sie von einem anderen Nutzer gelöscht.</html>",
                            "Fehler", JOptionPane.ERROR_MESSAGE);
                    dispose();
                    return;
                }

                //Check if it has been altered in the meantime
                if(!(
                    ware.getString("name").equals(originalName) &&
                    ware.getString("seller").equals(originalSeller) &&
                    ware.getDouble("realPrice") == originalRealPrice &&
                    ware.getDouble("fictivePrice") == originalFictivePrice &&
                    ware.getString("packageName").equals(originalPackageName) &&
                    ware.getString("packageUnit").equals(originalPackageUnit) &&
                    ware.getDouble("packageSize") == originalPackageSize &&
                    ware.getInt("available") == originalAvailable
                  )) {
                    JOptionPane.showMessageDialog(parent, "<html>Die Ware wurde"
                            + " zwischenzeitlich von einem anderen Nutzer"
                            + " bearbeitet.<br/> Aktualisiere die Daten...</html>",
                            "Warnung", JOptionPane.WARNING_MESSAGE);
                    setFieldsAccordingToDatabase();
                    return;
                }

                //If not, update the database
                String update = "UPDATE goods SET";

                update += " name = " +  "'" +name + "'";
                update += ", seller = " +  "'" +seller + "'";
                update += ", realPrice = " +realPrice;
                update += ", fictivePrice = " +fictivePrice;
                update += ", packageName = " +  "'" +packageName + "'";
                update += ", packageSize = " +packageSize;
                update += ", packageUnit = " +  "'" +packageUnit + "'";
                update += ", available = " + piecesAvailable;
                update += " WHERE id = " + wareID;

                stmt.executeUpdate(update);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation mit" +
                    " der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //dispose
        dispose();
    }//GEN-LAST:event_inputSent

    private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
        wareID = -1;
        dispose();
    }//GEN-LAST:event_cancel


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField fictivePriceInput;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField nameInput;
    private javax.swing.JTextField packageDescriptionInput;
    private javax.swing.JTextField packageSizeInput;
    private javax.swing.JTextField packageUnitInput;
    private javax.swing.JTextField piecesAvailableInput;
    private javax.swing.JTextField realPriceInput;
    private javax.swing.JTextField sellerInput;
    // End of variables declaration//GEN-END:variables

    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    int wareID;
    boolean createNew;
    Statement stmt;
    java.awt.Frame parent;

    //The following fields are used to check if some other user has
    //changed the ware this dialog operates on while this dialog was open.
    String originalName = null;
    String originalSeller = null;
    double originalRealPrice = 0;
    double originalFictivePrice = 0;
    int originalAvailable = 0;
    String originalPackageName = null;
    String originalPackageUnit = null;
    double originalPackageSize = 0;


    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Synchronises this client's input fields with values from the database.
     * Also sets the originalXXX fields accordingly.
     * @throws SQLException if an error occurs while communicating with the
     * database.
     */
    private void setFieldsAccordingToDatabase() throws SQLException {
        ResultSet ware = stmt.executeQuery("SELECT * FROM goods WHERE id = "+wareID);

        //Check if the ware has been deleted in the meantime
        if(!ware.next()) {
            JOptionPane.showMessageDialog(parent, "<html>Die gewählte Ware"
                    + " existiert nicht.<br/>Möglicherweise wurde"
                    + " sie von einem anderen Nutzer gelöscht.</html>",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        //if not, set the fields
        nameInput.setText(ware.getString("name"));
        originalName = ware.getString("name");
        sellerInput.setText(ware.getString("seller"));
        originalSeller = ware.getString("seller");
        realPriceInput.setText(""+ware.getDouble("realPrice"));
        originalRealPrice = ware.getDouble("realPrice");
        fictivePriceInput.setText(""+ware.getDouble("fictivePrice"));
        originalFictivePrice = ware.getDouble("fictivePrice");
        packageDescriptionInput.setText(ware.getString("packageName"));
        originalPackageName = ware.getString("packageName");
        packageSizeInput.setText(""+ware.getDouble("packageSize"));
        originalPackageSize = ware.getDouble("packageSize");
        packageUnitInput.setText(ware.getString("packageUnit"));
        originalPackageUnit = ware.getString("packageUnit");
        piecesAvailableInput.setText(""+ware.getInt("available"));
        originalAvailable = ware.getInt("available");
    }

    /**
     * Returns the ID of the ware this dialog operates on.
     * @return The ware's ID, or <code>-1</code> if the dialog was disposed.
     */
    public int getWareID() {
        return wareID;
    }

    /**
     * Shows a modal <code>WareEditingDialog</code>.
     * @param parent The dialog's parent frame.
     * @param databaseConnection A connection to the database holding the
     * 'goods' table.
     * @param wareID The ware which should be modified. If a new ware should be
     * created, pass <code>-1</code> here.
     * @return Let the returned value be <code>ID</code>, then the following
     * applies:<br/>
     * <ul>
     * <li>If <code>ID</code> is greater than zero, it is the ID of the
     * ware which has been edited.</li>
     * <li>If <code>ID</code> is zero, this means that a new ware has been created.</li>
     * <li>If <code>ID</code> is less than zero, this means that the user has
     * cancelled.</li>
     * </ul>
     * @see salssuite.server.module.MagazineModule#buildDatabase
     */
    public static int showWareEditingDialog(java.awt.Frame parent,
            Connection databaseConnection, int wareID) {
        WareEditingDialog dia = new WareEditingDialog(parent, true,
                databaseConnection, wareID);
        dia.setVisible(true);
        return dia.getWareID();
    }

    //============================INNER CLASSES===============================//
}
