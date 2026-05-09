import java.util.*;
import java.io.*;

public class DataStore {

    // ─────────────── In-memory data ───────────────
    public List<User>           users       = new ArrayList<>();
    public List<StudentProfile> students    = new ArrayList<>();
    public List<Course>         courses     = new ArrayList<>();
    public List<Enrollment>     enrollments = new ArrayList<>();
    public List<GradeRecord>    grades      = new ArrayList<>();

    // ─────────────── File paths ───────────────
    private static final String USERS_FILE       = "data/users.txt";
    private static final String STUDENTS_FILE    = "data/students.txt";
    private static final String COURSES_FILE     = "data/courses.txt";
    private static final String ENROLLMENTS_FILE = "data/enrollments.txt";
    private static final String GRADES_FILE      = "data/grades.txt";

    // ─────────────── Initialization ───────────────
    public void initialize() {
        new File("data").mkdirs();
        loadUsers();
        loadStudents();
        loadCourses();
        loadEnrollments();
        loadGrades();

        // Seed demo data if empty
        if (users.isEmpty()) seedDemoData();
    }

    private void seedDemoData() {
        // Users
        users.add(new User("admin",    "admin123",   "admin",      "Admin User",       "A001"));
        users.add(new User("john_doe", "pass123",    "student",    "John Doe",         "S001"));
        users.add(new User("jane_s",   "pass456",    "student",    "Jane Smith",       "S002"));
        users.add(new User("prof_ali", "teach789",   "instructor", "Prof. Ali Kaya",   "I001"));
        users.add(new User("prof_cem", "teach000",   "instructor", "Prof. Cem Demir",  "I002"));

        // Student profiles
        students.add(new StudentProfile("S001", "John Doe",   "Computer Science",  2, "john_doe"));
        students.add(new StudentProfile("S002", "Jane Smith",  "Mathematics",       3, "jane_s"));

        // Courses
        courses.add(new Course("CS101", "Introduction to Programming",  3, 30, "prof_ali"));
        courses.add(new Course("CS201", "Data Structures",              3, 25, "prof_ali"));
        courses.add(new Course("MATH101","Calculus I",                  4, 40, "prof_cem"));
        courses.add(new Course("CS301", "Algorithms",                   3, 20, "prof_ali"));
        courses.add(new Course("MATH201","Linear Algebra",              3, 35, "prof_cem"));

        // Enrollments
        enrollments.add(new Enrollment("john_doe", "CS101"));
        enrollments.add(new Enrollment("john_doe", "CS201"));
        enrollments.add(new Enrollment("john_doe", "MATH101"));
        enrollments.add(new Enrollment("jane_s",   "MATH101"));
        enrollments.add(new Enrollment("jane_s",   "MATH201"));

        // Grades
        grades.add(new GradeRecord("john_doe", "CS101",  78.0, 85.0));
        grades.add(new GradeRecord("john_doe", "MATH101", 65.0, 72.0));
        grades.add(new GradeRecord("jane_s",   "MATH101", 90.0, 95.0));

        saveAll();
        System.out.println("[DataStore] Demo data seeded.");
    }

    // ─────────────── Authentication ───────────────
    public User authenticate(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }

    // ─────────────── Finders ───────────────
    public User findUser(String username) {
        for (User u : users)
            if (u.getUsername().equals(username)) return u;
        return null;
    }

    public StudentProfile findStudentProfileByUsername(String username) {
        for (StudentProfile sp : students)
            if (sp.getUsername().equals(username)) return sp;
        return null;
    }

    public Course findCourse(String courseCode) {
        for (Course c : courses)
            if (c.getCourseCode().equalsIgnoreCase(courseCode)) return c;
        return null;
    }

    // ─────────────── Course queries ───────────────
    public List<Course> getCoursesByInstructor(String instructorUsername) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses)
            if (c.getInstructorUsername().equals(instructorUsername)) result.add(c);
        return result;
    }

    public int countEnrollmentForCourse(String courseCode) {
        int count = 0;
        for (Enrollment e : enrollments)
            if (e.getCourseCode().equalsIgnoreCase(courseCode)) count++;
        return count;
    }

    public boolean isStudentEnrolled(String studentUsername, String courseCode) {
        for (Enrollment e : enrollments)
            if (e.getStudentUsername().equals(studentUsername)
                    && e.getCourseCode().equalsIgnoreCase(courseCode))
                return true;
        return false;
    }

    // ─────────────── Enrollment operations ───────────────
    public List<Enrollment> getEnrollmentsByStudent(String studentUsername) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments)
            if (e.getStudentUsername().equals(studentUsername)) result.add(e);
        return result;
    }

    public List<Enrollment> getEnrollmentsByCourse(String courseCode) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments)
            if (e.getCourseCode().equalsIgnoreCase(courseCode)) result.add(e);
        return result;
    }

    /**
     * Enrolls a student in a course. Returns a status message.
     */
    public String enrollStudent(String studentUsername, String courseCode) {
        Course course = findCourse(courseCode);
        if (course == null) return "ERROR: Course not found.";
        if (isStudentEnrolled(studentUsername, courseCode))
            return "ERROR: Already enrolled in " + courseCode + ".";
        if (countEnrollmentForCourse(courseCode) >= course.getQuota())
            return "ERROR: Course quota is full.";

        enrollments.add(new Enrollment(studentUsername, courseCode));
        saveEnrollments();
        return "SUCCESS: Enrolled in " + courseCode + " - " + course.getCourseName() + ".";
    }

    /**
     * Removes a student from a course.
     */
    public String removeEnrollment(String studentUsername, String courseCode) {
        Iterator<Enrollment> it = enrollments.iterator();
        while (it.hasNext()) {
            Enrollment e = it.next();
            if (e.getStudentUsername().equals(studentUsername)
                    && e.getCourseCode().equalsIgnoreCase(courseCode)) {
                it.remove();
                saveEnrollments();
                return "SUCCESS: Dropped " + courseCode + ".";
            }
        }
        return "ERROR: Enrollment not found.";
    }

    // ─────────────── Grade operations ───────────────
    public GradeRecord findGrade(String studentUsername, String courseCode) {
        for (GradeRecord g : grades)
            if (g.getStudentUsername().equals(studentUsername)
                    && g.getCourseCode().equalsIgnoreCase(courseCode))
                return g;
        return null;
    }

    public List<GradeRecord> getGradesByStudent(String studentUsername) {
        List<GradeRecord> result = new ArrayList<>();
        for (GradeRecord g : grades)
            if (g.getStudentUsername().equals(studentUsername)) result.add(g);
        return result;
    }

    public void upsertGrade(String studentUsername, String courseCode,
                            double midterm, double finalExam) {
        GradeRecord existing = findGrade(studentUsername, courseCode);
        if (existing != null) {
            existing.setMidterm(midterm);
            existing.setFinalExam(finalExam);
        } else {
            grades.add(new GradeRecord(studentUsername, courseCode, midterm, finalExam));
        }
        saveGrades();
    }

    // ─────────────── GPA calculation ───────────────
    public double calculateGPA(String studentUsername) {
        List<GradeRecord> studentGrades = getGradesByStudent(studentUsername);
        if (studentGrades.isEmpty()) return 0.0;

        double totalPoints  = 0.0;
        int    totalCredits = 0;

        for (GradeRecord g : studentGrades) {
            Course c = findCourse(g.getCourseCode());
            int credits = (c != null) ? c.getCredit() : 3; // default 3 if not found
            totalPoints  += g.getGradePoint() * credits;
            totalCredits += credits;
        }
        return (totalCredits == 0) ? 0.0 : totalPoints / totalCredits;
    }

    // ─────────────── Save helpers ───────────────
    private void saveAll() {
        saveUsers(); saveStudents(); saveCourses(); saveEnrollments(); saveGrades();
    }

    public void saveUsers() {
        writeLines(USERS_FILE, users, u -> u.toFileString());
    }
    public void saveStudents() {
        writeLines(STUDENTS_FILE, students, s -> s.toFileString());
    }
    public void saveCourses() {
        writeLines(COURSES_FILE, courses, c -> c.toFileString());
    }
    public void saveEnrollments() {
        writeLines(ENROLLMENTS_FILE, enrollments, e -> e.toFileString());
    }
    public void saveGrades() {
        writeLines(GRADES_FILE, grades, g -> g.toFileString());
    }

    @FunctionalInterface
    interface Stringifier<T> { String apply(T t); }

    private <T> void writeLines(String path, List<T> list, Stringifier<T> fn) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (T item : list) pw.println(fn.apply(item));
        } catch (IOException e) {
            System.err.println("Save error [" + path + "]: " + e.getMessage());
        }
    }

    // ─────────────── Load helpers ───────────────
    public void loadUsers() {
        users.clear();
        for (String line : readLines(USERS_FILE)) {
            String[] p = line.split(",", 5);
            if (p.length == 5)
                users.add(new User(p[0], p[1], p[2], p[3], p[4]));
        }
    }
    public void loadStudents() {
        students.clear();
        for (String line : readLines(STUDENTS_FILE)) {
            String[] p = line.split(",", 5);
            if (p.length == 5)
                students.add(new StudentProfile(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4]));
        }
    }
    public void loadCourses() {
        courses.clear();
        for (String line : readLines(COURSES_FILE)) {
            String[] p = line.split(",", 5);
            if (p.length == 5)
                courses.add(new Course(p[0], p[1], Integer.parseInt(p[2]),
                        Integer.parseInt(p[3]), p[4]));
        }
    }
    public void loadEnrollments() {
        enrollments.clear();
        for (String line : readLines(ENROLLMENTS_FILE)) {
            String[] p = line.split(",", 2);
            if (p.length == 2) enrollments.add(new Enrollment(p[0], p[1]));
        }
    }
    public void loadGrades() {
        grades.clear();
        for (String line : readLines(GRADES_FILE)) {
            String[] p = line.split(",", 4);
            if (p.length == 4)
                grades.add(new GradeRecord(p[0], p[1],
                        Double.parseDouble(p[2]), Double.parseDouble(p[3])));
        }
    }

    private List<String> readLines(String path) {
        List<String> lines = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null)
                if (!line.trim().isEmpty()) lines.add(line.trim());
        } catch (IOException e) {
            System.err.println("Load error [" + path + "]: " + e.getMessage());
        }
        return lines;
    }
}
