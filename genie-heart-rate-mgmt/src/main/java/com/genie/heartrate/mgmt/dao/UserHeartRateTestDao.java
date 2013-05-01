/**
 * 
 */
package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;



/**
 * @author manojkumar
 *
 */
public class UserHeartRateTestDao 
{
	private static final String UPDATE = "UPDATE user_heart_rate_test SET resting_heart_rate=:restingHeartRate, resting_heart_rate_timestamp=:restingHeartRateTimestamp, " +
			"maximal_heart_rate=:maximalHeartRate, maximal_heart_rate_timestamp=:maximalHeartRateTimestamp, threshold_heart_rate=:thresholdHeartRate, threshold_heart_rate_timestamp=:thresholdHeartRateTimestamp " +
			"WHERE userid=:userid;";	
	
		
	private BasicDataSource dataSource;
	
	public BasicDataSource getDataSource()
	{
		return this.dataSource;
	}
	
	public void setDataSource(BasicDataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	
	public UserHeartRateTest getHeartRateTestResults(Long userid)
	{
		UserHeartRateTest userHeartRateTest = null;
		try
		{
			userHeartRateTest = new JdbcTemplate(dataSource).queryForObject("SELECT * FROM user_heart_rate_test WHERE userid=?", 
				ParameterizedBeanPropertyRowMapper.newInstance(UserHeartRateTest.class), userid);
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
		return userHeartRateTest;
	}
	
	public int createHeartRateTestResults(UserHeartRateTest userHeartRateTest)
	{
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName("user_heart_rate_test")
		.usingColumns("userid", "resting_heart_rate", "resting_heart_rate_timestamp", "maximal_heart_rate", "maximal_heart_rate_timestamp", "threshold_heart_rate", "threshold_heart_rate_timestamp")
		.execute(new BeanPropertySqlParameterSource(userHeartRateTest));
	}
	
	public int updateHeartRateTestResults(UserHeartRateTest heartRateTest)
	{
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE, new BeanPropertySqlParameterSource(heartRateTest));			
	}
	
	public void deleteHeartRateTestResults(Long userid)
	{
		
		UserHeartRateTest userHeartRateTest = null;
		try
		{
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			userHeartRateTest = jdbcTemplate.queryForObject("SELECT * FROM user_heart_rate_test WHERE userid=?", 
				ParameterizedBeanPropertyRowMapper.newInstance(UserHeartRateTest.class), userid);
			
			if (null != userHeartRateTest ){
				jdbcTemplate.update("DELETE FROM user_heart_rate_test where userid = ?", userid);
			}
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
	}
}
