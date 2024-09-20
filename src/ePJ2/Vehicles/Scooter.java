package ePJ2.Vehicles;

public class Scooter extends Vehicle{
    
    private Integer maxSpeed;

    public Scooter(String[] vehicleData){
        super(vehicleData[0], vehicleData[1], vehicleData[2], Integer.valueOf(vehicleData[4]));
        this.maxSpeed = Integer.valueOf(vehicleData[6]);
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
