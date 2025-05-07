package escapehub1;

public class Review {
    private int reviewId;
    private String userId;
    private int roomId;
    private String content;
    private double rating;

    public Review(int reviewId, String userId, int roomId, String content, double rating) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.roomId = roomId;
        this.content = content;
        this.rating = rating;
    }

    public int getReviewId() { return reviewId; }
    public String getUserId() { return userId; }
    public int getRoomId() { return roomId; }
    public String getContent() { return content; }
    public double getRating() { return rating; }
}
