package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.LecturaSensor;

@SuppressWarnings("unused")
public class AlmacenTexto {

    private final String rutaArchivo;
    private static final String SEPARADOR = ",";

    public AlmacenTexto(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void guardar(List<LecturaSensor> lecturas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (LecturaSensor lectura : lecturas) {
                String linea = String.join(SEPARADOR,
                        lectura.getIdSensor(),
                        lectura.getTimestamp(),
                        String.valueOf(lectura.getTemperatura()),
                        String.valueOf(lectura.getHumedad()),
                        String.valueOf(lectura.getPm25())
                );
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    public List<LecturaSensor> cargar() throws IOException {
        List<LecturaSensor> lecturas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(SEPARADOR);
                if (partes.length == 5) {
                    LecturaSensor lectura = new LecturaSensor(
                            partes[0],
                            partes[1],
                            Double.parseDouble(partes[2]),
                            Double.parseDouble(partes[3]),
                            Double.parseDouble(partes[4])
                    );
                    lecturas.add(lectura);
                }
            }
        }
        return lecturas;
    }

    private String sanitizar(String texto) {
        if (texto == null) return "";
        return texto.replace(SEPARADOR, " ");
    }
}