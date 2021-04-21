package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.functional.KMeans;
import sample.functional.Thresholding;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Controller {
    @FXML
    public Text thresholdLabel;
    @FXML
    private Stage stage;
    private String path;
    @FXML
    public Slider thresholdValue;
    @FXML
    public ImageView original;
    @FXML
    public Button thresholdingButton;
    @FXML
    public Button kMeansButton;
    @FXML
    public CheckBox erosion;
    @FXML
    public CheckBox blur;
    @FXML
    public ImageView kmeans;
    @FXML
    public ImageView thresholded;
    @FXML
    public Button openImageButton;
    @FXML
    public Text resultLabelKmeans;
    @FXML
    public Text resultLabelThreshold;
    private String filename;

    public void handlePerformKMeansButton(ActionEvent actionEvent) {
        deletePreviousResults(System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\") + "\\results\\", filename, "kmeans");
        long percentage = KMeans.perform(path, filename, erosion.isSelected(), blur.isSelected());
        Image image = new Image("file:///" + System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\") + "\\results\\" + filename + "-kmeans-" + percentage + ".jpg");
        this.kmeans.setImage(image);
        this.resultLabelKmeans.setText("Loss: " + percentage + "%");
    }

    public void handlePerformThresholdingButton(ActionEvent actionEvent) {
        deletePreviousResults(System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\") + "\\results\\", filename, "thresholding");
        long percentage = Thresholding.perform(path, filename, thresholdValue.getValue(), erosion.isSelected(), blur.isSelected());
        Image image = new Image("file:///" + System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\") + "\\results\\" + filename + "-thresholding-" + percentage + ".jpg");
        this.thresholded.setImage(image);
        this.resultLabelThreshold.setText("Loss: " + percentage + "%");
    }

    public void handleDraggingFile(DragEvent actionEvent) {
        Dragboard db = actionEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            if (db.getFiles().size() > 1) {
                File file = db.getFiles().get(db.getFiles().size() - 1);
                db.getFiles().forEach(s -> {
                    if (!s.equals(file))
                        db.getFiles().remove(s);
                });
            }
//            dragDashboardLabel.setText(db.getFiles().get(0).getName());
            this.path = db.getFiles().get(0).getAbsolutePath();
            Image image = null;
            if (!path.isEmpty()) {
                path = path.replaceAll("\\\\", "\\\\\\\\");
                image = new Image("file:///".concat(path));
            }
            this.original.setImage(image);
            success = true;
        }
        /* let the source know whether the string was successfully
         * transferred and used */
        actionEvent.setDropCompleted(success);

        actionEvent.consume();
    }

//    public void onDragFileDetected(DragEvent event) {
////        if (event.getGestureSource() != dragDashboard
////                && event.getDragboard().hasFiles()) {
//            /* allow for both copying and moving, whatever user chooses */
//            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
//        }
//        event.consume();
//    }

    public void handleOpeningImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            this.path = file.getAbsolutePath();
            Image image = null;
            if (!path.isEmpty()) {
                this.filename = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
                path = path.replaceAll("\\\\", "\\\\\\\\");
                image = new Image("file:///".concat(path));
            }
            this.original.setImage(image);
        }
    }

    public void handleChangeThreshold(MouseDragEvent event) {
        this.thresholdLabel.setText(thresholdLabel.getText() + ": " + thresholdValue);
        event.consume();
    }

    private void deletePreviousResults(String directoryName, String file, String operation) {
        File folder = new File(directoryName);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            List<File> list = Arrays.asList(Objects.requireNonNull(folder.listFiles()));
            list.stream().filter(s -> {
                String filename = s.getName().substring(0, s.getName().lastIndexOf("-"));
                return filename.equals(file + "-" + operation);
            }).forEach(File::delete);

        }

    }


//    public void handlePerformOtsu(ActionEvent actionEvent) {
//        long percentage = Thresholding.performOtsu(path, erosion.isSelected(), blur.isSelected());
//        Image image = new Image("file:///" + System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\") + "\\src\\sample\\results\\result.jpg");
//        this.result.setImage(image);
//        this.resultLabel.setText("Loss: " + percentage + "%");
//    }
}

