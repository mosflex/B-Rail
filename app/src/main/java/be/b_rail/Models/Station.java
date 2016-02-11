package be.b_rail.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jawad on 09-02-16.
 */
public class Station {


    private long _id;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    private CharSequence nameHtml;


    /**************************************************/

    public Station(Station station){
        this._id = station.get_Id();
        this.name = station.getName();
        this.nameHtml = station.getNameHtml();
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

    public CharSequence getNameHtml() {
        return nameHtml;
    }
    public void setNameHtml(CharSequence nameHtml) {
        this.nameHtml = nameHtml;
    }
}
