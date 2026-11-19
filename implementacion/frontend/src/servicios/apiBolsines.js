// Llamadas al backend. Cada función corresponde a un endpoint.

async function post(url, datos) {
  const respuesta = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(datos || {})
  })
  const cuerpo = await respuesta.json().catch(() => null)

  // Los flujos alternativos llegan como error con { flujo, mensaje }
  if (!respuesta.ok) {
    throw cuerpo || { flujo: 'ERROR', mensaje: 'No se pudo conectar con el servidor.' }
  }
  return cuerpo
}

async function get(url) {
  const respuesta = await fetch(url)
  if (respuesta.status === 204) return null
  const cuerpo = await respuesta.json().catch(() => null)
  if (!respuesta.ok) {
    throw cuerpo || { flujo: 'ERROR', mensaje: 'No se pudo conectar con el servidor.' }
  }
  return cuerpo
}

// ---------- Sesión ----------
export const listarUsuarios = () => get('/api/sesion/usuarios')
export const obtenerSesionActual = () => get('/api/sesion/actual')
export const iniciarSesion = (nombreUsuario, password) => post('/api/sesion/iniciar', { nombreUsuario, password })
export const cerrarSesion = () => post('/api/sesion/cerrar')

// ---------- CU 36 Consultar seguimiento de bolsines ----------
// Cada función llama al método homónimo de PantallaConsultarSeguimientoBolsines (backend),
// con los mismos parámetros del diagrama, y recibe el estado de la pantalla (VistaSeguimiento).
const CU36 = '/api/seguimiento-bolsines'

function conParametros(ruta, parametros) {
  const consulta = new URLSearchParams()
  Object.entries(parametros).forEach(([nombre, valor]) => {
    if (valor !== null && valor !== undefined && valor !== '') consulta.append(nombre, valor)
  })
  const texto = consulta.toString()
  return texto ? `${ruta}?${texto}` : ruta
}

export const opcionConsultarSeguimientoBolsines = () => post(`${CU36}/opcion`)
export const tomarFiltro = (numeroPrecinto, codigoCMDestino) =>
  post(conParametros(`${CU36}/filtro`, { numeroPrecinto, codigoCMDestino }))
export const tomarSeleccionBolsin = (numeroBolsin) => post(conParametros(`${CU36}/seleccion`, { numeroBolsin }))
export const tomarConfirmacionEnvioMail = (confirma) => post(conParametros(`${CU36}/confirmacion`, { confirma }))
export const tomarCancelacion = () => post(`${CU36}/cancelacion`)
