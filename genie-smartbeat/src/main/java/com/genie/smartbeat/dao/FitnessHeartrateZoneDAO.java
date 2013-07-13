package com.genie.smartbeat.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.genie.smartbeat.beans.FitnessHeartrateZoneBean;

/**
 * @author dhasarathy
 **/

public class FitnessHeartrateZoneDAO {

	private static final String 	TABLE_FITNESS_HEARTRATE_ZONE 	= "fitness_heartrate_zone_model";
	private static final String[] 	COLUMNS_FITNESS_HEARTRATE_ZONE 	= { "userid", 
																		"heartrate_zone_1_start", 
																		"heartrate_zone_1_end", 
																		"heartrate_zone_2_start", 
																		"heartrate_zone_2_end", 
																		"heartrate_zone_3_start", 
																		"heartrate_zone_3_end", 
																		"heartrate_zone_4_start", 
																		"heartrate_zone_4_end", 
																		"heartrate_zone_5_start", 
																		"heartrate_zone_5_end", 
																		"heartrate_zone_6_start", 
																		"heartrate_zone_6_end" 
																	 };
	private static final int COLUMN_USERID = 0;
	
	private BasicDataSource dataSource;
	
	public BasicDataSource getDataSource() {
		return dataSource;
	}
	
	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public int createHeartrateZoneModel(FitnessHeartrateZoneBean fitnessHeartrateZoneBean){
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		return simpleJdbcInsert.withTableName(TABLE_FITNESS_HEARTRATE_ZONE)
		.usingColumns(COLUMNS_FITNESS_HEARTRATE_ZONE)
		.execute(new BeanPropertySqlParameterSource(fitnessHeartrateZoneBean));
	}
	
	private static final String QUERY_ALL_USING_USERID = "SELECT * FROM " + TABLE_FITNESS_HEARTRATE_ZONE + " WHERE " + COLUMNS_FITNESS_HEARTRATE_ZONE[COLUMN_USERID] + " =?";
	public FitnessHeartrateZoneBean getHeartrateZoneModelByUserid(String userid){
		FitnessHeartrateZoneBean fitnessHeartrateZoneBean = null;
		try{
			fitnessHeartrateZoneBean = new JdbcTemplate(dataSource).queryForObject(QUERY_ALL_USING_USERID, ParameterizedBeanPropertyRowMapper.newInstance(FitnessHeartrateZoneBean.class),userid);
		}catch(DataAccessException e){
			
		}
		
		return fitnessHeartrateZoneBean;
	}
	
	private static final String DELETE_HEARTRATE_ZONE_MODEL_BY_USER_ID = 	"DELETE FROM " + TABLE_FITNESS_HEARTRATE_ZONE + 
																			" WHERE " + COLUMNS_FITNESS_HEARTRATE_ZONE[COLUMN_USERID] + " =?";
	public void deleteHeartrateZoneModelByUserid(String userid){
		FitnessHeartrateZoneBean fitnessHeartrateZoneBean = getHeartrateZoneModelByUserid(userid);
		if(null != fitnessHeartrateZoneBean){
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			jdbcTemplate.update(DELETE_HEARTRATE_ZONE_MODEL_BY_USER_ID,userid);
		}
	}
	
	private static final String UPDATE_HEARTRATE_ZONE_MODEL = "UPDATE " + TABLE_FITNESS_HEARTRATE_ZONE +" SET " 			
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[1] + "=:heartrateZone1Start, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[2] + "=:heartrateZone1End, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[3] + "=:heartrateZone2Start, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[4] + "=:heartrateZone2End, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[5] + "=:heartrateZone3Start, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[6] + "=:heartrateZone3End, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[7] + "=:heartrateZone4Start, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[8] + "=:heartrateZone4End, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[9] + "=:heartrateZone5Start, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[10] + "=:heartrateZone5End, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[11] + "=:heartrateZone6Start, "
																+ COLUMNS_FITNESS_HEARTRATE_ZONE[12] + "=:heartrateZone6End, "	
																+ "WHERE " + COLUMNS_FITNESS_HEARTRATE_ZONE[COLUMN_USERID] + "=:userid;";
	public int updateHeartrateZoneModel(FitnessHeartrateZoneBean fitnessHeartrateZoneBean){
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return jdbcTemplate.update(UPDATE_HEARTRATE_ZONE_MODEL, new BeanPropertySqlParameterSource(fitnessHeartrateZoneBean));
	}
}

