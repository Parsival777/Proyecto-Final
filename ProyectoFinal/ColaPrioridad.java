import java.util.PriorityQueue;
import java.util.Scanner;

public class ColaPrioridad {
    private static PriorityQueue<Tarea> colaTareas = new PriorityQueue<>((t1, t2) -> {
        
        return Integer.compare(t1.prioridad, t2.prioridad);
    });
    
    private static Scanner scanner = new Scanner(System.in);
    
    public static void menuColaPrioridad() {
        System.out.println("\n=== GESTIÓN DE COLA DE PRIORIDADES ===");
        System.out.println("1. Agregar tarea");
        System.out.println("2. Eliminar tarea (mayor prioridad)");
        System.out.println("3. Mostrar todas las tareas");
        System.out.println("4. Buscar tarea por nombre");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
        
        procesarOpcionCola();
    }
    
    private static void procesarOpcionCola() {
        String input = scanner.nextLine().trim();
        
        switch (input) {
            case "1":
                agregarTarea();
                menuColaPrioridad();
                break;
            case "2":
                eliminarTarea();
                menuColaPrioridad();
                break;
            case "3":
                mostrarTareasRecursivo(colaTareas.size());
                menuColaPrioridad();
                break;
            case "4":
                buscarTarea();
                menuColaPrioridad();
                break;
            case "0":
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
                menuColaPrioridad();
        }
    }
    
    private static void agregarTarea() {
        System.out.print("Nombre de la tarea: ");
        String nombre = obtenerEntradaNoVacia("Nombre de la tarea: ");
        
        System.out.print("Prioridad (1-5, donde 1 es la más alta): ");
        int prioridad = obtenerEnteroEnRango(1, 5, "Prioridad (1-5, donde 1 es la más alta): ");
        
        Tarea tarea = new Tarea(nombre, prioridad);
        colaTareas.add(tarea);
        System.out.println("Tarea agregada: " + tarea);
    }
    
    private static void eliminarTarea() {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }
        
        Tarea tarea = colaTareas.poll();
        System.out.println("Tarea eliminada: " + tarea);
    }
    
    private static void mostrarTareasRecursivo(int n) {
        if (n <= 0) {
            if (colaTareas.isEmpty()) {
                System.out.println("No hay tareas para mostrar.");
            }
            return;
        }
        
        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        mostrarTareaRecursivo(tareas, tareas.length - n);
        mostrarTareasRecursivo(n - 1);
    }
    
    private static void mostrarTareaRecursivo(Tarea[] tareas, int index) {
        if (index < tareas.length) {
            System.out.println((index + 1) + ". " + tareas[index]);
        }
    }
    
    private static void buscarTarea() {
        System.out.print("Ingrese el nombre de la tarea a buscar: ");
        String nombre = obtenerEntradaNoVacia("Ingrese el nombre de la tarea a buscar: ");
        
        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        boolean encontrada = buscarTareaRecursivo(tareas, nombre, 0);
        
        if (!encontrada) {
            System.out.println("Tarea no encontrada.");
        }
    }
    
    private static boolean buscarTareaRecursivo(Tarea[] tareas, String nombre, int index) {
        if (index >= tareas.length) {
            return false;
        }
        
        if (tareas[index].nombre.equalsIgnoreCase(nombre)) {
            System.out.println("Tarea encontrada: " + tareas[index]);
            return true;
        }
        
        return buscarTareaRecursivo(tareas, nombre, index + 1);
    }
    
    
    private static String obtenerEntradaNoVacia(String mensaje) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.print("Este campo no puede estar vacío. " + mensaje);
            return obtenerEntradaNoVacia(mensaje);
        }
        return input;
    }
    
    private static int obtenerEnteroEnRango(int min, int max, String mensaje) {
        String input = scanner.nextLine().trim();
        try {
            int valor = Integer.parseInt(input);
            if (valor >= min && valor <= max) {
                return valor;
            } else {
                System.out.print("Por favor, ingrese un número entre " + min + " y " + max + ". " + mensaje);
                return obtenerEnteroEnRango(min, max, mensaje);
            }
        } catch (NumberFormatException e) {
            System.out.print("Entrada no válida. Por favor, ingrese un número. " + mensaje);
            return obtenerEnteroEnRango(min, max, mensaje);
        }
    }
    
    static class Tarea {
        String nombre;
        int prioridad; 
        
        public Tarea(String nombre, int prioridad) {
            this.nombre = nombre;
            this.prioridad = prioridad;
        }
        
        @Override
        public String toString() {
            String nivelPrioridad;
            switch (prioridad) {
                case 1: nivelPrioridad = "Muy Alta"; break;
                case 2: nivelPrioridad = "Alta"; break;
                case 3: nivelPrioridad = "Media"; break;
                case 4: nivelPrioridad = "Baja"; break;
                case 5: nivelPrioridad = "Muy Baja"; break;
                default: nivelPrioridad = "Desconocida";
            }
            return "Tarea{" + "nombre='" + nombre + '\'' + ", prioridad=" + nivelPrioridad + " (" + prioridad + ")" + '}';
        }
    }
}