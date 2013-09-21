package com.genie.smartbeat.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessHeartrateZoneBean;

/**
 * @author dhasarathy
 **/

public class FitnessHeartrateZoneDAOTest {

	private static AbstractApplicationContext smartbeatContext;
	private static FitnessHeartrateZoneDAO fitnessHeartrateZoneDAO;
	private static FitnessHeartrateZoneBean fitnessHeartrateZoneBean;
	private static String userid = "user-1";
	private static Double heartrateZone1Start 	= 50.0;
	private static Double heartrateZone1End 	= 105.0;
	private static Double heartrateZone2Start 	= 105.0;
	private static Double heartrateZone2End 	= 133.0;
	private static Double heartrateZone3Start 	= 133.0;
	private static Double heartrateZone3End 	= 156.0;
	private static Double heartrateZone4Start 	= 156.0;
	private static Double heartrateZone4End 	= 164.0;
	private static Double heartrateZone5Start 	= 164.0;
	private static Double heartrateZone5End 	= 182.0;
	private static Double heartrateZone6Start 	= 182.0;
	private static Double heartrateZone6End 	= 190.0;
	
	@BeforeClass
	public static void setupBeforeClass(){
		smartbeatContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		smartbeatContext.registerShutdownHook();
		fitnessHeartrateZoneDAO = (FitnessHeartrateZoneDAO)smartbeatContext.getBean("fitnessHeartrateZoneDAO");
		fitnessHeartrateZoneBean = new FitnessHeartrateZoneBean();
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
	}
	
	@AfterClass
	public static void tearDown()
	{
		smartbeatContext.close();
	}
	
	@Test
	public void testCreateHeartrateZoneModel() {		
		fitnessHeartrateZoneDAO.createHeartrateZoneModel(fitnessHeartrateZoneBean);
		FitnessHeartrateZoneBean testBean = fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid(userid);
		assertNotNull(testBean);
		assertEquals(userid, testBean.getUserid());
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid(userid);
		testBean.setUserid("");
		Assert.assertEquals(0, fitnessHeartrateZoneDAO.createHeartrateZoneModel(testBean));
	
		testBean.setUserid(null);
		Assert.assertEquals(0, fitnessHeartrateZoneDAO.createHeartrateZoneModel(testBean));
	
	}

	@Test
	public void testGetHeartrateZoneModelByUserid() {
		FitnessHeartrateZoneBean testBean = fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid(userid);
		assertNull(testBean);
		fitnessHeartrateZoneDAO.createHeartrateZoneModel(fitnessHeartrateZoneBean);
		testBean = fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid(userid);
		assertNotNull(testBean);
		assertEquals(userid, testBean.getUserid());
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid(userid);
	}

	@Test
	public void testDeleteHeartrateZoneModelByUserid() {
		fitnessHeartrateZoneDAO.createHeartrateZoneModel(fitnessHeartrateZoneBean);
		FitnessHeartrateZoneBean testBean = fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid(userid);
		assertNotNull(testBean);
		assertEquals(userid, testBean.getUserid());
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid(userid);
		testBean = fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid(userid);
		assertNull(testBean);
	}

	@Test
	public void testUpdateHeartrateZoneModel() {
		Double updatedHeartrateZone1Start = heartrateZone1Start + 5.0;
		int affectedRows = fitnessHeartrateZoneDAO.updateHeartrateZoneModel(fitnessHeartrateZoneBean);
		assertEquals(0, affectedRows);
		fitnessHeartrateZoneDAO.createHeartrateZoneModel(fitnessHeartrateZoneBean);
		FitnessHeartrateZoneBean testBean = fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid(userid);
		assertEquals(heartrateZone1Start, testBean.getHeartrateZone1Start());
		fitnessHeartrateZoneBean.setHeartrateZone1Start(updatedHeartrateZone1Start);
		affectedRows = fitnessHeartrateZoneDAO.updateHeartrateZoneModel(fitnessHeartrateZoneBean);
		assertEquals(1, affectedRows);
		testBean = fitnessHeartrateZoneDAO.getHeartrateZoneModelByUserid(userid);
		assertEquals(updatedHeartrateZone1Start, testBean.getHeartrateZone1Start());
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid(userid);
		fitnessHeartrateZoneBean.setHeartrateZone1Start(heartrateZone1Start);
		
		testBean.setUserid("");
		Assert.assertEquals(0, fitnessHeartrateZoneDAO.createHeartrateZoneModel(testBean));
	
		testBean.setUserid(null);
		Assert.assertEquals(0, fitnessHeartrateZoneDAO.createHeartrateZoneModel(testBean));
	
	}	
}

