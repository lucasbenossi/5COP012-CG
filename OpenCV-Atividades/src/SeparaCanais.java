
import org.opencv.core.Mat;

public class SeparaCanais {
	
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
