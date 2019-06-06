import org.opencv.core.Mat;

public class Cadeia {
	public static Mat cadeia2(Mat img) {
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
		
		int i = 0;
		int j = 0;
		Direction direction = Direction.east;
		do {
			for(int k = 0; k < 8; k++) {
				Direction d = direction.getNextDirection();
				double[] pixel = img.get(i + d.di, j + d.dj);
				if(pixel[0] == 0) {
					
				}
			}
		} while(i != startI && j != startJ);
		
		return img;
	}
	
	public static Mat cadeia(Mat img) {
		Mat mat = img.clone();
		
		Pixel start = null;
		for(int i = 0; i < mat.rows(); i++) {
			for(int j = 0; j < mat.cols(); j++) {
				Pixel pixel = new Pixel(i, j, img.get(i, j));
				if(pixel.isBlack()) {
					start = pixel;
					break;
				}
			}
			if(start != null) {
				break;
			}
		}
		
		Walker walker = new Walker(mat, start);
		double[] color = {255, 255, 0};
		int i = 0;
		do {
			walker.next();
			Pixel p = walker.getPixel();
			mat.put((int) p.i, (int) p.j, color);
			i++;
		} while(i < 100);
		
		return mat;
	}
	
	private static class Walker {
		private Mat mat;
		private Pixel start;
		private Pixel pixel;
		private Direction direction;
		
		public Walker(Mat mat, Pixel start) {
			super();
			this.mat = mat;
			this.start = start;
			this.pixel = start;
			this.direction = Direction.east;
		}

		public void next() {
			Direction d = this.direction.getNextDirection();
			while(!d.equals(this.direction)) {
				int i = (int) pixel.i;
				int j = (int) pixel.j;
				
				Pixel p1 = getPixelInDirection(mat, i, j, d.getNextDirection());
				Pixel p2 = getPixelInDirection(mat, i, j, d);
				
				if(p1.isWhite() && p2.isBlack()) {
					this.pixel = p2;
					break;
				}
				
				d = d.getNextDirection();
			}
			
			System.out.println(String.format("%s %d %d", d.toString(), (int) this.pixel.i, (int) this.pixel.j));
			this.direction = d;
		}
		
		public boolean hasNext() {
			if(this.pixel.i == this.start.i && this.pixel.j == this.start.j) {
				return false;
			}
			return true;
		}
		
		public Pixel getPixel() {
			return this.pixel;
		}
		
		private Pixel getPixelInDirection(Mat mat, int i, int j, Direction direction) {
			int ni = i + direction.di;
			int nj = j + direction.dj;
			double[] bgr = Utils.pixelOrNull(mat, ni, nj);
			if(bgr == null) {
				bgr = Pixel.bgrWhite;
			}
			return new Pixel(ni, nj, bgr);
		}
	}
	
	private static enum Direction {
		east(0, 1), northeast(-1, 1), north(-1, 0), northwest(-1, -1), west(0, -1), southwest(1, -1), south(1, 0), southeast(1, 1);
		
		int di, dj;
		
		private Direction(int di, int dj) {
			this.di = di;
			this.dj = dj;
		}
		
		public Direction getNextDirection() {
			int i = this.ordinal();
			i++;
			if(i == 8) {
				i = 0;
			}
			return Direction.values()[i];
		}
	}
	
	public static void main(String[] args) throws Exception {
		Main.main(args);
	}
}
