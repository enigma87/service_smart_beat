package com.genie.smartbeat.core.errors;

public enum HomeostasisIndexModelErrors {

	HOMEOSTASIS_INDEX_MODEL_ERROR(0),
	ABSENCE_OF_HOMEOSTASIS_INDEX_MODEL(1);
	
	private int errorCode;
	
	private  HomeostasisIndexModelErrors(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	@Override
	public String toString() {
		switch (this) {			  
		
		  case HOMEOSTASIS_INDEX_MODEL_ERROR: return "homeostasis_index_model_error_" + getErrorCode() + "homeostasis index model error";
		  case ABSENCE_OF_HOMEOSTASIS_INDEX_MODEL: return "homeostasis_index_model_error_" + getErrorCode() + "absence of homeostasis index model";
		}
		return null;
	}	
}
