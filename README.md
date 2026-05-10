# 🎓 University Automation App

A full-featured **Student Information System** built with Java (console) and a modern **Web UI**. Supports three roles — **Student**, **Instructor**, and **Admin** — with course management, enrollment, grade tracking, and GPA calculation.

---

## 📸 Screenshots

### Web Version — Login
![Login](https://img.shields.io/badge/Dark_Theme-Modern_UI-6366f1?style=for-the-badge)

### Web Version — Student Dashboard
![Dashboard](https://img.shields.io/badge/Role--Based-Dashboards-22c55e?style=for-the-badge)

---

## ✨ Features

### 👨‍🎓 Student
| Feature | Description |
|---------|-------------|
| **My Profile** | View student ID, name, department, year, and GPA |
| **Available Courses** | Browse all courses with quota info; enroll directly |
| **My Courses** | View enrolled courses; drop a course |
| **My Grades** | See midterm, final, weighted average, and letter grade |
| **Transcript** | Full academic record with cumulative GPA (4.0 scale) |

### 👨‍🏫 Instructor
| Feature | Description |
|---------|-------------|
| **My Courses** | View assigned courses with enrollment counts |
| **Enrollments** | See which students are enrolled per course |
| **Grade Entry** | Enter or update midterm and final scores |

### 🔑 Admin
| Feature | Description |
|---------|-------------|
| **All Users** | View complete user directory with role badges |
| **All Courses** | Browse every course with enrollment status |
| **All Students** | Student directory with GPA overview |

---

## 🔑 Demo Accounts

| Username | Password | Role |
|----------|----------|------|
| `john_doe` | `pass123` | Student |
| `jane_s` | `pass456` | Student |
| `prof_ali` | `teach789` | Instructor |
| `prof_cem` | `teach000` | Instructor |
| `admin` | `admin123` | Admin |

---

## 🚀 How to Run

### Option 1: Web Version (Recommended)

Simply open in any browser — **no server required**:

```
UniversityAutomationApp/web/index.html
```

> Data is stored in your browser's `localStorage` and persists across sessions.

### Option 2: Java Console Version

**Compile:**
```bash
cd UniversityAutomationApp
mkdir -p out
javac -d out src/*.java
```

**Run:**
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
    │   ├── UniversityAutomationApp.java   # Entry point & console UI
    │   ├── DataStore.java                 # Data management (load/save/query)
    │   ├── User.java                      # User credentials and role
    │   ├── StudentProfile.java            # Student academic info
    │   ├── Course.java                    # Course details and quota
    │   ├── Enrollment.java                # Student ↔ Course link
    │   └── GradeRecord.java               # Midterm, final, average, letter grade
    ├── data/                       # Persistent data (text files)
    │   ├── users.txt
    │   ├── students.txt
    │   ├── courses.txt
    │   ├── enrollments.txt
    │   └── grades.txt
    └── web/                        # Web UI (standalone)
        ├── index.html
        ├── style.css
        ├── data.js                        # Data layer (mirrors Java DataStore)
        └── app.js                         # UI controller
```

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| **Console App** | Java (JDK 11+) |
| **Web Frontend** | HTML5, CSS3, Vanilla JavaScript |
| **Data Storage** | Text files (Java) / localStorage (Web) |
| **Design** | Dark theme, Inter font, responsive layout |

---

## 📄 License

This project is for educational purposes.

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/KHALEDNOAMAN">KHALEDNOAMAN</a>
</p>
