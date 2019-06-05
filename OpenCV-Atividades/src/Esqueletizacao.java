import org.opencv.core.Mat;

public class Esqueletizacao {
	
	private static final double[] black = {0, 0, 0};
	static final double[] white = {255, 255, 255};
	private static final boolean[][] estruturante = {{false, true, false}, {true, true, true}, {false, true, false}};
    private static final int range = 3;
    
    public static Mat lantuejoul(Mat img) {
    	Mat result = new Mat(img.rows(), img.cols(), img.type());
        Utils.paintItWhite(result);

        Mat corroded = img.clone();

        while (!isFullWhite(corroded)) {
            result = add(result, subtract(corroded, opening(corroded)));
            corroded = erosion(corroded);
        }

        return result;
    }
    
    private static Mat erosion(Mat img) {
        Mat result = new Mat(img.rows(), img.cols(), img.type());
        Utils.paintItWhite(result);
        for (int i = 1; i < img.rows() - 1; i++) {
            for (int j = 1; j < img.cols() - 1; j++) {
                double[] centro = img.get(i, j);
                if (centro[0] == 0) {
                    boolean contido = true;
                    for (int k = 0; k < range && contido; k++) {
                        for (int m = 0; m < range; m++) {
                            double[] pixel = img.get(i - 1 + k, j - 1 + m);
                            if (estruturante[k][m] && pixel[0] != 0) {
                                contido = false;
                                break;
                            }
                        }
                    }
                    if (contido) {
                        result.put(i, j, black);
                    }
                }
            }
        }

        return result;
    }
    
    private static Mat dilate(Mat img) {
        Mat result = new Mat(img.rows(), img.cols(), img.type());
        Utils.paintItWhite(result);
        for (int i = 1; i < img.rows() - 1; i++) {
            for (int j = 1; j < img.cols() - 1; j++) {
                double[] centro = img.get(i, j);
                if (centro[0] == 0) {
                    boolean contido = true;
                    for (int k = 0; k < range && contido; k++) {
                        for (int m = 0; m < range; m++) {
                            if (estruturante[k][m]) {
                                result.put(i - 1 + k, j - 1 + m, black);
                            }
                        }
                    }
                }

            }
        }
        return result;
    }
    
    private static Mat opening(Mat img) {
        return dilate(erosion(img));
    }

    @SuppressWarnings("unused")
	private static Mat closing(Mat img) {
        return erosion(dilate(img));
    }
    
    private static boolean isFullWhite(Mat mat) {
        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.cols(); j++) {
                if (mat.get(i, j)[0] != 255) {
                    return false;
                }
            }
        }
        return true;
    }
	
	@SuppressWarnings("unused")
	private static Mat and(Mat a, Mat b) {
		Mat result = new Mat(a.rows(), a.cols(), a.type());
		for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) {
                double[] pixelA = a.get(i, j);
                double[] pixelB = b.get(i, j);
                if (pixelA[0] == 0 && pixelB[0] == 0) {
                    result.put(i, j, black);
                } else {
                    result.put(i, j, white);
                }
            }
        }
		return result;
	}
	
	private static Mat or(Mat a, Mat b) {
		Mat result = new Mat(a.rows(), a.cols(), a.type());
		for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) {
                double[] pixelA = a.get(i, j);
                double[] pixelB = b.get(i, j);
                if (pixelA[0] == 0 || pixelB[0] == 0) {
                    result.put(i, j, black);
                } else {
                    result.put(i, j, white);
                }
            }
        }
		return result;
	}
	
	@SuppressWarnings("unused")
	private static Mat complement(Mat a) {
		Mat result = new Mat(a.rows(), a.cols(), a.type());
		for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) {
                double[] pixel = a.get(i, j);
                if(pixel[0] == 255) {
                	result.put(i, j, black);
                } else {
                	result.put(i, j, white);
                }
            }
        }
		return result;
	}
	
	private static Mat subtract(Mat a, Mat b) {
		Mat result = new Mat(a.rows(), a.cols(), a.type());
		for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.cols(); j++) {
                double[] pixelA = a.get(i, j);
                double[] pixelB = b.get(i, j);
                if (pixelA[0] == 0 && pixelB[0] == 255) {
                    result.put(i, j, black);
                } else {
                    result.put(i, j, white);
                }
            }
        }
		return result;
	}
	
	private static Mat add(Mat a, Mat b) {
		return or(a, b);
	}
}
