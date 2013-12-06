package com.jackfruit.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.jackfruit.model.MsgClient;
import com.jackfruit.model.MsgGameServer;
import com.jackfruit.model.MsgSystem;
import com.jackfruit.model.RoutingMember;

public enum NetLayer {
	
	INS;
	
	private static int PORT = 8084;
	private static String SESSION_ID = "ID";
	
	public void start() {
		
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast(
				"codec", new ProtocolCodecFilter(new NetCodecFactory()));
		acceptor.setHandler(new NetIoHandler());
		try {
			acceptor.bind(new InetSocketAddress(PORT));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private static class NetCodecFactory implements ProtocolCodecFactory {

		@Override
		public ProtocolEncoder getEncoder(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ProtocolDecoder getDecoder(IoSession session) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	private static class NetIoHandler extends IoHandlerAdapter {
		
		public void sessionOpened(IoSession session) throws Exception {
			int count = NetService.INS.buildCurrentCount();
			session.setAttribute(SESSION_ID, count);
			NetService.INS.idSessionMap.put(count, session);
		}
		
		public void sessionClosed(IoSession session) throws Exception {
			int count = (int) session.getAttribute(SESSION_ID);
			NetService.INS.idSessionMap.remove(count);
		}

		public void messageReceived(IoSession session, Object message) throws Exception {
			actorEncoder(session, message);
		}
		
	}
	
	public static void actorDecode(Object obj) {
		if(obj instanceof MsgClient) {
			MsgClient msg = (MsgClient)obj;
			for(int sessionId : msg.sessionIdList) {
				IoSession session = NetService.INS.getSession(sessionId);
				if(session == null) {
					continue;
				}
				session.write(msg.buffer);
			}
		} else if(obj instanceof MsgSystem) {
			MsgSystem msg = (MsgSystem)obj;
			IoSession session = NetService.INS.getSession(msg.sessionId);
			if(session != null) {
				NetService.INS.redirectRouting(msg.sessionId, msg.serverMember);
			}
		} 
	}
	
	public static void actorEncoder(IoSession session, Object message) {
		// 路由
		RoutingMember routingMember = NetService.INS.getRouingMember(session);
		if(routingMember == null) {
			return;
		}
		
		// 消息
		int sessionId = (int)session.getAttribute(SESSION_ID);
		IoBuffer buffer = (IoBuffer)message;
		MsgGameServer msgGameServer = new MsgGameServer(sessionId, buffer);
		
	}
	
}
