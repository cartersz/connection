package com.orvibo.cloud.connection.server.tcp;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by sunlin on 2017/7/11.
 */
public class LogTest {
    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void logTest() throws Exception {
        logger.info("Start test...");
        new ClassPathXmlApplicationContext("spring-server.xml").start();
    }
}
