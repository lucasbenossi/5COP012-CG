import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Imagem {
	private Mat mat;
	private String name;
	
	
	public Imagem(String name, Mat img) {
		this.mat = img;
		this.name = name;
	}
	
	public static Imagem load(String name) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		return new Imagem(name, Imgcodecs.imread(name + ".png"));
	}
	
	public void save() {
		Imgcodecs.imwrite(this.name + ".png", this.mat);
	}
	
	public void show() {
		JLabel display = new JLabel();
		display.setIcon(new ImageIcon(Utils.matToBufferedImage(this.mat)));
		
		JFrame frame = new JFrame();
		frame.add(display);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Mat getMat() {
		return this.mat;
	}
	
	public Imagem hough() {
		return new Imagem(this.name + "-hough", Hough.hough(this.mat));
	}
	
	public Imagem cadeia() {
		return new Imagem(this.name + "-cadeia", Cadeia.cadeia(this.mat));
	}
	
	public Imagem preenchimento(int i, int j, double[] novo, double limiar) {
		return new Imagem(this.name + "-preenchimento", Preencimento.preencher(this.mat, i, j, novo, limiar));
	}
	
	public Imagem grayscale() {
		return new Imagem(this.name + "-grayscale", Grayscale.convert(this.mat));
	}
	
	public Imagem sobel(int limiar) {
		return new Imagem(this.name + "-sobel", Convolucao.sobel(this.mat, limiar));
	}
	
	public Imagem mean() {
		return new Imagem(this.name + "-mean", Convolucao.operation(this.mat, Convolucao::mean));
	}
	
	public Imagem median() {
		return new Imagem(this.name + "-median", Convolucao.operation(this.mat, Convolucao::median));
	}
	
	public Imagem lantuejoul() {
		return new Imagem(this.name + "-lantuejoul", Esqueletizacao.lantuejoul(this.mat));
	}
	
	public Imagem eqHSV() {
		return new Imagem(this.name + "-eqHSV", HistEq.eqHSV(this.mat));
	}
	
	public Imagem eqGrayscale() {
		return new Imagem(this.name + "-eqGrayscale", HistEq.eqGrayscale(this.mat));
	}
	
	public Imagem eqAllChannels() {
		return new Imagem(this.name + "-eqAllChannels", HistEq.eqAllChannels(this.mat));
	}
	
	public Imagem separaCanais(int canal) {
		return new Imagem(this.name + "-separaCanais", SeparaCanais.separaCanais(this.mat, canal));
	}
	
	public Imagem rotacao(double angulo) {
		return new Imagem(this.name + "-rotacao", Transformacao.rotacao(this.mat, angulo));
	}
	
	public Imagem translacao(int dx, int dy) {
		return new Imagem(this.name + "-translacao", Transformacao.translacao(this.mat, dx, dy));
	}
	
	public Imagem escala(double sx, double sy) {
		return new Imagem(this.name + "-escala", Transformacao.escala(this.mat, sx, sy));
	}
	
	public Imagem zoomInQuadrado() {
		return new Imagem(this.name + "-zoomInQuadrado", Zoom.zoomInQuadrado(this.mat));
	}
	
	public Imagem zoomInLinear() {
		return new Imagem(this.name + "-zoomInLinear", Zoom.zoomInLinear(this.mat));
	}
	
	public Imagem zoomOutQuadrado() {
		return new Imagem(this.name + "-zoomOutQuadrado", Zoom.zoomOutQuadrado(this.mat));
	}
	
	public Imagem zoomOutLinear() {
		return new Imagem(this.name + "-zoomOutLinear", Zoom.zoomOutLinear(this.mat));
	}
	
	public Imagem invert() {
		return new Imagem(this.name + "-invert", Invert.invert(this.mat));
	}
}
