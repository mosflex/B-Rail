package be.b_rail.Models;

/**
 * Created by Jawad on 09-02-16.
 */
public class Station {

    private long id;
    private long idStation;
    private String name;


    /**************************************************/
    public Station(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdStation() {
        return idStation;
    }

    public void setIdStation(long idStation) {
        this.idStation = idStation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
