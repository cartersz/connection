package com.orvibo.cloud.connection.common.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by sunlin on 2018/3/27.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MongoDBConfig.class})
public class MongoDBConfigTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testConnection() throws InterruptedException {
        System.out.println("####=> " + mongoTemplate.count(new Query(), "message"));

        Thread.sleep(1000 * 60 *1);

    }
}