package NP.MidTerm1.zad21;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(String message) {
        super(message);
    }
}

class Product{
    private int price;
    private char taxType;

    public Product(int price, char taxType) {
        this.price = price;
        this.taxType = taxType;
    }

    public double tax(){
        if (taxType == 'A')
            return price*0.18*0.15;
        else if (taxType == 'B')
            return price*0.05*0.15;
        else return 0;
    }

    public int getPrice() {
        return price;
    }

    public char getTaxType() {
        return taxType;
    }
}

class Receipt{
    private String id;
    private List<Product> products;

    public Receipt(String id, List<Product> products) throws AmountNotAllowedException {
        this.id = id;
        this.products = products;
        if (sum() > 30000)
            throw new AmountNotAllowedException("Receipt with amount "+sum()+" is not allowed to be scanned");
    }

    public int sum (){
        return products.stream().mapToInt(Product::getPrice).sum();
    }

    public double taxReturn(){
        return products.stream().mapToDouble(Product::tax).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f", id, sum(), taxReturn());
    }
}

class MojDDV{
    private List<Receipt> fiskalni;

    public MojDDV() {
        this.fiskalni = new ArrayList<>();
    }

    public void readRecords (InputStream inputStream){
        Scanner in = new Scanner(inputStream);

        while (in.hasNextLine()){
            String line = in.nextLine();
            String [] parts = line.split("\\s+");

            String id = parts[0];
            ArrayList<Product> products = new ArrayList<>();
            for (int i=1; i<parts.length; i+=2){
                int price = Integer.parseInt(parts[i]);
                char taxType = parts[i+1].charAt(0);
                products.add(new Product(price,taxType));
            }
            try {
                Receipt r = new Receipt(id, products);
                fiskalni.add(r);
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void printTaxReturns(OutputStream outputStream){
        fiskalni.forEach(System.out::println);
    }
}
public class MojDDVTest {

    public static void main(String[] args) throws AmountNotAllowedException {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        //System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        //mojDDV.printStatistics(System.out);

    }
}