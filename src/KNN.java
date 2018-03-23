import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class KNN {
	final static String pathPattern = "./data/";

	public static void main(String[] args) throws FileNotFoundException {
		double[][] trainData = read(pathPattern+"train.csv",421570);
		double[][] testData = read(pathPattern+"test.csv",115064);
		long startTime = System.nanoTime();
		predict(trainData,testData,5);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println(duration);
	}

	public static void predict(double[][] train,double[][] test,int k) throws FileNotFoundException {
		
		
		String fileName= "./data/test.csv";
		File file= new File(fileName);
		Scanner inputStream = new Scanner(file);
		String[] columns = inputStream.next().split(",");
		String[] data = new String[115064];
		int ii = 0;
		while(inputStream.hasNext()){
			String line= inputStream.next();
			String[] values = line.split(",");
			for(int j = 0 ; j < values.length ; j++) {
				if(j == 2) {
					data[ii] = values[j];
				} 
			}
			ii++;
		}
		inputStream.close();
		
		
		
		
		
		
		Hashtable<Double,Integer> hash = new Hashtable<Double,Integer>();
		for(int i = 0 ; i < test.length ; i++) {
			double[] euclids = new double[train.length];
			double sum = 0;
			for(int j = 0 ; j < train.length ; j++) {
				double euclid = euclidean(train[j][0],train[j][1],train[j][3],test[i][0],test[i][1],test[i][2]);
				euclids[j] = euclid;
				hash.put(euclid, j);
			}
			Arrays.sort(euclids);
			
//			for(int j=0;j<euclids.length;j++) {
//				System.out.println("----->"+euclids[j]);
//			}
			double w = 0;
			for(int j = 0 ; j < k ; j++) {
				w += 1/(euclids[j]+1.2345);
				//System.out.println("euclid : "+euclids[j]);
				sum += train[hash.get(euclids[j])][2] * (1/(euclids[j]+1.2345));
			}
//			System.out.println("w8 : "+w);
//			System.out.println("sum : "+sum);
			System.out.println((int)test[i][0]+"_"+(int)test[i][1]+"_"+data[i]+","+sum/(w));
		}
	}

	public static double euclidean(double a,double b,double c,double x,double y,double z) {
		return Math.sqrt(Math.pow(a-x, 2)+Math.pow(b-y, 2)+Math.pow(c-z, 2));
	}

	public static double[][] read(String path,int size) throws FileNotFoundException {
		String fileName= path;
		File file= new File(fileName);
		Scanner inputStream = new Scanner(file);
		String[] columns = inputStream.next().split(",");
		double[][] data = new double[size][columns.length-1];
		int i = 0;
		while(inputStream.hasNext()){
			String line= inputStream.next();
			String[] values = line.split(",");
			int k = 0;
			for(int j = 0 ; j < values.length ; j++) {
				if(j != 2) {
					if(j == values.length-1) {
						if(values[j].equals("TRUE")) {
							data[i][k] = 1;
						} else {
							data[i][k] = 0;
						}
					}else {
						data[i][k] = Double.parseDouble(values[j]);
					}
					k++;
				} 
			}
			i++;
		}
		inputStream.close();
		return data;
	}
}
