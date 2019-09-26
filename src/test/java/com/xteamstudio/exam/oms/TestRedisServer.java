package com.xteamstudio.exam.oms;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.jdbc.Sql;
import redis.embedded.RedisServer;

@Component
@Sql(value = "classpath:data.sql")
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
