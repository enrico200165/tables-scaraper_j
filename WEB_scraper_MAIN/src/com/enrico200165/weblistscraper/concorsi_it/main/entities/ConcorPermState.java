package com.enrico200165.weblistscraper.concorsi_it.main.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;


import com.enrico200165.weblistscraper.entities.EntityBase;


/**
 * Models the basic common fields of a project that will be stored on DB Every Web site or channel will have a different "ProspectCoreData", with
 * different fields, that will be modeled by another class, *probably* - derived by this to provide polimorphism - probably with all fields
 * transient and including an objetc of this class, to use, manually the persistence of the basic prospect (and aboid getting into the
 * complexity of the persistence of class and superclass Etc.
 * 
 * @author enrico
 *
 */

@Entity
public class ConcorPermState extends EntityBase {

	public ConcorPermState() {
	}

	// --- Indirizzo ---
	@Id
	int dummyID;
	public int getDummyID() {
		return dummyID;
	}
	public void setDummyID(int dummyID) {
		this.dummyID = dummyID;
	}
	public Date getReportExtrlastRun() {
		return reportExtrlastRun;
	}
	public void setReportExtrlastRun(Date reportExtrlastRun) {
		this.reportExtrlastRun = reportExtrlastRun;
	}
	public long getReportExtrRunNumber() {
		return reportExtrRunNumber;
	}
	public void setReportExtrRunNumber(long reportExtrRunNumber) {
		this.reportExtrRunNumber = reportExtrRunNumber;
	}

	Date reportExtrlastRun;
	long reportExtrRunNumber;	
}