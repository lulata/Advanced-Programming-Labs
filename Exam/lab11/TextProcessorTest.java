package NP.Exam.lab11;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class TextProcessor {
    List<Map<String, Integer>> listOfHistograms;
    Map<String, Integer> wordsHistogram;
    List<List<Integer>> vectorRepresentation;
    List<String> input;

    public TextProcessor() {
        this.listOfHistograms = new ArrayList<>();
        this.wordsHistogram = new HashMap<>();
        this.vectorRepresentation = new ArrayList<>();
        this.input = new LinkedList<>();
    }


    public void readText(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        input = br.lines().collect(Collectors.toList());
        input.stream()
                .forEach(line -> {
                    Map<String, Integer> histogram = Arrays.stream(line.toLowerCase().replaceAll("[^A-Za-z\\s+]", "").split("\\s+"))
                            .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum, LinkedHashMap::new));
                    listOfHistograms.add(histogram);
                });

        generateWordsHistogramMap();
        generateVectorRepresentation();
    }

    public void printTextsVectors(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        vectorRepresentation.stream().forEach(System.out::println);
        pw.flush();
    }


    public void printCorpus(OutputStream os, int n, boolean ascending) {
        PrintWriter pw = new PrintWriter(os);
        Comparator<Map.Entry<String, Integer>> entryComparator = (e1, e2) -> e1.getValue().compareTo(e2.getValue());
        entryComparator = ascending ? entryComparator : entryComparator.reversed();
        pw.println(wordsHistogram.entrySet().stream()
                .sorted(entryComparator.thenComparing(Map.Entry::getKey))
                .limit(n)
                .map(entry -> String.format("%s : %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n")));
        pw.flush();
    }

    public void mostSimilarTexts(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        double maxSimilarity = Double.MIN_VALUE;
        int indexOfFirstVector = 0;
        int indexOfSecondVector = 0;
        for (int i = 0; i < vectorRepresentation.size(); i++) {
            for (int j = 0; j < vectorRepresentation.size(); j++) {
                if (i != j) {
                    double similarity = CosineSimilarityCalculator.cosineSimilarity(vectorRepresentation.get(i), vectorRepresentation.get(j));
                    if (maxSimilarity < similarity) {
                        maxSimilarity = similarity;
                        indexOfFirstVector = i;
                        indexOfSecondVector = j;
                    }
                }
            }
        }
        pw.println(input.get(indexOfFirstVector));
        pw.println(input.get(indexOfSecondVector));
        pw.printf("%.10f", maxSimilarity);
        pw.flush();
    }

    private void generateWordsHistogramMap() {
        wordsHistogram = listOfHistograms.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
    }

    private void generateVectorRepresentation() {
        List<Map.Entry<String, Integer>> frequencies = getSortedWordFreq();
        listOfHistograms.stream().forEach(map -> {
            List<Integer> vector = new ArrayList<>();
            frequencies.stream().forEach(entry -> {
                vector.add(map.getOrDefault(entry.getKey(), 0));
            });
            vectorRepresentation.add(vector);
        });
    }

    private List<Map.Entry<String, Integer>> getSortedWordFreq() {
        return wordsHistogram.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }
}

class CosineSimilarityCalculator {
    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}

public class TextProcessorTest {

    public static void main(String[] args) {
        TextProcessor textProcessor = new TextProcessor();

        textProcessor.readText(System.in);

        System.out.println("===PRINT VECTORS===");
        textProcessor.printTextsVectors(System.out);

        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
        textProcessor.printCorpus(System.out, 20, true);

        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
        textProcessor.printCorpus(System.out, 20, false);

        System.out.println("===MOST SIMILAR TEXTS===");
        textProcessor.mostSimilarTexts(System.out);
    }
}
