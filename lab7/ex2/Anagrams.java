package NP.lab7.ex2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        // Vasiod kod ovde
        Scanner in = new Scanner(inputStream);
        TreeMap<String, ArrayList<String>> anagrams = new TreeMap<>();
        while (in.hasNextLine()) {
            String word = in.nextLine();
            boolean isAnAnagram = false;
            for (String key : anagrams.keySet()) {
                isAnAnagram = isAnagram(key, word);
                if (isAnagram(word, key)) {
                    anagrams.get(key).add(word);
                    break;
                }
            }
            if (!isAnAnagram) {
                ArrayList<String> list = new ArrayList<>();
                list.add(word);
                anagrams.put(word, list);
            }

        }
        StringBuilder sb = new StringBuilder();

        for (String key : anagrams.keySet()) {
            if (anagrams.get(key).size() >= 5) {
                sb.append(String.join(" ", anagrams.get(key)));
                sb.append("\n");
            }
        }
        System.out.println(sb);
    }

    public static boolean isAnagram(String firstWord, String secondWord) {
        char[] firstWordChars = firstWord.toCharArray();
        char[] secondWordChars = secondWord.toCharArray();
        Arrays.sort(firstWordChars);
        Arrays.sort(secondWordChars);
        return Arrays.equals(firstWordChars, secondWordChars);

    }
}

