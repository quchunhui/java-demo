package demo.patomqtt;

import demo.bean.PropertiesBean;
import demo.utils.PropertiesUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PatoMqttConnect {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main( String[] args ) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        String productKey = propertiesBean.getProductKey();
        String deviceName = propertiesBean.getDeviceName();
        String deviceSecret = propertiesBean.getDeviceSecret();

        //计算Mqtt建连参数
        PatoMqttSign sign = new PatoMqttSign();
        sign.calculate(productKey, deviceName, deviceSecret);

        System.out.println("username: " + sign.getUsername());
        System.out.println("password: " + sign.getPassword());
        System.out.println("clientid: " + sign.getClientid());

        ///使用Paho连接阿里云物联网平台
        String port = "443";
        String broker = "ssl://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com" + ":" + port;
        try{
            //Paho Mqtt 客户端
            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient sampleClient = new MqttClient(broker, sign.getClientid(), persistence);

            //Paho Mqtt 连接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            // 设置超时时间
            connOpts.setConnectionTimeout(MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT);
            // 设置会话心跳时间
            connOpts.setKeepAliveInterval(MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT);
            connOpts.setUserName(sign.getUsername());
            connOpts.setPassword(sign.getPassword().toCharArray());

            //Mqtt连接
            sampleClient.connect(connOpts);
            System.out.println("Broker: " + broker + " Connected");

//            //Mqtt断开连接
//            sampleClient.disconnect();
//            System.out.println("Broker: " + broker + " Disonnected");
        }catch (MqttException e) {
            e.printStackTrace();
        }
    }
}