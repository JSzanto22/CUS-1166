import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Vehicle extends ComputationNode {

    private Owner owner;
    private Job currentJob;
    private List<Checkpoint> checkpoints;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;

    public Vehicle(String id, String status, Owner owner,
                   LocalDateTime arrivalTime, LocalDateTime departureTime) {
        super(id, status);
        this.owner = owner;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.checkpoints = new ArrayList<>();
        this.currentJob = null;
    }

    // Getters and Setters
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setJob(Job job) {
        this.currentJob = job;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    //methods on uml

    public void executeJob() {
        if (currentJob == null) {
            System.out.println("No job assigned to vehicle " + getId());
            return;
        }

        setStatus("BUSY");
        currentJob.updateStatus("IN_PROGRESS");

        System.out.println("Vehicle " + getId() +
                " is executing job " + currentJob.getJobId());
    }

    public Checkpoint triggerCheckpoint() {
        if (currentJob == null) {
            System.out.println("No job to checkpoint.");
            return null;
        }

        Checkpoint checkpoint = new Checkpoint(
                "CP-" + getId() + "-" + (checkpoints.size() + 1),
                LocalDateTime.now(),
                currentJob.getStatus()
        );

        checkpoints.add(checkpoint);

        System.out.println("Checkpoint created for vehicle " + getId());
        return checkpoint;
    }

    public void restoreCheckpoint(Checkpoint checkpoint) {
        if (checkpoint == null) {
            System.out.println("Checkpoint is null.");
            return;
        }

        System.out.println("Restoring vehicle " + getId() +
                " from checkpoint " + checkpoint.getCheckpointId());

        setStatus("RESTORED");
    }

    public void finishJob() {
        if (currentJob == null) {
            System.out.println("No job to finish.");
            return;
        }

        currentJob.complete();
        setStatus("AVAILABLE");

        System.out.println("Vehicle " + getId() +
                " finished job " + currentJob.getJobId());

        currentJob = null;
    }

    public void depart() {
        setStatus("DEPARTED");
        System.out.println("Vehicle " + getId() + " has departed.");
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + getId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", owner=" + (owner != null ? owner.getName() : "none") +
                ", currentJob=" + (currentJob != null ? currentJob.getJobId() : "none") +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                '}';
    }
}