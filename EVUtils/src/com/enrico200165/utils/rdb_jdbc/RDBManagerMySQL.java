package com.enrico200165.utils.rdb_jdbc;

import java.io.IOException;

import org.apache.log4j.Logger;

public class RDBManagerMySQL extends RDBManager {

	
	public RDBManagerMySQL(String DBMSURLPar, String varURL, String DBName, String username, String password)
			throws ClassNotFoundException {

		super(DBConst.JDBC_DRV_MYSQL, DBMSURLPar + "://"+varURL + "/"+DBName+"?user="+username+"&password="+password, DBName, username, password);
	}

	
	
	public RDBManagerMySQL(String driverClassName, String DBMSURLPar, String varURL, String DBName, String username, String password)
			throws ClassNotFoundException {

		super(DBConst.JDBC_DRV_MYSQL, DBMSURLPar + varURL + DBName, DBName, username, password);
	}

	public boolean startDBServer() {

		try {
			Runtime.getRuntime().exec("net start mysql");
			log.info("tento di avviare mysql server");
			return true;
		} catch (IOException e) {
			log.error("non sono riuscito ad avviare mysql da java", e);
			return false;
		}
	}

	@Override
	protected String buildConnStr() {
		if (DBMSFullURL == null) {
			log.error("DB URL is null, cannot open it");
			return "";
		}
		return DBMSFullURL;
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(RDBManagerMySQL.class);
}
