package ru.alexeySapunov.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.alexeySapunov.netty.common.message.*;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ServerChannelInboundHandler extends SimpleChannelInboundHandler<Message> {

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
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws IOException {
        if(msg instanceof TextMessage) {
            TextMessage message = (TextMessage) msg;
            System.out.println("Incoming text message from client: " + message.getText());
        }

        if(msg instanceof DateMessage) {
            DateMessage message = (DateMessage) msg;
            System.out.println("Incoming date message from client: " + message.getDate());
        }

        if (msg instanceof DownloadFileRequestMessage) {
            var message = (DownloadFileRequestMessage) msg;
            try(RandomAccessFile accessFile = new RandomAccessFile(message.getPath(), "r")) {
                final FileMessage fileMessage = new FileMessage();
                byte[] content = new byte[(int) accessFile.length()];
                accessFile.read(content);
                fileMessage.setContent(content);
                ctx.writeAndFlush(fileMessage);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Catch cause" + cause.getMessage());
        ctx.close();
    }
}
