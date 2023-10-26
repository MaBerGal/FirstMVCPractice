package practiceMVC;

// For Swing and native system GUI functionality.
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
// In order to find and set an icon image.
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class that represents the user interface view for employee management.
 */
public class EmployeeView {
    /**
     * Controller for managing employee data.
     */
    private ListController employeeController;

    /**
     * Controller for view-related functionality.
     */
    private ViewController viewController;

    /**
     * Represents the currently selected employee.
     */
    private EmployeeModel currentEmployee;

    /**
     * Label for displaying employee information.
     */
    private JLabel employeeInfoLabel = new JLabel();

    /**
     * Node representing the current employee in the controller's linked list.
     */
    private ListController<EmployeeModel>.Node<EmployeeModel> currentEmployeeNode;

    /**
     * Enumeration representing the two modes of the employee data input view.
     */
    public enum Mode {
        CREATE,
        MODIFY
    }

    /**
     * The current mode of the view.
     */
    private Mode currentMode;

    /**
     * Text fields for entering employee information.
     */
    private JTextField nameField, employeeNumberField, hireDateField, phoneField, emailField;

    /**
     * Placeholder text for name input.
     */
    private String placeHolderName = "Enter name here";

    /**
     * Placeholder text for employee number input.
     */
    private String placeHolderNumber = "#####";

    /**
     * Placeholder text for hire date input.
     */
    private String placeHolderDate = "DD/MM/YYYY";

    /**
     * Placeholder text for phone number input.
     */
    private String placeHolderPhone = "9 digits";

    /**
     * Placeholder text for email address input.
     */
    private String placeHolderEmail = "example@mail.com";

    /**
     * Buttons for user interactions.
     */
    private JButton backButton, nextButton, deleteButton, createButton, modifyButton, filterButton;

    /**
     * Flag indicating whether the filter is applied.
     */
    private boolean isFilterApplied = false;

    /**
     * Panels for displaying view and creation modes.
     */
    private JPanel viewMode = new JPanel(new BorderLayout());
    private JPanel creationMode;

    /**
     * Scroll pane for displaying content with added scrolling functionality.
     */
    private JScrollPane viewScrollPane;

    /**
     * Main frame for the view.
     */
    private JFrame frame;

    /**
     * Constructor that initializes the EmployeeView with a list controller.
     *
     * @param controller The controller for doubly-linked list employee data logic.
     */
    public EmployeeView(ListController controller) {
        // Initialize the employee controller with the provided controller.
        this.employeeController = controller;

        // Create a new ViewController instance and associate it with this view.
        this.viewController = new ViewController(this);

        // Initialize the currentEmployee to null since no employee is selected initially.
        this.currentEmployee = null;

        // Initialize the current employee node as the first node in the list.
        currentEmployeeNode = employeeController.getFirstNode();

        try {
            // Set the look and feel to Nimbus for a modified UI appearance.
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Handle any exceptions that occur during look and feel setup and print the stack trace.
            e.printStackTrace();
        }

        // Create a new JFrame for the application that will encapsulate the rest of elements.
        frame = new JFrame("Employee Management");

        // Create an ImageIcon from an image file to serve as the window icon.
        ImageIcon icon = createImageIcon("resources/caimicon.png");

        if (icon != null) {
            // Set the window icon.
            frame.setIconImage(icon.getImage());
        }

        // Set the default close operation to exit the application when the frame is closed.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the initial size of the frame to 500x300 pixels. It will be able to be rescaled.
        frame.setSize(500, 300);

        // Create GUI components

        // Create text fields for user input and set placeholders.
        nameField = viewController.createLimitedTextField(20, placeHolderName, 50);
        employeeNumberField = viewController.createLimitedTextField(20, placeHolderNumber, 5);
        hireDateField = viewController.createLimitedTextField(20, placeHolderDate, 10);
        phoneField = viewController.createLimitedTextField(20, placeHolderPhone, 9);
        emailField = viewController.createLimitedTextField(20, placeHolderEmail, 30);

        // Set the horizontal and vertical alignment of the employee info to center.
        employeeInfoLabel.setHorizontalAlignment(JLabel.CENTER);
        employeeInfoLabel.setVerticalAlignment(JLabel.CENTER);

        // Create panels to organize the view mode and creation mode.
        viewMode = new JPanel(new BorderLayout());
        viewMode.setBackground(new java.awt.Color(250, 255, 251));
        creationMode = new JPanel(new BorderLayout());

        // Create buttons for the view mode.
        backButton = new JButton("Back");
        nextButton = new JButton("Next");
        deleteButton = new JButton("Delete");
        createButton = new JButton("Create");
        modifyButton = new JButton("Modify");
        filterButton = new JButton("Apply Filter");

        // Set attributes using the ViewController instance.
        viewController.setAllAttributes();

        // Create a panel to hold the buttons in the view mode.
        JPanel buttonPanel = new JPanel(new FlowLayout()); // FlowLayout has centered alignment and a 5-unit horizontal gap by default.
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(createButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(filterButton);

        // Add the button panel to the view mode panel in the south position.
        viewMode.add(buttonPanel, BorderLayout.SOUTH);

        // Set the background color of buttons.
        backButton.setBackground(new java.awt.Color(204, 247, 255));
        nextButton.setBackground(new java.awt.Color(204, 247, 255));
        deleteButton.setBackground(new java.awt.Color(255, 179, 179));
        createButton.setBackground(new java.awt.Color(204, 255, 212));
        modifyButton.setBackground(new java.awt.Color(255, 230, 204));
        filterButton.setBackground(new java.awt.Color(246, 204, 255));

        // Add the button panel to the view mode panel in the south position.
        viewMode.add(buttonPanel, BorderLayout.SOUTH);

        // Add the employeeInfoLabel to the view mode panel in the center.
        viewMode.add(employeeInfoLabel, BorderLayout.CENTER);

        // Update button states based on the current mode.
        viewController.updateButtonStates();

        // Create a scroll pane for the view mode.
        viewScrollPane = new JScrollPane(viewMode);

        // Add the view scroll pane to the main frame.
        frame.add(viewScrollPane);

        // Initially, set the view mode using ViewController.
        viewController.switchToViewMode();

        // Make the main frame visible to the user.
        frame.setVisible(true);

    }

    /**
     * Loads an image file from the given path and creates an ImageIcon from it.
     *
     * @param path The file path of the image to load.
     * @return An ImageIcon created from the loaded image, or null if an error occurs.
     */
    private ImageIcon createImageIcon(String path) {
        try {
            // Read the image file into a BufferedImage.
            BufferedImage image = ImageIO.read(new File(path));

            // Create an ImageIcon from the loaded image.
            return new ImageIcon(image);
        } catch (IOException e) {
            // Handle any IOException that occurs during image loading.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the controller for managing employee data.
     *
     * @return The ListController containing employee data.
     */
    public ListController<EmployeeModel> getEmployeeController() {
        return employeeController;
    }

    /**
     * Get the currently selected employee model.
     *
     * @return The current EmployeeModel.
     */
    public EmployeeModel getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * Get the label used for displaying employee information.
     *
     * @return The JLabel used for displaying employee information.
     */
    public JLabel getEmployeeInfoLabel() {
        return employeeInfoLabel;
    }

    /**
     * Get the node representing the current employee in the linked list.
     *
     * @return The ListController.Node representing the current employee.
     */
    public ListController<EmployeeModel>.Node<EmployeeModel> getCurrentEmployeeNode() {
        return currentEmployeeNode;
    }

    /**
     * Get the text field for entering the employee's name.
     *
     * @return The JTextField for the employee's name.
     */
    public JTextField getNameField() {
        return nameField;
    }

    /**
     * Get the text field for entering the employee's number.
     *
     * @return The JTextField for the employee's number.
     */
    public JTextField getEmployeeNumberField() {
        return employeeNumberField;
    }

    /**
     * Get the text field for entering the employee's hire date.
     *
     * @return The JTextField for the employee's hire date.
     */
    public JTextField getHireDateField() {
        return hireDateField;
    }

    /**
     * Get the text field for entering the employee's phone number.
     *
     * @return The JTextField for the employee's phone number.
     */
    public JTextField getPhoneField() {
        return phoneField;
    }

    /**
     * Get the text field for entering the employee's email address.
     *
     * @return The JTextField for the employee's email address.
     */
    public JTextField getEmailField() {
        return emailField;
    }

    /**
     * Get the "Back" button for navigating in the view.
     *
     * @return The "Back" JButton.
     */
    public JButton getBackButton() {
        return backButton;
    }

    /**
     * Get the "Next" button for navigating in the view.
     *
     * @return The "Next" JButton.
     */
    public JButton getNextButton() {
        return nextButton;
    }

    /**
     * Get the "Delete" button for removing an employee.
     *
     * @return The "Delete" JButton.
     */
    public JButton getDeleteButton() {
        return deleteButton;
    }

    /**
     * Get the "Create" button for creating a new employee.
     *
     * @return The "Create" JButton.
     */
    public JButton getCreateButton() {
        return createButton;
    }

    /**
     * Get the "Modify" button for editing an employee.
     *
     * @return The "Modify" JButton.
     */
    public JButton getModifyButton() {
        return modifyButton;
    }

    /**
     * Get the "Apply Filter" button for applying a filter.
     *
     * @return The "Apply Filter" JButton.
     */
    public JButton getFilterButton() {
        return filterButton;
    }

    /**
     * Check if a filter is currently applied.
     *
     * @return true if a filter is applied, false otherwise.
     */
    public boolean isFilterApplied() {
        return isFilterApplied;
    }

    /**
     * Get the panel for displaying the view mode.
     *
     * @return The JPanel for view mode.
     */
    public JPanel getViewMode() {
        return viewMode;
    }

    /**
     * Get the panel for displaying the creation mode.
     *
     * @return The JPanel for creation mode.
     */
    public JPanel getCreationMode() {
        return creationMode;
    }

    /**
     * Get the scroll pane for displaying content in the view.
     *
     * @return The JScrollPane for view content.
     */
    public JScrollPane getViewScrollPane() {
        return viewScrollPane;
    }

    /**
     * Get the current mode of the view.
     *
     * @return The current Mode of the view.
     */
    public Mode getCurrentMode() {
        return currentMode;
    }

    /**
     * Get the main frame for the view.
     *
     * @return The main JFrame for the view.
     */
    public JFrame getFrame() {
        return frame;
    }


}
