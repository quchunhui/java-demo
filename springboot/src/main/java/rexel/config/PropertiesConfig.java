package rexel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "openapi")
public class PropertiesConfig {
    private String address;
    private String accessId;
    private String accessKey;
    private String deviceId;
}
