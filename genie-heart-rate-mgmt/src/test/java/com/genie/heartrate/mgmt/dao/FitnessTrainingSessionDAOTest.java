package com.genie.heartrate.mgmt.dao;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;

/**
 * @author dhasarathy
 **/

public class FitnessTrainingSessionDAOTest {
	
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
	private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)appContext.getBean("fitnessTrainingSessionDao");
	private static FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
	private static final long now = new Date().getTime();
	private static final long nowPastOneHour = now - 3600000;
	private static final Integer trainingSessionId = 20131;
	
	@BeforeClass
	public static void setupBeforeClass(){
		fitnessTrainingSessionBean.setUserid("ff2d44bb-8af8-46e3-b88f-0cd777ac188e");
		fitnessTrainingSessionBean.setTrainingSessionId(trainingSessionId);
		fitnessTrainingSessionBean.setStartTime(new Timestamp(now));
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

}

