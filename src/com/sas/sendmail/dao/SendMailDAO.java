package com.sas.sendmail.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.sas.sendmail.bean.SendMailBean;
import com.sas.util.StatementManager;
import com.sas.util.VARSQLFormat;

public class SendMailDAO {
	public List<SendMailBean> querySendMail(Connection conn) throws Exception {
		StringBuilder sb = new StringBuilder();	

		sb.append("  select id, subject, receiver , mail_content , is_send, have_send , process_date ");
		sb.append("  from cidb.cdm.SEND_MAIL ");
		sb.append("  where have_send = 0 and is_send = 1 ");
		
		String strVRSQL = sb.toString();
		PreparedStatement psm = null;
		ResultSet rs = null;
		List<SendMailBean> smList = new ArrayList<SendMailBean>();
		
		try {
			psm = VARSQLFormat.prepareDBStatement(strVRSQL, conn, null);
			rs = psm.executeQuery();
			while (rs.next()) {
				SendMailBean be = new SendMailBean();
				be.setId(rs.getInt("id"));
				be.setSubject(rs.getString("subject"));
				be.setReceiver(rs.getString("receiver"));
				be.setMail_content(rs.getString("mail_content"));
				smList.add(be);
			}		
			return smList;
		} catch (Exception ex) {
			throw ex;
		} finally {
			StatementManager.close(psm, rs);
		}
	}
	
	
	

	public void updateSendMail(int id , Connection conn)  throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("   update cidb.cdm.SEND_MAIL  set is_send = 1 , have_send = 1 where id = ':id' ");
		String strVRSQL = sb.toString();
		PreparedStatement psm = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		try {
			psm = VARSQLFormat.prepareDBStatement(strVRSQL, conn, map);
			psm.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			StatementManager.close(psm, null);
		}
	}
}
