
import java.util.ArrayList;
import java.util.Comparator;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Convolucao {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String prefix = "hl2ep2";
		
		Mat color = Imgcodecs.imread(prefix + ".png");
		Mat cinza = toGrayscale(color);
		
		Mat media = color;
		Mat cinzaMedia = cinza;
		Mat mediana = color;
		Mat cinzaMediana = cinza;
		
		for(int i = 0; i < 10; i++) {
			media = doWhat(media, What.MEAN);
			cinzaMedia = doWhat(cinzaMedia, What.MEAN);
			mediana = doWhat(mediana, What.MEDIAN);
			cinzaMediana = doWhat(cinzaMediana, What.MEDIAN);
		}
		
		Imgcodecs.imwrite(prefix + "-cinza.png", cinza);
		Imgcodecs.imwrite(prefix + "-media.png", media);
		Imgcodecs.imwrite(prefix + "-cinza-media.png", cinzaMedia);
		Imgcodecs.imwrite(prefix + "-mediana.png", mediana);
		Imgcodecs.imwrite(prefix + "-cinza-mediana.png", cinzaMediana);
		
//		String prefix = "debian";
//		
//		Mat og = Imgcodecs.imread(prefix + ".png");
//		
//		Imgcodecs.imwrite(prefix + "-sobel.png", sobel(og, 60));
		
		System.out.println("done");
	}
	
	public static final double R1[][] = {{-1,-2,-1},{0,0,0},{1,2,1}};
	public static final double R2[][] = {{-1,0,1},{-2,0,2},{-1,0,1}};
	public static final double black[] = {0,0,0};
	public static final double white[] = {255,255,255};
	
	public static Mat doWhat(Mat img, What what) {
		Mat novo = new Mat(img.rows(), img.cols(), img.type());
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				ArrayList<double[]> pixels = new ArrayList<double[]>(9);
				
				for(int k = 0; k < 3; k++) {
					for(int l = 0; l < 3; l++) {
						double[] pixel = pixelOrNull(img, i-1+l, j-1+k);
						if(pixel != null) {
							pixels.add(pixel);
						}
					}
				}
				
				if(what.equals(What.MEAN)) {
					novo.put(i, j, mean(pixels));
				} else if(what.equals(What.MEDIAN)) {
					novo.put(i, j, median(pixels));
				}
			}
		}
		
		return novo;
	}
	
	private enum What {
		MEAN, MEDIAN;
	}
	
	public static double[] mean(ArrayList<double[]> pixels) {
		double[] mean = new double[3];
		int n = pixels.size();
		
		for(int c = 0; c < 3; c++) {
			double sum = 0;
			for(double[] pixel : pixels) {
				sum += pixel[c];
			}
			mean[c] = sum / n;
		}
		
		return mean;
	}
	
	public static double[] median(ArrayList<double[]> pixels) {
		double[] result = {0, 0, 0};
		int meio = pixels.size() / 2;
		
		for(int c = 0; c < 3; c++) {
			pixels.sort(new DoubleArrComp(c));
			result[c] = pixels.get(meio)[c];
		}
		
		return result;
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
	
	private static class DoubleArrComp implements Comparator<double[]> {
		private int i;
		
		public DoubleArrComp(int i) {
			this.i = i;
		}
		
		@Override
		public int compare(double[] o1, double[] o2) {
			if(o1[i] < o2[i]) {
				return -1;
			} else if(o1[i] > o2[i]) {
				return 1;
			} else {
				return i;
			}
		}
		
	}
	
	public static Mat sobel(Mat img, double limiar){
		Mat novo = toGrayscale(img);
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				Mat h = h(img, i, j);
				double[] r1 = r(h, R1);
				double[] r2 = r(h, R2);
				
				double[] result = {0, 0, 0};
				for(int c = 0; c < 3; c++) {
					result[c] = Math.sqrt(r1[c]*r1[c] + r2[c]*r2[c]);
				}
				double mean = (result[0] + result[1] + result[2]) / 3;
				
				if(mean > limiar) {
					novo.put(i, j, black);
				} else{
					novo.put(i, j, white);
				}
			}
		}
		return novo;
	}
	
	public static Mat h(Mat img, int i, int j) {
		Mat h = new Mat(3, 3, img.type());
		
		for(int k = 0; k < 3; k++) {
			for(int l = 0; l < 3; l++) {
				double[] pixel = pixelOrNull(img, i-1+k, j-1+l);
				
				if(pixel != null) {
					h.put(k, l, pixel);
				} else {
					h.put(k, l, black);
				}
			}
		}
		
		return h;
	}
	
	public static double[] r(Mat h, double[][] R) {
		double[] sum = {0, 0, 0};
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				double[] pixel = h.get(i, j);
				double r = R[i][j];
				
				for(int c = 0; c < 3; c++) {
					sum[c] += r * pixel[c];
				}
			}
		}
		
		return sum;
	}
}