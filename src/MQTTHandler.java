/**
 * Created by Pernille on 20/11/2017.
 */


import org.eclipse.paho.client.mqttv3.*;

public class MQTTHandler implements MqttCallback {


    MqttClient client;

    public MQTTHandler() {
    }


    public void start() {
        try {
            client = new MqttClient("tcp://127.0.0.1:1883", "Sending");
            client.connect();
            client.setCallback(this);
            client.subscribe("#");
//            MqttMessage message = new MqttMessage();
//            message.setPayload("A single message from my computer fff"
//                    .getBytes());
//            client.publish("test/state", message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void parseMessage(String topic, MqttMessage mqttMessage){

        // Extract info
        String deviceId = topic.substring(0,10);
        String messageType = topic.substring(11);
        String payload = mqttMessage.toString();


        // Debug content
        System.out.println(deviceId + " : "+ messageType + " : " + payload);

        // Decide what to do with content
        switch (messageType) {
            case "fire":
                Database.logFireState(payload,deviceId);
                break;
            case "siren_cmd":
                Database.logSirenCommand(payload,deviceId);
                break;
            case "siren_state":
                Database.logSirenState(payload,deviceId);
                break;
            default:
                System.out.print("this message didn't fit possible outcomes: " + topic + " - "+ mqttMessage.toString());
                // TODO: handle default
        }


    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        parseMessage(topic,message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }
}