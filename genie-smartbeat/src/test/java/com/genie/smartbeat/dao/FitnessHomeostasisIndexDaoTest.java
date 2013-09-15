package com.genie.smartbeat.dao;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;
import com.genie.smartbeat.dao.FitnessHomeostasisIndexDAO;

public class FitnessHomeostasisIndexDaoTest {

	private static AbstractApplicationContext appContext;
	private static FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) appContext.getBean("fitnessHomeostasisIndexDAO");
	private static FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
	private static final long now = new Date().getTime();
	private static final long nowPastOneHour = now - 3600000;
	private static final long nowPastTwoHour = now - 7200000;
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	private static final Integer traineeClassification = 2;
	private static final Double localRegressionMinimumOfHomeostasisIndex = 130.0;
	private static final Double recentMinimumOfHomeostasisIndex = 110.0;
	private static final Double recentTotalLoadOfExercise = 100.0;
	private static final Double previousTotalLoadOfExercise = 125.0;
	
	@BeforeClass
	public static void setupBeforeClass(){
	
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		appContext.registerShutdownHook();
		fitnessHomeostasisIndexBean.setUserid(userid);
		fitnessHomeostasisIndexBean.setTraineeClassification(traineeClassification);
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(localRegressionMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentMinimumOfHomeostasisIndex(recentMinimumOfHomeostasisIndex);
		fitnessHomeostasisIndexBean.setRecentTotalLoadOfExercise(recentTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(previousTotalLoadOfExercise);
		fitnessHomeostasisIndexBean.setRecentEndTime(new Timestamp(now));
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(nowPastTwoHour));
	}
	
	@Before
	public void setUpBeforeTest() throws Exception{
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
	}
	
	@Test
	public void testCreateAndGetHomeoStasisIndex() {
		
	    fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
	    FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean1 = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
	    
	    Assert.assertNotNull(fitnessHomeostasisIndexBean1);
	    Assert.assertEquals(userid, fitnessHomeostasisIndexBean1.getUserid());
	    Assert.assertEquals(traineeClassification, fitnessHomeostasisIndexBean1.getTraineeClassification());
	    Assert.assertEquals(localRegressionMinimumOfHomeostasisIndex, fitnessHomeostasisIndexBean1.getLocalRegressionMinimumOfHomeostasisIndex());
	    Assert.assertEquals(recentMinimumOfHomeostasisIndex, fitnessHomeostasisIndexBean1.getRecentMinimumOfHomeostasisIndex());
	    Assert.assertEquals(recentTotalLoadOfExercise, fitnessHomeostasisIndexBean1.getRecentTotalLoadOfExercise());
	    Assert.assertEquals(previousTotalLoadOfExercise, fitnessHomeostasisIndexBean1.getPreviousTotalLoadOfExercise());
	    Assert.assertNotNull(fitnessHomeostasisIndexBean1.getRecentEndTime());
	    Assert.assertNotNull(fitnessHomeostasisIndexBean1.getPreviousEndTime());
	    
	    fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);

	    fitnessHomeostasisIndexBean1.setUserid("");
	    Assert.assertEquals(0, fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean1));
	
	    fitnessHomeostasisIndexBean1.setUserid(null);
	    Assert.assertEquals(0, fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean1));
	}
	
	@Test
	public void testUpdateHomeoStasisIndexModel() {
		
	    fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);     
	    
	    FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean1 = new FitnessHomeostasisIndexBean();
	    fitnessHomeostasisIndexBean1.setUserid(userid);
	    fitnessHomeostasisIndexBean1.setTraineeClassification(3);
	    fitnessHomeostasisIndexBean1.setLocalRegressionMinimumOfHomeostasisIndex(180.0);
	    fitnessHomeostasisIndexBean1.setRecentMinimumOfHomeostasisIndex(115.0);
	    fitnessHomeostasisIndexBean1.setRecentTotalLoadOfExercise(140.3);
	    fitnessHomeostasisIndexBean1.setPreviousTotalLoadOfExercise(100.0);
	    fitnessHomeostasisIndexBean1.setPreviousEndTime(new Timestamp(now));
	    fitnessHomeostasisIndexBean1.setRecentEndTime(new Timestamp(nowPastOneHour));
	    
	    fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean1);
	    
	    FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean2 = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);   
	    
	    Assert.assertNotNull(fitnessHomeostasisIndexBean2);
	    Assert.assertEquals(userid, fitnessHomeostasisIndexBean2.getUserid());
	    Assert.assertEquals((Integer)3, fitnessHomeostasisIndexBean2.getTraineeClassification());
	    Assert.assertEquals(180.0, fitnessHomeostasisIndexBean2.getLocalRegressionMinimumOfHomeostasisIndex());
	    Assert.assertEquals(115.0, fitnessHomeostasisIndexBean2.getRecentMinimumOfHomeostasisIndex());
	    Assert.assertEquals(140.3, fitnessHomeostasisIndexBean2.getRecentTotalLoadOfExercise());
	    Assert.assertEquals(100.0, fitnessHomeostasisIndexBean2.getPreviousTotalLoadOfExercise());
	    Assert.assertNotNull(fitnessHomeostasisIndexBean2.getRecentEndTime());
	    Assert.assertNotNull(fitnessHomeostasisIndexBean2.getPreviousEndTime());
	    
	    fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);

	    fitnessHomeostasisIndexBean1.setUserid("");
	    Assert.assertEquals(0, fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean1));
	
	    fitnessHomeostasisIndexBean1.setUserid(null);
	    Assert.assertEquals(0, fitnessHomeostasisIndexDAO.updateHomeostasisIndexModel(fitnessHomeostasisIndexBean1));
	}
	
	@Test
	public void testGetTraineeClassificationByUserid() {
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
		Integer traineeClassification = fitnessHomeostasisIndexDAO.getTraineeClassificationByUserid(fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(fitnessHomeostasisIndexBean.getTraineeClassification(), traineeClassification);
		
		traineeClassification = fitnessHomeostasisIndexDAO.getTraineeClassificationByUserid("NONEXISTENT");
		Assert.assertEquals(null, traineeClassification);
		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
	}

	@Test
	public void testDeleteHomeostasisIndexModelByUserid() {
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		FitnessHomeostasisIndexBean testBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(fitnessHomeostasisIndexBean.getUserid(), testBean.getUserid());
		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
		testBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(null, testBean);
		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
		testBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(null, testBean);
	}
	
	@Test
	public void testGetHomeostasisIndexModelByUserid() {
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		FitnessHomeostasisIndexBean testBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(fitnessHomeostasisIndexBean.getTraineeClassification(), testBean.getTraineeClassification());
		Assert.assertEquals(fitnessHomeostasisIndexBean.getUserid(), testBean.getUserid());
		
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
		testBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid());
		Assert.assertNull(testBean);
	}
}
