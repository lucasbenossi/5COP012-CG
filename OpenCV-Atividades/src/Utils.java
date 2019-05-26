import org.opencv.core.Mat;

public class Utils {
	public static double[] pixelOrNull(Mat img, int i, int j) {
		if(i >= 0 && i < img.rows()) {
			if(j >= 0 && j < img.cols()) {
				return img.get(i, j);
			}
		}
		return null;
	}
	
	public static boolean isEqual(double[] a, double[] b) {
		for(int i = 0; i < 3; i++) {
			if(a[i] != b[i]) {
				return false;
			}
		}
		
		return true;
	}
}
