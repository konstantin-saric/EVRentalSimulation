package ePJ2.DataHandlers;

import ePJ2.App;
import ePJ2.Parser.Parser;
import ePJ2.Rental.Rental;
import ePJ2.Vehicles.Bicycle;
import ePJ2.Vehicles.Car;
import ePJ2.Vehicles.Scooter;
import ePJ2.Vehicles.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa koja bavi podacima vezanim za vozila
 * Stvara listu svih vozila koje kompanija posjeduje
 * Serijalizuje i deserijalizuje sva vozila sa kvarovima
 */
public class VehicleDataHandler {

    /**
     *Metoda koja stvara listu vozila prema podacima dobijenih iz parsera
     *@param path putanja do fajla u kojem se nalaze podaci o vozilima 
     */
    public static List<Vehicle> fetchVehicles(String path){
        List<String[]> vehicleData = Parser.readData(path);

        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        for(int i = 0; i < vehicleData.size(); i++){
            boolean unique = true;
            if(vehicleData.get(i)[8].equals("bicikl")){
                Bicycle bike = new Bicycle(vehicleData.get(i));
                for(Vehicle v: vehicles)
                    if(v instanceof Bicycle && v.getID().equals(bike.getID())) {
                        unique = false;
                        break;
                    }
                if(unique)
                    vehicles.add(bike);
            }
            else if(vehicleData.get(i)[8].equals("automobil")){
                Car car = new Car(vehicleData.get(i));
                for(Vehicle v: vehicles)
                    if(v instanceof Car && v.getID().equals(car.getID())) {
                        unique = false;
                        break;
                    }
                if(unique)
                    vehicles.add(car);
            }
            else if(vehicleData.get(i)[8].equals("trotinet")){
                Scooter scooter = new Scooter(vehicleData.get(i));
                for(Vehicle v: vehicles)
                    if(v instanceof Scooter && v.getID().equals(scooter.getID())) {
                        unique = false;
                        break;
                    }
                if(unique)
                    vehicles.add(scooter);
            }
            else{
             continue;
            }
        }

        return vehicles;
    }

    /**
     * Metoda koja serijalizuje sva vozila koja imaju kvar
     * @param rentals Lista svih iznajmljivanja
     */
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
            objOut.close();
            fOut.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda koja deserijalizuje sva vozila sa kvarom
     * @return Lista vozila sa kvarom
     */
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
