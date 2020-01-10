package com.enrico200165.weblistscraper.tools;

import java.util.List;

import com.enrico200165.utils.str_regex.*;
import org.apache.log4j.Logger;

import com.enrico200165.weblistscraper.configs.HostConfig;

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
	
	private static org.apache.log4j.Logger log = Logger.getLogger(FormManagerDummy.class);
}
