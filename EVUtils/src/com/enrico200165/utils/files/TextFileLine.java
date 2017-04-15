package com.enrico200165.utils.files;

/**
 * @author it068498
 * To contain a file line and its coordinates
 */
public class TextFileLine {

	public TextFileLine(String pathPar,long lineNrPar,String linePar) {
		super();
		fPathName = pathPar;
		lineNr = lineNrPar;
		_line = linePar;
	}
	
	
	
	/**
	 * @return the fPathName
	 */
	public String getfPathName() {
		return fPathName;
	}
	/**
	 * @return the lineNr
	 */
	public long getLineNr() {
		return lineNr;
	}
	/**
	 * @return the _line
	 */
	public String get_line() {
		return _line;
	}
	/**
	 * @param fPathName the fPathName to set
	 */
	public void setfPathName(String fPathName) {
		this.fPathName = fPathName;
	}
	/**
	 * @param lineNr the lineNr to set
	 */
	public void setLineNr(long lineNr) {
		this.lineNr = lineNr;
	}
	/**
	 * @param line the _line to set
	 */
	public void set_line(String line) {
		_line = line;
	}



	String fPathName; // file from which it was read
	long lineNr; 
	String _line;
}
