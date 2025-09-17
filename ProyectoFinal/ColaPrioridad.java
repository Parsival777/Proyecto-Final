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
        
        procesarOpcionCola(scanner.nextLine().trim());
    }
    
    private static void procesarOpcionCola(String input) {
        procesarOpcionRecursiva(input);
    }
    
    private static void procesarOpcionRecursiva(String input) {
        procesarCasoBase(input, "0", () -> System.out.println("Volviendo al menú principal..."));
        
        procesarOpcion(input, "1", ColaPrioridad::agregarTarea);
        procesarOpcion(input, "2", ColaPrioridad::eliminarTarea);
        procesarOpcion(input, "3", ColaPrioridad::mostrarTareasRecursivo);
        procesarOpcion(input, "4", ColaPrioridad::buscarTarea);
        
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
            menuColaPrioridad();
        }
    }
    
    private static void procesarCasoDefault(String input) {
        boolean esValida = verificarOpcionValida(input, new String[]{"0", "1", "2", "3", "4"}, 0);
        
        if (!esValida) {
            System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 4.");
            menuColaPrioridad();
        }
    }
    
    private static boolean verificarOpcionValida(String input, String[] opciones, int index) {
        if (index >= opciones.length) return false;
        if (input.equals(opciones[index])) return true;
        return verificarOpcionValida(input, opciones, index + 1);
    }
    
    private static void agregarTarea() {
        System.out.print("Nombre de la tarea: ");
        String nombre = obtenerEntradaNoVaciaRecursivo("Nombre de la tarea: ", scanner.nextLine().trim());
        
        System.out.print("Prioridad (1-5, donde 1 es la más alta): ");
        int prioridad = obtenerEnteroEnRangoRecursivo(1, 5, "Prioridad (1-5, donde 1 es la más alta): ", scanner.nextLine().trim(), 0);
        
        Tarea tarea = new Tarea(nombre, prioridad);
        colaTareas.add(tarea);
        System.out.println("Tarea agregada: " + tarea);
    }
    
    private static void eliminarTarea() {
        manejarColaVacia(() -> {
            Tarea tarea = colaTareas.poll();
            System.out.println("Tarea eliminada: " + tarea);
        });
    }
    
    private static void mostrarTareasRecursivo() {
        manejarColaVacia(() -> {
            Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
            System.out.println("Tareas en la cola:");
            mostrarTareasRecursivo(tareas, 0);
        });
    }
    
    private static void mostrarTareasRecursivo(Tarea[] tareas, int index) {
        if (index >= tareas.length) {
            return;
        }
        
        System.out.println((index + 1) + ". " + tareas[index]);
        mostrarTareasRecursivo(tareas, index + 1);
    }
    
    private static void buscarTarea() {
        System.out.print("Ingrese el nombre de la tarea a buscar: ");
        String nombre = obtenerEntradaNoVaciaRecursivo("Ingrese el nombre de la tarea a buscar: ", scanner.nextLine().trim());
        
        Tarea[] tareas = colaTareas.toArray(new Tarea[0]);
        boolean encontrada = buscarTareaRecursivo(tareas, nombre, 0);
        
        manejarResultadoBusqueda(encontrada);
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
    
    private static void manejarResultadoBusqueda(boolean encontrada) {
        if (!encontrada) {
            System.out.println("Tarea no encontrada.");
        }
    }
    
    private static void manejarColaVacia(Runnable action) {
        if (colaTareas.isEmpty()) {
            System.out.println("La cola de tareas está vacía.");
            return;
        }
        action.run();
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
        
        return verificarRangoRecursivo(min, max, mensaje, valor);
    }
    
    private static int verificarRangoRecursivo(int min, int max, String mensaje, int valor) {
        if (valor >= min && valor <= max) {
            return valor;
        }
        
        System.out.print("Por favor, ingrese un número entre " + min + " y " + max + ". " + mensaje);
        return obtenerEnteroEnRangoRecursivo(min, max, mensaje, scanner.nextLine().trim(), 0);
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
        String nombre;
        int prioridad;
        
        public Tarea(String nombre, int prioridad) {
            this.nombre = nombre;
            this.prioridad = prioridad;
        }
        
        @Override
        public String toString() {
            return "Tarea{" + "nombre='" + nombre + '\'' + ", prioridad=" + obtenerNivelPrioridad(prioridad) + " (" + prioridad + ")" + '}';
        }
        
        private String obtenerNivelPrioridad(int prioridad) {
            return obtenerNivelPrioridadRecursivo(prioridad, 1);
        }
        
        private String obtenerNivelPrioridadRecursivo(int prioridad, int nivel) {
            if (nivel > 5) return "Desconocida";
            
            if (prioridad == nivel) {
                switch (nivel) {
                    case 1: return "Muy Alta";
                    case 2: return "Alta";
                    case 3: return "Media";
                    case 4: return "Baja";
                    case 5: return "Muy Baja";
                }
            }
            
            return obtenerNivelPrioridadRecursivo(prioridad, nivel + 1);
        }
    }
}
