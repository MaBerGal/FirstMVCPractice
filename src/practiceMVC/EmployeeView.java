package practiceMVC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;

// Placeholder funcionality
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// Max character limit functionality
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;


public class EmployeeView {
    private EmployeeController employeeController;
    private EmployeeModel currentEmployee;

    private JLabel employeeInfoLabel = new JLabel();
    private EmployeeController<EmployeeModel>.Node<EmployeeModel> currentEmployeeNode;

    private enum Mode {
        CREATE,
        MODIFY
    }

    private Mode currentMode;


    private JTextField nameField, employeeNumberField, hireDateField, phoneField, emailField;
    String placeHolderName = "Enter name here";
    String placeHolderNumber = "#####";
    String placeHolderDate = "DD/MM/YYYY";
    String placeHolderPhone = "9 digits";
    String placeHolderEmail = "example@mail.com";
    private JButton backButton, nextButton, deleteButton, createButton, modifyButton, filterButton;

    private boolean isFilterApplied = false;

    private JPanel viewMode = new JPanel(new BorderLayout());
    private JPanel creationMode;
    private JScrollPane viewScrollPane;
    private JFrame frame;

    public EmployeeView(EmployeeController controller) {
        this.employeeController = controller;
        currentEmployee = null;

        currentEmployeeNode = employeeController.getFirstNode();


        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Employee Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // Create GUI components

        nameField = createLimitedTextField(20, placeHolderName, 50);
        employeeNumberField = createLimitedTextField(20, placeHolderNumber, 5);
        hireDateField = createLimitedTextField(20, placeHolderDate, 10);
        phoneField = createLimitedTextField(20, placeHolderPhone, 9);
        emailField = createLimitedTextField(20, placeHolderEmail, 30);

        employeeInfoLabel.setHorizontalAlignment(JLabel.CENTER);
        employeeInfoLabel.setVerticalAlignment(JLabel.CENTER);

        viewMode = new JPanel(new BorderLayout());
        creationMode = new JPanel(new BorderLayout());

        // Create buttons for view mode
        backButton = new JButton("Back");
        nextButton = new JButton("Next");
        deleteButton = new JButton("Delete");
        createButton = new JButton("Create");
        modifyButton = new JButton("Modify");
        filterButton = new JButton("Apply Filter");

        // Add buttons to view mode panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(createButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(filterButton);

        viewMode.add(buttonPanel, BorderLayout.SOUTH);
        viewMode.add(employeeInfoLabel, BorderLayout.CENTER);

        updateButtonStates();

        // Create a scroll pane for the view mode
        viewScrollPane = new JScrollPane(viewMode);
        frame.add(viewScrollPane);

        // Initially, start in view mode
        switchToViewMode();

        frame.setVisible(true);
    }

    private void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private JTextField createLimitedTextField(int columns, String placeholder, int maxChars) {
        JTextField textField = new JTextField(columns);
        addPlaceholder(textField, placeholder);
        setCharacterLimit(textField, maxChars);
        return textField;
    }

    private void setCharacterLimit(JTextField textField, int limit) {
        Document document = textField.getDocument();
        if (document instanceof PlainDocument) {
            PlainDocument plainDocument = (PlainDocument) document;
            plainDocument.setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if ((fb.getDocument().getLength() + string.length()) <= limit) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if ((fb.getDocument().getLength() + text.length() - length) <= limit) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            });
        }
    }

    private void switchToViewMode() {
        // Remove the components of the view mode
        viewMode.removeAll();

        // Clear existing action listeners
        clearActionListeners();

        // Display the current employee's information
        displayCurrentEmployee();

        // Add the view mode buttons
        JPanel viewModeButtons = createViewModeButtons();
        viewMode.add(viewModeButtons, BorderLayout.SOUTH);
        viewMode.add(employeeInfoLabel, BorderLayout.CENTER);

        // Update button states
        updateButtonStates();

        // Revalidate and repaint the view mode panel
        viewMode.revalidate();
        viewMode.repaint();

        // Debugging: Print current employee information and node details
        if (currentEmployeeNode != null) {
            System.out.println("Current Employee: " + currentEmployeeNode.getMain());
            System.out.println("Current Employee Node: " + currentEmployeeNode);
        } else {
            System.out.println("Current Employee: null");
        }

        // Check if currentEmployeeNode is null, and if so, reset it to the first employee
        if (currentEmployeeNode == null) {
            currentEmployeeNode = employeeController.getFirstNode();
            System.out.println("Resetting currentEmployeeNode to the first employee.");
        }
    }



    private void switchToCreationMode() {
        // Remove the components of the view mode
        viewMode.removeAll();

        // Add the creation mode components and buttons
        viewMode.add(createCreationModeComponents(), BorderLayout.CENTER);
        viewMode.add(createCreationModeButtons(), BorderLayout.SOUTH);

        // Revalidate and repaint the view mode panel
        viewMode.revalidate();
        viewMode.repaint();

        // Check if there is an action listener registered before attempting to remove it
        ActionListener[] actionListeners = filterButton.getActionListeners();
        if (actionListeners.length > 0) {
            filterButton.removeActionListener(actionListeners[0]);
        }
    }

    private JPanel createViewModeButtons() {
        // Add action listeners for view mode buttons
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentEmployeeNode != null && currentEmployeeNode.getNextNode() != null) {
                    System.out.println("Switching to next employee");
                    currentEmployeeNode = currentEmployeeNode.getNextNode();
                    displayCurrentEmployee();
                    updateButtonStates(); // Update button states
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentEmployeeNode != null && currentEmployeeNode.getPreviousNode() != null) {
                    System.out.println("Switching to previous employee");
                    currentEmployeeNode = currentEmployeeNode.getPreviousNode();
                    displayCurrentEmployee();
                    updateButtonStates(); // Update button states
                }
            }
        });

        System.out.println("Adding ActionListener to deleteButton");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentEmployeeNode != null) {
                    // Prompt the user for confirmation before deleting
                    int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this employee?", "Confirmation", JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        // Get the current employee
                        EmployeeModel currentEmployee = currentEmployeeNode.getMain();

                        // Call your EmployeeController's method to remove the current employee
                        employeeController.remove(currentEmployee);

                        // Try to get the next employee after deletion
                        currentEmployeeNode = currentEmployeeNode.getNextNode();

                        // Update the view to display the next employee (if available)
                        displayCurrentEmployee();

                        // Update button states
                        updateButtonStates();

                        // Optionally, you can show a message if there are no more employees
                        if (currentEmployeeNode == null) {
                            JOptionPane.showMessageDialog(frame, "No more employees to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    // Handle case where there's no current employee to delete
                    JOptionPane.showMessageDialog(frame, "No employee to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });



        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentMode = Mode.CREATE; // Set the mode to Create
                switchToCreationMode();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentEmployeeNode != null) {
                    currentMode = Mode.MODIFY; // Set the mode to Modify

                    // Get the currently selected employee's data
                    EmployeeModel selectedEmployee = currentEmployeeNode.getMain();

                    // Populate the input fields with the selected employee's data
                    nameField.setText(selectedEmployee.getName());
                    employeeNumberField.setText(String.valueOf(selectedEmployee.getEmployeeNumber()));

                    // Check if hireDate is null and provide a placeholder or an empty string
                    GregorianCalendar hireDate = selectedEmployee.getHireDate();
                    if (hireDate != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String formattedDate = dateFormat.format(hireDate.getTime());
                        hireDateField.setText(formattedDate);
                        phoneField.setText(selectedEmployee.getPhoneNumber());
                        emailField.setText(selectedEmployee.getEmailAddress());
                    } else {
                        hireDateField.setText(placeHolderDate); // Set a placeholder or empty string
                    }


                    switchToCreationMode();
                } else {
                    JOptionPane.showMessageDialog(frame, "No employee to modify.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });



        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFilterButton();

                if (isFilterApplied) {
                    // Apply the filter to show only employees hired in 2023
                    employeeController.filterByHireYear(2023);
                } else {
                    // Undo the filter to show the full list
                    employeeController.showFullEmployeeList();
                }
                // Refresh the current employee node
                currentEmployeeNode = employeeController.getFirstNode();
                displayCurrentEmployee();
                updateButtonStates();
            }
        });



        // Disable the back button if there's no previous employee
        backButton.setEnabled(currentEmployeeNode != null && currentEmployeeNode.getPreviousNode() != null);

        // Disable the next button if there's no next employee
        nextButton.setEnabled(currentEmployeeNode != null && currentEmployeeNode.getNextNode() != null);


        // Add buttons to view mode panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(createButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(filterButton);

        viewMode.add(buttonPanel, BorderLayout.SOUTH);

        return buttonPanel;

    }

    private void updateButtonStates() {
        // Disable the back button if there's no previous employee
        backButton.setEnabled(currentEmployeeNode != null && currentEmployeeNode.getPreviousNode() != null);

        // Disable the next button if there's no next employee
        nextButton.setEnabled(currentEmployeeNode != null && currentEmployeeNode.getNextNode() != null);

        // Disable the delete button if there's no current employee
        deleteButton.setEnabled(currentEmployeeNode != null);
    }


    private void toggleFilterButton() {
        isFilterApplied = !isFilterApplied;
        filterButton.setText(isFilterApplied ? "Undo Filter" : "Apply Filter");
        if (isFilterApplied) {
            // Handle applying the filter
        } else {
            // Handle undoing the filter
        }
    }

    private void showFullEmployeeList() {
        // Switch back to the full employee list
        switchToViewMode();
    }



    private JPanel createCreationModeComponents() {
        // Create components for creation mode
        JPanel creationComponents = new JPanel(new GridLayout(5, 2));
        creationComponents.add(new JLabel("<html>Name <b><font color='red'>*</font></b></html>"));
        creationComponents.add(nameField);
        creationComponents.add(new JLabel("<html>Employee Number <b><font color='red'>*</font></b></html>"));
        creationComponents.add(employeeNumberField);
        creationComponents.add(new JLabel("Hire Date"));
        creationComponents.add(hireDateField);
        creationComponents.add(new JLabel("Phone"));
        creationComponents.add(phoneField);
        creationComponents.add(new JLabel("Email"));
        creationComponents.add(emailField);

        return creationComponents;
    }

    private JPanel createCreationModeButtons() {
        // Create buttons for creation mode
        JButton acceptButton = new JButton("Accept");
        JButton cancelButton = new JButton("Cancel");

        // Add action listeners for creation mode buttons
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptButtonAction();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToViewMode();
            }
        });

        // Add buttons to creation mode panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(acceptButton);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    private void acceptButtonAction() {
        try {
            // Parse the input data from the text fields
            int employeeNumber = 0;
            String name = nameField.getText().trim();
            String hireDate = hireDateField.getText().trim();
            String phoneNumber = phoneField.getText().trim();
            String emailAddress = emailField.getText().trim();

            // Check if name and employee number are not left blank
            if (name.isEmpty() || employeeNumberField.getText().isEmpty() || name.equals(placeHolderName)) {
                JOptionPane.showMessageDialog(frame, "Name and employee number cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if name contains any numbers
            if (name.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(frame, "Name cannot contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse the employee number if it's not empty
            if (!employeeNumberField.getText().isEmpty()) {
                try {
                    employeeNumber = Integer.parseInt(employeeNumberField.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "Employee number must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Check if other fields are empty or contain placeholder values
            boolean anyFieldsEmpty = (hireDate.isEmpty() || hireDate.equals(placeHolderDate)) ||
                    (phoneNumber.isEmpty() || phoneNumber.equals(placeHolderPhone)) ||
                    (emailAddress.isEmpty() || emailAddress.equals(placeHolderEmail));

            if (anyFieldsEmpty) {
                int option = JOptionPane.showConfirmDialog(frame,
                        "You have the option to provide more information (hire date, phone, email). Do you want to proceed with just name and employee number?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.NO_OPTION) {
                    // User chose not to proceed with only name and employee number
                    return;
                }
            }

            if (currentMode == Mode.CREATE) {
                // Create a new EmployeeModel object with the provided input
                EmployeeModel newEmployee = new EmployeeModel(name, employeeNumber);

                if (!anyFieldsEmpty) {
                    // If other fields are not empty, use the full constructor
                    newEmployee = new EmployeeModel(name, employeeNumber, hireDate, phoneNumber, emailAddress);
                }

                // Add the new employee to the controller's list
                employeeController.add(newEmployee);

                // Set the current employee node to the newly added employee node
                if (currentEmployeeNode == null) {
                    currentEmployeeNode = employeeController.getFirstNode();
                }

                JOptionPane.showMessageDialog(frame, "Employee added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (currentMode == Mode.MODIFY) {
                // Modify the existing employee with the provided input
                if (currentEmployeeNode != null) {
                    EmployeeModel selectedEmployee = currentEmployeeNode.getMain();
                    String modifiedName = nameField.getText().trim();
                    int modifiedEmployeeNumber = Integer.parseInt(employeeNumberField.getText());
                    String modifiedHireDate = hireDateField.getText().trim();
                    String modifiedPhone = phoneField.getText().trim();
                    String modifiedEmail = emailField.getText().trim();

                    // Update the current employee with modified data
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

                    currentEmployee = selectedEmployee; // Set the modified employee as the current employee

                    JOptionPane.showMessageDialog(frame, "Employee modified successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            // Switch back to the view mode
            switchToViewMode();

            // Optionally, you can clear the input fields
            clearFields();
        } catch (IllegalArgumentException e) {
            // Handle any other unexpected validation errors
            JOptionPane.showMessageDialog(frame, "Invalid input. Please check the data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        // Reapply the placeholders to the text fields
        addPlaceholder(nameField, placeHolderName);
        addPlaceholder(employeeNumberField, placeHolderNumber);
        addPlaceholder(hireDateField, placeHolderDate);
        addPlaceholder(phoneField, placeHolderPhone);
        addPlaceholder(emailField, placeHolderEmail);
    }

    private void clearActionListeners() {
        ActionListener[] backListeners = backButton.getActionListeners();
        for (ActionListener listener : backListeners) {
            backButton.removeActionListener(listener);
        }

        ActionListener[] nextListeners = nextButton.getActionListeners();
        for (ActionListener listener : nextListeners) {
            nextButton.removeActionListener(listener);
        }

        ActionListener[] deleteListeners = deleteButton.getActionListeners();
        for (ActionListener listener : deleteListeners) {
            deleteButton.removeActionListener(listener);
        }

        ActionListener[] createListeners = createButton.getActionListeners();
        for (ActionListener listener : createListeners) {
            createButton.removeActionListener(listener);
        }

        ActionListener[] modifyListeners = modifyButton.getActionListeners();
        for (ActionListener listener : modifyListeners) {
            modifyButton.removeActionListener(listener);
        }

        ActionListener[] filterListeners = filterButton.getActionListeners();
        for (ActionListener listener : filterListeners) {
            filterButton.removeActionListener(listener);
        }
    }


    private void displayCurrentEmployee() {
        if (currentEmployeeNode != null) {
            EmployeeModel employee = currentEmployeeNode.getMain();

            // Get the current position and total employees from the controller
            int currentPosition = employeeController.getPosition(employee)+1;
            int totalEmployees = employeeController.getTotalElements();

            // Display the position and total employees
            StringBuilder labelText = new StringBuilder("<html>");
            labelText.append("<h2>Employee #").append(currentPosition).append(" of ").append(totalEmployees).append("</h2><br>");
            labelText.append("Name: ").append(employee.getName()).append("<br>");
            labelText.append("Employee Number: ").append(employee.getEmployeeNumber()).append("<br>");
            labelText.append("Hire Date: ").append(employee.getHireDate()).append("<br>");
            labelText.append("Phone: ").append(employee.getPhoneNumber()).append("<br>");
            labelText.append("Email: ").append(employee.getEmailAddress()).append("<br>");
            labelText.append("</html>");
            employeeInfoLabel.setText(labelText.toString());
        } else {
            // Handle case where there's no current employee
            employeeInfoLabel.setText("No employee to display.");
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EmployeeController<EmployeeModel> controller = new EmployeeController<>(); // Specify the type parameter
                new EmployeeView(controller);
            }
        });
    }
}
