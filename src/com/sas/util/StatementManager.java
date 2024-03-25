package com.sas.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.sql.*;

public class StatementManager {

	final static public void close(Statement sta, ResultSet rs) {
		if (rs != null) {
			synchronized (rs) {
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				rs = null;
			}
		}
		if (sta != null) {
			synchronized (sta) {
				try {
					sta.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				sta = null;
			}
		}

	}

}
