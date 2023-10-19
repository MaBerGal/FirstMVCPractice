package practiceMVC;

import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// Class for storing and managing employee information.
public class EmployeeModel {
    // Stores the unique identifier for the employee.
    private int employeeNumber;
    // Stores the employee's name.
    private String name;
    // Stores the date when the employee was hired.
    private GregorianCalendar hireDate;



    // Stores the employee's phone number.
    private String phoneNumber;



    // Stores the employee's email address.
    private String emailAddress;

    // Constructor for creating an EmployeeModel object with input validation.
    public EmployeeModel(String name, int employeeNumber, String hireDate, String phoneNumber, String emailAddress) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        this.hireDate = createGregorianCalendar(hireDate);
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;

    }

    // Constructor that accepts name and employee number
    public EmployeeModel(String name, int employeeNumber) {
        this.employeeNumber = employeeNumber;
        this.name = name;
        // Initialize hireDate, phoneNumber, and emailAddress to default values
        this.hireDate = null;
        this.phoneNumber = "";
        this.emailAddress = "";
    }

    // Getter methods for retrieving employee data.
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public String getName() {
        return name;
    }

    public GregorianCalendar getHireDate() {
        return hireDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    // Setter methods for modifying employee data.
    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHireDate(GregorianCalendar hireDate) {
        this.hireDate = hireDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public GregorianCalendar createGregorianCalendar(String date) {
        String[] parts = date.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1; // Calendar months are zero-based.
        int year = Integer.parseInt(parts[2]);
        return new GregorianCalendar(year, month, day);
    }


    @Override
    public String toString() {
        return name + " " + employeeNumber + " " + hireDate + " " + phoneNumber + " " + emailAddress;
    }

}