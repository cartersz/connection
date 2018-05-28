package com.orvibo.cloud.connection.server.tcp;

import com.orvibo.cloud.connection.common.protocol.CommandPackage;
import com.orvibo.cloud.connection.server.constants.Constants;
import com.orvibo.cloud.connection.server.tcp.command.CommandJsonReader;
import com.orvibo.cloud.connection.server.tcp.netty.CommandParser;
import com.orvibo.cloud.connection.server.tcp.netty.second.Package2ObjectDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sunlin on 2017/11/13.
 */
public class Package2ObjectDecoderTest {
    @Test
    public void testRequestKey() throws Exception{
//        CommandPackage cp = CommandJsonReader.getCommandPackage("RequestKey.json");
//        System.out.printf(String.format("RequestKey input command => %s", cp));
//
//        ByteBuf buf = CommandParser.parseCommand2ByteBuf(cp, Constants.PUBLIC_KEY);
//        ByteBuf input = buf.duplicate();
//
//        EmbeddedChannel channel = new EmbeddedChannel(new Package2ObjectDecoder());
//
//        assertTrue(channel.writeInbound(input.retain()));
//        assertTrue(channel.finish());
        CommandPackage cp = CommandJsonReader.getCommandPackage("RequestKey.json");
        System.out.printf(String.format("RequestKey input command => %s", cp));

        EmbeddedChannel channel = getChannel(cp);
        CommandPackage result = (CommandPackage)channel.readInbound();
        System.out.printf(String.format("RequestKey received command => %s", result));

        assertEquals(result.getPayload(), cp.getPayload());
        assertEquals(result.getLength(), cp.getLength());
        assertEquals(result.getProtocolType(), cp.getProtocolType());
        assertEquals(result.getSessionID(), cp.getSessionID());
    }

    @Test
    public void testDeviceLogin() throws Exception {
        CommandPackage cp = CommandJsonReader.getCommandPackage("DeviceLogin.json");
        System.out.printf(String.format("DeviceLogin input command => %s", cp));

        EmbeddedChannel channel = getChannel(cp);
        CommandPackage result = (CommandPackage)channel.readInbound();
        System.out.printf(String.format("DeviceLogin received command => %s", result));

        assertEquals(result.getPayload(), cp.getPayload());
        assertEquals(result.getLength(), cp.getLength());
        assertEquals(result.getProtocolType(), cp.getProtocolType());
        assertEquals(result.getSessionID(), cp.getSessionID());
    }

    private EmbeddedChannel getChannel(CommandPackage cp) throws Exception {

        ByteBuf input = CommandParser.parseCommand2ByteBuf(cp, Constants.PUBLIC_KEY);

        EmbeddedChannel channel = new EmbeddedChannel(new Package2ObjectDecoder());
        assertTrue(channel.writeInbound(input.retain()));
        assertTrue(channel.finish());
        return channel;
    }
}
