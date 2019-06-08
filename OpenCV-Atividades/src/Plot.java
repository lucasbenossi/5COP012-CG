import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class Plot {
	public static void plot() {
		int rows = 500;
		int cols = 500;
		Function function = new Function("f(x) = x^2");

		double hViewLo = -1;
		double hViewHi = 1;
		double vViewLo = -1;
		double vViewHi = 1;
		
		double iSteps = (vViewHi - vViewLo) / rows;
		double jSteps = (hViewHi - hViewLo) / cols;
		
		BufferedImage screen = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < cols; x++) {
			for(int y = 0; y < rows; y++) {
				screen.setRGB(x, y, Color.WHITE.getRGB());
			}
		}
		
		int j = (int) ((0 - hViewLo) / jSteps);
		if(j >= 0 && j < cols){
			for(int i = 0; i < rows; i++){
				screen.setRGB(j, i, Color.BLACK.getRGB());
			}
		}
		
		int i = (int) ((0 - vViewLo) / iSteps);
		if(i >= 0 && i < rows){
			for(j = 0; j < cols; j++){
				screen.setRGB(j, i, Color.BLACK.getRGB());
			}
		}
		
		for(j = 0; j < cols; j++){
			double x = j * jSteps + hViewLo;
			double y = new Expression("f("+x+")", function).calculate();
			i = (int) (rows - (y - vViewLo) / iSteps);
			if(i >= 0 && i < rows){
				screen.setRGB(j, i, Color.BLACK.getRGB());
			}
		}
		
		
		
		JLabel display = new JLabel(new ImageIcon(screen));
		
		JFrame frame = new JFrame();
		frame.add(display);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
