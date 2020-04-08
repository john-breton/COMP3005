package frontend;

import backend.DatabaseQueries;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CheckoutScreen extends JFrame implements ActionListener {

    // JTextFields
    private final JTextField checkoutshippingStreetNumTF = new JTextField(5),
            checkoutshippingStreetNameTF = new JTextField(15),
            checkoutshippingApartmentTF = new JTextField(5),
            checkoutshippingCityTF = new JTextField(15),
            checkoutshippingCountryTF = new JTextField(15),
            checkoutshippingPostalCodeTF = new JTextField(15),

    checkoutBillingStreetNumTF = new JTextField(5),
            checkoutBillingStreetNameTF = new JTextField(15),
            checkoutBillingApartmentTF = new JTextField(5),
            checkoutBillingCityTF = new JTextField(15),
            checkoutBillingCountryTF = new JTextField(15),
            checkoutBillingPostalCodeTF = new JTextField(15),

    checkoutCreditCardNumTF = new JTextField(15),
            checkoutCreditCardExpTF = new JTextField(5),
            checkoutCreditCardCVVTF = new JTextField(5);

    // JComboBoxes
    final JComboBox<String> checkoutShippingProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr),
            checkoutBillingProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr);

    // JLabels
    private final JLabel checkoutErrorLabel = new JLabel("", JLabel.CENTER);

    private boolean sameAsBilling;

    // Current username
    private final String username;
    private final ArrayList<Object> userInfo;

    /**
     * Creates the "Checkout" interface for the userScreen
     * Accepts new shipping and billing addresses
     * Accepts credit card details
     * <p>
     * TODO: Possibly add a cart view during checkout, but we could also not... Maybe. But that's an "if I feel like it" feature.
     */
    public CheckoutScreen(String username, String orderCost) {
        checkoutShippingProvinceCB.setBackground(Color.WHITE);
        checkoutBillingProvinceCB.setBackground(Color.WHITE);
        this.username = username;
        this.userInfo = DatabaseQueries.lookForaUser(username);
        Container c = this.getContentPane();
        // Clear GUI in order to reload
        this.setPreferredSize(new Dimension(800, 500));
        if (this.getJMenuBar() != null) this.getJMenuBar().setVisible(false);
        c.removeAll();

        // Setup Components
        /* JButtons */
        JButton cancelOrder = FrontEndUtilities.formatButton("Cancel Checkout"),
                submitOrder = FrontEndUtilities.formatButton("Confirm Order");

        /* Panel */
        final GridBagLayout layout = new GridBagLayout();
        JPanel checkoutPanel = new JPanel(layout);

        /* JLabels */
        JLabel checkoutTotalPriceLabel = new JLabel("Total Price: ", JLabel.RIGHT),
                checkoutUserLabel = new JLabel("User: "),
                checkoutNameLabel = new JLabel("Name: "),
                checkoutEmailLabel = new JLabel("Receipt will be sent to: "),
                // checkout shipping address info
                checkoutShippingLabel = new JLabel("Confirm Shipping Address"),
                checkoutStreetNumLabel = new JLabel("*Street Number: "),
                checkoutStreetNameLabel = new JLabel("*Street Name: ", JLabel.RIGHT),
                checkoutApartmentLabel = new JLabel("*Apartment: ", JLabel.RIGHT),
                checkoutCityLabel = new JLabel("*City: "),
                checkoutProvinceLabel = new JLabel("*Province: ", JLabel.RIGHT),
                checkoutCountryLabel = new JLabel("*Country: "),
                checkoutPostalCodeLabel = new JLabel("*Postal Code: ", JLabel.RIGHT),
                //  checkout Billing info
                checkoutBillingAddressLabel = new JLabel("Billing Info"),
                checkoutCreditCardNumLabel = new JLabel("Credit Card #: "),
                checkoutCreditCardExpLabel = new JLabel("EXP: "),
                checkoutCreditCardCVV = new JLabel("CVV: ", JLabel.RIGHT),
                checkoutBillingStreetNumLabel = new JLabel("Street Number: "),
                checkoutBillingStreetNameLabel = new JLabel("Street Name: ", JLabel.RIGHT),
                checkoutBillingApartmentLabel = new JLabel("Apartment: ", JLabel.RIGHT),
                checkoutBillingCityLabel = new JLabel("City: "),
                checkoutBillingProvinceLabel = new JLabel("Province: ", JLabel.RIGHT),
                checkoutBillingCountryLabel = new JLabel("Country: "),
                checkoutBillingPostalCodeLabel = new JLabel("Postal Code: ", JLabel.RIGHT);

        /* JCheckBoxes */
        JCheckBox billingSameAsShipping = new JCheckBox("Billing Address is the same as Shipping Address");
        billingSameAsShipping.setSelected(true); // selected by default

        /* ActionListeners */
        cancelOrder.addActionListener(this);
        submitOrder.addActionListener(this);
        billingSameAsShipping.addActionListener(e -> {
            sameAsBilling = !billingSameAsShipping.isSelected();
            checkoutBillingStreetNumTF.setEnabled(sameAsBilling);
            checkoutBillingStreetNameTF.setEnabled(sameAsBilling);
            checkoutBillingApartmentTF.setEnabled(sameAsBilling);
            checkoutBillingCityTF.setEnabled(sameAsBilling);
            checkoutBillingProvinceCB.setEnabled(sameAsBilling);
            checkoutBillingCountryTF.setEnabled(sameAsBilling);
            checkoutBillingPostalCodeTF.setEnabled(sameAsBilling);
        });

        // Setup checkoutPanel
        {
            checkoutBillingStreetNumTF.setEnabled(false);
            checkoutBillingStreetNameTF.setEnabled(false);
            checkoutBillingApartmentTF.setEnabled(false);
            checkoutBillingCityTF.setEnabled(false);
            checkoutBillingProvinceCB.setEnabled(false);
            checkoutBillingCountryTF.setEnabled(false);
            checkoutBillingPostalCodeTF.setEnabled(false);

            GridBagConstraints con = new GridBagConstraints();
            Dimension spacer = new Dimension(35, 35);
            con.gridy = 0;
            con.gridx = 1;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.LINE_START;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(cancelOrder, con);
            con.gridx = 0;
            con.gridwidth = 8;
            checkoutErrorLabel.setForeground(Color.red);
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutErrorLabel, con);

            con.gridy = 1;
            con.gridx = 1;
            con.weightx = 1.0;
            con.gridwidth = 1;
            con.anchor = GridBagConstraints.LINE_START;
            checkoutUserLabel.setFont(checkoutUserLabel.getFont().deriveFont(Font.BOLD));
            checkoutPanel.add(checkoutUserLabel, con);
            con.gridx = 2;
            JLabel checkoutUsernameLabel = new JLabel(username);
            checkoutPanel.add(checkoutUsernameLabel, con);

            con.gridy = 2;
            con.gridx = 1;
            con.gridwidth = 1;
            checkoutNameLabel.setFont(checkoutNameLabel.getFont().deriveFont(Font.BOLD));
            checkoutPanel.add(checkoutNameLabel, con);
            con.gridx = 2;
            con.gridwidth = 4;
            JLabel checkoutNameField = new JLabel(userInfo.get(2).toString() + " " + userInfo.get(3).toString());
            checkoutPanel.add(checkoutNameField, con);

            con.gridy = 3;
            con.gridx = 1;
            con.gridwidth = 2;
            checkoutEmailLabel.setFont(checkoutEmailLabel.getFont().deriveFont(Font.BOLD));
            checkoutPanel.add(checkoutEmailLabel, con);
            con.gridx = 3;
            con.gridwidth = 4;
            JLabel checkoutEmailConfirm = new JLabel(userInfo.get(4).toString());
            checkoutPanel.add(checkoutEmailConfirm, con);

            con.gridy = 4;
            con.gridx = 1;
            checkoutPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 5;
            con.gridx = 1;
            con.gridwidth = 3;
            checkoutShippingLabel.setFont(checkoutShippingLabel.getFont().deriveFont(Font.BOLD));
            checkoutPanel.add(checkoutShippingLabel, con);

            con.gridy = 6;
            con.gridx = 1;
            con.gridwidth = 1;
            checkoutPanel.add(checkoutStreetNumLabel, con);
            con.gridx = 2;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutshippingStreetNumTF, con);
            con.gridx = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutStreetNameLabel, con);
            con.gridx = 4;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutshippingStreetNameTF, con);
            con.gridx = 5;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutApartmentLabel, con);
            con.gridx = 6;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutshippingApartmentTF, con);

            con.gridy = 7;
            con.gridx = 1;
            con.gridwidth = 1;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutCityLabel, con);
            con.gridx = 4;
            checkoutPanel.add(checkoutProvinceLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutshippingCityTF, con);
            con.gridx = 5;
            con.gridwidth = 1;
            checkoutPanel.add(checkoutShippingProvinceCB, con);

            con.gridy = 8;
            con.gridx = 1;
            con.gridwidth = 1;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutCountryLabel, con);
            con.gridx = 4;
            checkoutPanel.add(checkoutPostalCodeLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutshippingCountryTF, con);
            con.gridx = 5;
            con.anchor = GridBagConstraints.LINE_START;
            checkoutPanel.add(checkoutshippingPostalCodeTF, con);

            con.gridy = 9;
            con.gridx = 1;
            checkoutPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 10;
            con.gridx = 1;
            con.gridwidth = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutBillingAddressLabel.setFont(checkoutBillingAddressLabel.getFont().deriveFont(Font.BOLD));
            checkoutPanel.add(checkoutBillingAddressLabel, con);
            con.gridx = 3;
            checkoutPanel.add(billingSameAsShipping, con);

            con.gridy = 11;
            con.gridx = 1;
            con.gridwidth = 1;
            checkoutPanel.add(checkoutCreditCardNumLabel, con);
            con.gridx = 2;
            con.gridwidth = 3;
            //  checkout Billing info
            checkoutPanel.add(checkoutCreditCardNumTF, con);

            con.gridy = 12;
            con.gridx = 1;
            con.gridwidth = 1;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutCreditCardExpLabel, con);
            con.gridx = 2;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutCreditCardExpTF, con);
            con.gridx = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutCreditCardCVV, con);
            con.gridx = 4;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutCreditCardCVVTF, con);

            con.gridy = 13;
            con.gridx = 1;
            checkoutPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 14;
            con.gridx = 1;
            con.gridwidth = 1;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutBillingStreetNumLabel, con);
            con.gridx = 2;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutBillingStreetNumTF, con);
            con.gridx = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutBillingStreetNameLabel, con);
            con.gridx = 4;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutBillingStreetNameTF, con);
            con.gridx = 5;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutBillingApartmentLabel, con);
            con.gridx = 6;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutBillingApartmentTF, con);

            con.gridy = 15;
            con.gridx = 1;
            con.gridwidth = 1;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutBillingCityLabel, con);
            con.gridx = 4;
            checkoutPanel.add(checkoutBillingProvinceLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutBillingCityTF, con);
            con.gridx = 5;
            con.gridwidth = 1;
            checkoutPanel.add(checkoutBillingProvinceCB, con);

            con.gridy = 16;
            con.gridx = 1;
            con.gridwidth = 1;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutBillingCountryLabel, con);
            con.gridx = 4;
            checkoutPanel.add(checkoutBillingPostalCodeLabel, con);
            con.gridx = 2;
            con.gridwidth = 2;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(checkoutBillingCountryTF, con);
            con.gridx = 5;
            con.anchor = GridBagConstraints.LINE_START;
            checkoutPanel.add(checkoutBillingPostalCodeTF, con);

            con.gridy = 17;
            con.gridx = 1;
            checkoutPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 18;
            con.gridx = 4;
            con.gridwidth = 1;
            con.anchor = GridBagConstraints.LINE_END;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutTotalPriceLabel, con);
            con.gridx = 5;
            con.anchor = GridBagConstraints.CENTER;
            // JLabels
            JLabel checkoutTotalPriceValueLabel = new JLabel(orderCost, JLabel.CENTER);
            checkoutPanel.add(checkoutTotalPriceValueLabel, con);
            con.gridwidth = 2;
            con.gridx = 6;
            con.anchor = GridBagConstraints.LINE_END;
            con.fill = GridBagConstraints.NONE;
            checkoutPanel.add(submitOrder, con);

            con.gridy = 19;
            con.gridx = 5;
            con.gridwidth = 2;
            con.anchor = GridBagConstraints.LINE_END;
            JLabel checkoutSuccessLabel = new JLabel("", JLabel.CENTER);
            checkoutPanel.add(checkoutSuccessLabel, con);

            con.gridy = 20; // shift everything to the top and center
            con.gridx = 0;
            con.weighty = 1.0;
            con.weightx = 1.0;
            con.gridwidth = 1;
            con.anchor = GridBagConstraints.CENTER;
            con.fill = GridBagConstraints.BOTH;
            checkoutPanel.add(Box.createGlue(), con);
            con.gridx = 6;
            checkoutPanel.add(Box.createGlue(), con);

            checkoutPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            DatabaseQueries.lookForaUser(username);

            c.add(checkoutPanel);
            FrontEndUtilities.configureFrame(this);
        }
    }

    /**
     * Place an order for books within a user's cart.
     */
    private void placeOrder() {

        // Check each of the address fields :(
        // Street Numbers
        if (checkoutshippingStreetNumTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Shipping street number cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingStreetNumTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Billing street number cannot be empty.");
            return;
        }
        try {
            Double.parseDouble(checkoutShippingStreetNumTF.getText());
            if (!sameAsBilling) {
                Double.parseDouble(checkoutBillingStreetNumTF.getText());
            }
        } catch (NumberFormatException ex) {
            checkoutErrorLabel.setText("Registration Failed. Street numbers cannot contain letters.");
            return;
        }

        // Street Names
        if (checkoutShippingStreetNameTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Shipping street name cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingStreetNameTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Billing street name cannot be empty.");
            return;
        }
        // Cities
        if (FrontEndUtilities.check(checkoutShippingCityTF.getText())) {
            checkoutErrorLabel.setText("Registration Failed. Shipping city cannot contain numerical values.");
            return;
        }
        if (FrontEndUtilities.check(checkoutBillingCityTF.getText())) {
            checkoutErrorLabel.setText("Registration Failed. Billing city cannot contain numerical values.");
            return;
        }
        if (checkoutCityTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Shipping city name cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingCityTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Billing city name cannot be empty.");
            return;
        }
        if (checkoutShippingProvinceCB.getSelectedIndex() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Please select a shipping province.");
            return;
        }
        if (!sameAsBilling && checkoutBillingProvinceCB.getSelectedIndex() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Please select a billing province.");
            return;
        }
        // Countries
        if (FrontEndUtilities.check(checkoutCountryTF.getText())) {
            checkoutErrorLabel.setText("Registration Failed. Shipping country cannot contain numerical values.");
            return;
        }
        if (FrontEndUtilities.check(checkoutBillingCountryTF.getText())) {
            checkoutErrorLabel.setText("Registration Failed. Billing country cannot contain numerical values.");
            return;
        }
        if (checkoutCountryTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Shipping country cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingCountryTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Billing country cannot be empty.");
            return;
        }
        // Postal Code
        if (checkoutPostalCodeTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Shipping postal code cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingPostalCodeTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Registration Failed. Billing postal code cannot be empty.");
            return;
        }
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
                case "Cancel Checkout" -> {
                    this.dispose();
                    new UserScreen(username); // checkoutScreen
                }
                case "Confirm Order" -> placeOrder(); // checkoutScreen
                default -> System.out.println("Error");
            }
        }
    }
}
