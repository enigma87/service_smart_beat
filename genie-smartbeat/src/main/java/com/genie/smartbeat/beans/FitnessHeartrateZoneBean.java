package com.genie.smartbeat.beans;

/**
 * @author dhasarathy
 **/

public class FitnessHeartrateZoneBean {

	private String userid;
	private Double heartrateZone1Start;
	private Double heartrateZone1End;
	private Double heartrateZone2Start;
	private Double heartrateZone2End;
	private Double heartrateZone3Start;
	private Double heartrateZone3End;
	private Double heartrateZone4Start;
	private Double heartrateZone4End;
	private Double heartrateZone5Start;
	private Double heartrateZone5End;
	private Double heartrateZone6Start;
	private Double heartrateZone6End;
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public Double getHeartrateZone1Start() {
		return heartrateZone1Start;
	}
	
	public void setHeartrateZone1Start(Double heartrateZone1Start) {
		this.heartrateZone1Start = heartrateZone1Start;
	}
	
	public Double getHeartrateZone1End() {
		return heartrateZone1End;
	}
	
	public void setHeartrateZone1End(Double heartrateZone1End) {
		this.heartrateZone1End = heartrateZone1End;
	}
	
	public Double getHeartrateZone2Start() {
		return heartrateZone2Start;
	}
	
	public void setHeartrateZone2Start(Double heartrateZone2Start) {
		this.heartrateZone2Start = heartrateZone2Start;
	}
	
	public Double getHeartrateZone2End() {
		return heartrateZone2End;
	}
	
	public void setHeartrateZone2End(Double heartrateZone2End) {
		this.heartrateZone2End = heartrateZone2End;
	}
	
	public Double getHeartrateZone3Start() {
		return heartrateZone3Start;
	}
	
	public Double getHeartrateZone3End() {
		return heartrateZone3End;
	}
	
	public void setHeartrateZone3Start(Double heartrateZone3Start) {
		this.heartrateZone3Start = heartrateZone3Start;
	}
	
	public void setHeartrateZone3End(Double heartrateZone3End) {
		this.heartrateZone3End = heartrateZone3End;
	}
	
	public Double getHeartrateZone4Start() {
		return heartrateZone4Start;
	}
	
	public void setHeartrateZone4Start(Double heartrateZone4Start) {
		this.heartrateZone4Start = heartrateZone4Start;
	}
	
	public Double getHeartrateZone4End() {
		return heartrateZone4End;
	}
	
	public void setHeartrateZone4End(Double heartrateZone4End) {
		this.heartrateZone4End = heartrateZone4End;
	}
	
	public Double getHeartrateZone5Start() {
		return heartrateZone5Start;
	}
	
	public void setHeartrateZone5Start(Double heartrateZone5Start) {
		this.heartrateZone5Start = heartrateZone5Start;
	}
	
	public Double getHeartrateZone5End() {
		return heartrateZone5End;
	}
	
	public void setHeartrateZone5End(Double heartrateZone5End) {
		this.heartrateZone5End = heartrateZone5End;
	}
	
	public Double getHeartrateZone6Start() {
		return heartrateZone6Start;
	}
	
	public void setHeartrateZone6Start(Double heartrateZone6Start) {
		this.heartrateZone6Start = heartrateZone6Start;
	}
	
	public Double getHeartrateZone6End() {
		return heartrateZone6End;
	}
	
	public void setHeartrateZone6End(Double heartrateZone6End) {
		this.heartrateZone6End = heartrateZone6End;
	}
	
	public static final int ZONE_START 	= 0;
	public static final int ZONE_END 	= 1;
	public void setHeartrateZones(double[][] heartrateZones){
		this.setHeartrateZone1Start(heartrateZones[1][ZONE_START]);
		this.setHeartrateZone1End(heartrateZones[1][ZONE_END]);
		this.setHeartrateZone2Start(heartrateZones[2][ZONE_START]);
		this.setHeartrateZone2End(heartrateZones[2][ZONE_END]);
		this.setHeartrateZone3Start(heartrateZones[3][ZONE_START]);
		this.setHeartrateZone3End(heartrateZones[3][ZONE_END]);
		this.setHeartrateZone4Start(heartrateZones[4][ZONE_START]);
		this.setHeartrateZone4End(heartrateZones[4][ZONE_END]);
		this.setHeartrateZone5Start(heartrateZones[5][ZONE_START]);
		this.setHeartrateZone5End(heartrateZones[5][ZONE_END]);
		this.setHeartrateZone6Start(heartrateZones[6][ZONE_START]);
		this.setHeartrateZone6End(heartrateZones[6][ZONE_END]);
	}
	
	public double[][] getHeartrateZones(){
		double[][] heartrateZones = new double[7][2];
		
		heartrateZones[1][ZONE_START] 	= this.getHeartrateZone1Start();
		heartrateZones[1][ZONE_END] 	= this.getHeartrateZone1End();
		heartrateZones[2][ZONE_START] 	= this.getHeartrateZone2Start();
		heartrateZones[2][ZONE_END] 	= this.getHeartrateZone2End();
		heartrateZones[3][ZONE_START] 	= this.getHeartrateZone3Start();
		heartrateZones[3][ZONE_END] 	= this.getHeartrateZone3End();
		heartrateZones[4][ZONE_START] 	= this.getHeartrateZone4Start();
		heartrateZones[4][ZONE_END] 	= this.getHeartrateZone4End();
		heartrateZones[5][ZONE_START] 	= this.getHeartrateZone5Start();
		heartrateZones[5][ZONE_END] 	= this.getHeartrateZone5End();
		heartrateZones[6][ZONE_START] 	= this.getHeartrateZone6Start();
		heartrateZones[6][ZONE_END] 	= this.getHeartrateZone6End();
		
		return heartrateZones;
	}

	public boolean isValidForTableInsert(){
		if ((null != this.getUserid() && !this.getUserid().isEmpty())) {
			return true;
		}
		return false;
	}
}