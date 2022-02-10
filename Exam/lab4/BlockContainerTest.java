package NP.Exam.lab4;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<E extends Comparable<E>> {
    private int capacityOfBlock;
    private List<Set<E>> blocks;

    public BlockContainer(int capacityOfBlock) {
        this.capacityOfBlock = capacityOfBlock;
        this.blocks = new ArrayList<>();
    }

    public void add(E element) {
        if (!blocks.isEmpty()) {
            Set<E> lastSet = blocks.get(blocks.size() - 1);
            if (lastSet.size() == capacityOfBlock) {
                Set<E> newSet = new TreeSet<>();
                newSet.add(element);
                blocks.add(newSet);
            } else {
                lastSet.add(element);
            }
        } else {
            Set<E> newSet = new TreeSet<>();
            newSet.add(element);
            blocks.add(newSet);
        }
    }

    public boolean remove(E element) {
        Set<E> lastSet = blocks.get(blocks.size() - 1);
        boolean removeFlag = lastSet.remove(element);
        if (lastSet.size() == 0) {
            blocks.remove(blocks.size() - 1);
        }
        return removeFlag;
    }

    public void sort() {
        List<E> list = blocks
                .stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        blocks.clear();
        list.stream().forEach(this::add);
    }

    @Override
    public String toString() {
        return blocks
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде




