package escapehub1;

import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private String userId;
    private int roomId;
    private LocalDateTime reserveTime;

    // ✅ 기본 생성자
    public Reservation() {}

    // ✅ 전체 필드 초기화 생성자
    public Reservation(int reservationId, String userId, int roomId, LocalDateTime reserveTime) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.roomId = roomId;
        this.reserveTime = reserveTime;
    }

    // ✅ getter
    public int getReservationId() { return reservationId; }
    public String getUserId() { return userId; }
    public int getRoomId() { return roomId; }
    public LocalDateTime getReserveTime() { return reserveTime; }

    // ✅ setter
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public void setReserveTime(LocalDateTime reserveTime) { this.reserveTime = reserveTime; }
}
