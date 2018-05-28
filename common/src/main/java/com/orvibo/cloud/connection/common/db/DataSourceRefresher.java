package com.orvibo.cloud.connection.common.db;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sunlin on 2017/10/23.
 */
@Component
public class DataSourceRefresher {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceRefresher.class);

    @ApolloConfig("CLOUD1.datasource-druid")
    private Config config;

    @Autowired
    private org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

    @ApolloConfigChangeListener("CLOUD1.datasource-druid")
    public void dataSourceOnChange(ConfigChangeEvent changeEvent) {
        refreshScope.refresh("dataSource");
    }


    public String getUrl() {
        return config.getProperty("datasource.druid.url", "jdbc:mysql://192.168.2.201:3306/test_master?useUnicode=true&characterEncoding=UTF-8");
    }

    public String getUsername() {
        return config.getProperty("datasource.druid.username", "root");
    }

    public String getPassword() {
        return config.getProperty("datasource.druid.password", "orvibo888");
    }

    public String getFilters() {
        return config.getProperty("datasource.druid.filters", "mergeStat");
    }

    public int getMaxActive() {
        return config.getIntProperty("datasource.druid.maxActive", 20);
    }


    public int getMinIdle() {
        return config.getIntProperty("datasource.druid.minIdle", 1);
    }

    public int getInitialSize() {
        return config.getIntProperty("datasource.druid.initialSize", 1);
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return config.getIntProperty("datasource.druid.timeBetweenEvictionRunsMillis", 60000);
    }

    public int getMinEvictableIdleTimeMillis() {
        return config.getIntProperty("datasource.druid.minEvictableIdleTimeMillis", 300000);
    }

    public boolean isTestWhileIdle() {
        return config.getBooleanProperty("datasource.druid.testWhileIdle", true);
    }

    public boolean isTestOnBorrow() {
        return config.getBooleanProperty("datasource.druid.testOnBorrow", false);
    }

    public boolean isTestOnReturn() {
        return config.getBooleanProperty("datasource.druid.testOnReturn", false);
    }
}
