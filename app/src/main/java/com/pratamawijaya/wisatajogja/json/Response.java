package com.pratamawijaya.wisatajogja.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pratama on 2/18/15.
 */
public class Response {
    @SerializedName("routes")
    private List<Route> routeList;

    public List<Route> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
    }
}
