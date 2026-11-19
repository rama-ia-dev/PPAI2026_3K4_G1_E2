// Convierte "2026-03-07T13:05:12" en "07/03/2026 13:05"
export function formatearFechaHora(texto) {
  if (!texto) return '-'
  const [fecha, hora] = texto.split('T')
  const [anio, mes, dia] = fecha.split('-')
  return `${dia}/${mes}/${anio} ${hora.substring(0, 5)}`
}
