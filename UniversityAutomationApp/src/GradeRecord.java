public class GradeRecord {
    private String studentUsername;
    private String courseCode;
    private double midterm;
    private double finalExam;

    public GradeRecord(String studentUsername, String courseCode, double midterm, double finalExam) {
        this.studentUsername = studentUsername;
        this.courseCode      = courseCode;
        this.midterm         = midterm;
        this.finalExam       = finalExam;
    }

    // Getters
    public String getStudentUsername() { return studentUsername; }
    public String getCourseCode()      { return courseCode; }
    public double getMidterm()         { return midterm; }
    public double getFinalExam()       { return finalExam; }

    // Setters
    public void setMidterm(double midterm)     { this.midterm = midterm; }
    public void setFinalExam(double finalExam) { this.finalExam = finalExam; }

    /** Weighted average: 40% midterm + 60% final */
    public double calculateAverage() {
        return (midterm * 0.4) + (finalExam * 0.6);
    }

    /** Letter grade based on 100-point scale */
    public String getLetterGrade() {
        double avg = calculateAverage();
        if (avg >= 90) return "AA";
        if (avg >= 85) return "BA";
        if (avg >= 80) return "BB";
        if (avg >= 75) return "CB";
        if (avg >= 70) return "CC";
        if (avg >= 60) return "DC";
        if (avg >= 50) return "DD";
        if (avg >= 40) return "FD";
        return "FF";
    }

    /** Grade point (4.0 scale) for GPA calculation */
    public double getGradePoint() {
        String letter = getLetterGrade();
        switch (letter) {
            case "AA": return 4.0;
            case "BA": return 3.5;
            case "BB": return 3.0;
            case "CB": return 2.5;
            case "CC": return 2.0;
            case "DC": return 1.5;
            case "DD": return 1.0;
            default:   return 0.0;
        }
    }

    public String toFileString() {
        return studentUsername + "," + courseCode + "," + midterm + "," + finalExam;
    }

    @Override
    public String toString() {
        return String.format("Course: %-10s | Midterm: %5.1f | Final: %5.1f | Avg: %5.1f | Grade: %s",
                courseCode, midterm, finalExam, calculateAverage(), getLetterGrade());
    }
}
