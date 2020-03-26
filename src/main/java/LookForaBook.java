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
    private static final String USER = "postgres";
    private static final String DATABASE = "lookinnabook";

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


            return connection.createStatement().execute("INSERT into project.user values ('" + username + "', '" + password + "', '" + first_name + "', '" + last_name + "', '" + email + "', '" + shippingAdd + "', '" + billingAdd + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @return
     */
    protected void addAddress() {

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
