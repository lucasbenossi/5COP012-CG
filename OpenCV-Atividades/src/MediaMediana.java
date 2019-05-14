
import java.util.ArrayList;
import java.util.Comparator;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class MediaMediana {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String prefix = "hl2ep2";
		
		Mat og = Imgcodecs.imread(prefix + ".png");
		Mat cinza = toGrayscale(og);
		
		Mat media = og;
		Mat cinzaMedia = cinza;
		Mat mediana = og;
		Mat cinzaMediana = cinza;
		
		for(int i = 0; i < 3; i++) {
			media = media(media);
			cinzaMedia = media(cinzaMedia);
			mediana = mediana(mediana);
			cinzaMediana = mediana(cinzaMediana);
		}
		
		Imgcodecs.imwrite(prefix + "-cinza.png", cinza);
		Imgcodecs.imwrite(prefix + "-media.png", media);
		Imgcodecs.imwrite(prefix + "-cinza-media.png", cinzaMedia);
		Imgcodecs.imwrite(prefix + "-mediana.png", mediana);
		Imgcodecs.imwrite(prefix + "-cinza-mediana.png", cinzaMediana);
		
		System.out.println("done");
	}
	
	public static Mat media(Mat img) {
		Mat novo = new Mat(img.rows(), img.cols(), img.type());
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				ArrayList<double[]> arr = new ArrayList<double[]>(9);
				
				for(int k = 0; k < 3; k++) {
					for(int l = 0; l < 3; l++) {
						arr.add(pixelOrNull(img, i-1+l, j-1+k));
					}
				}
				
				double[] mean = mean(arr.toArray(new double[9][]));
				novo.put(i, j, mean);
			}
		}
		
		return novo;
	}
	
	
	public static Mat mediana(Mat img) {
		Mat novo = new Mat(img.rows(), img.cols(), img.type());
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				ArrayList<double[]> list = new ArrayList<double[]>(9);
				
				for(int k = 0; k < 3; k++) {
					for(int l = 0; l < 3; l++) {
						double[] pixel = pixelOrNull(img, i-1+l, j-1+k);
						if(pixel != null) {
							list.add(pixel);
						}
					}
				}
				
				list.sort(new DoubleArrComp());
				novo.put(i, j, list.get(list.size() / 2));
			}
		}
		
		return novo;
	}
	
	public static class DoubleArrComp implements Comparator<double[]> {

		@Override
		public int compare(double[] o1, double[] o2) {
			if(o1[0] < o2[0]) {
				return -1;
			} else if(o1[0] > o2[0]) {
				return 1;
			} else {
				return 0;
			}
		}
		
	}
	
	public static Mat toGrayscale(Mat img) {
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
	
	private static double[] pixelOrNull(Mat img, int i, int j) {
		if(i >= 0 && i < img.rows()) {
			if(j >= 0 && j < img.cols()) {
				return img.get(i, j);
			}
		}
		return null;
	}
	
	private static double[] mean(double[]... values) {
		double[] sum = {0, 0, 0};
		int n = 0;
		
		for(double[] value : values) {
			if(value != null) {
				sum[0] += value[0];
				sum[1] += value[1];
				sum[2] += value[2];
				n++;
			}
		}
		
		double[] mean = {sum[0] / n, sum[1] / n, sum[2] / n};
		
		return mean;
	}
	
	private static double mean(double... values) {
		double sum = 0;
		for(double value : values) {
			sum += value;
		}
		return sum / values.length;
	}
}
