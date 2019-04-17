package lmbenossi;

import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Translacao {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		try (Scanner scan = new Scanner(System.in);) {
			Mat img = Imgcodecs.imread("hl2ep2.png");
			
			System.out.print("dx: ");
			int dx = scan.nextInt();
			scan.nextLine();
			System.out.print("dy: ");
			int dy = scan.nextInt();
			
			Imgcodecs.imwrite("hl2ep2-translacao.png", translacao(img, dx, dy));
			
			System.out.println("done");
		}
	}
	
	public static Mat translacao(Mat img, int dx, int dy) {
		Mat novo = new Mat(img.rows(), img.cols(), img.type());
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				novo.put(i, j, 0, 0, 0);
			}
		}
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				if(i + dx > img.rows()) {
					continue;
				}
				if(i + dx < 0) {
					continue;
				}
				
				if(j + dy > img.cols()) {
					continue;
				}
				if(j + dy < 0) {
					continue;
				}
				
				novo.put(i + dx, j + dy, img.get(i, j));
			}
		}
		
		return novo;
	}

}
