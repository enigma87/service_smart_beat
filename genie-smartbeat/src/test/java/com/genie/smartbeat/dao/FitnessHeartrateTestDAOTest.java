package com.genie.smartbeat.dao;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.dao.FitnessHeartrateTestDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;

/**
 * @author dhasarathy
 **/

public class FitnessHeartrateTestDAOTest {

	private static ApplicationContext smartbeatContext;
	private static FitnessHeartrateTestDAO fitnessHeartrateTestDAO;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean1;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean2;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean3;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean4;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean5;
	
	private static long now = new Date().getTime();
	private static long oneHourBefore 	= now - (1*3600000);
	private static long twoHoursBefore = now - (2*3600000);
	private static long threeHoursBefore = now - (3*3600000);
	
	@BeforeClass
	public static void beforeClass(){
		
		smartbeatContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		fitnessHeartrateTestDAO = (FitnessHeartrateTestDAO)smartbeatContext.getBean("fitnessHeartrateTestDAO");
		
		fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean1.setUserid("user1");
		fitnessHeartrateTestBean1.setHeartrateTestId("user1Test1");
		fitnessHeartrateTestBean1.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean1.setHeartrate(124.0);
		fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(threeHoursBefore));
		fitnessHeartrateTestBean1.setDayOfRecord(1);
		
		fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean2.setUserid("user1");
		fitnessHeartrateTestBean2.setHeartrateTestId("user1Test2");
		fitnessHeartrateTestBean2.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean2.setHeartrate(130.0);
		fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(twoHoursBefore));
		fitnessHeartrateTestBean2.setDayOfRecord(3);
		
		fitnessHeartrateTestBean3 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean3.setUserid("user1");
		fitnessHeartrateTestBean3.setHeartrateTestId("user1Test3");
		fitnessHeartrateTestBean3.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean3.setHeartrate(146.0);
		fitnessHeartrateTestBean3.setTimeOfRecord(new Timestamp(oneHourBefore));
		fitnessHeartrateTestBean3.setDayOfRecord(8);
		
		fitnessHeartrateTestBean4 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean4.setUserid("user1");
		fitnessHeartrateTestBean4.setHeartrateTestId("user1Test4");
		fitnessHeartrateTestBean4.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		fitnessHeartrateTestBean4.setHeartrate(160.0);
		fitnessHeartrateTestBean4.setTimeOfRecord(new Timestamp(now));
		fitnessHeartrateTestBean4.setDayOfRecord(9);
		
		fitnessHeartrateTestBean5 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean5.setUserid("user2");
		fitnessHeartrateTestBean5.setHeartrateTestId("user2Test1");
		fitnessHeartrateTestBean5.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_MAXIMAL);
		fitnessHeartrateTestBean5.setHeartrate(160.0);
		fitnessHeartrateTestBean5.setTimeOfRecord(new Timestamp(now));
		fitnessHeartrateTestBean5.setDayOfRecord(10);
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
	public void testGetNumberOfHeartrateTestsByUser(){
		Integer numberOfHeartrateTests = fitnessHeartrateTestDAO.getNumberOfHeartrateTestsByUser(fitnessHeartrateTestBean1.getUserid());
		assertEquals(new Integer(0), numberOfHeartrateTests);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean4);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean5);
		numberOfHeartrateTests = fitnessHeartrateTestDAO.getNumberOfHeartrateTestsByUser(fitnessHeartrateTestBean1.getUserid());
		assertEquals(new Integer(4), numberOfHeartrateTests);
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean2.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean3.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean4.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean5.getHeartrateTestId());
	}

	@Test
	public void testGetRecentHeartrateTestForUserByType() {
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		FitnessHeartrateTestBean bean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUserByType(fitnessHeartrateTestBean1.getUserid(), fitnessHeartrateTestBean1.getHeartrateType());
		assertEquals(fitnessHeartrateTestBean3.getHeartrateTestId(), bean.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean2.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean3.getHeartrateTestId());		
	}
	
	@Test
	public void testGetRecentHeartrateTestForUser(){
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean4);
		FitnessHeartrateTestBean bean = fitnessHeartrateTestDAO.getRecentHeartrateTestForUser(fitnessHeartrateTestBean1.getUserid());
		assertEquals(fitnessHeartrateTestBean4.getHeartrateTestId(), bean.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean2.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean3.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean4.getHeartrateTestId());
	}
	
	@Test
	public void testGetNumberOfHeartRateTestsForUserByType(){
		Integer numberOfHeartrateTests = 0;
		numberOfHeartrateTests = fitnessHeartrateTestDAO.getNumberOfHeartRateTestsForUserByType(fitnessHeartrateTestBean1.getUserid(), fitnessHeartrateTestBean1.getHeartrateType());
		assertEquals(new Integer(0), numberOfHeartrateTests);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean4);
		numberOfHeartrateTests = fitnessHeartrateTestDAO.getNumberOfHeartRateTestsForUserByType(fitnessHeartrateTestBean1.getUserid(), fitnessHeartrateTestBean1.getHeartrateType());
		assertEquals(new Integer(3), numberOfHeartrateTests);
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean2.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean3.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean4.getHeartrateTestId());
	}
	
	@Test
	public void testGetNRecentHeartRateTestsForUserByType(){
		
		List<FitnessHeartrateTestBean> nRecentHeartRateTests = fitnessHeartrateTestDAO.getNRecentHeartRateTestsForUserByType(fitnessHeartrateTestBean1.getUserid(), fitnessHeartrateTestBean1.getHeartrateType(), 2);
		assertEquals(0, nRecentHeartRateTests.size());
		
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean4);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean5);
		
		nRecentHeartRateTests = fitnessHeartrateTestDAO.getNRecentHeartRateTestsForUserByType(fitnessHeartrateTestBean1.getUserid(), fitnessHeartrateTestBean1.getHeartrateType(), 2);
		assertEquals(2, nRecentHeartRateTests.size());
		
		nRecentHeartRateTests = fitnessHeartrateTestDAO.getNRecentHeartRateTestsForUserByType(fitnessHeartrateTestBean1.getUserid(), fitnessHeartrateTestBean1.getHeartrateType(), 4);
		assertEquals(3, nRecentHeartRateTests.size());
		
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean2.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean3.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean4.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean5.getHeartrateTestId());
	}
	
	@Test
	public void testGetTodaysHeartRateTestCountForUser(){
		Integer todaysHRTCount = fitnessHeartrateTestDAO.getTodaysHeartRateTestCountForUser(fitnessHeartrateTestBean1.getUserid());
		assertEquals(new Integer(0), todaysHRTCount);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		todaysHRTCount = fitnessHeartrateTestDAO.getTodaysHeartRateTestCountForUser(fitnessHeartrateTestBean1.getUserid());
		assertEquals(new Integer(1), todaysHRTCount);
		long oneDayBefore = now - (25*3600000);
		fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(oneDayBefore));
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		todaysHRTCount = fitnessHeartrateTestDAO.getTodaysHeartRateTestCountForUser(fitnessHeartrateTestBean1.getUserid());
		assertEquals(new Integer(1), todaysHRTCount);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		todaysHRTCount = fitnessHeartrateTestDAO.getTodaysHeartRateTestCountForUser(fitnessHeartrateTestBean1.getUserid());
		assertEquals(new Integer(2), todaysHRTCount);
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean2.getHeartrateTestId());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean3.getHeartrateTestId());
		fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(twoHoursBefore));
	}
}

