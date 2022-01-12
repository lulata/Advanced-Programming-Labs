package NP.MidTerm1.zad22;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", amount));
    }
}

enum TaxType{
    A,B,V
}

class Item{
    private int price;
    private TaxType taxType;

    public Item(int price, TaxType taxType) {
        this.price = price;
        this.taxType = taxType;
    }

    public Item(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public double calculatedTax(){
        if(taxType.equals(TaxType.A))
            return price * 0.18 * 0.15;
        else if(taxType.equals(TaxType.B))
            return price * 0.05 * 0.15;
        else return 0;
    }

}

class Receipt implements Comparable<Receipt>{
    private long id;
    List<Item> items;

    Receipt(long id){
        this.id = id;
        items = new ArrayList<>();
    }

    public Receipt(long id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static int totalAmount(List<Item> items){
        return items.stream().mapToInt(x -> x.getPrice()).sum();
    }

    public int sumAmount(){
        return items.stream().mapToInt(x -> x.getPrice()).sum();
    }

    public double taxReturns(){
        return items.stream().mapToDouble(x -> x.calculatedTax()).sum();
    }

    public static Receipt createReceipts(String line) throws AmountNotAllowedException {
        String [] parts = line.split("\\s+");
        long id = Long.parseLong(parts[0]);
        List<Item> items = new ArrayList<>();

        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if(Character.isDigit(i.charAt(0))){
                        items.add(new Item(Integer.parseInt(i)));
                    } else {
                        items.get(items.size()-1).setTaxType(TaxType.valueOf(i));
                    }
                });

        if(totalAmount(items) > 30000){
            throw new AmountNotAllowedException(totalAmount(items));
        }
        return new Receipt(id, items);
    }

    @Override
    public int compareTo(Receipt receipt) {
        return Comparator.comparing(Receipt::taxReturns).thenComparing(Receipt::sumAmount)
                .compare(this, receipt);
    }

    @Override
    public String toString() {
        return String.format("%10d\t%10d\t%10.5f", id, sumAmount(), taxReturns());
    }
}

class MojDDV{
    private List<Receipt> receipts;

    public MojDDV(){
        receipts = new ArrayList<>();
    }

    void readRecords (InputStream inputStream){
        receipts = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .map(line -> {
                    try {
                        return Receipt.createReceipts(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());;
                        return null;
                    }
                })
                .collect(Collectors.toList());
        receipts = receipts.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    void printTaxReturns (OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        receipts.stream().forEach(i -> printWriter.println(i));
        printWriter.flush();
    }

    public void printStatistics(PrintStream out) {
        //min: MIN max: MAX sum: SUM count: COUNT average: AVERAGE
        PrintWriter printWriter = new PrintWriter(out);
        DoubleSummaryStatistics summaryStatistics = new DoubleSummaryStatistics();
        summaryStatistics = receipts.stream()
                .mapToDouble(Receipt::taxReturns).summaryStatistics();

        printWriter.println(String.format("min:\t%5.3f\nmax:\t%5.3f\nsum:\t%5.3f\ncount:\t%-5d\navg:\t%5.3f",
                summaryStatistics.getMin(), summaryStatistics.getMax(), summaryStatistics.getSum(),
                summaryStatistics.getCount(), summaryStatistics.getAverage()));
        printWriter.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}