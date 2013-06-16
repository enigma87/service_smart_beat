package com.genie.heartrate.mgmt.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.heartrate.mgmt.beans.FitnessShapeIndexBean;

/**
 * @author dhasarathy
 **/

public class FitnessShapeIndexDAO {
	private static final String TABLE_FITNESS_SHAPE_INDEX = "fitness_shape_index_model";
	private static final String[] COLUMNS_FITNESS_SHAPE_INDEX = {"userid", "shape_index", "time_of_record", "session_of_record"};
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
	
	public int createFitnessShapeIndexModel(FitnessShapeIndexBean fitnessShapeIndexBean){
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName(TABLE_FITNESS_SHAPE_INDEX)
				.usingColumns(COLUMNS_FITNESS_SHAPE_INDEX)
				.execute(new BeanPropertySqlParameterSource(fitnessShapeIndexBean));
	}

	private static final String QUERY_ALL_USING_USERID = "SELECT * FROM " + TABLE_FITNESS_SHAPE_INDEX + " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_USERID] + " =?";
	public FitnessShapeIndexBean getShapeIndexModelByUserId(String userid){
		FitnessShapeIndexBean fitnessShapeIndexBean = null;
		try{
			fitnessShapeIndexBean = new JdbcTemplate(dataSource).queryForObject(QUERY_ALL_USING_USERID, ParameterizedBeanPropertyRowMapper.newInstance(FitnessShapeIndexBean.class));
		}catch(DataAccessException e){
			// TODO Auto-generated catch block
		}
		return fitnessShapeIndexBean;
	}
	
	private static final String DELETE_SHAPE_INDEX_MODEL_BY_USER_ID = "DELETE FROM " + TABLE_FITNESS_SHAPE_INDEX + " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_USERID] + " =?";
	public void deleteShapeIndexModelByUserid(String userid){
		FitnessShapeIndexBean fitnessShapeIndexBean = getShapeIndexModelByUserId(userid);
		if(null != fitnessShapeIndexBean){
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(DELETE_SHAPE_INDEX_MODEL_BY_USER_ID);
		}
	}
	
	private static final String UPDATE_SHAPE_INDEX_MODEL = "UPDATE" + TABLE_FITNESS_SHAPE_INDEX +" set " 
	+ COLUMNS_FITNESS_SHAPE_INDEX[1] + "=:shapeIndex, " 
	+ COLUMNS_FITNESS_SHAPE_INDEX[2] + "=:timeOfRecord,"	
	+ COLUMNS_FITNESS_SHAPE_INDEX[4] + "=:sessionOfRecord"
	+ "WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_USERID] + "=:userid;";	
	public int updateShapeIndexModel(FitnessShapeIndexBean fitnessShapeIndexBean){
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE_SHAPE_INDEX_MODEL, new BeanPropertySqlParameterSource(fitnessShapeIndexBean));
	}
}

