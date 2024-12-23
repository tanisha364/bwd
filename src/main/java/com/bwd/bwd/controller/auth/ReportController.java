package com.bwd.bwd.controller.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.controller.rest.UserController;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.PackageReport;
import com.bwd.bwd.response.PackageReportResponse;
import com.bwd.bwd.response.PdfReport;
import com.bwd.bwd.response.Report;
import com.bwd.bwd.response.ReportpdfResponse;
import com.bwd.bwd.response.ResponsePackageReport;
import com.bwd.bwd.response.ResponseReport;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.serviceimpl.JwtUserToken;

@CrossOrigin("*")
@RequestMapping(path = "/report", produces = "application/json")
@RestController
public class ReportController {

	@Autowired
	UserAuthController auc;		
	
	@Autowired
	UserAccountsAuthRepo uaar;	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserController jut;	

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
	
	@PostMapping("/getmypackage")
	public ResponseEntity<ResponsePackageReport> getReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {
	    ResponseEntity<ResponsePackageReport> entity;
	    HttpHeaders headers = new HttpHeaders();

	    ResponsePackageReport rpr = new ResponsePackageReport();
	    StatusResponse sr = new StatusResponse();
	    PackageReportResponse prr = new PackageReportResponse();
	    List<PackageReport> packageReports = new ArrayList<>();
	  
	    boolean validToken = checkToken(authorizationHeader);
	    boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

	    if (validToken) {
	        if (validAccessToken) {
	            try {

	            	
	        		String uidQuery = "SELECT useraccountid FROM user_accounts WHERE userid = ?";						
					int UID = jdbcTemplate.queryForObject(uidQuery, Integer.class,  data.getUserid());
					
	            	String option = "SELECT optionid FROM useroption_tbl WHERE archived=0 and useraccountid = "+UID;
	                
	            	List<Map<String, Object>> check = jdbcTemplate.queryForList(option);
	            	System.out.println(check);
	            	for (Map<String, Object> row : check) {
	            		Integer op = (Integer) row.get("optionid");
	            		System.out.println(op);

	            		String packageid = "SELECT packageid FROM option_tbl WHERE `option` = ?";
	            		int pi = jdbcTemplate.queryForObject(packageid, Integer.class, new Object[]{op});

	            		PackageReport pr = new PackageReport();
	            		System.out.println(pi);
	            		if(pi>0) {

	            			String ProductName = "SELECT Pro_ProductName FROM product_tbl WHERE Pro_ProductId = ?";
	            			String pn = jdbcTemplate.queryForObject(ProductName, String.class, new Object[]{pi});		                

	            			pr.setPackageName(pn);

	            		}else if(pi<0) {

	            			pi = Math. abs(pi);
	            			System.out.println("......"+pi);
	            			String ProductName = "SELECT custompackname FROM custompackage_tbl WHERE archived=0 and custompackid = ?";
	            			String pn = jdbcTemplate.queryForObject(ProductName, String.class, new Object[]{pi});

	            			pr.setPackageName(pn);
	            		}
	            		
	            		List<Report> jpr = getReport(pi);
	            		pr.setReports(jpr);

	            		packageReports.add(pr);
	            	}	                	               

	            	prr.setPackages(packageReports);

	                sr.setValid(true);
	                sr.setStatusCode(1);
	                sr.setMessage("Report with Authentic Token");

	                rpr.setStatus(sr);
	                rpr.setData(prr);
	                entity = new ResponseEntity<>(rpr, headers, HttpStatus.OK);
	                
	                
	            } catch (NullPointerException npex) {
	                npex.printStackTrace();
	                System.out.println(npex.getMessage());
	                sr.setValid(false);
	                sr.setStatusCode(0);
	                sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
	                rpr.setData(null);
	                rpr.setStatus(sr);
	                entity = new ResponseEntity<>(rpr, headers, HttpStatus.UNAUTHORIZED);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	                System.out.println(ex.getMessage());
	                sr.setValid(false);
	                sr.setStatusCode(0);
	                sr.setMessage("Unauthentic Token Or Unauthentic User");
	                rpr.setData(null);
	                rpr.setStatus(sr);
	                entity = new ResponseEntity<>(rpr, headers, HttpStatus.UNAUTHORIZED);
	            }
	        } else {
	            sr.setValid(false);
	            sr.setStatusCode(21);
	            sr.setMessage("Unauthentic Access Token");
	            rpr.setData(null);
	            rpr.setStatus(sr);
	            entity = new ResponseEntity<>(rpr, headers, HttpStatus.UNAUTHORIZED);
	        }
	    } else {
	        sr.setValid(false);
	        sr.setStatusCode(20);
	        sr.setMessage("Unauthentic Token");
	        rpr.setData(null);
	        rpr.setStatus(sr);
	        entity = new ResponseEntity<>(rpr, headers, HttpStatus.UNAUTHORIZED);
	    }

	    return entity;
	}

	public List<Report> getReport(int pi) {
	    List<Report> listJPR = new ArrayList<>();

	    String ReportId = "SELECT ProRep_ReportId FROM productreport_tbl WHERE ProRep_ProductId = ?";
	    List<Map<String, Object>> riList = jdbcTemplate.queryForList(ReportId, new Object[]{pi});

	    for (Map<String, Object> riMap : riList) {
	        int ri = (int) riMap.get("ProRep_ReportId");

	        String ReportName = "SELECT Rep_ReportName FROM reports_tbl WHERE Rep_ReportId = ?";
	        List<Map<String, Object>> rnList = jdbcTemplate.queryForList(ReportName, new Object[]{ri});

	        for (Map<String, Object> rnMap : rnList) {
	            String reportName = (String) rnMap.get("Rep_ReportName");

	            Report report = new Report();
	            report.setReportName(reportName);
	            report.setReportId(ri);

	            listJPR.add(report);
	        }
	    }

	    return listJPR;
	}
	
	@SuppressWarnings("deprecation")
	@PostMapping("/viewmyreport")
	public ResponseEntity<ResponseReport> getReportpdf(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {
	    ResponseEntity<ResponseReport> entity;
	    HttpHeaders headers = new HttpHeaders();

	    ResponseReport rr = new ResponseReport();
	    StatusResponse sr = new StatusResponse();
	    ReportpdfResponse rpr = new ReportpdfResponse();

	    boolean validToken = checkToken(authorizationHeader);

	    boolean validAccessToken = auc.isValidAccessToken(data.getUserid());
	    
	    if (validToken) {
	        if (validAccessToken) {
	            try {
	            	
	            	String report = "select Rep_ReportName, Rep_ReportType, Rep_ReportIntroduction,Rep_ReportCode from reports_tbl where Rep_ReportId =" + data.getReportId();
	            	List<Map<String, Object>> rows = jdbcTemplate.queryForList(report);

					for (Map<String, Object> row : rows) {
						String ReportName = (String) row.get("Rep_ReportName");
						String ReportIntroduction = (String) row.get("Rep_ReportIntroduction");
						String ReportType = (String) row.get("Rep_ReportType");
						String ReportCode = (String) row.get("Rep_ReportCode");
						
						  rpr.setReport_Name(ReportName);
						  rpr.setReport_Introduction(ReportIntroduction);
						  rpr.setReport_Type(ReportType);
						  rpr.setReport_code(ReportCode);
					}
	              
					String disclaimer = "select text from pages where pageid = 26";
					String Disclaimer = jdbcTemplate.queryForObject(disclaimer, String.class);						
					rpr.setReport_Disclaimer(Disclaimer);					
					
					String qi = "select text from pages where pageid = 62";
					String QuickScreen_Intro = jdbcTemplate.queryForObject(qi, String.class);	
					rpr.setQuickScreen_Intro(QuickScreen_Intro);
					
					String si = "select text from pages where pageid = 63";
					String StrengthChart_Intro = jdbcTemplate.queryForObject(si, String.class);	
					rpr.setStrengthChart_Intro(StrengthChart_Intro);
					
					String name = "select CONCAT(firstname,' ', lastname) from user_accounts where userid = ?";
					String username = jdbcTemplate.queryForObject(name,new Object[]{data.getUserid()}, String.class);						
					rpr.setUser_Name(username);					
					
	                List<PdfReport> reports = jdbcTemplate.query("CALL `2sidereportcolor_select_new`(?, ?, ?, ?)",
	                		new Object[]{0, data.getRegnum(), data.getReportId(), data.getCompanyid()}, 
	                		new ResultSetExtractor<List<PdfReport>>() {

	                	@Override
	                	public List<PdfReport> extractData(ResultSet rs) throws SQLException,  
	                	DataAccessException {  
	                		List<PdfReport> list = new ArrayList<>();
	                		while(rs.next()){  
	                			PdfReport report = new PdfReport();
	                			report.setCapability(rs.getString(1));
	                			report.setLeft(rs.getString(2));
	                			report.setRight(rs.getString(3));	                		
	                			report.setText(rs.getString(4));
	                			report.setWeight(rs.getString(5));
	                			report.setScore(rs.getInt(7));
	                			report.setColor1(rs.getString(8));
	                			report.setColor2(rs.getString(9));
	                			report.setColor3(rs.getString(10));
	                			report.setColor4(rs.getString(11));
	                			report.setColor5(rs.getString(12));
	                			report.setColor6(rs.getString(13));	                			
	                			report.setSequence(rs.getInt(14));

	                			list.add(report);  
	                		}  
	                		return list;  
	                	}  
	                });  
	                
	                rpr.setReportId(data.getReportId());	              
	                rpr.setReport(reports);
	               	                
	                sr.setValid(true);
	                sr.setStatusCode(1);
	                sr.setMessage("Report fetched successfully");

	                rr.setStatus(sr);
	                rr.setData(rpr);
	                entity = new ResponseEntity<>(rr, headers, HttpStatus.OK);
	            } catch (NullPointerException npex) {
	                npex.printStackTrace();
	                System.out.println(npex.getMessage());
	                sr.setValid(false);
	                sr.setStatusCode(0);
	                sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");
	                rr.setData(null);
	                rr.setStatus(sr);
	                entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
	            } catch (Exception ex) {
	                ex.printStackTrace();
	                System.out.println(ex.getMessage());
	                sr.setValid(false);
	                sr.setStatusCode(0);
	                sr.setMessage("Unauthentic Token Or Unauthentic User");
	                rr.setData(null);
	                rr.setStatus(sr);
	                entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
	            }
	        } else {
	            sr.setValid(false);
	            sr.setStatusCode(21);
	            sr.setMessage("Unauthentic Access Token");
	            rr.setData(null);
	            rr.setStatus(sr);
	            entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
	        }
	    } else {
	        sr.setValid(false);
	        sr.setStatusCode(20);
	        sr.setMessage("Unauthentic Token");
	        rr.setData(null);
	        rr.setStatus(sr);
	        entity = new ResponseEntity<>(rr, headers, HttpStatus.UNAUTHORIZED);
	    }

	    return entity;
	}

	
}

