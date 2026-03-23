/* Project: Lab9
 * Class: OwnerForm.java
 * Author: Thurman Patterson
 * Date: Feb 22 2026
 * This program creates a panel for owner info and then sends it to the logger
 */


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
            clearFields();
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

            // Log entry aa
            String logMsg =
                    "OWNER_SUBMIT | ownerId=" + ownerId +
                            ", make=" + make +
                            ", model=" + model +
                            ", year=" + year +
                            ", residencyTime=" + residency;

            logger.info(logMsg);

            JOptionPane.showMessageDialog(panel1, "Owner form submitted");
            clearFields();
        });
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

        Color cardFill = new Color(0xF5, 0xF8, 0xF6);   // light gray-green
        Color cardBorder = new Color(0xD7, 0xE3, 0xDD); // subtle border
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(cardFill);
        Dimension btn = new Dimension(120, 36);
        backButton = new JButton("Back");
        backButton.setPreferredSize(btn);
        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(btn);
        submitButton = new JButton("Submit");
        submitButton.setPreferredSize(btn);
        submitButton.setFocusPainted(false);
        buttonPanel.add(backButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(submitButton);

        JPanel formBody = new JPanel(new BorderLayout());
        formBody.setOpaque(false);
        formBody.add(fieldsPanel, BorderLayout.NORTH);
        card.add(formBody, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        // Fill the screen area so the card isn't taller than the viewport (which clips the bottom
        // and can hide the Submit row on the taller owner form).
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel1.add(card, gbc);
    }

    public JPanel getPanel() {
        return panel1;
    }
}