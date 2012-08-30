package info.realjin.newsintime.domain;

import java.util.HashMap;
import java.util.Map;

public class AppMessage {
	private Map<String, Object> msgMap;
	public static final String MSG_CILACT_CIACT_ITEM = "CILACT_CIACT_ITEM";
	public static final String MSG_CIACT_CILACT_ITEM = "CIACT_CILACT_ITEM";

	public AppMessage() {
		msgMap = new HashMap<String, Object>();
	}

	public void putMessage(String name, Object msg) {
		msgMap.put(name, msg);
	}

	public Object getMessage(String name) {
		return msgMap.get(name);
	}

	public Map<String, Object> getMsgMap() {
		return msgMap;
	}

	public void setMsgMap(Map<String, Object> msgMap) {
		this.msgMap = msgMap;
	}
}
