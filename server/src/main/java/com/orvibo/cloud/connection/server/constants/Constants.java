package com.orvibo.cloud.connection.server.constants;

/**
 * Created by sunlin on 2017/6/20.
 */
public class Constants {

    public static final int HEAD[] = {0x68, 0x64};
    public static final String HEAD_STRING = "hd";

    public static final String PUBLIC_KEY = "khggd54865SNJHGF";

    // 使用公钥加密的协议
    public static final String PROTOCOL_TYPE_PK = "pk";
    // 使用动态密钥加密的协议；
    public static final String PROTOCOL_TYPE_DK = "dk";

    public final static String CHARSET_UTF_8 = "UTF-8";

    /**
     * 使用到的队列
     */
    public final static String CONNECTION_TEST_TOPIC = "connection-test";

}
