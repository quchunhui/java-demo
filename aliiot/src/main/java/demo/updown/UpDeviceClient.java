package demo.updown;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Event;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

import demo.bean.PropertiesBean;
import demo.utils.PropertiesUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *  设备端代码开发
 */
public class UpDeviceClient {
    private static final String TAG = "UpDeviceClient";
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        /*
         * mqtt连接信息
         */
        UpDeviceClient manager = new UpDeviceClient();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = propertiesBean.getProductKey();
        deviceInfo.deviceName = propertiesBean.getDeviceName();
        deviceInfo.deviceSecret = propertiesBean.getDeviceSecret();

        /*
         * 服务器端的java http 客户端使用TSLv1.2。
         */
        System.setProperty("https.protocols", "TLSv2");
        manager.init(deviceInfo, propertiesBean.getRegionId());
    }

    private void init(final DeviceInfo deviceInfo, String region) {
        LinkKitInitParams params = new LinkKitInitParams();
        /*
         * 设置 Mqtt 初始化参数
         */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = deviceInfo.productKey;
        config.deviceName = deviceInfo.deviceName;
        config.deviceSecret = deviceInfo.deviceSecret;
//        config.channelHost = deviceInfo.productKey + ".iot-as-patomqtt." + region + ".aliyuncs.com:1883";

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

        /*
         * 建立链接
         */
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            public void onError(AError aError) {
                ALog.e(TAG, "Init Error error=" + aError);
            }

            public void onInitDone(InitResult initResult) {
                ALog.i(TAG, "onInitDone result=" + initResult);

                List<Property> properties =   LinkKit.getInstance().getDeviceThing().getProperties();
                ALog.i(TAG, "设备属性列表" + JSON.toJSONString(properties));

                List<Event> getEvents  =   LinkKit.getInstance().getDeviceThing().getEvents();
                ALog.i(TAG, "设备事件列表" + JSON.toJSONString(getEvents));

                //属性上报
                Random random = new Random();
                double var1 = (double)random.nextInt(1000);
                double var2 = (double)random.nextInt(1000);
                System.out.println("var1=" + var1);
                System.out.println("var2=" + var2);
                Map<String, ValueWrapper> reportData = new HashMap<>();
                reportData.put("Var1", new ValueWrapper.DoubleValueWrapper(var1));
                reportData.put("Var2", new ValueWrapper.DoubleValueWrapper(var2));
                handlePropertySet(reportData);

//                //事件上报
//                Map<String,ValueWrapper> values = new HashMap<>();
//                values.put("eventValue", new ValueWrapper.IntValueWrapper(0));
//                OutputParams outputParams = new OutputParams(values);
//                handleEventSet(outputParams);
            }
        });
    }

    /**
     *  Alink JSON 方式设备端上报属性
     * @param reportData       上报属性值
     */
    private void handlePropertySet(Map<String, ValueWrapper> reportData) {
        ALog.i(TAG, "上报 属性identity=" + reportData);

        LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
            /*
             * 属性上报成功
             */
            public void onSuccess(String s, Object o) {
                ALog.i(TAG, "上报成功 onSuccess() called with: s = [" + s + "], o = [" + o + "]");
            }

            /*
             * 属性上报失败
             */
            public void onError(String s, AError aError) {
                ALog.i(TAG, "上报失败onError() called with: s = [" + s + "], aError = [" + JSON.toJSONString(aError) + "]");
            }
        });
    }

    /**
     *  Alink JSON 方式设备端上报事件
     * @param params      事件上报参数
     */
    private void handleEventSet(OutputParams params) {
        ALog.i(TAG, "上报事件 identifyID=" + "Offline_alarm" + "  params=" + JSON.toJSONString(params));
        LinkKit.getInstance().getDeviceThing().thingEventPost("Offline_alarm",  params, new IPublishResourceListener() {
            /*
             * 属性上报成功
             */
            public void onSuccess(String s, Object o) {
                ALog.i(TAG, "上报成功 onSuccess() called with: s = [" + s + "], o = [" + o + "]");
            }

            /*
             * 属性上报失败
             */
            public void onError(String s, AError aError) {
                ALog.i(TAG, "上报失败onError() called with: s = [" + s + "], aError = [" + JSON.toJSONString(aError) + "]");
            }
        });
    }
}