package com.pratamawijaya.wisatajogja.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pratama on 2/18/15.
 */

public class Legs {
    @SerializedName("steps")
    private List<Steps> stepsList;

    @SerializedName("distance")
    private Distance distance;

    @SerializedName("duration")
    private Distance duration;

    @SerializedName("end_address")
    private String end_address;

    @SerializedName("start_address")
    private String start_address;

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public Distance getDistance() {
        return distance;
    }

    public Distance getDuration() {
        return duration;
    }

    public void setDuration(Distance duration) {
        this.duration = duration;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }
}
