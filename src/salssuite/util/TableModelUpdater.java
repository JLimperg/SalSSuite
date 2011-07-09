package salssuite.util;

import java.awt.Frame;
import java.sql.ResultSet;
import java.util.LinkedList;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 * Automates the process of building a <code>TableModel</code> from a
 * <code>ResultSet</code>. This class mainly takes care of delegating the
 * work to a <code>SwingWorker</code> and displaying progress in a reasonable
 * way. How exactly each table row should be created is determined by the
 * implementation of {@link #buildTableRow}.
 * @author Jannis Limperg
 */
public abstract class TableModelUpdater {

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    Frame parent;
    DefaultTableModel tableModel;
    int totalRows;
    String message;

    //============================CONSTRUCTORS================================//

    /**
     * Constructs a <code>TableModelUpdater</code> that works on the given
     * <code>tableModel</code>, displaying dialogs on top of <code>parent</code>.
     * @param parent <code>Frame</code> that should be used as a parent
     * frame for dialogs etc.
     * @param tableModel The <code>TableModel</code> this <code>TableModelUpdater</code>
     * should fill with data.
     * @param message A message to be displayed while building the model. If this
     * is <code>null</code>, a generic message is displayed.
     * @throws IllegalArgumentException if <code>tableModel</code> is <code>null</code>
     * or <code>totalRows</code> is less than or equal to zero.
     */
    public TableModelUpdater(Frame parent, DefaultTableModel tableModel, String
            message) {

        if(tableModel == null)
            throw new IllegalArgumentException("Table model may not be null!");

        this.parent = parent;
        this.tableModel = tableModel;
        if(message != null)
            this.message = message;
        else
            this.message = "Lade die Daten...";
    }

    //==============================METHODS===================================//

    /**
     * Override this method to specify how exactly each table row should be built.
     * You may assume that <code>data</code> is a <code>ResultSet</code> where
     * the cursor points to a valid database table row.
     * @param data A <code>ResultSet</code> containing at least one row
     * which the cursor points to.
     * @return An array of <code>Object</code>s that represent the table row
     * to be created, or <code>null</code> if an error occurred.
     */
    abstract public Object[] buildTableRow(ResultSet data);

    /**
     * Starts updating the table model. This method will create a
     * <code>SwingWorker</code> and set up a <code>ProgressMonitor</code>
     * that monitors how many rows have already been built.
     * @param data A <code>ResultSet</code> containing all the data that is
     * necessary to build the table. The method will call
     * {@link #buildTableRow} on every row of this <code>ResultSet</code>.
     * @param totalRows The total number of rows in <code>data</code>. This
     * is required for displaying progress properly.
     * @return The <code>SwingWorker</code> doing the actual update stuff.
     */
    public SwingWorker<LinkedList<Object[]>, Object[]>
            update(final ResultSet data, int totalRows) {

        //create the ProgressMonitor
        final ProgressMonitor monitor = new ProgressMonitor(parent, message,
                null, 0, totalRows);

        //create the SwingWorker
        SwingWorker<LinkedList<Object[]>, Object[]> worker =
                new SwingWorker<LinkedList<Object[]>, Object[]>() {

            int rowsProcessed = 0;

            @Override
            protected LinkedList<Object[]> doInBackground() throws Exception {
                LinkedList<Object[]> rows = new LinkedList<Object[]>();
                while(data.next()) {
                    if(monitor.isCanceled())
                        break;

                    Object[] row = buildTableRow(data);
                    if(row == null) {
                        cancel(true);
                        break;
                    }
                    rows.add(row);
                    publish(row);
                }
                return rows;
            }

            @Override
             protected void process(java.util.List<Object[]> chunks) {
                 for (Object[] row : chunks) {
                     tableModel.addRow(row);
                     rowsProcessed ++;
                 }
                 monitor.setProgress(rowsProcessed);
             }

             @Override
             protected void done() {
                 monitor.close();
             }
        }; //end SwingWorker

        //start the SwingWorker
        worker.execute();
        return worker;
    }

    //============================INNER CLASSES===============================//

}
