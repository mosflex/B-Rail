package be.b_rail.Models;

/**
 * Created by Moshab on 26-06-16.
 */
public class Favourite {

    public Favourite(String departure, String arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String departure;
    public String arrival;

}
