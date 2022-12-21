package Visualization;

import Project.*;
import Simulation.SimulationEngine;
import Simulation.SimulationParameters;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Visualizer implements IMapObserver {
    private IWorldMap map;
    private Thread engineThread;
    private SimulationEngine engine;
    private VBox statistics;
    private Text NumberOfAnimalsValue;
    private Text NumberOfGrassValue;
    private Text SimAgeValue;
    private Button pauseButton;
    private Button eraJumpButton;
    private TextField eraJumpField;
    private int squareSize;
    private GraphicsContext WorldMap;
    private Canvas mapCanvas;
    public void start(Stage primaryStage){
        initialize();
        run(primaryStage);
    }
    private void initialize(){
        SimulationParameters params = new SimulationParameters(50, 100,1, 10, 10, 5, 20, 1, 1, 0, 0);
        map = new EarthMap(100, 100);
        map.addMapObserver(this);
        engine = new SimulationEngine(map, params);
        engineThread = new Thread(engine);
        this.squareSize = calculateSquareSize(map);
        mapCanvas = new Canvas();
        mapCanvas.setWidth(map.getSize().x * squareSize);
        mapCanvas.setHeight(map.getSize().y * squareSize);
        WorldMap = mapCanvas.getGraphicsContext2D();

        SimAgeValue = new Text("-");
        NumberOfAnimalsValue = new Text("-");
        NumberOfGrassValue = new Text("-");
    }
    private void run(Stage primaryStage) {
        primaryStage.setTitle("Simulation");
        VBox mapBox = new VBox();
        mapBox.getChildren().add(mapCanvas);
        mapBox.setLayoutX(1400 - mapCanvas.getWidth());
        mapBox.setAlignment(Pos.CENTER);

        statistics = new VBox();
        Text statsHeading = new Text("Simulation Statistics: ");
        statsHeading.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 25));
        Text NumberOfAnimalsHeading = new Text("Number of animals on map: ");
        Text NumberOfGrassHeading = new Text("Number of grass on map: ");
        Text SimAgeHeading = new Text("Simulation age: ");
        statistics.getChildren().addAll(statsHeading,
                SimAgeHeading, SimAgeValue,
                NumberOfAnimalsHeading, NumberOfAnimalsValue,
                NumberOfGrassHeading, NumberOfGrassValue);
        statistics.setAlignment(Pos.CENTER);

        createPauseButton();
        createEraJumpButton();

        VBox LeftBox = new VBox(statistics, pauseButton, eraJumpButton, eraJumpField);
        LeftBox.setAlignment(Pos.CENTER);
        LeftBox.setLayoutX(200);
        Group root = new Group(LeftBox, mapBox);
        Scene scene = new Scene(root, 1400, 750);
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
    private void createEraJumpButton() {
        eraJumpButton = new Button("Jump simulation eras");
        eraJumpField = new TextField("Input number of ages to skip");
        eraJumpButton.setMinWidth(200);

        eraJumpButton.setOnAction(event -> {
            if (engine.isSimPaused()) {
                int agesToJump = Integer.parseInt(eraJumpField.getText());
                engine.skipAges(agesToJump);
            }
        });
    }
    private void drawStats() {
        SimAgeValue.setText(String.valueOf(engine.getSimAge()));
        NumberOfAnimalsValue.setText(String.valueOf(engine.getAnimalsOnMap()));
        NumberOfGrassValue.setText(String.valueOf(engine.getGrassOnMap()));
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
            drawStats();
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