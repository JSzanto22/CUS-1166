import javax.swing.*;
import java.awt.*;

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
        initComponents();

        backButton.addActionListener(e -> {
           // logger.info("OWNER clicked BACK");
            mainFrame.showScreen(MainFrame.ROLE_SCREEN);
        });

        clearButton.addActionListener(e -> {
            ownerIdTextField.setText("");
            vehicleMakeTextField.setText("");
            vehicleModelTextField.setText("");
            vehicleYearTextField.setText("");
            residencyTimeTextField.setText("");
           // logger.info("OWNER clicked CLEAR");
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
                JOptionPane.showMessageDialog(panel1, "Vehicle Year must be a valid number (ex: 2020).");
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

    private void initComponents() {
        panel1 = new JPanel(new BorderLayout(10, 10));
        panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        ownerIdTextField = new JTextField(20);
        vehicleMakeTextField = new JTextField(20);
        vehicleModelTextField = new JTextField(20);
        vehicleYearTextField = new JTextField(20);
        residencyTimeTextField = new JTextField(20);

        fieldsPanel.add(new JLabel("Owner ID:"));
        fieldsPanel.add(ownerIdTextField);
        fieldsPanel.add(new JLabel("Vehicle Make:"));
        fieldsPanel.add(vehicleMakeTextField);
        fieldsPanel.add(new JLabel("Vehicle Model:"));
        fieldsPanel.add(vehicleModelTextField);
        fieldsPanel.add(new JLabel("Vehicle Year:"));
        fieldsPanel.add(vehicleYearTextField);
        fieldsPanel.add(new JLabel("Residency Time(minutes):"));
        fieldsPanel.add(residencyTimeTextField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        backButton = new JButton("Back");
        clearButton = new JButton("Clear");
        submitButton = new JButton("Submit");
        buttonPanel.add(backButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(submitButton);

        panel1.add(fieldsPanel, BorderLayout.CENTER);
        panel1.add(buttonPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel1;
    }
}