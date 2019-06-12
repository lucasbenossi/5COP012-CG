import org.opencv.core.Mat;

public class Hough {

	public static Mat hough(Mat mat) {
		if(!Matrix.isBinary(mat)) {
			throw new RuntimeException("Imagem não é binária.");
		}
		
		Mat clone = mat.clone();
		
		int rMax = (int) Math.sqrt(Math.pow(mat.rows(), 2) + Math.pow(mat.cols(), 2));
		
		int[][] acumulator = new int[360][rMax * 2];
		
		for(int i = 0; i < mat.rows(); i++) {
			for(int j = 0; j < mat.cols(); j++) {
				double[] pixel = mat.get(i, j);
				if(pixel[0] == 0) {
					for(int theta = 0; theta < 360; theta++) {
						double rad = Math.toRadians(theta);
						double r = j * Math.cos(rad) + i * Math.sin(rad);
						acumulator[theta][(int) (r + rMax)]++;
					}
				}
			}
		}
		
		int max = 0;
		for(int i = 0; i < acumulator.length; i++) {
			for(int j = 0; j < acumulator[i].length; j++) {
				if(acumulator[i][j] > max) {
					max = acumulator[i][j];
				}
			}
		}
		
		double[] red = {0, 0, 255};
		
		for(int i = 0; i < acumulator.length; i++) {
			double rad = Math.toRadians(i);
			for(int j = 0; j < acumulator[i].length; j++) {
				if(acumulator[i][j] > max * 0.95) {
					for(int x = 0; x < mat.cols(); x++) {
						int r = j - rMax;
						int y = (int) ((-1 * (Math.cos(rad) / Math.sin(rad))) * x + r / Math.sin(rad));
						if(y > 0 && y < clone.rows()) {
							clone.put(y, x, red);
						}
					}
				}
			}
		}
		
		return clone;
	}
}
