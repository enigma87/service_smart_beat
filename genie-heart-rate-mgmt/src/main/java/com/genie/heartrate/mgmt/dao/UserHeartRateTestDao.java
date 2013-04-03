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
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;



/**
 * @author manojkumar
 *
 */
public class UserHeartRateTestDao 
{
	private static final String UPDATE = "UPDATE user_heart_rate_test SET resting_hr=:restingHr, resting_hr_ts=:restingHrTs, " +
			"maximal_hr=:maximalHr, maximal_hr_ts=:maximalHrTs, threshold_hr=:thresholdHr, threshold_hr_ts=:thresholdHrTs " +
			"WHERE userid=:userid;";
	
	private BasicDataSource dataSource;
	
	public BasicDataSource getDataSource()
	{
		return this.dataSource;
	}
	
	public void setDataDource(BasicDataSource dataSource)
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
		return simpleJdbcInsert.withTableName("")
		.usingColumns("userid", "resting_hr", "resting_hr_ts", "maximal_hr", "maximal_hr_ts", "threshold_hr", "threshold_hr_ts")
		.execute(new BeanPropertySqlParameterSource(userHeartRateTest));
	}
	
	public int updateHeartRateTestResults(UserHeartRateTest heartRateTest)
	{
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE, new BeanPropertySqlParameterSource(heartRateTest));
	}
}
