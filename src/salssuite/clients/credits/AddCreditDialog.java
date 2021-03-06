/*
 * AddCreditDialog.java
 *
 * Created on 02.06.2011, 17:43:27
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

package salssuite.clients.credits;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import salssuite.util.Util;

/**
 * Dialog that can be used to let the user create a new credit. Performs all
 * database operations on its own, therefore no interaction with other UI
 * components is necessary.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class AddCreditDialog extends javax.swing.JDialog {

    //Serialization not supported!
    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     * @param parent This dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     * @param dbcon A connection to the database holding the 'credits', 'citizens'
     * and 'companies' tables.
     */
    public AddCreditDialog(java.awt.Frame parent, boolean modal, Connection
            dbcon) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        this.dbcon = dbcon;

        //prepare database access
        try {
            stmt = dbcon.createStatement();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //add default values to the GUI
        GregorianCalendar now = new GregorianCalendar();
        currentYearDisplay1.setText(""+now.get(GregorianCalendar.YEAR));
        currentYearDisplay2.setText(""+now.get(GregorianCalendar.YEAR));
        startDayInput.setText(now.get(GregorianCalendar.DAY_OF_MONTH)+"."+
                (now.get(GregorianCalendar.MONTH)+1)+".");
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        IDInput = new javax.swing.JTextField();
        citizenOrCompanyDisplay = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        amountInput = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        interestInput = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        startDayInput = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        endDayInput = new javax.swing.JTextField();
        currentYearDisplay1 = new javax.swing.JLabel();
        currentYearDisplay2 = new javax.swing.JLabel();
        confirmButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        IDTypeChooser = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kredit hinzufügen");

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+2));
        jLabel1.setText("Kredit hinzufügen");

        jLabel2.setText("Kreditnehmer");

        jLabel3.setText("Nr.");

        IDInput.setToolTipText("ID des Bürgers, der den Kredit erhält.");
        IDInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateCitizenOrCompanyDisplay(evt);
            }
        });

        jLabel5.setText("Betrag");

        amountInput.setToolTipText("geliehener Betrag");

        jLabel6.setText("Zinsen pro Tag");

        interestInput.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        interestInput.setText("0");
        interestInput.setToolTipText("Zinsrate pro Tag");

        jLabel7.setText("%");

        jLabel8.setText("ausgegeben am");

        startDayInput.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        startDayInput.setToolTipText("Tag, an dem dieser Kredit ausgegeben wurde. Format TT.MM");
        startDayInput.setNextFocusableComponent(confirmButton);

        jLabel9.setText("fällig am");

        endDayInput.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        endDayInput.setToolTipText("Tag, an dem dieser Kredit zurückgezahlt werden muss. Format TT.MM");

        currentYearDisplay1.setText("2011");

        currentYearDisplay2.setText("2011");

        confirmButton.setText("Absenden");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirm(evt);
            }
        });

        cancelButton.setText("Abbrechen");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });

        IDTypeChooser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Betrieb", "Bürger" }));
        IDTypeChooser.setToolTipText("Auswählen, ob ein Bürger oder ein Betrieb den Kredit erhalten soll.");
        IDTypeChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCitizenOrCompanyDisplay_ComboBox(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(IDTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(IDInput, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(citizenOrCompanyDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 328, Short.MAX_VALUE)
                        .addComponent(confirmButton)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(endDayInput, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(currentYearDisplay1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startDayInput, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(currentYearDisplay2)))
                        .addGap(273, 273, 273))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(4, 4, 4)
                        .addComponent(amountInput, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(interestInput, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(130, 130, 130))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {amountInput, interestInput});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {endDayInput, startDayInput});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(IDTypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(IDInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(citizenOrCompanyDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(amountInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(interestInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(endDayInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(currentYearDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(startDayInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentYearDisplay2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(confirmButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {endDayInput, startDayInput});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {amountInput, interestInput});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
        creditID = -1;
        dispose();
    }//GEN-LAST:event_cancel

    private void confirm(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirm
        //parse IDs
        int ID = -1;
        

        try {
            ID = Integer.parseInt(IDInput.getText());
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte positive ganze Zahl"
                    + " in das Feld für die Betriebs- bzw. Bürgernummer eingeben.",
                    "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //check if ID exists
        try {
            if(IDTypeChooser.getSelectedIndex() == 1) { //we're operating on a citizen
                ResultSet citizen = stmt.executeQuery("SELECT id FROM citizens"
                        + " WHERE id = " + ID);
                if(!citizen.next()) {
                    JOptionPane.showMessageDialog(this, "Kein Bürger mit dieser"
                            + " Nummer vorhanden.", "Datenfehler",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            else { //we're operating on a company
                ResultSet company = stmt.executeQuery("SELECT id FROM companies"
                        + " WHERE id = " + ID);
                if(!company.next()) {
                    JOptionPane.showMessageDialog(this, "Keine Firma mit dieser"
                            + " Nummer vorhanden.", "Datenfehler",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //parse amount and interest
        double amount, interest;
        try {
            amount = Double.parseDouble(amountInput.getText().replaceAll(",", "."));
            interest = Double.parseDouble(interestInput.getText().replaceAll(",", "."));
            if(amount < 0 || interest < 0)
                throw new NumberFormatException();
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "<html>Bitte positive Fließkomma"
                    + "zahlen ('4'; '4,4'; '090,789' etc.)<br/> in die Felder für"
                    + " Betrag und Zinsen eingeben.</html>", "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //parse dates
        String[] endDayText = endDayInput.getText().trim().split("\\.");
        String[] startDayText = startDayInput.getText().trim().split("\\.");

        if(endDayText.length < 2 || startDayText.length < 2) {
            JOptionPane.showMessageDialog(this, "In die Felder für die Daten bitte"
                    + " Tag und Monat im Format TT.MM. eingeben.",
                    "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GregorianCalendar startDay, endDay;
        try {
            int endDayDay, endDayMonth, startDayDay, startDayMonth;
            endDayDay = Integer.parseInt(endDayText[0]);
            endDayMonth = Integer.parseInt(endDayText[1]);
            startDayDay = Integer.parseInt(startDayText[0]);
            startDayMonth = Integer.parseInt(startDayText[1]);

            if(endDayDay < 1 || endDayDay > 31 || startDayDay < 1 || startDayDay
                    > 31 || endDayMonth < 1 || endDayMonth > 12 || startDayMonth
                    < 1 || startDayMonth > 12)
                throw new NumberFormatException();

            int year = new GregorianCalendar().get(GregorianCalendar.YEAR);
            endDay = new GregorianCalendar(year, endDayMonth-1,
                    endDayDay);
            startDay = new GregorianCalendar(year,
                    startDayMonth-1, startDayDay);

            if(endDay.before(startDay)) {
                JOptionPane.showMessageDialog(this, "<html>Der Tag, an dem der"
                        + " Kredit fällig ist,<br/> muss vor dem Ausgabetag"
                        + " liegen.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültiges Datumsformat.",
                    "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //make ID strings for the database
        String companyIDString, citizenIDString;
        if(IDTypeChooser.getSelectedIndex() == 1) {
            citizenIDString = ""+ID;
            companyIDString = "-1";
        }
        else {
            citizenIDString = "-1";
            companyIDString = ""+ID;
        }

        //insert everything into the database
        try {
            stmt.executeUpdate("INSERT INTO credits VALUES("
                    + "DEFAULT,"
                    + companyIDString + ","
                    + citizenIDString + ","
                    + amount + ","
                    + interest + ","
                    + "'" + Util.getDateString(startDay) + "',"
                    + "'" + Util.getDateString(endDay) + "',"
                    + "0)");

            //Get the new credit's ID. Note that in extremely seldom cases, the
            //following statement might return the ID of another credit which
            //was created almost simultaneously with the one we have created.
            //Unfortunately SQL does not provide a method to find out the new credit's
            //ID right when creating it.
            ResultSet creditIDQuery = stmt.executeQuery("SELECT MAX(id) "
                    + "AS creditID FROM"
                    + " credits");
            if(creditIDQuery.next())
                creditID = creditIDQuery.getInt("creditID");
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //we're done, dispose
        dispose();
    }//GEN-LAST:event_confirm

    private void updateCitizenOrCompanyDisplay(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_updateCitizenOrCompanyDisplay
        //get ID
        int ID;
        try {
            ID = Integer.parseInt(IDInput.getText());
        }
        catch(NumberFormatException e) {
            citizenOrCompanyDisplay.setForeground(Color.RED);
            citizenOrCompanyDisplay.setText("(Ungültige Eingabe)");
            return;
        }

        //are we operating on a citizen or a company?
        boolean company = false;
        if(IDTypeChooser.getSelectedIndex() == 0)
            company = true;

        //get citizen/company if possible
        try {
            ResultSet citizenOrCompany;
            if(!company)
                citizenOrCompany = stmt.executeQuery("SELECT forename, surname FROM"
                    + " citizens WHERE id = "+ID);
            else
                citizenOrCompany = stmt.executeQuery("SELECT name FROM"
                    + " companies WHERE id = "+ID);

            if(citizenOrCompany.next()) {
                citizenOrCompanyDisplay.setForeground(Color.BLACK);
                if(!company)
                    citizenOrCompanyDisplay.setText(
                            citizenOrCompany.getString("forename").split(" ")[0] +
                            " " + citizenOrCompany.getString("surname"));
                else
                    citizenOrCompanyDisplay.setText(
                            citizenOrCompany.getString("name"));
            }
            else {
                citizenOrCompanyDisplay.setForeground(Color.RED);
                if(!company)
                    citizenOrCompanyDisplay.setText("(Bürger unbekannt)");
                else
                    citizenOrCompanyDisplay.setText("(Betrieb unbekannt)");
            }
        }
        catch(SQLException e) {
            citizenOrCompanyDisplay.setForeground(Color.RED);
            citizenOrCompanyDisplay.setText("(Netzwerkfehler)");
        }
    }//GEN-LAST:event_updateCitizenOrCompanyDisplay

    private void updateCitizenOrCompanyDisplay_ComboBox(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCitizenOrCompanyDisplay_ComboBox
        updateCitizenOrCompanyDisplay(null);
    }//GEN-LAST:event_updateCitizenOrCompanyDisplay_ComboBox

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IDInput;
    private javax.swing.JComboBox IDTypeChooser;
    private javax.swing.JTextField amountInput;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel citizenOrCompanyDisplay;
    private javax.swing.JButton confirmButton;
    private javax.swing.JLabel currentYearDisplay1;
    private javax.swing.JLabel currentYearDisplay2;
    private javax.swing.JTextField endDayInput;
    private javax.swing.JTextField interestInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField startDayInput;
    // End of variables declaration//GEN-END:variables

    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Connection dbcon;
    Statement stmt;
    int creditID = -1;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * If the user has successfully created a credit using this dialog, returns
     * its ID. Otherwise returns <code>-1</code>.
     * @return The new credit's ID, or <code>-1</code> if the user has cancelled.
     */
    public int getCreditID() {
        return creditID;
    }

    //============================INNER CLASSES===============================//
}
