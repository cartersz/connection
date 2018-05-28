package com.orvibo.cloud.connection.server.tcp.command;

import com.alibaba.fastjson.JSON;
import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by sunlin on 2017/7/3.
 */
public class CommandJsonReader {

    private static final Logger logger = LoggerFactory.getLogger(CommandJsonReader.class);

    public static String getJsonString(String fileName) throws Exception{
        String filePath = CommandJsonReader.class.getResource(fileName).getPath();
        logger.info("file path => {}",filePath);
        InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(new File(filePath)));
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        String line;
        StringBuilder stringBuilder=new StringBuilder();
        while ((line=bufferedReader.readLine())!=null){
            stringBuilder.append(line);
        }
        bufferedReader.close();
        inputStreamReader.close();
        return stringBuilder.toString();
    }

    public static CommandPackage getCommandPackage(String fileName) throws Exception {
        String json = getJsonString(fileName);

        return JSON.parseObject(json, CommandPackage.class);
    }


}
