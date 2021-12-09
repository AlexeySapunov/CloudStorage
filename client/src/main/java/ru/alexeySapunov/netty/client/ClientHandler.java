package ru.alexeySapunov.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.alexeySapunov.netty.common.logInSignUpService.DBClient;
import ru.alexeySapunov.netty.common.message.EndFileDownloadMessage;
import ru.alexeySapunov.netty.common.message.FileMessage;
import ru.alexeySapunov.netty.common.message.Message;
import ru.alexeySapunov.netty.common.message.TextMessage;

import java.io.RandomAccessFile;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    private final Callback onMessageReceivedCallback;

    public ClientHandler(Callback onMessageReceivedCallback) {
        this.onMessageReceivedCallback = onMessageReceivedCallback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if (onMessageReceivedCallback != null) {
            onMessageReceivedCallback.callback(msg);
        }

        if (msg instanceof DBClient) {
            DBClient message = (DBClient) msg;
            System.out.println("Receive message " + ((DBClient) msg).getName());

            String name = message.getName();
            String log = message.getLog();
            String password = message.getPass();

            new DBClient(name, log, password);
        }

        if (msg instanceof TextMessage) {
            System.out.println("Receive message " + ((TextMessage) msg).getText());
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
        cause.printStackTrace();
    }
}
