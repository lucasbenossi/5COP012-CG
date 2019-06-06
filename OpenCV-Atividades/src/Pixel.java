
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
		for (int c = 0; c < 3; c++) {
			if(this.bgr[c] != 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isWhite() {
		for(int c = 0; c < 3; c++) {
			if(this.bgr[c] != 255) {
				return false;
			}
		}
		return true;
	}
}