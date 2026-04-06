public class PendingRequest {

    private final String id;
    private final String type;
    private final String summary;
    private final String logMessageOnAccept;
    private final Job jobToQueueOnAccept;

    public PendingRequest(String id, String type, String summary, String logMessageOnAccept, Job jobToQueueOnAccept) {
        this.id = id;
        this.type = type;
        this.summary = summary;
        this.logMessageOnAccept = logMessageOnAccept;
        this.jobToQueueOnAccept = jobToQueueOnAccept;
    }

    public String getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }

    public String getLogMessageOnAccept() {
        return logMessageOnAccept;
    }

    public Job getJobToQueueOnAccept() {
        return jobToQueueOnAccept;
    }
}
