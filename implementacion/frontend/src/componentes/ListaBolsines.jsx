import { Box, Button, Chip, Paper, Stack, Typography } from '@mui/material'
import PlaceIcon from '@mui/icons-material/Place'
import { formatearFechaHora } from '../utilidades/formatos'

// Lista de bolsines en tránsito (al hacer clic se centra en el mapa)
export default function ListaBolsines({ bolsines, enfocado, alEnfocar, alSeleccionar }) {
  return (
    <Paper variant="outlined" sx={{ p: 1, flexGrow: 1, overflowY: 'auto', maxHeight: { md: 'calc(100vh - 420px)' }, minHeight: 160 }}>
      <Typography variant="body2" color="text.secondary" sx={{ px: 1, py: 0.5 }}>
        {bolsines.length} {bolsines.length === 1 ? 'bolsín' : 'bolsines'} en el mapa
      </Typography>

      <Stack gap={1}>
        {bolsines.map((b) => {
          const activo = enfocado === b.numeroBolsin
          return (
            <Box
              key={b.numeroBolsin}
              onClick={() => alEnfocar(b.numeroBolsin)}
              sx={{
                p: 1.5, borderRadius: 2, cursor: 'pointer', border: 1,
                borderColor: activo ? 'primary.main' : 'divider',
                bgcolor: activo ? 'rgba(29,78,137,0.06)' : 'background.paper'
              }}
            >
              <Stack direction="row" alignItems="center" gap={1}>
                <PlaceIcon color={activo ? 'primary' : 'action'} fontSize="small" />
                <Typography fontWeight={700}>Bolsín {b.numeroBolsin}</Typography>
                <Box sx={{ flexGrow: 1 }} />
                <Chip size="small" label={b.modeloGPSTracker} variant="outlined" />
              </Stack>
              <Typography variant="body2" sx={{ mt: 0.5 }}>Destino: {b.nombreCMDestino}</Typography>
              <Typography variant="body2" color="text.secondary">
                Precinto {b.numeroPrecinto} · Act. {formatearFechaHora(b.fechaHoraUltimaActualizacion)}
              </Typography>
              {activo && (
                <Button size="small" variant="contained" sx={{ mt: 1 }} onClick={(e) => { e.stopPropagation(); alSeleccionar(b) }}>
                  Informar ubicación al Gerente
                </Button>
              )}
            </Box>
          )
        })}
      </Stack>
    </Paper>
  )
}
