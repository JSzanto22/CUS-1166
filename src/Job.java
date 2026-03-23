import java.time.LocalDateTime;

public class Job {
    // Attributes from UML
    private String jobId;
    private String status;
    private int duration;
    private LocalDateTime deadline;

    // Constructor to initialize a new Job
    public Job(String jobId, String status, int duration, LocalDateTime deadline) {
        this.jobId = jobId;
        this.status = status;
        this.duration = duration;
        this.deadline = deadline;
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
}

