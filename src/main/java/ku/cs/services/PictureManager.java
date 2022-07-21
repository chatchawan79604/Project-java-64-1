package ku.cs.services;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PictureManager {
    public String importProfileImage(String path, String directory) {
        if (checkImageType(path)) {
            String[] spiltPath = path.split("\\.");
            int lastIndex = spiltPath.length;
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
            String pictureName = localDateTime.format(format);
            String outputPicture = "data/images/" + directory + "/"+pictureName + "."+spiltPath[lastIndex-1];
            //ต้องทำระบบตรวจสอบชื่อรูปซ้ำ
            File imageFile = new File(outputPicture);
            while(imageFile.exists()){
                outputPicture = FixImageNameExisted(outputPicture);
                imageFile = new File(outputPicture);
            }
            try {
                InputStream inputStream = new FileInputStream(path);
                OutputStream outputStream = new FileOutputStream(imageFile);
                System.out.println(imageFile.toPath());
                int byteInput;
                while ((byteInput = inputStream.read()) != -1) {
                    outputStream.write(byteInput);
                }
                return outputPicture;
            } catch (IOException e) {
                System.err.println(e);
                return null;
            }
        }
        return "data/images/default/default_profile_picture.png";
    }
    private String FixImageNameExisted(String outputPicture){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        outputPicture = localDateTime.format(format);
        return outputPicture;
    }

    public boolean checkImageType(String path){
        String[] spiltPath = path.split("\\.");
        int lastIndex = spiltPath.length;
        if (spiltPath[lastIndex - 1].equals("jpg") || spiltPath[lastIndex - 1].equals("png") || spiltPath[lastIndex - 1].equals("jpeg")) {
            return true;
        }
        return false;
    }
}
