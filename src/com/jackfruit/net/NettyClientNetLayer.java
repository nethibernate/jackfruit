package com.jackfruit.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NettyClientNetLayer {
	
	private int port = 8084;
	
	
	public void start() throws IOException {
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("decoder", new NettyClientDecoder());
				ch.pipeline().addLast("encoder", new NettyClientEncoder());
				ch.pipeline().addLast(new NettyClientIoHandler());
			}
		});
		InetSocketAddress address = new InetSocketAddress(port);
		try {
			bootstrap.bind(address).sync();
		} catch (Exception e) {
			throw new RuntimeException("NettyClientNetLayer start fail", e);
		}
	}

}
