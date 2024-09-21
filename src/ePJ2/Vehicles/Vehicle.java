package ePJ2.Vehicles;

import javafx.geometry.Point2D;

public abstract class Vehicle {

    protected String ID;
    protected String manufacturer;
    protected String model;
    protected Integer price;
    protected Integer batteryLevel;
    protected Boolean inUse;

    public Vehicle(String ID, String manufacturer, String model, Integer price){
        this.ID = ID;
        this.manufacturer = manufacturer;
        this.model = model;
        this.batteryLevel = 100;
        this.price = price;
        this.inUse = false;
    }

    public void discharge(){
        batteryLevel--;
    }

    public void recharge(){
        if(batteryLevel < 100)
            batteryLevel++;
    }

    public void rechargeFull(){
        batteryLevel = 100;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
            String string = "ID: " + this.ID + " Model: " + this.model + " Price: " + this.price;
            return string;
    }


    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Boolean isInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }
}
