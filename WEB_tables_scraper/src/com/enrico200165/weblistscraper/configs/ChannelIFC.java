package com.enrico200165.weblistscraper.configs;

/**
 * @author enrico
 * Marketing channel attributes
 * In main config because our simple tool always works within a single channel 
 */
public interface ChannelIFC {

	String getType();
	void   setType(String v);

	String getVendor();
	void   setVendor(String v);

	String getName();
	void   setName(String v);

	String getItem();
	void   setItem(String v);

}