package com.example.tele_clima_20125424.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class CityDTO {
    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;
    private String countryFlagEmoji;

    public String getCountryFlagEmoji() {
        return countryFlagEmoji;
    }

    public void setCountryFlagEmoji(String countryFlagEmoji) {
        this.countryFlagEmoji = countryFlagEmoji;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}


