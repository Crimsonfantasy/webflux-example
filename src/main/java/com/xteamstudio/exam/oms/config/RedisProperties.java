package com.xteamstudio.exam.oms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * spring application properties not auto config, when use webflux, so read config self.
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@PropertySource("classpath:application.properties")
public class RedisProperties {

    private int port;

    private String host;

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }
}