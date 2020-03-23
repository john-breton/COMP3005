import java.sql.*;

/**
 * The LookForaBook class represents the back-end for the LookInnaBook application.
 * The class is responsible for processing user requests and accessing a database
 * via SQL queries.
 *
 * @author Ryan Godfrey, John Breton
 * @version 1.0
 */
public class LookForaBook {

    // Just putting this here so we can change it when we test.
    private static final String USER = "ryan";

    /**
     * Query the login information from the database.
     * Should we use some encryption algorithm? Probably.
     * Will we? Of course not. That sounds like a lot of work.
     *
     * @param username The username that was entered.
     * @param password The password that was entered.
     * @return True if the password matches the password stored for the given username, false otherwise.
     */
    protected boolean[] lookForaLogin(String username, String password) {
        boolean[] returnArr = {false, false}; // [0] = active user, [1] = admin

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", USER, "")) {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * from project.user LEFT OUTER JOIN project.librarian USING (user_name) where user_name = '" + username.toLowerCase().trim() + "'");

            while (result.next()) {
                returnArr[0] = password.equals(result.getString("password"));
                if(returnArr[0])
                    returnArr[1] = result.getString("salary") != null;
            }
            return returnArr;

        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return returnArr;
    }
}
