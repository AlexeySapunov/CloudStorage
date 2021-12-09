package ru.alexeySapunov.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.alexeySapunov.netty.common.handler.JsonDecoder;
import ru.alexeySapunov.netty.common.handler.JsonEncoder;
import ru.alexeySapunov.netty.common.logInSignUpService.LoginSignUpClients;
import ru.alexeySapunov.netty.common.message.DownloadFileRequestMessage;
import ru.alexeySapunov.netty.common.message.TextMessage;

import java.sql.SQLException;

public class Client {

    Callback onMessageReceivedCallback;

    public static void main(String[] args) throws InterruptedException {
        new Client().run();
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 3, 0, 3),
                                    new LengthFieldPrepender(3),
                                    new JsonDecoder(),
                                    new JsonEncoder(),
                                    new ClientHandler(onMessageReceivedCallback)
                            );
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            System.out.println("Client started");

            ChannelFuture channel = bootstrap.connect("localhost", 9000).sync();

            LoginSignUpClients client = new LoginSignUpClients();
            client.loginClients();
            channel.channel().writeAndFlush(client);

            TextMessage textMessage = new TextMessage();
            textMessage.setText("New incoming message");
            channel.channel().writeAndFlush(textMessage);

            DownloadFileRequestMessage message = new DownloadFileRequestMessage();
            message.setPath("C:\\Java\\netty\\bigFile.txt");
            channel.channel().writeAndFlush(message);

            channel.channel().closeFuture().sync();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
