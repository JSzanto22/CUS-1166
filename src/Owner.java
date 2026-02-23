public class Owner {
    private String ownerID;
    private String vehicleInfo;
    private String approxResidencyTime;

   
    public Owner() {
    }


    public Owner(String ownerID, String vehicleInfo, String approxResidencyTime) {
        this.ownerID = ownerID;
        this.vehicleInfo = vehicleInfo;
        this.approxResidencyTime = approxResidencyTime;
    }

    public String getOwnerID() {
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