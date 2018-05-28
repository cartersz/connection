package com.orvibo.cloud.connection.common.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2018/3/27.
 */
@Configuration
public class MongoDBConfig extends AbstractMongoConfiguration {
    @Override
    public String getDatabaseName() {
        return "vihome_cloud";
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        List<ServerAddress> serverAddressList = new ArrayList<ServerAddress>();
        serverAddressList.add(new ServerAddress("192.168.2.201", 27017));
        List<MongoCredential> mongoCredentials = new ArrayList<MongoCredential>();
        mongoCredentials.add(MongoCredential.createCredential("mongouser", "admin", "snjJH65sdp09#de".toCharArray()));

        return new MongoClient(serverAddressList,mongoCredentials);
    }

}
