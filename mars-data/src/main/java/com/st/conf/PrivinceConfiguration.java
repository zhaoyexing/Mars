package com.st.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/2/23.
 */
@Configuration
@ConfigurationProperties(prefix = "province")
public class PrivinceConfiguration {
    public Map<String,String> name = new HashMap<>();

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }
}
