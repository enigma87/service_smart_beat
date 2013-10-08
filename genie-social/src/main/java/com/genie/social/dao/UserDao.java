/**
 * 
 */
package com.genie.social.dao;

import java.util.ArrayList;
import java.util.List;

import java.util.Iterator;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.social.beans.UserBean;
import com.genie.social.beans.UserIdBean;

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
														"gender",
														"email", 
														"image_url", 
														"created_ts", 
														"last_updated_ts", 
														"last_login_ts", 
														"active", 
														"privilege_level"};
	private static final int COLUMN_USERID 			= 0;
	private static final int COLUMN_ACCESS_TOKEN 	= 1;
	private static final int COLUMN_EMAIL 			= 8;	
	
	public BasicDataSource getDataSource()
	{
		return this.dataSource;
	}
	
	public void setDataSource(BasicDataSource dataSource)
	{
		this.dataSource = dataSource;
	}		
	
	public int createUser(UserBean user) {
		int createStatus = 0;
		if (user.isValidForTableInsert()) {
			SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
			try{
				createStatus = simpleJdbcInsert.withTableName(TABLE_USER)
						.usingColumns(COLUMNS_USER)
						.execute(new BeanPropertySqlParameterSource(user));
			}catch(DataAccessException e){
				createStatus = 1;
			}
		}
		return createStatus;
	}
	
	private static final String UPDATE = "UPDATE " + TABLE_USER + " SET "
			+ COLUMNS_USER[1] + "=:accessToken," 
			+ COLUMNS_USER[2] + "=:accessTokenType," 
			+ COLUMNS_USER[3] + "=:firstName," 
			+ COLUMNS_USER[4] + "=:middleName,"
			+ COLUMNS_USER[5] + "=:lastName,"
			+ COLUMNS_USER[6] + "=:dob,"
			+ COLUMNS_USER[7] + "=:gender," 
			+ COLUMNS_USER[8] + "=:email," 
			+ COLUMNS_USER[9] + "=:imageUrl," 
			+ COLUMNS_USER[10] + "=:createdTs," 
			+ COLUMNS_USER[11] + "=:lastUpdatedTs," 
			+ COLUMNS_USER[12] + "=:lastLoginTs," 
			+ COLUMNS_USER[13] + "=:active," 
			+ COLUMNS_USER[14] + "=:privilegeLevel " +
			"WHERE " + COLUMNS_USER[COLUMN_USERID] + "=:userid;";	
	
	public int updateUser(UserBean user) {
		if (user.isValidForTableInsert()) {
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			return jdbcTemplate.update(UPDATE, new BeanPropertySqlParameterSource(user));
		}
		return 0;
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
	
	private static final String SELECT_ALL_USERS  =	"SELECT * FROM " + TABLE_USER;
	public List<UserIdBean> getUserIds(){
		List<UserIdBean> userIds = new ArrayList<UserIdBean>();
		List<UserBean> users = null;
		
		try{
			users = new JdbcTemplate(dataSource).query(SELECT_ALL_USERS, 
					ParameterizedBeanPropertyRowMapper.newInstance(UserBean.class));
			for(Iterator<UserBean> iter = users.iterator() ; iter.hasNext();){
				UserBean userBean = iter.next();
				UserIdBean userIdBean = new UserIdBean();
				userIdBean.setUserid(userBean.getUserid());
				userIdBean.setFirstName(userBean.getFirstName());
				userIdBean.setLastName(userBean.getLastName());
				userIds.add(userIdBean);
			}
		}
		catch(EmptyResultDataAccessException ex){
			
		}
		
		return userIds;
		
	}
}