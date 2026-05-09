public class Enrollment {
    private String studentUsername;
    private String courseCode;

    public Enrollment(String studentUsername, String courseCode) {
        this.studentUsername = studentUsername;
        this.courseCode      = courseCode;
    }

    // Getters
    public String getStudentUsername() { return studentUsername; }
    public String getCourseCode()      { return courseCode; }

    public String toFileString() {
        return studentUsername + "," + courseCode;
    }

    @Override
    public String toString() {
        return "Enrollment{student='" + studentUsername + "', course='" + courseCode + "'}";
    }
}
