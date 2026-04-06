import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private String clientID;
    private String approxJobDuration;
    private String jobDeadline;
    //Networking Stuff
    private static final String HOST = "localhost";
    private static final int PORT = 9806;
   
    public Client() {
    }

 
    public Client(String clientID, String approxJobDuration, String jobDeadline) {
        this.clientID = clientID;
        this.approxJobDuration = approxJobDuration;
        this.jobDeadline = jobDeadline;
    }

    public void sendJob(Job job) {
    	try(Socket socket = new Socket(HOST, PORT)) {
    		
    		// Step 1: Identify client type
    		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
    		dos.writeUTF("JOB_OWNER");
    		dos.flush();
    		
    		// Step 2: Send Object
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(job);
            oos.flush();

            byte[] data = bos.toByteArray();
            dos.writeInt(data.length);
            dos.write(data);
            dos.flush();    

            // Step 3: wait for responses
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            String ack = dis.readUTF();        // "received"
            System.out.println("Server ACK: " + ack);

            String decision = dis.readUTF();   // "accepted" or "rejected"
            System.out.println("Server Decision: " + decision);
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
   
    public String getClientID() {
        return clientID;
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
                ", approxJobDuration='" + approxJobDuration + '\'' +
                ", jobDeadline='" + jobDeadline + '\'' +
                '}';
    }
}