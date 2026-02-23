import javax.swing.*;
import java.awt.*;

public class IntroPanel extends JPanel {

    private JButton continueButton;
    private JTextArea introText;

    public IntroPanel(MainFrame mainFrame) {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        introText = new JTextArea(
                "Vehicle Cloud Real-Time System\n\n" +

                "This system is designed to allow vehicle owners and clients " +
                "to interact through a centralized cloud-based platform. " +
                "Vehicle owners must provide Owner ID, Vehicle Make, Vehicle Model, " +
                "Vehicle Year, and Residency Time (in minutes).\n\n" +

                "Clients must provide Client ID, Client Name, Job Duration, " +
                "and Job Deadline.\n\n" +

                "Depending on whether you are registering as an Owner or a Client, " +
                "the corresponding information will be required."
        );

        introText.setEditable(false);
        introText.setFocusable(false);
        introText.setLineWrap(true);
        introText.setWrapStyleWord(true);
        introText.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(introText);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        continueButton = new JButton("Continue");
        continueButton.addActionListener(e -> mainFrame.showScreen(MainFrame.ROLE_SCREEN));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(continueButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Resize listener to scale text and button
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizeComponents();
            }
        });
    }

    private void resizeComponents() {
        int width = getWidth();

        // Scale font based on width
        int textSize = Math.max(14, width / 50);
        int buttonSize = Math.max(14, width / 60);

        introText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, textSize));
        continueButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, buttonSize));
    }
}