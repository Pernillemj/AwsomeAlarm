import org.eclipse.paho.client.mqttv3.MqttClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Pernille on 20/11/2017.
 */

import org.eclipse.paho.client.mqttv3.*;


public class Server {

    public static MqttClient mqttClient;

    public static void main(String[] args) throws IOException {

        mqttClient = new MQTTHandler().start();

        System.out.println("Threaded Server is Running and listening on port 9999....");
        ServerSocket mysocket = new ServerSocket(9999);

        Database database = new Database();

        while (true) {
            Socket sock = mysocket.accept();
            AppThreadHandler server = new AppThreadHandler(sock, database);

            Thread serverThread = new Thread(server);
            serverThread.start();

        }
    }

/*
    public static void publishToMqtt(String topic, String payload){
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            publishingClient.publish(topic, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    */

}



