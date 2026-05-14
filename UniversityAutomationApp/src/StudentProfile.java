public class StudentProfile {
    private String studentId;
    private String fullName;
    private String department;
    private int year;
    private String username;

    public StudentProfile(String studentId, String fullName, String department, int year, String username) {
        this.studentId  = studentId;
        this.fullName   = fullName;
        this.department = department;
        this.year       = year;
        this.username   = username;
    }

    // Getters
    public String getStudentId()   { return studentId; }
    public String getFullName()    { return fullName; }
    public String getDepartment()  { return department; }
    public int    getYear()        { return year; }
    public String getUsername()    { return username; }

    // Setters
    public void setFullName(String fullName)    { this.fullName = fullName; }
    public void setDepartment(String dept)      { this.department = dept; }
    public void setYear(int year)               { this.year = year; }

    public String toFileString() {
        return studentId + "," + fullName + "," + department + "," + year + "," + username;
    }

    @Override
    public String toString() {
        return String.format("ID: %-10s | Name: %-25s | Dept: %-20s | Year: %d",
                studentId, fullName, department, year);
    }
}

