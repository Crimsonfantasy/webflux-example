package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.config.RedisProperties;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class EmbeddedRedisTestConfiguration {

    private final redis.embedded.RedisServer redisServer;

    public EmbeddedRedisTestConfiguration(RedisProperties redisProperties) throws IOException {
        this.redisServer = new redis.embedded.RedisServer(redisProperties.getPort());
    }

    @PostConstruct
    public void startRedis() {
        this.redisServer.start();
    }

    public void stopRedis() {
        this.redisServer.stop();
    }
}