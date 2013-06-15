package com.genie.heartrate.mgmt.dao;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.genie.heartrate.mgmt.beans.FitnessHomeostasisIndexBean;

public class FitnessHomeostasisIndexDaoTest {

	@Test
	public void testCreateHomeoStasisIndexTest() {
		
		ApplicationContext appContext;
		appContext = new ClassPathXmlApplicationContext("META-INF/spring/testApplicationContext.xml");
		FitnessHomeostasisIndexDAO fitnessHomeostasisIndexDAO = (FitnessHomeostasisIndexDAO) appContext.getBean("fitnessHomeostasisIndexDAO");
		Assert.assertNotNull(fitnessHomeostasisIndexDAO);
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = new FitnessHomeostasisIndexBean();
		fitnessHomeostasisIndexBean.setUserid("1234-ed54");
		fitnessHomeostasisIndexBean.setTraineeClassification(2);
		fitnessHomeostasisIndexBean.setPreviousTotalLoadOfExercise(120.8);
		
		
		//fitnessHomeostasisIndexDAO.createHomeoStasisIndexModel(fitnessHomeostasisIndexBean);
		
	}

}
