import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class OwnerForm {
    private JPanel panel1;
    private JTextField ownerIdTextField;
    private JTextField vehicleMakeTextField;
    private JTextField vehicleModelTextField;
    private JTextField vehicleYearTextField;
    private JTextField residencyTimeTextField;
    private JButton clearButton;
    private JButton submitButton;
    private JLabel statusLabel;

    private final Owner owner;
    private volatile boolean awaitingDecision;

    public OwnerForm(Owner owner, Logger logger) {
        this.owner = owner;
        initComponents();

        clearButton.addActionListener(e -> clearFields());
        submitButton.addActionListener(e -> submitOwnerVehicle());
    }

    private void submitOwnerVehicle() {
        if (awaitingDecision) {
            JOptionPane.showMessageDialog(panel1,
                    "This request is still pending. Wait for an accept or reject decision before sending again.");
            return;
        }

        String ownerId = ownerIdTextField.getText().trim();
        String make = vehicleMakeTextField.getText().trim();
        String model = vehicleModelTextField.getText().trim();
        String yearStr = vehicleYearTextField.getText().trim();
        String residency = residencyTimeTextField.getText().trim();

        if (ownerId.isEmpty() || make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || residency.isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Please fill in all fields before submitting.");
            return;
        }

        int year;
        int residencyMinutes;
        try {
            year = Integer.parseInt(yearStr);
            residencyMinutes = Integer.parseInt(residency);
            if (year < 1900 || year > 2100 || residencyMinutes <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel1, "Vehicle year and residency time must be valid positive numbers.");
            return;
        }

        owner.setOwnerID(ownerId);
        owner.setVehicleMake(make);
        owner.setVehicleModel(model);
        owner.setVehicleYear(String.valueOf(year));
        owner.setVehicleInfo(make + " " + model + " " + year);
        owner.setApproxResidencyTime(String.valueOf(residencyMinutes));

        Vehicle vehicle = new Vehicle(
                ownerId + "-" + System.currentTimeMillis(),
                "PENDING",
                owner,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(residencyMinutes)
        );

        awaitingDecision = true;
        setSubmissionEnabled(false);
        setStatus("Pending admin review. No additional submissions are allowed until a decision is returned.", new Color(0x8A, 0x5A, 0x00));

        new Thread(() -> {
            try {
                String decision = owner.sendVehicle(vehicle);
                SwingUtilities.invokeLater(() -> handleDecision(decision));
            } catch (RuntimeException ex) {
                SwingUtilities.invokeLater(() -> handleFailure(ex));
            }
        }, "owner-submit-" + ownerId).start();
    }

    private void handleDecision(String decision) {
        awaitingDecision = false;
        setSubmissionEnabled(true);

        if ("accepted".equalsIgnoreCase(decision)) {
            setStatus("Accepted by the VC controller.", new Color(0x1B, 0x5E, 0x20));
            JOptionPane.showMessageDialog(panel1, "Request accepted. You may submit another vehicle now.");
            clearFields();
            return;
        }

        setStatus("Rejected by the VC controller.", new Color(0xB7, 0x1C, 0x1C));
        JOptionPane.showMessageDialog(panel1, "Request rejected. Review the data and submit again if needed.");
    }

    private void handleFailure(RuntimeException ex) {
        awaitingDecision = false;
        setSubmissionEnabled(true);
        setStatus("Connection problem. Submission did not complete.", new Color(0xB7, 0x1C, 0x1C));
        JOptionPane.showMessageDialog(panel1, "Connection error while waiting for a decision: " + ex.getMessage());
    }

    private void setSubmissionEnabled(boolean enabled) {
        submitButton.setEnabled(enabled);
        clearButton.setEnabled(enabled);
    }

    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private void clearFields() {
        ownerIdTextField.setText("");
        vehicleMakeTextField.setText("");
        vehicleModelTextField.setText("");
        vehicleYearTextField.setText("");
        residencyTimeTextField.setText("");
    }

    private void initComponents() {
        panel1 = new JPanel(new GridBagLayout());
        panel1.setBackground(new Color(0x6F, 0xA6, 0x8C));
        panel1.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        Color cardFill = new Color(0xF5, 0xF8, 0xF6);
        Color cardBorder = new Color(0xD7, 0xE3, 0xDD);

        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(cardFill);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Vehicle Owner Registration");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(0x26, 0x32, 0x38));
        card.add(title, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        fieldsPanel.setBackground(cardFill);

        ownerIdTextField = new JTextField(20);
        vehicleMakeTextField = new JTextField(20);
        vehicleModelTextField = new JTextField(20);
        vehicleYearTextField = new JTextField(20);
        residencyTimeTextField = new JTextField(20);

        JLabel l1 = new JLabel("Owner ID:");
        l1.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l1);
        fieldsPanel.add(ownerIdTextField);
        JLabel l2 = new JLabel("Vehicle Make:");
        l2.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l2);
        fieldsPanel.add(vehicleMakeTextField);
        JLabel l3 = new JLabel("Vehicle Model:");
        l3.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l3);
        fieldsPanel.add(vehicleModelTextField);
        JLabel l4 = new JLabel("Vehicle Year:");
        l4.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l4);
        fieldsPanel.add(vehicleYearTextField);
        JLabel l5 = new JLabel("Residency Time (minutes):");
        l5.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l5);
        fieldsPanel.add(residencyTimeTextField);

        statusLabel = new JLabel("Ready to submit.");
        statusLabel.setForeground(new Color(0x55, 0x63, 0x6C));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(cardFill);

        Dimension btnSize = new Dimension(120, 36);
        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(btnSize);
        submitButton = new JButton("Submit");
        submitButton.setPreferredSize(btnSize);
        submitButton.setFocusPainted(false);

        buttonPanel.add(clearButton);
        buttonPanel.add(submitButton);

        JPanel formBody = new JPanel(new BorderLayout(0, 12));
        formBody.setOpaque(false);
        formBody.add(fieldsPanel, BorderLayout.NORTH);
        formBody.add(statusLabel, BorderLayout.SOUTH);

        card.add(formBody, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel1.add(card, gbc);
    }

    public JPanel getPanel() {
        return panel1;
    }
}


