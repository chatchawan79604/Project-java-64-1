package ku.cs.services;

import ku.cs.models.Review;
import ku.cs.models.ReviewList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ReviewDataSource implements DataSource<ReviewList> {
    private String directoryName;
    private String fileName;

    public ReviewDataSource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        makeFileIfNotExist(directoryName, fileName);
    }

    public void makeFileIfNotExist(String directory, String targetFile) {
        File file = new File(directoryName);
        if(!file.exists()){
            file.mkdir();
        }
        String path = directoryName + File.separator + fileName;
        file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // อ่านข้อมูลจากไฟล์ csv มาสร้างเป็น object Review แล้วเพิ่มใน ReviewList
    public ReviewList readData(){
        ReviewList reviewList = new ReviewList();

        String path = directoryName + File.separator + fileName;
        File file =  new File(path);

        FileReader reader = null;
        BufferedReader buffer = null;
        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);

            String line = "";
            while ( (line = buffer.readLine())!=null ){
                String[] data = line.split(",");
                int rating = Integer.parseInt(data[0]);
                String nameReviewer = data[1];
                String usernameReviewer = data[2];
                LocalDateTime time = LocalDateTime.parse(data[3], DateTimeFormatter.ISO_DATE_TIME);
                line = buffer.readLine();
                String comment = line;

                Review review = new Review(rating,comment,nameReviewer, usernameReviewer,time);
                reviewList.addReviewList(review);
            }
            reviewList.sortLastReviewList();
        }
        catch (FileNotFoundException e){
            System.err.println("Cannot read file "+ path);
        }
        catch (IOException e) {
            System.err.println("Error reading form data");
        }
        finally {
            try{
                buffer.close();
                reader.close();
            } catch (IOException e){
                System.err.println("Error closing files");
            }
        }
        return reviewList;
    }

    public void writeData(ReviewList reviewList) {
        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file,StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            buffer.write(reviewList.toCsv());
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addData(Review reviewAdd){
        String path = directoryName + File.separator + fileName;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8,true);
            buffer = new BufferedWriter(writer);

            buffer.write(reviewAdd.toString());
            buffer.newLine();
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
