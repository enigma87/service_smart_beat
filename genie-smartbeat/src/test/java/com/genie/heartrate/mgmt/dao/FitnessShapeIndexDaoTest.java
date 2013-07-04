package com.genie.heartrate.mgmt.dao;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessShapeIndexBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.dao.FitnessShapeIndexDAO;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;

public class FitnessShapeIndexDaoTest {
	
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
    private static FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO)appContext.getBean("fitnessShapeIndexDAO");
	private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)appContext.getBean("fitnessTrainingSessionDAO");
	private static FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
	private static FitnessShapeIndexBean fitnessShapeIndexBean = new FitnessShapeIndexBean();
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	private static final long now = new Date().getTime();
	private static final long nowPastOneHour = now - 3600000;
	private static final long nowPastTwoHour = now - 7200000;
	private static final Timestamp nowTimeStamp = new Timestamp(now);
	private static final Timestamp nowPastOneHourTimeStamp = new Timestamp(nowPastOneHour);
	private static final Timestamp nowPastTwoHourTimeStamp = new Timestamp(nowPastTwoHour);
	private static final String trainingSessionId = "20131";
	private static final Double shapeIndex = 100.0;
	
	
	@BeforeClass
	public static void setupBeforeClass(){
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
	public void testCreateAndGetShapeIndexDao() {
			
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
		
		FitnessShapeIndexBean fitnessShapeIndexBean1 = fitnessShapeIndexDAO.getRecentShapeIndexModel(userid);
		Assert.assertNotNull(fitnessShapeIndexBean1);
		Assert.assertEquals(userid, fitnessShapeIndexBean1.getUserid());
		Assert.assertEquals(shapeIndex, fitnessShapeIndexBean1.getShapeIndex());
		Assert.assertNotNull(fitnessShapeIndexBean1.getTimeOfRecord());
		Assert.assertEquals(trainingSessionId, fitnessShapeIndexBean1.getSessionOfRecord());
		
		fitnessShapeIndexDAO.deleteShapeIndexModel(userid);
					
	}
	
	@Test
	public void testUpdateShapeIndexDao() {
			
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
		
		FitnessShapeIndexBean fitnessShapeIndexBean1 = new FitnessShapeIndexBean();
		fitnessShapeIndexBean1.setUserid(userid);
		fitnessShapeIndexBean1.setShapeIndex(180.0);
		fitnessShapeIndexBean1.setSessionOfRecord("20132");
		fitnessShapeIndexBean1.setTimeOfRecord(nowTimeStamp);
		
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean1);
		
		FitnessShapeIndexBean fitnessShapeIndexBean2 = fitnessShapeIndexDAO.getRecentShapeIndexModel(userid);
		Assert.assertNotNull(fitnessShapeIndexBean2);
		Assert.assertEquals(userid, fitnessShapeIndexBean2.getUserid());
		Assert.assertEquals(180.0, fitnessShapeIndexBean2.getShapeIndex());
		Assert.assertNotNull(fitnessShapeIndexBean2.getTimeOfRecord());
		Assert.assertEquals("20132", fitnessShapeIndexBean2.getSessionOfRecord());
		
		
		fitnessShapeIndexDAO.deleteShapeIndexModel(userid);
					
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
		Assert.assertEquals("20132", recentTrainingSessionId );
		FitnessShapeIndexBean fitnessShapeIndexBean2 = fitnessShapeIndexDAO.getShapeIndexModelByTrainingSessionId(recentTrainingSessionId);
		Assert.assertEquals(150.0, fitnessShapeIndexBean2.getShapeIndex() );
		
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById("20132");
		fitnessShapeIndexDAO.deleteShapeIndexModel(userid);
		
		
	}

}
