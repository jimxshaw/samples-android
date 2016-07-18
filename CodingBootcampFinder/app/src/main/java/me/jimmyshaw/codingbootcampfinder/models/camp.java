package me.jimmyshaw.codingbootcampfinder.models;

public class Camp {

    private float mLatitude;
    private float mLongitude;
    private String mLocationTitle;
    private String mLocationAddress;
    private String mLocationImgUrl;

    public Camp(float latitude, float longitude, String locationTitle, String locationAddress, String locationImgUrl) {
        mLatitude = latitude;
        mLongitude = longitude;
        mLocationTitle = locationTitle;
        mLocationAddress = locationAddress;
        mLocationImgUrl = locationImgUrl;
    }

    public String getLocationTitle() {
        return mLocationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.mLocationTitle = locationTitle;
    }

    public String getLocationImgUrl() {
        return mLocationImgUrl;
    }

    public void setLocationImgUrl(String locationImgUrl) {
        this.mLocationImgUrl = locationImgUrl;
    }

    public String getLocationAddress() {
        return mLocationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.mLocationAddress = locationAddress;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        this.mLongitude = longitude;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        this.mLatitude = latitude;
    }


}
