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

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.TreeMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import salssuite.util.Util;

/**
 * Generic component to generate filtered <code>ResultSet</code>s based on user
 * input. The usage of a <code>FilterPanel</code> can roughly be divided into
 * two stages:
 * <p>
 * <b>Setup</b> - The programmer chooses which columns of a database table
 * can be used to filter the table. The <code>FilterPanel</code>
 * supports columns containing values of type <code>String</code> (that
 * is <code>VARCHAR</code>) and any number type, and columns containing dates
 * stored as <code>VARCHAR</code>s.<br/>
 * These columns can be chosen as so called 'fields' (string fields, number fields,
 * date fields). Every field is then given a localised description which the
 * panel uses as a prompt for the user.<br/>
 * Based on this setup description, the panel builds its user interface. For
 * every field a descriptive label and a <code>JInputField</code> are created.
 * For number and date fields, a button to choose an operator (see below) is provided
 * in addition.
 * <p>
 * <b>Input processing</b> - Whenever the user enters text into an input field
 * and presses Enter, a so called 'seach string' for the database is created.
 * This is a query with <code>WHERE</code> clauses corresponding to all user
 * input entered (so the effects of two or more filters add to each other). The
 * result is retrieved as a <code>ResultSet</code> and <code>ActionListeners</code>
 * registered with this panel can obtain and use it. Note that the returned
 * <code>ResultSet</code> is not intended for scrolling, so only one component
 * should use it, as it will otherwise probably get locked.
 * <br/>
 * Different types of fields are handled differently when generating the
 * search query. For string fields, the query is:<br/>
 * <code>WHERE (someVarcharColumn) LIKE "%userInput%"</code><br/>
 * For number and date fields, the user can choose from five operators
 * ({@code <, >, <=, >=, =}) using a toggle button.
 * <p>
 * For a full query string syntax see {@link #FilterPanel the constructor}.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class FilterPanel extends javax.swing.JPanel{

    private static final long serialVersionUID=1;

    //==============================CONSTANTS=================================//

    private static final int NUMBER_FIELD = 1;
    private static final int DATE_FIELD = 2;
    private static final int STRING_FIELD = 3;

    //===============================FIELDS===================================//

    ResultSet results;

    Statement stmt;
    Frame parent;

    /**
     * The first part of the SELECT statement:
     * SELECT (columns) FROM (tableName)
     */
    String basicQueryString;

    LinkedList<ActionListener> listeners = new LinkedList<ActionListener>();
    /**
     * Holds all the *internal* field names (corresponding to the database
     * columns) and their input fields. For string fields and number fields,
     * the array contains only one JTextField. For date fields, it contains
     * three JTextFields, where the first one represents the day, the second
     * one the month, and the third one the year.
     */
    TreeMap<String, JTextField[]> fieldInputFields = new TreeMap<String, JTextField[]>();
    /**
     * Holds all the *internal* field names (corresponding to the database
     * columns) and their display panels.
     */
    TreeMap<String, JPanel> fieldPanels = new TreeMap<String, JPanel>();
    /**
     * Holds all the *internal* field names (corresponding to the database
     * columns) and the corresponding OperatorSwitcherButtons. There are
     * only number fields and date fields in this list.
     */
    TreeMap<String, OperatorSwitcherButton> operatorSwitcherButtons =
            new TreeMap<String, OperatorSwitcherButton>();
    /**
     * Holds all the *internal* field names (corresponding to the database
     * columns) and the corresponding field types. These can be
     * NUMBER_FIELD, STRING_FIELD or DATE_FIELD.
     */
    TreeMap<String, Integer> fieldTypes = new TreeMap<String, Integer>();
    
    /**
     * Value of the 'ORDER BY' SQL parameter.
     */
    String orderBy;

    //============================CONSTRUCTORS================================//

    /**
     * Sole constructor. From the values given here, the user interface is
     * constructed as well as the 'SELECT'-statement used to get the results.
     * See the class documentation for details.
     * <p>
     * The SELECT-statement is constructed in the following way:<br/>
     * <code>
     * SELECT (columns[0], columns[1], columns[2], ...)<br/>
     * FROM tableName<br/>
     * WHERE stringField[0] LIKE '%userInput1%' AND stringField[1] LIKE '%userInput2%' AND ...<br/>
     * AND numberField[0] chosenOperator1 userInput3 AND ...<br/>
     * AND dateField[0] chosenOperator2 userInput4 AND ...<br/>
     * ORDER BY orderBy
     * </code>
     * <p>
     * String fields are fields where the user can enter any string, which is then
     * compared to a string stored in the database. Number fields are fields
     * where the user can enter a number. Date fields are fields where the user
     * can enter a date. What field to choose depends on the data type of the
     * corresponding column in the database.
     * <p>
     * Of course, only fields with actual user input are included in the statement.
     * <code>chosenOperator</code> is one out of <code>&gt; &lt; = &gt;= &lt;=</code>
     * and is chosen by the user through a GUI.
     * @param parent This panel's parent frame. Dialogs this panel uses are modal
     * to <code>parent</code>.
     * @param maxWidth This panel's maximum width.
     * @param databaseConnection A connection to the appropriate database.
     * @param columns The columns which should be included in the <code>ResultSet</code>
     * representing filtered data.
     * @param tableName The name of the database table containing the <code>columns</code>.
     * @param orderBy Parameter of the query's <code>ORDER BY</code> clause.
     * A comma-separated list of the columns by which the filtered data should
     * be ordered.
     * @param stringFields All fields which contain strings. The strings in this
     * array have to correspond to columns in the database containing strings
     * (<code>VARCHAR</code>s).
     * @param numberFields All fields which contain numbers. The strings in
     * this array have to correspond to columns in the database containing numbers.
     * @param dateFields All fields which contain dates. The strings in
     * this array have to correspond to columns in the database containing dates.
     * @param stringFieldDescriptions The descriptions of all the <code>stringFields</code>.
     * These descriptions are presented to the user in the GUI, so choose
     * meaningful things here. The order of the items must correspond to that
     * of those in the <code>stringFields</code> array.
     * @param numberFieldDescriptions The descriptions of all the <code>numberFields</code>.
     * These descriptions are presented to the user in the GUI, so choose
     * meaningful things here. The order of the items must correspond to that
     * of those in the <code>numberFields</code> array.
     * @param dateFieldDescriptions The descriptions of all the <code>dateFields</code>.
     * These descriptions are presented to the user in the GUI, so choose
     * meaningful things here. The order of the items must correspond to that
     * of those in the <code>dateFields</code> array.
     * @throws SQLException if any error related to the database occurs. This
     * can be a lost connection, a missing table, missing rows and so on.
     * @throws IllegalArgumentException if
     * <ul>
     * <li>any of <code>columns, tableName, databaseConnection</code> is <code>null</code>
     * or <code>tableName</code> is a void string</li>
     * <li>all field arrays are <code>null</code> or have a length of zero</li>
     * <li>a descriptions string is <code>null</code> although the corresponding
     * field string is not <code>null</code> and has a length greater than zero</li>
     * <li>a description string's length is less than that of the corresponding
     * field string</li>
     * </ul>
     */
    public FilterPanel(java.awt.Frame parent, int maxWidth, Connection databaseConnection,
            String[] columns, String tableName, String orderBy, String[] stringFields, String[]
            numberFields, String[] dateFields, String[] stringFieldDescriptions, String[]
            numberFieldDescriptions, String[] dateFieldDescriptions) throws SQLException{

        if(columns == null || tableName == null || tableName.length() == 0
          || databaseConnection == null
          || (stringFields == null && numberFields == null && dateFields == null)
          || (stringFields.length == 0 && numberFields.length == 0 && dateFields.length == 0)
          || (numberFields != null && numberFields.length > 0 && numberFieldDescriptions == null)
          || (stringFields != null && stringFields.length > 0 && stringFieldDescriptions == null)
          || (dateFields != null && dateFields.length > 0 && dateFieldDescriptions == null)
          || (stringFields != null && stringFieldDescriptions.length < stringFields.length)
          || (numberFields != null && numberFieldDescriptions.length < numberFields.length)
          || (dateFields != null && dateFieldDescriptions.length < dateFields.length)
          )
            throw new IllegalArgumentException("Some parameters are missing or" +
                    " incorrect.");

        this.parent = parent;
        this.orderBy = orderBy;


        //build basic query string
        basicQueryString = "SELECT ";
        for(String column : columns) {
            basicQueryString += column;
            if(!column.equals(columns[columns.length -1]))
                basicQueryString += ",";
        }

        basicQueryString += " FROM ";
        basicQueryString += tableName;

        //set the ResultSet to contain the full data of the table
        stmt = databaseConnection.createStatement();
        results = stmt.executeQuery(basicQueryString);

        //============================BUILD GUI===============================//

        //a) build string fields
        for(int ct = 0; ct < stringFields.length; ct++) {

            JPanel filterFieldPanel = new JPanel(); //holds input field and description

            filterFieldPanel.add(new JLabel(stringFieldDescriptions[ct]));
            final JTextField input = new JTextField();
            input.setText("");
            input.setColumns(10);
            input.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent ev) {
                    input.selectAll();
                }
            });

            input.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent ev) {
                    filterEntered();
                }
            });

            filterFieldPanel.add(input);
            fieldInputFields.put(stringFields[ct], new JTextField[] {input});
            fieldPanels.put(stringFields[ct], filterFieldPanel);
            fieldTypes.put(stringFields[ct], STRING_FIELD);
        }//end for(stringFields)

        //b) build number fields
        for(int ct = 0; ct < numberFields.length; ct++) {

            JPanel filterFieldPanel = new JPanel(); //holds input field and description

            filterFieldPanel.add(new JLabel(numberFieldDescriptions[ct]));
            final JTextField input = new JTextField();
            input.setText("");
            input.setColumns(10);
            input.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent ev) {
                    input.selectAll();
                }
            });

            input.addActionListener(new ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent ev) {
                    filterEntered();
                }
            });
            
            //add operator switcher button
            OperatorSwitcherButton button = new OperatorSwitcherButton(
                        OperatorValue.EQUALS);
            filterFieldPanel.add(button);
            
            //add input field to field panel and add everything to its list
            filterFieldPanel.add(input);
            fieldInputFields.put(numberFields[ct], new JTextField[] {input});
            operatorSwitcherButtons.put(numberFields[ct], button);
            fieldPanels.put(numberFields[ct], filterFieldPanel);
            fieldTypes.put(numberFields[ct], NUMBER_FIELD);
        }//end for(numberFields)

        //c) build date fields
        for(int ct = 0; ct < dateFields.length; ct++) {

            JPanel filterFieldPanel = new JPanel(); //holds input field and description
            filterFieldPanel.add(new JLabel(dateFieldDescriptions[ct]));

            //add operator switcher button
            OperatorSwitcherButton button = new OperatorSwitcherButton(
                        OperatorValue.EQUALS);
            filterFieldPanel.add(button);

            //add input fields for day, month, and year

            JTextField[] inputFields = new JTextField[3];

            for(int ct2 = 0; ct2 < 3; ct2++) {
                final JTextField input = new JTextField();
                inputFields[ct2] = input;

                if(ct2 == 3)
                    input.setColumns(4);
                else
                    input.setColumns(2);

                input.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(java.awt.event.FocusEvent ev) {
                        input.selectAll();
                    }
                });

                input.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent ev) {
                        filterEntered();
                    }
                });

                filterFieldPanel.add(input);
                if(ct2 != 2)
                    filterFieldPanel.add(new JLabel("."));
            }

            //add everything to its list
            fieldInputFields.put(dateFields[ct], inputFields);
            operatorSwitcherButtons.put(dateFields[ct], button);
            fieldPanels.put(dateFields[ct], filterFieldPanel);
            fieldTypes.put(dateFields[ct], DATE_FIELD);
        }//end for(dateFields)

        //determine necessary panel height and add panels

        //Panel height is that of the first panel in the list.
        //Note that it is assumed that all panels have the same height.
        int panelHeight = (int)(fieldPanels.values().toArray(new JPanel[1])[0].
                getPreferredSize().getHeight())+5;
        int height = panelHeight;
        int width = 0;

        for(JPanel panel : fieldPanels.values()) {
            add(panel);

            int panelWidth = (int)panel.getPreferredSize().getWidth();

            width += panelWidth;

            if(width > maxWidth) {
                height += panelHeight;
                width = panelWidth;
            }
        }

        setPreferredSize(new Dimension(maxWidth, height));
    }

    //==============================METHODS===================================//

    /**
     * Evaluates all filters currently active, updates the ResultSet and
     * informs ActionListeners.
     */
    private void filterEntered() {
        
        //=========================BUILD QUERY STRING=========================//
        String queryString = ""+basicQueryString;

        int fieldCount = 0;

        for(String fieldName : fieldTypes.keySet()) {

            //if the user has not requested any filter for this field: continue
            String input1 = fieldInputFields.get(fieldName)[0].getText();
            if(input1.length() == 0)
                continue;

            //test if the user attempts to use forbidden characters
            if(!Util.checkInput(input1))
                return;

            fieldCount++;

            if(fieldCount == 1)
                queryString += " WHERE ";

            //add AND keyword if necessary
            if(fieldCount > 1)
                queryString += " AND ";

            //add string field
            if(fieldTypes.get(fieldName) == STRING_FIELD) {
                queryString += "LOWER(" + fieldName + ") LIKE"
                        + " LOWER('%" + input1 + "%')";
            }

            //add number field
            if(fieldTypes.get(fieldName) == NUMBER_FIELD) {
                try {
                    Double.parseDouble(input1.replaceAll(",", "."));
                }
                catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "In einem Zahlenfeld" +
                            " steht keine Zahl.", "Eingabefehler", JOptionPane.
                            ERROR_MESSAGE);
                    return;
                }

                queryString += fieldName + " ";
                queryString += operatorSwitcherButtons.get(fieldName)
                        .getStringOperator() + " ";
                queryString += input1;
            }

            //add date field
            if(fieldTypes.get(fieldName) == DATE_FIELD) {
                int input2, input3;

                try {
                    Integer.parseInt(fieldInputFields.get(fieldName)[0]
                            .getText());
                    input2 = Integer.parseInt(fieldInputFields.get(fieldName)[1]
                            .getText());
                    input3 = Integer.parseInt(fieldInputFields.get(fieldName)[2]
                            .getText());
                }
                catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "In einem Datumsfeld" +
                            " steht kein korrektes Datum.", "Eingabefehler", JOptionPane.
                            ERROR_MESSAGE);
                    return;
                }

                GregorianCalendar date = new GregorianCalendar(
                        Util.expandYear(input3), input2-1,
                        Integer.parseInt(input1));

                queryString += fieldName + " ";
                queryString += operatorSwitcherButtons.get(fieldName)
                        .getStringOperator() + " ";
                queryString += "'"+Util.getDateString(date)+"'";
            }
        }//end for all fields

        //append ORDER BY clause
        queryString += " ORDER BY " + orderBy;

        //=============================EXECUTE QUERY==========================//

        try {
            results = stmt.executeQuery(queryString);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(parent, "Fehler bei der Kommunikation" +
                            " mit der Datenbank.", "Netzwerkfehler", JOptionPane.
                            ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        //inform listeners
        for(ActionListener listener : listeners)
            listener.actionPerformed(null);
    }

    /**
     * Returns a subset of the given database table where all rows match the
     * filters entered by the user. See {@link #FilterPanel}.
     * @return The filtered data. This <code>ResultSet</code> might contain
     * no rows, but is never <code>null</code>.
     * <p>
     * Note that this method should only be called <i>once</i> when a filter is
     * entered. The <code>ResultSet</code> is not regenerated, so the cursor is
     * likely to be positioned after the last row, making the <code>ResultSet</code>
     * (it being non-scrollable) quite useless.
     */
    public ResultSet getFilteredData() {
        return results;
    }

    /**
     * Clears all filters entered by the user so far and updates the filtered
     * data accordingly. Note that an <code>ActionEvent</code> is fired after
     * the operation is completed, so whatever you usually do with the data
     * should be done automatically.
     */
    public void clearFilters() {
        for(JTextField[] inputFields : fieldInputFields.values())
            for(JTextField inputField : inputFields)
                inputField.setText("");

        filterEntered();
    }

    /**
     * Adds a component to the list of components that is informed whenever
     * a filter is changed. Note that the <code>ActionEvent</code>s this
     * panel fires are all <code>null</code>.
     * @param <T> Any class that implements the <code>ActionListener</code>
     * interface.
     * @param listener The listener.
     */
    public <T extends ActionListener> void addActionListener(T listener) {
        listeners.add(listener);
    }

    //===================INNER CLASS OPERATORSWITCHERBUTTON===================//

    /**
     * A button with five states representing the five operators the user can
     * choose from for date and number fields: <, >, <=, >=, = . The user can
     * press the button to switch between its states and the user can
     * determine which operator is chosen by calling getOperator().
     */
    private class OperatorSwitcherButton extends javax.swing.JButton
            implements ActionListener{

        private static final long serialVersionUID=1;

        OperatorValue operator = null;

        public OperatorSwitcherButton(OperatorValue operator) {
            super();
            this.operator = operator;
            setDisplay();
            addActionListener(this);
        }

        //switches the operator.
        public void actionPerformed(java.awt.event.ActionEvent ev) {
            if(operator.ordinal() == 4) {
                setOperator(OperatorValue.EQUALS);
                return;
            }


            for(OperatorValue tmp : OperatorValue.values()) {
                if(tmp.ordinal() == operator.ordinal()+1) {
                   setOperator(tmp);
                   break;
                }
            }
        }

        /**
         * Sets the current operator.
         * @param operator The new operator.
         */
        public void setOperator(OperatorValue operator) {
            this.operator = operator;
            setDisplay();
        }

        /**
         * Returns the operator as an <code>OperatorValue</code>.
         * @return The operator.
         */
        public OperatorValue getOperator() {
            return operator;
        }

        /**
         * Returns the operator as a <code>String</code>.
         * @return "<" | ">" | "<=" | ">=" | "="
         */
        public String getStringOperator() {
            switch(operator) {
                case EQUALS: return "=";
                case LESS_THAN: return "<";
                case GREATER_THAN: return ">";
                case GREATER_THAN_EQUAL: return ">=";
                case LESS_THAN_EQUAL: return "<=";
                default: return null;
            }
        }

        //sets the button display according to the currently chosen operator.
        private void setDisplay() {
            switch(operator) {
                case EQUALS:
                    setText("=");
                    break;
                case LESS_THAN:
                    setText("<");
                    break;
                case GREATER_THAN:
                    setText(">");
                    break;
                case GREATER_THAN_EQUAL:
                    setText(">=");
                    break;
                case LESS_THAN_EQUAL:
                    setText("<=");
                    break;
            }
        }
    }

    //======================INNER ENUM OPERATORVALUES=========================//

    /**
     * These are the operators which the user can choose from using an
     * OperatorSwitcherButton.
     */
    private enum OperatorValue {
        EQUALS, LESS_THAN, GREATER_THAN, GREATER_THAN_EQUAL, LESS_THAN_EQUAL;
    }
}
