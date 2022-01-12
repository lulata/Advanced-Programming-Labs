package NP.MidTerm1.zad10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде

class Book{
    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price){
        this.title = title;
        this.category = category;
        this.price = price;
    }

    String getCategory(){
        return category;
    }

    float getPrice(){
        return price;
    }

    String getTitle(){
        return title;
    }

    public String toString() {
        return String.format("%s (%s) %.2f",title,category,price);
    }
}

class BookCollection{
    private List<Book> books;

    public BookCollection(){
        books = new ArrayList<Book>();
    }

    public void addBook(Book book){
        books.add(book);
    }

    public List<Book> getCheapestN(int n){
        Comparator<Book>comparator=Comparator.comparing((Book book)->book.getPrice())
                .thenComparing(book -> book.getTitle());
        return
                books.stream().sorted(comparator).limit(n).collect(Collectors.toList());
    }

    public void printByCategory(String category)
    {
        Comparator<Book> comparator=Comparator.comparing((Book book)->book.getTitle())
                .thenComparing((Book book)->book.getPrice());

        books.stream()
                .filter(x->x.getCategory().equals(category))
                .sorted(comparator).forEach(x-> System.out.println(x.toString()));
    }
}