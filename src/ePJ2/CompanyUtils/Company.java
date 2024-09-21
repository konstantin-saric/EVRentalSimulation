package ePJ2.CompanyUtils;

import java.awt.*;
import java.io.*;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import ePJ2.App;
import ePJ2.Rental.Rental;
import ePJ2.Vehicles.Bicycle;
import ePJ2.Vehicles.Car;
import ePJ2.Vehicles.Scooter;
import ePJ2.Vehicles.Vehicle;

import javafx.scene.control.TextArea;

public class Company {
    private List<Vehicle> vehicles;
    private List<List<Rental>> rentalLists;
    private List<List<Receipt>> receiptLists;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
    DateTimeFormatter dtfr = DateTimeFormatter.ofPattern("d.M.yyyy HH-mm");
    DateTimeFormatter dtfd = DateTimeFormatter.ofPattern("d.M.yyyy");
    Properties properties = new Properties();

    public Company(List<Vehicle> vehicles, List<Rental> rentals){

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String propPath = rootPath + "app.properties";
        try{
            properties.load(new FileInputStream(propPath));
        }
        catch(IOException e){
            e.printStackTrace();
        }

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

    public List<TextArea> calculateBusinessStatistics(){
        TextArea summaryStatistics = new TextArea();
        List<TextArea> dailyStatistics = new ArrayList<TextArea>();
        List<TextArea> businessStatistics = new ArrayList<TextArea>();

        Double k = 0.0;

        Double summaryTotalRevenue = 0.0;
        Double summaryTotalDiscounts = 0.0;
        Double summaryTotalPromotions = 0.0;
        Double summaryTotalDistanceWide = 0.0;
        Double summaryTotalDistanceNarrow = 0.0;
        Double summaryTotalRepairs = 0.0;
        Double summaryTotalMaintenance = 0.0;
        Double summaryTotalExpenditure = 0.0;
        Double summaryTotalTax = 0.0;

        for (List<Receipt> rl : this.receiptLists) {
            for (Receipt r : rl) {
                k = 0.0;
                summaryTotalRevenue += r.getPrice();
                summaryTotalPromotions += (r.isPromotion() ? Double.parseDouble(properties.getProperty("DISCOUNT_PROM")) * r.getBasePrice() : 0);
                summaryTotalDiscounts += (r.getReceiptNumber() % 10 == 0 ? Double.parseDouble(properties.getProperty("DISCOUNT")) * r.getBasePrice() : 0);
                if (r.isDistanceWide())
                    summaryTotalDistanceWide += r.getBasePrice() * Double.parseDouble(properties.getProperty("DISTANCE_WIDE"));
                else
                    summaryTotalDistanceNarrow += r.getBasePrice() * Double.parseDouble(properties.getProperty("DISTANCE_NARROW"));
                if (r.isMalfunction()) {
                    if (r.getRentedVehicle() instanceof Car)
                        k = Double.parseDouble(properties.getProperty("CAR_REPAIR_FEE"));
                    else if (r.getRentedVehicle() instanceof Bicycle)
                        k = Double.parseDouble(properties.getProperty("BIKE_REPAIR_FEE"));
                    else if (r.getRentedVehicle() instanceof Scooter)
                        k = Double.parseDouble(properties.getProperty("SCOOTER_REPAIR_FEE"));

                        summaryTotalRepairs += k * r.getRentedVehicle().getPrice();
                }
                summaryTotalMaintenance += Double.parseDouble(properties.getProperty("MAINTENANCE_FEE")) * r.getPrice();
                summaryTotalExpenditure += Double.parseDouble(properties.getProperty("EXPENDITURES")) * r.getPrice();
                summaryTotalTax += (r.getPrice() - (Double.parseDouble(properties.getProperty("MAINTENANCE_FEE")) * r.getPrice())
                            - (k * r.getRentedVehicle().getPrice() - Double.parseDouble(properties.getProperty("EXPENDITURES")) * r.getPrice()))
                            * Double.parseDouble(properties.getProperty("TAX"));
            }
        }

        String summaryStatisticsString = "------------- Summary Statistics -------------\n" +
                                         "Total Revenue: " + summaryTotalRevenue + "\n" +
                                         "Total Discounts: " + summaryTotalDiscounts + "\n" +
                                         "Total Promotions: " + summaryTotalPromotions + "\n" +
                                         "Total Wide Distance Rentals: " + summaryTotalDistanceWide + "\n" +
                                         "Total Narrow DistanceRentals: " + summaryTotalDistanceNarrow + "\n" +
                                         "Total Maintenance Fees: " + summaryTotalMaintenance + "\n" +
                                         "Total Repair Fees: " + summaryTotalRepairs + "\n" +
                                         "Total Expenditures: " + summaryTotalExpenditure + "\n" +
                                         "Total Tax: " + summaryTotalTax + "\n" +
                                         "----------------------------------------------\n";
        summaryStatistics.setText(summaryStatisticsString);
        summaryStatistics.setEditable(false);
        businessStatistics.add(summaryStatistics);


        Double dailyTotalRevenue = 0.0;
        Double dailyTotalDiscounts = 0.0;
        Double dailyTotalPromotions = 0.0;
        Double dailyTotalDistanceWide = 0.0;
        Double dailyTotalDistanceNarrow = 0.0;
        Double dailyTotalRepairs = 0.0;
        Double dailyTotalMaintenance = 0.0;

        String day = new String();

        if(!receiptLists.get(0).isEmpty()) {
            day = dtfd.format(receiptLists.get(0).get(0).getDate());

            for (int i = 0; i < receiptLists.size(); i++) {
                if (day.equals(dtfd.format(receiptLists.get(i).getFirst().getDate()))) {
                    for (Receipt r : receiptLists.get(i)) {
                        k = 0.0;
                        dailyTotalRevenue += r.getPrice();
                        dailyTotalPromotions += (r.isPromotion() ? Double.parseDouble(properties.getProperty("DISCOUNT_PROM")) * r.getBasePrice() : 0);
                        dailyTotalDiscounts += (r.getReceiptNumber() % 10 == 0 ? Double.parseDouble(properties.getProperty("DISCOUNT")) * r.getBasePrice() : 0);
                        if (r.isDistanceWide())
                            dailyTotalDistanceWide += r.getBasePrice() * Double.parseDouble(properties.getProperty("DISTANCE_WIDE"));
                        else
                            dailyTotalDistanceNarrow += r.getBasePrice() * Double.parseDouble(properties.getProperty("DISTANCE_NARROW"));
                        if (r.isMalfunction()) {
                            if (r.getRentedVehicle() instanceof Car)
                                k = Double.parseDouble(properties.getProperty("CAR_REPAIR_FEE"));
                            else if (r.getRentedVehicle() instanceof Bicycle)
                                k = Double.parseDouble(properties.getProperty("BIKE_REPAIR_FEE"));
                            else if (r.getRentedVehicle() instanceof Scooter)
                                k = Double.parseDouble(properties.getProperty("SCOOTER_REPAIR_FEE"));

                            dailyTotalRepairs += k * r.getRentedVehicle().getPrice();
                        }
                        dailyTotalMaintenance += Double.parseDouble(properties.getProperty("MAINTENANCE_FEE")) * r.getPrice();
                    }
                } else {
                    day = dtfd.format(receiptLists.get(i).getFirst().getDate());
                    i--;

                    String dailyStatisticsString = "------------- " + day + " -------------\n" +
                            "Total Revenue: " + dailyTotalRevenue + "\n" +
                            "Total Discounts: " + dailyTotalDiscounts + "\n" +
                            "Total Promotions: " + dailyTotalPromotions + "\n" +
                            "Total Wide Distance Rentals: " + dailyTotalDistanceWide + "\n" +
                            "Total Narrow DistanceRentals: " + dailyTotalDistanceNarrow + "\n" +
                            "Total Maintenance Fees: " + dailyTotalMaintenance + "\n" +
                            "Total Repair Fees: " + dailyTotalRepairs + "\n" +
                            "----------------------------------------------\n";

                    TextArea daily = new TextArea(dailyStatisticsString);
                    dailyStatistics.add(daily);

                    dailyTotalRevenue = 0.0;
                    dailyTotalDiscounts = 0.0;
                    dailyTotalPromotions = 0.0;
                    dailyTotalDistanceWide = 0.0;
                    dailyTotalDistanceNarrow = 0.0;
                    dailyTotalRepairs = 0.0;
                    dailyTotalMaintenance = 0.0;

                }
            }
        }
        for(TextArea t: dailyStatistics){
            t.setEditable(false);
            businessStatistics.add(t);
        }

        return businessStatistics;
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
