package com.enrico200165.utils.rdb_jdbc;

public interface IRowSetSelector {

	long getRowsNumber();

	boolean loadFromDB();
	
	boolean writeToFile(String fname, String delimiter, boolean withHeader);
	
}