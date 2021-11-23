package ripple.server.helper;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ripple.common.tcp.message.HeartbeatRequest;

import java.net.InetSocketAddress;
import java.util.UUID;

public class NettyApi {
    public static void heartbeat(Channel channel) {
        HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
        heartbeatRequest.setUuid(UUID.randomUUID());
        InetSocketAddress localAddress = ((NioSocketChannel) channel).localAddress();
        InetSocketAddress removeAddress = ((NioSocketChannel) channel).remoteAddress();
        System.out.println("[" + localAddress.getHostString() + ":" + localAddress.getPort()
                + "<-->" + removeAddress.getHostString() + ":" + removeAddress.getPort() + "] "
                + "Send heartbeat request. uuid = " + heartbeatRequest.getUuid().toString());
        channel.writeAndFlush(heartbeatRequest);
    }
}
