package NP.MidTerm1.zad14;

import java.util.Scanner;

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}

class MinMax<T extends Comparable<T>> {
    private T min;
    private T max;
    int minElements = 0, maxElements = 0, total = 0;

    public MinMax() {
        min = null;
        max = null;
    }

    public void update(T x) {
        if (min == null || min.compareTo(x) > 0) {
            min = x;
            minElements = 1;
        } else if (min.compareTo(x) == 0) {
            ++minElements;
        }
        if (max == null || max.compareTo(x) < 0) {
            max = x;
            maxElements = 1;
        } else if (max.compareTo(x) == 0) {
            maxElements++;
        }
        total++;
    }

    T max() {
        return max;
    }

    T min() {
        return min;
    }

    @Override
    public String toString() {
        return min + " " + max + " " + (total - maxElements - minElements) + "\n";
    }
}
