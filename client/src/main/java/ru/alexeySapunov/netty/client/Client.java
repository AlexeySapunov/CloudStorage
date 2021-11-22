package ru.alexeySapunov.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        new Client().run();
    }

    public void run() throws InterruptedException {
        final NioEventLoopGroup worker = new NioEventLoopGroup(1);
        try {
            final Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(512, 0, 2, 0, 2),
                                    new LengthFieldPrepender(2),
                                    new StringEncoder(),
                                    new StringDecoder(),
                                    new SimpleChannelInboundHandler<String>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                            System.out.println("Incoming massage from server: " + msg);
                                        }
                                    }
                            );
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
            final Channel channel = bootstrap.connect("localhost", 9000).sync().channel();

            while (true) {
                channel.writeAndFlush("new massage from client: " + new Date());
                Thread.sleep(5000);
            }
        } finally {
            worker.shutdownGracefully();
        }
    }
}
