package demo.updown;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.*;

import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import demo.bean.PropertiesBean;
import demo.utils.PropertiesUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DownServerClient {
    private static DefaultAcsClient client = null;
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        client = getIotClient(propertiesBean);
        if (client == null) {
            return;
        }

        Random random = new Random();
        double var1 = (double)random.nextInt(1000);
        double var2 = (double)random.nextInt(1000);
        double var3 = (double)random.nextInt(1000);
        double var4 = (double)random.nextInt(1000);
        System.out.println("var1=" + var1);
        System.out.println("var2=" + var2);
        System.out.println("var3=" + var3);
        System.out.println("var4=" + var4);

        Map<String, Object> ext = new HashMap<>();
        ext.put("testVar1", var1);
        ext.put("testVar2", var2);
        ext.put("testVar3", var3);
        ext.put("testVar4", var4);

        //设备信息
        String productKey = propertiesBean.getProductKey();
        String deviceName = propertiesBean.getDeviceName();

        //设置设备的属性
        SetDeviceProperty(productKey, deviceName, JSON.toJSONString(ext));

//        //批量设置设备属性
//        List<String>  deviceNames = new ArrayList<>();
//        deviceNames.add(deviceName);
//        SetDevicesProperty(productKey, deviceNames, JSON.toJSONString(ext));
//
//        //调用设备的服务
//        InvokeThingService(productKey, deviceName, "ModifyVehicleInfo", "{}");
//
//        //批量调用设备的服务
//        List<String>  deviceNamesService = new ArrayList<>();
//        deviceNamesService.add(deviceName);
//        InvokeThingsService(productKey, deviceNamesService, "ModifyVehicleInfo", "{}");
    }

    /**
     * 设置设备的属性
     */
    private static void SetDeviceProperty(String ProductKey, String DeviceName, String Items) {
        SetDevicePropertyRequest request = new SetDevicePropertyRequest();
        request.setIotId(null);
        request.setProductKey(ProductKey);
        request.setDeviceName(DeviceName);
        request.setItems(Items);

        SetDevicePropertyResponse response = null;
        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                System.out.println("设置设备的属性成功");
                System.out.println(JSON.toJSONString(response));
            } else {
                System.out.println("设置设备的属性失败");
                System.out.println(JSON.toJSONString(response));
            }
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("设置设备的属性失败！" + JSON.toJSONString(response));
        }
    }

    /**
     * 批量设置设备属性
     *
     * @param ProductKey      设置属性的设备所隶属的产品Key
     * @param DeviceNames     要设置属性的设备名称列表。
     * @param Items  要设置的属性信息，组成为key:value，数据格式为 JSON String。  必须
     */
    private static void SetDevicesProperty(
        String ProductKey, List<String> DeviceNames, String Items) {
        SetDevicesPropertyRequest request = new SetDevicesPropertyRequest();
        request.setProductKey(ProductKey);
        request.setDeviceNames(DeviceNames);
        request.setItems(Items);

        SetDevicesPropertyResponse response = null;
        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                System.out.println("批量设置设备属性成功");
                System.out.println(JSON.toJSONString(response));
            } else {
                System.out.println("批量设置设备属性失败");
                System.out.println(JSON.toJSONString(response));
            }
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("批量设置设备属性失败！" + JSON.toJSONString(response));
        }
    }

    /**
     * 在一个设备上执行指定的服务（调用设备服务）
     *  @param ProductKey 设备所隶属的产品Key
     * @param DeviceName 设备的名称
     * @param Identifier 高级版设备的服务Identifier  必须
     * @param Args       要启用服务的入参信息    必须
     */
    private static void InvokeThingService(
        String ProductKey, String DeviceName, String Identifier, String Args) {
        InvokeThingServiceRequest request = new InvokeThingServiceRequest();
        request.setIotId(null);
        request.setProductKey(ProductKey);
        request.setDeviceName(DeviceName);
        request.setIdentifier(Identifier);
        request.setArgs(Args);

        InvokeThingServiceResponse response = null;
        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                System.out.println("服务执行成功");
                System.out.println(JSON.toJSONString(response));
            } else {
                System.out.println("服务执行失败");
                System.out.println(JSON.toJSONString(response));
            }

            response.getData();
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("服务执行失败！" + JSON.toJSONString(response));
        }
    }

    /**
     * 批量调用设备服务
     *
     * @param ProductKey 设备所隶属的产品Key  必须
     * @param DeviceNames 设备的名称列表。必须
     * @param Identifier  高级版设备的服务Identifier  必须
     * @param Args 要启用服务的入参信息  必须
     */
    private static void InvokeThingsService(
        String ProductKey, List<String> DeviceNames, String Identifier, String Args) {
        InvokeThingsServiceRequest request = new InvokeThingsServiceRequest();
        request.setProductKey(ProductKey);
        request.setDeviceNames(DeviceNames);
        request.setIdentifier(Identifier);
        request.setArgs(Args);

        InvokeThingsServiceResponse response = null;
        try {
            response = client.getAcsResponse(request);

            if (response.getSuccess() != null && response.getSuccess()) {
                System.out.println("批量调用设备服务成功");
                System.out.println(JSON.toJSONString(response));
            } else {
                System.out.println("批量调用设备服务失败");
                System.out.println(JSON.toJSONString(response));
            }
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("批量调用设备服务失败！" + JSON.toJSONString(response));
        }
    }

    /**
     * 获取DefaultAcsClient
     * @param propertiesBean 配置文件
     * @return DefaultAcsClient
     */
    private static DefaultAcsClient getIotClient(PropertiesBean propertiesBean) {
        String accessKey = propertiesBean.getAccessKey();
        String accessSecret = propertiesBean.getAccessSecret();
        String productCode = propertiesBean.getProductCode();
        String regionId = propertiesBean.getRegionId();
        String domain = propertiesBean.getDomain();

        DefaultAcsClient client = null;
        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
            DefaultProfile.addEndpoint(regionId, regionId, productCode, domain);
            client = new DefaultAcsClient(profile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return client;
    }
}