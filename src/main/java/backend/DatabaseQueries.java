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
    private static Connection connection;
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
     * @return True if the insertion was successful, false otherwise.
     */
    public static boolean addHasAdd(String username, boolean isShipping, boolean isBilling) {
        try {
            statement.execute("INSERT into project.hasadd " +
                    "values ('" + username +
                    "', nextval('project.hasadd_add_id_seq'),'"
                    + isShipping + "', '" + isBilling + "')");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Query user information from the database.
     *
     * @param username The username that was entered.
     * @return ArrayList of user credentials from the database. [0] = username, [1] = password, [2] = first name, [3] = last name, [4] = email, [5] = salary
     * null if no user found.
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
            int rowsAffected = 0;
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
     * @param salary new salary
     * @return true if admin relation is updated, false otherwise
     */
    public static boolean updateAdmin(String username, String salary){
        try {
            if (salary == null || salary.isEmpty()) {
                int rowsAffected = statement.executeUpdate("UPDATE project.librarian " +
                        "SET salary = NULL " +
                        "WHERE user_name = '" + username + "'");
                return rowsAffected == 1; //true if admin deleted
            } else {
                int rowsAffected = statement.executeUpdate("INSERT INTO project.librarian " +
                        "values('" + username + "','" + salary + "') " +
                        "ON CONFLICT (user_name) DO UPDATE SET salary = '" + salary + "'");
                return rowsAffected == 1; // true if admin updated or added
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Query the addresses to update user's address
     *
     * @param num        the num
     * @param name       the name
     * @param apartment  the apartment
     * @param city       the city
     * @param prov       the province
     * @param country    the country
     * @param postalCode the postal code
     * @return true if successful update, false otherwise
     */
    public static boolean updateAddress(String username, String num, String name, String apartment, String city, String prov, String country, String postalCode, boolean isShipping, boolean isBilling){
        // attempt to update address
        try {
            int rowsAffected = 0;
            String add_id = null;
            ResultSet result = statement.executeQuery("UPDATE project.address " +
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

            while(result.next()){
                rowsAffected++;
                add_id = result.getString("add_id");
            }

            if(rowsAffected == 0) { //user doesn't have an address yet
                return addAddress(num, name, apartment, city, prov, country, postalCode) && addHasAdd(username, isShipping, isBilling);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Object> lookForaBook(String isbn){
        ArrayList<Object> bookInfo = new ArrayList<>();
        int rowCount = 0;

        try {
            ResultSet result = statement.executeQuery("SELECT * FROM project.book natural join project.publishes WHERE isbn = " + isbn);

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

            if (rowCount == 1) return bookInfo; // user found

            if (rowCount == 0) return null;

            // whaaaaaat
            bookInfo.clear();
            bookInfo.add("-1");
            System.out.println("Houston, we gotta problem in lookForanAddress");
            return bookInfo;

        } catch (SQLException e) {
            if(!e.toString().contains("invalid input syntax for type"))
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> lookForaAuthor(String isbn){
        ArrayList<String> authInfo = new ArrayList<>();
        int rowCount = 0;

        try{
            ResultSet result = statement.executeQuery("SELECT * FROM project.author natural join project.writes WHERE isbn = " + isbn);

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

    public static ArrayList<String> lookForaGenre(String isbn){
        ArrayList<String> genreInfo = new ArrayList<>();
        int rowCount = 0;

        try{
            ResultSet result = statement.executeQuery("SELECT * FROM project.genre natural join project.hasgenre WHERE isbn = " + isbn);

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

    public static int updateBook(String isbn, String title, String version, String pageCount, String year, String stock, String[] genres, String price, String royalty, String[] authors, String publisher){
        try{
            statement.executeUpdate("UPDATE project.book " +
                    "SET name = '" + title + "'," +
                    "version = '" + version + "'," +
                    "num_pages = '" + pageCount + "'," +
                    "price = '" + price + "'," +
                    "royalty = '" + royalty + "'," +
                    "stock = '" + stock + "'" +
                    "WHERE isbn = '" + isbn + "'");
            if(updatePublisher(isbn, publisher, year)) {
                if (updateGenre(isbn, genres)) {
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

    private static boolean updatePublisher(String isbn, String publisher, String year){
        try{
            statement.executeUpdate("UPDATE project.publishes " +
                    "SET year = '" + year + "'," +
                    "pub_name = '" + publisher + "'" +
                    "WHERE isbn = '" + isbn + "'");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean updateGenre(String isbn, String[] genres){
        try {
            for(String s : genres) {
                s = s.trim();
                statement.executeUpdate("INSERT INTO project.genre " +
                        "VALUES ('" + s + "')" +
                        "ON CONFLICT (name) DO NOTHING");
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

    private static boolean updateAuthor(String isbn, String[] authors){
        try {
            for(String s : authors) {
                String[] names = s.trim().split(" ");
                statement.executeUpdate("INSERT INTO project.author " +
                        "VALUES ('" + names[0].trim() + "', '" + names[1].trim() + "')" +
                        "ON CONFLICT (auth_fn, auth_ln) DO NOTHING");
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
