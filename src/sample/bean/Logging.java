package sample.bean;

import javafx.scene.control.Alert;

public class Logging {

    //CONSTANTS
    private static final String DIALOG_TITLE = "Dialog SplitFile";

    //VARIABLES


    /**
     * Logging message for screen
     *
     * @param level
     * @param header
     * @param msg
     */
    public static void loggingMessage(Alert.AlertType level, String header, String msg) {
        Alert alert = new Alert(level);
        alert.setTitle(DIALOG_TITLE);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Logging message for screen
     *
     * @param level
     * @param header
     * @param msg
     */
    public static void loggingMessage(Alert.AlertType level, String title, String header, String msg) {
        Alert alert = new Alert(level);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * @param intLines
     */
    public void showFormatLines(Integer intLines) {

    }


}
