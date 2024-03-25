package com.sas.util;

import java.sql.*;

import javax.naming.*;
import javax.sql.*;

import com.sas.cfg.db_config;

public class ConnectionProvider {
	private static String _jndi_loc;
	private static boolean bDBJndiLocType;
	private static boolean bInitDBType = false;

	final private static DataSource getDataSource() throws Exception {
		_jndi_loc = _jndi_loc == null ? db_config.getJndiLocInfo() : _jndi_loc;
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup(_jndi_loc);
		return ds;
	}

	final public static boolean isJNDI() {
		if (!bInitDBType) {
			bInitDBType = true;
			bDBJndiLocType = db_config.isDBJndiLocType();
		}
		return bDBJndiLocType;
	}

	final public static synchronized void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	final static public Connection getKeyConnection() throws Exception {
		return ConnectionProvider.getConnection();
	}

	final static public Connection getConnection() throws Exception {

		if (!isJNDI()) {
			String[] sa = db_config.getJDBCInfo();
			Class.forName(sa[0]);
			Connection conn = DriverManager.getConnection(sa[1], sa[2], sa[3]);

			return conn;
		} else {
			Connection conn = getDataSource().getConnection();
			return conn;
		}
	}

}
