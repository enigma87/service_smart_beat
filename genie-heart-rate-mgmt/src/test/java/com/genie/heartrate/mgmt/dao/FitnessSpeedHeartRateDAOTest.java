package com.genie.heartrate.mgmt.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.FitnessSpeedHeartRateBean;

public class FitnessSpeedHeartRateDAOTest {

	@Test
	public void testFitnessSpeedHeartRateDAOTest() {
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
	    FitnessSpeedHeartRateDAO fitnessSpeedHeartRateDAO = (FitnessSpeedHeartRateDAO)appContext.getBean("fitnessSpeedHeartRateDAO");
		
		
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = new FitnessSpeedHeartRateBean();
		fitnessSpeedHeartRateBean.setUserid("1001-234-ed34");
		fitnessSpeedHeartRateBean.setCurrentVdot(40.5);
		fitnessSpeedHeartRateBean.setPreviousVdot(80.0);
		fitnessSpeedHeartRateDAO.createSpeedHeartRateModel(fitnessSpeedHeartRateBean);
		
		
	}

}
