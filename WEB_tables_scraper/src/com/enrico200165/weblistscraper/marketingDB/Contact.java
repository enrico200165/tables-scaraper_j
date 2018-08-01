package com.enrico200165.weblistscraper.marketingDB;

import com.enrico200165.weblistscraper.common.ContactType;
import com.enrico200165.weblistscraper.configs.ChannelIFC;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class Contact {

	static final String NON_SET_STRING = "non-impostato";

	public Contact() {
	}


	public Contact(ProspectABC prospect, String chType, String chVend,String chID,
				   String contactComm,String contMessage) {

		setProspectFK(prospect.it().getInternalID());

		// duplciating info from prospect to avoid perforning joins, no time yet to learn them
		setChannelID(prospect.it().getHomeID());
		// dovrebbe essere settato dopo che l'azione ha avuto successo
		this.contactDate = new Date();

		this.channelType = chType;
		this.channelVendor = chVend;
		this.channelID = chID;

		this.contactDescription = NON_SET_STRING;
		setProspectID(prospect.it().getUserID());
	}

	public Contact setChannelFields(ChannelIFC ch) {

		this.channelType = ch.getType();
		this.channelVendor = ch.getVendor();
		this.channelID = ch.getName();
		this.channelItem = ch.getItem();

		return this;
	}

	public String dump() {
		String ret = "";
		String sep = "; ";
		ret += "prospectFK="+getProspectFK()+sep;
		ret += "contactDate="+this.contactDate+sep;
		ret += "prospectID="+this.prospectID+sep;
		ret += "channelID="+this.channelID+sep;

		return ret;
	}

	public long getProspectFK() {
		return prospectFK;
	}

	public void setProspectFK(long prospectFK) {
		this.prospectFK = prospectFK;
	}

	public String getProspectID() {
		return prospectID;
	}

	public void setProspectID(String prospectID) {
		this.prospectID = prospectID;
	}

	public String getContactMail() {
		return contactMail;
	}

	public void setContactMail(String contactMail) {
		this.contactMail = contactMail;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getChannelVendor() {
		return channelVendor;
	}

	public void setChannelVendor(String channelVendor) {
		this.channelVendor = channelVendor;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getChannelItem() {
		return channelItem;
	}

	public void setChannelItem(String channelItem) {
		this.channelItem = channelItem;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getContacterIdentity() {
		return contacterIdentity;
	}

	public void setContacterIdentity(String contacterIdentity) {
		this.contacterIdentity = contacterIdentity;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public String getContactExecID() {
		return contactExecID;
	}

	public void setContactExecID(String contactExecID) {
		this.contactExecID = contactExecID;
	}

	public String getUserContactKey() {
		return userContactKey;
	}

	public void setUserContactKey(String userContactKey) {
		this.userContactKey = userContactKey;
	}

	public String getUserContactCommand() {
		return userContactCommand;
	}

	public void setUserContactCommand(String userContactCommand) {
		this.userContactCommand = userContactCommand;
	}

	public String getContactDescription() {
		return contactDescription;
	}

	public void setContactDescription(String contactDescription) {
		this.contactDescription = contactDescription;
	}

	public String getContactContent() {
		return contactContent;
	}

	public void setContactContent(String contactContent) {
		this.contactContent = contactContent;
	}

	public static String getNonSetString() {
		return NON_SET_STRING;
	}

	public Date getContactDate() {
		return this.contactDate;
	}


	public void setContactDate(Date contactDate) {
		this.contactDate = contactDate;
	}

	public Contact setContactDateNow() {
		setContactDate(new java.util.Date());
		return this;
	}

	public void setContactExecIDNumber(int n) {
		setContactExecID("" + n);
	}

	// --- ProspectCoreData FKs ---
	@Id
	@GeneratedValue
	long internalID;

	long prospectFK;

	Date contactDate;
	ContactType	contactType;


	@Index
	String prospectID;
	@Index
	String contactMail;

	// -- channel info ---
	String channelType; // web, email, banner, search
	String channelVendor; // ex. Web: japan-guide.com email: gmail
	String channelID; // ex. WEB: site name: japan-guide | mailing: mailing
						// template name
	String channelItem; // WEB: which user table, url of japan-guide

	// --- campaign info
	String campaignName;
	String contacterIdentity; // WEB: my username | mail: sender identity

	// --- contact info ---
	String contactExecID; // identify a marketing action, ex.a Unica Campaign
							// flowchart run,
	// WEB: execution of my sw | mail: mailing run ID
	String userContactKey; // sometimes the user ID not used by sites, ex. when
							// posting a site
	// message to "superMan" the post might use some OTHER strange id, this is
	// it
	String userContactCommand; // if you made a POST of a site message this
								// would contain the POST URL
	String contactDescription;
	String contactContent; // site message POSTED body, mail content
}