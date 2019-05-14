import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList<double[]> list = new ArrayList<double[]>();
		
		for(int i = 10; i > 0; i--) {
			double[] arr = new double[3];
			arr[0] = i;
			arr[1] = i;
			arr[2] = i;
			
			list.add(arr);
		}
		
		list.sort(new MediaMediana.DoubleArrComp());
		
		for(double[] arr : list) {
			System.out.println(arr[0]);
		}
	}
}
