package com.jackfruit.model;

import org.apache.mina.core.buffer.IoBuffer;

public class MsgGameServer {
	
	/** sessionId */
	public int sessionId;
	/** 消息内容  */
	public IoBuffer buffer;
	
	public MsgGameServer(int sessionId, IoBuffer buffer) {
		this.sessionId = sessionId;
		this.buffer = buffer;
	}

}
