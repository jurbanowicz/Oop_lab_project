package IO;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVFileWriter {
    ArrayList<String> data;

    public CSVFileWriter() {
        this.data = new ArrayList<>();
        data.add("Age, Animals, Grass\n");
    }

    public void writeLine(String dataLine) {
        this.data.add(dataLine);
    }

    public void saveFile() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy:MM:dd_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String fileName = "Simulation-" + dtf.format(now);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./src/main/resources/SimulationsData/" + fileName + ".csv"));
            for (int line = 0; line < data.size(); line++) {
                writer.write(data.get(line));
            }
            writer.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

}
