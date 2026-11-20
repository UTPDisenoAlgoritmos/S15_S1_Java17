# UTP · Semana 15, sesión 1: Resolución de problemas

Curso Análisis y Diseño de Algoritmos, 100000SI35, 2026-2. Repaso de la unidad 3 antes de la PC3 de la segunda sesión de semana 15. El banco contiene **10 ejercicios, 10 CSV de entrada y código Java 17**. Los datos representan situaciones didácticas simuladas.

## Inicio rápido

1. Extraer el ZIP completo.
2. Abrir una terminal **en esta carpeta**, donde está `pom.xml`.
3. Comprobar `java -version`, `javac -version` y `mvn -version`. Maven debe usar un JDK 17, no solo un JRE ni un JDK 11.
4. Compilar y ejecutar las pruebas:

```powershell
mvn clean verify
java -jar target/utp-s15-s1.jar
```

El menú permite elegir 1–10, 11 para todos y 0 para salir. Si aún no puedes descargar dependencias, el ZIP incluye un JAR ya compilado:

```powershell
java -jar bin/utp-s15-s1.jar
```

Este JAR requiere Java 17 o posterior y **no necesita Maven ni dependencias externas de ejecución**. La carpeta `datos` debe estar en el directorio de trabajo. Maven sí requiere acceso inicial a su repositorio para plugins y JUnit.

## Comandos de trabajo

```powershell
# Un ejercicio con traza por fases
java -jar target/utp-s15-s1.jar 1 --trace

# Comparar búsquedas y límites de intervalo
java -jar target/utp-s15-s1.jar 4 --trace
java -jar target/utp-s15-s1.jar 3 --target 711

# Variar meta de suma exacta o capacidad de mochila
java -jar target/utp-s15-s1.jar 9 --target 7
java -jar target/utp-s15-s1.jar 10 --capacity 40

# Ejecutar todos y exportar resultados, sin redirigir la entrada del menú
java -jar target/utp-s15-s1.jar --all --report resultados/mi_ejecucion.csv

# Usar copias de los diez CSV, conservando sus nombres y cabeceras
java -jar target/utp-s15-s1.jar 8 --data datos_modificados

java -jar target/utp-s15-s1.jar --help
```

En macOS/Linux se usan los mismos comandos de Java y Maven. `--trace` muestra fases de ordenación y límites de búsqueda binaria. Las trazas recursivas de factorial, fases y backtracking se explican en la PPT y el código, sin imprimir miles de estados. `--target` afecta búsquedas E3–E5 y meta E9. `--capacity` afecta E10.

## Los diez ejercicios

| N.º | Problema y entrada | Resultado de control |
|---|---|---|
| 1 | 20 tickets, Shell Sort | Orden ascendente, gaps 10,5,2,1 |
| 2 | 24 pedidos, QuickSort Lomuto y Shell | Igual orden por clave, primer pivote 25 en índice 10 |
| 3 | 18 repuestos sin ordenar, búsqueda lineal | 736: índice 16; 799: −1 |
| 4 | 11 códigos ordenados, lineal y binaria | 125: índice 6, 7/3 revisiones; 126: −1, 11/4 |
| 5 | 16 productos, ordenar y buscar | 125: índice original 7 y ordenado 10 |
| 6 | Factoriales, recursión/bucle/pila explícita | 4!=24; 20!=2432902008176640000 |
| 7 | 80 fases, recursión indirecta y bucle | Primera anomalía: índice 45, secuencia 46 |
| 8 | 64 sensores, máximo por división y bucle | SEN-038, 46,7 °C, primer máximo |
| 9 | Bloques 2,3,5,6 GB, vuelta atrás | Meta 8: B1+B4 y B2+B3 |
| 10 | 20 pedidos, selección óptima 0/1 | S/ 1901 con 50 kg, sobre 16 elegibles |

Los índices empiezan en 0. Una fila de datos en posición `i` está en la línea física `i+2` cuando hay cabecera y no hay líneas vacías. Los algoritmos operan sobre la lista de datos, no sobre números de línea física.

## Organización por capas

```text
datos/                              10 CSV de entrada
src/main/java/pe/edu/utp/algoritmos/
  domain/                           Registros y contadores
  data/CsvReader.java               Estructura CSV UTF-8
  data/CaseRepository.java           Cabeceras y mapeo de modelos
  algorithms/                       Algoritmos independientes de archivos
  app/                              Menú, casos, medición y reporte
src/test/java/                      JUnit 5
docs/                               Casos, guía docente, fuentes y contratos
resultados/                         Referencias reproducibles y acta de verificación
bin/utp-s15-s1.jar                   Ejecutable de conveniencia
```

Los algoritmos reciben objetos y arreglos. No abren archivos. El CSV se lee una vez antes de medir. Las ordenaciones usan copias y mueven el registro completo para conservar la asociación entre ID y clave. El archivo original permanece disponible para búsquedas que necesitan su orden de llegada.

## Contratos y límites

- CSV UTF-8 separado por comas, con cabecera exacta y un registro por línea física. Admite BOM, comillas, comas dentro de campos y comillas dobles escapadas. No admite campos multilinea. Una línea vacía se omite; un archivo sin cabecera se rechaza. Solo cabecera representa un conjunto vacío.
- IDs no vacíos y únicos donde existen. Se rechazan enteros inválidos y categorías ajenas al contrato del caso. Los límites del dominio se comprueban antes del cálculo correspondiente.
- Binaria exige orden ascendente por la misma clave. El chequeo se hace fuera de la medición de consulta. Lineal retorna la primera coincidencia; binaria retorna cualquier coincidencia. Con duplicados pueden diferir los índices sin que haya un error.
- Shell con gaps por mitades y QuickSort Lomuto final no son estables. El JAR compara claves ordenadas y conservación de registros, sin exigir el mismo orden de IDs empatados.
- Factorial usa `long`: solo 0–20. Se rechazan −1 y 21. La pila explícita guarda enteros pendientes; su tamaño no representa marcos de llamadas JVM.
- QuickSort didáctico acepta hasta 1000 registros; recursión indirecta hasta 500 eventos. Estos topes didácticos evitan cargas excesivas en este taller, sin garantizar idéntica capacidad de pila en toda JVM. Se informan errores, no se recortan filas.
- E8 desempata por menor índice, devuelve −1 si no hay lecturas y usa décimas de °C enteras.
- E9 requiere capacidades positivas y hasta 20 bloques. La poda por resto negativo depende de esa positividad. IDs distintos representan recursos distintos, incluso si tienen igual capacidad.
- E10 usa pesos positivos, beneficios no negativos, capacidad no negativa y hasta 20 elegibles. Suma beneficios en `long` y comprueba pesos sin overflow.

## Optimalidad y tamaño de backtracking

E10 primero valida las 20 filas y luego aplica la regla de negocio `ciudad=LIMA AND prioridad=ALTA AND estado=LISTO`. Quedan 16 pedidos; las cuatro exclusiones incumplen esas categorías. **No se seleccionan las primeras 16 filas ni las de mayor beneficio.** Los excluidos tienen beneficios altos a propósito: el filtro expresa elegibilidad, no conveniencia algorítmica.

Se obtiene el óptimo exacto **del conjunto elegible**. Cambiar el filtro define otro problema. El dataset pequeño se diseñó para que la enumeración de `2^16=65536` subconjuntos sea viable. Si hay más de 20 elegibles, ambas estrategias rechazan la carga en lugar de declarar óptimo de una muestra.

La cota `beneficioActual + sumaBeneficiosPendientes` nunca subestima lo alcanzable con beneficios no negativos. Si no supera el mejor beneficio, la rama puede descartarse cuando buscamos un óptimo. Esto no enumera todos los óptimos empatados. La enumeración independiente evalúa todas las máscaras y verifica el objetivo y la factibilidad. En esta entrada el óptimo es único:

```text
P02 P04 P06 P08 P09 P13 P14
8 + 12 + 4 + 6 + 7 + 3 + 10 = 50 kg
423 + 508 + 150 + 160 + 300 + 100 + 260 = S/ 1901
```

E9 puede producir muchas soluciones. Su memoria de salida se añade al espacio O(n) de la pila y selección parcial. La fuerza bruta de E10 recorre n ítems por máscara, O(n·2^n); el árbol de decisiones visita O(2^n) estados en el peor caso, con el costo adicional de registrar mejores soluciones. Las podas reducen el trabajo observado, sin eliminar la naturaleza exponencial general.

## Métricas, resultados y pruebas

`resultados/referencia.csv` conserva los conteos y tiempos de una ejecución de referencia. `docs/resultados_esperados.json` conserva salidas independientes de los tiempos. Los tiempos incluyen instrumentación y construcción interna de trazas, y dependen del entorno. Lectura, validación previa, formateo de salida, impresión y comparación de orden con la biblioteca quedan fuera del cronómetro. No constituyen un benchmark formal.

El proyecto incluye 36 ejecuciones de pruebas JUnit (27 de algoritmos, incluidas las parametrizadas, y 9 del lector), además de cientos de escenarios generados dentro de varias pruebas. Cubren los 10 CSV, arreglos aleatorios, partición exacta, duplicados, enteros extremos, factoriales 0–20 con BigInteger, dominio inválido, primera anomalía, máximos empatados, estados restaurados y mochila contrastada con enumeración.

Consulta [casos y datos](docs/CASOS_Y_DATOS.md), [métricas y pruebas](docs/METRICAS_Y_PRUEBAS.md), [resumen de fuentes](docs/RESUMEN_OCHO_SESIONES.md) y [guía docente](docs/GUIA_DOCENTE.md).

## Fuentes técnicas

- [JUnit 5.10.2, guía oficial](https://docs.junit.org/5.10.2/user-guide/): pruebas y aserciones.
- [Apache Maven: seleccionar versión de Java](https://maven.apache.org/guides/mini/guide-java-target.html): compilación con release 17.
- [Oracle Java 17: System.nanoTime](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html#nanoTime()): medición de intervalos de tiempo.

El contenido curricular se apoya en los ocho adjuntos, el sílabo y la rúbrica presencial proporcionados. La rúbrica docente institucional tiene nueve indicadores y máximo de 20 puntos; el material prepara las actividades, mientras que la valoración depende de su conducción observable en el aula.
