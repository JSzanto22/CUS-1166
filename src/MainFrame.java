import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class MainFrame extends JFrame {

    // Screen name constants
    public static final String ROLE_SCREEN = "role";
    public static final String OWNER_SCREEN = "owner";
    public static final String CLIENT_SCREEN = "client";
    public static final String PROGRESS_SCREEN = "progress";

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, ProgressReport> progressReports = new HashMap<>();
    private ProgressReport currentReport;

    public MainFrame() {

        // Frame setup
        setTitle("Vehicular Cloud Console");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel(
                "Vehicular Cloud Real-Time System",
                SwingConstants.CENTER
        );
        add(header, BorderLayout.NORTH);

        // Footer
        JLabel footer = new JLabel(
                "CUS1166 Software Engineering",
                SwingConstants.CENTER
        );
        add(footer, BorderLayout.SOUTH);

        // CardLayout container
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        Logger logger = new FileLogger("src/logs.txt");

        contentPanel.add(createRoleSelectionPanel(), ROLE_SCREEN);
        contentPanel.add(new OwnerForm(this, logger).getPanel(), OWNER_SCREEN);
        contentPanel.add(new ClientForm(this, logger).getPanel(), CLIENT_SCREEN);
        contentPanel.add(createProgressPanel(), PROGRESS_SCREEN);

        add(contentPanel, BorderLayout.CENTER);

        // Show first screen
        showScreen(ROLE_SCREEN);

        setVisible(true);
    }

    //Allows other panels to switch screens
    public void showScreen(String screenName) {

        cardLayout.show(contentPanel, screenName);

    }

     
    /*
     * Redacted for now
     * This is a tester panel now
     */
//Role selection panel (Owner & Client)
    private JPanel createRoleSelectionPanel() {

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JLabel label = new JLabel(
                "Select User Type",
                SwingConstants.CENTER
        );

        JButton ownerButton = new JButton("Vehicle Owner");
        JButton clientButton = new JButton("Client");
        JButton progressButton = new JButton("View Progress Reports");

        // Navigation actions
        ownerButton.addActionListener(e ->
                showScreen(OWNER_SCREEN)
        );

        clientButton.addActionListener(e ->
                showScreen(CLIENT_SCREEN)
        );

        progressButton.addActionListener(e ->
                showScreen(PROGRESS_SCREEN)
        );

        panel.add(label);
        panel.add(ownerButton);
        panel.add(clientButton);
        panel.add(progressButton);

        return panel;
    }

    private JPanel createProgressPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Enter Job ID to view/update progress report", SwingConstants.CENTER);

        JTextField jobIdField = new JTextField(20);
        JButton getReportButton = new JButton("Get Report");
        JTextArea reportArea = new JTextArea(10, 40);
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);

        // Update controls
        JPanel updatePanel = new JPanel(new FlowLayout());
        JTextField progressField = new JTextField(5);
        JButton updateProgressButton = new JButton("Update Progress");
        JTextField statusField = new JTextField(10);
        JButton updateStatusButton = new JButton("Update Status");

        updateProgressButton.addActionListener(e -> {
            if (currentReport != null) {
                try {
                    double prog = Double.parseDouble(progressField.getText());
                    currentReport.updateProgress(prog);
                    reportArea.setText(currentReport.getSummary());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid progress value");
                }
            }
        });

        updateStatusButton.addActionListener(e -> {
            if (currentReport != null) {
                String status = statusField.getText().trim();
                currentReport.updateStatus(status);
                if ("completed".equalsIgnoreCase(status) || "failed".equalsIgnoreCase(status)) {
                    currentReport.setEndTime(java.time.LocalDateTime.now());
                }
                reportArea.setText(currentReport.getSummary());
            }
        });

        updatePanel.add(new JLabel("Progress:"));
        updatePanel.add(progressField);
        updatePanel.add(updateProgressButton);
        updatePanel.add(new JLabel("Status:"));
        updatePanel.add(statusField);
        updatePanel.add(updateStatusButton);

        getReportButton.addActionListener(e -> {
            String jobId = jobIdField.getText().trim();
            currentReport = progressReports.get(jobId);
            if (currentReport != null) {
                reportArea.setText(currentReport.getSummary());
            } else {
                reportArea.setText("No progress report found for Job ID: " + jobId);
                currentReport = null;
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(label);
        topPanel.add(jobIdField);
        topPanel.add(getReportButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(updatePanel, BorderLayout.SOUTH);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showScreen(ROLE_SCREEN));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        // Add back button to south, but since updatePanel is south, maybe add to a new panel
        // For simplicity, add back to updatePanel
        updatePanel.add(backButton);

        return panel;
    }

    public void addProgressReport(String jobId, ProgressReport report) {
        progressReports.put(jobId, report);
    }
}