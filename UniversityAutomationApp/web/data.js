// ═══════════════════════════════════════════════════════
//  DATA LAYER — mirrors Java DataStore
// ═══════════════════════════════════════════════════════

const DataStore = {
  users: [],
  students: [],
  courses: [],
  enrollments: [],
  grades: [],

  initialize() {
    const saved = localStorage.getItem('uniapp_data');
    if (saved) {
      const d = JSON.parse(saved);
      this.users = d.users || [];
      this.students = d.students || [];
      this.courses = d.courses || [];
      this.enrollments = d.enrollments || [];
      this.grades = d.grades || [];
    } else {
      this.seedDemo();
    }
  },

  save() {
    localStorage.setItem('uniapp_data', JSON.stringify({
      users: this.users, students: this.students, courses: this.courses,
      enrollments: this.enrollments, grades: this.grades
    }));
  },

  seedDemo() {
    this.users = [
      { username:'admin', password:'admin123', role:'admin', fullName:'Admin User', refId:'A001' },
      { username:'john_doe', password:'pass123', role:'student', fullName:'John Doe', refId:'S001' },
      { username:'jane_s', password:'pass456', role:'student', fullName:'Jane Smith', refId:'S002' },
      { username:'prof_ali', password:'teach789', role:'instructor', fullName:'Prof. Ali Kaya', refId:'I001' },
      { username:'prof_cem', password:'teach000', role:'instructor', fullName:'Prof. Cem Demir', refId:'I002' }
    ];
    this.students = [
      { studentId:'S001', fullName:'John Doe', department:'Computer Science', year:2, username:'john_doe' },
      { studentId:'S002', fullName:'Jane Smith', department:'Mathematics', year:3, username:'jane_s' }
    ];
    this.courses = [
      { code:'CS101', name:'Introduction to Programming', credit:3, quota:30, instructor:'prof_ali' },
      { code:'CS201', name:'Data Structures', credit:3, quota:25, instructor:'prof_ali' },
      { code:'MATH101', name:'Calculus I', credit:4, quota:40, instructor:'prof_cem' },
      { code:'CS301', name:'Algorithms', credit:3, quota:20, instructor:'prof_ali' },
      { code:'MATH201', name:'Linear Algebra', credit:3, quota:35, instructor:'prof_cem' }
    ];
    this.enrollments = [
      { student:'john_doe', course:'CS101' },
      { student:'john_doe', course:'CS201' },
      { student:'john_doe', course:'MATH101' },
      { student:'jane_s', course:'MATH101' },
      { student:'jane_s', course:'MATH201' }
    ];
    this.grades = [
      { student:'john_doe', course:'CS101', midterm:78, final:85 },
      { student:'john_doe', course:'MATH101', midterm:65, final:72 },
      { student:'jane_s', course:'MATH101', midterm:90, final:95 }
    ];
    this.save();
  },

  // Auth
  authenticate(u, p) {
    return this.users.find(x => x.username === u && x.password === p) || null;
  },

  // Finders
  findUser(u) { return this.users.find(x => x.username === u); },
  findStudent(u) { return this.students.find(x => x.username === u); },
  findCourse(c) { return this.courses.find(x => x.code.toLowerCase() === c.toLowerCase()); },

  // Enrollment
  getEnrollmentsByStudent(u) { return this.enrollments.filter(x => x.student === u); },
  getEnrollmentsByCourse(c) { return this.enrollments.filter(x => x.course.toLowerCase() === c.toLowerCase()); },
  countEnrolled(c) { return this.getEnrollmentsByCourse(c).length; },
  isEnrolled(u, c) { return this.enrollments.some(x => x.student === u && x.course.toLowerCase() === c.toLowerCase()); },

  enroll(u, c) {
    const course = this.findCourse(c);
    if (!course) return { ok: false, msg: 'Course not found.' };
    if (this.isEnrolled(u, c)) return { ok: false, msg: 'Already enrolled in ' + c + '.' };
    if (this.countEnrolled(c) >= course.quota) return { ok: false, msg: 'Course quota is full.' };
    this.enrollments.push({ student: u, course: course.code });
    this.save();
    return { ok: true, msg: 'Enrolled in ' + course.code + ' — ' + course.name + '.' };
  },

  drop(u, c) {
    const idx = this.enrollments.findIndex(x => x.student === u && x.course.toLowerCase() === c.toLowerCase());
    if (idx === -1) return { ok: false, msg: 'Enrollment not found.' };
    this.enrollments.splice(idx, 1);
    this.save();
    return { ok: true, msg: 'Dropped ' + c.toUpperCase() + '.' };
  },

  // Grades
  getGradesByStudent(u) { return this.grades.filter(x => x.student === u); },
  findGrade(u, c) { return this.grades.find(x => x.student === u && x.course.toLowerCase() === c.toLowerCase()); },

  upsertGrade(u, c, mid, fin) {
    const existing = this.findGrade(u, c);
    if (existing) { existing.midterm = mid; existing.final = fin; }
    else { this.grades.push({ student: u, course: c.toUpperCase(), midterm: mid, final: fin }); }
    this.save();
  },

  // Courses by instructor
  getCoursesByInstructor(u) { return this.courses.filter(x => x.instructor === u); },

  // GPA
  calcAvg(mid, fin) { return mid * 0.4 + fin * 0.6; },

  letterGrade(avg) {
    if (avg >= 90) return 'AA'; if (avg >= 85) return 'BA'; if (avg >= 80) return 'BB';
    if (avg >= 75) return 'CB'; if (avg >= 70) return 'CC'; if (avg >= 60) return 'DC';
    if (avg >= 50) return 'DD'; if (avg >= 40) return 'FD'; return 'FF';
  },

  gradePoint(letter) {
    const m = { AA:4.0, BA:3.5, BB:3.0, CB:2.5, CC:2.0, DC:1.5, DD:1.0, FD:0, FF:0 };
    return m[letter] || 0;
  },

  calcGPA(u) {
    const gs = this.getGradesByStudent(u);
    if (!gs.length) return 0;
    let tp = 0, tc = 0;
    gs.forEach(g => {
      const c = this.findCourse(g.course);
      const cr = c ? c.credit : 3;
      const avg = this.calcAvg(g.midterm, g.final);
      tp += this.gradePoint(this.letterGrade(avg)) * cr;
      tc += cr;
    });
    return tc === 0 ? 0 : tp / tc;
  },

  gradeColor(letter) {
    const m = { AA:'#22c55e', BA:'#4ade80', BB:'#a3e635', CB:'#facc15', CC:'#fb923c', DC:'#f87171', DD:'#ef4444', FD:'#dc2626', FF:'#dc2626' };
    return m[letter] || '#9399ab';
  }
};
