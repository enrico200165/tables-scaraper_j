
package com.enrico200165.utils.rdb_jdbc.test;

import com.enrico200165.utils.rdb_jdbc.JDBCEVTable;
import com.enrico200165.utils.rdb_jdbc.RDBManagerMicrosoftSQLSrv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MicrosoftSQLSrvTest {

	/* Info da sample action table uac_sys.dbo.UA_ActionCustomer
	 I campinella sample table e il matching con la contact history:
	[CustomerID] 			: Indiv_ID, 1
	[ActionDateTime]		: ContactDateTime 3
	[ResponseChannel]		: Contact_Channel
	[CampaignCode]			: Campaign code
	[OfferCode]				: Offer_Code
	[CellCode]				: Cell_Name
	[TreatmentCode]			: TreatmentInstID
	[ProductID]				: ???
	[ResponseTypeCode]

	Campi disponibili nella join delle contact tables

	Indiv_ID	
	CellID	
	PackageID	
	ContactDateTime	
	UpdateDateTime	
	ContactStatusID	
	DateID	
	TimeID	
	UserDefinedFields	
	ValueBefore	
	UsageBefore	
	Campaign_Code	
	Offer_Code	
	Cell_Name	
	Contact_Channel	
	DirectResponse	
	Product_Promo	
	--- presumibilmente dalla tabella di dettaglio
	Indiv_ID	
	TreatmentInstID	
	ContactStatusID	
	ContactDateTime	
	UpdateDateTime	
	DateID	
	TimeID	
	UserDefinedFields	
	ValueBefore	
	UsageBefore	
	Campaign_Code	
	Offer_Code	
	Cell_Name	
	Contact_Channel	
	DirectResponse
	Product_Promo	
	RTSelectionMethod
	 	 
	 */

	
	public static void main(String[] s) {
		MicrosoftSQLSrvTest tfm  = new MicrosoftSQLSrvTest();
		tfm.perform();
	}

	
	public MicrosoftSQLSrvTest() {
		super();
		contactTFields = new ArrayList<String>();
		buildFieldList();
	}
	
	
	/**
	 * gestisce i nomi delle colonne da escludere dal result set
	 */
	void buildFieldList() {			
		contactTFields.add(contactTable+"."+"Indiv_ID");
		contactTFields.add(contactTable+"."+"CellID");
		contactTFields.add(contactTable+"."+"ContactDateTime");
		contactTFields.add(contactTable+"."+"ContactStatusID");
		contactTFields.add(contactTable+"."+"Campaign_Code");
		contactTFields.add(contactTable+"."+"Offer_Code");
		contactTFields.add(contactTable+"."+"Contact_Channel");
		contactTFields.add(contactTable+"."+"DirectResponse");
	}

	String fields() {
		String s = "";
		for (int i = 0; i < contactTFields.size(); i++) {
			s+= contactTFields.get(i);
			if (i < contactTFields.size()-1)
				s +=" ,";
		}
		return s;
	}

	public  void perform() {

		String campSysDB = "emm_camp";
		RDBManagerMicrosoftSQLSrv mgr = null;

		try {
			mgr = new RDBManagerMicrosoftSQLSrv(
					"com.microsoft.sqlserver.jdbc.SQLServerDriver",
					"jdbc:sqlserver" /* DBMSURLPar */, "localhost", campSysDB,// dbName,
					"integratedSecurity=true;" /* var URL */, "sa", "unica*03", false,
					true);

			mgr.open(false);

			JDBCEVTable contactH = new JDBCEVTable("UA_IndivContactHistory", mgr); // non necessaria
			contactH.readMetadata();
			log.info(contactH.toString());

			JDBCEVTable contactHDet = new JDBCEVTable("UA_IndivDtlContactHist", mgr); // non necessaria
			contactHDet.readMetadata();
			log.info(contactHDet.toString());

			String query = "Select "+fields();
			query += " from " + contactTable + ", "+ contactHDet.getName();
			query += " where " + contactTable + ".Indiv_ID = "
					+ contactHDet.getName() + ".Indiv_ID;";

			String sep = "\t";
			Statement stmt = mgr.getConnection(false).createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();

			String UnicaHome = "C:\\Unica\\Campaign\\";
			// -- prepariamo quello che possiamo preparare ---
			String pathName = UnicaHome + "partitions\\partition1\\invidual_action_table.csv";
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
				System.out.println("escludo col nr: " + entry.getKey()
						+ " nome: " + entry.getValue());
			}

			// ======== scriviamo il file
			FileOutputStream fos = null;
			OutputStreamWriter outwriter = null;
			String line = "";
			int pathNr = 1;
			int rowNum = 0;
			try {
				fos = new FileOutputStream(pathName);
				outwriter = new OutputStreamWriter(new FileOutputStream(
						pathName), "UTF-8");

				outwriter.write(header + "\n");

				int limitRows = 10;
				while (rs.next() && rowNum < limitRows) {
					rowNum++;
					line = "";
					for (int i = 1; i <= numberOfColumns; i++) {
						String colName = rsmd.getColumnName(i);
						if (colsToExclude.containsKey(colName)) {
							System.out.println("saltata colonna: " + i
									+ " nome: " + colsToExclude.get(i));
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

	private static Logger log = LogManager.getLogger(MicrosoftSQLSrvTest.class.getName());
}
