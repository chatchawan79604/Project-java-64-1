package ku.cs.services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class CategoryDataSource implements DataSource<HashMap<String, ArrayList>>{
    private final String directoryName;
    private final String filename;

    HashMap<String , ArrayList> mapAttributeProducts;

    public CategoryDataSource(){
        this("data", "category_data.csv");
    }

    public CategoryDataSource(String directoryName, String filename){
        this.directoryName = directoryName;
        this.filename = filename;
        makeFileIfNotExist(directoryName, filename);
    }

    @Override
    public HashMap<String ,ArrayList> readData() {
        mapAttributeProducts = new HashMap<>();

        String path = directoryName + File.separator + filename;
        File file = new File(path);

        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);

            String line = "";
            while( (line = buffer.readLine())!=null ){
                String[] data = line.split(",");

                ArrayList<String> attributeList = new ArrayList<>();
                for(int i=1; i< data.length; i++){
                    attributeList.add(data[i]);
                }
                mapAttributeProducts.put(data[0],attributeList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mapAttributeProducts;
    }

    @Override
    public void writeData(HashMap<String, ArrayList> map) {
        String path = directoryName + File.separator + filename;
        File file = new File(path);

        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            for(String category : map.keySet()){
                String result = "";
                result += category ;
                for(int i=0; i<map.get(category).size(); i++){
                    result += "," + map.get(category).get(i);
                }
                buffer.write(result);
                buffer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                buffer.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void addData(String category, ArrayList<String> attributes){
        String path = directoryName + File.separator + filename;
        File file = new File(path);
        FileWriter writer = null;
        BufferedWriter buffer = null;

        try {
            writer = new FileWriter(file,StandardCharsets.UTF_8,true);
            buffer = new BufferedWriter(writer);

            String result = category;
            for(String attribute : attributes){
                result += "," + attribute;
            }
            buffer.write(result);
            buffer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
        File file = new File(directoryName);
        if(!file.exists()){
            file.mkdir();
        }
        String path = directoryName + File.separator + filename;
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
