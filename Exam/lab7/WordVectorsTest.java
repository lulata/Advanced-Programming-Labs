package NP.Exam.lab7;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Vector {
    public static List<Integer> sumVectors(List<Integer> v1, List<Integer> v2) {
        return IntStream.range(0, v1.size())
                .mapToObj(i -> v1.get(i) + v2.get(i))
                .collect(Collectors.toList());
    }
}

class WordVectors {
    Map<String, List<Integer>> vectorsByWords;
    List<List<Integer>> text;

    public WordVectors(String[] words, List<List<Integer>> vectors) {
        this.text = new LinkedList<>();
        vectorsByWords = IntStream.range(0, words.length)
                .boxed()
                .collect(Collectors.toMap(i -> words[i], vectors::get));
    }

    public void readWords(List<String> words) {
        words.stream()
                .map(word -> vectorsByWords.getOrDefault(word, Arrays.asList(5, 5, 5, 5, 5)))
                .forEach(v -> text.add(v));
    }

    public List<Integer> slidingWindow(int n) {
        return IntStream.range(0, text.size() - n + 1)
                .mapToObj(i -> text.subList(i, i + n)
                        .stream()
                        .reduce(Arrays.asList(0, 0, 0, 0, 0), Vector::sumVectors))
                .map(list -> list.stream()
                        .mapToInt(i -> i)
                        .max()
                        .orElse(-1))
                .collect(Collectors.toList());
    }
}

public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}




