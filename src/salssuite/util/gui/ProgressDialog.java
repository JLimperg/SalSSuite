/*
 * ProgressDialog.java
 *
 * Created on 02.11.2010, 11:51:22
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

import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * Dialog that combines a <code>JProgressBar</code> and a <code>JLabel</code>
 * to display a message to the user. Those two <code>Component</code>s can
 * be accessed through setter/getter methods. This dialog is mainly intended
 * for displaying 'indeterminate progress'; for monitorable progress a
 * {@link javax.swing.ProgressMonitor} should be used.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class ProgressDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1;

    /**
     * Sole constructor.
     * @param parent The dialog's parent frame.
     * @param modal Whether the dialog should be modal or not.
     */
    public ProgressDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();
        messageDisplay = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(messageDisplay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(messageDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel messageDisplay;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables

    //========================================================================//
    //=============================OWN CODE===================================//
    //========================================================================//

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    //============================CONSTRUCTORS================================//

    //==============================METHODS===================================//

    /**
     * Creates a <code>ProgressDialog</code> and displays it.
     * @param parent The dialog's parent frame.
     * @param modal Whether this dialog should be modal or not.
     * @param defaultCloseOperation How this dialog should behave on close. One
     * of {@link javax.swing.JDialog#DISPOSE_ON_CLOSE},
     * {@link javax.swing.JDialog#HIDE_ON_CLOSE}
     * and {@link javax.swing.JDialog#DO_NOTHING_ON_CLOSE}.
     * @param indeterminate Whether the progress bar should monitor indeterminate
     * progress.
     * @param message The message that should be displayed to the user.
     * @return The created dialog after it has been set visible.
     */
    public static ProgressDialog showProgressDialog(java.awt.Frame parent,
            boolean modal, int defaultCloseOperation, boolean indeterminate,
            String message) {
        final ProgressDialog dia = new ProgressDialog(parent, modal);
        dia.getProgressBar().setIndeterminate(indeterminate);
        dia.setMessage(message);
        dia.setDefaultCloseOperation(defaultCloseOperation);
        dia.setVisible(true);
        
        return dia;
    }

    /**
     * Returns the <code>JProgressBar</code> this dialog uses to display
     * progress.
     * @return The progress bar.
     */
    public JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * Exchanges the dialog's <code>JProgressBar</code>. Also automatically
     * validates and repaints the dialog.
     * @param progressBar The new progress bar.
     */
    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
        validate();
        repaint();
    }

    /**
     * Returns the label this dialog uses to display messages to the user.
     * @return The label.
     */
    public JLabel getLabel() {
        return messageDisplay;
    }

    /**
     * Exchanges the dialog's <code>JLabel</code>. Automatically validates
     * and repaints the dialog.
     * @param label The new label.
     */
    public void setLabel(JLabel label) {
        messageDisplay = label;
    }

    /**
     * Convenience method equivalent to <code>getLabel().getText()</code>.
     * @return The message displayed to the user by the label.
     */
    public String getMessage() {
        return messageDisplay.getText();
    }

    /**
     * Convenience method equivalent to <code>getLabel().setText(message)</code>.
     * @param message A message the dialog displays to the user.
     */
    public void setMessage(String message) {
        messageDisplay.setText(message);
    }

    //============================INNER CLASSES===============================//
}