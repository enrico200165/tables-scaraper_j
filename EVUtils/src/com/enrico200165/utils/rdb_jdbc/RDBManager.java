package com.enrico200165.utils.rdb_jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class RDBManager implements IRDBManager {

	public RDBManager(String driverClassName, String DBMFullURLPar, String DBName, String username, String password)
			throws ClassNotFoundException {

		this.driverClass = driverClassName;
		Class.forName(this.driverClass);
		log.debug("OK: JDBC driver caricato");
		this.dbName = DBName; // un po' ridondamte con DBMSFullURL che
								// normalmente la include
		this.DBMSFullURL = DBMFullURLPar; // andrebbe costruita dentro i db
											// manager
		this.password = password;
		this.username = username;
		log.debug("configured RDB manager, values\n" + "DBMSFullURL: " + this.DBMSFullURL);
	}

	public String getDBName() {
		return this.dbName;
	}

	public void setDBName(String name) {
		this.dbName = name;
	}

	public void setDBURL(String s) {
		this.DBMSFullURL = s;
	}

	public String getDBURL() {
		return this.DBMSFullURL;
	}

	public void setDriverName(String s) throws ClassNotFoundException {
		this.driverClass = s;
	}

	public String getDriverName() {
		return this.driverClass;
	}

	abstract protected String buildConnStr(); /*
											 * {
											 * if (DBMSFullURL == null) {
											 * log.error(
											 * "DB URL is null, cannot open it"
											 * );
											 * return "";
											 * }
											 * return DBMSFullURL;
											 * }
											 */

	/**
	 * @return
	 */
	boolean connectToDBMS() {
		Statement stmt;

		if (!startDBServer()) {
			log.error("Nono sono riuscito ad avviare il server");
			return false;
		}

		String connStr = buildConnStr();

		try {
			log.debug("connetto a DBMS e DB con URL: " + connStr);
			if (conn != null) {
				conn.close();
				conn = null;
			}
			conn = DriverManager.getConnection(connStr, username, password);
			log.trace("connected to DBMS ok, conn string:\n" + connStr);
			return true;
		} catch (SQLException e) {
			log.warn("DB conn. fallita, forse DB non esiste?", e);
		}
		return false;
	}

	void useDB(String dbname, boolean necessary) throws SQLException {
		if (!necessary) return;
		Statement stmt = conn.createStatement();
		String useStatement = "USE " + this.getDBName();
		stmt.executeUpdate(useStatement);
	}

	/**
	 * @return
	 */
	public boolean openDB(boolean createIfNotExists) {
		String stmtStr = "CREATE DATABASE IF NOT EXISTS " + this.getDBName();

		if (conn == null) {
			log.error("oggetto connessione jdbc � null");
			return false;
		}
		try {
			/*
			 * troppo pericoloso, attivarlo quando necessario
			 * Statement stmt = conn.createStatement();
			 * if (createIfNotExists) {
			 * stmt.executeUpdate(stmtStr);
			 * log.info("apro il database con: " + stmtStr);
			 * }
			 */
			useDB(this.getDBName(), false /*
										 * andrebbe passato come parametro ma
										 * sono di fretta ristrutturare poi
										 */);
		} catch (SQLException e) {
			log.fatal("non riesco ad aprire il database", e);
			return false;
		}
		return true;
	}

	final public Connection getConnection(boolean closeAndReopen) {
		try {
			if (conn != null && closeAndReopen) {
				conn.close();
				log.debug("OK connessione DB chiusa. RIAPRO IMMEDIATAMENTE");
				conn = null;
				conn = DriverManager.getConnection(buildConnStr(), username, password);
			}
			if (conn == null) {
				connectToDBMS();
			}
		} catch (SQLException e) {
			log.error("getConnection(), chiusura connessione per clean up fallita", e);
		}
		return conn;
	}

	public boolean flush() {
		if (conn != null) try {
			conn.commit();
			return true;
		} catch (SQLException e) {
			log.error("flush/commit fallito", e);
			return false;
		}
		return false;
	}

	public boolean closeConnection() {
		if (conn != null) try {
			flush();
			conn.close();
			conn = null;
			return true;
		} catch (SQLException e) {
			log.error("chiusura connessione DBMSfallita", e);
		}
		return false;
	}

	public boolean open(boolean tryToCreateIfNotExists) {
		return connectToDBMS() && openDB(tryToCreateIfNotExists);
	}


	Statement st;

	String DBMSFullURL;
	Connection conn;
	String dbName;
	String driverClass;
	String username;
	String password;

	private static Logger log = LogManager.getLogger(RDBManager.class.getSimpleName());
}
