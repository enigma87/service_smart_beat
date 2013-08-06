package com.genie.smartbeat.beans;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;

import org.junit.Test;

public class FitnessHeartrateTestBeanTest {

	@Test
	public void testFitnessHeartrateTestBean() {
		
		String userid = "123456_abcd_789";
		String heartrateTestId = "2013_05";
		Integer heartrateType = 2;
		Double heartrate = 36.5;
		long now = new Date().getTime();
		Timestamp timeOfRecord = new Timestamp(now);
		Integer dayOfRecord = 10;
		
		FitnessHeartrateTestBean fitnessHeartRateTestBean = new FitnessHeartrateTestBean();
		fitnessHeartRateTestBean.setUserid(userid);
		fitnessHeartRateTestBean.setHeartrateTestId(heartrateTestId);
		fitnessHeartRateTestBean.setHeartrateType(heartrateType);
		fitnessHeartRateTestBean.setHeartrate(heartrate);
		fitnessHeartRateTestBean.setTimeOfRecord(timeOfRecord);
		fitnessHeartRateTestBean.setDayOfRecord(dayOfRecord);
		
		Assert.assertEquals(userid, fitnessHeartRateTestBean.getUserid());
		Assert.assertEquals(heartrateTestId,fitnessHeartRateTestBean.getHeartrateTestId());
		Assert.assertEquals(heartrateType, fitnessHeartRateTestBean.getHeartrateType());
		Assert.assertEquals(heartrate, fitnessHeartRateTestBean.getHeartrate());
		Assert.assertEquals(timeOfRecord,fitnessHeartRateTestBean.getTimeOfRecord());
		Assert.assertEquals(dayOfRecord,fitnessHeartRateTestBean.getDayOfRecord());
	}

}
