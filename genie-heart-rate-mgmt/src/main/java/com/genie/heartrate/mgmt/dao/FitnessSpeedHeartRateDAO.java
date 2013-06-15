package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.heartrate.mgmt.beans.FitnessSpeedHeartRateBean;

/**
 * @author dhasarathy
 **/

public class FitnessSpeedHeartRateDAO {

	private static final String TABLE_FITNESS_SPEED_HEARTRATE = "fitness_speed_heartrate_model";
	private static final String[] COLUMNS_FITNESS_SPEED_HEARTRATE = {"userid", "current_vdot", "previous_vdot"};
	private static final int COLUMN_USERID = 0;
	
	private BasicDataSource dataSource;
	
	public BasicDataSource getDataSource()
	{
		return this.dataSource;
	}
	
	public void setDataSource(BasicDataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	public int createSpeedHeartRateModel(FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean){
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName(TABLE_FITNESS_SPEED_HEARTRATE)
		.usingColumns(COLUMNS_FITNESS_SPEED_HEARTRATE)
		.execute(new BeanPropertySqlParameterSource(fitnessSpeedHeartRateBean));
	}
	
	private static final String QUERY_ALL_USING_USERID = "SELECT * FROM " + TABLE_FITNESS_SPEED_HEARTRATE + " WHERE " + COLUMNS_FITNESS_SPEED_HEARTRATE[COLUMN_USERID] + " =?";
	public FitnessSpeedHeartRateBean getSpeedHeartRateModelByUserid(String userid){
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = null;
		try{
			fitnessSpeedHeartRateBean = new JdbcTemplate(dataSource).queryForObject(QUERY_ALL_USING_USERID, ParameterizedBeanPropertyRowMapper.newInstance(FitnessSpeedHeartRateBean.class),userid);
		}catch(DataAccessException ex){
			// TODO Auto-generated catch block
		}
		return fitnessSpeedHeartRateBean;
	}
	
	private static final String DELETE_SPEED_HEARTRATE_MODEL_BY_USER_ID = "DELETE FROM " + TABLE_FITNESS_SPEED_HEARTRATE + " WHERE " + COLUMNS_FITNESS_SPEED_HEARTRATE[COLUMN_USERID] + " =?";
	public void deleteSpeedHeartRateModelByUserid(String userid){
		FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean = getSpeedHeartRateModelByUserid(userid);
		if(null != fitnessSpeedHeartRateBean){
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(DELETE_SPEED_HEARTRATE_MODEL_BY_USER_ID,userid);
		}
	}
	
	private static final String UPDATE_SPEED_HEARTRATE_MODEL = "UPDATE " + TABLE_FITNESS_SPEED_HEARTRATE +" SET " 
			+ COLUMNS_FITNESS_SPEED_HEARTRATE[1] + "=:currentVdot, "
			+ COLUMNS_FITNESS_SPEED_HEARTRATE[2] + "=:previousVdot, "				
			+ "WHERE " + COLUMNS_FITNESS_SPEED_HEARTRATE[COLUMN_USERID] + "=:userid;";
	public int updateSpeedHeartrateModel(FitnessSpeedHeartRateBean fitnessSpeedHeartRateBean){
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE_SPEED_HEARTRATE_MODEL, new BeanPropertySqlParameterSource(fitnessSpeedHeartRateBean));
	}
}

