import java.util.*;

/**
 * UniversityAutomationApp — Console-based entry point.
 * Student-level features: login, view profile, browse courses,
 * enroll/drop, view grades, and view transcript.
 */
public class UniversityAutomationApp {

    private static final DataStore store = new DataStore();
    private static final Scanner   sc    = new Scanner(System.in);
    private static User currentUser = null;

    // ─────────────────────────────────────────────────────────
    //  main
    // ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        store.initialize();
        showLoginPanel();
    }

    // ─────────────────────────────────────────────────────────
    //  LOGIN
    // ─────────────────────────────────────────────────────────
    private static void showLoginPanel() {
        while (true) {
            printHeader("UNIVERSITY AUTOMATION SYSTEM — LOGIN");
            System.out.print("  Username : ");
            String username = sc.nextLine().trim();
            System.out.print("  Password : ");
            String password = sc.nextLine().trim();

            currentUser = store.authenticate(username, password);
            if (currentUser == null) {
                System.out.println("\n  [!] Invalid credentials. Please try again.\n");
            } else {
                System.out.println("\n  Welcome, " + currentUser.getFullName()
                        + " (" + currentUser.getRole() + ")!\n");
                showDashboard();
                return;
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    //  DASHBOARD — routes by role
    // ─────────────────────────────────────────────────────────
    private static void showDashboard() {
        switch (currentUser.getRole()) {
            case "student":    showStudentDashboard();    break;
            case "instructor": showInstructorDashboard(); break;
            case "admin":      showAdminDashboard();      break;
            default:
                System.out.println("Unknown role. Logging out.");
        }
    }

    // ─────────────────────────────────────────────────────────
    //  STUDENT DASHBOARD
    // ─────────────────────────────────────────────────────────
    private static void showStudentDashboard() {
        while (true) {
            printHeader("STUDENT DASHBOARD  —  " + currentUser.getFullName());
            System.out.println("  [1] My Profile");
            System.out.println("  [2] Available Courses");
            System.out.println("  [3] My Courses  (Enroll / Drop)");
            System.out.println("  [4] My Grades");
            System.out.println("  [5] My Transcript  (GPA)");
            System.out.println("  [0] Logout");
            System.out.print("\n  Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": showStudentProfile();       break;
                case "2": showAvailableCoursesPanel(); break;
                case "3": showMyCoursesPanel();        break;
                case "4": showGradesPanel();           break;
                case "5": showTranscriptPanel();       break;
                case "0":
                    System.out.println("\n  Goodbye!\n");
                    currentUser = null;
                    showLoginPanel();
                    return;
                default:
                    System.out.println("  [!] Invalid option.\n");
            }
        }
    }

    // ── 1. My Profile ────────────────────────────────────────
    private static void showStudentProfile() {
        printHeader("MY PROFILE");
        StudentProfile sp = store.findStudentProfileByUsername(currentUser.getUsername());
        if (sp == null) {
            System.out.println("  No profile found for your account.");
        } else {
            System.out.println("  Student ID  : " + sp.getStudentId());
            System.out.println("  Full Name   : " + sp.getFullName());
            System.out.println("  Department  : " + sp.getDepartment());
            System.out.println("  Year        : " + sp.getYear());
            System.out.println("  Username    : " + sp.getUsername());
        }
        pressEnter();
    }

    // ── 2. Available Courses ─────────────────────────────────
    private static void showAvailableCoursesPanel() {
        printHeader("AVAILABLE COURSES");
        if (store.courses.isEmpty()) {
            System.out.println("  No courses found.");
        } else {
            System.out.printf("  %-10s | %-35s | Credits | Quota | Enrolled | Instructor%n",
                    "Code", "Name");
            printLine(95);
            for (Course c : store.courses) {
                int enrolled = store.countEnrollmentForCourse(c.getCourseCode());
                boolean full = enrolled >= c.getQuota();
                System.out.printf("  %-10s | %-35s |    %-4d |  %-4d |  %-7d | %s%s%n",
                        c.getCourseCode(), c.getCourseName(), c.getCredit(),
                        c.getQuota(), enrolled, c.getInstructorUsername(),
                        full ? "  [FULL]" : "");
            }
        }

        System.out.println("\n  Enter a course code to enroll, or press ENTER to go back:");
        System.out.print("  Code: ");
        String code = sc.nextLine().trim();
        if (!code.isEmpty()) {
            String result = store.enrollStudent(currentUser.getUsername(), code);
            System.out.println("\n  " + result);
            pressEnter();
        }
    }

    // ── 3. My Courses ────────────────────────────────────────
    private static void showMyCoursesPanel() {
        printHeader("MY ENROLLED COURSES");
        List<Enrollment> myEnrollments = store.getEnrollmentsByStudent(currentUser.getUsername());

        if (myEnrollments.isEmpty()) {
            System.out.println("  You are not enrolled in any courses.");
        } else {
            System.out.printf("  %-4s | %-10s | %-35s | Credits%n", "#", "Code", "Name");
            printLine(70);
            int i = 1;
            for (Enrollment e : myEnrollments) {
                Course c = store.findCourse(e.getCourseCode());
                String name    = (c != null) ? c.getCourseName() : "Unknown";
                int    credits = (c != null) ? c.getCredit() : 0;
                System.out.printf("  %-4d | %-10s | %-35s | %d%n",
                        i++, e.getCourseCode(), name, credits);
            }
        }

        System.out.println("\n  Enter a course code to DROP, or press ENTER to go back:");
        System.out.print("  Code: ");
        String code = sc.nextLine().trim();
        if (!code.isEmpty()) {
            String result = store.removeEnrollment(currentUser.getUsername(), code);
            System.out.println("\n  " + result);
            pressEnter();
        }
    }

    // ── 4. My Grades ─────────────────────────────────────────
    private static void showGradesPanel() {
        printHeader("MY GRADES");
        List<GradeRecord> myGrades = store.getGradesByStudent(currentUser.getUsername());

        if (myGrades.isEmpty()) {
            System.out.println("  No grades recorded yet.");
        } else {
            System.out.printf("  %-10s | %-35s | Midterm | Final  |   Avg  | Grade%n",
                    "Code", "Course Name");
            printLine(85);
            for (GradeRecord g : myGrades) {
                Course c = store.findCourse(g.getCourseCode());
                String name = (c != null) ? c.getCourseName() : "Unknown";
                System.out.printf("  %-10s | %-35s |  %5.1f  |  %5.1f |  %5.1f | %s%n",
                        g.getCourseCode(), name,
                        g.getMidterm(), g.getFinalExam(),
                        g.calculateAverage(), g.getLetterGrade());
            }
        }
        pressEnter();
    }

    // ── 5. Transcript ────────────────────────────────────────
    private static void showTranscriptPanel() {
        printHeader("OFFICIAL TRANSCRIPT");
        StudentProfile sp = store.findStudentProfileByUsername(currentUser.getUsername());

        // Header block
        System.out.println("  ┌─────────────────────────────────────────────────────┐");
        System.out.printf ("  │  Name       : %-37s│%n",
                (sp != null) ? sp.getFullName() : currentUser.getFullName());
        System.out.printf ("  │  Student ID : %-37s│%n",
                (sp != null) ? sp.getStudentId() : "N/A");
        System.out.printf ("  │  Department : %-37s│%n",
                (sp != null) ? sp.getDepartment() : "N/A");
        System.out.printf ("  │  Year       : %-37s│%n",
                (sp != null) ? sp.getYear() : "N/A");
        System.out.println("  └─────────────────────────────────────────────────────┘");

        List<GradeRecord> myGrades = store.getGradesByStudent(currentUser.getUsername());
        double totalCredits = 0;

        System.out.println();
        if (myGrades.isEmpty()) {
            System.out.println("  No completed courses.");
        } else {
            System.out.printf("  %-10s | %-30s | Cr | Midterm | Final  |   Avg  | Grade | Points%n",
                    "Code", "Course");
            printLine(90);
            for (GradeRecord g : myGrades) {
                Course c = store.findCourse(g.getCourseCode());
                String name   = (c != null) ? c.getCourseName() : "Unknown";
                int    credit = (c != null) ? c.getCredit() : 3;
                totalCredits += credit;
                System.out.printf("  %-10s | %-30s | %2d |  %5.1f  |  %5.1f |  %5.1f | %-5s | %.1f%n",
                        g.getCourseCode(), name, credit,
                        g.getMidterm(), g.getFinalExam(),
                        g.calculateAverage(), g.getLetterGrade(),
                        g.getGradePoint());
            }
            printLine(90);
        }

        double gpa = store.calculateGPA(currentUser.getUsername());
        System.out.printf("%n  Total Credits Attempted : %.0f%n", totalCredits);
        System.out.printf("  Cumulative GPA          : %.2f / 4.00%n", gpa);
        System.out.println("  GPA Standing            : " + gpaStanding(gpa));
        pressEnter();
    }

    private static String gpaStanding(double gpa) {
        if (gpa >= 3.5) return "Dean's List";
        if (gpa >= 2.0) return "Good Standing";
        if (gpa >= 1.0) return "Academic Probation";
        return "Academic Suspension";
    }

    // ─────────────────────────────────────────────────────────
    //  INSTRUCTOR DASHBOARD (stub)
    // ─────────────────────────────────────────────────────────
    private static void showInstructorDashboard() {
        while (true) {
            printHeader("INSTRUCTOR DASHBOARD  —  " + currentUser.getFullName());
            System.out.println("  [1] My Courses");
            System.out.println("  [2] Course Enrollments");
            System.out.println("  [3] Enter / Update Grades");
            System.out.println("  [0] Logout");
            System.out.print("\n  Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": showInstructorCourses(); break;
                case "2": showCourseEnrollments(); break;
                case "3": showGradeEntryPanel();   break;
                case "0":
                    currentUser = null;
                    showLoginPanel();
                    return;
                default:
                    System.out.println("  [!] Invalid option.\n");
            }
        }
    }

    private static void showInstructorCourses() {
        printHeader("MY COURSES");
        List<Course> myCourses = store.getCoursesByInstructor(currentUser.getUsername());
        if (myCourses.isEmpty()) {
            System.out.println("  No courses assigned.");
        } else {
            for (Course c : myCourses) {
                int enrolled = store.countEnrollmentForCourse(c.getCourseCode());
                System.out.printf("  %s | %-35s | Credits: %d | Enrolled: %d/%d%n",
                        c.getCourseCode(), c.getCourseName(), c.getCredit(),
                        enrolled, c.getQuota());
            }
        }
        pressEnter();
    }

    private static void showCourseEnrollments() {
        printHeader("COURSE ENROLLMENTS");
        System.out.print("  Enter course code: ");
        String code = sc.nextLine().trim();
        List<Enrollment> list = store.getEnrollmentsByCourse(code);
        if (list.isEmpty()) {
            System.out.println("  No students enrolled in " + code + ".");
        } else {
            System.out.printf("  Students enrolled in %s:%n", code.toUpperCase());
            for (Enrollment e : list) {
                StudentProfile sp = store.findStudentProfileByUsername(e.getStudentUsername());
                String name = (sp != null) ? sp.getFullName() : e.getStudentUsername();
                System.out.println("    - " + name + " (" + e.getStudentUsername() + ")");
            }
        }
        pressEnter();
    }

    private static void showGradeEntryPanel() {
        printHeader("GRADE ENTRY");
        System.out.print("  Student username : ");
        String username = sc.nextLine().trim();
        System.out.print("  Course code      : ");
        String courseCode = sc.nextLine().trim();

        if (!store.isStudentEnrolled(username, courseCode)) {
            System.out.println("  [!] Student is not enrolled in " + courseCode + ".");
            pressEnter();
            return;
        }

        try {
            System.out.print("  Midterm score (0-100): ");
            double midterm = Double.parseDouble(sc.nextLine().trim());
            System.out.print("  Final score   (0-100): ");
            double finalEx = Double.parseDouble(sc.nextLine().trim());

            if (midterm < 0 || midterm > 100 || finalEx < 0 || finalEx > 100) {
                System.out.println("  [!] Scores must be between 0 and 100.");
            } else {
                store.upsertGrade(username, courseCode, midterm, finalEx);
                GradeRecord g = store.findGrade(username, courseCode);
                System.out.printf("%n  Saved — Average: %.1f  Grade: %s%n",
                        g.calculateAverage(), g.getLetterGrade());
            }
        } catch (NumberFormatException ex) {
            System.out.println("  [!] Invalid number entered.");
        }
        pressEnter();
    }

    // ─────────────────────────────────────────────────────────
    //  ADMIN DASHBOARD (stub)
    // ─────────────────────────────────────────────────────────
    private static void showAdminDashboard() {
        while (true) {
            printHeader("ADMIN DASHBOARD  —  " + currentUser.getFullName());
            System.out.println("  [1] List All Users");
            System.out.println("  [2] List All Courses");
            System.out.println("  [3] List All Students");
            System.out.println("  [0] Logout");
            System.out.print("\n  Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": listAllUsers();    break;
                case "2": listAllCourses();  break;
                case "3": listAllStudents(); break;
                case "0":
                    currentUser = null;
                    showLoginPanel();
                    return;
                default:
                    System.out.println("  [!] Invalid option.\n");
            }
        }
    }

    private static void listAllUsers() {
        printHeader("ALL USERS");
        for (User u : store.users)
            System.out.printf("  %-15s | %-12s | %s%n",
                    u.getUsername(), u.getRole(), u.getFullName());
        pressEnter();
    }

    private static void listAllCourses() {
        printHeader("ALL COURSES");
        for (Course c : store.courses) {
            int enrolled = store.countEnrollmentForCourse(c.getCourseCode());
            System.out.printf("  %s | %-35s | Credits: %d | %d/%d enrolled%n",
                    c.getCourseCode(), c.getCourseName(), c.getCredit(),
                    enrolled, c.getQuota());
        }
        pressEnter();
    }

    private static void listAllStudents() {
        printHeader("ALL STUDENTS");
        for (StudentProfile sp : store.students)
            System.out.println("  " + sp);
        pressEnter();
    }

    // ─────────────────────────────────────────────────────────
    //  Utility helpers
    // ─────────────────────────────────────────────────────────
    private static void printHeader(String title) {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.printf ("  ║  %-52s║%n", title);
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printLine(int len) {
        System.out.println("  " + "─".repeat(len));
    }

    private static void pressEnter() {
        System.out.print("\n  Press ENTER to continue...");
        sc.nextLine();
        System.out.println();
    }
}
