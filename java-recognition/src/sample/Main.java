package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class Main extends Application {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Image analysis");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
//        File[] files = new File("C:\\Users\\dell\\PycharmProjects\\surface-crack-detection\\dataset\\craquelures\\train\\image").listFiles();
//        for (File file : files) {
//            Thresholding.perform(file.getAbsolutePath(), Paths.get(file.getAbsolutePath()).getFileName().toString(), 120, true, true);
//        }
    }
}
