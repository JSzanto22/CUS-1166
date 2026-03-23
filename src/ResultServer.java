import java.util.ArrayList;
import java.util.List;

public class ResultServer {
    private final String serverId;
    private final List<Job> completedJobs;

    public ResultServer(String serverId) {
        this.serverId = serverId;
        this.completedJobs = new ArrayList<>();
    }

    public void storeResult(Job job) {
        if (job != null) {
            completedJobs.add(job);
        }
    }

    public void deleteVehicleData(Vehicle vehicle) {
        // soon
    }

    public String getServerId() {
        return serverId;
    }

    public List<Job> getCompletedJobs() {
        return new ArrayList<>(completedJobs);
    }
}
