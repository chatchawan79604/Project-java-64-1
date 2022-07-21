package ku.cs.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReportList<T> {
    ArrayList<Report<T>> reports;

    public ReportList(ArrayList<Report<T>> reports) {
        this.reports = reports;
    }

    public ReportList() {
        this.reports = new ArrayList<>();
    }

    public void addReport(Report<T> report){
        reports.add(report);
    }

    public String toCsv() {
        String csv = "";
        for (Report<T> report : reports) {
            csv += report.toString() + "\n";
        }
        return csv;
    }

    public int getSize(){
        return reports.size();
    }

    public Report<T> getReportsOfIndex(int index) {
        return reports.get(index);
    }

    public void sortedReportsByTime(){
        // มีการเปลี่ยนแปลงข้อมูล products (type ArrayList)
        Collections.sort(reports, new Comparator<Report>() {
            @Override
            public int compare(Report o1, Report o2) {
                if(o1.getTime().isBefore(o2.getTime())) {return 1;}
                if(o1.getTime().isAfter(o2.getTime())) {return -1;}
                return 0;
            }
        });
    }

    public void removeReportIfContain(Report<T> t) {
        for (Report<T> report : reports) {
            if (t.toString().equals(report.toString())) {
                reports.remove(t);
                return;
            }
        }
        return;
    }
}
