package Visualization;

import Project.*;
import Simulation.SimulationEngine;
import Simulation.SimulationParameters;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {
//    @Override
//    public void init() throws Exception {
////        SimulationParameters params = new SimulationParameters(50, 100,1, 10, 10, 5, 20, 1, 1, 0, 0);
////        map = new EarthMap(100, 100);
////        map.addMapObserver(this);
////        engine = new SimulationEngine(map, params);
////        engineThread = new Thread(engine);
////        this.squareSize = calculateSquareSize(map);
////        mapCanvas = new Canvas();
////        mapCanvas.setWidth(map.getSize().x * squareSize);
////        mapCanvas.setHeight(map.getSize().y * squareSize);
////        WorldMap = mapCanvas.getGraphicsContext2D();
////
////        SimAgeValue = new Text("-");
////        NumberOfAnimalsValue = new Text("-");
////        NumberOfGrassValue = new Text("-");
//    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        startingScreen(primaryStage);
    }
    private void startingScreen(Stage primaryStage) {

        Text mapHeight = new Text("Map Height: ");
        TextField mapHeightValue = new TextField();
        Text mapWidth = new Text("Map Width");
        TextField mapWidthValue = new TextField();
        Button startButton = new Button("Start");
        startButton.setOnAction((EventHandler<ActionEvent>) event -> {
//            for (int i = 0; i < 2; i++) {
                Visualizer visualizer = new Visualizer();
                visualizer.start(new Stage());
//            }
        });

        VBox inputs = new VBox(mapHeight, mapHeightValue, mapWidth, mapWidthValue, startButton);
        Scene startingScreen = new Scene(inputs, 500, 500);

        primaryStage.setScene(startingScreen);
        primaryStage.show();
    }
}

