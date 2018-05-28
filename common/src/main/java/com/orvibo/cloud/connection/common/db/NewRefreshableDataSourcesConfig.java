package com.orvibo.cloud.connection.common.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by sunlin on 2017/10/25.
 */
@Configuration
//@EnableApolloConfig("CLOUD1.datasource-druid")
@ImportResource("classpath:spring-apolloconfig.xml")
@Import(RefreshAutoConfiguration.class)
@RefreshScope
@EnableJpaRepositories("com.orvibo.cloud.connection.common.db")
@EnableTransactionManagement
public class NewRefreshableDataSourcesConfig {

    @ApolloConfig("CLOUD1.datasource-druid")
    private Config config;

    @RefreshScope
    @Bean("dataSource")
    public DataSource dataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(config.getProperty("datasource.druid.url", "jdbc:mysql://192.168.2.201:3306/test_master?useUnicode=true&characterEncoding=UTF-8"));
        dataSource.setUsername(config.getProperty("datasource.druid.username", "root"));
        dataSource.setPassword(config.getProperty("datasource.druid.password", "orvibo888"));

        dataSource.setFilters(config.getProperty("datasource.druid.filters", "mergeStat"));

        dataSource.setMaxActive(config.getIntProperty("datasource.druid.maxActive", 20));
        dataSource.setMinIdle(config.getIntProperty("datasource.druid.minIdle", 1));
        dataSource.setInitialSize(config.getIntProperty("datasource.druid.initialSize", 1));

        dataSource.setTimeBetweenEvictionRunsMillis(config.getIntProperty("datasource.druid.timeBetweenEvictionRunsMillis", 60000));
        dataSource.setMinEvictableIdleTimeMillis(config.getIntProperty("datasource.druid.minEvictableIdleTimeMillis", 300000));

        dataSource.setTestWhileIdle(config.getBooleanProperty("datasource.druid.testWhileIdle", true));
        dataSource.setTestOnBorrow(config.getBooleanProperty("datasource.druid.testOnBorrow", false));
        dataSource.setTestOnReturn(config.getBooleanProperty("datasource.druid.testOnReturn", false));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan("com.orvibo.cloud.connection.common.db.entity");
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(vendorAdapter);
        Properties pps = new Properties();
        pps.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        pps.put("hibernate.show_sql", true);
        pps.put("hibernate.hbm2ddl.auto", "none");
        factory.setJpaProperties(pps);
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) throws SQLException {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

}
