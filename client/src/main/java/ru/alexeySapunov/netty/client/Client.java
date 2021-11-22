package ru.alexeySapunov.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.alexeySapunov.netty.common.handler.JsonDecoder;
import ru.alexeySapunov.netty.common.handler.JsonEncoder;
import ru.alexeySapunov.netty.common.message.*;

import java.io.IOException;
import java.io.RandomAccessFile;
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
                                    new JsonDecoder(),
                                    new JsonEncoder(),
                                    new SimpleChannelInboundHandler<Message>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws IOException {
                                            if (msg instanceof FileMessage) {
                                                var message = (FileMessage) msg;
                                                try(final RandomAccessFile accessFile = new RandomAccessFile("file", "rw")) {
                                                    accessFile.write(message.getContent());
                                                }
                                                ctx.close();
                                            }
                                        }
                                    }
                            );
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
            final Channel channel = bootstrap.connect("localhost", 9000).sync().channel();

           while (true) {
                final DownloadFileRequestMessage message = new DownloadFileRequestMessage();
                message.setPath("C:\\Java\\netty\\test.json");
                channel.writeAndFlush(message);

                final TextMessage textMessage = new TextMessage();
                textMessage.setText("new text message from client");
                channel.writeAndFlush(textMessage);

                final DateMessage dateMessage = new DateMessage();
                dateMessage.setDate(new Date());
                channel.writeAndFlush(dateMessage);

                Thread.sleep(1000);
            }
        } finally {
            worker.shutdownGracefully();
        }
    }
}
