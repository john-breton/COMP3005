package backend;
/*
 * Copyright © 3.2020. Ryan Godfrey, John Breton.
 * All rights reserved.
 */

import java.sql.*;
import java.util.ArrayList;

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
                    "values ('" + username +
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
     * @return ArrayList of user credentials from the database. [0] = username, [1] = password, [2] = first name, [3] = last name, [4] = email, [5] = salary, [6] = shippingID, [7] = billingID
     * null if no user found.
     */
    public static ArrayList<Object> lookForaUser(String username) {
        ArrayList<Object> userCred = new ArrayList<>();

        int rowCount = 0;

        try {

            ResultSet result = statement.executeQuery("SELECT * FROM project.user left outer join project.librarian using (user_name) WHERE user_name = '" + username + "'");

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
     * @return An ArrayList of address info. [0] = street num, [1] = street name, [2] = apartment, [3] = city, [4] = province, [5] = country, [6] = postal code, [7] = isShipping, [8] = isBilling
     */
    private static ArrayList<Object> lookForanAddress(String username) {
        ArrayList<Object> addInfo = new ArrayList<>();
        int rowCount = 0;

        try {
            ResultSet result = statement.executeQuery("SELECT * FROM project.address NATURAL JOIN project.hasadd WHERE user_name = '" + username + "'");

            while (result.next()) {
                rowCount++; // count results
                addInfo.add(result.getString("street_num"));
                addInfo.add(result.getString("street_name"));
                addInfo.add(result.getString("apartment"));
                addInfo.add(result.getString("city"));
                addInfo.add(result.getString("province"));
                addInfo.add(result.getString("country"));
                addInfo.add(result.getString("postal_code"));
                addInfo.add(result.getBoolean("isshipping"));
                addInfo.add(result.getBoolean("isbilling"));
            }

            if (rowCount == 1 || rowCount == 2) return addInfo; // user found

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
     * @param password  The new password that was entered TODO: confirm password before passing
     * @param firstName The first name that was entered
     * @param lastName  The last name that was entered
     * @param email     The email that was entered
     * @param shippAdd  The shipping address ID that was found and passed via lookForaUser
     * @param billAdd   The billing address ID that was found and passed via lookForaUser
     * @return ArrayList of the updated user credentials (or added credentials but we don't want this), null otherwise
     */
    protected static boolean updateUser(String username, String password, String firstName, String lastName, String email, String salary, ArrayList<Object> shippAdd, ArrayList<Object> billAdd) {
        try {

            int rowsAffected = statement.executeUpdate("INSERT INTO project.user " +
                    "values('" + username +
                    "', '" + password +
                    "', '" + firstName +
                    "', '" + lastName +
                    "', '" + email +
                    "', '" + shippAdd +
                    "', '" + billAdd +
                    "') on conflict (user_name) " +
                    "do update set password = '" + password +
                    "', first_name = '" + firstName +
                    "', last_name = '" + lastName +
                    "', email = '" + email +
                    "'"); // shipping and billing addresses will need to be updated through another/ nested query??

            if (rowsAffected == 1) { // means a row was added or updated since searing by user_name
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
