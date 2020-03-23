import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LookAttaBook extends LookForaBook implements ActionListener {
    JFrame f = new JFrame("LookInnaBook");
    Container c = f.getContentPane();

    private JLabel loginSuccess = new JLabel("");
    private JTextField usernameField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);

    public LookAttaBook() {
        loginScreen();
    }

    /**
     * Displays a screen for users to login. VERY basic right now
     */
    public void loginScreen() {
        // clear GUI to load new contents
        // seriously inefficient, but I'm lazy
        f.setPreferredSize(new Dimension(300, 300));
        f.setLocationRelativeTo(null);
        c.removeAll();
        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

        // Login Page
        JPanel mainLoginPage = new JPanel();
        mainLoginPage.setLayout(new BoxLayout(mainLoginPage, BoxLayout.PAGE_AXIS));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");

        // username panel
        JPanel usernamePanel = new JPanel();
        usernamePanel.setMaximumSize(new Dimension(300, 20));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        // password panel
        JPanel passwordPanel = new JPanel();
        passwordPanel.setMaximumSize(new Dimension(300, 20));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // login options
        JPanel loginPanel = new JPanel();
        loginPanel.setMinimumSize(new Dimension(300, 20));
        loginPanel.add(loginSuccess);
        loginPanel.add(registerButton);
        loginPanel.add(loginButton);

        // setup ActionListeners
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        // add to page
        mainLoginPage.add(Box.createVerticalGlue());
        mainLoginPage.add(usernamePanel);
        mainLoginPage.add(passwordPanel);
        mainLoginPage.add(loginPanel);

        c.add(mainLoginPage);

        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setResizable(false);
    }

    /**
     * Display the screen for registration of a new customer.
     */
    private void regScreen() {
        // clear GUI in order to reload
        // seriously inefficient, but I'm lazy
        f.setSize(new Dimension(600, 750));
        f.setLocationRelativeTo(null);
        c.removeAll();

        // Registration Page
        JPanel regPage = new JPanel();
        regPage.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        // user info labels/ text fields
        JLabel newUsernameLabel = new JLabel("*Username: ");
        JLabel newPasswordLabel = new JLabel("*Password: ");
        JLabel confirmPasswordLabel = new JLabel("*Confirm Password: ");
        JLabel firstNameLabel = new JLabel("*First Name: ");
        JLabel lastNameLabel = new JLabel("*Last Name: ");
        JLabel emailLabel = new JLabel("*Email: ");

        JTextField newUsernameTF = new JTextField(20);
        JTextField newPasswordTF = new JTextField();
        JTextField confirmPasswordTF = new JTextField();
        JTextField firstNameTF = new JTextField();
        JTextField lastNameTF = new JTextField();
        JTextField emailTF = new JTextField();

        // Shipping info labels/ text fields
        JLabel shippingAddressLabel = new JLabel("Shipping Address: ");
        JLabel shippingStreetNumLabel = new JLabel("*Street Number: ");
        JLabel shippingStreetNameLabel = new JLabel("*Street Name: ");
        JLabel shippingApartmentLabel = new JLabel("Apartment: ");
        JLabel shippingCityLabel = new JLabel("*City: ");
        JLabel shippingProvinceLabel = new JLabel("*Province: ");
        JLabel shippingCountryLabel = new JLabel("*Country: ");
        JLabel shippingPostalCodeLabel = new JLabel("*PostalCode: ");

        JTextField shipStreetNumTF = new JTextField(20);
        JTextField shipStreetNameTF = new JTextField();
        JTextField shipApartmentTF = new JTextField();
        JTextField shipCityTF = new JTextField();
        JTextField shipProvinceTF = new JTextField();
        JTextField shipCountryTF = new JTextField();
        JTextField shipPostalCodeTF = new JTextField();

        //Billing Info labels/ text fields
        JCheckBox billingSameAsShipping = new JCheckBox("Billing Address is the same as Shipping Address");

        JLabel billingAddressLabel = new JLabel("Billing Address: ");
        JLabel billStreetNumLabel = new JLabel("Shipping Address: ");
        JLabel billStreetNameLabel = new JLabel("Street Name: ");
        JLabel billApartmentLabel = new JLabel("Apartment: ");
        JLabel billCityLabel = new JLabel("City: ");
        JLabel billProvinceLabel = new JLabel("Province: ");
        JLabel billCountryLabel = new JLabel("Country: ");
        JLabel billPostalCodeLabel = new JLabel("PostalCode: ");

        JTextField billStreetNumTF = new JTextField(20);
        JTextField billStreetNameTF = new JTextField();
        JTextField billApartmentTF = new JTextField();
        JTextField billCityTF = new JTextField();
        JTextField billProvinceTF = new JTextField();
        JTextField billCountryTF = new JTextField();
        JTextField billPostalCodeTF = new JTextField();

        // Welcome message
        //TODO: Somehow center this damn message!
        JPanel welcome = new JPanel();
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.PAGE_AXIS));

        JLabel newUserWelcome1 = new JLabel("Welcome to LookInnaBook!");
        JLabel newUserWelcome2 = new JLabel("Enter your information in the space provided below.");
        JLabel newUserWelcome3 = new JLabel("Required fields are indicated with a \"*\".");

        welcome.add(newUserWelcome1);
        welcome.add(newUserWelcome2);
        welcome.add(newUserWelcome3);

        // Shipping Address Panel
        JPanel shipAdd = new JPanel();
        shipAdd.setLayout(new GridLayout(0, 2));
        shipAdd.add(shippingAddressLabel);
        shipAdd.add(Box.createRigidArea(new Dimension(10, 10)));
        shipAdd.add(shippingStreetNumLabel);
        shipAdd.add(shipStreetNumTF);
        shipAdd.add(shippingStreetNameLabel);
        shipAdd.add(shipStreetNameTF);
        shipAdd.add(shippingApartmentLabel);
        shipAdd.add(shipApartmentTF);
        shipAdd.add(shippingCityLabel);
        shipAdd.add(shipCityTF);
        shipAdd.add(shippingProvinceLabel);
        shipAdd.add(shipProvinceTF);
        shipAdd.add(shippingCountryLabel);
        shipAdd.add(shipCountryTF);
        shipAdd.add(shippingPostalCodeLabel);
        shipAdd.add(shipPostalCodeTF);
        shipAdd.setPreferredSize(new Dimension(400, 200));

        // Billing Address Panel
        JPanel billAdd = new JPanel();
        billAdd.setLayout(new GridLayout(0, 2));
        billAdd.add(billingAddressLabel);
        billAdd.add(Box.createRigidArea(new Dimension(10, 10)));
        billAdd.add(billStreetNumLabel);
        billAdd.add(billStreetNumTF);
        billAdd.add(billStreetNameLabel);
        billAdd.add(billStreetNameTF);
        billAdd.add(billApartmentLabel);
        billAdd.add(billApartmentTF);
        billAdd.add(billCityLabel);
        billAdd.add(billCityTF);
        billAdd.add(billProvinceLabel);
        billAdd.add(billProvinceTF);
        billAdd.add(billCountryLabel);
        billAdd.add(billCountryTF);
        billAdd.add(billPostalCodeLabel);
        billAdd.add(billPostalCodeTF);
        billAdd.setPreferredSize(new Dimension(400, 200));

        billStreetNumTF.setEnabled(false);
        billStreetNameTF.setEnabled(false);
        billApartmentTF.setEnabled(false);
        billCityTF.setEnabled(false);
        billProvinceTF.setEnabled(false);
        billCountryTF.setEnabled(false);
        billPostalCodeTF.setEnabled(false);

        // Information Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 2));
        infoPanel.add(newUsernameLabel);
        infoPanel.add(newUsernameTF);
        infoPanel.add(newPasswordLabel);
        infoPanel.add(newPasswordTF);
        infoPanel.add(confirmPasswordLabel);
        infoPanel.add(confirmPasswordTF);
        infoPanel.add(firstNameLabel);
        infoPanel.add(firstNameTF);
        infoPanel.add(lastNameLabel);
        infoPanel.add(lastNameTF);
        infoPanel.add(emailLabel);
        infoPanel.add(emailTF);
        infoPanel.setPreferredSize(new Dimension(400, 200));


        JButton cancelReg = new JButton("Cancel Registration");
        JButton submitReg = new JButton("Submit");

        // add panels
        con.anchor = GridBagConstraints.LINE_START;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridy = 0;
        con.gridx = 1;
        con.gridwidth = 2;
        con.insets = new Insets(5, 0, 5, 0);

        con.anchor = GridBagConstraints.CENTER;
        regPage.add(welcome, con);
        con.anchor = GridBagConstraints.LINE_END;

        con.anchor = GridBagConstraints.LINE_START;
        con.gridy = 1;
        regPage.add(infoPanel, con);
        con.gridy = 2;
        regPage.add(shipAdd, con);
        con.gridy = 3;
        billingSameAsShipping.setSelected(true);
        regPage.add(billingSameAsShipping, con);
        con.gridy = 4;
        regPage.add(billAdd, con);
        con.gridy = 5;
        con.gridx = 1;
        con.gridwidth = 1;
        regPage.add(cancelReg, con);
        con.gridx = 2;
        regPage.add(submitReg, con);

        // add action listeners
        cancelReg.addActionListener(this);
        submitReg.addActionListener(this);
        billingSameAsShipping.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (billingSameAsShipping.isSelected()) {
                    billStreetNumTF.setEnabled(false);
                    billStreetNameTF.setEnabled(false);
                    billApartmentTF.setEnabled(false);
                    billCityTF.setEnabled(false);
                    billProvinceTF.setEnabled(false);
                    billCountryTF.setEnabled(false);
                    billPostalCodeTF.setEnabled(false);
                } else {
                    billStreetNumTF.setEnabled(true);
                    billStreetNameTF.setEnabled(true);
                    billApartmentTF.setEnabled(true);
                    billCityTF.setEnabled(true);
                    billProvinceTF.setEnabled(true);
                    billCountryTF.setEnabled(true);
                    billPostalCodeTF.setEnabled(true);
                }
            }
        });

        c.add(regPage);
    }

    /**
     * Display the screen for a librarian...possible to display an option for user screen? (maybe the librarian wants to shop ;) )
     * SHOULD ALSO BE ABLE TO SIGN UP A NEW ADMIN
     */
    public void adminScreen() {
        // Admin Page
        // TODO: Setup Admin View
    }

    /**
     * Display the screen for a user
     */
    public void userScreen() {
        // User Page
        // TODO: Setup User View
    }

    /**
     * Implements ActionListeners for JButtons
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o instanceof JButton) {
            switch (((JButton) o).getText()) {
                case "Login":
                    login();
                    break;
                case "Register":
                    regScreen();
                    break;
                case "Cancel Registration":
                    loginScreen();
                    break;
                case "Submit":
                    System.out.print("Submitted");
                    break;
                default:
                    System.out.print("Error");
            }
        }
    }

    /**
     * Checks the username/ password combo. Currently only informs user if successful via JLabel
     *
     * @see super.lookForaLogin() for full implementation
     */
    private void login() {
        if (super.lookForaLogin(usernameField.getText(), passwordField.getText()))
            loginSuccess.setText("Login successful! Redirecting you now...");
        else loginSuccess.setText("Login not successful. Please try again.");
    }
}