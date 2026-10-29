package experiment;

import generator.GeneradorDatos;
import model.LecturaSensor;
import sorting.Ordenador;
import java.util.Random;

public class BancoDeOrdenamiento {

    public static void main(String[] args) {
        experimentoUno();
        experimentoDos();
        experimentoTres();
        experimentoCuatro();
    }

    // ---------- METODOS AUXILIARES ----------

    private static LecturaSensor[] copiar(LecturaSensor[] original) {
        return original.clone();
    }

    private static LecturaSensor[] desordenar(LecturaSensor[] original) {
        LecturaSensor[] copia = original.clone();
        Random r = new Random(777L);
        for (int i = copia.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            LecturaSensor tmp = copia[i];
            copia[i] = copia[j];
            copia[j] = tmp;
        }
        return copia;
    }

    private static void reportar(String nombre, long tiempo) {
        System.out.printf("  %-12s: %5d ms%n", nombre, tiempo);
    }

    // ---------- EXPERIMENTO 1 ----------

    private static void experimentoUno() {
        System.out.println("=== EXP 1: ALGORITMOS SIMPLES CON DATOS DESORDENADOS ===");
        LecturaSensor[] base = desordenar(GeneradorDatos.generar(10_000));

        LecturaSensor[] a = copiar(base);
        long t = System.currentTimeMillis();
        Ordenador.burbuja(a);
        reportar("Burbuja", System.currentTimeMillis() - t);

        LecturaSensor[] b = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.seleccion(b);
        reportar("Seleccion", System.currentTimeMillis() - t);

        LecturaSensor[] c = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.insercion(c);
        reportar("Insercion", System.currentTimeMillis() - t);
        System.out.println();
    }

    // ---------- EXPERIMENTO 2 ----------

    private static void experimentoDos() {
        System.out.println("=== EXP 2: LOS MISMOS TRES, PERO CON DATOS YA ORDENADOS ===");
        System.out.println("(asi es como llegan de la red de sensores: en orden cronologico)");
        LecturaSensor[] base = GeneradorDatos.generar(10_000);

        LecturaSensor[] a = copiar(base);
        long t = System.currentTimeMillis();
        Ordenador.burbuja(a);
        reportar("Burbuja", System.currentTimeMillis() - t);

        LecturaSensor[] b = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.seleccion(b);
        reportar("Seleccion", System.currentTimeMillis() - t);

        LecturaSensor[] c = copiar(base);
        t = System.currentTimeMillis();
        Ordenador.insercion(c);
        reportar("Insercion", System.currentTimeMillis() - t);
        System.out.println();
    }

    // ---------- EXPERIMENTO 3 ----------

    private static void experimentoTres() {
        System.out.println("=== EXP 3: SIMPLES CONTRA AVANZADOS ===");
        int[] tamanos = {1_000, 10_000, 100_000};

        for (int n : tamanos) {
            System.out.println("-- " + String.format("%,d", n) + " lecturas desordenadas --");
            LecturaSensor[] base = desordenar(GeneradorDatos.generar(n));

            LecturaSensor[] a = copiar(base);
            long t = System.currentTimeMillis();
            Ordenador.insercion(a);
            reportar("Insercion", System.currentTimeMillis() - t);

            LecturaSensor[] b = copiar(base);
            t = System.currentTimeMillis();
            Ordenador.mergeSort(b);
            reportar("MergeSort", System.currentTimeMillis() - t);

            LecturaSensor[] c = copiar(base);
            t = System.currentTimeMillis();
            Ordenador.heapSort(c);
            reportar("HeapSort", System.currentTimeMillis() - t);
            System.out.println();
        }
    }

    // ---------- EXPERIMENTO 4 ----------

    private static void experimentoCuatro() {
        System.out.println("=== EXP 4: QUICKSORT CON PIVOTE = PRIMER ELEMENTO ===");

        System.out.println("-- Caso A: 50.000 lecturas DESORDENADAS --");
        LecturaSensor[] revueltas = desordenar(GeneradorDatos.generar(50_000));
        long t = System.currentTimeMillis();
        Ordenador.quickSortPivotePrimero(revueltas);
        reportar("QuickSort", System.currentTimeMillis() - t);
        System.out.println();
    }
}