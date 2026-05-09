# University Automation App — Java Console Application

## Demo Accounts
| Username   | Password  | Role       |
|------------|-----------|------------|
| john_doe   | pass123   | Student    |
| jane_s     | pass456   | Student    |
| prof_ali   | teach789  | Instructor |
| admin      | admin123  | Admin      |

## Student Features
1. **My Profile**      — View student ID, name, department, year
2. **Available Courses** — Browse all courses with quota/enrollment info; enroll directly
3. **My Courses**      — View enrolled courses; drop a course
4. **My Grades**       — See midterm, final, average and letter grade per course
5. **My Transcript**   — Full academic record with weighted GPA (4.0 scale)

## Grade Weights
- Midterm : 40%
- Final   : 60%

## Letter Grade Scale
| Avg  | Grade | Points |
|------|-------|--------|
| ≥ 90 | AA    | 4.0    |
| ≥ 85 | BA    | 3.5    |
| ≥ 80 | BB    | 3.0    |
| ≥ 75 | CB    | 2.5    |
| ≥ 70 | CC    | 2.0    |
| ≥ 60 | DC    | 1.5    |
| ≥ 50 | DD    | 1.0    |
| ≥ 40 | FD    | 0.0    |
| < 40 | FF    | 0.0    |

## How to Run

### Compile
```bash
mkdir -p out
javac -d out src/*.java
```

### Run
```bash
java -cp out UniversityAutomationApp
```

> Data is persisted to the `data/` folder as plain text files.
> The app auto-seeds demo data on first run.

## Class Structure (matches UML)
```
UniversityAutomationApp  — Entry point & console UI
DataStore                — Central data management (load/save/query)
User                     — Credentials and role
StudentProfile           — Student academic info
Course                   — Course details and quota
Enrollment               — Student ↔ Course link
GradeRecord              — Midterm, final, average, letter grade
```
