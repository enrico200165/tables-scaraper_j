package com.enrico200165.weblistscraper.configs;


import org.apache.log4j.Logger;

public class SessionLimitsBase {

	public SessionLimitsBase() {
		reset();
	}

	public void reset() {
		this.reads = 0;
		this.saves = 0;
		this.contacts = 0;
		this.processed = 0;
	}

	public int getReads() {
		return reads;
	}

	public void setReads(int reads) {
		this.reads = reads;
	}

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}

	public int getSaves() {
		return saves;
	}

	public void setSaves(int saves) {
		this.saves = saves;
	}

	public int getContacts() {
		return contacts;
	}

	public void setContacts(int contacts) {
		this.contacts = contacts;
	}


	public boolean canReadEntry(boolean preIncrement) {
		if (preIncrement)
			this.reads++;
		return (getMaxEntriesRead() == 0) || (this.reads <= getMaxEntriesRead());
	}

	public boolean canSaveProspect(boolean preIncrement) {
		if (preIncrement)
			this.saves++;
		return (getMaxNewProspectsSaves() == 0) || (this.saves <= getMaxNewProspectsSaves());
	}

	public boolean canContactEntry() {
		return (getMaxContactsExec() == 0) || (this.contacts < getMaxContactsExec());
	}


	public void contactNrIncr() {
		this.contacts++;
	}

	public String toString() {
		return "max reads=" + getMaxEntriesRead() + " max saves=" + getMaxNewProspectsSaves()
				+ " max contacts=" + getMaxContactsExec()
				+ " reads=" + this.reads + " saves=" + this.saves + " contacts=" + this.contacts;
	}


	public int getMaxEntriesRead() {
		return this.maxEntriesRead;
	}

	public void setMaxEntriesRead(int v) {
		this.maxEntriesRead = v;
	}

	public int getMaxProcess() {
		return this.maxProcess;
	}

	public void setMaxProcess(int v) {
		this.maxProcess = v;
	}

	public int getMaxNewProspectsSaves() {
		return this.maxNewProspectsSaves;
	}

	public void setMaxNewProspectsSaves(int v) {
		this.maxNewProspectsSaves = v;
	}

	public int getMaxContactsExec() {
		return this.maxContactsExec;
	}

	public void setMaxContactsExec(int v) {
		this.maxContactsExec = v;
	}

	public int getMaxHTTPCalls() {
		return this.maxHTTPCalls;
	}

	public void setMaxHTTPCalls(int v) {
		this.maxHTTPCalls = v;
	}

	int maxEntriesRead;
	int maxProcess;
	int maxNewProspectsSaves;
	int maxContactsExec;
	int maxHTTPCalls;


	int processed;
	int saves;
	int contacts;
	int reads;

	private static Logger log = Logger.getLogger(SessionLimitsBase.class);
}