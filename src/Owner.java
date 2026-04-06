import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Owner implements Serializable {
    private String ownerID;
    private String vehicleInfo;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleYear;
    private String approxResidencyTime;

    private static final String HOST = VehicularCloudController.HOST;
    private static final int PORT = VehicularCloudController.PORT;
    private transient Socket socket;
    private transient DataInputStream inputStream;
    private transient DataOutputStream outputStream;

    public Owner() {
        connectToController("VEHICLE_OWNER");
    }

    public Owner(String ownerID, String vehicleInfo, String approxResidencyTime) {
        this.ownerID = ownerID;
        this.vehicleInfo = vehicleInfo;
        this.approxResidencyTime = approxResidencyTime;
        connectToController("VEHICLE_OWNER");
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
            throw new IllegalStateException("Could not connect owner to VC controller.", e);
        }
    }
    
    public synchronized String sendVehicle(Vehicle vehicle) {
        ensureConnected();

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(vehicle);
            oos.flush();

            byte[] data = bos.toByteArray();
            outputStream.writeInt(data.length);
            outputStream.write(data);
            outputStream.flush();

            String decision = inputStream.readUTF();
            System.out.println("Server Decision: " + decision);
            return decision;

        } catch (IOException e) {
            throw new IllegalStateException("Could not send vehicle to VC controller.", e);
        }
    }

    private void ensureConnected() {
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            throw new IllegalStateException("Owner is not connected to the VC controller.");
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

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
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
                ", vehicleMake='" + vehicleMake + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", vehicleYear='" + vehicleYear + '\'' +
                ", approxResidencyTime='" + approxResidencyTime + '\'' +
                '}';
    }
}
