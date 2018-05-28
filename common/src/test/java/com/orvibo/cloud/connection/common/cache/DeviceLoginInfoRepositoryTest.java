package com.orvibo.cloud.connection.common.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by sunlin on 2017/9/20.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RefreshableRedisConfig.class})
//@ContextConfiguration(classes = {ApolloConfig.class})
public class DeviceLoginInfoRepositoryTest {

    @Autowired
    DeviceLoginInfoRepository deviceLoginInfoRepository;

    @Test
    public void testCRUDOperation() throws Exception{
        DeviceLoginInfo info = new DeviceLoginInfo();
        info.setSessionID("sessionId_1");
        info.setDeviceIp("/127.0.0.1:61715");
        info.setProtocolType("dk");
        info.setKey("key_123");
        info.setLoginServer("192.168.6.89");
        info.setTimestamp(System.currentTimeMillis());

        DeviceParameters dp = new DeviceParameters();
        dp.setHardware("hardware_1");
        dp.setSoftware("software_1");
        info.setDeviceParameters(dp);
        deviceLoginInfoRepository.save(info);

        System.out.println("#####111 fetch from redis by id: " + deviceLoginInfoRepository.findById("/127.0.0.1:61715").get());
        System.out.println("#####111 count from redis: " + deviceLoginInfoRepository.count());
        Thread.sleep(1000 * 60 *1);

        System.out.println("#####222fetch from redis by id: " + deviceLoginInfoRepository.findById("/127.0.0.1:61715"));
        System.out.println("#####222count from redis: " + deviceLoginInfoRepository.count());
        deviceLoginInfoRepository.delete(info);
        Thread.sleep(1000 * 60 *2);
    }

}