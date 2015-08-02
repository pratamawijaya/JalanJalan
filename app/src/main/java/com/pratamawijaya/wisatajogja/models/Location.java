package com.pratamawijaya.wisatajogja.models;

/**
 * Created by pratama on 2/18/15.
 */
public class Location {
  private String name;
  private double lat;
  private double lng;

  public Location(String name, double lat, double lng) {
    this.name = name;
    this.lat = lat;
    this.lng = lng;
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

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  @Override public String toString() {
    return name;
  }

  @Override public int hashCode() {
    return super.hashCode();
  }
}
