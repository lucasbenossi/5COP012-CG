import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.Mat;

public class Transformacao {
	public static Mat translacao(Mat img, int dx, int dy) {
		PixelArray pixels = new PixelArray(img);
		
		double[][] matrizTranslacao = matrizTranslacao(dx, dy);
		for(Pixel pixel : pixels) {
			transformacao(matrizTranslacao, pixel);
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
            transformacao(translateOrigin, pixel);
            transformacao(rotationMatrix, pixel);
            transformacao(translateBack, pixel);
        }
		
		return pixels.toMat();
	}
	
	private static void transformacao(double[][] matriz, Pixel pixel) {
        double[] result;

        if (matriz.length == 2) {
        	double[] vec = {pixel.i, pixel.j};
            result = multiplica(vec, matriz);
        } else {
        	double[] vec = {pixel.i, pixel.j, 1};
            result = multiplica(vec, matriz);
        }

        pixel.i = result[0];
        pixel.j = result[1];
    }
	
	private static double[] multiplica(double[] vetor, double[][] matriz) {
        double[] result = new double[vetor.length];
        
        for (int i = 0; i < vetor.length; i++) {
            double sum = 0;
            for (int j = 0; j < vetor.length; j++) {
                sum += vetor[j] * matriz[i][j];
            }
            result[i] = sum;
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
	
	private static class Pixel {
		double i, j;
		double[] rgb;
		
		public Pixel(double i, double j, double[] rgb) {
			this.i = i;
			this.j = j;
			this.rgb = rgb;
		}
	}
	
	private static class PixelArray implements Iterable<Pixel> {
		private Mat img;
		private ArrayList<Pixel> pixels;
		
		public PixelArray(Mat img) {
			this.img = img;
			this.pixels = new ArrayList<>(img.rows() * img.cols());
			
			for(int i = 0; i < img.rows(); i++) {
				for(int j = 0; j < img.cols(); j++) {
					pixels.add(new Pixel(i, j, img.get(i, j)));
				}
			}
		}
		
		public Mat toMat() {
			Mat mat = Utils.createBlackImg(this.img.rows(), this.img.cols(), this.img.type());
			
			for(Pixel pixel : this.pixels) {
				if(pixel.i >= 0 && pixel.i < this.img.rows() && pixel.j >= 0 && pixel.j < this.img.cols()) {
					mat.put((int) pixel.i, (int) pixel.j, pixel.rgb);
				}
			}
			
			return mat;
		}

		@Override
		public Iterator<Pixel> iterator() {
			return this.pixels.iterator();
		}
	}
}
