
package com.enrico200165.utils.rdb_jdbc;

import java.sql.Connection;

/**
 * @author enrico
 * this should be an abstract RDBMS data manager, independent from the specific RDBMS
 * its responsibilities are
 * - manage the DB server
 * - manage the DB
 * - nothing else (no table, no data model )
 * 
 */
public interface IRDBManager  {
	
    boolean startDBServer();
    
    boolean open(boolean tryToCreateIfNotExists);

    boolean openDB(boolean createIfNotExists);

    boolean closeConnection();

    
    Connection getConnection(boolean closeAndReopen);

    void setDBURL(String s);
    String getDBURL();
 
	String getDBName();

    
    void setDriverName(String s) throws ClassNotFoundException;
    String  getDriverName();
    
	

    boolean flush();
	
	
//    String buildConnStr();
}
