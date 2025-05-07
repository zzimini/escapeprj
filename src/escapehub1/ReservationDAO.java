package escapehub1;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
	
	//예약번호 확인
	public static Reservation findReservationById(int reservationId) {
	    String sql = "SELECT * FROM RESERVATIONS WHERE reservation_id = ?";

	    try (Connection conn = ConnectionManager.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, reservationId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return new Reservation(
	                rs.getInt("reservation_id"),
	                rs.getString("user_id"),
	                rs.getInt("room_id"),
	                rs.getTimestamp("reserve_time").toLocalDateTime()
	            );
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}

	//예약 관리/삭제 2번페이지(관리자)
	public static List<Reservation> getReservationsByRoomId(int roomId) {
	    List<Reservation> list = new ArrayList<>();
	    String sql = "SELECT * FROM RESERVATIONS WHERE room_id = ?";

	    try (Connection conn = ConnectionManager.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, roomId);
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            list.add(new Reservation(
	                    rs.getInt("reservation_id"),
	                    rs.getString("user_id"),
	                    rs.getInt("room_id"),
	                    rs.getTimestamp("reserve_time").toLocalDateTime()
	            ));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}

	//방삭제 - 예약 확인
	 // EscapeRoomDAO.java 또는 ReservationDAO.java 중 한 곳에 추가
	    public static boolean hasReservationsForRoom(int roomId) {
	        String sql = "SELECT COUNT(*) FROM RESERVATIONS WHERE room_id = ?";

	        try (Connection conn = ConnectionManager.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setInt(1, roomId);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                return rs.getInt(1) > 0;
	            }

	        } catch (Exception e) {
	            //e.printStackTrace();
	        }

	        return false;
	    }

    // 예약 삽입
    public static boolean insertReservation(String userId, int roomId, Timestamp reserveTime) {
        String sql = "INSERT INTO RESERVATIONS (reservation_id, user_id, room_id, reserve_time) " +
                     "VALUES (reservation_seq.NEXTVAL, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setInt(2, roomId);
            pstmt.setTimestamp(3, reserveTime);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 사용자 예약 목록
    public static List<Reservation> getUserReservations(String userId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM RESERVATIONS WHERE user_id = ? ORDER BY reserve_time DESC";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getString("user_id"),
                        rs.getInt("room_id"),
                        rs.getTimestamp("reserve_time").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 예약 삭제
    public static boolean deleteReservation(int reservationId,String userId) {
        String sql = "DELETE FROM RESERVATIONS WHERE reservation_id = ? AND user_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationId);
            pstmt.setString(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            //e.printStackTrace();
        }
        return false;
    }

    // 예약된 시간 가져오기
    public static List<LocalDateTime> getReservedTimes(int roomId) {
        List<LocalDateTime> list = new ArrayList<>();
        String sql = "SELECT reserve_time FROM RESERVATIONS WHERE room_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getTimestamp("reserve_time").toLocalDateTime());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM RESERVATIONS ORDER BY reservation_id DESC";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("reservation_id"));
                r.setUserId(rs.getString("user_id"));
                r.setRoomId(rs.getInt("room_id"));
                r.setReserveTime(rs.getTimestamp("reserve_time").toLocalDateTime());
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public static boolean deleteReservationByAdmin(int reservationId) {
        String sql = "DELETE FROM RESERVATIONS WHERE reservation_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationId);
            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
