package com.st.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/3/15.
 */
@Configuration
@ConfigurationProperties(prefix = "provinces")
public class ProvinceConfiguration {
    public Map<String,String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
