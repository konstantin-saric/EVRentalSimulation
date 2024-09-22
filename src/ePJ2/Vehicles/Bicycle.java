package ePJ2.Vehicles;

import ePJ2.App;

import java.io.Serializable;


/**
 * Nasljedjuje Vehicle i predstavlja bicikl
 */
public class Bicycle extends Vehicle implements Serializable {
    
    private Integer range;

    public Bicycle(String[] vehicleData){
        super(vehicleData[0], vehicleData[1], vehicleData[2], Integer.valueOf(vehicleData[4]));
        this.range = Integer.valueOf(vehicleData[5]);
        this.repairPrice = this.price * Double.parseDouble(App.properties.getProperty("BIKE_REPAIR_FEE"));
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }
}
