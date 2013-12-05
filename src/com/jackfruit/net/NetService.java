package com.jackfruit.net;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.session.IoSession;

import com.jackfruit.model.RoutingMember;
import com.jackfruit.model.ServerMember;

public enum NetService {
	
	INS;
	
	/** 网关-路由的映射 */
	public Map<ServerMember, RoutingMember> serverRoutingMap = new HashMap<ServerMember, RoutingMember>();
	/** 玩家-下一级网关的映射 */
	public Map<IoSession, ServerMember> sessionServerMap = new ConcurrentHashMap<IoSession, ServerMember>();
	/** ID-玩家的映射 */
	public Map<Integer, IoSession> idSessionMap = new ConcurrentHashMap<Integer, IoSession>();
	
	
	
	/** 计数 */
	public AtomicInteger sessionCount = new AtomicInteger(0); 
	
	
	public int buildCurrentCount() {
		do {
			sessionCount.incrementAndGet();
			if(sessionCount.get() <= 0) {
				sessionCount.set(1);
			}
		} while(idSessionMap.containsKey(sessionCount.get()));
		
		return sessionCount.get();
	}
	

}
