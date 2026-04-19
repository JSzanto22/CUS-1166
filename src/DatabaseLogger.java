import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DatabaseLogger implements Logger {
    private static final ZoneId EASTERN_TIME = ZoneId.of("America/New_York");
    private static final String URL = "jdbc:mysql://localhost:3306/cus1166";
    private static final String USER = "yourusername";
    private static final String PASSWORD = "yourpassword";

    public static void rebuildSchema() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
                statement.executeUpdate("DROP TABLE IF EXISTS vehicles");
                statement.executeUpdate("DROP TABLE IF EXISTS jobs");
                statement.executeUpdate(
                        "CREATE TABLE vehicles ("
                                + "vehicle_id VARCHAR(50) PRIMARY KEY, "
                                + "logged_at VARCHAR(80) NOT NULL, "
                                + "owner_id VARCHAR(50) NOT NULL, "
                                + "vehicle_make VARCHAR(50) NOT NULL, "
                                + "vehicle_model VARCHAR(50) NOT NULL, "
                                + "vehicle_year VARCHAR(10) NOT NULL, "
                                + "residency_time VARCHAR(20) NOT NULL)"
                );
                statement.executeUpdate(
                        "CREATE TABLE jobs ("
                                + "job_id VARCHAR(50) PRIMARY KEY, "
                                + "logged_at VARCHAR(80) NOT NULL, "
                                + "client_id VARCHAR(50) NOT NULL, "
                                + "client_name VARCHAR(100) NOT NULL, "
                                + "job_duration INT NOT NULL, "
                                + "job_deadline VARCHAR(50) NOT NULL)"
                );
        } catch (SQLException e) {
            throw new IllegalStateException("Could not rebuild database schema.", e);
        }
    }

    public void info(String info) {
        System.out.println("INFO " + time() + " " + info);
    }

    @Override
    public void info(Vehicle vehicle) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(extractSQL(vehicle));
        } catch (SQLException e) {
            throw new IllegalStateException("Could not execute SQL statement.", e);
        }
    }

    @Override
    public void info(Job job) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(extractSQL(job));
        } catch (SQLException e) {
            throw new IllegalStateException("Could not execute SQL statement.", e);
        }
    }

    @Override
    public void warning(String warning) {
        System.out.println("WARNING " + time() + " " + warning);
    }

    @Override
    public void error(String error) {
        System.err.println("ERROR " + time() + " " + error);
    }

    private static String extractSQL(Vehicle vehicle) {
        Owner owner = vehicle.getOwner();
        String ownerId = "";
        String vehicleMake = "";
        String vehicleModel = "";
        String vehicleYear = "";
        String residencyTime = "";

        if (owner != null) {
            ownerId = owner.getOwnerID();
            vehicleMake = owner.getVehicleMake();
            vehicleModel = owner.getVehicleModel();
            vehicleYear = owner.getVehicleYear();
            residencyTime = owner.getApproxResidencyTime();
        }

        return "INSERT INTO vehicles (vehicle_id, logged_at, owner_id, vehicle_make, vehicle_model, vehicle_year, residency_time) "
                + "VALUES ('" + vehicle.getId() + "', '" + time() + "', '" + ownerId + "', '" + vehicleMake + "', '"
                + vehicleModel + "', '" + vehicleYear + "', '" + residencyTime + "')";
    }

    private static String extractSQL(Job job) {
        String clientId = "";
        String clientName = "";
        String jobDeadline = "";

        if (job != null) {
            clientId = job.getClientId();
            clientName = job.getClientName();
            jobDeadline = job.getRequestedDeadline();
        }

        return "INSERT INTO jobs (job_id, logged_at, client_id, client_name, job_duration, job_deadline) "
                + "VALUES ('" + job.getJobId() + "', '" + time() + "', '" + clientId + "', '"
                + clientName + "', " + job.getDuration() + ", '" + jobDeadline + "')";
    }

    private static String time() {
        return ZonedDateTime.now(EASTERN_TIME).toString();
    }
}
