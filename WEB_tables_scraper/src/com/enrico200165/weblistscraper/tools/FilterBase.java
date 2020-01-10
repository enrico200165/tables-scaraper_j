package com.enrico200165.weblistscraper.tools;

import org.apache.log4j.Logger;

public class FilterBase {

	FilterBase() {
		reset();
	}

	void reset() {
		nrFalseProspect = 0;
		nrTrueProspect = 0;
	}

	public int nrTrueProspect() {
		return nrTrueProspect;
	}

	public int nrFalseProspect() {
		return this.nrFalseProspect;
	}

	FilterBase incrTrueProspect() {
		nrTrueProspect++;
		return this;
	}

	FilterBase incrFalseProspect() {
		nrFalseProspect++;
		return this;
	}



	// --- entry ---
	FilterBase incrTrueEntry() {
		nrTrueEntry++;
		return this;
	}
	FilterBase incrFalseEntry() {
		nrFalseEntry++;
		return this;
	}

	public int nrTrueEntry() {
		return nrTrueEntry;
	}

	public int nrFalseEntry() {
		return this.nrFalseEntry;
	}

	int nrTrueEntry;
	int nrFalseEntry;
	int nrTrueProspect;
	int nrFalseProspect;

	private static Logger log = Logger.getLogger(FilterBase.class);
}
