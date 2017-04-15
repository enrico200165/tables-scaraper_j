package com.enrico200165.weblistscraper.tools;



public class Result {
	
	
	public enum RC {
		OK,  NOT_FOUND, MUST_EXIT,ERROR, ACTION_NOT_ALLOWED
	}
	
	public Result() {
		rc = RC.OK;
		detailedCode = 0;
		errorMessage ="";
		continua = true;
	}
	

	public int getDetailedCode() {
		return detailedCode;
	}


	public void setDetailedCode(int detailedCode) {
		this.detailedCode = detailedCode;
	}


	public RC getRc() {
		return rc;
	}


	public String getRetStr() {
		return retStr;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public Result setRc(RC rcPar) {
		this.rc = rcPar;
		return this;
	}


	public Result setErrorMessage(String m) {
		this.errorMessage = m;
		return this;
	}

	
	
	public boolean isOk() {
		return (this.rc == RC.OK);
	}
	
	
	public Result setRetStr(String s) {
		this.retStr = s;
		return this;
	}
	

	public boolean getContinua() {
		return continua;
	}

	public Result  setContinua(boolean continua) {
		this.continua = continua;
		return this;
	}

	public String getMessage() {
		return errorMessage;
	}



	// boolean ok;
	RC rc;
	
	String retStr;
	int detailedCode;
    String errorMessage;
	boolean continua;
}
