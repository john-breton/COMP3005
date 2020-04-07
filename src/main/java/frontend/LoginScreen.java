package frontend;

import backend.DatabaseQueries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame implements ActionListener {

    public LoginScreen() {
        Container c = this.getContentPane();
        // Clear GUI to load new contents
        this.setPreferredSize(new Dimension(300, 300));
        if (this.getJMenuBar() != null) this.getJMenuBar().setVisible(false);
        c.removeAll();
        FrontEndUtilities.usernameField.setText("");
        FrontEndUtilities.passwordField.setText("");
        FrontEndUtilities.loginSuccess.setText("");

        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

        /* Component declarations */
        // JButtons
        JButton loginButton = FrontEndUtilities.formatButton("Login");
        JButton registerButton = FrontEndUtilities.formatButton("Register");
        JButton lookupOrderButton = FrontEndUtilities.formatButton("Order Lookup");

        // JLabels
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");

        // JPanels
        JPanel mainLoginPage = new JPanel();
        JPanel usernamePanel = new JPanel();
        JPanel passwordPanel = new JPanel();
        JPanel loginPanel = new JPanel();


        /* Component setup */
        // JButton formatting
        loginButton.setBackground(Color.WHITE);
        registerButton.setBackground(Color.WHITE);
        lookupOrderButton.setBackground(Color.WHITE);

        // Login Page
        mainLoginPage.setLayout(new BoxLayout(mainLoginPage, BoxLayout.PAGE_AXIS));

        // Username panel
        usernamePanel.setMaximumSize(new Dimension(300, 20));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(FrontEndUtilities.usernameField);

        // Password panel
        passwordPanel.setMaximumSize(new Dimension(300, 20));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(FrontEndUtilities.passwordField);

        // Login options
        loginPanel.setMinimumSize(new Dimension(300, 20));
        loginPanel.add(FrontEndUtilities.loginSuccess);
        loginPanel.add(registerButton);
        loginPanel.add(loginButton);
        loginPanel.add(lookupOrderButton);

        // Setup ActionListeners
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        lookupOrderButton.addActionListener(this);

        // Add to page
        mainLoginPage.add(Box.createVerticalGlue());
        mainLoginPage.add(usernamePanel);
        mainLoginPage.add(passwordPanel);
        mainLoginPage.add(loginPanel);

        c.add(mainLoginPage);

        this.getRootPane().setDefaultButton(loginButton);
        FrontEndUtilities.configureFrame(this);
    }

    /**
     * Gives the admin user the choice of where to go.
     */
    private void adminScreenChoice() {
        JButton userButton = FrontEndUtilities.formatButton("Customer View");
        JButton adminButton = FrontEndUtilities.formatButton("Administrative View");

        Object[] options = {adminButton, userButton};
        final JOptionPane screenChoice = new JOptionPane("Which screen would you like to be directed to?", JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
        final JDialog dialog = screenChoice.createDialog("Admin");
        dialog.setContentPane(screenChoice);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        userButton.addActionListener(e -> {
            this.dispose();
            new UserScreen(FrontEndUtilities.usernameField.getText());
            dialog.setVisible(false);
        });
        adminButton.addActionListener(e -> {
            this.dispose();
            new AdminScreen(FrontEndUtilities.usernameField.getText());
            dialog.setVisible(false);
        });

        dialog.setIconImage(FrontEndUtilities.WINDOW_ICON.getImage());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Checks the username/ password combo. Currently only informs user if successful via JLabel
     *
     * @see super.lookForaLogin() for full implementation
     */
    private void login() {
        boolean[] validCred = DatabaseQueries.lookForaLogin(FrontEndUtilities.usernameField.getText(), FrontEndUtilities.passwordField.getPassword());
        if (validCred[0]) {
            if (validCred[1]) {
                adminScreenChoice();
            } else {
                new UserScreen(FrontEndUtilities.usernameField.getText());
            }
            this.dispose();
        } else FrontEndUtilities.loginSuccess.setText("Login not successful. Please try again.");
    }

    /**
     * Implements ActionListeners for GUI components
     *
     * @param e The ActionEvent that was triggered via a JButton.
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        switch (((JButton) o).getText()) {
            case "Login" -> login();
            case "Register" -> {
                this.dispose();
                new RegistrationScreen();
            } // Login screen
            case "Order Lookup" -> {
                this.dispose();
                new LookupOrderScreen();
            }
            default -> System.out.println("Error");
        }
    }
}
