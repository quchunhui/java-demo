package rexel.iotextension.mqtt;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rexel.iotextension.ExtensionConst;

public class DeviceMqttPublish {
    public static void main(String[] args) throws Exception {
        final String clientId = ExtensionConst.clientId;
        final String serverUrl = ExtensionConst.mqttUrl;
        final String tenant = ExtensionConst.tenant;
        final String username = ExtensionConst.username;
        final String password = ExtensionConst.password;

        // MQTT connection options
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(tenant + "/" + username);
        options.setPassword(password.toCharArray());

        // connect the client to Cumulocity IoT
        final MqttClient client = new MqttClient(serverUrl, clientId, null);
        client.connect(options);

        // generate a random temperature (10º-20º) measurement and send it every 7 seconds
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            try {
                int temp = (int) (Math.random() * 10 + 10);
                System.out.println("Sending temperature measurement (" + temp + "º) ...");
                client.publish(ExtensionConst.publish_topic, new MqttMessage(("211," + temp).getBytes()));

                String msg1 = ""
                    + "200,QchMeasurement1,AR_TEST_VAR1," + (Math.random() * 10 + 10) + "\n"
                    + "200,QchMeasurement1,AR_TEST_VAR2," + (Math.random() * 10 + 10) + "\n"
                    + "200,QchMeasurement1,AR_TEST_VAR2," + (Math.random() * 10 + 10) + "\n";
                client.publish("s/us", new MqttMessage((msg1).getBytes()));

                String msg2 = "200,QchMeasurement2,VT_TEST_VAR4," + "中文字符" + "\n";
                client.publish("s/us", new MqttMessage((msg2).getBytes()));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }, 1, 10, TimeUnit.SECONDS);
    }
}