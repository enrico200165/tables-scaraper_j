package com.enrico200165.weblistscraper.configs;

public class ChannelConfigVanilla implements ChannelIFC {


    // web, email, banner, search
	@Override public String getType() { return this.type; }
    @Override public void setType(String channel) { this.type = channel;  }

    // ex. Web: japan-guide.com email: gmail
    @Override public String getVendor() { return this.vendor;}
    @Override public void setVendor(String vendor) { this.vendor = vendor; }


    // ex. WEB: site name: japan-guide | mailing: mailing template name
    @Override public String getName() { return this.name; }
    @Override public void setName(String name) { this.name = name; }


	@Override public String getItem() {
		return this.item;
	}
    @Override public void setItem(String item) {  this.item = item; }

	String type;
	String vendor;
	String name;
	String item;
}
