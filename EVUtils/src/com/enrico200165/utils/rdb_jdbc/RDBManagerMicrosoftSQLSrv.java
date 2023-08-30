package com.enrico200165.utils.rdb_jdbc;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.IOException;

public class RDBManagerMicrosoftSQLSrv extends RDBManager {

	public RDBManagerMicrosoftSQLSrv(String driverClassName, String DBMSURLPar,
			String host, String DBName, String varURL, String username,
			String password, boolean createIfNotExist, boolean integrSec)
			throws ClassNotFoundException {
		super(DBConst.MS_SQLSRV_DRV_CLASS, "jdbc:sqlserver", DBName,username, password);
		this.host = host;
		setDBName(DBName);
	}

	public void setHostDBName(String host, String dbName) {
		this.host = host;
		this.dbName = dbName;
	}

	protected String buildConnStr() {
		if (host.length() == 0 || dbName.length() == 0) {
			log.log(Level.SEVERE, "you forgot the patchy initialization of MS Sequel server (host & dbname set in a set method");
		}
		log.log(Level.WARNING,  "al momento funziona solo con integrated security. Per supportare entrambi i modi va portata fuori dal "
				+ "costruttore la costruzione della stringa, in un metodo dedicato");
		String s = "jdbc:sqlserver://";
		if (host != null && host.length() > 0)
			s += host + ";";

		if (this.dbName != null && this.dbName.length() > 0)
			s += "databaseName=" + this.dbName + ";";

		if (this.username != null && this.username.length() > 0) {
			s += "user=" + this.username + ";";
			s += "password=" + this.password + ";";
		}
		// s += "integratedSecurity=true;";
		return s;
	}

	public boolean startDBServer() {

		try {
			if ("giusto per fregare il compilatore".length() > 9999) {
				Runtime.getRuntime().exec("dir");
			}
			log.info("in startDBServer for SQL Server");
			log.info("I do nothing");
			return true;
		} catch (IOException e) {
			log.log(Level.SEVERE, "non sono riuscito ad avviare MS SQL Server da java", e.toString());
			return false;
		}
	}

	String host;
	private static Logger log = LogManager.getLogManager().getLogger(RDBManagerMicrosoftSQLSrv.class.getSimpleName());
}
