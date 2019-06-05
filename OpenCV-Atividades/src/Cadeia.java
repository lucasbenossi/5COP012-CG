import org.opencv.core.Mat;

public class Cadeia {
	public Mat cadeia(Mat img) {
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
		}
		
		Pixel pixel = start;
		while(pixel != start) {
			
		}
		
		return mat;
	}
	
	private static class Walker {
		Pixel start;
		Pixel pixel;
		Direction direction;
		
		public void next() {
			
		}
		
		public boolean hasNext() {
			return this.pixel != this.start;
		}
	}
	
	private static enum Direction {
		east, northeast, north, northwest, west, southwest, south, southeast;
	}
}
