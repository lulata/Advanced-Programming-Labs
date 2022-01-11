package NP.lab3.ex2;

import java.util.*;

class InvalidFormatException extends Exception {
    public InvalidFormatException() {
        super("Invalid format!");
    }
}

class InvalidNameException extends Exception {
    public String name;

    public InvalidNameException(String name) {
        super(name);
        this.name = name;
    }
}

class InvalidNumberException extends Exception {
    public InvalidNumberException() {
        super("Invalid number!");
    }
}

class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
        super("Maximum size exceeded!");
    }
}


class Contact implements Comparable<Contact> {
    String name;
    ArrayList<String> phoneNumbers;

    Contact(String name, String... phoneNumber) throws InvalidNameException, MaximumSizeExceddedException, InvalidNumberException {
        if (!nameCheck(name)) {
            throw new InvalidNameException(name);
        } else {
            this.name = name;
        }
        if (phoneNumber.length > 5) {
            throw new MaximumSizeExceddedException();
        }
        phoneNumbers = new ArrayList<String>();
        for (String s : phoneNumber) {
            if (!numberCheck(s)) {
                throw new InvalidNumberException();
            }phoneNumbers.add(s);
        }
    }

    static boolean nameCheck(String name) {
        if (name.length() < 4 || name.length() > 10) {
            return false;
        }
        char[] chars = name.toCharArray();
        for (char aChar : chars) {
            if (!Character.isAlphabetic(aChar)) {
                return false;
            }
        }
        return true;
    }

    static boolean numberCheck(String number) {
        if (number.length() != 9) return false;
        for (int i = 0; i < number.length(); ++i) {
            if (!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        return number.startsWith("070") || number.startsWith("071") || number.startsWith("072") || number.startsWith("075")
                || number.startsWith("076") || number.startsWith("077") || number.startsWith("078");
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        String[] numbers = new String[phoneNumbers.size()];
        for (int i = 0; i < phoneNumbers.size(); ++i) {
            numbers[i] = phoneNumbers.get(i);
        }
        Arrays.sort(numbers);
        return numbers;
    }



    public void addNumber(String number) throws InvalidNumberException, MaximumSizeExceddedException {
        if (!numberCheck(number)){
            throw new InvalidNumberException();
        }if (phoneNumbers.size() > 5){
            throw new MaximumSizeExceddedException();
        }else{
            phoneNumbers.add(number);
        }
    }

    @Override
    public String toString() {
        String[] numbers = getNumbers();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s\n%d\n", name, numbers.length));
        for (String number : numbers) {
            sb.append(number).append("\n");
        }
        return sb.toString();
    }

    public Contact valueOf(String s) throws InvalidFormatException, InvalidNameException, MaximumSizeExceddedException, InvalidNumberException {
        String[] info = s.split(" ");
        if (!nameCheck(info[0] + " " + info[1])){
            throw new InvalidFormatException();
        }
        String name = info[0] + " " + info[1];
        String[] numbers = new String[info.length - 2];
        for (int i = 2; i < info.length; ++i) {
            if (!numberCheck(info[i])){
                throw new InvalidFormatException();
            }else{
                numbers[i - 2] = info[i];
            }
        }
        return new Contact(name, numbers);
    }

    @Override
    public int compareTo(Contact c) {
        return this.getName().compareTo(c.getName());
    }
}

class PhoneBook {
    ArrayList<Contact> contacts;

    PhoneBook() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) throws MaximumSizeExceddedException, InvalidNameException {
        if (contacts.size() > 250){
            throw new MaximumSizeExceddedException();
        }
        for (Contact c : contacts) {
            if (c.getName().equals(contact.getName())){
                throw new InvalidNameException(contact.getName());
            }
        }contacts.add(contact);
    }

    public Contact getContactForName(String name) {
        for (Contact c : contacts) {
            if (c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }

    public int numberOfContacts() {
        return contacts.size();
    }

    public Contact[] getContacts() {
        Contact[] temp = new Contact[numberOfContacts()];
        for (int i = 0; i < contacts.size(); ++i) {
            temp[i] = contacts.get(i);
        }
        Arrays.sort(temp);
        return temp;
    }

    public void removeContact(String name) {
        for(int i = 0; i < contacts.size(); ++i) {
            if(contacts.get(i).getName().equals(name)) {
                contacts.remove(i);
                return;
            }
        }
    }

    @Override
    public String toString() {
        Collections.sort(contacts);
        StringBuilder sb = new StringBuilder();
        for (Contact c : contacts) {
            sb.append(c.toString()).append("\n");
        }
        return sb.toString();
    }

    public Contact[] getContactsForNumber(String number_prefix) {
        Contact[] cons = new Contact[contacts.size()];
        int i = 0;
        for (Contact c : contacts) {
            for (String number : c.getNumbers()) {
                if (number.startsWith(number_prefix))
                    cons[i++] = c;
            }
        }
        return Arrays.copyOf(cons, i);
    }

    public static void saveAsTextFile(PhoneBook phonebook, String path) {
    }

    public static PhoneBook loadFromTextFile(String path) {
        return null;
    }
}

public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch (line) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine())
            phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook, text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if (!pb.equals(phonebook)) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while (jin.hasNextLine()) {
            String command = jin.nextLine();
            switch (command) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(), jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while (jin.hasNextLine()) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        } catch (InvalidNameException e) {
            System.out.println(e.name);
            exception_thrown = true;
        } catch (Exception e) {
        }
        if (!exception_thrown) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = {"And\nrej", "asd", "AAAAAAAAAAAAAAAAAAAAAA", "Ð�Ð½Ð´Ñ€ÐµÑ˜A123213", "Andrej#", "Andrej<3"};
        for (String name : names_to_test) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if (!exception_thrown) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = {"+071718028", "number", "078asdasdasd", "070asdqwe", "070a56798", "07045678a", "123456789", "074456798", "073456798", "079456798"};
        for (String number : numbers_to_test) {
            try {
                new Contact("Andrej", number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if (!exception_thrown)
                System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for (int i = 0; i < nums.length; ++i) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej", nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if (!exception_thrown)
            System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej", getRandomLegitNumber(rnd), getRandomLegitNumber(rnd), getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070", "071", "072", "075", "076", "077", "078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for (int i = 3; i < 9; ++i)
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}

