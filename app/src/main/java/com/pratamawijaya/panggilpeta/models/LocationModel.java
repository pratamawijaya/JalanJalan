package com.pratamawijaya.panggilpeta.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by pratama on 2/18/15.
 */
public class LocationModel extends RealmObject {
    @PrimaryKey
    private String id;
    private String asal;
    private String tujuan;
    private String distance;
    private String duration;
    private RealmList<LatLngModel> latLngRealmList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsal() {
        return asal;
    }

    public void setAsal(String asal) {
        this.asal = asal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public RealmList<LatLngModel> getLatLngRealmList() {
        return latLngRealmList;
    }

    public void setLatLngRealmList(RealmList<LatLngModel> latLngRealmList) {
        this.latLngRealmList = latLngRealmList;
    }
}
