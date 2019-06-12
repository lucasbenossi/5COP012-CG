import java.awt.Color;
import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public class Matrix {
	public static double[] pixelOrNull(Mat img, int i, int j) {
		if(i >= 0 && i < img.rows()) {
			if(j >= 0 && j < img.cols()) {
				return img.get(i, j);
			}
		}
		return null;
	}
	
	public static void paintItBlack(Mat mat) {
		for(int i = 0; i < mat.rows(); i++) {
			for(int j = 0; j < mat.cols(); j++) {
				mat.put(i, j, Pixel.bgrBlack);
			}
		}
	}

	public static void paintItWhite(Mat mat) {
	    for (int i = 0; i < mat.rows(); i++) {
	        for (int j = 0; j < mat.cols(); j++) {
	            mat.put(i, j, Pixel.bgrWhite);
	        }
	    }
	}

	static BufferedImage matToBufferedImage(Mat mat) {
		BufferedImage img = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < mat.rows(); i++) {
			for(int j = 0; j < mat.cols(); j++) {
				double[] pixel = mat.get(i, j);
				img.setRGB(j, i, new Color((int) pixel[2], (int) pixel[1], (int) pixel[0]).getRGB());
			}
		}
		return img;
	}
	
	public static boolean isBinary(Mat mat) {
		for(int i = 0; i < mat.rows(); i++) {
			for(int j = 0; j < mat.cols(); j++) {
				double pixel[] = mat.get(i, j);
				if(!Pixel.isBlack(pixel) && !Pixel.isWhite(pixel)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static Mat toBinary(Mat mat, double[] fill) {
		for(int i = 0; i < mat.rows(); i++) {
			for(int j = 0; j < mat.cols(); j++) {
				double pixel[] = mat.get(i, j);
				if(!Pixel.isBlack(pixel) && !Pixel.isWhite(pixel)) {
					mat.put(i, j, fill);
				}
			}
		}
		return mat;
	}
}
