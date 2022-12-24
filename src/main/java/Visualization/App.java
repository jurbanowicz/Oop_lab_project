package Visualization;

import IO.ParametersWriter;
import Simulation.SimulationParameters;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    private TextField NumberOfAnimals;
    private TextField animalStartingEnergy;
    private TextField dailyEnergyCost;
    private TextField mapHeightValue;
    private TextField mapWidthValue;
    private TextField genotypeLength;
    private TextField grassAmount;
    private TextField grassGrowing;
    private TextField grassEnergy;
    private TextField breedingCost;
    private TextField breedingEnergy;
    private TextField mutationsNumber;
    private TextField simSleepTime;
    private ComboBox<String> mapVariant;
    private ComboBox<String> grassVariant;
    private ComboBox<String> mutationsVariant;
    private ComboBox<String> behaviourVariant;


    @Override
    public void start(Stage primaryStage) throws Exception {
        startingScreen(primaryStage);
    }
    private void startingScreen(Stage primaryStage) {
        Text mainText = new Text("Input Simulation parameters: ");
        mainText.setFont(Font.font(20));

        Text animalsText = new Text("Number of animals on the map: ");
        NumberOfAnimals = new TextField();
        NumberOfAnimals.setMaxWidth(50);

        Text startingEnergyText = new Text("Animal's Starting energy: ");
        animalStartingEnergy = new TextField();
        animalStartingEnergy.setMaxWidth(50);

        Text dailyEnergyText = new Text("Daily energy cost for animals: ");
        dailyEnergyCost = new TextField();
        dailyEnergyCost.setMaxWidth(50);

        Text genotypelenghtText = new Text("Animal's genotype lenght: ");
        genotypeLength = new TextField();
        genotypeLength.setMaxWidth(50);

        Text grassAmountText = new Text("Starting grass amount: ");
        grassAmount = new TextField();
        grassAmount.setMaxWidth(50);

        Text grassGrowingText = new Text("Amount of grass to grow each day: ");
        grassGrowing = new TextField();
        grassGrowing.setMaxWidth(50);

        Text grassEnergyText = new Text("Amount of energy that grass provides: ");
        grassEnergy = new TextField();
        grassEnergy.setMaxWidth(50);

        Text breedingCostText = new Text("Breeding energy cost for animals: ");
        breedingCost = new TextField();
        breedingCost.setMaxWidth(50);

        Text breedingEnergyText = new Text("Energy required for breeding: ");
        breedingEnergy = new TextField();
        breedingEnergy.setMaxWidth(50);

        Text mutationsNumberText = new Text("Number of mutations occuring: ");
        mutationsNumber = new TextField();
        mutationsNumber.setMaxWidth(50);

        Text simSleepTimeText = new Text("Simulation speed (ms): ");
        simSleepTime = new TextField();
        simSleepTime.setMaxWidth(50);


        Text mapHeight = new Text("Map Height: ");
        mapHeightValue = new TextField();
        mapHeightValue.setMaxWidth(50);
        Text mapWidth = new Text("Map Width");
        mapWidthValue = new TextField();
        mapWidthValue.setMaxWidth(50);

        Button startButton = new Button("Start");

        Text gameVariantText = new Text("Select game variants: ");

        mapVariant = new ComboBox<String>();
        mapVariant.getItems().add("Earth Map");
        mapVariant.getItems().add("Hell's Portal Map");
        mapVariant.setMinWidth(200);
        Text mapVariantText = new Text("Map Variant: ");
        VBox mapVarBox = new VBox(mapVariantText, mapVariant);
        mapVarBox.setAlignment(Pos.CENTER);

        grassVariant = new ComboBox<String>();
        grassVariant.getItems().add("Arboreous Equator");
        grassVariant.getItems().add("Deadly Corpses");
        grassVariant.setMinWidth(200);
        Text grassVariantText = new Text("Grass Spawning Variant: ");
        VBox grassVarBox = new VBox(grassVariantText, grassVariant);
        grassVarBox.setAlignment(Pos.CENTER);

        mutationsVariant = new ComboBox<String>();
        mutationsVariant.getItems().add("Fully Randomized");
        mutationsVariant.getItems().add("Slight Corrections");
        mutationsVariant.setMinWidth(200);
        Text mutationsVarText = new Text("Mutations Variant: ");
        VBox mutationsVarBox = new VBox(mutationsVarText, mutationsVariant);
        mutationsVarBox.setAlignment(Pos.CENTER);

        behaviourVariant = new ComboBox<String>();
        behaviourVariant.getItems().add("Full predestination");
        behaviourVariant.getItems().add("A touch of craziness");
        behaviourVariant.setMinWidth(200);
        Text behaviourVarText = new Text("Animal Behaviour: ");
        VBox behaviourVarBox = new VBox(behaviourVarText, behaviourVariant);
        behaviourVarBox.setAlignment(Pos.CENTER);

        VBox fileSection = createSaveLoad();


        startButton.setOnAction(event -> {
            try {
                SimulationParameters params = readParameters();
                Visualizer visualizer = new Visualizer();
                visualizer.start(new Stage(), params);
            } catch (NumberFormatException e) {
                ErrorScreen();
                System.out.print(e.getMessage());
            }
        });

        Button gameInfoButton = new Button("More Infromations");

        gameInfoButton.setOnAction(event -> {
            getInfoScreen();
        });

        HBox buttons = new HBox(gameInfoButton, startButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        VBox leftBox = new VBox(
                animalsText, NumberOfAnimals,
                startingEnergyText, animalStartingEnergy,
                dailyEnergyText, dailyEnergyCost,
                genotypelenghtText, genotypeLength,
                grassAmountText, grassAmount,
                grassGrowingText, grassGrowing,
                grassEnergyText, grassEnergy,
                breedingCostText, breedingCost,
                breedingEnergyText, breedingEnergy,
                mutationsNumberText, mutationsNumber,
                mapHeight, mapHeightValue,
                mapWidth, mapWidthValue,
                simSleepTimeText, simSleepTime
        );
        leftBox.setAlignment(Pos.CENTER);

        VBox rightBox = new VBox(
                gameVariantText, mapVarBox, grassVarBox, mutationsVarBox, behaviourVarBox, fileSection
        );
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setSpacing(20);

        HBox inputs = new HBox(leftBox, rightBox);
        inputs.setAlignment(Pos.CENTER);
        inputs.setSpacing(50);


        VBox root = new VBox(
                mainText, inputs, buttons
        );
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        Scene startingScreen = new Scene(root, 600, 700);

        primaryStage.setTitle("Simulation Settings");

        primaryStage.setScene(startingScreen);
        primaryStage.show();
    }
    private SimulationParameters readParameters() throws NumberFormatException{
        int animalsNo = Integer.parseInt(NumberOfAnimals.getText());
        float animalsEnergy = Float.parseFloat(animalStartingEnergy.getText());
        int dailyEnergy = Integer.parseInt(dailyEnergyCost.getText());
        int genotypeLen = Integer.parseInt(genotypeLength.getText());
        int startingGrass = Integer.parseInt(grassAmount.getText());
        int growingGrass = Integer.parseInt(grassGrowing.getText());
        int grassE = Integer.parseInt(grassEnergy.getText());
        int breedingEnergyCost = Integer.parseInt(breedingCost.getText());
        int breedingRequiredEnergy = Integer.parseInt(breedingEnergy.getText());
        int mutationsOccuring = Integer.parseInt(mutationsNumber.getText());

        if (mutationsOccuring > genotypeLen) {
            throw new NumberFormatException("Number of mutations occuring is larger than animal's genotype");
        }
        if (breedingEnergyCost > breedingRequiredEnergy) {
            throw new NumberFormatException("Breeding energy cost is higher tahn emergy required to breed");
        }


        String mutationsVar = mutationsVariant.getValue();
        int mutationVal;
        if (mutationsVar == null) {
            throw new NumberFormatException();
        }
        if (Objects.equals(mutationsVar, "Fully Randomized")) {
            mutationVal = 0;
        } else {
            mutationVal = 1;
        }

        int mapHeight = Integer.parseInt(mapHeightValue.getText());
        int mapWidth = Integer.parseInt(mapWidthValue.getText());

        String mapVar = mapVariant.getValue();
        if (mapVar == null) {
            throw new NumberFormatException();
        }
        int mapVal;
        if (Objects.equals(mapVar, "Earth Map")) {
            mapVal = 0;
        } else {
            mapVal = 1;
        }

        String moveVar = behaviourVariant.getValue();
        if (moveVar == null) {
            throw new NumberFormatException();
        }
        int moveVal;
        if (Objects.equals(moveVar, "Full predestination")) {
            moveVal = 0;
        } else {
            moveVal = 1;
        }

        int sleepTime = Integer.parseInt(simSleepTime.getText());

        return new SimulationParameters(animalsNo, animalsEnergy, dailyEnergy,
                genotypeLen, startingGrass, growingGrass, grassE, breedingEnergyCost, breedingRequiredEnergy,
                mutationsOccuring, mutationVal, mapHeight, mapWidth, mapVal, moveVal, sleepTime
                );
    }
    private void loadParametersFromFile(String fileName) {
        try {
            String filePath = "/Users/jacekurbanowicz/Desktop/WIET/Obiektowe/oop_project_1/src/main/resources/SimSpecs/";
            BufferedReader reader = new BufferedReader(new FileReader(filePath + fileName + ".txt"));
            NumberOfAnimals.setText(reader.readLine());
            animalStartingEnergy.setText(reader.readLine());
            dailyEnergyCost.setText(reader.readLine());
            genotypeLength.setText(reader.readLine());
            grassAmount.setText(reader.readLine());
            grassGrowing.setText(reader.readLine());
            grassEnergy.setText(reader.readLine());
            breedingCost.setText(reader.readLine());
            breedingEnergy.setText(reader.readLine());
            mutationsNumber.setText(reader.readLine());
            mapHeightValue.setText(reader.readLine());
            mapWidthValue.setText(reader.readLine());
            simSleepTime.setText(reader.readLine());



        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
    private VBox createSaveLoad() {
        Button loadButton = new Button("Load Settings from a File");
        Button saveButton = new Button("Save Settings to a File");
        Text filePathText = new Text("Input the file name");
        TextField filePath = new TextField();

        loadButton.setOnAction(event -> {
            loadParametersFromFile(filePath.getText());
        });
        saveButton.setOnAction(event -> {
            ParametersWriter writer = new ParametersWriter();
            writer.write(readParameters(), filePath.getText());
        });
        VBox root = new VBox(filePathText, filePath, loadButton, saveButton);
        root.setAlignment(Pos.CENTER);

        return root;
    }
    private void ErrorScreen() {
        Stage stage = new Stage();
        Text error = new Text("Input correct values");
        VBox root = new VBox(error);
        root.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(root, 200, 100));
        stage.setTitle("INPUT ERROR");
        stage.show();
    }
    private void getInfoScreen() {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(root, 500,500));
        stage.setTitle("Game Information");
        stage.show();
    }
}

