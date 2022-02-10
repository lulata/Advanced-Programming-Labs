package NP.MidTerm2.lab14;


import java.util.*;
import java.util.stream.Collectors;

class OperationNotAllowedException extends Exception {

    public OperationNotAllowedException(String msg) {
        super(msg);
    }
}

abstract class Student {
    String id;
    Map<Integer, List<Integer>> gradesByTerm;
    Set<String> courses;

    public Student(String id) {
        this.id = id;
        gradesByTerm = new TreeMap<>();
        courses = new TreeSet<>();
    }

    public void addGrade(int term, String courseName, int grade) throws OperationNotAllowedException {
        if (term > gradesByTerm.size())
            throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, id));
        gradesByTerm.putIfAbsent(term, new ArrayList<>());

        if (gradesByTerm.get(term).size() == 3)
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d", id, term));

        gradesByTerm.get(term).add(grade);
        courses.add(courseName);

    }

    public boolean isGraduate() {
        return courses.size() == gradesByTerm.size() * 3;
    }

    public double averageGradeForTerm(int term) {
        return gradesByTerm.get(term)
                .stream()
                .mapToInt(Integer::valueOf)
                .average()
                .orElse(5.00);
    }

    private String reportFormTerm(int term) {
        StringBuilder sb = new StringBuilder();

        List<Integer> grades = gradesByTerm.get(term);
        sb.append(String.format("Term %d\n", term));
        sb.append(String.format("Courses: %d\n", grades.size()));
        sb.append(String.format("Average grade for term: %.2f\n", averageGradeForTerm(term)));
        return sb.toString();
    }

    public double averagaGrade() {
        return gradesByTerm
                .values()
                .stream()
                .flatMap(Collection::stream)
                .mapToInt(Integer::valueOf)
                .average()
                .orElse(5.0);
    }

    public String getDetailedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student: " + id).append("\n");
        gradesByTerm.keySet()
                .forEach(term -> sb.append(reportFormTerm(term)));

        sb.append(String.format("Average grade: %.2f\n", averagaGrade()));
        sb.append(String.format("Courses attended: %s", courses.stream().collect(Collectors.joining(","))));
        return sb.toString();

    }

    public String getId() {
        return id;
    }

    public int getCourses() {
        return courses.size();
    }

    abstract public String shortLog();
}

class Student3 extends Student {
    public Student3(String id) {
        super(id);
        for (int i = 1; i <= 6; i++)
            gradesByTerm.put(i, new ArrayList<>());
    }

    @Override
    public String shortLog() {
        return String.format("Student with ID %s graduated with average grade %.2f in 3 years.\n", this.id, this.averagaGrade());
    }
}

class Student4 extends Student {
    public Student4(String id) {
        super(id);
        for (int i = 1; i <= 8; i++)
            gradesByTerm.put(i, new ArrayList<>());
    }

    @Override
    public String shortLog() {
        return String.format("Student with ID %s graduated with average grade %.2f in 4 years.\n", this.id, this.averagaGrade());
    }
}

class Course {
    String courseName;
    List<Integer> grades;

    public Course(String courseName) {
        this.courseName = courseName;
        grades = new ArrayList<>();
    }

    public void addGrade(int grade) {
        grades.add(grade);
    }

    public int getNumberOfStudents() {
        return grades.size();
    }

    public double average() {
        return grades
                .stream()
                .mapToInt(Integer::valueOf)
                .average()
                .orElse(5.0);
    }

    public String getCourseName() {
        return courseName;
    }
}

class Faculty {
    Map<String, Student> studentsById;
    StringBuilder logs;
    Map<String, Course> coursesByName;

    public Faculty() {
        studentsById = new HashMap<>();
        logs = new StringBuilder();
        coursesByName = new HashMap<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        if (yearsOfStudies == 3)
            studentsById.put(id, new Student3(id));
        else
            studentsById.put(id, new Student4(id));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Student student = studentsById.get(studentId);
        student.addGrade(term, courseName, grade);
        coursesByName.putIfAbsent(courseName, new Course(courseName));
        coursesByName.get(courseName).addGrade(grade);
        if (student.isGraduate()) {
            logs.append(student.shortLog());
            studentsById.remove(student.getId());
        }

    }

    String getFacultyLogs() {
        return logs.toString().substring(0, logs.toString().length() - 1);
    }

    String getDetailedReportForStudent(String id) {
        return studentsById.get(id).getDetailedReport();
    }

    void printFirstNStudents(int n) {
        Comparator<Student> comparator = Comparator.comparing(Student::getCourses).thenComparing(Student::averagaGrade).thenComparing(Student::getId).reversed();
        studentsById
                .values()
                .stream()
                .sorted(comparator)
                .limit(n)
                .forEach(s -> System.out.println(String.format("Student: %s Courses passed: %d Average grade: %.2f", s.getId(), s.getCourses(), s.averagaGrade())));
    }

    void printCourses() {
        Comparator<Course> comparator = Comparator.comparing(Course::getNumberOfStudents).thenComparing(Course::average).thenComparing(Course::getCourseName);

        coursesByName
                .values()
                .stream()
                .sorted(comparator)
                .forEach(course -> System.out.println(String.format("%s %d %.2f", course.getCourseName(), course.getNumberOfStudents(), course.average())));

    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase == 10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase == 11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i = 11; i < 15; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
