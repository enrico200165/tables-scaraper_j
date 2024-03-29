package com.enrico200165.weblistscraper.configs.drupal7;

import com.enrico200165.utils.str_regex.NameValuePairString;
import com.enrico200165.weblistscraper.configs.HostConfig;
import com.enrico200165.weblistscraper.tools.FormManagerABC;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;


/**
 * @author enrico Functionoid to read hidden values in Drupal 7 login form outputs list of name values of hidden fields
 */
public class FormManagerLoginDrupal7 extends FormManagerABC {

	public FormManagerLoginDrupal7(HostConfig hPar) {
		super(hPar);
	}

	@Override
	public void setFormFields(List<NameValuePairString> formFields,
			List<NameValuePairString> replaceVals, List<String> removeList,
			List<NameValuePairString> addList) {
		replaceVals.add(new NameValuePairString("aEMAIL",hcfg.getUserName()));
		replaceVals.add(new NameValuePairString("aPASSWORD",hcfg.getPassword()));
	}

	static Logger log=Logger.getLogger(FormManagerLoginDrupal7.class.getSimpleName());
}
