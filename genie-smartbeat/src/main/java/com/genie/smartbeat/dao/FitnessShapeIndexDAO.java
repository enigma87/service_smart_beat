package com.genie.smartbeat.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.smartbeat.beans.FitnessShapeIndexBean;

/**
 * @author dhasarathy
 **/

public class FitnessShapeIndexDAO {
	private static final String TABLE_FITNESS_SHAPE_INDEX = "fitness_shape_index_model";
	private static final String[] COLUMNS_FITNESS_SHAPE_INDEX = {"userid", "shape_index", "time_of_record", "session_of_record"};
	private static final int COLUMN_USERID = 0;
	private static final int COLUMN_TIME_OF_RECORD = 2;
	private static final int COLUMN_SESSION_OF_RECORD = 3;
	
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
		// set nanos to 0, MySQL doesn't support it
		fitnessShapeIndexBean.getTimeOfRecord().setNanos(0);

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName(TABLE_FITNESS_SHAPE_INDEX)
				.usingColumns(COLUMNS_FITNESS_SHAPE_INDEX)
				.execute(new BeanPropertySqlParameterSource(fitnessShapeIndexBean));
	}

	private static final String QUERY_SELECT_RECENT_TIME_OF_RECORD = "(" 
	+ "select max(" + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_TIME_OF_RECORD] + ")" 
	+ " FROM " + TABLE_FITNESS_SHAPE_INDEX ;
	//+ ")";
	
	private static final String QUERY_RECENT_SHAPE_INDEX_MODEL = "SELECT * FROM " + TABLE_FITNESS_SHAPE_INDEX 
	+ " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_TIME_OF_RECORD] + " = " + QUERY_SELECT_RECENT_TIME_OF_RECORD
	+ " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_USERID] + " =? "+ ")";
	public FitnessShapeIndexBean getRecentShapeIndexModel(String userid){
		FitnessShapeIndexBean fitnessShapeIndexBean = null;
		try{
			fitnessShapeIndexBean = new JdbcTemplate(dataSource).queryForObject(QUERY_RECENT_SHAPE_INDEX_MODEL, ParameterizedBeanPropertyRowMapper.newInstance(FitnessShapeIndexBean.class),userid);
		}catch(DataAccessException e){
			// TODO Auto-generated catch block
		}
		return fitnessShapeIndexBean;
	}
	
	private static final String QUERY_RECENT_SHAPE_INDEX_TRAINING_SESSION = "SELECT * FROM " + TABLE_FITNESS_SHAPE_INDEX 
	+ " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_SESSION_OF_RECORD] + " =?";
	public FitnessShapeIndexBean getShapeIndexModelByTrainingSessionId(String trainingSessionId){
		FitnessShapeIndexBean fitnessShapeIndexBean = null;
		try{
			fitnessShapeIndexBean = new JdbcTemplate(dataSource).queryForObject(QUERY_RECENT_SHAPE_INDEX_TRAINING_SESSION, ParameterizedBeanPropertyRowMapper.newInstance(FitnessShapeIndexBean.class),trainingSessionId);
		}catch(DataAccessException e){
			// TODO Auto-generated catch block
		}
		return fitnessShapeIndexBean;
	}
	
	private static final String QUERY_SELECT_TIME_RANGE = 	"SELECT * FROM " + TABLE_FITNESS_SHAPE_INDEX
															+ " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_USERID] + "=?"
															+ " AND "   + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_TIME_OF_RECORD] + ">=?"
															+ " AND "   + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_TIME_OF_RECORD] + "<=?";
	public List<FitnessShapeIndexBean> getShapeIndexHistoryDuringInterval(String userid, Timestamp startInterval, Timestamp endInterval){
		List<FitnessShapeIndexBean> shapeIndexBeans = new ArrayList<FitnessShapeIndexBean>();		
		try{
			shapeIndexBeans =  new JdbcTemplate(this.getDataSource()).query(QUERY_SELECT_TIME_RANGE,
					ParameterizedBeanPropertyRowMapper.newInstance(FitnessShapeIndexBean.class),userid,startInterval,endInterval);			
		}catch(DataAccessException e){
			
		}
		return shapeIndexBeans;
	}
	
	private static final String UPDATE_SHAPE_INDEX_MODEL = "UPDATE " + TABLE_FITNESS_SHAPE_INDEX +" set "
	+ COLUMNS_FITNESS_SHAPE_INDEX[0] + "=:userid,"
	+ COLUMNS_FITNESS_SHAPE_INDEX[1] + "=:shapeIndex, " 
	+ COLUMNS_FITNESS_SHAPE_INDEX[2] + "=:timeOfRecord"	
	+ " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_SESSION_OF_RECORD] + "=:sessionOfRecord;";	
	
	public int updateShapeIndexModel(FitnessShapeIndexBean fitnessShapeIndexBean){
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE_SHAPE_INDEX_MODEL, new BeanPropertySqlParameterSource(fitnessShapeIndexBean));
	}

	private static final String DELETE_SHAPE_INDEX_MODEL_BY_USER_ID = "DELETE FROM " + TABLE_FITNESS_SHAPE_INDEX + " WHERE " + COLUMNS_FITNESS_SHAPE_INDEX[COLUMN_USERID] + " =?";
	public void deleteShapeIndexModel(String userid){
		FitnessShapeIndexBean fitnessShapeIndexBean = getRecentShapeIndexModel(userid);		
		if (null != fitnessShapeIndexBean ){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(DELETE_SHAPE_INDEX_MODEL_BY_USER_ID ,userid );
		}
	}
}

