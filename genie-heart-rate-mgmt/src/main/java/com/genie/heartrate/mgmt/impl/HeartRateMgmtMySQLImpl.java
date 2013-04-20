/**
 * 
 */
package com.genie.heartrate.mgmt.impl;

import java.util.Calendar;
import java.util.Map;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;
import com.genie.heartrate.mgmt.beans.UserHeartRateZone;
import com.genie.heartrate.mgmt.core.HeartRateMgmt;
import com.genie.heartrate.mgmt.dao.UserHeartRateTestDao;
import com.genie.heartrate.mgmt.dao.UserHeartRateZoneDao;
import com.genie.heartrate.mgmt.util.HeartRateConts;
import com.genie.heartrate.mgmt.util.HeartRateUtil;

/**
 * @author manojkumar
 *
 */
public class HeartRateMgmtMySQLImpl implements HeartRateMgmt 
{

	private UserHeartRateTestDao userHeartRateTestDao;
	private UserHeartRateZoneDao userHeartRateZoneDao;
	
	public UserHeartRateTestDao getUserHeartRateTestDao()
	{
		return this.userHeartRateTestDao;
	}
	
	public void setUserHeartRateTestDao(UserHeartRateTestDao userHeartRateTestDao)
	{
		this.userHeartRateTestDao = userHeartRateTestDao;
	}
	
	public UserHeartRateZoneDao getUserHeartRateZoneDao()
	{
		return this.userHeartRateZoneDao;
	}
	
	public void setUserHeartRateZoneDao(UserHeartRateZoneDao userHeartRateZoneDao)
	{
		this.userHeartRateZoneDao = userHeartRateZoneDao;
	}
	
	
	
	public UserHeartRateTest getHeartRateTestResultsForUser(Long userid) 
	{
		return userHeartRateTestDao.getHeartRateTestResults(userid);
	}

	public void saveHeartRateTestResultsForUser(UserHeartRateTest uhrt) 
	{
			UserHeartRateTest heartRateTest = getHeartRateTestResultsForUser(uhrt.getUserid());			
			if(null == heartRateTest){
				userHeartRateTestDao.createHeartRateTestResults(uhrt);
			}else{			
				uhrt.fillInTheBlanks(heartRateTest);			
				userHeartRateTestDao.updateHeartRateTestResults(uhrt);
			}				
			//TODO Trigger HRZ calculation			
	}

	public UserHeartRateZone getHeartRateZonesForUser(Long userid) 
	{
		return userHeartRateZoneDao.getHeartRateZone(userid);
	}

	public void saveHeartRateZonesForUser(UserHeartRateZone userHeartRateZone) 
	{
		UserHeartRateZone fromDb = userHeartRateZoneDao.getHeartRateZone(userHeartRateZone.getUserid());
		if (fromDb == null)
			userHeartRateZoneDao.createHeartRateZone(userHeartRateZone);
		else
		{
			userHeartRateZone.setCreatedTs(fromDb.getCreatedTs());
			userHeartRateZoneDao.updateHeartRateZone(userHeartRateZone);
		}
	}

}
