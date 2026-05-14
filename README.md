# 🎓 University Automation App

A full-featured **Student Information System** built with Java Swing. Supports four roles — **Student**, **Instructor**, **Advisor**, and **Admin** — with course management, enrollment, grade tracking, and GPA calculation.

---

## ✨ Features

### 👨‍🎓 Student
| Feature | Description |
|---------|-------------|
| **Available Courses** | Browse all courses with quota info; enroll directly |
| **My Courses** | View enrolled courses; drop a course |
| **Transcript & Grades** | Full academic record with GPA (4.0 scale) |

### 👨‍🏫 Instructor
| Feature | Description |
|---------|-------------|
| **My Courses** | View assigned courses with enrollment counts |
| **Enter Grades** | Select course, view enrolled students, enter/update midterm and final scores |

### 🎓 Advisor (elevated access)
| Feature | Description |
|---------|-------------|
| **My Courses** | View assigned courses with enrollment counts |
| **Enter Grades** | Enter/update midterm and final scores |
| **All Students** | Full student directory with GPA and academic standing; click any student to view grade details |
| **System Reports** | View all users and all courses in the system |

### 🔑 Admin
| Feature | Description |
|---------|-------------|
| **Add User** | Create new users with role assignment |
| **Add Student Profile** | Create student academic profiles |
| **Add Course** | Create courses and assign instructors |
| **System Reports** | View all users and all courses |

---

## 🔑 Demo Accounts

| Username | Password | Role |
|----------|----------|------|
| `john_doe` | `pass123` | Student |
| `jane_s` | `pass456` | Student |
| `khaled_student` | `khaled123` | Student |
| `khaled_admin` | `khaled456` | Admin |
| `prof_ali` | `teach789` | Instructor |
| `prof_cem` | `teach000` | Instructor |
| `prof_nazife` | `nazife789` | Advisor |
| `admin` | `admin123` | Admin |

---

## 🚀 How to Run

### Compile
```bash
cd UniversityAutomationApp
javac --release 21 -d out src\UniversityAutomationApp.java src\DataStore.java src\User.java src\StudentProfile.java src\Course.java src\Enrollment.java src\GradeRecord.java
```

### Run
```bash
java -cp out UniversityAutomationApp
```

> Data is persisted to the `data/` folder as plain text files. Demo data is auto-seeded on first run.

---

## 📊 Grading System

### Grade Weights
- **Midterm**: 40%
- **Final**: 60%

### Letter Grade Scale

| Average | Grade | Points |
|---------|-------|--------|
| ≥ 90 | AA | 4.0 |
| ≥ 85 | BA | 3.5 |
| ≥ 80 | BB | 3.0 |
| ≥ 75 | CB | 2.5 |
| ≥ 70 | CC | 2.0 |
| ≥ 60 | DC | 1.5 |
| ≥ 50 | DD | 1.0 |
| ≥ 40 | FD | 0.0 |
| < 40 | FF | 0.0 |

### GPA Standing
| GPA | Standing |
|-----|----------|
| ≥ 3.50 | Dean's List |
| ≥ 2.00 | Good Standing |
| ≥ 1.00 | Academic Probation |
| < 1.00 | Academic Suspension |

---

## 🏗️ Project Structure

```
UniversityAutomationApp/
├── README.md
├── .gitignore
└── UniversityAutomationApp/
    ├── src/                        # Java source code
    │   ├── UniversityAutomationApp.java   # Entry point & Swing UI
    │   ├── DataStore.java                 # Data management (load/save/query)
    │   ├── User.java                      # User credentials and role
    │   ├── StudentProfile.java            # Student academic info
    │   ├── Course.java                    # Course details and quota
    │   ├── Enrollment.java                # Student ↔ Course link
    │   └── GradeRecord.java               # Midterm, final, average, letter grade
    └── data/                       # Persistent data (text files)
        ├── users.txt
        ├── students.txt
        ├── courses.txt
        ├── enrollments.txt
        └── grades.txt
```

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| **Application** | Java Swing (JDK 21+) |
| **Data Storage** | Text files with CSV format |
| **Architecture** | MVC-inspired (DataStore + UI panels) |

---

## 📄 License

This project is for educational purposes.

---

<p align="center">
  Made by <a href="https://github.com/KHALEDNOAMAN">KHALEDNOAMAN</a>
</p>
