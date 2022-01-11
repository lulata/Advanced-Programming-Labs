package NP.lab2.ex2;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

enum Operator {VIP, ONE, TMOBILE}

abstract class Contact {
    private final int day;
    private final int month;
    private final int year;
    private final String date;

    public Contact(String date) {
        this.date = date;
        String[] splitDates = date.split("-");
        year = Integer.parseInt(splitDates[0]);
        month = Integer.parseInt(splitDates[1]);
        day = Integer.parseInt(splitDates[2]);
    }

    public String getDate() {
        return date;
    }

    public boolean isNewerThan(Contact c) {
        if (this.year > c.year) {
            return true;
        } else if (this.year == c.year) {
            if (this.month > c.month) {
                return true;
            } else if (this.month == c.month) {
                return this.day > c.day;
            }
        }
        return false;
    }

    public abstract String getType();

    @Override
    public abstract String toString();
}

class EmailContact extends Contact {
    private final String email;

    public EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String toString() {
        return "\"" + email + "\"";
    }
}

class PhoneContact extends Contact {
    private final String phone;
    private Operator operator;

    public PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
        setOperator();
    }

    private void setOperator() {
        char num = phone.charAt(2);
        switch (num) {
            case '0':
            case '1':
            case '2':
                operator = Operator.TMOBILE;
                break;
            case '5':
            case '6':
                operator = Operator.ONE;
                break;
            case '7':
            case '8':
                operator = Operator.VIP;
        }
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return "\"" + phone + "\"";
    }
}

class Student {
    private final String firstName;
    private final String lastName;
    private final String city;
    private final int age;
    private final long index;

    private Contact[] contacts;
    private int contactSize;

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contactSize = 0;
    }

    public void addEmailContact(String date, String email) {
        if (contactSize == 0) {
            contacts = new Contact[1];
            contacts[contactSize++] = new EmailContact(date, email);
        } else {
            Contact[] temp = new Contact[contactSize + 1];
            Contact newEmail = new EmailContact(date, email);
            for (int i = 0; i < contactSize; i++) {
                temp[i] = contacts[i];
            }temp[contactSize++] = newEmail;
            contacts = temp;
        }
    }

    public void addPhoneContact(String date, String phone) {
        if (contactSize == 0) {
            contacts = new Contact[1];
            contacts[contactSize++] = new PhoneContact(date, phone);
        } else {
            Contact[] temp = new Contact[contactSize + 1];
            Contact newPhone = new PhoneContact(date, phone);
            for (int i = 0; i < contactSize; i++) {
                temp[i] = contacts[i];
            }temp[contactSize++] = newPhone;
            contacts = temp;
        }
    }

    public Contact[] getEmailContacts() {
        Contact[] temp = new Contact[contactSize];
        int j = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Email")) {
                temp[j] = c;
                j++;
            }
        }
        Contact[] email = new Contact[j];
        for (int i = 0; i < j; i++) {
            email[i] = temp[i];
        }
        return email;
    }

    public Contact[] getPhoneContacts() {
        Contact[] temp = new Contact[contactSize];
        int j = 0;

        for (Contact c : contacts) {
            if (c.getType().equals("Phone")) {
                temp[j] = c;
                j++;
            }
        }
        Contact[] phone = new Contact[j];
        for (int i = 0; i < j; i++) {
            phone[i] = temp[i];
        }
        return phone;
    }

    public String getCity() {
        return city;
    }

    public long getIndex() {
        return index;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getContactsSize() {
        return contactSize;
    }

    public Contact getLatestContact() {
        Contact latest = contacts[0];
        for (Contact c : contacts) {
            if (c.isNewerThan(latest)) {
                latest = c;
            }
        }
        return latest;
    }

    @Override
    public String toString() {
        return "{" +
                "\"ime\":\"" + firstName + "\", " + "\"prezime\":\"" + lastName + "\", " + "\"vozrast\":" + age + ", " +
                "\"grad\":\"" + city + "\", " + "\"indeks\":" + index + ", " +
                "\"telefonskiKontakti\":" + Arrays.toString(getPhoneContacts()) + ", " +
                "\"emailKontakti\":" + Arrays.toString(getEmailContacts()) + '}';
    }
}

class Faculty {
    private final String name;
    private final Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName) {
        int count = 0;
        for (Student s : students) {
            if (s.getCity().equals(cityName))
                count++;
        }
        return count;
    }


    public Student getStudent(long index) {
        Student student = null;
        for (Student s : students) {
            if (s.getIndex() == index)
                student = s;
        }
        return student;
    }

    public double getAverageNumberOfContacts() {
        double sum = 0;
        for (Student s : students) {
            sum += s.getContactsSize();
        }
        return sum / (double) students.length;
    }

    public Student getStudentWithMostContacts() {
        Student max = students[0];
        for (Student s : students) {
            if (s.getContactsSize() > max.getContactsSize()) {
                max = s;
            } else if (s.getContactsSize() == max.getContactsSize()) {
                if (s.getIndex() > max.getIndex()) {
                    max = s;
                }
            }
        }
        return max;
    }

    @Override
    public String toString() {
        return "{" + "\"fakultet\":\"" + name + "\", " +
                "\"studenti\":" + Arrays.toString(students) + "}";
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
