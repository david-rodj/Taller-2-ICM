# Guion para Video Demo - Aplicación Móvil con Mapa y Multimedia
**Duración estimada: 6 minutos**

---

## INTRODUCCIÓN (30 segundos)
**[0:00 - 0:30]**

**Visual**: App en pantalla principal
**Narración**: 
"Hola, bienvenidos. Hoy les voy a mostrar una aplicación móvil desarrollada en Android con Kotlin y Jetpack Compose que integra funcionalidades de mapas, cámara, galería y sensores. La aplicación cuenta con dos módulos principales: uno de captura multimedia y otro de navegación por mapas. Empecemos."

---

## PARTE 1: PANTALLA PRINCIPAL Y NAVEGACIÓN (30 segundos)
**[0:30 - 1:00]**

**Visual**: Pantalla principal con dos botones
**Acciones**:
- Mostrar la pantalla principal
- Señalar cada botón

**Narración**:
"Esta es la pantalla principal. Tenemos dos opciones: el botón de Cámara/Galería para capturar y seleccionar contenido multimedia, y el botón de Mapa para acceder a las funcionalidades de geolocalización y navegación. Comencemos explorando el módulo de cámara."

---

## PARTE 2: MÓDULO DE CÁMARA Y GALERÍA (2 minutos)
**[1:00 - 3:00]**

### A) Permisos y Modo Foto (45 segundos)
**[1:00 - 1:45]**

**Visual**: Entrar a Cámara/Galería
**Acciones**:
- Tap en botón "Cámara/Galería"
- Mostrar solicitud de permisos (si aparece)
- Aceptar permisos

**Narración**:
"Al entrar por primera vez, la aplicación solicita permisos de cámara y almacenamiento, necesarios para el funcionamiento correcto. Aquí vemos la interfaz principal del módulo multimedia."

**Visual**: Interfaz con switch Foto/Video
**Acciones**:
- Mostrar el switch (debe estar en Foto)
- Señalar los dos botones

**Narración**:
"El switch nos permite alternar entre modo Foto y Video. Actualmente estamos en modo Foto. Tenemos dos opciones: tomar una foto nueva con la cámara o seleccionar una desde la galería."

### B) Capturar Foto (30 segundos)
**[1:45 - 2:15]**

**Visual**: Tomar foto
**Acciones**:
- Tap en "Tomar Foto"
- Capturar una foto
- Mostrar la foto en pantalla

**Narración**:
"Presiono 'Tomar Foto', se abre la cámara nativa del dispositivo, tomo la foto y automáticamente se muestra en la pantalla. La imagen se ajusta perfectamente al contenedor manteniendo su proporción."

### C) Seleccionar Foto de Galería (30 segundos)
**[2:15 - 2:45]**

**Visual**: Galería
**Acciones**:
- Tap en "Seleccionar Foto"
- Navegar por galería
- Seleccionar una foto
- Mostrar en pantalla

**Narración**:
"Ahora pruebo la opción de galería. Al presionar 'Seleccionar Foto', se abre el selector de archivos del sistema. Elijo una imagen y se carga instantáneamente en la aplicación."

### D) Modo Video (15 segundos)
**[2:45 - 3:00]**

**Visual**: Cambiar a modo video
**Acciones**:
- Cambiar switch a Video
- Mostrar cambio en interfaz

**Narración**:
"Cambio al modo Video usando el switch. Observen cómo la interfaz se actualiza mostrando 'Grabar Video' y 'Seleccionar Video'. El área de visualización ahora muestra un ícono de video como placeholder."

---

## PARTE 3: MÓDULO DE MAPA (2 minutos 30 segundos)
**[3:00 - 5:30]**

### A) Entrada y Permisos (30 segundos)
**[3:00 - 3:30]**

**Visual**: Volver y entrar a Mapa
**Acciones**:
- Tap en flecha atrás
- Tap en botón "Mapa"
- Mostrar solicitud de permisos de ubicación
- Aceptar permisos

**Narración**:
"Regreso a la pantalla principal y entro al módulo de Mapa. La aplicación solicita permisos de ubicación para poder mostrar nuestra posición actual y rastrear nuestro recorrido. Acepto los permisos."

### B) Características del Mapa (30 segundos)
**[3:30 - 4:00]**

**Visual**: Mapa cargado con ubicación
**Acciones**:
- Mostrar mapa cargado
- Señalar marcador azul (ubicación actual)
- Hacer zoom in/out
- Desplazar mapa

**Narración**:
"El mapa se carga usando OpenStreetMap. Vemos nuestro marcador de ubicación actual en azul con el título 'Mi Ubicación'. El mapa es completamente interactivo: puedo hacer zoom con dos dedos y desplazarme libremente."

### C) Sensor de Luz - Modo Oscuro (30 segundos)
**[4:00 - 4:30]**

**Visual**: Activar modo oscuro
**Acciones**:
- Cubrir sensor de luz con mano/objeto
- Mostrar cambio a modo oscuro
- Descubrir sensor
- Mostrar cambio a modo claro

**Narración**:
"La aplicación incluye un sensor de luz ambiental. Si cubro el sensor de luz del dispositivo, el mapa cambia automáticamente a modo oscuro para mejor visualización. Al descubrirlo, regresa al modo claro. Esto funciona en tiempo real sin intervención del usuario."

### D) Búsqueda de Dirección y Ruta (45 segundos)
**[4:30 - 5:15]**

**Visual**: Buscar dirección
**Acciones**:
- Escribir dirección en campo de búsqueda (ej: "Torre Colpatria, Bogotá")
- Tap en botón de búsqueda
- Mostrar marcador rojo en destino
- Mostrar línea roja de ruta

**Narración**:
"Ahora probemos la búsqueda. Escribo 'Torre Colpatria, Bogotá' y presiono buscar. La aplicación usa geocoding para encontrar las coordenadas, coloca un marcador rojo en el destino, y automáticamente crea una línea roja mostrando la ruta directa desde mi ubicación actual hasta ese punto."

### E) Crear Destino con Long Press (30 segundos)
**[5:15 - 5:45]**

**Visual**: Long press en mapa
**Acciones**:
- Mantener presionado otro punto del mapa
- Mostrar cómo se elimina el marcador anterior
- Mostrar nuevo marcador con dirección
- Mostrar nueva ruta

**Narración**:
"También puedo establecer un destino manteniendo presionado cualquier punto del mapa. Observen: el marcador y ruta anteriores se eliminan automáticamente. La aplicación usa reverse geocoding para obtener la dirección del punto seleccionado y crea una nueva ruta. Solo puede existir un destino a la vez."

### F) Rastreo de Recorrido (15 segundos)
**[5:45 - 6:00]**

**Visual**: Mostrar línea azul
**Acciones**:
- Señalar línea azul en el mapa
- Mostrar switch "Seguir mi ubicación"

**Narración**:
"La línea azul muestra mi recorrido histórico, mientras que la línea roja indica la ruta directa al destino. El switch 'Seguir mi ubicación' mantiene el mapa centrado en mi posición actual."

---

## CIERRE (30 segundos)
**[5:30 - 6:00]**

**Visual**: Pantalla general o volver a inicio
**Acciones**:
- Volver a pantalla principal
- Mostrar ambos botones brevemente

**Narración**:
"En resumen, hemos visto una aplicación completa que integra captura de fotos y videos, selección desde galería, mapas interactivos con OpenStreetMap, geolocalización en tiempo real, búsqueda de direcciones, creación de rutas, sensor de luz para modo oscuro automático, y rastreo del recorrido del usuario. Todo desarrollado con Kotlin, Jetpack Compose, y siguiendo las mejores prácticas de Android moderno. Gracias por su atención."

---

## NOTAS TÉCNICAS PARA LA GRABACIÓN

### Preparación antes de grabar:
1. ✅ Desinstalar y reinstalar la app para mostrar permisos
2. ✅ Tener buena iluminación para probar sensor de luz
3. ✅ Tener objeto opaco para cubrir sensor
4. ✅ Cargar batería del dispositivo
5. ✅ Activar ubicación GPS
6. ✅ Tener conexión a internet para tiles del mapa
7. ✅ Preparar dirección conocida para buscar
8. ✅ Limpiar notificaciones de pantalla

### Tips de grabación:
- Hablar claro y con buen ritmo
- Pausar 1-2 segundos entre secciones
- Si cometes error, continúa desde el inicio de esa sección
- Mantén el dispositivo estable
- Graba en orientación vertical (portrait)
- Usa herramienta de screen recording de Android Studio o ADB

### Permisos a mostrar:
1. Cámara
2. Micrófono (para video)
3. Almacenamiento/Galería
4. Ubicación (precisa y aproximada)

### Funcionalidades clave a destacar:
- ✅ Captura foto/video
- ✅ Galería foto/video
- ✅ Ubicación en tiempo real
- ✅ Sensor de luz (modo oscuro/claro)
- ✅ Geocoding (dirección → coordenadas)
- ✅ Reverse geocoding (coordenadas → dirección)
- ✅ Rutas dinámicas
- ✅ Un solo destino a la vez
- ✅ Rastreo de recorrido
- ✅ Mapa interactivo