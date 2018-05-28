package com.orvibo.cloud.connection.server.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Created by sunlin on 2017/7/20.
 */
public class BusinessCodeConvert extends ClassicConverter {
    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        String bc = LogUtil.getBusinessCode();
        return bc == null ? "" : bc;
    }
}
