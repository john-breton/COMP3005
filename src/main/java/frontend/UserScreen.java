package frontend;

import backend.DatabaseQueries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class UserScreen extends JFrame implements ActionListener {

    private static final JTextField userSearchTF = new JTextField();
    private static final JComboBox<String> searchFilters = new JComboBox<>(FrontEndUtilities.searchFilterArr);
    private static final JComboBox<String> resultFilters = new JComboBox<>(FrontEndUtilities.resultFilterArr);
    private final ButtonGroup cartItems = new ButtonGroup();
    private final JLabel errorLabel = new JLabel("");
    private final JLabel totalPrice;
    private final JPanel searchResult = new JPanel(new GridLayout(1, 1));
    private final JPanel cart = new JPanel(new GridLayout(1, 1));
    private ArrayList<JButton> bookButtons = new ArrayList<>();
    private final ArrayList<JToggleButton> cartButtons = new ArrayList<>();
    private static String username;

    public UserScreen(String username) {
        this.username = username;
        totalPrice = new JLabel("$0.00", JLabel.CENTER);
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
        userSearchTF.setMinimumSize(new Dimension(200, 20));
        userSearchTF.setMaximumSize(new Dimension(200, 20));
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
        con.gridx = 2;
        searchAndResults.add(errorLabel, con);

        con.gridx = 0;
        con.gridy = 3;
        con.weighty = 1.0;
        con.weightx = 1.0;
        con.gridwidth = 4;
        con.insets = new Insets(5, 5, 5, 5);
        con.fill = GridBagConstraints.BOTH;
        con.anchor = GridBagConstraints.CENTER;
        searchResult.setBorder(BorderFactory.createLineBorder(Color.black));
        cart.setBorder(BorderFactory.createLineBorder(Color.black));
        searchResult.setSize(500, 600);
        for (JButton btn : bookButtons) {
            searchResult.add(btn);
        }
        JScrollPane scrollResults = new JScrollPane(searchResult,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        searchAndResults.add(scrollResults, con);

        searchAndResults.setMinimumSize(searchResultDimension);

        // setup right side of window
        cartPanel.setLayout(new BorderLayout());
        cartLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        cartPanel.add(cartLabel, BorderLayout.PAGE_START);
        cartPanel.add(checkoutPanel, BorderLayout.PAGE_END);
        JScrollPane currentCart = new JScrollPane(cart,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cartPanel.add(currentCart, BorderLayout.CENTER);
        cartPanel.setPreferredSize(cartDimension);

        userView.resetToPreferredSizes();
        userView.setLeftComponent(searchAndResults);
        userView.setRightComponent(cartPanel);


        c.add(userView);
        checkForExistingCart(this.username);
        FrontEndUtilities.configureFrame(this);
    }

    /**
     * Attempts to search for a book.
     */
    private void search() {
        String searchText = userSearchTF.getText();
        searchResult.removeAll();
        searchResult.repaint();
        bookButtons = new ArrayList<>();
        errorLabel.setText("");

        // TODO make this a label
        if (searchText.isEmpty()) {
            errorLabel.setText("There's nothing here to search with!");
            return;
        }
        String searchTerm = Objects.requireNonNull(searchFilters.getSelectedItem()).toString().toLowerCase();
        if (searchTerm.equals("publisher")) {
            searchTerm = "pub_name";
        } else if (searchTerm.equals("genre")) {
            searchTerm = "name";
        }

        // Execute the search.
        ArrayList<Object> results = DatabaseQueries.lookForaBook(searchText, searchTerm);
        if (results == null) {
            errorLabel.setText("Did not find any books, please try again!");
        } else {
            if (results.size() / 11 > 4) {
                searchResult.setLayout(new GridLayout(results.size() / 11, 1));
            } else {
                searchResult.setLayout(new GridLayout(4, 1));
            }
            for (int i = 0; i < results.size() / 11; i++) {
                createBook(results, i * 11);
            }
            for (JButton btn : bookButtons) {
                searchResult.add(btn);
            }
        }

        this.invalidate();
        this.validate();
        this.repaint();
    }

    /**
     * Creates and adds a book as a button after a search has been completed.
     *
     * @param results The data used to construct a book JButton.
     */
    private void createBook(ArrayList<Object> results, int i) {
        JButton book = FrontEndUtilities.formatButton("");
        book.setMinimumSize(new Dimension(400, 50));
        book.addActionListener(this);
        StringBuilder text = new StringBuilder();
        text.append("<html>");
        text.append("<u>ISBN:</u> ").append(results.get(i).toString()).append("<br/>");
        text.append("<u>Title:</u> ").append(results.get(i + 1).toString()).append("<br/>");
        text.append("<u>Author(s):</u> ");
        String[] authors = results.get(i + 9).toString().split(",");
        int count = 0;
        for (String curr : authors) {
            if (count == 0 && count == authors.length - 1) {
                text.append(curr, 1, curr.length() - 1);
            } else if (count == authors.length - 1) {
                text.append(curr, 0, curr.length() - 1);
            } else if (count == 0) {
                text.append(curr.substring(1)).append(", ");
            } else {
                text.append(curr).append(", ");
            }
            count++;
        }
        text.append("<br/><u>Edition:</u> ").append(results.get(i + 2).toString()).append("<br/>");
        text.append("<u>Page Count:</u> ").append(results.get(i + 3).toString()).append("<br/>");
        text.append("<u>Price:</u> $").append(results.get(i + 4).toString()).append("<br/>");
        text.append("<u>Stock:</u> ").append(results.get(i + 6).toString()).append("<br/>");
        text.append("<u>Publisher:</u> ").append(results.get(i + 7).toString()).append("<br/>");
        text.append("<u>Year:</u> ").append(results.get(i + 8).toString()).append("<br/>");
        text.append("</html>");
        book.setText(text.toString());
        book.setHorizontalAlignment(SwingConstants.LEFT);
        bookButtons.add(book);
    }

    /**
     * Add an item to the user's cart.
     *
     * @param text The text that will be parsed to create the cart item.
     */
    private void addToCart(String text) {
        String[] splitEmUp = text.split("Title");
        String title = splitEmUp[1].substring(6);
        StringBuilder usefulTitle = new StringBuilder();
        int i = 0;
        while ((title.charAt(i) != '<')) {
            usefulTitle.append(title.charAt(i));
            i++;
        }
        // Book already in cart
        for (JToggleButton btn : cartButtons) {
            if (btn.getName().equals(usefulTitle.toString())) {
                return;
            }
        }
        // Book was not in cart.
        JToggleButton item = new JToggleButton("<html><u>Title</u>: " + usefulTitle.toString() + "<br/><u>Quantity</u>: 1</html>");
        item.setBackground(Color.WHITE);
        item.setHorizontalAlignment(SwingConstants.LEFT);
        item.setName(usefulTitle.toString());
        if (cartButtons.size() > 4) {
            cart.setLayout(new GridLayout(cartButtons.size() + 1, 1));
        } else {
            cart.setLayout(new GridLayout(4, 1));
        }
        cartItems.add(item);
        cartButtons.add(item);
        for (JToggleButton btn : cartButtons) {
            cart.add(btn);
        }
        priceUpdate(text);

        this.invalidate();
        this.validate();
        this.repaint();
    }

    /**
     * Update the total price of a user's cart.
     *
     * @param text The text that will be parsed to find the price of the book.
     */
    private void priceUpdate(String text) {
        String[] splitEmUp = text.split("Price");
        String price = splitEmUp[1].substring(7);
        StringBuilder usefulPrice = new StringBuilder();
        int i = 0;
        while ((price.charAt(i) != '<')) {
            usefulPrice.append(price.charAt(i));
            i++;
        }
        double currPrice = Double.parseDouble(totalPrice.getText().substring(1));
        double lastPrice = Double.parseDouble(usefulPrice.toString());
        currPrice += lastPrice;
        totalPrice.setText("$" + currPrice);
    }

    /**
     * Check if a user has an existing cart with items within it.
     */
    private void checkForExistingCart(String username) {
        
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
            default -> addToCart(((JButton) o).getText());
        }
    }
}
