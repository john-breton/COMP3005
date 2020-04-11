package frontend;

import backend.DatabaseQueries;
import backend.Reports;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class AdminScreenUtilities extends AdminScreen {

    public AdminScreenUtilities(String username) {
        super(username);
    }

    /**
     * Populates edit user fields with appropriate data from database
     * and correctly adjusts checkboxes
     */
    public static void fetchEditUserData() {
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
        } else if (editUserSearchTF.getText().equals(username)) {
            defaultAdminViewFields();
            editUserErrorLabel.setText("You cannot edit your own information.");
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
            isUserAdminCB.setSelected(!editSalaryTF.getText().isEmpty());

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

                    editBillStreetNumTF.setText((String) addIter.next());
                    editBillStreetNameTF.setText((String) addIter.next());
                    editBillApartmentTF.setText((String) addIter.next());
                    editBillCityTF.setText((String) addIter.next());
                    editBillProvinceCB.setSelectedItem(addIter.next());
                    editBillCountryTF.setText((String) addIter.next());
                    editBillPostalCodeTF.setText((String) addIter.next());
                    editBillingSameAsShipping.setSelected(true);

                    editBillingSameAsShipping.setSelected( // true if shipping address == billing address
                            editBillStreetNumTF.getText().equals(editShippingStreetNumTF.getText()) &&
                                    editBillStreetNameTF.getText().equals(editShippingStreetNameTF.getText()) &&
                                    editBillApartmentTF.getText().equals(editShippingApartmentTF.getText()) &&
                                    editBillCityTF.getText().equals(editShippingCityTF.getText()) &&
                                    editBillProvinceCB.getSelectedIndex() == editShippingProvinceCB.getSelectedIndex() &&
                                    editBillCountryTF.getText().equals(editShippingCountryTF.getText()) &&
                                    editBillPostalCodeTF.getText().equals(editShippingPostalCodeTF.getText())
                    );

                    if (editBillingSameAsShipping.isSelected()) { // shipping == billing, user can only have 1 shipping/ billing address
                        editBillStreetNumTF.setText("");
                        editBillStreetNameTF.setText("");
                        editBillApartmentTF.setText("");
                        editBillCityTF.setText("");
                        editBillProvinceCB.setSelectedItem("--");
                        editBillCountryTF.setText("");
                        editBillPostalCodeTF.setText("");
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
     *
     * @return true if successful, false otherwise
     */
    public static boolean sendEditUserData() {
        // check if one of the address fields is not empty (they must fill out all fields or none)
        boolean shippingAttempt = !editShippingStreetNumTF.getText().isEmpty() ||
                !editShippingStreetNameTF.getText().isEmpty() ||
                !editShippingApartmentTF.getText().isEmpty() ||
                !editShippingCityTF.getText().isEmpty() ||
                editShippingProvinceCB.getSelectedIndex() != 0 ||
                !editShippingCountryTF.getText().isEmpty() ||
                !editShippingPostalCodeTF.getText().isEmpty();

        boolean billingAttempt = !editBillStreetNumTF.getText().isEmpty() ||
                !editBillStreetNameTF.getText().isEmpty() ||
                !editBillApartmentTF.getText().isEmpty() ||
                !editBillCityTF.getText().isEmpty() ||
                editBillProvinceCB.getSelectedIndex() != 0 ||
                !editBillCountryTF.getText().isEmpty() ||
                !editBillPostalCodeTF.getText().isEmpty();

        boolean addressAttempt = shippingAttempt || billingAttempt;

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
        // Ensure the email field is valid.
        if (!editEmailTF.getText().contains("@")) {
            editUserErrorLabel.setText("Update Failed. Email not valid.");
            return false;
        }
        boolean sameShipAndBill = editBillingSameAsShipping.isSelected();

        // Check each of the address fields
        {
            // Check for empty fields
            if (addressAttempt) {
                if (!isUserAdminCB.isSelected())
                    editSalaryTF.setText(null);
                // Ensure user fills out shipping address first
                if (!shippingAttempt) {
                    editUserErrorLabel.setText("Update Failed. Cannot have billing address without shipping address.");
                    return false;
                }
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
            } else if (editSalaryTF.getText().isEmpty() && isUserAdminCB.isSelected()) {
                editUserErrorLabel.setText("Update Failed. Admins need a salary.");
                return false;
            }

            // Check validity
            try {
                if (!editShippingStreetNumTF.getText().isEmpty()) Double.parseDouble(editShippingStreetNumTF.getText());
                if (!sameShipAndBill) {
                    if (!editBillStreetNumTF.getText().isEmpty()) Double.parseDouble(editBillStreetNumTF.getText());
                }
            } catch (NumberFormatException ex) {
                editUserErrorLabel.setText("Update Failed. Street numbers cannot contain letters.");
                return false;
            }
            try {
                if (isUserAdminCB.isSelected()) {
                    Double.parseDouble(editSalaryTF.getText());
                }
            } catch (NumberFormatException e) {
                confirmAdminReg.setText("Update Failed. Salary cannot contain letters.");
                return false;
            }
            if (FrontEndUtilities.check(editShippingCityTF.getText())) {
                editUserErrorLabel.setText("Update Failed. Shipping city cannot contain numerical values.");
                return false;
            }
            if (!sameShipAndBill && FrontEndUtilities.check(editBillCityTF.getText())) {
                editUserErrorLabel.setText("Update Failed. Billing city cannot contain numerical values.");
                return false;
            }
            if (FrontEndUtilities.check(editShippingCountryTF.getText())) {
                editUserErrorLabel.setText("Update Failed. Shipping country cannot contain numerical values.");
                return false;
            }
            if (!sameShipAndBill && FrontEndUtilities.check(editBillCountryTF.getText())) {
                editUserErrorLabel.setText("Update Failed. Billing country cannot contain numerical values.");
                return false;
            }
            if (!editShippingPostalCodeTF.getText().isEmpty() && editShippingPostalCodeTF.getText().length() != 6) {
                editUserErrorLabel.setText("Registration Failed. Postal Codes must be 6 digits.");
                return false;
            }
            if (!sameShipAndBill && !editBillPostalCodeTF.getText().isEmpty() && editBillPostalCodeTF.getText().length() != 6) {
                editUserErrorLabel.setText("Registration Failed. Postal Codes must be 6 digits.");
                return false;
            }
        }

        // Attempt to update the user in the database.
        if (DatabaseQueries.updateUser(currentUserNameLabel.getText(), new String(editPasswordTF.getPassword()), editFirstNameTF.getText(), editLastNameTF.getText(), editEmailTF.getText())) {
            DatabaseQueries.updateAdmin(currentUserNameLabel.getText(), editSalaryTF.getText());
        } else {
            return false;
        }

        /* If we get here, the following insertion methods will not fail. */
        // Update the user's addresses.
        if (addressAttempt) {
            DatabaseQueries.updateAddress(currentUserNameLabel.getText(), editShippingStreetNumTF.getText(), editShippingStreetNameTF.getText(), editShippingApartmentTF.getText(), editShippingCityTF.getText(), Objects.requireNonNull(editShippingProvinceCB.getSelectedItem()).toString(), editShippingCountryTF.getText(), editShippingPostalCodeTF.getText(), true);
            if (!sameShipAndBill) {
                // Need to add the billing address as a separate address.
                DatabaseQueries.updateAddress(currentUserNameLabel.getText(), editBillStreetNumTF.getText(), editBillStreetNameTF.getText(), editBillApartmentTF.getText(), editBillCityTF.getText(), Objects.requireNonNull(editBillProvinceCB.getSelectedItem()).toString(), editBillCountryTF.getText(), editBillPostalCodeTF.getText(), false);
            } else {
                DatabaseQueries.updateAddress(currentUserNameLabel.getText(), editShippingStreetNumTF.getText(), editShippingStreetNameTF.getText(), editShippingApartmentTF.getText(), editShippingCityTF.getText(), Objects.requireNonNull(editShippingProvinceCB.getSelectedItem()).toString(), editShippingCountryTF.getText(), editShippingPostalCodeTF.getText(), false);
            }
        }
        // Done.
        return true;
    }

    /**
     * Populates the edit book screen with information about a certain book
     */
    public static void fetchEditBookData() {
        ArrayList<Object> updateBookInfo = new ArrayList<>();
        if (!editBookSearchTF.getText().isEmpty())
            updateBookInfo = DatabaseQueries.lookForaBook(editBookSearchTF.getText(), "isbn");

        if (editBookSearchTF.getText().isEmpty()) {
            defaultAdminViewFields();
            editBookErrorLabel.setText("Please enter an ISBN before searching");
        } else if (updateBookInfo == null || updateBookInfo.size() == 0) {
            defaultAdminViewFields();
            editBookErrorLabel.setText("Book not found. Please try again.");
        } else {
            Iterator<Object> bookItr = updateBookInfo.iterator();
            StringBuilder authors = new StringBuilder();
            StringBuilder genres = new StringBuilder();
            editBookErrorLabel.setText(""); // clear errors + search bar
            editBookSearchTF.setText("");

            // update fields
            currentISBNLabel.setText((String) bookItr.next());
            editBookTitleTF.setText((String) bookItr.next());
            editBookTitleTF.setCaretPosition(0);
            editBookVersionTF.setText((String) bookItr.next());
            editBookNumPagesTF.setText((String) bookItr.next());
            editBookPriceTF.setText((String) bookItr.next());
            editBookRoyaltyTF.setText((String) bookItr.next());
            editBookStockTF.setText((String) bookItr.next());
            editBookPublisherTF.setText((String) bookItr.next());
            editBookYearTF.setText((String) bookItr.next());

            // the book has authors...DUH
            if (bookItr.hasNext()) {
                Iterator<String> authItr = ((ArrayList<String>) bookItr.next()).iterator();

                while (authItr.hasNext()) {
                    authors.append(authItr.next());
                    if (authItr.hasNext())
                        authors.append(", ");
                }
            }
            editBookAuthorTF.setText(authors.toString());

            // the book has genres...again DUH
            if (bookItr.hasNext()) {
                Iterator<String> genItr = ((ArrayList<String>) bookItr.next()).iterator();

                while (genItr.hasNext()) {
                    genres.append(genItr.next());
                    if (genItr.hasNext())
                        genres.append(", ");
                }
            }
            editBookGenreTF.setText(genres.toString());

            // enable fields
            currentISBNLabel.setEnabled(true);
            editBookTitleTF.setEnabled(true);
            editBookTitleTF.setEnabled(true);
            editBookVersionTF.setEnabled(true);
            editBookNumPagesTF.setEnabled(true);
            editBookPriceTF.setEnabled(true);
            editBookRoyaltyTF.setEnabled(true);
            editBookStockTF.setEnabled(true);
            editBookPublisherTF.setEnabled(true);
            editBookYearTF.setEnabled(true);
            editBookAuthorTF.setEnabled(true);
            editBookGenreTF.setEnabled(true);
            deleteBookButton.setEnabled(true);
        }
    }

    /**
     * Send the book information to the database to be updated
     *
     * @return return true if successful, false otherwise
     */
    public static boolean sendEditBookData() {
        // check all fields are not empty
        if (editBookTitleTF.getText().isEmpty() ||
                editBookVersionTF.getText().isEmpty() ||
                editBookNumPagesTF.getText().isEmpty() ||
                editBookYearTF.getText().isEmpty() ||
                editBookStockTF.getText().isEmpty() ||
                editBookGenreTF.getText().isEmpty() ||
                editBookPriceTF.getText().isEmpty() ||
                editBookRoyaltyTF.getText().isEmpty() ||
                editBookAuthorTF.getText().isEmpty() ||
                editBookPublisherTF.getText().isEmpty()) {
            editBookErrorLabel.setText("Update Failed. Please make all fields are filled out properly");
            return false;
        }

        // check all fields are valid
        // version can only be numbers
        try {
            Double.parseDouble(editBookVersionTF.getText());
        } catch (NumberFormatException e) {
            editBookErrorLabel.setText("Update Failed. Version cannot contain letters or spaces");
            return false;
        }
        // Page Count can only be numbers
        try {
            Double.parseDouble(editBookNumPagesTF.getText());
        } catch (NumberFormatException e) {
            editBookErrorLabel.setText("Update Failed. Page Count cannot contain letters or spaces");
            return false;
        }
        // Stock can only be numbers
        try {
            Double.parseDouble(editBookStockTF.getText());
        } catch (NumberFormatException e) {
            editBookErrorLabel.setText("Update Failed. Stock cannot contain letters or spaces");
            return false;
        }
        // Year can only be numbers && must be 4 characters
        try {
            if (editBookYearTF.getText().length() != 4)
                throw new IllegalArgumentException();
            Double.parseDouble(editBookYearTF.getText());
        } catch (IllegalArgumentException e) {
            if (e instanceof NumberFormatException)
                editBookErrorLabel.setText("Update Failed. Year cannot contain letters or spaces");
            else editBookErrorLabel.setText("Update Failed. Year must be in the format: YYYY");
            return false;
        }
        // Price can only be numbers
        try {
            Double.parseDouble(editBookPriceTF.getText());
        } catch (NumberFormatException e) {
            editBookErrorLabel.setText("Update Failed. Price cannot contain letters or spaces");
            return false;
        }
        // Royalty can only be numbers
        try {
            if (Double.parseDouble(editBookRoyaltyTF.getText()) > 100.0)
                throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            if (e instanceof NumberFormatException)
                editBookErrorLabel.setText("Update Failed. Royalty cannot contain letters or spaces");
            else editBookErrorLabel.setText("Update Failed. Royalty cannot be greater than 100%");
            return false;
        }

        String[] genres = editBookGenreTF.getText().split(",");
        String[] authors = editBookAuthorTF.getText().split(",");

        switch (DatabaseQueries.updateBook(currentISBNLabel.getText(), editBookTitleTF.getText(), editBookVersionTF.getText(), editBookNumPagesTF.getText(), editBookYearTF.getText(), editBookStockTF.getText(), genres, editBookPriceTF.getText(), editBookRoyaltyTF.getText(), authors, editBookPublisherTF.getText())) {
            case 1 -> {
                editBookErrorLabel.setText("There was an error with the given authors.");
                return false;
            }
            case 2 -> {
                editBookErrorLabel.setText("There was an error with the given genre.");
                return false;
            }
            case 3 -> {
                editBookErrorLabel.setText("There was an error with the given publisher.");
                return false;
            }
            case 4 -> {
                editBookErrorLabel.setText("There was an error with the given book.");
                return false;
            }
            default -> {
                return true;
            }
        }
    }

    /**
     *  Populates editOrder with order info
     */
    public static void fetchOrderDetails() {
        ArrayList<String> orderInfo = new ArrayList<>();
        if(!searchOrderNumber.getText().isEmpty())
            orderInfo = DatabaseQueries.lookForanOrder(searchOrderNumber.getText());
        else {
            defaultAdminViewFields();
            lookupOrderErrorLabel.setText("Please enter an order number before searching.");
        }
        if(orderInfo == null || orderInfo.size() == 0){
            defaultAdminViewFields();
            lookupOrderErrorLabel.setText("Order not found");
        } else {
            currentOrderNumber.setText(searchOrderNumber.getText());
            trackingNumber.setText(orderInfo.get(0));
            dateOrderPlaced.setText(orderInfo.get(1).substring(0, 11));
            editStatusCB.setSelectedItem(orderInfo.get(2));

            lookupOrderErrorLabel.setText(""); // clear errors + search bar
            searchOrderNumber.setText("");

            trackingNumber.setEnabled(true);
            editStatusCB.setEnabled(true);
        }
    }

    /**
     *  Sends order information to the DB
     * @return true if order is updated
     */
    public static boolean sendOrderDetails() {
        lookupOrderErrorLabel.setForeground(Color.RED);
        if(editStatusCB.getSelectedIndex() > 2){
            if(trackingNumber.getText().isEmpty()){
                lookupOrderErrorLabel.setText("Order with this status needs a tracking number");
                return false;
            }
        }
        if(editStatusCB.getSelectedIndex() == 0){
            lookupOrderErrorLabel.setText("Order needs a status");
            return false;
        } else {
               return DatabaseQueries.updateOrder(currentOrderNumber.getText(), trackingNumber.getText(), (String) editStatusCB.getSelectedItem());
        }
    }

    /**
     * Retrieves information from the GUI and sends it to the DB to be inserted
     *
     * @return true if successful, false otherwise
     */
    public static boolean addBook() {
        // check all fields are not empty
        if (newISBNTF.getText().isEmpty() ||
                newBookTitleTF.getText().isEmpty() ||
                newBookVersionTF.getText().isEmpty() ||
                newBookNumPagesTF.getText().isEmpty() ||
                newBookYearTF.getText().isEmpty() ||
                newBookStockTF.getText().isEmpty() ||
                newBookGenreTF.getText().isEmpty() ||
                newBookPriceTF.getText().isEmpty() ||
                newBookRoyaltyTF.getText().isEmpty() ||
                newBookAuthorNameTF.getText().isEmpty() ||
                newBookPublisherTF.getText().isEmpty()) {
            addBookErrorLabel.setText("Book Addition Failed. Please make all fields are filled out properly");
            return false;
        }

        // check all fields are valid
        // ISBN can only be numbers && must be 13 characters
        try {
            if (newISBNTF.getText().length() != 13)
                throw new IllegalArgumentException();
            Double.parseDouble(newISBNTF.getText());
        } catch (IllegalArgumentException e) {
            if (e instanceof NumberFormatException)
                addBookErrorLabel.setText("Book Addition Failed. ISBN cannot contain letters or spaces");
            else addBookErrorLabel.setText("Book Addition Failed. ISBN must be 13 digits");
            return false;
        }
        // version can only be numbers
        try {
            Double.parseDouble(newBookVersionTF.getText());
        } catch (NumberFormatException e) {
            addBookErrorLabel.setText("Book Addition Failed. Version cannot contain letters or spaces");
            return false;
        }
        // Page Count can only be numbers
        try {
            Double.parseDouble(newBookNumPagesTF.getText());
        } catch (NumberFormatException e) {
            addBookErrorLabel.setText("Book Addition Failed. Page Count cannot contain letters or spaces");
            return false;
        }
        // Stock can only be numbers
        try {
            Double.parseDouble(newBookStockTF.getText());
        } catch (NumberFormatException e) {
            addBookErrorLabel.setText("Book Addition Failed. Stock cannot contain letters or spaces");
            return false;
        }
        // Year can only be numbers && must be 4 characters
        try {
            if (newBookYearTF.getText().length() != 4)
                throw new IllegalArgumentException();
            Double.parseDouble(newBookYearTF.getText());
        } catch (IllegalArgumentException e) {
            if (e instanceof NumberFormatException)
                addBookErrorLabel.setText("Book Addition Failed. Year cannot contain letters or spaces");
            else addBookErrorLabel.setText("Book Addition Failed. Year must be in the format: YYYY");
            return false;
        }
        // Price can only be numbers
        try {
            Double.parseDouble(newBookPriceTF.getText());
        } catch (NumberFormatException e) {
            addBookErrorLabel.setText("Book Addition Failed. Price cannot contain letters or spaces");
            return false;
        }
        // Royalty can only be numbers
        try {
            if (Double.parseDouble(newBookRoyaltyTF.getText()) > 100.0)
                throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            if (e instanceof NumberFormatException)
                addBookErrorLabel.setText("Book Addition Failed. Royalty cannot contain letters or spaces");
            else addBookErrorLabel.setText("Book Addition Failed. Royalty cannot be greater than 100%");
            return false;
        }

        newBookGenreTF.setText(newBookGenreTF.getText().replaceAll("\\s+", ","));
        String[] genres = newBookGenreTF.getText().split(",");
        String[] authors = newBookAuthorNameTF.getText().split(",");

        switch (DatabaseQueries.addBook(newISBNTF.getText(), newBookTitleTF.getText(), newBookVersionTF.getText(), newBookNumPagesTF.getText(), newBookYearTF.getText(), newBookStockTF.getText(), genres, newBookPriceTF.getText(), newBookRoyaltyTF.getText(), authors, newBookPublisherTF.getText())) {
            case 1 -> {
                addBookErrorLabel.setText("There was an error with the given authors.");
                return false;
            }
            case 2 -> {
                addBookErrorLabel.setText("There was an error with the given genres.");
                return false;
            }
            case 3 -> {
                addBookErrorLabel.setText("There was an error with the given publisher. Make sure to add publishers BEFORE books");
                return false;
            }
            case 4 -> {
                addBookErrorLabel.setText("A book with that ISBN already exists.");
                return false;
            }
            default -> {
                return true;
            }
        }
    }

    /**
     * Inserts publisher information to the database
     *
     * @return true if successful, false otherwise
     */
    public static boolean addPublisher() {
        if (newPublisherNameTF.getText().isEmpty() ||
                newPublisherEmailTF.getText().isEmpty() ||
                newPublisherStreetNumTF.getText().isEmpty() ||
                newPublisherStreetNameTF.getText().isEmpty() ||
                newPublisherCityTF.getText().isEmpty() ||
                pubProvinceCB.getSelectedIndex() == 0 ||
                newPublisherCountryTF.getText().isEmpty() ||
                newPublisherPostalCodeTF.getText().isEmpty() ||
                newPublisherBankAccountTF.getText().isEmpty()) {
            addPublisherErrorLabel.setText("Publisher Addition Failed. Please make all required fields are filled out properly");
            return false;
        }

        // check all fields are valid
        // email must contain an '@'....or else wtf did they provide
        if (!newPublisherEmailTF.getText().contains("@")) {
            addPublisherErrorLabel.setText("Publisher Addition Failed. The email provided is not valid.");
            return false;
        }
        // phone can only be numbers and must be 10 digits long
        try {
            if (!newPublisherPhoneTF.getText().isEmpty()) {
                newPublisherPhoneTF.setText(newPublisherPhoneTF.getText().replaceAll("\\s+", ""));
                if (newPublisherPhoneTF.getText().length() != 10) {
                    addPublisherErrorLabel.setText("Publisher Addition Failed. Phone Number must be 10 digits long");
                    return false;
                }
                Double.parseDouble(newPublisherPhoneTF.getText());
            }
        } catch (NumberFormatException e) {
            addPublisherErrorLabel.setText("Publisher Addition Failed. Phone Number cannot contain letters");
            return false;
        }
        // Street number can only be numbers
        try {
            Double.parseDouble(newPublisherStreetNumTF.getText());
        } catch (NumberFormatException e) {
            addPublisherErrorLabel.setText("Publisher Addition Failed. Street Number cannot contain letters or spaces");
            return false;
        }
        // City can only be letters
        if (FrontEndUtilities.check(newPublisherCityTF.getText())) {
            addPublisherErrorLabel.setText("Publisher Addition Failed. City cannot contain numbers, spaces or special characters");
            return false;
        }
        // Country can only be letters
        if (FrontEndUtilities.check(newPublisherCountryTF.getText())) {
            addPublisherErrorLabel.setText("Publisher Addition Failed. Country cannot contain numbers, spaces or special characters");
            return false;
        }
        // Bank Acc can only be numbers && must be 15 digits
        try {
            newPublisherBankAccountTF.setText(newPublisherBankAccountTF.getText().replaceAll("\\s+", ""));
            if (newPublisherBankAccountTF.getText().length() != 15) {
                addPublisherErrorLabel.setText("Invalid bank account number");
                return false;
            }
            Double.parseDouble(newPublisherBankAccountTF.getText());
        } catch (NumberFormatException e) {
            addPublisherErrorLabel.setText("Publisher Addition Failed. Bank Account cannot contain letters or spaces");
            return false;
        }
        // Postal Code must be 6 digits
        if (newPublisherPostalCodeTF.getText().length() != 6) {
            addPublisherErrorLabel.setText("Invalid postal code");
            return false;
        }


        newPublisherNameTF.setText(newPublisherNameTF.getText().replaceAll("'", ""));
        // attempt to add publisher
        if (DatabaseQueries.addPublisher(newPublisherNameTF.getText(), newPublisherEmailTF.getText(), newPublisherPhoneTF.getText(), newPublisherBankAccountTF.getText())) {
            // add address
            DatabaseQueries.addAddress(newPublisherStreetNumTF.getText(), newPublisherStreetNameTF.getText(), newPublisherApartmentTF.getText(), newPublisherCityTF.getText(), Objects.requireNonNull(pubProvinceCB.getSelectedItem()).toString(), newPublisherCountryTF.getText(), newPublisherPostalCodeTF.getText());
            //associate publisher with the address
            return DatabaseQueries.addPubAdd(newPublisherNameTF.getText());
        }
        addPublisherErrorLabel.setText("Publisher Addition Failed. Publisher already exists.");
        return false;
    }

    /**
     * Attempts to register a user.
     *
     * @return True if registration was successful, false otherwise.
     * @see super.registerNewUser, super.addHasAdd, super.addAddress, super.countAddresses for further implementation.
     */
    public static boolean addLibrarian() {
        // check if one of the address fields is not empty (they must fill out all fields or none)
        boolean shippingAttempt = !shippingAdminStreetNumTF.getText().isEmpty() ||
                !shippingAdminStreetNameTF.getText().isEmpty() ||
                !shippingAdminApartmentTF.getText().isEmpty() ||
                !shippingAdminCityTF.getText().isEmpty() ||
                shippingAdminProvinceCB.getSelectedIndex() != 0 ||
                !shippingAdminCountryTF.getText().isEmpty() ||
                !shippingAdminPostalCodeTF.getText().isEmpty();
        boolean billingAttempt = !billAdminStreetNumTF.getText().isEmpty() ||
                !billAdminStreetNameTF.getText().isEmpty() ||
                !billAdminApartmentTF.getText().isEmpty() ||
                !billAdminCityTF.getText().isEmpty() ||
                billAdminProvinceCB.getSelectedIndex() != 0 ||
                !billAdminCountryTF.getText().isEmpty() ||
                !billAdminPostalCodeTF.getText().isEmpty();
        boolean addressAttempt = shippingAttempt || billingAttempt;

        addUserErrorLabel.setForeground(Color.red);
        // Check for a valid username.
        if (newAdminUsernameTF.getText().length() == 0) {
            addUserErrorLabel.setText("Registration Failed. Please provide a username.");
            return false;
        }
        // Check for a valid password.
        if (confirmAdminPasswordTF.getPassword().length == 0 || confirmAdminPasswordTF.getPassword().length == 0) {
            addUserErrorLabel.setText("Registration Failed. Please provide and confirm a password.");
            return false;
        }
        // Check to see if the password matches the confirm password textfield.
        if (!(new String(confirmAdminPasswordTF.getPassword()).equals(new String(newAdminPasswordTF.getPassword())))) {
            addUserErrorLabel.setText("Registration Failed. Passwords do not match.");
            return false;
        }
        // Check to see if the names contain any numbers/are empty.
        if (firstNameAdminTF.getText().length() == 0 || lastNameAdminTF.getText().length() == 0) {
            addUserErrorLabel.setText("Registration Failed. Please enter both a first and last name.");
            return false;
        }
        if (FrontEndUtilities.check(firstNameAdminTF.getText())) {
            addUserErrorLabel.setText("Registration Failed. First names cannot contain numerical values.");
            return false;
        }
        if (FrontEndUtilities.check(lastNameAdminTF.getText())) {
            addUserErrorLabel.setText("Registration Failed. Last names cannot contain numerical values.");
            return false;
        }
        // Ensure the email field is not empty.
        if (emailAdminTF.getText().length() == 0) {
            addUserErrorLabel.setText("Registration Failed. Email cannot be blank.");
            return false;
        }
        // Ensure the email field is valid
        if (!emailAdminTF.getText().contains("@")) {
            addUserErrorLabel.setText("Registration Failed. Email not valid.");
            return false;
        }
        boolean sameShipAndBill = adminBillingSameAsShipping.isSelected();

        // Check each of the address fields :(
        {
            // check for empty fields
            if (addressAttempt || !newIsUserAdminCB.isSelected()) {
                if (!newIsUserAdminCB.isSelected())
                    salaryAdminTF.setText(null);
                // ensure user fills out shipping address first
                if (!shippingAttempt) {
                    if (billingAttempt) {
                        addUserErrorLabel.setText("Registration Failed. Cannot have billing address without shipping address.");
                        return false;
                    }
                }
                if (shippingAdminStreetNumTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Shipping street number cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && billAdminStreetNumTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Billing street number cannot be empty.");
                    return false;
                }
                if (shippingAdminStreetNameTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Shipping street name cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && billAdminStreetNameTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Billing street name cannot be empty.");
                    return false;
                }
                if (shippingAdminCityTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Shipping city name cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && billAdminCityTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Billing city name cannot be empty.");
                    return false;
                }
                if (shippingAdminProvinceCB.getSelectedIndex() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Please select a shipping province.");
                    return false;
                }
                if (!sameShipAndBill && billAdminProvinceCB.getSelectedIndex() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Please select a billing province.");

                }
                if (shippingAdminCountryTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Shipping country cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && billAdminCountryTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Billing country cannot be empty.");
                    return false;
                }
                // Postal Code
                if (shippingAdminPostalCodeTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Shipping postal code cannot be empty.");
                    return false;
                }
                if (!sameShipAndBill && billAdminPostalCodeTF.getText().length() == 0) {
                    addUserErrorLabel.setText("Registration Failed. Billing postal code cannot be empty.");
                    return false;
                }
            } else if (salaryAdminTF.getText().isEmpty() && newIsUserAdminCB.isSelected()) {
                addUserErrorLabel.setText("Registration Failed. Admins need a salary.");
                return false;
            }

            // check validity
            try {
                if (!billAdminStreetNumTF.getText().isEmpty()) Double.parseDouble(billAdminStreetNumTF.getText());
                if (!sameShipAndBill) {
                    if (!billAdminStreetNumTF.getText().isEmpty())
                        Double.parseDouble(billAdminStreetNumTF.getText());
                }
            } catch (NumberFormatException ex) {
                addUserErrorLabel.setText("Registration Failed. Street numbers cannot contain letters.");
                return false;
            }
            try {
                if (newIsUserAdminCB.isSelected()) {
                    Double.parseDouble(salaryAdminTF.getText());
                }
            } catch (NumberFormatException e) {
                addUserErrorLabel.setText("Registration Failed. Salary cannot contain letters.");
                return false;
            }
            if (FrontEndUtilities.check(shippingAdminCityTF.getText())) {
                addUserErrorLabel.setText("Registration Failed. Shipping city cannot contain numerical values.");
                return false;
            }
            if (FrontEndUtilities.check(billAdminCityTF.getText())) {
                addUserErrorLabel.setText("Registration Failed. Billing city cannot contain numerical values.");
                return false;
            }
            if (FrontEndUtilities.check(shippingAdminCountryTF.getText())) {
                addUserErrorLabel.setText("Registration Failed. Shipping country cannot contain numerical values.");
                return false;
            }
            if (FrontEndUtilities.check(billAdminCountryTF.getText())) {
                addUserErrorLabel.setText("Registration Failed. Billing country cannot contain numerical values.");
                return false;
            }
            if (!shippingAdminPostalCodeTF.getText().isEmpty() && shippingAdminPostalCodeTF.getText().length() != 6) {
                addUserErrorLabel.setText("Registration Failed. Postal Codes must be 6 digits.");
                return false;
            }
            if (!sameShipAndBill && !billAdminPostalCodeTF.getText().isEmpty() && billAdminPostalCodeTF.getText().length() != 6) {
                addUserErrorLabel.setText("Registration Failed. Postal Codes must be 6 digits.");
                return false;
            }
        }


        // Attempt to add the user to the database.
        if (DatabaseQueries.registerNewUser(newAdminUsernameTF.getText(), new String(newAdminPasswordTF.getPassword()), firstNameAdminTF.getText(), lastNameAdminTF.getText(), emailAdminTF.getText())) {
            DatabaseQueries.updateAdmin(newAdminUsernameTF.getText(), salaryAdminTF.getText());
            confirmAdminReg.setText("Registration Successful");
            confirmAdminReg.setForeground(Color.BLACK);
        } else {
            addUserErrorLabel.setText("Registration Failed. A user with that username is already registered in the system.");
            return false;
        }

        /* If we get here, the following insertion methods will not fail. */
        // Add the shipping address and billing address.
        if (addressAttempt) {
            DatabaseQueries.addAddress(shippingAdminStreetNumTF.getText(), shippingAdminStreetNameTF.getText(), shippingAdminApartmentTF.getText(), shippingAdminCityTF.getText(), Objects.requireNonNull(shippingAdminProvinceCB.getSelectedItem()).toString(), shippingAdminCountryTF.getText(), shippingAdminPostalCodeTF.getText());
            DatabaseQueries.addHasAdd(newAdminUsernameTF.getText(), true);
            if (!sameShipAndBill) {
                // Need to add the billing address as a separate address.
                DatabaseQueries.addAddress(billAdminStreetNumTF.getText(), billAdminStreetNameTF.getText(), billAdminApartmentTF.getText(), billAdminCityTF.getText(), Objects.requireNonNull(billAdminProvinceCB.getSelectedItem()).toString(), billAdminCountryTF.getText(), billAdminPostalCodeTF.getText());
            } else {
                DatabaseQueries.addAddress(shippingAdminStreetNumTF.getText(), shippingAdminStreetNameTF.getText(), shippingAdminApartmentTF.getText(), shippingAdminCityTF.getText(), Objects.requireNonNull(shippingAdminProvinceCB.getSelectedItem()).toString(), shippingAdminCountryTF.getText(), shippingAdminPostalCodeTF.getText());
            }
            DatabaseQueries.addHasAdd(newAdminUsernameTF.getText(), false);
        }
        // Done.
        return true;
    }


    public static void reportSelection(){
        String[] sortOption = {"Name", "ASC"};
        switch(Objects.requireNonNull(reportSortCB.getSelectedItem()).toString()){
            case "Name: A-Z" -> {
                sortOption[0] = "name";
                sortOption[1] = "ASC";
            }
            case "Name: Z-A" -> {
                sortOption[0] = "name";
                sortOption[1] = "DESC";
            }
            case "Quantity: High to Low" -> {
                sortOption[0] = "quantity";
                sortOption[1] = "DESC";
            }
            case "Quantity: Low to High" -> {
                sortOption[0] = "quantity";
                sortOption[1] = "ASC";
            }
            case "Revenue: High to Low" -> {
                sortOption[0] = "revenue";
                sortOption[1] = "DESC";
            }
            case "Revenue: Low to High" -> {
                sortOption[0] = "revenue";
                sortOption[1] = "ASC";
            }
            case "Cost: High to Low" -> {
                sortOption[0] = "cost";
                sortOption[1] = "DESC";
            }
            case "Cost: Low to High" -> {
                sortOption[0] = "cost";
                sortOption[1] = "ASC";
            }
            case "Profit: High to Low" -> {
                sortOption[0] = "profit";
                sortOption[1] = "DESC";
            }
            case "Profit: Low to High" -> {
                sortOption[0] = "profit";
                sortOption[1] = "ASC";
            }
            case "Date: High to Low" -> {
                sortOption[0] = "date";
                sortOption[1] = "DESC";
            }
            case "Date: Low to High" -> {
                sortOption[0] = "date";
                sortOption[1] = "ASC";
            }
        }

        if(reportCB.getSelectedIndex() != 0) {
            switch (Objects.requireNonNull(reportCB.getSelectedItem()).toString()) {
                case "Sales v Expense" -> {
                    if (reportSortCB.getSelectedIndex() > 1) {
                        reportContainer.setText(Reports.expenseReport(Objects.requireNonNull(timeCB.getSelectedItem()).toString(), sortOption));
                        reportErrorLabel.setText("");
                    }else reportErrorLabel.setText("Invalid Report Selection. Cannot Sort Dates by Name");
                }
                case "Sales per Genre" -> {
                    if (reportSortCB.getSelectedIndex() != 2 && reportSortCB.getSelectedIndex() != 3) {
                        reportContainer.setText(Reports.genreReport(Objects.requireNonNull(timeCB.getSelectedItem()).toString(), sortOption));
                        reportErrorLabel.setText("");
                    } else reportErrorLabel.setText("Invalid Report Selection. Cannot Sort Genres by Date");
                }
                case "Sales per Author" -> {
                    if (reportSortCB.getSelectedIndex() != 2 && reportSortCB.getSelectedIndex() != 3){
                        reportContainer.setText(Reports.authorReport(Objects.requireNonNull(timeCB.getSelectedItem()).toString(), sortOption));
                        reportErrorLabel.setText("");
                    } else reportErrorLabel.setText("Invalid Report Selection. Cannot Sort Authors by Date");
                }
                case "Sales per Publisher" -> {
                    if (reportSortCB.getSelectedIndex() != 2 && reportSortCB.getSelectedIndex() != 3) {
                        reportContainer.setText(Reports.publisherReport(Objects.requireNonNull(timeCB.getSelectedItem()).toString(), sortOption));
                        reportErrorLabel.setText("");
                    } else reportErrorLabel.setText("Invalid Report Selection. Cannot Sort Publishers by Date");
                }
            }
        } else reportErrorLabel.setText("Please select a type of report.");

        reportContainer.setVisible(true);
        reportContainer.setCaretPosition(0);
    }

}
