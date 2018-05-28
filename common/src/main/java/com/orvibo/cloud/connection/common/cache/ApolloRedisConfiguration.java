package com.orvibo.cloud.connection.common.cache;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by sunlin on 2017/10/13.
 */
@Component
public class ApolloRedisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ApolloRedisConfiguration.class);

    @ApolloConfig("CLOUD1.redis-client")
    private Config config;

    @Autowired
    private org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

    public RedisClusterConfiguration redisClusterConfiguration() throws IOException {
        String clusterNnodes = config.getProperty("redis.cluster.nodes", "192.168.2.201:7000,192.168.2.202:7000,192.168.2.192:7000");
        int maxRedirects = config.getIntProperty("redis.cluster.max-redirects", 1);
        logger.info("get config value from apollo, clusterNnodes={}, maxRedirects={}", clusterNnodes, maxRedirects);
        HashMap map = new HashMap();
        map.put("spring.redis.cluster.nodes", clusterNnodes);
        if(maxRedirects >= 0) {
            map.put("spring.redis.cluster.max-redirects", maxRedirects);
        }

        return new RedisClusterConfiguration((PropertySource)(new MapPropertySource("redis.config", map)));
    }

    @ApolloConfigChangeListener("CLOUD1.redis-client")
    private void redisConfigOnChange(ConfigChangeEvent changeEvent) {
        if (changeEvent.isChanged("redis.cluster.nodes") || changeEvent.isChanged("redis.cluster.max-redirects")) {
            logger.info("config changed. notification redisTemplate bean refresh");
            refreshScope.refresh("redisConnectionFactory");
            //下面这个刷新不需要，因为redisTemplate会实时从redisConnectionFactory获取redis连接，只需要redisConnectionFactory保持最新就可以了
//            refreshScope.refresh("redisTemplate");
        }
    }

}
