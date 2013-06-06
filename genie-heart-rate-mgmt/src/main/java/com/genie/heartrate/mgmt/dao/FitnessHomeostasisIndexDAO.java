package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.heartrate.mgmt.beans.FitnessHomeostasisIndexBean;

/**
 * @author dhasarathy
 **/

public class FitnessHomeostasisIndexDAO {
	
	private static final String TABLE_FITNESS_HOMEOSTASIS_INDEX = "fitness_homeostasis_index_model";
	private static final String[] COLUMNS_FITNESS_HOMEOSTASIS_INDEX = {"userid", "homeostasis_index", "total_load_of_exercise", "time_at_full_recovery", "last_training_session_id"};
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
	
	public int createHomeoStasisIndexModel(FitnessHomeostasisIndexBean fitnessHomeostasisIndexBean){
		
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName(TABLE_FITNESS_HOMEOSTASIS_INDEX)
		.usingGeneratedKeyColumns(COLUMNS_FITNESS_HOMEOSTASIS_INDEX)
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

}

