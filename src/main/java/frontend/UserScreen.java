package frontend;

import backend.DatabaseQueries;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The UserScreen class is responsible for allowing a user to search,
 * sort, and add books to their cart. Users can increase the quantity of any item
 * in their cart, as well as deleting items from their cart. Once a user has a
 * satisfiable amount of books in their cart, their can checkout and place an order.
 * Cart's persist even if a user logs out, such that their cart will be ready when
 * they return.
 *
 * @author John Breton, Ryan Godfrey
 * @version April 14th, 2020
 */
public class UserScreen extends JFrame implements ActionListener {

    private final JTextField userSearchTF = new JTextField();
    private final JComboBox<String> searchFilters = new JComboBox<>(FrontEndUtilities.searchFilterArr);
    private final ButtonGroup cartItems = new ButtonGroup();
    private final JLabel errorLabel = new JLabel("");
    private final JLabel totalPrice;
    private final JPanel searchResult = new JPanel(new GridLayout(1, 1));
    private final JPanel cart = new JPanel(new GridLayout(1, 1));
    private ArrayList<JButton> bookButtons = new ArrayList<>();
    private final ArrayList<JToggleButton> cartButtons = new ArrayList<>();
    private final JButton removeItemFromCart = FrontEndUtilities.formatButton("-");
    private final JButton addItemToCart = FrontEndUtilities.formatButton("+");
    private final JButton checkoutButton = FrontEndUtilities.formatButton("Checkout");
    private final String username;
    private String cartID;

    public UserScreen(String username) {
        removeItemFromCart.setEnabled(false);
        addItemToCart.setEnabled(false);
        checkoutButton.setEnabled(false);

        this.username = username;
        totalPrice = new JLabel("$0.00", JLabel.CENTER);
        this.setPreferredSize(new Dimension(798, 850));
        if (this.getJMenuBar() != null) this.getJMenuBar().setVisible(false);
        Container c = this.getContentPane();
        c.removeAll();

        searchFilters.setBackground(Color.WHITE);
        JComboBox<String> resultFilters = new JComboBox<>(FrontEndUtilities.resultFilterArr);
        resultFilters.setBackground(Color.WHITE);

        // Dimensions
        Dimension addRemoveButtonDimensions = new Dimension(25, 25);
        Dimension searchResultDimension = new Dimension(500, c.getHeight());
        Dimension cartDimension = new Dimension(c.getWidth() - (int) searchResultDimension.getWidth(), c.getHeight());

        /* Components */
        // Panels and Panes
        JSplitPane userView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        userView.setBackground(Color.WHITE);
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
        addItemToCart.setMargin(new Insets(0, 0, 0, 0));
        removeItemFromCart.setMargin(new Insets(0, 0, 0, 0));
        JButton searchButton = FrontEndUtilities.formatButton("Search");
        JButton logoutButton = FrontEndUtilities.formatButton("Logout");

        // ActionListeners
        addItemToCart.addActionListener(this);
        removeItemFromCart.addActionListener(this);
        checkoutButton.addActionListener(this);
        searchButton.addActionListener(this);
        logoutButton.addActionListener(this);

        /* Setup Panels */
        // Price panel
        addItemToCart.setPreferredSize(addRemoveButtonDimensions);
        pricePanel.add(addItemToCart);
        pricePanel.add(totalPriceLabel);
        pricePanel.add(totalPrice);
        removeItemFromCart.setPreferredSize(addRemoveButtonDimensions);
        pricePanel.add(removeItemFromCart);

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
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        searchAndResults.setBackground(Color.WHITE);
        searchAndResults.add(scrollResults, con);

        searchAndResults.setMinimumSize(searchResultDimension);

        // setup right side of window
        cartPanel.setLayout(new BorderLayout());
        cartLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        cartPanel.add(cartLabel, BorderLayout.PAGE_START);
        cartPanel.add(checkoutPanel, BorderLayout.PAGE_END);
        JScrollPane currentCart = new JScrollPane(cart,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cartPanel.add(currentCart, BorderLayout.CENTER);
        cartPanel.setPreferredSize(cartDimension);

        userView.resetToPreferredSizes();
        userView.setLeftComponent(searchAndResults);
        userView.setRightComponent(cartPanel);
        cartPanel.setBackground(Color.WHITE);
        checkoutPanel.setBackground(Color.WHITE);
        pricePanel.setBackground(Color.WHITE);

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
        String text = buildBookString(results, i);
        book.setText(text);
        book.setHorizontalAlignment(SwingConstants.LEFT);
        bookButtons.add(book);
    }

    @NotNull
    private String buildBookString(ArrayList<Object> results, int i) {
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
        return text.toString();
    }

    /**
     * Add an item to the user's cart.
     *
     * @param text    The text that will be parsed to create the cart item.
     * @param addToDB True if the item should be added to the bask_item table, false otherwise.
     */
    private void addToCart(String text, boolean addToDB) {
        String[] splitEmUp = text.split("Title");
        String[] findISBN = text.split("ISBN");
        String[] findPrice = text.split("Price");
        String price = findPrice[1].substring(6);
        String isbn = findISBN[1].substring(6);
        String title = splitEmUp[1].substring(6);
        StringBuilder usefulTitle = new StringBuilder();
        StringBuilder usefulISBN = new StringBuilder();
        StringBuilder usefulPrice = new StringBuilder();
        StringBuilder usefulQuantity = new StringBuilder();
        int i = 0;
        while ((title.charAt(i) != '<')) {
            usefulTitle.append(title.charAt(i));
            i++;
        }
        i = 0;
        while ((isbn.charAt(i) != '<')) {
            usefulISBN.append(isbn.charAt(i));
            i++;
        }
        i = 0;
        while ((price.charAt(i) != '<')) {
            usefulPrice.append(price.charAt(i));
            i++;
        }
        // Book already in cart
        for (JToggleButton btn : cartButtons) {
            if (btn.getName().equals(usefulISBN.toString())) {
                return;
            }
        }
        // Book was not in cart.
        JToggleButton item;
        if (addToDB) {
            DatabaseQueries.addToCart(cartID, usefulISBN.toString());
            item = new JToggleButton("<html><u>Title</u>: " + usefulTitle.toString() + "<br/><u>Price</u>: " + usefulPrice.toString() + "<br/><u>Quantity</u>: 1</html>");
        } else {
            item = new JToggleButton("<html><u>Title</u>: " + usefulTitle.toString() + "<br/><u>Price</u>: " + usefulPrice.toString() + "<br/><u>Quantity</u>: ");
        }
        item.setBackground(Color.WHITE);
        item.setHorizontalAlignment(SwingConstants.LEFT);
        item.setName(usefulISBN.toString());
        item.setMinimumSize(new Dimension(298, 100));
        if (cartButtons.size() > 8) {
            cart.setLayout(new GridLayout(cartButtons.size() + 1, 1));
        } else {
            cart.setLayout(new GridLayout(9, 1));
        }
        cartItems.add(item);
        cartButtons.add(item);
        for (JToggleButton btn : cartButtons) {
            cart.add(btn);
        }
        if (addToDB) {
            priceUpdate(text, true);
        }

        this.invalidate();
        this.validate();
        this.repaint();
    }

    /**
     * Update the total price of a user's cart.
     *
     * @param text     The text that will be parsed to find the price of the book.
     * @param increase True if the price should be increased, false if it should be decreased.
     */
    private void priceUpdate(String text, boolean increase) {
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
        if (increase) {
            currPrice += lastPrice;
        } else {
            currPrice -= lastPrice;
        }
        // Allow checkout
        boolean allowCheckout = currPrice != 0;
        removeItemFromCart.setEnabled(allowCheckout);
        addItemToCart.setEnabled(allowCheckout);
        checkoutButton.setEnabled(allowCheckout);

        totalPrice.setText("$" + currPrice);
    }

    /**
     * Check if a user has an existing cart with items within it.
     * If items exist, these items are populated within the cart.
     */
    private void checkForExistingCart(String username) {
        ArrayList<String> items = DatabaseQueries.checkForCart(username);
        cartID = DatabaseQueries.getCartID(username);
        // Check for cart items.
        if (items != null) {
            // The cart contains items.
            for (int i = 0; i < items.size() / 2; i++) {
                ArrayList<Object> results = DatabaseQueries.lookForaBook(items.get((i * 2) + 1), "isbn");
                String book = buildBookString(Objects.requireNonNull(results), 0);
                addToCart(book, false);
                // Sets the quantity to the correct value.
                cartButtons.get(cartButtons.size() - 1).setText(cartButtons.get(cartButtons.size() - 1).getText() + Integer.parseInt(items.get((i * 2) + 2)) + "<html>");
                // Updates the price to reflect the total quantity of each item contained within the cart.
                for (int j = 0; j < Integer.parseInt(items.get((i * 2) + 2)); j++) {
                    priceUpdate(book, true);
                }
            }
        }
    }

    /**
     * Update the quantity of an item in a user's cart.
     *
     * @param increase True if the quantity should increase by 1, false if it should decrease by 1.
     */
    private void quantityUpdate(boolean increase) {
        if (!cartButtons.isEmpty()) {
            JToggleButton item = null;
            for (JToggleButton btn : cartButtons) {
                if (btn.isSelected()) {
                    item = btn;
                }
            }
            if (item == null) {
                return;
            }
            String isbn = item.getName();
            DatabaseQueries.updateQuantity(cartID, isbn, increase);
            String[] findQuantity = item.getText().split("Quantity");
            String quantity = findQuantity[1].substring(6);
            StringBuilder usefulQuantity = new StringBuilder();
            int i = 0;
            while ((quantity.charAt(i) != '<')) {
                usefulQuantity.append(quantity.charAt(i));
                i++;
            }
            int realQuantity = Integer.parseInt(usefulQuantity.toString());
            item.setText(findQuantity[0] + "</u><u>Quantity</u>: " + (realQuantity + (increase ? 1 : -1)) + "</html>");
            if (realQuantity == 1 && !increase) {
                // Need to remove it from the cart.
                cartButtons.remove(item);
                if (cartButtons.size() > 8) {
                    cart.setLayout(new GridLayout(cartButtons.size() + 1, 1));
                } else {
                    cart.setLayout(new GridLayout(9, 1));
                }
                cart.removeAll();
                for (JToggleButton btn : cartButtons) {
                    cart.add(btn);
                }
            }
            priceUpdate(item.getText(), increase);

            this.invalidate();
            this.validate();
            this.repaint();
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
            case "Logout" -> FrontEndUtilities.confirmLogout(this); // Anywhere and everywhere
            case "+" -> quantityUpdate(true); // User screen
            case "-" -> quantityUpdate(false); // User screen
            case "Checkout" -> {
                this.dispose();
                new CheckoutScreen(username, totalPrice.getText()); // User Screen
            }
            case "Search" -> search(); // User screen
            default -> addToCart(((JButton) o).getText(), true);
        }
    }
}
