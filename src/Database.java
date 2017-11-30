/**
 * Created by Pernille on 20/11/2017.
 */

import java.sql.*;
import java.util.ArrayList;


public class Database {

    public static void main(String[] args) throws Exception {

        /// DO NO USE !!!!!!!!!!


//       registerUser("Sebastiwn", "s@mail", "noget");
//        registerDevice("1234567891", "98.68987,65.0986", "kitchen");
//        logState(1, "-", 0, "123");
//        updateCurrentState(0, "command", 0, "1234567891");
//        registerDeviceToUser("1234567891", 1);
//        editDevice("129", "986", "k");
//        getCurrentState("123");
//        getLoggedStates("123");


/*        logFireState("1", "1234567891");
        logFireState("0", "1234567891");
        logSirenCommand("cmd", "1234567891");
        logSirenCommand("command", "1234567891");
        logSirenState("1", "1234567891");
        logSirenState("-1", "1234567891");*/
    }

    public static void writeToDb(String dbCommand) {

        try {
            Connection connection = getConnection();

            PreparedStatement statement = connection.prepareStatement(dbCommand);

            statement.executeUpdate();

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet retrieveFromDb(String queryString) {

        try {

            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(queryString);
            /*while (resultSet.next()) {
                int f = resultSet.getInt("fire_state");
                String cmd = resultSet.getString("sirene_cmd");
                int s = resultSet.getInt("sirene_state");
                String id = resultSet.getString("Devices_devices_id");

                // TODO: Remove before launch
                //              System.out.println(id + " " + f + " " + cmd + " " + s);
            }*/

            connection.close();

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

        updateCurrentState(0, "0", 0, deviceID);
        logState(0, "0", 0, deviceID);
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


    public static void logFireState(String value, String deviceID) {

        // Current state

        System.out.println("logging fire state for deviceID : " + deviceID + " With value : " + value);

        String query = "UPDATE current_state SET fire_state ='" + value + "' WHERE Devices_devices_id ='" + deviceID + "'";

        writeToDb(query);

        // Logged state
        String query2 = "INSERT INTO logged_state (timestamp, fire_state, Devices_devices_id) " +
                "VALUES (CURRENT_TIMESTAMP(6),'" + value + "','" + deviceID + "')";

        writeToDb(query2);
    }

    public static void logSirenCommand(String value, String deviceID) {

        // Current state
        String query = "UPDATE current_state SET sirene_cmd ='" + value + "' WHERE Devices_devices_id ='" + deviceID + "'";

        writeToDb(query);

        // Logged state
        String query2 = "INSERT INTO logged_state (timestamp, sirene_cmd, Devices_devices_id) " +
                "VALUES (CURRENT_TIMESTAMP(6),'" + value + "','" + deviceID + "')";

        writeToDb(query2);
    }


    public static void logSirenState(String value, String deviceID) {

        // Current state
        String query = "UPDATE current_state SET sirene_state ='" + value + "' WHERE Devices_devices_id ='" + deviceID + "'";

        writeToDb(query);

        // Logged state
        String query2 = "INSERT INTO logged_state (timestamp, sirene_state, Devices_devices_id) " +
                "VALUES (CURRENT_TIMESTAMP(6),'" + value + "','" + deviceID + "')";

        writeToDb(query2);
    }


    public static String getCurrentState(String deviceID) {

        String result = "";

        String query = "SELECT * FROM current_state " +
                "WHERE Devices_devices_id = '" + deviceID + "'";

        ResultSet rs = retrieveFromDb(query);

        try {
            while (rs.next()) {
                int f = rs.getInt("fire_state");
                String cmd = rs.getString("sirene_cmd");
                int s = rs.getInt("sirene_state");
                String id = rs.getString("Devices_devices_id");

                String state = f + "," + cmd + "," + s + "," + id;

                result = state;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<String> getLoggedStates(String deviceId) {
        ArrayList<String> resultList = new ArrayList<String>();

        String query = "SELECT * FROM logged_state " +
                "WHERE Devices_devices_id = '" + deviceId + "'";

        ResultSet rs = retrieveFromDb(query);

        try {
            while (rs.next()) {
                int f = rs.getInt("fire_state");
                String cmd = rs.getString("sirene_cmd");
                int s = rs.getInt("sirene_state");
                String id = rs.getString("Devices_devices_id");

                String state = f + "," + cmd + "," + s + "," + id;

                resultList.add(state);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
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
