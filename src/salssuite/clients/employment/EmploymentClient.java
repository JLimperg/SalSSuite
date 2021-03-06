/*
 * EmploymentClient.java
 *
 * Created on 31.03.2011, 17:58:20
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

package salssuite.clients.employment;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import salssuite.clients.ConnectDialog;
import salssuite.clients.Converter;
import salssuite.server.module.EmploymentModule;
import salssuite.util.Constants;
import salssuite.util.Util;
import salssuite.util.gui.HelpBrowser;

/**
 * Client for the employment centre. This client enables the user to quickly and
 * easily attach citizens to companies and edit the relevant company properties.
 * It uses only data from the main server database (specifically citizen and
 * company data).
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class EmploymentClient extends javax.swing.JFrame {

    private static final long serialVersionUID  = 1;

    /**
     * Sole constructor.
     */
    public EmploymentClient() {
        initComponents();

        //usability
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e) {}


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                Constants.employmentClientNode.putInt("window.x", getX());
                Constants.employmentClientNode.putInt("window.y", getY());
                System.exit(0);
            }
        });


        setLocation(Constants.employmentClientNode.getInt("window.x", 200),
                Constants.employmentClientNode.getInt("window.y", 200));
        setSize(Constants.employmentClientNode.getInt("window.width", 800),
                Constants.employmentClientNode.getInt("window.height", 600));

        //set up the lists for drag and drop
        unemployedList.setTransferHandler(new ListTransferHandler());
        companyEmployedList.setTransferHandler(new ListTransferHandler());

        //set up list and table models
        unemployedList.setModel(unemployedListModel);
        companyEmployedList.setModel(companyEmployedListModel);
        companyTableModel = (DefaultTableModel)companyTable.getModel();

        //This listener will update the company displays whenever the user
        //selects a new table row.
        companyTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                updateCompanyDisplays();
            }
        });
        
        //connect to the server
        String[] theserver;
        while(true) {
            theserver = ConnectDialog.showConnectDialog(this, EmploymentModule.
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
                stmt = dbcon.createStatement();
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

        //provide initial data
        updateUnemployedListModel();
        updateCompanyTableModel();

        //display Converter
        Converter.displayNameToIDClient(this, theserver[0], Integer.parseInt(
                theserver[1]), theserver[2], theserver[3]);
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked", "serial"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        topLeftPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        companyTable = new javax.swing.JTable();
        displayOnlyWithFreeJobsToggle = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        companyFilterInput = new javax.swing.JTextField();
        companyFilterFeedbackDisplay = new javax.swing.JLabel();
        bottomLeftPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        companyEmployedList = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        companyNameDisplay = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        companyFounderDisplay = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        companyDescriptionDisplay = new javax.swing.JLabel();
        companyJobsInput = new javax.swing.JTextField();
        companyOccupiedJobsDisplay = new javax.swing.JLabel();
        companyFreeJobsDisplay = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        rightPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        unemployedList = new javax.swing.JList();
        unemployedFilterInput = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        unemployedFilterFeedbackDisplay = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        totalUnemployedDisplay = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        totalFreeJobsDisplay = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SalSSuite - Arbeitsamt");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPane1.setDividerLocation(400);

        jSplitPane3.setDividerLocation(300);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jLabel1.setText("Firmen");

        companyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Freie Stellen"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        companyTable.setToolTipText("Eine Tabellenzeile markieren, um Details über die Firma zu erfahren.");
        companyTable.setColumnSelectionAllowed(true);
        companyTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(companyTable);
        companyTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        displayOnlyWithFreeJobsToggle.setFont(new java.awt.Font("Dialog", 0, 12));
        displayOnlyWithFreeJobsToggle.setText("Zeige nur Firmen mit freien Stellen");
        displayOnlyWithFreeJobsToggle.setToolTipText("Wenn das Kästchen ausgewählt ist, werden in der Tabelle nur Firmen angezeigt, die mindestens eine freie Stelle haben.");
        displayOnlyWithFreeJobsToggle.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                displayOnlyWithFreeJobsToggleItemStateChanged(evt);
            }
        });

        jLabel11.setText("Nr.");

        companyFilterInput.setToolTipText("Gesuchte Firmen-ID eingeben und <Enter> drücken.");
        companyFilterInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyFilterInputActionPerformed(evt);
            }
        });

        companyFilterFeedbackDisplay.setForeground(new java.awt.Color(255, 0, 0));
        companyFilterFeedbackDisplay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout topLeftPanelLayout = new javax.swing.GroupLayout(topLeftPanel);
        topLeftPanel.setLayout(topLeftPanelLayout);
        topLeftPanelLayout.setHorizontalGroup(
            topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(displayOnlyWithFreeJobsToggle)
                    .addGroup(topLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(companyFilterFeedbackDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(companyFilterInput, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        topLeftPanelLayout.setVerticalGroup(
            topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(companyFilterInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(companyFilterFeedbackDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayOnlyWithFreeJobsToggle))
        );

        jSplitPane3.setTopComponent(topLeftPanel);

        jLabel2.setText("Firma - Details");

        companyEmployedList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        companyEmployedList.setToolTipText("Einen Bürger in die Arbeitslosenliste ziehen, um ihn zu 'entlassen'.");
        companyEmployedList.setDragEnabled(true);
        companyEmployedList.setDropMode(javax.swing.DropMode.INSERT);
        companyEmployedList.setEnabled(false);
        jScrollPane4.setViewportView(companyEmployedList);

        jLabel5.setText("Name:");
        jLabel5.setToolTipText("Der Firmenname.");

        companyNameDisplay.setFont(new java.awt.Font("Dialog", 0, 12));
        companyNameDisplay.setText("-");
        companyNameDisplay.setToolTipText("Der Firmenname.");

        jLabel6.setText("Leiter:");
        jLabel6.setToolTipText("Der Gründer oder Verantwortliche der Firma.");

        companyFounderDisplay.setFont(new java.awt.Font("Dialog", 0, 12));
        companyFounderDisplay.setText("-");
        companyFounderDisplay.setToolTipText("Der Gründer und Verantwortliche der Firma.");

        jLabel7.setText("Jobs");

        jLabel8.setText("gesamt");

        jLabel9.setText("besetzt");

        jLabel10.setText("frei");

        jLabel14.setText("Angestellte");

        jLabel3.setText("Beschr.:");
        jLabel3.setToolTipText("Eine Beschreibung des Tätigkeitsfeldes der Firma.");

        companyDescriptionDisplay.setFont(new java.awt.Font("Dialog", 0, 12));
        companyDescriptionDisplay.setText("-");
        companyDescriptionDisplay.setToolTipText("Eine Beschreibung des Tätigkeitsfeldes der Firma.");

        companyJobsInput.setText("0");
        companyJobsInput.setToolTipText("Anzahl der Angestellten, die diese Firma beschäftigen kann (inklusive Gründer!) eingeben und <Enter> drücken.");
        companyJobsInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                updateTotalJobsCount(evt);
            }
        });

        companyOccupiedJobsDisplay.setFont(new java.awt.Font("Dialog", 0, 12));
        companyOccupiedJobsDisplay.setText("-");
        companyOccupiedJobsDisplay.setToolTipText("Anzahl der Angestellten dieser Firma.");

        companyFreeJobsDisplay.setText("-");
        companyFreeJobsDisplay.setToolTipText("Freie Stellen.");

        refreshButton.setText("Aktualisieren");
        refreshButton.setToolTipText("Synchronisiert alle Anzeigen mit der Datenbank.");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bottomLeftPanelLayout = new javax.swing.GroupLayout(bottomLeftPanel);
        bottomLeftPanel.setLayout(bottomLeftPanelLayout);
        bottomLeftPanelLayout.setHorizontalGroup(
            bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bottomLeftPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(bottomLeftPanelLayout.createSequentialGroup()
                        .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bottomLeftPanelLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(companyDescriptionDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
                            .addGroup(bottomLeftPanelLayout.createSequentialGroup()
                                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(bottomLeftPanelLayout.createSequentialGroup()
                                        .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(companyJobsInput, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(companyOccupiedJobsDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(companyFreeJobsDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(companyFounderDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                                    .addComponent(companyNameDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))))
                        .addContainerGap())
                    .addComponent(jLabel14)
                    .addGroup(bottomLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                        .addComponent(refreshButton)
                        .addContainerGap())))
        );
        bottomLeftPanelLayout.setVerticalGroup(
            bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(refreshButton))
                .addGap(18, 18, 18)
                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(companyNameDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(companyFounderDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(companyDescriptionDisplay))
                .addGap(31, 31, 31)
                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bottomLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(companyJobsInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(companyOccupiedJobsDisplay)
                    .addComponent(companyFreeJobsDisplay))
                .addGap(9, 9, 9)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane3.setRightComponent(bottomLeftPanel);

        jSplitPane1.setLeftComponent(jSplitPane3);

        jLabel4.setText("Arbeitslose");

        unemployedList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        unemployedList.setToolTipText("Einen Bürger in die Angestelltenliste ziehen, um ihn 'einzustellen'.");
        unemployedList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        unemployedList.setDragEnabled(true);
        unemployedList.setDropMode(javax.swing.DropMode.INSERT);
        jScrollPane3.setViewportView(unemployedList);

        unemployedFilterInput.setToolTipText("ID des gesuchten Bürgers eingeben und <Enter> drücken.");
        unemployedFilterInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unemployedFilterInputActionPerformed(evt);
            }
        });

        jLabel12.setText("Nr.");

        unemployedFilterFeedbackDisplay.setForeground(new java.awt.Color(255, 0, 0));
        unemployedFilterFeedbackDisplay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel13.setText("Arbeitslose ges.:");

        jLabel16.setText("Freie Stellen ges.:");

        jButton1.setText("Hilfe!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                help(evt);
            }
        });

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addGap(6, 6, 6)
                        .addComponent(unemployedFilterInput, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(unemployedFilterFeedbackDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalUnemployedDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalFreeJobsDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)))
                .addContainerGap())
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unemployedFilterFeedbackDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jButton1)
                        .addComponent(jLabel12)
                        .addComponent(unemployedFilterInput)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(totalUnemployedDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16)
                    .addComponent(totalFreeJobsDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(rightPanel);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateTotalJobsCount(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_updateTotalJobsCount
        if(evt.getKeyCode() != java.awt.event.KeyEvent.VK_ENTER)
            return;

        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        //get new jobs count
        int newJobsCount;
        try {
            newJobsCount = Integer.parseInt(companyJobsInput.getText());
            if(newJobsCount < 0)
                throw new NumberFormatException();
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Bitte eine positive ganze Zahl"
                    + " für die Gesamtzahl der Stellen eingeben.", "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            companyJobsInput.requestFocus();
            companyJobsInput.selectAll();
            setCursor(Cursor.getDefaultCursor());
            return;
        }

        //test if new jobs count is less than current employees added, if so abort
        int index = companyTable.convertRowIndexToModel(companyTable.getSelectedRow());
        if(index < 0)
            return;
        int ID = Integer.parseInt((String)companyTableModel.getValueAt(index, 0));
        int freeJobs = -1;

        try {
            freeJobs = newJobsCount - getNumberOfEmployees(ID);
            if(freeJobs < 0) {
                updateCompanyDisplays();
                return;
            }

            //if not set new jobs count
            stmt.executeUpdate("UPDATE companies SET jobs = "+newJobsCount+" WHERE"
                    + " ID = "+ID);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            setCursor(Cursor.getDefaultCursor());
            return;
        }

        updateCompanyDisplays();
        companyTableModel.setValueAt(freeJobs, index, 2);
        setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_updateTotalJobsCount

    private void displayOnlyWithFreeJobsToggleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayOnlyWithFreeJobsToggleItemStateChanged
        updateCompanyTableModel();
        updateCompanyDisplays();
    }//GEN-LAST:event_displayOnlyWithFreeJobsToggleItemStateChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Constants.employmentClientNode.putInt("window.x", (int)getLocation().getX());
        Constants.employmentClientNode.putInt("window.y", (int)getLocation().getY());
        Constants.employmentClientNode.putInt("window.width", (int)getSize().getWidth());
        Constants.employmentClientNode.putInt("window.height", (int)getSize().getHeight());
    }//GEN-LAST:event_formWindowClosing

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        updateCompanyTableModel();
        updateCompanyDisplays();
        updateUnemployedListModel();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void unemployedFilterInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unemployedFilterInputActionPerformed
        int filter;
        try {
            filter = Integer.parseInt(unemployedFilterInput.getText());
        }
        catch(NumberFormatException e) {
            unemployedFilterFeedbackDisplay.setText("falsche Eingabe");
            unemployedFilterInput.setText("");
            unemployedFilterInput.requestFocus();
            return;
        }

        for(int ct = 0; ct < unemployedListModel.getSize(); ct++) {

            String description = (String)unemployedListModel.getElementAt(ct);

            if(filter == Integer.parseInt(description.split(" ")[0])) {
                int index = unemployedListModel.indexOf(description);
                unemployedList.ensureIndexIsVisible(index);
                unemployedList.setSelectedIndex(index);
                unemployedFilterFeedbackDisplay.setText("");
                unemployedFilterInput.setText("");
                unemployedFilterInput.requestFocus();
                return;
            }
        }

        unemployedFilterFeedbackDisplay.setText("nicht gefunden");
    }//GEN-LAST:event_unemployedFilterInputActionPerformed

    private void companyFilterInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyFilterInputActionPerformed
        int filter;
        try {
            filter = Integer.parseInt(companyFilterInput.getText());
        }
        catch(NumberFormatException e) {
            companyFilterFeedbackDisplay.setText("falsche Eingabe");
            companyFilterInput.setText("");
            companyFilterInput.requestFocus();
            return;
        }

        for(int ct = 0; ct < companyTableModel.getRowCount(); ct++) {

            int ID = Integer.parseInt((String)companyTableModel.getValueAt(ct, 0));
            if(ID == filter) {
                companyTable.changeSelection(ct, 0, false, false);
                companyFilterFeedbackDisplay.setText("");
                updateCompanyDisplays();
                return;
            }
        }

        companyFilterFeedbackDisplay.setText("nicht gefunden");
    }//GEN-LAST:event_companyFilterInputActionPerformed

    private void help(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_help
        new HelpBrowser("EmploymentClient").setVisible(true);
    }//GEN-LAST:event_help

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomLeftPanel;
    private javax.swing.JLabel companyDescriptionDisplay;
    private javax.swing.JList companyEmployedList;
    private javax.swing.JLabel companyFilterFeedbackDisplay;
    private javax.swing.JTextField companyFilterInput;
    private javax.swing.JLabel companyFounderDisplay;
    private javax.swing.JLabel companyFreeJobsDisplay;
    private javax.swing.JTextField companyJobsInput;
    private javax.swing.JLabel companyNameDisplay;
    private javax.swing.JLabel companyOccupiedJobsDisplay;
    private javax.swing.JTable companyTable;
    private javax.swing.JCheckBox displayOnlyWithFreeJobsToggle;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel topLeftPanel;
    private javax.swing.JLabel totalFreeJobsDisplay;
    private javax.swing.JLabel totalUnemployedDisplay;
    private javax.swing.JLabel unemployedFilterFeedbackDisplay;
    private javax.swing.JTextField unemployedFilterInput;
    private javax.swing.JList unemployedList;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Connection dbcon;
    Statement stmt;

    DefaultListModel unemployedListModel = new DefaultListModel();
    DefaultListModel companyEmployedListModel = new DefaultListModel();
    DefaultTableModel companyTableModel = new DefaultTableModel();

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Creates and displays an <code>EmploymentClient</code>.
     * @param args Command line arguments are not supported.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}

        new EmploymentClient().setVisible(true);
    }

    /**
     * Fetches data from the database and builds the model for the companyTable.
     */
    private void updateCompanyTableModel() {
        //erase the old data
        companyTableModel.setRowCount(0);
        
        //determine the number of rows to process
        int totalRows;
        final Statement stmt2;
        try {
            stmt2 = dbcon.createStatement();
            ResultSet rowCount = stmt2.executeQuery("SELECT COUNT(*) FROM"
                    + " companies");
            rowCount.next();
            totalRows = rowCount.getInt(1);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //create the ProgressMonitor
        final ProgressMonitor monitor = new ProgressMonitor(this,
                "Lade Firmendaten...", null, 0, totalRows);


        //create the worker
        SwingWorker<Object, Integer> worker = new SwingWorker<Object, Integer>() {

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    int totalFreeJobsCount = 0;
                    boolean showOnlyWithFreeJobs = displayOnlyWithFreeJobsToggle.
                            isSelected();

                    //fetch general company data
                    ResultSet companyData = stmt2.executeQuery("SELECT id, name, jobs"
                            + " FROM companies ORDER BY id");

                    //build table row for each company
                    int rowsProcessed = 0;
                    while(companyData.next()) {
                        if(monitor.isCanceled())
                            break;
                        rowsProcessed ++;
                        publish(rowsProcessed);

                        if(showOnlyWithFreeJobs &&
                                getFreeJobsCount(companyData.getInt("ID")) <= 0)
                            continue;

                        int ID = companyData.getInt("id");
                        String name = companyData.getString("name");
                        int freeJobs = getFreeJobsCount(ID);

                        //build the row
                        companyTableModel.addRow(new String[]{
                           ""+ID, name, ""+freeJobs
                        });

                        //update the total number of free jobs
                        totalFreeJobsCount += freeJobs;
                    }
                    
                    totalFreeJobsDisplay.setText(""+totalFreeJobsCount);
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                            + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return null;
                }

                return null;
            } //end doInBackground()


            @Override
            protected void process(List<Integer> chunks) {
                monitor.setProgress(chunks.get(chunks.size()-1));
            }

        }; //end SwingWorker
        
        worker.execute();
    }

    /**
     * Updates the information about the company selected in the company table.
     * This method will also call updateCompanyEmployedListModel().
     */
    private void updateCompanyDisplays() {

        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        //get selected company
        int selectedIndex = companyTable.convertRowIndexToModel(companyTable.getSelectedRow());

        if(selectedIndex < 0) { //no company selected
            companyEmployedList.setEnabled(false);
            companyEmployedListModel.removeAllElements();
            companyDescriptionDisplay.setText("-");
            companyNameDisplay.setText("-");
            companyFounderDisplay.setText("-");
            companyJobsInput.setText("0");
            companyOccupiedJobsDisplay.setText("-");
            companyFreeJobsDisplay.setText("-");
            setCursor(Cursor.getDefaultCursor());
            return;
        }

        companyEmployedList.setEnabled(true);

        int ID = Integer.parseInt((String)companyTableModel.getValueAt(
                selectedIndex, 0));

        //update the displays
        try {
            ResultSet company = stmt.executeQuery("SELECT * FROM companies WHERE" +
                    " ID = "+ID);
            company.next();

            companyDescriptionDisplay.setText(company.getString("productDescription"));
            companyNameDisplay.setText(company.getString("name"));
            int totalJobsCount = company.getInt("jobs");
            companyJobsInput.setText(""+totalJobsCount);
            int freeJobsCount = getFreeJobsCount(ID);
            companyFreeJobsDisplay.setText(""+freeJobsCount);
            companyOccupiedJobsDisplay.setText(""+(totalJobsCount - freeJobsCount));

            //get founder
            Statement stmt2 = dbcon.createStatement();
            ResultSet founder = stmt2.executeQuery("SELECT forename, surname, id"
                    + " FROM citizens WHERE companyID = "+ID+" AND isBoss = 1");
            if(founder.next())
                companyFounderDisplay.setText(founder.getInt("id") + " " +
                        founder.getString("forename")+ " " +
                        founder.getString("surname"));
            else
                companyFounderDisplay.setText("UNBEKANNT");
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        finally {
            setCursor(Cursor.getDefaultCursor());
        }

        updateCompanyEmployedListModel();
    }

    /**
     * Fetches data from the database and builds the model for the
     * companyEmployedList.
     */
    private void updateCompanyEmployedListModel() {

        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        //erase old data
        companyEmployedListModel = new DefaultListModel();
        companyEmployedList.setModel(companyEmployedListModel);

        //get selected company
        int index = companyTable.convertRowIndexToModel(companyTable.getSelectedRow());
        if(index < 0)
            return;
        int ID = Integer.parseInt((String)companyTableModel.getValueAt(index, 0));

        //fetch database stuff and update the list
        try {
            ResultSet employees = stmt.executeQuery("SELECT id, forename, surname"
                    + " FROM citizens WHERE companyId = " + ID + " AND isBoss = 0"
                    + " ORDER BY id");
            while(employees.next()) {
                companyEmployedListModel.addElement(
                        employees.getInt("ID") + " " +
                        employees.getString("forename") + " " +
                        employees.getString("surname"));
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Fetches data from the database and builds the model for the
     * unemployedList.
     */
    private void updateUnemployedListModel() {
        unemployedListModel = new DefaultListModel();
        unemployedList.setModel(unemployedListModel);

        //determine how many rows we have to proess
        int totalRows;
        final Statement stmt2;
        try {
            stmt2 = dbcon.createStatement();
            ResultSet rowCount = stmt2.executeQuery("SELECT COUNT(*) FROM"
                    + " citizens WHERE companyId < 0");
            rowCount.next();
            totalRows = rowCount.getInt(1);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler bei der Kommunikation mit der"
                    + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //create ProgressMonitor
        final ProgressMonitor monitor = new ProgressMonitor(this,
                "Lade Arbeitslose...", null, 0, totalRows);

        //create worker
        SwingWorker<Object, Integer> worker = new SwingWorker<Object, Integer>() {

            @Override
            protected Object doInBackground() throws Exception {
                int totalUnemployedCount = 0;

                try {
                    ResultSet unemployed = stmt2.executeQuery("SELECT id, forename,"
                            + "surname"
                            + " FROM citizens WHERE companyId < 0 ORDER BY id");
                    while(unemployed.next()) {
                        unemployedListModel.addElement(
                                unemployed.getInt("ID") + " " +
                                unemployed.getString("forename") + " "+
                                unemployed.getString("surname"));
                        totalUnemployedCount ++;
                        publish(totalUnemployedCount);
                    }
                }
                catch(SQLException e) {
                    JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                            + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return null;
                }

                totalUnemployedDisplay.setText(""+totalUnemployedCount);
                return null;
            } //end doInBackground

            @Override
            protected void process(List<Integer> chunks) {
                monitor.setProgress(chunks.get(chunks.size()-1));
            }
        }; //end SwingWorker

        worker.execute();
    }

    /**
     * Determines how many free jobs the given company has.
     * @param companyID The company's ID.
     * @return The number of jobs the company has to offer.
     * @throws SQLException if something goes wrong while communicating with
     * the database.
     * @throws IllegalArgumentException if a company with given ID does not exist.
     */
    private int getFreeJobsCount(int companyID) throws SQLException {
        Statement stmt2 = dbcon.createStatement();
        ResultSet companyData = stmt2.executeQuery("SELECT jobs FROM companies "
                + "WHERE id = "+companyID);

        if(!companyData.next())
            throw new IllegalArgumentException("Unknown company ID: "+companyID);

        int totalJobsCount = companyData.getInt("jobs");
        int freeJobsCount = 0;
        int occupiedJobsCount = getNumberOfEmployees(companyID);

        freeJobsCount = totalJobsCount - occupiedJobsCount;
        return freeJobsCount;
    }

    /**
     * Returns the number of employees the given company currently has. The founder
     * is included in the sum.
     * @param companyID The company's ID.
     * @return The number of employees.
     * @throws SQLException if there is an error while communicating with the
     * database.
     */
    private int getNumberOfEmployees(int companyID) throws SQLException {

        Statement stmt2 = dbcon.createStatement();
        ResultSet employeeData = stmt2.executeQuery("SELECT ID FROM citizens "
                + "WHERE companyID = "+companyID);

        int ct = 0;
        while(employeeData.next())
            ct++;

        return ct;
    }

    //============================INNER CLASSES===============================//

    /**
     * Custom TransferHandler for the lists used in by this client. As the lists
     * only exchange strings, this handler only supports moving strings around.
     * It is assumed throughout that both components used are JLists. Furthermore
     * it is assumed that both lists use DefaultListModels or subclasses of
     * DefaultListModel.
     *
     * This TransferHandler takes care of updating the database according to
     * changes made by drag and drop, and of the correct visualisation of said
     * changes for the user.
     */
    private class ListTransferHandler extends TransferHandler {

        private static final long serialVersionUID = 1;

        @Override
        public int getSourceActions(JComponent component) {
            return TransferHandler.MOVE;
        }

        @Override
        public Transferable createTransferable(JComponent component) {
            return new StringSelection((String)((JList)component).
                    getSelectedValue());
        }

        @Override
        public void exportDone(JComponent component, Transferable transferable,
                int action) {
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport transferSupport) {

            if(!transferSupport.getComponent().isEnabled())
                return false;

            // we only import Strings
            if (!transferSupport.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }

            // we only accept move actions
            boolean moveSupported = (MOVE & transferSupport.getSourceDropActions()) == MOVE;
            if (moveSupported)
                transferSupport.setDropAction(MOVE);
            else
                return false;

            //make sure there are no forbidden characters in the input
            try {
                if(!Util.checkInput((String)transferSupport.getTransferable().
                        getTransferData(DataFlavor.stringFlavor)))
                    return false;
            }
            catch(Exception e) {
                return false;
            }

            return true;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport transferSupport) {

            if(!transferSupport.isDrop())
                return false;

            JList list = (JList)transferSupport.getComponent();
            DefaultListModel listModel = (DefaultListModel)list.getModel();
            String data;
            int selectedCompanyIndex = companyTable.convertRowIndexToModel(
                    companyTable.getSelectedRow());
            int ID;

            try {
                //get the transferred data
                data = (String)transferSupport.getTransferable().getTransferData(
                    DataFlavor.stringFlavor);
                
                //get moved citizen's ID
                ID = Integer.parseInt(data.split(" ")[0]);
            }
            catch(Exception ex) {
                return false;
            }

            //check if citizen has been deleted in the meantime
            ResultSet citizen;
            try {
                Statement stmt2 = dbcon.createStatement();
                citizen = stmt2.executeQuery("SELECT forename, surname,"
                        + " companyId FROM citizens"
                        + " WHERE id = "+ID);
                if(!citizen.next()) {
                    JOptionPane.showMessageDialog(null, "Dieser Bürger wurde"
                            + " zwischenzeitlich von einem anderen Benutzer gelöscht.",
                            "Fehler", JOptionPane.ERROR_MESSAGE);
                    unemployedListModel.removeElement(data);
                    companyEmployedListModel.removeElement(data);
                }
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                        + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                updateCompanyTableModel();
                updateCompanyDisplays();
                updateUnemployedListModel();
                return false;
            }

            //if an employee has been moved from the company list to the
            //unemployed list
            if(list == unemployedList) {
                try {
                    //Check if this employee has been 'employed' at another
                    //company or marked 'unemployed' in the meantime.
                    int companyID = Integer.parseInt((String)companyTableModel.
                        getValueAt(selectedCompanyIndex, 0));
                    int companyIDInDatabase = citizen.getInt("companyId");
                    if(companyID != companyIDInDatabase) {
                        if(companyIDInDatabase > 0)
                            JOptionPane.showMessageDialog(null, "<html>Der gewählte"
                                    + " Bürger wurde von einem anderen Benutzer bei der"
                                    + " Firma Nr. "+companyIDInDatabase+" eingestellt.<br/>"
                                    + " Aktualisiere die Daten...</html>",
                                    "Fehler", JOptionPane.ERROR_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(null, "<html>Der gewählte"
                                    + " Bürger wurde bereits von einem anderen"
                                    + " Benutzer als arbeitslos markiert..<br/>"
                                    + " Aktualisiere die Daten...</html>",
                                    "Fehler", JOptionPane.ERROR_MESSAGE);
                        updateCompanyTableModel();
                        updateCompanyDisplays();
                        updateUnemployedListModel();
                        return false;
                    }

                    //update the database
                    stmt.executeUpdate("UPDATE citizens SET companyId = -1"
                            + " WHERE id = "+ID);

                    //update the visual representation
                    totalUnemployedDisplay.setText(""+
                            (Integer.parseInt(totalUnemployedDisplay.getText())+1));
                    totalFreeJobsDisplay.setText(""+
                            (Integer.parseInt(totalFreeJobsDisplay.getText())+1));
                    companyEmployedListModel.removeElement(data);

                    //Insert the moved citizen in the right place so that the
                    //list remains ordered by ID.
                    for(int ct = 0; ct < unemployedListModel.size(); ct ++) {
                        int IDinList = Integer.parseInt(((String)unemployedListModel.
                                getElementAt(ct)).split(" ")[0]);
                        if(IDinList > ID) {
                            listModel.add(ct, data);
                            break;
                        }
                    }

                    //add one free job to the company row in the table
                    companyTableModel.setValueAt(""+(Integer.parseInt((String)
                        companyTableModel.getValueAt(selectedCompanyIndex, 2)) + 1),
                        selectedCompanyIndex, 2);
                }
                catch(SQLException e) {
                    JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                            + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    updateCompanyTableModel();
                    updateCompanyDisplays();
                    updateUnemployedListModel();
                    return false;
                }
            }

            //if an employee has been moved from the unemployed list to the
            //company list
            else {
                try {
                    //Check if this employee has been 'employed' at some
                    //company in the meantime.
                    if(citizen.getInt("companyId") > 0) {
                        JOptionPane.showMessageDialog(null, "<html>Der gewählte"
                                + " Bürger wurde von einem anderen Benutzer bei einem"
                                + " Betrieb eingestellt.<br/>"
                                + " Aktualisiere die Daten...</html>",
                                "Fehler", JOptionPane.ERROR_MESSAGE);
                        updateCompanyTableModel();
                        updateCompanyDisplays();
                        updateUnemployedListModel();
                        return false;
                    }
                }
                catch(SQLException e) {
                    JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                            + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    updateCompanyTableModel();
                    updateCompanyDisplays();
                    updateUnemployedListModel();
                    return false;
                }

                //get ID of the company to which the citizen is being moved
                if(selectedCompanyIndex < 0)
                    return false;
                int companyID = Integer.parseInt((String)companyTableModel.
                        getValueAt(selectedCompanyIndex, 0));

                
                try {
                    //check if the company has free jobs
                    try {
                        if(getFreeJobsCount(companyID) <= 0) {
                            JOptionPane.showMessageDialog(null, "Dieser Betrieb hat"
                                    + " keine freien Stellen.", "Eingabefehler",
                                    JOptionPane.ERROR_MESSAGE);
                            updateCompanyEmployedListModel();
                            return false;
                        }
                    }
                    catch(IllegalArgumentException e) {
                        //This exception indicates that a company with given
                        //ID does not exist.
                        JOptionPane.showMessageDialog(null, "Die gewählte Firma"
                                + " wurde gelöscht. Aktualisiere die Daten...",
                                "Fehler", JOptionPane.ERROR_MESSAGE);
                        updateCompanyTableModel();
                        updateCompanyDisplays();
                        updateUnemployedListModel();
                    }

                    //update the database
                    stmt.executeUpdate("UPDATE citizens SET companyId = "+companyID+
                            " WHERE id = "+ID);

                    //update the visual representation
                    totalUnemployedDisplay.setText(""+
                            (Integer.parseInt(totalUnemployedDisplay.getText())-1));
                    totalFreeJobsDisplay.setText(""+
                            (Integer.parseInt(totalFreeJobsDisplay.getText())-1));
                    unemployedListModel.removeElement(data);
                    updateCompanyEmployedListModel();

                    //subtract one free job in the company's table row
                    companyTableModel.setValueAt(""+(Integer.parseInt((String)
                        companyTableModel.getValueAt(selectedCompanyIndex, 2)) - 1),
                        selectedCompanyIndex, 2);
                }
                catch(SQLException e) {
                    JOptionPane.showMessageDialog(null, "Fehler bei der Kommunikation mit der"
                            + " Datenbank", "Netzwerkfehler", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    updateCompanyTableModel();
                    updateCompanyDisplays();
                    updateUnemployedListModel();
                    return false;
                }
            }

            updateCompanyDisplays();
            return true;
        }
    }
}
