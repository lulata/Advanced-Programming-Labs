package NP.MidTerm1.zad7;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde

class Category {
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Category category = (Category) other;
        return this.categoryName.equals(category.categoryName);
    }

}

class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(String message) {
        super("Category " + message + " was not found");
    }
}



class NewsItem {
    protected String title;
    protected Date date;
    protected LocalDateTime localDate;
    protected Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.localDate = convertToLocalDateTimeViaInstant(date);
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getLocalDate() {
        return localDate;
    }

    protected String getTeaser(){
        long minutes = localDate.until(LocalDateTime.now(), ChronoUnit.MINUTES);
        return String.format("%s\n%d", title, minutes);
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
class TextNewsItem extends NewsItem{
    private String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    protected String getTeaser() {
        return String.format("%s\n%s", super.getTeaser(), text.length() <= 80 ? text : text.substring(0, 80));
    }

}
class MediaNewsItem extends NewsItem {
    private String url;
    private int numReviews;

    public MediaNewsItem(String title, Date date, Category category, String url, int numReviews) {
        super(title, date, category);
        this.url = url;
        this.numReviews = numReviews;
    }

    public String getUrl() {
        return url;
    }

    public int getNumReviews() {
        return numReviews;
    }

    @Override
    protected String getTeaser() {
        return String.format("%s\n%s\n%d", super.getTeaser(), url, numReviews);
    }

}

class FrontPage {
    private List<NewsItem> items;
    private Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        items = new LinkedList<>();
    }

    void addNewsItem(NewsItem newsItem) {
        items.add(newsItem);
    }

    List<NewsItem> listByCategory(Category category) {
        return items.stream()
                .filter(i -> i.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException{
        Category temp = new Category(category);
        if (!Arrays.asList(categories).contains(temp))
            throw new CategoryNotFoundException(category);

        return listByCategory(temp);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (NewsItem item : items) {
            sb.append(item.getTeaser()).append("\n");
        }
        return sb.toString();
    }

}