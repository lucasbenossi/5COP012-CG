import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Prova {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		try (Scanner scan = new Scanner(System.in);) {
			String prefix = "image";
			
			Mat img = Imgcodecs.imread(prefix + ".png");
			
			img = toGrayscale(img);
			img = HistEq.eqHSV(img);
			
			System.out.print("angulo: ");
			double angulo = scan.nextInt();
			scan.nextLine();
			img = Transformacao.rotacao(img, angulo);
			
			img = Esqueletizacao.lantuejoul(img);
			
			
			Imgcodecs.imwrite(prefix + "-prova.png", img);
						
			System.out.println("done");
		}
	}
	
	public static Mat toGrayscale(Mat img) {
		Mat hsv = img.clone();
		Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				double[] pixel = hsv.get(i, j);
				pixel[1] = 0;
				hsv.put(i, j, pixel);
			}
		}
		
		Mat bgr = hsv.clone();
		Imgproc.cvtColor(hsv, bgr, Imgproc.COLOR_HSV2BGR);
		
		return bgr;
	}
	
	
}
