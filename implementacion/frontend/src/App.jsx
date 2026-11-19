import { useEffect, useState } from 'react'
import { Box } from '@mui/material'
import BarraSuperior from './componentes/BarraSuperior'
import PantallaIniciarSesion from './pantallas/PantallaIniciarSesion'
import PantallaMenuPrincipal from './pantallas/PantallaMenuPrincipal'
import PantallaConsultarSeguimientoBolsines from './pantallas/PantallaConsultarSeguimientoBolsines'
import { cerrarSesion, obtenerSesionActual } from './servicios/apiBolsines'

// Navegación entre pantallas: login -> menú -> CU 36
export default function App() {
  const [sesion, setSesion] = useState(null)
  const [pantalla, setPantalla] = useState('menu')

  // Al abrir la app, recupera la sesión activa (si existe)
  useEffect(() => {
    obtenerSesionActual().then(setSesion).catch(() => setSesion(null))
  }, [])

  const salir = async () => {
    await cerrarSesion()
    setSesion(null)
    setPantalla('menu')
  }

  if (!sesion) {
    return <PantallaIniciarSesion alIniciarSesion={setSesion} />
  }

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
      <BarraSuperior sesion={sesion} alSalir={salir} alIrAlInicio={() => setPantalla('menu')} />

      {pantalla === 'menu' && (
        <PantallaMenuPrincipal sesion={sesion} alElegirOpcion={setPantalla} />
      )}

      {pantalla === 'seguimiento' && (
        <PantallaConsultarSeguimientoBolsines alSalir={() => setPantalla('menu')} />
      )}
    </Box>
  )
}
