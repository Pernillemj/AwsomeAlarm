/**
 * Created by Pernille on 20/11/2017.
 */

import java.sql.*;



public class Database {

    public static void main(String[] args) throws Exception {

//       registerUser("Sebastiwn", "s@mail", "noget");
//        registerDevice("128", "98.68987,65.0986", "kitchen");
//        logState(1, "-", 0, "123");
//        updateCurrentState(1, "-", 0, "999");
//        registerDeviceToUser("123", 2);
//        editDevice("129", "986", "k");
//        getCurrentState("123");
//        getLoggedStates("123");

    }

    public static void writeToDb(String dbCommand) {

        try {
            Connection connection = getConnection();

            PreparedStatement statement = connection.prepareStatement(dbCommand);

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet retrieveFromDb(String queryString) {

        try {

            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                int f = resultSet.getInt("fire_state");
                String cmd = resultSet.getString("sirene_cmd");
                int s = resultSet.getInt("sirene_state");
                String id = resultSet.getString("Devices_devices_id");

                System.out.println(id + " " + f + " " + cmd + " " + s);
            }
            return resultSet;

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public static void registerUser(String name, String email, String password) {

        String query = "INSERT INTO Users (name, email, password)\n" +
                "VALUES ('" + name + "', '" + email + "', '" + password + "')";

        writeToDb(query);
    }

    public static void registerDevice(String deviceID, String location, String deviceName) {

        String query = "INSERT INTO devices (devices_id, coordinate, device_name)\n" +
                "VALUES ('" + deviceID + "', '" + location + "' , '" + deviceName + "')";

        writeToDb(query);
    }

    public static void deleteDevice(String deviceID) {

        String query = "DELETE FROM devices WHERE devices_id = '" + deviceID + "' ";

        writeToDb(query);

    }

    public static void editDevice(String deviceID, String location, String deviceName) {

        deleteDevice(deviceID);
        registerDevice(deviceID, location, deviceName);
    }

    public static void registerDeviceToUser(String deviceID, int userID) {

        String query = "INSERT INTO Devices_has_users\n" +
                "VALUES ('" + deviceID + "', '" + userID + "')";

        writeToDb(query);
    }

    public static void logState(int fireState, String sirenCommand, int sirenState, String deviceID) {

        String query = "INSERT INTO logged_state " +
                "VALUES (CURRENT_TIMESTAMP(6),'" + fireState + "','" + sirenCommand + "','" + sirenState + "','" + deviceID + "')";

        writeToDb(query);

    }

    public static void updateCurrentState(int fireState, String sirenCommand, int sirenState, String deviceID) {


            String query = "REPLACE INTO current_state " +
                    "VALUES (CURRENT_TIMESTAMP(6),'" + fireState + "','" + sirenCommand + "','" + sirenState + "','" + deviceID + "')";

            writeToDb(query);

    }

    public static String getCurrentState (String deviceID) {

        String result = "";

        String query = "SELECT * FROM current_state " +
                "WHERE Devices_devices_id = '" + deviceID + "'";

        retrieveFromDb(query);

        return result;
    }

    public static String getLoggedStates (String deviceId) {
        String result = "";

        String query = "SELECT * FROM logged_state " +
                "WHERE Devices_devices_id = '" + deviceId + "'";
        retrieveFromDb(query);

        return result;
    }


    public static Connection getConnection() throws Exception {
        try {
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost/p3_database";
            String username = "AwsomeAlarm";
            String password = "password";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to DB");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
