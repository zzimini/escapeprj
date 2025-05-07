package escapehub1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL = "jdbc:oracle:thin:@192.168.0.196:1521:xe"; 
    private static final String USER = "jimin"; 
    private static final String PASSWORD = "0000"; 

    public static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 로딩
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ DB 연결 실패");
        }
    }
}
