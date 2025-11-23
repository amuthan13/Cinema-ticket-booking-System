import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Update these with your DB credentials before compiling
    private static final String URL = "jdbc:mysql://localhost:3306/cinema_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // ensure driver is loaded
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
