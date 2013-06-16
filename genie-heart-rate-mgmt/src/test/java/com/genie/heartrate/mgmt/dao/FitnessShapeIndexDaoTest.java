package com.genie.heartrate.mgmt.dao;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.FitnessShapeIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessSpeedHeartRateBean;
import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;

public class FitnessShapeIndexDaoTest {
	
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
    FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO)appContext.getBean("fitnessShapeIndexDAO");
	private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)appContext.getBean("fitnessTrainingSessionDAO");
	private static FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
	private static FitnessShapeIndexBean fitnessShapeIndexBean = new FitnessShapeIndexBean();
	private static final long now = new Date().getTime();
	private static final long nowPastOneHour = now - 3600000;
	private static final long nowPastTwoHour = now - 7200000;
	private static final String trainingSessionId = "20131";
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	
	@BeforeClass
	public static void setupBeforeClass(){
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setTrainingSessionId(trainingSessionId);
		fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastTwoHour));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(nowPastOneHour));
		fitnessShapeIndexBean.setUserid(userid);
		fitnessShapeIndexBean.setSessionOfRecord(trainingSessionId);
		fitnessShapeIndexBean.setShapeIndex(100.0);
		fitnessShapeIndexBean.setTimeOfRecord(new Timestamp(nowPastOneHour));
		
	}

	//@Test
	public void testCreateShapeIndexDao() {
		
	
		
		FitnessShapeIndexBean fitnessShapeIndexBean = new FitnessShapeIndexBean();
		fitnessShapeIndexBean.setUserid("1001-234-ed34");		
		fitnessShapeIndexBean.setShapeIndex(90.9);
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
			
		
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
		
		FitnessTrainingSessionBean fitnessTrainingSessionBean2 = fitnessTrainingSessionDAO.getRecentFitnessTrainingSessionByUserid(userid);
		String recentTrainingSessionId = fitnessTrainingSessionBean2.getTrainingSessionId();
		Assert.assertEquals("20132", recentTrainingSessionId );
		FitnessShapeIndexBean fitnessShapeIndexBean2 = fitnessShapeIndexDAO.getRecentShapeIndexModelByTraininSessionId(recentTrainingSessionId);
		Assert.assertEquals(150.0, fitnessShapeIndexBean2.getShapeIndex() );
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById("20132");
		
	}

}
