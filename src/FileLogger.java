import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;



public class FileLogger implements Logger{
    private static final ZoneId EASTERN_TIME = ZoneId.of("America/New_York");

	private FileWriter fileWriter;
	
	public FileLogger(String filePath) {
		try {
			fileWriter = new FileWriter(filePath, true);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
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
		return ZonedDateTime.now(EASTERN_TIME).toString();
		
	}
	
	/*
	 * 1) Implement system that ensures FileWritter closes when the application gets closed
	 * 2) Implement system that uses the given current timezone instead of UTC
	 * 3) 
	 */

}
