package com.st.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 读取city.properties配置文件中的全国网名占比
 * Created by zhaoyx on 2016/9/23.
 */
@Configuration
@ConfigurationProperties(prefix = "city")
public class CityConfiguration {

    public Map<String,Double> name = new HashMap<>();

    public  Map<String, Double> getName() {
        return name;
    }

    public  void setName(Map<String, Double> name) {
        this.name = name;
    }
}
