package rexel.iotextension.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import rexel.iotextension.ExtensionConst;

public class DeviceMqttSubscribe {
    public static void main(String[] args) throws Exception {
        final String externalId = ExtensionConst.clientId;
        final String serverUrl = ExtensionConst.mqttUrl;
        final String tenant = ExtensionConst.tenant;
        final String username = ExtensionConst.username;
        final String password = ExtensionConst.password;

        // MQTT connection options
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(tenant + "/" + username);
        options.setPassword(password.toCharArray());

        // connect the client to Cumulocity IoT
        final MqttClient client = new MqttClient(serverUrl, externalId, null);
        client.connect(options);

        client.subscribe(ExtensionConst.subscribe_topic, (topic, message) -> {
            final String payload = new String(message.getPayload());
            System.out.println("received. topic=" + topic + ", payload=" + payload);
        });
    }
}