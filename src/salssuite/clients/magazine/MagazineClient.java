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


import java.awt.event.WindowAdapter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import salssuite.util.Constants;
import salssuite.clients.ConnectDialog;
import salssuite.clients.Converter;
import salssuite.server.module.MagazineModule;
import salssuite.util.gui.HelpBrowser;

/**
 * Client for management of the magazine for the project. The magazine is
 * responsible for delivering all sorts of wares to the companies. This works
 * as follows:
 * <ol>
 * <li>
 * A list of wares which the magazine offers is created before the project
 * starts.
 * </li>
 * <li>
 * On every day (possibly multiple times) companies come to the magazine and
 * order certain wares.
 * </li>
 * <li>
 * The magazine drives to a local super market and buys the wares.
 * </li>
 * <li>
 * The wares are delivered to the companies.
 * </li>
 * </ol>
 *
 * The magazine client reflects these processes in its three panels. This class
 * is therefore mainly a wrapper for the panels with a {@link javax.swing.JTabbedPane}
 * inside.
 * <p>
 * Using the panels, the user
 * (that is: the magazine employee) can create a list of wares or it can be
 * imported when the project is set up. Afterwards orders can be received and
 * a shopping list can be created containing all the ordered wares.
 * <p>
 * For detailed documentation on those steps, see the individual panels
 * responsible for certain aspects of the client.
 * @author Jannis Limperg
 * @version 1.0
 * @see MagazineClientOrderPanel
 * @see MagazineClientShoppingListPanel
 * @see MagazineClientWarePanel
 * @see salssuite.server.module.MagazineModule
 */
public class MagazineClient extends javax.swing.JFrame {

    public static final long serialVersionUID=1;

    /**
     * Sole constructor.
     */
    public MagazineClient() {
        initComponents();

        int port = -1;
        String serverAddress = null;

        //connect to server
        String[] server;
        do {
           server = ConnectDialog.showConnectDialog(this,
                    MagazineModule.NAME);
            serverAddress = server[0];
            port = Integer.parseInt(server[1]);


            try {
                DriverManager.setLoginTimeout(10);
                dbcon = DriverManager.getConnection("jdbc:derby://"+
                        serverAddress + ":" +
                        port +
                        "/general;"+
                        "user="+server[2]+";"+
                        "password="+server[3]);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Konnte nicht mit der Datenbank" +
                        " verbinden.",
                        "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                serverAddress = null;
                e.printStackTrace();
                continue;
            }
        } while(serverAddress == null);


        try {
            //add GUI elements
            MagazineClientWarePanel warePanel = new MagazineClientWarePanel(this);
            addTab(warePanel, "Waren");
            MagazineClientOrdersPanel ordersPanel = new MagazineClientOrdersPanel(this);
            addTab(ordersPanel, "Bestellungen");
            MagazineClientShoppingListPanel shoppingListPanel =
                    new MagazineClientShoppingListPanel(this);
            addTab(shoppingListPanel, "Einkaufsliste");

            pack();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, "Konnte Daten nicht initialisieren." +
                    " Beende die Anwendung...",
                    "Kritischer Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        //usability tweaks
        addWindowListener(new WindowAdapter() {
            @Override
           public void windowClosing(java.awt.event.WindowEvent evt) {
               Preferences node = Constants.magazineClientNode;
               node.putInt("window.x", getX());
               node.putInt("window.y", getY());
               node.putInt("window.width", getWidth());
               node.putInt("window.height", getHeight());
           }
        });

        Preferences node = Constants.magazineClientNode;
        setLocation(node.getInt("window.x", 300), node.getInt("window.y", 200));
        setSize(node.getInt("window.width", (int)getPreferredSize().getWidth()),
                node.getInt("window.height", (int)getPreferredSize().getHeight()));

        //display Converter
        Converter.displayNameToIDClient(this, server[0], Integer.parseInt(
                server[1]), server[2], server[3]);
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SalSSuite - Warenlager");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exit(evt);
            }
        });

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(730, 630));

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
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exit(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exit
        System.exit(0);
    }//GEN-LAST:event_exit

    private void help(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_help
        new HelpBrowser("MagazineClient").setVisible(true);
    }//GEN-LAST:event_help

    /**
     * Creates a new <code>MagazineClient</code> and displays it.
     * @param args Command line arguments are not supported.
     */
    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){}

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MagazineClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Connection dbcon;

    PrintWriter out;
    Scanner in;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//


    //DATABASE

    /**
     * Returns this client's connection to the magazine database. The connection
     * should be used by all parts (that is: panels) of this client to connect
     * to the database.
     * @return The connection.
     */
    public Connection getDatabaseConnection() {
        return dbcon;
    }

    //GUI

    /**
     * Adds one panel to the {@link javax.swing.JTabbedPane} enclosed in this frame. Note
     * that the panel should have a size of 730x630px to fit into the client
     * GUI best.
     * @param panel The panel to be added.
     * @param description A description which appears on the tab.
     * @see javax.swing.JTabbedPane#addTab
     */
    public void addTab(JPanel panel, String description) {
        jTabbedPane1.addTab(description, panel);
        pack();
    }

    /**
     * Removes a panel from the list of panels shown by this GUI.
     * @param panel The panel to be removed.
     * @see javax.swing.JTabbedPane#remove
     */
    public void removeTab(JPanel panel) {
        jTabbedPane1.remove(panel);
    }

    //============================INNER CLASSES===============================//
}
