package com.enrico200165.utils.rdb_jdbc;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.IOException;
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
			log.log(Level.SEVERE, "non sono riuscito ad avviare mysql da java", e.toString());
			return false;
		}
	}

	@Override
	protected String buildConnStr() {
		if (DBMSFullURL == null) {
			log.log(Level.SEVERE, "DB URL is null, cannot open it");
			return "";
		}
		return DBMSFullURL;
	}

	static Logger log=Logger.getLogger(RDBManagerMySQL.class.getSimpleName());
}
