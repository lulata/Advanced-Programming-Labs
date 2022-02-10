package NP.MidTerm2.lab9;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Flight {

    private Airport from;
    private Airport to;
    private LocalTime departTime;
    private LocalTime arriveTime;
    private Duration duration;

    public Flight(Airport from, Airport to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.departTime = LocalTime.of(0, 0).plusMinutes(time);
        this.arriveTime = departTime.plusMinutes(duration);
        this.duration = Duration.ofMinutes(duration);
    }

    private String getTimes() {
        StringBuilder result = new StringBuilder(String.format("%s-%s", departTime, arriveTime));
        if (departTime.isAfter(arriveTime)) {
            result.append(" +1d");
        }
        return result.toString();
    }

    private String getDuration() {
        int hours = (int) duration.toHours();
        int minutes = (int) duration.minusHours(hours).toMinutes();
        return String.format("%dh%02dm", hours, minutes);
    }

    @Override
    public String toString() {
        return String.format("%s-%s %s %s", from.getCode(), to.getCode(), getTimes(), getDuration());
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public LocalTime getDepartTime() {
        return departTime;
    }

    public LocalTime getArriveTime() {
        return arriveTime;
    }
}

class Airport {
    private String name;
    private String country;
    private String code;
    private int passengers;
    private TreeSet<Flight> flightsFrom;
    private TreeSet<Flight> flightsTo;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        Comparator<Flight> comparator1 = Comparator.comparing(f -> f.getTo().getCode());
        Comparator<Flight> comparator2 = Comparator.comparing(f -> f.getFrom().getCode());
        flightsFrom = new TreeSet<Flight>(comparator1.thenComparing(Flight::getDepartTime));
        flightsTo = new TreeSet<Flight>(Comparator.comparing(Flight::getDepartTime).thenComparing(comparator2));
    }

    public void addFlightFrom(Flight f) {
        flightsFrom.add(f);
    }

    public void addFlightTo(Flight f) {
        flightsTo.add(f);
    }

    public void printFlightsFrom() {
        List<Flight> flightsToPrint = new LinkedList<>(flightsFrom);
        IntStream.range(0, flightsFrom.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, flightsToPrint.get(i).toString()))
                .forEach(System.out::println);
    }

    public void printFlightsTo() {
        System.out.println(flightsTo.stream()
                .map(Flight::toString)
                .collect(Collectors.joining("\n")));
    }

    public TreeSet<Flight> getFlightsFrom() {
        return flightsFrom;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }

    public TreeSet<Flight> getFlightsTo() {
        return flightsTo;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }
}

class Airports {
    Map<String, Airport> airports;

    public Airports() {
        this.airports = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.putIfAbsent(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Airport fromAirport = airports.get(from);
        Airport toAirport = airports.get(to);
        Flight newFlight = new Flight(fromAirport, toAirport, time, duration);
        fromAirport.addFlightFrom(newFlight);
        toAirport.addFlightTo(newFlight);
    }

    public void showFlightsFromAirport(String code) {
        Airport airport = airports.get(code);
        System.out.println(airport);
        airport.printFlightsFrom();
//        airport.printFlightsTo();
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport airportFrom = airports.get(from);
        List<String> flightsFromTo = airportFrom.getFlightsFrom()
                .stream()
                .filter(f -> f.getTo().getCode().equals(to))
                .map(Flight::toString)
                .collect(Collectors.toList());
        if (flightsFromTo.size() == 0) {
            System.out.println(String.format("No flights from %s to %s", from, to));
        } else {
            System.out.println(String.join("\n", flightsFromTo));
        }
    }

    public void showDirectFlightsTo(String to) {
        Airport airportTo = airports.get(to);
        airportTo.printFlightsTo();
    }
}


public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde


