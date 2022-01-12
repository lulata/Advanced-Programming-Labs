package NP.MidTerm1.zad4;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.List;



public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}



class Archive {
    protected int id;
    protected LocalDate dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
}

class LockedArchive extends Archive {
    private LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public LocalDate getDateToOpen() {
        return dateToOpen;
    }
}

class SpecialArchive extends Archive {
    private int maxOpen;
    private int timesOpened;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        timesOpened = 0;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public void incrementTimesOpened() {
        this.timesOpened++;
    }

    public int getTimesOpened() {
        return timesOpened;
    }
}

class ArchiveStore {

    private List<Archive> archives;
    private List<String> logs;



    public ArchiveStore() {
        this.archives = new LinkedList<>();
        this.logs = new LinkedList<>();
    }

    public void archiveItem(Archive archive, LocalDate date) {
        archive.setDateArchived(date);
        archives.add(archive);
        logs.add(String.format("Item %d archived at %s", archive.id, getFormattedDay(date)));
    }

    public void openItem(int open, LocalDate date) throws NonExistingItemException {
        Archive wantedArchive = archives.stream()
                .filter(e -> e.getId() == open&&e.getDateArchived().equals(date))
                .findFirst()
                .orElseThrow(() -> new NonExistingItemException(open));

        if (wantedArchive instanceof LockedArchive)
            openLockedArchive((LockedArchive) wantedArchive, date);
        else
            openSpecialArchive((SpecialArchive) wantedArchive, date);
    }

    private void openLockedArchive(LockedArchive archive, LocalDate date) {
        if (date.isBefore(archive.getDateToOpen()))
            logs.add(String.format("Item %d cannot be opened before %s", archive.getId(), getFormattedDay(archive.getDateToOpen())));
        else
            logs.add(String.format("Item %d opened at %s", archive.getId(), getFormattedDay(date)));
    }

    private void openSpecialArchive(SpecialArchive archive, LocalDate date) {
        if (archive.getMaxOpen() <= archive.getTimesOpened()) {
            logs.add(String.format("Item %d cannot be opened more than %d times", archive.getId(), archive.getMaxOpen()));
        } else {
            logs.add(String.format("Item %d opened at %s", archive.getId(), getFormattedDay(date)));
            archive.incrementTimesOpened();
        }
    }

    public String getLog() {
        return String.join("\n", logs);
    }

    private String getFormattedDay(LocalDate date) {
        return String.format("%d-%02d-%02d", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

}

class NonExistingItemException extends Exception {

    NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }

}


