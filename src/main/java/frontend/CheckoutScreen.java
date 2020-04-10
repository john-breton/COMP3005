package frontend;

import backend.DatabaseQueries;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class CheckoutScreen extends JFrame implements ActionListener {
    /* JButtons */
    private final JButton cancelOrder = FrontEndUtilities.formatButton("Cancel Checkout"),
            submitOrder = FrontEndUtilities.formatButton("Confirm Order");

    // JTextFields
    private final JTextField checkoutShippingStreetNumTF = new JTextField(5),
            checkoutShippingStreetNameTF = new JTextField(10),
            checkoutShippingApartmentTF = new JTextField(5),
            checkoutShippingCityTF = new JTextField(10),
            checkoutShippingCountryTF = new JTextField(10),
            checkoutShippingPostalCodeTF = new JTextField(10),

    checkoutBillingStreetNumTF = new JTextField(5),
            checkoutBillingStreetNameTF = new JTextField(10),
            checkoutBillingApartmentTF = new JTextField(5),
            checkoutBillingCityTF = new JTextField(10),
            checkoutBillingCountryTF = new JTextField(10),
            checkoutBillingPostalCodeTF = new JTextField(10),

    checkoutCreditCardNumTF = new JTextField(10),
            checkoutCreditCardExpTF = new JTextField(5),
            checkoutCreditCardCVVTF = new JTextField(5);

    // JComboBoxes
    final JComboBox<String> checkoutShippingProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr),
            checkoutBillingProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr);

    /* JCheckBoxes */
    private final JCheckBox billingSameAsShipping = new JCheckBox("Billing Address is the same as Shipping Address");

    // JLabels
    private final JLabel checkoutErrorLabel = new JLabel("", JLabel.CENTER);

    // Current username
    private final String username;
    private final String totalCost;

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
        totalCost = orderCost;
        ArrayList<Object> userInfo = DatabaseQueries.lookForaUser(username);
        Container c = this.getContentPane();
        // Clear GUI in order to reload
        this.setPreferredSize(new Dimension(800, 500));
        this.setMaximumSize(new Dimension(800, 500));
        if (this.getJMenuBar() != null) this.getJMenuBar().setVisible(false);
        c.removeAll();

        // Setup Components

        /* Panel */
        final GridBagLayout layout = new GridBagLayout();
        JPanel checkoutPanel = new JPanel(layout);
        /*{

            @Override
            public void paint(Graphics g)
            {
                super.paint(g);
                int[][] dims = layout.getLayoutDimensions();
                g.setColor(Color.BLUE);
                int x = 0;
                for (int add : dims[0])
                {
                    x += add;
                    g.drawLine(x, 0, x, getHeight());
                }
                int y = 0;
                for (int add : dims[1])
                {
                    y += add;
                    g.drawLine(0, y, getWidth(), y);
                }
            }

        };*/
        /* JLabels */
        JLabel checkoutTotalPriceLabel = new JLabel("Total Price: ", JLabel.RIGHT),
                checkoutUserLabel = new JLabel("<html><u>User</u>: " + Objects.requireNonNull(userInfo).get(0) + "</html>"),
                checkoutNameLabel = new JLabel("<html><u>Name</u>: " + userInfo.get(2) + " " + userInfo.get(3) + "</html>"),
                checkoutEmailLabel = new JLabel("<html><u>Email will be sent to</u>: " + userInfo.get(4) + "</html>"),
                // checkout shipping address info
                checkoutShippingLabel = new JLabel("Confirm Shipping Address"),
                checkoutStreetNumLabel = new JLabel("*Street Number: "),
                checkoutStreetNameLabel = new JLabel("*Street Name: ", JLabel.RIGHT),
                checkoutApartmentLabel = new JLabel("Apartment: ", JLabel.RIGHT),
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

        /* Setup checkoutPanel */
        // Get the addresses of the user.
        ArrayList<Object> addresses = DatabaseQueries.lookForanAddressWithID(username);
        boolean sameAddress = (Objects.requireNonNull(addresses).get(0).toString().equals(addresses.get(8).toString()) && Objects.requireNonNull(addresses).get(6).toString().equals(addresses.get(14).toString()));

        /* ActionListeners */
        cancelOrder.addActionListener(this);
        submitOrder.addActionListener(this);
        billingSameAsShipping.addActionListener(e -> {
            checkoutBillingStreetNumTF.setEnabled(sameAddress);
            checkoutBillingStreetNameTF.setEnabled(sameAddress);
            checkoutBillingApartmentTF.setEnabled(sameAddress);
            checkoutBillingCityTF.setEnabled(sameAddress);
            checkoutBillingProvinceCB.setEnabled(sameAddress);
            checkoutBillingCountryTF.setEnabled(sameAddress);
            checkoutBillingPostalCodeTF.setEnabled(sameAddress);
        });

        billingSameAsShipping.setSelected(sameAddress);
        // Populate the fields with values where appropriate.
        if (!sameAddress) {
            checkoutBillingStreetNumTF.setText(addresses.get(8).toString());
            checkoutBillingStreetNameTF.setText(addresses.get(9).toString());
            checkoutBillingApartmentTF.setText(addresses.get(10).toString());
            checkoutBillingCityTF.setText(addresses.get(11).toString());
            checkoutBillingProvinceCB.setSelectedItem(addresses.get(12));
            checkoutBillingCountryTF.setText(addresses.get(13).toString());
            checkoutBillingPostalCodeTF.setText(addresses.get(14).toString());
        }
        checkoutShippingStreetNumTF.setText(addresses.get(0).toString());
        checkoutShippingStreetNameTF.setText(addresses.get(1).toString());
        checkoutShippingApartmentTF.setText(addresses.get(2).toString());
        checkoutShippingCityTF.setText(addresses.get(3).toString());
        checkoutShippingProvinceCB.setSelectedItem(addresses.get(4));
        checkoutShippingCountryTF.setText(addresses.get(5).toString());
        checkoutShippingPostalCodeTF.setText(addresses.get(6).toString());

        // Only enable the billing fields if the user has a different billing and shipping address.
        checkoutBillingStreetNumTF.setEnabled(!sameAddress);
        checkoutBillingStreetNameTF.setEnabled(!sameAddress);
        checkoutBillingApartmentTF.setEnabled(!sameAddress);
        checkoutBillingCityTF.setEnabled(!sameAddress);
        checkoutBillingProvinceCB.setEnabled(!sameAddress);
        checkoutBillingCountryTF.setEnabled(!sameAddress);
        checkoutBillingPostalCodeTF.setEnabled(!sameAddress);

        GridBagConstraints con = new GridBagConstraints();
        Dimension spacer = new Dimension(25, 25);
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
        con.gridwidth = 4;
        con.anchor = GridBagConstraints.LINE_START;
        checkoutUserLabel.setFont(checkoutUserLabel.getFont().deriveFont(Font.BOLD));
        checkoutPanel.add(checkoutUserLabel, con);

        con.gridy = 2;
        con.gridx = 1;
        con.gridwidth = 4;
        checkoutNameLabel.setFont(checkoutNameLabel.getFont().deriveFont(Font.BOLD));
        checkoutPanel.add(checkoutNameLabel, con);

        con.gridy = 3;
        con.gridx = 1;
        con.gridwidth = 4;
        checkoutEmailLabel.setFont(checkoutEmailLabel.getFont().deriveFont(Font.BOLD));
        checkoutPanel.add(checkoutEmailLabel, con);

        con.gridy = 4;
        con.gridx = 1;
        checkoutPanel.add(Box.createRigidArea(spacer), con);

        con.gridy = 5;
        con.gridx = 1;
        con.gridwidth = 4;
        checkoutShippingLabel.setFont(checkoutShippingLabel.getFont().deriveFont(Font.BOLD));
        checkoutPanel.add(checkoutShippingLabel, con);

        con.gridy = 6;
        con.gridx = 1;
        con.gridwidth = 1;
        checkoutPanel.add(checkoutStreetNumLabel, con);
        con.gridx = 2;
        con.fill = GridBagConstraints.NONE;
        checkoutPanel.add(checkoutShippingStreetNumTF, con);
        con.gridx = 3;
        con.fill = GridBagConstraints.HORIZONTAL;
        checkoutPanel.add(checkoutStreetNameLabel, con);
        con.gridx = 4;
        con.fill = GridBagConstraints.NONE;
        checkoutPanel.add(checkoutShippingStreetNameTF, con);
        con.gridx = 5;
        con.fill = GridBagConstraints.HORIZONTAL;
        checkoutPanel.add(checkoutApartmentLabel, con);
        con.gridx = 6;
        con.fill = GridBagConstraints.NONE;
        checkoutPanel.add(checkoutShippingApartmentTF, con);

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
        checkoutPanel.add(checkoutShippingCityTF, con);
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
        checkoutPanel.add(checkoutShippingCountryTF, con);
        con.gridx = 5;
        con.gridwidth = 1;
        con.anchor = GridBagConstraints.LINE_START;
        checkoutPanel.add(checkoutShippingPostalCodeTF, con);

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
        con.gridwidth = 1;
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

    /**
     * Place an order for books within a user's cart.
     */
    private void placeOrder() {
        checkoutErrorLabel.setForeground(Color.RED);
        boolean sameAsBilling = billingSameAsShipping.isSelected();
        // Check each of the address fields :(
        // Street Numbers
        if (checkoutShippingStreetNumTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Shipping street number cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingStreetNumTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Billing street number cannot be empty.");
            return;
        }
        try {
            Double.parseDouble(checkoutShippingStreetNumTF.getText());
            if (!sameAsBilling) {
                Double.parseDouble(checkoutBillingStreetNumTF.getText());
            }
        } catch (NumberFormatException ex) {
            checkoutErrorLabel.setText("Could not place order. Street numbers cannot contain letters.");
            return;
        }

        // Street Names
        if (checkoutShippingStreetNameTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Shipping street name cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingStreetNameTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Billing street name cannot be empty.");
            return;
        }
        // Cities
        if (FrontEndUtilities.check(checkoutShippingCityTF.getText())) {
            checkoutErrorLabel.setText("Could not place order. Shipping city cannot contain numerical values.");
            return;
        }
        if (FrontEndUtilities.check(checkoutBillingCityTF.getText())) {
            checkoutErrorLabel.setText("Could not place order. Billing city cannot contain numerical values.");
            return;
        }
        if (checkoutShippingCityTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Shipping city name cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingCityTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Billing city name cannot be empty.");
            return;
        }
        if (checkoutShippingProvinceCB.getSelectedIndex() == 0) {
            checkoutErrorLabel.setText("Could not place order. Please select a shipping province.");
            return;
        }
        if (!sameAsBilling && checkoutBillingProvinceCB.getSelectedIndex() == 0) {
            checkoutErrorLabel.setText("Could not place order. Please select a billing province.");
            return;
        }
        // Countries
        if (FrontEndUtilities.check(checkoutShippingCountryTF.getText())) {
            checkoutErrorLabel.setText("Could not place order. Shipping country cannot contain numerical values.");
            return;
        }
        if (FrontEndUtilities.check(checkoutBillingCountryTF.getText())) {
            checkoutErrorLabel.setText("Registration Failed. Billing country cannot contain numerical values.");
            return;
        }
        if (checkoutShippingCountryTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Shipping country cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingCountryTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Billing country cannot be empty.");
            return;
        }
        // Postal Code
        if (checkoutShippingPostalCodeTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Shipping postal code cannot be empty.");
            return;
        }
        if (!sameAsBilling && checkoutBillingPostalCodeTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Billing postal code cannot be empty.");
            return;
        }
        // Credit Card number
        if (checkoutCreditCardNumTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Please provide a credit card number.");
            return;
        }
        if (checkoutCreditCardNumTF.getText().length() != 16) {
            checkoutErrorLabel.setText("Could not place order. Invalid credit card number.");
            return;
        }
        try {
            Double.parseDouble(checkoutCreditCardNumTF.getText());
        } catch (NumberFormatException ex) {
            checkoutErrorLabel.setText("Could not place order. Credit card numbers cannot contain letters.");
            return;
        }
        // Credit Card expiry
        if (checkoutCreditCardExpTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Invalid credit card number.");
            return;
        }
        if (checkoutCreditCardExpTF.getText().length() != 4) {
            checkoutErrorLabel.setText("Could not place order. Invalid credit card exipry.");
        }
        try {
            Double.parseDouble(checkoutCreditCardExpTF.getText());
        } catch (NumberFormatException ex) {
            checkoutErrorLabel.setText("Could not place order. Credit card exipry cannot contain letters.");
            return;
        }
        // Credit Card CCV
        if (checkoutCreditCardCVVTF.getText().length() == 0) {
            checkoutErrorLabel.setText("Could not place order. Invalid credit card number.");
            return;
        }
        if (checkoutCreditCardCVVTF.getText().length() != 3) {
            checkoutErrorLabel.setText("Could not place order. Invalid credit card expiry.");
        }
        try {
            Double.parseDouble(checkoutCreditCardCVVTF.getText());
        } catch (NumberFormatException ex) {
            checkoutErrorLabel.setText("Could not place order. Credit card expiry cannot contain letters.");
            return;
        }
        // Generate a tracking number.
        Random r = new Random();
        int low = 10000000;
        int trackingNumber1 = r.nextInt(99999999 - low) + low;
        int trackingNumber2 = r.nextInt(99999999 - low) + low;
        String trackingNumber = String.valueOf(trackingNumber1) + trackingNumber2;
        String orderNumber;

        // All fields are correct. Need to determine if addresses have changed.
        // Do not associate these addresses with a user, but associate them with the order.
        // Case 1: Same billing and shipping.
        DatabaseQueries.addAddress(checkoutShippingStreetNumTF.getText(), checkoutShippingStreetNameTF.getText(), checkoutShippingApartmentTF.getText(), checkoutShippingCityTF.getText(), Objects.requireNonNull(checkoutShippingProvinceCB.getSelectedItem()).toString(), checkoutShippingCountryTF.getText(), checkoutShippingPostalCodeTF.getText());
        if (sameAsBilling) {
            orderNumber = DatabaseQueries.addOrder(trackingNumber, totalCost.substring(1), true);
        } else {
        // Case 2: Different billing and shipping.
            DatabaseQueries.addAddress(checkoutBillingStreetNumTF.getText(), checkoutBillingStreetNameTF.getText(), checkoutBillingApartmentTF.getText(), checkoutBillingCityTF.getText(), Objects.requireNonNull(checkoutBillingProvinceCB.getSelectedItem()).toString(), checkoutBillingCountryTF.getText(), checkoutBillingPostalCodeTF.getText());
            orderNumber = DatabaseQueries.addOrder(trackingNumber, totalCost.substring(1), false);
        }
        checkoutErrorLabel.setForeground(Color.BLACK);
        checkoutErrorLabel.setText("Order successful! Your order number is: " + orderNumber);
        checkoutErrorLabel.setFocusable(true);
        submitOrder.setEnabled(false);
        cancelOrder.setText("Return to Bookstore");
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
                case "Cancel Checkout", "Return to Bookstore" -> {
                    this.dispose();
                    new UserScreen(username); // checkoutScreen
                }
                case "Confirm Order" -> placeOrder(); // checkoutScreen
                default -> System.out.println("Error");
            }
        }
    }
}
