package com.enrico200165.utils.rdb_jdbc;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author it068498
 *
 */
/**
 * @author it068498
 *
 */
public class JDBCEVUtility {

	public static String escapeForSQL(String originale) {
		if (originale == null) {
			log.log(Level.SEVERE, "errore, string null");
			return "";
		}
		originale = originale.replace("'", "''");
		return originale;
	}

	
	public static String wrapSingleQuotes(String originale, boolean appendComma) {
		String ret = "'"+originale+"'";
		if (appendComma)
			ret += ",";
		return ret;
	}

	
	/**
	 * non fa nulla esiste solo per essere riusata
	 * @param originale
	 * @return
	 */
	public static String escapeGeneric(String originale) {
		if (originale == null) {
			log.log(Level.SEVERE, "errore, string null");
			return "";
		}
		originale = originale.replace("'", "''");

		return originale;
	}

	
	/**
	 * Commento scritto quando funzione dimenticata
	 * CREDO servisse a estrarre la data  da una stringa che dava anche l'ora
	 * @param dataCSV
	 * @return
	 */
	public static String dataCalendario(String dataCSV) {
		String d = "";
		d += dataCSV.substring(6, 10);
		d += dataCSV.substring(3, 5);
		d += dataCSV.substring(0, 2);

		return d;
	}

	static Logger log=Logger.getLogger(JDBCEVUtility.class.getSimpleName());
}
