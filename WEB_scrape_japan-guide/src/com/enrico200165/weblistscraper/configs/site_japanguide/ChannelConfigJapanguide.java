package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.configs.ChannelConfigVanilla;

public class ChannelConfigJapanguide extends ChannelConfigVanilla {
	
	@Override
	public String getType() {
		// web, email, banner, search
		return "web";
	}

	@Override
	public String getVendor() {
		// ex. Web: japan-guide.com email: gmail 
		return "japan-guide";
	}

	@Override
	public String getName() {
		// ex. WEB: site name: japan-guide | mailing: mailing template name
		return "language-exchange";
	}

	@Override
	public String getItem() {
		return "http://www.japan-guide.com/local/?aCAT=3";
	}
}
