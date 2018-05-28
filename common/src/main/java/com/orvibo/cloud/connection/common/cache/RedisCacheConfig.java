package com.orvibo.cloud.connection.common.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.io.IOException;

/**
 * Created by sunlin on 2017/9/20.
 */
@Configuration
@EnableRedisRepositories
public class RedisCacheConfig {

    @Bean
    public RedisConnectionFactory connectionFactory() throws IOException {

        return new JedisConnectionFactory(redisClusterConfiguration());
    }

    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() throws IOException{
        return new RedisClusterConfiguration(new ResourcePropertySource("redis.properties", "classpath:redis.properties"));
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() throws IOException{
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

}
