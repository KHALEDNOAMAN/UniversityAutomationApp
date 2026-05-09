// ═══════════════════════════════════════════════════════
//  APP CONTROLLER — UI logic
// ═══════════════════════════════════════════════════════

let currentUser = null;
let currentPage = null;

// ─── Init ───
document.addEventListener('DOMContentLoaded', () => {
  DataStore.initialize();
  setupLogin();
  setupLogout();
  setupHamburger();
});

// ─── Toast Notifications ───
function showToast(msg, type = 'info') {
  let container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const t = document.createElement('div');
  t.className = 'toast ' + type;
  t.textContent = msg;
  container.appendChild(t);
  setTimeout(() => t.remove(), 3000);
}

// ─── Screen Switching ───
function showScreen(id) {
  document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
  document.getElementById(id).classList.add('active');
}

// ═══════════════════════════════════════════════════════
//  LOGIN
// ═══════════════════════════════════════════════════════

function setupLogin() {
  document.getElementById('login-form').addEventListener('submit', e => {
    e.preventDefault();
    const u = document.getElementById('login-username').value.trim();
    const p = document.getElementById('login-password').value.trim();
    const errEl = document.getElementById('login-error');

    const user = DataStore.authenticate(u, p);
    if (!user) {
      errEl.textContent = 'Invalid credentials. Please try again.';
      errEl.classList.remove('hidden');
    } else {
      errEl.classList.add('hidden');
      currentUser = user;
      enterDashboard();
    }
  });

  document.querySelectorAll('.demo-chip').forEach(chip => {
    chip.addEventListener('click', () => {
      document.getElementById('login-username').value = chip.dataset.user;
      document.getElementById('login-password').value = chip.dataset.pass;
    });
  });
}

function setupLogout() {
  document.getElementById('logout-btn').addEventListener('click', () => {
    currentUser = null;
    currentPage = null;
    document.getElementById('login-username').value = '';
    document.getElementById('login-password').value = '';
    document.getElementById('login-error').classList.add('hidden');
    showScreen('login-screen');
  });
}

function setupHamburger() {
  document.getElementById('hamburger-btn').addEventListener('click', () => {
    document.getElementById('sidebar').classList.toggle('open');
  });
}

// ═══════════════════════════════════════════════════════
//  DASHBOARD ENTRY
// ═══════════════════════════════════════════════════════

function enterDashboard() {
  showScreen('dashboard-screen');

  // Update sidebar
  const initials = currentUser.fullName.split(' ').map(w => w[0]).join('').substring(0, 2);
  document.getElementById('sidebar-avatar').textContent = initials;
  document.getElementById('sidebar-name').textContent = currentUser.fullName;
  document.getElementById('sidebar-role').textContent = currentUser.role;

  const hour = new Date().getHours();
  const greeting = hour < 12 ? 'Good morning' : hour < 18 ? 'Good afternoon' : 'Good evening';
  document.getElementById('topbar-greeting').textContent = greeting + ', ' + currentUser.fullName.split(' ')[0];

  buildNav();
  showToast('Welcome, ' + currentUser.fullName + '!', 'success');
}

// ═══════════════════════════════════════════════════════
//  NAVIGATION
// ═══════════════════════════════════════════════════════

function buildNav() {
  const menu = document.getElementById('nav-menu');
  menu.innerHTML = '';

  const items = getNavItems();
  items.forEach((item, i) => {
    const li = document.createElement('li');
    li.className = 'nav-item';
    const a = document.createElement('a');
    a.innerHTML = `<span class="nav-icon">${item.icon}</span> ${item.label}`;
    a.addEventListener('click', () => {
      menu.querySelectorAll('a').forEach(x => x.classList.remove('active'));
      a.classList.add('active');
      document.getElementById('sidebar').classList.remove('open');
      item.action();
    });
    li.appendChild(a);
    menu.appendChild(li);
  });

  // Auto-click first item
  menu.querySelector('a').click();
}

function getNavItems() {
  switch (currentUser.role) {
    case 'student': return [
      { icon:'📋', label:'My Profile', action: pageProfile },
      { icon:'📚', label:'Available Courses', action: pageAvailableCourses },
      { icon:'🎒', label:'My Courses', action: pageMyCourses },
      { icon:'📊', label:'My Grades', action: pageGrades },
      { icon:'🎓', label:'Transcript', action: pageTranscript }
    ];
    case 'instructor': return [
      { icon:'📚', label:'My Courses', action: pageInstructorCourses },
      { icon:'👥', label:'Enrollments', action: pageEnrollments },
      { icon:'📝', label:'Enter Grades', action: pageGradeEntry }
    ];
    case 'admin': return [
      { icon:'👤', label:'All Users', action: pageAllUsers },
      { icon:'📚', label:'All Courses', action: pageAllCourses },
      { icon:'🎓', label:'All Students', action: pageAllStudents }
    ];
    default: return [];
  }
}

// ═══════════════════════════════════════════════════════
//  HELPER — set page content
// ═══════════════════════════════════════════════════════

function setContent(title, html) {
  document.getElementById('page-title').textContent = title;
  const area = document.getElementById('content-area');
  area.innerHTML = html;
  area.style.animation = 'none';
  area.offsetHeight; // reflow
  area.style.animation = 'fadeIn 0.3s ease';
}

function esc(s) {
  const d = document.createElement('div');
  d.textContent = s;
  return d.innerHTML;
}

// ═══════════════════════════════════════════════════════
//  STUDENT PAGES
// ═══════════════════════════════════════════════════════

function pageProfile() {
  const sp = DataStore.findStudent(currentUser.username);
  if (!sp) { setContent('My Profile', '<div class="empty-state"><div class="empty-icon">😕</div><p>No profile found.</p></div>'); return; }

  setContent('My Profile', `
    <div class="card">
      <div class="card-header"><span class="card-title">Student Information</span></div>
      <div class="profile-grid">
        <div class="profile-field"><div class="label">Student ID</div><div class="value">${esc(sp.studentId)}</div></div>
        <div class="profile-field"><div class="label">Full Name</div><div class="value">${esc(sp.fullName)}</div></div>
        <div class="profile-field"><div class="label">Department</div><div class="value">${esc(sp.department)}</div></div>
        <div class="profile-field"><div class="label">Year</div><div class="value">${sp.year}</div></div>
        <div class="profile-field"><div class="label">Username</div><div class="value">${esc(sp.username)}</div></div>
        <div class="profile-field"><div class="label">GPA</div><div class="value">${DataStore.calcGPA(sp.username).toFixed(2)} / 4.00</div></div>
      </div>
    </div>
  `);
}

function pageAvailableCourses() {
  const courses = DataStore.courses;
  let rows = '';
  courses.forEach(c => {
    const enrolled = DataStore.countEnrolled(c.code);
    const full = enrolled >= c.quota;
    const alreadyIn = DataStore.isEnrolled(currentUser.username, c.code);
    rows += `<tr>
      <td><strong>${esc(c.code)}</strong></td>
      <td>${esc(c.name)}</td>
      <td>${c.credit}</td>
      <td>${enrolled}/${c.quota}</td>
      <td>${esc(c.instructor)}</td>
      <td>${full ? '<span class="badge badge-danger">FULL</span>' :
        alreadyIn ? '<span class="badge badge-info">Enrolled</span>' :
        `<button class="btn btn-success btn-sm" onclick="doEnroll('${c.code}')">Enroll</button>`}</td>
    </tr>`;
  });

  setContent('Available Courses', `
    <div class="stats-grid">
      <div class="stat-card"><div class="stat-icon purple">📚</div><div><div class="stat-value">${courses.length}</div><div class="stat-label">Total Courses</div></div></div>
      <div class="stat-card"><div class="stat-icon green">✅</div><div><div class="stat-value">${DataStore.getEnrollmentsByStudent(currentUser.username).length}</div><div class="stat-label">My Enrollments</div></div></div>
    </div>
    <div class="card">
      <div class="card-header"><span class="card-title">Course Catalog</span></div>
      <div class="table-wrap"><table>
        <thead><tr><th>Code</th><th>Course Name</th><th>Credits</th><th>Quota</th><th>Instructor</th><th>Action</th></tr></thead>
        <tbody>${rows}</tbody>
      </table></div>
    </div>
  `);
}

function doEnroll(code) {
  const r = DataStore.enroll(currentUser.username, code);
  showToast(r.msg, r.ok ? 'success' : 'error');
  pageAvailableCourses();
}

function pageMyCourses() {
  const enrs = DataStore.getEnrollmentsByStudent(currentUser.username);
  if (!enrs.length) {
    setContent('My Courses', '<div class="empty-state"><div class="empty-icon">📭</div><p>You are not enrolled in any courses.</p></div>');
    return;
  }
  let rows = '';
  enrs.forEach((e, i) => {
    const c = DataStore.findCourse(e.course);
    rows += `<tr>
      <td>${i + 1}</td>
      <td><strong>${esc(e.course)}</strong></td>
      <td>${c ? esc(c.name) : 'Unknown'}</td>
      <td>${c ? c.credit : '—'}</td>
      <td><button class="btn btn-danger btn-sm" onclick="doDrop('${e.course}')">Drop</button></td>
    </tr>`;
  });

  setContent('My Courses', `
    <div class="card">
      <div class="card-header"><span class="card-title">Enrolled Courses (${enrs.length})</span></div>
      <div class="table-wrap"><table>
        <thead><tr><th>#</th><th>Code</th><th>Course Name</th><th>Credits</th><th>Action</th></tr></thead>
        <tbody>${rows}</tbody>
      </table></div>
    </div>
  `);
}

function doDrop(code) {
  const r = DataStore.drop(currentUser.username, code);
  showToast(r.msg, r.ok ? 'success' : 'error');
  pageMyCourses();
}

function pageGrades() {
  const gs = DataStore.getGradesByStudent(currentUser.username);
  if (!gs.length) {
    setContent('My Grades', '<div class="empty-state"><div class="empty-icon">📊</div><p>No grades recorded yet.</p></div>');
    return;
  }
  let rows = '';
  gs.forEach(g => {
    const c = DataStore.findCourse(g.course);
    const avg = DataStore.calcAvg(g.midterm, g.final);
    const letter = DataStore.letterGrade(avg);
    const color = DataStore.gradeColor(letter);
    rows += `<tr>
      <td><strong>${esc(g.course)}</strong></td>
      <td>${c ? esc(c.name) : 'Unknown'}</td>
      <td>${g.midterm.toFixed(1)}</td>
      <td>${g.final.toFixed(1)}</td>
      <td>${avg.toFixed(1)}</td>
      <td><span class="badge badge-grade" style="background:${color}20;color:${color}">${letter}</span></td>
    </tr>`;
  });

  setContent('My Grades', `
    <div class="card">
      <div class="card-header"><span class="card-title">Grade Report</span></div>
      <div class="table-wrap"><table>
        <thead><tr><th>Code</th><th>Course</th><th>Midterm</th><th>Final</th><th>Average</th><th>Grade</th></tr></thead>
        <tbody>${rows}</tbody>
      </table></div>
    </div>
  `);
}

function pageTranscript() {
  const sp = DataStore.findStudent(currentUser.username);
  const gs = DataStore.getGradesByStudent(currentUser.username);
  const gpa = DataStore.calcGPA(currentUser.username);
  const initials = currentUser.fullName.split(' ').map(w => w[0]).join('').substring(0, 2);

  let totalCredits = 0;
  let rows = '';
  gs.forEach(g => {
    const c = DataStore.findCourse(g.course);
    const cr = c ? c.credit : 3;
    totalCredits += cr;
    const avg = DataStore.calcAvg(g.midterm, g.final);
    const letter = DataStore.letterGrade(avg);
    const color = DataStore.gradeColor(letter);
    const points = DataStore.gradePoint(letter);
    rows += `<tr>
      <td><strong>${esc(g.course)}</strong></td>
      <td>${c ? esc(c.name) : 'Unknown'}</td>
      <td>${cr}</td>
      <td>${g.midterm.toFixed(1)}</td>
      <td>${g.final.toFixed(1)}</td>
      <td>${avg.toFixed(1)}</td>
      <td><span class="badge badge-grade" style="background:${color}20;color:${color}">${letter}</span></td>
      <td>${points.toFixed(1)}</td>
    </tr>`;
  });

  const standing = gpa >= 3.5 ? "Dean's List" : gpa >= 2.0 ? 'Good Standing' : gpa >= 1.0 ? 'Academic Probation' : 'Academic Suspension';
  const standingColor = gpa >= 3.5 ? 'success' : gpa >= 2.0 ? 'info' : 'danger';

  setContent('Transcript', `
    <div class="transcript-header">
      <div class="transcript-avatar">${initials}</div>
      <div class="transcript-info">
        <h3>${esc(sp ? sp.fullName : currentUser.fullName)}</h3>
        <div class="transcript-meta">
          <span>🆔 ${esc(sp ? sp.studentId : 'N/A')}</span>
          <span>🏛️ ${esc(sp ? sp.department : 'N/A')}</span>
          <span>📅 Year ${sp ? sp.year : 'N/A'}</span>
        </div>
      </div>
    </div>
    <div class="gpa-summary">
      <div class="gpa-card"><div class="gpa-value">${gpa.toFixed(2)}</div><div class="gpa-label">Cumulative GPA</div></div>
      <div class="gpa-card"><div class="gpa-value">${totalCredits}</div><div class="gpa-label">Total Credits</div></div>
      <div class="gpa-card"><div class="gpa-value"><span class="badge badge-${standingColor}" style="font-size:14px">${standing}</span></div><div class="gpa-label">Standing</div></div>
    </div>
    <div class="card" style="margin-top:24px">
      <div class="card-header"><span class="card-title">Academic Record</span></div>
      ${gs.length ? `<div class="table-wrap"><table>
        <thead><tr><th>Code</th><th>Course</th><th>Cr</th><th>Mid</th><th>Final</th><th>Avg</th><th>Grade</th><th>Pts</th></tr></thead>
        <tbody>${rows}</tbody>
      </table></div>` : '<div class="empty-state"><p>No completed courses.</p></div>'}
    </div>
  `);
}

// ═══════════════════════════════════════════════════════
//  INSTRUCTOR PAGES
// ═══════════════════════════════════════════════════════

function pageInstructorCourses() {
  const cs = DataStore.getCoursesByInstructor(currentUser.username);
  if (!cs.length) {
    setContent('My Courses', '<div class="empty-state"><div class="empty-icon">📭</div><p>No courses assigned.</p></div>');
    return;
  }
  let rows = '';
  cs.forEach(c => {
    const en = DataStore.countEnrolled(c.code);
    rows += `<tr>
      <td><strong>${esc(c.code)}</strong></td><td>${esc(c.name)}</td>
      <td>${c.credit}</td><td>${en}/${c.quota}</td>
      <td>${en >= c.quota ? '<span class="badge badge-danger">Full</span>' : '<span class="badge badge-success">Open</span>'}</td>
    </tr>`;
  });
  setContent('My Courses', `<div class="card"><div class="card-header"><span class="card-title">Courses I Teach</span></div>
    <div class="table-wrap"><table><thead><tr><th>Code</th><th>Name</th><th>Credits</th><th>Enrolled</th><th>Status</th></tr></thead><tbody>${rows}</tbody></table></div></div>`);
}

function pageEnrollments() {
  const cs = DataStore.getCoursesByInstructor(currentUser.username);
  let html = '';
  cs.forEach(c => {
    const enrs = DataStore.getEnrollmentsByCourse(c.code);
    let rows = '';
    enrs.forEach((e, i) => {
      const sp = DataStore.findStudent(e.student);
      rows += `<tr><td>${i+1}</td><td>${esc(e.student)}</td><td>${sp ? esc(sp.fullName) : '—'}</td><td>${sp ? esc(sp.department) : '—'}</td></tr>`;
    });
    html += `<div class="card"><div class="card-header"><span class="card-title">${esc(c.code)} — ${esc(c.name)} (${enrs.length} students)</span></div>
      ${enrs.length ? `<div class="table-wrap"><table><thead><tr><th>#</th><th>Username</th><th>Name</th><th>Dept</th></tr></thead><tbody>${rows}</tbody></table></div>` :
      '<div class="empty-state"><p>No students enrolled.</p></div>'}</div>`;
  });
  setContent('Course Enrollments', html || '<div class="empty-state"><div class="empty-icon">📭</div><p>No courses.</p></div>');
}

function pageGradeEntry() {
  const cs = DataStore.getCoursesByInstructor(currentUser.username);
  let opts = cs.map(c => `<option value="${c.code}">${c.code} — ${esc(c.name)}</option>`).join('');

  setContent('Enter Grades', `
    <div class="card">
      <div class="card-header"><span class="card-title">Grade Entry</span></div>
      <div class="form-row">
        <div class="input-group"><label>Course</label><select id="ge-course" class="input-group input" style="width:100%;padding:12px 16px;background:var(--bg-input);border:1px solid var(--border);border-radius:8px;color:var(--text-primary);font-size:15px;font-family:var(--font)">
          <option value="">Select course...</option>${opts}</select></div>
      </div>
      <div id="ge-students"></div>
    </div>
  `);

  document.getElementById('ge-course').addEventListener('change', function() {
    const code = this.value;
    const container = document.getElementById('ge-students');
    if (!code) { container.innerHTML = ''; return; }

    const enrs = DataStore.getEnrollmentsByCourse(code);
    if (!enrs.length) { container.innerHTML = '<div class="empty-state"><p>No students enrolled.</p></div>'; return; }

    let rows = '';
    enrs.forEach(e => {
      const sp = DataStore.findStudent(e.student);
      const g = DataStore.findGrade(e.student, code);
      rows += `<tr>
        <td>${esc(e.student)}</td><td>${sp ? esc(sp.fullName) : '—'}</td>
        <td><input type="number" min="0" max="100" step="0.1" id="mid-${e.student}" value="${g ? g.midterm : ''}" placeholder="0-100" style="width:80px;padding:6px 10px;background:var(--bg-input);border:1px solid var(--border);border-radius:6px;color:var(--text-primary);font-family:var(--font)"></td>
        <td><input type="number" min="0" max="100" step="0.1" id="fin-${e.student}" value="${g ? g.final : ''}" placeholder="0-100" style="width:80px;padding:6px 10px;background:var(--bg-input);border:1px solid var(--border);border-radius:6px;color:var(--text-primary);font-family:var(--font)"></td>
        <td><button class="btn btn-success btn-sm" onclick="saveGrade('${e.student}','${code}')">Save</button></td>
      </tr>`;
    });
    container.innerHTML = `<div class="table-wrap" style="margin-top:16px"><table>
      <thead><tr><th>Username</th><th>Name</th><th>Midterm</th><th>Final</th><th></th></tr></thead>
      <tbody>${rows}</tbody></table></div>`;
  });
}

function saveGrade(student, course) {
  const mid = parseFloat(document.getElementById('mid-' + student).value);
  const fin = parseFloat(document.getElementById('fin-' + student).value);
  if (isNaN(mid) || isNaN(fin) || mid < 0 || mid > 100 || fin < 0 || fin > 100) {
    showToast('Scores must be between 0 and 100.', 'error'); return;
  }
  if (!DataStore.isEnrolled(student, course)) {
    showToast('Student is not enrolled in this course.', 'error'); return;
  }
  DataStore.upsertGrade(student, course, mid, fin);
  const avg = DataStore.calcAvg(mid, fin);
  showToast(`Saved — ${student}: Avg ${avg.toFixed(1)}, Grade ${DataStore.letterGrade(avg)}`, 'success');
}

// ═══════════════════════════════════════════════════════
//  ADMIN PAGES
// ═══════════════════════════════════════════════════════

function pageAllUsers() {
  let rows = '';
  DataStore.users.forEach(u => {
    const roleClass = u.role === 'admin' ? 'danger' : u.role === 'instructor' ? 'warning' : 'info';
    rows += `<tr><td>${esc(u.username)}</td><td>${esc(u.fullName)}</td>
      <td><span class="badge badge-${roleClass}">${u.role}</span></td><td>${esc(u.refId)}</td></tr>`;
  });
  setContent('All Users', `
    <div class="stats-grid">
      <div class="stat-card"><div class="stat-icon purple">👤</div><div><div class="stat-value">${DataStore.users.length}</div><div class="stat-label">Total Users</div></div></div>
      <div class="stat-card"><div class="stat-icon blue">🎓</div><div><div class="stat-value">${DataStore.users.filter(u=>u.role==='student').length}</div><div class="stat-label">Students</div></div></div>
      <div class="stat-card"><div class="stat-icon orange">👨‍🏫</div><div><div class="stat-value">${DataStore.users.filter(u=>u.role==='instructor').length}</div><div class="stat-label">Instructors</div></div></div>
      <div class="stat-card"><div class="stat-icon green">🔑</div><div><div class="stat-value">${DataStore.users.filter(u=>u.role==='admin').length}</div><div class="stat-label">Admins</div></div></div>
    </div>
    <div class="card"><div class="card-header"><span class="card-title">User Directory</span></div>
    <div class="table-wrap"><table><thead><tr><th>Username</th><th>Full Name</th><th>Role</th><th>ID</th></tr></thead><tbody>${rows}</tbody></table></div></div>`);
}

function pageAllCourses() {
  let rows = '';
  DataStore.courses.forEach(c => {
    const en = DataStore.countEnrolled(c.code);
    rows += `<tr><td><strong>${esc(c.code)}</strong></td><td>${esc(c.name)}</td><td>${c.credit}</td>
      <td>${en}/${c.quota}</td><td>${esc(c.instructor)}</td>
      <td>${en >= c.quota ? '<span class="badge badge-danger">Full</span>' : '<span class="badge badge-success">Open</span>'}</td></tr>`;
  });
  setContent('All Courses', `<div class="card"><div class="card-header"><span class="card-title">Course Directory (${DataStore.courses.length})</span></div>
    <div class="table-wrap"><table><thead><tr><th>Code</th><th>Name</th><th>Credits</th><th>Enrolled</th><th>Instructor</th><th>Status</th></tr></thead><tbody>${rows}</tbody></table></div></div>`);
}

function pageAllStudents() {
  let rows = '';
  DataStore.students.forEach(sp => {
    const gpa = DataStore.calcGPA(sp.username);
    rows += `<tr><td>${esc(sp.studentId)}</td><td>${esc(sp.fullName)}</td>
      <td>${esc(sp.department)}</td><td>${sp.year}</td><td>${gpa.toFixed(2)}</td><td>${esc(sp.username)}</td></tr>`;
  });
  setContent('All Students', `<div class="card"><div class="card-header"><span class="card-title">Student Directory (${DataStore.students.length})</span></div>
    <div class="table-wrap"><table><thead><tr><th>ID</th><th>Name</th><th>Department</th><th>Year</th><th>GPA</th><th>Username</th></tr></thead><tbody>${rows}</tbody></table></div></div>`);
}
