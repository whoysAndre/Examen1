document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();

  const username = document.getElementById('ndni').value;
  const password = document.getElementById('password').value;
  console.log(`Usuario: ${username}, Contraseña: ${password}`);


  const res = await fetch('http://localhost:8080/Examen1/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ username, password }),
  });

  const data = await res.json();


  if (data.success) {
    alert('Login exitoso');
    Cookies.set('token', data.token, { expires: 7 }); // Guardar el token en una cookie
    Cookies.set('username', username, { expires: 7 }); // Guardar el nombre de usuario en una cookie
    window.location.href = "http://localhost:8080/Examen1/principal.html";
    //console.log("Token guardado en cookie:", data.token);
  } else {
    alert('Error: ' + data.message);
  }
});