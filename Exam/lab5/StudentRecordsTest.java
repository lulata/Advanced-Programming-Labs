package NP.Exam.lab5;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * January 2016 Exam problem 1
 */
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here

class Student implements Comparable<Student> {
    String id;
    String branch;
    ArrayList<Integer> notes;

    Student(String id, String branch, ArrayList<Integer> notes) {
        this.id = id;
        this.branch = branch;
        this.notes = notes;
    }

    public long getNumTens() {
        return notes.stream().filter(i -> i.equals(10)).count();
    }

    public long getNumNines() {
        return notes.stream().filter(i -> i.equals(9)).count();
    }

    public long getNumEights() {
        return notes.stream().filter(i -> i.equals(8)).count();
    }

    public long getNumSevens() {
        return notes.stream().filter(i -> i.equals(7)).count();
    }

    public long getNumSixes() {
        return notes.stream().filter(i -> i.equals(6)).count();
    }


    public String getId() {
        return id;
    }

    public Double getAvg() {
        return 1.0 * notes.stream().mapToInt(i -> i).sum() / notes.size();
    }


    public int compare(Student o1, Student o2) {
        // TODO Auto-generated method stub
        if (o1.branch.compareTo(o2.branch) == 0) {
            if (o1.getAvg().compareTo(o2.getAvg()) == 0) {
                return o1.getId().compareTo(o2.getId());
            }
            return o2.getAvg().compareTo(o1.getAvg());
        }
        return o1.branch.compareTo(o2.branch);
    }

    @Override
    public int compareTo(Student arg0) {
        // TODO Auto-generated method stub
        return this.compare(this, arg0);
    }


}

class Branch// implements Comparable<Branch>
{
    String branch;
    Map<Integer, Integer> notes;

    Branch(String branch) {
        this.branch = branch;
        notes = new TreeMap<>();
    }

    public void addNote(int note) {
        notes.computeIfPresent(note, (key, val) -> {
            val += 1;
            return val;
        });
        notes.putIfAbsent(note, 1);
    }

    public Integer getTens() {
        if (notes.get(10) == null)
            return 0;
        return notes.get(10);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(branch).append("\n");

        for (Integer i : notes.keySet()) {
            int numStars = notes.get(i);

            sb.append(String.format("%2d | ", i));
            if (numStars % 10 == 0)
                for (int j = 0; j < Math.round(numStars / 10); j++) {
                    sb.append("*");
                }
            else
                for (int j = 0; j < Math.round(numStars / 10) + 1; j++) {
                    sb.append("*");
                }

            sb.append(String.format("(%d)\n", numStars));
        }
        return sb.toString();
    }
	/*@Override
	public int compareTo(Branch arg0) {
		return this.getTens().compareTo(arg0.getTens());
	}*/
}

class StudentRecords {
    TreeSet<Student> records;
    ArrayList<Branch> branches;

    public StudentRecords() {
        // TODO Auto-generated constructor stub
        records = new TreeSet<>();
        branches = new ArrayList<>();
    }

    public void writeDistribution(OutputStream outputStream) {
        // TODO Auto-generated method stub
        PrintWriter pw = new PrintWriter(outputStream);

        branches.stream().sorted(Comparator.comparing(Branch::getTens).reversed())
                .forEach(b -> {
                    pw.print(b);
                });
		/*
		for(Branch b : branches)
		{
			pw.print(b);
		}*/
        pw.flush();
    }


    public int readRecords(InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        int j = 0;
        while (in.hasNextLine()) {
            String line[] = in.nextLine().split("\\s+");

            String id = line[0];
            String branch = line[1];
            Branch b = new Branch(branch);
            boolean ifb = branches.stream().anyMatch(br -> br.branch.equals(branch));
            ArrayList<Integer> notes = new ArrayList<>();
            for (int i = 2; i < line.length; i++) {
                notes.add(Integer.parseInt(line[i]));
                if (ifb) {
                    final int temp = i;
                    branches.stream().forEach(br ->
                    {
                        if (br.branch.equals(branch)) {
                            br.addNote(Integer.parseInt(line[temp]));
                        }

                    });
                } else
                    b.addNote(Integer.parseInt(line[i]));
            }


            Student st = new Student(id, branch, notes);
            records.add(st);
            if (!ifb)
                branches.add(b);
            j++;

        }
        in.close();
        return j;
    }

    public void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        StringBuilder sb = new StringBuilder();
        String check = "";
        for (Student s : records) {
            if (!check.equals(s.branch)) {
                sb.append(s.branch);
                sb.append("\n");
                check = s.branch;
            }
            sb.append(String.format("%s %.2f\n", s.id, s.getAvg()));
        }
        pw.print(sb.toString());
        pw.flush();
    }


}
