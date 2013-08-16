package com.genie.smartbeat.dao;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.core.FitnessManager;
import com.genie.smartbeat.dao.FitnessTrainingSessionDAO;
import com.genie.smartbeat.domain.ShapeIndexAlgorithm;
import com.genie.smartbeat.impl.FitnessManagerMySQLImpl;

/**
 * @author dhasarathy
 **/

public class FitnessTrainingSessionDAOTest {
	
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
	private static FitnessTrainingSessionDAO fitnessTrainingSessionDAO = (FitnessTrainingSessionDAO)appContext.getBean("fitnessTrainingSessionDAO");
	private static FitnessManagerMySQLImpl fitnessTrainingSessionManager = new FitnessManagerMySQLImpl();
	private static FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
	private static final long now = new Date().getTime();
	private static final long nowPastOneHour = now - 3600000;
	private static final long nowPastTwoHour = now - 7200000;
	private static final String trainingSessionId = "20131";
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	
	@BeforeClass
	public static void setupBeforeClass(){
		//set up test data
		setupTestData();
		
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setTrainingSessionId(trainingSessionId);
		fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastTwoHour));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(nowPastOneHour));
		fitnessTrainingSessionBean.setSurfaceIndex(ShapeIndexAlgorithm.RUNNING_SURFACE_MUD_SNOW_SAND);
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

		fitnessTrainingSessionDAO.deleteFitnessTrainingSessionById(trainingSessionId);
		localTrainingSessionBean = fitnessTrainingSessionDAO.getFitnessTrainingSessionById(trainingSessionId);
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
	
	@Test
	public void testGetFitnessTrainingSessionByRange() {
		
			List<String>  sessions = fitnessTrainingSessionDAO.getFitnessTrainingSessionByTimeRange("TEST073a9e7d-9cf2-49a0-8926-f27362fd547e" ,Timestamp.valueOf("2013-07-04 00:00:00"), Timestamp.valueOf("2013-07-05 23:59:59"));
			for (Iterator<String> i = sessions.iterator(); i.hasNext();) {
				String id = i.next();
				System.out.println(id);
			}
	}
	
	public static void setupTestData() {

		// throw in some dummy values to test
		String [][] dummyValues = {{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","1","2013-07-03 18:23:10","2013-07-03 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
			{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","2","2013-07-04 18:23:10","2013-07-04 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
			{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","3","2013-07-05 18:23:10","2013-07-05 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
			{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","4","2013-08-03 18:23:10","2013-08-03 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
			{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","5","2013-08-04 18:23:10","2013-08-04 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
			{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","6","2013-08-05 18:23:10","2013-08-05 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
			{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","7","2013-08-06 18:23:10","2013-08-06 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
			{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","8","2013-08-07 18:23:10","2013-08-07 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"},
			{"TEST073a9e7d-9cf2-49a0-8926-f27362fd547e","9","2013-08-08 18:23:10","2013-08-08 18:23:10","1","2","3","4","5","6","1","2","3","4","5","6","1"}};

		for (int i=0; i < dummyValues.length; i++) {
			FitnessTrainingSessionBean newTrainingSession = new FitnessTrainingSessionBean();
			String [] row = dummyValues[i];
			
			newTrainingSession.setUserid(row[0]);
			newTrainingSession.setTrainingSessionId(row[1]);
			newTrainingSession.setStartTime(Timestamp.valueOf(row[2]));
			newTrainingSession.setEndTime(Timestamp.valueOf(row[3]));
			newTrainingSession.setHrz1Time(Double.valueOf(row[4]));
			newTrainingSession.setHrz2Time(Double.valueOf(row[5]));
			newTrainingSession.setHrz3Time(Double.valueOf(row[6]));
			newTrainingSession.setHrz4Time(Double.valueOf(row[7]));
			newTrainingSession.setHrz5Time(Double.valueOf(row[8]));
			newTrainingSession.setHrz6Time(Double.valueOf(row[9]));
			newTrainingSession.setHrz1Distance(Double.valueOf(row[10]));
			newTrainingSession.setHrz1Distance(Double.valueOf(row[11]));
			newTrainingSession.setHrz1Distance(Double.valueOf(row[12]));
			newTrainingSession.setHrz1Distance(Double.valueOf(row[13]));
			newTrainingSession.setHrz1Distance(Double.valueOf(row[14]));
			newTrainingSession.setHrz1Distance(Double.valueOf(row[15]));
			newTrainingSession.setSurfaceIndex(Integer.valueOf(row[16]));
		
			fitnessTrainingSessionDAO.createFitnessTrainingSession(newTrainingSession);
		}
	}
	
	@AfterClass
	public static void freeTestData () {
		fitnessTrainingSessionDAO.deleteTestData();
	}
}