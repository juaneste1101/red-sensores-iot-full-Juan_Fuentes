# Criterio de Decisión Teórica - Semana 6

## 1. ArrayList vs. LinkedList para eliminación frecuente de eventos antiguos
**Elección:** `LinkedList` (o en su defecto `ArrayDeque`).

**Justificación técnica:**
- En un **`ArrayList`**, los elementos se almacenan en un arreglo contiguo en memoria. Eliminar el evento más antiguo (ubicado en el índice 0) implica un costo temporal de **$O(n)$**, ya que todos los elementos restantes deben desplazarse un espacio a la izquierda (*shift*) para cubrir el hueco.
- En una **`LinkedList`**, cada elemento es un nodo con referencias al siguiente y anterior. La eliminación del primer nodo (`removeFirst()` / `remove(0)`) consiste únicamente en reasignar el puntero de la cabeza (`head`), lo que se ejecuta en tiempo constante **$O(1)$**.
- **Conclusión:** Si la operación principal sobre el historial es eliminar constantemente los registros más antiguos (purgar la cabeza), `LinkedList` evita el sobrecosto de reordenamiento de memoria que sufre `ArrayList`.

---

## 2. Deque vs. Queue y su relación con el NodoDoble
**Diferencias estructurales:**
- **`Queue` (Cola FIFO):** Interfaz restringida a un solo sentido de flujo. Los elementos ingresan únicamente por el final (*tail*) mediante `offer()`/`add()` y se extraen por el inicio (*head*) mediante `poll()`/`remove()`.
- **`Deque` (Double-Ended Queue):** Interfaz bidireccional que permite la inserción, consulta y eliminación de elementos por **ambos extremos** (cabeza y cola) mediante métodos como `addFirst()`, `addLast()`, `pollFirst()` y `pollLast()`.

**Relación con `NodoDoble`:**
- La estructura teórica de un `NodoDoble` almacena un dato y dos punteros: uno apuntando al nodo `siguiente` y otro al nodo `anterior`.
- Un `Deque` requiere internamente una estructura de punteros dobles para garantizar que el desplazamiento y eliminación en ambas direcciones (hacia adelante y hacia atrás) se ejecute en costo **$O(1)$**, siendo la abstracción directa del comportamiento bidireccional que ofrece un `NodoDoble`.

---

## 3. Colas y equidad en el monitoreo de sensores vs. Pila (Stack)
**Por qué la cola (`Queue`) es apropiada:**
- Garantiza el principio de **equidad (*Fairness*)** mediante el comportamiento **FIFO (First-In, First-Out)**. Las lecturas de los sensores se procesan en el orden estricto en que fueron capturadas y transmitidas, respetando la secuencia cronológica real de la red IoT.

**Efecto de usar una Pila (`Stack` / LIFO):**
- Una pila opera bajo la modalidad **LIFO (Last-In, First-Out)**, donde el último dato ingresado es el primero en ser procesado.
- En un escenario de alto tráfico de sensores, las lecturas viejas quedarían atrapadas en el fondo de la pila mientras las lecturas nuevas continúan llegando. Esto genera un fenómeno de **inanición de datos (*starvation*)**, impidiendo que los eventos críticos del pasado sean analizados a tiempo.

---

## 4. Lista de adyacencia (`Map<String, List<String>>`) vs. Matriz de adyacencia
**Relación conceptual:**
La estructura `Map<String, List<String>>` es la implementación directa en Java de una **lista de adyacencia**, donde la clave representa el identificador del sensor origen y el valor es la lista de identificadores de sus sensores vecinos.

**Ventajas frente a la matriz de adyacencia:**
1. **Eficiencia en memoria ($O(V + E)$):** En redes de sensores reales la matriz es *dispersa* (la mayoría de sensores solo se conectan con 2 o 3 vecinos cercanos). La lista de adyacencia solo consume memoria para las conexiones existentes, mientras que la matriz reserva un espacio cuadrático **$O(V^2)$**, desperdiciando memoria con entradas nulas/cero.
2. **Escalabilidad dinámica:** Permite agregar o remover nodos de la red en tiempo de ejecución sin necesidad de redimensionar arreglos bidimensionales estáticos.

**Desventajas:**
- Comprobar si existe una conexión directa entre dos sensores específicos $A$ y $B$ requiere buscar $B$ en la lista de $A$, lo que toma costo **$O(\text{grado de } A)$**. En una matriz de adyacencia, esa verificación es directa en **$O(1)$** mediante `matriz[i][j]`.

---

## 5. ArrayDeque vs. ConcurrentLinkedQueue en escenarios multihilo
**Concepto de Seguridad para Concurrencia (*Thread-Safety*):**
*Thread-safety* es la propiedad de una estructura de datos que garantiza su correcto funcionamiento cuando es accedida y modificada simultáneamente por múltiples hilos de ejecución (*threads*), evitando condiciones de carrera (*race conditions*) y corrupción de estado sin requerir bloqueos externos manuales.

**Diferencia relevante y elección:**
- **`ArrayDeque`:** **No es thread-safe**. Si un hilo de ingesta inserta datos mientras otro hilo de procesamiento los remueve, la estructura colapsa lanzando excepciones de tipo `ConcurrentModificationException` o corrompiendo sus índices internos.
- **`ConcurrentLinkedQueue`:** **Es thread-safe y no bloqueante (*lock-free*)**. Utiliza algoritmos atómicos basados en instrucciones de hardware **CAS (Compare-And-Swap)**. Permite que múltiples hilos de sensores encolen lecturas y múltiples trabajadores procesen datos en paralelo de forma segura y sin cuellos de botella por bloqueos de hilo (`synchronized`).