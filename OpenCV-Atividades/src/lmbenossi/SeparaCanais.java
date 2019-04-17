package lmbenossi;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class SeparaCanais {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String[] canalNome = {"red", "green", "blue"};
		Mat img = Imgcodecs.imread("hl2ep2.png");
		
		for(int i = 0; i < 3; i++) {
			Imgcodecs.imwrite("hl2ep2-" + canalNome[i] + ".png", separaCanais(img, i));
		}
		
		System.out.println("done");
	}
	
	public static Mat separaCanais(Mat img, int canal) {
		Mat novo = new Mat(img.rows(), img.cols(), img.type());
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				double[] pixel = img.get(i,j);
				double[] novoPixel = new double[3];
				double valorCanal = pixel[canal];
				novoPixel[0] = valorCanal;
				novoPixel[1] = valorCanal;
				novoPixel[2] = valorCanal;
				novo.put(i, j, novoPixel);
			}
		}
		
		return novo;
	}

}
