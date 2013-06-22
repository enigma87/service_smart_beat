package com.genie.heartrate.mgmt.impl;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.FitnessHomeostasisIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessShapeIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessSpeedHeartRateBean;
import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;
import com.genie.heartrate.mgmt.core.FitnessManager;
import com.genie.heartrate.mgmt.dao.FitnessHomeostasisIndexDAO;
import com.genie.heartrate.mgmt.dao.FitnessShapeIndexDAO;
import com.genie.heartrate.mgmt.dao.FitnessSpeedHeartRateDAO;
/**
 * @author vidhun
 *
 */

public class FitnessManagerMySQLImplTest {

	ApplicationContext appContext;	
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	
	@Before
	public void setUpBeforeClass() throws Exception{
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");	
	}

	@Test
	public void testSaveFitnessTrainingSession(){
		
		long now = new Date().getTime();
		long nowPastOneHour = now - 3600000;
		long nowPastFiftyMinutes = now - 3000000;
		long twoDaysAfterNow = now + (2*24*3600000);
		long twoDaysAfterNowPastThreeHours = twoDaysAfterNow - (3*3600000);
		String fitnessTrainingSessionId = "20131";
	    System.out.println(new Timestamp(now));
		/*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = dateFormat.parse("10/06/2012");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = date.getTime();
		currentnew Timestamp(time);*/
		
		/*Creating the Bean for the first Training Session*/	
		FitnessTrainingSessionBean fitnessTrainingSessionBean = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean.setUserid(userid);
		fitnessTrainingSessionBean.setStartTime(new Timestamp(nowPastFiftyMinutes));
		fitnessTrainingSessionBean.setEndTime(new Timestamp(now));
		fitnessTrainingSessionBean.setHrz1Time(4.0);
		fitnessTrainingSessionBean.setHrz2Time(8.0);
		fitnessTrainingSessionBean.setHrz3Time(11.0);
		fitnessTrainingSessionBean.setHrz4Time(3.0);
		fitnessTrainingSessionBean.setHrz5Time(10.0);
		fitnessTrainingSessionBean.setHrz6Time(14.0);
		fitnessTrainingSessionBean.setHrz1Distance(421.7304);
		fitnessTrainingSessionBean.setHrz2Distance(895.1108);
		fitnessTrainingSessionBean.setHrz3Distance(1342.9544);
		fitnessTrainingSessionBean.setHrz4Distance(402.9899);
		fitnessTrainingSessionBean.setHrz5Distance(1408.0273);
		fitnessTrainingSessionBean.setHrz6Distance(2070.7614);
			
        /*Saving the first Fitness training session for the user*/
		FitnessManager fitnessManager = (FitnessManager)appContext.getBean("fitnessManagerMySQLImpl");
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		String trainingSessionId = fitnessTrainingSessionBean.getTrainingSessionId();
		
		/*Creating the DAOs for SpeedHeartRate, ShapeIndex and Homeostasis Models*/
		FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO) appContext.getBean("fitnessSpeedHeartRateDAO");
		FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO) appContext.getBean("fitnessShapeIndexDAO");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) appContext.getBean("fitnessHomeostasisIndexDAO");
		
		/*Getting the bean for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		FitnessShapeIndexBean fitnessShapeIndexBean = fitnessShapeIndexDAO.getRecentShapeIndexModel(userid);
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = fitnessHomeostasisIndexDAO.getHomeostasisIndexModelByUserid(userid);
		
		/*Asserting the Model data for SpeedHeartRate, ShapeIndex and Homeostasis Models for the user*/
		Assert.assertEquals(userid, fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(23.468882249859, fitnessSpeedHeartRateBean.getCurrentVdot());
		Assert.assertNull(null, fitnessSpeedHeartRateBean.getPreviousVdot());
		
		Assert.assertEquals(userid, fitnessShapeIndexBean.getUserid());
		Assert.assertEquals(100.0, fitnessShapeIndexBean.getShapeIndex());
		Assert.assertEquals(trainingSessionId, fitnessShapeIndexBean.getSessionOfRecord());
		Assert.assertNotNull(fitnessShapeIndexBean.getTimeOfRecord());
		
		Assert.assertEquals(userid, fitnessHomeostasisIndexBean.getUserid());
		Assert.assertEquals(212.25, fitnessHomeostasisIndexBean.getCurrentTotalLoadOfExercise());
		Assert.assertEquals(-212.25, fitnessHomeostasisIndexBean.getLocalRegressionMinimumOfHomeostasisIndex());
		Assert.assertNull(fitnessHomeostasisIndexBean.getPreviousTotalLoadOfExercise());
		Assert.assertNotNull(fitnessHomeostasisIndexBean.getCurrentEndTime());
		Assert.assertNull(fitnessHomeostasisIndexBean.getPreviousEndTime());
		
		/*Creating the Bean for the second Training Session*/
		FitnessTrainingSessionBean fitnessTrainingSessionBean1 = new FitnessTrainingSessionBean();
		fitnessTrainingSessionBean1.setUserid(userid);
		fitnessTrainingSessionBean1.setStartTime(new Timestamp(twoDaysAfterNowPastThreeHours));
		fitnessTrainingSessionBean1.setEndTime(new Timestamp(twoDaysAfterNow));
		fitnessTrainingSessionBean1.setHrz1Time(28.0);
		fitnessTrainingSessionBean1.setHrz2Time(92.0);
		fitnessTrainingSessionBean1.setHrz3Time(20.0);
		fitnessTrainingSessionBean1.setHrz4Time(10.0);
		fitnessTrainingSessionBean1.setHrz5Time(15.0);
		fitnessTrainingSessionBean1.setHrz6Time(15.0);
		fitnessTrainingSessionBean1.setHrz1Distance(500.7304);
		fitnessTrainingSessionBean1.setHrz2Distance(12075.2131);
		fitnessTrainingSessionBean1.setHrz3Distance(2865.35891);
		fitnessTrainingSessionBean1.setHrz4Distance(1576.2194);
		fitnessTrainingSessionBean1.setHrz5Distance(2471.7788);
		fitnessTrainingSessionBean1.setHrz6Distance(2588.0264);
	
		/*Saving the second Fitness training session for the user*/
		fitnessManager.saveFitnessTrainingSession(fitnessTrainingSessionBean);
		String trainingSessionId1 = fitnessTrainingSessionBean.getTrainingSessionId();
	}
	
	//@Test
	public void testGetFitnessSupercompensationPoints(){
		
		long now = new Date().getTime();
		long nowPastOneHour = now - 3600000;
		long nowPastTwoDays = now - (3600000*48);
		String fitnessTrainingSessionId = "20131";
	    
		/*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = dateFormat.parse("10/06/2012");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = date.getTime();
		currentnew Timestamp(time);*/
		
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
		fitnessHomeostasisIndexBean.setUserid(userid);
		fitnessHomeostasisIndexBean.setTraineeClassification(2);
		fitnessHomeostasisIndexBean.setCurrentEndTime(new Timestamp(nowPastOneHour));
		fitnessHomeostasisIndexBean.setCurrentTotalLoadOfExercise(120.0);
		fitnessHomeostasisIndexBean.setLocalRegressionMinimumOfHomeostasisIndex(180.0);
		fitnessHomeostasisIndexBean.setPreviousEndTime(new Timestamp(nowPastTwoDays));
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(140.0);		
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) appContext.getBean("fitnessHomeostasisIndexDAO");
		Assert.assertNotNull(fitnessHomeostasisIndexDAO);
		fitnessHomeostasisIndexDAO.createHomeostasisIndexModel(fitnessHomeostasisIndexBean);
		
		
		
		FitnessManager fitnessManager = new FitnessManagerMySQLImpl();
		fitnessManager = (FitnessManager)appContext.getBean("fitnessManagerMySQLImpl");
		Double points = ((FitnessManagerMySQLImpl)fitnessManager).getFitnessSupercompensationPoints(userid);
		fitnessHomeostasisIndexDAO.deleteHomeostasisIndexModelByUserid(userid);
	}
}
