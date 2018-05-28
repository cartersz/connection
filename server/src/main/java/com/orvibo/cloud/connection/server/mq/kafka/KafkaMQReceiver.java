package com.orvibo.cloud.connection.server.mq.kafka;

import com.orvibo.cloud.connection.common.protocol.BaseCommandDTO;
import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.common.protocol.DTOBuilder;
import com.orvibo.cloud.connection.common.protocol.ResponseCommandDTO;
import com.orvibo.cloud.connection.server.constants.Constants;
import com.orvibo.cloud.connection.server.mq.MQReceiver;
import com.orvibo.cloud.connection.server.tcp.netty.ConnectionManager;
import com.orvibo.cloud.connection.server.tcp.netty.second.NettyTCPServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sunlin on 2017/9/6.
 */
public class KafkaMQReceiver implements MQReceiver {

    private NettyTCPServer tcpServer;

    private ConnectionManager connectionManager;

    private static final Logger logger = LoggerFactory.getLogger(KafkaMQReceiver.class);

    public void receiveBaseCommandDTO(String jsonContent) {
        logger.info("receive mq message jsonContent => {}", jsonContent);
        if (StringUtils.isEmpty(jsonContent)) {
            return ;
        }
        ResponseCommandDTO commandDTO = DTOBuilder.buildResponseCommandDTO(jsonContent);
        if (0 == commandDTO.getCmd()) {
//            connectionManager.setKeyForConnection(commandDTO.getDeviceIp(), commandDTO.getKey());
            connectionManager.setKeyForConnection(commandDTO.getDeviceIp(), Constants.PUBLIC_KEY);
        }
        CommandPackage cp = buildCommandPackage(commandDTO);
        tcpServer.sendCommand(commandDTO.getDeviceIp(), cp);
    }

    private CommandPackage buildCommandPackage(BaseCommandDTO commandDTO) {
        CommandPackage cp = new CommandPackage();
        cp.setPayload(commandDTO.getPayload());
        cp.setProtocolType(commandDTO.getProtocolType());
        cp.setSessionID(commandDTO.getSessionID());
        return cp;
    }

    public NettyTCPServer getTcpServer() {
        return tcpServer;
    }

    public void setTcpServer(NettyTCPServer tcpServer) {
        this.tcpServer = tcpServer;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
