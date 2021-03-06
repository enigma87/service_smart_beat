package com.genie.smartbeat.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.smartbeat.beans.FitnessTrainingSessionBean;
import com.genie.smartbeat.beans.FitnessTrainingSessionIdBean;

/**
 * @author dhasarathy
 **/

public class FitnessTrainingSessionDAO {
	
	private static final String TABLE_FITNESS_TRAINING_SESSION = "fitness_training_session";
	private static final String[] COLUMNS_FITNESS_TRAINING_SESSION = {	"userid", 
																		"training_session_id", 
																		"start_time", 
																		"end_time", 
																		"hrz_1_time", 
																		"hrz_2_time", 
																		"hrz_3_time", 
																		"hrz_4_time", 
																		"hrz_5_time", 
																		"hrz_6_time", 
																		"hrz_1_distance",
																		"hrz_2_distance",
																		"hrz_3_distance",
																		"hrz_4_distance",
																		"hrz_5_distance",
																		"hrz_6_distance",
																		"surface_index", 																		 
																		"vdot", 
																		"health_perception_index", 
																		"muscle_state_perception_index", 
																		"session_stress_perception_index",
																		"average_altitude",
																		"extra_load"};
	private static final int COLUMN_TRAINING_SESSION_ID = 1;
	private static final int COLUMN_USERID = 0;
	private static final int COLUMN_START_TIME = 2;
	private static final int COLUMN_END_TIME = 3;

	private BasicDataSource dataSource;
	
	public BasicDataSource getDataSource()
	{
		return this.dataSource;
	}
	
	@Autowired
	public void setDataSource(BasicDataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	public int createFitnessTrainingSession(FitnessTrainingSessionBean fitnessTrainingSessionBean){
		int createStatus = 0;
		if (fitnessTrainingSessionBean.isValidForTableInsert()) {
			try{
			SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);			
			createStatus = simpleJdbcInsert.withTableName(TABLE_FITNESS_TRAINING_SESSION)
				.usingColumns(COLUMNS_FITNESS_TRAINING_SESSION)
				.execute(new BeanPropertySqlParameterSource(fitnessTrainingSessionBean));
			}catch(DuplicateKeyException e){
				createStatus = DAOOperationStatus.DUPLICATE_KEY_EXCEPTION;
			}			
		}
		return createStatus;
	}
	
	private static final String QUERY_ALL_USING_TRAINING_SESSION_ID = "SELECT * FROM " + TABLE_FITNESS_TRAINING_SESSION + " WHERE " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_TRAINING_SESSION_ID] + " =?";
	public FitnessTrainingSessionBean getFitnessTrainingSessionById(String trainingSessionId){
		FitnessTrainingSessionBean fitnessTrainingSessionBean = null;
		try {
			fitnessTrainingSessionBean = new JdbcTemplate(dataSource).queryForObject(QUERY_ALL_USING_TRAINING_SESSION_ID, 
					ParameterizedBeanPropertyRowMapper.newInstance(FitnessTrainingSessionBean.class),trainingSessionId);
		} catch (DataAccessException e) {
			fitnessTrainingSessionBean = null;			
		}
		return fitnessTrainingSessionBean;
	}
	
	private static final String QUERY_SELECT_END_TIME = "(" + "select max(" + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_END_TIME] + ")" + " FROM " + TABLE_FITNESS_TRAINING_SESSION +" WHERE " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_USERID] + " =?"+")" ;
	private static final String QUERY_RECENT_TRAINING_SESSION_ID = "SELECT * FROM " + TABLE_FITNESS_TRAINING_SESSION + " WHERE "+COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_END_TIME]+ " = "+QUERY_SELECT_END_TIME;
	public FitnessTrainingSessionBean getRecentFitnessTrainingSessionForUser(String userid){
		FitnessTrainingSessionBean fitnessTrainingSessionBean = null;
		try {
			fitnessTrainingSessionBean = new JdbcTemplate(dataSource).queryForObject(QUERY_RECENT_TRAINING_SESSION_ID, 
					ParameterizedBeanPropertyRowMapper.newInstance(FitnessTrainingSessionBean.class),userid);
		} catch (DataAccessException e) {
			fitnessTrainingSessionBean = null;			
		}
		return fitnessTrainingSessionBean;
	}
	
	private static final String QUERY_SELECT_TIME_RANGE = "select * "  
			+ " from " + TABLE_FITNESS_TRAINING_SESSION 
			+ " where " 
			+ COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_USERID] + "= ?"
			+ " and " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_START_TIME] + " >= timestamp(?) "
			+ " and " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_END_TIME] + "< timestamp(?)"
			+ " ORDER BY " 	+ COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_START_TIME] + " DESC";

	public List<FitnessTrainingSessionBean>  getFitnessTrainingSessionsByTimeRange (String userID, Timestamp startTimestamp, Timestamp endTimestamp) {
		
		List<FitnessTrainingSessionBean>  trainingSessions = new JdbcTemplate(this.getDataSource()).query(QUERY_SELECT_TIME_RANGE,
				ParameterizedBeanPropertyRowMapper.newInstance(FitnessTrainingSessionBean.class),
				userID, startTimestamp.toString(), endTimestamp.toString());
		
		 return trainingSessions;
	}

	public List<FitnessTrainingSessionIdBean>  getFitnessTrainingSessionIdsByTimeRange (String userID, Timestamp startTimestamp, Timestamp endTimestamp) {
		List <FitnessTrainingSessionIdBean> trainingSessionIds = new ArrayList<FitnessTrainingSessionIdBean>();
		
		List<FitnessTrainingSessionBean>  trainingSessions = new JdbcTemplate(this.getDataSource()).query(QUERY_SELECT_TIME_RANGE,
				ParameterizedBeanPropertyRowMapper.newInstance(FitnessTrainingSessionBean.class),
				userID, startTimestamp.toString(), endTimestamp.toString());
		
		 for (Iterator<FitnessTrainingSessionBean> iter = trainingSessions.iterator(); iter.hasNext();) {
			FitnessTrainingSessionBean trainingSessionBean=  iter.next();
			FitnessTrainingSessionIdBean fitnessTrainingSessionIdBean = new FitnessTrainingSessionIdBean();
			fitnessTrainingSessionIdBean.setTrainingSessionId(trainingSessionBean.getTrainingSessionId());
			fitnessTrainingSessionIdBean.setStartTime(trainingSessionBean.getStartTime());
			fitnessTrainingSessionIdBean.setEndTime(trainingSessionBean.getEndTime());
			trainingSessionIds.add(fitnessTrainingSessionIdBean);
		}
		
		return trainingSessionIds;
	}
	
	private static final String DELETE_SESSION_USING_TRAINING_SESSION_ID = "DELETE FROM " + TABLE_FITNESS_TRAINING_SESSION + " WHERE " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_TRAINING_SESSION_ID] + " =?";
	public void deleteFitnessTrainingSessionById(String fitnessTrainingSessionId){
		FitnessTrainingSessionBean fitnessTrainingSessionBean = getFitnessTrainingSessionById(fitnessTrainingSessionId);
		if(null != fitnessTrainingSessionBean){
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(DELETE_SESSION_USING_TRAINING_SESSION_ID, fitnessTrainingSessionId);			
		}
	}
	
	private static final String QUERY_ALL_BY_USERID = 	"SELECT * "  
														+ " FROM "  + TABLE_FITNESS_TRAINING_SESSION 
														+ " WHERE "	+ COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_USERID] + "= ?";	
	public double[] getVdotHistory(String userid, int n){
		
		double[] vdotHistory = null;
		String QUERY_N_RECENT = QUERY_ALL_BY_USERID + 
								" ORDER BY " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_END_TIME] + " DESC" +
								" LIMIT " + n;
		List<FitnessTrainingSessionBean> nRecentTrainingSessions = new ArrayList<FitnessTrainingSessionBean>();
		try{
			nRecentTrainingSessions = new JdbcTemplate(dataSource).query(QUERY_N_RECENT, ParameterizedBeanPropertyRowMapper.newInstance(FitnessTrainingSessionBean.class), userid);
			Collections.reverse(nRecentTrainingSessions);
			if(n == nRecentTrainingSessions.size()){
				vdotHistory = new double[n];
				int count = 0;
				for(Iterator<FitnessTrainingSessionBean> i = nRecentTrainingSessions.iterator(); i.hasNext();){
					FitnessTrainingSessionBean trainingSessionBean = i.next();
					vdotHistory[count++] = trainingSessionBean.getVdot();
				}
	
			}
		}catch(DataAccessException e){
			vdotHistory = null;
		}
		return vdotHistory;
	}
	
	private static final String COUNT_QUERY_ALL_TESTS = "SELECT COUNT(*) FROM ( " +
															QUERY_ALL_BY_USERID +
														" ) AS TEMP";
	public Integer getTrainingSessionCountByUser(String userid){
		Integer numberOfTrainingSessions = 0;
		try{
			numberOfTrainingSessions = new JdbcTemplate(dataSource).queryForInt(COUNT_QUERY_ALL_TESTS,userid);
		}catch(DataAccessException e){
			numberOfTrainingSessions = 0;
		}
		return numberOfTrainingSessions;
	}
	
	private static final String DELETE_TRAINING_SESSIONS_FOR_USER = "DELETE FROM " + TABLE_FITNESS_TRAINING_SESSION + " WHERE " + COLUMNS_FITNESS_TRAINING_SESSION[COLUMN_USERID] + "=?";
	
	public void deleteAllTrainingSessionsForUser(String userid) {
		
		try{
			new JdbcTemplate(dataSource).update(DELETE_TRAINING_SESSIONS_FOR_USER, userid);
		}catch(DataAccessException e){
			
		}
	}
}

