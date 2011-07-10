/*
 * Converter.java
 *
 * Created on 11.01.2010, 13:56:43
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import salssuite.util.Util;

/**
 * Converts various attributes of companies and citizens into each other.
 *
 * <p>For citizens, this form allows the user to enter a citizen ID and
 * obtain the citizen's forename and surname, or to enter a surname and forename
 * and obtain the ID.</p>
 *
 * <p>For companies, the conversion happens between ID, company name and
 * product description.</p>
 * @author Jannis Limperg
 * @version 1.0
 */
public class Converter extends JFrame {

    private static final long serialVersionUID=1;

    //=================================FIELDS=================================//

    int citizenID = -1;
    String citizenForename = null;
    String citizenSurname = null;

    Connection dbcon;
    Statement stmt;

    //===============================CONSTRUCTOR==============================//

    /**
     * Creates a standalone <code>Converter</code> which asks the user for the server to
     * connect to. You would possibly rather use the
     * constructor {@link #Converter(JFrame parent)} to let the client behave
     * like a dialog.
     */
    public Converter() {
        initComponents();
        helpPanel.setCaretPosition(0);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectToServer(null, -1, null, null);
    }

    /**
     * Creates a dialog-like <code>Converter</code> which pops up next to
     * the parent window. It is disposed if the latter is closed. This client will ask
     * the user for a server to connect to.
     * @param parent The parent frame of this <code>Converter</code>.
     */
    public Converter(JFrame parent) {
        parent.addWindowListener(new WindowAdapter() {
            public void windowClosing(Event evt) {
                dispose();
            }
        });
        initComponents();
        helpPanel.setCaretPosition(0);
        setLocation((int)(parent.getLocation().getX()+parent.getPreferredSize().
                getWidth()),
                (int)parent.getLocation().getY());
        connectToServer(null, -1, null, null);
    }

    /**
     * Creates a standalone <code>Converter</code>. You would possibly rather use the
     * constructor {@link #Converter(JFrame parent, String serverAddress,
     * int port, String userName, String password)} to let the client behave
     * like a dialog.
     * @param serverAddress The internet address of the server the client should
     * connect to. If <code>null</code>, the client asks the user.
     * @param port The port the server listens on.
     * @param userName The user name required for authentication with the database.
     * @param password The password required for authentication with the database.
     */
    public Converter(String serverAddress, int port, String userName,
            String password) {
        initComponents();
        helpPanel.setCaretPosition(0);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectToServer(serverAddress, port, userName, password);
    }


    /**
     * Creates a dialog-like <code>Converter</code> which pops up just to the
     * right of the parent frame to provide a good working environment for the user.
     * The client behaves like a dialog, which means it disposes when closing
     * the parent window.
     * @param parent The parent of this <code>Converter</code>.
     * @param serverAddress The internet address of the server the client should
     * connect to. If <code>null</code>, the client asks the user.
     * @param port The port the server listens on.
     * @param userName The user name required for authentication with the database.
     * @param password The password required for authentication with the database.
     */
    public Converter(JFrame parent, String serverAddress, int port,
            String userName, String password) {
        parent.addWindowListener(new WindowAdapter() {
            public void windowClosing(Event evt) {
                dispose();
            }
        });
        initComponents();
        helpPanel.setCaretPosition(0);
        setLocation((int)(parent.getLocation().getX()+parent.getPreferredSize().
                getWidth()),
                (int)parent.getLocation().getY());
        connectToServer(serverAddress, port, userName, password);
    }

    //================================METHODS=================================//

    /**
     * Displays an error message to the user using the citizenFeedbackDisplay.
     * @param message A message string to be displayed.
     */
    private void displayCitizenError(String message) {
        citizenFeedbackDisplay.setForeground(Color.RED);
        citizenFeedbackDisplay.setText(message);
    }

    /**
     * Resets the citizenDisplayFeedback to its original state.
     */
    private void resetCitizenErrors() {
        citizenFeedbackDisplay.setForeground(Color.GREEN);
        citizenFeedbackDisplay.setText("ID oder Name eingeben und <Enter> drücken");
    }

    /**
     * Displays an error message to the user using the companyFeedbackDisplay.
     * @param message A message string to be displayed.
     */
    private void displayCompanyError(String message) {
        companyFeedbackDisplay.setForeground(Color.RED);
        companyFeedbackDisplay.setText(message);
    }

    /**
     * Resets the citizenDisplayFeedback to its original state.
     */
    private void resetCompanyErrors() {
        companyFeedbackDisplay.setForeground(Color.GREEN);
        companyFeedbackDisplay.setText("ID, Betriebsname oder Produkt eingeben"
                + " und < Enter> drücken");
    }

    /**
     * Checks if the input of the citizen fields is valid. Note that this method
     * does not query the database for actual data about the chosen citizen.
     * @return true if the input is valid, false if not.
     */
    private boolean parseCitizenInput() {
        try {
            citizenFeedbackDisplay.setText("");

            if(citizenIDInput.getText().length() == 0)
                citizenID = -1;
            else {
                citizenID = Integer.parseInt(citizenIDInput.getText());
                if(citizenID <= 0)
                    throw new NumberFormatException();
            }
                

            citizenSurname = citizenSurnameInput.getText();
            citizenForename = citizenForenameInput.getText();

            //check if user attempts to use forbidden characters
            if(!Util.checkInput(citizenSurname) || !Util.checkInput(citizenForename))
                return false;

            return true;
        }
        catch (NumberFormatException e) {
            citizenID = -1; citizenForename = null; citizenSurname = null;
            citizenIDInput.setText("");
            citizenIDInput.requestFocus();
            citizenForenameInput.setText("");
            citizenSurnameInput.setText("");
            displayCitizenError("Ungültige Eingabe");
            return(false);
        }
    }//end parseCitizenInput()


    /**
     * Attempts to connect to the given server, using the given credentials.
     * A call to this method will set the global fields dbcon and stmt if
     * a connection to the server can be established.
     * @param addr The server's IP address or hostname.
     * @param port The port on which the Derby server listens.
     * @param userName The username to use when connecting to the Derby server.
     * @param password The password to use when connecting to the Derby server.
     */
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        citizenIDInput = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        citizenSurnameInput = new javax.swing.JTextField();
        citizenFeedbackDisplay = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        citizenForenameInput = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        companyIDInput = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        companyNameInput = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        companyProductInput = new javax.swing.JTextField();
        companyFeedbackDisplay = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        helpPanel = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bürger- und Betriebs-Konverter");

        jLabel1.setText("ID");

        citizenIDInput.setToolTipText("ID des Bürgers eingeben und Enter drücken.");
        citizenIDInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSent(evt);
            }
        });
        citizenIDInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAll(evt);
            }
        });

        jLabel3.setText("Nachname");

        citizenSurnameInput.setToolTipText("Vorname und Nachname des Bürgers eintippen und Enter drücken.");
        citizenSurnameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSent(evt);
            }
        });
        citizenSurnameInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAll(evt);
            }
        });

        citizenFeedbackDisplay.setForeground(java.awt.Color.green);
        citizenFeedbackDisplay.setText("ID oder Name eingeben und <Enter> drücken");
        citizenFeedbackDisplay.setMaximumSize(new java.awt.Dimension(500, 20));
        citizenFeedbackDisplay.setPreferredSize(new java.awt.Dimension(350, 20));

        jLabel2.setText("Vorname");

        citizenForenameInput.setToolTipText("Vorname und Nachname des Bürgers eintippen und Enter drücken.");
        citizenForenameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSent(evt);
            }
        });
        citizenForenameInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAll(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(24, 24, 24))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(citizenSurnameInput, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                            .addComponent(citizenIDInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                            .addComponent(citizenForenameInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)))
                    .addComponent(citizenFeedbackDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(citizenIDInput, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(citizenForenameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(citizenSurnameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(citizenFeedbackDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {citizenForenameInput, citizenSurnameInput});

        jTabbedPane1.addTab("Bürger", jPanel5);

        jLabel4.setText("ID");

        companyIDInput.setToolTipText("ID eines Betriebs eingeben und <Enter> drücken");
        companyIDInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyIDEntered(evt);
            }
        });
        companyIDInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAll(evt);
            }
        });

        jLabel5.setText("Name");

        companyNameInput.setToolTipText("Name eines Betriebs eingeben und <Enter> drücken");
        companyNameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyNameEntered(evt);
            }
        });
        companyNameInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAll(evt);
            }
        });

        jLabel6.setText("Produkt");

        companyProductInput.setToolTipText("Produktbeschreibung eines Betriebs eingeben und <Enter> drücken");
        companyProductInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyProductEntered(evt);
            }
        });
        companyProductInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAll(evt);
            }
        });

        companyFeedbackDisplay.setForeground(java.awt.Color.green);
        companyFeedbackDisplay.setText("ID, Betriebsname oder Produkt eingeben und < Enter> drücken");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(companyFeedbackDisplay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(companyIDInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                            .addComponent(companyNameInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                            .addComponent(companyProductInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(companyIDInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(companyNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(companyProductInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(companyFeedbackDisplay)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {companyIDInput, companyNameInput, companyProductInput});

        jTabbedPane1.addTab("Betriebe", jPanel6);

        helpPanel.setContentType("text/html");
        helpPanel.setEditable(false);
        helpPanel.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <h1>Verwendung des Konverters</h1>\n    <h2>ID-Eingabefelder</h2>\n    <p>Bei den beiden ID-Feldern (ID des Bürgers oder ID des Betriebs) genügt es, eine Zahl einzugeben und &lt;Enter&gt; zu drücken. Der Konverter wird dann nach den entsprechenden Daten für die anderen Felder suchen und diese gegebenenfalls anzeigen.\n    </p>\n    <h2>Text-Eingabefelder</h2>\n    <p>Bei allen anderen Feldern muss Text eingegeben werden. Dieser Text kann an irgendeiner Stelle der zugehörigen Werte des Bürgers bzw. Betriebs vorkommen. Beispiel: Wird in das Feld für den Vornamen eines Bürgers \"Rich\" eingegeben, so findet der Konverter die Bürger mit den Vornamen \"<b>Rich</b>ard\" und \"Hein<b>rich</b>\". Groß- und Kleinschreibung spielt hierbei keine Rolle.\n    </p>\n    <p>Dabei kann es natürlich vorkommen, dass eine Suchanfrage mehrere Bürger findet. Beispielsweise kann die Anfrage \"Vorname Marc und Nachname Schneider\" sowohl Marcel als auch Marc Schneider finden. In dem Fall gibt der Konverter unten eine Warnung aus und Sie sollten die Anfrage genauer spezifizieren.\n    </p>\n    <p>Wenn Sie die ID zu einem Bürger suchen, werden die beiden Felder \"Vorname\" und \"Nachname\" berücksichtigt. Bei der Suche nach einem Betrieb wird immer nur das Eingabefeld berücksichtigt, in dem Sie &lt;Enter&gt; drücken.\n    </p>\n  </body>\n</html>\n");
        jScrollPane1.setViewportView(helpPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Hilfe", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Queries the server for the citizen input recently typed in.
     * @param evt The action event allocated to this call.
     */
    private void inputSent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSent
        if(!parseCitizenInput())
            return;

        // CASE 1: Determine name for given citizenID
        if(evt.getSource() == citizenIDInput) {
            //erase all the fields
            citizenForenameInput.setText("");
            citizenSurnameInput.setText("");

            if(citizenIDInput.getText().length() == 0)
                return;

            //query the server for the citizen's name
            try {
                ResultSet citizen = stmt.executeQuery("SELECT forename, surname FROM" +
                        " citizens WHERE id = "+Integer.parseInt(citizenIDInput.getText()));

                if(!citizen.next()) {
                    displayCitizenError("Bürger nicht gefunden");
                    return;
                }

                citizenSurnameInput.setText(citizen.getString("surname"));
                citizenForenameInput.setText(citizen.getString("forename"));
                citizenIDInput.requestFocus();
                citizenIDInput.selectAll();
                resetCitizenErrors();
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation "
                        + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }

        // CASE 2: Determine citizenID for given forename and surname.
        else if (evt.getSource() == citizenSurnameInput || evt.getSource() == citizenForenameInput) {
            citizenIDInput.setText("");
            
            //Query the server for the citizen's ID
            try {
                //Note that the queries do not search for exakt matches but
                //use LIKE statements.
                ResultSet citizen = stmt.executeQuery("SELECT id, forename, surname" +
                        " FROM citizens" +
                        " WHERE LOWER(forename) LIKE LOWER("
                        + "'"+citizenForename+"%') AND LOWER(surname) LIKE LOWER('" +
                        citizenSurname+"%')");

                if(!citizen.next()) {
                    displayCitizenError("Kein Bürger dieses Namens gefunden");
                    return;
                }

                citizenIDInput.setText(citizen.getString("id"));
                citizenForenameInput.setText(citizen.getString("forename"));
                citizenSurnameInput.setText(citizen.getString("surname"));
                citizenForenameInput.requestFocus();
                citizenForenameInput.selectAll();
                resetCitizenErrors();

                if(citizen.next())
                    displayCitizenError("Warnung: Mehrere Bürger dieses Namens");
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation "
                        + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }
    }//GEN-LAST:event_inputSent

    private void companyIDEntered(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyIDEntered
        //erase all the other fields
        companyNameInput.setText("");
        companyProductInput.setText("");

        //parse the company's ID
        int companyID;

        try {
            companyID = Integer.parseInt(companyIDInput.getText());
            if(companyID <= 0)
                throw new NumberFormatException();
        }
        catch(NumberFormatException e) {
            displayCompanyError("Ungültige Eingabe");
            return;
        }

        //query the database
        try {
            ResultSet company = stmt.executeQuery("SELECT name, productDescription"
                    + " FROM companies WHERE id = "+companyID);
            if(!company.next())
                displayCompanyError("Betrieb nicht gefunden");
            else {
                companyNameInput.setText(company.getString("name"));
                companyProductInput.setText(company.getString("productDescription"));
                resetCompanyErrors();
            }

        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        companyIDInput.requestFocus();
        companyIDInput.selectAll();
    }//GEN-LAST:event_companyIDEntered

    private void companyNameEntered(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyNameEntered
        //erase all the other fields
        companyIDInput.setText("");
        companyProductInput.setText("");

        String companyName = companyNameInput.getText();
        
        //check if the user attempts to use forbitten characters
        if(!Util.checkInput(companyName))
            return;

        //query the database
        try {
            ResultSet company = stmt.executeQuery("SELECT id, productDescription,"
                    + "name FROM companies WHERE LOWER(name) LIKE "
                    + "LOWER('%"+companyName+"%')");
            if(company.next()) {
                companyIDInput.setText(company.getString("id"));
                companyProductInput.setText(company.getString("productDescription"));
                companyNameInput.setText(company.getString("name"));
                resetCompanyErrors();

                if(company.next())
                    displayCompanyError("Warnung: Mehrere Betriebe dieses Namens");
            }
            else
                displayCompanyError("Kein Betrieb dieses Namens gefunden");

            companyNameInput.requestFocus();
            companyNameInput.selectAll();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }//GEN-LAST:event_companyNameEntered

    private void companyProductEntered(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyProductEntered
        //erase all the other fields
        companyNameInput.setText("");
        companyIDInput.setText("");

        String product = companyProductInput.getText();

        //check if the user attempts to use forbidden characters
        if(!Util.checkInput(product))
            return;

        //query the database
        try {
            ResultSet company = stmt.executeQuery("SELECT id, name,"
                    + "productDescription FROM companies WHERE"
                    + " LOWER(productDescription) LIKE LOWER('%"+product+"%')");
            if(company.next()) {
                companyIDInput.setText(company.getString("id"));
                companyNameInput.setText(company.getString("name"));
                companyProductInput.setText(company.getString("productDescription"));
                resetCompanyErrors();

                if(company.next())
                    displayCompanyError("Warnung: Mehrere Betriebe mit diesem Produkt");
            }
            else
                displayCompanyError("Kein Betrieb mit diesem Produkt gefunden");

            companyProductInput.requestFocus();
            companyProductInput.selectAll();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }//GEN-LAST:event_companyProductEntered

    private void selectAll(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_selectAll
        ((JTextField)evt.getSource()).selectAll();
    }//GEN-LAST:event_selectAll

    /**
     * Creates a new standalone client and displays it.
     * @param args Command line arguments are not supported.
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {}

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Converter().setVisible(true);
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
     * Creates a <code>Converter</code> behaving like a dialog towards the
     * <code>JFrame parent</code> and displays it. Cf. constructor
     * {@link #Converter(JFrame parent)}.
     * @param parent The parent for this <code>Converter</code>.
     */
    public static void displayNameToIDClient(final JFrame parent) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Converter(parent).setVisible(true);
            }
        });
    }//end displayNameToIDClient() II

    /**
     * Creates a <code>Converter</code> behaving like a dialog towards <code>parent</code>.
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
                new Converter(parent, serverAddr, port, userName, password).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel citizenFeedbackDisplay;
    private javax.swing.JTextField citizenForenameInput;
    private javax.swing.JTextField citizenIDInput;
    private javax.swing.JTextField citizenSurnameInput;
    private javax.swing.JLabel companyFeedbackDisplay;
    private javax.swing.JTextField companyIDInput;
    private javax.swing.JTextField companyNameInput;
    private javax.swing.JTextField companyProductInput;
    private javax.swing.JEditorPane helpPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}//end class
