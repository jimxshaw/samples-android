package org.guildsa.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
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

    public boolean process() {
        /*
            Apple's Top 10 Free Applications XML link:
                http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml
            XML beautify link:
                http://codebeautify.org/xmlviewer

            In order to understand how this procress method is actually parsing Apple's XML link,
            we have to clearly see how the XML data is laid out. Copy the XML link in to the codebeautify
            link to see better formatted XML.

            Each application in the top 10 applications list is nested in its own entry tag. Within
            entry, there are tags that corresponds to the fields in our Application model class.
        */

        boolean status = true;
        Application currentRecord;
        boolean inEntry = false; // Variable telling us if we're inside an entry tag.
        String textValue = "";

        try {
            // This class is used to create implementations of XML Pull Parser defined
            // in XMPULL V1 API.
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // Specifies that the parser produced by this factory will provide support for XML
            // namespaces. By default the value of this is set to false.
            factory.setNamespaceAware(true);
            // Use the factory to instantiate an XML Pull Parser.
            XmlPullParser xpp = factory.newPullParser();
            // The setInput method must take in an argument of type Reader so we have to use the
            // StringReader class on our String xmlData.
            xpp.setInput(new StringReader(this.xmlData));

            // Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.).
            int eventType = xpp.getEventType();

            // Keep looping through the file until we reach the end of the file.
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();

                switch(eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d("ParseApplication", "Starting tag for " + tagName);
                        if (tagName.equalsIgnoreCase("entry")) {
                            inEntry = true;
                            currentRecord = new Application();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d("ParseApplication", "Ending tag for " + tagName);
                        break;
                    default:
                        // Nothing else to do.
                }
                // Get next parsing event and will continue parsing until the end of the document.
                eventType = xpp.next();
            }
        }
        catch(Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}
