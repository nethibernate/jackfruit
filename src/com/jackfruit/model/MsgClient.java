package com.jackfruit.model;

import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

public class MsgClient {
	
	/** sessionId */
	public List<Integer> sessionIdList;
	/** 消息内容 */
	public IoBuffer buffer;
	
}
