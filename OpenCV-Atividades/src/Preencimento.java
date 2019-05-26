
import org.opencv.core.Mat;

public class Preencimento {
	public static Mat preencher(Mat img, int i, int j, double[] novo, double limiar) {
		Mat clone = img.clone();
		preencherRecursivo(clone, i, j, clone.get(i, j), novo, limiar);
		return clone;
	}
	
	private static void preencherRecursivo(Mat img, int i, int j, double[] inicial, double[] novo, double limiar) {
		double[] pixel = Utils.pixelOrNull(img, i, j);
		if(pixel == null) {
			return;
		}
		
		if(!Utils.isEqual(pixel, novo) && estaNoLimiar(pixel, inicial, limiar)) {
			img.put(i, j, novo);
			preencherRecursivo(img, i, j-1, inicial, novo, limiar);
			preencherRecursivo(img, i, j+1, inicial, novo, limiar);
			preencherRecursivo(img, i-1, j, inicial, novo, limiar);
			preencherRecursivo(img, i+1, j, inicial, novo, limiar);
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
