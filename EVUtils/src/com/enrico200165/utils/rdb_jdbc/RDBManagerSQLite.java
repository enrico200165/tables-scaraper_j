package com.enrico200165.utils.rdb_jdbc;

import java.io.*;
import java.sql.*;
import org.apache.log4j.Logger;

public class RDBManagerSQLite extends RDBManager {

	public RDBManagerSQLite(String DBPathname, boolean mustExist)
			throws ClassNotFoundException, IOException {

		super(/* driverClassName */DBConst.JDBC_DRV_SQLITE,
		/* DBMSURLPar */DBConst.JDBC_DB_URL_SQLITE + DBPathname, DBPathname,
		/* username */"", /* password */"");

		if (mustExist) {
			File f = new File(DBPathname);
			if (!f.exists() || f.isDirectory()) {
				log.error("sqlite db path does not exist:\n" + DBPathname);
				throw (new IOException("File does NOT exists"));
			}
		}
	}

	public String getDBURL() {
		return this.DBMSFullURL;
	}

	@Override
	protected String buildConnStr() {
		if (DBMSFullURL == null) {
			log.error("DB URL is null, cannot open it");
			return "";
		}
		return DBMSFullURL;
	}

	public void setDriverName(String s) throws ClassNotFoundException {
		this.driverClass = s;
		Class.forName(this.driverClass);
		log.debug("OK: JDBC driver caricato");
	}

	public String getDriverName() {
		return this.driverClass;
	}

	void useDB(String dbname) throws SQLException {
	}

	public boolean startDBServer() {
		log.debug("in sqlite non c'è nessun DB da avviare");
		return true;
	}

	public boolean openDB(boolean createIfNotExists) {
		log.trace("openDB() per sqlite adesso non fa assolutamente nulla, se ci sono problemi contolla qui");
		return true;
	}

	boolean openDB() {
		return openDB(false);
	}

	boolean connectToDBMS() {
		return super.connectToDBMS();
	}

	public boolean open() {
		return connectToDBMS() && openDB();
	}

	private static org.apache.log4j.Logger log = Logger
			.getLogger(RDBManagerSQLite.class);
}
