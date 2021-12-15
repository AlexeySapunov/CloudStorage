package ru.alexeySapunov.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.alexeySapunov.netty.common.handler.JsonDecoder;
import ru.alexeySapunov.netty.common.handler.JsonEncoder;
import ru.alexeySapunov.netty.common.logInSignUpService.AuthService;
import ru.alexeySapunov.netty.common.logInSignUpService.LoginClient;
import ru.alexeySapunov.netty.common.logInSignUpService.SignUpClient;
import ru.alexeySapunov.netty.common.message.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Date;

public class Client {

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
                                    new SimpleChannelInboundHandler<Message>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
                                            if (msg instanceof LoginClient) {
                                                System.out.println(((LoginClient) msg).getName() + " logged in successfully!");
                                            }

                                            if (msg instanceof SignUpClient) {
                                                System.out.println(((SignUpClient) msg).getName() + " successfully registered!");
                                            }

                                            if (msg instanceof AuthService) {
                                                var message = (AuthService) msg;
                                                System.out.println("Lets try log in!");
                                                message.login();
                                            }

                                            if (msg instanceof TextMessage) {
                                                System.out.println("Receive message " + ((TextMessage) msg).getText());
                                            }

                                            if (msg instanceof DateMessage) {
                                                System.out.println("Receive message " + ((DateMessage) msg).getDate());
                                            }

                                            if (msg instanceof FileMessage) {
                                                System.out.println("New incoming file download message");
                                                var message = (FileMessage) msg;
                                                try (var accessFile = new RandomAccessFile("file", "rw")) {
                                                    accessFile.seek(message.getStartPosition());
                                                    accessFile.write(message.getContent());
                                                }
                                            }

                                            if (msg instanceof EndFileDownloadMessage) {
                                                ctx.close();
                                            }
                                        }

                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                            ctx.close();
                                            System.out.println("Catch cause" + cause.getMessage());
                                        }
                                    }
                            );
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            System.out.println("Client started");

            ChannelFuture channel = bootstrap.connect("localhost", 9000).sync();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            AuthService service = new AuthService();

            System.out.println("Welcome, please, make your choice\n" +
                    "If you want log in, please enter LOG\n" +
                    "If you want sign up, please enter REG");
            String choice = reader.readLine();

            if (choice.equals("LOG")) {
                channel.channel().writeAndFlush(service.login());
            } else if (choice.equals("REG")) {
                channel.channel().writeAndFlush(service.signUp());
            } else {
                System.out.println("Incorrect input, please try again later");
                channel.channel().closeFuture().sync();
            }

            TextMessage textMessage = new TextMessage();
            textMessage.setText("New client connected");
            channel.channel().writeAndFlush(textMessage);

            DateMessage dateMessage = new DateMessage(new Date());
            channel.channel().writeAndFlush(dateMessage.getDate());

            DownloadFileRequestMessage message = new DownloadFileRequestMessage();
            message.setPath("C:\\Java\\netty\\bigFile.txt");
            channel.channel().writeAndFlush(message);

            channel.channel().closeFuture().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
