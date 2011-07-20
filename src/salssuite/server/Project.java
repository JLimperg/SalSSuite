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

package salssuite.server;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import salssuite.util.FileAccessException;
import salssuite.util.Util;

/**
 * Represents one SalS project. It contains the following data (on which detailed
 * information is given below):
 * <ul>
 * <li>the project's name</li>
 * <li>the port on which the Derby database is started</li>
 * <li>the output and input path</li>
 * <li>the days on which the project starts and ends</li>
 * </ul>
 * 
 * <p>
 * These project settings can be saved to an xml file and loaded from it using
 * methods provided by this class.
 *
 * <h3>Meanings of the fields</h3>
 * <b>project name</b> - An arbitrary name for the project. This name is used
 * by the programme to distinguish projects.
 * <p>
 * <b>port</b> - Any valid port number. This is provided in case the default
 * port is blocked (which is not very likely as it is a really high one) so
 * that the user can bypass the blocked port by specifying another one. The server
 * actually uses this port (for the authentication mechanism) <i>and</i> the port
 * one above this port (for the database server). So if for example
 * "49800" is given here, the server uses ports 49800 and 49801.
 * <p>
 * <b>output path</b> - The directory to which all output of the server goes.
 * <p>
 * <b>input path</b> - The directory from which data can be imported when creating
 * a new project. The user can put csv files here which are read by certain
 * modules to build data which is then available in the database.<br/>
 * This can for example be data about all the citizens of the state: It is
 * rather helpful to be able to pass the citizen data as a table which might
 * have been obtained from the secretariat instead of entering every citizen
 * by hand.
 * <p>
 * <b>start day & end day</b> - The days on which the project starts and ends
 * respectively.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class Project {


//==============================CONSTANT======================================\\


//===============================FIELDS=======================================\\

    //the port the server is to listen on
    int port;

    //the files for the server to do i/o
    File inputPath;
    File outputPath;

    //the durance of the project, in days.
    int projectDurance;

    //starting and ending day
    GregorianCalendar startDay;
    GregorianCalendar endDay;

    //project's name
    String projectName;


//=============================CONSTRUCTORS===================================\\

    /**
     * Default constructor. Note that <code>startDay</code> and <code>endDay</code>
     * <i>must</i> lie in the same year so the project durance can be computed
     * correctly (see {@link #computeProjectDurance}).
     * @param name The project's name.
     * @param startDay The day on which the project starts.
     * @param endDay The day on which the project ends.
     * @param port The port the server should run on. Must be a positive
     * <code>int</code>.
     * @param inputPath The path where the input comes from. Must be an existing
     * directory. If no input should be processed, pass <code>null</code> here.
     * @param outputPath The path generated output should be written
     * to. Must be an existing directory.
     * @throws ProjectException if
     * <ul>
     * <li><code>inputPath</code> is not <code>null</code> and is not a valid directory, or
     * does not exist</li>
     * <li><code>outputPath</code> is not a valid directory or does not exist</li>
     * <li><code>port</code> is negative</li>
     * <li><code>endDay</code> does not represent a date after startDay</li>
     * <li><code>endDay</code> and <code>startDay</code> do not lie in the
     * same year.
     * </ul>
     */
    public Project(String name, GregorianCalendar startDay,
            GregorianCalendar endDay,
            int port, File inputPath, File outputPath) throws ProjectException {

        this.projectName = name; this.startDay = startDay; this.endDay= endDay;
        this.port = port; this.inputPath = inputPath; this.outputPath = outputPath;

        if(inputPath != null && !inputPath.isDirectory() && inputPath.exists())
            throw new ProjectException("Der Eingabepfad ist kein gültiger Ordner"
                    + " oder existiert nicht.");
        
        if(!outputPath.isDirectory() && outputPath.exists())
            throw new ProjectException("Der Ausgabepfad ist nicht gesetzt oder"
                    + " existiert nicht.");

        if(port < 0)
            throw new ProjectException("Der Port darf nicht negativ sein.");

        if(!this.endDay.after(this.startDay))
            throw new ProjectException("Der Starttag muss vor dem Endtag liegen.");

        if(!(this.endDay.get(GregorianCalendar.YEAR) ==
                this.startDay.get(GregorianCalendar.YEAR)))
            throw new ProjectException("End- und Starttag müssen im gleichen"
                    + " Jahr liegen.");

        computeProjectDurance();
    }//end constructor 1

    /**
     * Creates an empty project. All fields are assigned <code>null</code> or
     * <code>-1</code> respectively. The fields should be assigned their
     * values afterwards, otherwise errors are very likely to occur.
     */
    public Project() {
        
    }//end constructor 3


//===============================METHODS======================================\\


    //I/O

    /**
     * Prints all data to the specified <code>outputFile</code>. The data is written in
     * xml format, so <code>outputFile</code> should end in '.xml'.
     * <p>
     * The file structure is as follows:<br/>
     * <br/><code>
     * &lt;project_data&gt;<br/>
     * --- &lt;project_name&gt;...&lt;/project_name&gt;<br/>
     * --- &lt;project_durance&gt;...&lt;/project_durance&gt;<br/>
     * --- &lt;output_path&gt;...&lt;/output_path&gt;<br/>
     * --- &lt;input_path&gt;...&lt;/input_path&gt;<br/>
     * --- &lt;currency_symbol&gt;...&lt;/currency_symbol&gt;<br/>
     * --- &lt;end_day&gt;...&lt;/end_day&gt;<br/>
     * --- &lt;start_day&gt;...&lt;/start_day&gt;<br/>
     * --- &lt;port&gt;...&lt;/port&gt;<br/>
     * &lt;/project_data&gt;</code>
     * @param outputFile The file the data should be written to.
     * @throws FileAccessException  if connecting to the file fails for any
     * reason or if an error occurs while writing the data.
     */
    public void saveData(File outputFile) throws FileAccessException {

        PrintWriter out;
        //connect to the file
        try {
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
            out = new PrintWriter(new FileWriter(outputFile));
        }
        catch(Exception e) {
            throw new FileAccessException("Fehler: Konnte Ausgabedatei nicht öffnen.",
                    outputFile);
        }

        //print the data
        try {
            //header
            out.println("<project_data>");

            //data
            out.println("\t<project_name>"+projectName+"</project_name>");
            out.println("\t<project_durance>"+projectDurance+"</project_durance>");
            out.println("\t<output_path>"+outputPath.getAbsolutePath()+"</output_path>");
            if(inputPath != null)
                out.println("\t<input_path>"+inputPath.getAbsolutePath()+"</input_path>");
            else
                out.println("\t<input_path></input_path>");
            out.println("\t<end_day>"+Util.getDateString(endDay)+"</end_day>");
            out.println("\t<start_day>"+Util.getDateString(startDay)+"</start_day>");
            out.println("\t<port>"+port+"</port>");

            //footer
            out.println("</project_data>");
            out.flush();
            out.close();
        }
        catch(Exception e) {
            throw new FileAccessException("Fehler beim Schreiben der Daten ("+e.getMessage()+
                    ").");
        }
    }


    /**
     * Loads the project data from an xml file created with the
     * {@link #saveData} method.
     * Note that no effort is made to ensure that good input is provided. If the
     * method encounters any difficulties, a <code>FileAccessException</code> or
     * <code>ProjectException</code> will be thrown.
     * @param inputFile The file containing the data in xml format.
     * @throws FileAccessException  if connecting to the file fails for any
     * reason.
     * @throws ProjectException if an error occurs while restoring the data.
     */
    public void loadData(File inputFile) throws FileAccessException, ProjectException{

        //connect to the file and build the DOM
        Node root;
        NodeList vals;
        try {
            Document xmldoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                parse(inputFile);
            root = xmldoc.getDocumentElement();
            vals = root.getChildNodes();
        }
        catch(Exception e) {
            throw new FileAccessException("Konnte Inputdatei nicht lesen.", inputFile);
        }

        //restore the data
        try {
            for(int ct = 0; ct < vals.getLength(); ct++) {
                Node temp = vals.item(ct);

                if(!(temp instanceof Element))
                    continue;

                Element tmp = (Element)temp;

                String name = tmp.getTagName();
                String content = tmp.getTextContent();

                if(name.equals("project_name"))
                    projectName = tmp.getTextContent();
                else if (name.equals("project_durance"))
                    projectDurance = Integer.parseInt(content);
                else if (name.equals("output_path"))
                    outputPath = new File(content);
                else if (name.equals("input_path"))
                    inputPath = new File(content);
                else if (name.equals("end_day"))
                    endDay = parseDate(content);
                else if (name.equals("start_day"))
                    startDay = parseDate(content);
                else if (name.equals("port"))
                    port = Integer.parseInt(content);
            }
        }
        catch (Exception e) {
            throw new ProjectException("Fehler: Ungültiger Input.");
        }

    }//end loadData()

    //DATA MANAGEMENT

    /**
     * Recomputes the project durance using the currently set <code>startDay</code>
     * and <code>endDay</code>.<br/>
     * Errors might occur due to logical errors, such as the startDay being later
     * than the endDay. This can happen if
     * {@link #setStartDay} or {@link #setEndDay} were used improperly
     * before.
     */
    public void computeProjectDurance() {
        projectDurance = endDay.get(GregorianCalendar.DAY_OF_YEAR) - startDay.get(
                GregorianCalendar.DAY_OF_YEAR);
    }

    //Parses a GregorianCalendar from a standard date string.
    //throws ProjectException if not parseable or invalid.
    private GregorianCalendar parseDate(String date) throws ProjectException{

        String[] split = Util.parseDateString(date);

        //test whether string contains valid ints
        try {
            int D = Integer.parseInt(split[2]);
            int M = Integer.parseInt(split[1]);
            int Y = Integer.parseInt(split[0]);

            if(D <= 0 || D > 31 || M< 0 || M > 12 || Y<= 0)
                throw new NumberFormatException();
            
            return new GregorianCalendar(Y, M-1, D);
        }
        catch(Exception e) {
            throw new ProjectException("Date not parseable.");
        }
    }


    //GETTER/SETTER

    /**
     * Returns the day on which this project ends.
     * @return The ending day.
     */
    public GregorianCalendar getEndDay() {
        return endDay;
    }

    /**
     * Sets the day on which the project ends.The project durance is
     * recomputed automatically.<br/>
     * It is not checked whether <code>endDay</code> is after <code>startDay</code>,
     * nor whether they represent days in the same year. However, both
     * is strongly recommended; otherwise the project durance will be computed
     * wrongly.
     * @param endDay The day on which the project ends.
     */
    public void setEndDay(GregorianCalendar endDay) {
        this.endDay = endDay;
        computeProjectDurance();
    }

    /**
     * Returns the path where all data is parsed from.
     * @return The input path.
     */
    public File getInputPath() {
        return inputPath;
    }

    /**
     * Sets the path all the data is parsed from. Must
     * be an existing directory.
     * @param inputPath The input path.
     * @throws ProjectException if <code>inputPath</code> is not a directory
     * or does not exist.
     * @throws NullPointerException if <code>inputPath</code> is <code>null</code>.
     */
    public void setInputPath(File inputPath) throws ProjectException {
        if(!inputPath.isDirectory() || !inputPath.exists())
            throw new ProjectException("Der Eingabepfad ist kein gültiger Ordner"
                    + " oder existiert nicht.");
        this.inputPath = inputPath;
    }

    /**
     * Returns the directory all output data is written to.
     * @return The output path.
     */
    public File getOutputPath() {
        return outputPath;
    }

    /**
     * Sets the path all output is written to. Must be
     * an existing directory.
     * @param outputPath The new output path.
     * @throws ProjectException if <code>outputPath</code> is not a valid directory
     * or does not exist.
     * @throws NullPointerException if <code>outputPath</code> is <code>null</code>.
     */
    public void setOutputPath(File outputPath) throws ProjectException {
        if(!outputPath.isDirectory())
            throw new ProjectException("Der Ausgabepfad ist kein gültiger Ordner"
                    + " oder existiert nicht.");
        this.outputPath = outputPath;
    }

    /**
     * Returns the port the server listens on.
     * @return The port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port the server listens on. Must
     * be a positive <code>int</code>.
     * @param port The listening port.
     * @throws ProjectException if <code>port</code> is less than zero.
     */
    public void setPort(int port) throws ProjectException {
        if(port < 0)
            throw new ProjectException("Der Port darf nicht negativ sein.");
        this.port = port;
    }

    /**
     * Returns the project durance in days, computed using the current
     * <code>startDay</code> and <code>endDay</code>.
     * @return The project durance.
     */
    public int getProjectDurance() {
        return projectDurance;
    }

    /**
     * Returns the project's name.
     * @return The project's name.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the project's name.
     * @param projectName The new project name.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Returns the day on which this project starts.
     * @return The starting day.
     */
    public GregorianCalendar getStartDay() {
        return startDay;
    }

    /**
     * Sets the day on which the project starts. The project durance is recomputed.
     * <br/>
     * It is not checked whether <code>endDay</code> is after <code>startDay</code>,
     * nor whether they represent days in the same year. However, both
     * is strongly recommended; otherwise the project durance will be computed
     * wrongly.
     * @param startDay The day when the project starts.
     */
    public void setStartDay(GregorianCalendar startDay) {
        this.startDay = startDay;
        computeProjectDurance();
    }


//===========================HANDLER METHODS==================================\\

}//end class
