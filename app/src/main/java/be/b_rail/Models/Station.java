package be.b_rail.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jawad & Moshab on 09-02-16.
 */
public class Station {


    private long _id;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("locationX")
    private String locationX;

    @SerializedName("locationY")
    private String locationY;

    @SerializedName("standardname")
    private String standardname;


    /**************************************************/

    public Station(Station station){
        this._id = station.get_Id();
        this.name = station.getName();

    }

    public long get_Id() {
        return _id;
    }
    public void set_Id(long _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getStandardname() {
        return standardname;
    }

    public void setStandardname(String standardname) {
        this.standardname = standardname;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }
}
