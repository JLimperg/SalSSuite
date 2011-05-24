package salssuite.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Provides general utility methods.
 * <p>
 * The date and time related methods are fairly important as they provide
 * a standard interface which all database modifications should use. This way
 * compatibility between them is ensured.
 * @author Jannis Limperg
 */
public class Util {

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    //============================CONSTRUCTORS================================//

    /**
     * Void constructor. This class provides <code>static</code> methods only;
     * objects of this class are without any function.
     */
    public Util() {

    }

    //==============================METHODS===================================//

    /**
     * Tests if user input contains forbidden characters. The list of forbidden
     * characters is defined by {link Constants#FORBIDDEN_CHARACTERS}.
     *
     * This method automatically displays a message to the user informing the
     * of the fact that certain characters in their input are forbidden.
     * @param input The user input.
     * @return <code>true</code> if the input is valid, that is it does not
     * contain any forbidden characters.<br/>
     * <code>false</code> if the input contains any forbidden characters.
     */
    public static boolean checkInput(String input) {
        
        LinkedList<String> containedForbiddenCharacters = new LinkedList<String>();

        for(String forbiddenCharacter : Constants.FORBIDDEN_CHARACTERS)
            if(input.contains(forbiddenCharacter))
                containedForbiddenCharacters.add(forbiddenCharacter);

        if(containedForbiddenCharacters.isEmpty())
            return true;

        String messageString = "<html>Ihre Eingabe enthält die "
                + "folgenden verbotenen Zeichen:";

        for(String forbiddenCharacter : containedForbiddenCharacters)
            messageString += "<br/><center>\" <b>" + forbiddenCharacter +
            "</b> \"</center>";

        messageString += "</html>";

        JOptionPane.showMessageDialog(null, messageString, "Verbotenes Zeichen",
                JOptionPane.ERROR_MESSAGE);

        return false;
    }

    /**
     * Does the same as {@link #checkInput} just without attempting to display
     * a graphical warning message. Instead a warning is printed to
     * <code>System.out</code>.
     * @param input The user input.
     * @return <code>true</code> if the input is valid, that is it does not
     * contain any forbidden characters.<br/>
     * <code>false</code> if the input contains any forbidden characters.
     */
    public static boolean checkInput_CL(String input) {
        LinkedList<String> containedForbiddenCharacters = new LinkedList<String>();

        for(String forbiddenCharacter : Constants.FORBIDDEN_CHARACTERS)
            if(input.contains(forbiddenCharacter))
                containedForbiddenCharacters.add(forbiddenCharacter);

        if(containedForbiddenCharacters.isEmpty())
            return true;

        String messageString = "Ihre Eingabe enthält die "
                + "folgenden verbotenen Zeichen:";

        for(String forbiddenCharacter : containedForbiddenCharacters)
            messageString +=  " '" + forbiddenCharacter + "'";

        messageString += "";

        System.out.println(messageString);

        return false;
    }

    /**
     * Copies the contents of one text file to another. If the files are
     * directories, all subfiles are copied recursively.
     * @param in The file or directory which should be copied.
     * @param out The file or directory to which the contents of <code>in</code>
     * should be copied.
     * @throws FileAccessException if the files are not accessible, not
     * writable or an error occurs while writing the contents.
     */
    public static void copyFile(File in, File out) throws FileAccessException {

        if(!in.exists())
            throw new FileAccessException("Zu kopierende Datei existiert nicht.", in);

        out.getParentFile().mkdirs();


        //if it's a directory, turn to the files in it
        if(in.isDirectory()) {
            out.mkdirs();
            for(File file : in.listFiles()) {
                copyFile(file, new File(out, file.getName()));
            }
            return;
        }

        //if not, copy the files
        try {
            out.createNewFile();
        }
        catch(IOException e) {
            throw new FileAccessException("Konnte Zieldatei nicht erstellen.",
                    out);
        }

        PrintWriter outPrinter;
        Scanner inReader;

        try {
            outPrinter = new PrintWriter(new FileWriter(out));
            inReader = new Scanner(new FileReader(in));
        }
        catch(IOException e) {
            throw new FileAccessException("Konnte Verbindung zu Ein- oder Aus" +
                    "gabedatei nicht herstellen.");
        }

        if(!out.canWrite()) {
            boolean success = out.setWritable(true);
            if(!success)
                throw new FileAccessException("Kann nicht in Zieldatei schreiben.", out);
        }

        while(true) {
            if(!inReader.hasNext())
                break;

            outPrinter.println(inReader.nextLine());
        }
        outPrinter.flush();
        outPrinter.close();
        inReader.close();
    }

    /**
     * Generates a random string of <code>length</code> characters. The string
     * consists only of letters and numbers.
     * @param length The number of characters the string should have.
     * @return A random string.
     */
    public static String generateRandomString(int length) {

        String randomString = "";

        while(true) {
            if(randomString.length() >= length)
                return randomString.substring(0, length-1);
            else
                randomString += java.util.UUID.randomUUID().toString()
                    .replaceAll("-","");
        }
    }

    /**
     * Adjusts the string length so that it is exactly <code>length</code> characters
     * long. The string is filled with space characters if it is too short or
     * truncated to <code>length</code> characters if it is too long. The
     * truncated substring is replaced with three dots.
     * @param original The string to be processed.
     * @param length The new string's length.
     * @return A new string of the given length.
     */
    public static String adjustStringLength(String original, int length) {

        if(original.length() < length) {
           String filled = original;
           int missingLength = length - original.length();
           for(int ct = 0; ct < missingLength; ct++)
               filled += " ";
           return filled;
        }
        else if(original.length() > length) {
            String trunc;
            trunc = original.substring(0, length-3);
            trunc += "...";
            return trunc;
        }
        else
            return original;
    }

    /**
     * Adds zeroes to the front of a number until it is <code>maxZeroes</code>
     * characters long. An example:
     * <p>
     * <code>
     * String output = addZeroes(10, 3);<br/>
     * System.out.println("The number is: "+output);<br/>
     * <br/>
     * The number is: 010
     * </code>
     * @param number The original figure.
     * @param maxZeroes The maximum number of zeroes to be added.
     * @return A string as defined above.
     */
    public static String addZeroes(int number, int maxZeroes) {

        String zeroes = "";

        for(int ct = maxZeroes-1; ct > 0; ct--) {
            if(number < Math.pow(10, ct))
                zeroes += "0";
            else
                break;
        }

        return zeroes + number;
    }

    /**
     * Adds zeroes to the front of a number until its front part (without the
     * decimals) is <code>maxZeroes</code> characters long. An example:
     * <p>
     * <code>
     * String output = addZeroes(10,789, 3);<br/>
     * System.out.println("The number is: "+output);<br/>
     * <br/>
     * The number is: 010,789
     * </code>
     * @param number The original figure.
     * @param maxZeroes The maximum number of zeroes to be added.
     * @return A string as defined above.
     */
    public static String addZeroes(double number, int maxZeroes) {
        String zeroes = "";

        for(int ct = maxZeroes-1; ct > 0; ct--) {
            if(number < Math.pow(10, ct))
                zeroes += "0";
            else
                break;
        }

        return zeroes + number;
    }

    /**
     * Adds zeroes to the front of a number until it is <code>maxZeroes</code>
     * characters long. An example:
     * <p>
     * <code>
     * String output = addZeroes("10", 3);<br/>
     * System.out.println("The number is: "+output);<br/>
     * <br/>
     * The number is: 010
     * </code>
     * @param number The original figure.
     * @param maxZeroes The maximum number of zeroes to be added.
     * @return A string as defined above.
     * @throws NumberFormatException if <code>number</code> cannot be parsed as
     * an <code>int</code>.
     */
    public static String addZeroes(String number, int maxZeroes) {

        int figure = Integer.parseInt(number);
        return addZeroes(figure, maxZeroes);
    }

    /**
     * Expands a year given with two characters (such as '10' for '2010') so
     * that it has 4 characters. From '31' to '99' it is assumed that the 20th
     * century is meant (resulting in 1931 to 1999), from '00' to '30' the year
     * is expanded to '2000' to '2030'.
     * @param shortYear The year given with two digits.
     * @return The year given with four digits. If the year originally had 3
     * or more digits, it is returned unchanged.
     */
    public static int expandYear(int shortYear) {

        if(shortYear >= 100)
            return shortYear;

        if(shortYear > 0 && shortYear <= 30)
            return 2000+shortYear;
        else if(shortYear > 30 && shortYear <= 99)
            return 1900+shortYear;
        else
            throw new IllegalArgumentException("This year cannot be expanded:"
                    + " "+shortYear);

    }

    /**
     * Constructs a string from the given <code>GregorianCalendar</code>
     * representing the date for storage in the database.
     * The format is yyyy-mm-dd.
     * @param date The date as a <code>GregorianCalendar</code>.
     * @return A date string.
     */
    public static String getDateString(GregorianCalendar date) {
        return ""+
                date.get(GregorianCalendar.YEAR)+"-"+
                (date.get(GregorianCalendar.MONTH)+1)+"-"+
                date.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * Constructs a date string for the current time. This is equal to
     * <code>getDateString(new GregorianCalendar())</code>.
     * @return The current date as a string.
     */
    public static String getDateString() {
        return getDateString(new GregorianCalendar());
    }

    /**
     * Splits a date string as generated by <code>getDateString()</code> into
     * <code>String</code>s for day, month, and year.
     * @param dateString The date string.
     * @return An array containing the year at position 0, the month at
     * position 1 and the day at position 2.
     */
    public static String[] parseDateString(String dateString) {
        return dateString.split("\\-");
    }

    /**
     * Constructs a string from the given <code>GregorianCalendar</code>
     * representing the time for storage in the database. The format is hh:mm:ss.
     * @param time The time as a <code>GregorianCalendar</code>.
     * @return The time as a string.
     */
    public static String getTimeString(GregorianCalendar time) {
        return ""+
                time.get(GregorianCalendar.HOUR_OF_DAY)+":"+
                time.get(GregorianCalendar.MINUTE)+":"+
                time.get(GregorianCalendar.SECOND);
    }

    /**
     * Constructs a time string for the current time. This is equal to
     * <code>getTimeString(new GregorianCalendar())</code>.
     * @return The current time as a string.
     */
    public static String getTimeString() {
        return getTimeString(new GregorianCalendar());
    }

    /**
     * Splits a time string as generated by <code>getTimeString()</code> into
     * <code>String</code>s for hour, minute and second.
     * @param timeString The time string.
     * @return An array containing the hour at position 0, the minute at
     * position 1 and the second at position 2.
     */
    public static String[] parseTimeString(String timeString) {
        return timeString.split("\\:");
    }

    //============================INNER CLASSES===============================//

}
