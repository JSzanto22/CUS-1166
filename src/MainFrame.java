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
    public static final String INTRO_SCREEN = "intro";
    public static final String ADMIN_SCREEN = "admin";
    private static final Color APP_BG = new Color(0x6F, 0xA6, 0x8C); // #6FA68C
    private static final Font APP_FONT = pickAppFont();

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, ProgressReport> progressReports = new HashMap<>();
    private ProgressReport currentReport;

    public MainFrame() {

        installGlobalFont(APP_FONT);

        // Frame setup
        setTitle("Vehicular Cloud Console");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(APP_BG);

        // Header a
        JLabel header = new JLabel(
                "Vehicular Cloud Real-Time System",
                SwingConstants.CENTER
        );
        header.setOpaque(true);
        header.setBackground(APP_BG);
        header.setForeground(Color.WHITE);
        add(header, BorderLayout.NORTH);

        // Footer
        JLabel footer = new JLabel(
                "CUS1166 Software Engineering",
                SwingConstants.CENTER
        );
        footer.setOpaque(true);
        footer.setBackground(APP_BG);
        footer.setForeground(Color.WHITE);
        add(footer, BorderLayout.SOUTH);

        // CardLayout container
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(APP_BG);

        Logger logger = new FileLogger("src/logs.txt");
        
        contentPanel.add(new IntroPanel(this), INTRO_SCREEN);
        contentPanel.add(createRoleSelectionPanel(), ROLE_SCREEN);
        contentPanel.add(new OwnerForm(this, logger).getPanel(), OWNER_SCREEN);
        contentPanel.add(new ClientForm(this, logger).getPanel(), CLIENT_SCREEN);
<<<<<<< HEAD
        contentPanel.add(createProgressPanel(), PROGRESS_SCREEN);
=======
        contentPanel.add(new AdminPanel(this), ADMIN_SCREEN);
>>>>>>> b6cc2ed6ae729672371b40a684a66c689a9d7c1e

        add(contentPanel, BorderLayout.CENTER);

        // Show first screen
        showScreen(INTRO_SCREEN);

        setVisible(true);
    }

    private static Font pickAppFont() {
        Font f = new Font("Futura", Font.PLAIN, 14);
        if (!"Futura".equalsIgnoreCase(f.getFamily())) {
            // Fallback if Futura isn't available on this machine
            f = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        }
        return f;
    }

    private static void installGlobalFont(Font base) {
        Font ui = base;
        UIManager.put("Label.font", ui);
        UIManager.put("Button.font", ui);
        UIManager.put("TextField.font", ui);
        UIManager.put("TextArea.font", ui);
        UIManager.put("TextPane.font", ui);
        UIManager.put("ComboBox.font", ui);
        UIManager.put("CheckBox.font", ui);
        UIManager.put("RadioButton.font", ui);
        UIManager.put("TitledBorder.font", ui.deriveFont(Font.BOLD));
        UIManager.put("OptionPane.messageFont", ui);
        UIManager.put("OptionPane.buttonFont", ui);
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

<<<<<<< HEAD
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
=======
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(APP_BG);
>>>>>>> b6cc2ed6ae729672371b40a684a66c689a9d7c1e

        JLabel label = new JLabel("Select User Type", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));

        JButton ownerButton = new JButton("Vehicle Owner");
        JButton clientButton = new JButton("Client");
<<<<<<< HEAD
        JButton progressButton = new JButton("View Progress Reports");
=======
        JButton adminButton = new JButton("Admin");
        Dimension buttonSize = new Dimension(200, 40);
        ownerButton.setPreferredSize(buttonSize);
        clientButton.setPreferredSize(buttonSize);
        adminButton.setPreferredSize(buttonSize);
        ownerButton.setForeground(Color.BLACK);
        clientButton.setForeground(Color.BLACK);
        adminButton.setForeground(Color.BLACK);
        ownerButton.setBackground(Color.WHITE);
        clientButton.setBackground(Color.WHITE);
        adminButton.setBackground(Color.WHITE);
        ownerButton.setOpaque(true);
        clientButton.setOpaque(true);
        adminButton.setOpaque(true);
        ownerButton.setContentAreaFilled(true);
        clientButton.setContentAreaFilled(true);
        adminButton.setContentAreaFilled(true);
        ownerButton.setFocusPainted(false);
        clientButton.setFocusPainted(false);
        adminButton.setFocusPainted(false);
>>>>>>> b6cc2ed6ae729672371b40a684a66c689a9d7c1e

        // Navigation actions
        ownerButton.addActionListener(e ->
                showScreen(OWNER_SCREEN)
        );

        clientButton.addActionListener(e ->
                showScreen(CLIENT_SCREEN)
        );

<<<<<<< HEAD
        progressButton.addActionListener(e ->
                showScreen(PROGRESS_SCREEN)
        );

        panel.add(label);
        panel.add(ownerButton);
        panel.add(clientButton);
        panel.add(progressButton);
=======
        adminButton.addActionListener(e -> showScreen(ADMIN_SCREEN));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 20, 10);
        panel.add(label, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(ownerButton, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(clientButton, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(adminButton, gbc);
>>>>>>> b6cc2ed6ae729672371b40a684a66c689a9d7c1e

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