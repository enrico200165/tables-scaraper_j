package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.marketingDB.*;

public class ProspectJapanguide extends ProspectABC {


    public ProspectJapanguide(ProspectCoreData prospectCoreData) {
        super(prospectCoreData);
    }

    public long updateAndGetScore() {

        long score = this.prospectCoreData.getScoreActOnThis();

        // --- age
        java.util.Date now = new java.util.Date();
        java.util.Date prospectRecorded = null;
        if (prospectCoreData.getProspectRecordedByMe() != null) {
            prospectRecorded = new java.util.Date(prospectCoreData.getProspectRecordedByMe().getTime());
        } else {
            prospectRecorded = now;
        }
        long recordedDaysAgo = (now.getTime() - prospectRecorded.getTime()) / 1000 / (3600 * 24);

        score -= recordedDaysAgo * 1000;

        // --- interests

        // --- sex
        if (prospectCoreData.isFemale())
            score += 3 * 1000;

        if (prospectCoreData.profileContainsItalian())
            score += 5 * 1000;

        if (prospectCoreData.profileContainsEnglish())
            score += 3 * 1000;

        // hobbies & interests
        String hobbies = prospectCoreData.getHobbiesList();
        if (hobbies != null && hobbies.toLowerCase().contains("travel"))
            score += 3 * 1000;


        this.prospectCoreData.setScoreActOnThis(score);

        return prospectCoreData.getScoreActOnThis();
    }
}
