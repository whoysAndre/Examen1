document.getElementById('changeForm').addEventListener('submit', async (e) => {
  e.preventDefault();

  const ndni = Cookies.get('ndni');
  const currentPassword = document.getElementById('currentPassword').value;
  const newPassword = document.getElementById('newPassword').value;
  const confirmPassword = document.getElementById('confirmPassword').value;
  if (document.getElementById('newPassword').value !== confirmPassword) {
    alert('Las contraseñas no coinciden');
    return;
  }

  const res = await fetch('http://localhost:8080/Examen1/passchange', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'authorization': 'Bearer ' + Cookies.get('token') // Asegúrate de enviar el token de autorización
    },
    body: JSON.stringify({ ndni, currentPassword, newPassword })
  });

  const data = await res.json();

  if (res.ok) {
    alert('Contraseña cambiada correctamente');
    window.location.href = 'index.html';
  } else {
    alert('Error: ' + data.message);
  }
});