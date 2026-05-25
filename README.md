# ðŸŽ“ University Automation App

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-6DB33F?style=for-the-badge&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

> **A Java-based university automation system for managing students, courses, instructors, and academic scheduling â€” built with core OOP principles.**

---

## ðŸŽ¯ Overview

University Automation App is a command-line application that streamlines university operations including student enrollment, course management, instructor assignments, and schedule generation. Designed to demonstrate clean object-oriented architecture and real-world software design patterns.

## âœ¨ Features

- ðŸ‘¨â€ðŸŽ“ **Student Management** â€” Add, update, delete, and search student records
- ðŸ“š **Course Registration** â€” Enroll/drop students from courses with capacity checks
- ðŸ‘¨â€ðŸ« **Instructor Assignment** â€” Assign instructors to courses and manage workloads
- ðŸ“… **Schedule Management** â€” Generate and view class schedules
- ðŸ” **Search & Filter** â€” Query students and courses by various criteria
- ðŸ’¾ **Data Persistence** â€” Save and load data between sessions

## ðŸ› ï¸ Tech Stack

| Technology | Purpose |
|-----------|---------|
| Java 11+ | Core language |
| OOP | Architecture (inheritance, polymorphism, encapsulation) |
| Collections Framework | Data management |
| File I/O | Data persistence |

## ðŸš€ Getting Started

### Prerequisites
```bash
Java JDK 11 or later
```

### Build & Run
```bash
# Clone the repository
git clone https://github.com/KHALEDNOAMAN/UniversityAutomationApp.git
cd UniversityAutomationApp

# Compile
javac -d bin src/*.java

# Run
java -cp bin Main
```

## ðŸ—ï¸ Architecture

```
UniversityAutomationApp/
â”œâ”€â”€ src/
â”‚   â”œâ”€â”€ Main.java              # Entry point
â”‚   â”œâ”€â”€ Student.java           # Student model
â”‚   â”œâ”€â”€ Course.java            # Course model
â”‚   â”œâ”€â”€ Instructor.java        # Instructor model
â”‚   â”œâ”€â”€ Schedule.java          # Scheduling logic
â”‚   â””â”€â”€ UniversitySystem.java  # Core system controller
â”œâ”€â”€ data/                      # Persistent data files
â””â”€â”€ README.md
```

### Design Principles
- **Single Responsibility** â€” Each class handles one concern
- **Encapsulation** â€” Private fields with public getters/setters
- **Inheritance** â€” Shared behavior through class hierarchies
- **Polymorphism** â€” Method overriding for flexible behavior

## ðŸ“„ License

This project is licensed under the MIT License.

## ðŸ‘¤ Author

**Khaled Noaman** â€” Computer Engineering Student at Istanbul Arel University

- [GitHub](https://github.com/KhaledNoaman)
- [LinkedIn](https://www.linkedin.com/in/khalednoaman1/)
