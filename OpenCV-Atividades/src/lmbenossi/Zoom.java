package lmbenossi;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Zoom {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat og = Imgcodecs.imread("image.png");
		
		Imgcodecs.imwrite("image-zoom-in-quadrado.png", zoomInQuadrado(og));
		Imgcodecs.imwrite("image-zoom-in-linear.png", zoomInLinear(og));
		Imgcodecs.imwrite("image-zoom-out-quadrado.png", zoomOutQuadrado(og));
		Imgcodecs.imwrite("image-zoom-out-linear.png", zoomOutLinear(og));
		Imgcodecs.imwrite("image-inverted.png", invert(og));
		
		System.out.println("done");
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
		
		for (int i = 0; i < rows - 1; i++) {
			int j;
			for (j = 0; j < cols - 1; j++) {
				double[] no = og.get(i, j);
				double[] ne = og.get(i, j + 1);
				double[] so = og.get(i + 1, j);
				double[] se = og.get(i + 1, j + 1);

				zoomed.put(2 * i, 2 * j, no);
				zoomed.put(2 * i, 2 * j + 1, mean(no, ne));
				zoomed.put(2 * i + 1, 2 * j, mean(no, so));
				zoomed.put(2 * i + 1, 2 * j + 1, mean(no, so, ne, se));
			}

			zoomed.put(2 * i, 2 * j, og.get(i, j));
			zoomed.put(2 * i, 2 * j + 1, og.get(i, j));
			zoomed.put(2 * i + 1, 2 * j, og.get(i, j));
			zoomed.put(2 * i + 1, 2 * j + 1, og.get(i, j));
		}

		return zoomed;		
	}
	
	public static Mat zoomOutQuadrado(Mat og) {
		int rows = og.rows();
		int cols = og.cols();
		
		Mat zoomed = new Mat(rows / 2, cols / 2, og.type());
		
		for (int i = 0; i < rows; i += 2) {
			for (int j = 0; j < cols; j += 2) {
				double[] rgb = og.get(i, j);
				
				zoomed.put(i / 2, j / 2, rgb);
			}
		}
		
		return zoomed;
	}
	
	public static Mat zoomOutLinear(Mat og) {
		int rows = og.rows();
		int cols = og.cols();
		
		Mat zoomed = new Mat(rows / 2, cols / 2, og.type());
		
		for (int i = 0; i < rows - 1; i += 2) {
			for (int j = 0; j < cols - 1; j += 2) {
				double []no = og.get(i, j);
                double []ne = og.get(i, j+1);
                double []so = og.get(i+1, j);
                double []se = og.get(i+1, j+1);

                zoomed.put(i / 2, j / 2, mean(no, ne, so, se));
			}
		}
		
		return zoomed;
	}
    
    public static Mat invert(Mat og) {
    	int rows = og.rows();
    	int cols = og.cols();
    	
    	Mat inverted = new Mat(rows, cols, og.type());
    	
    	for(int i = 0; i < rows; i++) {
    		for(int j = 0; j < cols; j++) {
    			double[] pixel = og.get(i, j);
    			
    			for (int k = 0; k < 3; k++) {
    	    		pixel[k] = 255 - pixel[k];
    	    	}
    			
    			inverted.put(i, j, pixel);
    		}
    	}
    	
    	return inverted;
    }
	
	public static double[] mean(double[]... values) {
    	double sum[] = new double[3];
    	double mean[] = new double[3];
    	
    	for (int i = 0; i < 3; i++) {
			sum[i] += 0;
			for (double[] value : values) {
				sum[i] += value[i];
			}
			mean[i] = sum[i] / values.length;
		}
    	
    	return mean;
    }
}
