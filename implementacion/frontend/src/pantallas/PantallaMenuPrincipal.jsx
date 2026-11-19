import { Box, Card, CardActionArea, CardContent, Chip, Container, Grid, Typography } from '@mui/material'
import TravelExploreIcon from '@mui/icons-material/TravelExplore'
import AddBoxIcon from '@mui/icons-material/AddBox'
import MoveToInboxIcon from '@mui/icons-material/MoveToInbox'
import LockIcon from '@mui/icons-material/Lock'

// Opciones del Encargado de Bolsines. Solo el CU 36 está implementado.
const OPCIONES = [
  { id: 'seguimiento', titulo: 'Consultar seguimiento de bolsines', detalle: 'Ver en el mapa los bolsines en tránsito', icono: <TravelExploreIcon />, habilitada: true },
  { id: 'generar', titulo: 'Generar bolsín', detalle: 'CU 19', icono: <AddBoxIcon />, habilitada: false },
  { id: 'cerrar', titulo: 'Cerrar bolsín', detalle: 'CU 39', icono: <LockIcon />, habilitada: false },
  { id: 'recepcion', titulo: 'Registrar recepción de bolsín', detalle: 'CU 28', icono: <MoveToInboxIcon />, habilitada: false }
]

export default function PantallaMenuPrincipal({ sesion, alElegirOpcion }) {
  return (
    <Container maxWidth="md" sx={{ py: 5 }}>
      <Typography variant="h5" fontWeight={700}>Hola, {sesion.nombreEmpleado.split(' ')[0]}</Typography>
      <Typography color="text.secondary" sx={{ mb: 3 }}>{sesion.comisionMedica.nombre} · ¿Qué querés hacer?</Typography>

      <Grid container spacing={2}>
        {OPCIONES.map((opcion) => (
          <Grid item xs={12} sm={6} key={opcion.id}>
            <Card variant="outlined" sx={{ height: '100%', opacity: opcion.habilitada ? 1 : 0.55 }}>
              <CardActionArea
                disabled={!opcion.habilitada}
                onClick={() => alElegirOpcion(opcion.id)}
                sx={{ height: '100%', p: 1 }}
              >
                <CardContent sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
                  <Box sx={{ color: 'primary.main', display: 'flex', '& svg': { fontSize: 36 } }}>{opcion.icono}</Box>
                  <Box sx={{ flexGrow: 1 }}>
                    <Typography fontWeight={600}>{opcion.titulo}</Typography>
                    <Typography variant="body2" color="text.secondary">{opcion.detalle}</Typography>
                  </Box>
                  {!opcion.habilitada && <Chip size="small" label="Próximamente" />}
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  )
}
