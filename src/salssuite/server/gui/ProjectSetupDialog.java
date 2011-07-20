/*
 * ProjectSetupDialog.java
 *
 * Created on 02.02.2010, 18:18:50
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

import java.awt.event.WindowAdapter;
import java.io.File;
import java.util.GregorianCalendar;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import salssuite.util.Constants;
import salssuite.util.gui.ExceptionDisplayDialog;
import salssuite.server.Project;
import salssuite.server.ProjectException;
import salssuite.util.Util;

/**
 * Creates or edits a {@link salssuite.server.Project}.
 * Using this dialog, the user can edit all fields of an existing
 * <code>Project</code> or create a new one.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class ProjectSetupDialog extends javax.swing.JDialog {

    private static final long serialVersionUID=1;

    /**
     * Sets up a new project.
     * @param parent This dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     */
    public ProjectSetupDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        port = Constants.GENERAL_DATABASE_PORT;
        portInput.setText(""+port);

        if(parent != null) {
            setLocation(parent.getX()+(parent.getWidth()/2), parent.getY()+
                    (parent.getHeight()/2));
        }
        else {
            setLocation(Constants.connectDialogNode.getInt("window.x", 200),
                    Constants.connectDialogNode.getInt("window.y", 200));
        }

        addWindowListener(new WindowAdapter() {
            @Override
           public void windowClosing(java.awt.event.WindowEvent evt) {
               Constants.connectDialogNode.putInt("window.x", getX());
               Constants.connectDialogNode.putInt("window.y", getY());
               isCancelled = true;
           }
        });
    }

    /**
     * Edits an existing project.
     * @param parent This dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     * @param project The project all values are imported from.
     */
    public ProjectSetupDialog(java.awt.Frame parent, boolean modal, Project project) {
        this(parent, modal);

        //Changing output/input pathes after the project has been created
        //does not make sense.
        outputPathButton.setEnabled(false);
        inputPathButton.setEnabled(false);

        //fill in old values
        projName = project.getProjectName();
        nameInput.setText(projName);
        nameInput.setEditable(false);

        port = project.getPort();
        portInput.setText(""+port);
        outputPath = project.getOutputPath();
        if(outputPath == null)
            outputPathDisplay.setText("Nicht gesetzt.");
        else
            outputPathDisplay.setText(Util.adjustStringLength(
                    outputPath.getAbsolutePath(), 30));
        inputPath = project.getInputPath();
        if(inputPath == null)
            inputPathDisplay.setText("Nicht gesetzt.");
        else {
            inputPathDisplay.setText(Util.adjustStringLength(
                    inputPath.getAbsolutePath(), 30));
        }
        startDay = project.getStartDay();
        endDay = project.getEndDay();

        if(startDay != null) {

            startDayDInput.setText(""+startDay.get
                    (GregorianCalendar.DAY_OF_MONTH));
            startDayMInput.setText(""+(startDay.get
                    (GregorianCalendar.MONTH)+1));
            startDayYInput.setText(""+startDay.get
                    (GregorianCalendar.YEAR));
        }
        if(endDay != null) {
            endDayDInput.setText(""+endDay.get
                    (GregorianCalendar.DAY_OF_MONTH));
            endDayMInput.setText(""+(endDay.get
                    (GregorianCalendar.MONTH)+1));
            endDayYInput.setText(""+endDay.get
                    (GregorianCalendar.YEAR));
        }
    }//end constructor 2

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        startDayDInput = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        startDayMInput = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        startDayYInput = new javax.swing.JTextField();
        endDayDInput = new javax.swing.JTextField();
        endDayMInput = new javax.swing.JTextField();
        endDayYInput = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        nameInput = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        outputPathDisplay = new javax.swing.JLabel();
        inputPathDisplay = new javax.swing.JLabel();
        outputPathButton = new javax.swing.JButton();
        inputPathButton = new javax.swing.JButton();
        portInput = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Projekt wählen");
        setModal(true);
        setResizable(false);

        jLabel1.setText("Projektname");

        jLabel2.setText("Starttag");

        startDayDInput.setToolTipText("Tag eingeben");
        startDayDInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllHandler(evt);
            }
        });

        jLabel3.setText(".");

        startDayMInput.setToolTipText("Monat eingeben");
        startDayMInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllHandler(evt);
            }
        });

        jLabel4.setText(".");

        startDayYInput.setToolTipText("Jahr eingeben");
        startDayYInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllHandler(evt);
            }
        });

        endDayDInput.setToolTipText("Tag eingeben");
        endDayDInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllHandler(evt);
            }
        });

        endDayMInput.setToolTipText("Monat eingeben");
        endDayMInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonPressed(evt);
            }
        });
        endDayMInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllHandler(evt);
            }
        });

        endDayYInput.setToolTipText("Jahr eingeben");
        endDayYInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllHandler(evt);
            }
        });

        jLabel5.setText("Endtag");

        jLabel6.setText(".");

        jLabel7.setText(".");

        nameInput.setToolTipText("Beliebigen Namen für das Projekt wählen");
        nameInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllHandler(evt);
            }
        });

        jLabel10.setText("Ausgabepfad");

        jLabel11.setText("Eingabepfad");

        jLabel12.setText("Server-Port");

        outputPathDisplay.setText("Bitte wählen.");

        inputPathDisplay.setText("<html>Bitte wählen oder frei lassen,<p>wenn nichts importiert<p>werden soll.</html>");

        outputPathButton.setText("Durchsuchen");
        outputPathButton.setToolTipText("Den Ausgabepfad wählen");
        outputPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chosePathes(evt);
            }
        });

        inputPathButton.setText("Durchsuchen");
        inputPathButton.setToolTipText("Den Eingabepfad wählen");
        inputPathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chosePathes(evt);
            }
        });

        portInput.setToolTipText("Server-Port eingeben. Normalerweise muss die Voreinstellung nicht geändert werden.");
        portInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllHandler(evt);
            }
        });

        jButton3.setText("OK");
        jButton3.setToolTipText("Projekt erstellen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonPressed(evt);
            }
        });

        jButton4.setText("Abbrechen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelled(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(endDayDInput)
                                    .addComponent(startDayDInput, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(endDayMInput)
                                    .addComponent(startDayMInput, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(endDayYInput)
                                    .addComponent(startDayYInput, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11))
                                .addGap(54, 54, 54)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(outputPathDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                                    .addComponent(inputPathDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(portInput, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(outputPathButton)
                            .addComponent(inputPathButton)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(325, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {endDayDInput, endDayMInput, startDayDInput, startDayMInput});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton3, jButton4});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startDayDInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(startDayMInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(startDayYInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(endDayDInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endDayMInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endDayYInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(outputPathButton))
                    .addComponent(outputPathDisplay))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(inputPathButton)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputPathDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {endDayDInput, endDayMInput, endDayYInput, nameInput, portInput, startDayDInput, startDayMInput, startDayYInput});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton3, jButton4});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectAllHandler(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_selectAllHandler
        try {
            ((JTextField)evt.getSource()).selectAll();
        }
        catch(Exception e){}
    }//GEN-LAST:event_selectAllHandler

    private void okButtonPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonPressed
        int oldPort = port;

        if(!parseInput())
            return;

        if(oldPort != port && oldPort != 0)
            JOptionPane.showMessageDialog(this, "Die Änderung des Ports wird erst"
                    + " nach einem Neustart des Servers wirksam.", "",
                    JOptionPane.WARNING_MESSAGE);

        dispose();
    }//GEN-LAST:event_okButtonPressed

    private void cancelled(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelled
        isCancelled = true;
        dispose();
    }//GEN-LAST:event_cancelled

    private void chosePathes(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chosePathes
        JFileChooser chooser = new JFileChooser();

        if(evt.getSource() == outputPathButton) {
            chooser.setDialogTitle("Ausgabepfad wählen");
            chooser.setSelectedFile(outputPath);
        }
        else {
            chooser.setDialogTitle("Eingabepfad wählen");
            chooser.setSelectedFile(inputPath);
        }

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);

        if(chooser.showDialog(this, "Wählen") == JFileChooser.APPROVE_OPTION) {
            if(evt.getSource() == outputPathButton) {
                outputPath = chooser.getSelectedFile();
                outputPathDisplay.setText(Util.adjustStringLength(
                        outputPath.getAbsolutePath(), 30
                        ));
            }
            else if(evt.getSource() == inputPathButton) {
                inputPath = chooser.getSelectedFile();
                inputPathDisplay.setText(Util.adjustStringLength(
                        inputPath.getAbsolutePath(), 30
                        ));
            }
        }
    }//GEN-LAST:event_chosePathes

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField endDayDInput;
    private javax.swing.JTextField endDayMInput;
    private javax.swing.JTextField endDayYInput;
    private javax.swing.JButton inputPathButton;
    private javax.swing.JLabel inputPathDisplay;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField nameInput;
    private javax.swing.JButton outputPathButton;
    private javax.swing.JLabel outputPathDisplay;
    private javax.swing.JTextField portInput;
    private javax.swing.JTextField startDayDInput;
    private javax.swing.JTextField startDayMInput;
    private javax.swing.JTextField startDayYInput;
    // End of variables declaration//GEN-END:variables


    //==============================OWN CODE==================================//

                                     //FIELDS
    boolean isCancelled;

    String projName;
    GregorianCalendar startDay;
    GregorianCalendar endDay;
    File outputPath;
    File inputPath;
    int port;

    /**
     * Returns the end day of the project. It is ensured that (a) this is a
     * valid date; (b) that the end day comes after the start day. It is not
     * ensured that both are dates in the future.
     * @return The project's end day.
     */
    public GregorianCalendar getEndDay() {
        return endDay;
    }

    /**
     * Returns the global path for all input data provided by the user.
     * If this path is set, it is ensured that it exists and is a directory.
     * @return The input path, or <code>null</code> if the user did not specify it. This means that
     * no data should be imported into the project.
     */
    public File getInputPath() {
        return inputPath;
    }

    /**
     * Returns the global output path.
     * @return The output path.
     * @see salssuite.server.Project
     */
    public File getOutputPath() {
        return outputPath;
    }


    /**
     * Returns the server port. It is ensured that this is a positive <code>int
     * </code>.
     * @return The server port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns this project's name, which is an arbitrary <code>String</code>.
     * @return The project's name.
     * @see salssuite.server.Project
     */
    public String getProjName() {
        return projName;
    }

    /**
     * Returns the start day of the project. It is ensured that (a) this is a
     * valid date; (b) that the end day comes after the start day. It is not
     * ensured that both are dates in the future.
     * @return The project's start day.
     */
    public GregorianCalendar getStartDay() {
        return startDay;
    }

    /**
     * Indicates that the user has cancelled editing or creating the project.
     * @return <code>true</code> if the action was cancelled;
     * <code>false</code> if not.
     */
    public boolean isCancelled() {
        return isCancelled;
    }

                                     //METHODS

    /**
     * Creates a new <code>ProjectSetupDialog</code>, displays it and
     * returns the new or edited <code>Project</code>. If the
     * dialog was cancelled, <code>null</code> is returned.
     * @param parent The dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     * @return A <code>Project</code> containing all the values specified by the
     * user, or <code>null</code> if the user has cancelled.
     */
    public static Project showProjectSetupDialog(java.awt.Frame parent, boolean modal) {

        ProjectSetupDialog dia = new ProjectSetupDialog(parent, modal);
        dia.setVisible(true);

        if(dia.isCancelled())
            return null;

        try {
            return new Project(
                    dia.projName,
                    dia.startDay,
                    dia.endDay,
                    dia.port,
                    dia.inputPath,
                    dia.outputPath
                    );
        }
        catch(ProjectException e) {
            new ExceptionDisplayDialog(parent, true, e, "Konnte Projektdaten nicht"
                    + "laden.").setVisible(true);
            return null;
        }

    }//end showProjectSetupDialog 1


    /**
     * Creates a new <code>ProjectSetupDialog</code>, displays it and
     * returns the new or edited <code>Project</code>. If the
     * dialog was cancelled, <code>null</code> is returned.
     * @param parent The dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     * @param project A project to be modified.
     * @return A <code>Project</code> containing all the values specified by the
     * user, or <code>null</code> if the user has cancelled.
     */
    public static Project showProjectSetupDialog(java.awt.Frame parent, boolean
            modal, Project project) {

        ProjectSetupDialog dia = new ProjectSetupDialog(parent, modal, project);
        dia.setVisible(true);

        if(dia.isCancelled())
            return null;

        try {
            return new Project(
                    dia.projName,
                    dia.startDay,
                    dia.endDay,
                    dia.port,
                    dia.inputPath,
                    dia.outputPath
                    );
            }
        catch(ProjectException e) {
            new ExceptionDisplayDialog(parent, true, e, "Konnte Projektdaten nicht"
                    + "laden.").setVisible(true);
            return null;
        }
    } //end showProjectSetupDialog 2

    /**
     * Parses the input currently entered by the user. If the input was correct,
     * true is returned, otherwise an error message is printed and parseInput()
     * returns false. Variables are set by this method, so if true is returned,
     * all fields are assigned according to the user's input.
     * @return true if everything seems to be all right;
     * false if there was an error in the input.
     */
    private boolean parseInput(){
        projName = nameInput.getText();

        //check if user attempts to use forbidden characters
        if(!Util.checkInput(projName))
            return false;

        if(projName.contains(System.getProperty("file.separator"))) {
            JOptionPane.showMessageDialog(this, "Der Projektname darf kein"
                    + " Pfadtrennzeichen enthalten. ('" +
                    System.getProperty("file.separator")+"')",
                    "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return(false);
        }

        try {
            port = Integer.parseInt(portInput.getText());
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Fehler: Der Server-Port muss"
                    + " eine positive ganze Zahl sein.", "Ungültige Eingabe",
                    JOptionPane.ERROR_MESSAGE);
            return(false);
        }

        if(port < 0) {
            JOptionPane.showMessageDialog(this, "Fehler: Port muss " +
                    "positiv sein.", "Ungültige Eingabe",
                    JOptionPane.ERROR_MESSAGE);
            return(false);
        }

        //parse dates
        int endD; int endM; int endY;
        int startD; int startM; int startY;

        boolean startDayParsedSuccessfully = false;

        try {
            startD = Integer.parseInt(startDayDInput.getText());
            startM = Integer.parseInt(startDayMInput.getText());
            startY = Util.expandYear(Integer.parseInt(startDayYInput.getText()));

            startDayParsedSuccessfully = true;

            endD = Integer.parseInt(endDayDInput.getText());
            endM = Integer.parseInt(endDayMInput.getText());
            endY = Util.expandYear(Integer.parseInt(endDayYInput.getText()));
        }
        catch (NumberFormatException e) {
            if(startDayParsedSuccessfully) {
                JOptionPane.showMessageDialog(this, "Ungültige Eingabe für den " +
                        "Endtag: Keine Zahl.", "Ungültige Eingabe",
                        JOptionPane.ERROR_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(this, "Ungültige Eingabe für den " +
                        "Starttag: Keine Zahl.", "Ungültige Eingabe",
                        JOptionPane.ERROR_MESSAGE);
            return(false);
        }
        
        //check whether ints are valid
        if(startD <= 0 || startM <= 0 || startY <= 0 || endD <= 0 || endM <= 0 ||
                endY <= 0 || startD > 31 || endD > 31 || endM > 12 || startM > 12) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe für Start- " +
                    "oder Endtag.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return(false);
        }

        //create GregorianCalendars
        endDay = new GregorianCalendar(endY, endM-1, endD);
        startDay = new GregorianCalendar(startY, startM-1, startD);

        //see whether the days are valid
        if(startDay.after(endDay)) {
            JOptionPane.showMessageDialog(this, "Fehler: Endtag ist vor Starttag.",
                    "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            endDay = null;
            startDay = null;
            return(false);
        }

        if(startDay.get(GregorianCalendar.YEAR) != endDay.get(GregorianCalendar.YEAR)) {
            JOptionPane.showMessageDialog(this, "Start- und Endtag müssen im"
                    + " gleichen Jahr liegen.", "Eingabefehler",
                    JOptionPane.ERROR_MESSAGE);
            endDay = startDay = null;
            return false;
        }

        //determine whether pathes are set
        if(outputPath == null) {
            JOptionPane.showMessageDialog(this, "Bitte Ausgabepfad " +
                    "wählen.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
            return(false);
        }
        outputPath.getParentFile().mkdirs();

        if(inputPath == null && inputPathButton.isEnabled()) {
            //If the inputPathButton is not enabled, this indicated that an
            //existing project should be modified. In this case it does not
            //make sense to alert the user because no input can be processed
            //anyway.
            int option = JOptionPane.showConfirmDialog(this, "<html>Wenn Sie den"
                    + " Eingabepfad nicht setzen,<p>"
                    + "werden keine Daten importiert. Fortfahren?</html>", "",
                    JOptionPane.WARNING_MESSAGE);
            if(option != JOptionPane.YES_OPTION)
                return false;
            inputPath = null;
        }

        return true;
    }//end parseInput()

}//end class
