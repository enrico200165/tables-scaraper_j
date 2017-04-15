package com.enrico200165.weblistscraper.tools;

import java.util.List;

import com.enrico200165.utils.str_regex.*;
import com.enrico200165.weblistscraper.configs.HostConfigABC;
import org.apache.log4j.Logger;

public abstract class FormManagerABC {

	public FormManagerABC(HostConfigABC hcfgPar) {
		this.hcfg = hcfgPar;
	}

	// deve solo avvalorare le collections
	public abstract void setFormFields(List<NameValuePairString> formFields,
			List<NameValuePairString> replaceVals, 
			List<String> removeList,
			List<NameValuePairString> addList);
	
	
	protected HostConfigABC hcfg;
	
	private static org.apache.log4j.Logger log = Logger.getLogger(FormManagerABC.class);
}
