package NP.MidTerm2.lab10;

import java.util.*;
import java.util.stream.Collectors;

class Names {
    private TreeMap<String, Integer> names;

    public Names() {
        this.names = new TreeMap<>();
    }

    public void addName(String name) {
        Integer value = names.computeIfAbsent(name, key -> 0);
        names.put(name, value + 1);
    }

    public void printN(int n) {
        names.entrySet().stream()
                .filter(e -> e.getValue() >= n)
                .forEach(e -> System.out.printf("%s (%d) %d\n",
                        e.getKey(), e.getValue(), distinctChars(e.getKey())));
    }

    private int distinctChars(String string) {
        return (int) string.toUpperCase().chars()
                .mapToObj(c -> (char) c)
                .distinct()
                .count();
    }


    public String findName(int len, int index) {
        deleteWantedNames(len);
        return new ArrayList<>(names.keySet()).get(index % names.size());
    }

    private void deleteWantedNames(int len) {
        names = names.entrySet().stream()
                .filter(e -> e.getKey().length() < len)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> n, //useless since all entries are distinct
                        TreeMap::new));
    }
}


public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde
