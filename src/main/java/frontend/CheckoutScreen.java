package frontend;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckoutScreen extends JFrame implements ActionListener {

    // JTextFields
    private final JTextField checkoutBillingStreetNumTF = new JTextField(5);
    private final JTextField checkoutBillingStreetNameTF = new JTextField(15);
    private final JTextField checkoutBillingApartmentTF = new JTextField(5);
    private final JTextField checkoutBillingCityTF = new JTextField(15);
    private final JTextField checkoutBillingCountryTF = new JTextField(15);
    private final JTextField checkoutBillingPostalCodeTF = new JTextField(15);

    // JComboBoxes
    final JComboBox<String> checkoutShippingProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr),
            checkoutBillingProvinceCB = new JComboBox<>(FrontEndUtilities.provincesArr);

    private String username;

    /**
     * Creates the "Checkout" interface for the userScreen
     * Accepts new shipping and billing addresses
     * Accepts credit card details
     * <p>
     * TODO: Possibly add a cart view during checkout, but we could also not... Maybe. But that's a "if I feel like it" feature.
     */
    public CheckoutScreen(String username, String orderCost) {
        CheckoutScreen.username = username;
        Container c = this.getContentPane();
        // Clear GUI in order to reload
        this.setPreferredSize(new Dimension(800, 800));
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
            boolean sameAsBilling = !billingSameAsShipping.isSelected();
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
            JLabel checkoutErrorLabel = new JLabel("", JLabel.CENTER);
            checkoutErrorLabel.setForeground(Color.red);
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutErrorLabel, con);

            con.gridy = 1;
            con.gridx = 1;
            con.weightx = 1.0;
            con.gridwidth = 3;
            con.anchor = GridBagConstraints.LINE_END;
            JLabel checkoutUsernameLabel = new JLabel("");
            checkoutPanel.add(checkoutUsernameLabel, con);

            con.gridy = 2;
            con.gridx = 1;
            con.gridwidth = 1;
            con.anchor = GridBagConstraints.LINE_START;
            checkoutPanel.add(checkoutNameLabel, con);
            con.gridx = 2;
            con.gridwidth = 4;
            JLabel checkoutNameField = new JLabel("");
            checkoutPanel.add(checkoutNameField, con);

            con.gridy = 3;
            con.gridx = 1;
            con.gridwidth = 2;
            checkoutPanel.add(checkoutEmailLabel, con);
            con.gridx = 3;
            con.gridwidth = 4;
            JLabel checkoutEmailConfirm = new JLabel("");
            checkoutPanel.add(checkoutEmailConfirm, con);

            con.gridy = 4;
            con.gridx = 1;
            checkoutPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 5;
            con.gridx = 1;
            con.gridwidth = 3;
            checkoutPanel.add(checkoutShippingLabel, con);

            con.gridy = 6;
            con.gridx = 1;
            con.gridwidth = 1;
            checkoutPanel.add(checkoutStreetNumLabel, con);
            con.gridx = 2;
            con.fill = GridBagConstraints.NONE;
            JTextField checkoutStreetNumTF = new JTextField(5);
            checkoutPanel.add(checkoutStreetNumTF, con);
            con.gridx = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutStreetNameLabel, con);
            con.gridx = 4;
            con.fill = GridBagConstraints.NONE;
            JTextField checkoutStreetNameTF = new JTextField(15);
            checkoutPanel.add(checkoutStreetNameTF, con);
            con.gridx = 5;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutApartmentLabel, con);
            con.gridx = 6;
            con.fill = GridBagConstraints.NONE;
            JTextField checkoutApartmentTF = new JTextField(5);
            checkoutPanel.add(checkoutApartmentTF, con);

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
            JTextField checkoutCityTF = new JTextField(15);
            checkoutPanel.add(checkoutCityTF, con);
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
            JTextField checkoutCountryTF = new JTextField(15);
            checkoutPanel.add(checkoutCountryTF, con);
            con.gridx = 5;
            con.anchor = GridBagConstraints.LINE_START;
            JTextField checkoutPostalCodeTF = new JTextField(15);
            checkoutPanel.add(checkoutPostalCodeTF, con);

            con.gridy = 9;
            con.gridx = 1;
            checkoutPanel.add(Box.createRigidArea(spacer), con);

            con.gridy = 10;
            con.gridx = 1;
            con.gridwidth = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
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
            JTextField checkoutCreditCardNumTF = new JTextField(15);
            checkoutPanel.add(checkoutCreditCardNumTF, con);

            con.gridy = 12;
            con.gridx = 1;
            con.gridwidth = 1;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutCreditCardExpLabel, con);
            con.gridx = 2;
            con.fill = GridBagConstraints.NONE;
            JTextField checkoutCreditCardExpTF = new JTextField(5);
            checkoutPanel.add(checkoutCreditCardExpTF, con);
            con.gridx = 3;
            con.fill = GridBagConstraints.HORIZONTAL;
            checkoutPanel.add(checkoutCreditCardCVV, con);
            con.gridx = 4;
            con.fill = GridBagConstraints.NONE;
            JTextField checkoutCreditCardCVVTF = new JTextField(5);
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

            c.add(checkoutPanel);
            FrontEndUtilities.configureFrame(this);
        }
    }

    /**
     * Place an order for books within a user's cart.
     */
    private void placeOrder() {

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
