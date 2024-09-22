package ePJ2.DataHandlers;

import ePJ2.Parser.Parser;
import ePJ2.Rental.Rental;
import ePJ2.Rental.RentalComparator;
import ePJ2.Vehicles.Vehicle;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa koja se bavi podacima vezanim za iznajmljivanja
 * Stvara listu svih iznajmljivanja koja ce stici kompaniji
 */
public class RentalDataHandler {
    
    /**
     *Metoda koja stvara listu iznajmljivanja prema podacima dobijenih iz parsera
     *@param rentalPath putanja do fajla u kojem se nalaze podaci o iznajmljivanjima
     */
    public static List<Rental> fetchRentals(String rentalPath, List<Vehicle> vehicles, BorderPane map){
        List<String[]> rentalData = Parser.readData(rentalPath);

        List<Rental> rentals = new ArrayList<Rental>();
        for(int i = 0; i < rentalData.size(); i++){
            boolean vehicleExists = false;
            Rental rental = new Rental(rentalData.get(i), vehicles, map);
            for(Vehicle v: vehicles){
                if(rental.getVehicleID().equals(v.getID())){
                    vehicleExists = true;
                    break;
                }
            }
            if(vehicleExists)
                rentals.add(rental);
        }

        RentalComparator rc = new RentalComparator();
        rentals.sort(rc);

        return rentals;
    }
}
