import java.util.Scanner;

public class EstadisticasTareas {
    private static int[] tiemposTareas = new int[100];
    private static int contadorTareas = 0;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void menuEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS DE TAREAS (RECURSIVIDAD) ===");
        System.out.println("1. Agregar tiempo de tarea");
        System.out.println("2. Calcular tiempo total estimado");
        System.out.println("3. Calcular tiempo promedio");
        System.out.println("4. Encontrar tiempo máximo");
        System.out.println("5. Encontrar tiempo mínimo");
        System.out.println("6. Distribuir tareas en proyectos grandes");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        
        procesarOpcionEstadisticas(scanner.nextLine().trim());
    }
    
    private static void procesarOpcionEstadisticas(String input) {
        procesarOpcionRecursiva(input);
    }
    
    private static void procesarOpcionRecursiva(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));
        
        procesarOpcion(input, "1", EstadisticasTareas::agregarTiempoTarea);
        procesarOpcion(input, "2", EstadisticasTareas::calcularTiempoTotal);
        procesarOpcion(input, "3", EstadisticasTareas::calcularTiempoPromedio);
        procesarOpcion(input, "4", EstadisticasTareas::encontrarTiempoMaximo);
        procesarOpcion(input, "5", EstadisticasTareas::encontrarTiempoMinimo);
        procesarOpcion(input, "6", EstadisticasTareas::distribuirTareas);
        
        procesarCasoDefault(input);
    }
    
    private static void procesarCasoBase(String input, String target, Runnable action) {
        if (input.equals(target)) {
            action.run();
        }
    }
    
    private static void procesarOpcion(String input, String opcion, Runnable funcion) {
        if (input.equals(opcion)) {
            funcion.run();
            menuEstadisticas();
        }
    }
    
    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4", "5", "6"}, 0);
        
        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 6.");
            menuEstadisticas();
        }
    }
    
    private static boolean verificarOpcionValida(String input, String[] opciones, int index) {
        if (index >= opciones.length) return false;
        if (input.equals(opciones[index])) return true;
        return verificarOpcionValida(input, opciones, index + 1);
    }
    
    private static void agregarTiempoTarea() {
        manejarCapacidadTareas();
    }
    
    private static void manejarCapacidadTareas() {
        if (contadorTareas >= tiemposTareas.length) {
            System.out.println("No se pueden agregar más tareas.");
            return;
        }
        
        System.out.print("Tiempo estimado para completar la tarea (horas): ");
        int tiempo = obtenerEnteroPositivoRecursivo("Tiempo estimado para completar la tarea (horas): ", scanner.nextLine().trim(), 0);
        
        tiemposTareas[contadorTareas++] = tiempo;
        System.out.println("Tiempo agregado: " + tiempo + " horas");
    }
    
    private static void calcularTiempoTotal() {
        manejarTareasVacias(() -> {
            int total = sumaRecursiva(tiemposTareas, contadorTareas - 1);
            System.out.println("Tiempo total estimado: " + total + " horas");
        });
    }
    
    private static int sumaRecursiva(int[] array, int index) {
        if (index < 0) {
            return 0;
        }
        return array[index] + sumaRecursiva(array, index - 1);
    }
    
    private static void calcularTiempoPromedio() {
        manejarTareasVacias(() -> {
            int total = sumaRecursiva(tiemposTareas, contadorTareas - 1);
            double promedio = (double) total / contadorTareas;
            System.out.println("Tiempo promedio por tarea: " + String.format("%.2f", promedio) + " horas");
        });
    }
    
    private static void encontrarTiempoMaximo() {
        manejarTareasVacias(() -> {
            int maximo = maximoRecursivo(tiemposTareas, contadorTareas - 1, Integer.MIN_VALUE);
            System.out.println("Tiempo máximo: " + maximo + " horas");
        });
    }
    
    private static int maximoRecursivo(int[] array, int index, int max) {
        if (index < 0) {
            return max;
        }
        
        return maximoRecursivo(array, index - 1, array[index] > max ? array[index] : max);
    }
    
    private static void encontrarTiempoMinimo() {
        manejarTareasVacias(() -> {
            int minimo = minimoRecursivo(tiemposTareas, contadorTareas - 1, Integer.MAX_VALUE);
            System.out.println("Tiempo mínimo: " + minimo + " horas");
        });
    }
    
    private static int minimoRecursivo(int[] array, int index, int min) {
        if (index < 0) {
            return min;
        }
        
        return minimoRecursivo(array, index - 1, array[index] < min ? array[index] : min);
    }
    
    private static void distribuirTareas() {
        manejarTareasVacias(() -> {
            System.out.print("Ingrese el número de proyectos grandes: ");
            int numProyectos = obtenerEnteroPositivoRecursivo("Ingrese el número de proyectos grandes: ", scanner.nextLine().trim(), 0);
            
            int[] proyectos = new int[numProyectos];
            distribuirRecursivo(tiemposTareas, contadorTareas - 1, proyectos, 0);
            
            System.out.println("Distribución de horas por proyecto:");
            mostrarDistribucionRecursiva(proyectos, 0);
        });
    }
    
    private static void distribuirRecursivo(int[] tareas, int tareaIndex, int[] proyectos, int proyectoIndex) {
        if (tareaIndex < 0) {
            return;
        }
        
        int proyectoMinimo = encontrarProyectoMinimo(proyectos, proyectos.length - 1, 0);
        proyectos[proyectoMinimo] += tareas[tareaIndex];
        
        distribuirRecursivo(tareas, tareaIndex - 1, proyectos, proyectoIndex);
    }
    
    private static int encontrarProyectoMinimo(int[] proyectos, int index, int minIndex) {
        if (index < 0) {
            return minIndex;
        }
        
        int nuevoMinIndex = proyectos[index] < proyectos[minIndex] ? index : minIndex;
        return encontrarProyectoMinimo(proyectos, index - 1, nuevoMinIndex);
    }
    
    private static void mostrarDistribucionRecursiva(int[] proyectos, int index) {
        if (index >= proyectos.length) {
            return;
        }
        
        System.out.println("Proyecto " + (index + 1) + ": " + proyectos[index] + " horas");
        mostrarDistribucionRecursiva(proyectos, index + 1);
    }
    
    private static void manejarTareasVacias(Runnable action) {
        if (contadorTareas == 0) {
            System.out.println("No hay tareas registradas.");
            return;
        }
        action.run();
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
}