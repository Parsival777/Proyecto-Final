
import java.util.PriorityQueue;
import java.util.Scanner;

public class GestionTareas {
    private static PriorityQueue<Tarea> colaTareas = new PriorityQueue<>((t1, t2) -> {
        return Integer.compare(t1.prioridad, t2.prioridad);
    });

    private static Scanner scanner = new Scanner(System.in);

    public static void menuGestionTareas() {
        System.out.println("\n=== GESTIÓN DE TAREAS (COLA DE PRIORIDADES) ===");
        System.out.println("1. Agregar tarea");
        System.out.println("2. Procesar tarea (mayor prioridad)");
        System.out.println("3. Mostrar todas las tareas");
        System.out.println("4. Buscar tarea por descripción");
        System.out.println("5. Calcular tiempo total de tareas pendientes");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        procesarOpcionTareas(scanner.nextLine().trim());
    }

    private static void procesarOpcionTareas(String input) {
        procesarOpcionRecursiva(input);
    }

    private static void procesarOpcionRecursiva(String input) {
        if (input.equals("0")) {
            System.out.println("Volviendo al menú principal...");
            return;
        }

        if (input.equals("1")) {
            agregarTarea();
            menuGestionTareas();
            return;
        }

        if (input.equals("2")) {
            procesarTarea();
            menuGestionTareas();
            return;
        }

        if (input.equals("3")) {
            mostrarTareasRecursivo();
            menuGestionTareas();
            return;
        }

        if (input.equals("4")) {
            buscarTarea();
            menuGestionTareas();
            return;
        }

        if (input.equals("5")) {
            calcularTiempoTotal();
            menuGestionTareas();
            return;
        }

        System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 5.");
        menuGestionTareas();
    }

    private static void agregarTarea() {
        System.out.print("Descripción de la tarea: ");
        String descripcion = obtenerEntradaNoVaciaRecursivo("Descripción de la tarea: ", scanner.nextLine().trim());

        System.out.print("Prioridad (1-5, donde 1 es la más alta): ");
        int prioridad = obtenerEnteroEnRangoRecursivo(1, 5, "Prioridad (1-5, donde 1 es la más alta): ", scanner.nextLine().trim(), 0);

        System.out.print("Tiempo estimado (minutos): ");
        int tiempo = obtenerEnteroPositivoRecursivo("Tiempo estimado (minutos): ", scanner.nextLine().trim(), 0);

        System.out.print("Departamento responsable: ");
        String departamento = obtenerEntradaNoVaciaRecursivo("Departamento responsable: ", scanner.nextLine().trim());

        Tarea tarea = new Tarea(descripcion, prioridad, tiempo, departamento);
        colaTareas.add(tarea);
        System.out.println("Tarea agregada: " + tarea);
    }

    private static void procesarTarea() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }
        
        Tarea tarea = colaTareas.poll();
        System.out.println("Tarea procesada: " + tarea);
    }

    private static void mostrarTareasRecursivo() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }
        
        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        System.out.println("Tareas en la cola:");
        mostrarTareasRecursivo(tareas, 0);
    }

    private static void mostrarTareasRecursivo(Tarea[] tareas, int index) {
        if (index >= tareas.length) {
            return;
        }

        System.out.println((index + 1) + ". " + tareas[index]);
        mostrarTareasRecursivo(tareas, index + 1);
    }

    private static void buscarTarea() {
        System.out.print("Ingrese la descripción a buscar: ");
        String descripcion = obtenerEntradaNoVaciaRecursivo("Ingrese la descripción a buscar: ", scanner.nextLine().trim());

        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        boolean encontrado = buscarTareaRecursivo(tareas, descripcion, 0);

        if (!encontrado) {
            System.out.println("Tarea no encontrada.");
        }
    }

    private static boolean buscarTareaRecursivo(Tarea[] tareas, String descripcion, int index) {
        if (index >= tareas.length) {
            return false;
        }

        if (tareas[index].descripcion.toLowerCase().contains(descripcion.toLowerCase())) {
            System.out.println("Tarea encontrada: " + tareas[index]);
            return true;
        }

        return buscarTareaRecursivo(tareas, descripcion, index + 1);
    }

    private static void calcularTiempoTotal() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }
        
        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        int tiempoTotal = calcularTiempoTotalRecursivo(tareas, 0, 0);
        System.out.println("Tiempo total estimado para tareas pendientes: " + tiempoTotal + " minutos");
    }

    private static int calcularTiempoTotalRecursivo(Tarea[] tareas, int index, int acumulado) {
        if (index >= tareas.length) {
            return acumulado;
        }
        return calcularTiempoTotalRecursivo(tareas, index + 1, acumulado + tareas[index].tiempoEstimado);
    }

    private static String obtenerEntradaNoVaciaRecursivo(String mensaje, String input) {
        if (input.isEmpty()) {
            System.out.print("Este campo no puede estar vacío. " + mensaje);
            return obtenerEntradaNoVaciaRecursivo(mensaje, scanner.nextLine().trim());
        }
        return input;
    }

    private static int obtenerEnteroEnRangoRecursivo(int min, int max, String mensaje, String input, int intentos) {
        if (intentos > 10) return min;

        boolean esNumero = verificarEsNumero(input, 0, true);

        if (!esNumero) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerEnteroEnRangoRecursivo(min, max, mensaje, scanner.nextLine().trim(), intentos + 1);
        }

        int valor = convertirStringAInt(input, 0, 0);

        if (valor >= min && valor <= max) {
            return valor;
        }

        System.out.print("Por favor, ingrese un número entre " + min + " y " + max + ". " + mensaje);
        return obtenerEnteroEnRangoRecursivo(min, max, mensaje, scanner.nextLine().trim(), 0);
    }

    private static int obtenerEnteroPositivoRecursivo(String mensaje, String input, int intentos) {
        if (intentos > 10) return 1;

        boolean esNumero = verificarEsNumero(input, 0, true);

        if (!esNumero) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerEnteroPositivoRecursivo(mensaje, scanner.nextLine().trim(), intentos + 1);
        }

        int valor = convertirStringAInt(input, 0, 0);

        if (valor <= 0) {
            System.out.print("Por favor, ingrese un número positivo. " + mensaje);
            return obtenerEnteroPositivoRecursivo(mensaje, scanner.nextLine().trim(), intentos + 1);
        }

        return valor;
    }

    private static boolean verificarEsNumero(String str, int index, boolean esNumero) {
        if (index >= str.length()) return esNumero;
        if (!esNumero) return false;

        char c = str.charAt(index);
        boolean esDigito = (c >= '0' && c <= '9') || (index == 0 && c == '-');

        return verificarEsNumero(str, index + 1, esNumero && esDigito);
    }

    private static int convertirStringAInt(String str, int index, int resultado) {
        if (index >= str.length()) return resultado;

        char c = str.charAt(index);
        if (c == '-') {
            return -convertirStringAInt(str, index + 1, 0);
        }

        int digito = c - '0';
        int nuevoResultado = resultado * 10 + digito;

        return convertirStringAInt(str, index + 1, nuevoResultado);
    }

    static class Tarea {
        String descripcion;
        int prioridad;
        int tiempoEstimado;
        String departamento;

        public Tarea(String descripcion, int prioridad, int tiempoEstimado, String departamento) {
            this.descripcion = descripcion;
            this.prioridad = prioridad;
            this.tiempoEstimado = tiempoEstimado;
            this.departamento = departamento;
        }

        @Override
        public String toString() {
            return "Tarea{" + "descripción='" + descripcion + '\'' + ", prioridad=" + prioridad + 
                   ", tiempo=" + tiempoEstimado + "min, departamento='" + departamento + "'}";
        }
    }
}