package ePJ2.Vehicles;

import ePJ2.App;

import java.io.Serializable;

public class Scooter extends Vehicle implements Serializable {
    
    private Integer maxSpeed;

    public Scooter(String[] vehicleData){
        super(vehicleData[0], vehicleData[1], vehicleData[2], Integer.valueOf(vehicleData[4]));
        this.maxSpeed = Integer.valueOf(vehicleData[6]);
        this.repairPrice = this.price * Double.parseDouble(App.properties.getProperty("SCOOTER_REPAIR_FEE"));
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
            String string = "ID: " + this.ID + " Model: " + this.model + " Max Speed: " + this.maxSpeed;
            return string;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
