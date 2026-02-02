/*
PROGRAMMER IDENTIFIER:
Full Name: RICH ANDREI CANTAROS
Student ID: 22-1867-386
*/

// REQUIRED: hardcode CSV as multiline string
const csvData = `ID,Name,Grade
1001,Ada Lovelace,A
1002,Alan Turing,A
1003,Grace Hopper,A
1004,Katherine Johnson,A-`;

function parseCsvToObjects(csvText) {
  const lines = csvText
    .split("\n")
    .map(l => l.trim())
    .filter(Boolean);

  if (lines.length === 0) return [];

  const header = lines[0].split(",").map(h => h.trim().toLowerCase());
  const idIdx = header.indexOf("id");
  const nameIdx = header.indexOf("name");
  const gradeIdx = header.indexOf("grade");

  const out = [];
  for (let i = 1; i < lines.length; i++) {
    const parts = lines[i].split(",").map(p => p.trim());
    const obj = {
      id: parts[idIdx] ?? parts[0] ?? "",
      name: parts[nameIdx] ?? parts[1] ?? "",
      grade: parts[gradeIdx] ?? parts[2] ?? ""
    };
    if (obj.id || obj.name || obj.grade) out.push(obj);
  }
  return out;
}

// REQUIRED: parse into an Array of Objects
let records = parseCsvToObjects(csvData);

const tableBody = document.getElementById("tableBody");
const form = document.getElementById("studentForm");
const idInput = document.getElementById("idInput");
const nameInput = document.getElementById("nameInput");
const gradeInput = document.getElementById("gradeInput");

// REQUIRED: render() clears and re-populates; uses template literals
function render() {
  tableBody.innerHTML = "";

  const rowsHtml = records.map((r, index) => {
    return `
      <tr>
        <td>${escapeHtml(r.id)}</td>
        <td>${escapeHtml(r.name)}</td>
        <td>${escapeHtml(r.grade)}</td>
        <td>
          <button class="danger" data-index="${index}">Delete</button>
        </td>
      </tr>
    `;
  }).join("");

  tableBody.innerHTML = rowsHtml;
}

// Create
function addRecord(id, name, grade) {
  records.push({ id, name, grade });
  render();
}

// Delete (row-specific)
function deleteRecord(index) {
  records.splice(index, 1);
  render();
}

// Event: Add
form.addEventListener("submit", (e) => {
  e.preventDefault();
  const id = idInput.value.trim();
  const name = nameInput.value.trim();
  const grade = gradeInput.value.trim();

  if (!id || !name || !grade) return;

  addRecord(id, name, grade);

  idInput.value = "";
  nameInput.value = "";
  gradeInput.value = "";
  idInput.focus();
});

// Event delegation: Delete buttons
tableBody.addEventListener("click", (e) => {
  const btn = e.target.closest("button[data-index]");
  if (!btn) return;
  const idx = Number(btn.getAttribute("data-index"));
  if (!Number.isInteger(idx)) return;
  deleteRecord(idx);
});

// Small helper to avoid injecting raw HTML
function escapeHtml(str) {
  return String(str)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

render();