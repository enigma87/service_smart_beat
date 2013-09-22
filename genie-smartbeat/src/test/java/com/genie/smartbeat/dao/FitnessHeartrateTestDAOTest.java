package com.genie.smartbeat.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;

/**
 * @author dhasarathy
 **/

public class FitnessHeartrateTestDAOTest {

	private static AbstractApplicationContext smartbeatContext;
	private static FitnessHeartrateTestDAO fitnessHeartrateTestDAO;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean1;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean2;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean3;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean4;
	private static FitnessHeartrateTestBean fitnessHeartrateTestBean5;

	
	
	
	private static long now; //= new Date().getTime();
	private static long oneHourBefore; //	= now - (1*3600000);
	private static long twoHoursBefore; //= now - (2*3600000);
	private static long threeHoursBefore; //= now - (3*3600000);
	
	@BeforeClass
	public static void beforeClass(){		
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);		

		now = cal.getTime().getTime();
		oneHourBefore = now -(1 * 3600000);
		twoHoursBefore = now -(2 * 3600000);
		threeHoursBefore = now - (3 * 3600000);
		
		smartbeatContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		smartbeatContext.registerShutdownHook();
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
		
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user2");
	}
	
	@Test
	public void testCreateHeartrateTest() {
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		FitnessHeartrateTestBean bean = fitnessHeartrateTestDAO.getHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		assertEquals(fitnessHeartrateTestBean1.getHeartrate(), bean.getHeartrate());
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		
		bean.setUserid("");
		Assert.assertEquals(0, fitnessHeartrateTestDAO.createHeartrateTest(bean));
		
		bean.setUserid(null);
		Assert.assertEquals(0, fitnessHeartrateTestDAO.createHeartrateTest(bean));
		
		bean.setHeartrateTestId("");
		Assert.assertEquals(0, fitnessHeartrateTestDAO.createHeartrateTest(bean));
		
		bean.setHeartrateTestId(null);
		Assert.assertEquals(0, fitnessHeartrateTestDAO.createHeartrateTest(bean));

	}
	
	@AfterClass
	public static void tearDown()
	{
		smartbeatContext.close();
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
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser(fitnessHeartrateTestBean1.getUserid());
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
public void testGetNRecentHeartRateTestsForUserByTypeWithOffset(){
		
		Calendar cal = Calendar.getInstance();		
		FitnessHeartrateTestDAO fitnessHeartrateTestDAO = (FitnessHeartrateTestDAO)smartbeatContext.getBean("fitnessHeartrateTestDAO");
		FitnessHeartrateZoneDAO fitnessHeartrateZoneDAO = (FitnessHeartrateZoneDAO) smartbeatContext.getBean("fitnessHeartrateZoneDAO");
		fitnessHeartrateZoneDAO.deleteHeartrateZoneModelByUserid("user1");
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		
		List<FitnessHeartrateTestBean> ohrTests = fitnessHeartrateTestDAO.getNRecentHeartRateTestsForUserByTypeWithOffset("user1", 
				ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC, 
				ShapeIndexAlgorithm.SOHR_STABILIZATION_LIMIT, 
				2);
		assertEquals(0, ohrTests.size());
	
		FitnessHeartrateTestBean fitnessHeartrateTestBean1 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean1.setUserid("user1");
		fitnessHeartrateTestBean1.setHeartrateTestId("user1Test1");
		fitnessHeartrateTestBean1.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean1.setHeartrate(101.0);
		fitnessHeartrateTestBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestBean1.setDayOfRecord(6);
	
		cal.add(Calendar.DATE, -1);
		FitnessHeartrateTestBean fitnessHeartrateTestBean2 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean2.setUserid("user1");
		fitnessHeartrateTestBean2.setHeartrateTestId("user1Test2");
		fitnessHeartrateTestBean2.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean2.setHeartrate(99.0);
		fitnessHeartrateTestBean2.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestBean2.setDayOfRecord(5);
	
		cal.add(Calendar.DATE, -1);
		FitnessHeartrateTestBean fitnessHeartrateTestBean3 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean3.setUserid("user1");
		fitnessHeartrateTestBean3.setHeartrateTestId("user1Test3");
		fitnessHeartrateTestBean3.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean3.setHeartrate(100.0);
		fitnessHeartrateTestBean3.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestBean3.setDayOfRecord(4);
	
		cal.add(Calendar.DATE, -1);
		FitnessHeartrateTestBean fitnessHeartrateTestBean4 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean4.setUserid("user1");
		fitnessHeartrateTestBean4.setHeartrateTestId("user1Test4");
		fitnessHeartrateTestBean4.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean4.setHeartrate(97.0);
		fitnessHeartrateTestBean4.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestBean4.setDayOfRecord(3);
	
		cal.add(Calendar.DATE, -1);
		FitnessHeartrateTestBean fitnessHeartrateTestBean5 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean5.setUserid("user1");
		fitnessHeartrateTestBean5.setHeartrateTestId("user1Test5");
		fitnessHeartrateTestBean5.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean5.setHeartrate(102.0);
		fitnessHeartrateTestBean5.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestBean5.setDayOfRecord(2);
		
		cal.add(Calendar.DATE, -1);
		FitnessHeartrateTestBean fitnessHeartrateTestBean6 = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean6.setUserid("user1");
		fitnessHeartrateTestBean6.setHeartrateTestId("user1Test6");
		fitnessHeartrateTestBean6.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		fitnessHeartrateTestBean6.setHeartrate(96.0);
		fitnessHeartrateTestBean6.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestBean6.setDayOfRecord(1);
	
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean4);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean5);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean6);
		
		ohrTests = fitnessHeartrateTestDAO.getNRecentHeartRateTestsForUserByTypeWithOffset("user1", 
																			ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC, 
																			ShapeIndexAlgorithm.SOHR_STABILIZATION_LIMIT, 
																			2);
		
		assertEquals(2, ohrTests.size());
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
	}

	 
	@Test
	public void testGetTodaysHeartRateTestCountForUser(){
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser(fitnessHeartrateTestBean1.getUserid());
		Integer todaysHRTCount = fitnessHeartrateTestDAO.getTodaysHeartRateTestCountForUser(fitnessHeartrateTestBean1.getUserid());
		assertEquals(new Integer(0), todaysHRTCount);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		//System.out.println("  " + fitnessHeartrateTestBean1.getTimeOfRecord().toString());
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
	}
	
	@Test
	public void testGetDayOfRecordForUser() {
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		fitnessHeartrateTestBean1.setDayOfRecord(3);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		Integer dayOfRecord = fitnessHeartrateTestDAO.getDayOfRecordForUser(fitnessHeartrateTestBean1.getUserid());
		Assert.assertNotNull(dayOfRecord);
		Assert.assertEquals(fitnessHeartrateTestBean1.getDayOfRecord(), dayOfRecord);
		fitnessHeartrateTestDAO.deleteHeartrateTestByTestId(fitnessHeartrateTestBean1.getHeartrateTestId());
		
		dayOfRecord = fitnessHeartrateTestDAO.getDayOfRecordForUser(fitnessHeartrateTestBean1.getUserid());
		Assert.assertEquals(0, dayOfRecord.intValue());
		
	}
	
	@Test
	public void testGetAllTestsByUser() {
		List<FitnessHeartrateTestBean> heartrateTestBeans = fitnessHeartrateTestDAO.getAllHeartrateTestsByUser("user1");	
		
		Assert.assertEquals(0, heartrateTestBeans.size());
		
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);
			
		heartrateTestBeans = fitnessHeartrateTestDAO.getAllHeartrateTestsByUser("user1");		
		
		Assert.assertEquals(3, heartrateTestBeans.size());
		
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
	}
	
	@Test
	public void testDeleteAllHeartrateTestsForUser(){
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		List<FitnessHeartrateTestBean> heartrateTestBeans = fitnessHeartrateTestDAO.getAllHeartrateTestsByUser("user1");
				
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean1);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean2);
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean3);                 
		heartrateTestBeans = fitnessHeartrateTestDAO.getAllHeartrateTestsByUser("user1");                               
		Assert.assertEquals(3, heartrateTestBeans.size());
		
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
		heartrateTestBeans = fitnessHeartrateTestDAO.getAllHeartrateTestsByUser("user1");                               
		Assert.assertEquals(0, heartrateTestBeans.size());
	}
	
	@Test
	public void testGetFitnessHeartrateTestsByTypeAndRange() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		Timestamp startTimestamp = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.DATE, +4);
		Timestamp endTimestamp = new Timestamp(cal.getTimeInMillis());		
		Assert.assertEquals(0, fitnessHeartrateTestDAO.getFitnessHeartrateTestsByTypeInTimeInterval("user1", ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC, startTimestamp, endTimestamp).size());
		
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		FitnessHeartrateTestBean fitnessHeartrateTestBean = new FitnessHeartrateTestBean();
		fitnessHeartrateTestBean.setUserid("user1");	
		fitnessHeartrateTestBean.setHeartrateType(ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC);
		/*First test case*/
		cal.add(Calendar.DATE, +1);
		fitnessHeartrateTestBean.setHeartrateTestId("user1Test1");
		fitnessHeartrateTestBean.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean);
		
		cal.add(Calendar.DATE, +1);
		fitnessHeartrateTestBean.setHeartrateTestId("user1Test2");
		fitnessHeartrateTestBean.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean);
		
		cal.add(Calendar.DATE, +1);
		fitnessHeartrateTestBean.setHeartrateTestId("user1Test3");
		fitnessHeartrateTestBean.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessHeartrateTestDAO.createHeartrateTest(fitnessHeartrateTestBean);

		List<FitnessHeartrateTestBean> heartrateTests =  fitnessHeartrateTestDAO.getFitnessHeartrateTestsByTypeInTimeInterval("user1", ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC, startTimestamp, endTimestamp);
		Assert.assertEquals(3, heartrateTests.size());

		cal.add(Calendar.HOUR, -23);
		endTimestamp = new Timestamp(cal.getTimeInMillis());
		heartrateTests =  fitnessHeartrateTestDAO.getFitnessHeartrateTestsByTypeInTimeInterval("user1", ShapeIndexAlgorithm.HEARTRATE_TYPE_STANDING_ORTHOSTATIC, startTimestamp, endTimestamp);
		Assert.assertEquals(2, heartrateTests.size());
		
		fitnessHeartrateTestDAO.deleteAllHeartrateTestsForUser("user1");
	}
}
