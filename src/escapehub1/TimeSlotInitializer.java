package escapehub1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeSlotInitializer {

    // 관리자가 설정한 시간들을 바탕으로 모든 날짜에 대해 반영
    public static void generateTimeSlotsFromAdminSetting() {
        List<EscapeRoom> rooms = EscapeRoomDAO.getAllRooms();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2030, 12, 31);

        String deleteSql = "DELETE FROM AVAILABLE_TIMES WHERE room_id = ?";
        String insertSql = "INSERT INTO AVAILABLE_TIMES (room_id, available_time) VALUES (?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // 🔁 각 방의 시간 설정을 미리 가져오기
            Map<Integer, List<String>> roomTimeMap = new HashMap<>();
            for (EscapeRoom room : rooms) {
                int roomId = room.getRoomId();
                List<String> adminTimes = TimeSlotManager.getAvailableTimesForRoom(roomId);
                roomTimeMap.put(roomId, adminTimes);
            }

            for (EscapeRoom room : rooms) {
                int roomId = room.getRoomId();

                // 1. 기존 데이터 삭제
                deleteStmt.setInt(1, roomId);
                deleteStmt.executeUpdate();

                List<String> times = roomTimeMap.get(roomId);
                if (times == null || times.isEmpty()) continue;

                // 2. 모든 날짜에 대해 시간 삽입
                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    for (String time : times) {
                        try {
                            LocalDateTime dt = LocalDateTime.parse(date + " " + time, formatter);
                            insertStmt.setInt(1, roomId);
                            insertStmt.setTimestamp(2, Timestamp.valueOf(dt));
                            insertStmt.addBatch();
                        } catch (Exception e) {
                            System.out.println("❌ 잘못된 시간 형식: " + date + " " + time);
                        }
                    }
                }

                insertStmt.executeBatch();  // 한 번에 insert
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
