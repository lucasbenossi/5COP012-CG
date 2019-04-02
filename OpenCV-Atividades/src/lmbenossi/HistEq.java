package lmbenossi;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class HistEq {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Imgcodecs.imwrite("grayscale-eqGrayscale.png", eqGrayscale(Imgcodecs.imread("grayscale.png")));
		Imgcodecs.imwrite("grayscale-eqHSV.png", eqHSV(Imgcodecs.imread("grayscale.png")));
		
		Imgcodecs.imwrite("color-eqAllChannels.png", eqAllChannels(Imgcodecs.imread("color.png")));
		Imgcodecs.imwrite("color-eqHSV.png", eqHSV(Imgcodecs.imread("color.png")));
		
		System.out.println("done");
	}
	
	public static Mat eqHSV(Mat img) {
		Mat hsv = img.clone();
		Imgproc.cvtColor(img, hsv, Imgproc.COLOR_RGB2HSV);
		
		eqChannel(hsv, 2);
		
		Mat rgb = hsv.clone();
		Imgproc.cvtColor(hsv, rgb, Imgproc.COLOR_HSV2BGR);
		
		return rgb;
	}
	
	public static Mat eqGrayscale(Mat img) {
		Mat eq = img.clone();
		eqChannel(eq, 0);
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				double[] pixel = eq.get(i, j);
				pixel[1] = pixel[0];
				pixel[2] = pixel[0];
				eq.put(i, j, pixel);
			}
		}
		
		return eq;
	}
	
	public static Mat eqAllChannels(Mat img) {
		Mat eq = img.clone();
		
		for(int channel = 0; channel < 3; channel ++) {
			eqChannel(eq, channel);
		}
		
		return eq;
	}
	
	public static void eqChannel(Mat img, int channel) {
		int H[] = new int[256];
		for(int i = 0; i < H.length; i++) {
			H[i] = 0;
		}
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				int pixel = (int) img.get(i, j)[channel];
				H[pixel]++;
			}
		}
		
		double P[] = new double[256];
		int totalPixels = img.rows() * img.cols();
		for(int i = 0; i < 256; i++) {
			P[i] = (double) H[i] / totalPixels;
		}
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				double[] pixel = img.get(i, j);
				int intensity = (int) pixel[channel];
				
				double value = 0;
				for(int k = 0; k < intensity; k++) {
					value += P[k]; 
				}
				value *= 255;
				
				pixel[channel] = (int) value;
				img.put(i, j, pixel);
			}
		}
	}

}
