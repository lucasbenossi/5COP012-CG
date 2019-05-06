import java.util.ArrayList;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Transformacao {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		try (Scanner scan = new Scanner(System.in);) {
			Mat img = Imgcodecs.imread("hl2ep2.png");
			
			System.out.print("angulo: ");
			double angulo = scan.nextInt();
			scan.nextLine();
			System.out.print("dx: ");
			int dx = scan.nextInt();
			scan.nextLine();
			System.out.print("dy: ");
			int dy = scan.nextInt();
			scan.nextLine();
			
			Imgcodecs.imwrite("hl2ep2-rotacao.png", rotacao(img, angulo));
			Imgcodecs.imwrite("hl2ep2-translacao.png", translacao(img, dx, dy));
			
			System.out.println("done");
		}
	}
	
	public static Mat translacao(Mat img, int dx, int dy) {
		ArrayList<Pixel> pixels = matParaArrayDePixels(img);
		
		double[][] matrizTranslacao = matrizTranslacao(dx, dy);
		for(Pixel pixel : pixels) {
			transformacao(matrizTranslacao, pixel);
		}
		
		return arrayDePixelsParsMat(pixels, img);
	}
	
	public static Mat rotacao(Mat img, double angulo) {
		ArrayList<Pixel> pixels = matParaArrayDePixels(img);
		
		angulo = Math.toRadians(angulo);

        double[][] translateOrigin = matrizTranslacao(img.rows() / -2, img.cols() / -2);
        double[][] rotationMatrix = matrizRotacao(angulo);
        double[][] translateBack = matrizTranslacao(img.rows() / 2, img.cols() / 2);

        for (Pixel pixel : pixels) {
            transformacao(translateOrigin, pixel);
            transformacao(rotationMatrix, pixel);
            transformacao(translateBack, pixel);
        }
		
		return arrayDePixelsParsMat(pixels, img);
	}
	
	public static Pixel transformacao(double[][] matriz, Pixel pixel) {
        double[] vec3 = {pixel.x, pixel.y, 1};
        double[] vec2 = {pixel.x, pixel.y};
        double[] result;

        if (matriz.length == 2) {
            result = multiplica(vec2, matriz);
        } else {
            result = multiplica(vec3, matriz);
        }

        pixel.x = result[0];
        pixel.y = result[1];

        return pixel;
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
	
	public static double[][] matrizIdentidade() {
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
	
	public static double[][] matrizRotacao(double angulo) {
        double[][] matriz = matrizIdentidade();

        matriz[0][0] = Math.cos(angulo);
        matriz[0][1] = Math.sin(angulo) * -1;
        matriz[1][0] = Math.sin(angulo);
        matriz[1][1] = Math.cos(angulo);

        return matriz;
    }
	
	public static double[][] matrizTranslacao(int dx, int dy) {
        double[][] mat = matrizIdentidade();

        mat[0][2] = dx;
        mat[1][2] = dy;

        return mat;
    }
	
	public static ArrayList<Pixel> matParaArrayDePixels(Mat img){
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				pixels.add(new Pixel(i, j, img.get(i, j)));
			}
		}
		
		return pixels;
	}
	
	public static Mat arrayDePixelsParsMat(ArrayList<Pixel> pixels, Mat original) {
		Mat novo = new Mat(original.rows(), original.cols(), original.type());
		
		for(int i = 0; i < original.rows(); i++) {
			for(int j = 0; j < original.cols(); j++) {
				double[] rgb = {0, 0, 0};
				novo.put(i, j, rgb);
			}
		}
		
		for(Pixel pixel : pixels) {
			if(pixel.x >= 0 && pixel.x < original.rows() && pixel.y >= 0 && pixel.y < original.cols()) {
				novo.put((int) pixel.x, (int) pixel.y, pixel.rgb);
			}
		}
		
		return novo;
	}
	
	private static class Pixel {
		double x, y;
		double[] rgb;
		
		public Pixel(double x, double y, double[] rgb) {
			this.x = x;
			this.y = y;
			this.rgb = rgb;
		}
	}
}
