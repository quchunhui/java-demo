package demo.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import demo.bean.PropertiesBean;

public class IotClientUtils {
    /**
     * 获取DefaultAcsClient
     * @param propertiesBean 配置文件
     * @return DefaultAcsClient
     */
    public static DefaultAcsClient getIotClient(PropertiesBean propertiesBean) {
        String accessKey = propertiesBean.getAccessKey();
        String accessSecret = propertiesBean.getAccessSecret();
        String productCode = propertiesBean.getProductCode();
        String regionId = propertiesBean.getRegionId();
        String domain = propertiesBean.getDomain();

        DefaultAcsClient client = null;
        try {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessSecret);
            DefaultProfile.addEndpoint(regionId, productCode, domain);
            client = new DefaultAcsClient(profile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return client;
    }
}
