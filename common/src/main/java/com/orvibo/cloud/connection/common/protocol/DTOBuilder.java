package com.orvibo.cloud.connection.common.protocol;

import com.alibaba.fastjson.JSON;

/**
 * Created by sunlin on 2017/9/5.
 */
public class DTOBuilder {

    public static BaseCommandDTO buildBaseCommandDTO(String loginServer, String device, CommandPackage commandPackage) {
        BaseCommandDTO baseCommandDTO = new BaseCommandDTO();
        baseCommandDTO.setSessionID(commandPackage.getSessionID());
        baseCommandDTO.setDeviceIp(device);
        baseCommandDTO.setLoginServer(loginServer);
        baseCommandDTO.setPayload(commandPackage.getPayload());
        baseCommandDTO.setProtocolType(commandPackage.getProtocolType());
        return baseCommandDTO;
    }

    public static ResponseCommandDTO buildResponseCommandDTO(int cmd, String jsonPayload, String loginServer, String deviceIp, String sessionID, String protocolType, String key) {
        ResponseCommandDTO responseCommandDTO = new ResponseCommandDTO();
        responseCommandDTO.setPayload(jsonPayload);
        responseCommandDTO.setLoginServer(loginServer);
        responseCommandDTO.setDeviceIp(deviceIp);
        responseCommandDTO.setSessionID(sessionID);
        responseCommandDTO.setCmd(cmd);
        responseCommandDTO.setProtocolType(protocolType);
        responseCommandDTO.setKey(key);
        return responseCommandDTO;
    }

    public static ResponseCommandDTO buildResponseCommandDTO(String jsonContent) {
        ResponseCommandDTO commandDTO = JSON.parseObject(jsonContent, ResponseCommandDTO.class);
        return commandDTO;
    }

}
