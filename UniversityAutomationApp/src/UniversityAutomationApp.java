import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UniversityAutomationApp extends JFrame {

    // ─────────────── UML Defined Fields ───────────────
    private User currentUser;
    private JComboBox<String> studentUserCombo;
    private JComboBox<String> instructorCombo;
    
    // Additional fields for UI management
    private DataStore dataStore;
    private JPanel mainContainer;
    private CardLayout cardLayout;

    // ─────────────── Main & Initialization ───────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UniversityAutomationApp().setVisible(true);
        });
    }

    public UniversityAutomationApp() {
        dataStore = new DataStore();
        dataStore.initialize();

        setTitle("University Automation System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        add(mainContainer, BorderLayout.CENTER);

        showLoginPanel();
    }

    // ─────────────── UML Method: showLoginPanel ───────────────
    public void showLoginPanel() {
        // Remove old panels to prevent accumulation on logout/login cycles
        mainContainer.removeAll();

        JPanel loginPanel = new JPanel(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(200, 200, 200, 200));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        centerPanel.add(new JLabel("Username:"));
        centerPanel.add(usernameField);
        centerPanel.add(new JLabel("Password:"));
        centerPanel.add(passwordField);
        centerPanel.add(new JLabel(""));
        centerPanel.add(loginButton);

        loginPanel.add(new JLabel("Student Information System - Login", JLabel.CENTER), BorderLayout.NORTH);
        loginPanel.add(centerPanel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                currentUser = dataStore.authenticate(username, password);
                if (currentUser != null) {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this,
                            "Welcome " + currentUser.getFullName() + "!");
                    showDashboard();
                } else {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this,
                            "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainContainer.add(loginPanel, "LOGIN");
        cardLayout.show(mainContainer, "LOGIN");
    }

    // ─────────────── UML Method: showDashboard ───────────────
    public void showDashboard() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());

        // Header
        String roleDisplay = currentUser.getRole().substring(0, 1).toUpperCase()
                           + currentUser.getRole().substring(1);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(new JLabel(" Logged in as: " + currentUser.getFullName()
                + " (" + roleDisplay + ")"), BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentUser = null;
                showLoginPanel();
            }
        });
        headerPanel.add(logoutButton, BorderLayout.EAST);
        dashboardPanel.add(headerPanel, BorderLayout.NORTH);

        // Role-based Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        if (currentUser.getRole().equals("admin")) {
            tabbedPane.addTab("Add User", createUsersPanel());
            tabbedPane.addTab("Add Student Profile", createStudentsPanel());
            tabbedPane.addTab("Add Course", createCoursesPanel());
            tabbedPane.addTab("System Reports", createReportsPanel());
        } else if (currentUser.getRole().equals("advisor")) {
            // Advisor = Instructor access + elevated admin-level access
            tabbedPane.addTab("My Courses", createInstructorCoursesPanel());
            tabbedPane.addTab("Enter Grades", createGradeEntryPanel());
            tabbedPane.addTab("All Students", createAdvisorStudentsPanel());
            tabbedPane.addTab("System Reports", createReportsPanel());
        } else if (currentUser.getRole().equals("instructor")) {
            tabbedPane.addTab("My Courses", createInstructorCoursesPanel());
            tabbedPane.addTab("Enter Grades", createGradeEntryPanel());
        } else if (currentUser.getRole().equals("student")) {
            tabbedPane.addTab("Available Courses", createAvailableCoursesPanel());
            tabbedPane.addTab("My Courses", createMyCoursesPanel());
            tabbedPane.addTab("Transcript & Grades", createTranscriptPanel());
        }

        dashboardPanel.add(tabbedPane, BorderLayout.CENTER);
        mainContainer.add(dashboardPanel, "DASHBOARD");
        cardLayout.show(mainContainer, "DASHBOARD");
    }

    // ==========================================
    // ADMIN PANELS
    // ==========================================

    // ─────────────── UML Method: createUsersPanel ───────────────
    public JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JComboBox<String> cboRole = new JComboBox<>(new String[]{"student", "instructor", "advisor", "admin"});
        JTextField txtFullName = new JTextField();
        JTextField txtRefId = new JTextField();
        JButton btnAdd = new JButton("Add User");

        form.add(new JLabel("Username:")); form.add(txtUsername);
        form.add(new JLabel("Password:")); form.add(txtPassword);
        form.add(new JLabel("Role:")); form.add(cboRole);
        form.add(new JLabel("Full Name:")); form.add(txtFullName);
        form.add(new JLabel("Ref ID:")); form.add(txtRefId);
        form.add(new JLabel("")); form.add(btnAdd);
        
        panel.add(form, BorderLayout.NORTH);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dataStore.findUser(txtUsername.getText()) != null) {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "User already exists!");
                    return;
                }
                dataStore.users.add(new User(
                    txtUsername.getText(),
                    new String(txtPassword.getPassword()),
                    (String)cboRole.getSelectedItem(),
                    txtFullName.getText(),
                    txtRefId.getText()
                ));
                dataStore.saveUsers();
                JOptionPane.showMessageDialog(UniversityAutomationApp.this, "User added successfully.");
                
                // Refresh combos if they are instantiated
                if(studentUserCombo != null) studentUserCombo.addItem(txtUsername.getText());
                if(instructorCombo != null && "instructor".equals(cboRole.getSelectedItem())) {
                    instructorCombo.addItem(txtUsername.getText());
                }
            }
        });

        return panel;
    }

    // ─────────────── UML Method: createStudentsPanel ───────────────
    public JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtDept = new JTextField();
        JTextField txtYear = new JTextField();
        
        // Use UML designated field
        studentUserCombo = new JComboBox<>();
        for(User u : dataStore.users) {
            if(u.getRole().equals("student")) studentUserCombo.addItem(u.getUsername());
        }

        JButton btnAdd = new JButton("Add Student Profile");

        form.add(new JLabel("Student ID:")); form.add(txtId);
        form.add(new JLabel("Full Name:")); form.add(txtName);
        form.add(new JLabel("Department:")); form.add(txtDept);
        form.add(new JLabel("Year:")); form.add(txtYear);
        form.add(new JLabel("Username:")); form.add(studentUserCombo);
        form.add(new JLabel("")); form.add(btnAdd);

        panel.add(form, BorderLayout.NORTH);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int year = Integer.parseInt(txtYear.getText());
                    dataStore.students.add(new StudentProfile(
                        txtId.getText(), txtName.getText(),
                        txtDept.getText(), year, (String)studentUserCombo.getSelectedItem()
                    ));
                    dataStore.saveStudents();
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Student Profile added.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Year must be a number!");
                }
            }
        });
        return panel;
    }

    // ─────────────── UML Method: createCoursesPanel ───────────────
    public JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtCode = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtCredit = new JTextField();
        JTextField txtQuota = new JTextField();
        
        // Use UML designated field
        instructorCombo = new JComboBox<>();
        for(User u : dataStore.users) {
            if(u.getRole().equals("instructor") || u.getRole().equals("advisor"))
                instructorCombo.addItem(u.getUsername());
        }

        JButton btnAdd = new JButton("Add Course");

        form.add(new JLabel("Course Code:")); form.add(txtCode);
        form.add(new JLabel("Course Name:")); form.add(txtName);
        form.add(new JLabel("Credits:")); form.add(txtCredit);
        form.add(new JLabel("Quota:")); form.add(txtQuota);
        form.add(new JLabel("Instructor:")); form.add(instructorCombo);
        form.add(new JLabel("")); form.add(btnAdd);

        panel.add(form, BorderLayout.NORTH);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int credit = Integer.parseInt(txtCredit.getText());
                    int quota = Integer.parseInt(txtQuota.getText());
                    dataStore.courses.add(new Course(
                        txtCode.getText(), txtName.getText(),
                        credit, quota, (String)instructorCombo.getSelectedItem()
                    ));
                    dataStore.saveCourses();
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Course added.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Credit and Quota must be numbers!");
                }
            }
        });
        return panel;
    }

    // ─────────────── UML Method: createReportsPanel ───────────────
    public JPanel createReportsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        
        String[] userCols = {"Username", "Role", "Name", "Ref ID"};
        DefaultTableModel userModel = new DefaultTableModel(userCols, 0);
        for (User u : dataStore.users) {
            userModel.addRow(new Object[]{u.getUsername(), u.getRole(), u.getFullName(), u.getReferenceId()});
        }
        JTable userTable = new JTable(userModel);
        panel.add(new JScrollPane(userTable));

        String[] courseCols = {"Code", "Name", "Credits", "Quota", "Instructor"};
        DefaultTableModel courseModel = new DefaultTableModel(courseCols, 0);
        for (Course c : dataStore.courses) {
            courseModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getCredit(), c.getQuota(), c.getInstructorUsername()});
        }
        JTable courseTable = new JTable(courseModel);
        panel.add(new JScrollPane(courseTable));

        return panel;
    }

    // ==========================================
    // ADVISOR PANELS
    // ==========================================

    // ─────────────── Advisor: All Students with GPA ───────────────
    public JPanel createAdvisorStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("All Students — Advisor View", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"Student ID", "Full Name", "Department", "Year", "GPA", "Standing"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        for (StudentProfile sp : dataStore.students) {
            double gpa = dataStore.calculateGPA(sp.getUsername());
            String standing;
            if      (gpa >= 3.50) standing = "Dean's List";
            else if (gpa >= 2.00) standing = "Good Standing";
            else if (gpa >= 1.00) standing = "Academic Probation";
            else                  standing = "Academic Suspension";
            model.addRow(new Object[]{
                sp.getStudentId(), sp.getFullName(),
                sp.getDepartment(), sp.getYear(),
                String.format("%.2f", gpa), standing
            });
        }

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Detail: show full transcript when a student row is selected
        JTextArea detailArea = new JTextArea(6, 40);
        detailArea.setEditable(false);
        detailArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailArea.setBorder(BorderFactory.createTitledBorder("Grade Detail"));
        panel.add(new JScrollPane(detailArea), BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                String studentId = model.getValueAt(row, 0).toString();
                StudentProfile sp = null;
                for (StudentProfile p : dataStore.students)
                    if (p.getStudentId().equals(studentId)) { sp = p; break; }
                if (sp == null) return;
                StringBuilder sb = new StringBuilder();
                sb.append("Student: ").append(sp.getFullName())
                  .append(" | ID: ").append(sp.getStudentId())
                  .append(" | Dept: ").append(sp.getDepartment()).append("\n");
                sb.append(String.format("%-12s %-8s %-8s %-8s %s\n",
                          "Course", "Midterm", "Final", "Avg", "Grade"));
                sb.append("─────────────────────────────────────────────\n");
                for (GradeRecord g : dataStore.getGradesByStudent(sp.getUsername())) {
                    sb.append(String.format("%-12s %-8.1f %-8.1f %-8.1f %s\n",
                        g.getCourseCode(), g.getMidterm(), g.getFinalExam(),
                        g.calculateAverage(), g.getLetterGrade()));
                }
                detailArea.setText(sb.toString());
            }
        });

        return panel;
    }

    // ==========================================
    // INSTRUCTOR PANELS
    // ==========================================

    // ─────────────── UML Method: createInstructorCoursesPanel ───────────────
    public JPanel createInstructorCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] courseCols = {"Course Code", "Course Name", "Credits", "Enrolled/Quota"};
        DefaultTableModel courseModel = new DefaultTableModel(courseCols, 0);
        List<Course> instructorCourses = dataStore.getCoursesByInstructor(currentUser.getUsername());
        for (Course c : instructorCourses) {
            int enrolled = dataStore.countEnrollmentForCourse(c.getCourseCode());
            courseModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getCredit(), enrolled + "/" + c.getQuota()});
        }
        JTable courseTable = new JTable(courseModel);
        panel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        return panel;
    }

    // ─────────────── UML Method: createGradeEntryPanel ───────────────
    public JPanel createGradeEntryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel topGradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topGradePanel.add(new JLabel("Select Course:"));
        JComboBox<String> cboCourses = new JComboBox<>();
        List<Course> instructorCourses = dataStore.getCoursesByInstructor(currentUser.getUsername());
        for (Course c : instructorCourses) {
            cboCourses.addItem(c.getCourseCode());
        }
        topGradePanel.add(cboCourses);
        panel.add(topGradePanel, BorderLayout.NORTH);

        String[] studentCols = {"Student Username", "Midterm", "Final Exam"};
        DefaultTableModel studentModel = new DefaultTableModel(studentCols, 0);
        JTable studentTable = new JTable(studentModel);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel gradeEntryPanel = new JPanel(new FlowLayout());
        JTextField txtMidterm = new JTextField(5);
        JTextField txtFinal = new JTextField(5);
        JButton btnSaveGrade = new JButton("Save Grade");
        gradeEntryPanel.add(new JLabel("Midterm:")); gradeEntryPanel.add(txtMidterm);
        gradeEntryPanel.add(new JLabel("Final:")); gradeEntryPanel.add(txtFinal);
        gradeEntryPanel.add(btnSaveGrade);
        panel.add(gradeEntryPanel, BorderLayout.SOUTH);

        cboCourses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                studentModel.setRowCount(0);
                String selectedCourse = (String)cboCourses.getSelectedItem();
                if (selectedCourse != null) {
                    List<Enrollment> enrolls = dataStore.getEnrollmentsByCourse(selectedCourse);
                    for (Enrollment en : enrolls) {
                        GradeRecord g = dataStore.findGrade(en.getStudentUsername(), selectedCourse);
                        if (g != null) {
                            studentModel.addRow(new Object[]{en.getStudentUsername(), g.getMidterm(), g.getFinalExam()});
                        } else {
                            studentModel.addRow(new Object[]{en.getStudentUsername(), "", ""});
                        }
                    }
                }
            }
        });

        studentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                    int row = studentTable.getSelectedRow();
                    txtMidterm.setText(studentModel.getValueAt(row, 1).toString());
                    txtFinal.setText(studentModel.getValueAt(row, 2).toString());
                }
            }
        });

        btnSaveGrade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = studentTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Select a student first!");
                    return;
                }
                String studentUser = studentModel.getValueAt(row, 0).toString();
                String courseCode = (String)cboCourses.getSelectedItem();
                try {
                    double midterm = Double.parseDouble(txtMidterm.getText());
                    double finalEx = Double.parseDouble(txtFinal.getText());
                    dataStore.upsertGrade(studentUser, courseCode, midterm, finalEx);
                    studentModel.setValueAt(midterm, row, 1);
                    studentModel.setValueAt(finalEx, row, 2);
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Grade saved successfully.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Please enter valid numeric grades.");
                }
            }
        });

        if (cboCourses.getItemCount() > 0) {
            cboCourses.setSelectedIndex(0);
        }

        return panel;
    }

    // ==========================================
    // STUDENT PANELS
    // ==========================================

    // ─────────────── UML Method: createAvailableCoursesPanel ───────────────
    public JPanel createAvailableCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] availCols = {"Code", "Name", "Credits", "Quota Left"};
        DefaultTableModel availModel = new DefaultTableModel(availCols, 0);
        for (Course c : dataStore.courses) {
            int left = c.getQuota() - dataStore.countEnrollmentForCourse(c.getCourseCode());
            availModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getCredit(), left});
        }
        JTable availTable = new JTable(availModel);
        panel.add(new JScrollPane(availTable), BorderLayout.CENTER);
        
        JPanel enrollBtnPanel = new JPanel(new FlowLayout());
        JButton btnEnroll = new JButton("Enroll Selected Course");
        enrollBtnPanel.add(btnEnroll);
        panel.add(enrollBtnPanel, BorderLayout.SOUTH);

        btnEnroll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = availTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Select a course to enroll.");
                    return;
                }
                String courseCode = availModel.getValueAt(row, 0).toString();
                String msg = dataStore.enrollStudent(currentUser.getUsername(), courseCode);
                JOptionPane.showMessageDialog(UniversityAutomationApp.this, msg);
                // In a full dynamic app, we'd trigger a refresh of My Courses here.
                // For simplicity, they can just click the tab to see updates next time it is built.
            }
        });

        return panel;
    }

    // ─────────────── UML Method: createMyCoursesPanel ───────────────
    public JPanel createMyCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] myCols = {"Code", "Name", "Credits"};
        DefaultTableModel myModel = new DefaultTableModel(myCols, 0);
        
        List<Enrollment> myEnrolls = dataStore.getEnrollmentsByStudent(currentUser.getUsername());
        for (Enrollment e : myEnrolls) {
            Course c = dataStore.findCourse(e.getCourseCode());
            if (c != null) {
                myModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName(), c.getCredit()});
            }
        }

        JTable myTable = new JTable(myModel);
        panel.add(new JScrollPane(myTable), BorderLayout.CENTER);
        
        JPanel dropBtnPanel = new JPanel(new FlowLayout());
        JButton btnDrop = new JButton("Drop Selected Course");
        dropBtnPanel.add(btnDrop);
        panel.add(dropBtnPanel, BorderLayout.SOUTH);

        btnDrop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = myTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(UniversityAutomationApp.this, "Select a course to drop.");
                    return;
                }
                String courseCode = myModel.getValueAt(row, 0).toString();
                String msg = dataStore.removeEnrollment(currentUser.getUsername(), courseCode);
                JOptionPane.showMessageDialog(UniversityAutomationApp.this, msg);
                myModel.removeRow(row);
            }
        });

        return panel;
    }

    // ─────────────── UML Method: createTranscriptPanel ───────────────
    public JPanel createTranscriptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        StudentProfile profile = dataStore.findStudentProfileByUsername(currentUser.getUsername());
        if (profile != null) {
            infoPanel.add(new JLabel(" Student ID: " + profile.getStudentId() + " | Department: " + profile.getDepartment()));
        } else {
            infoPanel.add(new JLabel(" Profile not found."));
        }
        double gpa = dataStore.calculateGPA(currentUser.getUsername());
        infoPanel.add(new JLabel(" Overall GPA: " + String.format("%.2f", gpa)));
        panel.add(infoPanel, BorderLayout.NORTH);

        String[] gradeCols = {"Course Code", "Midterm", "Final", "Average", "Letter Grade"};
        DefaultTableModel gradeModel = new DefaultTableModel(gradeCols, 0);
        List<GradeRecord> studentGrades = dataStore.getGradesByStudent(currentUser.getUsername());
        for (GradeRecord g : studentGrades) {
            gradeModel.addRow(new Object[]{g.getCourseCode(), g.getMidterm(), g.getFinalExam(), g.calculateAverage(), g.getLetterGrade()});
        }
        JTable gradeTable = new JTable(gradeModel);
        panel.add(new JScrollPane(gradeTable), BorderLayout.CENTER);

        return panel;
    }
}
