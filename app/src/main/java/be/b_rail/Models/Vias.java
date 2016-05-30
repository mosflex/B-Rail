package be.b_rail.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by AMOTBT on 30/05/2016.
 */
public class Vias {

    @SerializedName("number")
    private String number;
    @SerializedName("via")
    public ArrayList<Via> via;

    public ArrayList<Via> getVia() {
        return via;
    }

    public void setVia(ArrayList<Via> via) {
        this.via = via;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }





}
