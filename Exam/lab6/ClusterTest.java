package NP.Exam.lab6;

import java.util.*;

/**
 * January 2016 Exam problem 2
 */
public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

// your code here
abstract class Element<T> {
    long id;

    abstract long getId();

    abstract double getDistance(T item);
}

class Cluster<T extends Element<T>> {
    Map<Long, T> elements;

    public Cluster() {
        elements = new HashMap<>();
    }

    public void addItem(T element) {
        elements.put(element.getId(), element);
    }

    public void near(long id, int top) {
        List<T> list = new ArrayList<>(elements.values());
        T el = elements.get(id);

        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                // TODO Auto-generated method stub
                return Double.compare(o1.getDistance(el), o2.getDistance(el));
            }
        });

        for (int i = 1; i <= top; i++) {
            System.out.printf("%d. %d -> %.3f\n", i, list.get(i).getId(), list.get(i).getDistance(el));
        }
    }
}

class Point2D extends Element<Point2D> {
    long id;
    float x, y;

    Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    long getId() {
        // TODO Auto-generated method stub
        return id;
    }

    @Override
    double getDistance(Point2D item) {
        // TODO Auto-generated method stub
        return Math.sqrt(Math.pow(x - item.x, 2) + Math.pow(y - item.y, 2));
    }

}
