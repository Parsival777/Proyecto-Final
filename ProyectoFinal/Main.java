import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        mostrarMenuPrincipal();
    }
    
    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE GESTIÓN DE TAREAS ===");
        System.out.println("1. Gestión de Cola de Prioridades");
        System.out.println("2. Gestión de Árboles Binarios");
        System.out.println("3. Estadísticas de Tareas (Recursividad)");
        System.out.println("4. Gestión de Tablas Hash");
        System.out.println("5. Ordenamiento y Búsqueda");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
        
        procesarOpcionPrincipal();
    }
    
    private static void procesarOpcionPrincipal() {
        String input = scanner.nextLine().trim();
        
        switch (input) {
            case "1":
                ColaPrioridad.menuColaPrioridad();
                mostrarMenuPrincipal();
                break;
            case "2":
                ArbolBinario.menuArbolBinario();
                mostrarMenuPrincipal();
                break;
            case "3":
                EstadisticasTareas.menuEstadisticas();
                mostrarMenuPrincipal();
                break;
            case "4":
                TablaHash.menuTablaHash();
                mostrarMenuPrincipal();
                break;
            case "5":
                OrdenamientoBusqueda.menuOrdenamientoBusqueda();
                mostrarMenuPrincipal();
                break;
            case "0":
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción no válida. Por favor, ingrese una opción entre 0 y 5.");
                mostrarMenuPrincipal();
        }
    }
}