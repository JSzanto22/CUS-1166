import javax.swing.*;

public class OwnerForm {
    private JPanel panel1;


    private JTextField ownerIdTextField;
    private JTextField vehicleMakeTextField;
    private JTextField vehicleModelTextField;
    private JTextField vehicleYearTextField;
    private JTextField residencyTimeTextField;

    private JButton backButton;
    private JButton clearButton;
    private JButton submitButton;

    private final Logger logger;
    private final MainFrame mainFrame;

    public OwnerForm(MainFrame mainFrame, Logger logger) {
        this.mainFrame = mainFrame;
        this.logger = logger;

        backButton.addActionListener(e -> {
            logger.info("OWNER clicked BACK");
            mainFrame.showScreen(MainFrame.ROLE_SCREEN);
        });

        clearButton.addActionListener(e -> {
            ownerIdTextField.setText("");
            vehicleMakeTextField.setText("");
            vehicleModelTextField.setText("");
            vehicleYearTextField.setText("");
            residencyTimeTextField.setText("");
            logger.info("OWNER clicked CLEAR");
        });

        submitButton.addActionListener(e -> {
            // values
            String ownerId = ownerIdTextField.getText().trim();
            String make = vehicleMakeTextField.getText().trim();
            String model = vehicleModelTextField.getText().trim();
            String yearStr = vehicleYearTextField.getText().trim();
            String residency = residencyTimeTextField.getText().trim();

            // check boxes
            if (ownerId.isEmpty() || make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || residency.isEmpty()) {
                JOptionPane.showMessageDialog(panel1, "Please fill in all fields before submitting.");
                logger.warning("OWNER submit blocked: missing fields");
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
                if (year < 1900 || year > 2100) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel1, "Vehicle Year must be a valid number (ex: 2018).");
                logger.warning("OWNER submit blocked: invalid year=" + yearStr);
                return;
            }

            // Log entry
            String logMsg =
                    "OWNER_SUBMIT | ownerId=" + ownerId +
                            ", make=" + make +
                            ", model=" + model +
                            ", year=" + year +
                            ", residencyTime=" + residency;

            logger.info(logMsg);

            JOptionPane.showMessageDialog(panel1, "Owner form submitted and logged!");
        });
    }

    public JPanel getPanel() {
        return panel1;
    }
}