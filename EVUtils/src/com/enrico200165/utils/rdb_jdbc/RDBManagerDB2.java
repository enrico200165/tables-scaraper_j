package com.enrico200165.utils.rdb_jdbc;


import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class RDBManagerDB2 extends RDBManager {

	public RDBManagerDB2(String driverClassName /*
												 * superfluo. dovrei pescarlo
												 * dalle costanti
												 */, String DBMSURLPar, String varURL, String DBName, String username, String password)
			throws ClassNotFoundException {

		super(DBConst.JDBC_DRV_DB2, DBConst.JDBC_DB_URL_SCHEME_DB2 +"//"+ varURL + ":50000/" + DBName,DBName,username, password);
	}

	public boolean startDBServer() {
		return true;
	}


	@Override
	protected String buildConnStr() {
		if (DBMSFullURL == null) {
			log.log(Level.SEVERE, "DB URL is null, cannot open it");
			return "";
		}
		return DBMSFullURL;
	}

	static Logger log=Logger.getLogger(RDBManagerDB2.class.getSimpleName());
}
