import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    // Screen name constants
    public static final String ROLE_SCREEN = "role";
    public static final String OWNER_SCREEN = "owner";
    public static final String CLIENT_SCREEN = "client";

    private JPanel contentPanel;
    private CardLayout cardLayout;

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

        Logger logger = new FileLogger("logs.txt");

        contentPanel.add(createRoleSelectionPanel(), ROLE_SCREEN);
        contentPanel.add(new OwnerForm(this, logger).getPanel(), OWNER_SCREEN);
        contentPanel.add(new ClientForm(this, logger).getPanel(), CLIENT_SCREEN);

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

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JLabel label = new JLabel(
                "Select User Type",
                SwingConstants.CENTER
        );

        JButton ownerButton = new JButton("Vehicle Owner");
        JButton clientButton = new JButton("Client");

        // Navigation actions
        ownerButton.addActionListener(e ->
                showScreen(OWNER_SCREEN)
        );

        clientButton.addActionListener(e ->
                showScreen(CLIENT_SCREEN)
        );

        panel.add(label);
        panel.add(ownerButton);
        panel.add(clientButton);

        return panel;
    }
}