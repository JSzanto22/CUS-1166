import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class VehicularCloudController extends ComputationNode{
	
	private List<Job> activeJobs = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();
	private ResultServer resultServer;
	private static final int VEHICLE_LIMIT = 500;
	
	public VehicularCloudController(String id, String status, ResultServer resultServer) {
		super(id, status);
		this.resultServer = resultServer;
	}
	
	public boolean registerVehicle(Vehicle vehicle) {
		if(vehicles.size()+1 > VEHICLE_LIMIT) {
			return false;
		}
		vehicles.add(vehicle);
		return true;
	}
	
	public void removeVehicle(Vehicle vehicle) {
		vehicles.remove(vehicle);
	}
	
	public void assignJob(Job job, int redundancyLevel) {
		if(redundancyLevel < 0) redundancyLevel = 0;
		
		int i = 0;
		while(redundancyLevel >= 0 && vehicles.size() > i) {
			Vehicle vehicle = vehicles.get(i);
			if(vehicle.getCurrentJob() != null) {
				i++;
				continue;
			}
			vehicle.setCurrentJob(job);
			redundancyLevel--;
			i++;
		}
	}
	
	public Vehicle recruitVehicle(Job job, Checkpoint checkpoint) {
	    for (Vehicle v : vehicles) {
	        if (v.getCurrentJob() == null) {
	            v.setCurrentJob(job);
	            v.restoreCheckpoint(checkpoint);
	            return v;
	        }
	    }
	    return null; // no available vehicle
	}
	
	public void restartComputation(Job job, Vehicle vehicle, Checkpoint checkpoint) {
	    vehicle.setCurrentJob(job);
	    vehicle.restoreCheckpoint(checkpoint);
	}
	
	public void transferCompletedJob(Job job) {
	    resultServer.storeResult(job);
	    activeJobs.remove(job);
	}

	public void addJob(Job job) {
	    if (job != null) {
	        activeJobs.add(job);
	    }
	}

	public List<Job> getActiveJobs() {
	    return new ArrayList<>(activeJobs);
	}
	
	public List<Integer> computeCompletionTime() {
		List<Integer> result = new LinkedList<>();
		int total = 0;
		for(Job j : activeJobs) {
			total += j.getDuration();
			result.add(total);
		}
		return result;
	}

}
