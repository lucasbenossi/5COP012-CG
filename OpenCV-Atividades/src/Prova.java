import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Prova {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		try (Scanner scan = new Scanner(System.in);) {
			String prefix = "image";
			
			Mat img = Imgcodecs.imread(prefix + ".png");
			
			img = Grayscale.convert(img);
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
}
