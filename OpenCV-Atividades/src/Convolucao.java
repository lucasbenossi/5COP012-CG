
import java.util.ArrayList;
import java.util.Comparator;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Convolucao {
	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String prefix = "hl2ep2";
		
		Mat color = Imgcodecs.imread(prefix + ".png");
		Mat cinza = Grayscale.convert(color);
		
		Thread[] threads = {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String prefix = "debian";
					Mat img = Imgcodecs.imread(prefix + ".png");
					String filename = prefix + "-sobel.png";
					Imgcodecs.imwrite(filename, sobel(img, 50));
					System.out.println("done " + filename);
				}
			}),
			new Thread(new Runnable() {
				@Override
				public void run() {
					String prefix = "debian";
					Mat img = Imgcodecs.imread(prefix + ".png");
					
					Mat rotacao = Transformacao.rotacao(img, 30);
					String filename = prefix + "-rotacao.png";
					Imgcodecs.imwrite(filename, rotacao);
					System.out.println("done " + filename);
					
					Mat mediana = operation(rotacao, Convolucao::median);
					filename = prefix + "-rotacao-mediana.png";
					Imgcodecs.imwrite(filename, mediana);
					System.out.println("done " + filename);
				}
			}),
			new Thread(new DoWhatRunnable(prefix + "-color-mean.png", color, Convolucao::mean)),
			new Thread(new DoWhatRunnable(prefix + "-color-median.png", color, Convolucao::median)),
			new Thread(new DoWhatRunnable(prefix + "-cinza-mean.png", cinza, Convolucao::mean)),
			new Thread(new DoWhatRunnable(prefix + "-cinza-median.png", cinza, Convolucao::median))
		};
		
		
		for(Thread thread : threads) {
			thread.start();
		}
		
		for(Thread thread : threads) {
			thread.join();
		}
		
		System.out.println("done");
	}
	
	public static final double R1[][] = {{-1,-2,-1},{0,0,0},{1,2,1}};
	public static final double R2[][] = {{-1,0,1},{-2,0,2},{-1,0,1}};
	public static final double black[] = {0,0,0};
	public static final double white[] = {255,255,255};
	
	private static class DoWhatRunnable implements Runnable {
		private String filename;
		private Mat img;
		private Operation operation;

		public DoWhatRunnable(String filename, Mat img, Operation operation) {
			this.filename = filename;
			this.img = img;
			this.operation = operation;
		}

		@Override
		public void run() {
			Mat result = this.img;
			for(int i = 0; i < 20; i++) {
				result = Convolucao.operation(img, operation);
			}
			
			Imgcodecs.imwrite(filename, result);
			System.out.println("done " + filename);
		}
		
	}
	
	public static Mat operation(Mat img, Operation operation) {
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
				
				novo.put(i, j, operation.operation(pixels));
			}
		}
		
		return novo;
	}
	
	@FunctionalInterface
	private static interface Operation {
		public double[] operation(ArrayList<double[]> pixels);
	}
	
	private static double[] mean(ArrayList<double[]> pixels) {
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
	
	private static double[] median(ArrayList<double[]> pixels) {
		double[] result = {0, 0, 0};
		int meio = pixels.size() / 2;
		
		for(int c = 0; c < 3; c++) {
			pixels.sort(new DoubleArrComp(c));
			result[c] = pixels.get(meio)[c];
		}
		
		return result;
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
		Mat novo = Grayscale.convert(img);
		
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
	
	private static Mat h(Mat img, int i, int j) {
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
	
	private static double[] r(Mat h, double[][] R) {
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
