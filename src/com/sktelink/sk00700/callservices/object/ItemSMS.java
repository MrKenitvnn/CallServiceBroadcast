package com.sktelink.sk00700.callservices.object;

public class ItemSMS {

	private String smsId = "";
	private String smsType = "";
	private String smsMessage = "";
	private String smsProtocol = "";
	private String smsPhoneNumber = "";
	private String smsNewNumber = "";

	public String getSmsId() {
		return smsId;
	}

	public ItemSMS setSmsId(String smsId) {
		this.smsId = smsId;
		return this;
	}

	public String getSmsType() {
		return smsType;
	}

	public ItemSMS setSmsType(String smsType) {
		this.smsType = smsType;
		return this;
	}

	public String getSmsMessage() {
		return smsMessage;
	}

	public ItemSMS setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
		return this;
	}

	public String getSmsProtocol() {
		return smsProtocol;
	}

	public ItemSMS setSmsProtocol(String smsProtocol) {
		this.smsProtocol = smsProtocol;
		return this;
	}

	public String getSmsPhoneNumber() {
		return smsPhoneNumber;
	}

	public ItemSMS setSmsPhoneNumber(String smsPhoneNumber) {
		this.smsPhoneNumber = smsPhoneNumber;
		return this;
	}

	public String getSmsNewNumber() {
		return smsNewNumber;
	}

	public ItemSMS setSmsNewNumber(String smsNewNumber) {
		this.smsNewNumber = smsNewNumber;
		return this;
	}

}
