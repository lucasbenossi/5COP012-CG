
import org.opencv.core.Mat;

public class Preencimento {
	public static Mat preencher(Mat img, int i, int j, double[] cor, double limiar) {
		Mat clone = img.clone();
		preencherRecursivo(clone, i, j, clone.get(i, j), cor, limiar);
		return clone;
	}

	public static void preencherRecursivo(Mat img, int i, int j, double[] inicial, double[] cor, double limiar) {
		double[] pixel = Matrix.pixelOrNull(img, i, j);
		
		if(pixel != null && !Pixel.isEqual(pixel, cor) && estaNoLimiar(pixel, inicial, limiar)) {
			img.put(i, j, cor);
			preencherRecursivo(img, i, j-1, inicial, cor, limiar);
			preencherRecursivo(img, i, j+1, inicial, cor, limiar);
			preencherRecursivo(img, i-1, j, inicial, cor, limiar);
			preencherRecursivo(img, i+1, j, inicial, cor, limiar);
		}
	}
	
	private static boolean estaNoLimiar(double[] pixel, double[] inicial, double limiar) {
		for(int c = 0; c < 3; c++) {
			if(pixel[c] < inicial[c] - limiar || pixel[c] > inicial[c] + limiar) {
				return false;
			}
		}
		
		return true;
	}
}
