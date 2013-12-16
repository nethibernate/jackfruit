package com.jackfruit.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class NettyClientIoHandler extends ChannelInboundHandlerAdapter {
	
	private static AttributeKey<NettyClientSession> CLIENT_SESSION = 
			new AttributeKey<NettyClientSession>("ClientSession");
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyClientSession session = new NettyClientSession(ctx);
		ctx.attr(CLIENT_SESSION).set(session);
	}
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettyClientSession session = ctx.attr(CLIENT_SESSION).get();
		if(session == null) {
			return;
		}
		
		ctx.attr(CLIENT_SESSION).set(null);
		// TODO 注册session close任务
    }
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO 
	}

}
