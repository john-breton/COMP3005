package frontend;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LookupOrderScreen extends JFrame implements ActionListener {

    public LookupOrderScreen() {

        Container c = this.getContentPane();
        // Clear GUI in order to reload
        this.setPreferredSize(new Dimension(400, 200));
        if (this.getJMenuBar() != null) this.getJMenuBar().setVisible(false);
        c.removeAll();

        /* JPanels */
        JPanel orderScreen = new JPanel(new GridBagLayout());
        orderScreen.setBorder(new EmptyBorder(10, 10, 10, 10));

        /* JButtons */
        JButton cancelLookup = FrontEndUtilities.formatButton("Cancel Lookup");
        JButton lookupOrder = FrontEndUtilities.formatButton("Lookup Order");

        /* JLabels */
        JLabel orderNumberLabel = new JLabel("Order Number: ");
        JLabel trackingNumberLabel = new JLabel("Tracking Number: ");
        JLabel dateOrderPlacedLabel = new JLabel("Date Placed: ");
        JLabel userNameOrderLabel = new JLabel("Order Owner: ");
        JLabel orderStatusLabel = new JLabel("Status: ");

        /* ActionListeners */
        cancelLookup.addActionListener(this);
        lookupOrder.addActionListener(this);

        // Setup Panel
        GridBagConstraints con = new GridBagConstraints();
        con.gridy = 0;
        con.gridx = 0;
        con.gridwidth = 1;
        con.fill = GridBagConstraints.HORIZONTAL;
        con.anchor = GridBagConstraints.LINE_START;
        orderScreen.add(cancelLookup, con);
        con.gridx = 1;
        orderScreen.add(lookupOrder, con);

        con.gridy = 1;
        con.gridx = 0;
        orderScreen.add(orderNumberLabel, con);
        con.gridx = 1;
        // JTextField
        JTextField orderNumber = new JTextField();
        orderScreen.add(orderNumber, con);

        con.gridy = 2;
        con.gridx = 0;
        orderScreen.add(trackingNumberLabel, con);
        con.gridx = 1;
        // JLabels
        JLabel trackingNumber = new JLabel("123456789101112");
        orderScreen.add(trackingNumber, con);

        con.gridy = 3;
        con.gridx = 0;
        orderScreen.add(dateOrderPlacedLabel, con);
        con.gridx = 1;
        JLabel dateOrderPlaced = new JLabel("March 25, 2020");
        orderScreen.add(dateOrderPlaced, con);

        con.gridy = 4;
        con.gridx = 0;
        orderScreen.add(userNameOrderLabel, con);
        con.gridx = 1;
        JLabel userNameOrder = new JLabel("User's Username");
        orderScreen.add(userNameOrder, con);

        con.gridy = 5;
        con.gridx = 0;
        orderScreen.add(orderStatusLabel, con);
        con.gridx = 1;
        JLabel orderStatus = new JLabel("TBH Dont know where it is");
        orderScreen.add(orderStatus, con);

        c.add(orderScreen);

        FrontEndUtilities.configureFrame(this);
    }

    /**
     * Implements ActionListeners for GUI components
     *
     * @param e The ActionEvent that was triggered via a JButton.
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        switch (((JButton) o).getText()) {
            case "Cancel Lookup" -> {
                this.dispose();
                new LoginScreen();
            }
            case "Lookup Order" -> System.out.println("Looking up order");
            default -> System.out.println("Error");
        }
    }
}
