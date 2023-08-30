package com.enrico200165.utils.rdb_jdbc;

import java.util.logging.Level;

import java.sql.*;
import java.util.*;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author enrico
 *         Molte delle cose che sto scrivendo ui in freta andrebbero messer in
 *         RowSelector
 *         in una classe intermedia
 */
public class JDBCEVTable extends JDBCEVSelectABC {

	public JDBCEVTable(String tableName, IRDBManager dmdb) {
		super(dmdb);
		rs = null;
		this.tableName = tableName;
		this.dmdb = dmdb;
		this.fields = new ArrayList<JDBCEVField>();
	}

	public boolean deleteAllRows() {

		Statement st;
		try {
			int nrRighe = -1;
			st = this.getMyDBConn().createStatement();
			nrRighe = st.executeUpdate("DELETE FROM " + this.getName());
			log.info("tabella " + this.getName() + " tentata eliminazione di tutte le righe, righe ora: " + nrRighe);
		} catch (SQLException e) {
			log.log(Level.SEVERE, "tabella " + this.getName() + "cancellazione di tutte le righe fallita", e.toString());
		}

		return false;
	}

	/**
	 * (DEscrizione scritta dopo a freddo) It should be based on positional
	 * correspondence between the values in the parameter values and the fields
	 * in the table
	 * 
	 * @param st
	 * @param values
	 * @param unique
	 * @param linea
	 * @return
	 */
	public boolean insertRow(Statement statementPar, String[] values, boolean unique, long linea, boolean separate) {
		Statement st;
		String s = "INSERT INTO " + this.getName() + " ";
		String separateValues = "";
		String valuesTarget;
		try {

			if (statementPar == null) {
				st = this.dmdb.getConnection(false).createStatement();
			} else {
				st = statementPar;
			}

			if (separate) {
				s += " (";
				valuesTarget = separateValues;
			} else {
				s += "SET ";
				valuesTarget = s;
			}

			boolean firstLine = true;
			for (JDBCEVField f : fields) {
				if (!firstLine) {
					s += ","; // prepende
					separateValues += ",";
				}

				// correggi valori nulli
				if (values[f.posInCSVFile] == null) values[f.posInCSVFile] = "0";

				s += f.getFieldName();

				// mandiamo i valori su una stringa o sull'altra a seconda dei
				// casi
				firstLine = false;
				if (f.posInCSVFile >= 0 && f.posInCSVFile < values.length) {
					switch (f.tipo) {
					case java.sql.Types.DATE:
						valuesTarget += JDBCEVUtility.dataCalendario(values[f.posInCSVFile]);
						separateValues += JDBCEVUtility.dataCalendario(values[f.posInCSVFile]);
						break;
					case java.sql.Types.DOUBLE:
						valuesTarget += JDBCEVUtility.escapeForSQL(values[f.posInCSVFile]);
						separateValues += JDBCEVUtility.escapeForSQL(values[f.posInCSVFile]);
						break;
					case java.sql.Types.VARCHAR:
						valuesTarget += "'" + JDBCEVUtility.escapeForSQL(values[f.posInCSVFile]) + "'";
						separateValues += "'" + JDBCEVUtility.escapeForSQL(values[f.posInCSVFile]) + "'";
						break;
					default:
						log.log(Level.SEVERE, "tipo non gestito: " + f.tipo);
						valuesTarget += values[f.posInCSVFile];
						separateValues += values[f.posInCSVFile];
					}
				} else {
					if (f.getFieldName().equals("linea")) {
						valuesTarget += " linea =" + linea;
					} else {
						log.log(Level.SEVERE, "campo " + f.getFieldName() + " con posizione in csv fuori range: " + f.posInCSVFile + " not in [0-"
								+ values.length + "]");
						return false;
					}
				}
			}
			if (separate) {
				s += " ) VALUES (" + separateValues + ")";
			}
			st.executeUpdate(s);
		}
		catch (SQLException e) {
			// SQLite throws SQLException anzich�
			// MySQLIntegrityConstraintViolationException
			log.log(Level.SEVERE, "errore durante INSERT in tabella " + this.getName() + "\nstatement: " + s, e.toString());
			return false;
		}
		catch (Exception e) {
			if (!unique) {
				log.log(Level.SEVERE, "INSERT in " + this.getName() + " valore gia presente, ma duplicati dovrebbero essere accettati");
				return false;
			}
			log.log(Level.WARNING,  "inserimento fallito: " + s, e.toString());
			return false;
		}
		return true;
	}

	public boolean create() {
		if (existsInDB()) {
			log.log(Level.WARNING,  "tabella " + this.getName() + " esiste gia");
			return true;
		}
		if (fields.size() <= 0) {
			log.log(Level.SEVERE, "tabella " + this.getName() + " lista campi vuota");
			return false;
		}
		String createTableStStr = ""; // importante per la gestione delle
		// virgole
		String createIDXStStr;
		String pk = "";
		ArrayList<String> indici = new ArrayList<String>();

		for (JDBCEVField f : fields) {
			if (createTableStStr.length() > 0) createTableStStr += ",";
			createTableStStr += f.getFieldName();
			createTableStStr += " " + f.getTipoName();
			if (f.getTipo() == java.sql.Types.VARCHAR) createTableStStr += "(" + f.getLunghezza() + ")";

			// NB fallisce se c'� pi� di una primary key (composta)
			if (f.getTipoChiave() == TipoChiave.PRIMARY_KEY) pk = ", PRIMARY KEY(" + f.getFieldName() + ")";
			if (f.getTipoChiave() == TipoChiave.FOREIGN_KEY) indici.add(f.getFieldName());
		}
		createTableStStr = "CREATE TABLE " + getName() + "(" + createTableStStr + pk + ")";
		log.info("create(), create statement: " + createTableStStr);

		try {
			Statement stmt = dmdb.getConnection(false).createStatement();
			log.info("creo tablella con statement:\n" + createTableStStr);
			stmt.executeUpdate(createTableStStr);
			log.info("creata tabella: " + this.getName());
			for (String idxStr : indici) {
				createIDXStStr = "CREATE INDEX " + idxStr + "idx" + " ON " + this.getName() + "(" + idxStr + ")";
				stmt.executeUpdate(createIDXStStr);
				log.info("creato indice su colonna " + idxStr + " per tabella: " + this.getName());
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "fallita creazione tabella", e.toString());
		}

		return false;
	}

	public String getName() {
		return tableName;
	}

	public String toString() {
		String s = "";
		s += "tabella: " + this.getName() + " ( ";
		for (JDBCEVField f : fields) {
			s += "\n[ " + f.toString() + " ]";
		}
		s += " )";
		return s;
	}

	public boolean existsInDB() {

		DatabaseMetaData dbm;
		ResultSet rs;

		if (this.getMyDBConn() == null) {
			log.log(Level.SEVERE, "connessione nulla");
			return false;
		}

		try {
			dbm = this.getMyDBConn().getMetaData();
		} catch (SQLException e) {
			log.log(Level.SEVERE, "non riesco a ottenere i metadati", e.toString());
			return false;
		}

		try {
			// String[] types = { "TABLE" };
			rs = dbm.getTables(null, null, this.getName(), null);
			if (rs.next()) {
				while (rs.next()) {
					// log.info( rs.getString(1));
					// log.info(rs.getString(2));
					log.info("tabella da metadati: " + rs.getString(3));
				}
				return true;
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "controllo se esiste tabella: " + tableName, e.toString());
		}

		return false;
	}


	@Override
	public String getSQLString() {
		return "SELECT * FROM " + this.tableName;
	}

	String tableName;

	private static Logger log = LogManager.getLogManager().getLogger(JDBCEVTable.class.getSimpleName());

}
