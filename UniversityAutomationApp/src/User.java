public class User {
    private String username;
    private String password;
    private String role; // "student", "instructor", "admin"
    private String fullName;
    private String referenceId;

    public User(String username, String password, String role, String fullName, String referenceId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.referenceId = referenceId;
    }

    // Getters
    public String getUsername()    { return username; }
    public String getPassword()    { return password; }
    public String getRole()        { return role; }
    public String getFullName()    { return fullName; }
    public String getReferenceId() { return referenceId; }

    // Setters
    public void setPassword(String password)    { this.password = password; }
    public void setFullName(String fullName)    { this.fullName = fullName; }
    public void setReferenceId(String refId)    { this.referenceId = refId; }

    public String toFileString() {
        return username + "," + password + "," + role + "," + fullName + "," + referenceId;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "', fullName='" + fullName + "'}";
    }
}
