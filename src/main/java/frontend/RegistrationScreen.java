package frontend;

import backend.DatabaseQueries;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * The Registration class is responsible for registering users such that they
 * are a part of the system.
 *
 * @author Ryan Godfrey, John Breton
 * @version April 14th, 2020
 */
public class RegistrationScreen extends JFrame implements ActionListener {

    /* Component declarations */
    // JComboBox
    private final JComboBox<String> billProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr);
    private final JComboBox<String> shipProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr);
    // JTextFields
    private final JTextField
            billStreetNumTF = new JTextField(), shipStreetNumTF = new JTextField(),
            billStreetNameTF = new JTextField(), shipStreetNameTF = new JTextField(),
            billApartmentTF = new JTextField(), shipApartmentTF = new JTextField(),
            billCityTF = new JTextField(), shipCityTF = new JTextField(),
            billCountryTF = new JTextField(), shipCountryTF = new JTextField(),
            billPostalCodeTF = new JTextField(), shipPostalCodeTF = new JTextField(),
            newUsernameTF = new JTextField(), firstNameTF = new JTextField(),
            lastNameTF = new JTextField(), emailTF = new JTextField();
    // JPasswordField
    private final JPasswordField newPasswordTF = new JPasswordField();
    private final JPasswordField confirmPasswordTF = new JPasswordField();
    // JCheckBox
    private final JCheckBox billingSameAsShipping = new JCheckBox("Billing Address is the same as Shipping Address");
    // JLabels
    private final JLabel confirmRegistration = new JLabel("", JLabel.CENTER);

    /**
     * Display the screen for registration of a new customer.
     */
    public RegistrationScreen() {
        // Clear GUI in order to reload
        this.setPreferredSize(new Dimension(700, 550));
        this.setMaximumSize(new Dimension(700, 550));
        if (this.getJMenuBar() != null) this.getJMenuBar().setVisible(false);
        // Get the container for the JFrame.
        Container c = this.getContentPane();
        c.removeAll();

        // JButton
        JButton cancelReg = new JButton("Cancel Registration");
        JPanel buttons = new JPanel();
        buttons.add(cancelReg);
        JButton submitReg = new JButton("Submit");
        buttons.add(submitReg);

        // JPanels
        JPanel regPage = new JPanel(new GridBagLayout());
        regPage.setBorder(new EmptyBorder(15, 15, 15, 15));
        buttons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        buttons.add(cancelReg);
        buttons.add(submitReg);


        /* Component setup*/
        // JButton formatting
        cancelReg.setBackground(Color.WHITE);
        submitReg.setBackground(Color.WHITE);

        // Add action listeners
        cancelReg.addActionListener(this);
        submitReg.addActionListener(this);
        billingSameAsShipping.addChangeListener(e -> {
            boolean sameAsBilling = !billingSameAsShipping.isSelected();
            billStreetNumTF.setEnabled(sameAsBilling);
            billStreetNameTF.setEnabled(sameAsBilling);
            billApartmentTF.setEnabled(sameAsBilling);
            billCityTF.setEnabled(sameAsBilling);
            billProvinceCB.setEnabled(sameAsBilling);
            billCountryTF.setEnabled(sameAsBilling);
            billPostalCodeTF.setEnabled(sameAsBilling);
        });

        // Setup Registration Panel
        {
            GridBagConstraints con = new GridBagConstraints();
            Dimension spacer = new Dimension(35, 35);
            con.gridy = 0;
            con.gridx = 0;
            con.gridwidth = 6;
            con.fill = GridBagConstraints.HORIZONTAL;
            con.anchor = GridBagConstraints.CENTER;
            // Welcome message
            JLabel newUserWelcome1 = new JLabel("Welcome to LookInnaBook!", JLabel.CENTER);
            regPage.add(newUserWelcome1, con);

            con.gridy = 1;
            JLabel newUserWelcome2 = new JLabel("Enter your information in the space provided below.", JLabel.CENTER);
            regPage.add(newUserWelcome2, con);

            con.gridy = 2;
            JLabel newUserWelcome3 = new JLabel("Required fields are indicated with a \"*\".", JLabel.CENTER);
            regPage.add(newUserWelcome3, con);

            con.gridy = 3;
            con.gridx = 0;
            con.weightx = 1.0;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.LINE_START;
            regPage.add(new JLabel("Login Credentials"), con);

            con.gridy = 4;
            con.gridx = 0;
            con.gridwidth = 1;
            // User info
            JLabel newUsernameLabel = new JLabel("*Username: ");
            regPage.add(newUsernameLabel, con);
            con.gridx = 1;
            con.gridwidth = 2;
            regPage.add(newUsernameTF, con);

            con.gridy = 5;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel newPasswordLabel = new JLabel("*Password: ");
            regPage.add(newPasswordLabel, con);
            con.gridx = 3;
            JLabel confirmPasswordLabel = new JLabel("*Confirm Password: ", JLabel.RIGHT);
            regPage.add(confirmPasswordLabel, con);
            con.gridx = 1;
            con.gridwidth = 2;
            regPage.add(newPasswordTF, con);
            con.gridx = 4;
            regPage.add(confirmPasswordTF, con);

            con.gridy = 6;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel firstNameLabel = new JLabel("*First Name: ");
            regPage.add(firstNameLabel, con);
            con.gridx = 3;
            JLabel lastNameLabel = new JLabel("*Last Name: ", JLabel.RIGHT);
            regPage.add(lastNameLabel, con);
            con.gridx = 1;
            con.gridwidth = 2;
            regPage.add(firstNameTF, con);
            con.gridx = 4;
            regPage.add(lastNameTF, con);

            con.gridy = 7;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel emailLabel = new JLabel("*Email: ");
            regPage.add(emailLabel, con);
            con.gridx = 1;
            con.gridwidth = 2;
            regPage.add(emailTF, con);

            con.gridy = 8;
            con.gridx = 0;
            con.gridwidth = 1;
            regPage.add(Box.createRigidArea(spacer), con);

            con.gridy = 9;
            con.gridx = 0;
            con.gridwidth = 3;
            // User shipping address info
            JLabel shippingAddressLabel = new JLabel("Shipping Address");
            regPage.add(shippingAddressLabel, con);

            con.gridy = 10;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel shippingStreetNumLabel = new JLabel("*Street Number: ");
            regPage.add(shippingStreetNumLabel, con);
            con.gridx = 1;
            regPage.add(shipStreetNumTF, con);
            con.gridx = 2;
            JLabel shippingStreetNameLabel = new JLabel("*Street Name: ", JLabel.RIGHT);
            regPage.add(shippingStreetNameLabel, con);
            con.gridx = 3;
            regPage.add(shipStreetNameTF, con);
            con.gridx = 4;
            JLabel shippingApartmentLabel = new JLabel("Apartment: ", JLabel.RIGHT);
            regPage.add(shippingApartmentLabel, con);
            con.gridx = 5;
            regPage.add(shipApartmentTF, con);

            con.gridy = 11;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel shippingCityLabel = new JLabel("*City: ");
            regPage.add(shippingCityLabel, con);
            con.gridx = 3;
            JLabel shippingProvinceLabel = new JLabel("*Province: ", JLabel.RIGHT);
            regPage.add(shippingProvinceLabel, con);
            con.gridx = 1;
            con.gridwidth = 2;
            regPage.add(shipCityTF, con);
            con.gridx = 4;
            con.gridwidth = 1;
            regPage.add(shipProvinceCB, con);

            con.gridy = 12;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel shippingCountryLabel = new JLabel("*Country: ");
            regPage.add(shippingCountryLabel, con);
            con.gridx = 3;
            JLabel shippingPostalCodeLabel = new JLabel("*Postal Code: ", JLabel.RIGHT);
            regPage.add(shippingPostalCodeLabel, con);
            con.gridx = 1;
            con.gridwidth = 2;
            regPage.add(shipCountryTF, con);
            con.gridx = 4;
            regPage.add(shipPostalCodeTF, con);

            con.gridy = 13;
            con.gridx = 0;
            con.gridwidth = 1;
            regPage.add(Box.createRigidArea(spacer), con);

            con.gridy = 14;
            con.gridx = 0;
            con.gridwidth = 3;
            // User billing address info
            JLabel billingAddressLabel = new JLabel("Billing Address");
            regPage.add(billingAddressLabel, con);
            con.gridx = 1;
            regPage.add(billingSameAsShipping, con);

            con.gridy = 15;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel billStreetNumLabel = new JLabel("Street Number: ");
            regPage.add(billStreetNumLabel, con);
            con.gridx = 1;
            regPage.add(billStreetNumTF, con);
            con.gridx = 2;
            JLabel billStreetNameLabel = new JLabel("Street Name: ", JLabel.RIGHT);
            regPage.add(billStreetNameLabel, con);
            con.gridx = 3;
            regPage.add(billStreetNameTF, con);
            con.gridx = 4;
            JLabel billApartmentLabel = new JLabel("Apartment: ", JLabel.RIGHT);
            regPage.add(billApartmentLabel, con);
            con.gridx = 5;
            regPage.add(billApartmentTF, con);

            con.gridy = 16;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel billCityLabel = new JLabel("City: ");
            regPage.add(billCityLabel, con);
            con.gridx = 3;
            JLabel billProvinceLabel = new JLabel("Province: ", JLabel.RIGHT);
            regPage.add(billProvinceLabel, con);
            con.gridx = 1;
            con.gridwidth = 2;
            regPage.add(billCityTF, con);
            con.gridx = 4;
            con.gridwidth = 1;
            regPage.add(billProvinceCB, con);

            con.gridy = 17;
            con.gridx = 0;
            con.gridwidth = 1;
            JLabel billCountryLabel = new JLabel("Country: ");
            regPage.add(billCountryLabel, con);
            con.gridx = 3;
            JLabel billPostalCodeLabel = new JLabel("Postal Code: ", JLabel.RIGHT);
            regPage.add(billPostalCodeLabel, con);
            con.gridx = 1;
            con.gridwidth = 2;
            regPage.add(billCountryTF, con);
            con.gridx = 4;
            regPage.add(billPostalCodeTF, con);

            con.gridy = 18;
            con.gridx = 0;
            con.gridwidth = 6;
            regPage.add(buttons, con);

            con.gridy = 19;
            con.gridx = 0;
            con.gridwidth = 6;
            regPage.add(confirmRegistration, con);

            con.gridy = 19; // shift everything to the top
            con.gridx = 0;
            con.weighty = 1.0;
            con.gridwidth = 6;
            con.anchor = GridBagConstraints.CENTER;
            con.fill = GridBagConstraints.BOTH;
            Component glue = Box.createVerticalGlue();
            regPage.add(glue, con);
        }

        c.add(regPage);
        FrontEndUtilities.configureFrame(this);
    }

    /**
     * Attempts to register a user.
     *
     * @return True if registration was successful, false otherwise.
     * @see super.registerNewUser, super.addHasAdd, super.addAddress, super.countAddresses for further implementation.
     */
    private boolean register() {
        confirmRegistration.setForeground(Color.red);
        // Check for a valid username.
        if (newUsernameTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Please provide a username.");
            return false;
        }
        // Check for a valid password.
        if (newPasswordTF.getPassword().length == 0 || confirmPasswordTF.getPassword().length == 0) {
            confirmRegistration.setText("Registration Failed. Please provide and confirm a password.");
            return false;
        }
        // Check to see if the password matches the confirm password textfield.
        if (!(new String(confirmPasswordTF.getPassword()).equals(new String(newPasswordTF.getPassword())))) {
            confirmRegistration.setText("Registration Failed. Passwords do not match.");
            return false;
        }
        // Check to see if the names contain any numbers/are empty.
        if (firstNameTF.getText().length() == 0 || lastNameTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Please enter both a first and last name.");
            return false;
        }
        if (FrontEndUtilities.check(firstNameTF.getText())) {
            confirmRegistration.setText("Registration Failed. First names cannot contain numerical values.");
            return false;
        }
        if (FrontEndUtilities.check(lastNameTF.getText())) {
            confirmRegistration.setText("Registration Failed. Last names cannot contain numerical values.");
            return false;
        }
        // Ensure the email field is not empty.
        if (emailTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Email cannot be blank.");
            return false;
        }
        boolean sameShipAndBill = billingSameAsShipping.isSelected();

        // Check each of the address fields :(
        // Street Numbers
        if (shipStreetNumTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Shipping street number cannot be empty.");
            return false;
        }
        if (!sameShipAndBill && billStreetNumTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Billing street number cannot be empty.");
            return false;
        }
        try {
            Double.parseDouble(shipStreetNumTF.getText());
            if (!sameShipAndBill) {
                Double.parseDouble(billStreetNumTF.getText());
            }
        } catch (NumberFormatException ex) {
            confirmRegistration.setText("Registration Failed. Street numbers cannot contain letters.");
            return false;
        }

        // Street Names
        if (shipStreetNameTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Shipping street name cannot be empty.");
            return false;
        }
        if (!sameShipAndBill && billStreetNameTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Billing street name cannot be empty.");
            return false;
        }
        // Cities
        if (FrontEndUtilities.check(shipCityTF.getText())) {
            confirmRegistration.setText("Registration Failed. Shipping city cannot contain numerical values.");
            return false;
        }
        if (FrontEndUtilities.check(billCityTF.getText())) {
            confirmRegistration.setText("Registration Failed. Billing city cannot contain numerical values.");
            return false;
        }
        if (shipCityTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Shipping city name cannot be empty.");
            return false;
        }
        if (!sameShipAndBill && billCityTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Billing city name cannot be empty.");
            return false;
        }
        if (shipProvinceCB.getSelectedIndex() == 0) {
            confirmRegistration.setText("Registration Failed. Please select a shipping province.");
            return false;
        }
        if (!sameShipAndBill && billProvinceCB.getSelectedIndex() == 0) {
            confirmRegistration.setText("Registration Failed. Please select a billing province.");
            return false;
        }
        // Countries
        if (FrontEndUtilities.check(shipCountryTF.getText())) {
            confirmRegistration.setText("Registration Failed. Shipping country cannot contain numerical values.");
            return false;
        }
        if (FrontEndUtilities.check(billCountryTF.getText())) {
            confirmRegistration.setText("Registration Failed. Billing country cannot contain numerical values.");
            return false;
        }
        if (shipCountryTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Shipping country cannot be empty.");
            return false;
        }
        if (!sameShipAndBill && billCountryTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Billing country cannot be empty.");
            return false;
        }
        // Postal Code
        if (shipPostalCodeTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Shipping postal code cannot be empty.");
            return false;
        }
        if (!sameShipAndBill && billPostalCodeTF.getText().length() == 0) {
            confirmRegistration.setText("Registration Failed. Billing postal code cannot be empty.");
            return false;
        }

        // Attempt to add the user to the database.
        if (DatabaseQueries.registerNewUser(newUsernameTF.getText(), new String(newPasswordTF.getPassword()), firstNameTF.getText(), lastNameTF.getText(), emailTF.getText())) {
            confirmRegistration.setText("Registration Successful");
            confirmRegistration.setForeground(Color.BLACK);
        } else {
            confirmRegistration.setText("Registration Failed. A user with that username is already registered in the system. Please try again.");
            return false;
        }

        /* If we get here, the following insertion methods will not fail. */
        // Add the shipping address and billing address.
        // I changed this a bit, each user will now have exactly 2 addresses, 1 for shipping, 1 for billing for simplicity of updating their addresses later
        DatabaseQueries.addAddress(shipStreetNumTF.getText(), shipStreetNameTF.getText(), shipApartmentTF.getText(), shipCityTF.getText(), Objects.requireNonNull(shipProvinceCB.getSelectedItem()).toString(), shipCountryTF.getText(), shipPostalCodeTF.getText());
        DatabaseQueries.addHasAdd(newUsernameTF.getText(), true, false);
        if (!sameShipAndBill) {
            // Need to add the billing address as a separate address.
            DatabaseQueries.addAddress(billStreetNumTF.getText(), billStreetNameTF.getText(), billApartmentTF.getText(), billCityTF.getText(), Objects.requireNonNull(billProvinceCB.getSelectedItem()).toString(), billCountryTF.getText(), billPostalCodeTF.getText());
        } else {
            DatabaseQueries.addAddress(shipStreetNumTF.getText(), shipStreetNameTF.getText(), shipApartmentTF.getText(), shipCityTF.getText(), Objects.requireNonNull(shipProvinceCB.getSelectedItem()).toString(), shipCountryTF.getText(), shipPostalCodeTF.getText());
        }
        DatabaseQueries.addHasAdd(newUsernameTF.getText(), false, true);
        // Done.
        return true;
    }

    /**
     * Clears the fields in the registration screen
     */
    private void clearRegistrationFields() {
        newUsernameTF.setText("");
        newPasswordTF.setText("");
        confirmPasswordTF.setText("");
        firstNameTF.setText("");
        lastNameTF.setText("");
        emailTF.setText("");
        shipStreetNumTF.setText("");
        shipStreetNameTF.setText("");
        shipApartmentTF.setText("");
        shipCityTF.setText("");
        shipProvinceCB.setSelectedIndex(0);
        shipCountryTF.setText("");
        shipPostalCodeTF.setText("");
        billStreetNameTF.setText("");
        billStreetNumTF.setText("");
        billApartmentTF.setText("");
        billCityTF.setText("");
        billProvinceCB.setSelectedIndex(0);
        billCountryTF.setText("");
        billPostalCodeTF.setText("");
        confirmRegistration.setText("");
    }

    /**
     * Implements ActionListeners for GUI components
     *
     * @param e The ActionEvent that was triggered via a JButton.
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        switch (((JButton) o).getText()) {
            case "Cancel Registration", "Proceed to Login" -> {
                this.dispose();
                new LoginScreen();
            }
            case "Submit" -> {
                if (register()) {
                    confirmRegistration.setText("Registration Successful");
                    clearRegistrationFields();
                    ((JButton) o).setText("Proceed to Login");
                }
            }
            default -> System.out.println("Error");
        }
    }
}
