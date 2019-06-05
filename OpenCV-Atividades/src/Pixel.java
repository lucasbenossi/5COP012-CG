
class Pixel {
	public static double[] BLACK = {0, 0, 0};
	public static double[] WHITE = {255, 255, 255};
	
	double i, j;
	double[] bgr;
	
	public Pixel(double i, double j, double[] rgb) {
		this.i = i;
		this.j = j;
		this.bgr = rgb;
	}
	
	public boolean isBlack() {
		for (int c = 0; c < 3; c++) {
			if(this.bgr[c] != 0) {
				return false;
			}
		}
		return true;
	}
}