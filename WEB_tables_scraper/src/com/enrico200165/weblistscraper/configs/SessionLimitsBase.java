package com.enrico200165.weblistscraper.configs;

 
public abstract class SessionLimitsBase {
	
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

	abstract public int maxEntriesRead();
	abstract public int maxProcess();
	abstract public int maxNewProspectsSaves();
	abstract public int maxContactsExec();
	abstract public int maxHTTPCalls();
	
	public boolean canReadEntry(boolean preIncrement) {
		if (preIncrement)
			this.reads++;
		return (maxEntriesRead() == 0) || (this.reads <= maxEntriesRead()); 
	}
	
	public boolean canSaveProspect(boolean preIncrement) {
		if (preIncrement)
			this.saves++;
		return (maxNewProspectsSaves() == 0) || (this.saves <= maxNewProspectsSaves()); 
	}

	public boolean canContactEntry() {
		return (maxContactsExec() == 0) || (this.contacts < maxContactsExec()); 
	}
	
	
	public void contactNrIncr() {
		this.contacts++;
	}
	
	public String toString() {
		return "max reads="+maxEntriesRead()+" max saves="+maxNewProspectsSaves()+" max contacts="+maxContactsExec()
	+ " reads="+this.reads+" saves="+this.saves+" contacts="+this.contacts;
	}
		
	int reads;
	int processed;
	int saves;
	int contacts;
}
