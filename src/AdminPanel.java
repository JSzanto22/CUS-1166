import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminPanel extends JPanel {

    private static final Color APP_BG = new Color(0x6F, 0xA6, 0x8C);
    private static final Color CARD_FILL = new Color(0xF5, 0xF8, 0xF6);
    private static final Color CARD_BORDER = new Color(0xD7, 0xE3, 0xDD);

    private final VehicularCloudController vcController;
    private final VcRequestQueue requestQueue;
    private final Logger logger;

    private JTextArea logArea;
    private JLabel statusLabel;
    private JPanel requestsListPanel;

    public AdminPanel(MainFrame mainFrame, VehicularCloudController vcController,
                      VcRequestQueue requestQueue, Logger logger) {
        this.vcController = vcController;
        this.requestQueue = requestQueue;
        this.logger = logger;

        setLayout(new GridBagLayout());
        setBackground(APP_BG);
        setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(CARD_FILL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Admin - VC Controller");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(0x26, 0x32, 0x38));
        card.add(title, BorderLayout.NORTH);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(0x55, 0x63, 0x6C));
        card.add(statusLabel, BorderLayout.SOUTH);

        requestsListPanel = new JPanel();
        requestsListPanel.setLayout(new BoxLayout(requestsListPanel, BoxLayout.Y_AXIS));
        requestsListPanel.setBackground(CARD_FILL);

        JScrollPane requestsScroll = new JScrollPane(requestsListPanel);
        requestsScroll.setPreferredSize(new Dimension(520, 130));
        requestsScroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel headerRow = new JPanel(new GridLayout(1, 4, 8, 0));
        headerRow.setBackground(CARD_FILL);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        headerRow.add(new JLabel("Type"));
        headerRow.add(new JLabel("Request"));
        headerRow.add(new JLabel(""));
        headerRow.add(new JLabel(""));

        JPanel requestsBlock = new JPanel(new BorderLayout(0, 6));
        requestsBlock.setOpaque(false);
        requestsBlock.add(headerRow, BorderLayout.NORTH);
        requestsBlock.add(requestsScroll, BorderLayout.CENTER);

        JLabel reqTitle = new JLabel("Pending requests");
        reqTitle.setFont(reqTitle.getFont().deriveFont(Font.BOLD, 13f));

        JPanel topSection = new JPanel(new BorderLayout(8, 8));
        topSection.setOpaque(false);
        topSection.add(reqTitle, BorderLayout.NORTH);
        topSection.add(requestsBlock, BorderLayout.CENTER);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(Color.WHITE);
        logArea.setBorder(BorderFactory.createLineBorder(new Color(0xE0, 0xE5, 0xEA)));

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(520, 110));

        JLabel logTitle = new JLabel("Activity log");
        logTitle.setFont(logTitle.getFont().deriveFont(Font.BOLD, 13f));

        JPanel logSection = new JPanel(new BorderLayout(8, 8));
        logSection.setOpaque(false);
        logSection.add(logTitle, BorderLayout.NORTH);
        logSection.add(logScroll, BorderLayout.CENTER);

        JPanel centerStack = new JPanel();
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));
        centerStack.setOpaque(false);
        centerStack.add(topSection);
        centerStack.add(Box.createVerticalStrut(12));
        centerStack.add(logSection);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setBackground(CARD_FILL);
        Dimension btn = new Dimension(120, 36);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(btn);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> refreshAll());
        JButton completionButton = new JButton("Completion Time");
        completionButton.setPreferredSize(new Dimension(160, 36));
        completionButton.setFocusPainted(false);
        completionButton.addActionListener(e -> showCompletionTime());
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(btn);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainFrame.showScreen(MainFrame.ROLE_SCREEN));
        buttonRow.add(refreshButton);
        buttonRow.add(completionButton);
        buttonRow.add(backButton);

        JPanel centerBlock = new JPanel(new BorderLayout(0, 10));
        centerBlock.setOpaque(false);
        centerBlock.add(centerStack, BorderLayout.CENTER);
        centerBlock.add(buttonRow, BorderLayout.SOUTH);
        card.add(centerBlock, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(card, gbc);

        refreshAll();
    }

    private void refreshAll() {
        rebuildRequestRows();
        loadLog();
    }

    private void rebuildRequestRows() {
        requestsListPanel.removeAll();
        List<PendingRequest> list = requestQueue.getAll();
        if (list.isEmpty()) {
            JLabel empty = new JLabel("(No pending requests)");
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            requestsListPanel.add(empty);
        } else {
            for (PendingRequest r : list) {
                requestsListPanel.add(requestRow(r));
                requestsListPanel.add(Box.createVerticalStrut(6));
            }
        }
        requestsListPanel.revalidate();
        requestsListPanel.repaint();
        statusLabel.setText("Pending: " + list.size());
    }

    private JPanel requestRow(PendingRequest r) {
        JPanel row = new JPanel(new GridLayout(1, 4, 8, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE0, 0xE5, 0xEA)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel typeLab = new JLabel(r.getType());
        JLabel sumLab = new JLabel(r.getSummary());
        JButton accept = new JButton("Accept");
        JButton reject = new JButton("Reject");
        accept.setFocusPainted(false);
        reject.setFocusPainted(false);

        accept.addActionListener(e -> {
            try {
                r.accept();
                if (r.getRequestData() instanceof Vehicle vehicle) {
                    logger.info(vehicle);
                } else if (r.getRequestData() instanceof Job job) {
                    logger.info(job);
                } else {
                    logger.info(r.getLogMessageOnAccept());
                }
                requestQueue.remove(r);
                rebuildRequestRows();
                loadLog();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "Could not accept request: " + ex.getMessage());
            }
        });

        reject.addActionListener(e -> {
            try {
                r.reject();
                requestQueue.remove(r);
                rebuildRequestRows();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "Could not reject request: " + ex.getMessage());
            }
        });

        row.add(typeLab);
        row.add(sumLab);
        row.add(accept);
        row.add(reject);
        return row;
    }

    private void loadLog() {
        logArea.setText("DatabaseLogger is active. Accepted vehicles and jobs are inserted into the database.");
        String when = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm:ss a"));
        statusLabel.setText("Pending: " + requestQueue.getAll().size() + " | " + when);
    }

    private void showCompletionTime() {
        List<Job> jobs = vcController.getActiveJobs();
        if (jobs.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No jobs in the queue yet.",
                    "Queue Completion Time",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        List<Integer> completion = vcController.computeCompletionTime();
        StringBuilder sb = new StringBuilder();
        sb.append("Job ID\tDuration(min)\tCompletion(min)\n");
        
        //This yields completion time in the form of a single value *NOT NEEDED*
        //int total = completion.isEmpty() ? 0 : completion.get(completion.size() - 1);
        sb.append("\nTotal completion time: ").append(completion).append(" minutes");

        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "Queue Completion Time",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
