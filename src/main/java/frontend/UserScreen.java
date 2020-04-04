package frontend;

import backend.DatabaseQueries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UserScreen extends JFrame implements ActionListener {

    private static final JTextField userSearchTF = new JTextField();
    private static final JComboBox<String> searchFilters = new JComboBox<>(FrontEndUtilities.searchFilterArr);
    private static final JComboBox<String> resultFilters = new JComboBox<>(FrontEndUtilities.resultFilterArr);
    private final JPanel searchResult = new JPanel();

    public UserScreen() {
        JLabel totalPrice = new JLabel("$0.00", JLabel.CENTER);
        this.setPreferredSize(new Dimension(798, 850));
        if (this.getJMenuBar() != null) this.getJMenuBar().setVisible(false);
        Container c = this.getContentPane();
        c.removeAll();

        searchFilters.setBackground(Color.WHITE);
        resultFilters.setBackground(Color.WHITE);

        // Dimensions
        Dimension addRemoveButtonDimensions = new Dimension(25, 25);
        Dimension searchResultDimension = new Dimension(500, c.getHeight());
        Dimension cartDimension = new Dimension(c.getWidth() - (int) searchResultDimension.getWidth(), c.getHeight());

        /* Components */
        // Panels and Panes
        JSplitPane userView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        userView.setEnabled(false);
        JPanel cartPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel checkoutPanel = new JPanel();
        JPanel searchAndResults = new JPanel();

        // Labels
        JLabel searchLabel = new JLabel("Search: ");
        JLabel cartLabel = new JLabel("Cart: ");
        JLabel filterLabel = new JLabel("Sort by: ");
        JLabel totalPriceLabel = new JLabel("Total Price: ");

        // Buttons
        JButton addToCart = FrontEndUtilities.formatButton("+");
        JButton removeFromCart = FrontEndUtilities.formatButton("-");
        JButton checkoutButton = FrontEndUtilities.formatButton("Checkout");
        JButton searchButton = FrontEndUtilities.formatButton("Search");
        JButton logoutButton = FrontEndUtilities.formatButton("Logout");

        // ActionListeners
        addToCart.addActionListener(this);
        removeFromCart.addActionListener(this);
        checkoutButton.addActionListener(this);
        searchButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // ScrollPanes
        JScrollPane currentCart = new JScrollPane();

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
        Insets leftEdge = new Insets(5, 5, 5, 0);
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
        userSearchTF.setPreferredSize(new Dimension(200, 20));
        searchAndResults.add(userSearchTF, con);

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
        searchResult.setBorder(BorderFactory.createLineBorder(Color.black));

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
        FrontEndUtilities.configureFrame(this);
    }

    /**
     * Attempts to search for a book.
     */
    private void search() {
        String searchText = userSearchTF.getText();

        // TODO make this a label
        if (searchText.isEmpty()) {
            System.out.println("There's nothing here to search with!");
            return;
        }
        String searchTerm = searchFilters.getSelectedItem().toString().toLowerCase();
        if (searchTerm.equals("title")) {
            searchTerm = "name";
        } else if (searchTerm.equals("publisher")) {
            searchTerm = "pub_name";
        }

        // Execute the search.
        ArrayList<Object> results = DatabaseQueries.lookForaBook(searchText, searchTerm);

        if (searchResult == null) {
            System.out.println("Did not find any books matching that ISBN, please try again!");
        } else {
            JButton book = FrontEndUtilities.formatButton("");
            book.setMinimumSize(new Dimension(600, 100));
            StringBuilder text = new StringBuilder();
            text.append("<html>");
            text.append("<u>ISBN:</u> " + results.get(0).toString() + "<br/>");
            text.append("<u>Title:</u> " + results.get(1).toString() + "<br/>");
            text.append("<u>Author(s):</u> ");
            String[] authors = results.get(9).toString().split(",");
            int count = 0;
            for (String curr: authors) {
                if (count == 0 && count == authors.length - 1) {
                    text.append(curr, 1, curr.length() - 1);
                } else if (count == authors.length - 1) {
                    text.append(curr, 0, curr.length() - 1);
                } else if (count == 0) {
                    text.append(curr.substring(1));
                } else {
                    text.append(curr + ", ");
                }
                count++;
            }
            text.append("<br/><u>Edition:</u> " + results.get(2).toString() + "<br/>");
            text.append("<u>Page Count:</u> " + results.get(3).toString() + "<br/>");
            text.append("<u>Price:</u> $" + results.get(4).toString() + "<br/>");
            text.append("<u>Stock:</u> " + results.get(6).toString() + "<br/>");
            text.append("<u>Publisher:</u> " + results.get(7).toString() + "<br/>");
            text.append("<u>Year:</u> " + results.get(8).toString() + "<br/>");
            text.append("</html>");
            book.setText(text.toString());
            System.out.println(text);
            searchResult.add(book);
        }

        this.invalidate();
        this.validate();
        this.repaint();

    }

    /**
     * Implements ActionListeners for GUI components
     *
     * @param e The ActionEvent that was triggered via a JButton.
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        switch (((JButton) o).getText()) {
            case "Logout" -> FrontEndUtilities.confirmLogout(this); // Anywhere and everywhere
            case "+" -> System.out.println("Item Added"); // User screen
            case "-" -> System.out.println("Item removed"); // User screen
            case "Checkout" -> {
                this.dispose();
                new CheckoutScreen(); // User Screen
            }
            case "Search" -> search(); // User screen
            default -> System.out.println("Error");
        }
    }
}
