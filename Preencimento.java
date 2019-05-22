package Preenchimento;

import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class Preencimento {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat img = Imgcodecs.imread("imagem.png");
		HighGui.imshow("img_original", img);
		//  B	 G	  R
		//65.0 158.0 247.0 -> orelha laranjinha
		//157.0 221.0 250.0 -> pele
		//255.0 158.0 247.0 -> rosa
		
		//System.out.println(img.get(90, 100)[0] + " " + img.get(90, 100)[1] + " " + img.get(90, 100)[2]);
		
		preenchimentoCentro(img);
		//preenchimentoCor(img);
		//preenchimentoPonto(img);
		
		HighGui.waitKey(0);
	}
	
	public static void preenchimentoCentro(Mat img) {
		int centroX = img.cols()/2;
		int centroY = img.rows()/2;
		
		Mat imgPre = img.clone();
		double[] novoPixel = new double [3];
		
		//RGB no opencv é BGR!
		Scanner sc = new Scanner(System.in);
		System.out.println("Digite o novo valor para R: ");
	    novoPixel[2] = sc.nextDouble();
	    System.out.println("Digite o novo valor para G: ");
	    novoPixel[1] = sc.nextDouble();
	    System.out.println("Digite o novo valor para B: ");
	    novoPixel[0] = sc.nextDouble();
				
	    imgPre = preencherImagem(imgPre, centroX, centroY, img.get(centroX, centroY), novoPixel);
	    
	    Imgcodecs.imwrite("img_preenchida_centro.png", imgPre);
		HighGui.imshow("img_preenchida_centro", imgPre);
	}
	
	public static void preenchimentoCor(Mat img) {
		Mat imgPre = img.clone();
		
		double[] pixelAntigo = new double [3];
		double[] novoPixel = new double [3];
		
		//RGB no opencv é BGR!
		Scanner sc = new Scanner(System.in);
		System.out.println("Digite o valor de R que deseja mudar: ");
		pixelAntigo[2] = sc.nextDouble();
	    System.out.println("Digite o valor de G que deseja mudar: ");
	    pixelAntigo[1] = sc.nextDouble();
	    System.out.println("Digite o valor de B que deseja mudar: ");
	    pixelAntigo[0] = sc.nextDouble();
		
		System.out.println("Digite o novo valor para R: ");
	    novoPixel[2] = sc.nextDouble();
	    System.out.println("Digite o novo valor para G: ");
	    novoPixel[1] = sc.nextDouble();
	    System.out.println("Digite o novo valor para B: ");
	    novoPixel[0] = sc.nextDouble();
		
	    for(int i = 0; i < img.rows(); i++) {
			for(int j = 0 ; j < img.cols() ; j++) {
				imgPre = preencherImagem(imgPre, i, j, pixelAntigo, novoPixel);
			}
	    }
	    
	    Imgcodecs.imwrite("img_preenchida_centro.png", imgPre);
		HighGui.imshow("img_preenchida_centro", imgPre);
	}
	
	public static void preenchimentoPonto(Mat img) {
		Mat imgPre = img.clone();

		double[] novoPixel = new double [3];
		
		//RGB no opencv é BGR!
		Scanner sc = new Scanner(System.in);
		System.out.println("Digite o valor X do ponto: ");
		int pontoX = sc.nextInt();
	    System.out.println("Digite o valor de Y do ponto: ");
	    int pontoY = sc.nextInt();
	    
		
		System.out.println("Digite o novo valor para R: ");
	    novoPixel[2] = sc.nextDouble();
	    System.out.println("Digite o novo valor para G: ");
	    novoPixel[1] = sc.nextDouble();
	    System.out.println("Digite o novo valor para B: ");
	    novoPixel[0] = sc.nextDouble();
		
		imgPre = preencherImagem(imgPre, pontoX, pontoY, img.get(pontoX, pontoY), novoPixel);

	    Imgcodecs.imwrite("img_preenchida_centro.png", imgPre);
		HighGui.imshow("img_preenchida_centro", imgPre);
	}
	
	
	
	public static Mat preencherImagem(Mat img, int i, int j, double[] pixel, double[] novoPixel) {
		if(i > img.rows() || j > img.cols() || i < 0 || j < 0 ) {
			return img;
		}

		double[] pixelAtual = img.get(i, j);
	
		if(pixelAtual != null) {
			if(estaNoLimiar(pixelAtual, pixel, 60)) {
				img.put(i,j,novoPixel);
			} else {
				return img;
			}
			
			img = preencherImagem(img, i - 1, j, pixel, novoPixel); //cima
			img = preencherImagem(img, i, j + 1, pixel, novoPixel); //direito
			img = preencherImagem(img, i, j - 1, pixel, novoPixel); //esquerdo
			img = preencherImagem(img, i + 1, j, pixel, novoPixel); //baixo
		
		}

		return img;
	}
	
	public static boolean estaNoLimiar(double[] cor, double[] novaCor, double limiar) {
		double[] maisLim = {novaCor[0] + limiar, novaCor[1] + limiar, novaCor[2] + limiar};
		double[] menosLim = {novaCor[0] - limiar, novaCor[1] - limiar, novaCor[2] - limiar};
		if((cor[0] >= menosLim[0] && cor[0] <= maisLim[0]) 
				&& (cor[1] >= menosLim[1] && cor[1] <= maisLim[1])
				&& (cor[2] >= menosLim[2] && cor[2] <= maisLim[2])) {
			return true;
		}
		return false;
	}
}
