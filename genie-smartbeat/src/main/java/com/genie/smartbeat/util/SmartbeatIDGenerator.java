package com.genie.smartbeat.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTimeUtils;

/**
 * @author dhasarathy
 **/

public class SmartbeatIDGenerator {

	private static final String DELIMITER 	= "_";
	
	private static final int INDEX_USERID 		= 0;
	private static final int INDEX_MARKER	 	= 1;
	private static final int INDEX_YEAR 		= 2;
	private static final int INDEX_TEST_COUNT 	= 3;
	
	public static final String MARKER_TRAINING_SESSION_ID 	= "TRN";
	public static final String MARKER_HEARTRATE_TEST_ID 	= "HRT";
	
	public static String getFirstId(String userid, String marker){
		SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
		Date currentDate = new Date();
		return 	userid + DELIMITER + 
				marker + DELIMITER + 
				formatYear.format(currentDate) + DELIMITER + 
				"1";
	}
	
	public static String getNextId(String previousId){
		String nextId = null;
		String[] parts = previousId.split(DELIMITER);
		SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
		Date currentDate = new Date(DateTimeUtils.currentTimeMillis());
		String currentYear = formatYear.format(currentDate);
		Integer nextCount;
		if(currentYear.equals(parts[INDEX_YEAR])){
			nextCount = Integer.parseInt(parts[INDEX_TEST_COUNT]) + 1;			
		}else{
			nextCount = 1;
		}
		nextId = parts[INDEX_USERID] + DELIMITER +
								parts[INDEX_MARKER] + DELIMITER +
								currentYear + DELIMITER + 
								nextCount;
		return nextId;
	}
	
	public static String getId(String userid, String marker, Integer majorIndex, Integer minorIndex){
		String Id = userid+"_"+marker+"_"+majorIndex+"_"+minorIndex;
		return Id;
	}
}

