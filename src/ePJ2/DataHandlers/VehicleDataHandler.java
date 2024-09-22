package ePJ2.DataHandlers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ePJ2.App;
import ePJ2.CompanyUtils.Receipt;
import ePJ2.Parser.Parser;
import ePJ2.Rental.Rental;
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

    public static void serializeMalfunctions(List<Rental> rentals){

        String path = new String(App.VEHICLE_PATH);
        File fileDir = new File(path);
        String filename = new String("malfunctions.data");
        File file = new File(fileDir, filename);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(fOut);
            for(Rental r: rentals){
                if (!fileDir.isDirectory()) {
                    fileDir.mkdir();
                }
                objOut.writeObject(r.getRentedVehicle());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Vehicle> deserializeMalfunctions(){
        String path = new String(App.VEHICLE_PATH);
        File fileDir = new File(path);
        String filename = new String("malfunctions.data");
        File file = new File(fileDir, filename);

        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        try {
            FileInputStream fIn = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(fIn);

            for(int i = 0 ; ; i++){
                Object obj = objIn.readObject();
                if(obj instanceof Car) {
                    Car car = (Car) obj;
                    vehicles.add(car);
                }
                else if(obj instanceof Bicycle) {
                    Bicycle bike = (Bicycle) obj;
                    vehicles.add(bike);
                }
                else if(obj instanceof Scooter) {
                    Scooter scooter = (Scooter) obj;
                    vehicles.add(scooter);
                }
            }

        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (EOFException e) {
            return vehicles;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return vehicles;
    }
}
