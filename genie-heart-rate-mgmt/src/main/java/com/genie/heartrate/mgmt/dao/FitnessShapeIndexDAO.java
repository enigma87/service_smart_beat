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
	private static final int COLUMN_TIME_OF_RECORD = 2;
	
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

	private static final String QUERY_SELECT_RECENT_TIME_OF_RECORD = "(" 
	+ "select max(" + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_TIME_OF_RECORD] + ")" 
	+ " FROM " + TABLE_FITNESS_SHAPE_INDEX 
	+ ")";
	
	private static final String QUERY_RECENT_SHAPE_INDEX_MODEL = "SELECT * FROM " + TABLE_FITNESS_SHAPE_INDEX 
	+ " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_TIME_OF_RECORD] + " = " + QUERY_SELECT_RECENT_TIME_OF_RECORD
	+ " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_USERID] + " =?";
	public FitnessShapeIndexBean getRecentShapeIndexModel(String userid){
		FitnessShapeIndexBean fitnessShapeIndexBean = null;
		try{
			fitnessShapeIndexBean = new JdbcTemplate(dataSource).queryForObject(QUERY_RECENT_SHAPE_INDEX_MODEL, ParameterizedBeanPropertyRowMapper.newInstance(FitnessShapeIndexBean.class));
		}catch(DataAccessException e){
			// TODO Auto-generated catch block
		}
		return fitnessShapeIndexBean;
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

