package com.enrico200165.utils.rdb_jdbc;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
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
	static Logger log=Logger.getLogger(StatementEV.class.getSimpleName());
}
