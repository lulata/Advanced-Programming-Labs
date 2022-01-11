package NP.lab5.ex5;

import java.util.Scanner;
import java.util.LinkedList;

class ResizableArray<T> {
    private int size;
    private int capacity;
    private T[] array;

    public ResizableArray() {
        size = 0;
        capacity = 10;
        array = (T[]) new Object[capacity];
    }

    void addElement(T element) {
        if (size == capacity) {
            capacity *= 2;
            T[] newArray = (T[]) new Object[capacity];
            copy(newArray, array);
            array = newArray;
        }
        array[size++] = element;
    }

    boolean removeElement(T element) {
        int index = findElement(element);
        if(index==-1){
            return false;
        }
        for(int i=index;i<size-1;i++){
            array[i]=array[i+1];
        }
        --size;
        return true;
    }

    boolean contains(T element) {
        return findElement(element) != -1;
    }

    Object[] toArray() {
        T[] result = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = array[i];
        }
        return result;
    }

    boolean isEmpty() {
        return size == 0;
    }

    int count() {
        return size;
    }

    T elementAt(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    static <T> void copyAll(ResizableArray<? super T> dest, ResizableArray<? extends T> src) {
        for (int i = 0, len=src.count(); i < len; i++) {
            dest.addElement(src.elementAt(i));
        }
    }

    void copy(T[] a,T[] b){
        for(int i=0;i<b.length;i++){
            a[i]=b[i];
        }
    }

    int findElement(T element){
        for(int i=0;i<size;i++){
            if(array[i].equals(element)){
                return i;
            }
        }
        return -1;
    }
}

class IntegerArray extends ResizableArray<Integer> {
    public IntegerArray() {
        super();
    }

    double sum() {
        double sum = 0;
        for (int i = 0; i < count(); i++) {
            sum += elementAt(i);
        }
        return sum;
    }


    double mean() {
        return sum() / count();
    }

    int countNonZero() {
        int count = 0;
        for (int i = 0; i < count(); i++) {
            if (elementAt(i) != 0) {
                ++count;
            }
        }
        return count;
    }

    IntegerArray distinct() {
        IntegerArray result = new IntegerArray();
        for (int i = 0; i < this.count(); i++) {
            if (!(result.contains(elementAt(i)))) {
                result.addElement(elementAt(i));
            }
        }
        return result;
    }

    IntegerArray increment(int offset) {
        IntegerArray result = new IntegerArray();
        for (int i = 0; i < count(); i++) {
            result.addElement(elementAt(i) + offset);
        }
        return result;
    }


}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if (test == 0) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while (jin.hasNextInt()) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if (test == 1) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for (int i = 0; i < 4; ++i) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if (test == 2) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while (jin.hasNextInt()) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if (a.sum() > 100)
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if (test == 3) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for (int w = 0; w < 500; ++w) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k = 2000;
                int t = 1000;
                for (int i = 0; i < k; ++i) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for (int i = 0; i < t; ++i) {
                    a.removeElement(k - i - 1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}

