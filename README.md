# Bolsines – Consultar seguimiento de bolsines (CU 36)

Trabajo práctico de la materia **Diseño de Sistemas de Información** (Ingeniería en Sistemas de Información, UTN Facultad Regional Córdoba, ciclo 2026, curso 3K4, Grupo 1). Corresponde a la **Entrega 2** del Proyecto Práctico de Aplicación Integrador: el diseño de un caso de uso aplicando un patrón de diseño, y su implementación.

Integrantes: Ramiro Baudo, Ignacio Esquivel, David Frances, Federico Gon, Joaquin Ozan Helou y Facundo Santiago Vasquez.

## El caso de estudio

Un organismo nacional con comisiones médicas en todo el país envía documentación entre ellas por correo postal, dentro de **bolsines**: bolsas de tela cerradas con un **precinto** numerado. El sistema registra el armado, el envío y la recepción de esos bolsines.

Cada bolsín lleva un dispositivo **GPS Tracker** que informa su ubicación. Existen tres modelos de dispositivo y cada uno expone una interfaz distinta:

| Modelo | Operación | Recibe | Devuelve |
|---|---|---|---|
| XTR-4500L | `getBolsinLocation` | API Key, número de bolsín y código de la comisión médica de origen | JSON en un texto |
| NavTrack QX-7A | `retrieveTrackingData` | API Key, número de bolsín y código de la comisión médica de destino | Un texto con los datos separados por comas |
| GeoPulse MTR-900 | `fetchCargoPositions` | API Key y número de bolsín | Una matriz |

Los tres informan lo mismo (número de bolsín, latitud, longitud y fecha y hora de la última actualización), pero con formatos incompatibles.

## El caso de uso implementado

**CU 36 Consultar seguimiento de bolsines:** el Encargado de Bolsines de una comisión médica ve en un mapa dónde están los bolsines que su comisión envió y que todavía no llegaron a destino.

1. El Encargado de Bolsines elige la opción de consultar el seguimiento.
2. El sistema muestra la comisión médica del usuario que inició sesión.
3. Busca los bolsines en estado **Enviado** cuya comisión de origen es la del usuario.
4. Pide a cada GPS Tracker la ubicación de su bolsín.
5. Recibe de cada GPS Tracker la ubicación, en el formato de su modelo.
6. Muestra los bolsines en un mapa. Se puede filtrar por número de precinto o por comisión médica de destino.
7. El Encargado selecciona un bolsín del mapa.
8. El sistema pregunta si se quiere avisar por correo al Gerente de la comisión médica de destino.
9. El Encargado confirma.
10. El sistema busca el correo del Gerente.
11. Le envía la ubicación del bolsín (esto lo resuelve otro caso de uso, el **CU 31 Notificar ubicación de bolsín**, que el CU 36 incluye).

Flujos alternativos:

| Flujo | Situación |
|---|---|
| A1 | No hay bolsines en estado Enviado para la comisión del usuario. |
| A2 | No existe un bolsín con el número de precinto ingresado en el filtro. |
| A3 | El GPS Tracker no pudo informar la ubicación de un bolsín (los demás se muestran igual; si no responde ninguno, termina el caso de uso). |
| A4 | El Encargado cancela el caso de uso. |
| A5 | El Encargado no confirma el envío del correo. |

## El patrón aplicado: Adapter

Para que el sistema no dependa del formato de cada fabricante, se aplicó el patrón **Adapter**. El sistema le pide la ubicación a una interfaz común (`IAdaptadorGPSTracker`) y hay un adaptador por modelo que llama al servicio de ese fabricante y convierte la respuesta a un formato único (`UbicacionBolsin`). Agregar un modelo nuevo solo requiere un adaptador nuevo.

| Rol en el patrón | Clase |
|---|---|
| Cliente | `InterfazGPSTracker` |
| Objetivo | `IAdaptadorGPSTracker` |
| Adaptadores | `AdaptadorGPSTrackerXTR4500L`, `AdaptadorGPSTrackerNavTrackQX7A`, `AdaptadorGPSTrackerGeoPulseMTR900` |
| Adaptables (servicios de cada fabricante) | `ServicioGPSTrackerXTR4500L`, `ServicioGPSTrackerNavTrackQX7A`, `ServicioGPSTrackerGeoPulseMTR900` |

La justificación completa, los diagramas y las decisiones de diseño están en el documento de la entrega.

## Contenido del repositorio

| Carpeta | Contenido |
|---|---|
| `documento/` | Documento de la entrega (`PPAI2026_3K4_G1_E2_Diseño.pdf` y su versión `.docx`) y las capturas de la aplicación que incluye |
| `diagramas/1_analisis_corregido/` | Análisis del CU 36 hecho en la Entrega 1, con las correcciones de la docente aplicadas: clases, secuencia y máquinas de estado de Bolsín y Remito |
| `diagramas/2_diseno/` | Diseño del CU 36: estructura del patrón Adapter, clases de diseño y secuencias de diseño |
| `implementacion/api-gps-tracker/` | API que simula los tres modelos de GPS Tracker. La entregaron los docentes de la materia y se usa sin cambios |
| `implementacion/backend/` | Backend del CU 36 (Java 17, Spring Boot 3.3.5, JPA y base de datos H2) |
| `implementacion/frontend/` | Interfaz web (React 18, Vite, Material UI y Leaflet) |
| `herramientas/` | PlantUML, para generar las imágenes de los diagramas |

Los diagramas están escritos en PlantUML (`.puml`); al lado de cada uno está su imagen `.png`.

En el código, las clases siguen la clasificación usada en los diagramas: **boundary** (pantallas e interfaces con sistemas externos), **control** (gestores que coordinan el caso de uso) y **entity** (clases del dominio que se guardan en la base de datos).

## Cómo ejecutarlo

Requisitos: Java 17 o superior y Node.js 18 o superior.

Se levantan tres procesos, cada uno en su propia terminal y en este orden:

```
cd implementacion/api-gps-tracker
sh mvnw spring-boot:run         # API del GPS Tracker, puerto 8080

cd implementacion/backend
sh mvnw spring-boot:run         # backend del CU 36, puerto 8081

cd implementacion/frontend
npm install
npm run dev                     # interfaz web en http://localhost:5173
```

En Windows se usa `mvnw.cmd spring-boot:run` en lugar de `sh mvnw spring-boot:run`.

La base de datos se crea de nuevo, con datos de prueba, cada vez que arranca el backend.

### Usuarios de prueba

Todos tienen la contraseña `1234`.

| Usuario | Comisión médica | Qué permite probar |
|---|---|---|
| `eb.cordoba` | Córdoba | Flujo completo y los alternativos A2 a A5. Muestra los bolsines 101, 103, 105, 106 y 108 (los tres modelos de GPS). El bolsín 116 no existe en la API, por lo que dispara A3 |
| `eb.jujuy` | Jujuy | A1: la comisión no tiene bolsines enviados |
| `eb.rosario` | Rosario | Consulta con un solo bolsín enviado (102) |

Para probar A2 se filtra por un precinto que no existe, por ejemplo `99999`. Para ver A3 en todos los bolsines a la vez, se detiene la API del GPS Tracker antes de consultar.

El envío de correo está simulado: el mensaje se muestra en la consola del backend.

## Regenerar las imágenes de los diagramas

```
java -jar herramientas/plantuml.jar -charset UTF-8 -tpng diagramas/1_analisis_corregido/*.puml diagramas/2_diseno/*.puml
```
