package com.genie.smartbeat.beans;

import static org.junit.Assert.*;
import junit.framework.Assert;

import com.genie.smartbeat.beans.FitnessSpeedHeartRateBean;
import org.junit.Test;

public class FitnessSpeedHeartRateBeanTest {

	@Test
	public void testFitnessSpeedHeartRateBean() {
		String userid = "123456_abcd_789";
		Double currentVdot = 34.234567;
		Double previousVdot = 30.67894;
		
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = new FitnessSpeedHeartRateBean();
		fitnessSpeedHeartRateBean.setUserid(userid);
		fitnessSpeedHeartRateBean.setPreviousVdot(previousVdot);
		fitnessSpeedHeartRateBean.setCurrentVdot(currentVdot);
		
		Assert.assertEquals(userid,fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(previousVdot,fitnessSpeedHeartRateBean.getPreviousVdot());
		Assert.assertEquals(currentVdot,fitnessSpeedHeartRateBean.getCurrentVdot());

	}

}
