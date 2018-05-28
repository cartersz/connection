package com.orvibo.cloud.connection.common.cache;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.io.IOException;

/**
 * Created by sunlin on 2017/10/13.
 */

@Configuration
@EnableApolloConfig
@Import(RefreshAutoConfiguration.class)
@RefreshScope
@EnableRedisRepositories
public class ApolloConfig {

//    @Autowired
//    ApolloRedisConfiguration apolloRedisConfiguration;

    @Bean(name = "redisConnectionFactory")
    @RefreshScope
    public RedisConnectionFactory connectionFactory() throws IOException {

        return new JedisConnectionFactory(redisConfigBean().redisClusterConfiguration());
    }

    @Bean(name = "redisTemplate")
//    @RefreshScope
    public RedisTemplate<?, ?> redisTemplate() throws IOException {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
        template.setConnectionFactory(connectionFactory());
        return template;
    }

    @Bean
    public RedisConfigBean redisConfigBean() {
        return new RedisConfigBean();
    }
}
