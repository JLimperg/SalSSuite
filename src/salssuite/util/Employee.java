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

package salssuite.util;

/**
 * Represents an employee of a certain company. This class extends the standard
 * <code>Citizen</code> by adding a field for the salary.
 * @author Jannis Limperg
 * @version 1.0.1
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
