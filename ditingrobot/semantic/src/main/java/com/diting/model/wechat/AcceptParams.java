package com.diting.model.wechat;

public class AcceptParams {
	private String xml;
	private String nonce;
	private String timeStamp;
	private String msgSignature;
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getMsgSignature() {
		return msgSignature;
	}

	public void setMsgSignature(String msgSignature) {
		this.msgSignature = msgSignature;
	}


}
