package Visualization;

import Project.*;
import Simulation.SimulationEngine;
import Simulation.SimulationParameters;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application implements IMapObserver {
    private IWorldMap map;
    private Thread engineThread;
    private SimulationEngine engine;
    private VBox statistics;
    private Button pauseButton;
    private int squareSize;
    private GraphicsContext WorldMap;
    private Canvas mapCanvas;
    @Override
    public void init() throws Exception {
        SimulationParameters params = new SimulationParameters(50, 100,1, 10, 10, 5, 10, 1, 1, 0, 0);
        map = new EarthMap(100, 100);
        map.addMapObserver(this);
        engine = new SimulationEngine(map, params);
        engineThread = new Thread(engine);
        this.squareSize = calculateSquareSize(map);
        mapCanvas = new Canvas();
        mapCanvas.setWidth(map.getSize().x * squareSize);
        mapCanvas.setHeight(map.getSize().y * squareSize);
        WorldMap = mapCanvas.getGraphicsContext2D();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Simulation");
        VBox mapBox = new VBox();
        mapBox.getChildren().add(mapCanvas);
        mapBox.setLayoutX(1400 - mapCanvas.getWidth());
        mapBox.setAlignment(Pos.CENTER);

        statistics = new VBox();
        statistics.setAlignment(Pos.CENTER);

        createPauseButton();

        VBox LeftBox = new VBox(statistics, pauseButton);
        LeftBox.setAlignment(Pos.CENTER);
        LeftBox.setLayoutX(200);
        Group root = new Group(LeftBox, mapBox);
        Scene scene = new Scene(root, 1400, 750);
//        scene.setFill(Color.web("#A4EBA2"));
        engineThread.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void createPauseButton() {
        pauseButton = new Button("Pause Simulation");
        pauseButton.setMinWidth(200);
        pauseButton.setOnAction(event -> {
            if (engine.isSimPaused()) {
                engine.pauseSim(false);
                pauseButton.setText("Pause Simulation");
            } else {
                engine.pauseSim(true);
                pauseButton.setText("Resume simulation");
            }
        });
    }
    private void drawStats(VBox statistics) {
        statistics.getChildren().clear();

        statistics.getChildren().add(new Label("Sample Text"));
        statistics.getChildren().add(new Label("Number of animals on map: " + engine.getAnimalsOnMap()));
        statistics.getChildren().add(new Label("Simulation Age: " + engine.getSimAge()));
        statistics.getChildren().add(new Label("Grass on map: " + engine.getGrassOnMap()));
        statistics.setAlignment(Pos.CENTER);
    }
    private void drawMapCanvas() {
        WorldMap.setFill(Color.valueOf("#A4EBA2"));
        WorldMap.fillRect(0, 0, map.getSize().x * squareSize, map.getSize().y * squareSize);

        for (Grass grass: engine.getGrass()) {
            WorldMap.setFill(Color.valueOf("#0c4a20"));
            Vector2d position = grass.getPosition();
            WorldMap.fillOval(position.x * squareSize, position.y * squareSize, squareSize, squareSize);
        }
        for (Animal animal : engine.getAnimals()){
            WorldMap.setFill(Color.valueOf("#FF0000"));
            Vector2d position = animal.getPosition();
            WorldMap.fillOval(position.x * squareSize, position.y * squareSize, squareSize, squareSize);
        }

    }

    @Override
    public void updateMap() {
        Platform.runLater(() -> {
            drawMapCanvas();
            drawStats(statistics);
        });
    }
    private int calculateSquareSize(IWorldMap map) {
        Vector2d mapSize = map.getSize();
        if (mapSize.x > mapSize.y) {
            squareSize = 900 / mapSize.x;
        } else {
            squareSize = 750 / mapSize.y;
        }
        return squareSize;
    }
}

