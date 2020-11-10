package rexel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mindsphere.sdk")
public class MindSphereConfig {
    private String address;
    private String accessId;
    private String secretkey;
    private String appName;
    private String appVersion;
    private String hostTenant;
    private String userTenant;
}
