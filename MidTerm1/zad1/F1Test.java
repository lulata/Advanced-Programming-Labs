package NP.MidTerm1.zad1;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race {
    // vashiot kod ovde
    private ArrayList<F1Racer> drivers;

    public F1Race() {
        drivers = new ArrayList<>();
    }



    public void readResults(java.io.InputStream in) {
        java.util.Scanner scanner = new java.util.Scanner(in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            StringTokenizer st = new StringTokenizer(line, " ");
            String name = st.nextToken();
            ArrayList<String> times = new ArrayList<>();
            while (st.hasMoreTokens()) {
                times.add(st.nextToken());
            }
            drivers.add(new F1Racer(name, times));
        }
    }

    public int miliseconds(String time) {
        StringTokenizer st = new StringTokenizer(time, ":");
        int minutes = Integer.parseInt(st.nextToken());
        int seconds = Integer.parseInt(st.nextToken());
        int milliSeconds = Integer.parseInt(st.nextToken());
        return minutes * 60 * 1000 + seconds * 1000 + milliSeconds;
    }

    public void sortRacer(ArrayList<F1Racer> drivers){
        for (int i = 0; i < drivers.size() - 1; i++) {
            for (int j = 0; j < drivers.size() - i - 1; j++) {
                if (miliseconds(drivers.get(j).getLaps().get(0)) > miliseconds(drivers.get(j + 1).getLaps().get(0))) {
                    F1Racer temp = drivers.get(j);
                    drivers.set(j, drivers.get(j + 1));
                    drivers.set(j + 1, temp);
                }
            }
        }
    }

    public void printSorted(java.io.PrintStream out){
//        1. Alonso      1:53:563
//        2. Hamilton    1:54:371
//        3. Vettel      1:54:987
//        4. Massa       1:55:187
        ArrayList<F1Racer> bestDrivers = new ArrayList<>();
        for (F1Racer driver : drivers) {
            int minMilliseconds = Integer.MAX_VALUE;
            int indexBestLap = -1;
            for (int j = 0; j < driver.getLaps().size(); ++j) {
                int milliseconds = miliseconds(driver.getLaps().get(j));
                if (minMilliseconds > milliseconds) {
                    minMilliseconds = milliseconds;
                    indexBestLap = j;
                }
            }
            bestDrivers.add(new F1Racer(driver.getName(), driver.getLaps().get(indexBestLap)));
        }
        sortRacer(bestDrivers);
        for (int i = 0; i < bestDrivers.size(); ++i) {
            out.println(String.format("%d. %-9s %10s", i + 1, bestDrivers.get(i).getName(), bestDrivers.get(i).getLaps().get(0)));
        }

    }

}

class F1Racer {
    private String name;
    private ArrayList<String> laps;

    public F1Racer(String name, ArrayList<String> laps) {
        this.name = name;
        this.laps = laps;
    }

    public F1Racer(String name, String lap) {
        this.name = name;
        this.laps = new ArrayList<>();
        this.laps.add(lap);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLaps() {
        return laps;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append(" ");
        for (String lap : getLaps()) {
            sb.append(lap);
            sb.append(" ");
        }
        return sb.toString();
    }
}