package escapehub1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EscapeRoomDAO {

    // (이미 있던) 방 등록
    public static boolean insertRoom(EscapeRoom room) {
        String sql = "INSERT INTO ESCAPE_ROOMS (room_id, name, branch_name, location, theme, difficulty, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, room.getRoomId());
            pstmt.setString(2, room.getName());
            pstmt.setString(3, room.getBranchName());
            pstmt.setString(4, room.getLocation());
            pstmt.setString(5, room.getTheme());
            pstmt.setString(6, room.getDifficulty());
            pstmt.setDouble(7, room.getRating());

			
			  int result = pstmt.executeUpdate(); return result > 0;
			 
            
            //return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
            return false;
        }
    }

    // 방 ID로 방 찾기
    public static EscapeRoom findRoomById(int roomId) {
        String sql = "SELECT * FROM ESCAPE_ROOMS WHERE room_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new EscapeRoom(
                        rs.getInt("room_id"),
                        rs.getString("name"),
                        rs.getString("branch_name"),
                        rs.getString("location"),
                        rs.getString("theme"),
                        rs.getString("difficulty"),
                        rs.getDouble("rating")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    


    // 전체 방 조회
    public static List<EscapeRoom> getAllRooms() {
        List<EscapeRoom> roomList = new ArrayList<>();
        String sql = "SELECT * FROM ESCAPE_ROOMS";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EscapeRoom room = makeRoom(rs);
                roomList.add(room);
            }

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return roomList;
    }

    // 지역별 조회
    public static List<EscapeRoom> getRoomsByLocation(String location) {
        List<EscapeRoom> roomList = new ArrayList<>();
        String sql = "SELECT * FROM ESCAPE_ROOMS WHERE location = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, location);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EscapeRoom room = makeRoom(rs);
                roomList.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomList;
    }

    // 테마별 조회
    public static List<EscapeRoom> getRoomsByTheme(String theme) {
        List<EscapeRoom> roomList = new ArrayList<>();
        String sql = "SELECT * FROM ESCAPE_ROOMS WHERE theme = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, theme);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EscapeRoom room = makeRoom(rs);
                roomList.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomList;
    }

    // 난이도별 조회
    public static List<EscapeRoom> getRoomsByDifficulty(String difficulty) {
        List<EscapeRoom> roomList = new ArrayList<>();
        String sql = "SELECT * FROM ESCAPE_ROOMS WHERE difficulty = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, difficulty);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EscapeRoom room = makeRoom(rs);
                roomList.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomList;
    }

    // 인기순(별점 높은 순) 조회
    public static List<EscapeRoom> getRoomsByPopularity() {
        List<EscapeRoom> roomList = new ArrayList<>();
        String sql = "SELECT * FROM ESCAPE_ROOMS ORDER BY rating DESC";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                EscapeRoom room = makeRoom(rs);
                roomList.add(room);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomList;
    }

    // 방 수정
    public static boolean updateRoom(EscapeRoom room) {
        String sql = "UPDATE ESCAPE_ROOMS SET name = ?, branch_name = ?, location = ?, theme = ?, difficulty = ? WHERE room_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, room.getName());
            pstmt.setString(2, room.getBranchName());
            pstmt.setString(3, room.getLocation());
            pstmt.setString(4, room.getTheme());
            pstmt.setString(5, room.getDifficulty());
            pstmt.setInt(6, room.getRoomId());

            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 방 삭제
    public static boolean deleteRoom(int id) {
        String deleteTimes = "DELETE FROM AVAILABLE_TIMES WHERE room_id = ?";
        String deleteRoom = "DELETE FROM ESCAPE_ROOMS WHERE room_id = ?";

        try (Connection conn = ConnectionManager.getConnection()) {
            conn.setAutoCommit(false); // 트랜잭션 처리

            try (
                PreparedStatement pstmt1 = conn.prepareStatement(deleteTimes);
                PreparedStatement pstmt2 = conn.prepareStatement(deleteRoom)
            ) {
                pstmt1.setInt(1, id);
                pstmt1.executeUpdate();

                pstmt2.setInt(1, id);
                int affected = pstmt2.executeUpdate();

                conn.commit();
                return affected > 0;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // 공통 EscapeRoom 객체 생성
    private static EscapeRoom makeRoom(ResultSet rs) throws SQLException {
        return new EscapeRoom(
                rs.getInt("room_id"),
                rs.getString("name"),
                rs.getString("branch_name"),
                rs.getString("location"),
                rs.getString("theme"),
                rs.getString("difficulty"),
                rs.getDouble("rating")
        );
    }

    // 별점 추가 및 평균 갱신
    public static boolean addRating(int roomId, double newRating) {
        String selectSql = "SELECT rating, rating_count FROM ESCAPE_ROOMS WHERE room_id = ?";
        String updateSql = "UPDATE ESCAPE_ROOMS SET rating = ?, rating_count = ? WHERE room_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            selectStmt.setInt(1, roomId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                double currentRating = rs.getDouble("rating");
                int count = rs.getInt("rating_count");
                double newAverage = ((currentRating * count) + newRating) / (count + 1);
                int newCount = count + 1;

                updateStmt.setDouble(1, newAverage);
                updateStmt.setInt(2, newCount);
                updateStmt.setInt(3, roomId);

                return updateStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
