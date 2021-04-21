package sample.functional;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Thresholding {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static long perform(String path, String filename, double value, boolean erosion, boolean blurSelected) {
        long counter = 0;
        Mat img = Imgcodecs.imread(path);
//        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        if (erosion) {
            Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        }
        if (blurSelected) {
            Imgproc.GaussianBlur(img, img, new Size(5, 5), 0);
        }
//        Core.addWeighted(img, 1.4, img, 0, 0, img);
        Imgproc.threshold(img, img, value, 255, 1);
//        Imgproc.adaptiveThreshold(img,img,150,Imgproc.ADAPTIVE_THRESH_MEAN_C,1, 5,1.2);
//        Imgproc.threshold(img, img, 120, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
                if (((img.get(i, j)[0] == 255) && img.get(i, j)[2] == 0 && img.get(i, j)[1] == 0) || (img.get(i, j)[0] == 0 && img.get(i, j)[1] == 0 && img.get(i, j)[2] == 0)) {
                    img.put(i, j, 255, 255, 255);
                    counter++;
                } else {
                    img.put(i, j, 0, 0, 0);
                }
            }
        }
        String currentDirectory = System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\");
        counter = (long) (((double) counter) / (img.rows() * img.cols()) * 100);
        Imgcodecs.imwrite(currentDirectory + "\\results\\" + filename + "-thresholding-" + counter + ".jpg", img);
//        Imgcodecs.imwrite("C:\\Users\\dell\\PycharmProjects\\surface-crack-detection\\dataset\\craquelures\\train\\label\\" + filename + ".jpg", img);
        return counter;
    }
}
