package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import sample.bean.FileController;
import sample.bean.Logging;

import java.io.File;



public class Controller {

    @FXML
    public RadioButton countLinesTgl;

    @FXML
    public RadioButton fileLinesTgl;

    @FXML
    public RadioButton partitionTgl;

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
     * @param actionEvent
     */
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        //Init variables
        File inputFile = new File(inputPath.getText());
        File outputFile = new File(outputPath.getText());
        FileController fileController = new FileController();
        int templateChar = 0;

        try {
            //Exist the directories
            if (!outputFile.isDirectory()) {
                Logging.loggingMessage(Alert.AlertType.ERROR,
                        "Error de Directorios",
                        "Algun directorio no existe");
            } else {
                StringBuilder dirAbsoluteOut = new StringBuilder(outputFile.getAbsolutePath());
                dirAbsoluteOut.append("/");
                dirAbsoluteOut.append(inputFile.getName());


                if (fileLinesTgl.isSelected()) {
                    // Createde the file
                    Integer countLines = Integer.parseInt(numLines.getText());
                    if (countLines != null && inputFile.isFile()) {
                        System.out.println(inputFile.getAbsolutePath());
                        System.out.println(outputFile.getAbsolutePath());

                        //Llama a la funcion ue splitea y valida el fichero de entrada
                        Integer fileCount = fileController.splitFile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), countLines, templateChar);

                        System.out.println(fileController.getLog());

                        Logging.loggingMessage(Alert.AlertType.INFORMATION, "Exito",
                                "Se han creado  " + fileCount + " ficheros.", fileController.getIndexLevel() + " Errores.");
                    } else {
                        Logging.loggingMessage(Alert.AlertType.ERROR,
                                "Error de fichero",
                                "Algun fichero no existe");
                    }
                } else if (countLinesTgl.isSelected()) {
                    for (File fileTxt : outputFile.listFiles()) {
                        System.out.println(fileController.fileNumbers(fileTxt));
                    }
                } else if (partitionTgl.isSelected()) {
                    Integer countLines = Integer.parseInt(numLines.getText());
                    if (countLines != null && inputFile.isFile()) {

                    } else {
                        Logging.loggingMessage(Alert.AlertType.ERROR,
                                "Error de Directorios",
                                "Algun fichero o directorio no existe");
                    }
                } else {
                    Logging.loggingMessage(Alert.AlertType.ERROR,
                            "Error de Campos",
                            "No has seleccionado opción.");
                }

            }

        } catch (Exception e) {
            Logging.loggingMessage(Alert.AlertType.ERROR,
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
}
