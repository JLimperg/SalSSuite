package salssuite.server;

/**
 * Indicates that something went wrong concerning the data of a
 * {@link Project}.
 * <p>
 * This exception provides no special methods or fields, it is merely there
 * to identify a special kind of problem not only semantically, but also
 * syntactically.
 * @author Jannis Limperg
 */
public class ProjectException extends Exception {

    private static final long serialVersionUID=1;

    /**
     * Creates a new instance of <code>ProjectException</code> without a detail
     * message.
     */
    public ProjectException() {
    }


    /**
     * Creates an instance of <code>ProjectException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProjectException(String msg) {
        super(msg);
    }
}
