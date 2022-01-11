package NP.lab7.ex4;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[]{"во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја"};
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}

// vasiot kod ovde
class TermFrequency {
    private List<String> filtered;
    private int total;
    private int distinct;

    public TermFrequency(InputStream in, String[] stopWords) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        this.filtered = bf.lines().flatMap(line -> Arrays.stream(line.split(" +")))
                .map(String::toLowerCase).map(word -> {
                    if (word.length() == 0)
                        return word;
                    if (word.charAt(word.length() - 1) == ',' || word.charAt(word.length() - 1) == '.')
                        word = word.substring(0, word.length() - 1);
                    return word;
                }).filter(word -> {
                    return !Arrays.asList(stopWords).contains(word) && word.length() > 0;
                }).collect(Collectors.toList());

        this.total = filtered.size();
        this.distinct = (int) filtered.stream().distinct().count();
    }

    public int countTotal() {
        return total;
    }

    public int countDistinct() {
        return distinct;
    }

    List<String> mostOften(int n) {
        Map<String, List<String>> map = filtered.stream().collect(Collectors.groupingBy(word -> word));
        Comparator<Entry<String, List<String>>> byValue = (e1, e2) -> {
            if (e1.getValue().size() == e2.getValue().size()) {
                return e1.getKey().compareTo(e2.getKey());
            }
            return e2.getValue().size() - e1.getValue().size();
        };

        return map.entrySet().stream().sorted(byValue).limit(n).map(e -> e.getKey()).collect(Collectors.toList());
    }
}
