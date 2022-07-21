package ku.cs.services;

import ku.cs.models.Review;
import ku.cs.models.Report;
import ku.cs.models.ReportList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewReportDataSource implements DataSource<ReportList<Review>>{
    private final String directory;
    private final String filename;

    public ReviewReportDataSource(String directory, String filename) {
        this.directory = directory;
        this.filename = filename;
        makeFileIfNotExist(directory, filename);
    }

    @Override
    public ReportList<Review> readData() {
        ReportList<Review> reportedReviews = new ReportList<>();

        FileReader reader = null;
        BufferedReader buffer = null;
        String line = null;
        try {
            reader = new FileReader(directory + File.separator + filename, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);
            while((line = buffer.readLine()) != null){
                String category = line;
                String detail = buffer.readLine();
                line = buffer.readLine();
                String[] data = line.split(",");

                int rating = Integer.parseInt(data[0]);
                String nameReviewer = data[1];
                String usernameReviewer = data[2];
                LocalDateTime time = LocalDateTime.parse(data[3], DateTimeFormatter.ISO_DATE_TIME);
                line = buffer.readLine();
                String comment = line;
                Review reportedReview = new Review(rating,comment,nameReviewer, usernameReviewer,time);

                line = buffer.readLine();
                data = line.split(",");
                String reporter = data[0];
                time = LocalDateTime.parse(data[1], DateTimeFormatter.ISO_DATE_TIME);
                Report<Review> reviewReport = new Report<>(category,detail,reportedReview,reporter,time);
                reportedReviews.addReport(reviewReport);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                buffer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reportedReviews;
    }

    @Override
    public void writeData(ReportList<Review> reviewReportList) {
        String path = directory + File.separator + filename;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file,StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            buffer.write(reviewReportList.toCsv());
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

    @Override
    public void makeFileIfNotExist(String directory, String targetFile) {
        File file = new File(this.directory);
        if(!file.exists()){
            file.mkdir();
        }
        String path = this.directory + File.separator + filename;
        file = new File(path);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
