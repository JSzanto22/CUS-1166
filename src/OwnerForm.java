/* Project: Lab9 
 * Class: OwnerForm.java 
 * Author: Thurman Patterson (Logic updated for Milestone 5)
 * Date: April 5, 2026 
 * This program creates a panel for owner info and sends it to the VC Controller via Sockets.
 */ 
import javax.swing.*; 
import java.awt.*; 
import java.io.*;
import java.net.*;
import java.util.logging.Logger;

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
            mainFrame.showScreen(MainFrame.ROLE_SCREEN); 
        }); 

        clearButton.addActionListener(e -> { 
            clearFields(); 
        }); 

        submitButton.addActionListener(e -> { 
            // 1. Collect values
            String ownerId = ownerIdTextField.getText().trim(); 
            String make = vehicleMakeTextField.getText().trim(); 
            String model = vehicleModelTextField.getText().trim(); 
            String yearStr = vehicleYearTextField.getText().trim(); 
            String residency = residencyTimeTextField.getText().trim(); 

            // 2. Validation (Kept from original screen)
            if (ownerId.isEmpty() || make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || residency.isEmpty()) {
                JOptionPane.showMessageDialog(panel1, "Please fill in all fields before submitting."); 
                return; 
            } 

            // 3. Milestone 5 Network Logic
            // Running in a Thread prevents the UI from freezing while waiting for Admin approval
            new Thread(() -> {
                try (Socket socket = new Socket("localhost", 12345); 
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    // Send data to VC Controller
                    String logMsg = "VEHICLE_SUBMIT|" + ownerId + "|" + make + "|" + model + "|" + yearStr + "|" + residency;
                    out.println(logMsg);

                    // 4. Wait for Server Notification (Accepted/Rejected)
                    String serverResponse = in.readLine(); 

                    // 5. Notify User via Popup (Requirement)
                    SwingUtilities.invokeLater(() -> {
                        if ("ACCEPTED".equalsIgnoreCase(serverResponse)) {
                            JOptionPane.showMessageDialog(panel1, "Request ACCEPTED. Data saved to server logs.");
                            clearFields();
                        } else {
                            JOptionPane.showMessageDialog(panel1, "Request REJECTED. Data was not saved.");
                        }
                    });

                } catch (IOException ex) {
                    SwingUtilities.invokeLater(() -> 
                        JOptionPane.showMessageDialog(panel1, "Connection Error: Is the Server running?")
                    );
                    logger.severe("Connection failed: " + ex.getMessage());
                }
            }).start();
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

        JLabel l1 = new JLabel("Owner ID:"); l1.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l1); fieldsPanel.add(ownerIdTextField); 
        JLabel l2 = new JLabel("Vehicle Make:"); l2.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l2); fieldsPanel.add(vehicleMakeTextField); 
        JLabel l3 = new JLabel("Vehicle Model:"); l3.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l3); fieldsPanel.add(vehicleModelTextField); 
        JLabel l4 = new JLabel("Vehicle Year:"); l4.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l4); fieldsPanel.add(vehicleYearTextField); 
        JLabel l5 = new JLabel("Residency Time (minutes):"); l5.setHorizontalAlignment(SwingConstants.RIGHT);
        fieldsPanel.add(l5); fieldsPanel.add(residencyTimeTextField); 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); 
        buttonPanel.setBackground(cardFill); 
        
        Dimension btnSize = new Dimension(120, 36);
        backButton = new JButton("Back"); backButton.setPreferredSize(btnSize);
        clearButton = new JButton("Clear"); clearButton.setPreferredSize(btnSize);
        submitButton = new JButton("Submit"); submitButton.setPreferredSize(btnSize); 
        submitButton.setFocusPainted(false);

        buttonPanel.add(backButton); 
        buttonPanel.add(clearButton); 
        buttonPanel.add(submitButton); 

        JPanel formBody = new JPanel(new BorderLayout());
        formBody.setOpaque(false);
        formBody.add(fieldsPanel, BorderLayout.NORTH);

        card.add(formBody, BorderLayout.CENTER); 
        card.add(buttonPanel, BorderLayout.SOUTH); 

        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.weightx = 1; gbc.weighty = 1; 
        panel1.add(card, gbc); 
    } 

    public JPanel getPanel() { 
        return panel1; 
    } 
}


