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
 * Represents one citizen in the SalS project. This class may be seen rather as
 * a structure, as it mainly contains fields and the appropriate
 * getter/setter-methods.
 * @author Jannis Limperg
 * @version 1.0.1
 */
public class Citizen implements Comparable<Citizen>
{
    //================================FIELDS==================================//
    //basic data
    int ID = -1;
    String forename = null;
    String surname = null;
    String form = null;

    //profession data
    int companyNumber = -1;

    //==============================CONSTRUCTORS==============================//
    /**
     * Full constructor.
     * @param ID Citizen's ID.
     * @param forename Citizen's forename.
     * @param surname Citizen's surname.
     * @param form Citizen's form.
     * @param companyNumber The ID of the company in which the citizen works.
     */
    public Citizen(int ID, String forename, String surname, String form,
            int companyNumber)
    {
        this.ID=ID; this.forename=forename; this.surname=surname; this.form=form;
        this.companyNumber=companyNumber;
    }


    /**
     * Constructor to be used if all data except the company data is
     * already known.
     * @param ID Citizen's ID.
     * @param forename Citizen's forename.
     * @param surname Citizen's surname.
     * @param form Citizen's form.
     */
    public Citizen(int ID, String forename, String surname, String form)
    {
        this(ID, forename, surname, form, -1);
    }

    /**
     * Constructor to be used if you need a void Citizen object.
     */
    public Citizen()
    {
    }


    //================================METHODS=================================//
    
    //SPECIAL METHODS

    /**
     * Determines whether <code>citi</code> has the same ID as this citizen.
     * @param citi The citizen to compare with.
     * @return <code>true</code> if the two have the same ID;<br/>
     * <code>false</code> if their IDs differ, or if <code>null</code> is passed.
     */
    public boolean equals(Citizen citi) {
        if(citi == null)
            return false;

        if(ID == citi.getID())
        {
            return true;
        }
        return false;
    }//end equals()

    
    //GETTER/SETTER

    /**
     * Returns this citizen's ID.
     * @return The ID.
     * <code>-1</code> by default if not set.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the citizen's ID.
     * @param ID The new ID.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Returns the number of this citizen's company.
     * @return The number of the company where the citizen currently works.
     * <code>-1</code> by default if not set.
     */
    public int getCompanyNumber() {
        return companyNumber;
    }

    /**
     * Sets the citizen's company number.
     * @param companyNumber The new company number.
     */
    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    /**
     * Returns this citizen's forename.
     * @return The citizen's forename.
     * <code>null</code> if not set.
     */
    public String getForename() {
        return forename;
    }

    /**
     * Sets the citizen's forename.
     * @param forename The forename.
     */
    public void setForename(String forename) {
        this.forename = forename;
    }

    /**
     * Returns this citizen's form.
     * @return The citizen's form.
     * <code>null</code> if not set.
     */
    public String getForm() {
        return form;
    }

    /**
     * Sets the citizen's form.
     * @param form The citizen's form.
     */
    public void setForm(String form) {
        this.form = form;
    }

    /**
     * Returns this citizen's surname.
     * @return The citizen's surname.
     * <code>null</code> if not set.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the citizen's surname to the specified value.
     * @param surname The new surname.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    //REQUIRED BY INTERFACE
    /**
     * Compares the citizen to another one, only considering the ID.
     * @param citi The citizen this citizen is to be compared with.
     * @return <b>-1</b> if this citizen's ID is smaller;<br/>
     * <b>0</b> if the IDs are equal;<br/>
     * <b>1</b> if this citizen's ID is greater.
     */
    public int compareTo(Citizen citi) {
        if(citi.getID() < ID)
            return -1;
        else if(citi.getID() == ID)
            return 0;
        return 1;
    }

}//end class
