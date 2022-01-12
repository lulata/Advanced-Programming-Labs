package NP.MidTerm1.zad11;

import java.util.LinkedList;
import java.util.Scanner;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple

class Triple <T extends Number>{
    LinkedList<T> list;

    public Triple(T a, T b, T c){
        list = new LinkedList<>();
        list.add(a);
        list.add(b);
        list.add(c);
    }

    double max(){
       return list.stream().mapToDouble(Number::doubleValue).max().orElse(0.00);
    }

    double average(){
        return list.stream().mapToDouble(Number::doubleValue).average().orElse(0.00);
    }

    void sort(){
        list.sort((a,b)->a.doubleValue()>b.doubleValue()?1:-1);
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f",list.get(0).doubleValue()
                ,list.get(1).doubleValue(),list.get(2).doubleValue());
    }
}



