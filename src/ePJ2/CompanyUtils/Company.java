package ePJ2.CompanyUtils;

import java.io.*;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ePJ2.App;
import ePJ2.Rental.Rental;
import ePJ2.Vehicles.Vehicle;

public class Company {
    private List<Vehicle> vehicles;
    private List<List<Rental>> rentalLists;
    private List<List<Receipt>> receiptLists;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
    DateTimeFormatter dtfr = DateTimeFormatter.ofPattern(("d.M.yyy HH-mm"));

    public Company(List<Vehicle> vehicles, List<Rental> rentals){
        this.vehicles = vehicles;

        this.rentalLists = new ArrayList<List<Rental>>();
        this.rentalLists.add(new ArrayList<Rental>());

        this.receiptLists = new ArrayList<List<Receipt>>();
        this.receiptLists.add(new ArrayList<Receipt>());

        LocalDateTime currentDateTime = LocalDateTime.parse(rentals.get(0).getDate(), dtf);
        LocalDateTime prevDateTime = LocalDateTime.parse(rentals.get(0).getDate(), dtf);
        int i = 0;
        for(Rental r: rentals){
            currentDateTime = LocalDateTime.parse(r.getDate(), dtf);
            if(currentDateTime.equals(prevDateTime)){
                rentalLists.get(i).add(r);
            }
            else{
                i++;
                this.rentalLists.add(new ArrayList<Rental>());
                rentalLists.get(i).add(r);
            }
            prevDateTime = LocalDateTime.parse(r.getDate(), dtf);
        }
    }


    public Boolean dateTimeComplete(Integer timeTracker){
        for(Rental r: rentalLists.get(timeTracker)){
            if(!r.isFinished())
                return false;
        }
        return true;
    }

    public void writeReceipts(Integer dateTimeTracker) {
        for(Receipt r : this.receiptLists.get(dateTimeTracker)){
            String path = new String(App.RECEIPT_PATH + "\\" + dtfr.format(r.getDate()));
            File fileDir = new File(path);
            String filename = new String(r.getUser() + ".txt");
            File file = new File(fileDir, filename);
            try {
                if(!fileDir.isDirectory()){
                    fileDir.mkdir();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                writer.write(r.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<List<Rental>> getRentalLists() {
        return this.rentalLists;
    }

    public void setRentalLists(List<List<Rental>> rentalLists) {
        this.rentalLists = rentalLists;
    }

    public List<List<Receipt>> getReceiptLists() {
        return this.receiptLists;
    }

    public void setReceiptLists(List<List<Receipt>> receiptLists) {
        this.receiptLists = receiptLists;
    }

}
