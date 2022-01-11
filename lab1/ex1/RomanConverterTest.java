package NP.lab1.ex1;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    public static String toRoman(int n) {
        String roman = "";
        int numbers[] = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String letters[] = {"M",  "CM",  "D",  "CD", "C",  "XC", "L",  "XL",  "X",  "IX", "V",  "IV", "I"};
        int temp = n;
        for (int i = 0; i < numbers.length; i++){
            while (temp >= numbers[i]){
                roman += letters[i];
                temp -= numbers[i];
            }
        }

        return roman;
    }

}
