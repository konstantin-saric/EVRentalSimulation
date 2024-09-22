package ePJ2.Vehicles;

import ePJ2.App;

import java.io.Serializable;

/**
 * Nasljedjuje Vehicle i predstavlja trotinet
 */
public class Scooter extends Vehicle implements Serializable {
    
    private Integer maxSpeed;

    public Scooter(String[] vehicleData){
        super(vehicleData[0], vehicleData[1], vehicleData[2], Integer.valueOf(vehicleData[4]));
        this.maxSpeed = Integer.valueOf(vehicleData[6]);
        this.repairPrice = this.price * Double.parseDouble(App.properties.getProperty("SCOOTER_REPAIR_FEE"));
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
