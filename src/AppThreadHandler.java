import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Pernille on 04/12/2017.
 */
public class AppThreadHandler implements Runnable {

    Socket connectionSocket;
    Database database;

    public AppThreadHandler(Socket s, Database database) {
        try {
            connectionSocket = s;
            this.database = database;
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            BufferedWriter writer;
            writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));

            // get received string
            String line;
            line = reader.readLine();
            String[] parts = line.split(Pattern.quote(";"));
            String requestType = parts[0];
            switch (requestType) {
                // Test
                case "00":
                    testConnenction(writer, line);
                    break;
                case "01":
                    registersUser(writer, parts);
                    break;
                case "02":
                    logIn(writer, parts);
                    break;
                case "03":
                    registerAlarm(writer, parts);
                    break;
                case "04":
                    getUsersDevices(writer, parts[1]);
                    break;
                case "05":
                    getCurrentState(writer, parts[1]);
                    break;
                case "06":
                    loggedState(writer, parts[1]);
                    break;
                case "07":
                    turnOffAlarm(parts[1]);
                    break;
                case "08":
                    registerDeviceToUser(parts[1],parts[2]);
                    break;
                default:
                    System.out.println("RequestID :" + requestType);
                    writer.write("Request type not recognized");
                    writer.flush();
            }

            connectionSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerDeviceToUser(String deviceID, String userID) {
        database.registerDeviceToUser(deviceID,Integer.valueOf(userID));
    }

    private void turnOffAlarm(String part) {
        String topic = part + "/siren_cmd";
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload("-1".getBytes());
            Server.mqttClient.publish(topic, message);
            Server.mqttClient.subscribe("#");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getUsersDevices(BufferedWriter writer, String part) throws IOException {
        ResultSet resultSet = database.retrieveFromDb("SELECT Devices_devices_id FROM Devices_has_users WHERE users_user_id='" + part + "'");

        StringBuilder sb = new StringBuilder();
        try {
            while (resultSet.next()) {
                sb.append(resultSet.getString("Devices_devices_id"));
                sb.append(";");
            }
        } catch (Exception e) {

        }
        writer.write(sb.toString());

        writer.flush();
    }

    private void logIn(BufferedWriter writer, String[] parts) {
        ResultSet rs = database.retrieveFromDb("SELECT password FROM users WHERE email = '" + parts[1] + "'");

        try {
            rs.next();
            String dbPassword = rs.getString("password");

            if (parts[2].equals(dbPassword)) {
                writer.write("true");
            } else {
                writer.write("false");
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerAlarm(BufferedWriter writer, String[] parts) {

        database.registerDevice(parts[1], parts[2], parts[3]);

        ResultSet rs = database.retrieveFromDb("SELECT devices_id FROM Devices WHERE devices_id = '" + parts[1] + "'");

        try {
            rs.next();
            writer.write(rs.getString("devices_id") + " registered");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        registerDeviceToUser(parts[1],parts[4]);
    }

    private void testConnenction(BufferedWriter writer, String line) throws IOException {
        writer.write("got this: " + line.substring(3));
        writer.flush();
    }

    private void loggedState(BufferedWriter writer, String part) throws IOException {
        System.out.println("06");
        ArrayList<String> loggedStates = database.getLoggedStates(part);

        StringBuilder sb = new StringBuilder();
        for (String s : loggedStates) {
            System.out.println("Building string by adding: " + s);
            sb.append(s);
            sb.append(";");
        }

        System.out.println("Complete string: " + sb.toString());
        writer.write(sb.toString());
        writer.flush();
    }

    private void getCurrentState(BufferedWriter writer, String part) throws IOException {
        writer.write(database.getCurrentState(part));
        writer.flush();
    }

    private void registersUser(BufferedWriter writer, String[] parts) {
        database.registerUser(parts[1], parts[2], parts[3]);

        ResultSet rs = database.retrieveFromDb("SELECT user_id FROM users WHERE email = '" + parts[2] + "'");
        try {
            rs.next();
            String userId = String.valueOf(rs.getInt("user_id"));
            writer.write(userId);
            writer.flush();
            System.out.println("Getting user id" + rs.getInt("user_id") + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
