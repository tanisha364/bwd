package com.bwd.bwd.service.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bwd.bwd.db.DBOperation;
import com.bwd.bwd.request.UserData;
import com.bwd.bwd.response.company.CompanyDetailsResponse;
import com.bwd.bwd.response.company.CompanyListResponse;

@Service
public class CompanyServiceImpl implements CompanyServices{

	@Autowired
	private JdbcTemplate jdbcTemplate;	
	
	@Override
	public List<CompanyListResponse> findCompanyList(UserData requestData)
	{
		DBOperation dbop = new DBOperation();
		List<CompanyListResponse> listJPR = new ArrayList<CompanyListResponse>();
		//long useraccountid = requestData.getUseraccountid();
		//List<CompanyResponse> objects = null;

		//objects  = new ArrayList<CompanyResponse>();
		
		String accountIdQuery = "SELECT useraccountid FROM user_accounts WHERE userid = ?";
		List<Map<String, Object>> accountIdData = jdbcTemplate.queryForList(accountIdQuery, requestData.getUserid());	         	
		Long profileAccountId = (Long) accountIdData.get(0).get("useraccountid");

		String accessLevelQuery = "SELECT userlevel FROM user_accounts WHERE useraccountid = "+profileAccountId;

		List<Map<String, Object>> accessLevelQueryData = jdbcTemplate.queryForList(accessLevelQuery);	 
		Integer accessLevelObject = (Integer) accessLevelQueryData.get(0).get("userlevel");
		int accessLevel = accessLevelObject != null ? accessLevelObject.intValue() : 0;

		if(accessLevel == 8 || accessLevel == 9)
		{

			for(int i=0;i<=dbop.getNumberOfRow();i++)
			{
				String query = "SELECT c.companyid, c.companyname, c.whitelabel, c.type FROM company c ORDER BY c.companyname";

				dbop.setSelectQuery(query);
				dbop.executeSelectQuery();
				String data[][] = dbop.fetchRecord();	

				int dataLen = dbop.getNumberOfRow();
				System.out.println("Lenght : "+dataLen);
				for(int j=0;j<=dataLen;j++)
				{
					CompanyListResponse jpr = new CompanyListResponse();
					listJPR.add(jpr.getObject(data[j]));
				}

				return listJPR;					
			}
		}

		else {
			if(accessLevel == 1) {
				{
					for(int i=0;i<=dbop.getNumberOfRow();i++)
					{
						String query = "SELECT c.companyid, c.companyname, c.whitelabel, c.type"
								+ " FROM company c, user_association ua  WHERE c.companyid = ua.companyid AND ua.accesslevel != 0 And ua.useraccountid ="+profileAccountId+" ORDER BY c.companyname";

						dbop.setSelectQuery(query);
						dbop.executeSelectQuery();
						String data[][] = dbop.fetchRecord();	

						int dataLen = dbop.getNumberOfRow();
						System.out.println("Lenght : "+dataLen);
						for(int j=0;j<=dataLen;j++)
						{
							CompanyListResponse jpr = new CompanyListResponse();
							listJPR.add(jpr.getObject(data[j]));
						}

						return listJPR;						
					}
				}
			}else {
				listJPR = new ArrayList<>();
			}
		}
		return listJPR;	
	}


	@Override
	public List<CompanyDetailsResponse> findCompanyDetaist(UserData requestData)
	{
		DBOperation dbop = new DBOperation();
		int companyid = requestData.getCompanyid();
		List<CompanyDetailsResponse> listJPR = new ArrayList<CompanyDetailsResponse>();

		String query = """
				SELECT 
				cdt.companydetailid,
				cdt.companyid,
				CONCAT("https://media.publit.io/file/companylogo/", c.companylogo) as companylogo,
				cdt.isdefault,
				cdt.companydepid,
				cdt.companydepadd,
				cdt.companydepadd2,
				cdt.companydepcity,
				cdt.companydepstate,
				cdt.companydepzip,
				cdt.zone_id,
				cdt.companydepphone,
				cdt.companycontactid,
				cdt.country 
				FROM 
				companydetail_tbl cdt
				INNER JOIN 
				company c ON cdt.companyid = c.companyid
				WHERE 
				cdt.companyid = """+companyid;
		System.out.println(query);
		dbop.setSelectQuery(query);
		dbop.executeSelectQuery();
		String data[][] = dbop.fetchRecord();	

		int dataLen = dbop.getNumberOfRow();
		System.out.println("Lenght : "+dataLen);
		for(int j=0;j<=dataLen;j++)
		{
			CompanyDetailsResponse jpr = new CompanyDetailsResponse();
			listJPR.add(jpr.getObject(data[j]));
		}

		return listJPR;		
	}	
}
