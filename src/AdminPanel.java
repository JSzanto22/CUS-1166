import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
read onlyaa
 */
public class AdminPanel extends JPanel {

    public static final String LOG_PATH = "src/logs.txt";

    private static final Color APP_BG = new Color(0x6F, 0xA6, 0x8C);
    private static final Color CARD_FILL = new Color(0xF5, 0xF8, 0xF6);
    private static final Color CARD_BORDER = new Color(0xD7, 0xE3, 0xDD);

    private final VehicularCloudController vcController;
    private JTextArea logArea;
    private JLabel statusLabel;

    public AdminPanel(MainFrame mainFrame, VehicularCloudController vcController) {
        this.vcController = vcController;
        setLayout(new GridBagLayout());
        setBackground(APP_BG);
        setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setBackground(CARD_FILL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Admin – Activity log");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(new Color(0x26, 0x32, 0x38));
        card.add(title, BorderLayout.NORTH);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(0x55, 0x63, 0x6C));
        card.add(statusLabel, BorderLayout.SOUTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(Color.WHITE);
        logArea.setBorder(BorderFactory.createLineBorder(new Color(0xE0, 0xE5, 0xEA)));

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setPreferredSize(new Dimension(520, 200));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setBackground(CARD_FILL);
        Dimension btn = new Dimension(120, 36);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(btn);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadLog());
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
        centerBlock.add(scroll, BorderLayout.CENTER);
        centerBlock.add(buttonRow, BorderLayout.SOUTH);
        card.add(centerBlock, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(card, gbc);

        loadLog();
    }

    private void loadLog() {
        Path path = Path.of(LOG_PATH);
        try {
            if (!Files.isRegularFile(path)) {
                logArea.setText("(No log file yet. Submit an owner or client form to create one.)");
                statusLabel.setText("No file at " + LOG_PATH);
                return;
            }
            String text = Files.readString(path, StandardCharsets.UTF_8);
            logArea.setText(text);
            logArea.setCaretPosition(0);
            long lines = text.isEmpty() ? 0 : text.chars().filter(ch -> ch == '\n').count() + 1;
            String when = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm:ss a"));
            statusLabel.setText(lines + " line(s) · Last refreshed: " + when);
        } catch (IOException ex) {
            logArea.setText("Could not read log: " + ex.getMessage());
            statusLabel.setText("Error reading " + LOG_PATH);
        }
    }

    private void showCompletionTime() {
        int totalMinutes = vcController.computeCompletionTime();
        JOptionPane.showMessageDialog(
                this,
                "Total completion time (current queue): " + totalMinutes + " minutes",
                "Queue Completion Time",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
