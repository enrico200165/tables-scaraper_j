package com.enrico200165.weblistscraper.configs.site_japanguide;

import java.util.List;

import com.enrico200165.utils.str_regex.NameValuePairString;
import org.apache.log4j.Logger;

import com.enrico200165.weblistscraper.configs.HostConfigABC;
import com.enrico200165.weblistscraper.tools.FormManagerABC;

/**
 * @author enrico
 * Functionoid to read hidden values in Drupal 7 login form
 * outputs list of name values of hidden fields 
 */
public class LoginFormManagerJapanguide extends FormManagerABC {

	
	
	public LoginFormManagerJapanguide(HostConfigABC hcfgPar) {
		super(hcfgPar);
	}


	@Override
	public void setFormFields(List<NameValuePairString> formFields, List<NameValuePairString> replaceVals, List<String> removeList,
							  List<NameValuePairString> addList) {
		
		replaceVals.add(new NameValuePairString("aEMAIL", hcfg.getUserName()));
		replaceVals.add(new NameValuePairString("aPASSWORD", hcfg.getPassword()));
	}

	
	private static org.apache.log4j.Logger log = Logger.getLogger(LoginFormManagerJapanguide.class);
}
