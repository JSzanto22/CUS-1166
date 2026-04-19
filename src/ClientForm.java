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
    private JButton clearButton;
    private JButton submitButton;
    private JLabel statusLabel;

    private final Client client;
    private final Logger logger;
    private volatile boolean awaitingDecision;

    public ClientForm(Client client, Logger logger) {
        this.client = client;
        this.logger = logger;
        initComponents();
        clearButton.addActionListener(e -> clearFields());
        submitButton.addActionListener(e -> submitClientJob());
    }

    private void clearFields() {
        clientIDTextField.setText("");
        ClientNameTextField.setText("");
        jobDurationTextField.setText("");
        jobDeadlineTextField.setText("");
    }

    private void submitClientJob() {
        if (awaitingDecision) {
            JOptionPane.showMessageDialog(panel1,
                    "This request is still pending. Wait for an accept or reject decision before sending again.");
            return;
        }

        String clientID = clientIDTextField.getText().trim();
        String clientName = ClientNameTextField.getText().trim();
        String duration = jobDurationTextField.getText().trim();
        String deadline = jobDeadlineTextField.getText().trim();

        if (clientID.isEmpty() || clientName.isEmpty() || duration.isEmpty() || deadline.isEmpty()) {
            JOptionPane.showMessageDialog(panel1,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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
            return;
        }

        client.setClientID(clientID);
        client.setClientName(clientName);
        client.setApproxJobDuration(String.valueOf(durationMinutes));
        client.setJobDeadline(deadline);

        Job job = new Job(
                clientID,
                "PENDING",
                durationMinutes,
                LocalDateTime.now(),
                clientID,
                clientName,
                deadline
        );

        awaitingDecision = true;
        setSubmissionEnabled(false);
        setStatus("Pending admin review. No additional submissions are allowed until a decision is returned.", new Color(0x8A, 0x5A, 0x00));

        new Thread(() -> {
            try {
                String decision = client.sendJob(job);
                SwingUtilities.invokeLater(() -> handleDecision(decision));
            } catch (RuntimeException ex) {
                SwingUtilities.invokeLater(() -> handleFailure(ex));
            }
        }, "client-submit-" + clientID).start();
    }

    private void handleDecision(String decision) {
        awaitingDecision = false;
        setSubmissionEnabled(true);

        if ("accepted".equalsIgnoreCase(decision)) {
            setStatus("Accepted by the VC controller.", new Color(0x1B, 0x5E, 0x20));
            JOptionPane.showMessageDialog(panel1, "Request accepted. You may submit another job now.");
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
        JobDeadline = new JLabel("Job Deadline (yyyy-mm-dd):");
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

        statusLabel = new JLabel("Ready to submit.");
        statusLabel.setForeground(new Color(0x55, 0x63, 0x6C));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(cardFill);
        Dimension btn = new Dimension(120, 36);
        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(btn);
        submitButton = new JButton("Submit");
        submitButton.setPreferredSize(btn);
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
