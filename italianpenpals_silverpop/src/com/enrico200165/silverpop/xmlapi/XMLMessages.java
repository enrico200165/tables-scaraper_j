package com.enrico200165.silverpop.xmlapi;

import java.util.ArrayList;

import com.enrico200165.utils.html.HTMLGenElement;

public class XMLMessages {

	// auth1 e auth2 tipicametne/inizialmente username e password
	static String envBody(String operation,String payload) {
		return "<Envelope><Body><" +operation+">" + payload + "\n</"+operation+"></Body></Envelope>";

	}


	static HTMLGenElement envBodyEl(String operation, ArrayList<HTMLGenElement> children) {
		HTMLGenElement root = (new HTMLGenElement("Envelope"));
		HTMLGenElement tip = root.addChildQuick("Body","")
				.addChildQuick(operation, "");
		
		for (HTMLGenElement e : children) {
			tip.addChild(e);
		}
		
		return root;
	}

	
	
	public static String logout(String auth) {
		return envBody("Logout","");
	}

	public static String login(String auth1, String auth2) {
		String payload = "\n<USERNAME>" + auth1 + "</USERNAME>" + "\n<PASSWORD>" + auth2 + "</PASSWORD>";
		return envBody("Login",payload);
	}

	public static String getLists() {
		HTMLGenElement oper = new HTMLGenElement("Envelope");		
		HTMLGenElement tmp = oper.addChildQuick("Body", null).addChildQuick("GetLists", null);	

		tmp.addChildQuick("VISIBILITY", "1");
		tmp.addChildQuick("LIST_TYPE", "2");
		tmp.addChildQuick("INCLUDE_ALL_LISTS", "True");		
				
		return oper.getHTMLMarkUp(0);
	}

	
	public static String getListMetaData(String listID) {				
		HTMLGenElement oper = new HTMLGenElement("Envelope");		
		HTMLGenElement tmp = oper.addChildQuick("Body", null).addChildQuick("GetListMetaData", null);	
		tmp.addChildQuick("LIST_ID", listID);
		return oper.getHTMLMarkUp(0);
	}
	

	public static String ImportList(String listID,String mapFile, String source_file) {				
		
		HTMLGenElement oper = new HTMLGenElement("Envelope");		
		HTMLGenElement tmp = oper.addChildQuick("Body", null).addChildQuick("ImportList", null);	

		tmp.addChildQuick("LIST_ID", listID);
		
		tmp.addChildQuick("MAP_FILE", mapFile);
		tmp.addChildQuick("SOURCE_FILE", source_file);
		tmp.addChildQuick("EMAIL", "enrico200165@gmail.com");
		tmp.addChildQuick("LIST_ID", listID);
		
				
		return oper.getHTMLMarkUp(0);
	}	


	public static String getJobStatus(String jobID) {				
		
		HTMLGenElement oper = new HTMLGenElement("Envelope");		
		HTMLGenElement tmp = oper.addChildQuick("Body", null).addChildQuick("GetJobStatus", null);	

		tmp.addChildQuick("JOB_ID", jobID);
		
		return oper.getHTMLMarkUp(0);
	}	



	public static String RawRecipientDataExport(String emailAddr) {
		ArrayList<HTMLGenElement> children = new ArrayList<HTMLGenElement>();
		children.add(new HTMLGenElement("MOVE_TO_FTP",null));
		String ret = envBodyEl("RawRecipientDataExport",children).getHTMLMarkUp(0);
				
		return ret;
	}


	public static String WebTrackingDataExport(String emailAddr) {
		
		ArrayList<HTMLGenElement> children = new ArrayList<HTMLGenElement>();
		children.add(new HTMLGenElement("MOVE_TO_FTP",null));
		children.add(new HTMLGenElement("DATABASE_ID","49165"));
		children.add(new HTMLGenElement("ALL_EVENT_TYPES",null));
	
		String ret = envBodyEl("WebTrackingDataExport",children).getHTMLMarkUp(0);
				
		return ret;
	}

	
}
