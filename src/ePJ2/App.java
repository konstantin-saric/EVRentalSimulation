package ePJ2;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import ePJ2.DisplayHandlers.SceneHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ePJ2.Clock.Clock;
import ePJ2.CompanyUtils.Company;
import ePJ2.DataHandlers.RentalDataHandler;
import ePJ2.DataHandlers.VehicleDataHandler;
import ePJ2.Vehicles.*;
import ePJ2.Rental.Rental;
import ePJ2.Rental.RentalComparator;
 

public class App extends Application{

    private static final int GRID_SIZE = 20; // Number of cells in each row/column
    private static final int CELL_SIZE = 30; // Size of each cell in pixels

    private List<Vehicle> vehicles;
    private List<Rental> completeRentalList;
    private Company company;
    private Clock clock;

    private BorderPane gridBorderPane = new BorderPane();
    private HBox mapButtonHBox = new HBox();
    private HBox vehicleTableButtonHBox = new HBox();
    private HBox malfunctionTableButtonHBox = new HBox();
    private HBox statisticsButtonHBox = new HBox();
    private Scene map, vehiclesTableScene, malfunctionTableScene, businessStatisticsScene;
    private Button mapViewButton = new Button("Return to Map View");
    private Button vehicleTableButton = new Button("Switch to Vehicle Table");
    private Button malfunctionTableButton = new Button("Switch to Malfunction View");
    private Button statisticsButton = new Button("Switch to Business Statistics Overview");



    public static String RECEIPT_PATH;
    public static String DATA_PATH;

    void setButtonLogic(Stage primaryStage){
        mapViewButton.setOnAction(e -> {
            if(mapButtonHBox.getChildren().size() < 3) {
                mapButtonHBox.getChildren().clear();
                mapButtonHBox.getChildren().addAll(vehicleTableButton, malfunctionTableButton, statisticsButton);
            }
            SceneHandler.switchScene(primaryStage, map);
        });

        vehicleTableButton.setOnAction(e -> {
            if(vehicleTableButtonHBox.getChildren().size() < 3) {
                vehicleTableButtonHBox.getChildren().clear();
                vehicleTableButtonHBox.getChildren().addAll(mapViewButton, malfunctionTableButton, statisticsButton);
            }
            updateVehicleView(primaryStage);
            SceneHandler.switchScene(primaryStage, vehiclesTableScene);
        });


        malfunctionTableButton.setOnAction(e -> {
            if(malfunctionTableButtonHBox.getChildren().size() < 3) {
                malfunctionTableButtonHBox.getChildren().clear();
                malfunctionTableButtonHBox.getChildren().addAll(mapViewButton, vehicleTableButton, statisticsButton);
            }
            updateMalfunctionView(primaryStage);
            SceneHandler.switchScene(primaryStage, malfunctionTableScene);
        });

        statisticsButton.setOnAction(e -> {
            if(statisticsButtonHBox.getChildren().size() < 3) {
                statisticsButtonHBox.getChildren().clear();
                statisticsButtonHBox.getChildren().addAll(mapViewButton, vehicleTableButton, malfunctionTableButton);
            }
            updateStatisticsView(primaryStage);
            SceneHandler.switchScene(primaryStage, businessStatisticsScene);
        });
    }

    void updateVehicleView(Stage primaryStage){
        BorderPane vehicleTable = SceneHandler.createVehicleTable(vehicles);
        vehicleTable.setTop(vehicleTableButtonHBox);
        vehiclesTableScene = new Scene(vehicleTable);
        primaryStage.setTitle("Vehicle View");
    }

    void updateMalfunctionView(Stage primaryStage){
        BorderPane malfunctionTable = SceneHandler.createMalfunctionTable(company.getRentalLists().get(clock.getDayCounter()), clock.isSimFinished());
        malfunctionTable.setTop(this.malfunctionTableButtonHBox);
        malfunctionTableScene = new Scene(malfunctionTable);
        primaryStage.setTitle("Malfunction View");
    }

    void updateStatisticsView(Stage primaryStage){
        BorderPane statisticsView = SceneHandler.createStatisticsView(company);
        statisticsView.setTop(this.statisticsButtonHBox);
        businessStatisticsScene = new Scene(statisticsView);
        primaryStage.setTitle("Statistics View");
    }

    
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

        setButtonLogic(primaryStage);

        vehicles = VehicleDataHandler.fetchVehicles(DATA_PATH + "Vehicles.csv");
        completeRentalList = RentalDataHandler.fetchRentals(DATA_PATH + "Rentals.csv", vehicles, gridBorderPane);
        completeRentalList.sort(new RentalComparator());

        company = new Company(vehicles, completeRentalList);
        clock = new Clock(company);

        clock.start();

        SceneHandler.createGrid(20, 20, 30, gridBorderPane);
        mapButtonHBox.getChildren().addAll(vehicleTableButton, malfunctionTableButton,statisticsButton);
        gridBorderPane.setTop(mapButtonHBox);
        map = new Scene(gridBorderPane, GRID_SIZE * (CELL_SIZE + 3), GRID_SIZE * (CELL_SIZE + 4));
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
