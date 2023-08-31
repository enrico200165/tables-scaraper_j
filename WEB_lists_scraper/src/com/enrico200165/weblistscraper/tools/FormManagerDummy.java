package com.enrico200165.weblistscraper.tools;

import com.enrico200165.utils.str_regex.NameValuePairString;
import com.enrico200165.weblistscraper.configs.HostConfig;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;

public class FormManagerDummy extends FormManagerABC{

	public FormManagerDummy(HostConfig hcfgPar) {
		super(hcfgPar);
	}

	// deve solo avvalorare le collections
	public void setFormFields(List<NameValuePairString> formFields,
			List<NameValuePairString> replaceVals, 
			List<String> removeList,
			List<NameValuePairString> addList) {
		
	}
	
	
	protected HostConfig hcfg;
	
	static Logger log=Logger.getLogger(FormManagerDummy.class.getSimpleName());
}
