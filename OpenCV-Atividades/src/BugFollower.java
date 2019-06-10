import org.opencv.core.Mat;

public class BugFollower {
	public static Mat simple(Mat img) {
		Mat clone = img.clone();
		
		int startI = 0;
		int startJ = 0;
		boolean running = true;
		for(int i = 0; i < img.rows() && running; i++) {
			for(int j = 0; j < img.cols(); j++) {
				double[] pixel = img.get(i, j);
				if(pixel[0] == 0) {
					running = false;
					startI = i;
					startJ = j;
					break;
				}
			}
		}
		
		Bug bug = new Bug(startI, startJ);
		Direction direction = Direction.east;
		double[] color = {255,255,0};
		do {
			double[] pixel = img.get(bug.i, bug.j);
			if(pixel[0] == 0) {
				direction = direction.esquerda();
			} else if(pixel[0] == 255) {
				direction = direction.direita();
			}
			bug.move(direction);
			clone.put(bug.i, bug.j, color);
		} while (bug.i != startI || bug.j != startJ);
		
		return clone;
	}
	
	private static class Bug {
		int i, j;
		
		public Bug(int i, int j) {
			super();
			this.i = i;
			this.j = j;
		}
		
		public void move(Direction d) {
			this.i += d.i;
			this.j += d.j;
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
	}
	
	public static void main(String[] args) {
		Main.main(args);
	}
}
