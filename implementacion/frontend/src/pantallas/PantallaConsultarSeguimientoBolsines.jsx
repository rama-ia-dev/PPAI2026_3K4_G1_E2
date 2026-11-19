import { useEffect, useState } from 'react'
import { Alert, Box, Button, CircularProgress, Paper, Stack, Typography } from '@mui/material'
import CloseIcon from '@mui/icons-material/Close'
import RefreshIcon from '@mui/icons-material/Refresh'
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline'
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined'
import PanelFiltros from '../componentes/PanelFiltros'
import ListaBolsines from '../componentes/ListaBolsines'
import AvisoBolsinesSinUbicacion from '../componentes/AvisoBolsinesSinUbicacion'
import MapaBolsines from '../componentes/MapaBolsines'
import DialogoConfirmarEnvioMail from '../componentes/DialogoConfirmarEnvioMail'
import {
  opcionConsultarSeguimientoBolsines,
  tomarCancelacion,
  tomarConfirmacionEnvioMail,
  tomarFiltro,
  tomarSeleccionBolsin
} from '../servicios/apiBolsines'

// <<boundary>> PantallaConsultarSeguimientoBolsines (parte visual, en el navegador).
// Cada acción del EB llama al método homónimo de la pantalla en el backend, que devuelve
// la VistaSeguimiento: CM del usuario, mapa, bolsines sin ubicación, bolsín seleccionado,
// mensaje y paso (INICIO, SELECCION, CONFIRMACION o FIN). Esta pantalla solo la dibuja.
export default function PantallaConsultarSeguimientoBolsines({ alSalir }) {
  const [cargando, setCargando] = useState(true)
  const [vista, setVista] = useState(null)
  const [destinos, setDestinos] = useState([])     // CM destino para el filtro
  const [enfocado, setEnfocado] = useState(null)   // bolsín centrado en el mapa
  const [error, setError] = useState('')

  const ejecutar = async (accion) => {
    setError('')
    try {
      const nuevaVista = await accion()
      setVista(nuevaVista)
      return nuevaVista
    } catch (e) {
      setError(e.mensaje || 'No se pudo completar la operación.')
      return null
    }
  }

  // Paso 1: el EB selecciona la opción de consultar la ubicación de los bolsines
  const opcionConsultar = async () => {
    setCargando(true)
    setEnfocado(null)
    const nuevaVista = await ejecutar(opcionConsultarSeguimientoBolsines)
    const posiciones = nuevaVista?.mapaBolsines?.posiciones || []
    const unicos = {}
    posiciones.forEach((p) => { unicos[p.codigoCMDestino] = p.nombreCMDestino })
    setDestinos(Object.entries(unicos).map(([codigo, nombre]) => ({ codigo, nombre })))
    setCargando(false)
  }

  useEffect(() => { opcionConsultar() }, [])

  // Paso 6: filtro por número de precinto y/o CM destino (A2 si el precinto no existe)
  const filtrar = (numeroPrecinto, codigoCMDestino) => ejecutar(() => tomarFiltro(numeroPrecinto, codigoCMDestino))
  const limpiarFiltro = () => ejecutar(() => tomarFiltro(null, null))

  // Paso 7: el EB selecciona un bolsín del mapa
  const seleccionar = (bolsin) => ejecutar(() => tomarSeleccionBolsin(bolsin.numeroBolsin))

  // Paso 9 / A5: el EB confirma o no el envío del correo
  const responderConfirmacion = (confirma) => ejecutar(() => tomarConfirmacionEnvioMail(confirma))

  // A4: el EB cancela el caso de uso
  const cancelar = async () => {
    await ejecutar(tomarCancelacion)
    alSalir()
  }

  const posiciones = vista?.mapaBolsines?.posiciones || []
  const enSeleccion = vista && (vista.paso === 'SELECCION' || vista.paso === 'CONFIRMACION')

  return (
    <Box sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', p: { xs: 1.5, md: 2 }, gap: 2 }}>
      <Stack direction={{ xs: 'column', sm: 'row' }} alignItems={{ sm: 'center' }} gap={1}>
        <Box sx={{ flexGrow: 1 }}>
          <Typography variant="h5" fontWeight={700}>Seguimiento de bolsines</Typography>
          <Typography color="text.secondary">
            {vista?.cmUsuarioLogueado ? `Bolsines enviados desde ${vista.cmUsuarioLogueado}` : 'Bolsines en tránsito'}
          </Typography>
        </Box>
        {enSeleccion && (
          <Stack direction="row" gap={1}>
            <Button variant="outlined" startIcon={<RefreshIcon />} onClick={opcionConsultar}>Actualizar</Button>
            <Button variant="outlined" color="inherit" startIcon={<CloseIcon />} onClick={cancelar}>Cancelar</Button>
          </Stack>
        )}
      </Stack>

      {error && <Alert severity="error" onClose={() => setError('')}>{error}</Alert>}

      {cargando && (
        <Stack alignItems="center" sx={{ py: 8 }} gap={2}>
          <CircularProgress />
          <Typography color="text.secondary">Consultando la ubicación a los GPS Tracker...</Typography>
        </Stack>
      )}

      {/* Fin del caso de uso: A1, A3 total, A5 o envío exitoso (A4 vuelve al menú principal) */}
      {!cargando && vista?.paso === 'FIN' && (
        <Paper variant="outlined" sx={{ p: 6, textAlign: 'center' }}>
          {vista.mensaje?.startsWith('Se notificó')
            ? <CheckCircleOutlineIcon sx={{ fontSize: 56, color: 'success.main' }} />
            : <InfoOutlinedIcon sx={{ fontSize: 56, color: 'text.disabled' }} />}
          <Typography variant="h6" sx={{ mt: 1, mb: 1 }}>{vista.mensaje}</Typography>
          {!vista.mapaBolsines && <AvisoBolsinesSinUbicacion bolsines={vista.bolsinesSinUbicacion} />}
          <Stack direction="row" gap={1} justifyContent="center" sx={{ mt: 3 }}>
            <Button variant="outlined" onClick={opcionConsultar}>Nueva consulta</Button>
            <Button variant="contained" onClick={alSalir}>Volver al inicio</Button>
          </Stack>
        </Paper>
      )}

      {!cargando && enSeleccion && (
        <Box sx={{ flexGrow: 1, display: 'grid', gap: 2, gridTemplateColumns: { xs: '1fr', md: '360px 1fr' } }}>
          <Stack gap={2} sx={{ minWidth: 0 }}>
            <PanelFiltros comisionesDestino={destinos} alFiltrar={filtrar} alLimpiar={limpiarFiltro} />
            {vista.mensaje && <Alert severity="warning">{vista.mensaje}</Alert>}
            {/* A3: se sigue avisando qué bolsines no tienen ubicación aunque se filtre */}
            {!vista.mensaje?.includes('GPS Tracker') && (
              <AvisoBolsinesSinUbicacion bolsines={vista.bolsinesSinUbicacion} />
            )}
            <ListaBolsines bolsines={posiciones} enfocado={enfocado} alEnfocar={setEnfocado} alSeleccionar={seleccionar} />
          </Stack>
          <MapaBolsines mapa={vista.mapaBolsines} enfocado={enfocado} alSeleccionar={seleccionar} />
        </Box>
      )}

      {/* Paso 8: pregunta si enviar el correo al Gerente de la CM destino */}
      <DialogoConfirmarEnvioMail
        bolsin={vista?.paso === 'CONFIRMACION' ? vista.bolsinSeleccionado : null}
        alResponder={responderConfirmacion}
      />
    </Box>
  )
}
