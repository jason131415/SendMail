package com.sas.sendmail.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sas.iom.SAS.ILanguageService;
import com.sas.iom.SAS.IWorkspace;
import com.sas.iom.SAS.IWorkspaceHelper;
import com.sas.rio.MVAConnection;
import com.sas.services.connection.BridgeServer;
import com.sas.services.connection.ConnectionFactoryConfiguration;
import com.sas.services.connection.ConnectionFactoryInterface;
import com.sas.services.connection.ConnectionFactoryManager;
import com.sas.services.connection.ConnectionInterface;
import com.sas.services.connection.ManualConnectionFactoryConfiguration;
import com.sas.services.connection.Server;

public class DdimSASDAO {
	static Logger log = Logger.getLogger(DdimSASDAO.class);

	// ??ç·šSAS??ƒæ•¸è¨­å??
	String classID = Server.CLSID_SAS;

	String host = "192.168.80.19";
	int port = 8591;
	String userName = "sasdemo";
	String password = "ddim@123";
  
	
	/*
	String host = "sas-cm01p";
	int port = 8591;
	String userName = "sasadm@saspw";
	String password = "{sas002}D1E036442AE28CA10146074F4809ABE1";
*/
	
	/**
	 * 
	 * @param mailList
	 * @param messageList
	 * @return
	 * @throws Exception
	 */
	public boolean sendEmail(String subject, String email, String [] messageList) throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(" %macro etls_sendEmail();   ");
		sb.append(" filename sendMail email ( ").append(email).append(" ) subject=' ").append(subject)
				.append(" '  ;   ");
		sb.append("   %local etls_syntaxcheck;   ");
		sb.append("     %let etls_syntaxcheck = %sysfunc(getoption(syntaxcheck));   ");
		sb.append("     options nosyntaxcheck;  ");
		sb.append("     data _null_;   ");
		sb.append("         file sendMail;   ");

		for (String message : messageList) {
			sb.append("put '  ").append(message).append(" '; ");
		}

		sb.append(" 		;  ");
		sb.append("      run;   ");
		sb.append("      options &etls_syntaxcheck;   ");
		sb.append(" %mend etls_sendEmail;   ");

		sb.append("  %etls_sendEmail();   ");

		//log.info(sb.toString());
		ConnectionInterface cx = null;
		IWorkspace sasWorkspace = null;
		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		try {
			Server server = new BridgeServer(classID, host, port);
			ConnectionFactoryConfiguration cxfConfig = new ManualConnectionFactoryConfiguration(server);
			ConnectionFactoryManager cxfManager = new ConnectionFactoryManager();
			ConnectionFactoryInterface cxf = cxfManager.getFactory(cxfConfig);
			cx = cxf.getConnection(userName, password);
			sasWorkspace = IWorkspaceHelper.narrow(cx.getObject());
			ILanguageService sasLanguage = sasWorkspace.LanguageService();
			sasLanguage.Submit(sb.toString());
			sasLanguage.FlushList(Integer.MAX_VALUE);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (state != null) {
					state.close();
				}
				if (conn != null) {
					conn.close();
				}
				if (sasWorkspace != null) {
					sasWorkspace.Close();
				}
				if (cx != null) {
					cx.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		return false;
	}
}
