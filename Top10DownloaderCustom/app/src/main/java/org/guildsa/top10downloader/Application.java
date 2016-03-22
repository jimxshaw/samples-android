package org.guildsa.top10downloader;

// Here's our model class for a particular application within our Top 10 Applications List.
public class Application {
    // The fields don't necessarily have to correspond to the xml data retrieved from Apple but
    // for simplicity's sake, they will be in this case.

    private String name;
    private String artist;
    private String releaseDate;
    private String imageUrl;

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

    public String getImage() { return imageUrl; }

    public void setImage(String image) { this.imageUrl = image; }

    // In order to display each list item within our list view the way we want it, we must
    // override the .toString method. When the Application class gets used in any of our
    // layout views, this particular .toString method will automatically called as opposed to
    // the default .toString that's attached to all Java objects.
    @Override
    public String toString() {
        return "Name: " + getName() + "\n" +
                "Artist: " + getArtist() + "\n" +
                "Release Date: " + getReleaseDate().substring(0, 10);
    }
}
