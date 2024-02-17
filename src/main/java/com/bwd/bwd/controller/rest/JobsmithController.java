package com.bwd.bwd.controller.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import com.bwd.bwd.model.Category;

import com.bwd.bwd.model.auth.UserAccountsAuth;
import com.bwd.bwd.repository.CategoryRepo;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.repository.jobsmith.JobsmithCapabilityRepo;
import com.bwd.bwd.repository.jobsmith.JobsmithReportCapabilityRepo;
import com.bwd.bwd.repository.jobsmith.JobsmithReportRepo;
import com.bwd.bwd.request.JobsmithReportRequest;
import com.bwd.bwd.request.JobsmithReportRequestEdit;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.CapabilityInfoResponse;
import com.bwd.bwd.response.CapabilityResponse;
import com.bwd.bwd.response.CategoryInfoResponse;
import com.bwd.bwd.response.CategoryResponse;
import com.bwd.bwd.response.EditJobResponse;
import com.bwd.bwd.response.Editjobreport;
import com.bwd.bwd.response.Jobedit;
import com.bwd.bwd.response.JobsmithPremadeReport;
import com.bwd.bwd.response.JobsmithPremadeReportResponse;
import com.bwd.bwd.response.PreMadeReport;
import com.bwd.bwd.response.PreMadeReportResponse;
import com.bwd.bwd.response.ReportInfo;
import com.bwd.bwd.response.ReportInfoResponse;
import com.bwd.bwd.response.ReportResponse;
import com.bwd.bwd.response.ResponseEditJob;
import com.bwd.bwd.response.ResponseJobsmithPremadeReport;
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
import com.bwd.bwd.controller.auth.UserAuthController;
import com.bwd.bwd.db.DBOperation;
import com.bwd.bwd.db.DBSearch;
import com.bwd.bwd.db.DBUpdate;
import com.bwd.bwd.model.jobsmith.Critical;
import com.bwd.bwd.model.jobsmith.Important;
import com.bwd.bwd.model.jobsmith.JobSearchResult;
import com.bwd.bwd.model.jobsmith.JobsmithCapabilities;
import com.bwd.bwd.model.jobsmith.JobsmithReport;
import com.bwd.bwd.model.jobsmith.JobsmithReportCapability;
import com.bwd.bwd.model.jobsmith.Nicetohave;
import com.bwd.bwd.model.jobsmith.Reports;


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
    	ResponseEntity<UserInfoResponse> entityUir = null;
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
    	
    	if(validToken)
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

				entityUir = new ResponseEntity<>(uir, headers, HttpStatus.OK);
			} catch (Exception ex) {				
				System.out.println(ex.getMessage());
			
	    		sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
	    		list = null;				

				entityUir = new ResponseEntity<>(uir, headers, HttpStatus.NOT_FOUND);
			}
		}
    	else
    	{
    		sr.setValid(false);
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
    	}
    	
    	catresp.setCategories(list);
    	
    	catinforesp.setData(catresp);
    	catinforesp.setStatus(sr);
    	
    	entity = new ResponseEntity<>(catinforesp, headers, HttpStatus.OK);
    	
    	return entity;
    } 

	@PostMapping("/capabilities")
    public ResponseEntity<CapabilityInfoResponse> getCapability(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data)
    {
    	ResponseEntity<CapabilityInfoResponse> entity;
    	ResponseEntity<UserInfoResponse> entityUir = null;
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
    	
    	if(validToken)
		{	
    		try {
//				System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUseraccountid());
				uaa = uaar.getReferenceByUserid(data.getUserid());
				System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUserid());
				ui.setFirstname(uaa.getFirstname());
				ui.setLastname(uaa.getLastname());
				ui.setStatus(uaa.getStatus());
				ui.setStatusdate(uaa.getStatusdate());

				udr.setUserinfo(ui);
				uir.setStatus(sr);
				uir.setData(udr);
				
//				list = jcaprepo.findAll();   //.findCapaJobsmithCapabilities(type);
				try
				{
					int categoryid = data.getCategoryId();
					list = jcaprepo.findByRkaid(categoryid);		    		
					
		    		sr.setValid(true);
					sr.setStatusCode(1);
					sr.setMessage("Category List with Authentic Token ");				

					entityUir = new ResponseEntity<>(uir, headers, HttpStatus.OK);					
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage());			
		    		sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Fetch Error");
		    		list = null;				

					entityUir = new ResponseEntity<>(uir, headers, HttpStatus.NOT_FOUND);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());			
	    		sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
	    		list = null;				

				entityUir = new ResponseEntity<>(uir, headers, HttpStatus.NOT_FOUND);
			}
		}
    	else
    	{
    		sr.setValid(false);
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
    	}
    	
    	catresp.setCapbilities(list);
    	catInfoResp.setStatus(sr);
    	catInfoResp.setData(catresp);
    	
    	
    	
    	entity = new ResponseEntity<>(catInfoResp, headers, HttpStatus.OK);
    	
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
    	ResponseEntity<UserInfoResponse> entityUir = null;
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
    	
    	if(validToken)
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
				
//				list = jcaprepo.findAll();   //.findCapaJobsmithCapabilities(type);
				try
				{
					DBSearch dbs = new DBSearch();
					int categoryid = data.getCategoryId();
					System.out.println(" ********************* "+categoryid);
					list = dbs.findByCategoryType(categoryid); //findAll(); //  findByCategoryType(categoryid);    //       	
					listR = getResponseList(list,categoryid);
					
		    		sr.setValid(true);
					sr.setStatusCode(1);
					sr.setMessage("Category List with Authentic Token ");				

					entityUir = new ResponseEntity<>(uir, headers, HttpStatus.OK);					
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage());			
		    		sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Fetch Error");
		    		list = null;				

					entityUir = new ResponseEntity<>(uir, headers, HttpStatus.NOT_FOUND);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());			
	    		sr.setValid(false);
				sr.setStatusCode(0);
				sr.setMessage("Unauthentic Token Or Unauthentic User");
	    		list = null;				

				entityUir = new ResponseEntity<>(uir, headers, HttpStatus.NOT_FOUND);
			}
		}
    	else
    	{
    		sr.setValid(false);
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
    	}
    	
    	reportResp.setJobreports(listR); // setCapbilities(list);
    	reportInfoResp.setStatus(sr);
    	reportInfoResp.setData(reportResp);
    	
    	entity = new ResponseEntity<>(reportInfoResp, headers, HttpStatus.OK);
    	
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
	public  ResponseEntity<ResponseSaveJobsmithReports>  saveJobsmithReports(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody JobsmithReportRequest data){
		String message = "";
		
		ResponseEntity<ResponseSaveJobsmithReports> entity;
    	HttpHeaders headers = new HttpHeaders();
    	
    	ResponseSaveJobsmithReports rsjr = new ResponseSaveJobsmithReports();
    	StatusResponse sr = new StatusResponse();
    	
		JobsmithReportRequest jobrr = new JobsmithReportRequest();		
		JobsmithReport jr = new JobsmithReport();			
		    	
		UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();
    	
		UserInfo ui = new UserInfo();    	
    	
    	boolean validToken = false;
    	
    	validToken = checkToken(authorizationHeader);
    	
    	if(validToken)
		{
    		try {
    			System.out.println(data.getUseraccountid());
    			uaa = uaar.getReferenceByUserid(data.getUserid());  
				System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUserid());
				ui.setFirstname(uaa.getFirstname());
				ui.setLastname(uaa.getLastname());
				ui.setStatus(uaa.getStatus());
				ui.setStatusdate(uaa.getStatusdate());   
   		
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
			rsjr.setStatus(sr);		
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);			
    	}
		
		rsjr.setStatus(sr);
		
		entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);
		
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
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();
    	
		UserInfo ui = new UserInfo();    	
    	
    	boolean validToken = false;
    	
    	validToken = checkToken(authorizationHeader);
    	
    	if(validToken)
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
			rjpr.setData(null);
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
	public ResponseEntity<ResponseSavedJobReport> getSavedJobReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data)
	{
		System.out.println("C  A     L        L -------- getsavedjobreport");
		ResponseEntity<ResponseSavedJobReport> entity;
    	HttpHeaders headers = new HttpHeaders();
    	
    	ResponseSavedJobReport rsjr = new ResponseSavedJobReport();
    	StatusResponse sr = new StatusResponse();
    	SavedJobReportResponse jprr = new SavedJobReportResponse();
    	
    	UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();
    	
		UserInfo ui = new UserInfo();    	
    	
    	boolean validToken = false;
    	
    	validToken = checkToken(authorizationHeader);
    	
    	if(validToken)
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
    				
                	List<SavedJobReport> jpr =  getDataSavedJobReport();
                	jprr.setSavedJobReport(jpr);
                	
    				sr.setValid(true);
    				sr.setStatusCode(1);
    				sr.setMessage("Saved Job Reportt with Authentic Token ");                	
            		
                	rsjr.setStatus(sr);
                	rsjr.setData(jprr);
                	entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);       				
    			}catch(NullPointerException npex)
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
        	rsjr.setData(null);
			rsjr.setStatus(sr);
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
    	} 
    			
		return entity;
	}
	
	public List<SavedJobReport> getDataSavedJobReport()
	{
		DBOperation dbop = new DBOperation();
		List<SavedJobReport> listJPR = new ArrayList<SavedJobReport>();
		String [][]data;		
		String sqlQuery = """	
				SELECT 
					jobsmith_reportid, 
					jobsmith_report_name, 
					report_status, 
					CONCAT (b.firstname, ' ', b.lastname) AS CREATEBY, 
					DATE_FORMAT(a.date_modifed, '%Y-%m-%d %h:%i %p') AS date_modifed, 
					locked 
				FROM 
					jobsmith_report_tbl a 
					INNER JOIN user_accounts b ON b.useraccountid = a.useraccountid 
				WHERE 
					archived = 0
				""";
		System.out.println(sqlQuery);
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			SavedJobReport jpr = new SavedJobReport();
			listJPR.add(jpr.getObject(data[i]));
		}
		
		return listJPR;
	}
	
//	@PostMapping("/getsavedjobreportexpansion")
//	public ResponseEntity<ResponseSavedJobReportExpansion> getSavedJobReportExpansion(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData userData)
//	{
//		ResponseEntity<ResponseSavedJobReportExpansion> entity = null;
//    	HttpHeaders headers = new HttpHeaders();
//    	
//    	ResponseSavedJobReportExpansion respExp = new ResponseSavedJobReportExpansion();
//    	StatusResponse sr = new StatusResponse();    	
//    	SavedJobReportsExpansionResponse jprr = new SavedJobReportsExpansionResponse();
//    	
//    	UserAccountsAuth uaa = new UserAccountsAuth();
//		UserDataResponse udr = new UserDataResponse();
//		UserInfoResponse uir = new UserInfoResponse();
//    	
//		UserInfo ui = new UserInfo();    	
//    	
//    	boolean validToken = false;
//    	
//    	validToken = checkToken(authorizationHeader);
//    	
//    	if(validToken)
//		{
//    		try {
//    			try
//    			{    				
//    				uaa = uaar.getReferenceByUserid(userData.getUserid());  
//    				System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+userData.getUserid());
//    				ui.setFirstname(uaa.getFirstname());
//    				ui.setLastname(uaa.getLastname());
//    				ui.setStatus(uaa.getStatus());
//    				ui.setStatusdate(uaa.getStatusdate());   
//   				
//    		    	List<SavedJobReportsExpansion> jpr =  getDataSavedJobReportExpansion(userData.getReportId());
//    		    	
//		    		sr.setValid(true);
//					sr.setStatusCode(1);
//					sr.setMessage("Saved Job Reports Expansio with Authentic Token");
//    		    	
//    		    	jprr.setSavedJobReportsExpansion(jpr);
//    		    	
//    		    	respExp.setStatus(sr);
//    		    	respExp.setData(jprr);
//    		    	
//    		    	
//    		    	entity = new ResponseEntity<>(respExp, headers, HttpStatus.OK);		
//    				return entity;    				
//    			}catch(NullPointerException npex)
//    			{
//    				npex.printStackTrace();
//        			System.out.println(npex.getMessage());			
//            		sr.setValid(false);
//        			sr.setStatusCode(0);
//        			sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
//        			respExp.setData(null);
//        			respExp.setStatus(sr);
//        			entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);    				
//    				
//    			}catch(Exception ex)
//    			{
//        			ex.printStackTrace();
//        			System.out.println(ex.getMessage());			
//            		sr.setValid(false);
//        			sr.setStatusCode(0);
//        			sr.setMessage("Unauthentic Token Or Unauthentic User");
//        			respExp.setData(null);
//        			respExp.setStatus(sr);
//        			entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);
//    			}  		
//    		}catch (Exception ex) {
//    			ex.printStackTrace();
//    			System.out.println(ex.getMessage());			
//        		sr.setValid(false);
//    			sr.setStatusCode(0);
//    			sr.setMessage("Unauthentic Token Or Unauthentic User");
//    			respExp.setData(null);
//    			respExp.setStatus(sr);
//    			entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);
//    		} 
//		}  
//    	else
//    	{
//    		sr.setValid(false);
//			sr.setStatusCode(0);
//			sr.setMessage("Unauthentic Token");
//			
//			respExp.setData(null);
//			respExp.setStatus(sr);
//			entity = new ResponseEntity<>(respExp, headers, HttpStatus.UNAUTHORIZED);			
//    	}    
//    	return entity;
//	}
	
	@PostMapping("/getsavedjobreportexpansion")
	public ResponseEntity<ResponseSavedJobReportExpansion> getSavedJobReportExpansion(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData userData)
	{
		ResponseEntity<ResponseSavedJobReportExpansion> entity = null;
		HttpHeaders headers = new HttpHeaders();

		ResponseSavedJobReportExpansion respExp = new ResponseSavedJobReportExpansion();
		StatusResponse sr = new StatusResponse();    	
		SavedJobReportsExpansionResponse jprr = new SavedJobReportsExpansionResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if(validToken)
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");

			respExp.setData(null);
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
				  a.jobsmith_reportid = """+reportId;
			
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
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();
    	
		UserInfo ui = new UserInfo();    	
    	
    	boolean validToken = false;
    	
    	validToken = checkToken(authorizationHeader);
    	
    	if(validToken)
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
			sr.setStatusCode(0);
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
	public ResponseEntity<StatusResponse> updateArchive(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data)
	{
		ResponseEntity<StatusResponse> entity;
    	HttpHeaders headers = new HttpHeaders();
    	
    	StatusResponse sr = new StatusResponse();
    	
    	UserAccountsAuth uaa = new UserAccountsAuth();
    	
		UserInfo ui = new UserInfo();    	
    	
    	boolean validToken = false;
    	
    	validToken = checkToken(authorizationHeader);
    	
    	if(validToken)
		{
    		try {
    			try
    			{ 
    				uaa = uaar.getReferenceByUserid(data.getUserid());   
    				
    				ui.setFirstname(uaa.getFirstname());
    				ui.setLastname(uaa.getLastname());
    				ui.setStatus(uaa.getStatus());
			    	ui.setStatusdate(uaa.getStatusdate());      	
			    	
			    	
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");	
			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
    	}		
		return entity;
	}

	
	
	@PostMapping("/udpatereportstatus")
	public ResponseEntity<StatusResponse>udpatereportstatus(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data) {

		ResponseEntity<StatusResponse> entity; HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if(validToken)
		{
			try {
				try
				{ 
					uaa = uaar.getReferenceByUserid(data.getUserid());   

					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());      	

					DBUpdate dbi = new DBUpdate();
					sr = dbi.udpatereportstatus(data);

					if(sr.isValid() == true) { 
						entity = new ResponseEntity<>(sr, headers,HttpStatus.OK); 
					}
					else { 
						entity = new ResponseEntity<>(sr, headers,HttpStatus.UNAUTHORIZED);
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");	
			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
		}		

		return entity;
	}

	@PostMapping("/updatelock")
	public ResponseEntity<StatusResponse> updateLock(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UserData data)
	{
		ResponseEntity<StatusResponse> entity;
    	HttpHeaders headers = new HttpHeaders();
    	
    	StatusResponse sr = new StatusResponse();
    	
    	UserAccountsAuth uaa = new UserAccountsAuth();
    	
		UserInfo ui = new UserInfo();    	
    	
    	boolean validToken = false;
    	
    	validToken = checkToken(authorizationHeader);
    	
    	if(validToken)
		{
    		try {
    			try
    			{ 
    				uaa = uaar.getReferenceByUserid(data.getUserid());   
    				
    				ui.setFirstname(uaa.getFirstname());
    				ui.setLastname(uaa.getLastname());
    				ui.setStatus(uaa.getStatus());
			    	ui.setStatusdate(uaa.getStatusdate());      	
			    	
			    	
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");	
			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
    	}		
		return entity;
	}
	
	@PostMapping("/getsearchjobreport")
	public ResponseEntity<ResponseSavedJobReport> getSearchJobReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data)
	{
		ResponseEntity<ResponseSavedJobReport> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponseSavedJobReport rsjr = new ResponseSavedJobReport();
		StatusResponse sr = new StatusResponse();
		SavedJobReportResponse jprr = new SavedJobReportResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if(validToken)
		{
			try {
				try
				{
					uaa = uaar.getReferenceByUserid(data.getUserid());  					
					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());   

					List<SavedJobReport> jpr = getDataSearchJobReport(data.getJobsmithReportName());
					jprr.setSavedJobReport(jpr);

					sr.setValid(true);
					sr.setStatusCode(1);
					sr.setMessage("Search Job Report with Authentic Token ");                	

					rsjr.setStatus(sr);
					rsjr.setData(jprr);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);       				
				}catch(NullPointerException npex)
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
			rsjr.setData(null);
			rsjr.setStatus(sr);
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
		} 
		return entity;
	}
	
	public List<SavedJobReport> getDataSearchJobReport(String jobsmith_report_name)
	{
		DBOperation dbop = new DBOperation();
		List<SavedJobReport> listJPR = new ArrayList<>();
		String [][]data;		
		String sqlQuery = "SELECT \n" +
				"    jobsmith_reportid, \n" +
				"    jobsmith_report_name, \n" +
				"    report_status, \n" +
				"    CONCAT(b.firstname, ' ', b.lastname) AS createdBy, \n" +
				"    DATE_FORMAT(a.date_modifed, '%Y-%m-%d %h:%i %p') AS date_modifed, \n" +
				"    locked \n" +
				"FROM \n" +
				"    jobsmith_report_tbl a \n" +
				"    INNER JOIN user_accounts b ON b.useraccountid = a.useraccountid \n" +
				"WHERE \n" +
				" archived = 0 AND jobsmith_report_name LIKE '%" + jobsmith_report_name + "%'";

		System.out.println(sqlQuery);
		dbop.setSelectQuery(sqlQuery);		
		dbop.executeSelectQuery();
		data = dbop.fetchRecord();	
		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int i=0;i<=dataLen;i++)
		{
			SavedJobReport jpr = new SavedJobReport();
			listJPR.add(jpr.getObject(data[i]));
		}
		return listJPR;
	}
	
	
	@SuppressWarnings("deprecation")
	@PostMapping("/geteditjobreport")
	public ResponseEntity<ResponseEditJob> getJobReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {
		String message = "";

		ResponseEntity<ResponseEditJob> entity;
		HttpHeaders headers = new HttpHeaders();	

		ResponseEditJob rsjr = new ResponseEditJob();	    	
		EditJobResponse editresp = new EditJobResponse();
		StatusResponse sr = new StatusResponse();

		//List <Editjobreport> editjobreportList = new ArrayList<Editjobreport>();
		Editjobreport editjobreport = new Editjobreport();

		int jobreportid = data.getReportId();		    	

		UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if(validToken)
		{	  
			try {
				try
				{
					uaa = uaar.getReferenceByUserid(data.getUserid());  
					System.out.println(" ----------  "+data.getUserid());
					ui.setFirstname(uaa.getFirstname());
					ui.setLastname(uaa.getLastname());
					ui.setStatus(uaa.getStatus());
					ui.setStatusdate(uaa.getStatusdate());  

					String query1 = "SELECT jobsmith_reportid, jobsmith_report_name, jobsmith_report_note, " +
							"companyid, useraccountid, report_status " +
							"FROM jobsmith_report_tbl " +
							"WHERE archived != -9 AND jobsmith_reportid = ?";
					jdbcTemplate.query(query1, new Object[]{jobreportid}, rs -> {
						editjobreport.setJobreportid(rs.getString("jobsmith_reportid"));
						editjobreport.setJobtitle(rs.getString("jobsmith_report_name"));
						editjobreport.setJobnote(rs.getString("jobsmith_report_note"));
						editjobreport.setCompanyid(rs.getString("companyid"));
						editjobreport.setUseraccountid(rs.getString("useraccountid"));
						editjobreport.setReportstatus(rs.getString("report_status"));
					});

					String query2 = "SELECT a.jobsmith_report_capabilityid,a.capabilityid, b.Cap_Capability, a.weightage, a.sequence " +
							"FROM jobsmith_report_capability_tbl a " +
							"INNER JOIN capability_tbl b ON b.Cap_CapabilityId = a.capabilityid " +
							"INNER JOIN jobsmith_report_tbl c ON c.jobsmith_reportid = a.jobsmith_reportid " +
							"WHERE a.archived != -9 AND a.jobsmith_reportid = ?";
					jdbcTemplate.query(query2, new Object[]{jobreportid}, rs -> {
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

					//editjobreportList.add(editjobreport);
					editresp.setJobReportData(editjobreport);			        

					sr.setValid(true);
					sr.setStatusCode(1);
					sr.setMessage(message); 

					rsjr.setStatus(sr);
					rsjr.setData(editresp);
					entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);	
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
			rsjr.setData(null);
			rsjr.setStatus(sr);
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);
		} 
		return entity;
	}	

	@PostMapping("/editjobsmithreports")
	public  ResponseEntity<ResponseSaveJobsmithReports>  editJobsmithReports(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody JobsmithReportRequest data){
		String message = "";
		DBUpdate dbu = new DBUpdate();

		ResponseEntity<ResponseSaveJobsmithReports> entity;
		HttpHeaders headers = new HttpHeaders();

		ResponseSaveJobsmithReports rsjr = new ResponseSaveJobsmithReports();
		StatusResponse sr = new StatusResponse();

		UserAccountsAuth uaa = new UserAccountsAuth();
		UserDataResponse udr = new UserDataResponse();
		UserInfoResponse uir = new UserInfoResponse();

		UserInfo ui = new UserInfo();    	

		boolean validToken = false;

		validToken = checkToken(authorizationHeader);

		if(validToken)
		{
			try {
				System.out.println(data.getUseraccountid());
				uaa = uaar.getReferenceByUserid(data.getUserid());  
				System.out.println(" xxxxxxxxxxxxxxxxxxx ----------           "+data.getUserid());
				ui.setFirstname(uaa.getFirstname());
				ui.setLastname(uaa.getLastname());
				ui.setStatus(uaa.getStatus());
				ui.setStatusdate(uaa.getStatusdate());   

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
//						System.out.println(id);
//						System.out.println();
//						System.out.println();
//						System.out.println();
//						System.out.println();
						
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
			sr.setStatusCode(0);
			sr.setMessage("Unauthentic Token");
			rsjr.setStatus(sr);		
			entity = new ResponseEntity<>(rsjr, headers, HttpStatus.UNAUTHORIZED);			
		}

		rsjr.setStatus(sr);

		entity = new ResponseEntity<>(rsjr, headers, HttpStatus.OK);

		return entity;
	} 	
	
}