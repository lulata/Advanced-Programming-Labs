package NP.MidTerm2.lab7;

import java.util.*;


public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

class Stadium {
    String name;
    HashMap<String, Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        sectors = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        for (int i = 0; i < sectorNames.length; ++i) {
            addSector(sectorNames[i], sectorSizes[i]);
        }
    }

    void addSector(String name, int size) {
        Sector sector = new Sector(name, size);
        sectors.put(name, sector);
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        sectors.get(sectorName).reserveSeat(seat, type);
    }

    public void showSectors() {
        sectors.values().stream()
                .sorted(Comparator.comparing(Sector::getNumAvailableSeats).reversed().thenComparing(Sector::getCode))
                .forEach(System.out::println);
    }

}

class Sector {
    private String code;
    private int numAvailableSeats;
    private Set<Integer> occupiedSeats;
    int type;

    public Sector(String code, int numAvailableSeats) {
        this.code = code;
        this.numAvailableSeats = numAvailableSeats;
        this.occupiedSeats = new HashSet<>();
        type = 0;
    }

    public String getCode() {
        return code;
    }

    public int getNumAvailableSeats() {
        return numAvailableSeats;
    }

    public void reserveSeat(int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if (occupiedSeats.contains(seat))
            throw new SeatTakenException();
        if (this.type != 0 && type != this.type && type != 0)
            throw new SeatNotAllowedException();
        if (this.type == 0)
            this.type = type;

        occupiedSeats.add(seat);
        --numAvailableSeats;
    }

    private double getPercentTicketsSold() {
        int capacity = numAvailableSeats + occupiedSeats.size();
        return (capacity - numAvailableSeats) * 1.0 / capacity * 100;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, numAvailableSeats, numAvailableSeats + occupiedSeats.size(), getPercentTicketsSold());
    }
}

class SeatNotAllowedException extends Exception {

}

class SeatTakenException extends Exception {

}
