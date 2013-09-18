package com.genie.smartbeat.util;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

/**
 * @author dhasarathy
 **/

public class SmartbeatIDGeneratorTest {

	@Test
	public void testGetFirstId() {
		
	}

	@Test
	public void testGetNextId() {
		
	}

	@Test
	public void testGetCountFromId() {
		String id = SmartbeatIDGenerator.getFirstId("user1", SmartbeatIDGenerator.MARKER_HEARTRATE_TEST_ID);
		int count = SmartbeatIDGenerator.getCountFromId(id);
		assertEquals(1, count);
	}

	@Test
	public void testGetId() {
		
	}
	
	@Test
	public void testGetYearFromId(){
		String id = SmartbeatIDGenerator.getFirstId("user1", SmartbeatIDGenerator.MARKER_HEARTRATE_TEST_ID);
		int year = SmartbeatIDGenerator.getYearFromId(id);
		Calendar cal = Calendar.getInstance();
		assertEquals(cal.get(Calendar.YEAR), year);
	}

}

