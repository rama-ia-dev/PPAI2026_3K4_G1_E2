import { useEffect, useState } from 'react'
import { Alert, Box, Button, Card, CardContent, Chip, Stack, TextField, Typography } from '@mui/material'
import LocalShippingIcon from '@mui/icons-material/LocalShipping'
import { iniciarSesion, listarUsuarios } from '../servicios/apiBolsines'

// Pantalla de inicio de sesión (con accesos rápidos a los usuarios de prueba)
export default function PantallaIniciarSesion({ alIniciarSesion }) {
  const [usuarios, setUsuarios] = useState([])
  const [nombreUsuario, setNombreUsuario] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    listarUsuarios().then(setUsuarios).catch(() => setError('No se pudo conectar con el backend.'))
  }, [])

  const ingresar = async (evento) => {
    evento.preventDefault()
    try {
      alIniciarSesion(await iniciarSesion(nombreUsuario, password))
    } catch (e) {
      setError(e.mensaje)
    }
  }

  return (
    <Box sx={{ minHeight: '100vh', display: 'grid', placeItems: 'center', p: 2, bgcolor: 'primary.main' }}>
      <Card sx={{ width: '100%', maxWidth: 420 }}>
        <CardContent sx={{ p: 4 }}>
          <Stack alignItems="center" spacing={1} sx={{ mb: 3 }}>
            <LocalShippingIcon color="primary" sx={{ fontSize: 48 }} />
            <Typography variant="h5" fontWeight={700}>Sistema Bolsines</Typography>
            <Typography variant="body2" color="text.secondary">Ingresá con tu usuario</Typography>
          </Stack>

          <form onSubmit={ingresar}>
            <Stack spacing={2}>
              <TextField label="Usuario" value={nombreUsuario} onChange={(e) => setNombreUsuario(e.target.value)} autoFocus />
              <TextField label="Contraseña" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
              {error && <Alert severity="error">{error}</Alert>}
              <Button type="submit" variant="contained" size="large">Ingresar</Button>
            </Stack>
          </form>

          {/* Usuarios de prueba: completan los campos al hacer clic */}
          <Typography variant="caption" color="text.secondary" component="p" sx={{ mt: 3, mb: 1 }}>
            Usuarios de prueba (contraseña 1234):
          </Typography>
          <Stack direction="row" flexWrap="wrap" gap={1}>
            {usuarios.map((u) => (
              <Chip
                key={u.nombreUsuario}
                label={`${u.nombreUsuario} · ${u.comisionMedica.nombre.replace('Comisión Médica ', 'CM ')}`}
                onClick={() => { setNombreUsuario(u.nombreUsuario); setPassword('1234') }}
                variant="outlined"
              />
            ))}
          </Stack>
        </CardContent>
      </Card>
    </Box>
  )
}
