import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class VehicularCloudController extends ComputationNode{
    public static final String HOST = "localhost";
    public static final int PORT = 9816;
	
	private List<Job> activeJobs = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();
	private ResultServer resultServer;
	private VcRequestQueue requestQueue;
	private static final int VEHICLE_LIMIT = 500;
	// Networking Stuff
	private static ServerSocket serverSocket;
	private final LinkedList<ClientHandler> pendingMessages = new LinkedList<>();


	private class ClientHandler implements Runnable {
	    private Socket socket;
	    private DataInputStream inputStream;
	    private DataOutputStream outputStream;
	    private ClientType type;
	    private Object messageData; // latest message from client

	    ClientHandler(Socket socket, ClientType type) throws IOException {
	        this.socket = socket;
	        this.type = type;
	        this.inputStream = new DataInputStream(socket.getInputStream());
	        this.outputStream = new DataOutputStream(socket.getOutputStream());
	    }

	    // Accept and update relevant list
	    public synchronized void accept() throws IOException {
	        if (messageData instanceof Vehicle vehicle) {
	            registerVehicle(vehicle);
	        } else if (messageData instanceof Job job) {
	            addJob(job);
	        }
	        outputStream.writeUTF("accepted");
	        outputStream.flush();
	        messageData = null; // clear after processing
	    }

	    // Reject message
	    public synchronized void reject() throws IOException {
	        outputStream.writeUTF("rejected");
	        outputStream.flush();
	        messageData = null; // clear after processing
	    }

	    @Override
	    public void run() {
	        try {
	            while (true) {
	                int length = inputStream.readInt();
	                byte[] data = new byte[length];
	                inputStream.readFully(data);
	                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
	                Object obj = ois.readObject();

	                if (obj instanceof Vehicle || obj instanceof Job) {
	                    messageData = obj;
	                    synchronized (pendingMessages) {
	                        pendingMessages.add(this);
	                    }
	                    queuePendingMessage(this, obj);
	                }
	                // ignore unsupported objects
	            }
	        } catch (IOException | ClassNotFoundException e) {
	            System.out.println("Client disconnected: " + socket);
	        }
	    }

	    public Object getMessageData() {
	        return messageData;
	    }

	    public ClientType getClientType() {
	        return type;
	    }
	}
	
	private enum ClientType{
		VEHICLE_OWNER,
		JOB_OWNER
	}
	
	
	public VehicularCloudController(String id, String status, ResultServer resultServer, VcRequestQueue requestQueue) {
		super(id, status);
		this.resultServer = resultServer;
		this.requestQueue = requestQueue;
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		startServer();
	}
	
	private void startServer() {
	    new Thread(() -> {
	        System.out.println("Server started, waiting for clients...");
	        while (true) {
	            try {
	                Socket clientSocket = serverSocket.accept();
	                ClientType type = determineClientType(clientSocket);
	                ClientHandler handler = new ClientHandler(clientSocket, type);
	                new Thread(handler).start();  // spawn thread per client
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }).start();
	}
	
	private ClientType determineClientType(Socket clientSocket) {
	    try {
	        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
	        String typeStr = in.readUTF();
	        if (typeStr.equalsIgnoreCase("VEHICLE_OWNER")) return ClientType.VEHICLE_OWNER;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return ClientType.JOB_OWNER; // default
	}

	private void queuePendingMessage(ClientHandler handler, Object obj) {
	    if (requestQueue == null) {
	        return;
	    }

	    String requestId = UUID.randomUUID().toString();
	    String type;
	    String summary;
	    String acceptLog;

	    if (obj instanceof Vehicle vehicle) {
	        type = "Vehicle";
	        String ownerName = vehicle.getOwner() != null ? vehicle.getOwner().getOwnerID() : "unknown";
	        summary = vehicle.getId() + " / owner " + ownerName;
	        String vehicleMake = vehicle.getOwner() != null ? vehicle.getOwner().getVehicleMake() : "unknown";
	        String vehicleModel = vehicle.getOwner() != null ? vehicle.getOwner().getVehicleModel() : "unknown";
	        String vehicleYear = vehicle.getOwner() != null ? vehicle.getOwner().getVehicleYear() : "unknown";
	        String residencyTime = vehicle.getOwner() != null ? vehicle.getOwner().getApproxResidencyTime() : "unknown";
	        acceptLog = "OWNER_ACCEPT ownerId=" + ownerName
	                + ", vehicleMake=" + vehicleMake
	                + ", vehicleModel=" + vehicleModel
	                + ", vehicleYear=" + vehicleYear
	                + ", residencyTime=" + residencyTime;
	    } else if (obj instanceof Job job) {
	        type = "Job";
	        summary = job.getJobId();
	        acceptLog = "CLIENT_ACCEPT clientId=" + job.getClientId()
	                + ", clientName=" + job.getClientName()
	                + ", jobDuration=" + job.getDuration()
	                + ", jobDeadline=" + job.getRequestedDeadline();
	    } else {
	        return;
	    }

	    PendingRequest request = new PendingRequest(
	            requestId,
	            type,
	            summary,
	            acceptLog,
	            obj instanceof Job job ? job : null,
	            () -> {
	                try {
	                    handler.accept();
	                } catch (IOException e) {
	                    throw new IllegalStateException("Could not accept pending request " + requestId, e);
	                }
	            },
	            () -> {
	                try {
	                    handler.reject();
	                } catch (IOException e) {
	                    throw new IllegalStateException("Could not reject pending request " + requestId, e);
	                }
	            }
	    );

	    requestQueue.add(request);
	}
	
	public synchronized ClientHandler getNextPending() {
	    if (!pendingMessages.isEmpty()) {
	        return pendingMessages.removeFirst();
	    }
	    return null;
	}
	
	public synchronized boolean registerVehicle(Vehicle vehicle) {
	    if (vehicles.size() + 1 > VEHICLE_LIMIT) return false;
	    vehicles.add(vehicle);
	    return true;
	}
	
	public synchronized void removeVehicle(Vehicle vehicle) {
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

	public synchronized void addJob(Job job) {
	    if (job != null) activeJobs.add(job);
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
