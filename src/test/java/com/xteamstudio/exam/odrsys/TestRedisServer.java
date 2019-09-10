package com.xteamstudio.exam.odrsys;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

@Component
public class TestRedisServer implements InitializingBean, DisposableBean {
    private RedisServer redisServer = new RedisServer(6379);

    @Override
    public void destroy() throws Exception {
        redisServer.stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisServer.start();
    }
}
