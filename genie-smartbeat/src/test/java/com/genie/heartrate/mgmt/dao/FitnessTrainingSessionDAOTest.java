package com.genie.heartrate.mgmt.dao;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;

/**
 * @author dhasarathy
 **/

public class FitnessTrainingSessionDAOTest {
	
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
	private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)appContext.getBean("fitnessTrainingSessionDAO");
	private static FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
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
	}

	@Test
	public void testCreateAndGetFitnessTrainingSession() {
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		FitnessTrainingSessionBean localTrainingSessionBean = fitnessTrainingSessionDAO.getFitnessTrainingSessionById(trainingSessionId);
		assertNotNull(localTrainingSessionBean);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
	}	

	@Test
	public void testDeleteFitnessTrainingSessionById() {
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		FitnessTrainingSessionBean localTrainingSessionBean = fitnessTrainingSessionDAO.getFitnessTrainingSessionById(trainingSessionId);
		assertNull(localTrainingSessionBean);
	}
	
	@Test
	public void testGetRecentFitnessTrainingSession(){
		
		FitnessTrainingSessionBean fitnessTrainingSessionBean1 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean1.setUserid(userid);
		fitnessTrainingSessionBean1.setTrainingSessionId("20132");
		fitnessTrainingSessionBean1.setStartTime(new Timestamp(nowPastOneHour));
		fitnessTrainingSessionBean1.setEndTime(new Timestamp(now));
		
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean);
		fitnessTrainingSessionDAO.createFitnessTrainingSession(fitnessTrainingSessionBean1);
		
		FitnessTrainingSessionBean fitnessTrainingSessionBean2 = fitnessTrainingSessionDAO.getRecentFitnessTrainingSessionForUser(userid);
		Assert.assertEquals("20132", fitnessTrainingSessionBean2.getTrainingSessionId());
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById("20132");
	
	}

}

