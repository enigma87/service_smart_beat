package com.genie.smartbeat.json;

/**
 * @author dhasarathy
 **/

public class SaveHeartrateTestResponseJson {

	private HeartRateZoneResponseJson heartrateZones;
	private ShapeIndexResponseJson shapeIndex;
	
	public void setHeartrateZones(HeartRateZoneResponseJson heartrateZones) {
		this.heartrateZones = heartrateZones;
	}
	
	public HeartRateZoneResponseJson getHeartrateZones() {
		return heartrateZones;
	}
	
	public void setShapeIndex(ShapeIndexResponseJson shapeIndex) {
		this.shapeIndex = shapeIndex;
	}
	
	public ShapeIndexResponseJson getShapeIndex() {
		return shapeIndex;
	}
}

