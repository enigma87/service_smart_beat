package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.heartrate.mgmt.beans.FitnessTrainingSessionBean;

/**
 * @author dhasarathy
 **/

public class FitnessTrainingSessionDAO {

	private static final String TABLE_FITNESS_TRAINING_SESSION = "fitness_training_session";
	private static final String[] COLUMNS_FITNESS_TRAINING_SESSION = {"userid", "training_session_id", "start_time", "end_time", "total_load_of_exercise", "hrz_1_time", "hrz_2_time", "hrz_3_time", "hrz_4_time", "hrz_5_time", "hrz_6_time", "hrz_1_distance","hrz_2_distance","hrz_3_distance","hrz_4_distance","hrz_5_distance","hrz_6_distance"};
	private static final int COLUMN_TRAINING_SESSION_ID = 1;

	private BasicDataSource dataSource;
	
	public BasicDataSource getDataSource()
	{
		return this.dataSource;
	}
	
	public void setDataSource(BasicDataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	public Integer createFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean){
		
		Integer trainingSessionId = null;
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		if (1 == simpleJdbcInsert.withTableName(TABLE_FITNESS_TRAINING_SESSION)
				.usingColumns(COLUMNS_FITNESS_TRAINING_SESSION)
				.execute(new BeanPropertySqlParameterSource(fitnessTrainingSessionBean))){
			
		}
		return trainingSessionId;
	}

	private static final String QUERY_ALL_USING_TRAINING_SESSION_ID = "SELECT * FROM " + TABLE_FITNESS_TRAINING_SESSION + " WHERE " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_TRAINING_SESSION_ID] + " =?";
	public FitnessTrainingSessionBean getFitnessTrainingSessionById(Integer trainingSessionId){
		FitnessTrainingSessionBean fitnessTrainingSessionBean = null;
		try {
			fitnessTrainingSessionBean = new JdbcTemplate(dataSource).queryForObject(QUERY_ALL_USING_TRAINING_SESSION_ID, 
					ParameterizedBeanPropertyRowMapper.newInstance(FitnessTrainingSessionBean.class),trainingSessionId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block			
		}
		return fitnessTrainingSessionBean;
	}
	
	private static final String DELETE_SESSION_USING_TRAINING_SESSION_ID = "DELETE FROM " + TABLE_FITNESS_TRAINING_SESSION + " WHERE " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_TRAINING_SESSION_ID] + " =?";
	public void deleteFitnessTrainingSessionById(Integer trainingSessionId){
		FitnessTrainingSessionBean fitnessTrainingSessionBean = getFitnessTrainingSessionById(trainingSessionId);
		if(null != fitnessTrainingSessionBean){
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(DELETE_SESSION_USING_TRAINING_SESSION_ID, trainingSessionId);			
		}
	}
}

