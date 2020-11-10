package demo.gw;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.dm.api.BaseInfo;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.dm.api.SignUtils;
import com.aliyun.alink.dm.model.ResponseModel;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tools.AError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GWProductDemo {
    private static final String GWproductKey = "a10IkCI7B1r";
    private static final String GWdeviceName = "QDView1";
    private static final String GWdeviceSecret = "TtTUsAhGtxwtociWEvFrLmOldGfmvRqA";
    private static final String regionId = "cn-shanghai";

    public static void main(String[] args) {
        LinkKitInitParams params = new LinkKitInitParams();

        /*
         * 设置 Mqtt 初始化参数
         */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = GWproductKey;
        config.deviceName = GWdeviceName;
        config.deviceSecret = GWdeviceSecret;
        config.channelHost = GWproductKey + ".iot-as-mqtt." + regionId + ".aliyuncs.com:1883";

        /*
         * 是否接受离线消息
         * 对应 mqtt 的 cleanSession 字段
         */
        config.receiveOfflineMsg = false;
        params.mqttClientConfig = config;

        /*
         * 设置初始化，传入设备证书信息
         */
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = GWproductKey;
        deviceInfo.deviceName = GWdeviceName;
        deviceInfo.deviceSecret = GWdeviceSecret;
        params.deviceInfo = deviceInfo;

        /*
         * 建立连接
         */
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            public void onError(AError aError) {
                System.out.println("Init Error error=" + aError);
            }

            public void onInitDone(InitResult initResult) {
                System.out.println("onInitDone result=" + initResult);

                //获取网关下topo关系，查询网关与子设备是否已经存在topo关系
                //如果已经存在，则直接上线子设备
                gatewayGetSubDevices();
            }
        });
    }

    /**
     * 获取网关下topo关系，查询网关与自设备是否已经存在topo关系
     */
    private static void gatewayGetSubDevices() {
        LinkKit.getInstance().getGateway().gatewayGetSubDevices(new IConnectSendListener() {
            @Override
            public void onResponse(ARequest request, AResponse aResponse) {
               System.out.println("获取网关的topo关系成功 : " + JSONObject.toJSONString(aResponse));

                // 获取子设备列表结果
                try {
                    ResponseModel<List<DeviceInfo>> response = JSONObject.parseObject(
                        aResponse.data.toString(),
                        new TypeReference<ResponseModel<List<DeviceInfo>>>() {}.getType());

                    List<DeviceInfo> deviceInfoList = response.data;
                    System.out.println("子设备列表：" + deviceInfoList);
                    if (deviceInfoList == null || deviceInfoList.size() <= 0) {
                        return;
                    }

                    // 待添加拓扑关系子设备信息
                    deviceInfoList.forEach(GWProductDemo::gatewayAddSubDevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(ARequest request, AError error) {
               System.out.println("获取网关的topo关系失败 : " + JSONObject.toJSONString(error));
            }
        });
    }

    /**
     * 待添加拓扑关系子设备信息
     */
    private static void gatewayAddSubDevice(DeviceInfo deviceInfo) {
        BaseInfo baseInfo1 = new BaseInfo();
        baseInfo1.productKey = deviceInfo.productKey;
        baseInfo1.deviceName = deviceInfo.deviceName;
        String deviceSecret = deviceInfo.deviceSecret;

        LinkKit.getInstance().getGateway().gatewayAddSubDevice(baseInfo1, new ISubDeviceConnectListener() {
            @Override
            public String getSignMethod() {
                // 使用的签名方法
                return "hmacsha1";
            }

            @Override
            public String getSignValue() {
                // 获取签名，用户使用 deviceSecret 获得签名结果
                Map<String, String> signMap = new HashMap<>();
                signMap.put("productKey", baseInfo1.productKey);
                signMap.put("deviceName", baseInfo1.deviceName);
                signMap.put("clientId", getClientId());
                String sign = SignUtils.hmacSign(signMap, deviceSecret);
                System.out.println("[------]productKey=" + baseInfo1.productKey);
                System.out.println("[------]deviceName=" + baseInfo1.deviceName);
                System.out.println("[------]deviceSecret=" + deviceSecret);
                System.out.println("[------]签名:" + sign);
                return sign;
            }

            @Override
            public String getClientId() {
                // clientId 可为任意值
                // 注意：一定要使用一个固定的值，不能是每次都变化的。
                return "clientId";
            }

            @Override
            public Map<String, Object> getSignExtraData() {
                return null;
            }

            @Override
            public void onConnectResult(boolean isSuccess, ISubDeviceChannel iSubDeviceChannel, AError aError) {
                if (isSuccess) {
                    // 子设备添加成功，接下来可以做子设备上线的逻辑
                    System.out.println("topo关系添加成功 : " + JSONObject.toJSONString(iSubDeviceChannel));

                    // 子设备上线
                    gatewaySubDeviceLogin(deviceInfo);
                } else {
                   System.out.println("topo关系添加失败 : " + JSONObject.toJSONString(aError));
                }
            }

            @Override
            public void onDataPush(String s, AMessage aMessage) {
                // 收到子设备下行数据 topic=" + s  + ", data=" + message
                // 如禁用 删除 已经 设置、服务调用等 返回的数据message.data 是 byte[]
                System.out.println("onDataPush. s=" + s);
            }
        });
    }

    /**
     * 调用子设备上线之前，请确保已完成子设备添加。网关发现子设备连上网关之后，
     * 需要告知云端子设备上线，子设备上线之后可以执行子设备的订阅、发布等操作。
     */
    private static void gatewaySubDeviceLogin(DeviceInfo deviceInfo){
        BaseInfo baseInfo1 = new BaseInfo();
        baseInfo1.productKey = deviceInfo.productKey;
        baseInfo1.deviceName = deviceInfo.deviceName;

        LinkKit.getInstance().getGateway().gatewaySubDeviceLogin(baseInfo1, new ISubDeviceActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("子设备上线成功。");
                // 代理子设备上线成功
                // 上线之后可删除和禁用子设备
                // subDevDisable(null);
                // subDevDelete(null);
            }

            @Override
            public void onFailed(AError aError) {
                System.out.println("onFailed() called with: aError = [" + aError + "]");
            }
        });
    }
}
