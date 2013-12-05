package com.jackfruit.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public enum NetLayer {
	
	INS;
	
	private static int PORT = 8084;
	
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
			session.setAttribute("ID", count);
			NetService.INS.idSessionMap.put(count, session);
		}
		
		public void sessionClosed(IoSession session) throws Exception {
			int count = (int) session.getAttribute("ID");
			NetService.INS.idSessionMap.remove(count);
		}

		public void messageReceived(IoSession session, Object message) throws Exception {
			// Empty handler
		}
		
	}
	
}
