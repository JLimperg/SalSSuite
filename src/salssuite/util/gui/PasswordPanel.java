/*
 * PasswordPanel.java
 *
 * Created on 23.02.2010, 19:29:19
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

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import salssuite.util.Passwords;
import salssuite.util.Util;

/**
 * Asks the user for a password for one module.
 * <p>
 * This panel is meant to be used by modules before they display their own GUIs.
 * The <code>PasswordPanel</code> asks the user for a password and compares it
 * with the hashes stored in the server's password database. It displays the
 * module panel if the password was entered correctly.
 * <p>
 * The methods in {@link salssuite.util.Passwords} are used for encryption.
 * @author Jannis Limperg
 * @version 1.0
 * @see salssuite.server.gui.ServerGUI
 */
public class PasswordPanel extends javax.swing.JPanel {

    private static final long serialVersionUID=1;

    /**
     * Sole constructor. After the user has entered the password
     * correctly, this panel is replaced with <code>modulePanel</code>.
     * @param databaseConnection A connection to the server's password database.
     * @param moduleName The name of the module this panel is a protection for.
     * <code>moduleName</code> must be the identifier used in the
     * 'module' column of the 'permissions' table.
     * @param modulePanel The panel which is to be displayed after the password
     * was entered correctly.
     * @param descr A description of the GUI. This becomes the title of the
     * corresponding module tab in the {@link salssuite.server.gui.ServerGUI}.
     * @see salssuite.server.Server#buildServerDatabase
     */
    public PasswordPanel(Connection databaseConnection,
            String moduleName, JPanel modulePanel, String descr) {
        initComponents();
        this.panel = modulePanel;
        this.descr = descr;
        this.dbcon = databaseConnection;
        this.moduleName = moduleName;
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
        pwInput = new javax.swing.JPasswordField();
        messageLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        usernameInput = new javax.swing.JTextField();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                focusGainedHandler(evt);
            }
        });

        jLabel1.setText("Passwort:");

        pwInput.setToolTipText("Passwort eingeben");
        pwInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordSent(evt);
            }
        });

        jLabel2.setText("Benutzer");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameInput, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                            .addComponent(pwInput, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(usernameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pwInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(messageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pwInput, usernameInput});

    }// </editor-fold>//GEN-END:initComponents

    private void passwordSent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordSent
        //ask for password
        String username = usernameInput.getText();
        String pw = new String(pwInput.getPassword());
        String pwencrypt;

        //check if the user attempts do use forbidden characters
        if(!Util.checkInput(pw) || !Util.checkInput(username)) return;


        //encrypt password
        try {
            pwencrypt = Passwords.encryptPassword(pw);
        }
        catch(java.security.NoSuchAlgorithmException e) {
            displayError("Konnte Verschlüsselungsalgorithmus nicht laden.");
            return;
        }

        try {
            Statement stmt = dbcon.createStatement();

            //check if username and password match
            ResultSet account = stmt.executeQuery("SELECT hash FROM accounts"
                    + " WHERE username = '"+username+"'");

            if(!account.next()) {
                displayError("Benutzer unbekannt");
                usernameInput.selectAll();
                usernameInput.requestFocus();
                return;
            }

            if(!pwencrypt.equals(account.getString("hash"))) {
                displayError("Falsches Passwort");
                pwInput.selectAll();
                pwInput.requestFocus();
                return;
            }

            //check module permissions
            ResultSet perm = stmt.executeQuery("SELECT username FROM permissions "
                    + "WHERE username = '"+username+"' AND (module = '"+
                    moduleName+"' OR module = 'all')");

            if(!perm.next()) {
                displayError("Keine Berechtigung für dieses Modul");
                usernameInput.selectAll();
                usernameInput.requestFocus();
                return;
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //user is authenticated: display module panel
        displayModulePanel();
    }//GEN-LAST:event_passwordSent

    private void focusGainedHandler(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_focusGainedHandler
        pwInput.requestFocus();
    }//GEN-LAST:event_focusGainedHandler


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JPasswordField pwInput;
    private javax.swing.JTextField usernameInput;
    // End of variables declaration//GEN-END:variables


    //===============================OWN CODE===================================

    //FIELDS
    JPanel panel;
    String descr;
    Connection dbcon;
    String moduleName;

    Scanner in;

    //METHODS
    private void displayError(String message) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(message);
    }

    private void displayModulePanel() {
        pwInput.setText("");
        messageLabel.setText("");

        java.awt.Container parent = getParent();
        javax.swing.JFrame top = null;
        try {
            top = (javax.swing.JFrame)getTopLevelAncestor();
        }
        catch(ClassCastException e) {}

        if(parent instanceof JTabbedPane) {
            JTabbedPane tabbedParent = (JTabbedPane)parent;
            int index = tabbedParent.indexOfComponent(this);
            parent.remove(this);
            tabbedParent.insertTab(descr, null, panel, null, index);
            tabbedParent.setSelectedIndex(index);
        }
        else {
            parent.remove(this);
            parent.add(panel);
        }

        parent.validate();
        parent.repaint();

        if(top != null) {
            top.validate();
            top.pack();
        }
    }
}
