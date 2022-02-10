package NP.MidTerm2.lab3;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class Event {
    private String name;
    private String location;
    private LocalDateTime date;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, YYY HH:mm", Locale.US);

    public Event(String name, String location, LocalDateTime date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%s at %s, %s", date.format(formatter), location, name);
    }
}

class EventCalendar {
    private int year;
    Map<LocalDate, TreeSet<Event>> eventsByDate;
    private static final Comparator<Event> eventComparator = Comparator.comparing(Event::getDate).thenComparing(Event::getName);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");

    public EventCalendar(int year) {
        this.year = year;
        this.eventsByDate = new HashMap<>();
    }

    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        if (date.getYear() != year) {
            ZonedDateTime zonedDateTime = ZonedDateTime.of(date, ZoneId.of("UTC"));
            throw new WrongDateException(zonedDateTime.format(formatter));
        }
        Event newEvent = new Event(name, location, date);
        eventsByDate.computeIfAbsent(date.toLocalDate(), key -> new TreeSet<>(eventComparator)).add(newEvent);
    }

    public void listEvents(LocalDateTime date) {
        if (!eventsByDate.containsKey(date.toLocalDate())) {
            System.out.println("No events on this day!");
            return;
        }
        System.out.println(eventsByDate.get(date.toLocalDate()).stream()
                .map(Event::toString)
                .collect(Collectors.joining("\n")));
    }


    public void listByMonth() {
        Map<Integer, Integer> eventsByMonths = eventsByDate.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getMonth().getValue(), entry -> entry.getValue().size(), Integer::sum));
        fillEmptyFields(eventsByMonths);
        printEventsByMonths(eventsByMonths);
    }

    private void printEventsByMonths(Map<Integer, Integer> eventsByMonths) {
        System.out.print(eventsByMonths.entrySet().stream()
                .map(e -> String.format("%d : %d", e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n")));
    }

    private void fillEmptyFields(Map<Integer, Integer> eventsByMonths) {
        for (int i = 1; i <= 12; i++) {
            eventsByMonths.putIfAbsent(i, 0);
        }
    }
}

class WrongDateException extends Exception {
    public WrongDateException(String date) {
        super("Wrong date: " + date);
    }
}


public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime date = LocalDateTime.parse(parts[2], dtf);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        LocalDateTime date = LocalDateTime.parse(scanner.nextLine(), dtf);
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde
