
class Pixel {
	public static double[] bgrBlack = {0, 0, 0};
	public static double[] bgrWhite = {255, 255, 255};
	
	double i, j;
	double[] bgr;
	
	public Pixel(double i, double j, double[] bgr) {
		this.i = i;
		this.j = j;
		this.bgr = bgr;
	}
	
	public boolean isBlack() {
		return Pixel.isBlack(this.bgr);
	}
	
	public boolean isWhite() {
		return Pixel.isWhite(this.bgr);
	}
	
	public boolean isEqual(Pixel other) {
		return Pixel.isEqual(this.bgr, other.bgr);
	}
	
	public static boolean isEqual(double[] a, double[] b) {
		for(int i = 0; i < 3; i++) {
			if(a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean isBlack(double[] bgr) {
		for (int c = 0; c < 3; c++) {
			if(bgr[c] != 0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isWhite(double bgr[]) {
		for(int c = 0; c < 3; c++) {
			if(bgr[c] != 255) {
				return false;
			}
		}
		return true;
	}
}