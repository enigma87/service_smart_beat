package com.genie.heartrate.mgmt.dao;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAO;

/**
 * @author dhasarathy
 **/

public class FitnessHeartrateTestDAOTest {

	private static ApplicationContext smartbeatContext;
	private static FitnessHeartrateTestDAO fitnessHeartrateTestDAO;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean1;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean2;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean3;
	@BeforeClass
	public static void beforeClass(){
		
		smartbeatContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		fitnessHeartrateTestDAO = (FitnessHeartrateTestDAO)smartbeatContext.getBean("fitnessHeartrateTestDAO");
		
		long now = new Date().getTime();
		long oneHourBefore 	= now - (1*3600000);
		long twoHoursBefore = now - (2*3600000);
		
		fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean1.setUserid("user1");
		fitnessHeartrateTestBean1.setHeartrateTestId("user1Test1");
		fitnessHeartrateTestBean1.setHeartrateType(FitnessHeartrateTestBean.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean1.setHeartrate(124.0);
		fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(twoHoursBefore));
		
		fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean2.setUserid("user1");
		fitnessHeartrateTestBean2.setHeartrateTestId("user1Test2");
		fitnessHeartrateTestBean2.setHeartrateType(FitnessHeartrateTestBean.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean2.setHeartrate(130.0);
		fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(oneHourBefore));
		
		fitnessHeartrateTestBean3 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean3.setUserid("user1");
		fitnessHeartrateTestBean3.setHeartrateTestId("user1Test3");
		fitnessHeartrateTestBean3.setHeartrateType(FitnessHeartrateTestBean.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean3.setHeartrate(146.0);
		fitnessHeartrateTestBean3.setTimeOfRecord(new Timestamp(now));
	}
	
	@Test
	public void testCreateHeartrateTest() {
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		FitnessHeartrateTestBean bean = fitnessHeartrateTestDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		assertEquals(fitnessHeartrateTestBean1.getHeartrate(), bean.getHeartrate());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
	}

	@Test
	public void testGetHeartrateTestByTestId() {
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		FitnessHeartrateTestBean bean = fitnessHeartrateTestDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		assertEquals(fitnessHeartrateTestBean1.getHeartrate(), bean.getHeartrate());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		bean = fitnessHeartrateTestDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		assertNull(bean);
	}

	@Test
	public void testDeleteHeartrateTestByTestId() {
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		FitnessHeartrateTestBean bean = fitnessHeartrateTestDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		assertEquals(fitnessHeartrateTestBean1.getHeartrate(), bean.getHeartrate());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		bean = fitnessHeartrateTestDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		assertNull(bean);
	}

	@Test
	public void testGetRecentHeartrateTestForUser() {
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		FitnessHeartrateTestBean bean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUser(fitnessHeartrateTestBean1.getUserid(), fitnessHeartrateTestBean1.getHeartrateType());
		assertEquals(fitnessHeartrateTestBean3.getHeartrateTestId(), bean.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean2.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean3.getHeartrateTestId());		
	}

}

