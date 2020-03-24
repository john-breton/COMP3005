import com.jtattoo.plaf.graphite.GraphiteInternalFrameTitlePane;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The LookAttaBook class represents the front-end for the LookInnaBook application.
 * Users and admins interact with a GUI to register and login into their respective
 * application screens.
 *
 * @author Ryan Godfrey, John Breton
 * @version 1.0
 */
public class LookAttaBook extends LookForaBook implements ActionListener {
    private static final ImageIcon WINDOW_ICON = new ImageIcon(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("logo.png")).getImage());
    final JFrame f = new JFrame("LookInnaBook");
    final Container c = f.getContentPane();

    private final JLabel loginSuccess = new JLabel("");
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);

    public LookAttaBook() {
        // If you want to disable dark mode, this is the code for it. You can just remove it.
        // I haven't decided if it even looks good yet.
        // I decided I don't like it.
        /*try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }*/
        f.setIconImage(WINDOW_ICON.getImage());
        UIManager.getLookAndFeelDefaults().put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        loginScreen();
    }

    /**
     * Displays a screen for users to login. Minimalistic by design.
     */
    private void loginScreen() {
        // Clear GUI to load new contents
        // seriously inefficient, but I'm lazy
        // Nah this works well.
        f.setPreferredSize(new Dimension(300, 300));
        f.setLocationRelativeTo(null);
        c.removeAll();
        usernameField.setText("");
        passwordField.setText("");
        loginSuccess.setText("");

        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

        /* Component declarations */
        // JButtons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

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

        // Login Page
        mainLoginPage.setLayout(new BoxLayout(mainLoginPage, BoxLayout.PAGE_AXIS));

        // Username panel
        usernamePanel.setMaximumSize(new Dimension(300, 20));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        // Password panel
        passwordPanel.setMaximumSize(new Dimension(300, 20));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // Login options
        loginPanel.setMinimumSize(new Dimension(300, 20));
        loginPanel.add(loginSuccess);
        loginPanel.add(registerButton);
        loginPanel.add(loginButton);

        // Setup ActionListeners
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        // Add to page
        mainLoginPage.add(Box.createVerticalGlue());
        mainLoginPage.add(usernamePanel);
        mainLoginPage.add(passwordPanel);
        mainLoginPage.add(loginPanel);

        c.add(mainLoginPage);

        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getRootPane().setDefaultButton(loginButton);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setResizable(false);
    }

    /**
     * Display the screen for registration of a new customer.
     */
    private void regScreen() {
        // Clear GUI in order to reload
        // seriously inefficient, but I'm lazy
        // Just tell them to get faster computers.
        f.setSize(new Dimension(600, 750));
        f.setLocationRelativeTo(null);
        c.removeAll();

        /* Component declarations */
        // JTextFields
        JTextField billStreetNumTF = new JTextField(20), shipStreetNumTF = new JTextField(20);
        JTextField billStreetNameTF = new JTextField(), shipStreetNameTF = new JTextField();
        JTextField billApartmentTF = new JTextField(), shipApartmentTF = new JTextField();
        JTextField billCityTF = new JTextField(), shipCityTF = new JTextField();
        JTextField billProvinceTF = new JTextField(), shipProvinceTF = new JTextField();
        JTextField billCountryTF = new JTextField(), shipCountryTF = new JTextField();
        JTextField billPostalCodeTF = new JTextField(), shipPostalCodeTF = new JTextField();
        JTextField newUsernameTF = new JTextField(20);
        JTextField newPasswordTF = new JTextField();
        JTextField confirmPasswordTF = new JTextField();
        JTextField firstNameTF = new JTextField();
        JTextField lastNameTF = new JTextField();
        JTextField emailTF = new JTextField();

        // JLabels
        // User info
        JLabel newUsernameLabel = new JLabel("*Username: ");
        JLabel newPasswordLabel = new JLabel("*Password: ");
        JLabel confirmPasswordLabel = new JLabel("*Confirm Password: ");
        JLabel firstNameLabel = new JLabel("*First Name: ");
        JLabel lastNameLabel = new JLabel("*Last Name: ");
        JLabel emailLabel = new JLabel("*Email: ");
        // User shipping address info
        JLabel shippingAddressLabel = new JLabel("Shipping Address");
        JLabel shippingStreetNumLabel = new JLabel("*Street Number: ");
        JLabel shippingStreetNameLabel = new JLabel("*Street Name: ");
        JLabel shippingApartmentLabel = new JLabel("Apartment: ");
        JLabel shippingCityLabel = new JLabel("*City: ");
        JLabel shippingProvinceLabel = new JLabel("*Province: ");
        JLabel shippingCountryLabel = new JLabel("*Country: ");
        JLabel shippingPostalCodeLabel = new JLabel("*Postal Code: ");
        // User billing address info
        JLabel billingAddressLabel = new JLabel("Billing Address");
        JLabel billStreetNumLabel = new JLabel("Street Number: ");
        JLabel billStreetNameLabel = new JLabel("Street Name: ");
        JLabel billApartmentLabel = new JLabel("Apartment: ");
        JLabel billCityLabel = new JLabel("City: ");
        JLabel billProvinceLabel = new JLabel("Province: ");
        JLabel billCountryLabel = new JLabel("Country: ");
        JLabel billPostalCodeLabel = new JLabel("Postal Code: ");
        // Welcome message
        JLabel newUserWelcome1 = new JLabel("Welcome to LookInnaBook!");
        JLabel newUserWelcome2 = new JLabel("Enter your information in the space provided below.");
        JLabel newUserWelcome3 = new JLabel("Required fields are indicated with a \"*\".");

        // JCheckBox
        JCheckBox billingSameAsShipping = new JCheckBox("Billing Address is the same as Shipping Address");

        // JPanels
        JPanel regPage = new JPanel();
        JPanel welcome = new JPanel();
        JPanel shipAdd = new JPanel();
        JPanel billAdd = new JPanel();
        JPanel infoPanel = new JPanel();

        // JButton
        JButton cancelReg = new JButton("Cancel Registration");
        JButton submitReg = new JButton("Submit");


        /* Component setup*/
        // JButton formatting
        cancelReg.setBackground(Color.WHITE);
        submitReg.setBackground(Color.WHITE);

        // Registration Page
        regPage.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        // Welcome message
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.PAGE_AXIS));
        welcome.add(newUserWelcome1);
        welcome.add(newUserWelcome2);
        welcome.add(newUserWelcome3);
        newUserWelcome1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        newUserWelcome2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        newUserWelcome3.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        // Shipping Address Panel
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
        // Disable the fields by default.
        billStreetNumTF.setEnabled(false);
        billStreetNameTF.setEnabled(false);
        billApartmentTF.setEnabled(false);
        billCityTF.setEnabled(false);
        billProvinceTF.setEnabled(false);
        billCountryTF.setEnabled(false);
        billPostalCodeTF.setEnabled(false);

        // Information Panel
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

        // Add panels
        con.anchor = GridBagConstraints.LINE_START;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridy = 0;
        con.gridx = 1;
        con.gridwidth = 2;
        con.insets = new Insets(5, 0, 5, 0);

        con.anchor = GridBagConstraints.CENTER;
        regPage.add(welcome, con);
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

        // Add action listeners
        cancelReg.addActionListener(this);
        submitReg.addActionListener(this);

        // Changed this to a lambda and simplified the logic.
        billingSameAsShipping.addActionListener(e -> {
            boolean sameAsBilling = !billingSameAsShipping.isSelected();
            billStreetNumTF.setEnabled(sameAsBilling);
            billStreetNameTF.setEnabled(sameAsBilling);
            billApartmentTF.setEnabled(sameAsBilling);
            billCityTF.setEnabled(sameAsBilling);
            billProvinceTF.setEnabled(sameAsBilling);
            billCountryTF.setEnabled(sameAsBilling);
            billPostalCodeTF.setEnabled(sameAsBilling);
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
        f.setMinimumSize(new Dimension(798, 850));
        f.setLocationRelativeTo(null);
        c.removeAll();

        // Arrays
        String[] resultFilterArr = {"Price", "A-Z", "Z-A", "Year"};
        String[] searchFilterArr = {"Title", "Author", "Genre", "ISBN", "Publisher", "Year"};

        // Dimensions
        Dimension addRemoveButtonDimensions = new Dimension(25,25);
        Dimension searchResultDimension = new Dimension(500, c.getHeight());
        Dimension cartDimension = new Dimension(c.getWidth() - (int)searchResultDimension.getWidth(), c.getHeight());

        // Panels and Panes
        JSplitPane userView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        JPanel searchAndResults = new JPanel();
        JPanel cartPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel checkoutPanel = new JPanel();

        // Labels
        JLabel searchLabel = new JLabel("Search: ");
        JLabel cartLabel = new JLabel("Cart: ");
        JLabel filterLabel = new JLabel("Sort by: ");
        JLabel totalPrice = new JLabel("$0.00");
        JLabel totalPriceLabel = new JLabel("Total Price: ");

        // Buttons
        JButton addToCart = new JButton("+");
        JButton removeFromCart = new JButton("-");
        JButton checkoutButton = new JButton("Checkout");
        JButton searchButton = new JButton("Search");
        JButton logoutButton = new JButton("Logout");

        // ActionListeners
        addToCart.addActionListener(this);
        removeFromCart.addActionListener(this);
        checkoutButton.addActionListener(this);
        searchButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Combo Boxes, ScrollPanes and Text Fields
        JComboBox resultFilters = new JComboBox(resultFilterArr);
        JComboBox searchFilters = new JComboBox(searchFilterArr);
        JScrollPane currentCart = new JScrollPane();
        JScrollPane searchResult = new JScrollPane();
        JTextField searchText = new JTextField();

        // Price panel
        addToCart.setPreferredSize(addRemoveButtonDimensions);
        pricePanel.add(addToCart);
        pricePanel.add(totalPriceLabel);
        pricePanel.add(totalPrice);
        removeFromCart.setPreferredSize(addRemoveButtonDimensions);
        pricePanel.add(removeFromCart);

        // Checkout Panel
        checkoutPanel.setLayout(new BoxLayout(checkoutPanel, BoxLayout.PAGE_AXIS));
        checkoutPanel.add(pricePanel);
        checkoutButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        checkoutPanel.add(checkoutButton);

        searchAndResults.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        // setup left side of window
        Insets leftEdge =  new Insets(5, 5, 5, 0);
        Insets everythingElse = new Insets(5, 0, 5, 0);

        con.insets = leftEdge;
        con.anchor = GridBagConstraints.LINE_START;
        con.weighty = 0.0;
        con.weightx = 0.0;
        con.gridx = 0;
        con.gridy = 0;
        searchAndResults.add(logoutButton, con);

        con.gridx = 0;
        con.gridy = 1;
        searchLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        searchAndResults.add(searchLabel, con);

        con.insets = everythingElse;
        con.gridx = 1;
        searchAndResults.add(searchFilters, con);

        con.fill = GridBagConstraints.HORIZONTAL;
        con.gridx = 2;
        searchText.setPreferredSize(new Dimension(200, 20));
        searchAndResults.add(searchText, con);

        con.fill = GridBagConstraints.NONE;
        con.gridx = 3;
        searchAndResults.add(searchButton, con);

        con.gridx = 0;
        con.gridy = 2;
        con.insets = leftEdge;
        con.fill = GridBagConstraints.NONE;
        filterLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        searchAndResults.add(filterLabel, con);

        con.gridx = 1;
        con.insets = everythingElse;
        searchAndResults.add(resultFilters, con);

        con.gridx = 0;
        con.gridy = 3;
        con.weighty = 1.0;
        con.weightx = 1.0;
        con.gridwidth = 4;
        con.insets = new Insets(5, 5, 5, 5);
        con.fill = GridBagConstraints.BOTH;
        con.anchor = GridBagConstraints.CENTER;
        searchAndResults.add(searchResult, con);

        searchAndResults.setMinimumSize(searchResultDimension);

        // setup right side of window
        cartPanel.setLayout(new BorderLayout());
        cartLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        cartPanel.add(cartLabel, BorderLayout.PAGE_START);
        cartPanel.add(checkoutPanel, BorderLayout.PAGE_END);
        cartPanel.add(currentCart, BorderLayout.CENTER);
        cartPanel.setPreferredSize(cartDimension);

        userView.resetToPreferredSizes();
        userView.setLeftComponent(searchAndResults);
        userView.setRightComponent(cartPanel);


        c.add(userView);

    }

    /**
     * Implements ActionListeners for JButtons
     *
     * @param e The ActionEvent that was triggered via a JButton.
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o instanceof JButton) {
            switch (((JButton) o).getText()) {
                case "Login" -> login();
                case "Register" -> regScreen();
                case "Cancel Registration", "Logout" -> loginScreen();
                case "Submit" -> System.out.println("Submitted");
                case "+" -> System.out.println("Item Added");
                case "-" -> System.out.println("Item removed");
                case "Checkout" -> System.out.println("Checking-out");
                case "Search" -> System.out.println("Searching");
                default -> System.out.print("Error");
            }
        }
    }

    /**
     * Checks the username/ password combo. Currently only informs user if successful via JLabel
     *
     * @see super.lookForaLogin() for full implementation
     */
    private void login() {
        boolean[] validCred = super.lookForaLogin(usernameField.getText(), passwordField.getText());
        if (validCred[0]) {
            if(validCred[1]) {
                adminScreen();
                System.out.print("Hey Admin");
            }else userScreen();
        }
        else loginSuccess.setText("Login not successful. Please try again.");
    }
}