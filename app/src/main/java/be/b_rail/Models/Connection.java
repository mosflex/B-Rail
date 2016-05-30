package be.b_rail.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jawad & Moshab on 12-02-16.
 */
public class Connection {

    @SerializedName("id")
    private int         id;
    @SerializedName("duration")
    private int         duration;
    @SerializedName("departure")
    private Departure   departure;
    @SerializedName("arrival")
    private Arrival     arrival;
    @SerializedName("vias")
    private Vias vias;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public Vias getVias() {
        return this.vias;
    }

    public void setVias(Vias vias) {
        this.vias = vias;
    }
}
