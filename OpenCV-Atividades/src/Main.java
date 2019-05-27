
public class Main {

	public static void main(String[] args) throws Exception {
		Imagem img = Imagem.load("debian");
		img.rotacao(30).save();
	}
}
