import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Owner {
    private String ownerID;
    private String vehicleInfo;
    private String approxResidencyTime;
    //Networking Stuff
    private static final String HOST = "localhost";
    private static final int PORT = 9806;
   
    public Owner() {
    }


    public Owner(String ownerID, String vehicleInfo, String approxResidencyTime) {
        this.ownerID = ownerID;
        this.vehicleInfo = vehicleInfo;
        this.approxResidencyTime = approxResidencyTime;
    }
    
    public void sendVehicle(Vehicle vehicle) {
        try (Socket socket = new Socket(HOST, PORT)) {

            // Create output stream to send data to server
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Step 1: Identify this client as a VEHICLE_OWNER
            dos.writeUTF("VEHICLE_OWNER");
            dos.flush();

            // Step 2: Serialize the Vehicle object into a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(vehicle);   // convert object -> bytes
            oos.flush();

            // Step 3: Send the byte array length + actual data
            byte[] data = bos.toByteArray();
            dos.writeInt(data.length);  // send size first (server expects this)
            dos.write(data);            // send actual object bytes
            dos.flush();

            // Step 4: Listen for server responses
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            String ack = dis.readUTF();        // "received"
            System.out.println("Server ACK: " + ack);

            String decision = dis.readUTF();   // "accepted" or "rejected"
            System.out.println("Server Decision: " + decision);

        } catch (IOException e) {
            e.printStackTrace(); // handle connection errors
        }
    }

    public String getOwnerID() {
        return ownerID;
    }

    // Compatibility for classes expecting a generic "name" getter.
    public String getName() {
        return ownerID;
    }


    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }


    public String getVehicleInfo() {
        return vehicleInfo;
    }


    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    
    public String getApproxResidencyTime() {
        return approxResidencyTime;
    }

 
    public void setApproxResidencyTime(String approxResidencyTime) {
        this.approxResidencyTime = approxResidencyTime;
    }

    
    @Override
    public String toString() {
        return "Owner{" +
                "ownerID='" + ownerID + '\'' +
                ", vehicleInfo='" + vehicleInfo + '\'' +
                ", approxResidencyTime='" + approxResidencyTime + '\'' +
                '}';
    }
}