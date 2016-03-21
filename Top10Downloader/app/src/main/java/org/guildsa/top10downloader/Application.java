package org.guildsa.top10downloader;

// Here's our model class for a particular application within our Top 10 Applications List.
public class Application {
    // The fields don't necessarily have to correspond to the xml data retrieved from Apple but
    // for simplicity's sake, they will be in this case.

    private String name;
    private String artist;
    private String releaseDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
