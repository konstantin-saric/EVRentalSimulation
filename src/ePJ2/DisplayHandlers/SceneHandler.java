package ePJ2.DisplayHandlers;

import ePJ2.CompanyUtils.Company;
import ePJ2.CompanyUtils.Statistic;
import ePJ2.Rental.Rental;
import ePJ2.Vehicles.Bicycle;
import ePJ2.Vehicles.Car;
import ePJ2.Vehicles.Scooter;
import ePJ2.Vehicles.Vehicle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

    /**
     *Klasa koja upravlja i konstruise JavaFX scene 
     */
public class SceneHandler {

    public static Rectangle[][] mapArray = new Rectangle[20][20];
    public static TextArea dateTimeText = new TextArea("Test");


    /**
     *Metoda koja mijenja scenu na proslijedjenom stage-u
     *@param stage stage na kojem se mijenja scena
     *@param scene nova scena koja se postavlja na proslijedjen stage
     */
    public static void switchScene(Stage stage, Scene scene){
        stage.setScene(scene);
        stage.show();
    }

    /**
     *Metoda koja stvara grid u zadatom koordinatnom sistemu
     *Grid je konstruisan is elemenata tipa Rectangle
     *Implementira funkcionalnost predstavljanja informacija o iznajmljivanjima na proizvoljnom polju
     *@param gridHeight parametar koji odredjuje dimenzije grida 
     *@param gridWidth parametar koji odredjuje dimenzije grida
     *@param cellSize parametar koji odredjuje dimenzije grida
     */
    public static void createGrid(Integer gridHeight, Integer gridWidth, Integer cellSize, BorderPane map){

        GridPane gridPaneS1 = new GridPane();

        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                Rectangle cell;
                if(row > 4 && row < 15 && col > 4 && col < 15){
                    cell = new Rectangle(cellSize, cellSize, new Color(0.38, 0.54, 0.87, 1.0));
                }
                else{
                    cell = new Rectangle(cellSize, cellSize, Color.WHITE);
                }
                cell.setStroke(Color.BLACK);

                mapArray[row][col] = cell;

                gridPaneS1.add(cell, col, row);
            }
        }
        dateTimeText.setEditable(false);
        dateTimeText.setMaxHeight(30);
        map.setCenter(gridPaneS1);
        map.setBottom(dateTimeText);
    }

    /**
     *Metoda koja stvara tabelu sa informacijama o iznajmljivanjima na odredjenom polju na mapi
     *@param rentals lista iznajmljivanja iz kojih se stvara tabela
     *@return BorderPane u kojem se nalazi TableView tabela
     */
    @SuppressWarnings("unchecked")
    public static BorderPane createRentalTable(List<Rental> rentals){
        BorderPane tablePane = new BorderPane();
        TableView<Rental> table = new TableView<Rental>();

        TableColumn<Rental, LocalDateTime> dateCol = new TableColumn<Rental, LocalDateTime>("Date");
        TableColumn<Rental, String> userCol = new TableColumn<Rental, String>("User ID");
        TableColumn<Rental, String> vehicleCol = new TableColumn<Rental, String>("Vehicle ID");
        TableColumn<Rental, String> startLocCol = new TableColumn<Rental, String>("Start Location");
        TableColumn<Rental, String> currLocCol = new TableColumn<Rental, String>("Current Location");
        TableColumn<Rental, String> destLocCol = new TableColumn<Rental, String>("Destination Location");
        TableColumn<Rental, Number> durationCol = new TableColumn<Rental, Number>("Duration");

        dateCol.setCellValueFactory(new PropertyValueFactory<Rental, LocalDateTime>("date"));
        userCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("user"));
        vehicleCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("vehicleID"));
        startLocCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("startingLocStr"));
        currLocCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("currentLocStr"));
        destLocCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("destLocStr"));
        durationCol.setCellValueFactory(new PropertyValueFactory<Rental, Number>("duration"));

        ObservableList<Rental> rentalsTableList = FXCollections.observableArrayList(rentals);
        table.setEditable(true);
        table.setItems(rentalsTableList);
        table.getColumns().addAll(dateCol, userCol, vehicleCol, startLocCol, currLocCol, destLocCol, durationCol);
        tablePane.setCenter(table);

        return tablePane;
    }

    /**
     * Metoda koja stvara tabelu svih vozila
     * @param vehicles Lista svih vozila koje kompanija posjeduje
     * @return BorderPane koji u sebi sadrzi 3 TableView elementa u koja su poredana vozila razlicitih vrsta
     */
    public static BorderPane createVehicleTable(List<Vehicle> vehicles){

        List<Car> cars = new ArrayList<Car>();
        List<Scooter> scooters = new ArrayList<Scooter>();
        List<Bicycle> bikes = new ArrayList<Bicycle>();

        for(Vehicle v: vehicles){
            if(v instanceof Car){
                cars.add((Car)v);
            }
            if(v instanceof Scooter){
                scooters.add((Scooter)v);
            }
            if(v instanceof Bicycle){
                bikes.add((Bicycle)v);
            }
        }

        BorderPane tableBox = new BorderPane();
        TableView<Car> carTable = new TableView<Car>();
        TableView<Scooter> scooterTable = new TableView<Scooter>();
        TableView<Bicycle> bicycleTable = new TableView<Bicycle>();

        TableColumn<Car, String> carIDCol = new TableColumn<Car, String>("Vehicle ID");
        TableColumn<Car, String> carManufacturerCol = new TableColumn<Car, String>("Manufacturer");
        TableColumn<Car, String> carModelCol = new TableColumn<Car, String>("Model");
        TableColumn<Car, Number> carPriceCol = new TableColumn<Car, Number>("Price");
        TableColumn<Car, Number> carBatteryCol = new TableColumn<Car, Number>("Battery level");
        TableColumn<Car, LocalDate> carDateCol = new TableColumn<Car, LocalDate>("Date of Acquisition");
        TableColumn<Car, String> carDescriptionCol = new TableColumn<Car, String>("Description");

        carIDCol.setCellValueFactory(new PropertyValueFactory<Car, String>("ID"));
        carManufacturerCol.setCellValueFactory(new PropertyValueFactory<Car, String>("manufacturer"));
        carModelCol.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
        carPriceCol.setCellValueFactory(new PropertyValueFactory<Car, Number>("price"));
        carBatteryCol.setCellValueFactory(new PropertyValueFactory<Car, Number>("batteryLevel"));
        carDateCol.setCellValueFactory(new PropertyValueFactory<Car, LocalDate>("dateOfAcquisition"));
        carDescriptionCol.setCellValueFactory(new PropertyValueFactory<Car, String>("description"));

        ObservableList<Car> carsTableList = FXCollections.observableArrayList(cars);
        carTable.setEditable(true);
        carTable.setItems(carsTableList);
        carTable.getColumns().addAll(carIDCol, carManufacturerCol, carModelCol, carPriceCol, carBatteryCol, carDateCol, carDescriptionCol);


        TableColumn<Scooter, String> scooterIDCol = new TableColumn<Scooter, String>("Vehicle ID");
        TableColumn<Scooter, String> scooterManufacturerCol = new TableColumn<Scooter, String>("Manufacturer");
        TableColumn<Scooter, String> scooterModelCol = new TableColumn<Scooter, String>("Model");
        TableColumn<Scooter, Number> scooterPriceCol = new TableColumn<Scooter, Number>("Price");
        TableColumn<Scooter, Number> scooterBatteryCol = new TableColumn<Scooter, Number>("Battery level");
        TableColumn<Scooter, Number> scooterMaxSpeedCol = new TableColumn<Scooter, Number>("Maximum speed");


        scooterIDCol.setCellValueFactory(new PropertyValueFactory<Scooter, String>("ID"));
        scooterManufacturerCol.setCellValueFactory(new PropertyValueFactory<Scooter, String>("manufacturer"));
        scooterModelCol.setCellValueFactory(new PropertyValueFactory<Scooter, String>("model"));
        scooterPriceCol.setCellValueFactory(new PropertyValueFactory<Scooter, Number>("price"));
        scooterBatteryCol.setCellValueFactory(new PropertyValueFactory<Scooter, Number>("batteryLevel"));
        scooterMaxSpeedCol.setCellValueFactory(new PropertyValueFactory<Scooter, Number>("maxSpeed"));

        ObservableList<Scooter> scootersTableList = FXCollections.observableArrayList(scooters);
        scooterTable.setEditable(true);
        scooterTable.setItems(scootersTableList);
        scooterTable.getColumns().addAll(scooterIDCol, scooterManufacturerCol, scooterModelCol, scooterPriceCol, scooterBatteryCol, scooterMaxSpeedCol);

        TableColumn<Bicycle, String> bicycleIDCol = new TableColumn<Bicycle, String>("Vehicle ID");
        TableColumn<Bicycle, String> bicycleManufacturerCol = new TableColumn<Bicycle, String>("Manufacturer");
        TableColumn<Bicycle, String> bicycleModelCol = new TableColumn<Bicycle, String>("Model");
        TableColumn<Bicycle, Number> bicyclePriceCol = new TableColumn<Bicycle, Number>("Price");
        TableColumn<Bicycle, Number> bicycleBatteryCol = new TableColumn<Bicycle, Number>("Battery level");
        TableColumn<Bicycle, Number> bicycleRangeCol = new TableColumn<Bicycle, Number>("Range");

        bicycleIDCol.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("ID"));
        bicycleManufacturerCol.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("manufacturer"));
        bicycleModelCol.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("model"));
        bicyclePriceCol.setCellValueFactory(new PropertyValueFactory<Bicycle, Number>("price"));
        bicycleBatteryCol.setCellValueFactory(new PropertyValueFactory<Bicycle, Number>("batteryLevel"));
        bicycleRangeCol.setCellValueFactory(new PropertyValueFactory<Bicycle, Number>("range"));

        ObservableList<Bicycle> bikesTableList = FXCollections.observableArrayList(bikes);
        bicycleTable.setEditable(true);
        bicycleTable.setItems(bikesTableList);
        bicycleTable.getColumns().addAll(bicycleIDCol, bicycleManufacturerCol, bicycleModelCol, bicyclePriceCol, bicycleBatteryCol, bicycleRangeCol);

        carTable.setFixedCellSize(25);
        carTable.prefHeightProperty().bind(carTable.fixedCellSizeProperty().multiply(Bindings.size(carTable.getItems()).add(1.01)));
        carTable.minHeightProperty().bind(carTable.prefHeightProperty());
        carTable.maxHeightProperty().bind(carTable.prefHeightProperty());

        scooterTable.setFixedCellSize(25);
        scooterTable.prefHeightProperty().bind(scooterTable.fixedCellSizeProperty().multiply(Bindings.size(scooterTable.getItems()).add(1.01)));
        scooterTable.minHeightProperty().bind(scooterTable.prefHeightProperty());
        scooterTable.maxHeightProperty().bind(scooterTable.prefHeightProperty());

        bicycleTable.setFixedCellSize(25);
        bicycleTable.prefHeightProperty().bind(bicycleTable.fixedCellSizeProperty().multiply(Bindings.size(bicycleTable.getItems()).add(1.01)));
        bicycleTable.minHeightProperty().bind(bicycleTable.prefHeightProperty());
        bicycleTable.maxHeightProperty().bind(bicycleTable.prefHeightProperty());

        tableBox.setLeft(carTable);
        tableBox.setRight(scooterTable);
        tableBox.setBottom(bicycleTable);

        BorderPane tablePane = new BorderPane();
        tablePane.setCenter(tableBox);

        return tablePane;
    }

    /**
     * Metoda koja stvara prikaz svih kvarova koji postoje u trenutnom datum-vremenu
     * @param rentals Lista iznajmljivanja za trenutno datum-vrijeme
     * @param simFinished Provjera zavrsetka simulacije
     * @return Borderpane koji u sebi sadrzi ili TableView svih kvarova i informacija o njima ili TextArea u slucaju da nema kvarova ili da je simulacija gotova
     */
    public static BorderPane createMalfunctionTable(List<Rental> rentals, Boolean simFinished){
        BorderPane malfunctionPane = new BorderPane();
        TextArea malfunctionDone = new TextArea(simFinished?"Simulation done, no malfunctions to display!":"No malfunctions in current datetime!");
        TableView<Rental> malfunctionTable = new TableView<Rental>();

        malfunctionDone.setEditable(false);

        List<Rental> malfRentals = new ArrayList<Rental>();
        for(Rental r: rentals){
            if(r.isMalfunction())
                malfRentals.add(r);
        }

        if(malfRentals.isEmpty() || simFinished){
            malfunctionPane.setCenter(malfunctionDone);
            return malfunctionPane;
        }

        TableColumn<Rental, String> vehicleTypeCol= new TableColumn<Rental, String>("Vehicle type");
        TableColumn<Rental, String> IDCol = new TableColumn<Rental, String>("Vehicle ID");
        TableColumn<Rental, String> malfunctionTimeCol = new TableColumn<Rental, String>("Malfunction time");
        TableColumn<Rental, String> malfunctionDescCol= new TableColumn<Rental, String>("Malfunction description");

        vehicleTypeCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("rentedVehicleType"));
        IDCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("VehicleID"));
        malfunctionTimeCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("date"));
        malfunctionDescCol.setCellValueFactory(new PropertyValueFactory<Rental, String>("malfunctionDesc"));

        ObservableList<Rental> malfunctionsList = FXCollections.observableArrayList(malfRentals);
        malfunctionTable.setEditable(true);
        malfunctionTable.setItems(malfunctionsList);
        malfunctionTable.getColumns().addAll(vehicleTypeCol, IDCol, malfunctionTimeCol, malfunctionDescCol);

        malfunctionTable.setFixedCellSize(25);
        malfunctionTable.prefHeightProperty().bind(malfunctionTable.fixedCellSizeProperty().multiply(Bindings.size(malfunctionTable.getItems()).add(1.01)));
        malfunctionTable.minHeightProperty().bind(malfunctionTable.prefHeightProperty());
        malfunctionTable.maxHeightProperty().bind(malfunctionTable.prefHeightProperty());

        malfunctionPane.setCenter(malfunctionTable);

        return malfunctionPane;
    }

    /**
     * Metoda koja stvara tabelu svih poslovnih statistika kompanije do trenutnog datum-vremena
     * @param company Kompanija u kojoj se racuna statistika poslovanja
     * @return BorderPane koji sadrzi TableView sa sumarnim i dnevnim statistikama
     */
    public static BorderPane createStatisticsView(Company company){
        List<Statistic> statisticsList =  company.calculateBusinessStatistics();
        BorderPane statisticsView = new BorderPane();
        TableView<Statistic> statisticTable = new TableView<Statistic>();

        TableColumn<Statistic, String> dayCol = new TableColumn<Statistic, String>("Day");
        TableColumn<Statistic, String> revenueCol = new TableColumn<Statistic, String>("Revenue");
        TableColumn<Statistic, String> discountsCol = new TableColumn<Statistic, String>("Discounts");
        TableColumn<Statistic, String> promotionsCol = new TableColumn<Statistic, String>("Promotions");
        TableColumn<Statistic, String> distanceWideCol = new TableColumn<Statistic, String>("DistanceWide");
        TableColumn<Statistic, String> distanceNarrowCol = new TableColumn<Statistic, String>("DistanceNarrow");
        TableColumn<Statistic, String> repairsCol = new TableColumn<Statistic, String>("Repairs");
        TableColumn<Statistic, String> maintenanceCol = new TableColumn<Statistic, String>("Maintenance");
        TableColumn<Statistic, String> expenditureCol = new TableColumn<Statistic, String>("Expenditure");

        dayCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("day"));
        revenueCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("revenue"));
        discountsCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("discounts"));
        promotionsCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("promotions"));
        distanceWideCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("distanceWide"));
        distanceNarrowCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("distanceNarrow"));
        repairsCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("repairs"));
        maintenanceCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("maintenance"));
        expenditureCol.setCellValueFactory(new PropertyValueFactory<Statistic, String>("expenditure"));

        ObservableList<Statistic> statisticObservableList = FXCollections.observableArrayList(statisticsList);
        statisticTable.setItems(statisticObservableList);
        statisticTable.getColumns().addAll(dayCol, revenueCol, discountsCol, promotionsCol, distanceWideCol, distanceNarrowCol, repairsCol, maintenanceCol, expenditureCol);
        statisticsView.setCenter(statisticTable);

        return statisticsView;
    }

    /**
     * Metoda koja stvara tabelu svih vozila sa kvarovima i cijenama popravke
     * @param vehicles Lista vozila sa kvarovima
     * @return Borderpane koji sadrzi TableView u kojem se nalaze informacije o vozilima sa kvarovima
     */
    public static BorderPane createMalfunctionVehiclesTable(List<Vehicle> vehicles){
        List<Car> cars = new ArrayList<Car>();
        List<Scooter> scooters = new ArrayList<Scooter>();
        List<Bicycle> bikes = new ArrayList<Bicycle>();

        for(Vehicle v: vehicles){
            if(v instanceof Car){
                cars.add((Car)v);
            }
            if(v instanceof Scooter){
                scooters.add((Scooter)v);
            }
            if(v instanceof Bicycle){
                bikes.add((Bicycle)v);
            }
        }

        BorderPane tableBox = new BorderPane();
        TableView<Car> carTable = new TableView<Car>();
        TableView<Scooter> scooterTable = new TableView<Scooter>();
        TableView<Bicycle> bicycleTable = new TableView<Bicycle>();

        TableColumn<Car, String> carIDCol = new TableColumn<Car, String>("Vehicle ID");
        TableColumn<Car, String> carModelCol = new TableColumn<Car, String>("Model");
        TableColumn<Car, Double> carPriceCol = new TableColumn<Car, Double>("Acquisition Price");
        TableColumn<Car, String> carRepairPriceCol = new TableColumn<Car, String>("Repair Cost");

        carIDCol.setCellValueFactory(new PropertyValueFactory<Car, String>("ID"));
        carModelCol.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
        carPriceCol.setCellValueFactory(new PropertyValueFactory<Car, Double>("price"));
        carRepairPriceCol.setCellValueFactory(new PropertyValueFactory<Car, String>("repairPrice"));


        ObservableList<Car> carsTableList = FXCollections.observableArrayList(cars);
        carTable.setEditable(true);
        carTable.setItems(carsTableList);
        carTable.getColumns().addAll(carIDCol, carModelCol, carPriceCol, carRepairPriceCol);


        TableColumn<Scooter, String> scooterIDCol = new TableColumn<Scooter, String>("Vehicle ID");
        TableColumn<Scooter, String> scooterModelCol = new TableColumn<Scooter, String>("Model");
        TableColumn<Scooter, Number> scooterPriceCol = new TableColumn<Scooter, Number>("Acquisition Price");
        TableColumn<Scooter, String> scooterRepairPriceCol = new TableColumn<Scooter, String>("Repair Cost");


        scooterIDCol.setCellValueFactory(new PropertyValueFactory<Scooter, String>("ID"));
        scooterModelCol.setCellValueFactory(new PropertyValueFactory<Scooter, String>("model"));
        scooterPriceCol.setCellValueFactory(new PropertyValueFactory<Scooter, Number>("price"));
        scooterRepairPriceCol.setCellValueFactory(new PropertyValueFactory<Scooter, String>("repairPrice"));

        ObservableList<Scooter> scootersTableList = FXCollections.observableArrayList(scooters);
        scooterTable.setEditable(true);
        scooterTable.setItems(scootersTableList);
        scooterTable.getColumns().addAll(scooterIDCol, scooterModelCol, scooterPriceCol, scooterRepairPriceCol);

        TableColumn<Bicycle, String> bicycleIDCol = new TableColumn<Bicycle, String>("Vehicle ID");
        TableColumn<Bicycle, String> bicycleModelCol = new TableColumn<Bicycle, String>("Model");
        TableColumn<Bicycle, Number> bicyclePriceCol = new TableColumn<Bicycle, Number>("Acquisition Price");
        TableColumn<Bicycle, String> bicycleRepairPriceCol = new TableColumn<Bicycle, String>("Repair Cost");

        bicycleIDCol.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("ID"));
        bicycleModelCol.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("model"));
        bicyclePriceCol.setCellValueFactory(new PropertyValueFactory<Bicycle, Number>("price"));
        bicycleRepairPriceCol.setCellValueFactory(new PropertyValueFactory<Bicycle, String>("repairPrice"));

        ObservableList<Bicycle> bikesTableList = FXCollections.observableArrayList(bikes);
        bicycleTable.setEditable(true);
        bicycleTable.setItems(bikesTableList);
        bicycleTable.getColumns().addAll(bicycleIDCol, bicycleModelCol, bicyclePriceCol, bicycleRepairPriceCol);

        carTable.setFixedCellSize(25);
        scooterTable.setFixedCellSize(25);
        bicycleTable.setFixedCellSize(25);

        tableBox.setLeft(carTable);
        tableBox.setRight(scooterTable);
        tableBox.setCenter(bicycleTable);

        BorderPane tablePane = new BorderPane();
        tablePane.setCenter(tableBox);

        tablePane.setMaxHeight(750);
        tablePane.setMinWidth(800);

        return tablePane;
    }
}



