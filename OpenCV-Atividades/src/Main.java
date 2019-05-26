
public class Main {

	public static void main(String[] args) throws Exception {
		Imagem img = Imagem.load("waiting_for_the_but");
		double[] novo = {0, 0, 255};
		img.preenchimento(112, 112, novo, 50).save();
	}
}
