package rexel.iotextension;

import lombok.Getter;

@Getter
public class ExtensionConst {
    public static String tenant = "rexelcn0";
    public static String username = "chunhui.qu@rexel.com.cn";
    public static String password = "rexel2020";
    public static String mqttUrl = "tcp://rexelcn0.mciotextension.cn1.mindsphere-in.cn:1883";
    public static String restUrl = "https://rexelcn0.mciotextension.cn1.mindsphere-in.cn";
    public static String clientId = "local123456789";

    public static String identity_externalIds = "/identity/externalIds";
    public static String devicecontrol_deviceCredentials = "/devicecontrol/deviceCredentials";
    public static String devicecontrol_operations = "/devicecontrol/operations";

    public static String publish_topic = "s/us";
    public static String subscribe_topic = "s/ds";
}
