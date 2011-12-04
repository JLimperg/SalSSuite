/*
 * AccountManagingPanel.java
 *
 * Created on 20.04.2011, 15:48:53
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

package salssuite.server.gui;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import salssuite.util.Passwords;
import salssuite.util.Util;

/**
 * GUI for managing user accounts in the whole programme.
 * The user can add and remove accounts, change passwords and grant permissions
 * for certain modules.
 * <p>
 * Every account consists of a username and a password. Accounts can be used
 * to gain access to a) parts of the server and b) clients for certain modules. Access
 * is controlled on a by-module basis, meaning one account can only gain access
 * to everything related to one module as a whole, and not for example to the
 * server panel and the client separately.
 * <p>
 * When a new project is created, the user 'admin' is set up with the
 * password specified by the user when installing the SalSSuite. This user has
 * access to all modules, which is indicated by the database permission entry
 * 'all'.
 * <p>
 * Account data (usernames and password hashes) are stored in a table named
 * 'accounts'. Permissions for each module and username are stored in
 * 'permissions'. See {@link salssuite.server.Server#buildServerDatabase}
 * for details.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class AccountManagingPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     * @param dbcon A connection to the server database.
     * @param modules A list of module names. The name of a module can be
     * determined using <code>themodule.getName()</code> or
     * <code>themodule.NAME</code>, which should be equivalent.
     */
    public AccountManagingPanel(Connection dbcon, String[] modules) {
        initComponents();
        this.modules = modules;
        this.dbcon = dbcon;
        tableModel = (DefaultTableModel)table.getModel();

        try {
            parent = (javax.swing.JFrame)getTopLevelAncestor();
        }
        catch(ClassCastException e) {
            parent = new javax.swing.JFrame();
        }

        try {
            stmt = dbcon.createStatement();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "<html>Konnte nicht mit der Datenbank"
                    + " verbinden.</html>", "Schwerer Netzwerkfehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        updateTableModel();
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
        jPanel1 = new javax.swing.JPanel();
        addUserButton = new javax.swing.JButton();
        deleteUserButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        changePasswordButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Benutzername", "Berechtigungen"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableeditPermissions(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        addUserButton.setText("Benutzer hinzufügen");
        addUserButton.setToolTipText("Fügt einen Benutzer hinzu.");
        addUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUser(evt);
            }
        });

        deleteUserButton.setText("Benutzer löschen");
        deleteUserButton.setToolTipText("Löscht den in der Tabelle markierten Benutzer.");
        deleteUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteUser(evt);
            }
        });

        jLabel1.setText("Zum Bearbeiten der Berechtigungen eines Benutzers auf eine Zeile der Tabelle doppelklicken.");

        changePasswordButton.setText("Passwort ändern");
        changePasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePassword(evt);
            }
        });

        refreshButton.setText("Aktualisieren");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(addUserButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 309, Short.MAX_VALUE)
                            .addComponent(changePasswordButton)
                            .addGap(18, 18, 18)
                            .addComponent(deleteUserButton)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(49, 49, 49)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(refreshButton)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addUserButton)
                    .addComponent(deleteUserButton)
                    .addComponent(changePasswordButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addUser(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUser

        //ask for username
        String username = JOptionPane.showInputDialog(this, "Benutzername:",
                "", JOptionPane.QUESTION_MESSAGE);

        if(username == null)
            return;

        //check if user attempts to use forbidden characters
        if(!Util.checkInput(username))
            return;

        //check if user already exists
        try {
            ResultSet user = stmt.executeQuery("SELECT username FROM accounts "
                    + "WHERE username = '"+username+"'");
            if(user.next()) {
                JOptionPane.showMessageDialog(this, "Benutzer bereits vorhanden",
                        "Eingabefehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //ask for password
        String password = askForNewPassword();

        if(password == null)
            return;

        //update database
        try {
            stmt.executeUpdate("INSERT INTO accounts VALUES ('"+username+"', "
                    + "'"+password+"')");
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //open permission management dialog
        AccountEditingDialog dia = new AccountEditingDialog(parent, true,
                modules, dbcon, username);
        dia.setVisible(true);

        //update table
        updateTableModel();
}//GEN-LAST:event_addUser

    private void deleteUser(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteUser

        //get selected user
        int selectedIndex = table.convertRowIndexToModel(table.getSelectedRow());
        if(selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Kein Benutzer ausgewählt.",
                    "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = (String)table.getValueAt(selectedIndex, 0);

        //if user is admin, refuse to delete
        if(username.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Der Benutzer 'admin' kann"
                    + " nicht entfernt werden.", "Löschen nicht möglich",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //confim deletion
        int option = JOptionPane.showConfirmDialog(this, "Benutzer wirklich"
                + " löschen?", "Bestätigen", JOptionPane.WARNING_MESSAGE);

        if(option != JOptionPane.YES_OPTION)
            return;

        //delete from databases
        try {
            stmt.executeUpdate("DELETE FROM accounts WHERE username = '"+
                    username+"'");
            stmt.executeUpdate("DELETE FROM permissions WHERE username = '"+
                    username+"'");
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //update table model
        updateTableModel();
}//GEN-LAST:event_deleteUser

    private void changePassword(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePassword

        //determine username
        int selectedIndex = table.convertRowIndexToModel(table.getSelectedRow());
        if(selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Kein Benutzer ausgewählt.",
                    "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = (String)table.getValueAt(selectedIndex, 0);

        //ask for old password

        String oldPwHashDatabase;
        String oldPwHashEntered;

        try {
            ResultSet password = stmt.executeQuery("SELECT hash FROM accounts"
                    + " WHERE username = '"+username+"'");
            if(!password.next()) {
                JOptionPane.showMessageDialog(this, "Benutzer nicht bekannt.",
                        "Datenbankfehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            oldPwHashDatabase = password.getString("hash");
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        do {
            String oldPw = JOptionPane.showInputDialog(this, "Altes Passwort:",
                    "", JOptionPane.QUESTION_MESSAGE);
            if(oldPw == null)
                return;

            try {
                oldPwHashEntered = Passwords.encryptPassword(oldPw);
            } catch(NoSuchAlgorithmException e) {
                JOptionPane.showMessageDialog(this, "Konnte"
                        + " MD5-Verschlüsselungsalgorithmus nicht laden.",
                        "Schwerer Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(!oldPwHashDatabase.equals(oldPwHashEntered)) {
                JOptionPane.showMessageDialog(this, "Falsches Passwort",
                        "", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            break;
        }
        while(true);

        //ask for new password
        String newPw = askForNewPassword();

        if(newPw == null)
            return;

        //write new password to database
        try {
            stmt.executeUpdate("UPDATE accounts SET hash = '"+newPw+"' WHERE"
                    + " username = '"+username+"'");
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }//GEN-LAST:event_changePassword

    private void refresh(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh
        updateTableModel();
}//GEN-LAST:event_refresh

    private void tableeditPermissions(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableeditPermissions
        if(!(evt.getButton() == java.awt.event.MouseEvent.BUTTON1 &&
                evt.getClickCount() == 2))
            return;

        //get selected user
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        if(row < 0)
            return;

        String username = (String)table.getValueAt(row, 0);

        //if selected user is admin, editing is pointless
        if(username.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Die Berechtigungen des Benutzers"
                    + " 'admin' können nicht geändert werden.",
                    "Änderung nicht möglich", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //let user edit permissions
        AccountEditingDialog dia = new AccountEditingDialog(
                parent, true, modules, dbcon, username);
        dia.setVisible(true);

        //update table model
        updateTableModel();
}//GEN-LAST:event_tableeditPermissions


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addUserButton;
    private javax.swing.JButton changePasswordButton;
    private javax.swing.JButton deleteUserButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
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
    DefaultTableModel tableModel;

    String[] modules;

    javax.swing.JFrame parent;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Asks the user to enter a new password twice.
     * @return An MD5 hash of the new password, or <code>null</code> if the
     * action was cancelled by the user.
     */
    private String askForNewPassword() {

        do {
            String newPw1 = JOptionPane.showInputDialog(this, "Neues Passwort:",
                    "", JOptionPane.QUESTION_MESSAGE);

            if(newPw1 == null)
                return null;

            String newPw2 = JOptionPane.showInputDialog(this, "Neues Passwort"
                    + " wiederholen:", "", JOptionPane.QUESTION_MESSAGE);

            if(newPw1 == null)
                return null;

            if(!newPw1.equals(newPw2)) {
                JOptionPane.showMessageDialog(this, "Passwörter stimmen nicht"
                        + " überein.", "", JOptionPane.ERROR_MESSAGE);
                    continue;
            }

            String newPwHash;
            try {
                newPwHash = Passwords.encryptPassword(newPw1);
            }
            catch(NoSuchAlgorithmException e) {
                JOptionPane.showMessageDialog(this, "Konnte"
                        + " MD5-Verschlüsselungsalgorithmus nicht laden.",
                        "Schwerer Fehler", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            return newPwHash;

        } while(true);
    }

    /**
     * Updates this GUI's table to reflect the state of 'accounts' and
     * 'permissions' tables.
     */
    private void updateTableModel() {

        //clear old model
        tableModel.setRowCount(0);


        //generate the new one
        try {
            ResultSet usernames = stmt.executeQuery("SELECT DISTINCT username"
                    + " FROM accounts ORDER BY username");

            LinkedList<String> users = new LinkedList<String>();
            while(usernames.next())
                users.add(usernames.getString("username"));

            for(String username : users) {
                String permissionString = "";

                ResultSet permissions = stmt.executeQuery("SELECT DISTINCT"
                        + " module FROM permissions WHERE username = '"+
                        username+"'");
                if(permissions.next())
                    permissionString += permissions.getString("module");

                while(permissions.next())
                    permissionString += ", "+permissions.getString("module");

                tableModel.addRow(new String[] {
                   username, permissionString
                });
            }


        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }

    //============================INNER CLASSES===============================//
}