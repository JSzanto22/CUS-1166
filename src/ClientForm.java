import javax.swing.*;
import java.awt.*;

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

    private final MainFrame mainFrame;
    private final Logger logger;

    public ClientForm(MainFrame mainFrame, Logger logger) {
        this.mainFrame = mainFrame;
        this.logger = logger;
        initComponents();

        // Back Button - go back to role screen (CardLayout)
        backButton.addActionListener(e -> mainFrame.showScreen(MainFrame.ROLE_SCREEN));

        // Clear Button - clear all fields
        clearButton.addActionListener(e -> clearFields());

        // Submit Button - validate & log data
        submitButton.addActionListener(e -> submitClientJob());
    }

    private void clearFields() {
        clientIDTextField.setText("");
        ClientNameTextField.setText("");
        jobDurationTextField.setText("");
        jobDeadlineTextField.setText("");
    }

    private void submitClientJob() {
        String clientID = clientIDTextField.getText().trim();
        String clientName = ClientNameTextField.getText().trim();
        String duration = jobDurationTextField.getText().trim();
        String deadline = jobDeadlineTextField.getText().trim();

        //  box check
        if (clientID.isEmpty() || clientName.isEmpty() || duration.isEmpty() || deadline.isEmpty()) {
            JOptionPane.showMessageDialog(panel1,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            logger.warning("CLIENT_SUBMIT blocked: missing fields");
            return;
        }

        //  submit
        logger.info("CLIENT_SUBMIT clientId=" + clientID +
                " name=" + clientName +
                " durationHrs=" + duration +
                " deadline=" + deadline);

        JOptionPane.showMessageDialog(panel1, "Client job submitted (logged) successfully!");
        clearFields();
    }

    private void initComponents() {
        panel1 = new JPanel(new BorderLayout(10, 10));
        panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        ClientID = new JLabel("Client ID:");
        clientIDTextField = new JTextField(20);
        ClientName = new JLabel("Client Name:");
        ClientNameTextField = new JTextField(20);
        jobDuration = new JLabel("Job Duration (minutes):");
        jobDurationTextField = new JTextField(20);
        JobDeadline = new JLabel("Job Deadline:");
        jobDeadlineTextField = new JTextField(20);

        fieldsPanel.add(ClientID);
        fieldsPanel.add(clientIDTextField);
        fieldsPanel.add(ClientName);
        fieldsPanel.add(ClientNameTextField);
        fieldsPanel.add(jobDuration);
        fieldsPanel.add(jobDurationTextField);
        fieldsPanel.add(JobDeadline);
        fieldsPanel.add(jobDeadlineTextField);

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