package escapehub1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReviewManager {

    private static Scanner sc = new Scanner(System.in);
    
    //리뷰 확인
    public static Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM REVIEWS WHERE review_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Review(
                        rs.getInt("review_id"),
                        rs.getString("user_id"),
                        rs.getInt("room_id"),
                        rs.getString("review_content"),
                        rs.getDouble("rating")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    
    //리뷰 ID 조회 -> 삭제
    public static boolean reviewExists(int reviewId) {
        String sql = "SELECT 1 FROM REVIEWS WHERE review_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ✅ 방 ID로 리뷰 조회
    public static void viewReviews(int roomId) {
        System.out.println("\n===== 리뷰 목록 =====");

        String sql = "SELECT user_id, review_content FROM REVIEWS WHERE room_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String content = rs.getString("review_content");

                System.out.println("[" + userId + "] : " + content);
                found = true;
            }

            if (!found) {
                System.out.println("등록된 리뷰가 없습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 뒤로가기
        backToPrevious();
    }

    // ✅ 리뷰 등록
    public static void writeReview(String userId, int roomId, String content, double rating) {
        String sql = "INSERT INTO REVIEWS (review_id, user_id, room_id, review_content, rating) " +
                     "VALUES (review_seq.NEXTVAL, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setInt(2, roomId);
            pstmt.setString(3, content);
            pstmt.setDouble(4, rating);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println("✅ 리뷰가 등록되었습니다!");
                updateRoomRating(conn, roomId);
            } else {
                System.out.println("❌ 리뷰 등록 실패");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateRoomRating(Connection conn, int roomId) {
        String avgSql = "SELECT AVG(rating) FROM REVIEWS WHERE room_id = ?";
        String updateSql = "UPDATE ESCAPE_ROOMS SET rating = ? WHERE room_id = ?";

        try (PreparedStatement avgStmt = conn.prepareStatement(avgSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            avgStmt.setInt(1, roomId);
            ResultSet rs = avgStmt.executeQuery();

            if (rs.next()) {
                double avg = rs.getDouble(1);
                updateStmt.setDouble(1, avg);
                updateStmt.setInt(2, roomId);
                updateStmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 // 전체 리뷰 조회
    public static void viewAllReviewsForAdmin() {
        String sql = "SELECT review_id, user_id, room_id, review_content, rating FROM REVIEWS ORDER BY room_id DESC";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            boolean found = false;
            while (rs.next()) {
                int reviewId = rs.getInt("review_id");
                String userId = rs.getString("user_id");
                int roomId = rs.getInt("room_id");
                String content = rs.getString("review_content");
                double rating = rs.getDouble("rating");
                
                EscapeRoom room = EscapeRoomDAO.findRoomById(roomId);
                String roomInfo = (room != null) ? room.getName() + " (ID: " + roomId + ")" : "(방 정보 없음)";

                System.out.printf("[리뷰ID: %d] 사용자: %s | 방: %s | 평점: %.1f\n- 내용: %s\n\n",
                        reviewId, userId, roomInfo, rating, content);
                found = true;
            }

            if (!found) System.out.println("📭 등록된 리뷰가 없습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 특정 방 리뷰 조회
    public static void viewReviewsByRoomId(int roomId) {
        String sql = "SELECT review_id, user_id, review_content, rating FROM REVIEWS WHERE room_id = ? ORDER BY review_id desc";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                int reviewId = rs.getInt("review_id");
                String userId = rs.getString("user_id");
                String content = rs.getString("review_content");
                double rating = rs.getDouble("rating");

                EscapeRoom room = EscapeRoomDAO.findRoomById(roomId);
                String roomInfo = (room != null) ? room.getName() + " (ID: " + roomId + ")" : "(방 정보 없음)";

                System.out.printf("[리뷰ID: %d] 사용자: %s | 방: %s | 평점: %.1f\n- 내용: %s\n\n",
                        reviewId, userId, roomInfo, rating, content);
                found = true;
            }

            if (!found) System.out.println("해당 방의 리뷰가 없습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 리뷰 삭제
    public static boolean deleteReviewByAdmin(int reviewId) {
        String sql = "DELETE FROM REVIEWS WHERE review_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    // 뒤로가기 입력 메서드
	
	  private static void backToPrevious() { while (true) {
	  System.out.println("\n리뷰닫기(9)"); System.out.print("선택 > "); String input
	  = sc.nextLine();
	  
	  if (input.equals("9")) { break; } else {
	  System.out.println("잘못된 입력입니다. 다시 입력하세요."); } } }
	 
}
