package com.sas.sendmail.bean;

import java.sql.Timestamp;

public class SendMailBean {
	private int id;
	private String subject;
	private String receiver;
	private String mail_content;
	private String status;
	boolean is_send = false;
	boolean have_send = false;
	Timestamp process_date;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMail_content() {
		return mail_content;
	}
	public void setMail_content(String mail_content) {
		this.mail_content = mail_content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isIs_send() {
		return is_send;
	}
	public void setIs_send(boolean is_send) {
		this.is_send = is_send;
	}
	public boolean isHave_send() {
		return have_send;
	}
	public void setHave_send(boolean have_send) {
		this.have_send = have_send;
	}
	public Timestamp getProcess_date() {
		return process_date;
	}
	public void setProcess_date(Timestamp process_date) {
		this.process_date = process_date;
	}

	
}
