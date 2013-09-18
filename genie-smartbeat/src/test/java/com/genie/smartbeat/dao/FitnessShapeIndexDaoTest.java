package com.genie.smartbeat.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;

public class FitnessShapeIndexDaoTest {
	
	private static AbstractApplicationContext appContext;
    private static FitnessShapeIndexDAO fitnessShapeIndexDAO;
	private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO;
	private static FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
	private static FitnessShapeIndexBean fitnessShapeIndexBean = new FitnessShapeIndexBean();
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	private static final long now = new Date().getTime();
	private static final long nowPastOneHour = now - 3600000;
	private static final long nowPastTwoHour = now - 7200000;
	private static final Timestamp nowTimeStamp = new Timestamp(now);
	private static final Timestamp nowPastOneHourTimeStamp = new Timestamp(nowPastOneHour);
	private static final Timestamp nowPastTwoHourTimeStamp = new Timestamp(nowPastTwoHour);
	private static final long nowPastOneDay 	= now - (24*3600000);
	private static final long nowPastTwoDays 	= now - (2*24*3600000);
	private static final long nowPastThreeDays 	= now - (3*24*3600000);
	private static final long nowPastFourDays 	= now - (4*24*3600000);
	private static final String trainingSessionId = "20131";
	private static final Double shapeIndex = 100.0;
	
	
	@BeforeClass
	public static void setupBeforeClass(){
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		appContext.registerShutdownHook();
		fitnessShapeIndexDAO = (FitnessShapeIndexDAO)appContext.getBean("fitnessShapeIndexDAO");
		fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)appContext.getBean("fitnessTrainingSessionDAO");
		
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setTrainingSessionId(trainingSessionId);
		fitnessTrainingSessionBean.setStartTime(nowPastTwoHourTimeStamp);
		fitnessTrainingSessionBean.setEndTime(nowPastOneHourTimeStamp);
		fitnessShapeIndexBean.setUserid(userid);
		fitnessShapeIndexBean.setShapeIndex(shapeIndex);
		fitnessShapeIndexBean.setTimeOfRecord(nowPastOneHourTimeStamp);
		fitnessShapeIndexBean.setSessionOfRecord(trainingSessionId);
			
	}

	@Test
	public void testCreateFitnessShapeIndexModel() {
			
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
		
		FitnessShapeIndexBean fitnessShapeIndexBean1 = fitnessShapeIndexDAO.getRecentShapeIndexModel(userid);
		Assert.assertNotNull(fitnessShapeIndexBean1);
		Assert.assertEquals(userid, fitnessShapeIndexBean1.getUserid());
		Assert.assertEquals(shapeIndex, fitnessShapeIndexBean1.getShapeIndex());
		Assert.assertNotNull(fitnessShapeIndexBean1.getTimeOfRecord());
		Assert.assertEquals(trainingSessionId, fitnessShapeIndexBean1.getSessionOfRecord());
		
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(userid);
		
		fitnessShapeIndexBean1.setUserid("");
		Assert.assertEquals(0, fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1));
		
		fitnessShapeIndexBean1.setUserid(null);
		Assert.assertEquals(0, fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1));
		
		
		fitnessShapeIndexBean1.setSessionOfRecord("");
		Assert.assertEquals(0, fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1));

		fitnessShapeIndexBean1.setSessionOfRecord(null);
		Assert.assertEquals(0, fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1));

	}
	
	@Test
	public void testGetRecentShapeIndexModelBySessionId(){
		FitnessTrainingSessionBean fitnessTrainingSessionBean1 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean1.setUserid(userid);
		fitnessTrainingSessionBean1.setTrainingSessionId("20132");
		fitnessTrainingSessionBean1.setStartTime(new Timestamp(nowPastOneHour));
		fitnessTrainingSessionBean1.setEndTime(new Timestamp(now));
		
		FitnessShapeIndexBean  fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20132");
		fitnessShapeIndexBean1.setShapeIndex(150.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(now));
		
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean1);
		
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		FitnessTrainingSessionBean fitnessTrainingSessionBean2 = fitnessTrainingSessionDAO.getRecentFitnessTrainingSessionForUser(userid);
		String recentTrainingSessionId = fitnessTrainingSessionBean2.getTrainingSessionId();
		Assert.assertEquals(fitnessShapeIndexBean1.getSessionOfRecord(), recentTrainingSessionId );
		FitnessShapeIndexBean fitnessShapeIndexBean2 = fitnessShapeIndexDAO.getShapeIndexModelByTrainingSessionId(recentTrainingSessionId);
		Assert.assertEquals(fitnessShapeIndexBean1.getShapeIndex(), fitnessShapeIndexBean2.getShapeIndex() );
		
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById("20132");
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(userid);
	}
	
	@Test
	public void testGetShapeIndexHistoryDuringInterval(){
		List<FitnessShapeIndexBean> shapeIndexBeans = null;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		Timestamp endInterval = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.DATE, -3);
		Timestamp startInterval = new Timestamp(cal.getTimeInMillis());
		shapeIndexBeans = fitnessShapeIndexDAO.getShapeIndexHistoryDuringInterval(userid, startInterval,endInterval);
		Assert.assertEquals(0, shapeIndexBeans.size());
		
		FitnessShapeIndexBean  fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20135");
		fitnessShapeIndexBean1.setShapeIndex(150.0);
		fitnessShapeIndexBean1.setTimeOfRecord(nowPastOneHourTimeStamp);
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20134");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(nowPastOneDay));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20133");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(nowPastTwoDays));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20132");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(nowPastThreeDays));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20131");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(nowPastFourDays));
		
		shapeIndexBeans = fitnessShapeIndexDAO.getShapeIndexHistoryDuringInterval(userid, startInterval, endInterval);		
		Assert.assertEquals(3, shapeIndexBeans.size());
		
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(userid);
	}
	
	@Test
	public void testGetRecentShapeIndexModel() {
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
		FitnessShapeIndexBean fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setSessionOfRecord(fitnessShapeIndexBean.getSessionOfRecord() + 1);
		fitnessShapeIndexBean1.setShapeIndex(fitnessShapeIndexBean.getShapeIndex() + 10);
		fitnessShapeIndexBean1.setTimeOfRecord(nowTimeStamp);
		fitnessShapeIndexBean1.setUserid(fitnessShapeIndexBean.getUserid());
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		FitnessShapeIndexBean testShapeIndexBean = fitnessShapeIndexDAO.getRecentShapeIndexModel(fitnessShapeIndexBean.getUserid());
		Assert.assertEquals(fitnessShapeIndexBean1.getSessionOfRecord(), testShapeIndexBean.getSessionOfRecord());
		Assert.assertEquals(fitnessShapeIndexBean1.getTimeOfRecord(), testShapeIndexBean.getTimeOfRecord());
		
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(fitnessShapeIndexBean1.getUserid());
	}
	
	@Test
	public void testUpdateShapeIndexModel() {
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
		FitnessShapeIndexBean testBean = fitnessShapeIndexDAO.getShapeIndexModelByTrainingSessionId(fitnessShapeIndexBean.getSessionOfRecord());
		testBean.setShapeIndex(testBean.getShapeIndex() + 10);
		fitnessShapeIndexDAO.updateShapeIndexModel(testBean);
		
		testBean = fitnessShapeIndexDAO.getShapeIndexModelByTrainingSessionId(testBean.getSessionOfRecord());
		Assert.assertTrue(testBean.getShapeIndex() > fitnessShapeIndexBean.getShapeIndex());
		
		fitnessShapeIndexDAO.updateShapeIndexModel(fitnessShapeIndexBean);
		testBean = fitnessShapeIndexDAO.getShapeIndexModelByTrainingSessionId(testBean.getSessionOfRecord());
		Assert.assertEquals(testBean.getShapeIndex(), fitnessShapeIndexBean.getShapeIndex());
	
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(testBean.getUserid());
		
		testBean.setUserid("");
		Assert.assertEquals(0, fitnessShapeIndexDAO.createFitnessShapeIndexModel(testBean));
		
		testBean.setUserid(null);
		Assert.assertEquals(0, fitnessShapeIndexDAO.createFitnessShapeIndexModel(testBean));
		
		testBean.setSessionOfRecord("");
		Assert.assertEquals(0, fitnessShapeIndexDAO.createFitnessShapeIndexModel(testBean));
	
		testBean.setSessionOfRecord(null);
		Assert.assertEquals(0, fitnessShapeIndexDAO.createFitnessShapeIndexModel(testBean));
	}

	@Test 
	public void testDeleteShapeIndexHistoryForUser() {
		List<FitnessShapeIndexBean> shapeIndexBeans = null;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE,59);
		cal.set(Calendar.SECOND,59);
		Timestamp endInterval = new Timestamp(cal.getTimeInMillis());
		cal.add(Calendar.DATE, -5);
		Timestamp startInterval = new Timestamp(cal.getTimeInMillis());
		shapeIndexBeans = fitnessShapeIndexDAO.getShapeIndexHistoryDuringInterval(userid, startInterval,endInterval);
		Assert.assertEquals(0, shapeIndexBeans.size());
		cal.add(Calendar.DATE, 5);
		
		cal.add(Calendar.HOUR, -1);
		FitnessShapeIndexBean  fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20135");
		fitnessShapeIndexBean1.setShapeIndex(150.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		cal.add(Calendar.DATE, -1);
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20134");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		cal.add(Calendar.DATE, -1);
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20133");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		cal.add(Calendar.DATE, -1);
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20132");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		cal.add(Calendar.DATE, -1);
		fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setSessionOfRecord("20131");
		fitnessShapeIndexBean1.setShapeIndex(130.0);
		fitnessShapeIndexBean1.setTimeOfRecord(new Timestamp(cal.getTimeInMillis()));
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);

		cal.add(Calendar.DATE, -1);
		startInterval = new Timestamp(cal.getTimeInMillis());
		shapeIndexBeans = fitnessShapeIndexDAO.getShapeIndexHistoryDuringInterval(userid, startInterval, endInterval);		
		Assert.assertEquals(5, shapeIndexBeans.size());
		
		fitnessShapeIndexDAO.deleteShapeIndexHistoryForUser(userid);
		shapeIndexBeans = fitnessShapeIndexDAO.getShapeIndexHistoryDuringInterval(userid, startInterval, endInterval);
		Assert.assertEquals(0, shapeIndexBeans.size());
	}
}
