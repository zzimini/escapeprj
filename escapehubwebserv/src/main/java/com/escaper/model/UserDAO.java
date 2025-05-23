package com.escaper.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.escaper.util.DBUtil;

public class UserDAO {
    Connection conn = DBUtil.getConnection();

    public void registerUser(UserDTO user) {
        String sql = "INSERT INTO USERS (USER_ID, PASSWORD) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String userId, String password) {
        String sql = "SELECT * FROM USERS WHERE USER_ID=? AND PASSWORD=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
