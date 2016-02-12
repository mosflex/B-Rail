package be.b_rail.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jawad on 12-02-16.
 */
public class PlatformInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("normal")
    private String normal;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNormal() {
        return normal;
    }
    public void setNormal(String normal) {
        this.name = normal;
    }
}
