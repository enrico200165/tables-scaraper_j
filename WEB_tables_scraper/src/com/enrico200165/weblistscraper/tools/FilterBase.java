package com.enrico200165.weblistscraper.tools;

import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

public class FilterBase {

	FilterBase(SessionManagerAbstr smpar) {
		reset();
		smgr = smpar;
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


	SessionManagerAbstr smgr;
	int nrTrueEntry;
	int nrFalseEntry;
	int nrTrueProspect;
	int nrFalseProspect;
}
