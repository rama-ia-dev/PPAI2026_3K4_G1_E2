import { AppBar, Box, Button, Toolbar, Typography } from '@mui/material'
import LocalShippingIcon from '@mui/icons-material/LocalShipping'
import LogoutIcon from '@mui/icons-material/Logout'

// Barra superior: nombre del sistema, usuario logueado y su CM
export default function BarraSuperior({ sesion, alSalir, alIrAlInicio }) {
  return (
    <AppBar position="static" elevation={0}>
      <Toolbar sx={{ gap: 2 }}>
        <Box onClick={alIrAlInicio} sx={{ display: 'flex', alignItems: 'center', gap: 1, cursor: 'pointer' }}>
          <LocalShippingIcon />
          <Typography variant="h6" fontWeight={700}>Bolsines</Typography>
        </Box>

        <Box sx={{ flexGrow: 1 }} />

        <Box sx={{ textAlign: 'right', display: { xs: 'none', sm: 'block' } }}>
          <Typography variant="body2" fontWeight={600}>{sesion.nombreEmpleado}</Typography>
          <Typography variant="caption" sx={{ opacity: 0.85 }}>
            {sesion.rol} · {sesion.comisionMedica.nombre}
          </Typography>
        </Box>

        <Button color="inherit" startIcon={<LogoutIcon />} onClick={alSalir}>Salir</Button>
      </Toolbar>
    </AppBar>
  )
}
