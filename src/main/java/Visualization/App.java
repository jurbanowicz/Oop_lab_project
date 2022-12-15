package Visualization;

import Project.*;
import Simulation.SimulationEngine;
import Simulation.SimulationParameters;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.print.DocFlavor;
import java.net.URL;

public class App extends Application implements IMapObserver {
    private IWorldMap map;
    private Thread engineThread;
    private SimulationEngine engine;
    private GridPane gridPane;
    private VBox statistics;
    private Button pauseButton;
    @Override
    public void init() throws Exception {
        SimulationParameters params = new SimulationParameters(10, 50, 5, 10, 1, 10, 5, 20);
        map = new EarthMap(10, 20);
        map.addMapObserver(this);
        engine = new SimulationEngine(map, params);
        engineThread = new Thread(engine);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Simulation");
        gridPane = new GridPane();
        VBox mapBox = new VBox();
        mapBox.getChildren().add(gridPane);
        mapBox.setLayoutX(500);

        statistics = new VBox();
        statistics.setAlignment(Pos.CENTER);
        pauseButton = new Button("Pause Simulation");
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (engine.isSimPaused()) {
                    engine.pauseSim(false);
                    pauseButton.setText("Pause Simulation");
                } else {
                    engine.pauseSim(true);
                    pauseButton.setText("Resume simulation");
                }
            }
        });

        VBox LeftBox = new VBox(statistics, pauseButton);
        LeftBox.setAlignment(Pos.CENTER);
        LeftBox.setLayoutX(150);


        Group root = new Group(LeftBox, mapBox);
        Scene scene = new Scene(root, 1500, 800);
//        scene.setFill(Color.web("#A4EBA2"));
        engineThread.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void drawMap(GridPane gridPane) {
        gridPane.getChildren().clear();
        gridPane.setBackground(new Background(new BackgroundFill(Color.web("#A4EBA2"), CornerRadii.EMPTY, Insets.EMPTY)));
//        gridPane.add(new Text(" y\\x "), 0, 0, 1, 1);
        gridPane.getColumnConstraints().add(new ColumnConstraints(25));
        gridPane.getRowConstraints().add(new RowConstraints(25));

        Vector2d lowL = new Vector2d(0,0);
        Vector2d hiR = map.getSize();

        // Label the columns
        for (int i = 1; i <= hiR.x - lowL.x + 1; i++) {
//            Label label = new Label(Integer.toString(lowL.x + i - 1));
//            gridPane.add(label, i, 0, 1, 1);
            gridPane.getColumnConstraints().add(new ColumnConstraints(50));
//            GridPane.setHalignment(label, HPos.CENTER);
        }
        // Label the rows
        for (int i = 1; i <= hiR.y - lowL.y + 1; i++) {
//            Label label = new Label(Integer.toString(hiR.y - i + 1));
//            gridPane.add(label, 0, i, 1, 1);
            gridPane.getRowConstraints().add(new RowConstraints(50));
//            GridPane.setHalignment(label, HPos.CENTER);
        }
        // Fill the grid with map items
        for (int i = 1; i <= hiR.x - lowL.x + 1; i++) {
            for (int j = 1; j <= hiR.y - lowL.y + 1; j++) {
                if (map.isOccupied(new Vector2d(lowL.x + i - 1,hiR.y - j + 1))) {
                    VBox box = getObject(new Vector2d(lowL.x + i - 1, hiR.y - j + 1));
                    box.setAlignment(Pos.CENTER);
                    gridPane.add(box, i, j, 1, 1);
                    GridPane.setHalignment(box, HPos.CENTER);
                }
            }
        }
    }
    private VBox getObject(Vector2d currentPosition){
        Object object = map.objectAt(currentPosition);
        Circle item = new Circle();
        item.setRadius(10);
        if (object instanceof Animal) {
            item.setFill(Color.web("#FF0000"));
        } else {
            item.setFill(Color.web("#FF00F0"));
        }

        VBox box = new VBox(item);
        return box;
    }

    private void drawStats(VBox statistics) {
        statistics.getChildren().clear();

        statistics.getChildren().add(new Label("Sample Text"));
        statistics.getChildren().add(new Label("Number of animals on map: " + engine.getAnimalsOnMap()));
        statistics.getChildren().add(new Label("Simulation Age: " + engine.getSimAge()));

//        statistics.getChildren().add(pauseButton);

        statistics.setAlignment(Pos.CENTER);
//        gridPane.add(statistics, 0, 0, 1, 15);
    }

    @Override
    public void updateMap() {
        Platform.runLater(() -> {
            drawMap(gridPane);
            drawStats(statistics);
        });
    }
}

