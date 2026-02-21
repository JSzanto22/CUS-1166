import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;


public class FileLogger implements Logger{

	FileWriter fileWriter;
	
	public FileLogger(String filePath) throws IOException {
		fileWriter = new FileWriter(filePath, true);
		
	}
	
	public void info(String info) {
		
		try {
			fileWriter.write("INFO " + time() + " " + info + "\n");
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void warning(String warning){
		try {
			fileWriter.write("WARNING " + time() + " " + warning + "\n");
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void error(String error) {
		try {
			fileWriter.write("ERROR " + time() + " " + error + "\n");
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void close() throws IOException {
		fileWriter.close();
	}
	
	private String time() {
		return Instant.now().toString();
		
	}
	
	/*
	 * 1) Implement system that ensures FileWritter closes when the application gets closed
	 * 2) Implement system that uses the given current timezone instead of UTC
	 * 3) 
	 */

}
