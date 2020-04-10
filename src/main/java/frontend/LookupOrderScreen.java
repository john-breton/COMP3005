package frontend;

import backend.DatabaseQueries;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class LookupOrderScreen extends JFrame implements ActionListener {
    /* JTextFields */
    private final JTextField orderNumber = new JTextField(16);

    /* JLabels */
    private final JLabel invalidOrderLabel = new JLabel();
    private final JLabel orderStatus = new JLabel("");
    private final JLabel dateOrderPlaced = new JLabel("");
    private final JLabel trackingNumber = new JLabel("");

    public LookupOrderScreen() {

        Container c = this.getContentPane();
        // Clear GUI in order to reload
        this.setPreferredSize(new Dimension(400, 200));
        if (this.getJMenuBar() != null) this.getJMenuBar().setVisible(false);
        c.removeAll();

        /* JPanels */
        JPanel orderScreen = new JPanel(new GridBagLayout());
        orderScreen.setBorder(new EmptyBorder(10, 10, 10, 10));

        /* JLabels */
        JLabel orderNumberLabel = new JLabel("<html><u>Order Number</u>: </html>");
        JLabel trackingNumberLabel = new JLabel("<html><u>Tracking Number</u>: </html>");
        JLabel dateOrderPlacedLabel = new JLabel("<html><u>Date Placed</u>: </html>");
        JLabel orderStatusLabel = new JLabel("<html><u>Status</u>: </html>");

        /* JButtons */
        JButton cancelLookup = FrontEndUtilities.formatButton("Cancel Lookup");
        JButton lookupOrder = FrontEndUtilities.formatButton("Lookup Order");

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
        orderScreen.add(orderNumber, con);

        con.gridy = 2;
        con.gridx = 0;
        orderScreen.add(trackingNumberLabel, con);
        con.gridx = 1;
        orderScreen.add(trackingNumber, con);

        con.gridy = 3;
        con.gridx = 0;
        orderScreen.add(dateOrderPlacedLabel, con);
        con.gridx = 1;
        orderScreen.add(dateOrderPlaced, con);

        con.gridy = 4;
        con.gridx = 0;
        orderScreen.add(orderStatusLabel, con);
        con.gridx = 1;
        orderScreen.add(orderStatus, con);

        con.gridy = 5;
        con.gridx = 0;
        con.gridwidth = 100;
        con.anchor = GridBagConstraints.CENTER;
        orderScreen.add(invalidOrderLabel, con);
        invalidOrderLabel.setForeground(Color.RED);

        c.add(orderScreen);

        FrontEndUtilities.configureFrame(this);
    }

    /**
     * Lookup an order based on a tracking number.
     */
    private void lookUpOrder() {
        trackingNumber.setText("");
        dateOrderPlaced.setText("");
        orderStatus.setText("");
        invalidOrderLabel.setText("");
        if (orderNumber.getText().equals("")) {
            invalidOrderLabel.setText("Please enter an order number.");
            return;
        }
        ArrayList<String> orderInfo = DatabaseQueries.lookForanOrder(orderNumber.getText());
        if (Objects.requireNonNull(orderInfo).isEmpty()) {
            invalidOrderLabel.setText("No order found.");
        } else {
            trackingNumber.setText(orderInfo.get(0));
            dateOrderPlaced.setText(orderInfo.get(1).substring(0, 11));
            orderStatus.setText(orderInfo.get(2));
        }
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
            case "Lookup Order" -> lookUpOrder();
            default -> System.out.println("Error");
        }
    }
}
