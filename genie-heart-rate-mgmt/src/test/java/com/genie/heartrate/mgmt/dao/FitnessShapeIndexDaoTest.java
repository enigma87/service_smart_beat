package com.genie.heartrate.mgmt.dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.FitnessShapeIndexBean;
import com.genie.heartrate.mgmt.beans.FitnessSpeedHeartRateBean;

public class FitnessShapeIndexDaoTest {

	@Test
	public void testCreateShapeIndexDao() {
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
	    FitnessShapeIndexDAO fitnessShapeIndexDAO = (FitnessShapeIndexDAO)appContext.getBean("fitnessShapeIndexDAO");
		
		FitnessShapeIndexBean fitnessShapeIndexBean = new FitnessShapeIndexBean();
		fitnessShapeIndexBean.setUserid("1001-234-ed34");
		fitnessShapeIndexBean.setTraineeClassification(2);
		fitnessShapeIndexBean.setShapeIndex(90.9);
		fitnessShapeIndexDAO.createFitnessShapeIndexModel(fitnessShapeIndexBean);
			
		
	}

}
