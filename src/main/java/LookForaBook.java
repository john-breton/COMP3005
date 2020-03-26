/*
 * Copyright © 3.2020. Ryan Godfrey, John Breton.
 * All rights reserved.
 */

import java.sql.*;
import java.util.*;

/**
 * The LookForaBook class represents the back-end for the LookInnaBook application.
 * The class is responsible for processing user requests and accessing a database
 * via SQL queries.
 *
 * @author John Breton, Ryan Godfrey
 * @version 1.0
 */
public class LookForaBook {

    // Just putting this here so we can change it when we test.
    private static final String USER = "ryan";
    private static Statement statement = null;
    private static final String DATABASE = "lookinnabook";

    protected LookForaBook(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", USER, "");
            statement = connection.createStatement();
        }catch (SQLException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    /**
     * Query the login information from the database.
     *
     * @param username The username that was entered.
     * @param password The password that was entered.
     * @return True if the password matches the password stored for the given username, false otherwise.
     */
    protected boolean[] lookForaLogin(String username, char[] password) {
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
     * @param username    The username for the new user.
     * @param password    The password for the new user.
     * @param first_name  The first name of the new user.
     * @param last_name   The last name of the new user.
     * @param email       The email address for the new user.
     * @param shippingAdd The shipping address for the new user.
     * @param billingAdd  The billing address for the new user.
     * @return True if the insertion was successful, false otherwise.
     */
    protected boolean registerNewUser(String username, String password, String first_name, String last_name, String email, int shippingAdd, int billingAdd) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "")) {
            connection.createStatement().execute("INSERT into project.user values ('" + username + "', '" + password + "', '" + first_name + "', '" + last_name + "', '" + email + "', '" + shippingAdd + "', '" + billingAdd + "')");
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
     */
    protected int countAddresses() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "")) {
            ResultSet result = connection.createStatement().executeQuery("SELECT COUNT(*) from project.address");
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
     * @param addID      The id number associated with the address.
     * @param streetNum  The street number of the address.
     * @param streetName The street name of the address.
     * @param apartment  The apartment number of the address (can be null).
     * @param city       The city of the address.
     * @param province   The province of the address.
     * @param country    The country of the address.
     * @param postalCode The postal code of the address.
     * @return True if the insertion was successful, false otherwise.
     */
    protected boolean addAddress(int addID, String streetNum, String streetName, String apartment, String city, String province, String country, String postalCode) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "")) {
            connection.createStatement().execute("INSERT into project.address values ('" + addID + "', '" + streetNum + "', '" + streetName + "', '" + apartment + "', '" + city + "', '" + province + "', '" + country + "', '" + postalCode + "')");
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
     * @param addID      The id number associated with the address.
     * @param isShipping True if the address is a shipping address, false otherwise.
     * @param isBilling  True if the address is a shipping address, false otherwise.
     * @return True if the insertion was successful, false otherwise.
     */
    protected boolean addHasAdd(String username, int addID, boolean isShipping, boolean isBilling) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "")) {
            connection.createStatement().execute("INSERT into project.hasadd values ('" + username + "', '" + addID + "', '" + isShipping + "', '" + isBilling + "')");
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
    protected ArrayList lookForaUser(String username) {
        ArrayList userCred = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>(); // extract addresses

        int rowCount = 0;

        try {

            ResultSet result = statement.executeQuery("SELECT * FROM project.user left outer join project.librarian using (user_name) WHERE user_name = '" + username + "'");

            while(result.next()){ // loop over results
                rowCount++; // count results
                userCred.add(0, result.getString("user_name"));
                userCred.add(1, result.getString("password"));
                userCred.add(2, result.getString("first_name"));
                userCred.add(3, result.getString("last_name"));
                userCred.add(4, result.getString("email"));
                userCred.add(5, result.getString("salary"));
                addresses.add(0, result.getString("shipping_add"));
                addresses.add(1, result.getString("billing_add"));
            }

            for(String s : addresses){
                userCred.add(lookForanAddress(username, s));
            }

            if(rowCount == 1) return userCred; // user found

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
     * @param addID Address ID number
     * @return An ArrayList of address info. [0] = street num, [1] = street name, [2] = apartment, [3] = city, [4] = province, [5] = country, [6] = postal code, [7] = isShipping, [8] = isBilling
     */
    private ArrayList lookForanAddress(String username, String addID){
        ArrayList addInfo = new ArrayList<>();
        int rowCount = 0;

        try{
            ResultSet result = statement.executeQuery("SELECT * FROM project.address NATURAL JOIN project.hasadd WHERE add_id = '" + addID + "' and user_name = '" + username + "'");

            while(result.next()){
                rowCount++; // count results
                addInfo.add(0, result.getString("street_num"));
                addInfo.add(1, result.getString("street_name"));
                addInfo.add(2, result.getString("apartment"));
                addInfo.add(3, result.getString("city"));
                addInfo.add(4, result.getString("province"));
                addInfo.add(5, result.getString("country"));
                addInfo.add(6, result.getString("postal_code"));
                addInfo.add(7, result.getBoolean("isshipping"));
                addInfo.add(8, result.getBoolean("isbilling"));
            }

            if(rowCount == 1) return addInfo; // user found

            if (rowCount == 0) return null; // user not found

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
     * @param username The username that was entered
     * @param password The new password that was entered TODO: confirm password before passing
     * @param firstName The first name that was entered
     * @param lastName The last name that was entered
     * @param email The email that was entered
     * @param shippAdd The shipping address ID that was found and passed via lookForaUser
     * @param billAdd The billing address ID that was found and passed via lookForaUser
     * @return ArrayList of the updated user credentials (or added credentials but we don't want this), null otherwise
     */
    protected boolean updateUser(String username, String password, String firstName, String lastName, String email, String salary, ArrayList shippAdd, ArrayList billAdd){
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

            if(rowsAffected == 1){ // means a row was added or updated since searing by user_name
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return false;
    }
}
