package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.configs.ChannelIFC;

public class ChannelConfigItalianPenPalsUsersTable implements ChannelIFC {
	
	@Override
	public String getChannelType() {
		// web, email, banner, search
		return "web";
	}

	@Override
	public String getChannelVendor() {
		// ex. Web: japan-guide.com email: gmail 
		return "italianpenpals";
	}

	@Override
	public String getChannelName() {
		// ex. WEB: site name: japan-guide | mailing: mailing template name
		return "http://italianpenpas.org";
	}

	@Override
	public String getChannelItem() {
		return "penpals global list";
	}
}
