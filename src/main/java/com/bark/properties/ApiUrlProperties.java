package com.bark.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author Ballen  2024/10/23 17:52
 */
@Data
@Component
@ConfigurationProperties(prefix = "api")
public class ApiUrlProperties {
    private String url;
}
