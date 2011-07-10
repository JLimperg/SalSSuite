/*
 * DutyClient.java
 *
 * Created on 14.02.2010, 18:25:19
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

package salssuite.clients.duty;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import salssuite.util.Constants;
import salssuite.clients.Converter;
import salssuite.clients.ConnectDialog;
import salssuite.server.module.DutyModule;
import salssuite.util.Util;
import salssuite.util.gui.HelpBrowser;

/**
 * The client for logging in and out citizens. It is meant to be used by the
 * state's 'duty officers'.
 * @author Jannis Limperg
 * @version 1.0
 * @see salssuite.server.module.DutyModule
 */
public class DutyClient extends javax.swing.JFrame {

    private static final long serialVersionUID=1;

    /**
     * Sole constructor.
     */
    public DutyClient() {
        initComponents();

        //usability
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {}


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Constants.dutyClientNode.putInt("window.x", getX());
                Constants.dutyClientNode.putInt("window.y", getY());
                System.exit(0);
            }
        });


        setLocation(Constants.dutyClientNode.getInt("window.x", 200),
                Constants.dutyClientNode.getInt("window.y", 200));


        //setup connection to server
        String[] theserver;
        while(true) {
            theserver = ConnectDialog.showConnectDialog(this, DutyModule.
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

        //display Converter (probably useful for duty officers)
        Converter.displayNameToIDClient(this, theserver[0], Integer.parseInt(
                theserver[1]), theserver[2], theserver[3]);
        jTextField1.requestFocus();
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        display = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SalSSuite - Zoll");

        jButton1.setText("Ein-/Ausloggen");
        jButton1.setToolTipText("Den Bürger mit eingegebener ID ein- oder ausloggen.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInOut(evt);
            }
        });

        jLabel1.setText("ID");

        jTextField1.setToolTipText("Nummer des ein-/auszuloggenden Bürgers eingeben und Enter drücken");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInOut(evt);
            }
        });

        jButton2.setText("Hilfe!");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                help(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(display, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(display, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logInOut(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInOut
            parseID();

            if(ID == -1)
                return;

            logInOut(ID);
    }//GEN-LAST:event_logInOut

    private void help(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_help
        new HelpBrowser("DutyClient").setVisible(true);
}//GEN-LAST:event_help

    /**
     * Creates a standalone <code>DutyClient</code> and displays it.
     * @param args the command line arguments
     */
    public static void main(String args[]){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {

        } catch (InstantiationException ex) {

        } catch (IllegalAccessException ex) {

        } catch (UnsupportedLookAndFeelException ex) {
            
        }
        
        new DutyClient().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel display;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables


    //===================================OWN CODE=============================//

    //FIELDS
    int ID = -1;

    Connection dbcon;
    Statement stmt;

    //METHODS

    private void parseID() {
        try {
            ID = Integer.parseInt(jTextField1.getText());
            if(ID <= 0) {
                displayError("ID muss positiv sein");
                ID = -1;
            }
            jTextField1.selectAll();
            jTextField1.requestFocus();
        }
        catch(NumberFormatException e) {
            ID = -1;
            displayError("ID muss eine ganze Zahl sein");
            jTextField1.selectAll();
            jTextField1.requestFocus();
        }
        
    }//end parseID()

    /**
     * Logs the citizen with given ID in or out, depending on their current
     * state (that is, whether they are currently logged in or out).
     * @param ID The citizen to be logged in/out.
     */
    private void logInOut(int ID){       
        try {
            //check if citi exists
            ResultSet citi = stmt.executeQuery("SELECT id FROM citizens WHERE"
                    + " id = "+ID);
            if(!citi.next()) {
                displayError("Bürger Nr. "+ID+" existiert nicht.");
                return;
            }

            //determine whether citi is currently logged in or out
            ResultSet logs = stmt.executeQuery("SELECT type FROM logs WHERE" +
                    " citizenId = "+ID);
            
            logs.last();
            
            if(logs.getRow() == 0 || logs.getInt("type") == 0) { //means citi logged out: log him in
                stmt.executeUpdate("INSERT INTO logs VALUES (" +
                        ID + ", "+
                        "'"+Util.getDateString()+"', "+
                        "'"+Util.getTimeString()+"', "+
                        "1"+
                        ")");
                display("Bürger "+ID+" eingeloggt.");
            }
            else {
                stmt.executeUpdate("INSERT INTO logs VALUES (" +
                        ID + ", "+
                        "'"+Util.getDateString()+"', "+
                        "'"+Util.getTimeString()+"', "+
                        "0"+
                        ")");
                display("Bürger "+ID+" ausgeloggt.");
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation "
                    + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

    }//end logInOut()

    /**
     * Displays an error message to the user.
     * @param message The message to be displayed.
     */
    private void displayError(String message) {
        display.setForeground(Color.RED);
        display.setText(message);
    }//end displayError()

    /**
     * Displays a normal message to the user.
     * @param message The message to be displayed.
     */
    private void display(String message) {
        display.setForeground(Color.GREEN);
        display.setText(message);
    }//end display()
}
