package me.jimmyshaw.codingbootcampfinder.services;

import java.util.ArrayList;

import me.jimmyshaw.codingbootcampfinder.models.Camp;

public class DataService {

    private static DataService sDataService = new DataService();

    public static DataService getInstance() {
        return sDataService;
    }

    private DataService() {

    }

    public ArrayList<Camp> GetCampLocationsWithinTenMilesOfZipCode(int zipCode) {
        // Pretending to download data from a server.
        ArrayList<Camp> camps = new ArrayList<>();
        camps.add(new Camp(37.783697f, -122.408966f, "Hack Reactor", "944 Market St, San Francisco, CA 94102", "sof"));
        camps.add(new Camp(37.784517f, -122.397194f, "Devbootcamp", "633 Folsom St, San Francisco, CA 94107", "sof"));
        camps.add(new Camp(37.808544f, -122.253681f, "Coder Camps", "401 Grand Ave Suite 450, Oakland, CA 94610", "sof"));

        return camps;
    }
}
