package experiment;

import generator.GeneradorDatos;
import model.LecturaSensor;
import repository.Repositorio;
import repository.RepositorioArreglo;
import repository.RepositorioEnlazado;

public class BancoDeListas {

    public static void main(String[] args) {
        experimentoUno();
        experimentoDos();
        experimentoTres();
        experimentoCuatro();
        experimentoCinco();
    }

    // ---------- EXPERIMENTO 1 ----------

    private static void experimentoUno() {
        System.out.println("=== EXP 1: EL MISMO CODIGO, DOS MECANISMOS ===");
        LecturaSensor[] datos = GeneradorDatos.generar(5);

        Repositorio[] repositorios = { new RepositorioArreglo(), new RepositorioEnlazado() };

        for (Repositorio repo : repositorios) {
            for (LecturaSensor l : datos) {
                repo.agregarAlFinal(l);
            }
            System.out.printf("%-16s tamano: %d   primera: %s%n",
                    repo.getClass().getSimpleName(), repo.tamano(), repo.obtener(0).getTimestamp());
        }
        System.out.println();
    }

    // ---------- EXPERIMENTO 2 ----------

    private static void experimentoDos() {
        System.out.println("=== EXP 2: LA CUENTA QUE NO CUADRA ===");
        RepositorioEnlazado lista = new RepositorioEnlazado();
        for (LecturaSensor l : GeneradorDatos.generar(10)) {
            lista.agregarAlFinal(l);
        }

        System.out.println("Antes de eliminar:");
        System.out.println("  tamano() dice:       " + lista.tamano());
        System.out.println("  nodos que hay:       " + lista.contarRecorriendo());

        lista.eliminar(5);

        System.out.println("Despues de eliminar la posicion 5:");
        System.out.println("  tamano() dice:       " + lista.tamano());
        System.out.println("  nodos que hay:       " + lista.contarRecorriendo());
        System.out.println();
        System.out.println("Se pidio eliminar UNA lectura. Cuenta cuantas desaparecieron.");
        System.out.println();
    }

    // ---------- EXPERIMENTO 3 ----------

    private static void experimentoTres() {
        System.out.println("=== EXP 3: INSERTAR AL INICIO ===");
        int[] tamanos = {10_000, 50_000, 100_000};
        System.out.printf("%10s | %18s | %18s%n", "lecturas", "ARREGLO (ms)", "LISTA ENLAZADA (ms)");

        for (int n : tamanos) {
            LecturaSensor[] datos = GeneradorDatos.generar(n);

            // Arreglo
            RepositorioArreglo arr = new RepositorioArreglo();
            long inicioArr = System.currentTimeMillis();
            for (LecturaSensor l : datos) {
                arr.agregarAlInicio(l);
            }
            long tiempoArr = System.currentTimeMillis() - inicioArr;

            // Lista Enlazada
            RepositorioEnlazado lst = new RepositorioEnlazado();
            long inicioLst = System.currentTimeMillis();
            for (LecturaSensor l : datos) {
                lst.agregarAlInicio(l);
            }
            long tiempoLst = System.currentTimeMillis() - inicioLst;

            System.out.printf("%,10d | %,16d ms | %,17d ms%n", n, tiempoArr, tiempoLst);
        }
        System.out.println();
    }

    // ---------- EXPERIMENTO 4 ----------

    private static void experimentoCuatro() {
        System.out.println("=== EXP 4: ACCESO ALEATORIO vs RECORRIDO ===");
        int n = 50_000;
        LecturaSensor[] datos = GeneradorDatos.generar(n);

        RepositorioArreglo arr = new RepositorioArreglo();
        RepositorioEnlazado lst = new RepositorioEnlazado();

        for (LecturaSensor l : datos) {
            arr.agregarAlFinal(l);
            lst.agregarAlFinal(l);
        }

        long inicioArr = System.currentTimeMillis();
        for (int i = 0; i < n; i += 100) {
            arr.obtener(i);
        }
        long tiempoArr = System.currentTimeMillis() - inicioArr;

        long inicioLst = System.currentTimeMillis();
        for (int i = 0; i < n; i += 100) {
            lst.obtener(i);
        }
        long tiempoLst = System.currentTimeMillis() - inicioLst;

        System.out.printf("Acceso a elementos cada 100 posiciones (n=%,d):%n", n);
        System.out.printf("  Arreglo:        %4d ms%n", tiempoArr);
        System.out.printf("  Lista Enlazada: %4d ms%n%n", tiempoLst);
    }

    // ---------- EXPERIMENTO 5 ----------

    private static void experimentoCinco() {
        System.out.println("=== EXP 5: ELIMINAR DEL INICIO ===");
        int n = 20_000;
        LecturaSensor[] datos = GeneradorDatos.generar(n);

        RepositorioArreglo arr = new RepositorioArreglo();
        RepositorioEnlazado lst = new RepositorioEnlazado();

        for (LecturaSensor l : datos) {
            arr.agregarAlFinal(l);
            lst.agregarAlFinal(l);
        }

        long inicioArr = System.currentTimeMillis();
        while (arr.tamano() > 0) {
            arr.eliminar(0);
        }
        long tiempoArr = System.currentTimeMillis() - inicioArr;

        long inicioLst = System.currentTimeMillis();
        while (lst.tamano() > 0) {
            lst.eliminar(0);
        }
        long tiempoLst = System.currentTimeMillis() - inicioLst;

        System.out.printf("Eliminar todo desde el inicio (n=%,d):%n", n);
        System.out.printf("  Arreglo:        %4d ms%n", tiempoArr);
        System.out.printf("  Lista Enlazada: %4d ms%n%n", tiempoLst);
    }
}