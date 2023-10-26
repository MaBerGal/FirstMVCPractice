package practiceMVC;

// For working with dates and times
import java.util.GregorianCalendar;

/**
 * Class for storing and managing employee information.
 */
public class EmployeeModel {
    /**
     * Stores the unique identifier for the employee.
     */
    private int employeeNumber;

    /**
     * Stores the employee's name.
     */
    private String name;

    /**
     * Stores the date when the employee was hired.
     */
    private GregorianCalendar hireDate;

    /**
     * Stores the employee's phone number.
     */
    private String phoneNumber;

    /**
     * Stores the employee's email address.
     */
    private String emailAddress;

    /**
     * Constructor for creating an EmployeeModel object.
     *
     * @param name The name of the employee.
     * @param employeeNumber The unique identifier for the employee.
     * @param hireDate The date when the employee was hired in "DD/MM/YYYY" format.
     * @param phoneNumber The phone number of the employee.
     * @param emailAddress The email address of the employee.
     */
    public EmployeeModel(String name, int employeeNumber, String hireDate, String phoneNumber, String emailAddress) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.hireDate = createGregorianCalendar(hireDate);
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;

    }

    /**
     * Alternate constructor that accepts name and employee number. Initializes hireDate, phoneNumber, and emailAddress to default values.
     *
     * @param name The name of the employee.
     * @param employeeNumber The unique identifier for the employee.
     */
    public EmployeeModel(String name, int employeeNumber) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        // Initialize hireDate, phoneNumber, and emailAddress to default values.
        this.hireDate = null;
        this.phoneNumber = "";
        this.emailAddress = "";
    }

    // Getter methods for retrieving employee data.

    /**
     * Getter method for retrieving the employee's unique identifier.
     *
     * @return The unique identifier of the employee.
     */
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * Getter method for retrieving the employee's name.
     *
     * @return The name of the employee.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for retrieving the date when the employee was hired.
     *
     * @return A GregorianCalendar instance representing the date of hire.
     */
    public GregorianCalendar getHireDate() {
        return hireDate;
    }

    /**
     * Getter method for retrieving the employee's phone number.
     *
     * @return The phone number of the employee.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Getter method for retrieving the employee's email address.
     *
     * @return The email address of the employee.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    // Setter methods for modifying employee data.

    /**
     * Setter method for modifying the employee's unique identifier.
     *
     * @param employeeNumber The new unique identifier for the employee.
     */
    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    /**
     * Setter method for modifying the employee's name.
     *
     * @param name The new name of the employee.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter method for modifying the date when the employee was hired.
     *
     * @param hireDate A GregorianCalendar instance representing the new date of hire.
     */
    public void setHireDate(GregorianCalendar hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * Setter method for modifying the employee's phone number.
     *
     * @param phoneNumber The new phone number of the employee.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Setter method for modifying the employee's email address.
     *
     * @param emailAddress The new email address of the employee.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Creates a GregorianCalendar instance from a date string in format "DD/MM/YYYY" format.
     * @param date The date string to be converted.
     * @return A GregorianCalendar instance representing the provided date.
     */
    public GregorianCalendar createGregorianCalendar(String date) {
        // Split the date string into day, month, and year parts using the slash (/) as a delimiter.
        String[] parts = date.split("/");
        // Extract day, month (adjusted for zero-based indexing) and year.
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1; // Calendar months are zero-based.
        int year = Integer.parseInt(parts[2]);

        // Create and return the GregorianCalendar instance.
        return new GregorianCalendar(year, month, day);
    }

    /**
     * DEBUGGING PURPOSES. Returns a string representation of the EmployeeModel.
     *
     * @return A string containing the employee's name, unique identifier, hire date, phone number, and email address.
     */
    @Override
    public String toString() {
        return name + " " + employeeNumber + " " + hireDate + " " + phoneNumber + " " + emailAddress;
    }

}