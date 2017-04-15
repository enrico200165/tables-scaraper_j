package com.enrico200165.utils.rdb_jdbc;

import java.sql.*;
import java.util.*;

import com.enrico200165.utils.files.FileHelper;
import com.enrico200165.utils.files.IRenderableAsTexLineStringWrapper;
import com.enrico200165.utils.files.IRenderableAsTextLine;
import com.enrico200165.utils.files.TexLineRendererString;
import com.enrico200165.utils.str_regex.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author enrico
 *         Molte delle cose che sto scrivendo ui in freta andrebbero messer in
 *         RowSelector
 *         in una classe intermedia
 */
public abstract class JDBCEVSelectABC implements IRowSetSelector {

	public JDBCEVSelectABC(IRDBManager dmdb) {
		rs = null;
		this.dmdb = dmdb;
		this.fields = new ArrayList<JDBCEVField>();
	}

	
	public abstract String getSQLString();

	@Override
	public boolean loadFromDB() {
		StatementEV st;
		try {

			// load rows
			st = new StatementEV(this.dmdb.getConnection(false).createStatement());
			log.info("eseguo: " + getSQLString());
			rs = st.executeQuery(getSQLString(), true);

			// read metadata
			readMetadata();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Probabilmente è necessario aver fatto una select, NON LO CONTROLLO PERO'
	 * 
	 * @return
	 */
	public boolean readMetadata() {
		try {
			ResultSetMetaData md = rs.getMetaData();

			this.fields = new ArrayList<JDBCEVField>();
			int col = md.getColumnCount();
			for (int i = 1; i <= col; i++) {
				addField(md.getColumnName(i), md.getColumnType(i), md.getColumnDisplaySize(i), TipoChiave.UNDEFINED, 0);
			}
		} catch (SQLException e) {
			log.error("", e);
		}
		return true;
	}

	ArrayList<String> getHeaderInfo(ArrayList<String> escludeCOlumns) {

		if (this.fields.size() <= 0) readMetadata();

		ArrayList<String> fl = new ArrayList<String>();
		for (JDBCEVField f : this.fields) {
			if (escludeCOlumns == null || !escludeCOlumns.contains(f.getFieldName())) {
				fl.add(f.getFieldName());
			}
		}
		return fl;
	}

	@Override
	public boolean writeToFile(String fullFname, String delimiter, boolean withHeader) {
		if (!loadFromDB()) {
			log.error("failed to load from DB");
			return false;
		}

		List<IRenderableAsTextLine> lines = new ArrayList<IRenderableAsTextLine>();
		try {
			int nrCols = rs.getMetaData().getColumnCount();
			int rowNum = 0;
			IRenderableAsTexLineStringWrapper line;
			while (rs.next()) {
				line = new IRenderableAsTexLineStringWrapper();
				rowNum++;
				for (int i = 1; i <= nrCols; i++) {
					if (i == 1)
						line.concat(rs.getString(i));
					else
						line.concat(rs.getString(i), delimiter);
				}
				line.concat("\n");
				lines.add(line);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> headerAl = getHeaderInfo(null);
		String header = StringUtils.arrayListToString(headerAl, delimiter);

		FileHelper fh = new FileHelper(null, fullFname, "", null);
		fh.writeList(lines, header, "footer da rinominare", null /* filter */, new TexLineRendererString() /* renderer */, -1, -1, -1, true,
				delimiter);

		return true;
	}

	public long getRowsNumber() {
		int count = -1;

		Statement st;
		try {
			if (this.getMyDBConn() == null) {
				log.fatal("connessione DB � null");
				dmdb.getConnection(true);
				return -1;
			}

			st = this.getMyDBConn().createStatement();
			ResultSet res = st.executeQuery(getSQLString());
			while (res.next()) {
				count = res.getInt(1);
			}
			log.debug("nr righe: " + count);
		} catch (SQLException e) {
			log.error("fallita query per numero elementi", e);
		}
		return count;
	}

	
	Connection getMyDBConn() {
		return dmdb.getConnection(false);
	}

	public void addField(String nome, int tipo, int lunghezza, TipoChiave tipoChiave, int posInCSV) {
		fields.add(new JDBCEVField(nome, tipo, lunghezza, tipoChiave, posInCSV));

	}

	public String toString() {
		String s = "";
		s += "selettore " + getSQLString();
		return s;
	}

	public ArrayList<JDBCEVField> fields;
	protected IRDBManager dmdb;

	String sqlStatement;
	ResultSet rs;

	private static org.apache.log4j.Logger log = Logger.getLogger(JDBCEVSelectABC.class);
}
