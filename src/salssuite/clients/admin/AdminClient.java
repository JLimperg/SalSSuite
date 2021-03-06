/*
 * AdminClient.java
 *
 * Created on 20.04.2011, 14:40:16
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

package salssuite.clients.admin;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import salssuite.clients.ConnectDialog;
import salssuite.clients.Converter;
import salssuite.server.module.gui.CitizenModulePanel;
import salssuite.server.module.gui.CompanyModulePanel;
import salssuite.server.module.gui.DutyModulePanel;
import salssuite.server.gui.AccountManagingPanel;
import salssuite.server.module.*;
import salssuite.util.Constants;
import salssuite.util.gui.HelpBrowser;
import salssuite.util.gui.PasswordPanel;

/**
 * Client that displays module panels. This client displays all module panels
 * for standard SalSSuite modules and thereby allows the user to modify
 * module data even without physical access to the server. Additionally,
 * the client provides account management functionality through an
 * <code>AccountManagingPanel</code>.
 * <p>
 * This client is especially important when the server runs in command line
 * mode, i.e. without a GUI. In that case some basic operations (for example
 * manipulating citizen data) must be done using this client as the command
 * line interface for the server is reduced to the very basis of what can be
 * called a basis.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class AdminClient extends javax.swing.JFrame {

    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     */
    public AdminClient() {
        initComponents();

        //usability
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Constants.adminClientNode.putInt("window.x", getX());
                Constants.adminClientNode.putInt("window.y", getY());
                Constants.adminClientNode.putInt("window.width", (int)getSize().
                        getWidth());
                Constants.adminClientNode.putInt("window.height", (int)getSize().
                        getHeight());

                System.exit(0);
            }
        });


        setLocation(Constants.adminClientNode.getInt("window.x", 200),
                Constants.adminClientNode.getInt("window.y", 200));
        setPreferredSize(new java.awt.Dimension(Constants.adminClientNode.getInt(
                "window.width", 775), Constants.adminClientNode.getInt(
                "window.height", 500)));

        //connect to the server
        String[] theserver;
        while(true) {
            theserver = ConnectDialog.showConnectDialog(this, AdminModule.
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

        //add all the panels
        buildGUI();

        //display Converter
        Converter.displayNameToIDClient(this, theserver[0], Integer.parseInt(
                theserver[1]), theserver[2], theserver[3]);
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoutPanel = new javax.swing.JPanel();
        logoutButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JTabbedPane();
        jButton1 = new javax.swing.JButton();

        logoutButton.setText("Logout");

        jLabel1.setText("<html>Das Drücken dieses Knopfes wird alle<p> derzeit geöffneten Module sperren.</html>");

        javax.swing.GroupLayout logoutPanelLayout = new javax.swing.GroupLayout(logoutPanel);
        logoutPanel.setLayout(logoutPanelLayout);
        logoutPanelLayout.setHorizontalGroup(
            logoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logoutButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        logoutPanelLayout.setVerticalGroup(
            logoutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SalSSuite - Administration");

        mainPanel.setPreferredSize(new java.awt.Dimension(775, 500));

        jButton1.setText("Hilfe!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
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
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void help(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_help
        new HelpBrowser("AdminClient").setVisible(true);
    }//GEN-LAST:event_help

    /**
     * Creates a new <code>AdminClient</code> and displays it.
     * @param args Command line arguments are not supported.
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {

        } catch (InstantiationException ex) {

        } catch (IllegalAccessException ex) {

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {

        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel logoutPanel;
    private javax.swing.JTabbedPane mainPanel;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    /**
     * A list of module names for the account management feature. The name
     * is defined by <code>themodule.getName()</code> or <code>themodule.NAME</code>,
     * which should be equivalent. Note that if you develop new modules, for the
     * account management feature of the admin client to work correctly they
     * have to appear in this list.
     */
    public static final String[] MODULES = {
        //FIXME Add your additional modules here.
        AccountingModule.NAME,
        AdminModule.NAME,
        CitizenModule.NAME,
        CompanyModule.NAME,
        DutyModule.NAME,
        EmploymentModule.NAME,
        MagazineModule.NAME,
        CreditModule.NAME
    };

    //===============================FIELDS===================================//

    Connection dbcon;
    Statement stmt;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    private void buildGUI() {
        mainPanel.removeAll();

        //FIXME Add new module panels here.
        mainPanel.addTab("Bürgerverwaltung",
                new PasswordPanel(dbcon, CitizenModule.NAME, new
                CitizenModulePanel(dbcon), "Bürgerverwaltung"));
        mainPanel.addTab("Betriebe",
                new PasswordPanel(dbcon, CompanyModule.NAME,
                new CompanyModulePanel(dbcon), "Betriebe"));
        mainPanel.addTab("Zoll",
                new PasswordPanel(dbcon, DutyModule.NAME,
                new DutyModulePanel(dbcon), "Zoll"));
        mainPanel.addTab("Benutzer",
                new PasswordPanel(dbcon, "all",
                new AccountManagingPanel(dbcon, MODULES), "Benutzer"));

        //add logout panel
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buildGUI();
            }
        });

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.add(logoutPanel, BorderLayout.CENTER);

        mainPanel.addTab("Logout", wrapperPanel);

        pack();
    }

    //============================INNER CLASSES===============================//
}
