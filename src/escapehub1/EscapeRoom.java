package escapehub1;

//방탈출 정보 DB 연결

public class EscapeRoom {
    private int roomId;
    private String name;
    private String branchName; 
    private String location;
    private String theme;
    private String difficulty;
    private double rating; // 별점 평균

    public EscapeRoom(int roomId, String name, String branchName, String location, String theme, String difficulty, double rating) {
        this.roomId = roomId;
        this.name = name;
        this.branchName = branchName;
        this.location = location;
        this.theme = theme;
        this.difficulty = difficulty;
        this.rating = rating;
    }

    // Getter/Setter
    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }
    
    public String getBranchName() {
        return branchName;
    }

    public String getLocation() {
        return location;
    }

    public String getTheme() {
        return theme;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    
    @Override
    public String toString() {
        return "[" + roomId + "] " + name + " (" + branchName + ", " + location + ", " + theme + ", " + difficulty + ", 평점: " + rating + ")";
    }
    
 // EscapeRoom 클래스에 추가할 setter 메서드
    public void setName(String name) {
        this.name = name;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
