import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

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
    private final VehicularCloudController vcController;

    public ClientForm(MainFrame mainFrame, Logger logger, VehicularCloudController vcController) {
        this.mainFrame = mainFrame;
        this.logger = logger;
        this.vcController = vcController;
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

        int durationMinutes;
        try {
            durationMinutes = Integer.parseInt(duration);
            if (durationMinutes <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel1,
                    "Job Duration must be a valid positive number of minutes.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            logger.warning("CLIENT_SUBMIT blocked: invalid duration=" + duration);
            return;
        }

        String jobId = clientID + "-" + System.currentTimeMillis();
        Job job = new Job(jobId, "PENDING", durationMinutes, LocalDateTime.now());
        vcController.addJob(job);

        //  submit
        logger.info("CLIENT_SUBMIT clientId=" + clientID +
                " name=" + clientName +
                " durationMinutes=" + durationMinutes +
                " deadline=" + deadline);

        JOptionPane.showMessageDialog(panel1, "Client job submitted (logged) successfully!");
        clearFields();
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

        JLabel title = new JLabel("Client Job Submission");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(0x26, 0x32, 0x38));
        card.add(title, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        fieldsPanel.setBackground(cardFill);
        ClientID = new JLabel("Client ID:");
        ClientID.setHorizontalAlignment(SwingConstants.RIGHT);
        clientIDTextField = new JTextField(20);
        ClientName = new JLabel("Client Name:");
        ClientName.setHorizontalAlignment(SwingConstants.RIGHT);
        ClientNameTextField = new JTextField(20);
        jobDuration = new JLabel("Job Duration (minutes):");
        jobDuration.setHorizontalAlignment(SwingConstants.RIGHT);
        jobDurationTextField = new JTextField(20);
        JobDeadline = new JLabel("Job Deadline:");
        JobDeadline.setHorizontalAlignment(SwingConstants.RIGHT);
        jobDeadlineTextField = new JTextField(20);

        fieldsPanel.add(ClientID);
        fieldsPanel.add(clientIDTextField);
        fieldsPanel.add(ClientName);
        fieldsPanel.add(ClientNameTextField);
        fieldsPanel.add(jobDuration);
        fieldsPanel.add(jobDurationTextField);
        fieldsPanel.add(JobDeadline);
        fieldsPanel.add(jobDeadlineTextField);

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

        // NORTH keeps fields at natural height; extra space stays below title / above buttons.
        JPanel formBody = new JPanel(new BorderLayout());
        formBody.setOpaque(false);
        formBody.add(fieldsPanel, BorderLayout.NORTH);
        card.add(formBody, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

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