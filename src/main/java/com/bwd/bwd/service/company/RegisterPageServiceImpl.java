package com.bwd.bwd.service.company;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bwd.bwd.db.DBOperation;
import com.bwd.bwd.db.DBSearch;
import com.bwd.bwd.model.auth.AccountRequest;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.RegTextResponse;
import com.bwd.bwd.response.StatusResponse;

@Service
public class RegisterPageServiceImpl implements RegisterPageService{
	
	public List<RegTextResponse> getPageReport(AccountRequest userAccount)
	{
		DBOperation dbop = new DBOperation();
		List<RegTextResponse> listJPR = new ArrayList<RegTextResponse>();
		String [][]Data;

		String sqlQuery = "select logo, welcome from landingpage where code = '" + userAccount.getCode() + "' and landingid = " + userAccount.getLandingid();
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		Data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			RegTextResponse rtr = new RegTextResponse();
			listJPR.add(rtr.getObject(Data[i]));
		}

		return listJPR;
	}
	
	/*
	 * public StatusResponse getEmail(AccountRequest userAccount) { StatusResponse
	 * sr = new StatusResponse();
	 * 
	 * DBOperation dbop = new DBOperation();
	 * 
	 * String sqlQuery =
	 * "SELECT email from user_email_tbl where = "+userAccount.getEmail();
	 * 
	 * DBSearch dbs = new DBSearch(); String accountIdData =
	 * dbs.getUserAccountId(accountIdQuery,"uid"); Long profileAccountId =
	 * Long.parseLong(accountIdData);
	 * 
	 * // Use a prepared statement to avoid SQL injection String sqlUpdate =
	 * "UPDATE jobsmith_report_tbl a " +
	 * "INNER JOIN user_accounts b ON a.useraccountid = b.useraccountid " +
	 * "INNER JOIN jobsmith_user_profile_tbl c ON b.useraccountid = c.useraccountid SET report_status = '"
	 * +
	 * reportStatus+"', status_date=current_timestamp  WHERE a.jobsmith_reportid = "
	 * + jobsmithReportId+" AND c.companyid ="+companyid+" AND c.isAccess = 1";
	 * System.out.println(sqlUpdate); dbop.updateRecord(sqlUpdate);
	 * 
	 * sr.setStatusCode(dbop.getStatusCode());
	 * 
	 * if(dbop.getStatusCode() == 1) { if(dbop.getCountUpdated() > 0) {
	 * sr.setValid(true);
	 * sr.setMessage("No. of records Updated : "+dbop.getCountUpdated()); } else {
	 * sr.setValid(false); sr.setMessage("No record Updated"); } } return sr; }
	 */
	
}
