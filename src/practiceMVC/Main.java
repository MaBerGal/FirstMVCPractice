package practiceMVC;

// For thread security.
import javax.swing.SwingUtilities;

/**
 * The main class to start the application.
 */
public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure Swing components are initialized on the Event Dispatch Thread (EDT).
        // Note: Swing is not thread safe.
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * The run method creates a ListController and EmployeeView to start the application.
             */
            public void run() {
                ListController<EmployeeModel> listController = new ListController<>();
                new EmployeeView(listController);
            }
        });
    }
}

