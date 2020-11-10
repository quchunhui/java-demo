package rexel.iotextension.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import rexel.iotextension.ExtensionConst;

public class DeviceMqttCreate {
    public static void main(String[] args) throws Exception {
        // client, user and device details
        final String clientId = ExtensionConst.clientId;
        final String device_name = "QchDevice0409";
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

        // 创建设备（100,myDevice,myType）
        client.publish(ExtensionConst.publish_topic, ("100," + device_name + ",qchtype").getBytes(), 2, false);

        // 设置硬件信息（110,serialNumber,model,revision）
        client.publish(ExtensionConst.publish_topic, "110,serial123456789,MQTT test model,Rev0.1".getBytes(), 2, false);

        // 向云端申明支持的下发操作类型
        client.publish(ExtensionConst.publish_topic, "114,c8y_Restart,c8y_Command,c8y_Configuration,c8y_SoftwareList".getBytes(), 2, false);

        // 设置网关设备要求间隔时间（117,requireIntervalInSeconds）
        client.publish(ExtensionConst.publish_topic, "117,60".getBytes(), 2, false);
    }
}