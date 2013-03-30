/**
 * 
 */
package com.genie.account.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.genie.account.mgmt.beans.User;

/**
 * @author manojkumar
 *
 */
public class UserDao 
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
	
	public User getUserInfo(String email)
	{
		User user = null;
		try
		{
			user = new JdbcTemplate(dataSource).queryForObject("SELECT * FROM user WHERE email=?", 
				ParameterizedBeanPropertyRowMapper.newInstance(User.class), email);
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
		return user;
	}
	
	public void createUser(User user)
	{
		
	}
	
	public void updateUser(User user)
	{
		
	}
}
