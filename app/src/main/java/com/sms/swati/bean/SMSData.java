package com.sms.swati.bean;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class SMSData {

	// Number from which the sms was send
	private String sender_id;
	private String transactionType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String id;

	// SMS text body
	private String body;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	private String service;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
//		String mydate = java.text.DateFormat.getDateTimeInstance().format(date);
		this.date = date;
	}

	public void setSender_id(String sender_id) {

		this.sender_id = sender_id;
	}

	String date;

	public SMSData(String body, String sender_id) {
		this.body = body;
		this.sender_id = sender_id;
		// id = _id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSender_id() {
		return sender_id;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;

	}
}
