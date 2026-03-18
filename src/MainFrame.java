import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    // Screen name constants
    public static final String ROLE_SCREEN = "role";
    public static final String OWNER_SCREEN = "owner";
    public static final String CLIENT_SCREEN = "client";
    public static final String INTRO_SCREEN = "intro";
    private static final Color APP_BG = new Color(0x6F, 0xA6, 0x8C); // #6FA68C
    private static final Font APP_FONT = pickAppFont();

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {

        installGlobalFont(APP_FONT);

        // Frame setup
        setTitle("Vehicular Cloud Console");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(APP_BG);

        // Header
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

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(APP_BG);

        JLabel label = new JLabel("Select User Type", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));

        JButton ownerButton = new JButton("Vehicle Owner");
        JButton clientButton = new JButton("Client");
        Dimension buttonSize = new Dimension(200, 40);
        ownerButton.setPreferredSize(buttonSize);
        clientButton.setPreferredSize(buttonSize);
        ownerButton.setForeground(Color.BLACK);
        clientButton.setForeground(Color.BLACK);
        ownerButton.setBackground(Color.WHITE);
        clientButton.setBackground(Color.WHITE);
        ownerButton.setOpaque(true);
        clientButton.setOpaque(true);
        ownerButton.setContentAreaFilled(true);
        clientButton.setContentAreaFilled(true);
        ownerButton.setFocusPainted(false);
        clientButton.setFocusPainted(false);

        // Navigation actions
        ownerButton.addActionListener(e ->
                showScreen(OWNER_SCREEN)
        );

        clientButton.addActionListener(e ->
                showScreen(CLIENT_SCREEN)
        );

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

        return panel;
    }
}