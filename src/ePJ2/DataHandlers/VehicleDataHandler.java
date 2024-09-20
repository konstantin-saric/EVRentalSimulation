package ePJ2.DataHandlers;

import java.util.ArrayList;
import java.util.List;

import ePJ2.Parser.Parser;
import ePJ2.Vehicles.*;

public class VehicleDataHandler {

    /**
     *Metoda koja stvara listu vozila prema podacima dobijenih iz parsera
     *@param path putanja do fajla u kojem se nalaze podaci o vozilima 
     */
    public static List<Vehicle> fetchVehicles(String path){
        List<String[]> vehicleData = Parser.readData(path);

        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        for(int i = 0; i < vehicleData.size(); i++){
            if(vehicleData.get(i)[8].equals("bicikl")){
                vehicles.add(new Bicycle(vehicleData.get(i)));
            }
            else if(vehicleData.get(i)[8].equals("automobil")){
                vehicles.add(new Car(vehicleData.get(i)));
            }
            else if(vehicleData.get(i)[8].equals("trotinet")){
                vehicles.add(new Scooter(vehicleData.get(i)));
            }
            else{
             continue;   
            }
        }

        return vehicles;
    }


}
