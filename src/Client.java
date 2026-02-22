public class Client {
    private String clientID;
    private String approxJobDuration;
    private String jobDeadline;

   
    public Client() {
    }

 
    public Client(String clientID, String approxJobDuration, String jobDeadline) {
        this.clientID = clientID;
        this.approxJobDuration = approxJobDuration;
        this.jobDeadline = jobDeadline;
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