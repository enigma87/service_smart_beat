package com.genie.smartbeat.beans;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Date;

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
		
		assertEquals(userid, fitnessHeartRateTestBean.getUserid());
		assertEquals(heartrateTestId,fitnessHeartRateTestBean.getHeartrateTestId());
		assertEquals(heartrateType, fitnessHeartRateTestBean.getHeartrateType());
		assertEquals(heartrate, fitnessHeartRateTestBean.getHeartrate());
		assertEquals(timeOfRecord,fitnessHeartRateTestBean.getTimeOfRecord());
		assertEquals(dayOfRecord,fitnessHeartRateTestBean.getDayOfRecord());
	}

}
