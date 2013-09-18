package com.genie.smartbeat.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.smartbeat.beans.FitnessHomeostasisIndexBean;

/**
 * @author dhasarathy
 **/

public class FitnessHomeostasisIndexDAO {
	
	private static final String TABLE_FITNESS_HOMEOSTASIS_INDEX = "fitness_homeostasis_index_model";
	private static final String[] COLUMNS_FITNESS_HOMEOSTASIS_INDEX = {"userid", "trainee_classification", "local_regression_minimum_of_homeostasis_index","recent_minimum_of_homeostasis_index","recent_total_load_of_exercise", "recent_end_time", "previous_total_load_of_exercise", "previous_end_time"};
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
		int createStatus = 0;
		if (fitnessHomeostasisIndexBean.isValidForTableInsert()) {
			SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);

			try{
			createStatus = simpleJdbcInsert.withTableName(TABLE_FITNESS_HOMEOSTASIS_INDEX)
					.usingColumns(COLUMNS_FITNESS_HOMEOSTASIS_INDEX)
					.execute(new BeanPropertySqlParameterSource(fitnessHomeostasisIndexBean));
			}
			catch(DuplicateKeyException e){
				createStatus = DAOOperationStatus.DUPLICATE_KEY_EXCEPTION;
			}
		}
		return createStatus;
	}
	
	private static final String QUERY_ALL_USING_USERID = "SELECT * FROM " + TABLE_FITNESS_HOMEOSTASIS_INDEX + " WHERE " + COLUMNS_FITNESS_HOMEOSTASIS_INDEX[COLUMN_USERID] + " =?";
	public FitnessHomeostasisIndexBean getHomeostasisIndexModelByUserid(String userid){
		
		FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean = null;
		try{
			fitnessHomeostasisIndexBean = new JdbcTemplate(dataSource).queryForObject(QUERY_ALL_USING_USERID, ParameterizedBeanPropertyRowMapper.newInstance(FitnessHomeostasisIndexBean.class),userid);
		}catch(DataAccessException ex){
			fitnessHomeostasisIndexBean = null;			
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
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[3] + "=:recentMinimumOfHomeostasisIndex, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[4] + "=:recentTotalLoadOfExercise, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[5] + "=:recentEndTime, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[6] + "=:previousTotalLoadOfExercise, "
			+ COLUMNS_FITNESS_HOMEOSTASIS_INDEX[7] + "=:previousEndTime "				
			+ "WHERE " + COLUMNS_FITNESS_HOMEOSTASIS_INDEX[COLUMN_USERID] + "=:userid;";
	
	public int updateHomeostasisIndexModel(FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean){		       
       	
		if ((null!= getHomeostasisIndexModelByUserid(fitnessHomeostasisIndexBean.getUserid())) &&fitnessHomeostasisIndexBean.isValidForTableInsert()) {
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			return jdbcTemplate.update(UPDATE_HOMEOSTASIS_INDEX_MODEL, new BeanPropertySqlParameterSource(fitnessHomeostasisIndexBean));
		}
		return 0;	
	}
	
	public Integer getTraineeClassificationByUserid(String userid){
		Integer traineeClassification = null;
		FitnessHomeostasisIndexBean homeostasisIndexBean = getHomeostasisIndexModelByUserid(userid);
		if(null != homeostasisIndexBean){
			traineeClassification = homeostasisIndexBean.getTraineeClassification();
		}
		return traineeClassification;
	}
}