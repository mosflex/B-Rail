package be.ontime.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jawad on 12-02-16.
 */
public class Direction {

    @SerializedName("name")
    private String name;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
