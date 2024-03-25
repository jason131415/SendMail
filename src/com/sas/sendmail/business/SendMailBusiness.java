package com.sas.sendmail.business;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.sas.sendmail.bean.SendMailBean;
import com.sas.sendmail.dao.SendMailDAO;
import com.sas.sendmail.dao.DdimSASDAO;
import com.sas.util.ConnectionProvider;



public class SendMailBusiness {
	static Logger log = Logger.getLogger(SendMailBusiness.class);
	
	public static void main(String args[]){
		
		List<SendMailBean> sendMailList = new ArrayList<SendMailBean>();

		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
					
			//Step1. 撈取send_mail待發送的mail
			sendMailList = new SendMailDAO().querySendMail(conn);
				
			//Step2  發送mail
			if(sendMailList.size() > 0){
				for(SendMailBean be : sendMailList ){
					int id = be.getId();
					String subject = be.getSubject();
					String receiver = "' "+ be.getReceiver().replace(";", "' '") + " '";
					String mailContext = be.getMail_content();
					String [] context = be.getMail_content().split("<br>"); //斷行
					
					try {	
						log.info("id="+id + " subject="+ subject + " receiver=" + receiver + " mailContext=" + mailContext );
						new DdimSASDAO().sendEmail(subject, receiver, context);
						new SendMailDAO().updateSendMail(id, conn);			
					} catch (Exception e) {
						e.printStackTrace();
					}		
				}	
			}		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionProvider.close(conn);
		}
	}
}
