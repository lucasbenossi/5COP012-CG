
import java.util.ArrayList;
import java.util.Comparator;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class MediaMediana {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String prefix = "hl2ep2";
		
		Mat og = Imgcodecs.imread(prefix + ".png");
		Mat cinza = Prova.toGrayscale(og);
		
		Imgcodecs.imwrite(prefix + "-cinza.png", cinza);
		for(int i = 0; i < 10; i++) {
			cinza = medianaCinza(cinza);
		}
		Imgcodecs.imwrite(prefix + "-mediana-cinza.png", cinza);
		
		System.out.println("done");
	}
	
	public static Mat mediaCinza(Mat img) {
		Mat novo = new Mat(img.rows(), img.cols(), img.type());
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				double[] sum = {0, 0, 0};
				int n = 0;
				
				for(int k = 0; k < 3; k++) {
					for(int l = 0; l < 3; l++) {
						double[] pixel = pixelOrNull(img, i-1+l, j-1+k);
						if(pixel != null) {
							for(int m = 0; m < 3; m++) {
								sum[m] += pixel[m];
							}
							n++;
						}
					}
				}
				
				double[] mean = {sum[0] / n, sum[1] / n, sum[2] / n};
				novo.put(i, j, mean);
			}
		}
		
		return novo;
	}
	
	public static Mat medianaCinza(Mat img) {
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
