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

import com.genie.heartrate.mgmt.beans.UserHeartRateZone;



/**
 * @author manojkumar
 *
 */
public class UserHeartRateZoneDao 
{
	private static final String UPDATE = "UPDATE user_heart_rate_zone set hrz1_start=:hrz1Start, hrz1_end=:hrz1End, " +
			"hrz2_start=:hrz2Start, hrz2_end=:hrz2End, hrz3_start=:hrz3Start, hrz3_end=:hrz3End, hrz4_start=:hrz4Start, " +
			"hrz4_end=:hrz4End, hrz5_start=:hrz5Start, hrz5_end=:hrz5End, hrz6_start=:hrz6Start, hrz6_end=:hrz6End " +
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
	
	
	public UserHeartRateZone getHeartRateZone(Long userid)
	{
		UserHeartRateZone userHeartRateZone = null;
		try
		{
			userHeartRateZone = new JdbcTemplate(dataSource).queryForObject("SELECT * FROM user_heart_rate_zone WHERE userid=?", 
				ParameterizedBeanPropertyRowMapper.newInstance(UserHeartRateZone.class), userid);
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
		return userHeartRateZone;
	}
	
	public int createHeartRateZone(UserHeartRateZone heartRateZone)
	{
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName("")
		.usingColumns("userid", "hrz1_start", "hrz1_end", "hrz2_start", "hrz2_end", "hrz3_start", "hrz3_end",
				"hrz4_start", "hrz4_end", "hrz5_start", "hrz5_end", "hrz6_start", "hrz6_end", "created_ts", "updated_ts")
		.execute(new BeanPropertySqlParameterSource(heartRateZone));
	}
	
	public int updateHeartRateZone(UserHeartRateZone heartRateZone)
	{
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE, new BeanPropertySqlParameterSource(heartRateZone));
	}
}
