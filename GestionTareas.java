import java.util.PriorityQueue;
import java.util.Scanner;

public class GestionTareas {
    private static PriorityQueue<Tarea> colaTareas = new PriorityQueue<>((t1, t2) -> {
        return Integer.compare(t1.prioridad, t2.prioridad);
    });

    private static Scanner scanner = new Scanner(System.in);

    // Métodos de validación robustos
    private static int obtenerEnteroValido(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número entero válido.");
            }
        }
    }

    private static int obtenerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int valor = obtenerEnteroValido(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            } else {
                System.out.printf("Error: El valor debe estar entre %d y %d.\n", min, max);
            }
        }
    }

    private static int obtenerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = obtenerEnteroValido(mensaje);
            if (valor > 0) {
                return valor;
            } else {
                System.out.println("Error: El valor debe ser positivo.");
            }
        }
    }

    private static String obtenerStringNoVacio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Error: Este campo no puede estar vacío.");
            }
        }
    }

    public static void menuGestionTareas() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE TAREAS (COLA DE PRIORIDADES) ===");
            System.out.println("1. Agregar tarea");
            System.out.println("2. Procesar tarea (mayor prioridad)");
            System.out.println("3. Mostrar todas las tareas");
            System.out.println("4. Buscar tarea por descripción");
            System.out.println("5. Calcular tiempo total de tareas pendientes");
            System.out.println("6. Mostrar cantidad de tareas por prioridad");
            System.out.println("7. Limpiar todas las tareas");
            System.out.println("0. Volver al menú principal");

            opcion = obtenerEnteroEnRango("Seleccione una opción: ", 0, 7);

            switch (opcion) {
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                case 1:
                    agregarTarea();
                    break;
                case 2:
                    procesarTarea();
                    break;
                case 3:
                    mostrarTareasRecursivo();
                    break;
                case 4:
                    buscarTarea();
                    break;
                case 5:
                    calcularTiempoTotal();
                    break;
                case 6:
                    mostrarTareasPorPrioridad();
                    break;
                case 7:
                    limpiarTareas();
                    break;
            }
        } while (opcion != 0);
    }

    private static void agregarTarea() {
        String descripcion = obtenerStringNoVacio("Descripción de la tarea: ");
        int prioridad = obtenerEnteroEnRango("Prioridad (1-5, donde 1 es la más alta): ", 1, 5);
        int tiempo = obtenerEnteroPositivo("Tiempo estimado (minutos): ");
        String departamento = obtenerStringNoVacio("Departamento responsable: ");

        Tarea tarea = new Tarea(descripcion, prioridad, tiempo, departamento);
        colaTareas.add(tarea);
        System.out.println("✓ Tarea agregada correctamente: " + tarea);
    }

    private static void procesarTarea() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }

        Tarea tarea = colaTareas.poll();
        System.out.println("✓ Tarea procesada: " + tarea);
        System.out.println("Tareas pendientes: " + colaTareas.size());
    }

    private static void mostrarTareasRecursivo() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }

        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        System.out.println("\n=== TAREAS PENDIENTES ===");
        System.out.println("Total de tareas: " + tareas.length);
        System.out.println("-------------------------");
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
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }

        String descripcion = obtenerStringNoVacio("Ingrese la descripción a buscar: ");

        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        int encontradas = buscarTareaRecursivo(tareas, descripcion, 0, 0);

        if (encontradas == 0) {
            System.out.println("No se encontraron tareas con la descripción: '" + descripcion + "'");
        } else {
            System.out.println("Total de tareas encontradas: " + encontradas);
        }
    }

    private static int buscarTareaRecursivo(Tarea[] tareas, String descripcion, int index, int contador) {
        if (index >= tareas.length) {
            return contador;
        }

        if (tareas[index].descripcion.toLowerCase().contains(descripcion.toLowerCase())) {
            System.out.println("✓ Tarea encontrada: " + tareas[index]);
            return buscarTareaRecursivo(tareas, descripcion, index + 1, contador + 1);
        }

        return buscarTareaRecursivo(tareas, descripcion, index + 1, contador);
    }

    private static void calcularTiempoTotal() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }

        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        int tiempoTotal = calcularTiempoTotalRecursivo(tareas, 0, 0);
        System.out.println("Tiempo total estimado para " + tareas.length + " tareas pendientes: " + tiempoTotal + " minutos");

        // Calcular tiempo en horas y minutos
        int horas = tiempoTotal / 60;
        int minutos = tiempoTotal % 60;
        if (horas > 0) {
            System.out.println("Equivalente a: " + horas + " horas y " + minutos + " minutos");
        }
    }

    private static int calcularTiempoTotalRecursivo(Tarea[] tareas, int index, int acumulado) {
        if (index >= tareas.length) {
            return acumulado;
        }
        return calcularTiempoTotalRecursivo(tareas, index + 1, acumulado + tareas[index].tiempoEstimado);
    }

    private static void mostrarTareasPorPrioridad() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }

        System.out.println("\n=== TAREAS POR PRIORIDAD ===");
        int[] contadorPrioridades = new int[6]; // Índices 1-5 para prioridades

        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        contarTareasPorPrioridadRecursivo(tareas, 0, contadorPrioridades);

        for (int i = 1; i <= 5; i++) {
            System.out.println("Prioridad " + i + ": " + contadorPrioridades[i] + " tareas");
        }

        // Mostrar la prioridad con más tareas
        int maxPrioridad = encontrarPrioridadConMasTareas(contadorPrioridades, 1, 1, contadorPrioridades[1]);
        System.out.println("Prioridad con más tareas: " + maxPrioridad + " (" + contadorPrioridades[maxPrioridad] + " tareas)");
    }

    private static void contarTareasPorPrioridadRecursivo(Tarea[] tareas, int index, int[] contador) {
        if (index >= tareas.length) {
            return;
        }

        int prioridad = tareas[index].prioridad;
        if (prioridad >= 1 && prioridad <= 5) {
            contador[prioridad]++;
        }

        contarTareasPorPrioridadRecursivo(tareas, index + 1, contador);
    }

    private static int encontrarPrioridadConMasTareas(int[] contador, int index, int maxIndex, int maxValor) {
        if (index > 5) {
            return maxIndex;
        }

        if (contador[index] > maxValor) {
            return encontrarPrioridadConMasTareas(contador, index + 1, index, contador[index]);
        }

        return encontrarPrioridadConMasTareas(contador, index + 1, maxIndex, maxValor);
    }

    private static void limpiarTareas() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas ya está vacía.");
            return;
        }

        int cantidad = colaTareas.size();
        colaTareas.clear();
        System.out.println("✓ Se eliminaron " + cantidad + " tareas de la cola.");
    }

    // Método adicional para obtener estadísticas de la cola
    public static void mostrarEstadisticas() {
        if (colaTareas.isEmpty()) {
            System.out.println("No hay tareas en la cola.");
            return;
        }

        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        int tiempoTotal = calcularTiempoTotalRecursivo(tareas, 0, 0);

        System.out.println("\n=== ESTADÍSTICAS DE TAREAS ===");
        System.out.println("Total de tareas: " + tareas.length);
        System.out.println("Tiempo total estimado: " + tiempoTotal + " minutos");

        // Encontrar la tarea más larga
        Tarea tareaMasLarga = encontrarTareaMasLargaRecursivo(tareas, 0, tareas[0]);
        System.out.println("Tarea más larga: " + tareaMasLarga.descripcion + " (" + tareaMasLarga.tiempoEstimado + " min)");

        // Encontrar la tarea de mayor prioridad
        Tarea tareaMayorPrioridad = encontrarTareaMayorPrioridadRecursivo(tareas, 0, tareas[0]);
        System.out.println("Tarea de mayor prioridad: " + tareaMayorPrioridad.descripcion + " (Prioridad: " + tareaMayorPrioridad.prioridad + ")");
    }

    private static Tarea encontrarTareaMasLargaRecursivo(Tarea[] tareas, int index, Tarea maxTarea) {
        if (index >= tareas.length) {
            return maxTarea;
        }

        if (tareas[index].tiempoEstimado > maxTarea.tiempoEstimado) {
            return encontrarTareaMasLargaRecursivo(tareas, index + 1, tareas[index]);
        }

        return encontrarTareaMasLargaRecursivo(tareas, index + 1, maxTarea);
    }

    private static Tarea encontrarTareaMayorPrioridadRecursivo(Tarea[] tareas, int index, Tarea minTarea) {
        if (index >= tareas.length) {
            return minTarea;
        }

        if (tareas[index].prioridad < minTarea.prioridad) {
            return encontrarTareaMayorPrioridadRecursivo(tareas, index + 1, tareas[index]);
        }

        return encontrarTareaMayorPrioridadRecursivo(tareas, index + 1, minTarea);
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
            return String.format("[P%d] %s - %d min - Depto: %s",
                    prioridad, descripcion, tiempoEstimado, departamento);
        }
    }

    // Método para obtener la cola de tareas (para integración con otros módulos)
    public static PriorityQueue<Tarea> getColaTareas() {
        return colaTareas;
    }

    // Método para verificar si hay tareas pendientes
    public static boolean hayTareasPendientes() {
        return !colaTareas.isEmpty();
    }

    // Método para obtener la siguiente tarea sin procesarla
    public static Tarea verSiguienteTarea() {
        return colaTareas.peek();
    }
}
