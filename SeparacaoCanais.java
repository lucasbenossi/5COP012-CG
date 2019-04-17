package Atividade5;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class SeparacaoCanais{


		public static void main(String[] args) {
			//carregar a lib
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			//carregar imagem de um arquivo
			Mat img = Imgcodecs.imread("baboon.png");
			Mat img_R = img.clone();
			Mat img_G = img.clone();
			Mat img_B = img.clone();
			
			img_R = separarCanal(img,0);
			img_G = separarCanal(img,1);
			img_B = separarCanal(img,2);
			
						
			Imgcodecs.imwrite("img_CanalR.png", img_R);
			Imgcodecs.imwrite("img_CanalG.png", img_G);
			Imgcodecs.imwrite("img_CanalB.png", img_B);
			HighGui.imshow("img_CanalR", img_R);
			HighGui.imshow("img_CanalG", img_G);
			HighGui.imshow("img_CanalB", img_B);
			HighGui.imshow("img_Original", img);
			HighGui.waitKey(0);
		}		
		
		public static Mat separarCanal(Mat img, int canal) {
			Mat imgC = img.clone();
			
			for(int i = 0; i < img.rows(); i++) {
				for(int j = 0; j < img.cols(); j++) {
					double[] im = img.get(i,j);
					double[] vetor = {0,0,0};
					vetor[0] = im[canal];
					vetor[1] = im[canal];
					vetor[2] = im[canal];
					imgC.put(i,j, vetor);	
				}
			}
			
			
			return imgC;
		}
}