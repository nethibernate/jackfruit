package com.jackfruit.net;

import io.netty.channel.ChannelHandlerContext;

public class NettyClientSession {

	private ChannelHandlerContext ctx;
	
	public NettyClientSession(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
}
