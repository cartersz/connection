package com.orvibo.cloud.connection.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by sunlin on 2017/9/6.
 */
public class IPUtil {

    private static String localSeverIp;

    public static void initialLocalServerIp() throws UnknownHostException{
        if (localSeverIp == null) {
            localSeverIp = InetAddress.getLocalHost().getHostAddress().toString();
        }
    }

    public static String getLocalSeverIp() {
        return localSeverIp;
    }
}
