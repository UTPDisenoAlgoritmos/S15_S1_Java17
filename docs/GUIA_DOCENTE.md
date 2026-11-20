# Conducción presencial propuesta

## Logro y duración

Al finalizar la sesión, el estudiante resuelve y justifica problemas de ordenación, búsqueda, recursividad y selección óptima, mediante Java17, archivos CSV y pruebas JUnit5, contrastando resultados, métricas y casos extremos.

Tiempo: al finalizar la sesión. Sujeto: el estudiante. Verbo: resuelve y justifica. Contenido: problemas de ordenación, búsqueda, recursividad y selección óptima. Condición: Java17, CSV, pruebas y contraste de evidencia. Leerlo **verbalmente** y pedir reformulación a un estudiante.

La duración de 120 minutos es una propuesta didáctica, ajustable al bloque real. La PPT contiene un banco de 10 problemas, soluciones, referencias y tres anexos docentes ocultos. No proyectar las 57 diapositivas de forma lineal. Las notas de cada lámina contienen preguntas, respuestas, fuentes y criterios de intervención.

## Secuencia de 120 minutos

| Minutos | Conducción | Evidencia de estudiantes |
|---|---|---|
| 0–3 | Verbalizar logro, pedir paráfrasis | Un alumno reformula evidencia final |
| 3–9 | Dudas reales de S14_s2, respuestas entre pares antes del tema nuevo | Duda individual, respuesta de otra pareja y aclaración |
| 9–15 | Diagnóstico con voto antes de explicar | Elección de técnica y razón escrita |
| 15–20 | Utilidad profesional propuesta por parejas | Aplicación, restricción y beneficio expresados por ellos |
| 20–32 | Recuperación activa: asignar dos fuentes a cada equipo | Un ejemplo y una precondición por equipo |
| 32–44 | E1 y E4: estudiantes predicen gaps y límites | Traza en pizarra, corrección argumentada |
| 44–74 | E2/E5/E7/E10 en equipos; rotar roles al minuto15 | Modelo, traza, test, ejecución y conclusión |
| 74–97 | Variante individual E6/E8/E9 sin demo simultánea | Prueba propia y explicación individual |
| 97–110 | Defensa cruzada y feedback | Una objeción, respuesta y mejora comprobable |
| 110–116 | Cierre: organizar ideas aportadas por estudiantes | Síntesis en pizarra, acuerdo de elección de técnicas |
| 116–120 | Ticket individual y dos respuestas compartidas | Evidencia del logro y necesidad de refuerzo |

Si hay más equipos, duplicar los casos y cruzar revisiones en paralelo. Cada alumno profundiza durante clase en un caso propio y conoce otros mediante contraste. La ejecución personal de los diez casos se completa después de clase.

## Diagnóstico y decisiones

1. Una consulta en veinte claves mezcladas: lineal suele evitar costo de preparar orden.
2. Quick Lomuto final sobre ordenado: particiones muy desbalanceadas, peor O(n²).
3. Función que se llama con el mismo argumento y sin base alcanzable: no termina correctamente y puede agotar pila.
4. Capacidad7, A(6,8),B(4,7),C(3,6): B+C es factible y aporta13, superior a A.

Si domina un error, pedir a otra pareja un contraejemplo. No resolver todo el diagnóstico mientras los alumnos escuchan. Registrar pendientes y retomarlos durante práctica.

## Autonomía y retroalimentación

Variantes individuales: E6 añadir7 y predecir5040 con8 llamadas; E8 insertar un máximo empatado antes del índice37 y comprobar que gana el primero; E9 cambiar meta a7 y encontrar B1+B3. Mantener las soluciones fuera de pantalla durante el trabajo.

Roles: analista, programador, verificador y vocero. Cada estudiante explica una decisión propia. En parejas, combinar roles y luego intercambiar. El docente observa, pregunta y da ayuda gradual: pregunta orientadora, pista y contraejemplo. No dicta la solución ni ocupa toda la práctica con una demostración.

Retroalimentación: describir una fortaleza observable, plantear una pregunta sobre un límite, acordar una mejora específica y volver a comprobarla. Ejemplo: “Tu selección respeta50kg. ¿Cómo demuestras que no hay otra con más beneficio? Contrástala con enumeración y conserva esa evidencia”.

Escala sugerida para el trabajo del alumno, distinta de la rúbrica metodológica: correctitud5, modelo y algoritmo5, pruebas4, interpretación4 y defensa individual2.

## Alineación con la rúbrica presencial UTP, pp.1–2

| Indicador | Máximo | Evidencia prevista |
|---|---:|---|
| Logro de aprendizaje | 1,5 | Presentación verbal del logro con los cinco elementos |
| Dudas de sesión anterior | 1 | Actividad antes del desarrollo, con respuestas estudiantiles |
| Conocimientos previos | 1 | Diagnóstico participativo con argumento previo a la solución |
| Utilidad del tema | 1,5 | Estudiantes explicitan utilidad para su formación |
| Secuencia y explicación | 2 | Actividades coherentes y consignas claras |
| Desarrollo del tema | 3,5 | Mayor parte del desarrollo con desafíos, análisis y participación |
| Espacio práctico autónomo | 3,5 | Aplicación por sí mismos en situaciones simuladas alineadas al logro |
| Cierre | 2 | Construcción de ideas principales con participación |
| Discurso verbal | 4 | Pronunciación, ritmo y volumen comprensibles toda la sesión |
| Total | 20 | Valoración sujeta a la ejecución observable |

Ensayar consignas con frases breves y pausas. Comprobar audibilidad al fondo del aula. Pronunciar y explicar “pivote”, “cota” y “recursión indirecta”. Dar tiempo para pensar y evitar leer código a velocidad alta. Las actividades y la PPT apoyan el estándar esperado; no garantizan por sí solas el puntaje.

## Cierre y continuidad

Pedir al aula completar: “Binaria necesita…”, “Recursión termina cuando…” y “Una poda conserva el óptimo si…”. Organizar sus aportes antes de sistematizar. Ticket: elección de técnica, función de la pila, dominio de una poda y test propio. Utilizarlo para preparar refuerzo antes de la PC3 de sesión30.

Antes de clase abrir el JAR de `bin/`, seleccionar CSV y asegurar copias de trabajo. Si falta internet, usar el JAR precompilado. No confundir un problema de instalación con desconocimiento conceptual.
