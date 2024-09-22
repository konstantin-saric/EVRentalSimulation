package ePJ2.DisplayHandlers;

import ePJ2.Rental.Rental;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
     *Klasa koja upravlja i konstruise JavaFX stage-ove 
     */
public class WindowHandler {
    
    /**
     *Metoda koja prikazuje informacije o iznajmljenim vozilima u odredjenom polju grida
     *@param cellRental lista podataka o iznajmljivanjima u polju grida
     */
    public static void showCellInfo(Rental cellRental){
        Stage cellInfoWindow = new Stage();
        Scene cellInfoScene;
        List<Rental> cellrentalslist = new ArrayList<Rental>();
        cellrentalslist.add(cellRental);
        BorderPane cellInfoPane = SceneHandler.createRentalTable(cellrentalslist);

        cellInfoScene = new Scene(cellInfoPane);
        
        cellInfoWindow.setScene(cellInfoScene);
        cellInfoWindow.show();
    }
}
