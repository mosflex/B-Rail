package be.b_rail.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jawad & Moshab on 12-02-16.
 */
public class Departure {
    @SerializedName("delay")
    private int delay;

    @SerializedName("station")
    private String station;

    @SerializedName("stationinfo")
    private Station stationinfo;

    @SerializedName("time")
    private String time;

    @SerializedName("vehicle")
    private String vehicle;

    @SerializedName("platform")
    private String platform;

    @SerializedName("platforminfo")
    private PlatformInfo platforminfo;

    @SerializedName("canceled")
    private int canceled;

    @SerializedName("direction")
    private Direction direction;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Station getStationinfo() {
        return stationinfo;
    }

    public void setStationinfo(Station stationinfo) {
        this.stationinfo = stationinfo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public PlatformInfo getPlatforminfo() {
        return platforminfo;
    }

    public void setPlatforminfo(PlatformInfo platforminfo) {
        this.platforminfo = platforminfo;
    }

    public int getCanceled() {
        return canceled;
    }

    public void setCanceled(int canceled) {
        this.canceled = canceled;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


}
