package salssuite.util;

/**
 * Represents an employee of a certain company. This class extends the standard
 * <code>Citizen</code> by adding a field for the salary.
 * @author Jannis Limperg
 */
public class Employee extends Citizen {

    //==============================CONSTANTS=================================//

    //===============================FIELDS===================================//

    double salary = 0;

    //============================CONSTRUCTORS================================//

    /**
     * Full constructor.
     * @param ID The employee's ID.
     * @param forename The employee's forename.
     * @param surname The employee's surname.
     * @param form The employee's form.
     * @param companyNumber The number of the company this employee works for.
     * @param salary The employee's salary.
     */
    public Employee(int ID, String forename, String surname, String form,
            int companyNumber, double salary) {
        super(ID, forename, surname, form, companyNumber);
        this.salary = salary;
    }

    /**
     * Creates a new employee with unknown company number.
     * @param ID The employee's ID.
     * @param forename The employee's forename.
     * @param surname The employee's surname.
     * @param form The employee's form.
     * @param salary The employee's salary.
     */
    public Employee(int ID, String forename, String surname, String form, double
            salary) {
        super(ID, forename, surname, form);
        this.salary = salary;
    }

    /**
     * Creates an employee from the given <code>Citizen</code> object.
     * All field values are retained and the <code>salary</code> is set to zero.
     * @param fromCitizen The citizen who this Employee should represent.
     */
    public Employee(Citizen fromCitizen) {
        super(fromCitizen.getID(), fromCitizen.getForename(), fromCitizen.getSurname(),
                fromCitizen.getForm());
        this.salary = 0;
    }

    /**
     * Creates an empty <code>Employee</code> object.
     */
    public Employee() {

    }

    //==============================METHODS===================================//

    /**
     * Returns the employee's salary.
     * @return the salary.
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Sets the employee's salary.
     * @param salary The new salary.
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    //============================INNER CLASSES===============================//

}
