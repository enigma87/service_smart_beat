package com.genie.smartbeat.beans;

import org.junit.Assert;
import org.junit.Test;

public class FitnessHeartrateZoneBeanTest {

	@Test
	public void testFitnessHeartrateZoneBean() {
		String userid = "123456_abcd_789";
		Double heartrateZone1Start = 34.50;
		Double heartrateZone1End = 50.65;
		Double heartrateZone2Start = 50.66;
		Double heartrateZone2End = 61.56;
		Double heartrateZone3Start = 61.57;
		Double heartrateZone3End = 83.23;
		Double heartrateZone4Start = 83.24;
		Double heartrateZone4End = 101.34;
		Double heartrateZone5Start = 101.35;
		Double heartrateZone5End = 120.60;
		Double heartrateZone6Start = 120.61;
		Double heartrateZone6End = 140.05;
		double[][] heartrateZones = {{0.0,0.0},{35.50,51.65},{51.66,62.56},{62.57,84.23},{84.24,102.34},{102.35,121.60},{121.61,141.05}};
		
		/*Unit Test for get and set methods for class variables*/
		FitnessHeartrateZoneBean fitnessHeartrateZoneBean = new FitnessHeartrateZoneBean();
		fitnessHeartrateZoneBean.setUserid(userid);
		fitnessHeartrateZoneBean.setHeartrateZone1Start(heartrateZone1Start);
		fitnessHeartrateZoneBean.setHeartrateZone1End(heartrateZone1End);
		fitnessHeartrateZoneBean.setHeartrateZone2Start(heartrateZone2Start);
		fitnessHeartrateZoneBean.setHeartrateZone2End(heartrateZone2End);
		fitnessHeartrateZoneBean.setHeartrateZone3Start(heartrateZone3Start);
		fitnessHeartrateZoneBean.setHeartrateZone3End(heartrateZone3End);
		fitnessHeartrateZoneBean.setHeartrateZone4Start(heartrateZone4Start);
		fitnessHeartrateZoneBean.setHeartrateZone4End(heartrateZone4End);
		fitnessHeartrateZoneBean.setHeartrateZone5Start(heartrateZone5Start);
		fitnessHeartrateZoneBean.setHeartrateZone5End(heartrateZone5End);
		fitnessHeartrateZoneBean.setHeartrateZone6Start(heartrateZone6Start);
		fitnessHeartrateZoneBean.setHeartrateZone6End(heartrateZone6End);
		
		Assert.assertEquals(userid, fitnessHeartrateZoneBean.getUserid());
		Assert.assertEquals(heartrateZone1Start,fitnessHeartrateZoneBean.getHeartrateZone1Start());
		Assert.assertEquals(heartrateZone1End,fitnessHeartrateZoneBean.getHeartrateZone1End());
		Assert.assertEquals(heartrateZone2Start,fitnessHeartrateZoneBean.getHeartrateZone2Start());
		Assert.assertEquals(heartrateZone2End,fitnessHeartrateZoneBean.getHeartrateZone2End());
		Assert.assertEquals(heartrateZone3Start,fitnessHeartrateZoneBean.getHeartrateZone3Start());
		Assert.assertEquals(heartrateZone3End,fitnessHeartrateZoneBean.getHeartrateZone3End());
		Assert.assertEquals(heartrateZone4Start,fitnessHeartrateZoneBean.getHeartrateZone4Start());
		Assert.assertEquals(heartrateZone4End,fitnessHeartrateZoneBean.getHeartrateZone4End());
		Assert.assertEquals(heartrateZone5Start,fitnessHeartrateZoneBean.getHeartrateZone5Start());
		Assert.assertEquals(heartrateZone5End,fitnessHeartrateZoneBean.getHeartrateZone5End());
		Assert.assertEquals(heartrateZone6Start,fitnessHeartrateZoneBean.getHeartrateZone6Start());
		Assert.assertEquals(heartrateZone6End,fitnessHeartrateZoneBean.getHeartrateZone6End());
		
		/* Unit test for setHeartrateZones and getHeartrateZones*/
		fitnessHeartrateZoneBean.setHeartrateZones(heartrateZones);
		
		double[][] getHeartrateZones = null;
		getHeartrateZones = fitnessHeartrateZoneBean.getHeartrateZones();
		Assert.assertEquals(new Double(heartrateZones[1][0]), new Double(getHeartrateZones[1][0]));
		Assert.assertEquals(new Double(heartrateZones[1][1]), new Double(getHeartrateZones[1][1]));
		Assert.assertEquals(new Double(heartrateZones[2][0]), new Double(getHeartrateZones[2][0]));
		Assert.assertEquals(new Double(heartrateZones[2][1]), new Double(getHeartrateZones[2][1]));
		Assert.assertEquals(new Double(heartrateZones[3][0]), new Double(getHeartrateZones[3][0]));
		Assert.assertEquals(new Double(heartrateZones[3][1]), new Double(getHeartrateZones[3][1]));
		Assert.assertEquals(new Double(heartrateZones[4][0]), new Double(getHeartrateZones[4][0]));
		Assert.assertEquals(new Double(heartrateZones[4][1]), new Double(getHeartrateZones[4][1]));
		Assert.assertEquals(new Double(heartrateZones[5][0]), new Double(getHeartrateZones[5][0]));
		Assert.assertEquals(new Double(heartrateZones[5][1]), new Double(getHeartrateZones[5][1]));
		Assert.assertEquals(new Double(heartrateZones[6][0]), new Double(getHeartrateZones[6][0]));
		Assert.assertEquals(new Double(heartrateZones[6][1]), new Double(getHeartrateZones[6][1]));
	}

}
