import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientForm {
    private JPanel panel1;
    private JLabel ClientID;
    private JTextField clientIDTextField;
    private JLabel ClientName;
    private JTextField ClientNameTextField;
    private JLabel jobDuration;
    private JTextField jobDurationTextField;
    private JLabel JobDeadline;
    private JTextField jobDeadlineTextField;
    private JButton backButton;
    private JButton clearButton;
    private JButton submitButton;

    public ClientForm() {
        // Back Button - navigate to SelectRole panel
        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
            frame.setTitle("VCRTS - Select Role");
            frame.setContentPane(new SelectRole().getPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
        });

        // Clear Button - clear all fields
        clearButton.addActionListener(e -> {
            clientIDTextField.setText("");
            ClientNameTextField.setText("");
            jobDurationTextField.setText("");
            jobDeadlineTextField.setText("");
        });

        // Submit Button - validate & save data
        submitButton.addActionListener(e -> saveClientData());
    }

    private void saveClientData() {
        String clientID = clientIDTextField.getText().trim();
        String clientName = ClientNameTextField.getText().trim();
        String duration = jobDurationTextField.getText().trim();
        String deadline = jobDeadlineTextField.getText().trim();

        // Validate fields
        if (clientID.isEmpty() || clientName.isEmpty() || duration.isEmpty() || deadline.isEmpty()) {
            JOptionPane.showMessageDialog(panel1,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            FileWriter writer = new FileWriter("client_transactions.txt", true);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(dtf);

            writer.write("----- Client Submission -----\n");
            writer.write("Timestamp: " + timestamp + "\n");
            writer.write("Client ID: " + clientID + "\n");
            writer.write("Client Name: " + clientName + "\n");
            writer.write("Job Duration: " + duration + " hrs\n");
            writer.write("Job Deadline: " + deadline + "\n\n");

            writer.close();

            JOptionPane.showMessageDialog(panel1,
                    "Client job submitted successfully!");

            // Clear fields after successful submission
            clientIDTextField.setText("");
            ClientNameTextField.setText("");
            jobDurationTextField.setText("");
            jobDeadlineTextField.setText("");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(panel1,
                    "Error saving data!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return panel1;
    }

    // Optional: For testing the panel standalone
    public static void main(String[] args) {
        JFrame frame = new JFrame("VCRTS - Client Panel");
        frame.setContentPane(new ClientForm().getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}