package NP.MidTerm1.zad20;

import java.util.*;

class FileNameExistsException extends Exception {
    public FileNameExistsException(String file, String folder) {
        super("There is already a file named " + file + " in the folder " + folder);
    }
}

interface IFile {
    String getFileName();

    long getFileSize();

    String getFileInfo(String tabs);

    void sortBySize();

    long findLargestFile();

    String getType();
}

class File implements IFile {
    String name;
    long fileSize;

    public File(String name, long fileSize) {
        this.name = name;
        this.fileSize = fileSize;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String getFileInfo(String tabs) {
        return tabs + String.format("File name: %10s File size: %10d\n", name, fileSize);
    }

    @Override
    public void sortBySize() {
    }

    @Override
    public long findLargestFile() {
        return 0;
    }

    @Override
    public String getType() {
        return "File";
    }

}

class Folder implements IFile {
    String folderName;
    List<IFile> files;

    public Folder(String folderName) {
        this.folderName = folderName;
        this.files = new ArrayList<>();
    }

    public void addFile(IFile file) throws FileNameExistsException {
        if (files.stream().anyMatch(f -> f.getFileName().equals(file.getFileName()))) {
            throw new FileNameExistsException(file.getFileName(), folderName);
        }
        files.add(file);
    }

    @Override
    public String getFileName() {
        return folderName;
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(String tabs) {
        StringBuilder sb = new StringBuilder();
        sb.append(tabs).append(String.format("Folder name: %10s Folder size: %10d\n", folderName, getFileSize()));
        for (IFile file : files) {
            sb.append(file.getFileInfo(tabs + "\t"));
        }
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        Comparator<IFile> comparator = Comparator.comparing(IFile::getFileSize);
        files.sort(comparator);
        files.forEach(IFile::sortBySize);
    }

    @Override
    public long findLargestFile() {
        List<Long> sizes = new ArrayList<>();
        sizes.add(files.stream().filter(file -> file.getType().equals("File"))
                .mapToLong(IFile::getFileSize)
                .max().orElse(0));
        for(IFile file : files) {
            sizes.add(file.findLargestFile());
        }
        return sizes.stream().mapToLong(Long::longValue).max().orElse(0);
    }

    @Override
    public String getType() {
        return "Folder";
    }
}


class FileSystem {
    private Folder root;

    public FileSystem() {
        root = new Folder("root");
    }

    public void addFile(IFile file) throws FileNameExistsException {
        root.addFile(file);
    }

    public void sortBySize() {
        root.sortBySize();
    }

    public String getFileSystemInfo() {
        return root.getFileInfo("");
    }

    public long findLargestFile() {
        return root.findLargestFile();
    }

    @Override
    public String toString() {
        return root.getFileInfo("");
    }
}


public class FileSystemTest {

    public static Folder readFolder(Scanner sc) {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < totalFiles; i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String[] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args) {

        //file reading from input

        Scanner sc = new Scanner(System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());


    }
}
