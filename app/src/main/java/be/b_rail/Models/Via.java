package be.b_rail.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AMOTBT on 30/05/2016.
 */
public class Via {

    @SerializedName("timeBetween")
    private String timeBetween;
    @SerializedName("arrival")
    private Arrival arrival;
    @SerializedName("departure")
    private Departure departure;
    @SerializedName("station")
    private String station;
    @SerializedName("vehicle")
    private String vehicle;

    public String getTimeBetween() {
        return timeBetween;
    }

    public void setTimeBetween(String timeBetween) {
        this.timeBetween = timeBetween;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }
}
