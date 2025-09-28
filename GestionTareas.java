import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.List;

public class GestionTareas {
    private static PriorityQueue<Tarea> colaTareas = new PriorityQueue<>((t1, t2) -> {
        return Integer.compare(t1.prioridad, t2.prioridad);
    });

    private static Scanner scanner = new Scanner(System.in);

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

    private static String seleccionarDepartamento() {
        System.out.println("\nSeleccione el departamento:");
        System.out.println("1. Limpieza");
        System.out.println("2. Cocina");
        System.out.println("3. Barista");

        int opcion = obtenerEnteroEnRango("Opción: ", 1, 3);

        switch (opcion) {
            case 1: return "Limpieza";
            case 2: return "Cocina";
            case 3: return "Barista";
            default: return "General";
        }
    }

    private static String seleccionarEmpleadoPorDepartamento(String departamento) {
        // Obtener empleados del departamento seleccionado
        List<GestionEmpleados.Empleado> empleados = GestionEmpleados.obtenerEmpleadosPorDepartamento(departamento);

        if (empleados.isEmpty()) {
            System.out.println("No hay empleados en el departamento de " + departamento);
            return obtenerStringNoVacio("Ingrese el nombre del empleado manualmente: ");
        }

        System.out.println("\nEmpleados en " + departamento + ":");
        for (int i = 0; i < empleados.size(); i++) {
            System.out.println((i + 1) + ". " + empleados.get(i).nombre + " (ID: " + empleados.get(i).id + ")");
        }

        System.out.println((empleados.size() + 1) + ". Ingresar nombre manualmente");

        int opcion = obtenerEnteroEnRango("Seleccione un empleado: ", 1, empleados.size() + 1);

        if (opcion <= empleados.size()) {
            return empleados.get(opcion - 1).nombre;
        } else {
            return obtenerStringNoVacio("Ingrese el nombre del empleado: ");
        }
    }

    public static void menuGestionTareas() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE TAREAS ===");
            System.out.println("1. Agregar tarea");
            System.out.println("2. Procesar tarea (mayor prioridad)");
            System.out.println("3. Mostrar todas las tareas");
            System.out.println("4. Buscar tarea por descripción");
            System.out.println("5. Calcular tiempo total de tareas pendientes");
            System.out.println("6. Mostrar cantidad de tareas por prioridad");
            System.out.println("7. Buscar tareas por empleado");
            System.out.println("8. Distribución de tareas (Divide y Vencerás)");
            System.out.println("9. Limpiar todas las tareas");
            System.out.println("0. Volver al menú principal");

            opcion = obtenerEnteroEnRango("Seleccione una opción: ", 0, 9);

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
                    buscarTareaPorDescripcion();
                    break;
                case 5:
                    calcularTiempoTotal();
                    break;
                case 6:
                    mostrarTareasPorPrioridad();
                    break;
                case 7:
                    buscarTareasPorEmpleado();
                    break;
                case 8:
                    distribuirTareasDivideVenceras();
                    break;
                case 9:
                    limpiarTareas();
                    break;
            }
        } while (opcion != 0);
    }

    private static void agregarTarea() {
        String descripcion = obtenerStringNoVacio("Descripción de la tarea: ");
        int prioridad = obtenerEnteroEnRango("Prioridad (1-5, donde 1 es la más alta): ", 1, 5);
        int tiempo = obtenerEnteroPositivo("Tiempo estimado (minutos): ");
        String departamento = seleccionarDepartamento();
        String empleado = seleccionarEmpleadoPorDepartamento(departamento);

        Tarea tarea = new Tarea(descripcion, prioridad, tiempo, departamento, empleado);
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

    private static void buscarTareaPorDescripcion() {
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
        int[] contadorPrioridades = new int[6];

        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        contarTareasPorPrioridadRecursivo(tareas, 0, contadorPrioridades);

        for (int i = 1; i <= 5; i++) {
            System.out.println("Prioridad " + i + ": " + contadorPrioridades[i] + " tareas");
        }

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

    private static void buscarTareasPorEmpleado() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }

        String empleado = obtenerStringNoVacio("Nombre del empleado: ");

        System.out.println("Tareas asignadas a " + empleado + ":");
        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        int encontradas = buscarTareasPorEmpleadoRecursivo(tareas, empleado, 0, 0);

        if (encontradas == 0) {
            System.out.println("No se encontraron tareas para este empleado.");
        } else {
            System.out.println("Total encontradas: " + encontradas + " tareas");
        }
    }

    private static int buscarTareasPorEmpleadoRecursivo(Tarea[] tareas, String empleado, int index, int contador) {
        if (index >= tareas.length) {
            return contador;
        }

        if (tareas[index].empleadoAsignado.equalsIgnoreCase(empleado)) {
            System.out.println("- " + tareas[index].descripcion + " (" + tareas[index].tiempoEstimado + " min)");
            return buscarTareasPorEmpleadoRecursivo(tareas, empleado, index + 1, contador + 1);
        }

        return buscarTareasPorEmpleadoRecursivo(tareas, empleado, index + 1, contador);
    }

    private static void distribuirTareasDivideVenceras() {
        if (colaTareas.isEmpty()) {
            System.out.println("No hay tareas para distribuir.");
            return;
        }

        System.out.println("=== DISTRIBUCIÓN DE TAREAS (DIVIDE Y VENCERÁS) ===");
        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        System.out.println("Total de tareas a distribuir: " + tareas.length);
        distribuirTareasRecursivo(tareas, 0, tareas.length - 1, 1);
    }

    private static void distribuirTareasRecursivo(Tarea[] tareas, int inicio, int fin, int nivel) {
        if (inicio >= fin) {
            System.out.printf("Nivel %d: Tarea individual: %s\n", nivel, tareas[inicio].descripcion);
            return;
        }

        int medio = (inicio + fin) / 2;

        System.out.printf("Nivel %d:\n", nivel);
        System.out.printf("  Grupo 1 (tareas %d-%d): %d tareas\n", inicio + 1, medio + 1, medio - inicio + 1);
        System.out.printf("  Grupo 2 (tareas %d-%d): %d tareas\n", medio + 2, fin + 1, fin - medio);

        int tiempoGrupo1 = calcularTiempoGrupo(tareas, inicio, medio, 0);
        int tiempoGrupo2 = calcularTiempoGrupo(tareas, medio + 1, fin, 0);

        System.out.printf("  Tiempo Grupo 1: %d min | Tiempo Grupo 2: %d min\n", tiempoGrupo1, tiempoGrupo2);

        distribuirTareasRecursivo(tareas, inicio, medio, nivel + 1);
        distribuirTareasRecursivo(tareas, medio + 1, fin, nivel + 1);
    }

    private static int calcularTiempoGrupo(Tarea[] tareas, int inicio, int fin, int acumulado) {
        if (inicio > fin) {
            return acumulado;
        }
        return calcularTiempoGrupo(tareas, inicio + 1, fin, acumulado + tareas[inicio].tiempoEstimado);
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

    // ========== CLASE TAREA ==========
    static class Tarea {
        String descripcion;
        int prioridad;
        int tiempoEstimado;
        String departamento;
        String empleadoAsignado;

        public Tarea(String descripcion, int prioridad, int tiempoEstimado, String departamento, String empleadoAsignado) {
            this.descripcion = descripcion;
            this.prioridad = prioridad;
            this.tiempoEstimado = tiempoEstimado;
            this.departamento = departamento;
            this.empleadoAsignado = empleadoAsignado;
        }

        @Override
        public String toString() {
            return String.format("[P%d] %s - %d min - Depto: %s - Empleado: %s",
                    prioridad, descripcion, tiempoEstimado, departamento, empleadoAsignado);
        }
    }

    // ========== MÉTODOS PÚBLICOS PARA DEMO ==========
    public static void agregarTareaDemo(Tarea tarea) {
        colaTareas.add(tarea);
    }

    public static PriorityQueue<Tarea> getColaTareas() {
        return colaTareas;
    }

    public static boolean hayTareasPendientes() {
        return !colaTareas.isEmpty();
    }

    public static Tarea verSiguienteTarea() {
        return colaTareas.peek();
    }
}
