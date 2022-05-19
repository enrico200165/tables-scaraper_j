package com.enrico200165.utils.rdb_jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Administrator
 * 
 *         Creata come banale wrapper attorno a java.sql.Statement per includere
 *         tracing
 * 
 */
public class StatementEV {

	public StatementEV(Statement st) {
		this.st = st;
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql, boolean logIt) throws SQLException {
		if (logIt) {
			log.info("eseguo query: " + sql);
		}
		return st.executeQuery(sql);
	}

	Statement st;
	private static Logger log = LogManager.getLogger(StatementEV.class.getName());
}
