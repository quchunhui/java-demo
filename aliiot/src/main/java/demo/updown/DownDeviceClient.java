package demo.updown;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import demo.bean.PropertiesBean;
import demo.utils.PropertiesUtils;

public class DownDeviceClient {
    private static final String TAG = "DownDeviceClient";
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        /*
         * mqtt连接信息
         */
        DownDeviceClient manager = new DownDeviceClient();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = propertiesBean.getProductKey();
        deviceInfo.deviceName = propertiesBean.getDeviceName();
        deviceInfo.deviceSecret = propertiesBean.getDeviceSecret();

        /*
         * 服务器端的java http 客户端使用TSLv1.2。
         */
        System.setProperty("https.protocols", "TLSv2");
        manager.subscribe(deviceInfo, propertiesBean.getRegionId());
    }

    private void subscribe(final DeviceInfo deviceInfo, String region) {
        LinkKitInitParams params = new LinkKitInitParams();
        /*
         * 设置 Mqtt 初始化参数
         */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = deviceInfo.productKey;
        config.deviceName = deviceInfo.deviceName;
        config.deviceSecret = deviceInfo.deviceSecret;
        config.channelHost = deviceInfo.productKey + ".iot-as-patomqtt." + region + ".aliyuncs.com:1883";

        /*
         * 是否接受离线消息
         * 对应 patomqtt 的 cleanSession 字段
         */
        config.receiveOfflineMsg = false;
        params.mqttClientConfig = config;
        ALog.i(TAG, "patomqtt connetcion info=" + params);

        /*
         * 设置初始化，传入设备证书信息
         */
        params.deviceInfo = deviceInfo;

        //连接并设置连接成功以后的回调函数
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            @Override
            public void onError(AError aError) {
                System.out.println("Init error:" + aError);
            }

            //初始化成功以后的回调
            @Override
            public void onInitDone(InitResult initResult) {
                //设置订阅的topic
                MqttSubscribeRequest request = new MqttSubscribeRequest();
                request.topic = "/sys/" + deviceInfo.productKey + "/" + deviceInfo.deviceName + "/thing/event/property/post_reply";
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
        });
    }
}