package Visualization;

import IO.ParametersWriter;
import Simulation.SimulationParameters;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private ComboBox<String> simulationPresets;
    private CheckBox writeToCSVBox;


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

        Text gameVariantText = new Text("Select game variants");
        gameVariantText.setFont(Font.font(20));

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

        writeToCSVBox = new CheckBox("Save data to csv file");
        VBox rightBox = new VBox(
                gameVariantText, mapVarBox, grassVarBox, mutationsVarBox, behaviourVarBox, fileSection, writeToCSVBox
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
        int mutationsOccurring = Integer.parseInt(mutationsNumber.getText());

        if (mutationsOccurring > genotypeLen) {
            throw new NumberFormatException("Number of mutations occurring is larger than animal's genotype");
        }
        if (breedingEnergyCost > breedingRequiredEnergy) {
            throw new NumberFormatException("Breeding energy cost is higher than energy required to breed");
        }
        int mapHeight = Integer.parseInt(mapHeightValue.getText());
        int mapWidth = Integer.parseInt(mapWidthValue.getText());
        int sleepTime = Integer.parseInt(simSleepTime.getText());
        int mapVal = saveComboBoxParameter(mapVariant);
        int grassVal = saveComboBoxParameter(grassVariant);
        int mutationVal = saveComboBoxParameter(mutationsVariant);
        int moveVal = saveComboBoxParameter(behaviourVariant);

        boolean writeToCSV = writeToCSVBox.isSelected();

        return new SimulationParameters(animalsNo, animalsEnergy, dailyEnergy,
                genotypeLen, startingGrass, growingGrass, grassE, breedingEnergyCost, breedingRequiredEnergy,
                mutationsOccurring, mutationVal, mapHeight, mapWidth, mapVal, moveVal, sleepTime, grassVal, writeToCSV
                );
    }
    private void loadParametersFromFile(String fileName) {
        try {
            String filePath = "./src/main/resources/Presets/";
            BufferedReader reader = new BufferedReader(new FileReader(filePath + fileName));
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

            String mapVar = reader.readLine();
            loadComboBoxParameters(mapVar, mapVariant);
            String grassVar = reader.readLine();
            loadComboBoxParameters(grassVar, grassVariant);
            String mutationVar = reader.readLine();
            loadComboBoxParameters(mutationVar, mutationsVariant);
            String behaviourVar = reader.readLine();
            loadComboBoxParameters(behaviourVar, behaviourVariant);


            reader.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
    private int saveComboBoxParameter(ComboBox<String> comboBox) throws NumberFormatException {
        String variant = behaviourVariant.getValue();
        if (variant == null) {
            throw new NumberFormatException();
        }
        int value;
        if (comboBox.getSelectionModel().isSelected(0)) {
            value = 0;
        } else {
            value = 1;
        }
        return value;
    }
    private void loadComboBoxParameters(String value, ComboBox<String> comboBox) {
        if (value.equals("0")) {
            comboBox.getSelectionModel().select(0);
        } else {
            comboBox.getSelectionModel().select(1);
        }
    }
    private VBox createSaveLoad() {
        Text saveLoadText = new Text("Save and Load presets");
        saveLoadText.setFont(Font.font(20));
        Button loadButton = new Button("Load Settings from a File");
        Button saveButton = new Button("Save Settings to a File");
        Text filePathText = new Text("Input the file name and click Save to save current preset: ");
        Text loadFileText = new Text("Select a preset from the list and click load to load preset: ");
        TextField filePath = new TextField();
        createPresetsBox();

        loadButton.setOnAction(event -> {
            loadParametersFromFile(simulationPresets.getValue());
        });
        saveButton.setOnAction(event -> {
            ParametersWriter writer = new ParametersWriter();

            writer.write(readParameters(), filePath.getText());
            updatePresetBox();
        });

        VBox root = new VBox(saveLoadText, filePathText, filePath, saveButton, loadFileText, simulationPresets, loadButton);
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);

        return root;
    }
    private void createPresetsBox() {
        simulationPresets = new ComboBox<>();
        Set<String> files = readFilesFromDirectory("./src/main/resources/Presets");
        for (String file: files) {
            simulationPresets.getItems().add(file);
        }
    }
    private void updatePresetBox() {
        Set<String> files = readFilesFromDirectory("./src/main/resources/Presets");
        simulationPresets.getItems().clear();
        for (String file: files) {
            simulationPresets.getItems().add(file);
        }
    }
    private Set<String> readFilesFromDirectory(String filePath) {
        return Stream.of(Objects.requireNonNull(new File(filePath).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
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

