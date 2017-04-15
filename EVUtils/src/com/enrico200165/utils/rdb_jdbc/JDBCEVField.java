package com.enrico200165.utils.rdb_jdbc;

//import org.apache.log4j.Logger;

public class JDBCEVField {

	public JDBCEVField(String nome, int tipo, int lunghezza,
			TipoChiave tipoChiave, int posCSV) {
		this.fieldName = nome;
		this.tipo = tipo;
		this.lunghezza = (lunghezza == 0) ? 32 : lunghezza;
		this.tipoChiave = tipoChiave;
		posInCSVFile = posCSV;
	}

	public String toString() {
		return "nome="+fieldName + " tipo=" + getTipoName() + "-"
				+ lunghezza + " tipochiave=" + tipoChiave.toString() + " "
				+ "pos="+this.posInCSVFile;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getTipo() {
		return tipo;
	}

	public String getTipoName() {
		String s = "";
		switch (this.tipo) {
		case java.sql.Types.BOOLEAN:s = "BOOLEAN";break;
		case java.sql.Types.DATE:s = "DATE";break;
		case java.sql.Types.DOUBLE:s = "DOUBLE";break;
		case java.sql.Types.INTEGER:s = "INTEGER";break;
		case java.sql.Types.VARCHAR:s = "VARCHAR";break;
		case java.sql.Types.BIGINT:s="BIGINT";break; 
		case java.sql.Types.TIMESTAMP:s="TIMESTAMP";break;		
		default:
			s ="type-not-managed_int="+this.tipo;
			break;
		}

		return s;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getLunghezza() {
		return lunghezza;
	}

	public void setLunghezza(int lunghezza) {
		this.lunghezza = lunghezza;
	}

	public TipoChiave getTipoChiave() {
		return tipoChiave;
	}

	public void setTipoChiave(TipoChiave tipoChiave) {
		this.tipoChiave = tipoChiave;
	}

	public String fieldName;
	public int tipo; // valori da java.sql.Types
	public int lunghezza;
	TipoChiave tipoChiave;
	int posInCSVFile;
}
