package lmbenossi;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat og = Imgcodecs.imread("image.png");
		
		Imgcodecs.imwrite("image-zoom-in-quadrado.png", zoomInQuadrado(og));
		
		
	}
	
	public static Mat zoomInQuadrado(Mat og) {
		int rows = og.rows();
		int cols = og.cols();
		
		Mat zoomed = new Mat(rows * 2, cols * 2, og.type());
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				double[] rgb = og.get(i,  j);
				
				zoomed.put(2 * i, 2 * j, rgb);
				zoomed.put(2 * i, 2 * j + 1, rgb);
				zoomed.put(2 * i + 1, 2 * j, rgb);
				zoomed.put(2 * i + 1, 2 * j + 1, rgb);
			}
		}
		
		return zoomed;		
	}
	
	public static Mat zoomInLinear(Mat og) {
		int rows = og.rows();
		int cols = og.cols();
		
		Mat zoomed = new Mat(rows * 2, cols * 2, og.type());
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				double[] rgb = og.get(i,  j);  // TODO
				
				zoomed.put(2 * i, 2 * j, rgb);
				zoomed.put(2 * i, 2 * j + 1, rgb);
				zoomed.put(2 * i + 1, 2 * j, rgb);
				zoomed.put(2 * i + 1, 2 * j + 1, rgb);
			}
		}
		
		return zoomed;		
	}
}
