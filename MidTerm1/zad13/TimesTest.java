package NP.MidTerm1.zad13;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class Time implements Comparable<Time> {
    int hours;
    int minutes;

    public Time(String time) throws UnsupportedFormatException, InvalidTimeException {
        time = time.replace(".", ":");
        String[] parts = time.split(":");
        if (parts.length == 1)
            throw new UnsupportedFormatException(time);
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        if (hour < 0 || hour > 23) {
            throw new InvalidTimeException(String.format("%s", hour));
        }
        if (minute < 0 || minute > 59) {
            throw new InvalidTimeException(String.format("%s", minute));
        }
        this.hours = hour;
        this.minutes = minute;
    }

    String formatAMPM() {
        String ampm;
        if (hours == 0) {
            hours += 12;
            ampm = "AM";
        } else if (hours == 12) {
            ampm = "PM";
        } else if (hours < 12) {
            ampm = "AM";
        } else {
            hours -= 12;
            ampm = "PM";
        }
        return String.format("%2d:%02d %s", hours, minutes, ampm);
    }

    String format24() {
        return String.format("%2d:%02d", hours, minutes);
    }

    @Override
    public int compareTo(Time other) {
        if (hours < other.hours) {
            return -1;
        } else if (hours == other.hours) {
            if (minutes < other.minutes)
                return -1;
            else if (minutes > other.minutes)
                return 1;
            else
                return 0;
        } else return 1;
    }
}


class TimeTable {

    List<Time> times;

    public TimeTable() {
        times = new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            for (int i = 0; i < parts.length; i++) {
                Time time = new Time(parts[i]);
                times.add(time);
            }
        }
    }

    void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter writer = new PrintWriter(outputStream);
        List<String> list = new ArrayList<>();

        Collections.sort(times);

        for (Time time : times) {
            if (format == TimeFormat.FORMAT_24) {
                list.add(time.format24());
            } else {
                list.add(time.formatAMPM());
            }
        }

        for (String time : list) {
            writer.println(time);
        }

        writer.flush();
    }

}

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException(String message) {
        super(message);
    }
}