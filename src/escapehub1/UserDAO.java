package escapehub1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // 회원가입
    public static boolean registerUser(User user) {
        String sql = "INSERT INTO USERS (user_id, password, is_admin) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.isAdmin() ? 1 : 0);

            int result = pstmt.executeUpdate();
            return result > 0; // 성공하면 true

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    // 로그인 (아이디, 비밀번호 확인)
    public static User login(String userId, String password) {
        String sql = "SELECT * FROM USERS WHERE user_id = ? AND password = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                boolean isAdmin = rs.getInt("is_admin") == 1;
                return new User(userId, password, isAdmin); // 로그인 성공
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 로그인 실패
    }
    public static List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT user_id, password, is_admin FROM USERS ORDER BY user_id";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("user_id");
                String pw = rs.getString("password");
                boolean isAdmin = rs.getInt("is_admin")==1;
                userList.add(new User(id, pw, isAdmin));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    public static boolean deleteUser(String userId) {
        String sql = "DELETE FROM USERS WHERE user_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
}
