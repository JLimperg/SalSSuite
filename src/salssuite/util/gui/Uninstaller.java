/*
 * Uninstaller.java
 *
 * Created on 02.11.2010, 21:27:12
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

package salssuite.util.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import salssuite.util.Constants;

/**
 * Uninstalls the whole SalSSuite programme. This uninstaller a) removes the
 * whole programme directory (including itself if it is located there) and
 * b) removes all the {@link java.util.prefs.Preferences} nodes used by the SalSSuite.
 * <p>
 * The programme directory is usually retrieved from the <code>Preferences</code>
 * node {@link salssuite.util.Constants#installerNode}. If this node happens to be
 * non-existent or empty, the uninstaller asks the user to choose the programme
 * directory themselves.
 * <p>
 * The uninstaller removes the <code>Preferences</code> node
 * {@link salssuite.util.Constants#programmeNode} and all of its children.
 * @author Jannis Limperg
 * @version 1.0.1
 * @see Installer
 */
public class Uninstaller extends javax.swing.JFrame {

    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     */
    public Uninstaller() {
        initComponents();
        setLocation(300, 300);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Dieser Uninstaller deinstalliert die SalSSuite vollständig von Ihrem Computer.");

        jLabel2.setText("Dabei werden alle Projekte unwiderruflich gelöscht.");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel3.setText("Sichern Sie den Ordner \"Projekte\" im Programmverzeichnis, wenn Sie Ihre");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel4.setText("Projekte behalten wollen!");

        jLabel5.setText("Mit einem Klick auf \"Deinstallieren\" starten Sie die Deinstallation.");

        jButton1.setText("Deinstallieren");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uninstall(evt);
            }
        });

        jButton2.setText("Abbrechen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 385, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel
        System.exit(0);
    }//GEN-LAST:event_cancel

    private void uninstall(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uninstall

        //get programme directory from the installer node if possible
        File programmeDir = new File(Constants.installerNode.get("programmeDir", ""));

        //if the installer node contains no valid folder, ask the user for programme
        //directory
        if(!programmeDir.exists()) {
            JOptionPane.showMessageDialog(this, "<html>Konnte Programmverzeichnis nicht"
                    + " finden.<br/>Geben Sie im folgenden Dialog bitte ein"
                    + " gültiges Verzeichnis an.</html>", "Fehler",
                    JOptionPane.WARNING_MESSAGE);
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setDialogTitle("Programmverzeichnis wählen");

            int chosenOption = fileChooser.showOpenDialog(this);

            if(chosenOption != JFileChooser.APPROVE_OPTION)
                System.exit(0);

            programmeDir = fileChooser.getSelectedFile();
        }

        //remove everything from the programme directory including itself
        deleteRecursivelyOnExit(programmeDir);

        //remove all Preferences nodes
        try {
            Constants.programmeNode.removeNode();
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Konnte Einstellungen nicht"
                    + " entfernen.<p>Für weitere Informationen bitte diesen"
                    + " Uninstaller von<p>der Kommandozeile aus starten.");
            e.printStackTrace();
            System.exit(1);
        }

        //exit
        JOptionPane.showMessageDialog(this, "SalSSuite erfolgreich deinstalliert.",
                "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }//GEN-LAST:event_uninstall

    /**
     * Runs the uninstaller.
     * @param args Command line arguments are not supported.
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {

        } catch (InstantiationException ex) {

        } catch (IllegalAccessException ex) {

        } catch (UnsupportedLookAndFeelException ex) {

        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Uninstaller().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables


    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Deletes fileToBeDeleted and any of its children on exit.
     * @param fileToBeDeleted The file or directory to be deleted on exit.
     */
    private void deleteRecursivelyOnExit(File fileToBeDeleted) {

        fileToBeDeleted.deleteOnExit();

        if(fileToBeDeleted.isDirectory()) {
            for(File child : fileToBeDeleted.listFiles())
                deleteRecursivelyOnExit(child);
        }
    }

    //============================INNER CLASSES===============================//
}