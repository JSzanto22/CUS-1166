import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Serializable {
    private String clientID;
    private String clientName;
    private String approxJobDuration;
    private String jobDeadline;

    private static final String HOST = VehicularCloudController.HOST;
    private static final int PORT = VehicularCloudController.PORT;
    private transient Socket socket;
    private transient DataInputStream inputStream;
    private transient DataOutputStream outputStream;

    public Client() {
        connectToController("JOB_OWNER");
    }

    public Client(String clientID, String approxJobDuration, String jobDeadline) {
        this.clientID = clientID;
        this.approxJobDuration = approxJobDuration;
        this.jobDeadline = jobDeadline;
        connectToController("JOB_OWNER");
    }

    public Client(String clientID, String clientName, String approxJobDuration, String jobDeadline) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.approxJobDuration = approxJobDuration;
        this.jobDeadline = jobDeadline;
        connectToController("JOB_OWNER");
    }

    private void connectToController(String clientType) {
        try {
            socket = new Socket(HOST, PORT);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream.writeUTF(clientType);
            outputStream.flush();
        } catch (IOException e) {
            shutdownConnection();
            throw new IllegalStateException("Could not connect client to VC controller.", e);
        }
    }

    public synchronized String sendJob(Job job) {
        ensureConnected();

    	try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(job);
            oos.flush();

            byte[] data = bos.toByteArray();
            outputStream.writeInt(data.length);
            outputStream.write(data);
            outputStream.flush();

            String decision = inputStream.readUTF();
            System.out.println("Server Decision: " + decision);
            return decision;
    	} catch (IOException e) {
            throw new IllegalStateException("Could not send job to VC controller.", e);
    	}
    }

    private void ensureConnected() {
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            throw new IllegalStateException("Client is not connected to the VC controller.");
        }
    }

    public synchronized void shutdownConnection() {
        closeConnectionResource(inputStream);
        closeConnectionResource(outputStream);
        closeConnectionResource(socket);
        inputStream = null;
        outputStream = null;
        socket = null;
    }

    private void closeConnectionResource(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientID() {
        return clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

 
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

   
    public String getApproxJobDuration() {
        return approxJobDuration;
    }


    public void setApproxJobDuration(String approxJobDuration) {
        this.approxJobDuration = approxJobDuration;
    }

 
    public String getJobDeadline() {
        return jobDeadline;
    }


    public void setJobDeadline(String jobDeadline) {
        this.jobDeadline = jobDeadline;
    }


    @Override
    public String toString() {
        return "Client{" +
                "clientID='" + clientID + '\'' +
                ", clientName='" + clientName + '\'' +
                ", approxJobDuration='" + approxJobDuration + '\'' +
                ", jobDeadline='" + jobDeadline + '\'' +
                '}';
    }
}
