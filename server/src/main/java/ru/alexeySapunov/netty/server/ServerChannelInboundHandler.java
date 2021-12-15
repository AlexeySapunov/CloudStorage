package ru.alexeySapunov.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.alexeySapunov.netty.common.logInSignUpService.AuthService;
import ru.alexeySapunov.netty.common.logInSignUpService.DataBase;
import ru.alexeySapunov.netty.common.logInSignUpService.LoginClient;
import ru.alexeySapunov.netty.common.logInSignUpService.SignUpClient;
import ru.alexeySapunov.netty.common.message.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.concurrent.Executor;

public class ServerChannelInboundHandler extends SimpleChannelInboundHandler<Message> {

    private static final int BUFFER_SIZE = 1024 * 64;

    private final Executor executor;

    DataBase dataBase = new DataBase();

    public ServerChannelInboundHandler(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("Channel is registered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        System.out.println("Channel is unregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Channel is active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Channel is inactive");
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws SQLException, InterruptedException {
        if(msg instanceof LoginClient){
            var message = (LoginClient) msg;

            if(message.getName().equals(dataBase.getClients(message))) {
                ctx.writeAndFlush(msg);
                System.out.println(message.getName() + " successfully logged in");
            } else {
                System.out.println("Login failed!");
                AuthService service = new AuthService();
                ctx.writeAndFlush(service).sync();
                ctx.writeAndFlush(msg);
            }
        }

        if (msg instanceof SignUpClient){
            AuthService service = new AuthService();
            var message = (SignUpClient) msg;
            ctx.writeAndFlush(msg);
            dataBase.getNewClients(message);
            ctx.writeAndFlush(service).sync();
        }

        if (msg instanceof TextMessage) {
            TextMessage message = (TextMessage) msg;
            System.out.println("Incoming text message from client: " + message.getText());
            ctx.writeAndFlush(msg);
        }

        if (msg instanceof DateMessage) {
            DateMessage message = (DateMessage) msg;
            System.out.println("Incoming date message from client: " + message.getDate());
            ctx.writeAndFlush(msg);
        }

        if (msg instanceof DownloadFileRequestMessage) {
            executor.execute(() -> {
                var message = (DownloadFileRequestMessage) msg;
                try (var accessFile = new RandomAccessFile(message.getPath(), "r")) {
                    long fileLength = accessFile.length();
                    do {
                        var position = accessFile.getFilePointer();

                        final long availableBytes = fileLength - position;
                        byte[] bytes;

                        if (availableBytes >= BUFFER_SIZE) {
                            bytes = new byte[BUFFER_SIZE];
                        } else {
                            bytes = new byte[(int) availableBytes];
                        }

                        accessFile.read(bytes);

                        final FileMessage fileMessage = new FileMessage();
                        fileMessage.setContent(bytes);
                        fileMessage.setStartPosition(position);

                        ctx.writeAndFlush(fileMessage).sync();

                    } while (accessFile.getFilePointer() < fileLength);

                    ctx.writeAndFlush(new EndFileDownloadMessage());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Catch cause" + cause.getMessage());
        ctx.close();
    }
}
