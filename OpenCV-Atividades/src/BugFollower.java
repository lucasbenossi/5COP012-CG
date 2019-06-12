
import org.opencv.core.Mat;

public class BugFollower {
	public static Mat bugFollower(Mat img, BugFollowerType type) {
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
		
		Bug bug = new Bug(clone, startI, startJ);
		Direction direction = Direction.east;
		do {
			double[] bgr = img.get(bug.i, bug.j);
			if(type.equals(BugFollowerType.simple)) {
				if(Pixel.isBlack(bgr)) {
					direction = direction.esquerda();
					bug.move(direction);
				} else if(Pixel.isWhite(bgr)) {
					direction = direction.direita();
					bug.move(direction);
				}
			} else if(type.equals(BugFollowerType.backtracking)) {
				if(Pixel.isBlack(bgr)) {
					direction = direction.back();
					bug.move(direction);
					direction = direction.direita();
					bug.move(direction);
				} else if(Pixel.isWhite(bgr)) {
					direction = direction.direita();
					bug.move(direction);
				}
			}
		} while (!bug.isFinished());
		
		return clone;
	}
	
	private static class Bug {
		int startI, startJ, i, j;
		private Mat mat;
		private int moves;
		
		public Bug(Mat mat, int startI, int startJ) {
			super();
			this.startI = startI;
			this.startJ = startJ;
			this.mat = mat;
			
			this.i = startI;
			this.j = startJ;
			
			this.moves = 0;
		}
		
		public void move(Direction d) {
			this.i += d.i;
			this.j += d.j;
			mat.put(i, j, Pixel.bgrFuchsia);
			this.moves++;
		}
		
		public boolean isFinished() {
			return i == startI && j == startJ && moves > 20;
		}
	}
	
	private static enum Direction {
		east(0, 1), north(-1, 0), west(0, -1), south(1, 0);
		
		int i, j;

		private Direction(int i, int j) {
			this.i = i;
			this.j = j;
		}
		
		public Direction direita() {
			return Direction.values()[(this.ordinal() + 3) % 4];
		}
		
		public Direction esquerda() {
			return Direction.values()[(this.ordinal() + 1) % 4];
		}
		
		public Direction back() {
			return Direction.values()[(this.ordinal() + 2) % 4];
		}
	}
	
	public static enum BugFollowerType {
		simple, backtracking;
	}
	
	public static void main(String[] args) {
		Main.main(args);
	}
}
