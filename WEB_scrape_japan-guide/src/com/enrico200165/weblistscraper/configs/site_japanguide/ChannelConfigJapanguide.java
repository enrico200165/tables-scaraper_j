package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.configs.ChannelIFC;

public class ChannelConfigJapanguide implements ChannelIFC {
	
	@Override
	public String getChannelType() {
		// web, email, banner, search
		return "web";
	}

	@Override
	public String getChannelVendor() {
		// ex. Web: japan-guide.com email: gmail 
		return "japan-guide";
	}

	@Override
	public String getChannelName() {
		// ex. WEB: site name: japan-guide | mailing: mailing template name
		return "language-exchange";
	}

	@Override
	public String getChannelItem() {
		return "http://www.japan-guide.com/local/?aCAT=3";
	}
}
