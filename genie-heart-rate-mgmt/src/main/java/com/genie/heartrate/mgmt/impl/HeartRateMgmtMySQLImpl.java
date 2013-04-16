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

	public void saveHeartRateTestResultsForUser(Long userid, String json) 
	{
		Map<String, Object> heartRates = HeartRateUtil.parseHeartRates(json);
		if (heartRates != null && heartRates.size() > 0)
		{
			boolean create = false;
			UserHeartRateTest heartRateTest = getHeartRateTestResultsForUser(userid);
			if (heartRateTest == null)
			{
				heartRateTest = new UserHeartRateTest();
				heartRateTest.setUserid(userid);
				create = true;
			}
			
			if (heartRates.containsKey(HeartRateConts.RESTING_HEART_RATE))
			{
				heartRateTest.setRestingHeartRate(Integer.parseInt(heartRates.get(HeartRateConts.RESTING_HEART_RATE).toString()));
				heartRateTest.setRestingHeartRateTimestamp(Calendar.getInstance());
			}
			if (heartRates.containsKey(HeartRateConts.MAXIMAL_HEART_RATE))
			{
				heartRateTest.setMaximalHr(Integer.parseInt(heartRates.get(HeartRateConts.MAXIMAL_HEART_RATE).toString()));
				heartRateTest.setMaximalHrTs(Calendar.getInstance());
			}
			if (heartRates.containsKey(HeartRateConts.THRESHOLD_HEART_RATE))
			{
				heartRateTest.setThresholdHr(Integer.parseInt(heartRates.get(HeartRateConts.THRESHOLD_HEART_RATE).toString()));
				heartRateTest.setThresholdHrTs(Calendar.getInstance());
			}
			
			//TODO Trigger HRZ calculation
			
			if (create)
				userHeartRateTestDao.createHeartRateTestResults(heartRateTest);
			else
				userHeartRateTestDao.updateHeartRateTestResults(heartRateTest);
		}
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
