package com.orvibo.cloud.connection.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by sunlin on 2017/7/10.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static ClassPathXmlApplicationContext context;

    public static void main(String[] args) {

        logger.info("cloud connection service starting...");
        context = new ClassPathXmlApplicationContext("spring-server.xml");
        context.start();
        logger.info("cloud connection service started");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                context.stop();
            }
        });
    }

}
