package com.genie.smartbeat.beans;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.Assert;

import com.genie.smartbeat.beans.FitnessShapeIndexBean;

import org.junit.Test;

public class FitnessShapeIndexBeanTest {

	@Test
	public void testFitnessShapeIndexBean() {
		String userid =  "123456_abcd_789";	
		Double shapeIndex = 105.78;
		long now = new Date().getTime();
		Timestamp timeOfRecord = new Timestamp(now);	
		String sessionOfRecord = "2013_05";
		
		FitnessShapeIndexBean fitnessShapeIndexBean = new FitnessShapeIndexBean();
		fitnessShapeIndexBean.setUserid(userid);
		fitnessShapeIndexBean.setShapeIndex(shapeIndex);
		fitnessShapeIndexBean.setTimeOfRecord(timeOfRecord);
		fitnessShapeIndexBean.setSessionOfRecord(sessionOfRecord);
		
		Assert.assertEquals(userid,fitnessShapeIndexBean.getUserid());
		Assert.assertEquals(shapeIndex,fitnessShapeIndexBean.getShapeIndex());
		Assert.assertEquals(timeOfRecord,fitnessShapeIndexBean.getTimeOfRecord());
		Assert.assertEquals(sessionOfRecord,fitnessShapeIndexBean.getSessionOfRecord());
		
	}

}
