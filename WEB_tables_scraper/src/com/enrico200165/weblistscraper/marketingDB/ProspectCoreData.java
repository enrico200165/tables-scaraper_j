package com.enrico200165.weblistscraper.marketingDB;


import com.enrico200165.weblistscraper.entities.EntityBase;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;



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
@Entity
@Table(indexes = {@Index(name = "USER_ID",  columnList="userID", unique = true) })
public class ProspectCoreData extends EntityBase  {

    public ProspectCoreData() {
    }

    public long getInternalID() {
        return internalID;
    }
    /*
    public void setInternalID(long internalID) {
        this.internalID = internalID;
    }
*/

    public String getFirstName() {
        return firstName;
    }


    public void setFistName(String fistName) {
        this.firstName = fistName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getSex() {
        return sex;
    }


    public void setSex(String sex) {
        this.sex = sex;
    }


    public int getAge() {
        return age;
    }


    public void setAge(int age) {
        this.age = age;
    }


    public String getMotherCountry() {
        return motherCountry;
    }


    public void setMotherCountry(String motherCountry) {
        this.motherCountry = motherCountry;
    }


    public String getMotherTongue() {
        return motherTongue;
    }


    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }


    public String getCountryOfResidence() {
        return countryOfResidence;
    }


    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }


    public String getLanguageLearning() {
        return languageLearning;
    }


    public void setLanguageLearning(String languageLearning) {
        this.languageLearning = languageLearning;
    }


    public String getCountryLearning() {
        return countryLearning;
    }


    public void setCountryLearning(String countryLearning) {
        this.countryLearning = countryLearning;
    }


    public java.sql.Timestamp getProspectRecordedByMe() {
        return prospectRecordedByMe;
    }


    public void setProspectRecordedByMe(java.sql.Timestamp prospectRecordedByMe) {
        this.prospectRecordedByMe = prospectRecordedByMe;
    }


    public String dump() {
        String ret = "";
        String eq = " = ";
        String sep = " ";
        ret += "internalID"+eq+ getInternalID();
        ret += "userID"+eq+ getUserID();
        ret += sep + "score"+eq+getScoreActOnThis();
        ret += sep + "age"+eq+getAge();
        return ret;
    }


    public String getMainEmail() {
        return mainEmail;
    }


    public void setMainEmail(String mainEmail) {
        this.mainEmail = mainEmail;
    }


    public String getProfile() {
        return profile;
    }


    public void setProfile(String profile) {
        this.profile = profile;
    }


    public String getMyAnnotations() {
        return myAnnotations;
    }


    public void setMyAnnotations(String myAnnotations) {
        this.myAnnotations = myAnnotations;
    }


    public String getUserID() {
        return userID;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getAdOrProfileEntryUrl() {
        return adOrProfileEntryUrl;
    }


    public void setAdOrProfileEntryUrl(String adOrProfileEntryUrl) {
        this.adOrProfileEntryUrl = adOrProfileEntryUrl;
    }


    public String getPrivateMessageURL() {
        return privateMessageURL;
    }


    public void setPrivateMessageURL(String privateMessageURL) {
        this.privateMessageURL = privateMessageURL;
    }


    public ProspectCoreData setProspectRecordwsTStampNow() {
        setProspectRecordedTStamp(new java.util.Date());
        return this;
    }

    public ProspectCoreData setProspectRecordedTStamp(java.util.Date jd) {
        this.prospectRecordedByMe = new Timestamp(jd.getTime());
        return this;
    }

    // only for logging
    public String IDsToString() {
        String s = "";
        s += (this.mainEmail != null) ? (this.mainEmail) : "";
        s += (this.userID != null) ? (" " + this.userID) : "";
        return s;
    }


    public boolean isFemale() {
        return getSex().toLowerCase().contains("f");
    }


    public boolean profileContainsItalian() {
        if (getProfile() == null)
            return false;

        String profile = getProfile().toLowerCase();
        String[] itWords = {"sono", "studio", "impar", "viaggi", "abit"};

        int nrParole = 0;
        for (String word : itWords) {
            if (profile.contains(word)) {
                nrParole++;
            }
        }
        return nrParole >= 3;
    }


    public boolean profileContainsEnglish() {
        if (getProfile() == null)
            return false;

        String profile = getProfile().toLowerCase();
        String[] words = {" I "," am ","friend", "study", "learn"," would" ,"like", "speak", "interest"};

        int nrParole = 0;
        for (String word : words) {
            if (profile.contains(word)) {
                nrParole++;
            }
        }
        return nrParole >= 3;
    }




    public boolean store(DBManagerMKT ch, java.util.Date creationDate, boolean skipIfPresent, boolean commitNow) {
        return ch.storeOneProspect(this, creationDate, skipIfPresent, commitNow);
    }


    public String getHobbiesList() {
        return hobbiesList;
    }

    public void setHobbiesList(String hobbiesList) {
        this.hobbiesList = hobbiesList;
    }

    public Date getPostedOnHome() {
        return postedOnHome;
    }

    public void setPostedOnHome(java.util.Date d) {
        this.postedOnHome = new Date(d.getTime());
    }

    public String getHomeID() {
        return homeID;
    }

    public void setHomeID(String homeID) {
        this.homeID = homeID;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public long getScoreActOnThis() {
        return scoreActOnThis;
    }

    public void setScoreActOnThis(long scoreActOnThis) {
        this.scoreActOnThis = scoreActOnThis;
    }

    public void setPostedOnHome(Date postedOnHome) {
        this.postedOnHome = postedOnHome;
    }


    // ### user info ###
    @Id
    @GeneratedValue
    long internalID;

    @Column
    String lastName;

    @Column
    String firstName;

    @Column(length = 1)
    String sex;
    int age;
    String motherCountry;
    String motherTongue;
    java.sql.Timestamp prospectRecordedByMe;

    // --- ID canale (il sito Web o l'app dove lo abbiamo trovato
    String homeID;
    String channelType; // web, altro, manual

    // --- info addizionali ---
    String hobbiesList; // fior simplicity packed in a comma separated
    String countryOfResidence;
    String languageLearning;
    String countryLearning;
    @Column(length = 4096)
    String profile; // self description of the user on a web site, if any

    long scoreActOnThis;
    // ex. when you can message no more than N prospects per day you choose the N that have this bigger

    // --- email
    String mainEmail; // this is also account/channel info

    // --- parte specifica WEB ---
    String userID;
    String adOrProfileEntryUrl; // day they posted their ad or profile
    String privateMessageURL;
    java.sql.Date postedOnHome; // when an ad was posted or a profile created ON the home site, not in our list

    // eventually may contain several different pieces of info packed into one
    String myAnnotations;
    // ### channel info forced into profile not to multiply classes/tables ###

}