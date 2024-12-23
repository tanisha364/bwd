package com.bwd.bwd.controller.company;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bwd.bwd.controller.auth.UserAuthController;
import com.bwd.bwd.model.auth.UserInvite;
import com.bwd.bwd.repository.UserAccountsAuthRepo;
import com.bwd.bwd.repository.UserInviteRepo;
import com.bwd.bwd.request.InviteRequest;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.Departmentinfo;
import com.bwd.bwd.response.Invite;
import com.bwd.bwd.response.InviteResponse;
import com.bwd.bwd.response.Job;
import com.bwd.bwd.response.JobResponse;
import com.bwd.bwd.response.PackageReport;
import com.bwd.bwd.response.Participantinfo;
import com.bwd.bwd.response.StatusResponse;
import com.bwd.bwd.response.Testinfo;
import com.bwd.bwd.response.TokenResponse;
import com.bwd.bwd.response.TypeResponse;
import com.bwd.bwd.serviceimpl.JwtUserToken;
import com.bwd.bwd.serviceimpl.UserInfoImpl;
@CrossOrigin("*")
@RequestMapping(path = "user/invite", produces = "application/json")
@RestController
public class InviteController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserAuthController auc;		
	
	@Autowired
	UserInviteRepo uir;

	@Autowired
	UserAccountsAuthRepo uaar;	

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


	@PostMapping("")
	public ResponseEntity<InviteResponse> invite(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<InviteResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		StatusResponse sr = new StatusResponse();
		InviteResponse ir = new InviteResponse();
		Invite in = new Invite();

		List<Participantinfo> pp = new ArrayList<>();
		List<PackageReport> prt = new ArrayList<>();
		List<Departmentinfo> di = new ArrayList<>();


		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {

					String query = "select participant_type_id, participant_type from participant_type_tbl where archived = 0" ;		
					List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

					for (Map<String, Object> row : rows) {						
						int type_id = (int) row.get("participant_type_id");
						String participant_type = (String) row.get("participant_type");

						Participantinfo eiObj = new Participantinfo();
						eiObj.setParticipant_type_id(type_id);
						eiObj.setParticipant_type(participant_type);	                    
						pp.add(eiObj);
					}

					String query2 = "select departmentid, departmentname from department_tbl where companyid ="+ data.getCompanyid();					
					List<Map<String, Object>> rows2 = jdbcTemplate.queryForList(query2);

					for (Map<String, Object> row : rows2) {						
						int id = (int) row.get("departmentid");
						String type = (String) row.get("departmentname");

						Departmentinfo eiObj = new Departmentinfo();
						eiObj.setDepartmentid(id);
						eiObj.setDepartmentname(type);	                    
						di.add(eiObj);
					}


					String query3 ="select comptokenid from companytoken_tbl where flag=1 and archived=0 and companyid ="+data.getCompanyid();
					List<Map<String, Object>> rows3 =jdbcTemplate.queryForList(query3);

					for (Map<String, Object> row : rows3) {						
						int id = (int) row.get("comptokenid");

						PackageReport pr = new PackageReport();
						pr.setComptokenid(id);

						String query4 ="select productid from tokenprod_tbl where comptokenid = ?";
						int pid = jdbcTemplate.queryForObject(query4, Integer.class, id);

						if(pid>0) {

							String ProductName = "SELECT Pro_ProductName FROM product_tbl WHERE Pro_ProductId = ?";
							String pn = jdbcTemplate.queryForObject(ProductName, String.class, new Object[]{pid});

							pr.setProductId(pid);
							pr.setPackageName(pn);

						}else if(pid<0) {

							pid = Math. abs(pid);

							String ProductName = "SELECT custompackname FROM custompackage_tbl WHERE archived=0 and custompackid = ?";
							String pn = jdbcTemplate.queryForObject(ProductName, String.class, new Object[]{pid});

							pr.setProductId(pid);
							pr.setPackageName(pn);

						}	

						prt.add(pr);					
					}

					in.setParticipant(pp);
					in.setPackage(prt);
					in.setDepartment(di);

					ir.setData(in);	

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  	

					ir.setStatus(sr);
					entity = new ResponseEntity<>(ir, headers, HttpStatus.OK);        				
				}catch (NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");             
					entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");              
					entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");    
			entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}




	@PostMapping("/jobInv")
	public ResponseEntity<JobResponse> job(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<JobResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		StatusResponse sr = new StatusResponse();
		JobResponse ir = new JobResponse();

		List<Job> jb = new ArrayList<>();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {
					String query = "select jobid, jobname from job_tbl where departmentid ="+ data.getDepartmentid();		
					List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

					for (Map<String, Object> row : rows) {						
						int id = (int) row.get("jobid");
						String type = (String) row.get("jobname");

						Job eiObj = new Job();
						eiObj.setJobid(id);
						eiObj.setJobname(type);	                    
						jb.add(eiObj);
					}	

					ir.setData(jb);	

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  	

					ir.setStatus(sr);
					entity = new ResponseEntity<>(ir, headers, HttpStatus.OK);        				
				}catch (NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");             
					entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");              
					entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");    
			entity = new ResponseEntity<>(ir, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}


	@PostMapping("/TestInv")
	public ResponseEntity<TypeResponse> type(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody UserData data) {

		ResponseEntity<TypeResponse> entity = null;
		HttpHeaders headers = new HttpHeaders();
		StatusResponse sr = new StatusResponse();
		TypeResponse tr = new TypeResponse();

		List<Testinfo> prt = new ArrayList<>();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

		if (validToken) {
			if (validAccessToken) {
				try {
					String query = "select landingassessmentid from companytoken_assessment_tbl where comptokenid ="+ data.getComptokenid();		
					List<Map<String, Object>> rows3 =jdbcTemplate.queryForList(query);

					for (Map<String, Object> row : rows3) {						
						int id = (int) row.get("landingassessmentid");						

						String sql = "select description from landingassessment_tbl where landingassessmentid=" + id;
						String description = jdbcTemplate.queryForObject(sql, String.class);


						Testinfo test = new Testinfo();

						test.setLandingassessmentid(id);
						test.setDescription(description);

						prt.add(test);

					}

					sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  	

					tr.setStatus(sr);
					tr.setData(prt);

					entity = new ResponseEntity<>(tr, headers, HttpStatus.OK);        				
				}catch (NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");             
					entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");              
					entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");
				entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");    
			entity = new ResponseEntity<>(tr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
	
	@PostMapping("/saveinvitation")
	public ResponseEntity<StatusResponse> saveinvite(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody InviteRequest data) {

		ResponseEntity<StatusResponse> entity;
		HttpHeaders headers = new HttpHeaders();

		StatusResponse sr = new StatusResponse();

		boolean validToken = checkToken(authorizationHeader);

		boolean validAccessToken = auc.isValidAccessToken(data.getUserid());

	    if (validToken) {
	        if (validAccessToken) {
	            try {
	            	String code = UserInfoImpl.generateUniqueLinkId(20);
	            	
	            	String uid = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
	            	int UID = jdbcTemplate.queryForObject(uid, Integer.class, data.getUserid());
	            	
	            	UserInvite ui = new UserInvite();
	            	uir.save(ui.create(data.getCompanyid(), data.getLandingassessmentid(), UID, data.getInvitee_email_type(), data.getInvitee_firstname(), data.getInvitee_lastname(), data.getInvitee_email(), data.getInvitee_tel(), data.getInvitee_tel_code(), data.getParticipant_type(), data.getComptokenid(), data.getJobid(), code, data.getIs_admin()));
	            	
	            	sr.setValid(true);    
					sr.setStatusCode(1);
					sr.setMessage("Authenticate User Success");  														
					entity = new ResponseEntity<>(sr, headers, HttpStatus.OK);        				
				}catch (NullPointerException npex) {
					npex.printStackTrace();
					System.out.println(npex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or NULL Or Unauthentic User");             
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(ex.getMessage());
					sr.setValid(false);
					sr.setStatusCode(0);
					sr.setMessage("Unauthentic Token Or Unauthentic User");              
					entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
				}
			} else {
				sr.setValid(false);
				sr.setStatusCode(21);
				sr.setMessage("Unauthentic Access Token");

				entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
			}
		} else {
			sr.setValid(false);
			sr.setStatusCode(20);
			sr.setMessage("Unauthentic Token");    
			entity = new ResponseEntity<>(sr, headers, HttpStatus.UNAUTHORIZED);
		}
		return entity;
	}
}
