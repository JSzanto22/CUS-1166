public class PendingRequest {

    private final String id;
    private final String type;
    private final String summary;
    private final String logMessageOnAccept;
    private final Object requestData;
    private final Job jobToQueueOnAccept;
    private final Runnable acceptAction;
    private final Runnable rejectAction;

    public PendingRequest(String id, String type, String summary, String logMessageOnAccept, Object requestData, Job jobToQueueOnAccept,
                          Runnable acceptAction, Runnable rejectAction) {
        this.id = id;
        this.type = type;
        this.summary = summary;
        this.logMessageOnAccept = logMessageOnAccept;
        this.requestData = requestData;
        this.jobToQueueOnAccept = jobToQueueOnAccept;
        this.acceptAction = acceptAction;
        this.rejectAction = rejectAction;
    }

    public String getId() {
        return id;
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

    public Object getRequestData() {
        return requestData;
    }

    public Job getJobToQueueOnAccept() {
        return jobToQueueOnAccept;
    }

    public void accept() {
        if (acceptAction != null) {
            acceptAction.run();
        }
    }

    public void reject() {
        if (rejectAction != null) {
            rejectAction.run();
        }
    }
}
