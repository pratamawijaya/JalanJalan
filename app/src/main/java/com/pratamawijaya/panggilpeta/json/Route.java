package com.pratamawijaya.panggilpeta.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pratama on 2/18/15.
 */
public class Route {
    @SerializedName("legs")
    private List<Legs> legsList;

    public List<Legs> getLegsList() {
        return legsList;
    }

    public void setLegsList(List<Legs> legsList) {
        this.legsList = legsList;
    }
}
