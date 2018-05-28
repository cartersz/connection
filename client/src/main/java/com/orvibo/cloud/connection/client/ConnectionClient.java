package com.orvibo.cloud.connection.client;

import com.orvibo.cloud.connection.common.cache.DeviceLoginInfo;
import com.orvibo.cloud.connection.common.cache.DeviceLoginInfoRepository;
import com.orvibo.cloud.connection.common.cache.RefreshableRedisConfig;
import com.orvibo.cloud.connection.common.protocol.BaseCommandDTO;
import com.orvibo.cloud.connection.common.protocol.DTOBuilder;
import com.orvibo.cloud.connection.common.protocol.ResponseCommandDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Created by sunlin on 2017/9/12.
 */
public class ConnectionClient {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionClient.class);

    private static final AnnotationConfigApplicationContext ctx;

    private static final ClientKafkaMQSender clientKafkaMQSender;

    private static final DeviceLoginInfoRepository deviceLoginInfoRepository;

    static {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.register(RefreshableRedisConfig.class);
        ctx.refresh();
        clientKafkaMQSender = ctx.getBean(ClientKafkaMQSender.class);
        deviceLoginInfoRepository = ctx.getBean(DeviceLoginInfoRepository.class);
    }


    public static void sendCommand(String device, int cmd, String jsonPayload){
        logger.info("connection client send command,device={}, jsonPayload={}", device, jsonPayload);
        if (StringUtils.isEmpty(device) || StringUtils.isEmpty(jsonPayload)) {
            throw new IllegalArgumentException("device and jsonPayload should not be null");
        }
        DeviceLoginInfo deviceLoginInfo    = deviceLoginInfoRepository.findById(device).orElse(null);

        String topic = resolveServerTopic(deviceLoginInfo.getLoginServer());
        ResponseCommandDTO responseCommandDTO = getResponseCommandDTO(cmd, jsonPayload, deviceLoginInfo);

        clientKafkaMQSender.sendCommandDTO(topic, responseCommandDTO);
    }

    public static void sendCommand(BaseCommandDTO baseCommandDTO, int cmd, String key, String jsonPayload) {
        logger.info("connection client send command by topic and baseCommandDTO,cmd={}, baseCommandDTO={}, jsonPayload={}", cmd, baseCommandDTO, jsonPayload);
        if (baseCommandDTO == null || StringUtils.isEmpty(jsonPayload)) {
            throw new IllegalArgumentException("baseCommandDTO and jsonPayload should not be null");
        }

        String topic = resolveServerTopic(baseCommandDTO.getLoginServer());
        ResponseCommandDTO responseCommandDTO = getResponseCommandDTO(cmd, jsonPayload, key, baseCommandDTO);
        clientKafkaMQSender.sendCommandDTO(resolveServerTopic(baseCommandDTO.getLoginServer()), responseCommandDTO);
    }

    private static ResponseCommandDTO getResponseCommandDTO(int cmd, String jsonPayload, DeviceLoginInfo deviceLoginInfo) {
        return DTOBuilder.buildResponseCommandDTO(cmd, jsonPayload, deviceLoginInfo.getLoginServer(), deviceLoginInfo.getDeviceIp(), deviceLoginInfo.getSessionID(), deviceLoginInfo.getProtocolType(), deviceLoginInfo.getKey());
    }

    private static ResponseCommandDTO getResponseCommandDTO(int cmd, String jsonPayload, String key, BaseCommandDTO baseCommandDTO) {
        return DTOBuilder.buildResponseCommandDTO(cmd, jsonPayload, baseCommandDTO.getLoginServer(), baseCommandDTO.getDeviceIp(), baseCommandDTO.getSessionID(), baseCommandDTO.getProtocolType(),key);
    }
    private static String resolveServerTopic(String loginServer) {
        return "connection-test";
    }

}
