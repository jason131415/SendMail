package com.sas.cfg;

import java.util.*;

/**
 * 
 * @author Terry
 * 
 */
public class db_config {
	private final static String JDBC_TYPE = "jdbc";
	private final static String DB_CONNECT_TYPE = "db_connect_type";
	private final static String JDBC_JNDI_LOC = "jdbc_jndi_loc";
	private final static String CFG = "db_config";
	private static String[] _jdbcs;
	private static String _db_connect_type;
	private static String _jdbc_jndi_loc;
	private static Properties _rs;

	synchronized static Properties getRS() {
		_rs = _rs == null ? convertToProperties(ResourceBundle.getBundle(CFG)) : _rs;
		return _rs;
	}

	private final static String KEY_USE_2 = "key_use_2";
	private static String _key_use_2 = null;
	private static boolean b_key_use_2 = false;

	public static boolean isKeyUse2() {
		if (_key_use_2 == null) {
			_key_use_2 = getRS().getProperty(KEY_USE_2, "false");
			b_key_use_2 = "true".equals(_key_use_2);
		}
		return b_key_use_2;
	}

	public static boolean isDBJndiLocType() {
		_db_connect_type = (_db_connect_type == null) ? getRS().getProperty(DB_CONNECT_TYPE) : _db_connect_type;
		return JDBC_JNDI_LOC.equals(_db_connect_type);
	}

	public static String getJndiLocInfo() {
		_jdbc_jndi_loc = (_jdbc_jndi_loc == null) ? getRS().getProperty(JDBC_JNDI_LOC) : _jdbc_jndi_loc;
		return _jdbc_jndi_loc;
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

	private synchronized final static String[] getStringList(String key) {
		String pids = getRS().getProperty(key);
		// System.out.println("pids----->" + pids);
		List ls = new ArrayList();
		int last = 0;
		boolean bContinue = true;
		int length = pids.length();
		while (bContinue) {
			int index = pids.indexOf(',', last);
			index = index == -1 ? length : index;
			String s = pids.substring(last, index);
			ls.add(s);
			last = index + 1;
			bContinue = last < length;
		}
		String[] sa = new String[ls.size()];
		ls.toArray(sa);
		return sa;
	}

	public synchronized final static String[] getJDBCInfo() {
		if (_jdbcs == null) {
			_jdbcs = getStringList(JDBC_TYPE);
		}
		return _jdbcs;
	}

}
