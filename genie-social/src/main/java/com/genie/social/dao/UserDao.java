/**
 * 
 */
package com.genie.social.dao;

import java.util.Calendar;

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
	private static final String TABLE_USER = "user";
	private static final String[] COLUMNS_USER = 	{	"userid", 
														"access_token", 
														"access_token_type", 
														"first_name", 
														"middle_name", 
														"last_name", 
														"dob", 
														"email", 
														"image_url", 
														"created_ts", 
														"last_updated_ts", 
														"last_login_ts", 
														"active", 
														"privilege_level"};
	private static final int COLUMN_USERID 			= 0;
	private static final int COLUMN_ACCESS_TOKEN 	= 1;
	private static final int COLUMN_EMAIL 			= 7;	
	
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
		return simpleJdbcInsert.withTableName(TABLE_USER)
		.usingColumns(COLUMNS_USER)
		.execute(new BeanPropertySqlParameterSource(user));
	}
	
	private static final String UPDATE = "UPDATE " + TABLE_USER + " SET "
			+ COLUMNS_USER[1] + "=:accessToken," 
			+ COLUMNS_USER[2] + "=:accessTokenType," 
			+ COLUMNS_USER[3] + "=:firstName," 
			+ COLUMNS_USER[4] + "=:middleName,"
			+ COLUMNS_USER[5] + "=:lastName,"
			+ COLUMNS_USER[6] + "=:dob,"
			+ COLUMNS_USER[7] + "=:email," 
			+ COLUMNS_USER[8] + "=:imageUrl," 
			+ COLUMNS_USER[9] + "=:createdTs," 
			+ COLUMNS_USER[10] + "=:lastUpdatedTs," 
			+ COLUMNS_USER[11] + "=:lastLoginTs," 
			+ COLUMNS_USER[12] + "=:active," 
			+ COLUMNS_USER[13] + "=:privilegeLevel " +
			"WHERE " + COLUMNS_USER[COLUMN_USERID] + "=:userid;";	
	
	public int updateUser(UserBean user)
	{
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE, new BeanPropertySqlParameterSource(user));
	}
	
	private static final String DELETE_BY_ID = "DELETE FROM " + TABLE_USER + " WHERE " 
												+ COLUMNS_USER[COLUMN_USERID] + " =?";
	public void deleteUser(String userid){		
		try{
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);			
			jdbcTemplate.update(DELETE_BY_ID, userid);
		}
		catch(EmptyResultDataAccessException ex){			
		}
	}
	
	private static final String SELECT_BY_EMAIL = 	"SELECT * FROM " + TABLE_USER
													+ " WHERE " + COLUMNS_USER[COLUMN_EMAIL] + " =?";
	public UserBean getUserInfoByEmail(String email)
	{
		UserBean user = null;
		try
		{
			user = new JdbcTemplate(dataSource).queryForObject(SELECT_BY_EMAIL, 
				ParameterizedBeanPropertyRowMapper.newInstance(UserBean.class), email);
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
		return user;
	}
	
	private static final String SELECT_BY_ID = 	"SELECT * FROM " + TABLE_USER
												+ " WHERE " + COLUMNS_USER[COLUMN_USERID] + " =?";
	
	public UserBean getUserInfo(String userid)
	{
		UserBean user = null;
		try
		{
			user = new JdbcTemplate(dataSource).queryForObject(SELECT_BY_ID, 
				ParameterizedBeanPropertyRowMapper.newInstance(UserBean.class), userid);
		}
		catch(EmptyResultDataAccessException ex)
		{
			
		}
		return user;
	}
	
	private static final String SELECT_BY_ACCESS_TOKEN  = 	"SELECT * FROM " + TABLE_USER
															+ " WHERE " + COLUMNS_USER[COLUMN_ACCESS_TOKEN] + " =?";
	public UserBean getUserInfoByAccessToken(String accessToken){
		UserBean user = null;
		try{
			user = new JdbcTemplate(dataSource).queryForObject(SELECT_BY_ACCESS_TOKEN, 
				ParameterizedBeanPropertyRowMapper.newInstance(UserBean.class), accessToken);
		}
		catch(EmptyResultDataAccessException ex){
			
		}
		return user;
	}
	
	public boolean isExistingUser(String email){
		boolean isExistingUser = false;
		if(null != getUserInfoByEmail(email)){
			isExistingUser = true;
		}
		return isExistingUser;
	}
	
	public int getAgeOfUser(String userid){
		int age = -1;
		UserBean user = getUserInfo(userid);
	    if(null != user){
	    	Calendar today = Calendar.getInstance();
		    Calendar birthDate = Calendar.getInstance();
		    birthDate.setTime(user.getDob());
		    if (!birthDate.after(today)) {		    	
		    	age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
		    	// If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year   
		        if ( (birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
		                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH ))){
		            age--;
	
		         // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
		        }else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH )) &&
		                  (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH ))){
		            age--;
		        }
		    }
	    }
	    return age;
	}
}
