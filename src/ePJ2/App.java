package ePJ2;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import ePJ2.DisplayHandlers.SceneHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ePJ2.Clock.Clock;
import ePJ2.CompanyUtils.Company;
import ePJ2.DataHandlers.RentalDataHandler;
import ePJ2.DisplayHandlers.MapView;
import ePJ2.DataHandlers.VehicleDataHandler;
import ePJ2.Vehicles.*;
import ePJ2.Rental.Rental;
import ePJ2.Rental.RentalComparator;
 

public class App extends Application{

    private static final int GRID_SIZE = 20; // Number of cells in each row/column
    private static final int CELL_SIZE = 30; // Size of each cell in pixels

    BorderPane gridBorderPane = new BorderPane();
    Scene map, vehiclesTableScene, malfunctionTableScene, businessStatisticsScene;
    Button vehicleTableButton = new Button("Switch to Vehicle Table");
    Button mapViewButton = new Button("Return to Map View");
    Button malfunctionTableButton = new Button("Switch to Malfunction View");
    Button statisticsButton = new Button("Switch to Business Statistics Overview");

    public static String RECEIPT_PATH;
    public static String DATA_PATH;



    
    /** 
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String propPath = rootPath + "app.properties";
        Properties appProperties = new Properties();
        appProperties.load(new FileInputStream(propPath));

        App.RECEIPT_PATH = appProperties.getProperty("receiptsPath");
        App.DATA_PATH = appProperties.getProperty("dataPath");

        vehicleTableButton.setOnAction(e -> {
            SceneHandler.switchScene(primaryStage, vehiclesTableScene);
        });

        SceneHandler.createGrid(20, 20, 30, gridBorderPane);
        HBox mapButtonHBox = new HBox();
        mapButtonHBox.getChildren().addAll(vehicleTableButton);
        gridBorderPane.setTop(mapButtonHBox);
        map = new Scene(gridBorderPane, GRID_SIZE * (CELL_SIZE + 3), GRID_SIZE * (CELL_SIZE + 4));

        List<Vehicle> vehicles = VehicleDataHandler.fetchVehicles(DATA_PATH + "Vehicles.csv");
        List<Rental> completeRentalList = RentalDataHandler.fetchRentals(DATA_PATH + "Rentals.csv", vehicles, gridBorderPane);
        completeRentalList.sort(new RentalComparator());

        Company company = new Company(vehicles, completeRentalList);
        Clock clock = new Clock(company);

        clock.start();

        BorderPane borderPaneS2 = SceneHandler.createRentalTable(clock.getCompany().getRentalLists().get(clock.getDayCounter()));

        Label label = new Label();
        label.setText("vehiclesTableScene");

        mapViewButton.setOnAction(e -> {
            SceneHandler.switchScene(primaryStage, map);
        });

        BorderPane vehicleTable = SceneHandler.createVehicleTable(vehicles);
        HBox vehicleTableButtonHBox = new HBox();
        vehicleTableButtonHBox.getChildren().addAll(mapViewButton);
        vehicleTable.setTop(mapViewButton);
        vehiclesTableScene = new Scene(vehicleTable);

        primaryStage.setTitle("Map View");
        primaryStage.setScene(map);
        primaryStage.show();

    }

    
    /** 
     * @param args
     */
    public static void main(String[] args){
        launch(args); 
    }
}
