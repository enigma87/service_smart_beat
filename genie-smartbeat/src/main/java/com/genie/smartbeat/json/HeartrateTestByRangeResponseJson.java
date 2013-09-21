package com.genie.smartbeat.json;
import java.util.List;

import com.genie.smartbeat.beans.FitnessHeartrateTestBean;;

public class HeartrateTestByRangeResponseJson {

	private String userId;
	private List<FitnessHeartrateTestBean> heartrateTests;
	
	public List<FitnessHeartrateTestBean> getHeartrateTests() {
		return heartrateTests;
	}
	public void setHeartrateTests(List<FitnessHeartrateTestBean> heartrateTests) {
		this.heartrateTests = heartrateTests;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
