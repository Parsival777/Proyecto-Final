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
        
        procesarOpcionEstadisticas();
    }
    
    private static void procesarOpcionEstadisticas() {
        String input = scanner.nextLine().trim();
        
        switch (input) {
            case "1":
                agregarTiempoTarea();
                menuEstadisticas();
                break;
            case "2":
                calcularTiempoTotal();
                menuEstadisticas();
                break;
            case "3":
                calcularTiempoPromedio();
                menuEstadisticas();
                break;
            case "4":
                encontrarTiempoMaximo();
                menuEstadisticas();
                break;
            case "5":
                encontrarTiempoMinimo();
                menuEstadisticas();
                break;
            case "6":
                distribuirTareas();
                menuEstadisticas();
                break;
            case "0":
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 6.");
                menuEstadisticas();
        }
    }
    
    private static void agregarTiempoTarea() {
        if (contadorTareas >= tiemposTareas.length) {
            System.out.println("No se pueden agregar más tareas.");
            return;
        }
        
        System.out.print("Tiempo estimado para completar la tarea (horas): ");
        int tiempo = obtenerEnteroPositivo("Tiempo estimado para completar la tarea (horas): ");
        
        tiemposTareas[contadorTareas++] = tiempo;
        System.out.println("Tiempo agregado: " + tiempo + " horas");
    }
    
    private static void calcularTiempoTotal() {
        if (contadorTareas == 0) {
            System.out.println("No hay tareas registradas.");
            return;
        }
        
        int total = sumaRecursiva(tiemposTareas, contadorTareas - 1);
        System.out.println("Tiempo total estimado: " + total + " horas");
    }
    
    private static int sumaRecursiva(int[] array, int index) {
        if (index < 0) {
            return 0;
        }
        return array[index] + sumaRecursiva(array, index - 1);
    }
    
    private static void calcularTiempoPromedio() {
        if (contadorTareas == 0) {
            System.out.println("No hay tareas registradas.");
            return;
        }
        
        int total = sumaRecursiva(tiemposTareas, contadorTareas - 1);
        double promedio = (double) total / contadorTareas;
        System.out.println("Tiempo promedio por tarea: " + String.format("%.2f", promedio) + " horas");
    }
    
    private static void encontrarTiempoMaximo() {
        if (contadorTareas == 0) {
            System.out.println("No hay tareas registradas.");
            return;
        }
        
        int maximo = maximoRecursivo(tiemposTareas, contadorTareas - 1, Integer.MIN_VALUE);
        System.out.println("Tiempo máximo: " + maximo + " horas");
    }
    
    private static int maximoRecursivo(int[] array, int index, int max) {
        if (index < 0) {
            return max;
        }
        
        if (array[index] > max) {
            max = array[index];
        }
        
        return maximoRecursivo(array, index - 1, max);
    }
    
    private static void encontrarTiempoMinimo() {
        if (contadorTareas == 0) {
            System.out.println("No hay tareas registradas.");
            return;
        }
        
        int minimo = minimoRecursivo(tiemposTareas, contadorTareas - 1, Integer.MAX_VALUE);
        System.out.println("Tiempo mínimo: " + minimo + " horas");
    }
    
    private static int minimoRecursivo(int[] array, int index, int min) {
        if (index < 0) {
            return min;
        }
        
        if (array[index] < min) {
            min = array[index];
        }
        
        return minimoRecursivo(array, index - 1, min);
    }
    
    private static void distribuirTareas() {
        if (contadorTareas == 0) {
            System.out.println("No hay tareas registradas.");
            return;
        }
        
        System.out.print("Ingrese el número de proyectos grandes: ");
        int numProyectos = obtenerEnteroPositivo("Ingrese el número de proyectos grandes: ");
        
        int[] proyectos = new int[numProyectos];
        distribuirRecursivo(tiemposTareas, contadorTareas - 1, proyectos, 0);
        
        System.out.println("Distribución de horas por proyecto:");
        mostrarDistribucionRecursiva(proyectos, 0);
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
        
        if (proyectos[index] < proyectos[minIndex]) {
            minIndex = index;
        }
        
        return encontrarProyectoMinimo(proyectos, index - 1, minIndex);
    }
    
    private static void mostrarDistribucionRecursiva(int[] proyectos, int index) {
        if (index >= proyectos.length) {
            return;
        }
        
        System.out.println("Proyecto " + (index + 1) + ": " + proyectos[index] + " horas");
        mostrarDistribucionRecursiva(proyectos, index + 1);
    }
    
    
    private static int obtenerEnteroPositivo(String mensaje) {
        String input = scanner.nextLine().trim();
        try {
            int valor = Integer.parseInt(input);
            if (valor > 0) {
                return valor;
            } else {
                System.out.print("Por favor, ingrese un número positivo. " + mensaje);
                return obtenerEnteroPositivo(mensaje);
            }
        } catch (NumberFormatException e) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerEnteroPositivo(mensaje);
        }
    }
}