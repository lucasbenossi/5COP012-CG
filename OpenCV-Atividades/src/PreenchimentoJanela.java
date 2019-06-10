
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

		JLabel labelR = new JLabel("R:");
		JTextField textR = new JTextField(3);
		JLabel labelG = new JLabel("G:");
		JTextField textG = new JTextField(3);
		JLabel labelB = new JLabel("B:");
		JTextField textB = new JTextField(3);
		JLabel labelLimiar = new JLabel("Limiar:");
		JTextField textLimiar = new JTextField(3);
		JButton resetButton = new JButton("reset");

		JLabel display = new JLabel();
		display.setIcon(new ImageIcon(Matrix.matToBufferedImage(mat)));

		JPanel seletor = new JPanel();
		seletor.add(display);
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

		display.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {

				int x = event.getX();
				int y = event.getY();
				
				int limiar = Integer.parseInt(textLimiar.getText());
				int B = Integer.parseInt(textB.getText());
				int G = Integer.parseInt(textG.getText());
				int R = Integer.parseInt(textR.getText());
				
				double[] bgr = {B, G, R};
				
				Preencimento.preencherRecursivo(mat, y, x, mat.get(y, x), bgr, limiar);
				
				display.setIcon(new ImageIcon(Matrix.matToBufferedImage(mat)));
			}
		});
		
		resetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				mat = original.clone();
				display.setIcon(new ImageIcon(Matrix.matToBufferedImage(mat)));
			}
			
		});
		
		JPanel painel = new JPanel();
		painel.add(display);
		painel.add(seletor);

		JFrame frame = new JFrame("Preenchimento");
		frame.add(painel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
