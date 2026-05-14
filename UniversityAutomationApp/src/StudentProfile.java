public class StudentProfile {
    private String studentId;
    private String fullName;
    private String department;
    private int year;
    private int credit;       // total credits earned (UML required field)
    private String username;

    // Full constructor (with credit)
    public StudentProfile(String studentId, String fullName, String department,
                          int year, int credit, String username) {
        this.studentId  = studentId;
        this.fullName   = fullName;
        this.department = department;
        this.year       = year;
        this.credit     = credit;
        this.username   = username;
    }

    // Backward-compatible constructor (credit defaults to 0)
    public StudentProfile(String studentId, String fullName, String department,
                          int year, String username) {
        this(studentId, fullName, department, year, 0, username);
    }

    // Getters
    public String getStudentId()   { return studentId; }
    public String getFullName()    { return fullName; }
    public String getDepartment()  { return department; }
    public int    getYear()        { return year; }
    public int    getCredit()      { return credit; }
    public String getUsername()    { return username; }

    // Setters
    public void setFullName(String fullName)    { this.fullName = fullName; }
    public void setDepartment(String dept)      { this.department = dept; }
    public void setYear(int year)               { this.year = year; }
    public void setCredit(int credit)           { this.credit = credit; }

    public String toFileString() {
        return studentId + "," + fullName + "," + department + "," + year + "," + credit + "," + username;
    }

    @Override
    public String toString() {
        return String.format("ID: %-10s | Name: %-25s | Dept: %-20s | Year: %d | Credits: %d",
                studentId, fullName, department, year, credit);
    }
}
