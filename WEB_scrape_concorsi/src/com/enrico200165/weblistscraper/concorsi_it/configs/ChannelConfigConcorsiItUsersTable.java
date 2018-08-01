package com.enrico200165.weblistscraper.concorsi_it.configs;

import com.enrico200165.weblistscraper.configs.ChannelConfigVanilla;

public class ChannelConfigConcorsiItUsersTable extends ChannelConfigVanilla {
	
	@Override
	public String getType() {
		// web, email, banner, search
		return "web";
	}

	@Override
	public String getVendor() {
		// ex. Web: japan-guide.com email: gmail 
		return "italianpenpals";
	}

	@Override
	public String getName() {
		// ex. WEB: site name: japan-guide | mailing: mailing template name
		return "http://italianpenpas.org";
	}

	@Override
	public String getItem() {
		return "penpals global list";
	}
}
