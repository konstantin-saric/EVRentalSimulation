package ePJ2.DataHandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ePJ2.Parser.Parser;
import ePJ2.Rental.Rental;
import ePJ2.Rental.RentalComparator;
import ePJ2.Vehicles.Vehicle;
import javafx.scene.layout.BorderPane;

public class RentalDataHandler {
    
    /**
     *Metoda koja stvara listu iznajmljivanja prema podacima dobijenih iz parsera
     *@param rentalPath putanja do fajla u kojem se nalaze podaci o iznajmljivanjima
     */
    public static List<Rental> fetchRentals(String rentalPath, List<Vehicle> vehicles, BorderPane map){
        List<String[]> rentalData = Parser.readData(rentalPath);

        List<Rental> rentals = new ArrayList<Rental>();
        for(int i = 0; i < rentalData.size(); i++){
            rentals.add(new Rental(rentalData.get(i), vehicles, map));
        }

        RentalComparator rc = new RentalComparator();
        Collections.sort(rentals, rc);

        return rentals;
    }
}
