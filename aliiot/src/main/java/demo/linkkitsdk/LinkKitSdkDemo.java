package demo.linkkitsdk;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;
import demo.bean.PropertiesBean;
import demo.utils.PropertiesUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LinkKitSdkDemo {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main( String[] args ) throws InterruptedException {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        String productKey = propertiesBean.getProductKey();
        String deviceName = propertiesBean.getDeviceName();
        String deviceSecret = propertiesBean.getDeviceSecret();

        connect(productKey, deviceName, deviceSecret);
//        publish();
//        subscribe("/sys/"+ productKey + "/" + deviceName + "/thing/event/property/post_reply");
//        disconnect();
    }

    private static void connect(
        String productKey, String deviceName, String deviceSecret) throws InterruptedException {
        // 初始化参数
        LinkKitInitParams params = new LinkKitInitParams();

        // 设置 Mqtt 初始化参数
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = productKey;
        config.deviceName = deviceName;
        config.deviceSecret = deviceSecret;
        params.mqttClientConfig = config;

        // 设置初始化设备证书信息，传入：
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = productKey;
        deviceInfo.deviceName = deviceName;
        deviceInfo.deviceSecret = deviceSecret;
        params.deviceInfo = deviceInfo;

        // 初始化
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            public void onError(AError aError) {
                System.out.println("init failed !! code=" + aError.getCode() + ",msg=" + aError.getMsg() + ",subCode="
                    + aError.getSubCode() + ",subMsg=" + aError.getSubMsg());
            }

            public void onInitDone(InitResult initResult) {
                System.out.println("init success !!");
                publish("/a1B6t6ZG6oR/QCHTestDevice1/user/devmsg", "test111");
//                publish();
            }
        });
    }

    /**
     *  Alink JSON 方式设备端上报属性
     */
    private static void publish() {
        Map<String, ValueWrapper> reportData = new HashMap<>();
        Random random = new Random();
        double var1 = (double)random.nextInt(1000);
        double var2 = (double)random.nextInt(1000);
        double var3 = (double)random.nextInt(1000);
        System.out.println("var1=" + var1);
        System.out.println("var2=" + var2);
        System.out.println("var3=" + var3);
        reportData.put("Var1", new ValueWrapper.DoubleValueWrapper(var1));
        reportData.put("Var2", new ValueWrapper.DoubleValueWrapper(var2));
        reportData.put("Var3", new ValueWrapper.DoubleValueWrapper(var2));

        LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
            /*
             * 属性上报成功
             */
            public void onSuccess(String s, Object o) {
                System.out.println("上报成功 onSuccess() called with: s = [" + s + "], o = [" + o + "]");
            }

            /*
             * 属性上报失败
             */
            public void onError(String s, AError aError) {
                System.out.println("上报失败onError() called with: s = [" + s + "], aError = [" + JSON.toJSONString(aError) + "]");
            }
        });
    }

        /**
         * 发布消息
         *
         * @param topic 发送消息的topic
         * @param payload 发送的消息内容
         */
    private static void publish(String topic, String payload) {
        MqttPublishRequest request = new MqttPublishRequest();
        request.topic = topic;
        request.payloadObj = payload;
        request.qos = 0;
        LinkKit.getInstance().getMqttClient().publish(request, new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                System.out.println("onResponse");
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                System.out.println("onFailure");
            }
        });
    }

    /**
     * 订阅消息
     *
     * @param topic 订阅消息的topic
     */
    private static void subscribe(String topic) {
        MqttSubscribeRequest request = new MqttSubscribeRequest();
        request.topic = topic;
        request.isSubscribe = true;
        //发出订阅请求并设置订阅成功或者失败的回调函数
        LinkKit.getInstance().subscribe(request, new IConnectSubscribeListener() {
            @Override
            public void onSuccess() {
                System.out.println("subscribe onSuccess");
            }

            @Override
            public void onFailure(AError aError) {
                System.out.println("subscribe onFailure. aError=" + aError.getMsg());
            }
        });

        //设置订阅的下行消息到来时的回调函数
        IConnectNotifyListener notifyListener = new IConnectNotifyListener() {
            //此处定义收到下行消息以后的回调函数。
            @Override
            public void onNotify(String connectId, String topic, AMessage aMessage) {
                System.out.println(
                    "received message from " + topic + ":" + new String((byte[])aMessage.getData()));
            }

            @Override
            public boolean shouldHandle(String s, String s1) {
                System.out.println("notifyListener shouldHandle");
                return false;
            }

            @Override
            public void onConnectStateChange(String s, ConnectState connectState) {
                System.out.println("notifyListener onConnectStateChange");
            }
        };
        LinkKit.getInstance().registerOnNotifyListener(notifyListener);
    }

    /**
     * 取消订阅
     *
     * @param topic 取消订阅消息的topic
     */
    private static void unsubscribe(String topic) {
        MqttSubscribeRequest request = new MqttSubscribeRequest();
        request.topic = topic;
        request.isSubscribe = false;
        LinkKit.getInstance().getMqttClient().unsubscribe(request, new IConnectUnscribeListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(AError aError) {
            }
        });
    }

    /**
     * 断开连接
     */
    private static void disconnect() {
        // 反初始化
        LinkKit.getInstance().deinit();
        System.out.println("disconnect");
    }
}
