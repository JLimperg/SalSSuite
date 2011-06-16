/*
 * CompanyModulePanel.java
 *
 * Created on 10.05.2010, 13:01:02
 */

package salssuite.server.module.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import salssuite.util.gui.ExceptionDisplayDialog;
import salssuite.util.gui.FilterPanel;

/**
 * Permits the user to edit the company data. This GUI provides only a simple
 * list of companies, the actual editing is performed by
 * {@link CompanyEditingDialog}s.
 * @author Jannis Limperg
 */
public class CompanyModulePanel extends javax.swing.JPanel{

    private static final long serialVersionUID=1;

    /**
     * Sole constructor.
     * @param dbcon A connection to the server database.
     */
    public CompanyModulePanel(Connection dbcon) {
        
        initComponents();
        this.dbcon = dbcon;

        //connect to the database
        try {
            stmt = dbcon.createStatement();
            parent = (javax.swing.JFrame)getTopLevelAncestor();
        }
        catch(ClassCastException e) {parent = new javax.swing.JFrame();}
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Konnte nicht mit der Datenbank" +
                    " verbinden. Beende die Andwendung.", "Kritischer Netzwerkfehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
            return;
        }

        //set up filter panel
        try {
            filterPanel = new FilterPanel(parent,
                    (int)filterDummyPanel.getPreferredSize().getWidth(),
                    dbcon,
                    new String[] {"id, name, room, productDescription"},
                    "companies",
                    "ID",
                    new String[] {"name", "room", "productDescription"}, //string fields
                    new String[] {"id"}, //number fields
                    new String[] {}, //date fields
                    new String[] {"Firmenname", "Raum", "Produktbezeichnung"}, //string field descr
                    new String[] {"ID"}, //number field descr
                    new String[] {}  //date field descr
                );
            }
        catch(SQLException e) {
            new ExceptionDisplayDialog(parent, true, e, "DATENBANK-FEHLER").
                    setVisible(true);
            System.exit(1);
            return;
        }
        
        filterPanel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateTableModel();
            }
        });
        filterDummyPanel.setLayout(new BorderLayout());
        filterDummyPanel.add(filterPanel, BorderLayout.CENTER);

        //update the list
        updateTableModel();
        if(parent != null)
            parent.pack();
        validate();
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterDummyPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        addCompanyButton = new javax.swing.JButton();
        editCompanyButton = new javax.swing.JButton();
        deleteCompanyButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(750, 500));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 13));
        jLabel1.setText("Betrieb");

        addCompanyButton.setText("hinzufügen");
        addCompanyButton.setToolTipText("Fügt einen neuen Betrieb hinzu.");
        addCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCompany(evt);
            }
        });

        editCompanyButton.setText("bearbeiten");
        editCompanyButton.setToolTipText("Bearbeitet den gewählten Betrieb");
        editCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCompany_button(evt);
            }
        });

        deleteCompanyButton.setText("löschen");
        deleteCompanyButton.setToolTipText("Löscht den gewählten Betrieb.");
        deleteCompanyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCompany(evt);
            }
        });

        jButton1.setText("Liste erstellen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateCompanyList(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(addCompanyButton)
                .addGap(18, 18, 18)
                .addComponent(editCompanyButton)
                .addGap(18, 18, 18)
                .addComponent(deleteCompanyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 234, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCompanyButton)
                    .addComponent(jLabel1)
                    .addComponent(editCompanyButton)
                    .addComponent(deleteCompanyButton)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filterDummyPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterDummyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addCompany(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCompany
        //let user enter new company
        CompanyEditingDialog.showCompanyEditingDialog(parent, true,
                -1, dbcon);
        //update the GUI
        filterPanel.clearFilters();
    }//GEN-LAST:event_addCompany

    private void editCompany_button(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCompany_button
        editCompany();
    }//GEN-LAST:event_editCompany_button

    private void deleteCompany(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCompany

        //get selected company
        int selectedIndex = table.getSelectedRow();

        if(selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Kein Betrieb zum Bearbeiten " +
                    "gewählt", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int ID = Integer.parseInt((String)tableModel.getValueAt(selectedIndex, 0));

        //confirm deletion
        int option = JOptionPane.showConfirmDialog(this, "Firma wirklich löschen? Diese Aktion " +
                "kann nicht rückgängig gemacht werden.", "Löschen bestätigen",
                JOptionPane.WARNING_MESSAGE);

        if(option != JOptionPane.YES_OPTION)
            return;

        //delete
        try {
            stmt.executeUpdate("UPDATE citizens SET companyId = -1, isBoss = 0," +
                    "salary = 0 WHERE companyId = "+ID);
            stmt.executeUpdate("DELETE FROM companies WHERE id = "+ID);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation "
                    + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //update GUI
        filterPanel.clearFilters();
    }//GEN-LAST:event_deleteCompany

    private void generateCompanyList(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateCompanyList

        //ask the user for a file to store the data
        File destFile;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Csv-Dateien",
                "csv"));
        fileChooser.setDialogTitle("Betriebsliste speichern");
        int option = fileChooser.showSaveDialog(getTopLevelAncestor());
        if(option != JFileChooser.APPROVE_OPTION)
            return;
        destFile = fileChooser.getSelectedFile();

        //open the file and print the header
        PrintWriter out;
        try {
            out = new PrintWriter(new java.io.FileWriter(destFile));
            out.println("\"Nummer\",\"Name\",\"Raum\",\"Stellen ges. (inkl. Gründer)\","
                    + "\"Gründer\",\"Angestellte\"");
            out.flush();
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(getTopLevelAncestor(), "Konnte gewählte"
                    + "Datei nicht öffnen", "Dateifehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //generate the list
        try {
            Statement stmt2 = dbcon.createStatement();
            ResultSet companies = stmt.executeQuery("SELECT id,name,"
                    + "room,jobs FROM companies");

            while(companies.next()) {
                String row = "";

                int ID = companies.getInt("id");

                //generate the new row
                row += ID + ",";
                row += "\"" + companies.getString("name").replace("\"", "'") + "\",";
                row += "\"" + companies.getString("room").replace("\"", "'") + "\",";
                row += companies.getInt("jobs") + ",";

                //get the founder
                ResultSet founder = stmt2.executeQuery("SELECT forename,surname"
                        + " FROM citizens WHERE companyId = "+ID+" AND"
                        + " isBoss = 1");
                if(founder.next()) {
                    String forename = founder.getString("forename").replace("\"", "'").
                            split(" ")[0];
                    String surname = founder.getString("surname").replace("\"", "'");
                    row += "\"" + forename + " " + surname + "\",";
                }
                else
                    row += ",";

                //get the employees
                ResultSet employees = stmt2.executeQuery("SELECT forename, surname"
                        + " FROM citizens WHERE companyId = "+ID+" AND"
                        + " isBoss = 0");
                boolean firstEmployee = true;
                while(employees.next()) {
                    String forename = employees.getString("forename").replace("\"", "'").
                            split(" ")[0];
                    String surname = employees.getString("surname").replace("\"", "'");
                    if(firstEmployee) {
                        row += "\"" + forename + " " + surname + "\"";
                        out.println(row);
                        firstEmployee = false;
                    }
                    else {
                        out.println(",,,,,\"" + forename + " " + surname + "\"");
                    }
                }

                //if the company has no employees nothing
                //has been printed yet
                if(firstEmployee) {
                    out.println(row);
                }
            }

            out.flush();
            
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        //confirm generation
        JOptionPane.showMessageDialog(this, "Betriebsliste gespeichert.",
                "Erfolg", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_generateCompanyList


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCompanyButton;
    private javax.swing.JButton deleteCompanyButton;
    private javax.swing.JButton editCompanyButton;
    private javax.swing.JPanel filterDummyPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Connection dbcon;
    Statement stmt;
    Frame parent;
    DefaultTableModel tableModel = new DefaultTableModel();
    FilterPanel filterPanel;

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Permits the user to edit the currently selected company.
     */
    private void editCompany() {
        //get company
        int selectedIndex = table.getSelectedRow();

        if(selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Kein Betrieb zum Bearbeiten " +
                    "gewählt", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int ID = Integer.parseInt((String)tableModel.getValueAt(selectedIndex, 0));

        //let user edit company
        CompanyEditingDialog.showCompanyEditingDialog(
                parent, true, ID, dbcon);

        filterPanel.clearFilters();
    }

    /**
     * Updates the list model according to the current set of filters. This method
     * is called whenever the user modifies the filters.
     */
    private void updateTableModel() {
        //get filtered List
        ResultSet filteredData = filterPanel.getFilteredData();

        //clear list
        tableModel = new DefaultTableModel(null, new String[] {
            "ID", "Name", "Raum", "Produkt", "Gründer"
        })  {

            private static final long serialVersionUID = 1;

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        table.setModel(tableModel);

        //construct new information
        try {
            while(filteredData.next()) {

                int ID = filteredData.getInt("id");

                //get the founder
                String founder;
                Statement stmt2 = dbcon.createStatement();
                ResultSet citizens = stmt2.executeQuery("SELECT id,"
                        + " surname FROM citizens WHERE"
                        + " companyId = "+ID+" AND isBoss = 1");

                if(citizens.next())
                    founder = citizens.getString("id")+" " +
                            citizens.getString("surname");
                else
                    founder = "UNBEKANNT";
                
                tableModel.addRow(new String[] {
                    ""+ID,
                    filteredData.getString("name"),
                    filteredData.getString("room"),
                    filteredData.getString("productDescription"),
                    founder
                });
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation "
                    + "mit der Datenbank.", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //============================INNER CLASSES===============================//
}
