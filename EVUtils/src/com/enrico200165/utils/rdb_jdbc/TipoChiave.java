package com.enrico200165.utils.rdb_jdbc;


	public enum TipoChiave {
		UNDEFINED,
		NOIDX_NOKEY_NOUNIQUE,
		PRIMARY_KEY, 
		FOREIGN_KEY, 
		IDXED_UNIQUE, 
		IDXED_NOT_UNIQUE;
		
		public String toString() {
			switch (this) {
			case NOIDX_NOKEY_NOUNIQUE: 	return "NOIDX_NOKEY_NOUNIQUE";
			case PRIMARY_KEY: 			return "PRIMARY_KEY";
			case FOREIGN_KEY: 			return "FOREIGN_KEY";
			case IDXED_UNIQUE: 			return "IDXED_UNIQUE";
			case IDXED_NOT_UNIQUE: 		return "IDXED_NOT_UNIQUE";
			case UNDEFINED:				return "UNDEFINED";
			default: return "errore";
			}
		} 
	
	}
	
	
