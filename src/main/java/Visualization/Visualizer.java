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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

public class Visualizer implements IMapObserver {
    private IWorldMap map;
    private SimulationParameters parameters;
    private Thread engineThread;
    private SimulationEngine engine;
    private VBox statistics;
    private Text NumberOfAnimalsValue;
    private Text NumberOfGrassValue;
    private Text averageEnergy;
    private Text SimAgeValue;
    private Button pauseButton;
    private Button eraJumpButton;
    private TextField eraJumpField;
    private Button trackAnimalButton;
    private int squareSize;
    private GraphicsContext WorldMap;
    private Canvas mapCanvas;
    private String mapColor;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series<Number, Number> animalSeries;
    private XYChart.Series<Number, Number> grassSeries;
    private Animal trackedAnimal;
    private Text trackedHeading;
    private Text trackedGenotype;
    private Text trackedAge;
    private Text trackedEnergy;
    private Text trackedOffsprings;
    private Text trackedGrass;
    private Text trackedActiveGenom;
    private Text trackedAgeOfDeath;


    public void start(Stage primaryStage, SimulationParameters params){
        this.parameters = params;
        initialize(params);
        run(primaryStage);
    }
    private void initialize(SimulationParameters params){
        if (params.mapVariant == 0) {
            map = new EarthMap(params.mapHeight, params.mapWidth);
            mapColor = "#A4EBA2";
        } else {
            map = new PortalMap(params.mapHeight, params.mapWidth);
            mapColor = "#2B3768";
        }
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
        averageEnergy = new Text("-");
        trackedAnimal = null;
    }
    private void run(Stage primaryStage) {
        primaryStage.setTitle("Simulation");
        VBox mapBox = new VBox();
        mapBox.getChildren().add(mapCanvas);
        mapBox.setLayoutX(1400 - mapCanvas.getWidth());
        mapBox.setAlignment(Pos.CENTER);

        EventHandler<MouseEvent> animalSelectEvent = event -> {
            Vector2d position = new Vector2d((int)((event.getX())/squareSize), (int) event.getY()/squareSize);
            Object object =  map.objectAt(position);
            if (object instanceof Animal) {
                trackedAnimal = (Animal) object;
                updateTrackedAnimal();
            }
        };
        mapCanvas.addEventFilter(MouseEvent.MOUSE_CLICKED, animalSelectEvent);

        statistics = new VBox();
        Text statsHeading = new Text("Simulation Statistics: ");
        statsHeading.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 25));
        Text NumberOfAnimalsHeading = new Text("Number of animals on map: ");
        Text NumberOfGrassHeading = new Text("Number of grass on map: ");
        Text averageEnergyHeading = new Text("Average animal energy: ");
        Text SimAgeHeading = new Text("Simulation age: ");
        statistics.getChildren().addAll(statsHeading,
                SimAgeHeading, SimAgeValue,
                NumberOfAnimalsHeading, NumberOfAnimalsValue,
                averageEnergyHeading, averageEnergy,
                NumberOfGrassHeading, NumberOfGrassValue, createLineChart());
        statistics.setAlignment(Pos.CENTER);

        createPauseButton();
        VBox trackedAnimalBox = createTrackedAnimalBox();
//        createTrackAnimalButton();
//        createEraJumpButton();

        VBox LeftBox = new VBox(statistics, pauseButton, trackedAnimalBox);
        LeftBox.setAlignment(Pos.CENTER);
        LeftBox.setLayoutX(0);
        Group root = new Group(LeftBox, mapBox);
        Scene scene = new Scene(root, 1400, 750);
        engineThread.start();
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            engine.endSimulation();
        });
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
//    private void createTrackAnimalButton() {
//        trackAnimalButton = new Button("Track single animal");
//    }
    private VBox createTrackedAnimalBox() {

        trackedHeading = new Text("Select Animal to track: ");
        Text age = new Text("Age: ");
        trackedAge = new Text("-");
        HBox ageBox = new HBox(age, trackedAge);
        Text energy = new Text("Energy: ");
        trackedEnergy = new Text("-");
        HBox energyBox = new HBox(energy, trackedEnergy);
        Text offsprings = new Text("Number of offsprings: ");
        trackedOffsprings = new Text("-");
        HBox offspringsBox = new HBox(offsprings, trackedOffsprings);
        Text grassConsumed = new Text("Grass consumed: ");
        trackedGrass = new Text("-");
        HBox grassBox = new HBox(grassConsumed, trackedGrass);
        Text genotype = new Text("Genotype: ");
        trackedGenotype = new Text("-");
        HBox genotypeBox = new HBox(genotype, trackedGenotype);
        Text activeGenom = new Text("Currently acivated Genom: ");
        trackedActiveGenom = new Text("-");
        HBox genomBox = new HBox(activeGenom, trackedActiveGenom);
        Text ageOfDeath = new Text("Animal died at simulation age: ");
        trackedAgeOfDeath = new Text("-");
        HBox deathBox = new HBox(ageOfDeath, trackedAgeOfDeath);
        VBox trackedAnimalBox = new VBox(trackedHeading,
                ageBox, energyBox, offspringsBox, grassBox, genotypeBox, genomBox, deathBox);
        return trackedAnimalBox;
    }
    private void updateTrackedAnimal() {
        int[] genotype = trackedAnimal.getGenotype();
        float energy = trackedAnimal.getEnergy();
        int age = trackedAnimal.getAge();
        int grassConsumed = trackedAnimal.getGrassConsumed();
        int offsprings = trackedAnimal.getChildren();
        int deathAge = trackedAnimal.getDeathDate();
        trackedHeading.setText("Tracked animal Stats: ");
        trackedGenotype.setText(Arrays.toString(genotype));
        trackedAge.setText(String.valueOf(age));
        trackedEnergy.setText(String.valueOf(energy));
        trackedGrass.setText(String.valueOf(grassConsumed));
        trackedOffsprings.setText(String.valueOf(offsprings));
        trackedActiveGenom.setText("Add that later");
        if (deathAge > -1) {
            trackedAgeOfDeath.setText(String.valueOf(deathAge));
        } else {
            trackedAgeOfDeath.setText("Animal is still alive");
        }
    }
    private LineChart<Number, Number> createLineChart(){

        xAxis = new NumberAxis();
        yAxis = new NumberAxis();

        xAxis.setForceZeroInRange(false);
        xAxis.setLabel("Age");
        xAxis.setAnimated(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(50);

        yAxis.setLabel("Count");
        yAxis.setAnimated(false);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        animalSeries = new XYChart.Series<>();
        grassSeries = new XYChart.Series<>();

        animalSeries.setName("Animals");
        grassSeries.setName("Grass");

        lineChart.getData().add(animalSeries);
        lineChart.getData().add(grassSeries);

        lineChart.setCreateSymbols(false);

        lineChart.setMaxHeight(500);
        lineChart.setMaxWidth(500);

        return lineChart;
    }
    private void drawStats() {
        SimAgeValue.setText(String.valueOf(engine.getSimAge()));
        NumberOfAnimalsValue.setText(String.valueOf(engine.getAnimalsOnMap()));
        NumberOfGrassValue.setText(String.valueOf(engine.getGrassOnMap()));
        DecimalFormat df = new DecimalFormat("#.##");
        averageEnergy.setText(String.valueOf(df.format(engine.getAverageEnergy())));

        if(engine.getSimAge() % 1000 == 0){
            xAxis.setLowerBound(engine.getSimAge());
            animalSeries.getData().clear();
            grassSeries.getData().clear();
        }
        if (trackedAnimal != null) {
            updateTrackedAnimal();
        }

        xAxis.setUpperBound(engine.getSimAge());
        animalSeries.getData().add(new XYChart.Data<>(engine.getSimAge(), engine.getAnimalsOnMap()));
        grassSeries.getData().add(new XYChart.Data<>(engine.getSimAge(), engine.getGrassOnMap()));
    }
    private void drawMapCanvas() {
        WorldMap.setFill(Color.valueOf(mapColor));
        WorldMap.fillRect(0, 0, map.getSize().x * squareSize, map.getSize().y * squareSize);

        for (Grass grass: engine.getGrass()) {
            WorldMap.setFill(Color.valueOf("#33F03E"));
            Vector2d position = grass.getPosition();
            WorldMap.fillOval(position.x * squareSize, position.y * squareSize, squareSize, squareSize);
        }
        for (Animal animal : engine.getAnimals()){
            WorldMap.setFill(Color.valueOf(energyToColor(animal)));
            Vector2d position = animal.getPosition();
            WorldMap.fillOval(position.x * squareSize, position.y * squareSize, squareSize, squareSize);
        }
    }
    private String energyToColor(Animal animal) {
        float energy = animal.getEnergy();
        String color;
        if (energy > parameters.startingEnergy) {
            color = "#750000";
        } else if (energy > (parameters.startingEnergy/3) * 2){
            color = "#FF0000";
        } else if (energy > (parameters.startingEnergy/3)) {
            color = "#FF4545";
        } else {
            color = "#FE8C8C";
        }
        return color;
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