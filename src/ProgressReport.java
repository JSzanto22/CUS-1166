import java.time.LocalDateTime;

public class ProgressReport {
    private String reportId;
    private String jobId;
    private String status;
    private double progress;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Construct a new report.
     *
     * @param reportId  unique id for this report
     * @param jobId     associated job id
     */
    public ProgressReport(String reportId, String jobId) {
        this.reportId = reportId;
        this.jobId = jobId;
        this.status = "not started";
        this.progress = 0.0;
        this.startTime = LocalDateTime.now();
    }

    /**
     * Update the job progress percentage. Values are clamped to [0, 100].
     *
     * @param progress percentage of completion
     */
    public void updateProgress(double progress) {
        if (progress < 0.0) {
            this.progress = 0.0;
        } else if (progress > 100.0) {
            this.progress = 100.0;
        } else {
            this.progress = progress;
        }
    }

    /**
     * Update the job status text.
     *
     * @param status new state label
     */
    public void updateStatus(String status) {
        this.status = status;
    }

    /**
     * Return a simple text summary suitable for console/log display.
     */
    public String getSummary() {
        StringBuilder builder = new StringBuilder();
        builder.append("Progress Report ID: ").append(reportId).append("\n");
        builder.append("Job ID: ").append(jobId).append("\n");
        builder.append("Status: ").append(status).append("\n");
        builder.append("Progress: ").append(progress).append("%\n");
        builder.append("Start Time: ").append(startTime != null ? startTime : "N/A").append("\n");
        builder.append("End Time: ").append(endTime != null ? endTime : "N/A").append("\n");
        return builder.toString();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getReportId() {
        return reportId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public double getProgress() {
        return progress;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void createCheckpoint() {
        Checkpoint checkpoint = new Checkpoint(reportId, LocalDateTime.now(), getSummary());
        checkpoint.saveState();
    }
}