package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
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
    private TextField numTemplate;

    @FXML
    private TextField inputPath;

    @FXML
    private TextArea textAreaLog;


    private Object Stage;
    private StringBuilder strLog = new StringBuilder();


    public void initialize() {
        final String dir = System.getProperty("user.dir");
        //inputPath.setText(dir);
        //outputPath.setText(dir);
        numLines.setText("10");
        numTemplate.setText("10");
    }


    /**
     * @return
     */
    public String openDirectory() {
        String path = "";

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog((Window) Stage);

        if (selectedDirectory == null) {
            textAreaLog.setText("Directorio no seleccionado. \n");
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
            textAreaLog.setText("Fichero no seleccionado \n");
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
                Integer countLines = Integer.parseInt(numLines.getText());

                if (fileLinesTgl.isSelected()) {
                    // Createde the file
                    if (countLines != null && inputFile.isFile()) {
                        textAreaLog.appendText("Leiendo " + inputFile.getAbsolutePath() + "\n");
                        fileController.createFile(inputFile, outputFile.getAbsolutePath() + "/" + inputFile.getName(), countLines);
                        textAreaLog.appendText("Creado " + outputFile.getAbsolutePath() + "/" + inputFile.getName() + "\n");
                    } else {
                        Logging.loggingMessage(Alert.AlertType.ERROR,
                                "Error de fichero",
                                "Algun fichero no existe");
                    }
                } else if (countLinesTgl.isSelected()) {
                    textAreaLog.appendText(outputFile.getAbsolutePath() + "\n");
                    for (File fileTxt : outputFile.listFiles()) {
                        textAreaLog.appendText(fileTxt.getName() + "\t" + fileController.fileNumbers(fileTxt) + "\n");
                    }
                    textAreaLog.appendText("----------- END FILE -----------\n");
                } else if (partitionTgl.isSelected()) {
                    if (countLines != null && inputFile.isFile()) {
                        try {
                            templateChar = Integer.parseInt(numTemplate.getText());
                        } catch (Exception e) {
                            Logging.loggingMessage(Alert.AlertType.ERROR,
                                    "Error de Tempate",
                                    "El numero de template es incorrecto");
                        }

                        //Llama a la funcion ue splitea y valida el fichero de entrada
                        Integer fileCount = fileController.splitFile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), countLines, templateChar);

                        textAreaLog.appendText(fileController.getLog().toString() + "\n");
                        textAreaLog.appendText("Se han creado  " + fileCount + " ficheros.\n");
                        textAreaLog.appendText(fileController.getIndexLevel() + " Errores.\n");
                        textAreaLog.appendText("----------- END FILE -----------\n");

                        Logging.loggingMessage(Alert.AlertType.INFORMATION, "Exito",
                                "Se han creado  " + fileCount + " ficheros.", fileController.getIndexLevel() + " Errores.");

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
