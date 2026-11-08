package structures.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import model.LecturaSensor;

public class GestorDeRed {

    // Reto 1 & 10: Historial de eventos (List)
    private final List<String> historialEventos;

    // Retos 2 & 3: Navegación de alertas (List / Deque)
    private final List<String> historialAlertasNavegacion;
    private int indiceAlertaActual;

    // Reto 4: Programación de consultas sin duplicados (Set)
    private final Set<String> consultasProgramadas;

    // Reto 5: Rotación de sensores (Queue)
    private final Queue<String> colaSensores;

    // Retos 6, 7 & 9: Grafo de adyacencia de la red de sensores (Map)
    private final Map<String, List<String>> redConexiones;

    // Reto 8: Ingesta y procesamiento de lecturas FIFO (Queue)
    private final Queue<LecturaSensor> colaLecturas;

    public GestorDeRed() {
        this.historialEventos = new ArrayList<>();
        this.historialAlertasNavegacion = new ArrayList<>();
        this.indiceAlertaActual = -1;
        this.consultasProgramadas = new LinkedHashSet<>();
        this.colaSensores = new LinkedList<>();
        this.redConexiones = new HashMap<>();
        this.colaLecturas = new LinkedList<>();
    }

    // =========================================================================
    // RETO 1: Historial de eventos
    // =========================================================================
    public void agregarEvento(String evento) {
        if (evento != null && !evento.trim().isEmpty()) {
            String registroConFecha = new Date() + " - " + evento;
            historialEventos.add(registroConFecha);
        }
    }

    public List<String> obtenerEventosRecientes(int limite) {
        if (historialEventos.isEmpty()) {
            return Collections.emptyList();
        }
        int inicio = Math.max(0, historialEventos.size() - limite);
        return new ArrayList<>(historialEventos.subList(inicio, historialEventos.size()));
    }

    // =========================================================================
    // RETOS 2 & 3: Navegación de alertas
    // =========================================================================
    public void registrarAlerta(String alerta) {
        if (alerta != null && !alerta.trim().isEmpty()) {
            historialAlertasNavegacion.add(alerta);
            if (indiceAlertaActual == -1) {
                indiceAlertaActual = 0;
            }
        }
    }

    public String navegarAlSiguiente() {
        if (historialAlertasNavegacion.isEmpty()) {
            return null;
        }
        if (indiceAlertaActual < historialAlertasNavegacion.size() - 1) {
            indiceAlertaActual++;
        }
        return historialAlertasNavegacion.get(indiceAlertaActual);
    }

    public String navegarAlAnterior() {
        if (historialAlertasNavegacion.isEmpty()) {
            return null;
        }
        if (indiceAlertaActual > 0) {
            indiceAlertaActual--;
        }
        return historialAlertasNavegacion.get(indiceAlertaActual);
    }

    // =========================================================================
    // RETO 4 & 5: Programación de consultas y Rotación de sensores
    // =========================================================================
    public boolean programarConsulta(String idSensor) {
        if (idSensor == null || idSensor.trim().isEmpty()) {
            return false;
        }
        boolean esNuevo = consultasProgramadas.add(idSensor);
        if (esNuevo) {
            colaSensores.offer(idSensor);
        }
        return esNuevo;
    }

    public String obtenerSiguienteSensor() {
        if (colaSensores.isEmpty()) {
            return null;
        }
        String siguiente = colaSensores.poll();
        colaSensores.offer(siguiente);
        return siguiente;
    }

    // =========================================================================
    // RETO 6 & 7: Topología de red y Consulta de vecinos
    // =========================================================================
    public void registrarConexion(String sensorA, String sensorB) {
        if (sensorA == null || sensorB == null || sensorA.equals(sensorB)) {
            return;
        }
        redConexiones.computeIfAbsent(sensorA, k -> new ArrayList<>());
        redConexiones.computeIfAbsent(sensorB, k -> new ArrayList<>());

        if (!redConexiones.get(sensorA).contains(sensorB)) {
            redConexiones.get(sensorA).add(sensorB);
        }
        if (!redConexiones.get(sensorB).contains(sensorA)) {
            redConexiones.get(sensorB).add(sensorA);
        }
    }

    public List<String> obtenerVecinos(String idSensor) {
        if (idSensor == null) {
            return Collections.emptyList();
        }
        return redConexiones.getOrDefault(idSensor, Collections.emptyList());
    }

    // =========================================================================
    // RETO 8: Procesamiento de lecturas FIFO
    // =========================================================================
    public void recibirLectura(LecturaSensor lectura) {
        if (lectura != null) {
            colaLecturas.offer(lectura);
        }
    }

    public LecturaSensor procesarSiguienteLectura() {
        return colaLecturas.poll();
    }

    // =========================================================================
    // RETO 9: Detección de estaciones aisladas
    // =========================================================================
    public List<String> detectarEstacionesAisladas() {
        List<String> aisladas = new ArrayList<>();
        for (Map.Entry<String, List<String>> entrada : redConexiones.entrySet()) {
            if (entrada.getValue() == null || entrada.getValue().isEmpty()) {
                aisladas.add(entrada.getKey());
            }
        }
        return aisladas;
    }

    // =========================================================================
    // RETO 10: Limpieza del historial
    // =========================================================================
    public void limpiarHistorialAntiguo(int mantener) {
        if (mantener <= 0) {
            historialEventos.clear();
            return;
        }
        while (historialEventos.size() > mantener) {
            historialEventos.remove(0);
        }
    }

    // Getters auxiliares
    public List<String> getHistorialEventos() {
        return historialEventos;
    }

    public Set<String> getConsultasProgramadas() {
        return consultasProgramadas;
    }

    public Queue<String> getColaSensores() {
        return colaSensores;
    }

    public Map<String, List<String>> getRedConexiones() {
        return redConexiones;
    }

    public Queue<LecturaSensor> getColaLecturas() {
        return colaLecturas;
    }
}