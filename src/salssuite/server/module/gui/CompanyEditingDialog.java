/*
 * CompanyEditingDialog.java
 *
 * Created on 10.05.2010, 20:59:16
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

package salssuite.server.module.gui;

import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import salssuite.util.Employee;
import salssuite.util.Util;
import salssuite.util.gui.ExceptionDisplayDialog;

/**
 * Lets the user edit or create a company. This dialog reflects one line of
 * the 'companies' table in the general database.
 * See {@link salssuite.server.Server#buildServerDatabase} for the table structure.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class CompanyEditingDialog extends javax.swing.JDialog {

    private static final long serialVersionUID=1;

    /**
     * Sole constructor. If an existing company should be edited,
     * you can pass its ID to the dialog, otherwise just pass <code>-1</code>.
     * @param parent This dialog's parent frame.
     * @param companyID The ID of the company to be edited, or <code>-1</code> if
     * a new one should be created.
     * @param databaseConnection A connection to the database
     * holding the 'companies' table.
     * @param modal Whether this dialog should be modal or not.
     * @see salssuite.server.Server#buildServerDatabase
     */
    public CompanyEditingDialog(java.awt.Frame parent, boolean modal, int companyID,
            Connection databaseConnection) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        this.companyID = companyID;
        this.parent = parent;
        dbcon = databaseConnection;

        try {
            stmt = dbcon.createStatement();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Konnte keine Verbindung mit" +
                    " der Datenbank herstellen.", "Netzwerkfehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if(companyID < 0) {
            setTitle("SalSSuite - Neuen Betrieb erstellen");
            employeeList.setModel(listModel);
            return;
        }

        setTitle("SalSSuite - Betrieb bearbeiten");

        try {
            ResultSet company = stmt.executeQuery("SELECT * FROM companies " +
                    "WHERE id = "+companyID);
            company.next();

            Statement stmt2 = dbcon.createStatement();

            ResultSet boss = stmt2.executeQuery("SELECT id, surname, forename," +
                    " salary FROM citizens WHERE companyId = "+companyID+" AND" +
                    " isBoss = 1");

            if(!boss.next()) {
                JOptionPane.showMessageDialog(parent, "Chef der Firma nicht gefunden."
                        , "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                founderIDInput.setText("-1");
                founderSalaryInput.setText("0");
            }
            else {
                founderIDInput.setText(""+boss.getInt("id"));
                founderNameDisplay.setText("("+boss.getString("forename")+" "+
                        boss.getString("surname")+")");
                founderSalaryInput.setText(""+boss.getDouble("salary"));
            }

            jobsInput.setText(""+company.getInt("jobs"));
            nameInput.setText(company.getString("name"));
            roomInput.setText(company.getString("room"));
            productDescriptionInput.setText(company.getString("productDescription"));

            employeeList.setModel(listModel);

            ResultSet employees = stmt2.executeQuery("SELECT id, forename, surname," +
                    "salary FROM citizens WHERE companyId = "+companyID+" AND isBoss" +
                    " = 0");

            while(employees.next()) {
                String descr = "";
                descr += employees.getInt("id")+ " | ";
                descr += employees.getString("forename") + " ";
                descr += employees.getString("surname") + " | ";
                descr += employees.getDouble("salary");
                listModel.addElement(descr);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Zu bearbeitende Firma nicht" +
                    " gefunden.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            dispose();
            return;
        }
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
        nameInput = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        roomInput = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        founderIDInput = new javax.swing.JTextField();
        founderNameDisplay = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        employeeList = new javax.swing.JList();
        jLabel10 = new javax.swing.JLabel();
        founderSalaryInput = new javax.swing.JTextField();
        addEmloyeeButton = new javax.swing.JButton();
        deleteEmployeeButton = new javax.swing.JButton();
        editEmployeeButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        approveButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        productDescriptionInput = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jobsInput = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Firma bearbeiten");

        jLabel1.setText("Name");

        nameInput.setToolTipText("Der Name des Betriebs.");

        jLabel3.setText("Raum");

        roomInput.setToolTipText("Der Raum, in dem der Betrieb sich befindet.");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel4.setText("Firma");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel5.setText("Gründer");

        jLabel6.setText("Nummer");

        founderIDInput.setToolTipText("Die ID des Firmengründers.");
        founderIDInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                founderIDTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        jLabel8.setText("Mitarbeiter");

        employeeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        employeeList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editEmployee_list(evt);
            }
        });
        jScrollPane1.setViewportView(employeeList);

        jLabel10.setText("Lohn");

        founderSalaryInput.setToolTipText("Der Tageslohn des Gründers.");

        addEmloyeeButton.setText("hinzufügen");
        addEmloyeeButton.setToolTipText("Einen Mitarbeiter zur Mitarbeiterliste hinzufügen.");
        addEmloyeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployee(evt);
            }
        });

        deleteEmployeeButton.setText("löschen");
        deleteEmployeeButton.setToolTipText("Den ausgewählten Mitarbeiter löschen");
        deleteEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEmployee(evt);
            }
        });

        editEmployeeButton.setText("bearbeiten");
        editEmployeeButton.setToolTipText("Den ausgewählten Mitarbeiter bearbeiten.");
        editEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEmployee_button(evt);
            }
        });

        cancelButton.setText("Abbrechen");
        cancelButton.setToolTipText("Abbrechen und zur Firmenverwaltung zurückkehren.");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });

        jLabel7.setText("Mitarbeiter");

        approveButton.setText("Absenden");
        approveButton.setToolTipText("Die Firma speichern.");
        approveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                approve(evt);
            }
        });

        jLabel9.setText("Produktbeschreibung");

        productDescriptionInput.setToolTipText("Eine kurze Beschreibung der Produkte, die der Betrieb anbietet.");

        jLabel11.setText("maximale Anzahl Angestellte (inkl. Gründer)");

        jobsInput.setToolTipText("Anzahl der Angestellten, die dieser Betrieb maximal beschäftigen kann, inklusive Gründer.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(38, 38, 38))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(founderIDInput, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(founderNameDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(founderSalaryInput, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addEmloyeeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editEmployeeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteEmployeeButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jobsInput, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(128, 128, 128)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(roomInput, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameInput, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(productDescriptionInput, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addGap(18, 18, 18)
                        .addComponent(approveButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(roomInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(productDescriptionInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(founderIDInput))
                    .addComponent(founderNameDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(founderSalaryInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jobsInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(addEmloyeeButton)
                    .addComponent(editEmployeeButton)
                    .addComponent(deleteEmployeeButton))
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(approveButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel10, jLabel3, jLabel4, jLabel5, jLabel9});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void founderIDTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_founderIDTyped
        int ID;

        //get his ID
        try {
            ID = Integer.parseInt(founderIDInput.getText());
        }
        catch(NumberFormatException e){
            founderNameDisplay.setText("(ungültige Eingabe)");
            return;
        }

        //get name if possible

        try {
            ResultSet boss = stmt.executeQuery("SELECT forename, surname FROM" +
                    " citizens WHERE id = "+ID);

            if(!boss.next())
                founderNameDisplay.setText("(nicht gefunden)");
            else
                founderNameDisplay.setText("("+boss.getString("forename")+" "+
                        boss.getString("surname")+")");
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation" +
                    " mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_founderIDTyped

    private void editEmployee_list(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editEmployee_list
        if(evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2)
            editEmployee();
    }//GEN-LAST:event_editEmployee_list

    private void editEmployee_button(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEmployee_button
        editEmployee();
    }//GEN-LAST:event_editEmployee_button

    private void addEmployee(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployee
        //get new Employee
        Employee newEmpl = EmployeeEditingDialog.showEmployeeEditingDialog(parent,
                true, null, dbcon);

        if(newEmpl == null)
            return;

        //test if he happens to be the founder
        Employee founder = parseFounder(false);
        if(founder != null && founder.getID() == newEmpl.getID()) {
            JOptionPane.showMessageDialog(parent, "Neuer Angestellter darf " +
                    "nicht der Gründer sein.", "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //test if he's already in the list
        for(int ct = 0; ct < listModel.size(); ct++) {
            String descr = (String)listModel.getElementAt(ct);

            if(descr == null)
                continue;

            String ID = descr.split("\\|")[0].trim();
            if(ID.equals(""+newEmpl.getID())) {
                JOptionPane.showMessageDialog(parent, "Angestellter bereits in " +
                        "der Liste.", "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
            }
        }


        //add to list of employees
        String descr = "";
        descr += newEmpl.getID()+ " | ";
        descr += newEmpl.getForename() + " ";
        descr += newEmpl.getSurname() + " | ";
        descr += newEmpl.getSalary();
        listModel.addElement(descr);
    }//GEN-LAST:event_addEmployee

    private void deleteEmployee(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEmployee

        String descr = (String)employeeList.getSelectedValue();
        if(descr == null || descr.equals("ID | Name | Gehalt")) {
            JOptionPane.showMessageDialog(this, "Kein Angestellter zum Löschen" +
                    " ausgewählt.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //remove employee from the list
        listModel.removeElement(descr);
    }//GEN-LAST:event_deleteEmployee

    private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
        dispose();
    }//GEN-LAST:event_cancel

    private void approve(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_approve
        if(parseInput())
            dispose();
    }//GEN-LAST:event_approve

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEmloyeeButton;
    private javax.swing.JButton approveButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteEmployeeButton;
    private javax.swing.JButton editEmployeeButton;
    private javax.swing.JList employeeList;
    private javax.swing.JTextField founderIDInput;
    private javax.swing.JLabel founderNameDisplay;
    private javax.swing.JTextField founderSalaryInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jobsInput;
    private javax.swing.JTextField nameInput;
    private javax.swing.JTextField productDescriptionInput;
    private javax.swing.JTextField roomInput;
    // End of variables declaration//GEN-END:variables

    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Connection dbcon;
    Statement stmt;

    DefaultListModel listModel = new DefaultListModel();
    String currency;
    int companyID;
    java.awt.Frame parent;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Shows a <code>CompanyEditingDialog</code> to create or edit a company.
     * Note that all communication with the database is done by the dialog
     * itself.
     * @param parent The dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     * @param databaseConnection A connection to the general database
     * holding the 'companies' table.
     * @param companyID The ID of a new company, if one should be created. If
     * a company should be modified, pass a negative number here.
     * @return The ID of the company that has been created or modified. See
     * {@link #getCompanyID} for details.
     * @see salssuite.server.Server#buildServerDatabase
     */
    public static int showCompanyEditingDialog(java.awt.Frame parent, boolean modal,
            int companyID, Connection databaseConnection) {

        CompanyEditingDialog dia = new CompanyEditingDialog(parent, modal,
                companyID,databaseConnection);

        dia.setVisible(true);
        return dia.getCompanyID();
    }

    /**
     * Returns the ID of the company that is being modified or has been created.
     * This method should only be called after the dialog has been disposed,
     * otherwise it might return nonsensical values.
     * @return The ID of the company that has been edited or created, or
     * <code>0</code> if the dialog was being used to create a new company and
     * the user has cancelled the operation.
     */
    public int getCompanyID() {
        return companyID;
    }

    /**
     * Parses the input typed by the user and updates the database. Prints error
     * messages if the input is invalid. If not, the database is updated using
     * the new data.
     * @return true if successful, false if not.
     */
    private boolean parseInput() {

        int jobs;
        try {
            jobs = Integer.parseInt(jobsInput.getText());
            if(jobs <= 0)
                throw new NumberFormatException();

            int ct = 0;
            while(ct < listModel.getSize()+1)
                ct++;

            if(jobs < ct)
                jobs = ct;
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "<html>Bitte positive ganze Zahl in"
                    + " das Feld<br/>für die Anzahl der Angestellten eingeben.</html>",
                    "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String name = nameInput.getText();
        String room = roomInput.getText();
        String productDescription = productDescriptionInput.getText();

        if(!Util.checkInput(name) || !Util.checkInput(room) ||
                !Util.checkInput(productDescription))
            return false;

        Employee boss = parseFounder(true);
        if(boss == null) {
            return false;
        }


        try {
            //Check if any of the employees is already employed
            //at another company. If so, display a warning.
            String listOfAlreadyEmployedCitizens = "";

            for(int ct = 0; ct < listModel.getSize(); ct++) {
                String descr = (String)listModel.getElementAt(ct);
                if(descr == null)
                    continue;

                int emplID = Integer.parseInt(descr.split("\\|")[0].trim());

                ResultSet citizen = stmt.executeQuery("SELECT companyId,"
                        + " forename, surname, id FROM"
                        + " citizens WHERE id = "+emplID);
                citizen.next();
                if(citizen.getInt("companyId") != companyID &&
                        citizen.getInt("companyId") > 0) {
                    listOfAlreadyEmployedCitizens += "<br/>" +
                            citizen.getString("forename") + " " +
                            citizen.getString("surname") +
                            " (ID " + citizen.getInt("id") +
                            ", Betrieb Nr. "+
                            citizen.getInt("companyId") + ")";
                }
            }

            if(listOfAlreadyEmployedCitizens.length() > 0) {
                int option = JOptionPane.showConfirmDialog(this, "<html>Die"
                            + " folgenden Bürger sind bereits bei einem anderen Betrieb"
                            + " angestellt:" + listOfAlreadyEmployedCitizens
                            + " <br/>Wenn Sie sie bei diesem Betrieb anstellen,"
                            + " werden sie ihre anderen Stellen verlieren.<br/>Fortfahren?",
                            "Bestätigen", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                if(option != JOptionPane.YES_OPTION)
                    return false;
            }
            

            //add everything to the database
            if(companyID < 0) { //meaning a new company should be created
                stmt.executeUpdate("INSERT INTO companies (name, room," +
                        "productDescription, jobs) VALUES (" +
                        "'"+name + "', " +
                        "'"+room + "', " +
                        "'"+productDescription + "'," +
                        jobs +
                        ")");
                //get ID of recently created company
                ResultSet company = stmt.executeQuery("SELECT id FROM " +
                        "companies WHERE name = '"+name+"' AND room = '"+room+"' " +
                        "AND productDescription = '"+productDescription+"'");
                company.next();
                companyID = company.getInt("id");
            }
            else {
                stmt.executeUpdate("UPDATE companies SET " +
                        "name = '" + name + "', " +
                        "room = '" + room + "', " +
                        "productDescription = '" + productDescription + "'," +
                        "jobs = " + jobs +
                        " WHERE id = "+companyID);
            }

            //remove all employees this company previously had
            stmt.executeUpdate("UPDATE citizens SET companyId = -1, isBoss =" +
                    " 0, salary = 0 WHERE companyId = "+companyID);

            //add employees it has now
            stmt.executeUpdate("UPDATE citizens SET companyId = "+companyID+"," +
                    " isBoss = 1, salary = "+boss.getSalary()+" WHERE id = "
                    +boss.getID());

            for(int ct = 0; ct < listModel.getSize(); ct++) {
                String descr = (String)listModel.getElementAt(ct);

                if(descr == null)
                    continue;

                int emplID = Integer.parseInt(descr.split("\\|")[0].trim());
                double salary = Double.parseDouble(descr.split("\\|")[2].trim());

                stmt.executeUpdate("UPDATE citizens SET companyId = "+companyID+", " +
                        "salary = "+salary+", " +
                        "isBoss = 0" +
                        "WHERE id = "+emplID);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation "
                    + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Parses all data concerning the company's founder. In case of failure
     * prints out error messages automatically.
     * @param checkIfFounderIsAlreadyEmployed When set to true, this method
     * checks if the founder is already employed at another company and prints
     * a warning message if this is the case.
     * @return The parsed founder as an Employee, or null if there was an error.
     */
    private Employee parseFounder(boolean checkIfFounderIsAlreadyEmployed) {
        int ID;
        double salary;
        Employee founder;
        
        //get his ID and salary
        try {
            ID = Integer.parseInt(founderIDInput.getText());
            salary = Double.parseDouble(founderSalaryInput.getText().replaceAll(",", "."));
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Bitte gültige Werte in die Felder"
                    + " für die ID und den Lohn des Gründers eingeben.",
                    "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            //get name if possible
            ResultSet boss = stmt.executeQuery("SELECT forename, surname," +
                    " form,companyId FROM citizens WHERE id = "+ID);

            if(!boss.next()) {
                JOptionPane.showMessageDialog(this, "Angegebener " +
                    "Betriebsgründer nicht gefunden.", "Ungültige Eingabe",
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }
            else
                founder = new Employee(ID, boss.getString("forename"),
                        boss.getString("surname"), boss.getString("form"), salary);

            //Check if the founder is already employed at another company.
            //If so, display a warning.
            if(checkIfFounderIsAlreadyEmployed &&
                    boss.getInt("companyId") != companyID && boss.getInt("companyId") > 0) {
                int option = JOptionPane.showConfirmDialog(this, "<html>Der"
                        + " gewählte Gründer ist bereits bei einem anderen Betrieb"
                        + " angestellt.<br/>Wenn Sie ihn bei diesem Betrieb anstellen,"
                        + " wird er seine andere Stelle verlieren.<br/>Fortfahren?"
                        + "</html>",
                        "Bestätigen", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(option != JOptionPane.YES_OPTION)
                    return null;
            }

            return founder;
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation "
                    + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }        
    }

    /**
     * Permits the user to edit the currently selected employee from the list
     * of employees.
     */
    private void editEmployee() {
        //get selected employee
        String descr = (String)employeeList.getSelectedValue();
        int emplIndex = listModel.indexOf(descr);

        if(descr == null || descr.equals("ID | Name | Gehalt")) {
            return;
        }

        int ID;
        double salary;
        try {
            ID = Integer.parseInt(descr.split(" ")[0].trim());
            salary = Double.parseDouble(descr.split("\\|")[2].trim());
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            new ExceptionDisplayDialog(null, true, e, "Interner Datenfehler")
                    .setVisible(true);
            return;
        }

        Employee currentEmpl;
        try {
            ResultSet citizen = stmt.executeQuery("SELECT forename, surname," +
                    "form FROM citizens WHERE id = "+ID);
            if(!citizen.next()) {
                JOptionPane.showMessageDialog(parent, "Zu bearbeitender Angestellter" +
                        " nicht gefunden.", "Datenfehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentEmpl = new Employee(ID, citizen.getString("forename"),
                    citizen.getString("surname"), citizen.getString("form"),
                    salary);

        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation "
                    + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }


        //edit him
        Employee tmpEmployee = EmployeeEditingDialog.showEmployeeEditingDialog(null,
                true, currentEmpl, dbcon);

        if(tmpEmployee != null) {
            String newDescr = "";
                newDescr += tmpEmployee.getID()+" | ";
                newDescr += tmpEmployee.getForename() + " ";
                newDescr += tmpEmployee.getSurname() + " | ";
                newDescr += tmpEmployee.getSalary();
                listModel.set(emplIndex, newDescr);
        }
    }

    //============================INNER CLASSES===============================//
}
