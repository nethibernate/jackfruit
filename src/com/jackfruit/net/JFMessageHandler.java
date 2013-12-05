package com.jackfruit.net;

import com.jackfruit.protocol.message.JFInboundMessage;
import com.jackfruit.protocol.message.JFMessageType;
import com.jackfruit.router.IRouter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * 
 * @author nethibernate
 *
 */
public class JFMessageHandler extends ChannelInboundHandlerAdapter {
	
	/** The message Router */
	private IRouter router = null;
	
	public JFMessageHandler(IRouter router){
		this.router = router;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx){
		
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		JFInboundMessage message = (JFInboundMessage) msg;
		// check the message type whether it is validate
		if(message.getType() == JFMessageType.HAND_SHAKE){
			//register the current client
			
			return;
		}
		
		// check the current client whether it has registered here or not
		
		// parse the message route
		
		
		// route the message
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		
	}
}
