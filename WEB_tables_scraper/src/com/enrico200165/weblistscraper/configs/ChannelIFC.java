package com.enrico200165.weblistscraper.configs;

/**
 * @author enrico
 * Marketing channel attributes
 * In main config because our simple tool always works within a single channel 
 */
public interface ChannelIFC {

	String getChannelType();

	String getChannelVendor();

	String getChannelName();

	String getChannelItem();

}