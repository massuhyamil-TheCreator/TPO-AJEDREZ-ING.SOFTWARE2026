# Verificación de la versión con frontend

Se ejecutaron los tests reales de JUnit con Maven y Java 17: **49 tests,
0 fallos, 0 errores, 0 omitidos**. Son 39 tests existentes y 10 del nuevo
adaptador gráfico. La nota de la entrega anterior sobre no poder ejecutar
Maven quedó superada por esta verificación.

Se comparó el contenido de los 38 archivos Java originales con el ZIP fuente:
no se modificó ninguno. `domain/`, `application/` y `console/` permanecen
idénticos. Los cambios de integración están en `pom.xml`; las clases gráficas
están en el nuevo paquete `gui/`.

La ventana se inició con JavaFX en modo headless. Se dispararon eventos de
botones y se capturaron imágenes para revisar selección y destinos,
jugadas inválidas, cambio de turno, historial, capturas, orientación,
confirmación de reinicio, tamaño mínimo y jaque.

El README explica cómo iniciar la GUI, conservar el uso de consola y repetir
los tests. El backend sigue teniendo el mismo alcance: no se agregaron
enroque, promoción, captura al paso, mate ni tablas.
