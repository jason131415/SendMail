package com.sas.cfg;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Enumeration;

public class DDIM_config {
	private final static String CFG = "ddim_config";
	private static Properties _rs;

	private synchronized static Properties getRS() {
		_rs = _rs == null ? convertToProperties(ResourceBundle.getBundle(CFG)) : _rs;
		return _rs;
	}

	private static Properties convertToProperties(ResourceBundle rb) {
		Enumeration em = rb.getKeys();
		Properties p = new Properties();
		while (em.hasMoreElements()) {
			String key = (String) em.nextElement();
			String value = rb.getString(key);
			p.setProperty(key, value);
		}
		return p;
	}

	
	
	private final static String SMS_PATH = "sms_path";
	private static String _sms_path = null;
	public static String getSMSPath() {
		_sms_path = _sms_path == null ? getRS().getProperty(SMS_PATH) : _sms_path;
		return _sms_path;
	}

		
	
	private final static String SMS_PATH_FTP = "sms_path_ftp";
	private static String _sms_path_ftp = null;
	public static String getSMSPathFtp() {
		_sms_path_ftp = _sms_path_ftp == null ? getRS().getProperty(SMS_PATH_FTP) : _sms_path_ftp;
		return _sms_path_ftp;
	}
	
	private final static String SMS_SEED_FTP_SERVER = "sms_seed_ftp_server";
	private static String _sms_seed_ftp_server = null;
	public static String getSMSSeedFtpServer() {
		_sms_seed_ftp_server = _sms_seed_ftp_server == null ? getRS().getProperty(SMS_SEED_FTP_SERVER) : _sms_seed_ftp_server;
		return _sms_seed_ftp_server;
	}
	
	private final static String SMS_SEED_FTP_PORT = "sms_seed_ftp_port";
	private static String _sms_seed_ftp_port = null;
	public static String getSMSSeedFtpPort() {
		_sms_seed_ftp_port = _sms_seed_ftp_port == null ? getRS().getProperty(SMS_SEED_FTP_PORT) : _sms_seed_ftp_port;
		return _sms_seed_ftp_port;
	}
	
	private final static String SMS_SEED_FTP_USERNAME = "sms_seed_ftp_username";
	private static String _sms_seed_ftp_username = null;
	public static String getSMSSeedFtpUserName() {
		_sms_seed_ftp_username = _sms_seed_ftp_username == null ? getRS().getProperty(SMS_SEED_FTP_USERNAME) : _sms_seed_ftp_username;
		return _sms_seed_ftp_username;
	}
	
	private final static String SMS_SEED_FTP_PASSWORD = "sms_seed_ftp_password";
	private static String _sms_seed_ftp_password = null;
	public static String getSMSSeedFtpPassword() {
		_sms_seed_ftp_password = _sms_seed_ftp_password == null ? getRS().getProperty(SMS_SEED_FTP_PASSWORD) : _sms_seed_ftp_password;
		return _sms_seed_ftp_password;
	}
	
}
