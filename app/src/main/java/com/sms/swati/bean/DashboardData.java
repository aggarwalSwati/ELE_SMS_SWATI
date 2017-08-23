package com.sms.swati.bean;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class DashboardData {

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	String senderName;
	int msgCount;
}
