const apiUrl = 'http://localhost:8080/Examen1/apialumno';

// Función para cargar y mostrar todos los alumnos en la tabla
async function cargarAlumnos() {
  const res = await fetch(apiUrl);
  const alumnos = await res.json();

  const tbody = document.getElementById('tabla-alumnos-body');
  tbody.innerHTML = ''; // Limpiar tabla

  alumnos.forEach(alumno => {
    const tr = document.createElement('tr');

    tr.innerHTML = `
      <td>${alumno.codiEstdWeb}</td>
      <td>${alumno.ndniEstdWeb}</td>
      <td>${alumno.appaEstdWeb}</td>
      <td>${alumno.apmaEstdWeb}</td>
      <td>${alumno.nombEstdWeb}</td>
      <td>${new Date(alumno.fechNaciEstdWeb).toLocaleDateString()}</td>
      <td>${alumno.logiEstd}</td>
      <td>
        <button onclick="mostrarFormularioEditar(${alumno.codiEstdWeb})">Editar</button>
        <button onclick="eliminarAlumno(${alumno.codiEstdWeb})">Eliminar</button>
      </td>
    `;

    tbody.appendChild(tr);
  });
}

// Función para mostrar formulario de edición y rellenar campos
async function mostrarFormularioEditar(id) {
  const res = await fetch(`${apiUrl}?id=${id}`);
  if (!res.ok) {
    alert('Alumno no encontrado');
    return;
  }
  const alumno = await res.json();

  // Aquí asumes que tienes inputs con id's específicos
  document.getElementById('inputId').value = alumno.codiEstdWeb;
  document.getElementById('inputDni').value = alumno.ndniEstdWeb;
  document.getElementById('inputAppa').value = alumno.appaEstdWeb;
  document.getElementById('inputApma').value = alumno.apmaEstdWeb;
  document.getElementById('inputNombre').value = alumno.nombEstdWeb;
  document.getElementById('inputFechaNac').value = alumno.fechNaciEstdWeb.split('T')[0]; // yyyy-mm-dd
  document.getElementById('inputUsuario').value = alumno.logiEstd;
  document.getElementById('inputPass').value = alumno.passEstd;

  // Mostrar el formulario (puede ser modal o sección visible)
  document.getElementById('formEditar').style.display = 'block';
}

// Función para enviar formulario de edición o creación
async function guardarAlumno(event) {
  event.preventDefault();

  const id = document.getElementById('inputId').value;
  const alumno = {
    codiEstdWeb: id ? parseInt(id) : undefined,
    ndniEstdWeb: document.getElementById('inputDni').value,
    appaEstdWeb: document.getElementById('inputAppa').value,
    apmaEstdWeb: document.getElementById('inputApma').value,
    nombEstdWeb: document.getElementById('inputNombre').value,
    fechNaciEstdWeb: document.getElementById('inputFechaNac').value,
    logiEstd: document.getElementById('inputUsuario').value,
    passEstd: document.getElementById('inputPass').value
  };

  let method = 'POST';
  let url = apiUrl;
  if (id) {
    method = 'PUT';
  }

  const res = await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(alumno)
  });

  if (res.ok) {
    alert(id ? 'Alumno actualizado' : 'Alumno creado');
    document.getElementById('formEditar').style.display = 'none';
    cargarAlumnos();
  } else {
    alert('Error al guardar alumno');
  }
}

// Función para eliminar alumno
async function eliminarAlumno(id) {
  if (!confirm('¿Seguro que deseas eliminar este alumno?')) return;

  const res = await fetch(`${apiUrl}?id=${id}`, {
    method: 'DELETE'
  });

  if (res.ok) {
    alert('Alumno eliminado');
    cargarAlumnos();
  } else {
    alert('Error al eliminar alumno');
  }
}

// Al cargar la página, cargar la tabla
window.onload = () => {
  cargarAlumnos();

  // Asociar el submit del formulario a guardarAlumno
  document.getElementById('formEditar').addEventListener('submit', guardarAlumno);
}
