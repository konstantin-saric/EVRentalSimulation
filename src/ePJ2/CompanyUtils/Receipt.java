package ePJ2.CompanyUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import ePJ2.Rental.Rental;
import ePJ2.Vehicles.Bicycle;
import ePJ2.Vehicles.Car;
import ePJ2.Vehicles.Scooter;
import ePJ2.Vehicles.Vehicle;


public class Receipt {
    private LocalDateTime date;
    private String user;
    private String vehicleID;
    private Vehicle rentedVehicle;
    private String startingLocation;
    private String destLocation;
    private Integer duration;
    private Boolean distanceWide;
    private Boolean malfunction;
    private Boolean promotion;
    private Integer driverLicense;
    private Double basePrice;
    private Double price;
    private Integer receiptNumber;

    private static Integer rentalNumber = 0;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");

    public Receipt(Rental rental){
        Receipt.rentalNumber++;
        receiptNumber = rentalNumber;

        distanceWide = rental.isDistanceWide();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String propPath = rootPath + "app.properties";
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream(propPath));
        }
        catch(IOException e){
            e.printStackTrace();
        }

        try{
            this.date = LocalDateTime.parse(rental.getDate(), dtf);
        }
        catch(DateTimeParseException e){
            e.printStackTrace();
        }

        this.user = rental.getUser();
        this.vehicleID = rental.getVehicleID();
        this.rentedVehicle = rental.getRentedVehicle();
        this.startingLocation = rental.getStartingLocStr();
        this.destLocation = rental.getDestLocStr();
        this.duration = rental.getDuration();
        this.malfunction = rental.isMalfunction();
        this.promotion = rental.isPromotion();

        Random rando = new Random();
        driverLicense = 10000000 + rando.nextInt(90000000);

        if(malfunction){
            price = 0.0;
            basePrice = 0.0;
        }
        else{
            if(rentedVehicle instanceof Car){
                basePrice = duration * Double.parseDouble(properties.getProperty("CAR_UNIT_PRICE"));
            }
            else if(rentedVehicle instanceof Bicycle){
                basePrice = duration * Double.parseDouble(properties.getProperty("BIKE_UNIT_PRICE"));
            }
            else if(rentedVehicle instanceof Scooter){
                basePrice = duration * Double.parseDouble(properties.getProperty("SCOOTER_UNIT_PRICE"));
            }

            basePrice *= distanceWide?Double.parseDouble(properties.getProperty("DISTANCE_WIDE")):1;

            price = basePrice - (promotion?Double.parseDouble(properties.getProperty("DISCOUNT_PROM"))*basePrice:0)
                              - (receiptNumber%10==0?Double.parseDouble(properties.getProperty("DISCOUNT"))*basePrice:0);
        }


    }

    @Override
    public String toString(){
        String str = "//////////////////////////////////////////\n";
        str += "User name: " + this.user + "\n";
        str += "Driver licence number: " + this.driverLicense + "\n";
        str += "Date of rental: " + dtf.format(date) + "\n";
        str += "Rented vehicle ID: " + this.vehicleID + "\n";
        str += "Starting location: " + this.startingLocation + "\n";
        str += "Destination location: " + this.destLocation + "\n";
        str += "Promotion active?  " + (this.promotion?"Yes":"No") + "\n";
        str += "Vehicle malfunction? " + (this.malfunction?"Yes":"No") + "\n";
        str += "Subtotal: " + this.basePrice + "\n";
        str += "Total price: " + this.price + "\n";
        str += "//////////////////////////////////////////";

        return str;
    }



    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVehicleID() {
        return this.vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public Vehicle getRentedVehicle() {
        return this.rentedVehicle;
    }

    public void setRentedVehicle(Vehicle rentedVehicle) {
        this.rentedVehicle = rentedVehicle;
    }

    public String getStartingLocation() {
        return this.startingLocation;
    }

    public void setStartingLocation(String startingLocation) {
        this.startingLocation = startingLocation;
    }

    public String getDestLocation() {
        return this.destLocation;
    }

    public void setDestLocation(String destLocation) {
        this.destLocation = destLocation;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean isMalfunction() {
        return this.malfunction;
    }
    
    public void setMalfunction(Boolean malfunction) {
        this.malfunction = malfunction;
    }

    public Boolean isPromotion() {
        return this.promotion;
    }

    public void setPromotion(Boolean promotion) {
        this.promotion = promotion;
    }

    public Integer getDriverLicense() {
        return this.driverLicense;
    }

    public void setDriverLicense(Integer driverLicense) {
        this.driverLicense = driverLicense;
    }

    public Double getBasePrice() {
        return this.basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public DateTimeFormatter getSdf() {
        return this.dtf;
    }

    public void setSdf(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

    public Integer getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(Integer receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Boolean isDistanceWide() {
        return distanceWide;
    }

    public void setDistanceWide(Boolean distanceWide) {
        this.distanceWide = distanceWide;
    }
}
