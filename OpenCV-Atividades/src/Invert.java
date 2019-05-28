import org.opencv.core.Mat;

public class Invert {
	public static Mat invert(Mat og) {
    	int rows = og.rows();
    	int cols = og.cols();
    	
    	Mat inverted = new Mat(rows, cols, og.type());
    	
    	for(int i = 0; i < rows; i++) {
    		for(int j = 0; j < cols; j++) {
    			double[] pixel = og.get(i, j);
    			
    			for (int k = 0; k < 3; k++) {
    	    		pixel[k] = 255 - pixel[k];
    	    	}
    			
    			inverted.put(i, j, pixel);
    		}
    	}
    	
    	return inverted;
    }
}
