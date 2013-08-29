package com.genie.smartbeat.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author dhasarathy
 **/

public class SmartbeatIDGeneratorTest {

	@Test
	public void testGetFirstId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNextId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCountFromId() {
		String id = SmartbeatIDGenerator.getFirstId("user1", SmartbeatIDGenerator.MARKER_HEARTRATE_TEST_ID);
		int count = SmartbeatIDGenerator.getCountFromId(id);
		assertEquals(1, count);
	}

	@Test
	public void testGetId() {
		fail("Not yet implemented");
	}

}

