package ku.cs.models;

import java.time.LocalDateTime;

public class Review {
    private int rating;
    private String comment;
    private String nameReviewer;
    private LocalDateTime time;
    private String usernameReviewer;

    // ใช้ตอน user รีวิวแบบมี comment
    public Review(int rating, String comment, String reviewer, String usernameReviewer) {
        this.rating = rating;
        this.comment = comment;
        this.nameReviewer = reviewer;
        this.time = LocalDateTime.now();
        this.usernameReviewer = usernameReviewer;
    }

    // ใช้ตอน user รีวิวไม่แบบมี comment
    public Review(int rating, String reviewer, String usernameReviewer){
        this(rating, "", reviewer, usernameReviewer);
    }

    // ใช้ตอนอ่านไฟล์
    public Review(int rating, String comment, String reviewer, String usernameReviewer, LocalDateTime time) {
        this.rating = rating;
        this.comment = comment;
        this.nameReviewer = reviewer;
        this.usernameReviewer = usernameReviewer;
        this.time = time;
    }

    public LocalDateTime getTime(){
        return time;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getNameReviewer() {
        return nameReviewer;
    }

    public String getUsernameReviewer() {
        return usernameReviewer;
    }

    public String toString(){
        return rating + "," + nameReviewer + "," + usernameReviewer + "," + time + "\n" + comment;
    }


}
