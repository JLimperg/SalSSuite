/*
 * NameToIDClient.java
 *
 * Created on 11.01.2010, 13:56:43
 */

package salssuite.clients;

import java.awt.Color;
import java.awt.Event;
import java.awt.event.WindowAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import salssuite.util.Util;

/**
 * Obtains the ID of a citizen of which only the name is known
 * and vice versa. This client is meant to be distributed along with other clients so
 * the user can, if a client requires an ID but the user only knows
 * the citizen's name (or vice versa), get it using this one.
 * @author Jannis Limperg
 */
public class NameToIDClient extends JFrame {

    private static final long serialVersionUID=1;

    //=================================FIELDS=================================//

    int ID = -1;
    String forename = null;
    String surname = null;

    Connection dbcon;
    Statement stmt;

    //===============================CONSTRUCTOR==============================//

    /**
     * Creates a standalone <code>NameToIDClient</code> which asks the user for the server to
     * connect to. You would possibly rather use the
     * constructor {@link #NameToIDClient(JFrame parent)} to let the client behave
     * like a dialog.
     */
    public NameToIDClient() {
        initComponents();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {}

        connectToServer(null, -1, null, null);
    }

    /**
     * Creates a dialog-like <code>NameToIDClient</code> which pops up next to
     * the parent window. It is disposed if the latter is closed. This client will ask
     * the user for a server to connect to.
     * @param parent The parent frame of this <code>NameToIDClient</code>.
     */
    public NameToIDClient(JFrame parent) {
        parent.addWindowListener(new WindowAdapter() {
            public void windowClosing(Event evt) {
                dispose();
            }
        });
        initComponents();
        setLocation(
                (int)(parent.getLocation().getX()+parent.getSize().getWidth()),
                (int)(parent.getLocation().getY()));
        connectToServer(null, -1, null, null);
    }

    /**
     * Creates a standalone <code>NameToIDClient</code>. You would possibly rather use the
     * constructor {@link #NameToIDClient(JFrame parent, String serverAddress,
     * int port, String userName, String password)} to let the client behave
     * like a dialog.
     * @param serverAddress The internet address of the server the client should
     * connect to. If <code>null</code>, the client asks the user.
     * @param port The port the server listens on.
     * @param userName The user name required for authentication with the database.
     * @param password The password required for authentication with the database.
     */
    public NameToIDClient(String serverAddress, int port, String userName,
            String password) {
        initComponents();
        connectToServer(serverAddress, port, userName, password);
    }


    /**
     * Creates a dialog-like <code>NameToIDClient</code> which pops up just to the
     * right of the parent frame to provide a good working environment for the user.
     * The client behaves like a dialog, which means it disposes when closing
     * the parent window.
     * @param parent The parent of this <code>NameToIDClient</code>.
     * @param serverAddress The internet address of the server the client should
     * connect to. If <code>null</code>, the client asks the user.
     * @param port The port the server listens on.
     * @param userName The user name required for authentication with the database.
     * @param password The password required for authentication with the database.
     */
    public NameToIDClient(JFrame parent, String serverAddress, int port,
            String userName, String password) {
        parent.addWindowListener(new WindowAdapter() {
            public void windowClosing(Event evt) {
                dispose();
            }
        });
        initComponents();
        setLocation(
                (int)(parent.getLocation().getX()+parent.getSize().getWidth()),
                (int)(parent.getLocation().getY()));
        connectToServer(serverAddress, port, userName, password);
    }

    //================================METHODS=================================//

    //sets the infoLabel's color to be red and displays message
    private void displayError(String message) {
        infoLabel.setForeground(Color.RED);
        infoLabel.setText(message);
    }

    //returns false if an error occured.
    private boolean parseInput() {
        try {
            infoLabel.setText("");

            if(IDInput.getText().length() == 0)
                ID = -1;
            else {
                ID = Integer.parseInt(IDInput.getText());
                if(ID <= 0)
                    throw new NumberFormatException();
            }
                

            surname = surnameInput.getText();
            forename = forenameInput.getText();

            //check if user attempts to use forbidden characters
            if(!Util.checkInput(surname) || !Util.checkInput(forename))
                return false;

            return true;
        }
        catch (NumberFormatException e) {
            ID = -1; forename = null; surname = null;
            IDInput.setText("");
            IDInput.requestFocus();
            forenameInput.setText("");
            surnameInput.setText("");
            displayError("Ungültige Eingabe");
            return(false);
        }
    }//end parseInput()


    //connects to server with given address. If addr is
    //null, shows a server chooser dialog.
    private void connectToServer(String addr, int port, String userName, String password){

        if(addr == null) {
            String[] serverData = ConnectDialog.showConnectDialog(this, "duty");
            connectToServer(serverData[0], Integer.parseInt(serverData[1]),
                    serverData[2], serverData[3]);
            return;
        }

        try {
            DriverManager.setLoginTimeout(10);
            dbcon = DriverManager.getConnection("jdbc:derby://"+
                    addr + ":" +
                    port +
                    "/general;"+
                    "user="+userName+";"+
                    "password="+password);
            stmt = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Konnte nicht mit Server verbinden.",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            String[] serverData = ConnectDialog.showConnectDialog(this, "duty");
            connectToServer(serverData[0], Integer.parseInt(serverData[1]),
                    serverData[2], serverData[3]);
            return;
        }
        
    }//end connectToServer()
    //==================================GUI===================================//

    

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        IDInput = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        forenameInput = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        surnameInput = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Name zu ID");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridLayout(4, 1));

        jLabel1.setText("ID");

        IDInput.setToolTipText("ID des Bürgers eingeben und Enter drücken.");
        IDInput.setPreferredSize(new java.awt.Dimension(150, 25));
        IDInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSent(evt);
            }
        });
        IDInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                IDInputFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(74, 74, 74)
                .addComponent(IDInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(134, 134, 134))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(IDInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);

        jLabel2.setText("Vorname");

        forenameInput.setToolTipText("Vorname und Nachname des Bürgers eintippen und Enter drücken.");
        forenameInput.setPreferredSize(new java.awt.Dimension(150, 25));
        forenameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSent(evt);
            }
        });
        forenameInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                forenameInputFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(27, 27, 27)
                .addComponent(forenameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(forenameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)))
        );

        getContentPane().add(jPanel2);

        jLabel3.setText("Nachname");

        surnameInput.setToolTipText("Vorname und Nachname des Bürgers eintippen und Enter drücken.");
        surnameInput.setPreferredSize(new java.awt.Dimension(150, 25));
        surnameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSent(evt);
            }
        });
        surnameInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                surnameInputFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(surnameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(136, 136, 136))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(surnameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(jPanel3);

        infoLabel.setForeground(new java.awt.Color(46, 131, 37));
        infoLabel.setText("ID oder Name eingeben und Enter drücken");
        infoLabel.setMaximumSize(new java.awt.Dimension(500, 20));
        infoLabel.setPreferredSize(new java.awt.Dimension(350, 20));
        jPanel4.add(infoLabel);

        getContentPane().add(jPanel4);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Queries the server for the input recently typed in.
     * @param evt The action event allocated to this call.
     */
    private void inputSent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSent
        if(!parseInput())
            return;

        // CASE 1: Determine name for given ID
        if(evt.getSource() == IDInput) {
            //erase all the fields
            forenameInput.setText("");
            surnameInput.setText("");

            if(IDInput.getText().length() == 0)
                return;

            //query the server for the citizen's name
            try {
                ResultSet citizen = stmt.executeQuery("SELECT forename, surname FROM" +
                        " citizens WHERE id = "+Integer.parseInt(IDInput.getText()));

                if(!citizen.next()) {
                    JOptionPane.showMessageDialog(this, "Kein Bürger mit dieser ID" +
                            " vorhanden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                surnameInput.setText(citizen.getString("surname"));
                forenameInput.setText(citizen.getString("forename"));
                IDInput.requestFocus();
                IDInput.selectAll();
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation "
                        + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }

        // CASE 2: Determine ID for given forname and surname.
        else if (evt.getSource() == surnameInput || evt.getSource() == forenameInput) {
            IDInput.setText("");

            if(surname.length() == 0 || forename.length() == 0) {
                displayError("Vor- und Nachname angeben");
                if(surname.length() == 0)
                    surnameInput.requestFocus();
                if(forename.length() == 0)
                    forenameInput.requestFocus();
                return;
            }
            
            //Query the server for the citizen's ID
            try {
                //Note that the queries do not search for exakt matches, but
                //use LIKE statements.
                ResultSet citizen = stmt.executeQuery("SELECT id, forename, surname" +
                        " FROM citizens" +
                        " WHERE LOWER(forename) LIKE LOWER("
                        + "'"+forename+"%') AND LOWER(surname) LIKE LOWER('" +
                        surname+"%')");

                if(!citizen.next()) {
                    JOptionPane.showMessageDialog(this, "Kein Bürger mit diesem" +
                            " Namen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                IDInput.setText(citizen.getString("id"));
                forenameInput.setText(citizen.getString("forename"));
                surnameInput.setText(citizen.getString("surname"));

                forenameInput.requestFocus();
                forenameInput.selectAll();
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation "
                        + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }
    }//GEN-LAST:event_inputSent

    private void IDInputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_IDInputFocusGained
        IDInput.selectAll();
    }//GEN-LAST:event_IDInputFocusGained

    private void forenameInputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_forenameInputFocusGained
        forenameInput.selectAll();
    }//GEN-LAST:event_forenameInputFocusGained

    private void surnameInputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_surnameInputFocusGained
        surnameInput.selectAll();
    }//GEN-LAST:event_surnameInputFocusGained

    /**
     * Creates a new standalone client and displays it.
     * @param args Command line arguments are not supported.
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NameToIDClient().setVisible(true);
            }
        });
    }

    /**
     * Creates a new standalone client and displays it.
     */
    public static void displayNameToIDClient() {
        main(null);
    }//end displayNameToIDClient() I


    /**
     * Creates a <code>NameToIDClient</code> behaving like a dialog towards the
     * <code>JFrame parent</code> and displays it. Cf. constructor
     * {@link #NameToIDClient(JFrame parent)}.
     * @param parent The parent for this <code>NameToIDClient</code>.
     */
    public static void displayNameToIDClient(final JFrame parent) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NameToIDClient(parent).setVisible(true);
            }
        });
    }//end displayNameToIDClient() II

    /**
     * Creates a <code>NameToIDClient</code> behaving like a dialog towards <code>parent</code>.
     * It automatically connects to the server with IP <code>serverAddr</code>,
     * listening on port <code>port</code>. If connecting fails, the user is
     * asked to give other server information.
     * @param parent This dialog's parent frame.
     * @param serverAddr The internet address of the server the client should
     * connect to. If <code>null</code>, the client asks the user.
     * @param port The port the server listens on.
     * @param userName The user name required for authentication with the database.
     * @param password The password required for authentication with the database.
     */
    public static void displayNameToIDClient(final JFrame parent, final String serverAddr,
            final int port, final String userName, final String password) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NameToIDClient(parent, serverAddr, port, userName, password).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField IDInput;
    private javax.swing.JTextField forenameInput;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField surnameInput;
    // End of variables declaration//GEN-END:variables

}//end class
