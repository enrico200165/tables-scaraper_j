package com.enrico200165.utils.rdb_jdbc.test;

import com.enrico200165.utils.rdb_jdbc.JDBCEVTable;
import com.enrico200165.utils.rdb_jdbc.RDBManagerDB2;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IBM_DB2_test {

	/*
	 * Info da sample action table uac_sys.dbo.UA_ActionCustomer
	 * I campinella sample table e il matching con la contact history:
	 * [CustomerID] : Indiv_ID, 1
	 * [ActionDateTime] : ContactDateTime 3
	 * [ResponseChannel] : Contact_Channel
	 * [CampaignCode] : Campaign code
	 * [OfferCode] : Offer_Code
	 * [CellCode] : Cell_Name
	 * [TreatmentCode] : TreatmentInstID
	 * [ProductID] : ???
	 * [ResponseTypeCode]
	 * 
	 * Campi disponibili nella join delle contact tables
	 * 
	 * Indiv_ID
	 * CellID
	 * PackageID
	 * ContactDateTime
	 * UpdateDateTime
	 * ContactStatusID
	 * DateID
	 * TimeID
	 * UserDefinedFields
	 * ValueBefore
	 * UsageBefore
	 * Campaign_Code
	 * Offer_Code
	 * Cell_Name
	 * Contact_Channel
	 * DirectResponse
	 * Product_Promo
	 * --- presumibilmente dalla tabella di dettaglio
	 * Indiv_ID
	 * TreatmentInstID
	 * ContactStatusID
	 * ContactDateTime
	 * UpdateDateTime
	 * DateID
	 * TimeID
	 * UserDefinedFields
	 * ValueBefore
	 * UsageBefore
	 * Campaign_Code
	 * Offer_Code
	 * Cell_Name
	 * Contact_Channel
	 * DirectResponse
	 * Product_Promo
	 * RTSelectionMethod
	 */

	public static void main(String[] s) {
		IBM_DB2_test tfm = new IBM_DB2_test();
		tfm.perform();
	}

	public IBM_DB2_test() {
		super();
		contactTFields = new ArrayList<String>();
		buildFieldList();
	}

	/**
	 * gestisce i nomi delle colonne da escludere dal result set
	 */
	void buildFieldList() {
		contactTFields.add(contactTable + "." + "Indiv_ID");
		contactTFields.add(contactTable + "." + "CellID");
		contactTFields.add(contactTable + "." + "ContactDateTime");
		contactTFields.add(contactTable + "." + "ContactStatusID");
		contactTFields.add(contactTable + "." + "Campaign_Code");
		contactTFields.add(contactTable + "." + "Offer_Code");
		contactTFields.add(contactTable + "." + "Contact_Channel");
		contactTFields.add(contactTable + "." + "DirectResponse");
	}

	String fields() {
		String s = "";
		for (int i = 0; i < contactTFields.size(); i++) {
			s += contactTFields.get(i);
			if (i < contactTFields.size() - 1) s += " ,";
		}
		return s;
	}

	public void perform() {

		String host = "159.122.67.140";
		String dbname = "interact";
		String user = "db2admin";
		String password = "mcrE2014J9";

		RDBManagerDB2 mgr = null;

		try {

			// jdbc:db2://159.122.67.140:50000/interact
			mgr = new RDBManagerDB2(null, null, host, dbname, user, password);

			mgr.open(false);

			String tableName = "CUSTOMER_INFO";
			JDBCEVTable customer_info = new JDBCEVTable(tableName, mgr); // non																						// necessaria
			customer_info.readMetadata();
			log.info(customer_info.toString());

			String query = "Select * from " + tableName+ " "; //+ fields();
			query +=  "fetch first 10 rows only";

			String sep = "\t";
			Statement stmt = mgr.getConnection(false).createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();

			String UnicaHome = "C:\\Unica\\Campaign\\";
			// -- prepariamo quello che possiamo preparare ---
			String pathName = UnicaHome + "partitions\\partition1\\invidual_action_table.csv";
			pathName = "c:\\out.csv";
			String header = "";
			Map<Integer, String> colsToExclude = new HashMap<Integer, String>();
			for (int i = 1; i <= numberOfColumns; i++) {
				String colName = rsmd.getColumnName(i);
				if (!colsToExclude.containsKey(colName)) {
					header += colName + sep;
				} else {
					log.info("ignored column: " + colName);
				}
			}
			// QUI DOVREI AGGIUNGERE I NOMI DI COLONNA AGGIUNTIVA

			for (Map.Entry<Integer, String> entry : colsToExclude.entrySet()) {
				System.out.println("escludo col nr: " + entry.getKey() + " nome: " + entry.getValue());
			}

			// ======== scriviamo il file
			FileOutputStream fos = null;
			OutputStreamWriter outwriter = null;
			String line = "";
			int pathNr = 1;
			int rowNum = 0;
			try {
				fos = new FileOutputStream(pathName);
				outwriter = new OutputStreamWriter(new FileOutputStream(pathName), "UTF-8");

				outwriter.write(header + "\n");

				int limitRows = 10;
				while (rs.next() && rowNum < limitRows) {
					rowNum++;
					line = "";
					for (int i = 1; i <= numberOfColumns; i++) {
						String colName = rsmd.getColumnName(i);
						if (colsToExclude.containsKey(colName)) {
							System.out.println("saltata colonna: " + i + " nome: " + colsToExclude.get(i));
							// salto la colonna
						} else {
							line += rs.getString(i) + sep;
							// QUI DOVREI AGGIUNGERE I VALORI DI AZIONE
						}
					}
					line += "\n";
					outwriter.write(line);
				}
				outwriter.flush();
				outwriter.close();
				outwriter = null;
				fos.flush();
				fos.close();
				fos = null;
			} catch (IOException e) {
				log.error("Eccezione di IO", e);
			}

			log.info("scritto: " + pathName + " scritti: " + (rowNum));

			ProcessBuilder pb = new ProcessBuilder("C:/Program Files (x86)/Notepad++/notepad++.exe", pathName);
			pb.start();
		} catch (Exception e) {
			log.error("Eccezione di IO", e);
		}
	}

	static final String contactTable = "UA_IndivContactHistory";
	ArrayList<String> contactTFields;

	private static Logger log = LogManager.getLogger(IBM_DB2_test.class.getSimpleName());
}
