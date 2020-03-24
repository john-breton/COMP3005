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

    // JComboBox Arrays
    String[] resultFilterArr = {"Price", "A-Z", "Z-A", "Year"};
    String[] searchFilterArr = {"Title", "Author", "Genre", "ISBN", "Publisher", "Year"};
    String[] reportChoiceArr = {"Sales v Expense", "Sales per Genre", "Sales per Author", "Sales per Publisher", "Sales per Month", "Sales per Year", "Expense per Month", "Expense per Year"};

    /* JComboBoxes */
    // User
    JComboBox<String> resultFilters = new JComboBox<>(resultFilterArr);
    JComboBox<String> searchFilters = new JComboBox<>(searchFilterArr);
    JComboBox<String> reportChoiceBox = new JComboBox<>(reportChoiceArr);

    /* JLabels -- Global to update users */
    // Login
    private final JLabel loginSuccess = new JLabel("");
    // Registration
    private final JLabel confirmRegistration = new JLabel(""); // TODO: add to regScreen()
    // Admin
    private final JLabel confirmNewBookAddition = new JLabel("");
    private final JLabel confirmNewPublisherAddition = new JLabel("");
    // User
    private final JLabel totalPrice = new JLabel("$0.00");

    /* JTextFields -- Global in order for methods to access */
    // Login
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    // Registration
    private final JTextField billStreetNumTF = new JTextField(20), shipStreetNumTF = new JTextField(20);
    private final JTextField billStreetNameTF = new JTextField(), shipStreetNameTF = new JTextField();
    private final JTextField billApartmentTF = new JTextField(), shipApartmentTF = new JTextField();
    private final JTextField billCityTF = new JTextField(), shipCityTF = new JTextField();
    private final JTextField billProvinceTF = new JTextField(), shipProvinceTF = new JTextField();
    private final JTextField billCountryTF = new JTextField(), shipCountryTF = new JTextField();
    private final JTextField billPostalCodeTF = new JTextField(), shipPostalCodeTF = new JTextField();
    private final JTextField newUsernameTF = new JTextField(20);
    private final JTextField newPasswordTF = new JTextField();
    private final JTextField confirmPasswordTF = new JTextField();
    private final JTextField firstNameTF = new JTextField();
    private final JTextField lastNameTF = new JTextField();
    private final JTextField emailTF = new JTextField();
    // Admin
    private final JTextField newISBNTF = new JTextField(15);
    private final JTextField newBookTitleTF = new JTextField(15);
    private final JTextField newBookVersionTF = new JTextField(15);
    private final JTextField newBookGenreTF = new JTextField(15);
    private final JTextField newBookNumPagesTF = new JTextField(15);
    private final JTextField newBookPriceTF = new JTextField(15);
    private final JTextField newBookRoyaltyTF = new JTextField(15);
    private final JTextField newBookStockTF = new JTextField("0", 15);
    private final JTextField newBookAuthorFNTF = new JTextField(15);
    private final JTextField newBookAuthorLNTF = new JTextField(15);
    private final JTextField newBookPublisherTF = new JTextField(15);
    private final JTextField newBookYearTF = new JTextField(15);
    // User
    private final JTextField searchText = new JTextField();

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
        f.setPreferredSize(new Dimension(300, 300));
        c.removeAll();
        usernameField.setText("");
        passwordField.setText("");
        loginSuccess.setText("");

        c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));

        /* Component declarations */
        // JButtons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton lookupOrderButton = new JButton("Order Lookup");

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
        f.setPreferredSize(new Dimension(600, 750));
        c.removeAll();

        /* Component declarations */
        // JTextFields
        // Fields

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
        f.pack();
        f.setLocationRelativeTo(null);
    }

    /**
     * Display the screen for a librarian...possible to display an option for user screen? (maybe the librarian wants to shop ;) )
     * SHOULD ALSO BE ABLE TO SIGN UP A NEW ADMIN
     */
    public void adminScreen() {
        f.setPreferredSize(new Dimension(800, 800));
        c.removeAll();

        // Panels and JTabbedPanes
        JPanel addNewBookPanel = new JPanel(new GridBagLayout());
        JPanel addNewPublisherPanel = new JPanel(new GridBagLayout());
        JPanel editBookStockPanel = new JPanel();
        JPanel reportPanel = new JPanel();
        JPanel newUserPanel = new JPanel();
        JTabbedPane adminView = new JTabbedPane();
        JTabbedPane newEntitiesPanel = new JTabbedPane(JTabbedPane.BOTTOM);

        // JButtons
        JButton logoutAddBookButton = new JButton("Logout");
        JButton addBookButton = new JButton("Add Book");
        JButton logoutAddPublisherButton = new JButton("Logout");
        JButton addPublisherButton = new JButton("Add Publisher");
        JButton logoutAddUserButton = new JButton("Logout");
        JButton addUserButton = new JButton("Add User");

        JButton updateBookStockButton = new JButton("Update Stock");
        JButton generateReport = new JButton("Generate Report");

        // ActionListeners
        logoutAddBookButton.addActionListener(this);
        addBookButton.addActionListener(this);

        // JTextFields
        // See Fields

        // JLabels
        JLabel newBookLabel = new JLabel("Enter Book Information (required fields indicated by *): ");
        JLabel newISBNLabel = new JLabel("*ISBN: ");
        JLabel newBookTitleLabel = new JLabel("*Title: ");
        JLabel newBookVersionLabel = new JLabel("Version: ");
        JLabel newBookGenreLabel = new JLabel("Genre: ");
        JLabel newBookNumPagesLabel = new JLabel("Pg Count: ");
        JLabel newBookPriceLabel = new JLabel("*Price:");
        JLabel newBookRoyaltyLabel = new JLabel("*Royalty (%): ");
        JLabel newBookStockLabel = new JLabel("Stock:");
        JLabel newBookAuthorLabel = new JLabel("Author: ");
        JLabel newBookAuthorFNLabel = new JLabel("First Name: ");
        JLabel newBookAuthorLNLabel = new JLabel("Last Name: ");
        JLabel newBookPublisherLabel = new JLabel("Publisher (be sure to add new publishers before adding books): ");
        JLabel newBookYearLabel = new JLabel("Year: ");
        JLabel accountingLabel = new JLabel("*Accounting: ");

        // Setup addBookPanel
        {
        GridBagConstraints bookCon = new GridBagConstraints();
        Dimension spacer = new Dimension(15, 15);
        bookCon.gridx = 0;
        bookCon.gridy = 0;
        addNewBookPanel.add(logoutAddBookButton, bookCon);

        bookCon.gridx = 5;
        bookCon.anchor = GridBagConstraints.FIRST_LINE_END;
        addNewBookPanel.add(addBookButton, bookCon);

        bookCon.gridx = 1;
        bookCon.gridy = 1;
        bookCon.gridwidth = 3;
        bookCon.anchor = GridBagConstraints.LINE_START;
        addNewBookPanel.add(newBookLabel, bookCon);

        bookCon.gridy = 2;
        bookCon.gridx = 1;
        bookCon.gridwidth = 1;
        addNewBookPanel.add(newBookTitleLabel, bookCon);
        bookCon.gridx = 2;
        addNewBookPanel.add(newBookTitleTF, bookCon);
        bookCon.gridx = 3;
        addNewBookPanel.add(newISBNLabel, bookCon);
        bookCon.gridx = 4;
        addNewBookPanel.add(newISBNTF, bookCon);

        bookCon.gridy = 3;
        bookCon.gridx = 1;
        addNewBookPanel.add(newBookVersionLabel, bookCon);
        bookCon.gridx = 2;
        addNewBookPanel.add(newBookVersionTF, bookCon);
        bookCon.gridx = 3;
        addNewBookPanel.add(newBookNumPagesLabel, bookCon);
        bookCon.gridx = 4;
        addNewBookPanel.add(newBookNumPagesTF, bookCon);

        bookCon.gridy = 4;
        bookCon.gridx = 1;
        addNewBookPanel.add(newBookGenreLabel, bookCon);
        bookCon.gridx = 2;
        addNewBookPanel.add(newBookGenreTF, bookCon);
        bookCon.gridx = 3;
        addNewBookPanel.add(newBookStockLabel, bookCon);
        bookCon.gridx = 4;
        addNewBookPanel.add(newBookStockTF, bookCon);

        bookCon.gridy = 5;
        bookCon.gridx = 1;
        addNewBookPanel.add(newBookYearLabel, bookCon);
        bookCon.gridx = 2;
        addNewBookPanel.add(newBookYearTF, bookCon);

        bookCon.gridy = 6;
        bookCon.gridx = 1;
        addNewBookPanel.add(Box.createRigidArea(spacer), bookCon);

        bookCon.gridy = 7;
        bookCon.gridx = 1;
        addNewBookPanel.add(accountingLabel, bookCon);

        bookCon.gridy = 8;
        bookCon.gridx = 1;
        addNewBookPanel.add(newBookPriceLabel, bookCon);
        bookCon.gridx = 2;
        addNewBookPanel.add(newBookPriceTF, bookCon);
        bookCon.gridx = 3;
        addNewBookPanel.add(newBookRoyaltyLabel, bookCon);
        bookCon.gridx = 4;
        addNewBookPanel.add(newBookRoyaltyTF, bookCon);

        bookCon.gridy = 9;
        bookCon.gridx = 1;
        addNewBookPanel.add(Box.createRigidArea(spacer), bookCon);

        bookCon.gridy = 10;
        bookCon.gridx = 1;
        addNewBookPanel.add(newBookAuthorLabel, bookCon);

        bookCon.gridy = 11;
        bookCon.gridx = 1;
        addNewBookPanel.add(newBookAuthorFNLabel, bookCon);
        bookCon.gridx = 2;
        addNewBookPanel.add(newBookAuthorFNTF, bookCon);
        bookCon.gridx = 3;
        addNewBookPanel.add(newBookAuthorLNLabel, bookCon);
        bookCon.gridx = 4;
        addNewBookPanel.add(newBookAuthorLNTF, bookCon);

        bookCon.gridy = 12;
        bookCon.gridx = 1;
        addNewBookPanel.add(Box.createRigidArea(spacer), bookCon);

        bookCon.gridy = 13;
        bookCon.gridwidth = 4;
        addNewBookPanel.add(newBookPublisherLabel, bookCon);
        bookCon.gridx = 2;
        bookCon.gridy = 14;
        bookCon.gridwidth = 3;
        bookCon.fill = GridBagConstraints.HORIZONTAL;
        addNewBookPanel.add(newBookPublisherTF, bookCon);

        bookCon.gridwidth = 2;
        bookCon.gridy = 1;
        bookCon.gridx = 5;
        bookCon.anchor = GridBagConstraints.LINE_END;
        confirmNewBookAddition.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
        addNewBookPanel.add(confirmNewBookAddition, bookCon);

        bookCon.gridy = 15; // shift everything to the top
        bookCon.gridx = 0;
        bookCon.weighty = 1.0;
        bookCon.gridwidth = 6;
        bookCon.anchor = GridBagConstraints.CENTER;
        bookCon.fill = GridBagConstraints.BOTH;
        Component glue = Box.createVerticalGlue();
        addNewBookPanel.add(glue, bookCon);
    }

        // Setup addPublisherPanel
        {
            GridBagConstraints pubCon = new GridBagConstraints();
            Dimension spacer = new Dimension(15, 15);
            pubCon.gridx = 0;
            pubCon.gridy = 0;
            addNewPublisherPanel.add(logoutAddPublisherButton, pubCon);

            pubCon.gridx = 5;
            pubCon.anchor = GridBagConstraints.FIRST_LINE_END;
            addNewPublisherPanel.add(addPublisherButton, pubCon);

            pubCon.gridx = 1;
            pubCon.gridy = 1;
            pubCon.gridwidth = 3;
            pubCon.anchor = GridBagConstraints.LINE_START;
            addNewPublisherPanel.add(new JLabel(), pubCon);

            pubCon.gridy = 2;
            pubCon.gridx = 1;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 2;
            addNewPublisherPanel.add(new JTextField(15), pubCon);
            pubCon.gridx = 3;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 4;
            addNewPublisherPanel.add(new JTextField(15), pubCon);

            pubCon.gridy = 3;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 2;
            addNewPublisherPanel.add(new JTextField(15), pubCon);
            pubCon.gridx = 3;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 4;
            addNewPublisherPanel.add(new JTextField(15), pubCon);

            pubCon.gridy = 4;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 2;
            addNewPublisherPanel.add(new JTextField(15), pubCon);
            pubCon.gridx = 3;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 4;
            addNewPublisherPanel.add(new JTextField(15), pubCon);

            pubCon.gridy = 5;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 2;
            addNewPublisherPanel.add(new JTextField(15), pubCon);

            pubCon.gridy = 6;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(Box.createRigidArea(spacer), pubCon);

            pubCon.gridy = 7;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(new JTextField(15), pubCon);

            pubCon.gridy = 8;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 2;
            addNewPublisherPanel.add(new JTextField(15), pubCon);
            pubCon.gridx = 3;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 4;
            addNewPublisherPanel.add(new JTextField(15), pubCon);

            pubCon.gridy = 9;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(Box.createRigidArea(spacer), pubCon);

            pubCon.gridy = 10;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(new JLabel(), pubCon);

            pubCon.gridy = 11;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 2;
            addNewPublisherPanel.add(new JTextField(15), pubCon);
            pubCon.gridx = 3;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 4;
            addNewPublisherPanel.add(new JTextField(15), pubCon);

            pubCon.gridy = 12;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(Box.createRigidArea(spacer), pubCon);

            pubCon.gridy = 13;
            pubCon.gridwidth = 4;
            addNewPublisherPanel.add(new JLabel(), pubCon);
            pubCon.gridx = 2;
            pubCon.gridy = 14;
            pubCon.gridwidth = 3;
            pubCon.fill = GridBagConstraints.HORIZONTAL;
            addNewPublisherPanel.add(new JTextField(15), pubCon);

            pubCon.gridwidth = 2;
            pubCon.gridy = 1;
            pubCon.gridx = 5;
            pubCon.anchor = GridBagConstraints.LINE_END;
            confirmNewBookAddition.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
            addNewPublisherPanel.add(confirmNewPublisherAddition, pubCon);

            pubCon.gridy = 15; // shift everything to the top
            pubCon.gridx = 0;
            pubCon.weighty = 1.0;
            pubCon.gridwidth = 6;
            pubCon.anchor = GridBagConstraints.CENTER;
            pubCon.fill = GridBagConstraints.BOTH;
            Component glue = Box.createVerticalGlue();
            addNewPublisherPanel.add(glue, pubCon);
        }

        // Setup Tabbed Panes
        adminView.addTab("Add Stuff", null, newEntitiesPanel, "Add a new book or publisher");
        adminView.addTab("Edit Stuff", null, editBookStockPanel, "Edit the stock of a book");
        adminView.addTab("Reports", null, reportPanel, "Generate sales reports");
        newEntitiesPanel.addTab("Add Book", null, addNewBookPanel, "Add a new book to inventory");
        newEntitiesPanel.addTab("Add Publisher", null, addNewPublisherPanel, "Add a new publisher to database");
        newEntitiesPanel.addTab("Add User", null, newUserPanel, "Add a new user to the database");

        c.add(adminView);
        f.pack();
        f.setLocationRelativeTo(null);
    }

    /**
     * Display the screen for a user
     */
    public void userScreen() {
        f.setPreferredSize(new Dimension(798, 850));
        c.removeAll();

        // Dimensions
        Dimension addRemoveButtonDimensions = new Dimension(25,25);
        Dimension searchResultDimension = new Dimension(500, c.getHeight());
        Dimension cartDimension = new Dimension(c.getWidth() - (int)searchResultDimension.getWidth(), c.getHeight());

        /* Components */
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

        // ScrollPanes
        JScrollPane currentCart = new JScrollPane();
        JScrollPane searchResult = new JScrollPane();

        /* Setup Panels */
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
        f.pack();
        f.setLocationRelativeTo(null);
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
                case "Cancel Registration" -> loginScreen();
                case "Submit" -> System.out.println("Submitted");
                case "+" -> System.out.println("Item Added");
                case "-" -> System.out.println("Item removed");
                case "Checkout" -> System.out.println("Checking-out");
                case "Search" -> System.out.println("Searching");
                case "Logout" -> confirmLogout();
                case "Add Book" -> confirmNewBookAddition.setText("New Book Added");
                case "Order Lookup" -> System.out.println("Looking up order");
                default -> System.out.println("Error");
            }
        }
    }

    /**
     * Checks the username/ password combo. Currently only informs user if successful via JLabel
     *
     * @see super.lookForaLogin() for full implementation
     */
    private void login() {
        boolean[] validCred = super.lookForaLogin(usernameField.getText(), passwordField.getPassword());
        if (validCred[0]) {
            if(validCred[1]) {
                adminScreenChoice();
            }else userScreen();
        }
        else loginSuccess.setText("Login not successful. Please try again.");
    }

    /**
     * Confirms that the user wishes to logout
     */
    private void confirmLogout(){
        JButton logoutButton = new JButton("Logout");
        JButton cancelButton = new JButton("Cancel");

        Object[] options = {logoutButton, cancelButton};
        final JOptionPane areYouSure = new JOptionPane("Are you sure you want to logout?", JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
        final JDialog dialog = areYouSure.createDialog("Logout");
        dialog.setContentPane(areYouSure);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        logoutButton.addActionListener(e -> {
            loginScreen();
            dialog.setVisible(false);
        });
        cancelButton.addActionListener(e -> dialog.setVisible(false));

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Gives the admin user the choice of where to go
     */
    private void adminScreenChoice(){
        JButton userButton = new JButton("Customer View");
        JButton adminButton = new JButton("Administrative View");

        Object[] options = {adminButton, userButton};
        final JOptionPane screenChoice = new JOptionPane("Which screen would you like to be directed to?", JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
        final JDialog dialog = screenChoice.createDialog("Admin");
        dialog.setContentPane(screenChoice);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        userButton.addActionListener(e -> {
            userScreen();
            dialog.setVisible(false);
        });
        adminButton.addActionListener(e -> {
            adminScreen();
            dialog.setVisible(false);
        });

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
