/**
 * 
 */
package com.genie.social.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.social.beans.UserBean;

/**
 * @author vidhun
 *
 */
public class UserDao 
{	
	private BasicDataSource dataSource;
	private static final String[] COLUMNS_USER_BEAN = {"userid", "access_token", "access_token_type", "first_name", "middle_name", "last_name", "dob", "email", "image_url", "created_ts", "last_updated_ts", "last_login_ts", "active", "privilege_level"};
	
	public BasicDataSource getDataSource()
	{
		return this.dataSource;
	}
	
	public void setDataSource(BasicDataSource dataSource)
	{
		this.dataSource = dataSource;
	}		
	
	public int createUser(UserBean user)
	{
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName("user")
		.usingColumns(COLUMNS_USER_BEAN)
		.execute(new BeanPropertySqlParameterSource(user));
	}
	
	private static final String UPDATE = "UPDATE user SET access_token=:accessToken, access_token_type=:accessTokenType, first_name=:firstName, middle_name=:middleName, last_name=:lastName, dob=:dob, " +
			"email=:email, image_url=:imageUrl, created_ts=:createdTs, last_updated_ts=:lastUpdatedTs, last_login_ts=:lastLoginTs, active=:active, privilege_level=:privilegeLevel " +
			"WHERE userid=:userid;";	
	
	public int updateUser(UserBean user)
	{
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE, new BeanPropertySqlParameterSource(user));
	}
	
	public void deleteUser(String userid){
	
		UserBean user = null;
		try
		{
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			user = jdbcTemplate.queryForObject("SELECT * FROM user WHERE userid=?", 
			ParameterizedBeanPropertyRowMapper.newInstance(UserBean.class), userid);
			
			// when query returns null it is handled in catch, no need for a null check
			jdbcTemplate.update("DELETE FROM user where userid = ?", userid);
		}
		catch(EmptyResultDataAccessException ex)
		{
			return;
		}
	}
	
	public UserBean getUserInfoByEmail(String email)
	{
		UserBean user = null;
		try
		{
			user = new JdbcTemplate(dataSource).queryForObject("SELECT * FROM user WHERE email=?", 
				ParameterizedBeanPropertyRowMapper.newInstance(UserBean.class), email);
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
		return user;
	}
	
	public UserBean getUserInfo(String userid)
	{
		UserBean user = null;
		try
		{
			user = new JdbcTemplate(dataSource).queryForObject("SELECT * FROM user WHERE userid=?", 
				ParameterizedBeanPropertyRowMapper.newInstance(UserBean.class), userid);
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
		return user;
	}
	
	public UserBean getUserInfoByAccessToken(String accessToken){
		UserBean user = null;
		try
		{
			user = new JdbcTemplate(dataSource).queryForObject("SELECT * FROM user WHERE access_token=?", 
				ParameterizedBeanPropertyRowMapper.newInstance(UserBean.class), accessToken);
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
		return user;
	}
}
