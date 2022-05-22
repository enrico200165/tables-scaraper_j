package com.enrico200165.weblistscraper.tools;

import com.enrico200165.utils.str_regex.NameValuePairString;
import com.enrico200165.weblistscraper.configs.HostConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	private static Logger log = LogManager.getLogger(FormManagerDummy.class.getSimpleName());
}
