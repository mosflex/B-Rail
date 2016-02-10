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

    private CharSequence nameHtml;


    /**************************************************/
    public Station(){}

    public Station(String idStation, String name){
        this.idStation = idStation;
        this.name = name;
    }

    public Station(Station station){
        this.idStation = station.getIdStation();
        this.name = station.getName();
        this.nameHtml = station.getNameHtml();
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

    public CharSequence getNameHtml() {
        return nameHtml;
    }
    public void setNameHtml(CharSequence nameHtml) {
        this.nameHtml = nameHtml;
    }
}
