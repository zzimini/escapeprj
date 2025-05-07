package escapehub1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotManager {

    public static List<String> getAvailableTimeSlots(int roomId, String dateInput) {
        List<String> available = new ArrayList<>();

        String sql = "SELECT TO_CHAR(available_time, 'HH24:MI') AS time_str " +
                     "FROM AVAILABLE_TIMES " +
                     "WHERE room_id = ? AND TO_CHAR(available_time, 'YYYY-MM-DD') = ? " +
                     "AND available_time NOT IN (" +
                     "   SELECT reserve_time FROM RESERVATIONS WHERE room_id = ?" +
                     ") ORDER BY available_time";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            pstmt.setString(2, dateInput);
            pstmt.setInt(3, roomId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                available.add(rs.getString("time_str"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return available;
    }

    public static List<String> getAvailableTimesForRoom(int roomId) {
        List<String> times = new ArrayList<>();
        String sql = "SELECT DISTINCT TO_CHAR(available_time, 'HH24:MI') AS time_str " +
                     "FROM AVAILABLE_TIMES WHERE room_id = ? ORDER BY time_str";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                times.add(rs.getString("time_str"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    public static void ensureTimeSlotsForDate(int roomId, String date) {
        List<String> baseTimes = getAvailableTimesForRoom(roomId);
        if (baseTimes.isEmpty()) return;

        String checkSql = "SELECT COUNT(*) FROM AVAILABLE_TIMES WHERE room_id = ? AND TO_CHAR(available_time, 'YYYY-MM-DD') = ?";
        String insertSql = "INSERT INTO AVAILABLE_TIMES (room_id, available_time) VALUES (?, ?)";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkStmt.setInt(1, roomId);
            checkStmt.setString(2, date);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return;

            for (String t : baseTimes) {
                LocalDateTime dt = LocalDateTime.parse(date + " " + t, formatter);
                insertStmt.setInt(1, roomId);
                insertStmt.setTimestamp(2, Timestamp.valueOf(dt));
                insertStmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean insertTimeForAllDates(int roomId, String timeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2030, 12, 31);

        String checkSql = "SELECT COUNT(*) FROM AVAILABLE_TIMES WHERE room_id = ? AND available_time = ?";
        String insertSql = "INSERT INTO AVAILABLE_TIMES (room_id, available_time) VALUES (?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                LocalDateTime dt = LocalDateTime.parse(d + " " + timeStr.trim(), formatter);
                Timestamp ts = Timestamp.valueOf(dt);

                checkStmt.setInt(1, roomId);
                checkStmt.setTimestamp(2, ts);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) continue;

                insertStmt.setInt(1, roomId);
                insertStmt.setTimestamp(2, ts);
                insertStmt.executeUpdate();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTimeForAllDates(int roomId, String timeStr) {
        String sql = "DELETE FROM AVAILABLE_TIMES WHERE room_id = ? AND TO_CHAR(available_time, 'HH24:MI') = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            pstmt.setString(2, timeStr.trim());
            int result = pstmt.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateTimeForAllDates(int roomId, String oldTime, String newTime) {
        boolean deleted = deleteTimeForAllDates(roomId, oldTime);
        boolean inserted = insertTimeForAllDates(roomId, newTime);
        return deleted && inserted;
    }

    public static boolean updateMultipleTimesForAllDates(int roomId, List<String[]> timePairs) {
        boolean success = true;
        for (String[] pair : timePairs) {
            if (pair.length == 2) {
                if (!updateTimeForAllDates(roomId, pair[0].trim(), pair[1].trim())) {
                    success = false;
                    System.out.println("❌ 변경 실패: " + pair[0] + " > " + pair[1]);
                }
            }
        }
        return success;
    }

    public static boolean deleteMultipleTimesForAllDates(int roomId, List<String> times) {
        boolean success = true;
        for (String t : times) {
            if (!deleteTimeForAllDates(roomId, t.trim())) {
                success = false;
                System.out.println("❌ 삭제 실패: " + t.trim());
            }
        }
        return success;
    }

    public static void insertSampleTimeSlotsForFullYear(String startDate, int days) {
        try (Connection conn = ConnectionManager.getConnection()) {
            conn.setAutoCommit(false);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            int room1 = 1;
            String[] times1 = {"09:50", "11:10", "12:30", "13:50", "15:10", "16:30", "17:50", "19:10", "20:30", "21:50"};
            int room2 = 2;
            String[] times2 = {"12:05", "15:50"};
            int room3 = 3;
            String[] times3 = {"11:00", "13:30"};

            LocalDate base = LocalDate.parse(startDate);
            for (int i = 0; i < days; i++) {
                String date = base.plusDays(i).toString();
                insertSlotsForDate(conn, formatter, room1, date, times1);
                insertSlotsForDate(conn, formatter, room2, date, times2);
                insertSlotsForDate(conn, formatter, room3, date, times3);
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertSlotsForDate(Connection conn, DateTimeFormatter formatter, int roomId, String date, String[] times) throws Exception {
        String deleteSql = "DELETE FROM AVAILABLE_TIMES WHERE room_id = ? AND TO_CHAR(available_time, 'YYYY-MM-DD') = ?";
        String insertSql = "INSERT INTO AVAILABLE_TIMES (room_id, available_time) VALUES (?, ?)";

        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            deleteStmt.setInt(1, roomId);
            deleteStmt.setString(2, date);
            deleteStmt.executeUpdate();

            for (String t : times) {
                Timestamp ts = Timestamp.valueOf(LocalDateTime.parse(date + " " + t, formatter));
                insertStmt.setInt(1, roomId);
                insertStmt.setTimestamp(2, ts);
                insertStmt.executeUpdate();
            }
        }
    }
}
