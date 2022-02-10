package NP.MidTerm2.lab2;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            LocalDateTime date = LocalDateTime.parse(line, timeFormatter);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        LocalDateTime from = LocalDateTime.parse(line, timeFormatter);
        line = scanner.nextLine();
        LocalDateTime to = LocalDateTime.parse(line, timeFormatter);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

class WeatherStation {
    TreeSet<Measurement> measurments;

    int days;

    WeatherStation(int days) {
        this.days = days;
        measurments = new TreeSet<>(Comparator.comparing(Measurement::getDate));
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, LocalDateTime date) {
        if (!measurments.isEmpty() && !closeIntervalDates(date)) {
            measurments.add(new Measurement(temperature, wind, humidity, visibility, date));
            adjustCollection(date);
        } else if (measurments.isEmpty()) {
            measurments.add(new Measurement(temperature, wind, humidity, visibility, date));
        }
    }

    private void adjustCollection(LocalDateTime newDate) {
        while (measurments.first().getDate().until(newDate, ChronoUnit.DAYS) >= this.days) {
            measurments.pollFirst();
        }
    }

    public int total() {
        return measurments.size();
    }

    public void status(LocalDateTime from, LocalDateTime to) throws RuntimeException {
        List<Measurement> selectedMeasures = measurments.stream()
                .filter(m -> (m.getDate().isAfter(from) || m.getDate().isEqual(from)) &&
                        (m.getDate().isBefore(to) || m.getDate().isEqual(to)))
                .collect(Collectors.toList());


        double average = getAverageTemperature(selectedMeasures);
        selectedMeasures.forEach(System.out::println);
        System.out.printf("Average temperature: %.2f", average);
    }

    public double getAverageTemperature(List<Measurement> selectedMeasures) throws RuntimeException {
        return selectedMeasures.stream()
                .map(Measurement::getTemperature)
                .mapToDouble(m -> m)
                .average()
                .orElseThrow(RuntimeException::new);
    }

    private boolean closeIntervalDates(LocalDateTime date) {
        return measurments.last().getDate().until(date, ChronoUnit.SECONDS) < 150;
    }
}

class Measurement implements Comparable<Measurement> {
    float temperature;
    float wind;
    float humidity;
    float visibility;
    LocalDateTime date;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");


    public Measurement(float temperature, float wind, float humidity,
                       float visibility, LocalDateTime date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public float getTemperature() {
        return temperature;
    }

    public LocalDateTime getDate() {
        return date;
    }


    @Override
    public int compareTo(Measurement other) {
        long span = Math.abs(other.date.until(date, ChronoUnit.SECONDS));
        if (span < 150) {
            return 0;
        } else return date.compareTo(other.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return Float.compare(that.temperature, temperature) == 0 && Float.compare(that.wind, wind) == 0 && Float.compare(that.humidity, humidity) == 0 && Float.compare(that.visibility, visibility) == 0 && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, wind, humidity, visibility, date);
    }

    @Override
    public String toString() {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, ZoneId.of("GMT"));
        return String.format("%.1f %.1f km/h %.1f%% %.1f km %s",
                temperature, wind, humidity, visibility, zonedDateTime.format(formatter));
    }

}
