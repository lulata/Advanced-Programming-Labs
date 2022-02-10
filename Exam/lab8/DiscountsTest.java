package NP.Exam.lab8;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


class Product {
    int price;
    int discountedPrice;

    public Product(int discountedPrice, int price) {
        this.discountedPrice = discountedPrice;
        this.price = price;
    }

    public static Product ofString(String product) {
        String[] parts = product.split(":");
        return new Product(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public float discount() {
        return (float) (1 - (discountedPrice * 1.00 / price));
    }

    public int absoluteDiscount() {
        return price - discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", discount(), discountedPrice, price);
    }
}

class Store {
    String name;
    ArrayList<Product> products;

    public Store(String name, ArrayList<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public Integer numProducts() {
        return products.size();
    }

    public double[] getDiscounts() {
        double dis[] = new double[products.size()];
        int i = 0;
        for (Product m : products) {
            dis[i++] = m.discount();
        }
        return dis;
    }

    public double getAvgDis() {
        double sum = 0;

        for (double d : this.getDiscounts())
            sum += d;
        return sum * 100 / this.numProducts();
    }

    public int totalDiscount() {
        int sum = 0;
        for (Product i : products) {
            sum += i.absoluteDiscount();
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        sb.append("\n");

        sb.append(String.format("Average discount: %.1f%%\n", this.getAvgDis() - 1));
        sb.append(String.format("Total discount: %d\n", this.totalDiscount()));

        products.stream().sorted(Comparator.comparing(Product::discount).reversed())
                .forEach(i -> {
                    sb.append(String.format("%2.0f%% %d/%d\n", i.discount() * 100, i.discountedPrice, i.price));
                });
        ;
        sb.deleteCharAt(sb.toString().length() - 1);
        return sb.toString();
    }

}

class Discounts {
    ArrayList<Store> stores;

    Discounts() {
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream input) {
        Scanner in = new Scanner(input);
        int count = 0;
        while (in.hasNextLine()) {
            String[] line = in.nextLine().split("\\s+");
            String name = line[0];
            ArrayList<Product> products = new ArrayList<>();
            for (int i = 1; i < line.length; i++) {
                String[] split = line[i].split(":");
                Product p = new Product(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                products.add(p);
            }

            stores.add(new Store(name, products));
            count++;
        }
        return count;


    }

    public List<Store> byAverageDiscount() {
        return stores.stream().sorted(Comparator.comparing(Store::getAvgDis).reversed()
                .thenComparing(Store::getName)).limit(3).collect(Collectors.toList());

    }

    public List<Store> byTotalDiscount() {
        return stores.stream().sorted(Comparator.comparing(Store::totalDiscount).reversed()
                .thenComparing(Store::getName)).limit(3).collect(Collectors.toList());
    }
}


public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}
