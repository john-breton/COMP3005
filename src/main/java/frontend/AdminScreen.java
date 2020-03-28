package frontend;


import backend.DatabaseQueries;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class AdminScreen extends JFrame implements ActionListener, ChangeListener {
    private final JCheckBox billingSameAsShipping = new JCheckBox("Billing Address is the same as Shipping Address");
    private final JTextField billStreetNumTF = new JTextField(),
            billStreetNameTF = new JTextField(),
            billApartmentTF = new JTextField(10),
            billCityTF = new JTextField(),
            billCountryTF = new JTextField(),
            billPostalCodeTF = new JTextField();
    /* JComboBoxes */
    // User
    final JComboBox<String> billProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr),
            pubProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr),
            shippingAdminProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr),
            billAdminProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr),
            editShippingProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr),
            editBillProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr);
    final JCheckBox isUserAdminCB = new JCheckBox("Is the user an admin?");
    final JCheckBox editBillingSameAsShipping = new JCheckBox("Billing Address is the same as Shipping Address");
    // AdminScreen
    // addBook
    private final JTextField newISBNTF = new JTextField(15),
            newBookTitleTF = new JTextField(15),
            newBookVersionTF = new JTextField(15),
            newBookGenreTF = new JTextField(15),
            newBookNumPagesTF = new JTextField(15),
            newBookPriceTF = new JTextField(15),
            newBookRoyaltyTF = new JTextField(15),
            newBookStockTF = new JTextField("0", 15),
            newBookAuthorFNTF = new JTextField(15),
            newBookAuthorLNTF = new JTextField(15),
            newBookPublisherTF = new JTextField(15),
            newBookYearTF = new JTextField(15);
    // addPublisher
    private final JTextField newPublisherNameTF = new JTextField(15),
            newPublisherStreetNumTF = new JTextField(5),
            newPublisherStreetNameTF = new JTextField(5),
            newPublisherApartmentTF = new JTextField(5),
            newPublisherCityTF = new JTextField(15),
            newPublisherCountryTF = new JTextField(15),
            newPublisherPostalCodeTF = new JTextField(15),
            newPublisherEmailTF = new JTextField(15),
            newPublisherPhoneTF = new JTextField(15),
            newPublisherBankAccountTF = new JTextField(15);
    // addUser
    // Admin info
    private final JTextField newAdminUsernameTF = new JTextField(15),
            firstNameAdminTF = new JTextField(15),
            lastNameAdminTF = new JTextField(15),
            emailAdminTF = new JTextField(15),
            salaryAdminTF = new JTextField(15);
    private final JPasswordField newAdminPasswordTF = new JPasswordField(15),
            confirmAdminPasswordTF = new JPasswordField(15);
    // Admin shipping address info (can be EMPTY)
    private final JTextField shippingAdminStreetNumTF = new JTextField(15),
            shippingAdminStreetNameTF = new JTextField(15),
            shippingAdminApartmentTF = new JTextField(15),
            shippingAdminCityTF = new JTextField(15),
            shippingAdminCountryTF = new JTextField(15),
            shippingAdminPostalCodeTF = new JTextField(15),
    // Admin billing address info
    billAdminStreetNumTF = new JTextField(15),
            billAdminStreetNameTF = new JTextField(15),
            billAdminApartmentTF = new JTextField(15),
            billAdminCityTF = new JTextField(15),
            billAdminCountryTF = new JTextField(15),
            billAdminPostalCodeTF = new JTextField(15);
    //editBook
    private final JTextField editBookSearchTF = new JTextField(15),
            editBookTitleTF = new JTextField(15),
            editBookVersionTF = new JTextField(15),
            editBookGenreTF = new JTextField(15),
            editBookNumPagesTF = new JTextField(15),
            editBookPriceTF = new JTextField(15),
            editBookRoyaltyTF = new JTextField(15),
            editBookStockTF = new JTextField("0", 15),
            editBookAuthorFNTF = new JTextField(15),
            editBookAuthorLNTF = new JTextField(15),
            editBookPublisherTF = new JTextField(15),
            editBookYearTF = new JTextField(15);
    // editUser
    private final JTextField editFirstNameTF = new JTextField(15),
            editLastNameTF = new JTextField(15),
            editEmailTF = new JTextField(15),
            editSalaryTF = new JTextField(15),
            editUserSearchTF = new JTextField(15);
    private final JPasswordField editPasswordTF = new JPasswordField(15),
            confirmEditPasswordTF = new JPasswordField(15);
    // Admin shipping address info (can be EMPTY)
    private final JTextField editShippingStreetNumTF = new JTextField(15),
            editShippingStreetNameTF = new JTextField(15),
            editShippingApartmentTF = new JTextField(15),
            editShippingCityTF = new JTextField(15),
            editShippingCountryTF = new JTextField(15),
            editShippingPostalCodeTF = new JTextField(15),
    // Admin billing address info
    editBillStreetNumTF = new JTextField(15),
            editBillStreetNameTF = new JTextField(15),
            editBillApartmentTF = new JTextField(15),
            editBillCityTF = new JTextField(15),
            editBillCountryTF = new JTextField(15),
            editBillPostalCodeTF = new JTextField(15);
    // Admin
    private final JLabel confirmNewBookAddition = new JLabel("", JLabel.CENTER),
            confirmNewPublisherAddition = new JLabel("", JLabel.CENTER),
            confirmAdminReg = new JLabel("", JLabel.CENTER),
            addUserErrorLabel = new JLabel("", JLabel.CENTER),
            addBookErrorLabel = new JLabel("", JLabel.CENTER),
            addPublisherErrorLabel = new JLabel("", JLabel.CENTER),
            editUserErrorLabel = new JLabel("", JLabel.CENTER),
            currentUserNameLabel = new JLabel(""),
            confirmUserEditLabel = new JLabel("", JLabel.CENTER),
            editBookErrorLabel = new JLabel("", JLabel.CENTER),
            currentISBNLabel = new JLabel("Current ISBN Goes Here"),
            confirmBookEditLabel = new JLabel("", JLabel.CENTER);

    public AdminScreen() {
        Container c = this.getContentPane();
        this.setPreferredSize(new Dimension(950, 800));
        c.removeAll();

        /* JMenu Setup */
        JMenuBar adminMenuBar = new JMenuBar();
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem switchToUserScreen = new JMenuItem("Switch to User View");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        adminMenuBar.add(adminMenu);
        adminMenu.add(switchToUserScreen);
        adminMenu.addSeparator();
        adminMenu.add(logoutMenuItem);
        switchToUserScreen.addActionListener(this);
        logoutMenuItem.addActionListener(this);

        /* JTabbedPanes */
        JTabbedPane adminView = new JTabbedPane();
        JTabbedPane newEntitiesPanel = new JTabbedPane(JTabbedPane.BOTTOM);
        JTabbedPane editEntitiesPanel = new JTabbedPane(JTabbedPane.BOTTOM);

        // Setup Tabbed Panes
        adminView.addTab("Add Stuff", null, newEntitiesPanel, "Add a new book or publisher");
        adminView.addTab("Edit Stuff", null, editEntitiesPanel, "Edit the stock of a book");
        adminView.addTab("Reports", null, reportPanel(), "Generate sales reports");
        newEntitiesPanel.addTab("Add Book", null, addBook(), "Add a new book to inventory");
        newEntitiesPanel.addTab("Add Publisher", null, addPublisher(), "Add a new publisher to database");
        newEntitiesPanel.addTab("Add Users", null, addUser(), "Add a new user to the database");
        editEntitiesPanel.addTab("Edit Books", null, editBook(), "Edit properties of existing books");
        editEntitiesPanel.addTab("Edit User", null, editUser(), "Edit properties of existing users");

        /* ChangeListeners */
        adminView.addChangeListener(this);
        newEntitiesPanel.addChangeListener(this);
        editEntitiesPanel.addChangeListener(this);

        c.add(adminView);
        FrontEndUtilities.configureFrame(this);
    }

    /**
     * Clears all admin view fields and returns to default
     */
    private void defaultAdminViewFields() {
        // addBook
        newBookTitleTF.setText("");
        newISBNTF.setText("");
        newBookVersionTF.setText("");
        newBookNumPagesTF.setText("");
        newBookGenreTF.setText("");
        newBookStockTF.setText("");
        newBookYearTF.setText("");
        newBookPriceTF.setText("");
        newBookRoyaltyTF.setText("");
        newBookAuthorFNTF.setText("");
        newBookAuthorLNTF.setText("");
        newBookPublisherTF.setText("");
        addBookErrorLabel.setText("");
        confirmNewBookAddition.setText("");

        // addPublisher
        newPublisherNameTF.setText("");
        newPublisherEmailTF.setText("");
        newPublisherPhoneTF.setText("");
        newPublisherStreetNumTF.setText("");
        newPublisherStreetNameTF.setText("");
        newPublisherApartmentTF.setText("");
        newPublisherCityTF.setText("");
        newPublisherCountryTF.setText("");
        newPublisherPostalCodeTF.setText("");
        newPublisherBankAccountTF.setText("");
        pubProvinceCB.setSelectedIndex(0);
        addPublisherErrorLabel.setText("");
        confirmNewPublisherAddition.setText("");

        // addUser
        newAdminUsernameTF.setText("");
        newAdminPasswordTF.setText("");
        confirmAdminPasswordTF.setText("");
        firstNameAdminTF.setText("");
        lastNameAdminTF.setText("");
        emailAdminTF.setText("");
        salaryAdminTF.setText("");
        shippingAdminStreetNameTF.setText("");
        shippingAdminStreetNumTF.setText("");
        shippingAdminApartmentTF.setText("");
        shippingAdminCityTF.setText("");
        shippingAdminCountryTF.setText("");
        shippingAdminPostalCodeTF.setText("");
        billAdminStreetNameTF.setText("");
        billAdminStreetNumTF.setText("");
        billAdminApartmentTF.setText("");
        billAdminCityTF.setText("");
        billAdminCountryTF.setText("");
        billAdminPostalCodeTF.setText("");
        billAdminProvinceCB.setSelectedIndex(0);
        shippingAdminProvinceCB.setSelectedIndex(0);
        addUserErrorLabel.setText("");
        confirmUserEditLabel.setText("");

        // editBook
        editBookSearchTF.setText("");
        editBookTitleTF.setText("");
        currentISBNLabel.setText("");
        editBookVersionTF.setText("");
        editBookNumPagesTF.setText("");
        editBookGenreTF.setText("");
        editBookStockTF.setText("");
        editBookYearTF.setText("");
        editBookPriceTF.setText("");
        editBookRoyaltyTF.setText("");
        editBookAuthorFNTF.setText("");
        editBookAuthorLNTF.setText("");
        editBookPublisherTF.setText("");
        editBookErrorLabel.setText("");
        confirmBookEditLabel.setText("");

        // editUser
        isUserAdminCB.setSelected(false);
        editBillingSameAsShipping.setSelected(false);
        editUserSearchTF.setText("");
        currentUserNameLabel.setText("");
        editPasswordTF.setText("");
        confirmEditPasswordTF.setText("");
        editFirstNameTF.setText("");
        editLastNameTF.setText("");
        editEmailTF.setText("");
        editSalaryTF.setText("");
        editShippingStreetNameTF.setText("");
        editShippingStreetNumTF.setText("");
        editShippingApartmentTF.setText("");
        editShippingCityTF.setText("");
        editShippingCountryTF.setText("");
        editShippingPostalCodeTF.setText("");
        editBillStreetNameTF.setText("");
        editBillStreetNumTF.setText("");
        editBillApartmentTF.setText("");
        editBillCityTF.setText("");
        editBillCountryTF.setText("");
        editBillPostalCodeTF.setText("");
        editShippingProvinceCB.setSelectedIndex(0);
        editBillProvinceCB.setSelectedIndex(0);
        editUserErrorLabel.setText("");
        confirmUserEditLabel.setText("");

        // reports


        // Disable the fields by default.
        billingSameAsShipping.setSelected(true);
        billStreetNumTF.setEnabled(false);
        billStreetNameTF.setEnabled(false);
        billApartmentTF.setEnabled(false);
        billCityTF.setEnabled(false);
        billProvinceCB.setEnabled(false);
        billCountryTF.setEnabled(false);
        billPostalCodeTF.setEnabled(false);
        editBookTitleTF.setEnabled(false);
        currentISBNLabel.setEnabled(false);
        editBookVersionTF.setEnabled(false);
        editBookNumPagesTF.setEnabled(false);
        editBookGenreTF.setEnabled(false);
        editBookStockTF.setEnabled(false);
        editBookYearTF.setEnabled(false);
        editBookPriceTF.setEnabled(false);
        editBookRoyaltyTF.setEnabled(false);
        editBookAuthorFNTF.setEnabled(false);
        editBookAuthorLNTF.setEnabled(false);
        editBookPublisherTF.setEnabled(false);
        editBookErrorLabel.setEnabled(false);
        editBillStreetNumTF.setEnabled(false);
        editBillStreetNameTF.setEnabled(false);
        editBillApartmentTF.setEnabled(false);
        editBillCityTF.setEnabled(false);
        editBillProvinceCB.setEnabled(false);
        editBillCountryTF.setEnabled(false);
        editBillPostalCodeTF.setEnabled(false);
        editPasswordTF.setEnabled(false);
        confirmEditPasswordTF.setEnabled(false);
        editFirstNameTF.setEnabled(false);
        editLastNameTF.setEnabled(false);
        editEmailTF.setEnabled(false);
        editSalaryTF.setEnabled(false);
        editShippingStreetNumTF.setEnabled(false);
        editShippingStreetNameTF.setEnabled(false);
        editShippingApartmentTF.setEnabled(false);
        editShippingCityTF.setEnabled(false);
        editShippingCountryTF.setEnabled(false);
        editShippingPostalCodeTF.setEnabled(false);
        editBillingSameAsShipping.setEnabled(false);
        editShippingProvinceCB.setEnabled(false);

        isUserAdminCB.setEnabled(false);

    }

    /**
     * Creates the "Add Book" tab for the "Add Stuff" tab of the adminView
     *
     * @return addNewBookPanel for adminView
     */
    private JPanel addBook() {
        JPanel addNewBookPanel = new JPanel(new GridBagLayout());

        /* JButtons */
        JButton logoutAddBookButton = FrontEndUtilities.formatButton("Logout");
        JButton addBookButton = FrontEndUtilities.formatButton("Add Book");

        /* JLabels */
        JLabel newBookLabel = new JLabel("Enter Book Information (required fields indicated by *): "),
                newISBNLabel = new JLabel("*ISBN: ", JLabel.RIGHT), // this doesn't work and idk why
                newBookTitleLabel = new JLabel("*Title: "),
                newBookVersionLabel = new JLabel("Version: "),
                newBookGenreLabel = new JLabel("Genre: "),
                newBookNumPagesLabel = new JLabel("Pg Count: ", JLabel.RIGHT),
                newBookPriceLabel = new JLabel("*Price:"),
                newBookRoyaltyLabel = new JLabel("*Royalty (%): ", JLabel.RIGHT),
                newBookStockLabel = new JLabel("Stock:", JLabel.RIGHT),
                newBookAuthorLabel = new JLabel("Author: "),
                newBookAuthorFNLabel = new JLabel("First Name: "),
                newBookAuthorLNLabel = new JLabel("Last Name: ", JLabel.RIGHT),
                newBookPublisherLabel = new JLabel("Publisher (be sure to add new publishers before adding books): "),
                newBookYearLabel = new JLabel("Year: "),
                accountingLabel = new JLabel("*Accounting: ");

        /* ActionListeners */
        logoutAddBookButton.addActionListener(this);
        addBookButton.addActionListener(this);

        // Setup addBookPanel
        {
            GridBagConstraints bookCon = new GridBagConstraints();
            Dimension spacer = new Dimension(15, 15);
            bookCon.gridx = 0;
            bookCon.gridy = 0;
            addNewBookPanel.add(logoutAddBookButton, bookCon);

            bookCon.gridx = 1;
            bookCon.gridwidth = 4;
            bookCon.anchor = GridBagConstraints.CENTER;
            addBookErrorLabel.setForeground(Color.red);
            addNewBookPanel.add(addBookErrorLabel, bookCon);

            bookCon.gridx = 5;
            bookCon.gridwidth = 1;
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
            bookCon.gridwidth = 2;
            addNewBookPanel.add(accountingLabel, bookCon);

            bookCon.gridy = 8;
            bookCon.gridx = 1;
            bookCon.gridwidth = 1;
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
        return addNewBookPanel;
    }

    /**
     * Creates the "Add Publisher" tab for the "Add Stuff" tab of the adminView
     *
     * @return addNewPublisherPanel for adminView
     */
    private JPanel addPublisher() {
        JPanel addNewPublisherPanel = new JPanel(new GridBagLayout());

        /* JButtons */
        JButton logoutAddPublisherButton = FrontEndUtilities.formatButton("Logout");
        JButton addPublisherButton = FrontEndUtilities.formatButton("Add Publisher");

        /* JLabels */
        JLabel newPublisherLabel = new JLabel("Enter Publisher Information (required fields indicated by *): "),
                newPublisherNameLabel = new JLabel("*Name: "),
                newPublisherAddressLabel = new JLabel("*Address: "),
                newPublisherStreetNumLabel = new JLabel("*Street Number:"),
                newPublisherStreetNameLabel = new JLabel("*Street Name:", JLabel.RIGHT),
                newPublisherApartmentLabel = new JLabel("Apartment:", JLabel.RIGHT),
                newPublisherCityLabel = new JLabel("*City: "),
                newPublisherProvinceLabel = new JLabel("*Province: ", JLabel.RIGHT),
                newPublisherCountryLabel = new JLabel("*Country: "),
                newPublisherPostalCodeLabel = new JLabel("*Postal Code: ", JLabel.RIGHT),
                newPublisherEmailLabel = new JLabel("*Email: "),
                newPublisherPhoneLabel = new JLabel("Phone: ", JLabel.RIGHT),
                newPublisherBankAccountLabel = new JLabel("*Bank Account Number: ");

        /* ActionListeners */
        logoutAddPublisherButton.addActionListener(this);
        addPublisherButton.addActionListener(this);

        // Setup addPublisherPanel
        {
            GridBagConstraints pubCon = new GridBagConstraints();
            Dimension spacer = new Dimension(15, 15);
            pubCon.gridx = 0;
            pubCon.gridy = 0;
            pubCon.anchor = GridBagConstraints.FIRST_LINE_START;
            addNewPublisherPanel.add(logoutAddPublisherButton, pubCon);

            pubCon.gridx = 1;
            pubCon.gridwidth = 6;
            pubCon.anchor = GridBagConstraints.CENTER;
            addPublisherErrorLabel.setForeground(Color.red);
            addNewPublisherPanel.add(addPublisherErrorLabel, pubCon);

            pubCon.gridx = 7;
            pubCon.gridwidth = 1;
            pubCon.anchor = GridBagConstraints.FIRST_LINE_END;
            addNewPublisherPanel.add(addPublisherButton, pubCon);

            pubCon.gridx = 1;
            pubCon.gridy = 1;
            pubCon.weightx = 1.0;
            pubCon.gridwidth = 7;
            pubCon.anchor = GridBagConstraints.LINE_START;
            pubCon.fill = GridBagConstraints.HORIZONTAL;
            addNewPublisherPanel.add(newPublisherLabel, pubCon);

            pubCon.gridy = 2;
            pubCon.gridx = 1;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(newPublisherNameLabel, pubCon);
            pubCon.gridx = 2;
            pubCon.gridwidth = 5;
            addNewPublisherPanel.add(newPublisherNameTF, pubCon);

            pubCon.gridy = 3;
            pubCon.gridx = 1;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(newPublisherEmailLabel, pubCon);
            pubCon.gridx = 2;
            pubCon.gridwidth = 2;
            addNewPublisherPanel.add(newPublisherEmailTF, pubCon);
            pubCon.gridx = 4;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(newPublisherPhoneLabel, pubCon);
            pubCon.gridx = 5;
            pubCon.gridwidth = 2;
            addNewPublisherPanel.add(newPublisherPhoneTF, pubCon);

            pubCon.gridy = 4;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(Box.createRigidArea(spacer), pubCon);

            pubCon.gridy = 5;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(newPublisherAddressLabel, pubCon);

            pubCon.gridy = 6;
            pubCon.gridx = 1;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(newPublisherStreetNumLabel, pubCon);
            pubCon.gridx = 2;
            addNewPublisherPanel.add(newPublisherStreetNumTF, pubCon);
            pubCon.gridx = 3;
            addNewPublisherPanel.add(newPublisherStreetNameLabel, pubCon);
            pubCon.gridx = 4;
            addNewPublisherPanel.add(newPublisherStreetNameTF, pubCon);
            pubCon.gridx = 5;
            addNewPublisherPanel.add(newPublisherApartmentLabel, pubCon);
            pubCon.gridx = 6;
            addNewPublisherPanel.add(newPublisherApartmentTF, pubCon);

            pubCon.gridy = 7;
            pubCon.gridx = 1;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(newPublisherCityLabel, pubCon);
            pubCon.gridx = 2;
            pubCon.gridwidth = 2;
            addNewPublisherPanel.add(newPublisherCityTF, pubCon);
            pubCon.gridx = 4;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(newPublisherProvinceLabel, pubCon);
            pubCon.gridx = 5;
            pubCon.gridwidth = 2;
            addNewPublisherPanel.add(pubProvinceCB, pubCon);

            pubCon.gridy = 8;
            pubCon.gridx = 1;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(newPublisherCountryLabel, pubCon);
            pubCon.gridx = 2;
            pubCon.gridwidth = 2;
            addNewPublisherPanel.add(newPublisherCountryTF, pubCon);
            pubCon.gridx = 4;
            pubCon.gridwidth = 1;
            addNewPublisherPanel.add(newPublisherPostalCodeLabel, pubCon);
            pubCon.gridx = 5;
            pubCon.gridwidth = 2;
            addNewPublisherPanel.add(newPublisherPostalCodeTF, pubCon);

            pubCon.gridy = 9;
            pubCon.gridx = 1;
            addNewPublisherPanel.add(Box.createRigidArea(spacer), pubCon);

            pubCon.gridy = 10;
            pubCon.gridx = 1;
            pubCon.gridwidth = 2;
            addNewPublisherPanel.add(newPublisherBankAccountLabel, pubCon);
            pubCon.gridx = 3;
            pubCon.gridwidth = 4;
            addNewPublisherPanel.add(newPublisherBankAccountTF, pubCon);

            pubCon.gridwidth = 2;
            pubCon.gridy = 1;
            pubCon.gridx = 7;
            pubCon.anchor = GridBagConstraints.LINE_END;
            addNewPublisherPanel.add(confirmNewPublisherAddition, pubCon);

            pubCon.gridy = 15; // shift everything to the top
            pubCon.gridx = 0;
            pubCon.weighty = 1.0;
            pubCon.gridwidth = 8;
            pubCon.anchor = GridBagConstraints.CENTER;
            pubCon.fill = GridBagConstraints.BOTH;
            Component glue = Box.createVerticalGlue();
            addNewPublisherPanel.add(glue, pubCon);
        }

        return addNewPublisherPanel;
    }

    /**
     * Creates the "Add User" tab for the "Add Stuff" tab of the adminView
     *
     * @return addNewUserPanel for adminView
     */
    private JPanel addUser() {
        JPanel newUserPanel = new JPanel(new GridBagLayout());

        /* JButtons */
        JButton logoutAddUserButton = FrontEndUtilities.formatButton("Logout");
        JButton addUserButton = FrontEndUtilities.formatButton("Add User");

        /* JLabels */
        // Admin info
        JLabel newUserLoginCred = new JLabel("Login Credentials: "),
                newUserDetailLabel = new JLabel("User Details: "),
                newAdminUsernameLabel = new JLabel("*Username: "),
                newAdminPasswordLabel = new JLabel("*Password: "),
                confirmAdminPasswordLabel = new JLabel("*Confirm Password: ", JLabel.RIGHT),
                firstNameAdminLabel = new JLabel("*First Name: "),
                lastNameAdminLabel = new JLabel("*Last Name: ", JLabel.RIGHT),
                emailAdminLabel = new JLabel("*Email: "),
                salaryAdminLabel = new JLabel("Salary: ", JLabel.RIGHT),
                // Admin shipping address info (can be EMPTY)
                shippingAdminAddressLabel = new JLabel("Shipping Address"),
                shippingAdminStreetNumLabel = new JLabel("*Street Number: "),
                shippingAdminStreetNameLabel = new JLabel("*Street Name: ", JLabel.RIGHT),
                shippingAdminApartmentLabel = new JLabel("*Apartment: ", JLabel.RIGHT),
                shippingAdminCityLabel = new JLabel("*City: "),
                shippingAdminProvinceLabel = new JLabel("*Province: ", JLabel.RIGHT),
                shippingAdminCountryLabel = new JLabel("*Country: "),
                shippingAdminPostalCodeLabel = new JLabel("*Postal Code: ", JLabel.RIGHT),
                // Admin billing address info
                billingAdminAddressLabel = new JLabel("Billing Address"),
                billAdminStreetNumLabel = new JLabel("Street Number: "),
                billAdminStreetNameLabel = new JLabel("Street Name: ", JLabel.RIGHT),
                billAdminApartmentLabel = new JLabel("Apartment: ", JLabel.RIGHT),
                billAdminCityLabel = new JLabel("City: "),
                billAdminProvinceLabel = new JLabel("Province: ", JLabel.RIGHT),
                billAdminCountryLabel = new JLabel("Country: "),
                billAdminPostalCodeLabel = new JLabel("Postal Code: ", JLabel.RIGHT);

        /* JCheckBoxes */
        JCheckBox billingSameAsShipping = new JCheckBox("Billing Address is the same as Shipping Address");
        billingSameAsShipping.setSelected(true); // selected by default
        JCheckBox isUserAdminCB = new JCheckBox("Is the user an admin?");

        /* ActionListeners */
        logoutAddUserButton.addActionListener(this);
        addUserButton.addActionListener(this);
        billingSameAsShipping.addChangeListener(e -> {
            boolean sameAsBilling = !billingSameAsShipping.isSelected();
            billAdminStreetNumTF.setEnabled(sameAsBilling);
            billAdminStreetNameTF.setEnabled(sameAsBilling);
            billAdminApartmentTF.setEnabled(sameAsBilling);
            billAdminCityTF.setEnabled(sameAsBilling);
            billAdminProvinceCB.setEnabled(sameAsBilling);
            billAdminCountryTF.setEnabled(sameAsBilling);
            billAdminPostalCodeTF.setEnabled(sameAsBilling);
        });
        isUserAdminCB.addChangeListener(e -> {
            boolean admin = isUserAdminCB.isSelected();
            salaryAdminTF.setEnabled(admin);
            if (admin) {
                salaryAdminLabel.setText("*Salary: ");
                shippingAdminStreetNumLabel.setText("Street Number: ");
                shippingAdminStreetNameLabel.setText("Street Name: ");
                shippingAdminApartmentLabel.setText("Apartment: ");
                shippingAdminCityLabel.setText("City: ");
                shippingAdminProvinceLabel.setText("Province: ");
                shippingAdminCountryLabel.setText("Country: ");
                shippingAdminPostalCodeLabel.setText("Postal Code: ");
            } else {
                salaryAdminLabel.setText("Salary: ");
                shippingAdminStreetNumLabel.setText("*Street Number: ");
                shippingAdminStreetNameLabel.setText("*Street Name: ");
                shippingAdminApartmentLabel.setText("*Apartment: ");
                shippingAdminCityLabel.setText("*City: ");
                shippingAdminProvinceLabel.setText("*Province: ");
                shippingAdminCountryLabel.setText("*Country: ");
                shippingAdminPostalCodeLabel.setText("*Postal Code: ");
            }
        });

        // Setup newUserPanel
        {
            // Disable fields by default
            billAdminStreetNumTF.setEnabled(false);
            billAdminStreetNameTF.setEnabled(false);
            billAdminApartmentTF.setEnabled(false);
            billAdminCityTF.setEnabled(false);
            billAdminProvinceCB.setEnabled(false);
            billAdminCountryTF.setEnabled(false);
            billAdminPostalCodeTF.setEnabled(false);
            salaryAdminTF.setEnabled(false);

            GridBagConstraints userCon = new GridBagConstraints();
            Dimension spacer = new Dimension(35, 35);
            userCon.gridy = 0;
            userCon.gridx = 0;
            userCon.anchor = GridBagConstraints.FIRST_LINE_START;
            newUserPanel.add(logoutAddUserButton, userCon);
            userCon.gridx = 1;
            userCon.gridwidth = 6;
            userCon.anchor = GridBagConstraints.CENTER;
            addUserErrorLabel.setForeground(Color.red);
            newUserPanel.add(addUserErrorLabel, userCon);
            userCon.gridx = 7;
            userCon.gridwidth = 2;
            userCon.anchor = GridBagConstraints.FIRST_LINE_END;
            newUserPanel.add(addUserButton, userCon);

            userCon.gridy = 1;
            userCon.gridx = 1;
            userCon.weightx = 1.0;
            userCon.gridwidth = 6;
            userCon.anchor = GridBagConstraints.LINE_END;
            userCon.fill = GridBagConstraints.HORIZONTAL;
            newUserPanel.add(newUserLoginCred, userCon);
            userCon.gridx = 7;
            userCon.gridwidth = 1;
            newUserPanel.add(confirmAdminReg, userCon);

            userCon.gridy = 2;
            userCon.gridx = 1;
            newUserPanel.add(newAdminUsernameLabel, userCon);
            userCon.gridx = 4;
            userCon.gridwidth = 2;
            newUserPanel.add(isUserAdminCB, userCon);
            userCon.gridx = 2;
            newUserPanel.add(newAdminUsernameTF, userCon);

            userCon.gridy = 3;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(newAdminPasswordLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(confirmAdminPasswordLabel, userCon);
            userCon.gridx = 2;
            userCon.gridwidth = 2;
            newUserPanel.add(newAdminPasswordTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(confirmAdminPasswordTF, userCon);

            userCon.gridy = 4;
            userCon.gridx = 1;
            newUserPanel.add(Box.createRigidArea(spacer), userCon);

            userCon.gridy = 5;
            userCon.gridx = 1;
            userCon.gridwidth = 3;
            newUserPanel.add(newUserDetailLabel, userCon);

            userCon.gridy = 6;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(firstNameAdminLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(lastNameAdminLabel, userCon);
            userCon.gridx = 2;
            userCon.gridwidth = 2;
            newUserPanel.add(firstNameAdminTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(lastNameAdminTF, userCon);

            userCon.gridy = 7;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(emailAdminLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(salaryAdminLabel, userCon);
            userCon.gridx = 2;
            userCon.gridwidth = 2;
            newUserPanel.add(emailAdminTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(salaryAdminTF, userCon);

            userCon.gridy = 8;
            userCon.gridx = 1;
            newUserPanel.add(Box.createRigidArea(spacer), userCon);

            userCon.gridy = 9;
            userCon.gridx = 1;
            userCon.gridwidth = 3;
            newUserPanel.add(new JLabel("Address Information"), userCon);

            userCon.gridy = 10;
            userCon.gridx = 1;
            userCon.gridwidth = 3;
            newUserPanel.add(shippingAdminAddressLabel, userCon);

            userCon.gridy = 11;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(shippingAdminStreetNumLabel, userCon);
            userCon.gridx = 2;
            newUserPanel.add(shippingAdminStreetNumTF, userCon);
            userCon.gridx = 3;
            newUserPanel.add(shippingAdminStreetNameLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(shippingAdminStreetNameTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(shippingAdminApartmentLabel, userCon);
            userCon.gridx = 6;
            newUserPanel.add(shippingAdminApartmentTF, userCon);

            userCon.gridy = 12;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(shippingAdminCityLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(shippingAdminProvinceLabel, userCon);
            userCon.gridx = 2;
            userCon.gridwidth = 2;
            newUserPanel.add(shippingAdminCityTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(shippingAdminProvinceCB, userCon);

            userCon.gridy = 13;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(shippingAdminCountryLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(shippingAdminPostalCodeLabel, userCon);
            userCon.gridx = 2;
            userCon.gridwidth = 2;
            newUserPanel.add(shippingAdminCountryTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(shippingAdminPostalCodeTF, userCon);

            userCon.gridy = 14;
            userCon.gridx = 1;
            newUserPanel.add(Box.createRigidArea(spacer), userCon);

            userCon.gridy = 15;
            userCon.gridx = 1;
            userCon.gridwidth = 2;
            newUserPanel.add(billingAdminAddressLabel, userCon);
            userCon.gridx = 3;
            userCon.gridwidth = 4;
            newUserPanel.add(billingSameAsShipping, userCon);

            userCon.gridy = 16;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(billAdminStreetNumLabel, userCon);
            userCon.gridx = 2;
            newUserPanel.add(billAdminStreetNumTF, userCon);
            userCon.gridx = 3;
            newUserPanel.add(billAdminStreetNameLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(billAdminStreetNameTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(billAdminApartmentLabel, userCon);
            userCon.gridx = 6;
            newUserPanel.add(billAdminApartmentTF, userCon);

            userCon.gridy = 17;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(billAdminCityLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(billAdminProvinceLabel, userCon);
            userCon.gridx = 2;
            userCon.gridwidth = 2;
            newUserPanel.add(billAdminCityTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(billAdminProvinceCB, userCon);

            userCon.gridy = 18;
            userCon.gridx = 1;
            userCon.gridwidth = 1;
            newUserPanel.add(billAdminCountryLabel, userCon);
            userCon.gridx = 4;
            newUserPanel.add(billAdminPostalCodeLabel, userCon);
            userCon.gridx = 2;
            userCon.gridwidth = 2;
            newUserPanel.add(billAdminCountryTF, userCon);
            userCon.gridx = 5;
            newUserPanel.add(billAdminPostalCodeTF, userCon);

            userCon.gridy = 19; // shift everything to the top
            userCon.gridx = 0;
            userCon.weighty = 1.0;
            userCon.gridwidth = 8;
            userCon.anchor = GridBagConstraints.CENTER;
            userCon.fill = GridBagConstraints.BOTH;
            Component glue = Box.createVerticalGlue();
            newUserPanel.add(glue, userCon);
        }

        return newUserPanel;
    }

    /**
     * Creates the "Edit Book" tab for the "Edit Stuff" tab of the adminView
     * The idea is for the JTextFields to populate with the current data for the book after "Search Books" is pressed
     * then update the data with the contents of the JTextFields after "Update Book" is pressed
     * The currentISBNLabel should populate with the found book's ISBN
     *
     * @return editBookPanel for adminView
     */
    private JPanel editBook() {
        JPanel editBookPanel = new JPanel(new GridBagLayout());

        /* JButtons */
        JButton updateBookButton = FrontEndUtilities.formatButton("Update Book");
        JButton logoutButton = FrontEndUtilities.formatButton("Logout");
        JButton searchBookButton = FrontEndUtilities.formatButton("Search Books");

        /* JLabel */
        JLabel editBookLabel = new JLabel("Edit Book Information (required fields indicated by *): "),
                searchISBNLabel = new JLabel("Search ISBNs"),
                editISBNLabel = new JLabel("Book ISBN: ", JLabel.RIGHT),
                editBookTitleLabel = new JLabel("*Title: "),
                editBookVersionLabel = new JLabel("Version: "),
                editBookGenreLabel = new JLabel("Genre: "),
                editBookNumPagesLabel = new JLabel("Pg Count: ", JLabel.RIGHT),
                editBookPriceLabel = new JLabel("*Price:"),
                editBookRoyaltyLabel = new JLabel("*Royalty (%): ", JLabel.RIGHT),
                editBookStockLabel = new JLabel("Stock:", JLabel.RIGHT),
                editBookAuthorLabel = new JLabel("Author: "),
                editBookAuthorFNLabel = new JLabel("First Name: "),
                editBookAuthorLNLabel = new JLabel("Last Name: ", JLabel.RIGHT),
                editBookPublisherLabel = new JLabel("Publisher (be sure to add new publishers before editing books): "),
                editBookYearLabel = new JLabel("Year: "),
                accountingLabel = new JLabel("*Accounting: ");

        /* ActionListeners */
        logoutButton.addActionListener(this);
        updateBookButton.addActionListener(this);
        searchBookButton.addActionListener(this);

        // Setup editBookPanel
        {
            GridBagConstraints con = new GridBagConstraints();
            Dimension spacer = new Dimension(15, 15);
            con.gridx = 0;
            con.gridy = 0;
            editBookPanel.add(logoutButton, con);
            con.gridx = 1;
            con.gridwidth = 4;
            con.anchor = GridBagConstraints.CENTER;
            editBookErrorLabel.setForeground(Color.red);
            editBookPanel.add(editBookErrorLabel, con);
            con.gridx = 5;
            con.gridwidth = 1;
            con.anchor = GridBagConstraints.FIRST_LINE_END;
            editBookPanel.add(updateBookButton, con);

            con.gridy = 1;
            con.gridx = 1;
            con.gridwidth = 1;
            con.weightx = 1.0;
            con.anchor = GridBagConstraints.LINE_END;
            con.fill = GridBagConstraints.HORIZONTAL;
            editBookPanel.add(searchISBNLabel, con);
            con.gridwidth = 2;
            con.gridx = 2;
            editBookPanel.add(editBookSearchTF, con);
            con.gridwidth = 1;
            con.gridx = 4;
            editBookPanel.add(searchBookButton, con);
            con.gridx = 5;
            editBookPanel.add(confirmBookEditLabel, con);

            con.gridy = 2;
            con.gridx = 1;
            editBookPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 3;
            con.gridx = 1;
            con.gridwidth = 3;
            con.anchor = GridBagConstraints.LINE_START;
            editBookPanel.add(editBookLabel, con);

            con.gridy = 4;
            con.gridx = 1;
            con.gridwidth = 1;
            editBookPanel.add(editBookTitleLabel, con);
            con.gridx = 2;
            editBookPanel.add(editBookTitleTF, con);
            con.gridx = 3;
            editBookPanel.add(editISBNLabel, con);
            con.gridx = 4;
            editBookPanel.add(currentISBNLabel, con);

            con.gridy = 5;
            con.gridx = 1;
            editBookPanel.add(editBookVersionLabel, con);
            con.gridx = 2;
            editBookPanel.add(editBookVersionTF, con);
            con.gridx = 3;
            editBookPanel.add(editBookNumPagesLabel, con);
            con.gridx = 4;
            editBookPanel.add(editBookNumPagesTF, con);

            con.gridy = 6;
            con.gridx = 1;
            editBookPanel.add(editBookGenreLabel, con);
            con.gridx = 2;
            editBookPanel.add(editBookGenreTF, con);
            con.gridx = 3;
            editBookPanel.add(editBookStockLabel, con);
            con.gridx = 4;
            editBookPanel.add(editBookStockTF, con);

            con.gridy = 7;
            con.gridx = 1;
            editBookPanel.add(editBookYearLabel, con);
            con.gridx = 2;
            editBookPanel.add(editBookYearTF, con);

            con.gridy = 8;
            con.gridx = 1;
            editBookPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 9;
            con.gridx = 1;
            editBookPanel.add(accountingLabel, con);

            con.gridy = 10;
            con.gridx = 1;
            editBookPanel.add(editBookPriceLabel, con);
            con.gridx = 2;
            editBookPanel.add(editBookPriceTF, con);
            con.gridx = 3;
            editBookPanel.add(editBookRoyaltyLabel, con);
            con.gridx = 4;
            editBookPanel.add(editBookRoyaltyTF, con);

            con.gridy = 11;
            con.gridx = 1;
            editBookPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 12;
            con.gridx = 1;
            editBookPanel.add(editBookAuthorLabel, con);

            con.gridy = 13;
            con.gridx = 1;
            editBookPanel.add(editBookAuthorFNLabel, con);
            con.gridx = 2;
            editBookPanel.add(editBookAuthorFNTF, con);
            con.gridx = 3;
            editBookPanel.add(editBookAuthorLNLabel, con);
            con.gridx = 4;
            editBookPanel.add(editBookAuthorLNTF, con);

            con.gridy = 14;
            con.gridx = 1;
            editBookPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 15;
            con.gridwidth = 4;
            editBookPanel.add(editBookPublisherLabel, con);
            con.gridx = 2;

            con.gridy = 16;
            con.gridwidth = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            editBookPanel.add(editBookPublisherTF, con);

            con.gridy = 17; // shift everything to the top
            con.gridx = 0;
            con.weighty = 1.0;
            con.gridwidth = 6;
            con.anchor = GridBagConstraints.CENTER;
            con.fill = GridBagConstraints.BOTH;
            Component glue = Box.createVerticalGlue();
            editBookPanel.add(glue, con);
        }

        return editBookPanel;
    }

    /**
     * Creates the "Edit User" tab for the "Edit Stuff" tab of the adminView
     * The idea is for the JTextFields to populate with the current data for the user after "Search Users" is pressed
     * then update the data with the contents of the JTextFields after "Update User" is pressed
     *
     * @return editUserPanel for adminView
     */
    private JPanel editUser() {
        JPanel editUserPanel = new JPanel(new GridBagLayout());

        /* JButton */
        JButton logoutButton = FrontEndUtilities.formatButton("Logout"),
                confirmButton = FrontEndUtilities.formatButton("Update User"),
                searchButton = FrontEndUtilities.formatButton("Search Users");

        /* JLabels */
        // Search
        JLabel searchUserLabel = new JLabel("Search Username:"),
                editUserLabel = new JLabel("Edit:"),
                // User info
                editUserLoginCredLabel = new JLabel("Login Credentials: "),
                editUserDetailLabel = new JLabel("User Details: "),
                editUsernameLabel = new JLabel("Username: "),
                editPasswordLabel = new JLabel("New Password: "),
                editConfirmPasswordLabel = new JLabel("Confirm Password: ", JLabel.RIGHT),
                editFirstNameLabel = new JLabel("*First Name: "),
                editLastNameLabel = new JLabel("*Last Name: ", JLabel.RIGHT),
                editEmailLabel = new JLabel("*Email: "),
                editSalaryLabel = new JLabel("Salary: ", JLabel.RIGHT),
                // Admin shipping address info (can be EMPTY)
                editShippingAddressLabel = new JLabel("Shipping Address"),
                editShippingStreetNumLabel = new JLabel("*Street Number: "),
                editShippingStreetNameLabel = new JLabel("*Street Name: ", JLabel.RIGHT),
                editShippingApartmentLabel = new JLabel("*Apartment: ", JLabel.RIGHT),
                editShippingCityLabel = new JLabel("*City: "),
                editShippingProvinceLabel = new JLabel("*Province: ", JLabel.RIGHT),
                editShippingCountryLabel = new JLabel("*Country: "),
                editShippingPostalCodeLabel = new JLabel("*Postal Code: ", JLabel.RIGHT),
                // Admin billing address info
                editBillingAddressLabel = new JLabel("Billing Address"),
                editBillStreetNumLabel = new JLabel("Street Number: "),
                editBillStreetNameLabel = new JLabel("Street Name: ", JLabel.RIGHT),
                editBillApartmentLabel = new JLabel("Apartment: ", JLabel.RIGHT),
                editBillCityLabel = new JLabel("City: "),
                editBillProvinceLabel = new JLabel("Province: ", JLabel.RIGHT),
                editBillCountryLabel = new JLabel("Country: "),
                editBillPostalCodeLabel = new JLabel("Postal Code: ", JLabel.RIGHT);

        /* JCheckBoxes */
        editBillingSameAsShipping.setSelected(true); // selected by default

        /* ActionListeners */
        logoutButton.addActionListener(this);
        confirmButton.addActionListener(this);
        searchButton.addActionListener(this);
        editBillingSameAsShipping.addChangeListener(e -> {
            boolean sameAsBilling = !editBillingSameAsShipping.isSelected();
            editBillStreetNumTF.setEnabled(sameAsBilling);
            editBillStreetNameTF.setEnabled(sameAsBilling);
            editBillApartmentTF.setEnabled(sameAsBilling);
            editBillCityTF.setEnabled(sameAsBilling);
            editBillProvinceCB.setEnabled(sameAsBilling);
            editBillCountryTF.setEnabled(sameAsBilling);
            editBillPostalCodeTF.setEnabled(sameAsBilling);
        });
        isUserAdminCB.addChangeListener(e -> {
            boolean admin = isUserAdminCB.isSelected();
            editSalaryTF.setEnabled(admin);
            if (admin) {
                editSalaryLabel.setText("*Salary: ");
                editShippingStreetNumLabel.setText("Street Number: ");
                editShippingStreetNameLabel.setText("Street Name: ");
                editShippingApartmentLabel.setText("Apartment: ");
                editShippingCityLabel.setText("City: ");
                editShippingProvinceLabel.setText("Province: ");
                editShippingCountryLabel.setText("Country: ");
                editShippingPostalCodeLabel.setText("Postal Code: ");
            } else {
                editSalaryLabel.setText("Salary: ");
                editShippingStreetNumLabel.setText("*Street Number: ");
                editShippingStreetNameLabel.setText("*Street Name: ");
                editShippingApartmentLabel.setText("*Apartment: ");
                editShippingCityLabel.setText("*City: ");
                editShippingProvinceLabel.setText("*Province: ");
                editShippingCountryLabel.setText("*Country: ");
                editShippingPostalCodeLabel.setText("*Postal Code: ");
            }
        });

        // Setup editUserPanel
        {
            // Disable fields by default
            editBillStreetNumTF.setEnabled(false);
            editBillStreetNameTF.setEnabled(false);
            editBillApartmentTF.setEnabled(false);
            editBillCityTF.setEnabled(false);
            editBillProvinceCB.setEnabled(false);
            editBillCountryTF.setEnabled(false);
            editBillPostalCodeTF.setEnabled(false);
            editPasswordTF.setEnabled(false);
            confirmEditPasswordTF.setEnabled(false);
            editFirstNameTF.setEnabled(false);
            editLastNameTF.setEnabled(false);
            editEmailTF.setEnabled(false);
            editSalaryTF.setEnabled(false);
            editShippingStreetNumTF.setEnabled(false);
            editShippingStreetNameTF.setEnabled(false);
            editShippingApartmentTF.setEnabled(false);
            editShippingCityTF.setEnabled(false);
            editShippingCountryTF.setEnabled(false);
            editShippingPostalCodeTF.setEnabled(false);
            isUserAdminCB.setEnabled(false);
            editBillingSameAsShipping.setEnabled(false);
            editShippingProvinceCB.setEnabled(false);

            GridBagConstraints con = new GridBagConstraints();
            Dimension spacer = new Dimension(35, 35);

            con.gridy = 0;
            con.gridx = 0;
            con.anchor = GridBagConstraints.FIRST_LINE_START;
            editUserPanel.add(logoutButton, con);
            con.gridx = 0;
            con.gridwidth = 8;
            con.anchor = GridBagConstraints.CENTER;
            editUserErrorLabel.setForeground(Color.red);
            editUserPanel.add(editUserErrorLabel, con);
            con.gridx = 7;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.FIRST_LINE_END;
            editUserPanel.add(confirmButton, con);

            con.gridy = 1;
            con.gridx = 1;
            con.gridwidth = 1;
            con.weightx = 1.0;
            con.anchor = GridBagConstraints.LINE_END;
            con.fill = GridBagConstraints.HORIZONTAL;
            editUserPanel.add(searchUserLabel, con);
            con.gridwidth = 2;
            con.gridx = 2;
            editUserPanel.add(editUserSearchTF, con);
            con.gridwidth = 1;
            con.gridx = 4;
            editUserPanel.add(searchButton, con);
            con.gridx = 7;
            editUserPanel.add(confirmUserEditLabel, con);

            con.gridy = 2;
            con.gridx = 1;
            editUserPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 3;
            editUserPanel.add(editUserLabel, con);

            con.gridy = 4;
            con.gridx = 1;
            con.gridwidth = 6;
            con.anchor = GridBagConstraints.LINE_END;
            con.fill = GridBagConstraints.HORIZONTAL;
            editUserPanel.add(editUserLoginCredLabel, con);

            con.gridy = 5;
            con.gridx = 1;
            editUserPanel.add(editUsernameLabel, con);
            con.gridx = 4;
            con.gridwidth = 2;
            editUserPanel.add(isUserAdminCB, con);
            con.gridx = 2;
            editUserPanel.add(currentUserNameLabel, con);

            con.gridy = 6;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editPasswordLabel, con);
            con.gridx = 4;
            editUserPanel.add(editConfirmPasswordLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            editUserPanel.add(editPasswordTF, con);
            con.gridx = 5;
            editUserPanel.add(confirmEditPasswordTF, con);

            con.gridy = 7;
            con.gridx = 1;
            editUserPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 8;
            con.gridx = 1;
            con.gridwidth = 3;
            editUserPanel.add(editUserDetailLabel, con);

            con.gridy = 9;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editFirstNameLabel, con);
            con.gridx = 4;
            editUserPanel.add(editLastNameLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            editUserPanel.add(editFirstNameTF, con);
            con.gridx = 5;
            editUserPanel.add(editLastNameTF, con);

            con.gridy = 10;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editEmailLabel, con);
            con.gridx = 4;
            editUserPanel.add(editSalaryLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            editUserPanel.add(editEmailTF, con);
            con.gridx = 5;
            editUserPanel.add(editSalaryTF, con);

            con.gridy = 11;
            con.gridx = 1;
            editUserPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 12;
            con.gridx = 1;
            con.gridwidth = 3;
            editUserPanel.add(new JLabel("Address Information"), con);

            con.gridy = 13;
            con.gridx = 1;
            con.gridwidth = 3;
            editUserPanel.add(editShippingAddressLabel, con);

            con.gridy = 14;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editShippingStreetNumLabel, con);
            con.gridx = 2;
            editUserPanel.add(editShippingStreetNumTF, con);
            con.gridx = 3;
            editUserPanel.add(editShippingStreetNameLabel, con);
            con.gridx = 4;
            editUserPanel.add(editShippingStreetNameTF, con);
            con.gridx = 5;
            editUserPanel.add(editShippingApartmentLabel, con);
            con.gridx = 6;
            editUserPanel.add(editShippingApartmentTF, con);

            con.gridy = 15;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editShippingCityLabel, con);
            con.gridx = 4;
            editUserPanel.add(editShippingProvinceLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            editUserPanel.add(editShippingCityTF, con);
            con.gridx = 5;
            editUserPanel.add(editShippingProvinceCB, con);

            con.gridy = 16;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editShippingCountryLabel, con);
            con.gridx = 4;
            editUserPanel.add(editShippingPostalCodeLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            editUserPanel.add(editShippingCountryTF, con);
            con.gridx = 5;
            editUserPanel.add(editShippingPostalCodeTF, con);

            con.gridy = 17;
            con.gridx = 1;
            editUserPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 18;
            con.gridx = 1;
            con.gridwidth = 2;
            editUserPanel.add(editBillingAddressLabel, con);
            con.gridx = 3;
            con.gridwidth = 4;
            editUserPanel.add(editBillingSameAsShipping, con);

            con.gridy = 19;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editBillStreetNumLabel, con);
            con.gridx = 2;
            editUserPanel.add(editBillStreetNumTF, con);
            con.gridx = 3;
            editUserPanel.add(editBillStreetNameLabel, con);
            con.gridx = 4;
            editUserPanel.add(editBillStreetNameTF, con);
            con.gridx = 5;
            editUserPanel.add(editBillApartmentLabel, con);
            con.gridx = 6;
            editUserPanel.add(editBillApartmentTF, con);

            con.gridy = 20;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editBillCityLabel, con);
            con.gridx = 4;
            editUserPanel.add(editBillProvinceLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            editUserPanel.add(editBillCityTF, con);
            con.gridx = 5;
            editUserPanel.add(editBillProvinceCB, con);

            con.gridy = 21;
            con.gridx = 1;
            con.gridwidth = 1;
            editUserPanel.add(editBillCountryLabel, con);
            con.gridx = 4;
            editUserPanel.add(editBillPostalCodeLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            editUserPanel.add(editBillCountryTF, con);
            con.gridx = 5;
            editUserPanel.add(editBillPostalCodeTF, con);

            con.gridy = 22; // shift everything to the top
            con.gridx = 0;
            con.weighty = 1.0;
            con.gridwidth = 8;
            con.anchor = GridBagConstraints.CENTER;
            con.fill = GridBagConstraints.BOTH;
            Component glue = Box.createVerticalGlue();
            editUserPanel.add(glue, con);

            return editUserPanel;
        }
    }

    /**
     * Creates the "Reports" tab for the adminScreen
     * TODO: reportPanel()
     *
     * @return reportPanel for adminView
     */
    private JPanel reportPanel() {
        JPanel generateReportPanel = new JPanel(new GridBagLayout());
        JScrollPane reportContainer = new JScrollPane();

        /* JButtons */
        JButton generateReport = FrontEndUtilities.formatButton("Generate Report");
        JButton logout = FrontEndUtilities.formatButton("Logout");



        /* JLabels */

        /* ActionListeners */

        return new JPanel();
    }

    /**
     * Confirms the admin wants to exit the admin view
     */
    private void confirmViewSwitch() {
        JButton cancelButton = FrontEndUtilities.formatButton("Cancel");
        JButton userButton = FrontEndUtilities.formatButton("Customer View");

        Object[] options = {userButton, cancelButton};
        final JOptionPane screenChoice = new JOptionPane("Are you sure you wish to change views?\nYou cannot switch back without logging out.", JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[1]);
        screenChoice.setIcon(FrontEndUtilities.WINDOW_ICON);
        final JDialog dialog = screenChoice.createDialog("Admin");
        dialog.setContentPane(screenChoice);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        userButton.addActionListener(e -> {
            this.dispose();
            new UserScreen();
            dialog.setVisible(false);
        });
        cancelButton.addActionListener(e -> dialog.setVisible(false));

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Populates edit user fields with appropriate data from database
     * and correctly adjusts checkboxes
     */
    private void fetchEditUserData() {
        ArrayList<Object> update = DatabaseQueries.lookForaUser(editUserSearchTF.getText());

        if (editUserSearchTF.getText().isEmpty()) {
            defaultAdminViewFields();
            editUserErrorLabel.setText("Enter a username before searching");
        } else if (update == null || update.size() == 0) {
            defaultAdminViewFields();
            editUserErrorLabel.setText("User Not Found");
        } else if (update.get(0).equals("-1")) {
            defaultAdminViewFields();
            editUserErrorLabel.setText("Big boy error...contact someone");
        } else {
            Iterator<Object> itr = update.iterator();
            editUserErrorLabel.setText(""); // reset errors
            editUserSearchTF.setText(""); // clear search bar
            // update fields
            currentUserNameLabel.setText((String) itr.next());
            itr.next(); // skip password
            editFirstNameTF.setText((String) itr.next());
            editLastNameTF.setText((String) itr.next());
            editEmailTF.setText((String) itr.next());
            editSalaryTF.setText((String) itr.next());
            if (editSalaryTF.getText().equals("null")) {
                editSalaryTF.setText("");
                isUserAdminCB.setSelected(false);
            } else {
                isUserAdminCB.setSelected(true);
            }

            if (itr.hasNext()) { // user has an address (admins are not required to have an address)
                ArrayList<Object> shipAdd = (ArrayList<Object>) itr.next();
                if (shipAdd != null) {
                    Iterator<Object> addIter = shipAdd.iterator();
                    editShippingStreetNumTF.setText((String) addIter.next());
                    editShippingStreetNameTF.setText((String) addIter.next());
                    editShippingApartmentTF.setText((String) addIter.next());
                    editShippingCityTF.setText((String) addIter.next());
                    editShippingProvinceCB.setSelectedItem(addIter.next());
                    editShippingCountryTF.setText((String) addIter.next());
                    editShippingPostalCodeTF.setText((String) addIter.next());

                    boolean isShipping = (boolean) addIter.next();
                    boolean isBilling = (boolean) addIter.next();
                    editBillingSameAsShipping.setSelected(true);
                    if (addIter.hasNext()) { // user has a billing address
                        if (isShipping && isBilling) { // shipping == billing, user can only have 1 shipping/ billing address
                            editBillStreetNumTF.setText("");
                            editBillStreetNameTF.setText("");
                            editBillApartmentTF.setText("");
                            editBillCityTF.setText("");
                            editBillProvinceCB.setSelectedItem("");
                            editBillCountryTF.setText("");
                            editBillPostalCodeTF.setText("");
                        } else {
                            editBillingSameAsShipping.setSelected(false);
                            editBillStreetNumTF.setText((String) addIter.next());
                            editBillStreetNameTF.setText((String) addIter.next());
                            editBillApartmentTF.setText((String) addIter.next());
                            editBillCityTF.setText((String) addIter.next());
                            editBillProvinceCB.setSelectedItem(addIter.next());
                            editBillCountryTF.setText((String) addIter.next());
                            editBillPostalCodeTF.setText((String) addIter.next());
                        }
                    }
                }
            }
            // Enable fields after search
            editPasswordTF.setEnabled(true);
            confirmEditPasswordTF.setEnabled(true);
            editFirstNameTF.setEnabled(true);
            editLastNameTF.setEnabled(true);
            editEmailTF.setEnabled(true);
            editShippingStreetNumTF.setEnabled(true);
            editShippingStreetNameTF.setEnabled(true);
            editShippingApartmentTF.setEnabled(true);
            editShippingCityTF.setEnabled(true);
            editShippingCountryTF.setEnabled(true);
            editShippingPostalCodeTF.setEnabled(true);
            isUserAdminCB.setEnabled(true);
            editBillingSameAsShipping.setEnabled(true);
            editShippingProvinceCB.setEnabled(true);
        }
    }

    /**
     * Sends edit user fields with appropriate data to the database
     * Updates user about success
     * Clears fields if successful
     */
    private boolean sendEditUserData() {
        // Check to see if the password matches the confirm password textfield.
        if (!(new String(editPasswordTF.getPassword()).equals(new String(confirmEditPasswordTF.getPassword())))) {
            editUserErrorLabel.setText("Update Failed. Passwords do not match.");
            return false;
        }
        // Check to see if the names are empty.
        if (editFirstNameTF.getText().length() == 0 || editLastNameTF.getText().length() == 0) {
            editUserErrorLabel.setText("Update Failed. Please enter both a first and last name.");
            return false;
        }
        // Check to see if the names contain any numbers
        if (FrontEndUtilities.check(editFirstNameTF.getText())) {
            editUserErrorLabel.setText("Update Failed. First names cannot contain numerical values.");
            return false;
        }
        if (FrontEndUtilities.check(editLastNameTF.getText())) {
            editUserErrorLabel.setText("Update Failed. Last names cannot contain numerical values.");
            return false;
        }
        // Ensure the email field is not empty.
        if (editEmailTF.getText().length() == 0) {
            editUserErrorLabel.setText("Update Failed. Email cannot be blank.");
            return false;
        }
        boolean sameShipAndBill = editBillingSameAsShipping.isSelected();

        // Check each of the address fields :(
        // Street Numbers
        {
            // Check for empty fields
            if(!isUserAdminCB.isSelected()){
                editSalaryTF.setText(null);
                if (editShippingStreetNumTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Shipping street number cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && editBillStreetNumTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Billing street number cannot be empty.");
                    return false;
                }
                if (editShippingStreetNameTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Shipping street name cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && editBillStreetNameTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Billing street name cannot be empty.");
                    return false;
                }
                if (editShippingCityTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Shipping city name cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && editBillCityTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Billing city name cannot be empty.");
                    return false;
                }
                if (editShippingProvinceCB.getSelectedIndex() == 0) {
                    editUserErrorLabel.setText("Update Failed. Please select a shipping province.");
                    return false;
                }
                if (!sameShipAndBill && editBillProvinceCB.getSelectedIndex() == 0) {
                    editUserErrorLabel.setText("Update Failed. Please select a billing province.");
                    return false;
                }
                if (editShippingCountryTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Shipping country cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && editBillCountryTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Billing country cannot be empty.");
                    return false;
                }
                if (editShippingPostalCodeTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Shipping postal code cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && editBillPostalCodeTF.getText().length() == 0) {
                    editUserErrorLabel.setText("Update Failed. Billing postal code cannot be empty.");
                    return false;
                }
            } else {
                if(editSalaryTF.getText().isEmpty()){
                    editUserErrorLabel.setText("Update Failed. Admins need a salary.");
                    return false;
                }
            }

            // Check validity
            try {
                if(!editShippingStreetNumTF.getText().isEmpty()) Double.parseDouble(editShippingStreetNumTF.getText());
                if (!sameShipAndBill) {
                    if(!editBillStreetNumTF.getText().isEmpty()) Double.parseDouble(editBillStreetNumTF.getText());
                }
                if (isUserAdminCB.isSelected()){
                    if(!editSalaryTF.getText().isEmpty()) Double.parseDouble(editSalaryTF.getText());
                }
            } catch (NumberFormatException ex) {
                editUserErrorLabel.setText("Update Failed. Street numbers cannot contain letters.");
                return false;
            }
            if (FrontEndUtilities.check(editShippingCityTF.getText())) {
                editUserErrorLabel.setText("Update Failed. Shipping city cannot contain numerical values.");
                return false;
            }
            if (FrontEndUtilities.check(editBillCityTF.getText())) {
                editUserErrorLabel.setText("Update Failed. Billing city cannot contain numerical values.");
                return false;
            }
            if (FrontEndUtilities.check(editShippingCountryTF.getText())) {
                editUserErrorLabel.setText("Update Failed. Shipping country cannot contain numerical values.");
                return false;
            }
            if (FrontEndUtilities.check(editBillCountryTF.getText())) {
                editUserErrorLabel.setText("Update Failed. Billing country cannot contain numerical values.");
                return false;
            }


        }

        // Attempt to add the user to the database.
        if (DatabaseQueries.updateUser(currentUserNameLabel.getText(), new String(editPasswordTF.getPassword()), editFirstNameTF.getText(), editLastNameTF.getText(), editEmailTF.getText())) {
            DatabaseQueries.updateAdmin(currentUserNameLabel.getText(), editSalaryTF.getText());
        } else {
            editUserErrorLabel.setText("Update Failed. A user with that username is not registered in the system. Proceed to \"Add User\" screen.");
            return false;
        }

        /* If we get here, the following insertion methods will not fail. */
        // Add the editShippingping address.
        if (!sameShipAndBill) {
            // Need to add the billing address as a separate address.
            DatabaseQueries.updateAddress(currentUserNameLabel.getText(), editShippingStreetNumTF.getText(), editShippingStreetNameTF.getText(), editShippingApartmentTF.getText(), editShippingCityTF.getText(), Objects.requireNonNull(editShippingProvinceCB.getSelectedItem()).toString(), editShippingCountryTF.getText(), editShippingPostalCodeTF.getText(), true, false);
            DatabaseQueries.updateAddress(currentUserNameLabel.getText(), editBillStreetNumTF.getText(), editBillStreetNameTF.getText(), editBillApartmentTF.getText(), editBillCityTF.getText(), Objects.requireNonNull(editBillProvinceCB.getSelectedItem()).toString(), editBillCountryTF.getText(), editBillPostalCodeTF.getText(), false, true);
        }else {
            DatabaseQueries.updateAddress(currentUserNameLabel.getText(), editShippingStreetNumTF.getText(), editShippingStreetNameTF.getText(), editShippingApartmentTF.getText(), editShippingCityTF.getText(), Objects.requireNonNull(editShippingProvinceCB.getSelectedItem()).toString(), editShippingCountryTF.getText(), editShippingPostalCodeTF.getText(), true, true);
        }
        // Done.
        return true;
    }

    /**
     * Implements ActionListeners for GUI components
     *
     * @param e The ActionEvent that was triggered via a JButton.
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();


        if (o instanceof JButton) {
            switch (((JButton) o).getText()) {
                case "Logout" -> FrontEndUtilities.confirmLogout(this); // Anywhere and everywhere
                case "Search Users" -> fetchEditUserData(); // Admin Edit User Screen
                case "Update User" -> {
                    if (sendEditUserData()) {
                        defaultAdminViewFields();
                        confirmUserEditLabel.setText("User Updated");
                    }
                } // Admin Edit User Screen
                case "Search Books" -> System.out.println("Searching Books"); // Admin Edit Books Screen
                case "Update Book" -> {
                    defaultAdminViewFields();
                    confirmBookEditLabel.setText("Book Updated");
                } // Admin Edit Books Screen
                case "Add Book" -> {
                    defaultAdminViewFields();
                    confirmNewBookAddition.setText("New Book Added");
                }// Admin Add Book Screen
                case "Add Publisher" -> {
                    defaultAdminViewFields();
                    confirmNewPublisherAddition.setText("New Publisher Added");
                } // Admin Add Publisher Screen
                case "Add User" -> {
                    defaultAdminViewFields();
                    confirmAdminReg.setText("New User Added");
                } // Admin Add User Screen
                default -> System.out.println("Error");
            }
        }

        if (o instanceof JMenuItem) {
            switch (((JMenuItem) o).getText()) {
                case "Logout" -> FrontEndUtilities.confirmLogout(this); // Anywhere and everywhere
                case "Switch to User View" -> confirmViewSwitch();
                default -> System.out.println("Error");
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object o = e.getSource();

        if (o instanceof JTabbedPane) {
            defaultAdminViewFields();
        }

    }

}
