package com.genie.smartbeat.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;

/**
 * @author dhasarathy
 **/

public class FitnessHeartrateTestDAO {

	private static final String TABLE_FITNESS_HEARTRATE_TEST = "fitness_heartrate_test";
	private static final String []COLUMNS_FITNESS_HEARTRATE_TEST = {"userid", 
																	"heartrate_test_id", 
																	"heartrate_type", 
																	"heartrate", 
																	"time_of_record",
																	"day_of_record"};
	private static final int COLUMN_USERID 				= 0;
	private static final int COLUMN_HEARTRATE_TEST_ID 	= 1;
	private static final int COLUMN_HEARTRATE_TYPE 		= 2;
	private static final int COLUMN_TIME_OF_RECORD 		= 4;

	
	private BasicDataSource dataSource;
	
	public BasicDataSource getDataSource() {
		return dataSource;
	}
	
	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public int createHeartrateTest(FitnessHeartrateTestBean fitnessHeartrateTestBean){
		// set nano seconds to 0 in Timestamp - MySQL doesn't support it
		fitnessHeartrateTestBean.getTimeOfRecord().setNanos(0);
		
		if (fitnessHeartrateTestBean.isValidForTableInsert()) {
			SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
			
			return simpleJdbcInsert.withTableName(TABLE_FITNESS_HEARTRATE_TEST)
					.usingColumns(COLUMNS_FITNESS_HEARTRATE_TEST)
					.execute(new BeanPropertySqlParameterSource(fitnessHeartrateTestBean));
		}

		return 0;
	}
	
	private static final String QUERY_ALL_USING_TEST_ID = "SELECT * FROM " + TABLE_FITNESS_HEARTRATE_TEST + " WHERE " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_HEARTRATE_TEST_ID] + " =?";
	public FitnessHeartrateTestBean getHeartrateTestByTestId(String heartrateTestId){
		FitnessHeartrateTestBean fitnessHeartrateTestBean = null;
		try{
			fitnessHeartrateTestBean = new JdbcTemplate(dataSource).queryForObject(QUERY_ALL_USING_TEST_ID, ParameterizedBeanPropertyRowMapper.newInstance(FitnessHeartrateTestBean.class),heartrateTestId);
		}catch(DataAccessException e){
			
		}
		return fitnessHeartrateTestBean;
	}
	
	private static final String DELETE_HEARTRATE_TEST_BY_ID = "DELETE FROM " + TABLE_FITNESS_HEARTRATE_TEST + " WHERE " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_HEARTRATE_TEST_ID] + " =?";
	public void deleteHeartrateTestByTestId(String heartrateTestId){
		FitnessHeartrateTestBean fitnessHeartrateTestBean = getHeartrateTestByTestId(heartrateTestId);
		if(null != fitnessHeartrateTestBean){
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(DELETE_HEARTRATE_TEST_BY_ID,heartrateTestId);
		}
	}
		
	private static final String QUERY_SELECT_RECENT_TIME = "(" + 
				"select max(" + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_TIME_OF_RECORD] + ")" + 
				" FROM " + TABLE_FITNESS_HEARTRATE_TEST + 
				" WHERE " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_USERID] + " =?" + 																	
			 ")";
	private static final String QUERY_RECENT_TEST = "SELECT * FROM " + TABLE_FITNESS_HEARTRATE_TEST + 
	" WHERE "+ COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_USERID]+ " = ?" + 
	" AND "  + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_TIME_OF_RECORD]+ " = "+ QUERY_SELECT_RECENT_TIME;
	public FitnessHeartrateTestBean getRecentHeartrateTestForUser(String userid){
		FitnessHeartrateTestBean fitnessHeartrateTestBean = null;
		try{
			fitnessHeartrateTestBean =  new JdbcTemplate(dataSource).queryForObject(QUERY_RECENT_TEST, 
					ParameterizedBeanPropertyRowMapper.newInstance(FitnessHeartrateTestBean.class),userid, userid);
		}catch(DataAccessException e){
			
		}
		return fitnessHeartrateTestBean;
	}
	
	private static final String QUERY_ALL_TESTS = 	"SELECT * FROM " + TABLE_FITNESS_HEARTRATE_TEST + 
			" WHERE " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_USERID] + "=?";			

	public List<FitnessHeartrateTestBean> getAllHeartrateTestsByUser(String userid){
		List<FitnessHeartrateTestBean> heartrateTestBeans = new ArrayList<FitnessHeartrateTestBean>();		
		try{
			heartrateTestBeans =  new JdbcTemplate(this.getDataSource()).query(QUERY_ALL_TESTS,
					ParameterizedBeanPropertyRowMapper.newInstance(FitnessHeartrateTestBean.class),userid);			
		}catch(DataAccessException e){
			
		}
		return heartrateTestBeans;
	}
	
	
	private static final String COUNT_QUERY_ALL_TESTS = "SELECT COUNT(*) FROM ( " +
					QUERY_ALL_TESTS +
					" ) AS TEMP";
	public Integer getNumberOfHeartrateTestsByUser(String userid){
		Integer numberofHeartRateTests = 0;
		try{
			numberofHeartRateTests =  new JdbcTemplate(dataSource).queryForInt(COUNT_QUERY_ALL_TESTS, userid);
		}catch(DataAccessException e){
			
		}
		return numberofHeartRateTests;
	}
	
	private static final String QUERY_SELECT_RECENT_TIME_BY_TYPE = "(" + 
																		"select max(" + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_TIME_OF_RECORD] + ")" + 
																		" FROM " + TABLE_FITNESS_HEARTRATE_TEST + 
																		" WHERE " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_USERID] + " =?" + 
																		" AND " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_HEARTRATE_TYPE] + " =?" +														
																	 ")";
	private static final String QUERY_RECENT_TEST_BY_TYPE = "SELECT * FROM " + TABLE_FITNESS_HEARTRATE_TEST + 
															" WHERE "+ COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_USERID]+ " = ?" + 
															" AND "  + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_TIME_OF_RECORD]+ " = "+ QUERY_SELECT_RECENT_TIME_BY_TYPE;

	public FitnessHeartrateTestBean getRecentHeartrateTestForUserByType(String userid, Integer heartrateType){
		
		FitnessHeartrateTestBean fitnessHeartrateTestBean = null;		
		try{
			fitnessHeartrateTestBean =  new JdbcTemplate(dataSource).queryForObject(QUERY_RECENT_TEST_BY_TYPE, 
			ParameterizedBeanPropertyRowMapper.newInstance(FitnessHeartrateTestBean.class),userid, userid,heartrateType);
		}catch(DataAccessException e){
		
		}		
		return fitnessHeartrateTestBean;
	}
 	
	private static final String QUERY_ALL_TESTS_BY_TYPE = 	"SELECT * FROM " + TABLE_FITNESS_HEARTRATE_TEST + 
															" WHERE " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_USERID] + "=?" +
															" AND "	  + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_HEARTRATE_TYPE] + "=?";
	
	private static final String COUNT_QUERY_ALL_TESTS_BY_TYPE = "SELECT COUNT(*) FROM ( " +
																QUERY_ALL_TESTS_BY_TYPE +
																" ) AS TEMP";
	public Integer getNumberOfHeartRateTestsForUserByType(String userid, Integer heartrateType){
		
		Integer numberofHeartRateTests = 0;
		try{
			numberofHeartRateTests =  new JdbcTemplate(dataSource).queryForInt(COUNT_QUERY_ALL_TESTS_BY_TYPE, userid, heartrateType);
		}catch(DataAccessException e){
			
		}
		return numberofHeartRateTests;
	}
		
	public List<FitnessHeartrateTestBean> getNRecentHeartRateTestsForUserByType(String userid, Integer heartrateType, Integer n){
		String QUERY_N_RECENT_BY_TYPE =  QUERY_ALL_TESTS_BY_TYPE +
										 " ORDER BY " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_TIME_OF_RECORD] + " DESC" +
										 " LIMIT " + n;				
		List<FitnessHeartrateTestBean> nRecentHeartRateTests = new ArrayList<FitnessHeartrateTestBean>();
		try{
			nRecentHeartRateTests = new JdbcTemplate(dataSource).query(QUERY_N_RECENT_BY_TYPE, ParameterizedBeanPropertyRowMapper.newInstance(FitnessHeartrateTestBean.class), userid, heartrateType);
		}catch(DataAccessException e){
			
		}
		return nRecentHeartRateTests;
	}
	
	public Integer getDayOfRecordForUser(String userid){
		Integer recentDayofRecord = 0;
		FitnessHeartrateTestBean fitnessHeartRateTestBean = null;
		fitnessHeartRateTestBean = getRecentHeartrateTestForUser(userid);
		if(null != fitnessHeartRateTestBean){
			recentDayofRecord = fitnessHeartRateTestBean.getDayOfRecord();
		}
		return recentDayofRecord;
	}
	
	private static final String QUERY_TODAYS_TESTS = 	"SELECT * FROM " + TABLE_FITNESS_HEARTRATE_TEST + 
			" WHERE " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_USERID] 			+ "=?"  +
			" AND "   + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_TIME_OF_RECORD] 	+ ">=?" +
			" AND "   + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_TIME_OF_RECORD] 	+ "<=?";			

	private static final String COUNT_QUERY_TODAYS_TESTS = "SELECT COUNT(*) FROM ( " +
						QUERY_TODAYS_TESTS +
					" ) AS TEMP";

	public Integer getTodaysHeartRateTestCountForUser(String userid){
		Integer todaysHeartRateTestCount = 0;
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);		
		Timestamp today0000 = new Timestamp(cal.getTime().getTime());
		//System.out.println(today0000.toString());				
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		Timestamp today2359 = new Timestamp(cal.getTime().getTime());
		//System.out.println(today2359.toString());		
		try{
			todaysHeartRateTestCount =  new JdbcTemplate(dataSource).queryForInt(COUNT_QUERY_TODAYS_TESTS, userid,today0000,today2359);
		}catch(DataAccessException e){
			
		}
		
		return todaysHeartRateTestCount;
	}
	
	private static final String DELETE_ALL_TESTS = 	"DELETE FROM " + TABLE_FITNESS_HEARTRATE_TEST + 
													" WHERE " + COLUMNS_FITNESS_HEARTRATE_TEST[COLUMN_USERID] + "=?";				
	public void deleteAllHeartrateTestsForUser(String userid){
		try{
			new JdbcTemplate(dataSource).update(DELETE_ALL_TESTS, userid);
		}catch(DataAccessException e){
			
		}
	}
	
	
}
