package ePJ2.Vehicles;

import ePJ2.App;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Car extends Vehicle implements Serializable {
    
    private LocalDate dateOfAcquisition;
    private String description;


    public Car(String[] vehicleData){
        super(vehicleData[0], vehicleData[1], vehicleData[2], Integer.valueOf(vehicleData[4]));

        this.dateOfAcquisition = LocalDate.parse(vehicleData[3], App.dtfv);
        this.description = vehicleData[7];
        this.repairPrice = this.price * Double.parseDouble(App.properties.getProperty("CAR_REPAIR_FEE"));
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        String string = "ID: " + this.ID + " Model: " + this.model + " Date: " + App.dtfv.format(this.dateOfAcquisition) + " Desc: " + this.description;
        return string;
    }

    public String getDateOfAcquisition() {
        return App.dtfv.format(dateOfAcquisition);
    }

    public void setDateOfAcquisition(LocalDate dateOfAcquisition) {
        this.dateOfAcquisition = dateOfAcquisition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
