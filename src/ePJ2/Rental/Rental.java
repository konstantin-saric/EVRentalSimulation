package ePJ2.Rental;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ePJ2.CompanyUtils.Receipt;
import ePJ2.DisplayHandlers.SceneHandler;
import ePJ2.DisplayHandlers.WindowHandler;
import ePJ2.Vehicles.Car;
import ePJ2.Vehicles.Scooter;
import ePJ2.Vehicles.Vehicle;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.geom.Point2D;
import java.util.concurrent.FutureTask;


/**
     *Klasa koja predstavlja iznajmljivanja vozila
     */
public class Rental extends Thread{
    
    private LocalDateTime date;
    private String user;
    private String vehicleID;
    private Vehicle rentedVehicle;
    private String rentedVehicleType;
    private Point2D.Double startingLoc;
    private String startingLocStr;
    private Point2D.Double currentLoc;
    private String currentLocStr;
    private Point2D.Double destLoc;
    private String destLocStr;
    private Integer duration;
    private String malfunctionDesc;
    private Boolean malfunction;
    private Boolean distanceWide;
    private Boolean promotion;
    private Boolean finished;
    private Boolean skipped;

    private BorderPane map;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
    
    public Rental(String[] rentalData, List<Vehicle> vehicles, BorderPane map){
        
        date = LocalDateTime.parse(rentalData[0], dtf);
        this.map = map;

        rentalData[3] = rentalData[3].replaceAll("\"", "");
        rentalData[4] = rentalData[4].replaceAll("\"", "");
        
        String[] start = rentalData[3].split(","); 
        String[] dest = rentalData[4].split(",");

        Double[] startLocArr = new Double[2];
        Double[] destLocArr = new Double[2];

        startLocArr[0] = Double.parseDouble(start[0]);
        startLocArr[1] = Double.parseDouble(start[1]);

        destLocArr[0] = Double.parseDouble(dest[0]);
        destLocArr[1] = Double.parseDouble(dest[1]);

        //this.date = date.getTime();
        user = rentalData[1];
        
        vehicleID = rentalData[2];
        for(Vehicle v: vehicles){
            if(v.getID().equals(this.vehicleID))
                rentedVehicle = v;
        }


        if(rentedVehicle instanceof Car) {
            rentedVehicleType = "Car";
            malfunctionDesc = "Car breakdown!";
        }
        else if(rentedVehicle instanceof Scooter) {
            rentedVehicleType = "Scooter";
            malfunctionDesc = "Scooter breakdown!";
        }
        else {
            rentedVehicleType = "Bicycle";
            malfunctionDesc = "Bike breakdown!";
        }

        startingLoc = new Point2D.Double(startLocArr[0], startLocArr[1]);
        currentLoc = new Point2D.Double(startLocArr[0], startLocArr[1]);
        destLoc = new Point2D.Double(destLocArr[0], destLocArr[1]);
        duration = Integer.parseInt(rentalData[5]);
        malfunction = rentalData[6].equals("da") ? true : false;
        promotion = rentalData[7].equals("da") ? true : false;

        startingLocStr = new String("(" + (int)startingLoc.x + "," + (int)startingLoc.y + ")");
        currentLocStr = new String("(" + (int)startingLoc.x + "," + (int)startingLoc.y + ")");
        destLocStr = new String("(" + (int)destLoc.y + "," + (int)destLoc.x + ")");

        finished = malfunction ? true : false;
        skipped = false;

        if(((startLocArr[0] < 4 || startLocArr[0] > 15) || (startLocArr[1] < 4 || startLocArr[1] > 15)) ||
           (destLocArr[0] < 4 || destLocArr[0] > 15) || (destLocArr[1] < 4 || destLocArr[1] > 15)){
            distanceWide = true;
           }
        else
            distanceWide = false;
    }

    @Override
    public void run() {

        rentedVehicle.setInUse(true);
        
        while(!finished){
            double speedX = (destLoc.x - startingLoc.x) / duration;
            double speedY = (destLoc.y - startingLoc.y) / duration;

            while(!(((int)currentLoc.x == (int)destLoc.x) && ((int)currentLoc.y == (int)destLoc.y))){
                Point2D.Double prevLoc = new Point2D.Double(currentLoc.x, currentLoc.y);
                currentLoc.setLocation(currentLoc.x + speedX, currentLoc.y + speedY);
                if((speedY < 0 && currentLoc.y < destLoc.y) || (speedY > 0 && currentLoc.y > destLoc.y)){
                    currentLoc.y = destLoc.y;
                }
                if((speedX < 0 && currentLoc.x < destLoc.x) || (speedX > 0 && currentLoc.x > destLoc.x)){
                    currentLoc.x = destLoc.x;
                }
                currentLocStr = new String("(" + (int)currentLoc.x + "," + (int)currentLoc.y + ")");
                rentedVehicle.discharge();

                FutureTask<Void> updateTask = new FutureTask<Void>(() -> {
                    Rectangle currentCell = SceneHandler.mapArray[(int)currentLoc.x][(int)currentLoc.y];
                    Rectangle prevCell = SceneHandler.mapArray[(int)prevLoc.x][(int)prevLoc.y];

                    prevCell.setFill(
                            ((prevLoc.x < 5 || prevLoc.x >= 15) || (prevLoc.y < 5 || prevLoc.y >= 15))?Color.WHITE:new Color(0.38, 0.54, 0.87, 1.0)
                    );
                    currentCell.setFill(Color.RED);

                    prevCell.setOnMouseClicked(e -> {});
                    currentCell.setOnMouseClicked(e -> {
                        WindowHandler.showCellInfo(this);
                    });


                    return null;
                });

                Platform.runLater(updateTask);
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            rentedVehicle.setInUse(false);
            finished = true;
            System.out.println("Thread done");
        }

        FutureTask<Void> clearCell = new FutureTask<Void>(() -> {

            Rectangle currentCell = SceneHandler.mapArray[(int)currentLoc.x][(int)currentLoc.y];

            currentCell.setFill(
                    ((currentLoc.x < 5 || currentLoc.x >= 15) || (currentLoc.y < 5 || currentLoc.y >= 15))?Color.WHITE:new Color(0.38, 0.54, 0.87, 1.0)
            );

            return null;
        });

        Platform.runLater(clearCell);

    }

    public Receipt generateReceipt(){
        return new Receipt(this);
    }

    
    /** 
     * @return String
     */
    public String getDate(){
        return dtf.format(date);
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        
        String string = new String("User:" + this.user + " Start:" + this.startingLoc + " Dest:" + this.destLoc + " Malf:" + this.malfunction + " Date:" + dtf.format(date));
        return string;
    }

    
    /** 
     * @param date
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    
    /** 
     * @return String
     */
    public String getUser() {
        return this.user;
    }

    
    /** 
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    public String getVehicleID() {
        return this.vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public Point2D.Double getStartingLoc() {
        return this.startingLoc;
    }

    public void setStartingLoc(Point2D.Double startingLoc) {
        this.startingLoc = startingLoc;
    }

    public Point2D.Double getCurrentLoc(){
        return this.currentLoc;
    }

    public void setCurrentLoc(Point2D.Double currentLoc){
        this.currentLoc = currentLoc;
    }

    public Point2D.Double getDestLoc() {
        return this.destLoc;
    }

    public void setDestLoc(Point2D.Double destLoc) {
        this.destLoc = destLoc;
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

    public String getStartingLocStr(){
        return this.startingLocStr;
    }

    public void setStartingLocStr(String startingLocString){
        this.startingLocStr = startingLocString;
    }

    public String getCurrentLocStr(){
        return this.currentLocStr;
    }

    public void setCurrentLocStr(String currentLocStr){
        this.currentLocStr = currentLocStr;
    }

    public String getDestLocStr(){
        return this.destLocStr;
    }

    public void setDestLocStr(String destLocStr){
        this.destLocStr = destLocStr;
    }

    public Vehicle getRentedVehicle() {
        return this.rentedVehicle;
    }

    public void setRentedVehicle(Vehicle rentedVehicle) {
        this.rentedVehicle = rentedVehicle;
    }

    public Boolean isDistanceWide(){
        return this.distanceWide;
    }

    public Boolean isFinished(){
        return this.finished;
    }

    public BorderPane getMap() {
        return map;
    }

    public void setMap(BorderPane map) {
        this.map = map;
    }

    public void setSkipped(boolean skip){
        skipped = skip;
    }

    public boolean isSkipped(){
        return skipped;
    }

    public String getRentedVehicleType() {
        return rentedVehicleType;
    }

    public void setRentedVehicleType(String rentedVehicleType) {
        this.rentedVehicleType = rentedVehicleType;
    }

    public String getMalfunctionDesc() {
        return malfunctionDesc;
    }

    public void setMalfunctionDesc(String malfunctionDesc) {
        this.malfunctionDesc = malfunctionDesc;
    }
}
