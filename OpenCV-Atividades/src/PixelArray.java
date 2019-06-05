import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.Mat;

class PixelArray implements Iterable<Pixel> {
	private Mat img;
	private ArrayList<Pixel> pixels;
	
	public PixelArray(Mat img) {
		this.img = img;
		this.pixels = new ArrayList<>(img.rows() * img.cols());
		
		for(int i = 0; i < img.rows(); i++) {
			for(int j = 0; j < img.cols(); j++) {
				pixels.add(new Pixel(i, j, img.get(i, j)));
			}
		}
	}
	
	public Mat toMat() {
		Mat mat = this.img.clone();
		Utils.paintItBlack(mat);
		
		for(Pixel pixel : this.pixels) {
			if(pixel.i >= 0 && pixel.i < this.img.rows() && pixel.j >= 0 && pixel.j < this.img.cols()) {
				mat.put((int) pixel.i, (int) pixel.j, pixel.bgr);
			}
		}
		
		return mat;
	}

	@Override
	public Iterator<Pixel> iterator() {
		return this.pixels.iterator();
	}
}