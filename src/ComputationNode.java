
import java.io.Serializable;

public abstract class ComputationNode implements Serializable {
	String id;
	String status;
	
	public String getId() {
		return id;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public ComputationNode(String id, String status) {
		this.id = id;
		this.status = status;
	}
}
