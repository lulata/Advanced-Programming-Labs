package NP.lab1.ex3;

import java.util.*;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

class Account implements Cloneable {
    private String accName;
    private long idNum;
    private String balance;

    public Account(String user, String balance) {
        this.accName = user;
        this.balance = balance.substring(0, balance.length() - 1);
        this.idNum = new Random().nextLong();
        while (this.idNum < 0) {
            this.idNum = new Random().nextLong();
        }
    }

    public String getAccName() {
        return accName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public long getId() {
        return idNum;
    }





    @Override
    public String toString() {
        DecimalFormat pattern00dot00 = new DecimalFormat("0.00");
        return "Name: " + accName + "\n" + "Balance: " + pattern00dot00.format(Double.parseDouble(balance)) + "$\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return idNum == account.idNum;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNum);
    }
}

abstract class Transaction {
    final private long fromId;
    final private long toId;
    final private String description;
    final private String amount;

    public Transaction(long fromId, long toId, String amount, String description) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount.substring(0, amount.length() - 1);
        this.description = description;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        DecimalFormat pattern00dot00 = new DecimalFormat("0.00");
        return pattern00dot00.format(Double.parseDouble(amount)) + "$";
    }

    public String getDescription() {
        return description;
    }

    public abstract double getProvision();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return fromId == that.fromId && toId == that.toId && Objects.equals(description, that.description) && amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, description, amount);
    }
}

class FlatAmountProvisionTransaction extends Transaction {
    private final String flatAmount;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatAmount) {
        super(fromId, toId, amount, "FlatAmount");
        this.flatAmount = flatAmount.substring(0, flatAmount.length() - 1);
    }

    public double getFlatAmount() {
        return Double.parseDouble(flatAmount);
    }

    @Override
    public double getProvision() {

        return Double.parseDouble(flatAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return flatAmount.equals(that.flatAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatAmount);
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    private final int flatPercent;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int flatPercent) {
        super(fromId, toId, amount, "FlatPercent");
        this.flatPercent = flatPercent;
    }

    public int getFlatPercent() {
        return flatPercent;
    }

    @Override
    public double getProvision() {
        String temp = getAmount().replaceAll("\\$","");
        double amount =  Double.parseDouble(temp);
        double finalAmount= (int)amount*flatPercent;
        return  finalAmount/(100.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return flatPercent == that.flatPercent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flatPercent);
    }
}

class Bank {
    private String name;
    private Account[] accounts;
    private double totalTransfers;
    private double totalProvision;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = Arrays.copyOf(accounts, accounts.length);
        this.totalTransfers = 0.0;
        this.totalProvision = 0.0;
    }

    private Account findUser(long fromId) {
        for (Account account : accounts) {
            if (account.getId() == fromId) {
                return account;
            }
        }
        return null;
    }

    boolean makeTransaction(Transaction t) {
        Account accFrom = findUser(t.getFromId());
        Account accTo = findUser(t.getToId());
        if (accFrom == null || accTo == null){
            return false;
        }
        double accFromBalance = Double.parseDouble(accFrom.getBalance());
        double transactionValue = Double.parseDouble(t.getAmount().substring(0, t.getAmount().length() - 1));
        double provision = t.getProvision();
        if (accFromBalance >= transactionValue + provision) {
            double newBalanceFrom =   Double.parseDouble(accFrom.getBalance().replace("$","")) - t.getProvision() - Double.parseDouble(t.getAmount().replace("$",""));
            double newBalanceTo =   Double.parseDouble(accTo.getBalance().replace("$","")) + Double.parseDouble(t.getAmount().replace("$",""));
            if (accFrom.equals(accTo)){
                accFrom.setBalance(Double.toString(Double.parseDouble(accFrom.getBalance().replace("$","")) - t.getProvision()));
            }else{
                accFrom.setBalance(Double.toString(newBalanceFrom));
                accTo.setBalance(Double.toString(newBalanceTo));
            }
            totalProvision += provision;
            totalTransfers += transactionValue;
            return true;
        }
        return false;
    }


    public String totalTransfers() {
        DecimalFormat pattern00dot00 = new DecimalFormat("0.00");
        return pattern00dot00.format(totalTransfers) + "$";
    }

    public String totalProvision() {
        DecimalFormat pattern00dot00 = new DecimalFormat("0.00");
        return pattern00dot00.format(totalProvision) + "$";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account[] getAccounts() {
        Account[] temp = new Account[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            temp[i] = (Account) accounts[i].clone();
        }
        return temp;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }


    public void setTotalTransfers(int totalTransfers) {
        this.totalTransfers = totalTransfers;
    }

    public void setTotalProvision(double totalProvision) {
        this.totalProvision = totalProvision;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder("Name: " + name + "\n\n");
        for (Account account : accounts) {
            temp.append(account.toString());
        }
        return temp.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return totalTransfers == bank.totalTransfers && Double.compare(bank.totalProvision, totalProvision) == 0 && Objects.equals(name, bank.name) && Arrays.equals(accounts, bank.accounts);
    }
}


public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1) && !a1.equals(a2) && !a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
