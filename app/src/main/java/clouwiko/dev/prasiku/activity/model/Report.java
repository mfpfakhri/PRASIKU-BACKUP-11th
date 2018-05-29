package clouwiko.dev.prasiku.activity.model;

/**
 * Created by muham on 29/05/2018.
 */

public class Report {
    private String reportId;
    private String reportDate;
    private String reportReporterId;
    private String reportReporterName;
    private String reportReportedId;
    private String reportReportedName;
    private String reportMessage;
    private String reportStatus;
    private String reportCat;
    private String reportAdoptions;

    public Report() {
    }

    public Report(String reportId, String reportDate, String reportReporterId, String reportReporterName, String reportReportedId, String reportReportedName, String reportMessage, String reportStatus, String reportCat, String reportAdoptions) {
        this.reportId = reportId;
        this.reportDate = reportDate;
        this.reportReporterId = reportReporterId;
        this.reportReporterName = reportReporterName;
        this.reportReportedId = reportReportedId;
        this.reportReportedName = reportReportedName;
        this.reportMessage = reportMessage;
        this.reportStatus = reportStatus;
        this.reportCat = reportCat;
        this.reportAdoptions = reportAdoptions;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportReporterId() {
        return reportReporterId;
    }

    public void setReportReporterId(String reportReporterId) {
        this.reportReporterId = reportReporterId;
    }

    public String getReportReporterName() {
        return reportReporterName;
    }

    public void setReportReporterName(String reportReporterName) {
        this.reportReporterName = reportReporterName;
    }

    public String getReportReportedId() {
        return reportReportedId;
    }

    public void setReportReportedId(String reportReportedId) {
        this.reportReportedId = reportReportedId;
    }

    public String getReportReportedName() {
        return reportReportedName;
    }

    public void setReportReportedName(String reportReportedName) {
        this.reportReportedName = reportReportedName;
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getReportCat() {
        return reportCat;
    }

    public void setReportCat(String reportCat) {
        this.reportCat = reportCat;
    }

    public String getReportAdoptions() {
        return reportAdoptions;
    }

    public void setReportAdoptions(String reportAdoptions) {
        this.reportAdoptions = reportAdoptions;
    }
}
