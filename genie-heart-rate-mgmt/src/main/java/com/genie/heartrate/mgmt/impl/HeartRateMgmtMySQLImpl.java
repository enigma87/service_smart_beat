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
import com.genie.heartrate.mgmt.util.HeartRateConts;
import com.genie.heartrate.mgmt.util.HeartRateUtil;

/**
 * @author manojkumar
 *
 */
public class HeartRateMgmtMySQLImpl implements HeartRateMgmt 
{

	private UserHeartRateTestDao heartRateDao;
	
	public UserHeartRateTestDao getHeartRateDao()
	{
		return this.heartRateDao;
	}
	
	public void setHeartRateDao(UserHeartRateTestDao heartRateDao)
	{
		this.heartRateDao = heartRateDao;
	}
	
	public UserHeartRateTest getHeartRateTestResultsForUser(Long userid) 
	{
		return heartRateDao.getHeartRateTestResults(userid);
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
				heartRateTest.setRestingHr(Integer.parseInt(heartRates.get(HeartRateConts.RESTING_HEART_RATE).toString()));
				heartRateTest.setRestingHrTs(Calendar.getInstance());
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
				heartRateDao.createHeartRateTestResults(heartRateTest);
			else
				heartRateDao.updateHeartRateTestResults(heartRateTest);
		}
	}

	public UserHeartRateZone getHeartRateZonesForUser(Long userid) 
	{
		return null;
	}

	public void saveHeartRateZonesForUser(Long userid, String json) 
	{
		
	}

}
