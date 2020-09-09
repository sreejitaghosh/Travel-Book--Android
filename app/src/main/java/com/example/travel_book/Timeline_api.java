package com.example.travel_book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Timeline_api
{
    @JsonProperty("Caption")
    private String[] Caption;
    @JsonProperty("photo_url")
    private String[] photo_url;
    @JsonProperty("experience")
    private String[] experience;
    @JsonProperty("hotel")
    private String[] hotel;
    @JsonProperty("flight")
    private String[] flight;
    @JsonProperty("visa")
    private String[] visa;
    @JsonProperty("from_location")
    private String[] from_location;
    @JsonProperty("to_location")
    private String[] to_location;

    public String[] getCaption() {
        return Caption;
    }

    public void setCaption(String[] caption) {
        Caption = caption;
    }

    public String[] getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String[] photo_url) {
        this.photo_url = photo_url;
    }

    public String[] getExperience() {
        return experience;
    }

    public void setExperience(String[] experience) {
        this.experience = experience;
    }

    public String[] getHotel() {
        return hotel;
    }

    public void setHotel(String[] hotel) {
        this.hotel = hotel;
    }

    public String[] getFlight() {
        return flight;
    }

    public void setFlight(String[] flight) {
        this.flight = flight;
    }

    public String[] getVisa() {
        return visa;
    }

    public void setVisa(String[] visa) {
        this.visa = visa;
    }

    public String[] getFrom_location() {
        return from_location;
    }

    public void setFrom_location(String[] from_location) {
        this.from_location = from_location;
    }

    public String[] getTo_location() {
        return to_location;
    }

    public void setTo_location(String[] to_location) {
        this.to_location = to_location;
    }
}
