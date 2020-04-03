package backend;
/*
 * Copyright © 3.2020. Ryan Godfrey, John Breton.
 * All rights reserved.
 */

import java.sql.*;
import java.util.*;

/**
 * The backend.LookForaBook class represents the back-end for the util.LookInnaBook application.
 * The class is responsible for processing user requests and accessing a database
 * via SQL queries.
 *
 * @author John Breton, Ryan Godfrey
 * @version 1.0
 */
public class DatabaseQueries {

    // Just putting this here so we can change it when we test.
    private static final String USER = "ryan";
    private static final String DATABASE = "LookInnaBook";
    private static Statement statement;

    static {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prevent direct initialization of the class.
     */
    private DatabaseQueries() {
    }

    /**
     * Query the login information from the database.
     *
     * @param username The username that was entered.
     * @param password The password that was entered.
     * @return True if the password matches the password stored for the given username, false otherwise.
     */
    public static boolean[] lookForaLogin(String username, char[] password) {
        boolean[] returnArr = {false, false}; // [0] = active user, [1] = admin

        try {

            ResultSet result = statement.executeQuery("SELECT * from project.user LEFT OUTER JOIN project.librarian USING (user_name) where user_name = '" + username.toLowerCase().trim() + "'");

            while (result.next()) {
                returnArr[0] = String.valueOf(password).equals(result.getString("password"));
                if (returnArr[0])
                    returnArr[1] = result.getString("salary") != null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnArr;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param username   The username for the new user.
     * @param password   The password for the new user.
     * @param first_name The first name of the new user.
     * @param last_name  The last name of the new user.
     * @param email      The email address for the new user.
     * @return True if the insertion was successful, false otherwise.
     */
    public static boolean registerNewUser(String username, String password, String first_name, String last_name, String email) {
        try {
            statement.execute("INSERT into project.user " +
                    "values ('" + username.toLowerCase() +
                    "', '" + password +
                    "', '" + first_name +
                    "', '" + last_name +
                    "', '" + email +
                    "')");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Counts the number of addresses currently in the database.
     *
     * @return The total number of addresses currently stored, as an int.
     * @deprecated No longer used for any purpose.
     */
    public static int countAddresses() {
        try {
            ResultSet result = statement.executeQuery("SELECT COUNT(*) from project.address");
            result.next();
            return Integer.parseInt(result.getString("count"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Add an address into the database.
     *
     * @param streetNum  The street number of the address.
     * @param streetName The street name of the address.
     * @param apartment  The apartment number of the address (can be null).
     * @param city       The city of the address.
     * @param province   The province of the address.
     * @param country    The country of the address.
     * @param postalCode The postal code of the address.
     * @return True if the insertion was successful, false otherwise.
     */
    public static boolean addAddress(String streetNum, String streetName, String apartment, String city, String province, String country, String postalCode) {
        try {
            statement.execute("INSERT into project.address " +
                    "values (nextval('project.address_add_id_seq'), " +
                    "'" + streetNum +
                    "', '" + streetName +
                    "', '" + apartment +
                    "', '" + city +
                    "', '" + province +
                    "', '" + country +
                    "', '" + postalCode +
                    "')");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Add a hasAdd relation into the database.
     *
     * @param username   The username associated with the address.
     * @param isShipping True if the address is a shipping address, false otherwise.
     * @param isBilling  True if the address is a shipping address, false otherwise.
     */
    public static void addHasAdd(String username, boolean isShipping, boolean isBilling) {
        try {
            statement.execute("INSERT into project.hasadd " +
                    "values ('" + username +
                    "', nextval('project.hasadd_add_id_seq'),'"
                    + isShipping + "', '" + isBilling + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Query user information from the database.
     *
     * @param username The username that was entered.
     * @return ArrayList of user credentials from the database. [0] = username, [1] = password, [2] = first name, [3] = last name, [4] = email, [5] = salary null if no user found.
     */
    public static ArrayList<Object> lookForaUser(String username) {
        ArrayList<Object> userCred = new ArrayList<>();

        int rowCount = 0;

        try {

            ResultSet result = statement.executeQuery("SELECT * FROM project.user left outer join project.librarian using (user_name) WHERE user_name = '" + username.toLowerCase() + "'");

            while (result.next()) { // loop over results
                rowCount++; // count results
                userCred.add(0, result.getString("user_name"));
                userCred.add(1, result.getString("password"));
                userCred.add(2, result.getString("first_name"));
                userCred.add(3, result.getString("last_name"));
                userCred.add(4, result.getString("email"));
                userCred.add(5, result.getString("salary"));
            }

            userCred.add(lookForanAddress(username)); // adds a loooong ArrayList of both addresses

            if (rowCount == 1) return userCred; // user found

            if (rowCount == 0) return null; // user not found

            // whaaaaaat
            userCred.clear();
            userCred.add("-1");
            System.out.println("Houston, we gotta problem in lookForaUser");
            return userCred;


        } catch (SQLException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        return userCred;
    }

    /**
     * Query address information from the database
     *
     * @param username Username of the user
     * @return An ArrayList of address info. [0] = street num, [1] = street name, [2] = apartment, [3] = city, [4] = province, [5] = country, [6] = postal code
     */
    private static ArrayList<Object> lookForanAddress(String username) {
        ArrayList<Object> addInfo = new ArrayList<>();
        int rowCount = 0;

        try {
            ResultSet result = statement.executeQuery("SELECT * FROM project.address NATURAL JOIN project.hasadd WHERE user_name = '" + username.toLowerCase() + "'");

            while (result.next()) {
                rowCount++; // count results
                addInfo.add(result.getString("street_num"));
                addInfo.add(result.getString("street_name"));
                addInfo.add(result.getString("apartment"));
                addInfo.add(result.getString("city"));
                addInfo.add(result.getString("province"));
                addInfo.add(result.getString("country"));
                addInfo.add(result.getString("postal_code"));
            }

            if (rowCount == 2) return addInfo; // user found

            if (rowCount == 0) return null;

            // whaaaaaat
            addInfo.clear();
            addInfo.add("-1");
            System.out.println("Houston, we gotta problem in lookForanAddress");
            return addInfo;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addInfo;
    }

    /**
     * Query the user information from the edit user screen to find and update a user.
     * lookForaUser() should be invoked BEFORE this method to ensure the user is already present in the database
     *
     * @param username  The username that was entered
     * @param password  The new password that was entered
     * @param firstName The first name that was entered
     * @param lastName  The last name that was entered
     * @param email     The email that was entered
     * @return ArrayList of the updated user credentials (or added credentials but we don't want this), null otherwise
     */
    public static boolean updateUser(String username, String password, String firstName, String lastName, String email) {
        try {
            int rowsAffected;
            if(!password.isEmpty())
                rowsAffected = statement.executeUpdate("UPDATE project.user SET password = '" + password + "', first_name = '" + firstName + "', last_name = '" + lastName + "', email = '" + email + "' WHERE user_name = '" + username.toLowerCase() + "'");
            else
                rowsAffected = statement.executeUpdate("UPDATE project.user SET first_name = '" + firstName + "', last_name = '" + lastName + "', email = '" + email + "' WHERE user_name = '" + username.toLowerCase() + "'");

            if (rowsAffected == 1) { // means a row was updated
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Query the admin info to update/ add an admin
     *
     * @param username username to be updated
     * @param salary   new salary
     */
    public static void updateAdmin(String username, String salary){
        try {
            if (salary == null || salary.isEmpty()) {
                statement.executeUpdate("UPDATE project.librarian " +
                        "SET salary = NULL " +
                        "WHERE user_name = '" + username + "'");
            } else {
                statement.executeUpdate("INSERT INTO project.librarian " +
                        "values('" + username + "','" + salary + "') " +
                        "ON CONFLICT (user_name) DO UPDATE SET salary = '" + salary + "'");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Query the addresses to update user's address
     *
     * @param username   the username
     * @param num        the num
     * @param name       the name
     * @param apartment  the apartment
     * @param city       the city
     * @param prov       the province
     * @param country    the country
     * @param postalCode the postal code
     * @param isShipping the is shipping
     * @param isBilling  the is billing
     */
    public static void updateAddress(String username, String num, String name, String apartment, String city, String prov, String country, String postalCode, boolean isShipping, boolean isBilling){
        // attempt to update address
        try {
            int rowsAffected = statement.executeUpdate("UPDATE project.address " +
                    "SET street_num = '" + num + "'," +
                    "street_name = '" + name + "'," +
                    "apartment = '" + apartment + "'," +
                    "city = '" + city + "'," +
                    "province = '" + prov + "'," +
                    "country = '" + country + "'," +
                    "postal_code = '" + postalCode + "'" +
                    "FROM project.hasadd " +
                    "WHERE project.address.add_id = project.hasadd.add_id " +
                    "AND project.hasadd.user_name = '" + username + "'" +
                    "AND project.hasadd.isshipping = '" + isShipping + "'" +
                    "RETURNING project.hasadd.add_id");

            if(rowsAffected == 0) { //user doesn't have an address yet
                if (addAddress(num, name, apartment, city, prov, country, postalCode)) {
                    addHasAdd(username, isShipping, isBilling);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Query the database for information about a specific book
     *
     * @param isbn the isbn to be queried for
     * @return ArrayList containing book information. [1] = isbn, [2] = title, [3] = version, [4] = page count, [5] = price, [6] = royalty, [7] = stock, [8] = publisher name, [9] = year, [10] = author info (ArrayList), [11] = genre info (ArrayList)
     */
    public static ArrayList<Object> lookForaBook(String isbn){
        ArrayList<Object> bookInfo = new ArrayList<>();
        int rowCount = 0; // how many rows found with the given isbn (should be 1 or 0)

        try {
            ResultSet result = statement.executeQuery(String.format("SELECT * FROM project.book natural join project.publishes WHERE isbn =%s", isbn)); // retrieve all books and publisher information with the given isbn

            while (result.next()) {
                rowCount++; // count results
                bookInfo.add(result.getString("isbn"));
                bookInfo.add(result.getString("name"));
                bookInfo.add(result.getString("version"));
                bookInfo.add(result.getString("num_pages"));
                bookInfo.add(result.getString("price"));
                bookInfo.add(result.getString("royalty"));
                bookInfo.add(result.getString("stock"));
                bookInfo.add(result.getString("pub_name"));
                bookInfo.add(result.getString("year"));
            }
            // get author info
            bookInfo.add(lookForaAuthor(isbn));
            // get genres
            bookInfo.add(lookForaGenre(isbn));

            if (rowCount == 1) return bookInfo; // isbn found

            if (rowCount == 0) return null; // no isbn found

            // whaaaaaat (more that one or less than 0 ISBNs found??)
            bookInfo.clear();
            bookInfo.add("-1");
            System.out.println("Houston, we gotta problem in lookForaBook");
            return bookInfo;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Query the database for information about a book's author(s)
     *
     * @param isbn the isbn
     * @return ArrayList containing the names of the author(s)
     */
    public static ArrayList<String> lookForaAuthor(String isbn){
        ArrayList<String> authInfo = new ArrayList<>();
        int rowCount = 0;

        try{
            ResultSet result = statement.executeQuery(String.format("SELECT * FROM project.author natural join project.writes WHERE isbn =%s", isbn));

            while(result.next()){
                rowCount++;
                authInfo.add(result.getString("auth_fn") + " " + result.getString("auth_ln"));
            }

            if (rowCount > 0) return authInfo;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Query the database for information about a book's genre(s)
     *
     * @param isbn the isbn
     * @return ArrayList containing the genres for the book
     */
    public static ArrayList<String> lookForaGenre(String isbn){
        ArrayList<String> genreInfo = new ArrayList<>();
        int rowCount = 0;

        try{
            ResultSet result = statement.executeQuery(String.format("SELECT * FROM project.genre natural join project.hasgenre WHERE isbn = %s", isbn));

            while(result.next()){
                rowCount++;
                genreInfo.add(result.getString("name"));
            }

            if (rowCount > 0) return genreInfo;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Update a book's information within the database
     *
     * @param isbn      the current isbn (can't be changed)
     * @param title     the new title
     * @param version   the new version
     * @param pageCount the new page count
     * @param year      the new year
     * @param stock     the new stock
     * @param genres    the new genres
     * @param price     the new price
     * @param royalty   the new royalty
     * @param authors   the new authors
     * @param publisher the new publisher
     * @return 0 if successful update of all attributes, 1 if error with the author information, 2 if error with the genre information, 3 if error with publisher information, 4 if error with book information
     */
    public static int updateBook(String isbn, String title, String version, String pageCount, String year, String stock, String[] genres, String price, String royalty, String[] authors, String publisher){
        try{
            // attempt update of book info
            statement.executeUpdate("UPDATE project.book " +
                    "SET name = '" + title + "'," +
                    "version = '" + version + "'," +
                    "num_pages = '" + pageCount + "'," +
                    "price = '" + price + "'," +
                    "royalty = '" + royalty + "'," +
                    "stock = '" + stock + "'" +
                    "WHERE isbn = '" + isbn + "'");
            // attempt update of publisher info
            if(updatePublisher(isbn, publisher, year)) {
                // attempt update of genre ino
                if (updateGenre(isbn, genres)) {
                    // attempt update of author info
                    if (updateAuthor(isbn, authors)) {
                        return 0;
                    } else return 1;
                } else return 2;
            }else return 3;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 4;
    }

    /**
     * Update a book's publisher within the database
     *
     * @param isbn The isbn of the book
     * @param publisher The name of the publisher
     * @param year The year of publication
     * @return True if an existing publisher was updated as publisher of the book, false if no publisher is present
     */
    private static boolean updatePublisher(String isbn, String publisher, String year) {
        boolean result = false;
        try {
            statement.executeUpdate("UPDATE project.publishes " +
                    "SET year = '" + year + "'," +
                    "pub_name = '" + publisher + "'" +
                    "WHERE isbn = '" + isbn + "'");
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Update a book's genre info within the database
     *
     * @param isbn The isbn of the book
     * @param genres The list of genres for that book
     * @return true if successful update, false otherwise
     */
    private static boolean updateGenre(String isbn, String[] genres){
        try {
            statement.executeUpdate("DELETE FROM project.hasgenre WHERE isbn = '" + isbn + "'"); // delete all the book's current genre info in order to update
            for(String s : genres) {
                s = s.trim();
                // attempt to insert the genre info into the genre entity, do nothing if the genre already exists
                statement.executeUpdate("INSERT INTO project.genre " +
                        "VALUES ('" + s + "')" +
                        "ON CONFLICT (name) DO NOTHING");
                // attempt to update the book to have that genre, do nothing if the relationship already exists
                statement.executeUpdate("INSERT INTO project.hasgenre " +
                            "VALUES ('" + s + "', '" + isbn + "')" +
                            "ON CONFLICT (name, isbn) DO NOTHING");
            }
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update the book's author info within the database
     *
     * @param isbn The isbn of the current book
     * @param authors The list of authors for the current book
     * @return true if successful, false otherwise
     */
    private static boolean updateAuthor(String isbn, String[] authors){
        try {
            statement.executeUpdate("DELETE FROM project.writes WHERE isbn = '" + isbn + "'"); // delete all authors from the current book
            for(String s : authors) {
                String[] names = s.trim().split(" ");
                // attempt to insert the author's names into the database, do nothing if that author already exists
                statement.executeUpdate("INSERT INTO project.author " +
                        "VALUES ('" + names[0].trim() + "', '" + names[1].trim() + "')" +
                        "ON CONFLICT (auth_fn, auth_ln) DO NOTHING");
                // attempt to insert the relationship between author and book into the database, do nothing if the relationship already exists
                statement.execute("INSERT INTO project.writes " +
                        "VALUES ('" + names[0].trim() + "', '" + names[1].trim() +  "', '" + isbn + "')" +
                        "ON CONFLICT (auth_fn, auth_ln, isbn) DO NOTHING");
            }
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
