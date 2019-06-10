
public class Main {

	public static void main(String[] args) {
		System.out.println(Matrix.isBinary(Imagem.load("linha").sobel(30).getMat()));
	}
}
