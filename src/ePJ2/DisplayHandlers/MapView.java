package ePJ2.DisplayHandlers;

import java.util.concurrent.FutureTask;

import ePJ2.Clock.Clock;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MapView extends Application {

    Clock clock;
    Stage primaryStage;

    
    Scene map, scene2;

    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 30;
    

    public MapView(Clock clock){
        super();
        this.clock = clock;
    }

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

    }

//    public void update() throws InterruptedException{
//        FutureTask<Void> updateTask = new FutureTask<Void>(() -> {
//            Button buttonS1 = new Button("Switch to Scene 2");
//            Button buttonS2 = new Button("Switch to Scene 1 okay smile");

//            buttonS1.setOnAction(e -> {
//                SceneHandler.switchScene(primaryStage, scene2);
//            });

//            buttonS2.setOnAction(e -> {
//                SceneHandler.switchScene(primaryStage, map);
//            });

//            BorderPane gridBorderPane = SceneHandler.createGrid(20, 20, 30, clock.getCompany().getRentalLists().get(clock.getDayCounter()));
//            gridBorderPane.setTop(buttonS1);
//            map = new Scene(gridBorderPane, GRID_SIZE * (CELL_SIZE + 3), GRID_SIZE * (CELL_SIZE + 4));

//            primaryStage.setTitle("Map View");
//            primaryStage.setScene(map);
//            primaryStage.show();

//            return null;
//        });

//        Platform.runLater(updateTask);
//    }
}
