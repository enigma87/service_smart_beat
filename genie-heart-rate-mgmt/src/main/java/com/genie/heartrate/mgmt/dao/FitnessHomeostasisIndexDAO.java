package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.heartrate.mgmt.beans.FitnessHomeostasisIndexBean;

/**
 * @author dhasarathy
 **/

public class FitnessHomeostasisIndexDAO {
	
	private static final String TABLE_FITNESS_HOMEOSTASIS_INDEX = "fitness_homeostasis_index_model";
	private static final String[] COLUMNS_FITNESS_HOMEOSTASIS_INDEX = {"userid", "trainee_classification", "local_regression_minimum_of_homeostasis_index","current_total_load_of_exercise", "current_end_time", "previous_total_load_of_exercise", "previous_end_time"};
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
	
	public int createHomeostasisIndexModel(FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean){
		
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName(TABLE_FITNESS_HOMEOSTASIS_INDEX)
		.usingColumns(COLUMNS_FITNESS_HOMEOSTASIS_INDEX)
		.execute(new BeanPropertySqlParameterSource(fitnessHomeostasisIndexBean));
	}
	
	private static final String QUERY_ALL_USING_USERID = "SELECT * FROM " + TABLE_FITNESS_HOMEOSTASIS_INDEX + " WHERE " + COLUMNS_FITNESS_HOMEOSTASIS_INDEX[COLUMN_USERID] + " =?";
	public FitnessHomeostasisIndexBean getHomeostasisIndexModelByUserid(String userid){
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = null;
		try{
			fitnessHomeostasisIndexBean = new JdbcTemplate(dataSource).queryForObject(QUERY_ALL_USING_USERID, ParameterizedBeanPropertyRowMapper.newInstance(FitnessHomeostasisIndexBean.class),userid);
		}catch(DataAccessException ex){
			// TODO Auto-generated catch block			
		}		
		return fitnessHomeostasisIndexBean;
		
	}
	
	private static final String DELETE_HOMEOSTASIS_INDEX_MODEL_BY_USER_ID = "DELETE FROM " + TABLE_FITNESS_HOMEOSTASIS_INDEX + " WHERE " + COLUMNS_FITNESS_HOMEOSTASIS_INDEX[COLUMN_USERID] + " =?";
	public void deleteHomeostasisIndexModelByUserid(String userid){
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = getHomeostasisIndexModelByUserid(userid);
		if(null != fitnessHomeostasisIndexBean){
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(DELETE_HOMEOSTASIS_INDEX_MODEL_BY_USER_ID,userid);
		}
		
	}
	
	private static final String UPDATE_HOMEOSTASIS_INDEX_MODEL = "UPDATE " + TABLE_FITNESS_HOMEOSTASIS_INDEX +" SET " 
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[1] + "=:traineeClassification, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[2] + "=:localRegressionMinimumOfHomeostasisIndex, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[3] + "=:currentTotalLoadOfExercise, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[4] + "=:currentEndTime, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[5] + "=:previousTotalLoadOfExercise, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[6] + "=:previousEndTime "				
			+ "WHERE " + COLUMNS_FITNESS_HOMEOSTASIS_INDEX[COLUMN_USERID] + "=:userid;";
	
	public int updateHomeostasisIndexModel(FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean){
		       
		       	NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
				return jdbcTemplate.update(UPDATE_HOMEOSTASIS_INDEX_MODEL, new BeanPropertySqlParameterSource(fitnessHomeostasisIndexBean));
			}

}

