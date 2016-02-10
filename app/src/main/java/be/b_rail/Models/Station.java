package be.b_rail.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jawad on 09-02-16.
 */
public class Station {

    private long _id;
    @SerializedName("id")
    private String idStation;
    @SerializedName("name")
    private String name;


    /**************************************************/
    public Station(){}

    public Station(long _id,String idStation, String name  ){
        this._id = _id;
        this.idStation = idStation;
        this.name = name;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = _id;
    }

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
