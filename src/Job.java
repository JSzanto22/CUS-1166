import java.io.Serializable;
import java.time.LocalDateTime;

public class Job implements Serializable{
    // Attributes from UML
    private String jobId;
    private String status;
    private int duration;
    private LocalDateTime deadline;
    private String clientId;
    private String clientName;
    private String requestedDeadline;

    // Constructor to initialize a new Job
    public Job(String jobId, String status, int duration, LocalDateTime deadline) {
        this.jobId = jobId;
        this.status = status;
        this.duration = duration;
        this.deadline = deadline;
    }

    public Job(String jobId, String status, int duration, LocalDateTime deadline,
               String clientId, String clientName, String requestedDeadline) {
        this(jobId, status, duration, deadline);
        this.clientId = clientId;
        this.clientName = clientName;
        this.requestedDeadline = requestedDeadline;
    }


    public void updateStatus(String status) {
        this.status = status;
    }

    public void complete() {
        this.status = "Completed";

    }

  
    public String getJobId() {
        return jobId;
    }

    public int getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getRequestedDeadline() {
        return requestedDeadline;
    }
}

