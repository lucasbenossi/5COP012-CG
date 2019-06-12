import org.opencv.core.Mat;

public class Cadeia {
	public static Mat cadeia(Mat img) throws RuntimeException {
		if(!Matrix.isBinary(img)) {
			throw new RuntimeException("Imagem não é binária.");
		}
		
		Mat clone = img.clone();
		
		int startI = 0;
		int startJ = 0;
		
		boolean running = true;
		for(int i = 0; i < img.rows() && running; i++) {
			for(int j = 0; j < img.cols(); j++) {
				if(Pixel.isBlack(img.get(i, j))) {
					running = false;
					startI = i;
					startJ = j;
					break;
				}
			}
		}
		
		int i = startI;
		int j = startJ;
		clone.put(i, j, Pixel.bgrFuchsia);
		
		Direction anterior = Direction.east;
		do {
			Direction d = anterior;
			do {
				d = d.next();
				if(img.get(i+d.i, j+d.j)[0] == 0) {
					Direction d2 = d.next();
					if(img.get(i+d2.i, j+d2.j)[0] == 255) {
						break;
					}
				}
			} while(!d.equals(anterior));
			
			i += d.i;
			j += d.j;
			clone.put(i, j, Pixel.bgrFuchsia);
			System.out.println(d);
		} while(i != startI || j != startJ);
		
		return clone;
	}
	
	private static enum Direction {
		east(0, 1), northeast(-1, 1), north(-1, 0), northwest(-1, -1), west(0, -1), southwest(1, -1), south(1, 0), southeast(1, 1);
		
		int i, j;
		
		private Direction(int i, int j) {
			this.i = i;
			this.j = j;
		}
		
		public Direction next() {
			return Direction.values()[(this.ordinal() + 1) % 8];
		}
	}
	
	public static void main(String[] args) throws Exception {
		Main.main(args);
	}
}
