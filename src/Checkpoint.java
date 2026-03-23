import java.io.*;
import java.time.LocalDateTime;

/**
 * Checkpoint class for saving and loading job state snapshots.
 * 
 * Allows a job to be paused and resumed by saving current state to disk.
 */
public class Checkpoint implements Serializable {
    private String checkpointId;
    private LocalDateTime timestamp;
    private String jobState;

    /**
     * Construct a new checkpoint.
     * 
     * @param checkpointId unique identifier for this checkpoint
     * @param timestamp when the checkpoint was created
     * @param jobState the state of the job at checkpoint time
     */
    public Checkpoint(String checkpointId, LocalDateTime timestamp, String jobState) {
        this.checkpointId = checkpointId;
        this.timestamp = timestamp;
        this.jobState = jobState;
    }

    /**
     * Save this checkpoint to disk as a serialized file.
     */
    public void saveState() {
        try {
            new File("checkpoints").mkdirs();
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("checkpoints/" + checkpointId + ".cp"))) {
                oos.writeObject(this);
                System.out.println("Checkpoint saved: " + checkpointId);
            }
        } catch (IOException e) {
            System.err.println("Error saving checkpoint: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load a checkpoint from disk.
     * 
     * @param checkpointId the id of the checkpoint to load
     * @return the loaded Checkpoint object, or null if not found
     */
    public static Checkpoint loadState(String checkpointId) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("checkpoints/" + checkpointId + ".cp"))) {
            Checkpoint cp = (Checkpoint) ois.readObject();
            System.out.println("Checkpoint loaded: " + checkpointId);
            return cp;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading checkpoint: " + e.getMessage());
            return null;
        }
    }

    public String getCheckpointId() {
        return checkpointId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }

    @Override
    public String toString() {
        return String.format("Checkpoint { id=%s, timestamp=%s, jobState=%s }", 
            checkpointId, timestamp, jobState);
    }
}
