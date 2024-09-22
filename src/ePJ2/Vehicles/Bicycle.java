package ePJ2.Vehicles;

import ePJ2.App;

import java.io.Serializable;

public class Bicycle extends Vehicle implements Serializable {
    
    private Integer range;

    public Bicycle(String[] vehicleData){
        super(vehicleData[0], vehicleData[1], vehicleData[2], Integer.valueOf(vehicleData[4]));
        this.range = Integer.valueOf(vehicleData[5]);
        this.repairPrice = this.price * Double.parseDouble(App.properties.getProperty("BIKE_REPAIR_FEE"));
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
            String string = "ID: " + this.ID + " Model: " + this.model + " Range: " + this.range;
            return string;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }
}
