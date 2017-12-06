/**
 * Created by Pernille on 20/11/2017.
 */


import org.eclipse.paho.client.mqttv3.*;

public class MQTTHandler implements MqttCallback {


    MqttClient client;
    Database database;

    public MQTTHandler() {
    }


    public MqttClient start() {
        try {
            database = new Database();

            System.out.println("MQTTHandler started");
            client = new MqttClient("tcp://127.0.0.1:1883", "Sending");
            client.connect();
            client.setCallback(this);

            MqttMessage message = new MqttMessage();
            message.setPayload("Server MQTT".getBytes());
            client.publish("server", message);
            client.subscribe("#");

        } catch (MqttException e) {
            e.printStackTrace();
        }
        return client;
    }

    public void parseMessage(String topic, MqttMessage mqttMessage) {

        //System.out.println("Parsing message: " + topic + " " + mqttMessage);
        // Extract info
        String deviceId = topic.substring(0, 10);
        String messageType = topic.substring(11);
        String payload = mqttMessage.toString();

        // Debug content
        //System.out.println(deviceId + " : " + messageType + " : " + payload);

        // Decide what to do with content
        switch (messageType) {
            case "fire":
                database.logFireState(payload, deviceId);
                break;
            case "siren_cmd":
                database.logSirenCommand(payload, deviceId);
                break;
            case "siren_state":
                database.logSirenState(payload, deviceId);
                break;
            default:
                System.out.print("this message didn't fit possible outcomes: " + topic + " - " + mqttMessage.toString());
                // TODO: handle default
        }


    }

    public void publish(String topic, String message){

        MqttMessage msg = new MqttMessage();
        msg.setPayload(message.getBytes());
        try {
            client.publish("server", msg);
            client.subscribe("#");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println("recieved message:" + topic + " " + message);
        parseMessage(topic, message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }
}