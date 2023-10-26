package practiceMVC;

// For Swing GUI functionality.
import javax.swing.*;
// For working with text on Swing.
import javax.swing.text.*;
// For native system GUI functionality.
import java.awt.*;
// For handling action events.
import java.awt.event.ActionListener;
// For handling focus events.
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
// For formatting dates.
import java.text.SimpleDateFormat;
// For working with Gregorian calendars.
import java.util.GregorianCalendar;
// For accessing general Calendar-related functionality.
import java.util.Calendar;


public class ViewController {
    /**
     * The associated view controlled by this controller.
     */
    private EmployeeView view;

    /**
     * Controller for managing employee data.
     */
    private ListController<EmployeeModel> employeeController;

    /**
     * Represents the currently selected employee.
     */
    private EmployeeModel currentEmployee;

    /**
     * Label for displaying employee information.
     */
    private JLabel employeeInfoLabel;

    /**
     * Node representing the current employee in the controller's linked list.
     */
    private ListController<EmployeeModel>.Node<EmployeeModel> currentEmployeeNode;

    /**
     * Text field for entering the employee's name.
     */
    private JTextField nameField;

    /**
     * Text field for entering the employee's number.
     */
    private JTextField employeeNumberField;

    /**
     * Text field for entering the employee's hire date.
     */
    private JTextField hireDateField;

    /**
     * Text field for entering the employee's phone number.
     */
    private JTextField phoneField;

    /**
     * Text field for entering the employee's email address.
     */
    private JTextField emailField;

    /**
     * Button for navigating back to the previous employee.
     */
    private JButton backButton;

    /**
     * Button for navigating to the next employee.
     */
    private JButton nextButton;

    /**
     * Button for deleting an employee.
     */
    private JButton deleteButton;

    /**
     * Button for creating a new employee.
     */
    private JButton createButton;

    /**
     * Button for modifying an employee.
     */
    private JButton modifyButton;

    /**
     * Button for applying a filter.
     */
    private JButton filterButton;

    /**
     * Flag indicating if a filter is applied.
     */
    private boolean isFilterApplied;

    /**
     * Panel for displaying the view mode.
     */
    private JPanel viewMode;

    /**
     * Panel for displaying the creation mode.
     */
    private JPanel creationMode;

    /**
     * Scroll pane for displaying view content with added scrolling functionality.
     */
    private JScrollPane viewScrollPane;

    /**
     * The current mode of the view.
     */
    private EmployeeView.Mode currentMode;

    /**
     * The main frame for the view.
     */
    private JFrame frame;

    /**
     * Placeholder text for employee name input.
     */
    private String placeHolderName;

    /**
     * Placeholder text for employee number input.
     */
    private String placeHolderNumber;

    /**
     * Placeholder text for employee hire date input.
     */
    private String placeHolderDate;

    /**
     * Placeholder text for employee phone number input.
     */
    private String placeHolderPhone;

    /**
     * Placeholder text for employee email address input.
     */
    private String placeHolderEmail;

    /**
     * Constructs a new ViewController associated with the provided EmployeeView in order to provide all the necessary logic to it.
     *
     * @param view The EmployeeView controlled by this ViewController.
     */
    public ViewController(EmployeeView view) {
        this.view = view;

        // Initialize all attributes using getters from the EmployeeView class.
        setAllAttributes();

        // Establishing placeholder text for the text input fields.
        placeHolderName = "Enter name here";
        placeHolderNumber = "#####";
        placeHolderDate = "DD/MM/YYYY";
        placeHolderPhone = "9 digits";
        placeHolderEmail = "example@mail.com";
    }

    /**
     * Switches the EmployeeView to view mode, where it displays the current employee's information and view mode buttons.
     */
    public void switchToViewMode() {
        // Reset placeholder text for input fields.
        resetFields();

        // Remove the components of the view mode.
        viewMode.removeAll();

        // Clear existing action listeners to prevent event stacking.
        clearActionListeners();

        // Display the current employee's information.
        displayCurrentEmployee();

        // Add the view mode buttons.
        JPanel viewModeButtons = createViewModeButtons();
        viewMode.add(viewModeButtons, BorderLayout.SOUTH);
        viewMode.add(employeeInfoLabel, BorderLayout.CENTER);

        // Update the button states.
        updateButtonStates();

        // Revalidate and repaint the view mode panel.
        viewMode.revalidate();
        viewMode.repaint();

        // DEBUGGING PURPOSES. Print current employee information and node details
        if (currentEmployeeNode != null) {
            System.out.println("Current Employee: " + currentEmployeeNode.getMain());
            System.out.println("Current Employee Node: " + currentEmployeeNode);
        } else {
            System.out.println("Current Employee: null");
        }

        // Check if the current employee node is null, and if so, reset it to the first employee.
        if (currentEmployeeNode == null) {
            currentEmployeeNode = employeeController.getFirstNode();
            System.out.println("Resetting currentEmployeeNode to the first employee.");
        }
    }

    /**
     * Switches the EmployeeView to creation mode, allowing the user to input employee information.
     */
    public void switchToCreationMode() {
        // Remove the components of the view mode.
        viewMode.removeAll();

        // Add the creation mode components and buttons.
        viewMode.add(createCreationModeComponents(), BorderLayout.CENTER);
        viewMode.add(createCreationModeButtons(), BorderLayout.SOUTH);

        // Revalidate and repaint the view mode panel.
        viewMode.revalidate();
        viewMode.repaint();

        // Check if there is an action listener registered before attempting to remove it.
        ActionListener[] actionListeners = filterButton.getActionListeners();
        if (actionListeners.length > 0) {
            filterButton.removeActionListener(actionListeners[0]);
        }
    }

    /**
     * Clears all action listeners from various buttons.
     */
    public void clearActionListeners() {
        // Clear action listeners from the "Back" button
        ActionListener[] backListeners = backButton.getActionListeners();
        for (ActionListener listener : backListeners) {
            backButton.removeActionListener(listener);
        }

        // Clear action listeners from the "Next" button
        ActionListener[] nextListeners = nextButton.getActionListeners();
        for (ActionListener listener : nextListeners) {
            nextButton.removeActionListener(listener);
        }

        // Clear action listeners from the "Delete" button
        ActionListener[] deleteListeners = deleteButton.getActionListeners();
        for (ActionListener listener : deleteListeners) {
            deleteButton.removeActionListener(listener);
        }

        // Clear action listeners from the "Create" button
        ActionListener[] createListeners = createButton.getActionListeners();
        for (ActionListener listener : createListeners) {
            createButton.removeActionListener(listener);
        }

        // Clear action listeners from the "Modify" button
        ActionListener[] modifyListeners = modifyButton.getActionListeners();
        for (ActionListener listener : modifyListeners) {
            modifyButton.removeActionListener(listener);
        }

        // Clear action listeners from the "Filter" button
        ActionListener[] filterListeners = filterButton.getActionListeners();
        for (ActionListener listener : filterListeners) {
            filterButton.removeActionListener(listener);
        }
    }

    /**
     * Displays the information of the currently selected employee in the view.
     * If a filter is applied, this method ensures that only employees matching the filter are displayed.
     */
    public void displayCurrentEmployee() {
        if (currentEmployeeNode != null) {
            // Get the current employee and related information if it isn't null.
            EmployeeModel employee = currentEmployeeNode.getMain();
            int currentPosition = employeeController.getPosition(employee) + 1;
            int totalEmployees = employeeController.getTotalElements();

            // Create HTML-formatted text to display information using the StrinbBuilder class to append consecutive strings.
            StringBuilder labelText = new StringBuilder("<html>");
            labelText.append("<h2 style='color: #007acc;'>Employee #").append(currentPosition).append(" of ").append(totalEmployees).append("</h2><br>");
            labelText.append("<font color='#008000'><b>Name:</b></font> ").append(employee.getName()).append("<br>");
            labelText.append("<font color='#008000'><b>Employee Number:</b></font> ").append(employee.getEmployeeNumber()).append("<br>");

            if (isFilterApplied) {
                // Check if hireDate is null or not in 2023, and skip employees who don't match the filter.
                if (employee.getHireDate() == null || employee.getHireDate().get(Calendar.YEAR) != 2023) {
                    // Find the next matching employee which isn't null, whose date isn't null and matches the date criteria.
                    ListController<EmployeeModel>.Node<EmployeeModel> nextEmployeeNode = currentEmployeeNode;
                    while (nextEmployeeNode != null && (nextEmployeeNode.getMain().getHireDate() == null
                            || nextEmployeeNode.getMain().getHireDate().get(Calendar.YEAR) != 2023)) {
                        nextEmployeeNode = nextEmployeeNode.getNextNode();
                    }

                    if (nextEmployeeNode != null) {
                        // If a matching employee is found, update the current employee.
                        currentEmployeeNode = nextEmployeeNode;
                        employee = currentEmployeeNode.getMain();
                        currentPosition = employeeController.getPosition(employee) + 1;
                    } else {
                        // If no matching employee is found, inform the user and stop further processing.
                        employeeInfoLabel.setText("No employees hired in 2023 to display.");
                        return;
                    }

                }
            }

            // Check if hireDate, phone, and email are not null or empty.
            if (employee.getHireDate() != null) {
                labelText.append("<font color='#008000'><b>Hire Date:</b></font> ").append(formatGregorianCalendar(employee.getHireDate())).append("<br>");
            } else {
                // Indicate when the hire date is not specified.
                labelText.append("<font color='red'><b>Hire Date:</b></font> Not specified yet<br>");
            }

            if (!employee.getPhoneNumber().isEmpty()) {
                // Display the phone number if available.
                labelText.append("<font color='green'><b>Phone:</b></font> ").append(employee.getPhoneNumber()).append("<br>");
            } else {
                // Indicate when the phone number is not specified.
                labelText.append("<font color='red'><b>Phone:</b></font> Not specified yet<br>");
            }

            if (!employee.getEmailAddress().isEmpty()) {
                // Display the email address if available.
                labelText.append("<font color='green'><b>Email:</b></font> ").append(employee.getEmailAddress()).append("<br>");
            } else {
                // Indicate when the email address is not specified.
                labelText.append("<font color='red'><b>Email:</b></font> Not specified yet<br>");
            }

            labelText.append("</html>");
            // Set the label text with the formatted information.
            employeeInfoLabel.setText(labelText.toString());
        } else {
            // Handle case where there's no current employee to display.
            employeeInfoLabel.setText("No employee to display.");
        }
    }

    /**
     * Creates buttons for the view mode and adds action listeners to them.
     *
     * @return A JPanel containing the view mode buttons.
     */
    public JPanel createViewModeButtons() {
        // Add an action listener for the "Next" button
        nextButton.addActionListener(e -> {
            if (currentEmployeeNode != null) {
                // Get the next employee node in the linked list.
                ListController<EmployeeModel>.Node<EmployeeModel> nextEmployeeNode = currentEmployeeNode.getNextNode();

                // Iterate through employees to find the next valid one.
                while (nextEmployeeNode != null) {
                    EmployeeModel nextEmployee = nextEmployeeNode.getMain();

                    // Check if the filter is not applied or if the hire date matches the filter.
                    if (!isFilterApplied || (nextEmployee.getHireDate() != null && nextEmployee.getHireDate().get(Calendar.YEAR) == 2023)) {
                        // Update the current employee node to the next one, display the new employee and update button states.
                        currentEmployeeNode = nextEmployeeNode;
                        displayCurrentEmployee();
                        updateButtonStates();
                        return; // Found a valid employee, exit the loop.
                    }
                    // Move the next employee node to the next node in the list.
                    nextEmployeeNode = nextEmployeeNode.getNextNode();
                }
            }
        });

        // Add an action listener for the "Back" button
        backButton.addActionListener(e -> {
            if (currentEmployeeNode != null) {
                // Get the previous employee node in the linked list.
                ListController<EmployeeModel>.Node<EmployeeModel> previousEmployeeNode = currentEmployeeNode.getPreviousNode();

                // Iterate through employees to find the previous valid one.
                while (previousEmployeeNode != null) {
                    EmployeeModel previousEmployee = previousEmployeeNode.getMain();

                    // Check if the filter is not applied or if the hire date matches the filter.
                    if (!isFilterApplied || (previousEmployee.getHireDate() != null && previousEmployee.getHireDate().get(Calendar.YEAR) == 2023)) {
                        // Update the current employee node, display the previous employee and update button states.
                        currentEmployeeNode = previousEmployeeNode;
                        displayCurrentEmployee();
                        updateButtonStates();
                        return; // Found a valid employee, exit the loop
                    }
                    // Move the previous employee node to the previous node in the list.
                    previousEmployeeNode = previousEmployeeNode.getPreviousNode();
                }
            }
        });

        // DEBUGGING PURPOSES.
        System.out.println("Adding ActionListener to deleteButton");


        // Add an action listener for the "Delete" button.
        deleteButton.addActionListener(e -> {
            if (currentEmployeeNode != null) {
                // Prompt the user for confirmation before deleting.
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this employee?", "Confirmation", JOptionPane.YES_NO_OPTION);

                // User chose to delete the current employee.
                if (option == JOptionPane.YES_OPTION) {
                    // Get the current employee.
                    EmployeeModel currentEmployee = currentEmployeeNode.getMain();

                    // Call the link controller method to remove the current employee.
                    employeeController.remove(currentEmployee);

                    // Retrieve the next employee after deletion.
                    currentEmployeeNode = currentEmployeeNode.getNextNode();

                    // Update the view to display the next employee (if available).
                    displayCurrentEmployee();

                    // Update button states.
                    updateButtonStates();

                    // Shows a dialog to inform the user that there are no more employees to display after deletion.
                    if (currentEmployeeNode == null) {
                        JOptionPane.showMessageDialog(frame, "No more employees to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else {
                // Handle the case where there's no current employee to delete by showing an info dialog.
                JOptionPane.showMessageDialog(frame, "No employee to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add an action listener for the "Create" button.
        createButton.addActionListener(e -> {
            currentMode = EmployeeView.Mode.CREATE; // Set the mode to Create.
            switchToCreationMode(); // Builds Create mode.
        });

        // Add an action listener for the "Modify" button.
        modifyButton.addActionListener(e -> {
            if (currentEmployeeNode != null) {
                currentMode = EmployeeView.Mode.MODIFY; // Set the mode to Modify.

                // Get the currently selected employee's data.
                EmployeeModel selectedEmployee = currentEmployeeNode.getMain();

                // Populate the input fields with the selected employee's data (name and identifier).
                nameField.setText(selectedEmployee.getName());
                employeeNumberField.setText(String.valueOf(selectedEmployee.getEmployeeNumber()));

                // Set the text color of the fields to black to avoid the gray placeholder effect.
                nameField.setForeground(Color.BLACK);
                employeeNumberField.setForeground(Color.BLACK);

                // Check if hireDate is null and provide a placeholder.
                GregorianCalendar hireDate = selectedEmployee.getHireDate();
                if (hireDate != null) {
                    // Establish a string format to convert the hire date to.
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate = dateFormat.format(hireDate.getTime());
                    // Populate the hireDate, phone, and email fields with the selected employee's data.
                    hireDateField.setText(formattedDate);
                    phoneField.setText(selectedEmployee.getPhoneNumber());
                    emailField.setText(selectedEmployee.getEmailAddress());
                    // Set the text color of the fields to black to avoid the gray placeholder effect.
                    hireDateField.setForeground(Color.BLACK);
                    phoneField.setForeground(Color.BLACK);
                    emailField.setForeground(Color.BLACK);
                } else {
                    hireDateField.setText(placeHolderDate); // Set a placeholder in case it's null.
                }

                switchToCreationMode(); // Switch the view mode to creation for modification
            } else {
                // Handle the case where there's no current employee to modify by showing an info dialog.
                JOptionPane.showMessageDialog(frame, "No employee to modify.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add an action listener for the "Filter" button
        filterButton.addActionListener(e -> {
            toggleFilterButton(); // Toggles the filter state.
            displayCurrentEmployee(); // Updates the displayed employee information.
            updateButtonStates(); // Updates the button states based on the current view.
        });

        // Disable the back button if there's no previous employee.
        backButton.setEnabled(currentEmployeeNode != null && currentEmployeeNode.getPreviousNode() != null);

        // Disable the next button if there's no next employee.
        nextButton.setEnabled(currentEmployeeNode != null && currentEmployeeNode.getNextNode() != null);

        // Create a panel to hold the view mode buttons and add them to it.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(createButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(filterButton);

        viewMode.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the view mode.

        return buttonPanel; // Return the created button panel.
    }

    /**
     * Creates a panel containing components for the creation mode.
     *
     * @return A JPanel containing input fields and labels for creating or modifying an employee's data.
     */
    public JPanel createCreationModeComponents() {
        // Create a panel to hold the components for creation mode.
        JPanel creationComponents = new JPanel(new GridLayout(5, 2));

        // Create and configure JLabels and input fields for employee data entry.

        // Name label and field. Colors an asterisk in the label to indicate that it's an obligatory field.
        JLabel nameLabel = new JLabel("<html><div style='font-size: 12px; color: black;'>Name <span style='color: red;'>*</span></div></html>");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the label within its cell
        creationComponents.add(nameLabel);
        creationComponents.add(nameField);

        // Employee number label and field. Colors an asterisk in the label to indicate that it's an obligatory field.
        JLabel employeeNumberLabel = new JLabel("<html><div style='font-size: 12px; color: black;'>Employee Number <span style='color: red;'>*</span></div></html>");
        employeeNumberLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the label within its cell
        creationComponents.add(employeeNumberLabel);
        creationComponents.add(employeeNumberField);

        // Hire date label and field.
        JLabel hireDateLabel = new JLabel("<html><div style='font-size: 12px; color: black;'>Hire Date</div></html>");
        hireDateLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the label within its cell
        creationComponents.add(hireDateLabel);
        creationComponents.add(hireDateField);

        // Phone label and field.
        JLabel phoneLabel = new JLabel("<html><div style='font-size: 12px; color: black;'>Phone</div></html>");
        phoneLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the label within its cell
        creationComponents.add(phoneLabel);
        creationComponents.add(phoneField);

        // Email label and field.
        JLabel emailLabel = new JLabel("<html><div style='font-size: 12px; color: black;'>E-mail</div></html>");
        emailLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the label within its cell
        creationComponents.add(emailLabel);
        creationComponents.add(emailField);

        return creationComponents; // Return the complete creation mode panel.
    }

    /**
     * Creates buttons for the creation mode and adds action listeners to them.
     *
     * @return A JPanel containing buttons for accepting or canceling the creation or modification of an employee's data.
     */
    public JPanel createCreationModeButtons() {
        // Create buttons for the creation mode.
        JButton acceptButton = new JButton("Accept");
        JButton cancelButton = new JButton("Cancel");

        // Add action listeners for creation mode buttons.

        // "Accept" button action listener.
        acceptButton.addActionListener(e -> acceptButtonAction());

        // "Cancel" button action listener.
        cancelButton.addActionListener(e -> switchToViewMode());

        // Create a panel to hold the creation mode buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);

        return buttonPanel; // Returns the complete button panel.
    }


    /**
     * Updates the state (enabled or disabled) of various buttons in the view mode based on the current conditions.
     */
    public void updateButtonStates() {
        // Disable the back button if there's no previous employee matching the filter.
        backButton.setEnabled(currentEmployeeNode != null && hasPreviousMatchingEmployee(currentEmployeeNode));

        // Disable the next button if there's no previous employee matching the filter.
        nextButton.setEnabled(currentEmployeeNode != null && hasNextMatchingEmployee(currentEmployeeNode));

        // Disable the delete button if there's no current employee.
        deleteButton.setEnabled(currentEmployeeNode != null);

        // Disable the modify button if there's no current employee.
        modifyButton.setEnabled(currentEmployeeNode != null);
    }

    /**
     * Formats a given GregorianCalendar object into a string with the "dd/MM/yyyy" format.
     *
     * @param calendar The GregorianCalendar object to be formatted.
     * @return A formatted string representation of the calendar in the "dd/MM/yyyy" format.
     */
    public String formatGregorianCalendar(GregorianCalendar calendar) {
        SimpleDateFormat simplifiedDate = new SimpleDateFormat("dd/MM/yyyy");
        return simplifiedDate.format(calendar.getTime());
    }


    /**
     * Toggles the filter button to apply or undo a filter on employee data.
     * When the filter is applied, it updates the button text and handles filter application.
     * When the filter is undone, it updates the button text and handles removing the filter.
     */
    public void toggleFilterButton() {
        isFilterApplied = !isFilterApplied;
        filterButton.setText(isFilterApplied ? "Undo Filter" : "Apply Filter");
    }


    /**
     * Handles the action when the "Accept" button is clicked during the creation or modification of an employee.
     * Validates input data, creates or modifies an employee, and switches back to view mode.
     */
    public void acceptButtonAction() {
        try {
            // Parse the input data from the text fields by removing the whitespace from both ends.
            int employeeNumber = 0;
            String name = nameField.getText().trim();
            String hireDate = hireDateField.getText().trim();
            String phoneNumber = phoneField.getText().trim();
            String emailAddress = emailField.getText().trim();

            // Verify that name and employee number are not left blank or contain placeholder values. Inform the user otherwise.
            if (name.isEmpty() || employeeNumberField.getText().isEmpty() || name.equals(placeHolderName)) {
                JOptionPane.showMessageDialog(frame, "Name and employee number cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verify that the name doesn't contain any numbers. Inform the user otherwise.
            if (name.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(frame, "Name cannot contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse the employee number if it's not empty.
            if (!employeeNumberField.getText().isEmpty()) {
                try {
                    employeeNumber = Integer.parseInt(employeeNumberField.getText());
                } catch (NumberFormatException e) {
                    // Inform the user if the parsing has encountered an unvalid integer.
                    JOptionPane.showMessageDialog(frame, "Employee number must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Check if the employee number is already in use.
            if (employeeNumberExists(employeeNumber, currentEmployee)) {
                JOptionPane.showMessageDialog(frame, "An employee with the same employee number already exists. Please choose a different number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if hireDate is in the correct format.
            if (!hireDate.isEmpty() && !hireDate.equals(placeHolderDate)) {
                if (!isValidDateFormat(hireDate)) {
                    JOptionPane.showMessageDialog(frame, "Invalid date format (DD/MM/YYYY).", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Check if phoneNumber is in the correct format.
            if (!phoneNumber.isEmpty() && !phoneNumber.equals(placeHolderPhone)) {
                if (!isValidPhoneNumber(phoneNumber)) {
                    JOptionPane.showMessageDialog(frame, "Invalid phone number format (9 digits).", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Check if emailAddress is in the correct format.
            if (!emailAddress.isEmpty() && !emailAddress.equals(placeHolderEmail)) {
                if (!isValidEmailAddress(emailAddress)) {
                    JOptionPane.showMessageDialog(frame, "Invalid email address format (example@mail.com).", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Check if other fields are empty or contain placeholder values.
            boolean anyFieldsEmpty = (hireDate.isEmpty() || hireDate.equals(placeHolderDate)) ||
                    (phoneNumber.isEmpty() || phoneNumber.equals(placeHolderPhone)) ||
                    (emailAddress.isEmpty() || emailAddress.equals(placeHolderEmail));

            // In case that any of them are empty.
            if (anyFieldsEmpty) {
                // Presents the user with the possibility to only input employee name and number.
                int option = JOptionPane.showConfirmDialog(frame,
                        "You have the option to provide more information (hire date, phone, email). Do you want to proceed with just name and employee number?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.NO_OPTION) {
                    // User chose not to proceed with only name and employee number. Creation/modification continues.
                    return;
                }
            }

            // If on Create mode.
            if (currentMode == EmployeeView.Mode.CREATE) {
                // Create a new EmployeeModel object with the provided input.
                EmployeeModel newEmployee = new EmployeeModel(name, employeeNumber);

                if (!anyFieldsEmpty) {
                    // If other fields are not empty, use the full constructor.
                    newEmployee = new EmployeeModel(name, employeeNumber, hireDate, phoneNumber, emailAddress);
                }

                // Add the new employee to the controller's list.
                employeeController.add(newEmployee);

                // Set the current employee node to the newly added employee node.
                if (currentEmployeeNode == null) {
                    currentEmployeeNode = employeeController.getFirstNode();
                }

                // Informs the user that the creation has been successful via dialog.
                JOptionPane.showMessageDialog(frame, "Employee added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // If on Modify mode.
            } else if (currentMode == EmployeeView.Mode.MODIFY) {
                // Modify the existing employee with the provided input.
                if (currentEmployeeNode != null) {
                    EmployeeModel selectedEmployee = currentEmployeeNode.getMain();
                    String modifiedName = nameField.getText().trim();
                    int modifiedEmployeeNumber = Integer.parseInt(employeeNumberField.getText());
                    String modifiedHireDate = hireDateField.getText().trim();
                    String modifiedPhone = phoneField.getText().trim();
                    String modifiedEmail = emailField.getText().trim();

                    // Update the current employee with the modified data.
                    if (anyFieldsEmpty) {
                        selectedEmployee.setName(modifiedName);
                        selectedEmployee.setEmployeeNumber(modifiedEmployeeNumber);
                    } else {
                        selectedEmployee.setName(modifiedName);
                        selectedEmployee.setEmployeeNumber(modifiedEmployeeNumber);
                        selectedEmployee.setHireDate(selectedEmployee.createGregorianCalendar(modifiedHireDate));
                        selectedEmployee.setPhoneNumber(modifiedPhone);
                        selectedEmployee.setEmailAddress(modifiedEmail);
                    }

                    // Set the modified employee as the current employee.
                    currentEmployee = selectedEmployee;

                    // Informs the user that the modification has been successful via dialog.
                    JOptionPane.showMessageDialog(frame, "Employee modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            // Switch back to the view mode
            switchToViewMode();

            // Reset the input fields with their respective placeholders.
            resetFields();
        } catch (IllegalArgumentException e) {
            // Handle any other unexpected validation errors.
            JOptionPane.showMessageDialog(frame, "Invalid input. Please check the data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks if the provided employee number already exists in the list, excluding the specified employee.
     *
     * @param employeeNumber The employee number to check.
     * @param excludedEmployee The employee to exclude from the check.
     * @return True if the employee number already exists; false otherwise.
     */
    private boolean employeeNumberExists(int employeeNumber, EmployeeModel excludedEmployee) {
        // Iterate through the employees and check if the provided employee number exists, excluding the specified employee.
        for (ListController<EmployeeModel>.Node<EmployeeModel> node = employeeController.getFirstNode(); node != null; node = node.getNextNode()) {
            EmployeeModel employee = node.getMain();
            if (employee.getEmployeeNumber() == employeeNumber && employee != excludedEmployee) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a date string is in the valid format (DD/MM/YYYY).
     *
     * @param date The date string to validate.
     * @return True if the date is in the correct format; false otherwise.
     */
    private boolean isValidDateFormat(String date) {
        return date.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$");
    }

    /**
     * Checks if a phone number string is in the valid format (9 digits).
     *
     * @param phoneNumber The phone number string to validate.
     * @return True if the phone number is in the correct format; false otherwise.
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^[0-9]{9}$");
    }

    /**
     * Checks if an email address string is in the valid format.
     *
     * @param emailAddress The email address string to validate.
     * @return True if the email address is in the correct format; false otherwise.
     */
    private boolean isValidEmailAddress(String emailAddress) {
        return emailAddress.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Checks if there is a previous employee matching the filter applied in the list.
     *
     * @param node The current node in the list.
     * @return True if there is a previous matching employee; otherwise, false.
     */
    public boolean hasPreviousMatchingEmployee(ListController<EmployeeModel>.Node<EmployeeModel> node) {
        if (node == null) {
            return false; // No matching employee since there's no current node
        }

        ListController<EmployeeModel>.Node<EmployeeModel> previousNode = node.getPreviousNode();

        // Iterate through previous employees to find the previous valid one
        while (previousNode != null) {
            EmployeeModel previousEmployee = previousNode.getMain();

            // Check if a filter is applied or if the hire date matches the filter
            if (!isFilterApplied || (previousEmployee.getHireDate() != null && previousEmployee.getHireDate().get(Calendar.YEAR) == 2023)) {
                return true; // Found a previous valid employee
            }

            previousNode = previousNode.getPreviousNode();
        }

        return false; // No previous matching employee found
    }

    /**
     * Checks if there is a next employee matching the filter applied in the list.
     *
     * @param node The current node in the list.
     * @return True if there is a next matching employee; otherwise, false.
     */
    public boolean hasNextMatchingEmployee(ListController<EmployeeModel>.Node<EmployeeModel> node) {
        if (node == null) {
            return false; // No matching employee since there's no current node.
        }

        ListController<EmployeeModel>.Node<EmployeeModel> nextNode = node.getNextNode();

        // Iterate through next employees to find the next valid one.
        while (nextNode != null) {
            EmployeeModel nextEmployee = nextNode.getMain();

            // Check if a filter is applied or if the hire date matches the filter.
            if (!isFilterApplied || (nextEmployee.getHireDate() != null && nextEmployee.getHireDate().get(Calendar.YEAR) == 2023)) {
                return true; // Found a next valid employee.
            }

            nextNode = nextNode.getNextNode();
        }

        return false; // No next matching employee found.
    }

    /**
     * Resets the input fields by reapplying the placeholders to the text fields.
     */
    public void resetFields() {
        addPlaceholder(nameField, placeHolderName);
        addPlaceholder(employeeNumberField, placeHolderNumber);
        addPlaceholder(hireDateField, placeHolderDate);
        addPlaceholder(phoneField, placeHolderPhone);
        addPlaceholder(emailField, placeHolderEmail);
    }

    /**
     * Adds a placeholder to a JTextField by setting its initial text and text color.
     *
     * @param field The JTextField to which the placeholder is added.
     * @param placeholder The text to be used as a placeholder.
     */
    public void addPlaceholder(JTextField field, String placeholder) {
        // Set the initial text and text color for the placeholder.
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        // Add a FocusListener to handle placeholder behavior.
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // When the field gains focus, check if its text is the placeholder.
                if (field.getText().equals(placeholder)) {
                    // Clear the placeholder text and set the text color to black.
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // When the field loses focus, check if it's empty.
                if (field.getText().isEmpty()) {
                    // Restore the placeholder text and set the text color to gray.
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }


    /**
     * Creates a limited JTextField with the specified number of columns, a placeholder, and a character limit.
     *
     * @param columns The number of columns to display in the text field.
     * @param placeholder The text to be used as a placeholder.
     * @param maxChars The maximum number of characters allowed in the text field.
     * @return The configured JTextField.
     */
    public JTextField createLimitedTextField(int columns, String placeholder, int maxChars) {
        // Create a JTextField with the specified number of columns.
        JTextField textField = new JTextField(columns);

        // Add a placeholder to the text field.
        addPlaceholder(textField, placeholder);

        // Set the character limit for the text field.
        setCharacterLimit(textField, maxChars);

        return textField; // Return the limited text field.
    }

    /**
     * Sets a character limit for the provided JTextField by applying a DocumentFilter.
     *
     * @param textField The JTextField for which to set the character limit.
     * @param limit The maximum number of characters allowed in the text field.
     */
    public void setCharacterLimit(JTextField textField, int limit) {
        // Get the document associated with the text field.
        Document document = textField.getDocument();

        if (document instanceof PlainDocument) {
            // Cast the document to PlainDocument.
            PlainDocument plainDocument = (PlainDocument) document;

            // Set a custom DocumentFilter to limit character input.
            plainDocument.setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    // Check if inserting the new text stays within the character limit.
                    if ((fb.getDocument().getLength() + string.length()) <= limit) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    // Check if replacing text stays within the character limit.
                    if ((fb.getDocument().getLength() + text.length() - length) <= limit) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            });
        }
    }

    /**
     * Sets all the attributes of this class by obtaining references from an already provided view.
     */
    public void setAllAttributes() {
        this.employeeController = view.getEmployeeController();
        this.currentEmployee = view.getCurrentEmployee();
        this.employeeInfoLabel = view.getEmployeeInfoLabel();
        this.currentEmployeeNode = view.getCurrentEmployeeNode();
        this.nameField = view.getNameField();
        this.employeeNumberField = view.getEmployeeNumberField();
        this.hireDateField = view.getHireDateField();
        this.phoneField = view.getPhoneField();
        this.emailField = view.getEmailField();
        this.backButton = view.getBackButton();
        this.nextButton = view.getNextButton();
        this.deleteButton = view.getDeleteButton();
        this.createButton = view.getCreateButton();
        this.modifyButton = view.getModifyButton();
        this.filterButton = view.getFilterButton();
        this.isFilterApplied = view.isFilterApplied();
        this.viewMode = view.getViewMode();
        this.creationMode = view.getCreationMode();
        this.viewScrollPane = view.getViewScrollPane();
        this.currentMode = view.getCurrentMode();
        this.frame = view.getFrame();
    }




}
