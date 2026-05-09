public class Course {
    private String courseCode;
    private String courseName;
    private int credit;
    private int quota;
    private String instructorUsername;

    public Course(String courseCode, String courseName, int credit, int quota, String instructorUsername) {
        this.courseCode         = courseCode;
        this.courseName         = courseName;
        this.credit             = credit;
        this.quota              = quota;
        this.instructorUsername = instructorUsername;
    }

    // Getters
    public String getCourseCode()         { return courseCode; }
    public String getCourseName()         { return courseName; }
    public int    getCredit()             { return credit; }
    public int    getQuota()              { return quota; }
    public String getInstructorUsername() { return instructorUsername; }

    // Setters
    public void setCourseName(String name)           { this.courseName = name; }
    public void setCredit(int credit)                { this.credit = credit; }
    public void setQuota(int quota)                  { this.quota = quota; }
    public void setInstructorUsername(String instr)  { this.instructorUsername = instr; }

    public String toFileString() {
        return courseCode + "," + courseName + "," + credit + "," + quota + "," + instructorUsername;
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-35s | Credits: %d | Quota: %-3d | Instructor: %s",
                courseCode, courseName, credit, quota, instructorUsername);
    }
}
