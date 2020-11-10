package demo.utils;

import demo.bean.PropertiesBean;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtils {
    private static PropertiesUtils propertiesUtils = null;

    private PropertiesUtils() {
        //Nothing
    }

    public static PropertiesUtils getInstance() {
        if (propertiesUtils == null) {
            propertiesUtils = new PropertiesUtils();
        }
        return propertiesUtils;
    }

    public PropertiesBean readProperties() {
        Properties properties;
        try {
            Resource resource = new ClassPathResource("application.properties");
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String accessKey = properties.getProperty("aliot.accessKey");
        String accessSecret = properties.getProperty("aliot.accessSecret");
        String uid = properties.getProperty("aliot.uid");
        String regionId = properties.getProperty("aliot.regionId");
        String productKey = properties.getProperty("aliot.productKey");
        String deviceName = properties.getProperty("aliot.deviceName");
        String deviceSecret = properties.getProperty("aliot.deviceSecret");
        String productCode = properties.getProperty("aliot.productCode");
        String domain = properties.getProperty("aliot.domain");
        String version = properties.getProperty("aliot.version");

        PropertiesBean bean = new PropertiesBean();
        bean.setAccessKey(accessKey);
        bean.setAccessSecret(accessSecret);
        bean.setUid(uid);
        bean.setRegionId(regionId);
        bean.setProductKey(productKey);
        bean.setDeviceName(deviceName);
        bean.setDeviceSecret(deviceSecret);
        bean.setProductCode(productCode);
        bean.setDomain(domain);
        bean.setVersion(version);

        return bean;
    }
}
