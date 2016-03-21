package org.guildsa.top10downloader;

import java.util.ArrayList;

public class ParseApplications {

    // The xmlData field is the actual retrieved xml that we'll be parsing.
    private String xmlData;
    // Our 10 individual applications will be placed in this array list.
    private ArrayList<Application> applications;

    public ParseApplications(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<>();
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }
}
