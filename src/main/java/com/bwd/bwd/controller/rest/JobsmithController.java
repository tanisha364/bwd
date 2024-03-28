package com.bwd.bwd.controller.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.controller.auth.UserAuthController;
import com.bwd.bwd.db.DBOperation;
import com.bwd.bwd.db.DBSearch;
import com.bwd.bwd.db.DBUpdate;
import com.bwd.bwd.model.Category;
import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.model.jobsmith.Critical;
import com.bwd.bwd.model.jobsmith.Important;
import com.bwd.bwd.model.jobsmith.JobSearchResult;
import com.bwd.bwd.model.jobsmith.JobsmithCapabilities;
import com.bwd.bwd.model.jobsmith.JobsmithReport;
import com.bwd.bwd.model.jobsmith.JobsmithReportCapability;
import com.bwd.bwd.model.jobsmith.Nicetohave;
import com.bwd.bwd.model.jobsmith.Reports;
import com.bwd.bwd.repository.CategoryRepo;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.repository.jobsmith.JobsmithCapabilityRepo;
import com.bwd.bwd.repository.jobsmith.JobsmithReportCapabilityRepo;
import com.bwd.bwd.repository.jobsmith.JobsmithReportRepo;
import com.bwd.bwd.request.JobsmithReportRequest;
import com.bwd.bwd.request.JobsmithReportRequestEdit;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.AccessPermissionResponse;
import com.bwd.bwd.response.CapabilityInfoResponse;
import com.bwd.bwd.response.CapabilityResponse;
import com.bwd.bwd.response.CategoryInfoResponse;
import com.bwd.bwd.response.CategoryResponse;
import com.bwd.bwd.response.EditJobResponse;
import com.bwd.bwd.response.Editjobreport;
import com.bwd.bwd.response.JobTitleResponse;
import com.bwd.bwd.response.Jobedit;
import com.bwd.bwd.response.JobsmithPremadeReport;
import com.bwd.bwd.response.JobsmithPremadeReportResponse;
import com.bwd.bwd.response.PermissionResponse;
import com.bwd.bwd.response.PreMadeReport;
import com.bwd.bwd.response.PreMadeReportResponse;
import com.bwd.bwd.response.ReportInfo;
import com.bwd.bwd.response.ReportInfoResponse;
import com.bwd.bwd.response.ReportResponse;
import com.bwd.bwd.response.ResponseEditJob;
import com.bwd.bwd.response.ResponseJobTitle;
import com.bwd.bwd.response.ResponseJobsmithPremadeReport;
import com.bwd.bwd.response.ResponsePermission;
import com.bwd.bwd.response.ResponsePreMadeReport;
import com.bwd.bwd.response.ResponseSaveJobsmithReports;
import com.bwd.bwd.response.ResponseSavedJobReport;
import com.bwd.bwd.response.ResponseSavedJobReportExpansion;
import com.bwd.bwd.response.SavedJobReport;
import com.bwd.bwd.response.SavedJobReportResponse;
import com.bwd.bwd.response.SavedJobReportsExpansion;
import com.bwd.bwd.response.SavedJobReportsExpansionResponse;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.response.UserDataResponse;
import com.bwd.bwd.response.UserInfo;
import com.bwd.bwd.response.UserInfoResponse;
import com.bwd.bwd.response.actionPermi;
import com.bwd.bwd.serviceimpl.JwtUserToken;
import jakarta.servlet.http.HttpServletRequest;


@CrossOrigin("*")
@RequestMapping(path = "/rest/jobsmith", produces = "application/json")
@RestController
public class JobsmithController {

	@Autowired
	UserAuthController auc;		

	@Autowired 
	UserAuthController uac;

	@Autowired
	UserAccountsAuthRepo uaar;	

	@Autowired
	CategoryRepo cr;	

	@Autowired
	JobsmithReportRepo jrr;

	@Autowired
	JobsmithReportCapabilityRepo jrcr;	

	@Autowired
	JobsmithCapabilityRepo jcaprepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;		

	@PostMapping("/categories/{type}")
	public ResponseEntity<CategoryInfoResponse> getCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int type,@RequestBody UserData data)
	{
		ResponseEntity<CategoryInfoResponse> entity;
		HttpHeaders headers = new HttpHeaders();		

		CategoryInfoResponse catinforesp = new CategoryInfoResponse();
		CategoryResponse catresp = new CategoryResponse();   
		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);


		List<Category> list = null;

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());

		if(validToken)
		{	
			if(validAccessToken)
			{
				try {
					System.out.println(data.getUseraccountid());
					uaa = uaar.getReferenceByUserid(data.getUserid());

					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());

					udr.setUserinfo(ui);
					uir.setStatus(sr);
					uir.setData(udr);

					sr.setValid(true);
					sr.setStatusCode(1);
					sr.setMessage("Category List with Authentic Token ");
					list = cr.findByCategoryType(type);

					catresp.setCategories(list);

					catinforesp.setData(catresp);
					catinforesp.setStatus(sr);

					entity = new ResponseEntity<>(catinforesp, headers, HttpStatus.OK);	
				} catch (Exception ex) {				
					System.out.println(ex.getMessage());

					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					list = null;				

					catresp.setCategories(list);

					catinforesp.setData(catresp);
					catinforesp.setStatus(sr);

					entity = new ResponseEntity<>(catinforesp, headers, HttpStatus.UNAUTHORIZED);	
				}
			}else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Access Token Expired");

				catinforesp.setStatus(sr);

				entity = new ResponseEntity<>(catinforesp, headers, HttpStatus.UNAUTHORIZED);
			} 		
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			catinforesp.setStatus(sr);

			entity = new ResponseEntity<>(catinforesp, headers, HttpStatus.UNAUTHORIZED);	
			return entity;
		}

		return entity;
	} 

	@PostMapping("/capabilities")
	public ResponseEntity<CapabilityInfoResponse> getCapability(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data)
	{
		ResponseEntity<CapabilityInfoResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		CapabilityInfoResponse catInfoResp = new CapabilityInfoResponse();
		CapabilityResponse catresp = new CapabilityResponse();   
		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);


		List<JobsmithCapabilities> list = null;
		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());

		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					uaa = uaar.getReferenceByUserid(data.getUserid());
					System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUserid());
					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());

					udr.setUserinfo(ui);
					uir.setStatus(sr);
					uir.setData(udr);

					try
					{
						int categoryid = data.getCategoryId();
						list = jcaprepo.findByRkaid(categoryid);		    		

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Category List with Authentic Token ");		

						catresp.setCapbilities(list);
						catInfoResp.setStatus(sr);
						catInfoResp.setData(catresp);

						entity = new ResponseEntity<>(catInfoResp, headers, HttpStatus.OK);				
					}catch(Exception e)
					{
						e.printStackTrace();
						System.out.println(e.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Fetch Error");
						list = null;				

						catresp.setCapbilities(list);
						catInfoResp.setStatus(sr);
						catInfoResp.setData(catresp);	  

						entity = new ResponseEntity<>(catInfoResp, headers, HttpStatus.UNAUTHORIZED);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					list = null;				

					catresp.setCapbilities(list);
					catInfoResp.setStatus(sr);
					catInfoResp.setData(catresp);	  

					entity = new ResponseEntity<>(catInfoResp, headers, HttpStatus.UNAUTHORIZED);
				}
			}
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				list = null;				

				catresp.setCapbilities(list);
				catInfoResp.setStatus(sr);
				catInfoResp.setData(catresp);	  

				entity = new ResponseEntity<>(catInfoResp, headers, HttpStatus.UNAUTHORIZED);			
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			list = null;				

			catresp.setCapbilities(list);
			catInfoResp.setStatus(sr);
			catInfoResp.setData(catresp);	  

			entity = new ResponseEntity<>(catInfoResp, headers, HttpStatus.UNAUTHORIZED);	
			return entity;
		}    	
		return entity;
	} 	


	public boolean checkToken(String authorizationHeader)
	{
		boolean validToken = false;

		TokenResponse tr = new TokenResponse();
		ResponseEntity<TokenResponse> entityToken = null;

		entityToken = auc.validateBearerToken(authorizationHeader);

		tr = entityToken.getBody();		
		StatusResponse srToken = tr.getStatus();		
		validToken = srToken.isValid();

		return validToken;
	}

	@PostMapping("/premadereports")
	public ResponseEntity<ReportInfoResponse> getPreMadeReports(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data)
	{
		ResponseEntity<ReportInfoResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		ReportInfoResponse reportInfoResp = new ReportInfoResponse();
		ReportResponse reportResp = new ReportResponse();   
		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);		

		List<Reports> list = null;
		List<ReportInfo> listR = null;


		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());

		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					System.out.println(data.getUseraccountid());
					uaa = uaar.getReferenceByUserid(data.getUserid());

					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());

					udr.setUserinfo(ui);
					uir.setStatus(sr);
					uir.setData(udr); 

					try
					{
						DBSearch dbs = new DBSearch();
						int categoryid = data.getCategoryId();
						System.out.println(" ********************* "+categoryid);
						list = dbs.findByCategoryType(categoryid);     	
						listR = getResponseList(list,categoryid);

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Category List with Authentic Token ");	

						reportResp.setJobreports(listR); 
						reportInfoResp.setStatus(sr);
						reportInfoResp.setData(reportResp);

						entity = new ResponseEntity<>(reportInfoResp, headers, HttpStatus.OK);				
					}catch(Exception e)
					{
						e.printStackTrace();
						System.out.println(e.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Fetch Error");

						list = null;	

						reportResp.setJobreports(listR); 
						reportInfoResp.setStatus(sr);
						reportInfoResp.setData(reportResp);

						entity = new ResponseEntity<>(reportInfoResp, headers, HttpStatus.UNAUTHORIZED);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					list = null;

					reportResp.setJobreports(listR); 
					reportInfoResp.setStatus(sr);
					reportInfoResp.setData(reportResp);

					entity = new ResponseEntity<>(reportInfoResp, headers, HttpStatus.UNAUTHORIZED);
				}
			}
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				list = null;	

				reportResp.setJobreports(listR); 
				reportInfoResp.setStatus(sr);
				reportInfoResp.setData(reportResp);

				entity = new ResponseEntity<>(reportInfoResp, headers, HttpStatus.UNAUTHORIZED);			
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			list = null;

			reportResp.setJobreports(listR); 
			reportInfoResp.setStatus(sr);
			reportInfoResp.setData(reportResp);

			entity = new ResponseEntity<>(reportInfoResp, headers, HttpStatus.UNAUTHORIZED);
		} 

		return entity;
	}	



	public List<ReportInfo> getResponseList(List<Reports> rr, int categoryid)
	{
		List<ReportInfo> riList = new ArrayList<ReportInfo>();
		List<Reports> rList = rr;		

		Reports r = null;
		for (int i = 0; i < rList.size(); i++) 
		{
			r = rList.get(i);
			ReportInfo ri = new ReportInfo();
			ri.setCategoryid(categoryid);
			ri.setRep_repor_name(r.getRep_ReportName());
			ri.setRep_report_description(r.getRep_ReportDescription());
			ri.setRep_report_id(r.getRep_ReportId());
			ri.setRep_report_type(r.getRep_ReportType());
			riList.add(ri);
		}		
		return riList;
	}
	

	@PostMapping("/savejobsmithreports")
	public  ResponseEntity<ResponseSaveJobsmithReports> saveJobsmithReports(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody JobsmithReportRequest data, HttpServletRequest request){
		String message = "";

		ResponseEntity<ResponseSaveJobsmithReports> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponseSaveJobsmithReports rsjr = new ResponseSaveJobsmithReports();
		StatusResponse sr = new StatusResponse();

		JobsmithReport jr = new JobsmithReport();			

		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());

		int companyid = data.getCompanyid()	;
		
		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					System.out.println(data.getUseraccountid());
					uaa = uaar.getReferenceByUserid(data.getUserid());  
					System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUserid());
					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());   
					
					String accountIdQuery = "SELECT ua.useraccountid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = ?";
					List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, data.getUserid());	         	
					Long useraccountId = (Long) accountIdData.get(0).get("useraccountid");
					int useraccountIdInt = useraccountId.intValue();
					
					data.setUseraccountid(useraccountIdInt);
					
					// Check permission
					String permissionQuery = "SELECT jat.jobsmith_actionId, jat.jobsmith_functional_endpoint " +
							"FROM jobsmith_user_profile_tbl jup " +
							"JOIN jobsmith_permission_tbl jpt ON jup.jobsmith_profileId = jpt.jobsmith_profileId " +
							"JOIN jobsmith_action_tbl jat ON jpt.jobsmith_actionId = jat.jobsmith_actionId " +
							"JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid " +
							"WHERE ua.userid = ? AND jup.companyid = " + companyid+" AND jpt.IsPermission = 1 AND jup.isAccess = 1";
					List<Map<String, Object>> permissionData = jdbcTemplate.queryForList(permissionQuery, data.getUserid());

					// Get the endpoint being hit
					String endpoint = request.getRequestURI(); // Adjust this based on your endpoint structure

					boolean hasPermission = false;
					for (Map<String, Object> row : permissionData) {
						String functionalEndpoint = (String) row.get("jobsmith_functional_endpoint");
						if (functionalEndpoint.equals(endpoint)) {
							hasPermission = true;
							break;
						}
					}

					if (hasPermission) {
						jrr.save(jr.createJobsmithReport(data));
						jrr.flush();
						int id = jr.getJobsmith_reportid();

						ArrayList<Critical> ar = data.getCritical();		
						Iterator itr = ar.iterator();
						System.out.println("Size of Critical : "+ar.size());
						while (itr.hasNext())
						{   JobsmithReportCapability jrc = new JobsmithReportCapability();    	
						Critical ob = (Critical) itr.next();        	
						jrcr.save(jrc.createJobsmithReportCapability(id,Integer.parseInt(ob.getCapabilityid()),ob.getWeightage(),Integer.parseInt(ob.getSequence())));
						jrcr.flush();
						}

						ArrayList<Important> ari = data.getImportant();		
						Iterator itri = ari.iterator();
						System.out.println("Size of Critical : "+ari.size());
						while (itri.hasNext())
						{   JobsmithReportCapability jrc = new JobsmithReportCapability();    	
						Important ob = (Important) itri.next();        	
						jrcr.save(jrc.createJobsmithReportCapability(id,Integer.parseInt(ob.getCapabilityid()),ob.getWeightage(),Integer.parseInt(ob.getSequence())));
						jrcr.flush();
						}  

						ArrayList<Nicetohave> arn = data.getNicetohave();		
						Iterator itrn = arn.iterator();
						System.out.println("Size of Critical : "+ar.size());
						while (itrn.hasNext())
						{   JobsmithReportCapability jrc = new JobsmithReportCapability();    	
						Nicetohave ob = (Nicetohave) itrn.next();        	
						jrcr.save(jrc.createJobsmithReportCapability(id,Integer.parseInt(ob.getCapabilityid()),ob.getWeightage(),Integer.parseInt(ob.getSequence())));
						jrcr.flush();
						}  

						message = "Job report saved successfully = "+id;    

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage(message);   

						rsjr.setStatus(sr);
						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);	
					}
					else {
						// User doesn't have permission
						sr.setValid(false);
						sr.setStatusCode(11);
						sr.setMessage("User doesn't have permission to access this resource");      
						rsjr.setStatus(sr);
						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.FORBIDDEN);
					}

				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rsjr.setStatus(sr);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.NOT_FOUND);
				}  
			}
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rsjr.setStatus(sr);		
				entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);			
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			rsjr.setStatus(sr);
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
		} 

		return entity;
	} 

	@PostMapping("/getjobsmithreports")
	public ResponseEntity<ResponseJobsmithPremadeReport> getjobsmithreports(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData userData)
	{
		ResponseEntity<ResponseJobsmithPremadeReport> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponseJobsmithPremadeReport rjpr = new ResponseJobsmithPremadeReport();
		StatusResponse sr = new StatusResponse();    	
		JobsmithPremadeReportResponse jprr = new JobsmithPremadeReportResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(userData.getUserid());

		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					try
					{ 
						uaa = uaar.getReferenceByUserid(userData.getUserid());   
						System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+userData.getUserid());
						ui.setFirstname(uaa.getFirstname());
						ui.setLastname(uaa.getLastname());
						ui.setStatus(uaa.getStatus());
						ui.setStatusdate(uaa.getStatusdate());   

						List<JobsmithPremadeReport> jpr =  getData(userData.getReportId());

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Jobsmith Premade Report with Authentic Token ");     		    	

						jprr.setJobsmithPremadeReport(jpr);

						rjpr.setStatus(sr);
						rjpr.setData(jprr);

						entity = new ResponseEntity<>(rjpr, headers, HttpStatus.OK);	    				
					}catch(NullPointerException npex)
					{
						npex.printStackTrace();
						System.out.println(npex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
						rjpr.setData(null);
						rjpr.setStatus(sr);
						entity = new ResponseEntity<>(rjpr, headers, HttpStatus.UNAUTHORIZED);    				

					}catch(Exception ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or Unauthentic User");
						rjpr.setData(null);
						rjpr.setStatus(sr);
						entity = new ResponseEntity<>(rjpr, headers, HttpStatus.UNAUTHORIZED);
					}  		
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rjpr.setData(null);
					rjpr.setStatus(sr);
					entity = new ResponseEntity<>(rjpr, headers, HttpStatus.UNAUTHORIZED);
				}
			}    	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rjpr.setData(null);
				rjpr.setStatus(sr);		
				entity = new ResponseEntity<>(rjpr, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			rjpr.setStatus(sr);
			entity = new ResponseEntity<>(rjpr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
	
	
	public List<JobsmithPremadeReport> getData(int reportId)
	{
		DBOperation dbop = new DBOperation();
		List<JobsmithPremadeReport> listJPR = new ArrayList<JobsmithPremadeReport>();
		String [][]data;
		System.out.println(reportId);
		String sqlQuery = """
				SELECT
				jobsmith_premade_reportid,
				Rep_ReportName,
				jobsmith_categoryid,
				jobsmith_category,
				RepCap_CapabilityId,
				Cap_Capability,
				RepCap_Weightage,
				RepCap_Sequence
				FROM (
				SELECT
				a.jobsmith_premade_reportid,
				b.Rep_ReportName,
				a.jobsmith_categoryid,
				e.jobsmith_category,
				c.RepCap_CapabilityId,
				d.Cap_Capability,
				c.RepCap_Weightage,
				c.RepCap_Sequence,
				(SELECT COUNT(*)

				FROM reportcapability_tbl rc
				WHERE rc.RepCap_ReportId = c.RepCap_ReportId
				AND rc.RepCap_Weightage = c.RepCap_Weightage
				AND rc.RepCap_Sequence <= c.RepCap_Sequence) as row_num
				FROM
				jobsmith_premade_reports_tbl a
				INNER JOIN reports_tbl b ON a.jobsmith_premade_reportid = b.Rep_ReportId
				INNER JOIN reportcapability_tbl c ON a.jobsmith_premade_reportid = c.RepCap_ReportId
				INNER JOIN capability_tbl d ON c.RepCap_CapabilityId = d.Cap_CapabilityId
				INNER JOIN jobsmith_category_tbl e ON a.jobsmith_categoryid = e.jobsmith_categoryid
				WHERE
				c.RepCap_ReportId = """+reportId+"""
						) AS subquery
						WHERE
						(RepCap_Weightage = 'CRITICAL FACTORS' AND row_num <= 3)
						OR (RepCap_Weightage != 'CRITICAL FACTORS' AND row_num <= 5)
						ORDER BY RepCap_Sequence
						""";

		System.out.println(sqlQuery);
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			JobsmithPremadeReport jpr = new JobsmithPremadeReport();
			listJPR.add(jpr.getObject(data[i]));
		}

		return listJPR;
	}
	
	
	

	@PostMapping("/getsavedjobreport")
	public ResponseEntity<ResponseSavedJobReport> getSavedJobReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data, HttpServletRequest request)
	{
		System.out.println("C  A     L        L -------- getsavedjobreport");
		ResponseEntity<ResponseSavedJobReport> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponseSavedJobReport rsjr = new ResponseSavedJobReport();
		StatusResponse sr = new StatusResponse();
		SavedJobReportResponse jprr = new SavedJobReportResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		new UserDataResponse();
		new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());

		int companyid = data.getCompanyid()	;
		
		if(validToken)
		{
			if(validAccessToken)
			{		
			try {
				try
				{
					uaa = uaar.getReferenceByUserid(data.getUserid());  
					System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUserid());
					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());   

					// Check permission
					String permissionQuery = "SELECT jat.jobsmith_actionId, jat.jobsmith_functional_endpoint " +
							"FROM jobsmith_user_profile_tbl jup " +
							"JOIN jobsmith_permission_tbl jpt ON jup.jobsmith_profileId = jpt.jobsmith_profileId " +
							"JOIN jobsmith_action_tbl jat ON jpt.jobsmith_actionId = jat.jobsmith_actionId " +
							"JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid " +
							"WHERE ua.userid = ? AND jup.companyid = " + companyid+" AND jpt.IsPermission = 1  AND jup.isAccess = 1";
					List<Map<String, Object>> permissionData = jdbcTemplate.queryForList(permissionQuery, data.getUserid());

					// Get the endpoint being hit
					String endpoint = request.getRequestURI(); // Adjust this based on your endpoint structure

					boolean hasPermission = false;
					for (Map<String, Object> row : permissionData) {
						String functionalEndpoint = (String) row.get("jobsmith_functional_endpoint");
						if (functionalEndpoint.equals(endpoint)) {
							hasPermission = true;
							break;
						}
					}

					if (hasPermission) {

						String profileIdQuery = "SELECT jup.jobsmith_profileId FROM jobsmith_user_profile_tbl jup JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid WHERE userid = ? AND jup.companyid = " + companyid+" ";
						List<Map<String, Object>> profileIdData = jdbcTemplate.queryForList(profileIdQuery, data.getUserid());	         	
						int profileAccountId = (int) profileIdData.get(0).get("jobsmith_profileId");						

						if(profileAccountId==1 || profileAccountId==2) {

							List<SavedJobReport> jpr =  getallDataSavedJobReport(data);
							jprr.setSavedJobReport(jpr);

						}else {
							List<SavedJobReport> jpr =  getarchivedDataSavedJobReport(data);
							jprr.setSavedJobReport(jpr);
							}
						
						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Saved Job Report with Authentic Token ");                	

						rsjr.setStatus(sr);
						rsjr.setData(jprr);
						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);       				
					}
					else {
						// User doesn't have permission
						sr.setValid(false);
						sr.setStatusCode(11);
						sr.setMessage("User doesn't have permission to access this resource");
						rsjr.setData(null);
						rsjr.setStatus(sr);
						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.FORBIDDEN);
					}

				}
				catch(NullPointerException npex)
				{
					npex.printStackTrace();
					System.out.println(npex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
					rsjr.setData(null);
					rsjr.setStatus(sr);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);    				

				}catch(Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rsjr.setData(null);
					rsjr.setStatus(sr);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
				}  		
			}catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());			
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
				rsjr.setData(null);
				rsjr.setStatus(sr);
				entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
			} 
			}    	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rsjr.setData(null);
				rsjr.setStatus(sr);		
				entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			rsjr.setStatus(sr);
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}


	public List<SavedJobReport> getarchivedDataSavedJobReport(UserData data)
	{
		DBOperation dbop = new DBOperation();
		List<SavedJobReport> listJPR = new ArrayList<SavedJobReport>();
		String [][]Data;		
		int companyid = data.getCompanyid()	;

		//String accountIdQuery = "SELECT ua.useraccountid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = ?";
		//List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, data.getUserid());	         	
		//Long profileAccountId = (Long) accountIdData.get(0).get("useraccountid");

		String sqlQuery = "SELECT \r\n"
				+ "					jobsmith_reportid, \r\n"
				+ "					jobsmith_report_name, \r\n"
				+ "					report_status, \r\n"
				+ "					CONCAT (b.firstname, ' ', b.lastname) AS CREATEBY, \r\n"
				+ "					DATE_FORMAT(a.date_modifed, '%Y-%m-%d %h:%i %p') AS date_modifed, \r\n"
				+ "					locked \r\n"
				+ "				FROM \r\n"
				+ "					jobsmith_report_tbl a \r\n"
				+ "					INNER JOIN user_accounts b ON b.useraccountid = a.useraccountid \r\n"
				+ "					INNER JOIN jobsmith_user_profile_tbl c ON c.useraccountid = b.useraccountid\r\n"
				+ "				WHERE \r\n"
				+ "					archived = 0 AND a.companyid = " + companyid+" AND report_status IN ('Draft', 'LIVE') AND c.companyid = a.companyid ORDER BY jobsmith_report_name";
		System.out.println(sqlQuery);
		System.out.println("??????????????????????????????????????");
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		Data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			SavedJobReport jpr = new SavedJobReport();
			listJPR.add(jpr.getObject(Data[i]));
		}

		return listJPR;
	}
	
	

	public List<SavedJobReport> getallDataSavedJobReport(UserData data)
	{
		DBOperation dbop = new DBOperation();
		List<SavedJobReport> listJPR = new ArrayList<SavedJobReport>();
		String [][]Data;		
		int companyid = data.getCompanyid()	;

		//String accountIdQuery = "SELECT ua.useraccountid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = ?";
		//List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, data.getUserid());	         	
		//Long profileAccountId = (Long) accountIdData.get(0).get("useraccountid");

		String sqlQuery = "SELECT jobsmith_reportid, jobsmith_report_name, report_status, CONCAT (b.firstname, ' ', b.lastname) AS CREATEBY, "
				+ "					DATE_FORMAT(a.date_modifed, '%Y-%m-%d %h:%i %p') AS date_modifed, locked FROM jobsmith_report_tbl a \r\n"
				+ "    INNER JOIN user_accounts b ON b.useraccountid = a.useraccountid \r\n"
				+ "    INNER JOIN jobsmith_user_profile_tbl c ON c.useraccountid = b.useraccountid \r\n"
				+ "				WHERE archived = 0  AND a.companyid = " + companyid+" AND c.companyid = a.companyid ORDER BY jobsmith_report_name";
		System.out.println(sqlQuery);

		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		Data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			SavedJobReport jpr = new SavedJobReport();
			listJPR.add(jpr.getObject(Data[i]));
		}

		return listJPR;
	}


	@PostMapping("/getsavedjobreportexpansion")
	public ResponseEntity<ResponseSavedJobReportExpansion> getSavedJobReportExpansion(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData userData)
	{
		ResponseEntity<ResponseSavedJobReportExpansion> entity = null;
		HttpHeaders headers = new HttpHeaders();

		ResponseSavedJobReportExpansion respExp = new ResponseSavedJobReportExpansion();
		StatusResponse sr = new StatusResponse();    	
		SavedJobReportsExpansionResponse jprr = new SavedJobReportsExpansionResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		
		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(userData.getUserid());

		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					try
					{    				
						uaa = uaar.getReferenceByUserid(userData.getUserid());  
						System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+userData.getUserid());
						ui.setFirstname(uaa.getFirstname());
						ui.setLastname(uaa.getLastname());
						ui.setStatus(uaa.getStatus());
						ui.setStatusdate(uaa.getStatusdate());   

						List<SavedJobReportsExpansion> jpr =  getDataSavedJobReportExpansion(userData.getReportId());

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Saved Job Reports Expansio with Authentic Token");

						jprr.setSavedJobReportsExpansion(jpr);

						respExp.setStatus(sr);
						respExp.setData(jprr);


						entity = new ResponseEntity<>(respExp, headers, HttpStatus.OK);		
						return entity;    				
					}catch(NullPointerException npex)
					{
						npex.printStackTrace();
						System.out.println(npex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
						respExp.setData(null);
						respExp.setStatus(sr);
						entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);    				

					}catch(Exception ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or Unauthentic User");
						respExp.setData(null);
						respExp.setStatus(sr);
						entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);
					}  		
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					respExp.setData(null);
					respExp.setStatus(sr);
					entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);
				} 
			} 
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				respExp.setData(null);
				respExp.setStatus(sr);		
				entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			respExp.setStatus(sr);
			entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);
		}
    
		return entity;
	}

	

	public List<SavedJobReportsExpansion> getDataSavedJobReportExpansion(int reportId) 
	{
		DBOperation dbop = new DBOperation();
		List<SavedJobReportsExpansion> listJPR = new ArrayList<SavedJobReportsExpansion>();
		String [][]data;		
		String sqlQuery = """	
				SELECT 
				a.jobsmith_reportid, 
				a.capabilityid, 
				b.Cap_Capability, 
				a.weightage, 
				c.report_status, 
				DATE_FORMAT(c.status_date, '%Y-%m-%d %h:%i %p') AS status_date, 
				a.sequence 
				FROM 
				jobsmith_report_capability_tbl a 
				INNER JOIN capability_tbl b ON b.Cap_CapabilityId = a.capabilityid 
				INNER JOIN jobsmith_report_tbl c ON c.jobsmith_reportid = a.jobsmith_reportid 
				WHERE 
				a.jobsmith_reportid = """+reportId+" AND a.archived != -9";

		System.out.println(sqlQuery);
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			SavedJobReportsExpansion jpr = new SavedJobReportsExpansion();
			listJPR.add(jpr.getObject(data[i]));
		}

		return listJPR;
	}

	@PostMapping("/getpremadereport")
	public ResponseEntity<ResponsePreMadeReport> getPreMadeReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data)
	{
		System.out.println("C  A     L        L -------- getpremadereport");
		ResponseEntity<ResponsePreMadeReport> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponsePreMadeReport rpmr = new ResponsePreMadeReport();
		StatusResponse sr = new StatusResponse();
		PreMadeReportResponse jprr = new PreMadeReportResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
	
		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());

		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					try
					{
						uaa = uaar.getReferenceByUserid(data.getUserid());  
						System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUserid());
						ui.setFirstname(uaa.getFirstname());
						ui.setLastname(uaa.getLastname());
						ui.setStatus(uaa.getStatus());
						ui.setStatusdate(uaa.getStatusdate());    				

						List<PreMadeReport> jpr =  getDataPreMadeReport(data.getCategoryId());

						jprr.setPreMadeReport(jpr);

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Premade Report with Authentic Token");                	

						rpmr.setStatus(sr);
						rpmr.setData(jprr);
						entity = new ResponseEntity<>(rpmr, headers, HttpStatus.OK);       				
					}catch(NullPointerException npex)
					{
						npex.printStackTrace();
						System.out.println(npex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
						rpmr.setData(null);
						rpmr.setStatus(sr);
						entity = new ResponseEntity<>(rpmr, headers, HttpStatus.UNAUTHORIZED);    				

					}catch(Exception ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or Unauthentic User");
						rpmr.setData(null);
						rpmr.setStatus(sr);
						entity = new ResponseEntity<>(rpmr, headers, HttpStatus.UNAUTHORIZED);
					}  		
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rpmr.setData(null);
					rpmr.setStatus(sr);
					entity = new ResponseEntity<>(rpmr, headers, HttpStatus.UNAUTHORIZED);
				} 
			}    	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rpmr.setData(null);
				rpmr.setStatus(sr);		
				entity = new ResponseEntity<>(rpmr, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");	
			rpmr.setData(null);
			rpmr.setStatus(sr);
			entity = new ResponseEntity<>(rpmr, headers, HttpStatus.UNAUTHORIZED);
		}

		return entity;
	}
	
	
	
	public List<PreMadeReport> getDataPreMadeReport(int categoryId)
	{
		DBOperation dbop = new DBOperation();
		List<PreMadeReport> listJPR = new ArrayList<PreMadeReport>();
		String [][]data;		
		String sqlQuery = """	
				SELECT
				  a.jobsmith_premade_reportid,
				  b.Rep_ReportName ,
				  a.jobsmith_categoryid
				FROM
				  jobsmith_premade_reports_tbl a
				  inner join reports_tbl b ON a.jobsmith_premade_reportid = b.Rep_ReportId
				  inner JOIN reportcapability_tbl c ON a.jobsmith_premade_reportid = c.RepCap_ReportId
				WHERE
				  a.jobsmith_categoryid ="""+categoryId+""" 
						  AND a.archived = 0 
						GROUP BY
				  jobsmith_premade_reportid
				""";
		System.out.println(sqlQuery);
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			PreMadeReport jpr = new PreMadeReport();
			listJPR.add(jpr.getObject(data[i]));
		}

		return listJPR;
	}	

	@PostMapping("/getjobsearchresult")
	public List<JobSearchResult> getJobSearchResult(@RequestBody UserData data)
	{
		DBSearch dbs = new DBSearch();

		List<JobSearchResult> jobSearchResult = dbs.getObjects(data.getFindValue());

		return jobSearchResult;
	}	





	@PostMapping("/updatearchive")
	public ResponseEntity<StatusResponse> updateArchive(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data, HttpServletRequest request)
	{
		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);
		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());
		
		int companyid = data.getCompanyid()	;

		if(validToken)
		{
			if(validAccessToken)
			{
			try {
				try
				{ 
					uaa = uaar.getReferenceByUserid(data.getUserid());   

					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());      	

					// Check permission
					String permissionQuery = "SELECT jat.jobsmith_actionId, jat.jobsmith_functional_endpoint " +
							"FROM jobsmith_user_profile_tbl jup " +
							"JOIN jobsmith_permission_tbl jpt ON jup.jobsmith_profileId = jpt.jobsmith_profileId " +
							"JOIN jobsmith_action_tbl jat ON jpt.jobsmith_actionId = jat.jobsmith_actionId " +
							"JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid " +
							"WHERE ua.userid = ? AND jup.companyid = " + companyid+" AND jpt.IsPermission = 1 AND jup.isAccess = 1";
					List<Map<String, Object>> permissionData = jdbcTemplate.queryForList(permissionQuery, data.getUserid());

					// Get the endpoint being hit
					String endpoint = request.getRequestURI(); // Adjust this based on your endpoint structure

					boolean hasPermission = false;
					for (Map<String, Object> row : permissionData) {
						String functionalEndpoint = (String) row.get("jobsmith_functional_endpoint");
						if (functionalEndpoint.equals(endpoint)) {
							hasPermission = true;
							break;
						}
					}

					if (hasPermission) {

						String profileIdQuery = "SELECT jup.jobsmith_profileId FROM jobsmith_user_profile_tbl jup JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid WHERE userid = ?";
						List<Map<String, Object>> profileIdData = jdbcTemplate.queryForList(profileIdQuery, data.getUserid());	         	
						int profileAccountId = (int) profileIdData.get(0).get("jobsmith_profileId");

						if(profileAccountId==2) {

							// Get the user account id associated with the user's userid
							String userAccountIdQuery = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
							List<Map<String, Object>> userAccountIdData = jdbcTemplate.queryForList(userAccountIdQuery, data.getUserid());
							long accessibleUserAccountId = (long) userAccountIdData.get(0).get("useraccountid");	                	

							// Get the user account id associated with the report
							String reportAccountIdQuery = "SELECT useraccountid FROM jobsmith_report_tbl WHERE jobsmith_reportid = ?";
							List<Map<String, Object>> reportAccountIdData = jdbcTemplate.queryForList(reportAccountIdQuery, data.getReportId());	         	
							int accessibleReportAccountId = (int) reportAccountIdData.get(0).get("useraccountid");

							if (accessibleReportAccountId == accessibleUserAccountId) {

								DBUpdate dbi = new DBUpdate();
								sr = dbi.updateObject(data);

								if(sr.isValid() == true)
								{
									entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);
								}
								else
								{
									entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
								}
							} else {
								// User doesn't have permission to update this report
								sr.setValid(false);
								sr.setStatusCode(0);
								sr.setMessage("User doesn't have permission to update other user report");
								entity = new ResponseEntity<>(sr, headers, HttpStatus.FORBIDDEN);
							}
						}
						else {
							DBUpdate dbi = new DBUpdate();
							sr = dbi.updateObject(data);

							if(sr.isValid() == true)
							{
								entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);
							}
							else
							{
								entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
							}
						}
					}else {
						// User doesn't have permission
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("User doesn't have permission to access this resource");
						entity = new ResponseEntity<>(sr, headers, HttpStatus.FORBIDDEN);
					}			
				}catch(NullPointerException npex)
				{
					npex.printStackTrace();
					System.out.println(npex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);    
				}catch(Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}  		
			}catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());			
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
			}	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");	
				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}	
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);	
			return entity;
		}    		
		return entity;
	}




	@PostMapping("/udpatereportstatus")
	public ResponseEntity<StatusResponse>udpatereportstatus(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data, HttpServletRequest request) {

		ResponseEntity<StatusResponse> entity; HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();



		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());
		
		int companyid = data.getCompanyid()	;

		if(validToken)
		{
			if(validAccessToken)
			{
			try {
				try
				{ 
					uaa = uaar.getReferenceByUserid(data.getUserid());   

					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());      	

					// Check permission
					String permissionQuery = "SELECT jat.jobsmith_actionId, jat.jobsmith_functional_endpoint " +
							"FROM jobsmith_user_profile_tbl jup " +
							"JOIN jobsmith_permission_tbl jpt ON jup.jobsmith_profileId = jpt.jobsmith_profileId " +
							"JOIN jobsmith_action_tbl jat ON jpt.jobsmith_actionId = jat.jobsmith_actionId " +
							"JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid " +
							"WHERE ua.userid = ? AND jup.companyid = " + companyid+" AND jpt.IsPermission = 1 AND jup.isAccess = 1";
					List<Map<String, Object>> permissionData = jdbcTemplate.queryForList(permissionQuery, data.getUserid());

					// Get the endpoint being hit
					String endpoint = request.getRequestURI(); // Adjust this based on your endpoint structure

					boolean hasPermission = false;
					for (Map<String, Object> row : permissionData) {
						String functionalEndpoint = (String) row.get("jobsmith_functional_endpoint");
						if (functionalEndpoint.equals(endpoint)) {
							hasPermission = true;
							break;
						}
					}

					if (hasPermission) {
						String profileIdQuery = "SELECT jup.jobsmith_profileId FROM jobsmith_user_profile_tbl jup JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid WHERE userid = ?";
						List<Map<String, Object>> profileIdData = jdbcTemplate.queryForList(profileIdQuery, data.getUserid());	         	
						int profileAccountId = (int) profileIdData.get(0).get("jobsmith_profileId");

						if(profileAccountId==2) {

							// Get the user account id associated with the user's userid
							String userAccountIdQuery = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
							List<Map<String, Object>> userAccountIdData = jdbcTemplate.queryForList(userAccountIdQuery, data.getUserid());
							long accessibleUserAccountId = (long) userAccountIdData.get(0).get("useraccountid");	                	

							// Get the user account id associated with the report
							String reportAccountIdQuery = "SELECT useraccountid FROM jobsmith_report_tbl WHERE jobsmith_reportid = ?";
							List<Map<String, Object>> reportAccountIdData = jdbcTemplate.queryForList(reportAccountIdQuery, data.getReportId());	         	
							int accessibleReportAccountId = (int) reportAccountIdData.get(0).get("useraccountid");

							if (accessibleReportAccountId == accessibleUserAccountId) {

								DBUpdate dbi = new DBUpdate();
								sr = dbi.udpatereportstatus(data);

								if(sr.isValid() == true) { 
									entity = new ResponseEntity<>(sr, headers,HttpStatus.OK); 
								}
								else { 
									entity = new ResponseEntity<>(sr, headers,HttpStatus.UNAUTHORIZED);
								}
							} else {
								// User doesn't have permission to update this report
								sr.setValid(false);
								sr.setStatusCode(0);
								sr.setMessage("User doesn't have permission to update other user report");
								entity = new ResponseEntity<>(sr, headers, HttpStatus.FORBIDDEN);
							}
						}
						else {
							DBUpdate dbi = new DBUpdate();
							sr = dbi.udpatereportstatus(data);

							if(sr.isValid() == true) { 
								entity = new ResponseEntity<>(sr, headers,HttpStatus.OK); 
							}
							else { 
								entity = new ResponseEntity<>(sr, headers,HttpStatus.UNAUTHORIZED);
							}
						}
					}else {
						// User doesn't have permission
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("User doesn't have permission to access this resource");
						entity = new ResponseEntity<>(sr, headers, HttpStatus.FORBIDDEN);
					}			
				}
				catch(NullPointerException npex)
				{
					npex.printStackTrace();
					System.out.println(npex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);    
				}catch(Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}  		
			}catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());			
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
			}	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");	
				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);	
			return entity;
		}     		

		return entity;
	}	





	@PostMapping("/updatelock")
	public ResponseEntity<StatusResponse> updateLock(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data, HttpServletRequest request)
	{
		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());
		
		int companyid = data.getCompanyid()	;

		if(validToken)
		{
			if(validAccessToken)
			{
			try {
				try
				{ 
					uaa = uaar.getReferenceByUserid(data.getUserid());   

					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());      	
					// Check permission
					String permissionQuery = "SELECT jat.jobsmith_actionId, jat.jobsmith_functional_endpoint " +
							"FROM jobsmith_user_profile_tbl jup " +
							"JOIN jobsmith_permission_tbl jpt ON jup.jobsmith_profileId = jpt.jobsmith_profileId " +
							"JOIN jobsmith_action_tbl jat ON jpt.jobsmith_actionId = jat.jobsmith_actionId " +
							"JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid " +
							"WHERE ua.userid = ? AND jup.companyid = " + companyid+" AND jpt.IsPermission = 1 AND jup.isAccess = 1";
					List<Map<String, Object>> permissionData = jdbcTemplate.queryForList(permissionQuery, data.getUserid());

					// Get the endpoint being hit
					String endpoint = request.getRequestURI(); // Adjust this based on your endpoint structure

					boolean hasPermission = false;
					for (Map<String, Object> row : permissionData) {
						String functionalEndpoint = (String) row.get("jobsmith_functional_endpoint");
						if (functionalEndpoint.equals(endpoint)) {
							hasPermission = true;
							break;
						}
					}

					if (hasPermission) {

						String profileIdQuery = "SELECT jup.jobsmith_profileId FROM jobsmith_user_profile_tbl jup JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid WHERE userid = ?";
						List<Map<String, Object>> profileIdData = jdbcTemplate.queryForList(profileIdQuery, data.getUserid());	         	
						int profileAccountId = (int) profileIdData.get(0).get("jobsmith_profileId");

						if(profileAccountId==2) {

							// Get the user account id associated with the user's userid
							String userAccountIdQuery = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
							List<Map<String, Object>> userAccountIdData = jdbcTemplate.queryForList(userAccountIdQuery, data.getUserid());
							long accessibleUserAccountId = (long) userAccountIdData.get(0).get("useraccountid");	                	

							// Get the user account id associated with the report
							String reportAccountIdQuery = "SELECT useraccountid FROM jobsmith_report_tbl WHERE jobsmith_reportid = ?";
							List<Map<String, Object>> reportAccountIdData = jdbcTemplate.queryForList(reportAccountIdQuery, data.getReportId());	         	
							int accessibleReportAccountId = (int) reportAccountIdData.get(0).get("useraccountid");

							if (accessibleReportAccountId == accessibleUserAccountId) {

								DBUpdate dbi = new DBUpdate();
								sr = dbi.updateLock(data);

								if(sr.isValid() == true)
								{
									entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);
								}
								else
								{
									entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
								}
							} else {
								// User doesn't have permission to update this report
								sr.setValid(false);
								sr.setStatusCode(0);
								sr.setMessage("User doesn't have permission to update other user report");
								entity = new ResponseEntity<>(sr, headers, HttpStatus.FORBIDDEN);
							}
						}
						else {
							DBUpdate dbi = new DBUpdate();
							sr = dbi.updateLock(data);

							if(sr.isValid() == true)
							{
								entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);
							}
							else
							{
								entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
							}
						}
					}else {
						// User doesn't have permission
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("User doesn't have permission to access this resource");
						entity = new ResponseEntity<>(sr, headers, HttpStatus.FORBIDDEN);
					}			
				}catch(NullPointerException npex)
				{
					npex.printStackTrace();
					System.out.println(npex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");   
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);    
				}catch(Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}  		
			}catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());			
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
			}	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");	
				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");

			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);	
			return entity;
		} 
		return entity;
	}
	
	

	@PostMapping("/getsearchjobreport")
	public ResponseEntity<ResponseSavedJobReport> getSearchJobReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data, HttpServletRequest request)
	{
		ResponseEntity<ResponseSavedJobReport> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponseSavedJobReport rsjr = new ResponseSavedJobReport();
		StatusResponse sr = new StatusResponse();
		SavedJobReportResponse jprr = new SavedJobReportResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		new UserDataResponse();
		new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());

		int companyid = data.getCompanyid()	;

		if(validToken)
		{
			if(validAccessToken)
			{	
			try {
				try
				{
					uaa = uaar.getReferenceByUserid(data.getUserid());  					
					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());   

					// Check permission
					String permissionQuery = "SELECT jat.jobsmith_actionId, jat.jobsmith_functional_endpoint " +
							"FROM jobsmith_user_profile_tbl jup " +
							"JOIN jobsmith_permission_tbl jpt ON jup.jobsmith_profileId = jpt.jobsmith_profileId " +
							"JOIN jobsmith_action_tbl jat ON jpt.jobsmith_actionId = jat.jobsmith_actionId " +
							"JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid " +
							"WHERE ua.userid = ? AND jup.companyid = " + companyid+" AND jpt.IsPermission = 1 AND jup.isAccess = 1";
					List<Map<String, Object>> permissionData = jdbcTemplate.queryForList(permissionQuery, data.getUserid());

					// Get the endpoint being hit
					String endpoint = request.getRequestURI(); // Adjust this based on your endpoint structure

					boolean hasPermission = false;
					for (Map<String, Object> row : permissionData) {
						String functionalEndpoint = (String) row.get("jobsmith_functional_endpoint");
						if (functionalEndpoint.equals(endpoint)) {
							hasPermission = true;
							break;
						}
					}

					if (hasPermission) {
						String profileIdQuery = "SELECT jup.jobsmith_profileId FROM jobsmith_user_profile_tbl jup JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid WHERE userid = ? AND jup.companyid = " + companyid ;
						List<Map<String, Object>> profileIdData = jdbcTemplate.queryForList(profileIdQuery, data.getUserid());	         	
						int profileAccountId = (int) profileIdData.get(0).get("jobsmith_profileId");

						System.out.println(profileAccountId+"................................................");
						
						if(profileAccountId==1 || profileAccountId==2) {

							List<SavedJobReport> jpr = getallDataSearchJobReport(data);
							jprr.setSavedJobReport(jpr);
						}
						else {					
							List<SavedJobReport> jpr = getArchivedDataSearchJobReport(data);
							jprr.setSavedJobReport(jpr);
							}						

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Search Job Report with Authentic Token ");                	

						rsjr.setStatus(sr);
						rsjr.setData(jprr);
						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);       				
					}
					else {
						// User doesn't have permission
						sr.setValid(false);
						sr.setStatusCode(11);
						sr.setMessage("User doesn't have permission to access this resource");
						rsjr.setData(null);
						rsjr.setStatus(sr);
						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.FORBIDDEN);
					}
				}

				catch(NullPointerException npex)
				{
					npex.printStackTrace();
					System.out.println(npex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
					rsjr.setData(null);
					rsjr.setStatus(sr);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);    				

				}catch(Exception ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rsjr.setData(null);
					rsjr.setStatus(sr);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
				}  		
			}catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());			
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
				rsjr.setData(null);
				rsjr.setStatus(sr);
				entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
			} 
			}    	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rsjr.setData(null);
				rsjr.setStatus(sr);		
				entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");	
			rsjr.setData(null);
			rsjr.setStatus(sr);
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
		} 
		return entity;
	}
	
	
	public List<SavedJobReport> getArchivedDataSearchJobReport(UserData data)
	{
		DBOperation dbop = new DBOperation();
		List<SavedJobReport> listJPR = new ArrayList<>();
		String [][]Data;	
		int companyid = data.getCompanyid();
		String jobsmith_report_name = data.getJobsmithReportName()	;

		//String accountIdQuery = "SELECT ua.useraccountid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = ?";
		//List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, data.getUserid());	         	
		//Long profileAccountId = (Long) accountIdData.get(0).get("useraccountid");

		String sqlQuery = "SELECT \n" +
				"    jobsmith_reportid, \n" +
				"    jobsmith_report_name, \n" +
				"    report_status, \n" +
				"    CONCAT(b.firstname, ' ', b.lastname) AS createdBy, \n" +
				"    DATE_FORMAT(a.date_modifed, '%Y-%m-%d %h:%i %p') AS date_modifed, \n" +
				"    locked \n" +
				"FROM \n" +
				"    jobsmith_report_tbl a \n" +
				"    INNER JOIN user_accounts b ON b.useraccountid = a.useraccountid "	+ 
				"   INNER JOIN jobsmith_user_profile_tbl c ON c.useraccountid = b.useraccountid \n" +
				"WHERE \n" +
				" archived = 0 AND jobsmith_report_name LIKE '%" + jobsmith_report_name + "%' AND a.companyid = " + companyid+" AND c.companyid = a.companyid AND report_status IN ('Draft', 'LIVE')";
		
		System.out.println("..........................................................");

		System.out.println(sqlQuery);
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		Data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			SavedJobReport jpr = new SavedJobReport();
			listJPR.add(jpr.getObject(Data[i]));
		}
		return listJPR;
	}

	public List<SavedJobReport> getallDataSearchJobReport(UserData data)
	{
		DBOperation dbop = new DBOperation();
		List<SavedJobReport> listJPR = new ArrayList<>();
		String [][]Data;
		int companyid = data.getCompanyid()	;
		String jobsmith_report_name = data.getJobsmithReportName()	;

		//String accountIdQuery = "SELECT ua.useraccountid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = ?";
		//List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, data.getUserid());	         	
		//Long profileAccountId = (Long) accountIdData.get(0).get("useraccountid");
		String sqlQuery = "SELECT \n" +
				"    jobsmith_reportid, \n" +
				"    jobsmith_report_name, \n" +
				"    report_status, \n" +
				"    CONCAT(b.firstname, ' ', b.lastname) AS createdBy, \n" +
				"    DATE_FORMAT(a.date_modifed, '%Y-%m-%d %h:%i %p') AS date_modifed, \n" +
				"    locked \n" +
				"FROM \n" +
				"    jobsmith_report_tbl a \n" +
				"    INNER JOIN user_accounts b ON b.useraccountid = a.useraccountid "	+ 
				"   INNER JOIN jobsmith_user_profile_tbl c ON c.useraccountid = b.useraccountid \n" +
				"WHERE \n" +
				" archived = 0 AND jobsmith_report_name LIKE '%" + jobsmith_report_name + "%' AND a.companyid = " + companyid+" AND c.companyid = a.companyid";

		System.out.println(sqlQuery);
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		Data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			SavedJobReport jpr = new SavedJobReport();
			listJPR.add(jpr.getObject(Data[i]));
		}
		return listJPR;
	}


	@SuppressWarnings("deprecation")
	@PostMapping("/geteditjobreport")
	public ResponseEntity<ResponseEditJob> getJobReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data, HttpServletRequest request) {
		String message = "";

		ResponseEntity<ResponseEditJob> entity;
		HttpHeaders headers = new HttpHeaders();    

		ResponseEditJob rsjr = new ResponseEditJob();            
		EditJobResponse editresp = new EditJobResponse();
		StatusResponse sr = new StatusResponse();

		Editjobreport editjobreport = new Editjobreport();

		UserAccountsAuth uaa = new UserAccountsAuth();
		new UserDataResponse();
		new UserInfoResponse();

		UserInfo ui = new UserInfo();        

		boolean validToken = false;


		String accountIdQuery = "SELECT ua.useraccountid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = ?";
		List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, data.getUserid());	         	
		Long accountId = accountIdData.isEmpty() ? 0L : (Long) accountIdData.get(0).get("useraccountid");

		validToken = checkToken(authorizationHeader);


		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());
		
		int companyid = data.getCompanyid()	;

		if(validToken)
		{
			if(validAccessToken)
			{	
			try {
				try {
					uaa = uaar.getReferenceByUserid(data.getUserid());  
					System.out.println(" ----------  "+data.getUserid());
					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());  

					// Check permission
					String permissionQuery = "SELECT jat.jobsmith_actionId, jat.jobsmith_functional_endpoint " +
							"FROM jobsmith_user_profile_tbl jup " +
							"JOIN jobsmith_permission_tbl jpt ON jup.jobsmith_profileId = jpt.jobsmith_profileId " +
							"JOIN jobsmith_action_tbl jat ON jpt.jobsmith_actionId = jat.jobsmith_actionId " +
							"JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid " +
							"WHERE ua.userid = ? AND jup.companyid = " + companyid+" AND jpt.IsPermission = 1 AND jup.isAccess = 1";
					List<Map<String, Object>> permissionData = jdbcTemplate.queryForList(permissionQuery, data.getUserid());

					// Get the endpoint being hit
					String endpoint = request.getRequestURI(); // Adjust this based on your endpoint structure

					boolean hasPermission = false;
					for (Map<String, Object> row : permissionData) {
						String functionalEndpoint = (String) row.get("jobsmith_functional_endpoint");
						if (functionalEndpoint.equals(endpoint)) {
							hasPermission = true;
							break;
						}
					}

					if (hasPermission) {

						//int companyid = data.getCompanyid();
						System.out.println(companyid);
						String profileIdQuery = "SELECT jup.jobsmith_profileId FROM jobsmith_user_profile_tbl jup JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid WHERE userid = ?";
						List<Map<String, Object>> profileIdData = jdbcTemplate.queryForList(profileIdQuery, data.getUserid());	         	
						int profileAccountId = (int) profileIdData.get(0).get("jobsmith_profileId");

						if(profileAccountId==2) {

							// Get the user account id associated with the user's userid
							String userAccountIdQuery = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
							List<Map<String, Object>> userAccountIdData = jdbcTemplate.queryForList(userAccountIdQuery, data.getUserid());
							long accessibleUserAccountId = (long) userAccountIdData.get(0).get("useraccountid");	                	

							// Get the user account id associated with the report
							String reportAccountIdQuery = "SELECT useraccountid FROM jobsmith_report_tbl WHERE jobsmith_reportid = ?";
							List<Map<String, Object>> reportAccountIdData = jdbcTemplate.queryForList(reportAccountIdQuery, data.getReportId());	         	
							int accessibleReportAccountId = (int) reportAccountIdData.get(0).get("useraccountid");

							if (accessibleReportAccountId == accessibleUserAccountId) {

								// User has permission, proceed with fetching job report

								String query1 = "SELECT \r\n"
										+ "  jobsmith_reportid, \r\n"
										+ "  jobsmith_report_name, \r\n"
										+ "  jobsmith_report_note, \r\n"
										+ "  a.companyid, \r\n"
										+ "  a.useraccountid, \r\n"
										+ "  report_status \r\n"
										+ "FROM \r\n"
										+ "  jobsmith_report_tbl a \r\n"
										+ "  INNER JOIN user_accounts b ON b.useraccountid = a.useraccountid \r\n"
										+ "  INNER JOIN jobsmith_user_profile_tbl c ON c.useraccountid = b.useraccountid \r\n"
										+ "WHERE \r\n"
										+ "  archived != -9 \r\n"
										+ "  AND c.useraccountid = " +accountId+" "
										+ "  AND c.companyid = " +companyid+""
										+ "  AND jobsmith_reportid = "+data.getReportId();
								System.out.println(query1);
								jdbcTemplate.query(query1, new Object[] {}, rs -> {
									editjobreport.setJobreportid(rs.getString("jobsmith_reportid"));
									editjobreport.setJobtitle(rs.getString("jobsmith_report_name"));
									editjobreport.setJobnote(rs.getString("jobsmith_report_note"));
									editjobreport.setCompanyid(rs.getString("companyid"));
									editjobreport.setUseraccountid(rs.getString("useraccountid"));
									editjobreport.setReportstatus(rs.getString("report_status"));
								});

								// Fetch capability details
								String query2 = "SELECT \r\n"
										+ "  a.jobsmith_report_capabilityid, \r\n"
										+ "  a.capabilityid, \r\n"
										+ "  b.Cap_Capability, \r\n"
										+ "  a.weightage, \r\n"
										+ "  a.sequence \r\n"
										+ "FROM \r\n"
										+ "  jobsmith_report_capability_tbl a \r\n"
										+ "  INNER JOIN capability_tbl b ON b.Cap_CapabilityId = a.capabilityid \r\n"
										+ "  INNER JOIN jobsmith_report_tbl c ON c.jobsmith_reportid = a.jobsmith_reportid \r\n"
										+ "  INNER JOIN user_accounts u ON u.useraccountid = c.useraccountid INNER \r\n"
										+ "  JOIN jobsmith_user_profile_tbl p ON p.useraccountid = u.useraccountid WHERE a.archived != -9 \r\n"
										+ "  AND c.jobsmith_reportid = "+data.getReportId()+""
										+ "  AND p.companyid =  " +companyid+" "
										+ "  AND p.useraccountid = "+accountId+"";
								System.out.println(query2);
								jdbcTemplate.query(query2, new Object[]{}, rs -> {
									Jobedit jobedit = new Jobedit();
									jobedit.setJobsmith_report_capabilityid(rs.getString("jobsmith_report_capabilityid"));
									jobedit.setReport_CapabilityId(rs.getString("capabilityid"));
									jobedit.setCap_Capability(rs.getString("Cap_Capability"));
									jobedit.setReport_Weightage(rs.getString("weightage"));
									jobedit.setRepCap_Sequence(rs.getString("sequence"));

									String weightage = rs.getString("weightage");
									if (weightage.equalsIgnoreCase("CRITICAL")) {
										editjobreport.getCritical().add(jobedit);
									} else if (weightage.equalsIgnoreCase("IMPORTANT")) {
										editjobreport.getImportant().add(jobedit);
									} else if (weightage.equalsIgnoreCase("NICE TO HAVE")) {
										editjobreport.getNicetohave().add(jobedit);
									}
								});

								editresp.setJobReportData(editjobreport);                    

								sr.setValid(true);
								sr.setStatusCode(1);
								sr.setMessage(message); 

								rsjr.setStatus(sr);
								rsjr.setData(editresp);
								entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);  
							} else {
								// User doesn't have permission to update this report
								sr.setValid(false);
								sr.setStatusCode(0);
								sr.setMessage("User doesn't have permission to get other user report");
								rsjr.setData(null);
								rsjr.setStatus(sr);
								entity = new ResponseEntity<>(rsjr, headers, HttpStatus.FORBIDDEN);
							}

						} 	else {
							// User has permission, proceed with fetching job report

							String query1 = "SELECT \r\n"
									+ "  jobsmith_reportid, \r\n"
									+ "  jobsmith_report_name, \r\n"
									+ "  jobsmith_report_note, \r\n"
									+ "  a.companyid, \r\n"
									+ "  a.useraccountid, \r\n"
									+ "  report_status \r\n"
									+ "FROM \r\n"
									+ "  jobsmith_report_tbl a \r\n"
									+ "  INNER JOIN user_accounts b ON b.useraccountid = a.useraccountid \r\n"
									+ "  INNER JOIN jobsmith_user_profile_tbl c ON c.useraccountid = b.useraccountid \r\n"
									+ "WHERE \r\n"
									+ "  archived != -9 \r\n"

	                    		+ "  AND c.companyid = " +companyid+""
	                    		+ "  AND jobsmith_reportid = "+data.getReportId();
							System.out.println(query1);
							jdbcTemplate.query(query1, new Object[] {}, rs -> {
								editjobreport.setJobreportid(rs.getString("jobsmith_reportid"));
								editjobreport.setJobtitle(rs.getString("jobsmith_report_name"));
								editjobreport.setJobnote(rs.getString("jobsmith_report_note"));
								editjobreport.setCompanyid(rs.getString("companyid"));
								editjobreport.setUseraccountid(rs.getString("useraccountid"));
								editjobreport.setReportstatus(rs.getString("report_status"));
							});

							// Fetch capability details
							String query2 = "SELECT \r\n"
									+ "  a.jobsmith_report_capabilityid, \r\n"
									+ "  a.capabilityid, \r\n"
									+ "  b.Cap_Capability, \r\n"
									+ "  a.weightage, \r\n"
									+ "  a.sequence \r\n"
									+ "FROM \r\n"
									+ "  jobsmith_report_capability_tbl a \r\n"
									+ "  INNER JOIN capability_tbl b ON b.Cap_CapabilityId = a.capabilityid \r\n"
									+ "  INNER JOIN jobsmith_report_tbl c ON c.jobsmith_reportid = a.jobsmith_reportid \r\n"
									+ "  INNER JOIN user_accounts u ON u.useraccountid = c.useraccountid INNER \r\n"
									+ "  JOIN jobsmith_user_profile_tbl p ON p.useraccountid = u.useraccountid WHERE a.archived != -9 \r\n"
									+ "  AND c.jobsmith_reportid = "+data.getReportId()+""
									+ "  AND p.companyid =  " +companyid+"";
							System.out.println(query2);
							jdbcTemplate.query(query2, new Object[]{}, rs -> {
								Jobedit jobedit = new Jobedit();
								jobedit.setJobsmith_report_capabilityid(rs.getString("jobsmith_report_capabilityid"));
								jobedit.setReport_CapabilityId(rs.getString("capabilityid"));
								jobedit.setCap_Capability(rs.getString("Cap_Capability"));
								jobedit.setReport_Weightage(rs.getString("weightage"));
								jobedit.setRepCap_Sequence(rs.getString("sequence"));

								String weightage = rs.getString("weightage");
								if (weightage.equalsIgnoreCase("CRITICAL")) {
									editjobreport.getCritical().add(jobedit);
								} else if (weightage.equalsIgnoreCase("IMPORTANT")) {
									editjobreport.getImportant().add(jobedit);
								} else if (weightage.equalsIgnoreCase("NICE TO HAVE")) {
									editjobreport.getNicetohave().add(jobedit);
								}
							});

							editresp.setJobReportData(editjobreport);                    

							sr.setValid(true);
							sr.setStatusCode(1);
							sr.setMessage(message); 

							rsjr.setStatus(sr);
							rsjr.setData(editresp);
							entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);
						}
					}
					else {
						// User doesn't have permission
						sr.setValid(false);
						sr.setStatusCode(11);
						sr.setMessage("User doesn't have permission to access this resource");
						rsjr.setData(null);
						rsjr.setStatus(sr);
						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.FORBIDDEN);
					}

				} catch(NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());            
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthenticated Token Or NULL Or Unauthenticated User");
					rsjr.setData(null);
					rsjr.setStatus(sr);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);                    

				} catch(Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());            
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthenticated Token Or Unauthenticated User");
					rsjr.setData(null);
					rsjr.setStatus(sr);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
				}           
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());            
				sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthenticated Token Or Unauthenticated User");
				rsjr.setData(null);
				rsjr.setStatus(sr);
				entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
			} 
			}
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rsjr.setData(null);
				rsjr.setStatus(sr);		
				entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");	
			rsjr.setData(null);
			rsjr.setStatus(sr);
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
		} 			

		return entity;
	}



	@PostMapping("/editjobsmithreports")
	public  ResponseEntity<ResponseSaveJobsmithReports>  editJobsmithReports(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody JobsmithReportRequest data, HttpServletRequest request){
		String message = "";
		DBUpdate dbu = new DBUpdate();

		ResponseEntity<ResponseSaveJobsmithReports> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponseSaveJobsmithReports rsjr = new ResponseSaveJobsmithReports();
		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(data.getUserid());
		
		int companyid = data.getCompanyid()	;

		if(validToken)
		{
			if(validAccessToken)
			{
				try {
					System.out.println(data.getUseraccountid());
					uaa = uaar.getReferenceByUserid(data.getUserid());  
					System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUserid());
					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());  

					// Check permission
					String permissionQuery = "SELECT jat.jobsmith_actionId, jat.jobsmith_functional_endpoint " +
							"FROM jobsmith_user_profile_tbl jup " +
							"JOIN jobsmith_permission_tbl jpt ON jup.jobsmith_profileId = jpt.jobsmith_profileId " +
							"JOIN jobsmith_action_tbl jat ON jpt.jobsmith_actionId = jat.jobsmith_actionId " +
							"JOIN user_accounts ua ON jup.useraccountid = ua.useraccountid " +
							"WHERE ua.userid = ? AND jup.companyid = " + companyid+" AND jpt.IsPermission = 1 AND jup.isAccess = 1";
					List<Map<String, Object>> permissionData = jdbcTemplate.queryForList(permissionQuery, data.getUserid());

					// Get the endpoint being hit
					String endpoint = request.getRequestURI(); // Adjust this based on your endpoint structure

					boolean hasPermission = false;
					for (Map<String, Object> row : permissionData) {
						String functionalEndpoint = (String) row.get("jobsmith_functional_endpoint");						
						if (functionalEndpoint.equals(endpoint)) {
							hasPermission = true;
							break;
						}
					}

					if (hasPermission) {					

						String accountIdQuery = "SELECT ua.useraccountid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = ?";
						List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, data.getUserid());	         	
						Long useraccountId = (Long) accountIdData.get(0).get("useraccountid");
						int useraccountIdInt = useraccountId.intValue();

						data.setUseraccountid(useraccountIdInt);

						dbu.updateRecord(data);				
						int id = data.getJobsmith_reportid();

						ArrayList<Critical> ar = data.getCritical();		
						Iterator itr = ar.iterator();
						System.out.println("Size of Critical : "+ar.size());
						while (itr.hasNext())
						{   
							JobsmithReportCapability jrc = new JobsmithReportCapability();   

							Critical ob = (Critical) itr.next();  

							if(ob.getEditstatus() == 0)
							{

								jrcr.save(jrc.createJobsmithReportCapability(id,Integer.parseInt(ob.getCapabilityid()),ob.getWeightage(),Integer.parseInt(ob.getSequence())));
								jrcr.flush();
								System.out.println("Record added block : "+sr.getMessage());
							}else if(ob.getEditstatus() == -9)
							{
								JobsmithReportRequestEdit jrre = new JobsmithReportRequestEdit();
								jrre = jrre.createObject(ob.getJobsmith_report_capabilityid(), id,ob.getCapabilityid(),ob.getWeightage(),ob.getSequence());
								sr = dbu.updateRecord(jrre, true);
								System.out.println("Record deleted block : "+sr.getMessage());
							}else if(ob.getEditstatus() == -1)
							{
								JobsmithReportRequestEdit jrre = new JobsmithReportRequestEdit();
								jrre = jrre.createObject(ob.getJobsmith_report_capabilityid(), id,ob.getCapabilityid(),ob.getWeightage(),ob.getSequence());
								sr = dbu.updateRecord(jrre, false);
								System.out.println("Record update block : "+sr.getMessage());
							}else
							{
								System.out.println("Nothing to do anything");
							}
						}

						ArrayList<Important> ari = data.getImportant();		
						Iterator itri = ari.iterator();
						System.out.println("Size of Critical : "+ari.size());
						while (itri.hasNext())
						{   
							JobsmithReportCapability jrc = new JobsmithReportCapability();    	
							Important ob = (Important) itri.next();  

							if(ob.getEditstatus() == 0)
							{
								jrcr.save(jrc.createJobsmithReportCapability(id,Integer.parseInt(ob.getCapabilityid()),ob.getWeightage(),Integer.parseInt(ob.getSequence())));
								jrcr.flush();
								System.out.println("Record added block : "+sr.getMessage());
							}else if(ob.getEditstatus() == -9)
							{
								JobsmithReportRequestEdit jrre = new JobsmithReportRequestEdit();
								jrre = jrre.createObject(ob.getJobsmith_report_capabilityid(), id,ob.getCapabilityid(),ob.getWeightage(),ob.getSequence());
								sr = dbu.updateRecord(jrre, true);
								System.out.println("Record deleted block : "+sr.getMessage());
							}else if(ob.getEditstatus() == -1)
							{
								JobsmithReportRequestEdit jrre = new JobsmithReportRequestEdit();
								jrre = jrre.createObject(ob.getJobsmith_report_capabilityid(), id,ob.getCapabilityid(),ob.getWeightage(),ob.getSequence());
								sr = dbu.updateRecord(jrre, false);
								System.out.println("Record update block : "+sr.getMessage());
							}else
							{
								System.out.println("Nothing to do anything");
							}				
						}  

						ArrayList<Nicetohave> arn = data.getNicetohave();		
						Iterator itrn = arn.iterator();
						System.out.println("Size of Critical : "+ar.size());
						while (itrn.hasNext())
						{   
							JobsmithReportCapability jrc = new JobsmithReportCapability();    	
							Nicetohave ob = (Nicetohave) itrn.next();        	

							if(ob.getEditstatus() == 0)
							{
								jrcr.save(jrc.createJobsmithReportCapability(id,Integer.parseInt(ob.getCapabilityid()),ob.getWeightage(),Integer.parseInt(ob.getSequence())));
								jrcr.flush();
								System.out.println("Record added block : "+sr.getMessage());
							}else if(ob.getEditstatus() == -9)
							{
								JobsmithReportRequestEdit jrre = new JobsmithReportRequestEdit();
								jrre = jrre.createObject(ob.getJobsmith_report_capabilityid(), id,ob.getCapabilityid(),ob.getWeightage(),ob.getSequence());
								sr = dbu.updateRecord(jrre, true);
								System.out.println("Record deleted block : "+sr.getMessage());
							}else if(ob.getEditstatus() == -1)
							{
								JobsmithReportRequestEdit jrre = new JobsmithReportRequestEdit();
								jrre = jrre.createObject(ob.getJobsmith_report_capabilityid(), id,ob.getCapabilityid(),ob.getWeightage(),ob.getSequence());
								sr = dbu.updateRecord(jrre, false);
								System.out.println("Record update block : "+sr.getMessage());
							}else
							{
								System.out.println("Nothing to do anything");
							}

						}  

						message = "Job report saved successfully = "+id;    

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage(message);    	

						rsjr.setStatus(sr);

						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);					
					}
					else
					{
						// User doesn't have permission to update this report
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("User doesn't have permission to get other user report");
						rsjr.setData(null);
						rsjr.setStatus(sr);
						entity = new ResponseEntity<>(rsjr, headers, HttpStatus.FORBIDDEN);
					}
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");

					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.NOT_FOUND);
				}  
			}
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rsjr.setStatus(sr);		
				entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);			
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			rsjr.setStatus(sr);
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
		} 
		return entity;
	} 
	
	@PostMapping("/getjobtitle")
	public ResponseEntity<ResponseJobTitle> jobTitleExists(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData requestData)
	{

		ResponseEntity<ResponseJobTitle> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponseJobTitle rjt = new ResponseJobTitle();
		
		StatusResponse sr = new StatusResponse();    	

		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(requestData.getUserid());

		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					try
					{ 
						uaa = uaar.getReferenceByUserid(requestData.getUserid());   
						System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+requestData.getUserid());
						ui.setFirstname(uaa.getFirstname());
						ui.setLastname(uaa.getLastname());
						ui.setStatus(uaa.getStatus());
						ui.setStatusdate(uaa.getStatusdate());   
						
						DBSearch dbs = new DBSearch();

						List<JobTitleResponse> jobTitleResponse = dbs.getObjects(requestData.getFindValue(),requestData.getCompanyid());

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Job Title List");      		    	

						rjt.setData(jobTitleResponse);
						rjt.setStatus(sr);

						entity = new ResponseEntity<>(rjt, headers, HttpStatus.OK);	    				
					}catch(NullPointerException npex)
					{
						npex.printStackTrace();
						System.out.println(npex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
						rjt.setData(null);
						rjt.setStatus(sr);
						entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);    				

					}catch(Exception ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or Unauthentic User");
						rjt.setData(null);
						rjt.setStatus(sr);
						entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);
					}  		
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rjt.setData(null);
					rjt.setStatus(sr);
					entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);
				}
			}    	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rjt.setData(null);
				rjt.setStatus(sr);		
				entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			rjt.setStatus(sr);
			entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);
		}		

		return entity;
	}
	
	
	@SuppressWarnings("deprecation")
	@PostMapping("/getPermission")
	public ResponseEntity<ResponsePermission> getPermission(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData requestData)
	{
		ResponseEntity<ResponsePermission> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponsePermission rjt = new ResponsePermission();
		AccessPermissionResponse apr = new AccessPermissionResponse();
		PermissionResponse pr = new PermissionResponse();
		actionPermi actionPer = new actionPermi();
		
		StatusResponse sr = new StatusResponse();    	

		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		JwtUserToken jut = new JwtUserToken();
		boolean validAccessToken = false;
		validAccessToken = jut.isValidAccessToken(requestData.getUserid());
		
		 int companyid = requestData.getCompanyid();

		if(validToken)
		{
			if(validAccessToken)
			{		
				try {
					try
					{ 
						uaa = uaar.getReferenceByUserid(requestData.getUserid());   
						System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+requestData.getUserid());
						ui.setFirstname(uaa.getFirstname());
						ui.setLastname(uaa.getLastname());
						ui.setStatus(uaa.getStatus());
						ui.setStatusdate(uaa.getStatusdate());   
						
						String accountIdQuery = "SELECT ua.useraccountid FROM user_accounts ua JOIN jobsmith_report_tbl jrt ON ua.useraccountid = jrt.useraccountid WHERE userid = ?";
						List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, requestData.getUserid());	         	
						Long profileAccountId = (Long) accountIdData.get(0).get("useraccountid");						
						//int profileAccountIdInt = profileAccountId.intValue();
						
						
						
						String sqlQuery1 = "SELECT isAccess from jobsmith_user_profile_tbl WHERE companyid ="+companyid+" AND useraccountid = "+profileAccountId;

						System.out.println(sqlQuery1);
						
						jdbcTemplate.query(sqlQuery1, new Object[] {}, rs -> {
							pr.setIsAccess(rs.getInt("isAccess"));
						});
						
						String sqlQuery2 = "SELECT jat.jobsmith_actionname FROM jobsmith_action_tbl jat"
								+ " INNER JOIN jobsmith_permission_tbl jpt ON jat.jobsmith_actionId = jpt.jobsmith_actionId "
								+ " INNER JOIN jobsmith_user_profile_tbl jut ON jpt.jobsmith_profileId = jut.jobsmith_profileId "
								+ " WHERE jut.companyid ="+companyid+" AND jut.useraccountid = "+profileAccountId+"  AND jpt.IsPermission=1 ";
						
						System.out.println(sqlQuery2);
						
						jdbcTemplate.query(sqlQuery2, new Object[] {}, rs -> {
									
							actionPer.setName(rs.getString("jobsmith_actionname"));
							
							pr.getAction().add(actionPer);
						});

						apr.setPermissionResponse(pr);
						

						sr.setValid(true);
						sr.setStatusCode(1);
						sr.setMessage("Job List");      		    	

						rjt.setData(apr);
						rjt.setStatus(sr);

						entity = new ResponseEntity<>(rjt, headers, HttpStatus.OK);	    				
					}catch(NullPointerException npex)
					{
						npex.printStackTrace();
						System.out.println(npex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
						rjt.setData(null);
						rjt.setStatus(sr);
						entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);    				

					}catch(Exception ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getMessage());			
						sr.setValid(false);
						sr.setStatusCode(0);
						sr.setMessage("Unauthentic Token Or Unauthentic User");
						rjt.setData(null);
						rjt.setStatus(sr);
						entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);
					}  		
				}catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());			
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");
					rjt.setData(null);
					rjt.setStatus(sr);
					entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);
				}
			}    	
			else
			{
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				rjt.setData(null);
				rjt.setStatus(sr);		
				entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);
			}
		}
		else
		{
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");			
			rjt.setStatus(sr);
			entity = new ResponseEntity<>(rjt, headers, HttpStatus.UNAUTHORIZED);
		}		

		return entity;
		
	}
}