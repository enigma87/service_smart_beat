/**
 * 
 */
package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.genie.heartrate.mgmt.beans.UserHeartRateZone;



/**
 * @author manojkumar
 *
 */
public class UserHeartRateZoneDao 
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
	
	
	public UserHeartRateZone getHeartRateZone(Long userid)
	{
		return new UserHeartRateZone();
	}
	
	public void createHeartRateZone(UserHeartRateZone heartRateZone)
	{
		
	}
	
	public void updateHeartRateZone(UserHeartRateZone heartRateZone)
	{
		
	}
}
