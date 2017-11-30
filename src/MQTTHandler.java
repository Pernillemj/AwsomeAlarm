/**
 * Created by Pernille on 20/11/2017.
 */


import org.eclipse.paho.client.mqttv3.*;

public class MQTTHandler implements MqttCallback {


    MqttClient client;

    public MQTTHandler() {
    }

//    public static void main(String[] args) {
//        new MQTTHandler().start();
//    }

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

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println(message);

        Database.updateCurrentState(0, message.toString(),0,"123");
        Database.logState(0, message.toString(),0,"123");

//        Server.logState(topic.length(),message.toString(), message.getId(),"666");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}