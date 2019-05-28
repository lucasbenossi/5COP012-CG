
public class Main {

	public static void main(String[] args) throws Exception {
		Imagem img = Imagem.load("circulo");
		PreenchimentoJanela.abrir(img.escala(0.5, 0.5));
	}
}
