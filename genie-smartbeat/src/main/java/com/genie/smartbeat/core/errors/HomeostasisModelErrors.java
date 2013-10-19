package com.genie.smartbeat.core.errors;

public enum HomeostasisModelErrors {

	ABSENCE_OF_HOMEOSTASIS_INDEX_MODEL(0);
	
	private int errorCode;
	
	private  HomeostasisModelErrors(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	@Override
	public String toString() {
		switch (this) {			  
		
		  case ABSENCE_OF_HOMEOSTASIS_INDEX_MODEL: return "homeostasis_index_model_error_" + getErrorCode() + "absence of homeostasis index model";
		}
		return null;
	}	
}
