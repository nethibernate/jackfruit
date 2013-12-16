package com.jackfruit.net;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NettyClientDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		while(true) {
			if(in.readableBytes() < 4) {
				break;
			}
			in.markReaderIndex();
			int len = in.readInt();
			if(len > in.readableBytes()) {
				in.resetReaderIndex();
				break;
			}
			byte[] decoded = new byte[len];
			in.readBytes(decoded);
			out.add(decoded);
		}
	}

}
