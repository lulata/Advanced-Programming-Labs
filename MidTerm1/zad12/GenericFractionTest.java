package NP.MidTerm1.zad12;

import java.util.Scanner;

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

class GenericFraction<T extends Number, U extends Number> {
    T nominator;
    U denominator;

    public GenericFraction(T nominator, U denominator) throws ZeroDenominatorException {
        this.nominator = nominator;
        if (denominator.doubleValue() == 0) throw new ZeroDenominatorException();
        else this.denominator = denominator;
    }

    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        if (denominator != gf.denominator) {
            double imenitel;

            imenitel = denominator.doubleValue() * gf.denominator.doubleValue();
            GenericFraction<Double, Double> temp = new GenericFraction<Double, Double>(nominator.doubleValue() * gf.denominator.doubleValue() + gf.nominator.doubleValue() * denominator.doubleValue(), imenitel);

            return simplify(temp);
        }

        return simplify(new GenericFraction<Double, Double>(nominator.doubleValue() + gf.nominator.doubleValue(), denominator.doubleValue()));
    }

    double toDouble() {
        return nominator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString() {
        return String.format("%.2f / %.2f", nominator.doubleValue(), denominator.doubleValue());
    }

    public static GenericFraction<Double, Double> simplify(GenericFraction<Double, Double> dropka) throws ZeroDenominatorException {
        double tempbroitel = dropka.nominator;
        double tempimenitel = dropka.denominator;
        int nzd = 1;
        for (int i = 2; i < dropka.nominator / 2; ) {

            if (dropka.nominator % i != 0 || dropka.denominator % i != 0) i++;


            if (dropka.nominator % i == 0&&dropka.denominator % i == 0) {
                dropka.nominator /= i;
                dropka.denominator /= i;
                nzd *= i;

                if (dropka.nominator % i != 0 || dropka.denominator % i != 0) i++;

            }

        }


        return new GenericFraction<Double, Double>(tempbroitel / nzd, tempimenitel / nzd);


    }

}
class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException() {
        super("Denominator cannot be zero");
    }
}


