import java.sql.*;

/**
 * The LookForaBook class represents the back-end for the LookInnaBook application.
 * The class is responsible for processing user requests and accessing a database
 * via SQL queries.
 *
 * @author John Breton, Ryan Godfrey
 * @version 1.0
 */
public class LookForaBook {

    // Constants that must be changed depending on execution environment.
    private static final String USER = "ryan";
    private static final String DATABASE = "LookInnaBook";

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
     * Query the login information from the database.
     *
     * @param username The username that was entered.
     * @param password The password that was entered.
     * @return True if the password matches the password stored for the given username, false otherwise.
     */
    protected boolean[] lookForaLogin(String username, char[] password) {
        boolean[] returnArr = {false, false}; // [0] = active user, [1] = admin

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DATABASE, USER, "")) {
            Statement statement = connection.createStatement();

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
}
