/**
 * 
 */
package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.genie.heartrate.mgmt.beans.UserHeartRateTest;



/**
 * @author manojkumar
 *
 */
public class UserHeartRateTestDao 
{
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
		return new UserHeartRateTest();
	}
	
	public void createHeartRateTestResults(UserHeartRateTest heartRate)
	{
		
	}
	
	public void updateHeartRateTestResults(UserHeartRateTest heartRate)
	{
		
	}
}
