package ePJ2.Clock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.FutureTask;

import ePJ2.CompanyUtils.Company;
import ePJ2.DisplayHandlers.SceneHandler;
import ePJ2.Rental.Rental;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Clock extends Thread{
    private Company company;
    private LocalDateTime currentDateTime;
    Integer dateTimeTracker;

    
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
    DateTimeFormatter dtfClock = DateTimeFormatter.ofPattern("HH:mm");

    public Clock(Company company){
        super();
        this.company = company;
        dateTimeTracker = 0;
        currentDateTime = LocalDateTime.parse(this.company.getRentalLists().getFirst().getFirst().getDate(), dtf);
    }


    @Override
    public void run() {

        while(dateTimeTracker < company.getRentalLists().size()){

            boolean threadsStarted = false;

            currentDateTime = LocalDateTime.parse(this.company.getRentalLists().get(dateTimeTracker).getFirst().getDate(), dtf);
            System.out.println(dtf.format(currentDateTime));


            //Loop runs until all rentals in the datetime are completed
            while(!company.dateTimeComplete(dateTimeTracker)){

                if(!threadsStarted) {
                    for (Rental r : company.getRentalLists().get(dateTimeTracker)) {
                        boolean unique = true;
                        for (Rental t : company.getRentalLists().get(dateTimeTracker)) {
                            if (r.getVehicleID().equals(t.getVehicleID()) && !r.equals(t)) {
                                unique = false;
                            }
                        }
                        if(unique){
                            r.start();
                        }
                        else{
                            r.setSkipped(true);
                        }
                    }

                    threadsStarted = true;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            for(Rental r: company.getRentalLists().get(dateTimeTracker)){
                if(!r.isSkipped()) {
                    company.getReceiptLists().get(dateTimeTracker).add(r.generateReceipt());
                }
            }

            company.writeReceipts(dateTimeTracker);

            FutureTask<Void> clearMap = new FutureTask<Void>(() -> {
               for(int row = 0; row < 20; row++){
                   for(int col = 0; col < 20; col++){
                       Rectangle cell = SceneHandler.mapArray[row][col];
                       if(row > 4 && row < 15 && col > 4 && col < 15){
                           cell.setFill(new Color(0.38, 0.54, 0.87, 1.0));
                       }
                       else{
                           cell.setFill(Color.WHITE);
                       }
                   }
                }

                return null;
            });

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(clearMap);

            dateTimeTracker++;
        }
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDateTime getCurrentTime() {
        return this.currentDateTime;
    }

    public void setCurrentTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public DateTimeFormatter getDtf() {
        return this.dtf;
    }

    public void setDtf(DateTimeFormatter dtf) {
        this.dtf = dtf;
    }

    public DateTimeFormatter getDtfClock() {
        return this.dtfClock;
    }

    public void setDtfClock(DateTimeFormatter dtfClock) {
        this.dtfClock = dtfClock;
    }


    public Integer getDayCounter() {
        return this.dateTimeTracker;
    }

    public void setDayCounter(Integer dateTimeTracker) {
        this.dateTimeTracker = dateTimeTracker;
    }

}