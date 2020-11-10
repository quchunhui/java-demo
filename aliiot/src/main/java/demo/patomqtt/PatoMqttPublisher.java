package demo.patomqtt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import demo.bean.PropertiesBean;
import demo.utils.PropertiesUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.*;

public class PatoMqttPublisher {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main( String[] args ) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        String productKey = propertiesBean.getProductKey();
        String deviceName = propertiesBean.getDeviceName();
        String deviceSecret = propertiesBean.getDeviceSecret();

        //计算Mqtt建联参数
        PatoMqttSign sign = new PatoMqttSign();
        sign.calculate(productKey, deviceName, deviceSecret);

        System.out.println("username: " + sign.getUsername());
        System.out.println("password: " + sign.getPassword());
        System.out.println("clientid: " + sign.getClientid());

        //使用Paho连接阿里云物联网平台
        String port = "443";
        String broker = "ssl://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com" + ":" + port;
        MemoryPersistence persistence = new MemoryPersistence();
        try{
            //Paho Mqtt 客户端
            MqttClient sampleClient = new MqttClient(broker, sign.getClientid(), persistence);

            //Paho Mqtt 连接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setConnectionTimeout(MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT);
            connOpts.setKeepAliveInterval(MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT);
            connOpts.setUserName(sign.getUsername());
            connOpts.setPassword(sign.getPassword().toCharArray());
            sampleClient.connect(connOpts);
            System.out.println("broker: " + broker + " Connected");

//            //Paho Mqtt 消息订阅
//            String topicReply = "/sys/" + productKey + "/" + deviceName + "/thing/event/property/post_reply";
//            sampleClient.subscribe(topicReply, new MqttPostPropertyMessageListener());
//            System.out.println("subscribe: " + topicReply);
//
//            //Paho Mqtt 消息发布
//            String topic = "/sys/" + productKey + "/" + deviceName + "/thing/event/property/post";
//            String content = "{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"testVar1\":1.11, \"testVar2\":2.22}}";
//            MqttMessage message = new MqttMessage(content.getBytes());
//            message.setQos(0);
//            sampleClient.publish(topic, message);
//            System.out.println("publish: " + content);
            /*
             * https://www.yuque.com/cloud-dev/iot-tech/czle07
             * IoT物联网平台设备标签最佳实践
             */
            //Paho Mqtt 消息订阅
            String topicReply = "/sys/" + productKey + "/" + deviceName + "/thing/deviceinfo/update_reply";
            sampleClient.subscribe(topicReply, new MqttPostPropertyMessageListener());
            System.out.println("subscribe: " + topicReply);

            //Paho Mqtt 消息发布
            //Topic
            String topic = "/sys/" + productKey + "/" + deviceName + "/thing/deviceinfo/update";
            //数据Payload
//            {
//                "id": 1570605202,
//                "version": "1.0",
//                "params": [
//                {
//                    "attrKey": "coordinate",//标签(坐标key为coordinate，其他标签可自定义key)
//                    "attrValue": "120.14915:30.230687"//标签值
//                },
//                {
//                    "attrKey": "city",//标签
//                    "attrValue": "杭州"//标签值
//                }
//                ],
//                "method": "thing.deviceinfo.update"
//            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", 1570605202);
            jsonObject.put("version", "1.0");
            JSONArray jsonArray = new JSONArray();
            JSONObject param1 = new JSONObject();
            param1.put("attrKey", "coordinate");//标签(坐标key为coordinate，其他标签可自定义key)
            param1.put("attrValue", "122.14915:30.230687");//标签值
            jsonArray.add(param1);
            JSONObject param2 = new JSONObject();
            param2.put("attrKey", "city");//标签(坐标key为coordinate，其他标签可自定义key)
            param2.put("attrValue", "杭州");//标签值
            jsonArray.add(param2);
            JSONObject param3 = new JSONObject();
            param3.put("attrKey", "prov");//标签(坐标key为coordinate，其他标签可自定义key)
            param3.put("attrValue", "浙江");//标签值
            jsonArray.add(param3);
            jsonObject.put("params", jsonArray);
            jsonObject.put("method", "thing.deviceinfo.update");

            String content = jsonObject.toJSONString();
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(0);
            sampleClient.publish(topic, message);
            System.out.println("publish: " + content);

//            try {
//                Thread.sleep(1000 * 60 * 3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //Paho Mqtt 断开连接
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException e) {
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
        }
    }

    static class MqttPostPropertyMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String var1, MqttMessage var2) {
            System.out.println("reply topic  : " + var1);
            System.out.println("reply payload: " + var2.toString());
        }
    }
}