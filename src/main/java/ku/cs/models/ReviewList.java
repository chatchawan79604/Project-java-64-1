package ku.cs.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReviewList {
    private ArrayList<Review> reviews;

    public ReviewList(){
        reviews = new ArrayList<>();
    }

    public void addReviewList(Review review){
        reviews.add(review);
    }

    // คืนค่า ReviewList ที่เก็บสินค้าเรียงข้อมูลจากวันที่ล่าสุดขึ้นก่อน
    // ใช้ตอนอ่านไฟล์
    public void sortLastReviewList(){
        // มีการเปลี่ยนแปลงข้อมูล products (type ArrayList)
        Collections.sort(reviews, new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                if(o1.getTime().isBefore(o2.getTime())) {return 1;}
                if(o1.getTime().isAfter(o2.getTime())) {return -1;}
                return 0;
            }
        });
    }

    public int getSize(){
        return reviews.size();
    }

    public Review getByIndex(int i){
        return reviews.get(i);
    }

    public int getNumStar(int n){
        int result = 0;
        for(Review review : reviews){
            if(review.getRating() == n){
                result += 1;
            }
        }
        return result;
    }

    public double getAverageOfEachStar(int n){
        if(reviews.size() <= 0) return 0;
        double numStar = this.getNumStar(n);
        return (numStar/reviews.size());
    }

    public String toCsv(){
        String result = "";
        for(Review review : reviews){
            result += review.toString() + "\n";
        }
        return result;
    }

    public double averageRating(){
        if(reviews.size() == 0) return 0;
        double total = 0;
        for(Review review : reviews){
            total += review.getRating();
        }
        return total/reviews.size();
    }

    public boolean usernameReviewerIsInList(String userName){
        for(Review review : reviews){
            if(review.getUsernameReviewer().equals(userName)){
                return true;
            }
        }
        return false;
    }

    public boolean removeReviewIfContain(Review userReview){
        for(Review review : reviews){
            if(review.toString().equals(userReview.toString())){
                reviews.remove(review);
                return true;
            }
        }
        return false;
    }
}
