import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class Plot {
	public static void plot() {
		int width, height;
		Function function;
		
		try(Scanner scan = new Scanner(System.in);) {
			System.out.print("Width: ");
			width = scan.nextInt();
			scan.nextLine();
			
			System.out.print("Height: ");
			height = scan.nextInt();
			scan.nextLine();
			
//			System.out.print("Function: ");
//			function = new Function(scan.nextLine().trim());
		}
		
//		System.out.println(new Expression("f(3)", function).calculate());
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				image.setRGB(x, y, Color.WHITE.getRGB());
			}
		}
		
		JLabel display = new JLabel(new ImageIcon(image));
		
		JFrame frame = new JFrame();
		frame.add(display);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
