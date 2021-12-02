package ru.alexeySapunov.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.alexeySapunov.netty.common.dataBase.DBAuthService;
import ru.alexeySapunov.netty.common.handler.JsonDecoder;
import ru.alexeySapunov.netty.common.handler.JsonEncoder;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws InterruptedException, SQLException {
        new Server().run();
    }

    private void run() throws InterruptedException, SQLException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workersGroup = new NioEventLoopGroup();
        final ExecutorService threadPool = Executors.newCachedThreadPool();
        final DBAuthService authService = new DBAuthService();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workersGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 3, 0, 3),
                                    new LengthFieldPrepender(3),
                                    new JsonDecoder(),
                                    new JsonEncoder(),
                                    new ServerChannelInboundHandler(threadPool)
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            final Channel channel = serverBootstrap.bind(9000).sync().channel();
            System.out.println("Server started");
            authService.connectBase();
            System.out.println("DataBase connected");
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workersGroup.shutdownGracefully();
            threadPool.shutdownNow();
            authService.disconnectBase();
        }
    }
}
