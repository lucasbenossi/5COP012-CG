
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class PreenchimentoJanela {
	private static Mat mat;
	
	public static void abrir(Imagem imagem) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat original = imagem.getMat();
		mat = original.clone();

		JLabel labelR = new JLabel("R: ");
		JTextField textR = new JTextField(3);
		JLabel labelG = new JLabel("G: ");
		JTextField textG = new JTextField(3);
		JLabel labelB = new JLabel("B: ");
		JTextField textB = new JTextField(3);
		JLabel labelLimiar = new JLabel("Limiar: ");
		JTextField textLimiar = new JTextField(3);
		JButton resetButton = new JButton("reset");

		JLabel thumb = new JLabel();
		thumb.setIcon(new ImageIcon(matToBufferedImage(mat)));

		JPanel seletor = new JPanel();
		seletor.add(thumb);
		seletor.add(labelR);
		seletor.add(textR);
		seletor.add(labelG);
		seletor.add(textG);
		seletor.add(labelB);
		seletor.add(textB);
		seletor.add(labelLimiar);
		seletor.add(textLimiar);
		seletor.add(resetButton);
		seletor.setLayout(new BoxLayout(seletor, BoxLayout.Y_AXIS));

		thumb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {

				int x = event.getX();
				int y = event.getY();
				
				int limiar = Integer.parseInt(textLimiar.getText());
				int B = Integer.parseInt(textB.getText());
				int G = Integer.parseInt(textG.getText());
				int R = Integer.parseInt(textR.getText());
				
				double[] bgr = {B, G, R};
				
				Preencimento.preencherRecursivo(mat, y, x, mat.get(y, x), bgr, limiar);
				
				thumb.setIcon(new ImageIcon(matToBufferedImage(mat)));
			}
		});
		
		resetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				mat = original.clone();
				thumb.setIcon(new ImageIcon(matToBufferedImage(mat)));
			}
			
		});
		
		JPanel painel = new JPanel();
		painel.add(thumb);
		painel.add(seletor);

		JFrame frame = new JFrame("Preenchimento");
		frame.add(painel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	private static BufferedImage matToBufferedImage(Mat mat) {
		BufferedImage img = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < mat.rows(); i++) {
			for(int j = 0; j < mat.cols(); j++) {
				double[] pixel = mat.get(i, j);
				img.setRGB(j, i, new Color((int) pixel[2], (int) pixel[1], (int) pixel[0]).getRGB());
			}
		}
		return img;
	}

}
