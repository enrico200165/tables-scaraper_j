package com.enrico200165.utils.rdb_jdbc.test;

import org.junit.Test;

import com.enrico200165.utils.rdb_jdbc.JDBCEVTable;
import com.enrico200165.utils.rdb_jdbc.RDBManager;
import com.enrico200165.utils.rdb_jdbc.RDBManagerMySQL;

public class Mysql {

	@Test
	public void test() {
		// fail("Not yet implemented");

		try {
			RDBManager mgr = new RDBManagerMySQL(
				"jdbc:mysql" /*String DBMSURLPar*/, 
				"italianpenpals.org" /*String varURL*/, 
				"italianpenpals" /*String DBName*/, 
				"enrico200165"/*String username*/, 
				"Ruthdan0"/*String password*/
				);
			
			JDBCEVTable t = new JDBCEVTable("users", mgr);
			// t.getRowsNumber();
			t.writeToFile("pippo.csv","\t",true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
