import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Grayscale {
	public static Mat convert(Mat img) {
		Mat hsv = img.clone();
		Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
		
		for(int x = 0; x < img.rows(); x++) {
			for(int y = 0; y < img.cols(); y++) {
				double[] pixel = hsv.get(x, y);
				pixel[1] = 0;
				hsv.put(x, y, pixel);
			}
		}
		
		Mat bgr = hsv.clone();
		Imgproc.cvtColor(hsv, bgr, Imgproc.COLOR_HSV2BGR);
		
		return bgr;
	}
}
