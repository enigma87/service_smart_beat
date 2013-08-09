package com.genie.smartbeat.json;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.genie.smartbeat.beans.FitnessShapeIndexBean;

public class ShapeIndexHistoryResponseJson {
	String userID;
	List<ShapeIndex> shapeIndexes;
	
	public ShapeIndexHistoryResponseJson() {
		this.shapeIndexes = new ArrayList<ShapeIndex>();
	}
	
	private class ShapeIndex {
		private double shapeIndex;
		private Timestamp timeOfRecord;
		
		public ShapeIndex(double index, Timestamp time) {
			setShapeIndex(index);
			setTimeOfRecord(time);
		}

		@SuppressWarnings("unused")
		public Timestamp getTimeOfRecord() {
			return timeOfRecord;
		}

		public void setTimeOfRecord(Timestamp timeOfRecord) {
			this.timeOfRecord = timeOfRecord;
		}

		@SuppressWarnings("unused")
		public double getShapeIndex() {
			return shapeIndex;
		}

		public void setShapeIndex(double shapeIndex) {
			this.shapeIndex = shapeIndex;
		}
	}

	public void setShapeIndexes(List<FitnessShapeIndexBean> fitnessShapeIndexBeans) {
		for(Iterator<FitnessShapeIndexBean> i = fitnessShapeIndexBeans.iterator(); i.hasNext();){
			FitnessShapeIndexBean bean = i.next();

			shapeIndexes.add(new ShapeIndex(bean.getShapeIndex(), bean.getTimeOfRecord()));
		}
	}
	
	public List<ShapeIndex> getShapeIndexes() {
		return this.shapeIndexes;
	}
	
	public void setUserID(String userid) {
		this.userID = userid;
	}
	
	public String getUserID() {
		return this.userID;
	}
}
