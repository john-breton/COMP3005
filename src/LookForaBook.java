import java.sql.*;

public class LookForaBook {

    private boolean userIsAdmin;

    protected boolean lookForaLogin(String username, String password)
    {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook", "ryan", "")) {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT password from project.user where user_name = '" + username.toLowerCase().trim() + "'");

            while(result.next()) {
                String correctPassword = result.getString("password");

                if(password.equals(correctPassword))
                    return true;
            }
            return false;

        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return false;
    }
}
