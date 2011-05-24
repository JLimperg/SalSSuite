package salssuite.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Provides utility methods for dealing with passwords. This means especially 
 * encryption and managing password files.
 * @author Jannis Limperg
 */
public class Passwords {


//==============================CONSTANT======================================\\


//===============================FIELDS=======================================\\


//=============================CONSTRUCTORS===================================\\


//===============================METHODS======================================\\

    /**
     * Computes a password hash from the given <code>password</code>, using the
     * MD5 algorithm.
     * @param password The password to be encrypted.
     * @return An MD5 hash of <code>password</code>.
     * @throws java.security.NoSuchAlgorithmException if the MD5 algorithm was
     * not found on the system.
     */
    public static String encryptPassword(String password) throws java.security.
            NoSuchAlgorithmException{
        String pwencrypt;
        MessageDigest digi;

        digi = MessageDigest.getInstance("SHA");

        digi.reset();
        digi.update(password.getBytes());
        byte[] result = digi.digest();

        StringBuilder hexString = new StringBuilder();
        for (int i=0; i<result.length; i++) {
            hexString.append(Integer.toHexString(0xFF & result[i]));
        }
        pwencrypt = hexString.toString();

        return pwencrypt;
    }


    /**
     * Returns all the password hashes contained in <code>pwFile</code>.
     * @return The encrypted passwords, or <code>null</code> if the file was
     * not readable, not valid or did not contain any passwords.
     * @param pwFile The file from which the passwords are to be read.
     * Password hashes must be separated by newline characters.
     */
    public static String[] getAllPasswords(File pwFile) {

        LinkedList<String> passwords = new LinkedList<String>();
        Scanner in;

        //connect to file
        try {
            in = new Scanner(new java.io.FileReader(pwFile));
        }
        catch(java.io.IOException e) {
            e.printStackTrace();
            return null;
        }

        //read the data
        while(true) {
            if(!in.hasNext()) {
                break;
            }
            try {
                passwords.add(in.nextLine());
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return passwords.toArray(new String[1]);
    }

    /**
     * Returns the first password hash contained in the specified file. This
     * convenience method returns <code>getAllPasswords(pwFile)[0]</code>, or
     * <code>null</code> if the latter returns <code>null</code>.
     * @param pwFile The file containing one or more password hashes, separated
     * by newline characters.
     * @return The first of the passwords contained in <code>pwFile</code>.
     */
    public static String getPassword(File pwFile) {

        String[] passwords = getAllPasswords(pwFile);
        if(passwords != null)
            return getAllPasswords(pwFile)[0];
        else
            return null;
    }

    /**
     * Prints the given password hash to the given output file. If the file
     * does not yet exist, it is created along with its parent directories. All
     * data contained in the file is overwritten.
     * @param outputFile The file to write the password to.
     * @param password The password hash. For encryption use
     * {@link #encryptPassword}.
     * @throws FileAccessException if something goes wrong while accessing
     * the output file and writing the data.
     */
    public static void writePasswordToFile(String password, File outputFile) throws
            FileAccessException {

        try {
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();

            PrintWriter out = new PrintWriter(new FileWriter(outputFile));
            out.println(password);
            out.flush();
            out.close();
        }
        catch(java.io.IOException e) {
            throw new FileAccessException(e.getMessage(), outputFile);
        }

    }

//===========================HANDLER METHODS==================================\\

}//end class
