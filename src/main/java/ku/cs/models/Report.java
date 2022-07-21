package ku.cs.models;

import java.time.LocalDateTime;

public class Report<T> {
    private String category;
    private String detail;
    private T reportedObject;
    private String reporter;
    private LocalDateTime time;

    public Report(String category, String detail, T reportedObject, String reporter, LocalDateTime time) {
        this.category = category;
        this.detail = detail;
        this.reportedObject = reportedObject;
        this.reporter = reporter;
        this.time = time;
    }

    public Report(String category, String detail, T reportedObject, String reporter) {
        this(category,detail, reportedObject,reporter,LocalDateTime.now());
    }

    public String getCategory() {
        return category;
    }

    public String getDetail() {
        return detail;
    }

    public T getReportedObject() {
        return reportedObject;
    }

    public String getReporter() {
        return reporter;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return category + "\n" + detail + "\n" + reportedObject.toString() +
                "\n" + reporter +  "," + time;
    }
}
