# Métricas y verificación

## Definiciones

Cada variante recibe un objeto Metrics nuevo. `comparisons` cuenta contrastes de claves: uno por visita en búsquedas o comprobaciones de fases, uno por comparación entre valores en ordenación y máximo. No cuenta instrucciones Java ni comparaciones de límites. En binaria se cuenta un valor central revisado, aunque el código evalúe igualdad y orden.

`writes` cuenta asignaciones dentro del arreglo, no variables locales. Shell solo registra la escritura de inserción final si el elemento cambió de posición. Un swap entre índices distintos suma dos escrituras y un intercambio; intercambiar un índice consigo mismo no suma.

`calls` cuenta invocaciones recursivas, incluidos casos base. `depth` mide el máximo de llamadas simultáneas desde nivel 1. En factorial con pila explícita representa el tamaño máximo de esa estructura y se rotula como tal; no equivale a profundidad de JVM. En variantes iterativas sin pila explícita profundidad 0 indica ausencia de recursión instrumentada, no ausencia de toda llamada Java.

`nodes` cuenta estados: llamadas recursivas en algoritmos recursivos, iteraciones de recorrido en bucles y máscaras completas en enumeración de mochila. Estas unidades no equivalen entre variantes.

`prunes` cuenta ramas descartadas por restricción o cota. E9 registra resto negativo; E10 capacidad inviable antes de llamar y cota que no mejora. `solutions` registra resultados/hojas alcanzadas: no es el total de soluciones factibles si hubo podas de optimalidad. `multiplications` cuenta productos del factorial.

## Medición del tiempo

El cronómetro usa `System.nanoTime` y se detiene antes de formatear e imprimir el resultado. CSV, conversiones, copias de entrada, validación previa del orden de búsqueda y verificación posterior contra Arrays.sort quedan fuera. Captura de trazas e instrumentación interna permanecen dentro. El tiempo de cada búsqueda no incluye el costo de ordenar; se reportan como fases distintas.

Estos tiempos son muestras ilustrativas. La primera llamada puede incluir inicialización de clases y JIT. No afirmar superioridad universal ni usar una proporción de tiempos como demostración asintótica. Una comparación formal requeriría calentamiento, repeticiones controladas y un diseño de benchmark apropiado. Los conteos deterministas de la referencia son más útiles para el aula.

## Oráculos independientes

| Familia | Referencia |
|---|---|
| Ordenación | Arrays.sort sobre las claves y conservación del conjunto de registros |
| Búsqueda | Resultados manuales y pertenencia con Arrays.binarySearch |
| Factorial | BigInteger para todos los n entre 0 y 20 |
| Fases | Primera anomalía diseñada en índice 45 y casos mínimos explícitos |
| Máximo | Recorrido de referencia, empates y tamaños pares/impares |
| Suma exacta | Conjuntos esperados por IDs y escenarios sin solución |
| Mochila | Enumeración de todas las máscaras, instancias aleatorias y factibilidad recalculada |

## Ejecución de pruebas

```text
mvn clean verify
```

Surefire deja reportes XML y texto en `target/surefire-reports`. La entrega fue verificada con Java 17.0.10 y Maven 3.9.16: 36 ejecuciones JUnit, 0 fallos, 0 errores y 0 omitidas. Varios métodos ejecutan internamente muchos escenarios generados con semilla fija; no se presenta ese volumen como un número adicional de tests JUnit.

Se probaron los diez CSV y la consola con todos los casos. Los reportes de referencia son datos de apoyo, no expectativas rígidas sobre tiempos. Si se modifica el CSV, actualizar la predicción y las pruebas correspondientes.
