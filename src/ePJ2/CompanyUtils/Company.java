package ePJ2.CompanyUtils;

import ePJ2.App;
import ePJ2.Rental.Rental;
import ePJ2.Vehicles.Vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa koja predstavlja kompaniju
 * Drzi u sebi informacije o vozilima, iznajmljivanjima i racunima
 * Racuna statistike, pise racune i prati status iznajmljivanja za odredjeno datum-vrijeme
 */
public class Company {
    private List<Vehicle> vehicles;
    private List<List<Rental>> rentalLists;
    private List<List<Receipt>> receiptLists;


    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
    DateTimeFormatter dtfr = DateTimeFormatter.ofPattern("d.M.yyyy HH-mm");
    DateTimeFormatter dtfd = DateTimeFormatter.ofPattern("d.M.yyyy");

    /**
     * @param vehicles Lista vozila koje kompanija posjeduje
     * @param rentals Lista iznajmljivanja koje kompanija prima
     */
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

    /**
     * Metoda koja provjerava da status iznajmljivanja za odredjeni datum-vrijeme
     * @param timeTracker broj koji oznacava trenutno datum-vrijeme
     */
    public Boolean dateTimeComplete(Integer timeTracker){
        for(Rental r: rentalLists.get(timeTracker)){
            if(!r.isFinished() && !r.isSkipped())
                return false;
        }
        return true;
    }

    /**
     * Metoda koja ispisuje u fajlove sve racune za odredjeni datum-vrijeme
     * @param dateTimeTracker broj koji oznacava trenutno datum-vrijeme
     */
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

    /**
     * Metoda koja racuna statistiku poslovanja
     * @return Lista statistika koja ukljucuje ukupnu statistiku cijelog vremena poslovanja i statistiku odvojenu po datumu
     */
    public List<Statistic> calculateBusinessStatistics(){

        List<List<Receipt>> dailyReceipts = new ArrayList<List<Receipt>>();
        dailyReceipts.add(new ArrayList<Receipt>());
        String day;
        int k = 0;
        if(!receiptLists.get(0).isEmpty()) {
            day = dtfd.format(receiptLists.get(0).get(0).getDate());

            for (int i = 0; i < receiptLists.size(); i++) {
                if (!receiptLists.get(i).isEmpty() && day.equals(dtfd.format(receiptLists.get(i).get(0).getDate()))) {
                    for(Receipt r: receiptLists.get(i)){
                        dailyReceipts.get(k).add(r);
                    }
                } else if(!receiptLists.get(i).isEmpty() && !day.equals(dtfd.format(receiptLists.get(i).get(0).getDate()))){
                    dailyReceipts.add(new ArrayList<Receipt>());
                    day = dtfd.format(receiptLists.get(i).getFirst().getDate());
                    i--;
                    k++;
                }
            }
        }

        List<Statistic> statistics = new ArrayList<Statistic>();

        statistics.add(Statistic.createSummary(receiptLists));

        for(List<Receipt> rl: dailyReceipts){
            statistics.add(Statistic.createDaily(rl));
        }

        return statistics;
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
