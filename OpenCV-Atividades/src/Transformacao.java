import org.opencv.core.Mat;

public class Transformacao {
	public static Mat translacao(Mat img, int dx, int dy) {
		PixelArray pixels = new PixelArray(img);
		
		double[][] matrizTranslacao = matrizTranslacao(dx, dy);
		for(Pixel pixel : pixels) {
			transforma(pixel, matrizTranslacao);
		}
		
		return pixels.toMat();
	}
	
	public static Mat rotacao(Mat img, double angulo) {
		PixelArray pixels = new PixelArray(img);
		
		angulo = Math.toRadians(angulo);

        double[][] translateOrigin = matrizTranslacao(img.rows() / -2, img.cols() / -2);
        double[][] rotationMatrix = matrizRotacao(angulo);
        double[][] translateBack = matrizTranslacao(img.rows() / 2, img.cols() / 2);

        for (Pixel pixel : pixels) {
            transforma(pixel, translateOrigin);
            transforma(pixel, rotationMatrix);
            transforma(pixel, translateBack);
        }
		
		return pixels.toMat();
	}
	
	public static Mat escala(Mat img, double sx, double sy) {
		PixelArray pixels = new PixelArray(img);
		
		double[][] matriz = matrizEscala(sx, sy);
		for(Pixel pixel : pixels) {
			transforma(pixel, matriz);
		}
		
		return pixels.toMat();
	}
	
	static void transforma(Pixel pixel, double[][] matriz) {
		double[] vec = {pixel.i, pixel.j, 1};
        double[] result = multiplica(matriz, vec);

        pixel.i = result[0];
        pixel.j = result[1];
	}
	
	static double[] multiplica(double[][] matriz, double[] vetor) {
		double[] result = new double[vetor.length];
		
		for(int i = 0; i < vetor.length; i++) {
			result[i] = 0;
			for(int k = 0; k < vetor.length; k++) {
				result[i] += matriz[i][k] * vetor[k];
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unused")
	private static double[][] multiplica(double[][] a, double[][] b) {
		double[][] result = new double[a.length][];
		
		for(int i = 0; i < result.length; i++) {
			result[i] = new double[b[0].length];
			for(int j = 0; j < result[i].length; j++) {
				result[i][j] = 0;
				for(int k = 0; k < b.length; k++) {
					result[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		
		return result;
	}
	
	private static double[][] matrizIdentidade() {
		double[][] matriz = new double[3][];
		for(int i = 0; i < 3; i++) {
			matriz[i] = new double[3];
			for(int j = 0; j < 3; j++) {
				matriz[i][j] = 0;
			}
		}
		matriz[0][0] = 1;
		matriz[1][1] = 1;
		matriz[2][2] = 1;
		return matriz;
	}
	
	private static double[][] matrizRotacao(double angulo) {
        double[][] matriz = matrizIdentidade();

        matriz[0][0] = Math.cos(angulo);
        matriz[0][1] = Math.sin(angulo) * -1;
        matriz[1][0] = Math.sin(angulo);
        matriz[1][1] = Math.cos(angulo);

        return matriz;
    }
	
	private static double[][] matrizTranslacao(int dx, int dy) {
        double[][] mat = matrizIdentidade();

        mat[0][2] = dx;
        mat[1][2] = dy;

        return mat;
    }
	
	private static double[][] matrizEscala(double sx, double sy) {
		double[][] mat = matrizIdentidade();
		
		mat[0][0] = sx;
		mat[1][1] = sy;
		
		return mat;
	}
}
