package ePJ2.Vehicles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Car extends Vehicle{
    
    private LocalDate dateOfAcquisition;
    private String description;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy.");

    public Car(String[] vehicleData){
        super(vehicleData[0], vehicleData[1], vehicleData[2], Integer.valueOf(vehicleData[4]));

        this.dateOfAcquisition = LocalDate.parse(vehicleData[3], dtf);
        this.description = vehicleData[7];
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        String string = "ID: " + this.ID + " Model: " + this.model + " Date: " + dtf.format(this.dateOfAcquisition) + " Desc: " + this.description;
        return string;
    }

    public String getDateOfAcquisition() {
        return dtf.format(dateOfAcquisition);
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
