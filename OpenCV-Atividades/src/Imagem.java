import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Imagem {
	private Mat img;
	private String name;
	
	
	private Imagem(String name, Mat img) {
		this.img = img;
		this.name = name;
	}
	
	public static Imagem load(String name) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		return new Imagem(name, Imgcodecs.imread(name + ".png"));
	}
	
	public void save() {
		Imgcodecs.imwrite(this.name + ".png", this.img);
	}
	
	public Imagem grayscale() {
		return new Imagem(this.name + "-grayscale", Grayscale.convert(this.img));
	}
	
	public Imagem sobel(int limiar) {
		return new Imagem(this.name + "-sobel", Convolucao.sobel(this.img, limiar));
	}
	
	public Imagem mean() {
		return new Imagem(this.name + "-mean", Convolucao.operation(this.img, Convolucao::mean));
	}
	
	public Imagem median() {
		return new Imagem(this.name + "-median", Convolucao.operation(this.img, Convolucao::median));
	}
	
	public Imagem lantuejoul() {
		return new Imagem(this.name + "-lantuejoul", Esqueletizacao.lantuejoul(this.img));
	}
	
	public Imagem eqHSV() {
		return new Imagem(this.name + "-eqHSV", HistEq.eqHSV(this.img));
	}
	
	public Imagem eqGrayscale() {
		return new Imagem(this.name + "-eqGrayscale", HistEq.eqGrayscale(this.img));
	}
	
	public Imagem eqAllChannels() {
		return new Imagem(this.name + "-eqAllChannels", HistEq.eqAllChannels(this.img));
	}
	
	public Imagem separaCanais(int canal) {
		return new Imagem(this.name + "-separaCanais", SeparaCanais.separaCanais(this.img, canal));
	}
	
	public Imagem rotacao(double angulo) {
		return new Imagem(this.name + "-rotacao", Transformacao.rotacao(this.img, angulo));
	}
	
	public Imagem translacao(int dx, int dy) {
		return new Imagem(this.name + "-translacao", Transformacao.translacao(this.img, dx, dy));
	}
	
	public Imagem zoomInQuadrado() {
		return new Imagem(this.name + "-zoomInQuadrado", Zoom.zoomInQuadrado(this.img));
	}
	
	public Imagem zoomInLinear() {
		return new Imagem(this.name + "-zoomInLinear", Zoom.zoomInLinear(this.img));
	}
	
	public Imagem zoomOutQuadrado() {
		return new Imagem(this.name + "-zoomOutQuadrado", Zoom.zoomOutQuadrado(this.img));
	}
	
	public Imagem zoomOutLinear() {
		return new Imagem(this.name + "-zoomOutLinear", Zoom.zoomOutLinear(this.img));
	}
	
	public Imagem invert() {
		return new Imagem(this.name + "-invert", Zoom.invert(this.img));
	}
}
