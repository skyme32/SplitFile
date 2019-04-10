package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;


public class Controller {

    @FXML
    public CheckBox idSplitFlag;

    @FXML
    private TextField outputPath;

    @FXML
    private TextField numLines;

    @FXML
    private TextField inputPath;



    private Object Stage;


    public void initialize() {

        final String dir = System.getProperty("user.dir");
        inputPath.setText(dir);
        outputPath.setText(dir);
        numLines.setText("10");
    }


    /**
     * @return
     */
    public String openDirectory() {
        String path = "";

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog((Window) Stage);

        if (selectedDirectory == null) {
            System.err.println("No Directory selected");
        } else {
            path = selectedDirectory.getAbsolutePath();
        }
        return path;
    }

    /**
     * @return
     */
    public String openFile() {
        String path = "";

        FileChooser fileChooser = new FileChooser();
        File selectFile = fileChooser.showOpenDialog((Window) Stage);

        if (selectFile == null) {
            System.err.println("No File selected");
        } else {
            path = selectFile.getAbsolutePath();
        }
        return path;
    }

    /**
     * Logging message for screen
     *
     * @param level
     * @param header
     * @param msg
     */
    public void loggingMessage(Alert.AlertType level, String header, String msg) {
        Alert alert = new Alert(level);
        alert.setTitle("Dialog SplitFile");
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * @param actionEvent
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        //Init variables
        File inputFile = new File(inputPath.getText());
        File outputFile = new File(outputPath.getText());

        try {
            Integer countLines = Integer.parseInt(numLines.getText());

            //Exist the directories
            if (!inputFile.isFile() || !outputFile.isDirectory()) {
                loggingMessage(Alert.AlertType.ERROR,
                        "Error de Directorios",
                        "Algun fichero o directorio no existe");
            } else {
                StringBuilder dirAbsoluteOut = new StringBuilder(outputFile.getAbsolutePath());
                dirAbsoluteOut.append("/");
                dirAbsoluteOut.append(inputFile.getName());


                if (!idSplitFlag.isSelected()) {
                    // Createde the file
                    createFile(inputFile, outputFile.getAbsolutePath() + "/" + inputFile.getName(), countLines);
                    loggingMessage(Alert.AlertType.INFORMATION,
                            "Los siguientes Ficheros se han creado: ",
                            dirAbsoluteOut.toString());
                }else{
                    //TODO
                }
            }

        } catch (Exception e) {
            loggingMessage(Alert.AlertType.ERROR,
                    "Error de lineas",
                    "El numero añadido de lineas no es correcto");
        }

    }

    /**
     * Input File
     *
     * @param actionEvent
     */
    public void handleInputfile(ActionEvent actionEvent) {
        String text = openFile();
        inputPath.setText(text);
    }

    /**
     * Output File
     *
     * @param actionEvent
     */
    public void handleOutpufile(ActionEvent actionEvent) {
        String text = openDirectory();
        outputPath.setText(text);
    }

    /**
     *
     * @param ficheroOriginal
     * @param ficheroCopia
     */
    public static void createFile(File ficheroOriginal, String ficheroCopia, Integer countLines) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fichero = null;
        PrintWriter pw = null;

        // Lectura del fichero
        String linea;
        Integer count = 0;

        try {
            // Se abre el fichero original para lectura
            fr = new FileReader(ficheroOriginal);
            br = new BufferedReader(fr);

            // Se abre el fichero original para escritura
            fichero = new FileWriter(ficheroCopia);
            pw = new PrintWriter(fichero);


            while ((linea = br.readLine()) != null && count < countLines) {
                System.out.println(linea);
                pw.println(linea);
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
