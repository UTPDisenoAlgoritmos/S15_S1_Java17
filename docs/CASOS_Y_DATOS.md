# Enunciados, diccionario y pruebas de escritorio

Todos los CSV de entrada están en `datos/`. Los datos no corresponden a personas ni operaciones empresariales reales. Ninguno contiene mil filas: se prioriza una carga pequeña que cada estudiante pueda inspeccionar y verificar. El reporte de resultados es otro CSV de salida y no cuenta entre los diez archivos de entrada.

## E1 · Tickets, Shell Sort

Una mesa de ayuda debe organizar 20 tickets por minutos estimados. Ordenar de menor a mayor, mantener el ID con su valor y explicar cada gap.

- Archivo `01_tickets_shell.csv`, 20 filas. `id`: texto único. `valor`: entero en minutos.
- Gaps 10,5,2,1. Resultado: `[4,7,12,16,18,25,27,31,34,39,42,49,55,58,61,65,70,73,84,90]`.
- Métricas: 88 comparaciones, 68 escrituras, 0 swaps en esta implementación.
- Casos: vacío, uno, ordenado, inverso y repetidos. Las funciones generales aceptan claves enteras negativas, aunque los minutos de este CSV sean positivos.
- Pregunta: ¿por qué gap 2 ordena dos subsecuencias pero no necesariamente todo el arreglo?

## E2 · Pedidos, QuickSort

Ordenar 24 pedidos por minutos pendientes con Lomuto y pivote final, contrastando con Shell. No se exige estabilidad entre pedidos con igual clave.

- Archivo `02_pedidos_quick.csv`, 24 filas. `id`: único. `valor`: minutos enteros.
- Primer pivote 25, posición final 10. Salida en el JSON de referencia.
- Métricas Shell: 106 comparaciones, 70 escrituras. Quick: 89 comparaciones, 70 escrituras, 35 swaps, 31 llamadas, profundidad 7.
- Prueba adicional: cinco claves ascendentes producen 10 comparaciones de Quick con pivote final. Claves repetidas pueden desbalancear Lomuto.
- Pregunta: ¿por qué el pivote queda fijo antes de ordenar todo el arreglo?

## E3 · Repuestos, lineal

Buscar 736 y 799 en 18 códigos recibidos sin orden. Retornar primer índice o −1, sin ordenar.

- Archivo `03_repuestos_lineal.csv`, 18 filas. `id`: único. `valor`: código entero.
- 736: índice 16, 17 revisiones. 799: −1, 18 revisiones. 711: índice 0, una revisión.
- Probar vacío, primera y última clave, ausente y duplicados.
- Pregunta: ¿por qué no puedes parar al ver una clave mayor que el objetivo?

## E4 · Catálogo, binaria y lineal

Consultar un arreglo de 11 códigos ascendentes y comparar ambos métodos.

- Archivo `04_catalogo_binaria.csv`, 11 filas. `id`: único. `valor`: código entero, ordenado de menor a mayor.
- 125: medios 5,8,6; índice 6. Lineal 7 revisiones, binaria 3.
- 126: medios 5,8,6,7; −1. Lineal 11 revisiones, binaria 4.
- La precondición se comprueba antes de cronometrar. Una copia desordenada debe rechazarse.
- Con claves repetidas, binaria puede retornar otra coincidencia válida, mientras lineal devuelve la primera.
- Pregunta: ¿compensa ordenar para una sola consulta? Considera el costo de preparación.

## E5 · Tienda, integración

Organizar 16 productos y buscar 125,145,999 en original y ordenado.

- Archivo `05_tienda_integrada.csv`, 16 filas. `id`: único e inmutable. `valor`: código del producto.
- 125: índice 7 original, 10 ordenado, mismo ID E5-008.
- 145: índice 14 en ambos, mismo ID E5-015. La igualdad del índice es casual.
- 999: −1 en ambos.
- Shell y Quick ordenan registros completos. Se contrasta orden y conservación de pares ID-valor.
- Pregunta: ¿qué problema aparecería si ordenaras solo una columna separada del ID?

## E6 · Factoriales, recursión y pila

Calcular una lista de factoriales con tres variantes: directa, iterativa y pila explícita. Explicar diferencias de control y memoria.

- Archivo `06_factoriales.csv`, 6 filas. `n`: entero, entre 0 y 20.
- n=0,1,4,5,10,20. Resultados 1,1,24,120,3628800,2432902008176640000.
- Para n=4: 5 llamadas recursivas, profundidad 5, 4 multiplicaciones. El bucle también hace 4 multiplicaciones.
- La pila explícita guarda 4 enteros pendientes para n=4; no añade recursión al método.
- Rechazar n<0 y n>20. Contrastar 0–20 con BigInteger en tests.
- Pregunta: ¿qué guarda un marco mientras espera `factorial(n-1)`?

## E7 · Fases, recursión indirecta

Detectar la primera infracción de una secuencia que empieza ENSAMBLE y alterna con INSPECCION. Dos métodos se llaman mutuamente, luego se compara con un bucle.

- Archivo `07_fases_indirecta.csv`, 80 filas. `secuencia`: entero consecutivo desde 1. `fase`: ENSAMBLE o INSPECCION.
- Anomalía en índice 45, secuencia 46, línea física 47 del archivo original.
- 46 revisiones por variante, 46 llamadas y profundidad 46 en la indirecta.
- Reparar fila 46 y repetir: −1. Vacío: −1. Fase desconocida o secuencia rota: rechazo.
- Límite didáctico de recursión indirecta: 500 eventos. No recorta una carga mayor.
- Pregunta: ¿qué método está activo cuando detecta la anomalía?

## E8 · Sensores, máximo por división

Localizar la temperatura máxima y el primer índice en caso de empate. Comparar división recursiva y bucle.

- Archivo `08_sensores.csv`, 64 filas. `id`: único. `temperatura_decimas_c`: entero en décimas de °C, puede ser negativo en pruebas.
- Índices 37 y 50 tienen 467 décimas, equivalentes a 46,7 °C. Se elige 37, SEN-038.
- Ambos métodos comparan 63 pares de claves. La recursiva visita 127 llamadas, 7 niveles. El bucle visita 64 lecturas y usa O(1) auxiliar.
- No se copian subarreglos: se pasan límites. Vacío devuelve −1, no una temperatura inventada.
- Pregunta: ¿por qué tiempo O(n) aunque profundidad O(log n)?

## E9 · Bloques, suma exacta con vuelta atrás

Seleccionar todos los subconjuntos que reservan exactamente 8 GB con recursos de uso único.

- Archivo `09_bloques_backtracking.csv`, 4 filas. `id`: único. `valor`: GB enteros estrictamente positivos.
- B1=2,B2=3,B3=5,B4=6. Meta 8: B1+B4 y B2+B3.
- 27 estados, 5 podas por resto negativo, 2 soluciones registradas, profundidad 5.
- Meta 0: conjunto vacío. Meta 7: B1+B3. `[2,4]` con meta 7: sin solución.
- IDs distintos representan recursos distintos aunque tengan igual valor.
- Poda segura: con capacidades positivas, un resto negativo ya no puede volver a 0. Se rechazan ceros y negativos; no extrapolar esta regla a otro dominio.
- Máximo 20 bloques. El espacio de todas las salidas puede ser exponencial.
- Pregunta: ¿por qué hay que copiar la selección al registrar una solución?

## E10 · Pedidos, selección óptima

Maximizar el beneficio de pedidos elegibles con 50 kg. Incluir cada pedido como máximo una vez y contrastar el valor con una enumeración sin poda.

| Campo | Tipo y significado |
|---|---|
| id | Texto único del pedido |
| ciudad | LIMA o AREQUIPA; elegibilidad requiere LIMA |
| prioridad | ALTA o BAJA; elegibilidad requiere ALTA |
| estado | LISTO o PENDIENTE; elegibilidad requiere LISTO |
| peso_kg | Entero positivo; suma no supera capacidad |
| beneficio_soles | Entero no negativo; objetivo a maximizar |

- Archivo `10_pedidos_optimos.csv`: 20 filas, 16 elegibles y 4 excluidas por reglas, sin truncamiento ni selección por conveniencia.
- Óptimo: P02,P04,P06,P08,P09,P13,P14; 50 kg y S/1901.
- Recursiva: 7608 llamadas, 5248 podas, 251 hojas factibles evaluadas, 17 niveles. Las podas se cuentan tanto por capacidad como por cota.
- Enumeración: 65536 máscaras, 4823 factibles. El número de hojas alcanzadas con poda no representa todas las soluciones factibles del universo.
- Referencia exacta sobre los **16 elegibles**; no es una afirmación sobre los 20 pedidos sin restricciones ni sobre un conjunto empresarial mayor.
- Si aumenta a más de 20 elegibles, se rechaza la carga. Para producción se requiere otro diseño y justificar cualquier reducción de dominio.
- Probar capacidad 0, sin ítems que quepan, beneficios empatados, ceros, grandes valores y capacidad menor.
- Pregunta: ¿por qué suma de beneficios restantes es una cota superior admisible?
