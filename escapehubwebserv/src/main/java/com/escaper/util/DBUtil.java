package com.escaper.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // 본인 DB에 맞게 변경
    private static final String USER = "JIMIN";  // 본인 계정
    private static final String PASSWORD = "0000";  // 본인 비밀번호

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    
}
