package ku.cs.services;

public interface DataSource<T> {
    T readData();
    void writeData(T t);
    void makeFileIfNotExist(String directory,String targetFile);
}
