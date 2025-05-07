package escapehub1;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservationManager {

    private static Scanner sc = new Scanner(System.in);

    // 예약 생성 (사용자 선택 기반)
    public static void startReservation(String userId, int roomId) {
        EscapeRoom selectedRoom = EscapeRoomDAO.findRoomById(roomId);
        if (selectedRoom == null) {
            System.out.println("❌ 해당 ID의 방이 존재하지 않습니다.");
            return;
        }

        System.out.print("예약할 날짜 입력 (예: 2025-05-01): ");
        String dateInput = sc.nextLine().trim();
        
     // 1️⃣ 예약 날짜가 AVAILABLE_TIMES에 없는 경우 자동으로 기존 시간 복제
        TimeSlotManager.ensureTimeSlotsForDate(roomId, dateInput);


        List<String> availableTimes = TimeSlotManager.getAvailableTimeSlots(roomId, dateInput);
        List<LocalDateTime> reservedTimes = ReservationDAO.getReservedTimes(roomId);

        List<String> remainingTimes = new ArrayList<>();
        for (String time : availableTimes) {
            LocalDateTime dateTime = LocalDateTime.parse(dateInput + " " + time,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            if (!reservedTimes.contains(dateTime)) {
                remainingTimes.add(time);
            }
        }

        if (remainingTimes.isEmpty()) {
            System.out.println("⚠️ 선택한 날짜에 예약 가능한 시간이 없습니다.");
            return;
        }

        System.out.println("예약 가능한 시간:");
        remainingTimes.forEach(t -> System.out.println("- " + t));

        System.out.print("예약할 시간 선택 (예: 14:00): ");
        String selectedTime = sc.nextLine();

        try {
            LocalDateTime reserveTime = LocalDateTime.parse(dateInput + " " + selectedTime,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            boolean success = ReservationDAO.insertReservation(userId, roomId, Timestamp.valueOf(reserveTime));
            if (success) {
                System.out.println("✅ 예약이 완료되었습니다!");
                System.out.printf("- 방 이름: %s\n- 예약자: %s\n- 예약 시간: %s\n",
                        selectedRoom.getName(), userId, reserveTime.format(formatter));
            } else {
                System.out.println("❌ 예약 실패 (DB 오류)");
            }
        } catch (Exception e) {
            System.out.println("❌ 시간 형식 오류입니다.");
        }
    }

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // 내 예약 조회 및 취소
    public static void myReservations(String userId) {
        List<Reservation> list = ReservationDAO.getUserReservations(userId);

        if (list.isEmpty()) {
            System.out.println("📭 예약 내역이 없습니다.");
            return;
        }

        System.out.println("\n===== MY 예약 내역 =====");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Reservation r : list) {
            EscapeRoom room = EscapeRoomDAO.findRoomById(r.getRoomId());
            String roomName = (room != null) ? room.getName() : "(알 수 없음)";
            String date = r.getReserveTime().format(dateFormatter);
            String time = r.getReserveTime().format(timeFormatter);

            System.out.printf("[예약번호: %d] 방: %s (ID: %d), 날짜: %s, 시간: %s\n",
                    r.getReservationId(), roomName, r.getRoomId(), date, time);
        }

        System.out.print("예약 취소를 원하면 예약번호 입력 (취소 안하려면 0): ");
        String inputStr = sc.nextLine().trim();

        int input;
        try {
            input = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
            System.out.println("❌ 숫자로 입력해주세요.");
            return;
        }

        if (input == 0) return;

        System.out.print("정말 취소하시겠습니까? (Y/N): ");
        String confirm = sc.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean deleted = ReservationDAO.deleteReservation(input, userId);
            System.out.println(deleted ? "✅ 예약이 취소되었습니다." : "❌ 예약 취소 실패 (번호 확인)");
        } else {
            System.out.println("❌ 예약 취소가 중단되었습니다.");
        }
    }


    // 사용자 예약 목록에서 리뷰 가능한 방 반환
    public static List<EscapeRoom> getReservedRoomsByUser(String userId) {
        List<EscapeRoom> reservedRooms = new ArrayList<>();

        String sql = "SELECT DISTINCT r.* FROM ESCAPE_ROOMS r " +
                "JOIN RESERVATIONS res ON r.room_id = res.room_id " +
                "WHERE res.user_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reservedRooms.add(new EscapeRoom(
                        rs.getInt("room_id"),
                        rs.getString("name"),
                        rs.getString("branch_name"),
                        rs.getString("location"),
                        rs.getString("theme"),
                        rs.getString("difficulty"),
                        rs.getDouble("rating")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservedRooms;
    }

    // 별점 등록
    public static void rateRoom(String userId, int roomId) {
        System.out.print("별점을 입력하세요 (0.0 ~ 5.0): ");
        try {
            double rating = Double.parseDouble(sc.nextLine());
            if (rating < 0.0 || rating > 5.0) {
                System.out.println("❌ 별점 범위는 0.0 ~ 5.0 입니다.");
                return;
            }
            boolean updated = EscapeRoomDAO.addRating(roomId, rating);
            System.out.println(updated ? "✅ 별점이 등록되었습니다." : "❌ 별점 등록 실패");
        } catch (NumberFormatException e) {
            System.out.println("❌ 숫자를 입력해주세요.");
        }
    }
}
