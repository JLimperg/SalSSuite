/*
 * AccountingEntryDialog.java
 *
 * Created on 11.12.2010, 03:24:51
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

package salssuite.clients.accounting;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;
import salssuite.util.Util;

/**
 * Dialog to create or edit an entry for the accounting management. All fields
 * of the entry can be edited in this way.
 * <p>
 * The dialog does all communication
 * with the database on its own, therefore the
 * {@link salssuite.clients.accounting.AccountingClient}
 * can simply display it and wait for its disposal.
 * @author Jannis Limperg
 * @version 1.0
 * @see salssuite.server.module.AccountingModule
 */
public class AccountingEntryDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1;

    /**
     * Creates a new <code>AccountingEntryDialog</code> to let the user create
     * a new entry.
     * @param parent This dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     * @param dbcon A connection to the database where the accounting
     * information is stored.
     * @see salssuite.server.module.AccountingModule#buildDatabase
     */
    public AccountingEntryDialog(java.awt.Frame parent, boolean modal,
            Connection dbcon) {
        super(parent, modal);
        this.dbcon = dbcon;
        initComponents();

        try {
            stmt = dbcon.createStatement();

            ResultSet categoryList = stmt.executeQuery("SELECT * FROM "
                    + "accounting_categories");

            while(categoryList.next())
                categoryChooser.addItem(categoryList.getString("name"));
        }
        catch(SQLException ex) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        //fill in default values
        GregorianCalendar now = new GregorianCalendar();

        dayInput.setText(""+now.get(GregorianCalendar.DAY_OF_MONTH));
        monthInput.setText(""+(now.get(GregorianCalendar.MONTH)+1));
        yearInput.setText(""+now.get(GregorianCalendar.YEAR));

        hourInput.setText(""+now.get(GregorianCalendar.HOUR_OF_DAY));
        minutesInput.setText(""+now.get(GregorianCalendar.MINUTE));
        secondsInput.setText(""+now.get(GregorianCalendar.SECOND));

        //usability tweaks
        int xLocation = parent.getX() + parent.getWidth()/2 - getWidth()/2;
        int yLocation = parent.getY() + parent.getHeight()/2 - getHeight()/2;
        setLocation(xLocation, yLocation);
        setTitle("Neuer Eintrag");
        descriptionInput.requestFocus();
    }

    /**
     * Creates a new <code>AccountingEntryDialog</code> to let the user edit an
     * existing entry.
     * @param parent This dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     * @param dbcon A connection to the database where the accounting
     * information is stored.
     * @param ID The ID of the entry to be edited.
     * @see salssuite.server.module.AccountingModule#buildDatabase
     */
    public AccountingEntryDialog(java.awt.Frame parent, boolean modal,
            Connection dbcon, int ID) {     
        this(parent, modal, dbcon);
        this.ID = ID;

        //overwrite default values loaded in other constructor
        try {
            ResultSet entry = stmt.executeQuery("SELECT * FROM accounting "
                    + "WHERE id = "+ID);
            entry.next();
            
            date = entry.getString("date");
            String[] dateParts = Util.parseDateString(date);
            dayInput.setText(dateParts[2]);
            monthInput.setText(dateParts[1]);
            yearInput.setText(dateParts[0]);

            time = entry.getString("time");
            String[] timeParts = Util.parseTimeString(time);
            hourInput.setText(timeParts[0]);
            minutesInput.setText(timeParts[1]);
            secondsInput.setText(timeParts[2]);

            outgoInput.setText(entry.getString("outgo").replaceAll("-", ""));
            incomeInput.setText(entry.getString("income"));
            descriptionInput.setText(entry.getString("description"));
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        setTitle("Eintrag bearbeiten");
        outgoInput.requestFocus();
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
        dayInput = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        monthInput = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        yearInput = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        hourInput = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        minutesInput = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        secondsInput = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        categoryChooser = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        outgoInput = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        incomeInput = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        approveButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        descriptionInput = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        jLabel1.setText("Datum");

        dayInput.setToolTipText("Tag");
        dayInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel2.setText(".");

        monthInput.setToolTipText("Monat");
        monthInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel3.setText(".");

        yearInput.setToolTipText("Jahr");
        yearInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel4.setText("Uhrzeit");

        hourInput.setToolTipText("Stunde");
        hourInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel5.setText(":");

        minutesInput.setToolTipText("Minute");
        minutesInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel6.setText(":");

        secondsInput.setToolTipText("Sekunde");
        secondsInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel7.setText("Kategorie");

        categoryChooser.setToolTipText("Kategorie aus der Liste wählen.");

        jLabel8.setFont(new java.awt.Font("Ubuntu", 1, 15));
        jLabel8.setText("Ausgaben");

        outgoInput.setToolTipText("Für diesen Vorgang ausgegebenes Geld.");
        outgoInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Ubuntu", 1, 15));
        jLabel9.setText("Einnahmen");

        incomeInput.setToolTipText("Durch diesen Vorgang eingenommenes Geld.");
        incomeInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel10.setText("€");

        jLabel11.setText("€");

        approveButton.setText("Absenden");
        approveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Ubuntu", 1, 15));
        jLabel12.setText("-");

        jLabel13.setFont(new java.awt.Font("Ubuntu", 1, 15));
        jLabel13.setText("+");

        jLabel14.setText("Beschreibung");

        descriptionInput.setToolTipText("Möglichst kurze Beschreibung des Vorgangs.");
        descriptionInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send(evt);
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
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dayInput, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthInput, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearInput, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(3, 3, 3)
                        .addComponent(hourInput, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(minutesInput, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(secondsInput, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)))
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(incomeInput)
                                    .addComponent(outgoInput, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(138, 138, 138)
                                .addComponent(approveButton))
                            .addComponent(categoryChooser, 0, 325, Short.MAX_VALUE)
                            .addComponent(descriptionInput, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dayInput, hourInput, minutesInput, monthInput, secondsInput});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dayInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(monthInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(yearInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(hourInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(minutesInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(secondsInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(categoryChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(descriptionInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel12)
                    .addComponent(outgoInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(approveButton)
                    .addComponent(jLabel13)
                    .addComponent(incomeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dayInput, hourInput, minutesInput, monthInput, secondsInput});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void send(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send
        send();
    }//GEN-LAST:event_send

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton approveButton;
    private javax.swing.JComboBox categoryChooser;
    private javax.swing.JTextField dayInput;
    private javax.swing.JTextField descriptionInput;
    private javax.swing.JTextField hourInput;
    private javax.swing.JTextField incomeInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField minutesInput;
    private javax.swing.JTextField monthInput;
    private javax.swing.JTextField outgoInput;
    private javax.swing.JTextField secondsInput;
    private javax.swing.JTextField yearInput;
    // End of variables declaration//GEN-END:variables

    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    int ID = -1; //is positive if an entry should be updated, otherwise negative

    String description;
    String date;
    String time;
    String category;
    double outgo;
    double income;

    Connection dbcon;
    Statement stmt;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Displays an <code>AccountingEntryDialog</code> to the user, permitting
     * them to create a new entry.
     * @param parent This dialog's parent frame.
     * @param modal Whether the dialog should be modal or not.
     * @param dbcon A connection to the database where the accounting
     * information is stored.
     * @return The ID of the created/modified credit. See {@link #getCreditID}
     * for details.
     */
    public static int showAccountingEntryDialog(java.awt.Frame parent,
            boolean modal, Connection dbcon) {
        AccountingEntryDialog dia = new AccountingEntryDialog(parent, modal,
                dbcon);
        dia.setVisible(true);
        return dia.getCreditID();
    }

    /**
     * Displays an <code>AccountingEntryDialog</code> to the user, permitting
     * them to edit an existing entry.
     * @param parent This dialog's parent frame.
     * @param modal Whether the dialog should be modal or not.
     * @param dbcon A connection to the database where the accounting
     * information is stored.
     * @param ID The ID of the entry to be edited.
     * @return The ID of the created/modified credit. See {@link #getCreditID}
     * for details.
     */
    public static int showAccountingEntryDialog(java.awt.Frame parent,
            boolean modal, Connection dbcon, int ID) {
        AccountingEntryDialog dia = new AccountingEntryDialog(parent, modal,
                dbcon, ID);
        dia.setVisible(true);
        return dia.getCreditID();
    }

    /**
     * Returns the ID of the credit that has been created or modified.
     * @return The created or modified credit's ID, or <code>-1</code> if
     * the dialog has been disposed irregularly (f.ex. by being cancelled by
     * the user).
     */
    public int getCreditID() {
        return ID;
    }

    /**
     * Tries to parse the input and update the database. Displays error messages
     * and returns if any error occurs.
     */
    private void send() {

        if(!parseInput())
            return;

        try {
            //if a new entry should be created
            if(ID < 0) {
                stmt.executeUpdate("INSERT INTO accounting VALUES ("
                        + "DEFAULT,"
                        + "'"+description+"',"
                        + "'"+date+"',"
                        + "'"+time+"',"
                        + income+","
                        + "-"+outgo+","
                        + "'"+category+"'"
                        + ")");

                /*
                 * Determine the new credit's ID. Note that there is a very low
                 * chance that the following mechanism will return the ID of
                 * a credit that has been created by another user almost in
                 * the same instant.
                 */
                ResultSet newID = stmt.executeQuery("SELECT MAX(ID) FROM accounting");
                newID.next();
                ID = newID.getInt(1);
            }

            //if an existing entry should be updated
            else {
                stmt.executeUpdate("UPDATE accounting SET "
                        + "description = '"+description+"',"
                        + "date = '"+date+"',"
                        + "time = '"+time+"',"
                        + "income = "+income+","
                        + "outgo = -"+outgo+","
                        + "category = '"+category+"' "
                        + "WHERE id = "+ID);
            }

            dispose();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
    }

    /**
     * Parses the input and updates all fields of this dialog. Displays error
     * messages if any error occurs.
     * @return false if an error occured (most likely due to bad user input);
     * true if everything went fine.
     */
    private boolean parseInput() {

        //date and time
        int day, month, year;
        int hour, minutes, seconds;

        try {
            day = Integer.parseInt(dayInput.getText());
            month = Integer.parseInt(monthInput.getText());
            year = Integer.parseInt(yearInput.getText());

            hour = Integer.parseInt(hourInput.getText());
            minutes = Integer.parseInt(minutesInput.getText());
            seconds = Integer.parseInt(secondsInput.getText());

            if(day > 31 || day < 1 || month < 1 || month > 12 ||
                    year < 0 || hour < 0 || hour > 24 || minutes < 0 ||
                    minutes > 59 || seconds < 0 || seconds > 60)
                throw new NumberFormatException();

            date = ""+year+"-"+month+"-"+day;
            time = ""+hour+":"+minutes+":"+seconds;
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültiges Datums- oder"
                    + "Zeitformat.", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //outgo and income
        try {
            if(outgoInput.getText().length() != 0)
                outgo = Double.parseDouble(outgoInput.getText().replaceAll(
                        ",", "."));
            else
                outgo = 0;
            if(incomeInput.getText().length() != 0)
                income = Double.parseDouble(incomeInput.getText().replaceAll(
                        ",", "."));
            else
                income = 0;

            if(outgo == 0 && income == 0) {
                JOptionPane.showMessageDialog(this, "Mindestens eines der Felder"
                        + " 'Ausgaben' und 'Einnahmen' muss ausgefüllt werden.",
                        "Eingabefehler",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if(outgo < 0 || income < 0) {
                JOptionPane.showMessageDialog(this, "Ausgaben und Einnahmen"
                        + " müssen positiv sein.", "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe von"
                    + " Ausgaben oder Einnahmen.", "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //category
        category = (String)categoryChooser.getSelectedItem();
        if(category == null) {
            JOptionPane.showMessageDialog(this, "Bitte eine Kategorie wählen.",
                    "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //description
        description = descriptionInput.getText();
        if(description.length() == 0) {
            JOptionPane.showMessageDialog(this, "Bitte eine Beschreibung eingeben.",
                    "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //check if user attempts to use forbidden characters
        if(!Util.checkInput(description))
            return false;

        return true;
    }

    //============================INNER CLASSES===============================//
}
