package NP.lab3.ex1;

import java.util.Scanner;

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
        super("Invalid extra type");
    }
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
        super("Invalid pizza type");
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(Item item) {
        super(item + " out of stock!");
    }
}

class OrderLockedException extends Exception {
    public OrderLockedException() {
        super("Order is locked!");
    }
}

class EmptyOrder extends Exception {
    public EmptyOrder() {
        super("Order is empty!");
    }
}

enum PizzaType {
    Standard(10),
    Pepperoni(12),
    Vegetarian(8);

    private int price;

    PizzaType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

enum Extras {
    Ketchup(3),
    Coke(5);

    private int price;

    Extras(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

interface Item {
    int getPrice();
    String getType();
}

class PizzaItem implements Item {
    private String type;

    PizzaItem(String type) throws InvalidPizzaTypeException {
        if (type.equals("Standard") || type.equals("Pepperoni") || type.equals("Vegetarian")) {
            this.type = type;
        } else {
            throw new InvalidPizzaTypeException();
        }
    }

    @Override
    public int getPrice() {
        return PizzaType.valueOf(type).getPrice();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}

class ExtraItem implements Item {
    private String  type;

    ExtraItem(String type) throws InvalidExtraTypeException {
        if (type.equals("Coke") || type.equals("Ketchup")) {
            this.type = type;
        } else {
            throw new InvalidExtraTypeException();
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getPrice() {
        return Extras.valueOf(type).getPrice();
    }

    @Override
    public String toString() {
        return type;
    }
}

class Order {
    Item[] items;
    int[] counter;
    boolean locked;

    Order() {
        items = new Item[0];
        counter = new int[0];
        locked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (count > 10) {
            throw new ItemOutOfStockException(item);
        } else if (locked) {
            throw new OrderLockedException();
        }
        if (sameItem(item) == -1) {
            Item[] newItems = new Item[items.length + 1];
            int[] newCount = new int[counter.length + 1];
            for (int i = 0; i < items.length; i++) {
                newItems[i] = items[i];
                newCount[i] = counter[i];
            }
            newItems[newItems.length-1] = item;
            newCount[newCount.length-1] = count;
            items = newItems;
            counter = newCount;
        } else {
            counter[sameItem(item)] = count;
        }
    }

    public int sameItem(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].getType().equals(item.getType())) {
                return i;
            }
        }
        return -1;
    }

    public int getPrice() {
        int price = 0;
        for (int i = 0; i < items.length; i++) {
            price += items[i].getPrice() * counter[i];
        }
        return price;
    }

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            sb.append(String.format("%3d.%-15sx%2d%5d$\n", i + 1, items[i].toString(), counter[i], items[i].getPrice() * counter[i]));
        }
        sb.append(String.format("%-22s%5d$", "Total:", getPrice()));
        System.out.println(sb);
    }

    public void removeItem(int idx) throws ArrayIndexOutOfBoundsException, OrderLockedException {
        if (locked) {
            throw new OrderLockedException();
        }
        if (idx < 0 || idx >= counter.length) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            Item[] newItems = new Item[items.length - 1];
            int[] newCount = new int[counter.length - 1];
            for (int i = 0; i < idx; i++) {
                newItems[i] = items[i];
                newCount[i] = counter[i];
            }
            for (int i = idx; i < items.length - 1; i++) {
                newItems[i] = items[i + 1];
                newCount[i] = counter[i + 1];
            }
            items = newItems;
            counter = newCount;
        }
    }

    public void lock() throws EmptyOrder {
        if (items.length != 0)
            locked = true;
        else throw new EmptyOrder();
    }

}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
