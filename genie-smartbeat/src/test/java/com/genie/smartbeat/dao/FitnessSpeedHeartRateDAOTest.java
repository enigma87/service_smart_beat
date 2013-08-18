package com.genie.smartbeat.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.smartbeat.beans.FitnessSpeedHeartRateBean;
import com.genie.smartbeat.dao.FitnessSpeedHeartRateDAO;

public class FitnessSpeedHeartRateDAOTest {
	
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
	private static FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO)appContext.getBean("fitnessSpeedHeartRateDAO");
	private static FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = new FitnessSpeedHeartRateBean();
	private static final String userid = "ff2d44bb-8af8-46e3-b88f-0cd777ac188e";
	private static final Double currentVdot = 90.5;
	private static final Double previouVdot = 80.6;
    
	@BeforeClass
	public static void setupBeforeClass(){
		
		fitnessSpeedHeartRateBean.setUserid(userid);
		fitnessSpeedHeartRateBean.setCurrentVdot(currentVdot);
		fitnessSpeedHeartRateBean.setPreviousVdot(previouVdot);
		
	}
			
    
	@Test
	public void createSpeedHeartRateModel() {
		
		fitnessSpeedHeartRateDAO.createSpeedHeartRateModel(fitnessSpeedHeartRateBean);
		
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean1 = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
		Assert.assertEquals(userid,fitnessSpeedHeartRateBean1.getUserid());
		Assert.assertEquals(currentVdot,fitnessSpeedHeartRateBean1.getCurrentVdot());
		Assert.assertEquals(previouVdot,fitnessSpeedHeartRateBean1.getPreviousVdot());
		
		fitnessSpeedHeartRateDAO.deleteSpeedHeartRateModelByUserid(userid);
		
		fitnessSpeedHeartRateBean1.setUserid("");
		Assert.assertEquals(0, fitnessSpeedHeartRateDAO.createSpeedHeartRateModel(fitnessSpeedHeartRateBean1));
		
	}
	
	@Test
	public void testUpdateFitnessSpeedHeartRateModel() {
		
		fitnessSpeedHeartRateDAO.createSpeedHeartRateModel(fitnessSpeedHeartRateBean);
		
		Double currentVdotLatest = 111.7;
		Double previousVdotLatest = 90.5;
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean1 = new FitnessSpeedHeartRateBean();
		fitnessSpeedHeartRateBean1.setUserid(userid);
		fitnessSpeedHeartRateBean1.setCurrentVdot(currentVdotLatest);
		fitnessSpeedHeartRateBean1.setPreviousVdot(previousVdotLatest);
		
		fitnessSpeedHeartRateDAO.updateSpeedHeartrateModel(fitnessSpeedHeartRateBean1);
		
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean2 = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(userid);
	    Assert.assertEquals(userid, fitnessSpeedHeartRateBean2.getUserid());
	    Assert.assertEquals(currentVdotLatest,fitnessSpeedHeartRateBean2.getCurrentVdot());
	    Assert.assertEquals(previousVdotLatest,fitnessSpeedHeartRateBean2.getPreviousVdot());
		
		fitnessSpeedHeartRateDAO.deleteSpeedHeartRateModelByUserid(userid);
		
		fitnessSpeedHeartRateBean1.setUserid("");
		Assert.assertEquals(0, fitnessSpeedHeartRateDAO.updateSpeedHeartrateModel(fitnessSpeedHeartRateBean1));
	}

	@Test
	public void testGetSpeedHeartRateModelByUserid() {
		fitnessSpeedHeartRateDAO.createSpeedHeartRateModel(fitnessSpeedHeartRateBean);
		
		FitnessSpeedHeartRateBean localFitnessHeartRateBean =  fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(fitnessSpeedHeartRateBean.getUserid(), localFitnessHeartRateBean.getUserid());
		Assert.assertEquals(fitnessSpeedHeartRateBean.getCurrentVdot(), localFitnessHeartRateBean.getCurrentVdot());
		
		fitnessSpeedHeartRateDAO.deleteSpeedHeartRateModelByUserid(fitnessSpeedHeartRateBean.getUserid());
		localFitnessHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(null, localFitnessHeartRateBean);
	}
	
	@Test
	public void testDeleteSpeedHeartRateModelByUserid() {
		fitnessSpeedHeartRateDAO.createSpeedHeartRateModel(fitnessSpeedHeartRateBean);
		FitnessSpeedHeartRateBean localFitnessHeartRateBean =  fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(fitnessSpeedHeartRateBean.getUserid(), localFitnessHeartRateBean.getUserid());
		Assert.assertEquals(fitnessSpeedHeartRateBean.getCurrentVdot(), localFitnessHeartRateBean.getCurrentVdot());
		
		fitnessSpeedHeartRateDAO.deleteSpeedHeartRateModelByUserid(localFitnessHeartRateBean.getUserid());
		localFitnessHeartRateBean = fitnessSpeedHeartRateDAO.getSpeedHeartRateModelByUserid(fitnessSpeedHeartRateBean.getUserid());
		Assert.assertEquals(null, localFitnessHeartRateBean);
		
		fitnessSpeedHeartRateDAO.deleteSpeedHeartRateModelByUserid(fitnessSpeedHeartRateBean.getUserid());
	}
}
