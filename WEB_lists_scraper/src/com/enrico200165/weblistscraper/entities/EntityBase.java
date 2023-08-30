package com.enrico200165.weblistscraper.entities;

import javax.persistence.Transient;


/**
 * Models the basic common fields of a project that will be stored on DB
 * Every Web site or channel will have a different "ProspectCoreData", with different fields,
 * that will  be modeled by another class, *probably*
 * - derived by this to provide polimorphism
 * - probably with all fields transient and including an objetc of this class, to use, manually
 * the persistence of the basic prospect (and aboid getting into the complexity of the persistence
 * of class and superclass Etc.
 *
 * @author enrico
 */
//@Data
// @Entity
public class EntityBase {

    public EntityBase() {
        excluded = false;
    }

    public enum From {
        NOT_SET, CONTACT_HISTORY, WEB_SCRAPING
    }

    static final String NON_SET_STRING = "non-impostato";

    public boolean fromDB() {
        return this.from == From.CONTACT_HISTORY;
    }

    public void setFrom(From valPar) {
        this.from = valPar;
    }


    public void setExcluded(boolean par) {
        this.excluded = par;
    }

    @Transient
    From from = From.NOT_SET; // 0 DB, 1 Web

    @Transient
    boolean excluded;

    public From getFrom() {
        return from;
    }

    public boolean isExcluded() {
        return excluded;
    }


    public void addComment(String comment, String prefixSep, String postSep) {
        if (prefixSep != null) this.processingComments += prefixSep;
        this.processingComments += comment;
        if (postSep != null) this.processingComments += postSep;
    }

    public void addComment(String comment) {
        if (this.processingComments != null && this.processingComments.length() > 0) {
            addComment(comment, " - ", null); // prefix passed as comment
        }
        else {
            this.processingComments = comment; // prefix passed as comment
        }
    }


    String processingComments;
    // for explicative messages, specific to the entry, eventually inserted by processing code.
    // Ex. to explain explain it was "excluded" ie. "matched temporary work"
}