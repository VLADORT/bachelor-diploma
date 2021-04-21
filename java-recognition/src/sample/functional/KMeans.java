package sample.functional;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KMeans {
    private static long counter = 0;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static long perform(String path, String filename, boolean erosion, boolean blurSelected) {
        counter = 0;
        System.out.println(path);
        Mat img = Imgcodecs.imread(path);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        if (erosion) {
            Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        }
        if (blurSelected) {
            Imgproc.GaussianBlur(img, img, new Size(5, 5), 0);
        }
        Mat clusters =
                cluster(img, 2).get(0);
        countLoss(clusters);
        String currentDirectory = System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\");
        counter = (long) (((double) counter) / (img.rows() * img.cols()) * 100);
        counter = Math.min(counter, 100 - counter);
        Imgcodecs.imwrite(currentDirectory + "\\results\\" + filename + "-kmeans-" + counter + ".jpg", clusters);
        return counter;
    }

    private static void countLoss(Mat clusters) {
        for (int i = 0; i < clusters.rows(); i++) {
            for (int j = 0; j < clusters.cols(); j++) {
                if (clusters.get(i, j)[0] == 0) {
                    counter++;
                }
            }
        }
    }

    public static List<Mat> cluster(Mat cutout, int k) {
        Mat samples = cutout.reshape(1, cutout.cols() * cutout.rows());
        Mat samples32f = new Mat();
        samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);

        Mat labels = new Mat();
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
        Mat centers = new Mat();
        Core.kmeans(samples32f, k, labels, criteria, 15, Core.KMEANS_PP_CENTERS, centers);
        return showClusters(cutout, labels, centers);
    }

    private static List<Mat> showClusters(Mat cutout, Mat labels, Mat centers) {
        centers.convertTo(centers, CvType.CV_8UC1, 255.0);
        centers.reshape(2);

        List<Mat> clusters = new ArrayList<Mat>();
        for (int i = 0; i < centers.rows(); i++) {
            clusters.add(Mat.zeros(cutout.size(), cutout.type()));
        }

        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < centers.rows(); i++) {
            counts.put(i, 0);
        }
        int rows = 0;
        for (int y = 0; y < cutout.rows(); y++) {
            for (int x = 0; x < cutout.cols(); x++) {
                int label = (int) labels.get(rows, 0)[0];
                counts.put(label, counts.get(label) + 1);
                clusters.get(label).put(y, x, 255);
                rows++;
            }
        }
        System.out.println(counts);
        return clusters;
    }
}
