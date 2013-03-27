/**
 * 
 */
package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.genie.heartrate.mgmt.beans.HeartRate;



/**
 * @author manojkumar
 *
 */
public class HeartRateDao 
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
	
//	public User getUserInfo(String email)
//	{
//		User user = null;
//		try
//		{
//			user = new JdbcTemplate(dataSource).queryForObject("SELECT * FROM common.user WHERE email=?", 
//				ParameterizedBeanPropertyRowMapper.newInstance(User.class), email);
//		}
//		catch(EmptyResultDataAccessException ex)
//		{
//			
//		}
//		return user;
//	}
	
	
	public HeartRate getHeartRate(Integer userid)
	{
		return new HeartRate();
	}
	
	public void createHeartRate(HeartRate heartRate)
	{
		
	}
	
	public void updateHeartRate(HeartRate heartRate)
	{
		
	}
}
