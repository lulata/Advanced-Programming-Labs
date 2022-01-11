package NP.lab6.ex2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class IntegerList {
    private ArrayList<Integer> list;

    public IntegerList() {
        this.list = new ArrayList<>();
    }

    public IntegerList(Integer... numbers) {
        this.list = new ArrayList<>(numbers.length);
        Collections.addAll(this.list, numbers);
    }

    void add(int el, int idx) {
        if (idx <= list.size()) {
            list.add(idx, el);
        } else {
            ArrayList<Integer> tmp = new ArrayList<>(idx + 1);
            tmp.addAll(list);
            for (int i = list.size(); i < idx; i++) {
                tmp.add(0);
            }
            tmp.add(el);
            list = tmp;
        }
    }

    int remove(int idx) {
        inRange(idx);
        return list.remove(idx);
    }

    public void set(int el, int idx) {
        inRange(idx);
        list.set(idx, el);
    }

    int get(int idx) {
        inRange(idx);
        return list.get(idx);
    }

    int size() {
        return list.size();
    }

    int count(int el) {
        return (int) list.stream().filter(integer -> integer.equals(el)).count();
    }

    void removeDuplicates() {
        ArrayList<Integer> newList = new ArrayList<>();
        Collections.reverse(list);
        list.forEach(integer -> {
            if (!newList.contains(integer)) {
                newList.add(0, integer);
            }
        });
        list = newList;
    }

    int sumFirst(int k) {
        return list.stream().limit(k).mapToInt(Integer::intValue).sum();
    }


    int sumLast(int k) {
        return list.stream().skip(list.size() - k).mapToInt(Integer::intValue).sum();
    }

    public void shiftRight(int idx, int k) {
        inRange(idx);
        int newIndexOfElement = (idx + k) % list.size();
        int elementHolder = list.remove(idx);
        list.add(newIndexOfElement, elementHolder);
    }

    public void shiftLeft(int idx, int k) {
        inRange(idx);
        int newIndexOfElement = (idx - k) % list.size();
        if (newIndexOfElement < 0) {
            newIndexOfElement += list.size();
        }
        int elementHolder = list.remove(idx);
        list.add(newIndexOfElement, elementHolder);
    }

    IntegerList addValue(int value) {
        return new IntegerList(list.stream().mapToInt(i -> i + value).boxed().toArray(Integer[]::new));
    }

    void inRange(int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new IndexOutOfBoundsException();
        }
    }

}

public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}
